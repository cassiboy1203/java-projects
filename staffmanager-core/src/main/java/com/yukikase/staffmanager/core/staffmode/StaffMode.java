package com.yukikase.staffmanager.core.staffmode;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;
import com.yukikase.diframework.anotations.Singleton;
import com.yukikase.lib.IPermissionHandler;
import com.yukikase.staffmanager.core.commands.StaffCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
@Component
public class StaffMode implements IStaffMode {
    final List<UUID> playersInStaffMode = new ArrayList<>();
    private final IPermissionHandler permissionHandler;

    @Inject
    public StaffMode(IPermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    @Override
    public boolean isInStaffMode(Player player) {
        return this.playersInStaffMode.contains(player.getUniqueId());
    }

    @Override
    public boolean toggleStaffMode(Player player) {
        if (isInStaffMode(player))
            return leaveStaffMode(player);

        return enterStaffMode(player);
    }

    @Override
    public boolean leaveStaffMode(Player player) {
        this.playersInStaffMode.remove(player.getUniqueId());

        player.setInvulnerable(false);
        player.setAllowFlight(false);
        player.sendMessage(LEAVE_STAFF_MODE_MESSAGE);
        return true;
    }

    @Override
    public boolean enterStaffMode(Player player) {
        this.playersInStaffMode.add(player.getUniqueId());

        if (this.permissionHandler.playerHasPermission(player, StaffCommand.class, IStaffMode.GODMODE_PERMISSION))
            player.setInvulnerable(true);
        if (this.permissionHandler.playerHasPermission(player, StaffCommand.class, IStaffMode.FLY_PERMISSON))
            player.setAllowFlight(true);

        player.sendMessage(ENTER_STAFF_MODE_MESSAGE);
        return true;
    }
}
