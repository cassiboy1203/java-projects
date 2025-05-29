package com.yukikase.staffmanager.core.staffmode;

import org.bukkit.entity.Player;

public interface IStaffMode {
    String PERMISSION = "staff";
    String GODMODE_PERMISSION = "god";
    String FLY_PERMISSON = "fly";

    String ENTER_STAFF_MODE_MESSAGE = "You have entered staff mode.";
    String LEAVE_STAFF_MODE_MESSAGE = "You have left staff mode.";

    boolean isInStaffMode(Player player);

    boolean toggleStaffMode(Player player);

    boolean leaveStaffMode(Player player);

    boolean enterStaffMode(Player player);
}
