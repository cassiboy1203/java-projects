package com.yukikase.staffmanager.core.commands;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.exceptions.InvalidCommandUsageException;
import com.yukikase.lib.exceptions.PlayerNotFoundException;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.lib.permission.Permission;
import com.yukikase.staffmanager.core.PermissionRegister;
import com.yukikase.staffmanager.core.staffmode.IStaffMode;
import org.bukkit.Bukkit;
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

    private final IPermissionHandler permissionHandler;
    private final IStaffMode staffMode;

    @Inject
    public GamemodeCommand(IPermissionHandler permissionHandler, IStaffMode staffMode) {
        this.permissionHandler = permissionHandler;
        this.staffMode = staffMode;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Permission permission() {
        return PermissionRegister.STAFF_MODE_GAMEMODE;
    }

    @Override
    @Alias(alias = "gm")
    @Alias(alias = "gamemode")
    public boolean onCommand(CommandSender sender, String[] args) {
        throw new InvalidCommandUsageException();
    }

    @Alias(alias = "gmc")
    @Alias(alias = "gm", subcommand = "creative")
    @Alias(subcommand = "creative")
    @Alias(alias = "gm", subcommand = "1")
    @Alias(alias = "gm", subcommand = "c")
    @Alias(subcommand = "1")
    @Alias(subcommand = "c")
    public boolean onCreative(CommandSender sender, String[] args) {
        return changeGamemode(sender, GameMode.CREATIVE, args);
    }

    @Alias(alias = "gms")
    @Alias(alias = "gm", subcommand = "survival")
    @Alias(subcommand = "survival")
    @Alias(alias = "gm", subcommand = "0")
    @Alias(alias = "gm", subcommand = "s")
    @Alias(subcommand = "0")
    @Alias(subcommand = "s")
    public boolean onSurvival(CommandSender sender, String[] args) {
        return changeGamemode(sender, GameMode.SURVIVAL, args);
    }

    @Alias(alias = "gmsp")
    @Alias(alias = "gm", subcommand = "spectator")
    @Alias(subcommand = "spectator")
    @Alias(alias = "gm", subcommand = "4")
    @Alias(alias = "gm", subcommand = "sp")
    @Alias(subcommand = "4")
    @Alias(subcommand = "sp")
    public boolean onSpectator(CommandSender sender, String[] args) {
        return changeGamemode(sender, GameMode.SPECTATOR, args);
    }

    @Alias(alias = "gma")
    @Alias(alias = "gm", subcommand = "adventure")
    @Alias(subcommand = "adventure")
    @Alias(alias = "gm", subcommand = "3")
    @Alias(alias = "gm", subcommand = "a")
    @Alias(subcommand = "3")
    @Alias(subcommand = "a")
    public boolean onAdventure(CommandSender sender, String[] args) {
        return changeGamemode(sender, GameMode.ADVENTURE, args);
    }

    private boolean changeGamemode(CommandSender sender, GameMode gamemode, String[] args) {
        if (sender instanceof Player player) {
            if (!staffMode.isInStaffMode(player) && !this.permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE))
                throw new UnauthorizedException("Player does not have permission to change gamemode");

            if (args.length == 0) {
                changeGamemode(player, gamemode);
                return true;
            }
        }

        if (args.length == 1) {
            var other = Bukkit.getPlayer(args[0]);
            if (other == null) {
                throw new PlayerNotFoundException();
            }

            return changeGamemode(sender, other, gamemode);
        } else {
            throw new InvalidCommandUsageException();
        }
    }

    private void changeGamemode(Player player, GameMode gamemode) {
        player.setGameMode(gamemode);

        player.sendMessage(String.format(GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, gamemode.name().toLowerCase()));
    }

    private boolean changeGamemode(CommandSender sender, Player other, GameMode gamemode) {
        if (sender instanceof Player player) {
            if (!staffMode.isInStaffMode(player)) {
                if (!this.permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER))
                    throw new UnauthorizedException(PLAYER_NO_PERMISSION_GAMEMODE_OTHER);

            } else if (!this.permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)) {
                throw new UnauthorizedException(PLAYER_NO_PERMISSON_GAMEMODE_OTHER_STAFF);
            }
        }
        other.setGameMode(gamemode);
        return true;
    }
}
