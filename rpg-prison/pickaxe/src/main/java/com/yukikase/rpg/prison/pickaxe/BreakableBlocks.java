package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.packet.event.BlockBreakAnimationEvent;
import com.yukikase.lib.task.Timer;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlock;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockDrops;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockType;
import com.yukikase.rpg.prison.pickaxe.timer.BlockRespawnTask;
import com.yukikase.rpg.prison.pickaxe.timer.MiningTimer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static com.yukikase.lib.gui.TextHelper.bold;
import static com.yukikase.lib.gui.TextHelper.text;

@Component
@Singleton
public class BreakableBlocks implements IBreakableBlocks {
    private final Logger logger;

    private final EntitySet<BreakableBlockType> breakableBlockTypes;
    private final EntitySet<BreakableBlockDrops> breakableBlockDrops;
    private final Map<UUID, Timer> timers = new HashMap<>();
    private final YukikasePlugin plugin;
    private final IPickaxes pickaxes;

    public static final int ENTITY_ID = Integer.MAX_VALUE;

    @Inject
    public BreakableBlocks(YukikasePlugin plugin, EntitySet<BreakableBlockType> breakableBlockTypes, EntitySet<BreakableBlockDrops> breakableBlockDrops, IPickaxes pickaxes) {
        logger = plugin.getLogger();
        this.plugin = plugin;
        this.breakableBlockTypes = breakableBlockTypes;
        this.breakableBlockDrops = breakableBlockDrops;
        this.pickaxes = pickaxes;
    }


    @Override
    public void startMining(Player player, Location location) {

        if (pickaxes.isEnergyFull(player)) {
            player.showTitle(Title.title(bold("Energy full!!!", NamedTextColor.RED), text("Please extract your energy or enchant.", NamedTextColor.YELLOW)));
            return;
        }

        var block = location.getBlock();
        var blockType = breakableBlockTypes.get(block.getType().toString());

        if (blockType == null) return;

        var breakableBlock = new BreakableBlock(location, blockType);

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
        var blockType = block.type();
        pickaxes.addExperience(player, blockType.experience());
        pickaxes.addEnergy(player, blockType.energy());
        pickaxes.updateLore(player);

        List<BreakableBlockDrops> drops = new ArrayList<>();
        try {
            drops = breakableBlockDrops.queryBuilder().where().eq("breakableBlockType_id", block.type().material()).query();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        for (var drop : drops) {
            var random = new SecureRandom().nextInt(100);
            if (random <= drop.dropChance()) {
                var dropRange = drop.maxAmount() - drop.minAmount();
                int dropAmount;
                if (dropRange <= 0) {
                    dropAmount = drop.minAmount();
                } else {
                    dropAmount = new SecureRandom().nextInt(dropRange) + drop.minAmount();
                }

                var itemStacks = new ArrayList<ItemStack>();
                while (dropAmount > 0) {
                    var itemstack = new ItemStack(drop.drop(), Math.min(drop.drop().getMaxStackSize(), dropAmount));
                    itemStacks.add(itemstack);
                    dropAmount -= drop.drop().getMaxStackSize();
                }

                player.getInventory().addItem(itemStacks.toArray(new ItemStack[0]));
                //TODO: tell the player if his inventory is full
            }
        }


        player.playSound(block.location(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        block.location().getBlock().setType(blockType.respawnMaterial());
        var task = new BlockRespawnTask(block.location(), blockType.material(), blockType.respawnTime());
        plugin.startTask(task);
    }
}
