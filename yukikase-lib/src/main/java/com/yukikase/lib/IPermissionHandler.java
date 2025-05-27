package com.yukikase.lib;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public interface IPermissionHandler {

    boolean playerHasPermission(Player player, String permission);

    boolean playerHasPermission(Player player, String... args);

    boolean playerHasPermission(Player player, PermissionHandler permissionHandler, String... args);

    boolean playerHasPermission(Player player, Class<?> clazz);

    boolean playerHasPermission(Player player, Method method);

    boolean playerHasPermission(Player player, Class<?> clazz, String... args);
}
