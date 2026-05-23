package com.yukikase.rpg.prison.pickaxe.command;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.gui.chest.ChestGuiBuilder;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeMaterial;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.yukikase.lib.gui.TextHelper.text;

public class PickaxeCommand implements ICommand {

    private final EntitySet<PickaxeMaterial> pickaxeMaterials;

    @Inject
    public PickaxeCommand(EntitySet<PickaxeMaterial> pickaxeMaterials) {
        this.pickaxeMaterials = pickaxeMaterials;
    }

    @Override
    public String name() {
        return "pickaxe";
    }

    @Alias(subcommand = "menu")
    public int onMenu(Player player) {
        var pickaxeMenu = new ChestGuiBuilder(1, text("Pickaxes", NamedTextColor.AQUA), player);

        for (var material : pickaxeMaterials.getAll()) {
            pickaxeMenu.addItem(new ItemStack(material.getMaterial()), (p, e) -> onPickaxe(p, e, material));
        }

        pickaxeMenu.open(player);
        return Command.SINGLE_SUCCESS;
    }

    private void onPickaxe(Player player, InventoryClickEvent event, PickaxeMaterial material) {
        var pickaxeMenu = new ChestGuiBuilder(3, text(material.getName(), NamedTextColor.AQUA), player);

        pickaxeMenu.addItem(4, new ItemStack(material.getMaterial()), ChestGuiBuilder.DUMMY_EXECUTOR);

        var baseDamageItem = new ItemStack(Material.NETHERITE_SWORD);
        var meta = baseDamageItem.getItemMeta();
        meta.customName(text("Base damage", NamedTextColor.DARK_GRAY));
        baseDamageItem.setItemMeta(meta);

        var damagePerLevelItem = new ItemStack(Material.DIAMOND_SWORD);
        meta = damagePerLevelItem.getItemMeta();
        meta.customName(text("Damage", NamedTextColor.DARK_GRAY));
        damagePerLevelItem.setItemMeta(meta);

        var maxLevelItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        meta = maxLevelItem.getItemMeta();
        meta.customName(text("Maximum level", NamedTextColor.GREEN));
        maxLevelItem.setItemMeta(meta);

        pickaxeMenu.addItem(10, baseDamageItem, (p, e) -> onChangeBaseDamage(p, e, material));
        pickaxeMenu.addItem(12, damagePerLevelItem, (p, e) -> onChangeDamagePerLevel(p, e, material));
        pickaxeMenu.addItem(14, maxLevelItem, (p, e) -> onChangeMaxLevel(p, e, material));

        pickaxeMenu.open(player);
    }

    private void onChangeBaseDamage(Player player, InventoryClickEvent event, PickaxeMaterial material) {
    }

    private void onChangeDamagePerLevel(Player player, InventoryClickEvent event, PickaxeMaterial material) {

    }

    private void onChangeMaxLevel(Player player, InventoryClickEvent event, PickaxeMaterial material) {
    }
}
