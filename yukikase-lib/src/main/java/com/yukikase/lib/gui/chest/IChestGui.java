package com.yukikase.lib.gui.chest;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface IChestGui {
    void onOpen(Player player);

    void onClose(Player player);

    void onClick(Player player, InventoryClickEvent event, int slot);

    void open(Player player);

    void close(Player player);

    Component title();
}
