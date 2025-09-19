package com.yukikase.lib.command;

import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Aliases;
import com.yukikase.lib.interfaces.ICommand;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Configuration
public class PaperCommandRegister {

    @Register
    public void registerCommands(List<ICommand> commands, YukikasePlugin plugin) {
        for (var command : commands) {
            registerCommand(command, plugin);
        }
    }

    private void registerCommand(ICommand command, YukikasePlugin plugin) {
        var baseCommandName = command.name();

        var root = new CommandNode(null);

        for (var method : command.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Aliases.class) && !method.isAnnotationPresent(Alias.class)) continue;
            var aliases = method.getAnnotationsByType(Alias.class);
            for (var alias : aliases) {
                var commandName = alias.alias().isEmpty() ? baseCommandName : alias.alias();
                var subcommands = alias.subcommand();
                var parent = root.getOrCreateChild(commandName);
                for (var subcommand : subcommands) {
                    parent = parent.getOrCreateChild(subcommand);
                }
                var permission = command.permission() == null ? "" : command.permission().toString();
                permission = alias.permission().isEmpty() ? permission : alias.permission();
                parent.method(method);
                parent.permission(permission);
            }
        }

        for (var node : root.children()) {
            try {
                var paperCommand = node.toPaperCommandNode(command).build();
                plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
                    commands.registrar().register(paperCommand);
                });
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
