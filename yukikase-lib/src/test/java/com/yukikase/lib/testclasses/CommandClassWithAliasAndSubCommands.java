package com.yukikase.lib.testclasses;

import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.SubCommand;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandClassWithAliasAndSubCommands implements ICommand {
    @Override
    public String name() {
        return "command";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias("alias")
    public boolean onAlias(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @SubCommand("sub")
    public boolean onSubCommand(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias("alias")
    @SubCommand("sub2")
    public boolean onSubCommandWithAlias(CommandSender sender, Command command, String[] args) {
        return true;
    }
}
