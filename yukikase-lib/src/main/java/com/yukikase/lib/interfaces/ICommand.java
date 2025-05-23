package com.yukikase.lib.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ICommand {
    String name();

    boolean onCommand(CommandSender sender, Command command, String[] args);
}
