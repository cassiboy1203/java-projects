package com.yukikase.lib.gui.chest;

import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler {
    private GuiHandler() {
    }

    private static final Map<Component, IChestGui> guiMap = new HashMap<>();

    public static void registerGui(IChestGui gui) {
        guiMap.put(gui.title(), gui);
    }

    public static void unregisterGui(IChestGui gui) {
        guiMap.remove(gui.title());
    }

    public static IChestGui get(Component title) {
        return guiMap.get(title);
    }
}
