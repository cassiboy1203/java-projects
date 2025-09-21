package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yukikase.lib.entity.PlayerEntity;

@DatabaseTable
public class PlayerGroups {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, uniqueCombo = true, canBeNull = false)
    private PlayerEntity player;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, uniqueCombo = true, canBeNull = false)
    private Group group;

    public Group group() {
        return group;
    }
}
