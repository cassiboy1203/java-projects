package com.yukikase.lib;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.exceptions.NoPermissionFoundException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//TODO: remove this and only use it as a backup
@Component
public class DefaultPermissionHandler implements IPermissionHandler {

    private final JavaPlugin plugin;

    @Inject
    public DefaultPermissionHandler(YukikasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean playerHasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean playerHasPermission(Player player, String... args) {
        return playerHasPermission(player, PermissionHandler.DEFAULT, args);
    }

    @Override
    public boolean playerHasPermission(Player player, PermissionHandler permissionHandler, String... args) {
        var permissions = new ArrayList<String>();
        permissions.add(getPrefixOfPlugin());

        if (PermissionHandler.IGNORE_PLUGIN_PREFIX.equals(permissionHandler) || PermissionHandler.IGNORE_PREFIX.equals(permissionHandler))
            permissions.clear();

        permissions.addAll(List.of(args));

        return playerHasPermission(player, String.join(".", permissions));
    }

    @Override
    public boolean playerHasPermission(Player player, Class<?> clazz) {
        return playerHasPermission(player, clazz, new String[0]);
    }

    @Override
    public boolean playerHasPermission(Player player, Class<?> clazz, String... args) {
        var permission = getPermissionOfClass(clazz);
        if (permission.isEmpty())
            throw new NoPermissionFoundException(String.format(NoPermissionFoundException.CLASS_DOES_NOT_HAVE_PERMISSION, clazz.getName()));

        var permissions = new ArrayList<String>();
        permissions.add(permission);
        permissions.addAll(List.of(args));

        return player.hasPermission(String.join(".", permissions));
    }

    @Override
    public boolean playerHasPermission(Player player, Method method) {
        var permission = getPermissionOfMethod(method);
        if (permission.isEmpty())
            throw new NoPermissionFoundException(String.format(NoPermissionFoundException.METHOD_DOES_NOT_HAVE_PERMISSION, method.getName()));

        return player.hasPermission(permission);
    }

    private String getPermissionOfMethod(Method method) {
        var permissions = new ArrayList<String>();
        var prefix = getPermissionOfClass(method.getDeclaringClass());

        if (!prefix.isEmpty()) {
            permissions.add(prefix);
        }

        if (method.isAnnotationPresent(Permission.class)) {
            var annotation = method.getAnnotation(Permission.class);
            if (annotation.handler().equals(PermissionHandler.IGNORE_PREFIX))
                permissions.clear();

            permissions.addAll(List.of(annotation.value()));
        }

        return String.join(".", permissions);
    }

    private String getPermissionOfClass(Class<?> clazz) {
        var permissions = new ArrayList<String>();
        var prefix = getPrefixOfPlugin();
        if (prefix != null && !prefix.isEmpty()) {
            permissions.add(prefix);
        }

        if (clazz.isAnnotationPresent(Permission.class)) {
            var permission = clazz.getAnnotation(Permission.class);
            if (permission.handler().equals(PermissionHandler.IGNORE_PLUGIN_PREFIX) || permission.handler().equals(PermissionHandler.IGNORE_PREFIX))
                permissions.clear();

            permissions.addAll(List.of(permission.value()));
        }

        return String.join(".", permissions);
    }

    //TODO: fix classloader issues to get this working.

    private String getPrefixOfPlugin() {
        if (plugin.getClass().isAnnotationPresent(Permission.class)) {
            var prefix = plugin.getClass().getAnnotation(Permission.class).value();

            return String.join(".", prefix);
        }

        return null;
    }
}
