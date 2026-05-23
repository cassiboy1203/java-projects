package com.yukikase.lib.gui;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Singleton;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Singleton
public class MessageHandler implements Listener {
    private static final Map<UUID, MessageCallback> messageCallbacks = new ConcurrentHashMap<>();

    public static void handleNextMessage(Player player, MessageCallback callback) {
        messageCallbacks.put(player.getUniqueId(), callback);
    }

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        var callback = messageCallbacks.get(player.getUniqueId());
        if (callback != null) {
            messageCallbacks.remove(player.getUniqueId());
            callback.onMessage(PlainTextComponentSerializer.plainText().serialize(event.message()));
            event.setCancelled(true);
        }
    }

}
