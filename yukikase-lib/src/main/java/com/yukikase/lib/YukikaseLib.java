package com.yukikase.lib;

import com.yukikase.diframework.DIFramework;
import com.yukikase.diframework.DefaultInjector;
import com.yukikase.diframework.anotations.Singleton;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class YukikaseLib extends JavaPlugin {

    public DefaultInjector registerPlugin(YukikasePlugin plugin) {
        var injector = DIFramework.start(plugin.getClass(), YukikaseLib.class);
        injector.registerSingleton(YukikasePlugin.class, plugin);
        injector.registerSingleton(JavaPlugin.class, plugin);
        injector.runConfigurations();
        return injector;
    }
}
