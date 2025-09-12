package com.yukikase.rpg.prison.pickaxe.packets.events.listeners;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.lib.annotations.packet.PacketListener;
import com.yukikase.lib.packet.IPacketListener;
import com.yukikase.lib.packet.event.DigActionEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Component
public class DigEventListener implements IPacketListener {
    @PacketListener
    public void onDigAction(DigActionEvent event) {
        switch (event.action()){
            case START -> {
                startMining(event.player(), event.location());
            }
            case CANCEL -> {
                cancelMining(event.player(), event.location());
            }
        }
    }

    private void startMining(Player player, Location location){

    }

    private void cancelMining(Player player, Location location){

    }
}
