package com.yukikase.rpg.prison.pickaxe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IPickaxes {
    void removeFromCache(Player owner);

    void clearCache();

    void addExperience(Player owner, int amount);

    int getDamage(Player player);

    boolean isPickaxe(Player player, ItemStack item);

    ItemStack item(Player player);
}
