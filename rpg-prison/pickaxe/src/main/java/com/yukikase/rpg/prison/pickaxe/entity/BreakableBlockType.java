package com.yukikase.rpg.prison.pickaxe.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Material;

@DatabaseTable(tableName = "breakable_block_types")
public class BreakableBlockType {
    @DatabaseField(id = true)
    private String material;

    @DatabaseField
    private int hardness;

    @DatabaseField
    private int resistance;

    @DatabaseField
    private Material drop;

    @DatabaseField
    private Material smeltedDrop;

    @DatabaseField
    private int minDrop;

    @DatabaseField
    private int maxDrop;

    @DatabaseField
    private int smeltedDropMultiplier;

    @DatabaseField
    private int respawnTime;

    protected BreakableBlockType() {
    }

    public BreakableBlockType(Material material, int hardness, int resistance, Material drop, Material smeltedDrop, int minDrop, int maxDrop, int smeltedDropMultiplier, int respawnTime) {
        this.material = material.toString();
        this.hardness = hardness;
        this.resistance = resistance;
        this.drop = drop;
        this.smeltedDrop = smeltedDrop;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.smeltedDropMultiplier = smeltedDropMultiplier;
        this.respawnTime = respawnTime;
    }

    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    public void setMaterial(Material material) {
        this.material = material.name();
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public Material getDrop() {
        return drop;
    }

    public void setDrop(Material drop) {
        this.drop = drop;
    }

    public Material getSmeltedDrop() {
        return smeltedDrop;
    }

    public void setSmeltedDrop(Material smeltedDrop) {
        this.smeltedDrop = smeltedDrop;
    }

    public int getMinDrop() {
        return minDrop;
    }

    public void setMinDrop(int minDrop) {
        this.minDrop = minDrop;
    }

    public int getMaxDrop() {
        return maxDrop;
    }

    public void setMaxDrop(int maxDrop) {
        this.maxDrop = maxDrop;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }
}
