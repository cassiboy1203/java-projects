package com.yukikase.lib.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.entity.PlayerEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
public class PlayerJoinListener implements Listener {
    private final EntitySet<PlayerEntity> players;

    @Inject
    public PlayerJoinListener(EntitySet<PlayerEntity> players) {
        this.players = players;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        players.addIfNotExists(new PlayerEntity(player.getUniqueId(), player.getName()));
    }
}
