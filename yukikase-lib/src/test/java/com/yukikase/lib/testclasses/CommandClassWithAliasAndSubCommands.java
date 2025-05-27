package com.yukikase.lib.testclasses;

import com.yukikase.lib.PermissionHandler;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.annotations.command.Alias;
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

    @Alias(alias = "alias")
    @Permission(value = "method", handler = PermissionHandler.IGNORE_PREFIX)
    public boolean onAlias(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias(subcommand = "sub")
    public boolean onSubCommand(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias(alias = "alias", subcommand = "sub2")
    public boolean onSubCommandWithAlias(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias(alias = "manual")
    @Permission(handler = PermissionHandler.MANUAL)
    public boolean onManual(CommandSender sender, Command command, String[] args) {
        return true;
    }

    @Alias(alias = "multiple")
    @Alias(alias = "multiple", subcommand = "1")
    @Alias(alias = "multiple", subcommand = "2")
    @Alias(subcommand = "2")
    @Alias(alias = "multiple2")
    public boolean onMultiple(CommandSender sender, Command command, String[] args) {
        return true;
    }
}
