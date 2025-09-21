package com.yukikase.staffmanager.permissions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Permissible extends PermissibleBase {

    private final IPermissionHandler permissionHandler;
    private final Player player;

    public Permissible(@Nullable ServerOperator opable, IPermissionHandler permissionHandler, Player player) {
        super(opable);
        this.permissionHandler = permissionHandler;
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        return permissionHandler.hasPermission(player, inName);
    }
}
