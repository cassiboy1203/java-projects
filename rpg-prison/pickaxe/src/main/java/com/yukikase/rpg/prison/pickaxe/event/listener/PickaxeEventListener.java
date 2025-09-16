package com.yukikase.rpg.prison.pickaxe.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

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

        var miningSpeed = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (miningSpeed != null) {
            miningSpeed.setBaseValue(0);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        if (pickaxes.isPickaxe(player, event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            switch (event.getAction()) {
                case PLACE_ALL, PLACE_ONE -> {
                    var item = event.getCursor();

                    if (pickaxes.isPickaxe(player, item)) {
                        var inventory = event.getClickedInventory();

                        if (inventory == null) return;
                        if (!inventory.getType().equals(InventoryType.PLAYER)) event.setCancelled(true);
                    }
                }
                case HOTBAR_SWAP -> {
                    var hotbarNumber = event.getHotbarButton();
                    if (hotbarNumber == -1) return;

                    var item = player.getInventory().getItem(hotbarNumber);
                    if (pickaxes.isPickaxe(player, item)) {
                        event.setCancelled(true);
                    }
                }
                case MOVE_TO_OTHER_INVENTORY -> {
                    var item = event.getCurrentItem();
                    var inventory = event.getInventory();
                    if (inventory.getType().equals(InventoryType.CRAFTING) || inventory.getType().equals(InventoryType.PLAYER))
                        return;
                    if (pickaxes.isPickaxe(player, item)) {
                        event.setCancelled(true);
                    }
                }
                case SWAP_WITH_CURSOR -> {
                    var item = event.getCursor();
                    var inventory = event.getClickedInventory();
                    if (inventory == null) return;
                    if (pickaxes.isPickaxe(player, item) && !inventory.getType().equals(InventoryType.PLAYER)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();

        event.getDrops().removeIf(item -> pickaxes.isPickaxe(player, item));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        var player = event.getPlayer();

        if (!pickaxes.hasPickaxe(player)) {
            var pickaxeItem = pickaxes.item(player);
            var inventor = player.getInventory();
            inventor.addItem(pickaxeItem);
        }
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        var player = event.getPlayer();
        pickaxes.removeFromCache(player);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        var player = event.getPlayer();
        if (!pickaxes.isPickaxe(player, event.getItem())) return;
        event.setCancelled(true);
    }
}
