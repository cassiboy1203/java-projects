package com.yukikase.rpg.prison.pickaxe.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "pickaxe")
public class PickaxeEntity {
    @DatabaseField(id = true)
    UUID owner;
    @DatabaseField
    int level;
    @DatabaseField
    long experience;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    PickaxeMaterial material;

    public PickaxeEntity() {
    }

    public PickaxeEntity(UUID owner, int level, long experience, PickaxeMaterial material) {
        this.owner = owner;
        this.level = level;
        this.experience = experience;
        this.material = material;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public PickaxeMaterial getMaterial() {
        return material;
    }

    public void setMaterial(PickaxeMaterial material) {
        this.material = material;
    }
}
