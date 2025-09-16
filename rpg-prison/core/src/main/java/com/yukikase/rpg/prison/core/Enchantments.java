package com.yukikase.rpg.prison.core;

public enum Enchantments implements IEnchantment {
    UNBREAKING("unbreaking", "Has a change to not consume durability.", 3),
    MENDING("mending", "Restores durabilty on gaining xp.", 1),
    EXP_MULTI("multiply xp", "Has a change to multipty the experience gained", 3);

    private final String name;
    private final String description;
    private final int maxLevel;

    Enchantments(String name, String description, int maxLevel) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }
}
