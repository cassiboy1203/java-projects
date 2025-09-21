package com.yukikase.staffmanager.permissions.event.listener;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Value;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.staffmanager.permissions.IPermissionHandler;
import com.yukikase.staffmanager.permissions.Permissible;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Component
public class PlayerJoinListener implements Listener {

    private final IPermissionHandler permissionHandler;
    private final YukikasePlugin plugin;

    @Value("kickOnSetupFailure")
    private boolean kickOnSetupFailure;

    @Value("kickMessage")
    private String kickMessage;

    @Inject
    public PlayerJoinListener(IPermissionHandler permissionHandler, YukikasePlugin plugin) {
        this.permissionHandler = permissionHandler;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        permissionHandler.removeFromCache(player);
        permissionHandler.addToCache(player);

        try {
            var permField = player.getClass().getSuperclass().getDeclaredField("perm");
            permField.setAccessible(true);
            permField.set(player, new Permissible(player, permissionHandler, player));
            permField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.getLogger().severe("Failed to set permissible permissions for player: " + player.getName() + ". With the following error: " + e.getMessage());
            if (kickOnSetupFailure) {
                player.kick(net.kyori.adventure.text.Component.text(kickMessage));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        permissionHandler.removeFromCache(player);
    }
}
