package com.yukikase.lib.gui.scoreboard;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoardHandler {
    private ScoreBoardHandler() {
    }

    private static final Map<String, ScoreBoardGui> scoreBoardGuiMap = new HashMap<>();

    public static ScoreBoardGui get(String name) {
        return scoreBoardGuiMap.get(name);
    }

    public static void register(String name, ScoreBoardGui scoreBoardGui) {
        scoreBoardGuiMap.put(name, scoreBoardGui);
    }
}
