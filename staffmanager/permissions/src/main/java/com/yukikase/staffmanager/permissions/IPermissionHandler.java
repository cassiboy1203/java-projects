package com.yukikase.staffmanager.permissions;

import org.bukkit.entity.Player;

public interface IPermissionHandler {
    void loadGroups();

    void removeFromCache(Player player);

    void addToCache(Player player);

    void reloadCache();

    void reloadCache(Player player);

    boolean hasPermission(Player player, String permission);
}
