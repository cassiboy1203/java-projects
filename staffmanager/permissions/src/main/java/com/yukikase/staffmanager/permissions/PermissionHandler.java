package com.yukikase.staffmanager.permissions;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.staffmanager.permissions.entity.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

@Singleton
@Component
public class PermissionHandler implements IPermissionHandler {

    private final EntitySet<PlayerPermission> playerPermissions;
    private final EntitySet<GroupPermission> groupPermissions;
    private final EntitySet<PlayerGroups> playerGroups;
    private final EntitySet<Group> groups;
    private final EntitySet<GroupParent> groupParents;
    private final YukikasePlugin plugin;

    private final Map<UUID, PermissionNode> playerCache = new HashMap<>();
    private final Map<String, GroupNode> groupPermissionCache = new HashMap<>();
    private final Map<UUID, List<String>> playerGroupCache = new HashMap<>();


    @Inject
    public PermissionHandler(EntitySet<PlayerPermission> playerPermissions, EntitySet<GroupPermission> groupPermissions, EntitySet<PlayerGroups> playerGroups, EntitySet<Group> groups, EntitySet<GroupParent> groupParents, YukikasePlugin plugin) {
        this.playerPermissions = playerPermissions;
        this.groupPermissions = groupPermissions;
        this.playerGroups = playerGroups;
        this.groups = groups;
        this.groupParents = groupParents;
        this.plugin = plugin;
    }

    @Override
    public void loadGroups() {
        groupPermissionCache.clear();
        try {
            var groups = this.groups.getAll();
            for (var group : groups) {
                var node = groupPermissionCache.computeIfAbsent(group.name(), name -> new GroupNode(group));
                for (var parent : this.groupParents.queryBuilder().where().eq("child_id", group.name()).query()) {
                    var parentNode = groupPermissionCache.computeIfAbsent(parent.parent().name(), name -> new GroupNode(parent.parent()));
                    node.addParent(parentNode);
                }
            }

            for (var groupNode : groupPermissionCache.values()) {
                var group = groupNode.group();
                var groupPermission = this.groupPermissions.queryBuilder().where().eq("group_id", group.name()).query();
                var permissionsInfo = groupPermission.stream().map(gp -> new PermissionsInfo(gp.permission(), gp.value(), gp.contexts(), group.index())).toList();
                groupNode.permissions(PermissionNode.from(permissionsInfo));
                groupNode.permissionsInfos(permissionsInfo);

                this.groupPermissionCache.put(group.name(), groupNode);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Something went wrong while loading groups: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFromCache(Player player) {
        playerCache.remove(player.getUniqueId());
    }

    @Override
    public void addToCache(Player player) {
        addToCache(player.getUniqueId());
    }

    private void addToCache(UUID uuid) {
        try {
            var playerPermissions = this.playerPermissions.queryBuilder().where().eq("player_id", uuid).query();
            var permissions = PermissionsInfo.fromPlayer(playerPermissions);

            playerCache.put(uuid, PermissionNode.from(permissions));

            var groups = this.playerGroups.queryBuilder().where().eq("player_id", uuid).query();
            for (var group : groups) {
                var playerGroupCache = this.playerGroupCache.computeIfAbsent(uuid, id -> new ArrayList<>());
                if (!playerGroupCache.contains(group.group().name())) {
                    playerGroupCache.add(group.group().name());
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Something went wrong while loading player permissions: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reloadCache() {
        var players = playerCache.keySet();
        playerCache.clear();
        playerGroupCache.clear();
        groupPermissionCache.clear();
        loadGroups();
        for (var player : players) {
            addToCache(player);
        }
    }

    @Override
    public void reloadCache(Player player) {
        removeFromCache(player);
        addToCache(player);
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        var groups = new ArrayList<GroupNode>();
        var playerGroups = playerGroupCache.get(player.getUniqueId());
        if (playerGroups != null) {
            for (var groupName : playerGroupCache.get(player.getUniqueId())) {
                groups.add(groupPermissionCache.get(groupName));
            }
        }
        var groupPermissions = GroupNode.mergedPermissions(groups.toArray(new GroupNode[0]));
        var playerPermissions = playerCache.get(player.getUniqueId());

        var playerPermissionReply = playerPermissions.hasPermission(permission, null, player.isOp());
        var reply = playerPermissionReply;
        if (playerPermissionReply.isDefault() || playerPermissionReply.equals(PermissionNode.PermissionReply.FALSE)) {
            reply = groupPermissions.hasPermission(permission, null, player.isOp());
        }

        return reply == PermissionNode.PermissionReply.DEFAULT_TRUE || reply == PermissionNode.PermissionReply.POSITIVE;
    }
}
