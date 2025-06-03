package com.yukikase.lib.interfaces;

import com.yukikase.lib.permission.Permission;
import org.bukkit.command.CommandSender;

public interface ICommand {
    String name();

    default String description() {
        return "";
    }

    default String usage() {
        return "";
    }

    default Permission permission() {
        return null;
    }

    boolean onCommand(CommandSender sender, String[] args);
}
