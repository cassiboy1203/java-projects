package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.exceptions.NoEntityFoundException;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.gui.scoreboard.ScoreBoardGui;
import com.yukikase.lib.gui.scoreboard.ScoreBoardHandler;
import com.yukikase.rpg.prison.core.EnergyLevel;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeEntity;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Criteria;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.yukikase.lib.gui.TextHelper.*;

@Singleton
@com.yukikase.framework.anotations.injection.Component
public class Pickaxes implements IPickaxes {
    private final EntitySet<PickaxeEntity> pickaxes;
    private final PickaxeMaterial baseMaterial;
    private final EntitySet<PickaxeMaterial> materials;
    private final Map<UUID, PickaxeEntity> pickaxeCache;
    private final NamespacedKey key;

    @Inject
    public Pickaxes(EntitySet<PickaxeEntity> pickaxes, EntitySet<PickaxeMaterial> material, YukikasePlugin plugin) {
        try {
            var query = material.queryBuilder().where().eq("isDefault", "true").prepare();
            this.baseMaterial = material.query(query).getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.pickaxeCache = new HashMap<>();
        this.pickaxes = pickaxes;
        this.key = new NamespacedKey(plugin, "pickaxe");
        this.materials = material;
    }

    @Override
    public PickaxeEntity getPickaxe(UUID owner) {
        PickaxeEntity pickaxe = pickaxeCache.get(owner);
        if (pickaxe == null) {
            try {
                pickaxe = pickaxes.get(owner);
            } catch (NoEntityFoundException ignored) {
            }
            if (pickaxe == null) {
                pickaxe = createPickaxe(owner);
            }
            pickaxeCache.put(owner, pickaxe);
        }

        return pickaxe;
    }

    private PickaxeEntity createPickaxe(UUID owner) {
        var pickaxe = new PickaxeEntity(owner, 1, 0, baseMaterial);
        pickaxes.add(pickaxe);
        return pickaxe;
    }

    @Override
    public void removeFromCache(Player owner) {
        var pickaxe = pickaxeCache.remove(owner.getUniqueId());
        pickaxes.update(pickaxe);
    }

    @Override
    public void clearCache() {
        for (var pickaxe : pickaxeCache.values()) {
            pickaxes.update(pickaxe);
        }

        pickaxeCache.clear();
    }

    @Override
    public void addExperience(Player owner, int amount) {
        var pickaxe = getPickaxe(owner.getUniqueId());
        var experience = pickaxe.experience();
        var level = pickaxe.level();


        experience += amount;

        var nextLevel = PickaxeLevel.getLevel(level + 1);
        if (isMaxLevel(owner) && !isMaxTier(owner)) {
            if (pickaxe.experience() >= nextLevel.requiredExperience()) {
                owner.showTitle(Title.title(bold("Max xp reached!", NamedTextColor.RED), text("Please upgrade your pickaxe to continue gaining levels.", NamedTextColor.YELLOW)));
                return;
            }
        }
        while (nextLevel != null) {
            if (nextLevel.level() > pickaxe.material().getMaxLevel()) {
                break;
            }

            if (experience >= nextLevel.requiredExperience()) {
                level += 1;
                owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                nextLevel = PickaxeLevel.getLevel(level + 1);
            } else {
                break;
            }
        }

        pickaxe.experience(experience);
        pickaxe.level(level);

        pickaxes.update(pickaxe);
    }

    @Override
    public void addEnergy(Player owner, int amount) {
        var pickaxe = getPickaxe(owner.getUniqueId());
        var energy = pickaxe.energy();
        var energyLevel = pickaxe.energyLevel();

        energy += amount;

        var nextLevel = EnergyLevel.getLevel(energyLevel + 1);
        var max = nextLevel.requiredExperience();

        pickaxe.energy((int) Math.min(energy, max));

        pickaxes.update(pickaxe);
    }

    @Override
    public int getDamage(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.level();
        var material = pickaxe.material();

        var baseDamage = material.getBaseDamage();
        var damagePerLevel = material.getDamagePerLevel();

        return baseDamage + damagePerLevel * level;
    }

    @Override
    public boolean isPickaxe(Player player, ItemStack item) {
        if (item == null) return false;

        var meta = item.getItemMeta();
        if (meta == null) return false;

        var data = meta.getPersistentDataContainer();
        if (data.has(key, PersistentDataType.STRING)) {
            var value = data.get(key, PersistentDataType.STRING);
            return value != null && value.equals(player.getUniqueId().toString());
        }

        return false;
    }

    @Override
    public ItemStack item(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var material = pickaxe.material().getMaterial();
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        updateLore(item, player);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, player.getUniqueId().toString());
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean hasPickaxe(Player player) {
        var inventory = player.getInventory();
        for (var item : inventory.getContents()) {
            if (item != null && isPickaxe(player, item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addLevel(Player player, int level) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var newLevel = pickaxe.level() + level;
        if (newLevel < 1) {
            newLevel = 1;
        } else if (newLevel > pickaxe.material().getMaxLevel()) {
            newLevel = pickaxe.material().getMaxLevel();
        }

        pickaxe.level(newLevel);
        pickaxes.update(pickaxe);
    }

    @Override
    public void upgrade(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        if (!isMaxLevel(player)) return;

        PickaxeMaterial nextMaterial;
        nextMaterial = materials.get(pickaxe.material().getNextMaterial().getName());
        if (nextMaterial == null) return;
        pickaxe.material(nextMaterial);
        pickaxes.update(pickaxe);
    }

    @Override
    public ItemStack getPickaxeInInventory(Player player) {
        ItemStack pickaxeItem = null;
        var inventory = player.getInventory();
        for (var item : inventory.getContents()) {
            if (isPickaxe(player, item)) {
                pickaxeItem = item;
            }
        }
        return pickaxeItem;
    }

    private boolean isMaxLevel(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.level();
        return level >= pickaxe.material().getMaxLevel();
    }

    private boolean isMaxTier(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        return pickaxe.material().getNextMaterial() == null;
    }

    @Override
    public void updateLore(Player player) {
        var item = getPickaxeInInventory(player);
        updateLore(item, player);
        updateScoreboard(player);
    }

    @Override
    public boolean isEnergyFull(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var energy = pickaxe.energy();
        var energyLevel = pickaxe.energyLevel();
        var nextLevel = EnergyLevel.getLevel(energyLevel + 1);
        var max = nextLevel.requiredExperience();

        return energy >= max;
    }

    @Override
    public int extract(Player player, int amount) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var energy = pickaxe.energy();
        var result = 0;
        if (amount >= energy) {
            pickaxe.energy(0);
            result = energy;
        } else {
            pickaxe.energy(energy - amount);
            result = amount;
        }
        pickaxes.update(pickaxe);
        updateLore(player);
        return result;
    }

    @Override
    public int extractAll(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var energy = pickaxe.energy();
        return extract(player, energy);
    }

    @Override
    public ScoreBoardGui createScoreboard(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var xp = pickaxe.experience();
        var level = pickaxe.level();
        var nextLevel = PickaxeLevel.getLevel(level + 1);
        var xpTillNext = nextLevel.requiredExperience() - xp;
        var energy = pickaxe.energy();
        var nextEnergy = EnergyLevel.getLevel(pickaxe.energyLevel() + 1);

        var scoreboard = new ScoreBoardGui(SCOREBOARD_KEY, Criteria.DUMMY, bold("Mining info", NamedTextColor.AQUA));
        scoreboard.addLine("mining_lvl_text", bold("Mining level", NamedTextColor.YELLOW));
        scoreboard.addLine("mining_lvl", text(String.valueOf(level), NamedTextColor.WHITE));
        scoreboard.addLine("total_xp_text", bold("XP", NamedTextColor.GREEN));
        scoreboard.addLine("total_xp", text(String.valueOf(xp), NamedTextColor.WHITE));
        scoreboard.addLine("xp_till_next_text", bold("XP until next level", NamedTextColor.GOLD));
        scoreboard.addLine("xp_till_next", text(String.valueOf(xpTillNext), NamedTextColor.WHITE));
        scoreboard.addLine("xp_till_next_bar", progressbar(xp, xpTillNext, PickaxeLevel.getLevel(level).requiredExperience()));
        scoreboard.addLine("energy_text", bold("Energy", NamedTextColor.AQUA));
        scoreboard.addLine("energy_bar", progressbar(energy, nextEnergy.requiredExperience()));
        scoreboard.addLine("energy_progress", progressBarNumber(energy, nextEnergy.requiredExperience()));
        scoreboard.build();

        ScoreBoardHandler.register(SCOREBOARD_KEY, scoreboard);
        return scoreboard;
    }

    private void updateScoreboard(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.level();
        var experience = pickaxe.experience();
        var currentLevel = PickaxeLevel.getLevel(level);
        var nextLevel = PickaxeLevel.getLevel(level + 1);
        var nextlevelExperience = nextLevel == null ? currentLevel.requiredExperience() : nextLevel.requiredExperience();
        var xpTillNext = Math.max(nextlevelExperience - experience, 0);
        var energy = pickaxe.energy();
        var energyLevel = EnergyLevel.getLevel(pickaxe.energyLevel() + 1);

        var scoreboard = ScoreBoardHandler.get(SCOREBOARD_KEY);
        if (scoreboard != null) {
            scoreboard.updateLine("mining_lvl", text(String.valueOf(level), NamedTextColor.WHITE));
            scoreboard.updateLine("total_xp", text(String.valueOf(experience), NamedTextColor.WHITE));
            scoreboard.updateLine("xp_till_next", text(String.valueOf(xpTillNext), NamedTextColor.WHITE));
            scoreboard.updateLine("xp_till_next_bar", progressbar(experience, nextlevelExperience, currentLevel.requiredExperience()));
            scoreboard.updateLine("energy_bar", progressbar(energy, energyLevel.requiredExperience()));
            scoreboard.updateLine("energy_progress", progressBarNumber(energy, energyLevel.requiredExperience()));
        }
    }

    private void updateLore(ItemStack item, Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var meta = item.getItemMeta();

        meta.customName(text(player.getName() + "'s pickaxe", NamedTextColor.AQUA).append(text(" " + pickaxe.energyLevel(), NamedTextColor.GREEN)));

        var lore = new ArrayList<Component>();

        var nextLevel = PickaxeLevel.getLevel(pickaxe.level() + 1);
        var currentLevel = PickaxeLevel.getLevel(pickaxe.level());
        var energyLevel = EnergyLevel.getLevel(pickaxe.energyLevel() + 1);

        var nextlevelExperience = nextLevel == null ? currentLevel.requiredExperience() : nextLevel.requiredExperience();

        lore.add(bold("Mining level: ", NamedTextColor.AQUA).append(text(String.valueOf(pickaxe.level()), NamedTextColor.GREEN)));
        lore.add(progressbar(pickaxe.experience(), nextlevelExperience, currentLevel.requiredExperience()));
        lore.add(progressBarNumber(pickaxe.experience(), nextlevelExperience));
        lore.add(text(""));
        lore.add(bold("Energy", NamedTextColor.AQUA));
        lore.add(progressbar(pickaxe.energy(), energyLevel.requiredExperience()));
        lore.add(progressBarNumber(pickaxe.energy(), energyLevel.requiredExperience()));

        meta.lore(lore);
        item.setItemMeta(meta);
    }
}
