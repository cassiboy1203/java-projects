package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.rpg.prison.core.Level;

import java.util.SortedMap;
import java.util.TreeMap;

public class PickaxeLevel extends Level {
    private static final int MAX_LEVEL = 125;

    private static final int BASE_XP = 10;
    private static final int XP_PER_LEVEL = 5;

    private static final SortedMap<Integer, PickaxeLevel> levels = new TreeMap<>();

    static {
        for (int i = 1; i <= MAX_LEVEL; i++) {
            levels.put(i, new PickaxeLevel(i, Math.round(BASE_XP + Math.pow(XP_PER_LEVEL, i - 1))));
        }
    }

    private PickaxeLevel(int level, long requiredExperience) {
        super(level, requiredExperience);
    }

    public static PickaxeLevel getLevel(int level) {
        return levels.get(level);
    }
}
