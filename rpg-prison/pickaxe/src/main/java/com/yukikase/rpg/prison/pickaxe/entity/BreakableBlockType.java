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
    private int respawnTime;

    @DatabaseField
    private Material respawnMaterial;

    @DatabaseField
    private int experience;

    @DatabaseField
    private int energy;

    protected BreakableBlockType() {
    }

    public BreakableBlockType(Material material, int hardness, int resistance, int respawnTime) {
        this.material = material.toString();
        this.hardness = hardness;
        this.resistance = resistance;
        this.respawnTime = respawnTime;
    }

    public Material material() {
        return Material.getMaterial(material);
    }

    public int hardness() {
        return hardness;
    }

    public void hardness(int hardness) {
        this.hardness = hardness;
    }

    public int resistance() {
        return resistance;
    }

    public void resistance(int resistance) {
        this.resistance = resistance;
    }

    public int respawnTime() {
        return respawnTime;
    }

    public void respawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public Material respawnMaterial() {
        if (respawnMaterial == null) {
            return Material.STONE;
        }
        return respawnMaterial;
    }

    public void respawnMaterial(Material respawnMaterial) {
        this.respawnMaterial = respawnMaterial;
    }

    public int experience() {
        return experience;
    }

    public void experience(int experience) {
        this.experience = experience;
    }

    public int energy() {
        return energy;
    }

    public void energy(int energy) {
        this.energy = energy;
    }
}
