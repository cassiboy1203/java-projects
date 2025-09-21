package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.permissions.PermissionDefault;

@DatabaseTable
public class Permission {
    @DatabaseField(id = true)
    private String permission;

    @DatabaseField
    private String description;

    @DatabaseField
    private PermissionDefault permissionDefault;

    public String permission() {
        return permission;
    }

    public String description() {
        return description;
    }

    public PermissionDefault permissionDefault() {
        return permissionDefault;
    }
}
