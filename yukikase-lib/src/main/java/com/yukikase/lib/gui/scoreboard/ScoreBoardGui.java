package com.yukikase.lib.gui.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardGui {

    private static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    protected final Scoreboard scoreboard;
    protected final Objective objective;
    private final List<String> lines;

    public ScoreBoardGui(String name, Criteria criteria, Component title) {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(name, criteria, title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(title);
        lines = new ArrayList<>();
    }

    public ScoreBoardGui addLine(String key, Component line) {
        var score = objective.getScore(key);
        score.customName(line);
        lines.add(key);
        return this;
    }

    public ScoreBoardGui updateLine(String key, Component line) {
        var score = objective.getScore(key);
        score.customName(line);
        return this;
    }

    public ScoreBoardGui build() {
        var lineNumber = lines.size();
        for (var key : lines) {
            var score = objective.getScore(key);
            score.setScore(lineNumber);
            lineNumber--;
        }
        return this;
    }

    public void show(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void hide(Player player) {
        player.setScoreboard(scoreboardManager.getNewScoreboard());
    }
}
