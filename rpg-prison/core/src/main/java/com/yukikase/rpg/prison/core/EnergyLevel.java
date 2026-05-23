package com.yukikase.rpg.prison.core;

import java.util.SortedMap;
import java.util.TreeMap;

public class EnergyLevel extends Level {

    public static final int MAX_LEVEL = 100;

    private static final int BASE_XP = 10000;
    private static final int XP_PER_LEVEL = 1000;

    private static final SortedMap<Integer, EnergyLevel> levels = new TreeMap<>();

    static {
        for (int i = 1; i <= MAX_LEVEL; i++) {
            levels.put(i, new EnergyLevel(i, Math.round(XP_PER_LEVEL * Math.pow(i - 1, 1.5) + BASE_XP)));
        }
    }

    public static EnergyLevel getLevel(int level) {
        return levels.get(level);
    }

    protected EnergyLevel(int level, long requiredExperience) {
        super(level, requiredExperience);
    }
}
