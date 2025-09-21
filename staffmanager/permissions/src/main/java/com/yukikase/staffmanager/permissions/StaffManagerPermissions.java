package com.yukikase.staffmanager.permissions;

import com.yukikase.lib.YukikasePlugin;
import com.yukikase.staffmanager.core.StaffManagerCore;

public class StaffManagerPermissions extends YukikasePlugin {
    @Override
    protected void doEnable() {
        StaffManagerCore core = getRequiredDependency("StaffManagerCore");
        injector.registerSingleton(StaffManagerCore.class, core);

        saveDefaultConfig();
    }

    @Override
    public boolean databaseEnabled() {
        return true;
    }
}
