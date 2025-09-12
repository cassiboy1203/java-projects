package com.yukikase.lib.event;

import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;
import com.yukikase.lib.YukikasePlugin;
import org.bukkit.event.Listener;

import java.util.List;

@Configuration
public class EventListenerRegister {

    @Register
    public void registerEventListeners(List<Listener> listeners, YukikasePlugin plugin) {
        for (var listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
