package com.yukikase.lib.command;

import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.SubCommand;
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
    private CommandNode rootCommand;

    CommandRunner(ICommand command, IPermissionHandler permissionHandler) {
        this.command = command;
        this.permissionHandler = permissionHandler;
        this.rootCommand = new CommandNode("", "", null, true);

        registerCommands();
    }

    private void registerCommands() {
        Queue<CommandNode> commands = new LinkedList<>();

        for (var method : command.getClass().getMethods()) {
            var alias = command.name();
            var subCommand = "";
            if (method.isAnnotationPresent(Alias.class)) {
                alias = method.getAnnotation(Alias.class).value();
            }
            if (method.isAnnotationPresent(SubCommand.class)) {
                subCommand = method.getAnnotation(SubCommand.class).value();
            }

            if (alias.equals(command.name()) && subCommand.isEmpty() && !method.getName().equals("onCommand")) {
                continue;
            }

            var node = new CommandNode(alias, subCommand, method);
            commands.add(node);
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
            if (!player.hasPermission(this.permissionHandler.getPermissionOffMethod(this.command.getClass(), method)))
                throw new UnauthorizedException(UnauthorizedException.PLAYER_UNAUTHORIZED_SUB_COMMAND);
        }

        return (boolean) method.invoke(this.command, commandSender, command, args);
    }
}
