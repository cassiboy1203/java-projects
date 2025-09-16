package com.yukikase.rpg.prison.core;

public abstract class Level {
    protected int level;
    protected long requiredExperience;

    protected Level(int level, long requiredExperience) {
        this.level = level;
        this.requiredExperience = requiredExperience;
    }

    public int level() {
        return this.level;
    }

    public long requiredExperience() {
        return this.requiredExperience;
    }
}
