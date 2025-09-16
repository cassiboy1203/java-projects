package com.yukikase.lib.permission;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.exceptions.NoPermissionFoundException;
import com.yukikase.lib.exceptions.NoPermissionRegisterFound;
import org.bukkit.entity.Player;

@Component
public class DefaultPermissionHandler implements IPermissionHandler {

    private final YukikasePlugin plugin;

    @Inject
    public DefaultPermissionHandler(YukikasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean playerHasPermission(Player player, com.yukikase.lib.permission.Permission permission) {
        return playerHasPermission(player, permission.toString());
    }

    @Override
    public boolean playerHasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public Permission getPermission(String permission) {
        try {
            return (Permission) plugin.permissionRegister().getField(permission).get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            throw new NoPermissionFoundException(String.format("No permission found for name: %s", permission));
        } catch (NullPointerException e) {
            throw new NoPermissionRegisterFound();
        }
    }
}
