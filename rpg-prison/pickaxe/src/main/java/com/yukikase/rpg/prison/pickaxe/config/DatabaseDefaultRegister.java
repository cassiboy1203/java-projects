package com.yukikase.rpg.prison.pickaxe.config;

import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockType;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeMaterial;
import org.bukkit.Material;

@Configuration
public class DatabaseDefaultRegister {

    @Register
    public void registerDefaultPickaxeMaterials(EntitySet<PickaxeMaterial> materials) {
        if (materials.count() <= 0) {
            var material = new PickaxeMaterial("NETHERITE", 150, 12500, 500, Material.NETHERITE_PICKAXE, false, null);
            materials.add(material);
            material = new PickaxeMaterial("DIAMOND", 125, 5000, 200, Material.DIAMOND_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("IRON", 100, 2500, 100, Material.IRON_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("GOLDEN", 75, 500, 50, Material.GOLDEN_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("COPPER", 50, 250, 30, Material.COPPER_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("STONE", 25, 50, 10, Material.STONE_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("WOODEN", 10, 10, 3, Material.WOODEN_PICKAXE, true, material);
            materials.add(material);
        }
    }

    @Register
    public void registerDefaultBreakableBlockTypes(EntitySet<BreakableBlockType> breakableBlockTypes) {
        if (breakableBlockTypes.count() <= 0) {
            var breakableBlockType = new BreakableBlockType(Material.COAL_ORE, 50, 10, 10);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_COAL_ORE, 125, 25, 20);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COAL_BLOCK, 200, 40, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COPPER_ORE, 500, 100, 20);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_COPPER_ORE, 750, 150, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_COPPER_BLOCK, 800, 200, 45);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COPPER_BLOCK, 1250, 250, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.LAPIS_ORE, 2500, 500, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_LAPIS_ORE, 5000, 1000, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.LAPIS_BLOCK, 7500, 1500, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.IRON_ORE, 10000, 2000, 45);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_IRON_ORE, 15000, 3000, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_IRON_BLOCK, 20000, 4000, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.IRON_BLOCK, 25000, 5000, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.REDSTONE_ORE, 30000, 6000, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_REDSTONE_ORE, 40000, 7250, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.REDSTONE_BLOCK, 50000, 10000, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GOLD_ORE, 75000, 12500, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_GOLD_ORE, 80000, 15000, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_GOLD_BLOCK, 90000, 17500, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GOLD_BLOCK, 100000, 20000, 150);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHER_GOLD_ORE, 100000, 22500, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHER_QUARTZ_ORE, 150000, 26000, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GILDED_BLACKSTONE, 200000, 30000, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DIAMOND_ORE, 300000, 40000, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_DIAMOND_ORE, 400000, 45000, 210);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DIAMOND_BLOCK, 500000, 55000, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.AMETHYST_CLUSTER, 550000, 60000, 210);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.AMETHYST_BLOCK, 650000, 70000, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.EMERALD_ORE, 1000000, 100000, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_EMERALD_ORE, 1250000, 125000, 270);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.EMERALD_BLOCK, 1500000, 150000, 300);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.ANCIENT_DEBRIS, 1750000, 175000, 300);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHERITE_BLOCK, 2000000, 200000, 330);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.OBSIDIAN, 10000000, 500000, 420);
            breakableBlockTypes.add(breakableBlockType);
        }
    }
}
