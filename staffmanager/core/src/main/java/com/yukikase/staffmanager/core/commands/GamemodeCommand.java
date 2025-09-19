package com.yukikase.staffmanager.core.commands;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Argument;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.lib.permission.Permission;
import com.yukikase.staffmanager.core.PermissionRegister;
import com.yukikase.staffmanager.core.staffmode.IStaffMode;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class GamemodeCommand implements ICommand {
    public static final String NAME = "gamemode";

    static final String GAMEMODE_CHANGED_MESSAGE = "%sYour gamemode has been changed too %s";
    static final String PLAYER_NO_PERMISSION_GAMEMODE_OTHER = "Player does not have permission to change another players gamemode outside of staff mode";
    static final String PLAYER_NO_PERMISSON_GAMEMODE_OTHER_STAFF = "Player does not have permission to change another players gamemode";

    private final IStaffMode staffMode;
    private final IPermissionHandler permissionHandler;

    @Inject
    public GamemodeCommand(IStaffMode staffMode, IPermissionHandler permissionHandler) {
        this.staffMode = staffMode;
        this.permissionHandler = permissionHandler;
    }

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public Permission permission() {
        return PermissionRegister.GAMEMODE_BASE;
    }

    @Alias(alias = "gm")
    public int onCommand(CommandSender sender, @Argument(value = "gamemode", suggestions = {"0", "1", "2", "3", "s", "c", "a", "sp", "survival", "creative", "adventure", "spectator"}) String gamemodeArg, @Argument(value = "target", optional = true) Player other) {
        var gamemode = switch (gamemodeArg.toLowerCase()) {
            case "0", "survival", "s" -> GameMode.SURVIVAL;
            case "1", "creative", "c" -> GameMode.CREATIVE;
            case "2", "adventure", "a" -> GameMode.ADVENTURE;
            case "3", "spectator", "sp" -> GameMode.SPECTATOR;
            default -> throw new IllegalArgumentException("Invalid gamemode");
        };

        return changeGamemode(sender, gamemode, other);
    }

    @Alias(alias = "gmc")
    public int onCreative(CommandSender sender, @Argument(value = "target", optional = true) Player other) {
        return changeGamemode(sender, GameMode.CREATIVE, other);
    }

    @Alias(alias = "gms")
    public int onSurvival(CommandSender sender, @Argument(value = "target", optional = true) Player other) {
        return changeGamemode(sender, GameMode.SURVIVAL, other);
    }

    @Alias(alias = "gma")
    public int onAdventure(CommandSender sender, @Argument(value = "target", optional = true) Player other) {
        return changeGamemode(sender, GameMode.ADVENTURE, other);
    }

    @Alias(alias = "gmsp")
    public int onSpectator(CommandSender sender, @Argument(value = "target", optional = true) Player other) {
        return changeGamemode(sender, GameMode.SPECTATOR, other);
    }

    private int changeGamemode(CommandSender sender, GameMode gamemode, Player other) {
        if (sender instanceof Player player) {
            if (!staffMode.isInStaffMode(player) && !this.permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE))
                throw new UnauthorizedException("Player does not have permission to change gamemode");

            if (other == null) {
                changeGamemode(player, gamemode);
                return Command.SINGLE_SUCCESS;
            }
        }
        return changeGamemode(sender, other, gamemode);
    }

    private void changeGamemode(Player player, GameMode gamemode) {
        player.setGameMode(gamemode);

        player.sendMessage(String.format(GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, gamemode.name().toLowerCase()));
    }

    private int changeGamemode(CommandSender sender, Player other, GameMode gamemode) {
        if (sender instanceof Player player) {
            if (!staffMode.isInStaffMode(player)) {
                if (!this.permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER))
                    throw new UnauthorizedException(PLAYER_NO_PERMISSION_GAMEMODE_OTHER);

            } else if (!this.permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)) {
                throw new UnauthorizedException(PLAYER_NO_PERMISSON_GAMEMODE_OTHER_STAFF);
            }
        }
        other.setGameMode(gamemode);
        return Command.SINGLE_SUCCESS;
    }
}
