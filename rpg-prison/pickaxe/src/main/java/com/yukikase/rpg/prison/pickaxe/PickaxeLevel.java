package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.rpg.prison.core.Level;

import java.util.SortedMap;
import java.util.TreeMap;

public class PickaxeLevel extends Level {
    public static final int MAX_LEVEL = 125;

    private static final int BASE_XP = 10;
    private static final int XP_PER_LEVEL = 100;

    private static final SortedMap<Integer, PickaxeLevel> levels = new TreeMap<>();

    static {
        for (int i = 1; i <= MAX_LEVEL; i++) {
            levels.put(i, new PickaxeLevel(i, Math.round(XP_PER_LEVEL * Math.pow(i - 1, 2) + BASE_XP)));
        }
    }

    private PickaxeLevel(int level, long requiredExperience) {
        super(level, requiredExperience);
    }

    public static PickaxeLevel getLevel(int level) {
        return levels.get(level);
    }
}
