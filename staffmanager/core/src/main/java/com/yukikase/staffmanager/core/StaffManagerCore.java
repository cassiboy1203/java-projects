package com.yukikase.staffmanager.core;

import com.yukikase.diframework.anotations.Singleton;
import com.yukikase.lib.YukikasePlugin;

@Singleton
public class StaffManagerCore extends YukikasePlugin {
    public static final String NAME = "StaffManagerCore";

    @Override
    public Class<?> permissionRegister() {
        return PermissionRegister.class;
    }
}
