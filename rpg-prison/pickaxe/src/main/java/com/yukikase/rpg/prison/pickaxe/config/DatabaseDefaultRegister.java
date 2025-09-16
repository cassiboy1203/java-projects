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
            var material = new PickaxeMaterial("NETHERITE", 125, 75000, 1000, Material.NETHERITE_PICKAXE, false, null);
            materials.add(material);
            material = new PickaxeMaterial("DIAMOND", 100, 25000, 500, Material.DIAMOND_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("IRON", 75, 7000, 250, Material.IRON_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("GOLDEN", 50, 2000, 100, Material.GOLDEN_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("COPPER", 35, 250, 50, Material.LIGHTNING_ROD, false, material);
            materials.add(material);
            material = new PickaxeMaterial("STONE", 20, 50, 10, Material.STONE_PICKAXE, false, material);
            materials.add(material);
            material = new PickaxeMaterial("WOODEN", 10, 10, 3, Material.WOODEN_PICKAXE, true, material);
            materials.add(material);
        }
    }

    @Register
    public void registerDefaultBreakableBlockTypes(EntitySet<BreakableBlockType> breakableBlockTypes) {
        if (breakableBlockTypes.count() <= 0) {
            var breakableBlockType = new BreakableBlockType(Material.COAL_ORE, 50, 10, Material.COAL, Material.COAL, 1, 1, 1, 10);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_COAL_ORE, 125, 25, Material.COAL, Material.COAL, 1, 3, 1, 20);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COAL_BLOCK, 200, 40, Material.COAL_BLOCK, Material.COAL_BLOCK, 1, 1, 1, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COPPER_ORE, 500, 100, Material.COPPER_ORE, Material.COPPER_INGOT, 1, 1, 1, 20);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_COPPER_ORE, 750, 150, Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT, 1, 1, 3, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_COPPER_BLOCK, 800, 200, Material.RAW_COPPER, Material.COPPER_INGOT, 4, 9, 1, 45);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.COPPER_BLOCK, 1250, 250, Material.COPPER_BLOCK, Material.COPPER_BLOCK, 1, 1, 1, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.LAPIS_ORE, 2500, 500, Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, 1, 1, 1, 30);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_LAPIS_ORE, 5000, 1000, Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, 1, 3, 1, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.LAPIS_BLOCK, 7500, 1500, Material.LAPIS_BLOCK, Material.LAPIS_BLOCK, 1, 1, 1, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.IRON_ORE, 10000, 2000, Material.IRON_ORE, Material.IRON_INGOT, 1, 1, 1, 45);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_IRON_ORE, 15000, 3000, Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT, 1, 1, 3, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_IRON_BLOCK, 20000, 4000, Material.RAW_IRON, Material.IRON_INGOT, 4, 9, 1, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.IRON_BLOCK, 25000, 5000, Material.IRON_BLOCK, Material.IRON_BLOCK, 1, 1, 1, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.REDSTONE_ORE, 30000, 6000, Material.REDSTONE, Material.REDSTONE, 3, 5, 1, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_REDSTONE_ORE, 40000, 7250, Material.REDSTONE, Material.REDSTONE, 5, 9, 1, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.REDSTONE_BLOCK, 50000, 10000, Material.REDSTONE_BLOCK, Material.REDSTONE_BLOCK, 1, 1, 1, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GOLD_ORE, 75000, 12500, Material.GOLD_ORE, Material.GOLD_INGOT, 1, 1, 1, 60);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_GOLD_ORE, 80000, 15000, Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT, 1, 1, 3, 90);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.RAW_GOLD_BLOCK, 90000, 17500, Material.RAW_GOLD, Material.GOLD_INGOT, 4, 9, 1, 120);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GOLD_BLOCK, 100000, 20000, Material.GOLD_BLOCK, Material.GOLD_BLOCK, 1, 1, 1, 150);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHER_GOLD_ORE, 100000, 22500, Material.GOLD_NUGGET, Material.GOLD_NUGGET, 81, 100, 1, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHER_QUARTZ_ORE, 150000, 26000, Material.QUARTZ, Material.QUARTZ, 1, 3, 1, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.GILDED_BLACKSTONE, 200000, 30000, Material.GILDED_BLACKSTONE, Material.GILDED_BLACKSTONE, 1, 1, 2, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DIAMOND_ORE, 300000, 40000, Material.DIAMOND, Material.DIAMOND, 1, 1, 1, 180);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_DIAMOND_ORE, 400000, 45000, Material.DIAMOND, Material.DIAMOND, 1, 3, 1, 210);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DIAMOND_BLOCK, 500000, 55000, Material.DIAMOND_BLOCK, Material.DIAMOND_BLOCK, 1, 1, 1, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.AMETHYST_CLUSTER, 550000, 60000, Material.AMETHYST_SHARD, Material.AMETHYST_SHARD, 1, 1, 3, 210);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.AMETHYST_BLOCK, 650000, 70000, Material.AMETHYST_BLOCK, Material.AMETHYST_BLOCK, 1, 1, 1, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.BUDDING_AMETHYST, 750000, 80000, Material.BUDDING_AMETHYST, Material.BUDDING_AMETHYST, 1, 1, 1, 270);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.EMERALD_ORE, 1000000, 100000, Material.EMERALD, Material.EMERALD, 1, 1, 1, 240);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.DEEPSLATE_EMERALD_ORE, 1250000, 125000, Material.EMERALD, Material.EMERALD, 1, 3, 1, 270);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.EMERALD_BLOCK, 1500000, 150000, Material.EMERALD_BLOCK, Material.EMERALD_BLOCK, 1, 1, 1, 300);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.ANCIENT_DEBRIS, 1750000, 175000, Material.ANCIENT_DEBRIS, Material.NETHERITE_INGOT, 1, 1, 1, 300);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.NETHERITE_BLOCK, 2000000, 200000, Material.NETHERITE_BLOCK, Material.NETHERITE_BLOCK, 1, 1, 1, 330);
            breakableBlockTypes.add(breakableBlockType);
            breakableBlockType = new BreakableBlockType(Material.OBSIDIAN, 10000000, 500000, Material.OBSIDIAN, Material.OBSIDIAN, 1, 1, 1, 420);
            breakableBlockTypes.add(breakableBlockType);
        }
    }
}
