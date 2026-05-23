package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.lib.gui.scoreboard.ScoreBoardGui;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface IPickaxes {
    String SCOREBOARD_KEY = "Pickaxe";

    PickaxeEntity getPickaxe(UUID owner);

    void removeFromCache(Player owner);

    void clearCache();

    void addExperience(Player owner, int amount);

    void addEnergy(Player owner, int amount);

    int getDamage(Player player);

    boolean isPickaxe(Player player, ItemStack item);

    ItemStack item(Player player);

    boolean hasPickaxe(Player player);

    void addLevel(Player player, int level);

    void upgrade(Player player);

    ItemStack getPickaxeInInventory(Player player);

    void updateLore(Player player);

    boolean isEnergyFull(Player player);

    int extract(Player player, int amount);

    int extractAll(Player player);

    ScoreBoardGui createScoreboard(Player player);
}
