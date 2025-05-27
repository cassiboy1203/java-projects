package com.yukikase.lib.testclasses;

import com.yukikase.lib.PermissionHandler;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Permission(handler = PermissionHandler.MANUAL)
public class CommandClassManualPermission implements ICommand {
    @Override
    public String name() {
        return "manual";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String[] args) {
        return false;
    }
}
