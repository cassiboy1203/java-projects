package com.yukikase.lib.gui;

import com.yukikase.framework.anotations.injection.Singleton;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

@com.yukikase.framework.anotations.injection.Component
@Singleton
public class GuiHandler {
    private final Map<Component, IChestGui> guiMap = new HashMap<>();

    public void registerGui(IChestGui gui) {
        guiMap.put(gui.title(), gui);
    }

    public void unregisterGui(IChestGui gui) {
        guiMap.remove(gui.title());
    }

    public IChestGui get(Component title) {
        return guiMap.get(title);
    }
}
