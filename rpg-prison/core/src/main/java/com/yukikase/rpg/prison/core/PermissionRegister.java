package com.yukikase.rpg.prison.core;

import com.yukikase.lib.permission.Permission;

public class PermissionRegister {
    public static final Permission BASE = new Permission("rpgprison", "");
    public static final Permission CORE_BASE = new Permission("core", "", BASE);
}
