package com.yukikase.staffmanager.permissions;

import com.yukikase.staffmanager.permissions.entity.PermissionContext;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;

public class PermissionNode {
    private final String permission;
    private final Map<String, PermissionNode> children = new HashMap<>();
    private PermissionDefault permissionDefault;
    private boolean value;
    private List<PermissionContext> context = new ArrayList<>();
    private int priority = Integer.MIN_VALUE;

    public PermissionNode(String permission) {
        this.permission = permission;
        permissionDefault = PermissionDefault.OP;
    }

    public PermissionNode getOrCreate(String permission) {
        return children.computeIfAbsent(permission, PermissionNode::new);
    }

    public Collection<PermissionNode> getChildren() {
        return children.values();
    }

    public PermissionReply hasPermission(String permission, List<PermissionContext> context, boolean isOp) {
        var hasPermission = hasPermission(permission.split("\\."), context);
        if (hasPermission.equals(PermissionReply.FALSE)) {
            return switch (permissionDefault) {
                case OP -> isOp ? PermissionReply.DEFAULT_TRUE : PermissionReply.DEFAULT_FALSE;
                case NOT_OP -> isOp ? PermissionReply.DEFAULT_FALSE : PermissionReply.DEFAULT_TRUE;
                case TRUE -> PermissionReply.DEFAULT_TRUE;
                case FALSE -> PermissionReply.DEFAULT_FALSE;
            };
        }
        return hasPermission;
    }

    private PermissionReply hasPermission(String[] permissions, List<PermissionContext> contexts) {
        if (permissions == null || permissions.length == 0) return PermissionReply.FALSE;
        if (permission != null && permissions.length == 1) {
            if (permissions[0].equals(permission)) {
                if (value) return PermissionReply.POSITIVE;
                return PermissionReply.NEGATIVE;
            }
            if (permission.equals("*")) {
                if (value) return PermissionReply.POSITIVE;
                return PermissionReply.NEGATIVE;
            }
            return PermissionReply.FALSE;
        }

        for (var child : children.values()) {
            PermissionReply reply;
            if (this.permission == null) {
                reply = child.hasPermission(permissions, contexts);
            } else {
                reply = child.hasPermission(Arrays.copyOfRange(permissions, 1, permissions.length), contexts);
            }
            if (reply != PermissionReply.FALSE) return reply;
        }

        return PermissionReply.FALSE;
    }

    public static PermissionNode from(List<PermissionsInfo> permissions) {
        var root = new PermissionNode(null);
        for (var permission : permissions) {
            var permissionSplit = permission.permission().permission().split("\\.");
            var parent = root;
            for (var permissionStep : permissionSplit) {
                parent = parent.getOrCreate(permissionStep);
            }
            if (parent.priority < permission.priority()) {
                parent.permissionDefault = permission.permission().permissionDefault();
                parent.value = permission.value();
                parent.priority = permission.priority();
            }
        }
        return root;
    }

    public enum PermissionReply {
        POSITIVE,
        NEGATIVE,
        FALSE,
        DEFAULT_TRUE,
        DEFAULT_FALSE;

        boolean isDefault() {
            return this == DEFAULT_FALSE || this == DEFAULT_TRUE;
        }
    }
}
