package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class GroupParent {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group parent;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group child;

    public Group parent() {
        return parent;
    }
}
