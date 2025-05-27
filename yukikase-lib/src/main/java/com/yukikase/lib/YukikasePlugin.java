package com.yukikase.lib;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class YukikasePlugin extends JavaPlugin {
    public void useClassLoader() {
        Thread.currentThread().setContextClassLoader(getClassLoader());
    }
}
