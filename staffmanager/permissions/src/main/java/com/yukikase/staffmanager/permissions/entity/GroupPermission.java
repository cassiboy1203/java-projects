package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable
public class GroupPermission {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group group;

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

    public Group group() {
        return group;
    }
}
