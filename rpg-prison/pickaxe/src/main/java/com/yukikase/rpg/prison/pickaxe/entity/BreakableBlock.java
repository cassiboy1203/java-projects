package com.yukikase.rpg.prison.pickaxe.entity;

import org.bukkit.Location;

public class BreakableBlock {
    private String worldName;

    private Location location;

    private BreakableBlockType type;

    protected BreakableBlock() {
    }

    public BreakableBlock(Location location, BreakableBlockType type) {
        this.location = location;
        this.type = type;
    }

    public Location location() {
        return location;
    }

    public BreakableBlockType type() {
        return type;
    }

    public void type(BreakableBlockType type) {
        this.type = type;
    }

    public void location(Location location) {
        this.location = location;
    }
}
