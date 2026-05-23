package com.yukikase.lib;

import com.yukikase.framework.YukikaseFramework;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.injection.Injector;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class YukikaseLib extends JavaPlugin {

    private static final List<Class<? extends Listener>> LISTENERS = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

    }

    public Injector registerPlugin(YukikasePlugin plugin) {
        Injector injector;
        if (plugin.databaseEnabled()) {
            injector = YukikaseFramework.start(true, plugin.getClass(), YukikaseLib.class, YukikaseFramework.class);
        } else {
            injector = YukikaseFramework.start(plugin.getClass(), YukikaseLib.class);
        }
        injector.registerSingleton(YukikasePlugin.class, plugin);
        injector.registerSingleton(JavaPlugin.class, plugin);
        injector.registerSingleton(YukikaseLib.class, this);
        if (plugin.databaseEnabled()) {
            try {
                YukikaseFramework.startDatabase(injector);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        injector.runConfigurations();
        return injector;
    }

    public static void registerListener(YukikasePlugin plugin, Listener listener) {
        if (!LISTENERS.contains(listener.getClass())) {
            LISTENERS.add(listener.getClass());
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
