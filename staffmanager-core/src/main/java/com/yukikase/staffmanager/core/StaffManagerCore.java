package com.yukikase.staffmanager.core;

import com.yukikase.diframework.anotations.Singleton;
import com.yukikase.lib.YukikaseLib;
import com.yukikase.lib.annotations.PermissionPrefix;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Singleton
@PermissionPrefix(StaffManagerCore.PERMISSION_BASE)
public class StaffManagerCore extends JavaPlugin {
    public static final String PERMISSION_BASE = "staffmanager";
    public static final String NAME = "StaffManagerCore";

    @Override
    public void onEnable() {
        YukikaseLib lib = (YukikaseLib) Bukkit.getPluginManager().getPlugin("YukikaseLib");

        if (lib == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Missing dependency: YukikaseLib");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        var injector = lib.registerPlugin(this);
    }

    @Override
    public void onDisable() {
    }
}
