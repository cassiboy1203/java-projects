package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;

@DatabaseTable(tableName = "permission_group")
public class Group implements Comparable<Group> {
    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private int index;

    @DatabaseField
    private String prefix;

    @DatabaseField
    private String suffix;

    public String name() {
        return name;
    }

    public int index() {
        return index;
    }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    @Override
    public int compareTo(@NotNull Group group) {
        return group.index() - this.index;
    }
}
