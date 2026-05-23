package com.yukikase.lib.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.lib.gui.chest.GuiHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@Component
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!event.getView().getTopInventory().equals(event.getClickedInventory())) return;
            var gui = GuiHandler.get(event.getView().title());
            if (gui != null) {
                gui.onClick(player, event, event.getSlot());
            }
        }
    }
}
