package com.yukikase.lib.command;

import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Aliases;
import com.yukikase.lib.exceptions.InvalidCommandException;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.lib.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class CommandRunner extends BukkitCommand {
    private final ICommand command;
    private final IPermissionHandler permissionHandler;
    private final YukikasePlugin plugin;
    private final CommandNode rootCommand;

    CommandRunner(ICommand command, IPermissionHandler permissionHandler, YukikasePlugin plugin) {
        super(command.name());

        this.setName(command.name());
        this.setDescription(command.description());
        this.setUsage(command.usage());

        if (command.permission() != null) {
            this.setPermission(command.permission().toString());
        }

        this.command = command;
        this.permissionHandler = permissionHandler;
        this.rootCommand = new CommandNode("", "", null, null, true);
        this.plugin = plugin;

        var aliases = registerCommands();

        this.setAliases(aliases);
    }

    private List<String> registerCommands() {
        Queue<CommandNode> commands = new LinkedList<>();
        List<String> names = new ArrayList<>();

        var commandPermission = command.permission();

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
                    else if (!names.contains(alias.alias())) {
                        names.add(alias.alias());
                    }

                    Permission permission = commandPermission;
                    if (!alias.permission().isEmpty())
                        permission = this.permissionHandler.getPermission(alias.permission());

                    var node = new CommandNode(name, alias.subcommand(), permission, method);
                    commands.add(node);
                }
            } else {
                if (method.getName().equals("onCommand")) {
                    var node = new CommandNode(command.name(), "", commandPermission, method);
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
            var root = aliasCommands.getValue().stream().filter(n -> n.getSubcommand().isEmpty()).findFirst().orElse(new CommandNode(aliasCommands.getKey(), "", null, null));

            rootCommand.addChild(root);

            for (var child : aliasCommands.getValue()) {
                root.addChild(child);
            }
        }

        return names;
    }

    @Override
    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, @NonNull String[] args) {
        this.plugin.useClassLoader();
        var commandNode = rootCommand.getNodeToExecute(commandName, args);
        try {
            return runCommand(commandSender, Arrays.copyOfRange(args, commandNode.getDepth(), args.length), commandNode.getPermission(), commandNode.getMethod());
        } catch (IllegalAccessException | InvocationTargetException e) {
            // This exception should never be thrown.
            throw new InvalidCommandException();
        }
    }

    private boolean runCommand(CommandSender commandSender, String[] args, Permission permission, Method method) throws InvocationTargetException, IllegalAccessException {
        if (commandSender instanceof Player player) {
            if (permission != null && !permissionHandler.playerHasPermission(player, permission))
                throw new UnauthorizedException(UnauthorizedException.EC_PLAYER_UNAUTHORIZED_COMMAND);
        }

        return (boolean) method.invoke(this.command, commandSender, args);
    }
}
