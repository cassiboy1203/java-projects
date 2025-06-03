package com.yukikase.lib.testclasses;

import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.CommandSender;

public class CommandClassWithAliasAndSubCommands implements ICommand {
    @Override
    public String name() {
        return "command";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        return true;
    }

    @Alias(alias = "alias")
    public boolean onAlias(CommandSender sender, String[] args) {
        return true;
    }

    @Alias(subcommand = "sub", permission = "TEST_BASE")
    public boolean onSubCommand(CommandSender sender, String[] args) {
        return true;
    }

    @Alias(alias = "alias", subcommand = "sub2")
    public boolean onSubCommandWithAlias(CommandSender sender, String[] args) {
        return true;
    }

    @Alias(alias = "multiple")
    @Alias(alias = "multiple", subcommand = "1")
    @Alias(alias = "multiple", subcommand = "2")
    @Alias(subcommand = "2")
    @Alias(alias = "multiple2")
    public boolean onMultiple(CommandSender sender, String[] args) {
        return true;
    }
}
