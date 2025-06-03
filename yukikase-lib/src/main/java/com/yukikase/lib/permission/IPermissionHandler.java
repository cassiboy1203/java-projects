package com.yukikase.lib.permission;

import org.bukkit.entity.Player;

public interface IPermissionHandler {

    boolean playerHasPermission(Player player, Permission permission);

    boolean playerHasPermission(Player player, String permission);

    Permission getPermission(String permission);
}
