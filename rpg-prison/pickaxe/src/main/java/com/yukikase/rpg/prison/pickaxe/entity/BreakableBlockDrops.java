package com.yukikase.rpg.prison.pickaxe.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Material;

@DatabaseTable
public class BreakableBlockDrops {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private BreakableBlockType breakableBlockType;

    @DatabaseField(canBeNull = false)
    private Material drop;

    @DatabaseField
    private Material smeltedDrop;

    @DatabaseField
    private int minAmount;

    @DatabaseField
    private int maxAmount;

    @DatabaseField
    private int dropChance;

    protected BreakableBlockDrops() {
    }

    public BreakableBlockDrops(BreakableBlockType breakableBlockType, Material drop, int minAmount, int maxAmount, int dropChance) {
        this.breakableBlockType = breakableBlockType;
        this.drop = drop;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.dropChance = dropChance;
    }

    public BreakableBlockDrops(BreakableBlockType breakableBlockType, Material drop, Material smeltedDrop, int minAmount, int maxAmount, int dropChance) {
        this(breakableBlockType, drop, minAmount, maxAmount, dropChance);
        this.smeltedDrop = smeltedDrop;
    }

    public Material drop() {
        return drop;
    }

    public Material smeltedDrop() {
        return smeltedDrop;
    }

    public int minAmount() {
        return minAmount;
    }

    public BreakableBlockType breakableBlockType() {
        return breakableBlockType;
    }

    public int maxAmount() {
        return maxAmount;
    }

    public int dropChance() {
        return dropChance;
    }
}
