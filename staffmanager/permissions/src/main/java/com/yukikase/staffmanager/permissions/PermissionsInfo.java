package com.yukikase.staffmanager.permissions;

import com.yukikase.staffmanager.permissions.entity.GroupPermission;
import com.yukikase.staffmanager.permissions.entity.Permission;
import com.yukikase.staffmanager.permissions.entity.PermissionContext;
import com.yukikase.staffmanager.permissions.entity.PlayerPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record PermissionsInfo(Permission permission, boolean value, Collection<PermissionContext> permissionContexts,
                              int priority) {

    public static PermissionsInfo from(PlayerPermission permission) {
        return new PermissionsInfo(permission.permission(), permission.value(), permission.contexts(), Integer.MAX_VALUE);
    }

    public static List<PermissionsInfo> fromPlayer(List<PlayerPermission> permissions) {
        List<PermissionsInfo> permissionsInfos = new ArrayList<>();
        for (var permission : permissions) {
            permissionsInfos.add(from(permission));
        }
        return permissionsInfos;
    }

    public static PermissionsInfo from(GroupPermission permission) {
        return new PermissionsInfo(permission.permission(), permission.value(), permission.contexts(), permission.group().index());
    }

    public static List<PermissionsInfo> fromGroup(List<GroupPermission> permissions) {
        List<PermissionsInfo> permissionsInfos = new ArrayList<>();
        for (var permission : permissions) {
            permissionsInfos.add(from(permission));
        }
        return permissionsInfos;
    }
}
