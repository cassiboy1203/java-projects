package com.yukikase.staffmanager.permissions.command;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Argument;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.staffmanager.permissions.IPermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class ReloadCommand implements ICommand {

    private final IPermissionHandler permissionHandler;

    @Inject
    public ReloadCommand(IPermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    @Override
    public String name() {
        return "perm";
    }

    @Alias(subcommand = "reload")
    public int onReload(CommandSender sender) {
        permissionHandler.reloadCache();

        return Command.SINGLE_SUCCESS;
    }

    @Alias(subcommand = {"reload", "groups"})
    public int onReloadGroups(CommandSender sender) {
        permissionHandler.loadGroups();
        return Command.SINGLE_SUCCESS;
    }

    @Alias(subcommand = {"reload", "player"})
    public int onReloadPlayer(CommandSender sender, @Argument("player") Player player) {
        permissionHandler.reloadCache(player);
        return Command.SINGLE_SUCCESS;
    }
}
