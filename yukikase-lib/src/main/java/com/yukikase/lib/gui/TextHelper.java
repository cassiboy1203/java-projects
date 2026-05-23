package com.yukikase.lib.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TextHelper {
    private TextHelper() {
    }

    private static final int NUMBER_OF_BARS = 35;

    public static Component text(String text, NamedTextColor color) {
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false);
    }

    public static Component text(String text) {
        return text(text, NamedTextColor.GRAY);
    }

    public static Component bold(String text) {
        return bold(text, NamedTextColor.GRAY);
    }

    public static Component bold(String text, NamedTextColor color) {
        return text(text, color).decoration(TextDecoration.BOLD, true);
    }

    public static Component progressbar(long currentValue, long maxValue) {
        var percent = Math.min(((double) currentValue / (double) maxValue) * 100, 100);
        Component bar = text("");
        for (var i = 0; i < NUMBER_OF_BARS; i++) {
            if (i < percent / (double) 100 * NUMBER_OF_BARS) {
                bar = bar.append(text("|", NamedTextColor.GREEN));
            } else {
                bar = bar.append(text("|", NamedTextColor.RED));
            }
        }
        return bar.append(text(" " + Math.round(percent) + "%", NamedTextColor.WHITE));
    }

    public static Component progressbar(long currentValue, long maxValue, long startValue) {
        return progressbar(currentValue - startValue, maxValue - startValue);
    }

    public static Component progressBarNumber(long currentValue, long maxValue) {
        return text("(", NamedTextColor.GRAY).append(text(String.valueOf(currentValue), NamedTextColor.WHITE)).append(text(" / " + maxValue + ")"));
    }
}
