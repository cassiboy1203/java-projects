package com.yukikase.lib;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.annotations.PermissionPrefix;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultPermissionHandler implements IPermissionHandler {

    private final JavaPlugin plugin;

    @Inject
    public DefaultPermissionHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean playerHasPermission(Player player, Class<? extends ICommand> commandClass) {
        return playerHasPermission(player, commandClass, new String[]{});
    }

    @Override
    public boolean playerHasPermission(Player player, String... permission) {
        return playerHasPermission(player, null, permission);
    }

    @Override
    public boolean playerHasPermission(Player player, Class<? extends ICommand> commandClass, String... permission) {
        var prefix = getPermissionOffCommand(commandClass);
        var permissions = new ArrayList<String>();
        permissions.add(prefix);
        permissions.addAll(Arrays.asList(permission));

        var fullPermission = String.join(".", permissions);

        return player.hasPermission(fullPermission);
    }

    @Override
    public String getPermissionOffCommand(Class<? extends ICommand> commandClass) {
        var permissions = new ArrayList<String>();
        permissions.add(getPermissionPrefixOffCommand(commandClass));
        if (commandClass.isAnnotationPresent(Permission.class)) {
            var commandPermission = commandClass.getAnnotation(Permission.class).value();
            permissions.addAll(Arrays.asList(commandPermission));
        }

        return String.join(".", permissions);
    }

    @Override
    public String getPermissionPrefixOffCommand(Class<? extends ICommand> commandClass) {
        List<String> permissions = new ArrayList<>();

        if (this.plugin.getClass().isAnnotationPresent(PermissionPrefix.class)) {
            var pluginPrefix = this.plugin.getClass().getAnnotation(PermissionPrefix.class).value();
            permissions.addAll(Arrays.asList(pluginPrefix));
        }

        if (commandClass.isAnnotationPresent(PermissionPrefix.class)) {
            var commandPrefix = this.plugin.getClass().getAnnotation(PermissionPrefix.class).value();
            permissions.addAll(Arrays.asList(commandPrefix));
        }

        return String.join(".", permissions);
    }

    @Override
    public String getPermissionOffMethod(Class<? extends ICommand> commandClass, Method method) {
        if (method.isAnnotationPresent(Permission.class)) {
            var permissions = new ArrayList<String>();
            permissions.add(getPermissionPrefixOffCommand(commandClass));
            var permission = method.getAnnotation(Permission.class).value();
            permissions.addAll(Arrays.asList(permission));
            return String.join(".", permissions);
        }

        return getPermissionOffCommand(commandClass);
    }
}
