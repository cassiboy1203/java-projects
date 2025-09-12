package com.yukikase.rpg.prison.pickaxe.entity;

import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;
import org.bukkit.Material;

@Entity
public class PickaxeMaterial {
    @Id
    private String name;
    private int maxLevel;
    private int baseDamage;
    private int damagePerLevel;
    private Material material;
    private boolean isDefault;

    public String name() {
        return name;
    }

    public int maxLevel() {
        return maxLevel;
    }

    public int baseDamage() {
        return baseDamage;
    }

    public int damagePerLevel() {
        return damagePerLevel;
    }

    public Material material() {
        return material;
    }
}
