package com.yukikase.lib.command;

import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.IPermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.List;

@Configuration
public class CommandRegister {

    @Register
    public void registerCommands(List<ICommand> commands, YukikasePlugin plugin, IPermissionHandler permissionHandler) {
        for (var command : commands) {

            BukkitCommand executor = new CommandRunner(command, permissionHandler, plugin);

            try {
                var f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);

                var map = (CommandMap) f.get(Bukkit.getServer());
                map.register(executor.getName(), executor);
                f.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
