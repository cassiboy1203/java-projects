package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.packet.event.BlockBreakAnimationEvent;
import com.yukikase.lib.task.Timer;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlock;
import com.yukikase.rpg.prison.pickaxe.timer.BlockRespawnTask;
import com.yukikase.rpg.prison.pickaxe.timer.MiningTimer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

@Component
@Singleton
public class BreakableBlocks implements IBreakableBlocks {
    private final Logger logger;

    private final EntitySet<BreakableBlock> breakableBlocks;
    private final Map<UUID, Timer> timers = new HashMap<>();
    private final YukikasePlugin plugin;
    private final IPickaxes pickaxes;

    public static final int ENTITY_ID = Integer.MAX_VALUE;

    @Inject
    public BreakableBlocks(EntitySet<BreakableBlock> breakableBlocks, YukikasePlugin plugin, IPickaxes pickaxes) {
        this.breakableBlocks = breakableBlocks;
        logger = plugin.getLogger();
        this.plugin = plugin;
        this.pickaxes = pickaxes;
    }


    @Override
    public void startMining(Player player, Location location) {
        BreakableBlock breakableBlock = null;
        try {
            var blocks = breakableBlocks.query(BreakableBlock.getLocationQuery(breakableBlocks.queryBuilder(), location));
            if (!blocks.isEmpty()) breakableBlock = blocks.getFirst();
        } catch (SQLException e) {
            logger.warning("Something went wrong while trying to query BreakableBlocks: " + e.getMessage());
            return;
        }

        if (breakableBlock == null) return;

        var itemInHand = player.getInventory().getItemInMainHand();

        if (!pickaxes.isPickaxe(player, itemInHand)) return;


        var timer = this.timers.get(player.getUniqueId());
        if (timer != null) {
            timer.cancel();
            timers.remove(player.getUniqueId());
        }

        resetMiningAnimation(player, location);

        timer = new MiningTimer(breakableBlock, player, pickaxes.getDamage(player), this);

        plugin.startTimer(timer);
        timers.put(player.getUniqueId(), timer);
    }

    @Override
    public void cancelMining(Player player, Location location) {
        BreakableBlock breakableBlock = null;
        try {
            var blocks = breakableBlocks.query(BreakableBlock.getLocationQuery(breakableBlocks.queryBuilder(), location));
            if (!blocks.isEmpty()) breakableBlock = blocks.getFirst();
        } catch (SQLException e) {
            logger.warning("Something went wrong while trying to query BreakableBlocks: " + e.getMessage());
            return;
        }

        if (breakableBlock == null) return;
        var timer = this.timers.get(player.getUniqueId());
        if (timer != null) {
            timer.cancel();
            timers.remove(player.getUniqueId());
        }
        resetMiningAnimation(player, location);
    }

    @Override
    public void resetMiningAnimation(Player player, Location location) {
        sendBlockBreakStage(player, location, -1);
    }

    public void sendBlockBreakStage(Player player, Location location, int stage) {
        var packet = new BlockBreakAnimationEvent(ENTITY_ID, location, stage, player);
        plugin.writePacket(packet);
    }

    public void breakBlock(Player player, BreakableBlock block) {
        var blockType = block.getType();
        var drop = blockType.getDrop();
        var dropRange = blockType.getMaxDrop() - blockType.getMinDrop();
        int dropAmount;
        if (dropRange <= 0) {
            dropAmount = blockType.getMinDrop();
        } else {
            dropAmount = new Random().nextInt(dropRange) + blockType.getMinDrop();
        }
        var itemStacks = new ArrayList<ItemStack>();
        while (dropAmount > 0) {
            var itemStack = new ItemStack(drop, Math.min(drop.getMaxStackSize(), dropAmount));
            itemStacks.add(itemStack);
            dropAmount -= drop.getMaxStackSize();
        }

        player.give(itemStacks);

        block.getLocation().getBlock().setType(Material.STONE);
        var task = new BlockRespawnTask(block.getLocation(), blockType.getMaterial(), blockType.getRespawnTime());
        plugin.startTask(task);
    }
}
