package com.yukikase.rpg.prison.pickaxe;

import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.exceptions.NoEntityFoundException;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.framework.orm.query.QueryBuilder;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeEntity;
import com.yukikase.rpg.prison.pickaxe.entity.PickaxeMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Singleton
@com.yukikase.framework.anotations.injection.Component
public class Pickaxes implements IPickaxes {
    private final EntitySet<PickaxeEntity> pickaxes;
    private final PickaxeMaterial baseMaterial;
    private final Map<UUID, PickaxeEntity> pickaxeCache;

    @Inject
    public Pickaxes(EntitySet<PickaxeEntity> pickaxes, EntitySet<PickaxeMaterial> material) {
        material.where(QueryBuilder.equals("isDefault", true));
        this.baseMaterial = material.next();

        this.pickaxeCache = new HashMap<>();
        this.pickaxes = pickaxes;
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
        var experience = pickaxe.experience();
        var level = pickaxe.level();

        experience += amount;

        var nextLevel = PickaxeLevel.getLevel(level + 1);
        while (nextLevel != null) {
            if (nextLevel.level() > pickaxe.material().maxLevel()) {
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

        pickaxe.experience(experience);
        pickaxe.level(level);

        pickaxes.update(pickaxe);
    }

    @Override
    public int getDamage(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.level();
        var material = pickaxe.material();

        var baseDamage = material.baseDamage();
        var damagePerLevel = material.damagePerLevel();

        return baseDamage + damagePerLevel * level;
    }

    @Override
    public boolean isPickaxe(Player player, ItemStack item) {
        var pickaxe = getPickaxe(player.getUniqueId());

        var meta = item.getItemMeta();

        return Objects.equals(meta.customName(), Component.text(player.getName() + "'s pickaxe"));
    }

    @Override
    public ItemStack item(Player player) {
        var pickaxe = getPickaxe(player.getUniqueId());
        var level = pickaxe.level();
        var experience = pickaxe.experience();
        var material = pickaxe.material();

        var item = new ItemStack(material.material());
        var meta = item.getItemMeta();
        meta.customName(Component.text(player.getName() + "'s pickaxe").color(NamedTextColor.AQUA));
        var lore = meta.lore();
        assert lore != null;
        lore.add(Component.text("owner: " + player.getName()));
        lore.add(Component.text("level: " + level));
        lore.add(Component.text("experience: " + experience + "/" + PickaxeLevel.getLevel(level + 1).requiredExperience()));
        meta.lore(lore);
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
}
