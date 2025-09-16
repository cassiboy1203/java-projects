package com.yukikase.rpg.prison.pickaxe.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Material;

@DatabaseTable(tableName = "pickaxe_material")
public class PickaxeMaterial {
    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private int maxLevel;
    @DatabaseField
    private int baseDamage;
    @DatabaseField
    private int damagePerLevel;
    @DatabaseField
    private Material material;
    @DatabaseField(defaultValue = "false")
    private boolean isDefault;
    @DatabaseField(foreign = true, canBeNull = true)
    private PickaxeMaterial nextMaterial;

    protected PickaxeMaterial() {
    }

    public PickaxeMaterial(String name, int maxLevel, int baseDamage, int damagePerLevel, Material material, boolean isDefault, PickaxeMaterial nextMaterial) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.baseDamage = baseDamage;
        this.damagePerLevel = damagePerLevel;
        this.material = material;
        this.isDefault = isDefault;
        this.nextMaterial = nextMaterial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getDamagePerLevel() {
        return damagePerLevel;
    }

    public void setDamagePerLevel(int damagePerLevel) {
        this.damagePerLevel = damagePerLevel;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public PickaxeMaterial getNextMaterial() {
        return nextMaterial;
    }

    public void setNextMaterial(PickaxeMaterial nextMaterial) {
        this.nextMaterial = nextMaterial;
    }
}
