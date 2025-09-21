package com.yukikase.lib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class ChestGui implements IChestGui {
    protected final Inventory inventory;
    private final Component title;
    private final Map<Integer, GuiItem> guiItems = new HashMap<>();
    private final ItemStack dummyItem;
    protected boolean canBeModified = false;

    protected ChestGui(int size, Component title, Player player) {
        inventory = Bukkit.createInventory(player, size, title);
        this.title = title;

        dummyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        var meta = dummyItem.getItemMeta();
        meta.customName(Component.text(""));

        dummyItem.setItemMeta(meta);
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, dummyItem);
        }
    }

    public final void addItem(int slot, ItemStack item, OnGuiClickExecutor executor) {
        guiItems.put(slot, new GuiItem(item, executor));
        inventory.setItem(slot, item);
    }

    public final void removeItem(int slot) {
        guiItems.remove(slot);
        inventory.setItem(slot, dummyItem);
    }

    @Override
    public void onOpen(Player player) {
    }

    @Override
    public void onClose(Player player) {
    }

    @Override
    public final void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void close(Player player) {
        if (player.getOpenInventory().getTopInventory().equals(inventory)) {
            player.closeInventory();
        }
    }

    @Override
    public boolean canBeModified() {
        return canBeModified;
    }

    @Override
    public Component title() {
        return title;
    }

    @Override
    public final void onClick(Player player, ClickType clickType, int slot) {
        var item = guiItems.get(slot);
        if (item != null) {
            var executor = item.executor();
            if (executor != null) {
                item.executor().onGuiClick(player, clickType);
            }
        }
    }
}
