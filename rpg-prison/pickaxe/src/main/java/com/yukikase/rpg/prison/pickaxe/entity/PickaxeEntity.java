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
    @DatabaseField
    int energy;
    @DatabaseField
    int energyLevel;
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

    public UUID owner() {
        return owner;
    }

    public void owner(UUID owner) {
        this.owner = owner;
    }

    public int level() {
        return level;
    }

    public void level(int level) {
        this.level = level;
    }

    public long experience() {
        return experience;
    }

    public void experience(long experience) {
        this.experience = experience;
    }

    public PickaxeMaterial material() {
        return material;
    }

    public void material(PickaxeMaterial material) {
        this.material = material;
    }

    public int energy() {
        return energy;
    }

    public void energy(int energy) {
        this.energy = energy;
    }

    public void energyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int energyLevel() {
        return energyLevel;
    }
}
