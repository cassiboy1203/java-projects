package com.yukikase.lib.command;

import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.PermissionHandler;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Aliases;
import com.yukikase.lib.exceptions.InvalidCommandException;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class CommandRunner implements CommandExecutor {
    private final ICommand command;
    private final IPermissionHandler permissionHandler;
    private final YukikasePlugin plugin;
    private CommandNode rootCommand;

    CommandRunner(ICommand command, IPermissionHandler permissionHandler, YukikasePlugin plugin) {
        this.command = command;
        this.permissionHandler = permissionHandler;
        this.rootCommand = new CommandNode("", "", null, true);
        this.plugin = plugin;

        registerCommands();
    }

    private void registerCommands() {
        Queue<CommandNode> commands = new LinkedList<>();

        for (var method : command.getClass().getMethods()) {
            Alias[] aliases = null;
            if (method.isAnnotationPresent(Aliases.class)) {
                aliases = method.getAnnotationsByType(Alias.class);
            } else if (method.isAnnotationPresent(Alias.class)) {
                aliases = method.getAnnotationsByType(Alias.class);
            }

            if (aliases != null) {
                for (var alias : aliases) {
                    if (alias.alias().equals(command.name()) && alias.subcommand().isEmpty() && !method.getName().equals("onCommand")) {
                        continue;
                    }

                    var name = alias.alias();

                    if (alias.alias().isEmpty())
                        name = command.name();

                    var node = new CommandNode(name, alias.subcommand(), method);
                    commands.add(node);
                }
            } else {
                if (method.getName().equals("onCommand")) {
                    var node = new CommandNode(command.name(), "", method);
                    commands.add(node);
                }
            }
        }

        Map<String, List<CommandNode>> subCommands = new HashMap<>();

        while (!commands.isEmpty()) {
            var command = commands.poll();

            var alias = command.getCommand();

            if (subCommands.containsKey(alias)) {
                subCommands.get(alias).add(command);
            } else {
                subCommands.put(alias, new ArrayList<>());
                subCommands.get(alias).add(command);
            }
        }

        for (var aliasCommands : subCommands.entrySet()) {
            var root = aliasCommands.getValue().stream().filter(n -> n.getSubcommand().isEmpty()).findFirst().orElse(new CommandNode(aliasCommands.getKey(), "", null));

            rootCommand.addChild(root);

            for (var child : aliasCommands.getValue()) {
                root.addChild(child);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
        this.plugin.useClassLoader();
        var commandNode = rootCommand.getNodeToExecute(commandName, args);
        try {
            return runCommand(commandSender, command, Arrays.copyOfRange(args, commandNode.getDepth(), args.length), commandNode.getMethod());
        } catch (IllegalAccessException | InvocationTargetException e) {
            // This exception should never be thrown.
            throw new InvalidCommandException();
        }
    }

    private boolean runCommand(CommandSender commandSender, Command command, String[] args, Method method) throws InvocationTargetException, IllegalAccessException {
        if (commandSender instanceof Player player) {
            boolean checkForPermission = true;

            if (this.command.getClass().isAnnotationPresent(Permission.class)) {
                var permission = this.command.getClass().getAnnotation(Permission.class);
                if (permission.handler().equals(PermissionHandler.MANUAL))
                    checkForPermission = false;
            }

            if (method.isAnnotationPresent(Permission.class)) {
                var permission = method.getAnnotation(Permission.class);

                if (permission.handler().equals(PermissionHandler.MANUAL))
                    checkForPermission = false;
            }

            if (checkForPermission && !this.permissionHandler.playerHasPermission(player, method))
                throw new UnauthorizedException(UnauthorizedException.PLAYER_UNAUTHORIZED_SUB_COMMAND);
        }

        return (boolean) method.invoke(this.command, commandSender, command, args);
    }
}
