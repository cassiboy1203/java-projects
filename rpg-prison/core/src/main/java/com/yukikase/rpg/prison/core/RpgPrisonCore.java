package com.yukikase.rpg.prison.core;

import com.yukikase.lib.YukikasePlugin;

public class RpgPrisonCore extends YukikasePlugin {
    @Override
    public Class<?> permissionRegister() {
        return PermissionRegister.class;
    }
}
