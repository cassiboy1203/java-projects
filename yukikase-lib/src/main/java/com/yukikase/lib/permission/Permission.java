package com.yukikase.lib.permission;

import org.bukkit.permissions.PermissionDefault;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The minecraft permission.
 *
 * @param permission        The name of the permission
 * @param description       The description of the permission
 * @param permissionDefault The {@link PermissionDefault} of the permission the default is {@link PermissionDefault#OP}
 * @param parent            The parent permission will be added before the permission. Example: parent.permission
 * @param inherit           If the player inherits the permission from the parrent. This means that if the player has the parent permission he will also have the child permission. Default is {@link Boolean#FALSE}
 */
public record Permission(@NonNull String permission, @NonNull String description,
                         @NonNull PermissionDefault permissionDefault,
                         Permission parent, boolean inherit) {
    public Permission(String permission, String description, Permission parent, boolean inherit) {
        this(permission, description, PermissionDefault.OP, parent, inherit);
    }

    public Permission(String permission, String description, PermissionDefault permissionDefault, Permission parent) {
        this(permission, description, permissionDefault, parent, false);
    }

    public Permission(String permission, String description, Permission parent) {
        this(permission, description, parent, false);
    }

    public Permission(String permission, String description, PermissionDefault permissionDefault) {
        this(permission, description, permissionDefault, null, false);
    }

    public Permission(String permission, String description) {
        this(permission, description, null, false);
    }

    @Override
    public @NonNull String toString() {
        if (parent == null) {
            return permission;
        }

        return String.join(".", parent.toString(), permission);
    }
}
