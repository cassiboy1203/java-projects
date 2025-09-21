package com.yukikase.staffmanager.permissions;

import com.yukikase.staffmanager.permissions.entity.Group;

import java.util.ArrayList;
import java.util.List;

public final class GroupNode {
    private final Group group;
    private final List<GroupNode> parents = new ArrayList<>();
    private List<PermissionsInfo> permissionsInfos;
    private PermissionNode permissions;
    private PermissionNode fullPermissions;

    public GroupNode(Group group) {
        this.group = group;
    }

    public void permissionsInfos(List<PermissionsInfo> permissionsInfos) {
        this.permissionsInfos = permissionsInfos;
    }

    public void permissions(PermissionNode permissions) {
        this.permissions = permissions;
    }

    public void addParent(GroupNode groupNode) {
        this.parents.add(groupNode);
    }

    public List<PermissionsInfo> permissionsInfos() {
        if (parents.isEmpty()) {
            return permissionsInfos;
        }
        List<PermissionsInfo> permissionsInfos = new ArrayList<>();
        for (GroupNode groupNode : parents) {
            permissionsInfos.addAll(groupNode.permissionsInfos());
        }
        return permissionsInfos;
    }

    public PermissionNode permissions() {
        if (fullPermissions == null) {
            var permissionInfos = permissionsInfos();
            fullPermissions = PermissionNode.from(permissionInfos);
        }
        return fullPermissions;
    }

    public Group group() {
        return group;
    }

    public static PermissionNode mergedPermissions(GroupNode... groupNodes) {
        var permissionInfos = new ArrayList<PermissionsInfo>();
        for (var groupNode : groupNodes) {
            permissionInfos.addAll(groupNode.permissionsInfos());
        }
        return PermissionNode.from(permissionInfos);
    }
}
