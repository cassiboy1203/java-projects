package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.exceptions.NoEntityFoundException;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeEntity;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
@com.yukikase.framework.anotations.injection.Component
public class Pickaxes implements IPickaxes {
    private final EntitySet<PickaxeEntity> pickaxes;
    private final PickaxeMaterial baseMaterial;
    private final EntitySet<PickaxeMaterial> materials;
    private final Map<UUID, PickaxeEntity> pickaxeCache;
    private final YukikasePlugin plugin;
    private final NamespacedKey key;

    private final Logger logger;

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
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "pickaxe");
        this.materials = material;
        this.logger = plugin.getLogger();
    }

    private PickaxeEntity getPickaxe(UUID owner) {
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
        var experience = pickaxe.getExperience();
        var level = pickaxe.getLevel();

        experience += amount;

        var nextLevel = PickaxeLevel.getLevel(level + 1);
        while (nextLevel != null) {
            if (nextLevel.level() > pickaxe.getMaterial().getMaxLevel()) {
                break;
            }

            if (experience >= nextLevel.requiredExperience()) {
                experience -= nextLevel.requiredExperience();
                level += 1;
                nextLevel = PickaxeLevel.getLevel(level + 1);
            } else {
                break;
            }
        }

        pickaxe.setExperience(experience);
        pickaxe.setLevel(level);

        pickaxes.update(pickaxe);
    }

    @Override
    public int getDamage(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.getLevel();
        var material = pickaxe.getMaterial();

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
        var level = pickaxe.getLevel();
        var experience = pickaxe.getExperience();
        var material = pickaxe.getMaterial().getMaterial();
        var nextLevel = PickaxeLevel.getLevel(level + 1);
        var requiredExperience = nextLevel == null ? 0 : nextLevel.requiredExperience();

        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.customName(Component.text(player.getName() + "'s pickaxe").color(NamedTextColor.AQUA));
        var lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(Component.text("owner: " + player.getName()));
        lore.add(Component.text("level: " + level));
        lore.add(Component.text("experience: " + experience + "/" + requiredExperience));
        meta.lore(lore);
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
        var newLevel = pickaxe.getLevel() + level;
        if (newLevel < 1) {
            newLevel = 1;
        } else if (newLevel > pickaxe.getMaterial().getMaxLevel()) {
            newLevel = pickaxe.getMaterial().getMaxLevel();
        }

        pickaxe.setLevel(newLevel);
        pickaxes.update(pickaxe);
    }

    @Override
    public void upgrade(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        if (!isMaxLevel(player)) return;

        PickaxeMaterial nextMaterial;
        nextMaterial = materials.get(pickaxe.getMaterial().getNextMaterial().getName());
        if (nextMaterial == null) return;
        pickaxe.setMaterial(nextMaterial);
        pickaxe.setLevel(1);
        pickaxe.setExperience(0);
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
        var level = pickaxe.getLevel();
        return level >= pickaxe.getMaterial().getMaxLevel();
    }

    private void updateLore(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var item = getPickaxeInInventory(player);
        var meta = item.getItemMeta();
        var lore = new ArrayList<Component>();

        lore.add(Component.text("owner: " + player.getName()));
        lore.add(Component.text("level: " + pickaxe.getLevel()));
        lore.add(Component.text("experience: " + pickaxe.getExperience()));

        meta.lore(lore);
        item.setItemMeta(meta);
    }
}
