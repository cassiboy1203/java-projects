package com.yukikase.lib.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.gui.GuiHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@Component
public class InventoryInteractListener implements Listener {

    private final GuiHandler guiHandler;

    @Inject
    public InventoryInteractListener(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            var gui = guiHandler.get(event.getView().title());
            if (gui != null) {
                event.setCancelled(true);
                gui.onClick(player, event.getClick(), event.getSlot());
            }
        }
    }
}
