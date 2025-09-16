package com.yukikase.rpg.prison.pickaxe;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IBreakableBlocks {
    void startMining(Player player, Location location);

    void cancelMining(Player player, Location location);

    void resetMiningAnimation(Player player, Location location);
}
