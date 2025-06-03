package com.yukikase.lib;

import com.yukikase.diframework.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public abstract class YukikasePlugin extends JavaPlugin {
    protected Injector injector;

    public final void useClassLoader() {
        Thread.currentThread().setContextClassLoader(getClassLoader());
    }

    public Class<?> permissionRegister() {
        return null;
    }

    @Override
    public final void onDisable() {
        doDisable();
    }

    @Override
    public final void onEnable() {
        injector = register();

        doEnable();
    }

    protected void doEnable() {
    }

    protected void doDisable() {
    }

    private Injector register() {
        YukikaseLib lib = (YukikaseLib) Bukkit.getPluginManager().getPlugin("YukikaseLib");

        if (lib == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Missing dependency: YukikaseLib");
            Bukkit.getPluginManager().disablePlugin(this);
            return null;
        }

        return lib.registerPlugin(this);
    }
}
