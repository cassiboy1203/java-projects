package com.yukikase.rpg.prison.pickaxe.packet.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.annotations.packet.PacketListener;
import com.yukikase.lib.packet.IPacketListener;
import com.yukikase.lib.packet.event.BlockBreakAnimationEvent;
import com.yukikase.lib.packet.event.DigActionEvent;
import com.yukikase.rpg.prison.pickaxe.BreakableBlocks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Component
public class DigEventListener implements IPacketListener {

    private final BreakableBlocks breakableBlocks;

    @Inject
    public DigEventListener(BreakableBlocks breakableBlocks) {
        this.breakableBlocks = breakableBlocks;
    }

    @PacketListener
    public void onDigAction(DigActionEvent event) {
        switch (event.action()) {
            case START -> {
                startMining(event.player(), event.location());
            }
            case CANCEL -> {
                cancelMining(event.player(), event.location());
            }
        }
    }


    @PacketListener
    public void onDigAnimation(BlockBreakAnimationEvent event) {
        if (event.entityId() != BreakableBlocks.ENTITY_ID) {
            event.cancel();
        }
    }

    private void startMining(Player player, Location location) {
        breakableBlocks.startMining(player, location);
    }

    private void cancelMining(Player player, Location location) {
        breakableBlocks.cancelMining(player, location);
    }

    private void finishMining(Player player, Location location) {
        breakableBlocks.resetMiningAnimation(player, location);
    }
}
