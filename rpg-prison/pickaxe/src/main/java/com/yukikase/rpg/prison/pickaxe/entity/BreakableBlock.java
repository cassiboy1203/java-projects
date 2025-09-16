package com.yukikase.rpg.prison.pickaxe.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.SQLException;

@DatabaseTable(tableName = "breakable_blocks")
public class BreakableBlock {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String worldName;

    @DatabaseField
    private int x;

    @DatabaseField
    private int y;

    @DatabaseField
    private int z;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private BreakableBlockType type;

    protected BreakableBlock() {
    }

    public BreakableBlock(Location location, BreakableBlockType type) {
        this.worldName = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.type = type;
    }

    public Location getLocation() {
        var world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

    public BreakableBlockType getType() {
        return type;
    }

    public void setType(BreakableBlockType type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public static PreparedQuery<BreakableBlock> getLocationQuery(QueryBuilder<BreakableBlock, Object> queryBuilder, Location location) throws SQLException {
        queryBuilder.where().eq("worldName", location.getWorld().getName())
                .and().eq("x", location.getBlockX())
                .and().eq("y", location.getBlockY())
                .and().eq("z", location.getBlockZ());
        return queryBuilder.prepare();
    }
}
