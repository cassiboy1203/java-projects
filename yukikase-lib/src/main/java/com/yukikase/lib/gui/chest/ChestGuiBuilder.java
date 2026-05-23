package com.yukikase.lib.gui.chest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.yukikase.lib.gui.TextHelper.text;

public class ChestGuiBuilder implements IChestGui {
    public static final ItemStack DUMMY_ITEM;
    public static final OnGuiClickExecutor DUMMY_EXECUTOR = (p, e) -> e.setCancelled(true);

    static {
        DUMMY_ITEM = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        var meta = DUMMY_ITEM.getItemMeta();
        meta.customName(text(""));

        DUMMY_ITEM.setItemMeta(meta);
    }

    protected final Inventory inventory;
    protected final Component title;
    protected final int rows;
    protected final Player player;

    private final Map<Integer, GuiItem> guiItems = new HashMap<>();

    public ChestGuiBuilder(int rows, Component title, Player player) {
        inventory = Bukkit.createInventory(player, rows * 9, title);
        this.title = title;
        this.rows = rows;
        this.player = player;
    }

    public void addItem(int slot, ItemStack item, OnGuiClickExecutor executor) {
        guiItems.put(slot, new GuiItem(item, executor));
        inventory.setItem(slot, item);
    }

    public void addItem(ItemStack item, OnGuiClickExecutor executor) {
        for (var i = 0; i < inventory.getSize(); i++) {
            var currentItem = inventory.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                addItem(i, item, executor);
                return;
            }
        }
    }

    public void removeItem(int slot) {
        guiItems.remove(slot);
        inventory.setItem(slot, DUMMY_ITEM);
    }

    public void addBorder() {
        for (var i = 0; i < inventory.getSize(); i++) {
            var currentItem = inventory.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                if (i < 9 || i > inventory.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                    addDummyItem(i);
                }
            }
        }
    }

    public final void fill() {
        for (var i = 0; i < inventory.getSize(); i++) {
            var currentItem = inventory.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                addDummyItem(i);
            }
        }
    }

    public void fillRowWithDummyItems(int row) {
        var start = row * 9;
        for (var i = start; i < start + 9; i++) {
            var currentItem = inventory.getItem(i);
            if (currentItem == null || currentItem.getType().equals(Material.AIR)) {
                addItem(i, DUMMY_ITEM, DUMMY_EXECUTOR);
            }
        }
    }

    public void addDummyItem(int slot) {
        addItem(slot, DUMMY_ITEM, DUMMY_EXECUTOR);
    }

    @Override
    public void onOpen(Player player) {
    }

    @Override
    public void onClose(Player player) {
    }

    protected void beforeOpen(Player player) {
    }

    @Override
    public final void open(Player player) {
        GuiHandler.registerGui(this);
        beforeOpen(player);
        player.openInventory(inventory);
    }

    @Override
    public void close(Player player) {
        if (player.getOpenInventory().getTopInventory().equals(inventory)) {
            player.closeInventory();
        }
    }

    @Override
    public Component title() {
        return title;
    }

    protected GuiItem getGuiItem(int slot) {
        return guiItems.get(slot);
    }

    @Override
    public final void onClick(Player player, InventoryClickEvent event, int slot) {
        var item = getGuiItem(slot);
        if (item != null) {
            var executor = item.executor();
            if (executor != null) {
                item.executor().onGuiClick(player, event);
            }
        }
    }
}
