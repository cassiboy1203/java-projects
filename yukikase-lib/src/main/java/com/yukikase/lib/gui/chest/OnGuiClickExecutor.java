package com.yukikase.lib.gui.chest;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface OnGuiClickExecutor {
    void onGuiClick(Player player, InventoryClickEvent event);
}
