package com.yukikase.lib;

import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public interface IPermissionHandler {
    boolean playerHasPermission(Player player, Class<? extends ICommand> commandClass);

    boolean playerHasPermission(Player player, String... permission);

    boolean playerHasPermission(Player player, Class<? extends ICommand> commandClass, String... permission);

    String getPermissionOffCommand(Class<? extends ICommand> commandClass);

    String getPermissionPrefixOffCommand(Class<? extends ICommand> commandClass);

    String getPermissionOffMethod(Class<? extends ICommand> commandClass, Method method);
}
