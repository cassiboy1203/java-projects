package com.yukikase.lib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@FunctionalInterface
public interface OnGuiClickExecutor {
    void onGuiClick(Player player, ClickType clickType);
}
