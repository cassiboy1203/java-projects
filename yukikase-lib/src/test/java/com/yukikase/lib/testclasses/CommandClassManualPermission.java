package com.yukikase.lib.testclasses;

import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.CommandSender;

public class CommandClassManualPermission implements ICommand {
    @Override
    public String name() {
        return "manual";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        return false;
    }
}
