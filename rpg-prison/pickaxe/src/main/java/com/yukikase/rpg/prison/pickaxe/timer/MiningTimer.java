package com.yukikase.rpg.prison.pickaxe.timer;

import com.yukikase.lib.task.Timer;
import com.yukikase.rpg.prison.pickaxe.BreakableBlocks;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlock;
import org.bukkit.entity.Player;

public class MiningTimer extends Timer {

    private final BreakableBlock block;
    private final Player player;
    private final BreakableBlocks breakableBlocks;

    private final int damage;
    private int health;
    private final int maxHealth;

    public MiningTimer(BreakableBlock block, Player player, int damage, BreakableBlocks breakableBlocks) {
        this.block = block;
        this.player = player;
        this.breakableBlocks = breakableBlocks;
        this.damage = damage;

        this.maxHealth = block.getType().getHardness() * 20;
        this.health = maxHealth;
    }

    @Override
    public void run() {
        if (isCancelled()) return;
        if (!block.getLocation().getBlock().getType().equals(block.getType().getMaterial())) {
            cancel();
            return;
        }
        player.sendBlockDamage(block.getLocation(), 0, BreakableBlocks.ENTITY_ID);
        if (damage < block.getType().getResistance()) {
            breakableBlocks.resetMiningAnimation(player, block.getLocation());
            return;
        }

        health -= damage;

        if (health <= 0) {
            breakableBlocks.breakBlock(player, block);
            breakableBlocks.resetMiningAnimation(player, block.getLocation());
            cancel();
            return;
        }

        var stage = (int) Math.min(9, Math.max(0, Math.floor((double) (maxHealth - health) / maxHealth * 10)));

        breakableBlocks.sendBlockBreakStage(player, block.getLocation(), stage);
    }
}
