package com.yukikase.lib.command;

import com.yukikase.diframework.anotations.Configuration;
import com.yukikase.diframework.anotations.Register;
import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.CommandExecutor;

import java.util.List;

@Configuration
public class CommandRegister {

    @Register
    public void registerCommands(List<ICommand> commands, YukikasePlugin plugin, IPermissionHandler permissionHandler) {
        for (var command : commands) {

            CommandExecutor executor = new CommandRunner(command, permissionHandler, plugin);

            plugin.getCommand(command.name()).setExecutor(executor);
        }
    }
}
