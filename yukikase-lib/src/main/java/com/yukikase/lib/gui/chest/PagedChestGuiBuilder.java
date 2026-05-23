package com.yukikase.lib.gui.chest;

import com.yukikase.framework.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.yukikase.lib.gui.TextHelper.text;

public class PagedChestGuiBuilder extends ChestGuiBuilder {
    private PagedChestGuiBuilder prev;
    private PagedChestGuiBuilder next;
    private final int pageSize;

    private final Map<Integer, Tuple<ItemStack, OnGuiClickExecutor>> genericItems = new HashMap<>();

    private boolean borderAdded;

    private static final ItemStack NEXT_ITEM;
    private static final ItemStack PREV_ITEM;

    static {
        NEXT_ITEM = new ItemStack(Material.ARROW);
        PREV_ITEM = new ItemStack(Material.ARROW);

        var next_meta = NEXT_ITEM.getItemMeta();
        next_meta.customName(text("Next", NamedTextColor.YELLOW));
        NEXT_ITEM.setItemMeta(next_meta);

        var prev_meta = PREV_ITEM.getItemMeta();
        prev_meta.customName(text("Prev", NamedTextColor.YELLOW));
        PREV_ITEM.setItemMeta(prev_meta);
    }

    public PagedChestGuiBuilder(int rows, Component title, Player player) {
        super(rows, title, player);

        this.pageSize = (rows - 1) * 9;
        fillRowWithDummyItems(rows - 1);
    }

    protected final PagedChestGuiBuilder getOrCreateNext() {
        if (next == null) {
            next = new PagedChestGuiBuilder(rows, title, player);
            for (var item : genericItems.entrySet()) {
                next.addGenericItem(item.getKey(), item.getValue().t(), item.getValue().v());
            }
            if (borderAdded) {
                next.addBorder();
            }
            next.prev = this;
        }
        return next;
    }

    public void addPage(PagedChestGuiBuilder page) {
        if (next == null) {
            next = page;
            page.prev = this;
        } else {
            next.addPage(page);
        }
    }

    public void addGenericItem(int slot, ItemStack item, OnGuiClickExecutor executor) {
        genericItems.put(slot, new Tuple<>(item, executor));
        super.addItem(slot, item, executor);
    }

    @Override
    public void addBorder() {
        super.addBorder();
        borderAdded = true;
    }

    @Override
    protected void beforeOpen(Player player) {
        if (next != null) {
            addItem(inventory.getSize() - 4, NEXT_ITEM, this::onNext);
        }
        if (prev != null) {
            addItem(inventory.getSize() - 6, PREV_ITEM, this::onPrev);
        }
    }

    @Override
    public void addItem(ItemStack item, OnGuiClickExecutor executor) {
        for (var i = 0; i < pageSize; i++) {
            var currentSlot = inventory.getItem(i);
            if (currentSlot == null || currentSlot.isEmpty()) {
                super.addItem(i, item, executor);
                return;
            }
        }

        getOrCreateNext().addItem(item, executor);
    }

    @Override
    public void removeItem(int slot) {
    }

    private void onNext(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (next != null) {
            next.open(player);
        }
    }

    private void onPrev(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (prev != null) {
            prev.open(player);
        }
    }
}