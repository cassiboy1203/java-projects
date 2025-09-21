package com.yukikase.lib.gui;

import org.bukkit.inventory.ItemStack;

public record GuiItem(ItemStack itemStack, OnGuiClickExecutor executor) {
}
