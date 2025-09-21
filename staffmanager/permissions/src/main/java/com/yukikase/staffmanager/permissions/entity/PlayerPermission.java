package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yukikase.lib.entity.PlayerEntity;

import java.util.Collection;

@DatabaseTable
public class PlayerPermission {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private PlayerEntity player;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Permission permission;

    @DatabaseField(defaultValue = "true")
    private boolean value;

    public Permission permission() {
        return permission;
    }

    public boolean value() {
        return value;
    }

    public Collection<PermissionContext> contexts() {
        return null;
    }
}
