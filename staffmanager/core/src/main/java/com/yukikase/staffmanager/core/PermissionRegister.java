package com.yukikase.staffmanager.core;

import com.yukikase.lib.permission.Permission;

public abstract class PermissionRegister {
    private static final String STAFF = "staff";

    public static final Permission PERMISSION_BASE = new Permission("staffmanager", "The base of all staffmanager permissions");

    // Staff mode permissions
    public static final Permission STAFF_MODE_BASE = new Permission(STAFF, "Allows the player to enter staff mode", PERMISSION_BASE);
    public static final Permission STAFF_MODE_GOD = new Permission("god", "Allows the player to enter god mode while in staff mode", STAFF_MODE_BASE);
    public static final Permission STAFF_MODE_FLY = new Permission("fly", "Allows to player to enter flight mode while in staff mode", STAFF_MODE_BASE);

    // Gamemode permissions
    public static final Permission GAMEMODE_BASE = new Permission("gamemode", "Allows the player to change gamemodes", PERMISSION_BASE);
    public static final Permission GAMEMODE_OTHER = new Permission("other", "Allows the player to change other players gamemode", GAMEMODE_BASE);

    // Staff mode gamemode permissions
    public static final Permission STAFF_MODE_GAMEMODE = new Permission(STAFF, "Allows the player to change gamemodes while in staff mode", GAMEMODE_BASE, true);
    public static final Permission STAFF_MODE_GAMEMODE_OTHER = new Permission(STAFF, "Allow the player to change other players gamemodes while in staff mode", GAMEMODE_OTHER, true);
}
