package com.yukikase.lib.testclasses;

import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandClassSingleMethod implements ICommand {
    @Override
    public String name() {
        return "command";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String[] args) {
        return true;
    }
}
