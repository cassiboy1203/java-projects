package com.yukikase.lib.gui.chest;

import org.bukkit.inventory.ItemStack;

public record GuiItem(ItemStack itemStack, OnGuiClickExecutor executor) {
}
