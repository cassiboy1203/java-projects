package com.yukikase.lib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface IChestGui {
    void onOpen(Player player);

    void onClose(Player player);

    void onClick(Player player, ClickType clickType, int slot);

    void open(Player player);

    void close(Player player);

    Component title();

    boolean canBeModified();
}
