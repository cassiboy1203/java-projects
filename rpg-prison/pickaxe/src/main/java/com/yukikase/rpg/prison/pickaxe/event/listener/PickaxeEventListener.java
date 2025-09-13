package com.yukikase.rpg.prison.pickaxe.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
public class PickaxeEventListener implements Listener {

    private final IPickaxes pickaxes;

    @Inject
    public PickaxeEventListener(IPickaxes pickaxes) {
        this.pickaxes = pickaxes;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (!pickaxes.hasPickaxe(player)) {
            var pickaxeItem = pickaxes.item(player);

            var inventor = player.getInventory();
            inventor.addItem(pickaxeItem);
        }
    }
}
