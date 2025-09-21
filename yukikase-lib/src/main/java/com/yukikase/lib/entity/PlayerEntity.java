package com.yukikase.lib.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "player")
public class PlayerEntity {
    @DatabaseField(id = true)
    private UUID uuid;

    @DatabaseField
    private String name;

    protected PlayerEntity() {
    }

    public PlayerEntity(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
