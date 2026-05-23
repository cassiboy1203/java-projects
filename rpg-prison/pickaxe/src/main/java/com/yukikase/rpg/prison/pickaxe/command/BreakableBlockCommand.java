package com.yukikase.rpg.prison.pickaxe.command;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.gui.MessageHandler;
import com.yukikase.lib.gui.chest.ChestGuiBuilder;
import com.yukikase.lib.gui.chest.PagedChestGuiBuilder;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.Permission;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockDrops;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.yukikase.lib.gui.TextHelper.bold;
import static com.yukikase.lib.gui.TextHelper.text;

@com.yukikase.framework.anotations.injection.Component
public class BreakableBlockCommand implements ICommand {
    private static final ItemStack WAND = new ItemStack(Material.IRON_AXE);
    public static final NamespacedKey WAND_KEY = new NamespacedKey("pickaxe", "wand");

    static {
        var meta = WAND.getItemMeta();
        meta.customName(bold("Wand", NamedTextColor.DARK_GRAY));
        var data = meta.getPersistentDataContainer();
        data.set(WAND_KEY, PersistentDataType.STRING, WAND_KEY.getKey());
        WAND.setItemMeta(meta);
    }

    @Override
    public Permission permission() {
        return ICommand.super.permission();
    }

    private final EntitySet<BreakableBlockType> breakableBlockTypes;
    private final EntitySet<BreakableBlockDrops> breakableBlockDrops;

    private final List<BreakableBlockType> removedBlockTypes = new ArrayList<>();
    private final List<BreakableBlockType> addedBlockTypes = new ArrayList<>();

    @Inject
    public BreakableBlockCommand(EntitySet<BreakableBlockType> breakableBlockTypes, EntitySet<BreakableBlockDrops> breakableBlockDrops, YukikasePlugin plugin) {
        this.breakableBlockTypes = breakableBlockTypes;
        this.breakableBlockDrops = breakableBlockDrops;
    }

    @Override
    public String name() {
        return "breakableblock";
    }

    @Alias(subcommand = "menu")
    @Alias(alias = "bb", subcommand = "menu")
    public int onMenu(Player player) {
        openMenu(player);
        return Command.SINGLE_SUCCESS;
    }

    private void openMenu(Player player) {
        var menu = new PagedChestGuiBuilder(6, Component.text("Breakable blocks", NamedTextColor.GOLD), player);
        var confirmItem = new ItemStack(Material.EMERALD);
        var confirmMeta = confirmItem.getItemMeta();
        confirmMeta.customName(text("Confirm", NamedTextColor.GREEN));
        confirmItem.setItemMeta(confirmMeta);

        menu.addGenericItem(49, confirmItem, this::onConfirmChange);

        for (var breakableBlock : breakableBlockTypes.getAll()) {
            var item = new ItemStack(breakableBlock.material());
            var meta = item.getItemMeta();
            var lore = new ArrayList<Component>();

            lore.add(text("Left click", NamedTextColor.YELLOW).append(text(" to ").append(text("edit", NamedTextColor.AQUA))));
            lore.add(text("Right click", NamedTextColor.YELLOW).append(text(" to ").append(text("remove", NamedTextColor.RED))));

            meta.lore(lore);

            item.setItemMeta(meta);

            menu.addItem(item, (p, e) -> {
                onBreakableBlockType(p, e, breakableBlock);
            });
        }

        menu.open(player);
    }

    private void onConfirmChange(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        for (var removed : removedBlockTypes) {
            breakableBlockTypes.delete(removed);
        }
        player.closeInventory();
    }

    private void onBreakableBlockType(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        switch (event.getClick()) {
            case LEFT -> onEdit(player, breakableBlockType);
            case RIGHT -> onRemove(event, breakableBlockType);
        }
        System.out.println(event);
        event.setCancelled(true);
    }

    private void onEdit(Player player, BreakableBlockType breakableBlockType) {
        var menu = new ChestGuiBuilder(4, text("Block editor", NamedTextColor.YELLOW), player);

        var dropMaterial = Material.STONE;
        try {
            var drop1 = breakableBlockDrops.queryBuilder().limit(1L).where().eq("breakableBlockType_id", breakableBlockType.material().toString()).query();
            if (!drop1.isEmpty()) {
                dropMaterial = drop1.getFirst().drop();
            }
        } catch (SQLException ignored) {
        }

        menu.addItem(4, new ItemStack(breakableBlockType.material()), ChestGuiBuilder.DUMMY_EXECUTOR);

        var dropItem = new ItemStack(dropMaterial);
        var meta = dropItem.getItemMeta();
        meta.customName(text("Drops", NamedTextColor.GREEN));
        dropItem.setItemMeta(meta);

        var respawnMaterialItem = new ItemStack(breakableBlockType.material());
        var respawnMeta = respawnMaterialItem.getItemMeta();
        respawnMeta.customName(text("Respawn material", NamedTextColor.GREEN));
        respawnMaterialItem.setItemMeta(respawnMeta);

        var hardnessItem = new ItemStack(Material.ANVIL);
        var hardnessMeta = hardnessItem.getItemMeta();
        hardnessMeta.customName(text("Hardness", NamedTextColor.DARK_GRAY));
        hardnessItem.setItemMeta(hardnessMeta);

        var resistanceItem = new ItemStack(Material.DIAMOND_PICKAXE);
        var resistanceMeta = resistanceItem.getItemMeta();
        resistanceMeta.customName(text("Resistance", NamedTextColor.DARK_GRAY));
        resistanceItem.setItemMeta(resistanceMeta);

        var respawnItem = new ItemStack(Material.CLOCK);
        var respawnItemMeta = respawnItem.getItemMeta();
        respawnItemMeta.customName(text("Respawn time", NamedTextColor.AQUA));
        respawnItem.setItemMeta(respawnItemMeta);

        var experienceItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        var experienceMeta = experienceItem.getItemMeta();
        experienceMeta.customName(text("Experience", NamedTextColor.GREEN));
        experienceItem.setItemMeta(experienceMeta);

        var energyItem = new ItemStack(Material.BEACON);
        var energyMeta = energyItem.getItemMeta();
        energyMeta.customName(text("Energy", NamedTextColor.GREEN));
        energyItem.setItemMeta(energyMeta);

        var wandItem = new ItemStack(Material.IRON_AXE);
        var wandMeta = wandItem.getItemMeta();
        wandMeta.customName(bold("Wand", NamedTextColor.DARK_GRAY));
        wandItem.setItemMeta(wandMeta);

        menu.addItem(10, dropItem, (p, e) -> onDropItem(p, e, breakableBlockType));
        menu.addItem(12, hardnessItem, (p, e) -> onHardnessItem(p, e, breakableBlockType));
        menu.addItem(14, respawnItem, (p, e) -> onRespawnItem(p, e, breakableBlockType));
        menu.addItem(16, experienceItem, (p, e) -> onExperienceItem(p, e, breakableBlockType));
        menu.addItem(19, respawnMaterialItem, (p, e) -> onRespawnMaterial(p, e, breakableBlockType));
        menu.addItem(21, resistanceItem, (p, e) -> onResistanceItem(p, e, breakableBlockType));
        menu.addItem(23, wandItem, (p, e) -> onWandItem(p, e, breakableBlockType));
        menu.addItem(25, energyItem, (p, e) -> onEnergyItem(p, e, breakableBlockType));

        menu.fill();

        menu.open(player);
    }

    private void onRemove(InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        removedBlockTypes.add(breakableBlockType);
        event.getView().getTopInventory().clear(event.getSlot());
    }

    private void onDropItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        // TODO: add menu
    }

    private void onHardnessItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);

        player.closeInventory();
        player.sendMessage(text("Please enter the hardness. Type ", NamedTextColor.YELLOW).append(text("Cancel", NamedTextColor.RED).append(text(" to cancel.", NamedTextColor.YELLOW))));

        MessageHandler.handleNextMessage(player, m -> handleHardnessMessage(m, player, breakableBlockType));
    }

    private void handleHardnessMessage(String message, Player player, BreakableBlockType breakableBlockType) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(text("Cancelled", NamedTextColor.YELLOW));
            return;
        }

        try {
            var hardness = Integer.parseInt(message);
            breakableBlockType.hardness(hardness);
            breakableBlockTypes.update(breakableBlockType);

        } catch (NumberFormatException e) {
            player.sendMessage(text("Invalid hardness. Try again", NamedTextColor.RED));
            MessageHandler.handleNextMessage(player, m -> handleHardnessMessage(m, player, breakableBlockType));
        }
    }

    private void onRespawnItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);

        player.closeInventory();
        player.sendMessage(text("Please enter the respawn time in seconds. Type ", NamedTextColor.YELLOW).append(text("Cancel", NamedTextColor.RED).append(text(" to cancel.", NamedTextColor.YELLOW))));

        MessageHandler.handleNextMessage(player, m -> handleRespawnItemMessage(m, player, breakableBlockType));
    }

    private void handleRespawnItemMessage(String message, Player player, BreakableBlockType breakableBlockType) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(text("Cancelled", NamedTextColor.YELLOW));
            return;
        }

        try {
            var respawnTime = Integer.parseInt(message);
            breakableBlockType.respawnTime(respawnTime);
            breakableBlockTypes.update(breakableBlockType);
        } catch (NumberFormatException e) {
            player.sendMessage(text("Invalid respawn time. Try again.", NamedTextColor.RED));
            MessageHandler.handleNextMessage(player, m -> handleRespawnItemMessage(m, player, breakableBlockType));
        }
    }

    private void onExperienceItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);
        player.closeInventory();

        player.sendMessage(text("Please enter the experience. Type ", NamedTextColor.YELLOW).append(text("Cancel", NamedTextColor.RED)).append(text(" to cancel.", NamedTextColor.YELLOW)));

        MessageHandler.handleNextMessage(player, m -> handleExperienceMessage(m, player, breakableBlockType));
    }

    private void handleExperienceMessage(String message, Player player, BreakableBlockType breakableBlockType) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(text("Cancelled", NamedTextColor.YELLOW));
            return;
        }

        try {
            var experience = Integer.parseInt(message);
            breakableBlockType.experience(experience);
            breakableBlockTypes.update(breakableBlockType);
        } catch (NumberFormatException e) {
            player.sendMessage(text("Invalid experience. Try again.", NamedTextColor.RED));
            MessageHandler.handleNextMessage(player, m -> handleExperienceMessage(m, player, breakableBlockType));
        }
    }

    private void onRespawnMaterial(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        // TODO: add menu or text
    }

    private void onResistanceItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);
        player.closeInventory();

        player.sendMessage(text("Please enter the resistance. Type ", NamedTextColor.YELLOW).append(text("Cancel", NamedTextColor.RED)).append(text(" to cancel.", NamedTextColor.YELLOW)));
        MessageHandler.handleNextMessage(player, m -> handleResistanceMessage(m, player, breakableBlockType));
    }

    private void handleResistanceMessage(String message, Player player, BreakableBlockType breakableBlockType) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(text("Cancelled", NamedTextColor.YELLOW));
            return;
        }

        try {
            var resistance = Integer.parseInt(message);
            breakableBlockType.resistance(resistance);
            breakableBlockTypes.update(breakableBlockType);
        } catch (NumberFormatException e) {
            player.sendMessage(text("Invalid resistance. Try again.", NamedTextColor.RED));
            MessageHandler.handleNextMessage(player, m -> handleResistanceMessage(m, player, breakableBlockType));
        }
    }

    private void onWandItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);
        player.give(WAND);
    }

    private void onEnergyItem(Player player, InventoryClickEvent event, BreakableBlockType breakableBlockType) {
        event.setCancelled(true);
        player.closeInventory();

        player.sendMessage(text("Please enter the energy. Type ", NamedTextColor.YELLOW).append(text("Cancel", NamedTextColor.RED).append(text(" to cancel.", NamedTextColor.YELLOW))));
        MessageHandler.handleNextMessage(player, m -> handleEnergyMessage(m, player, breakableBlockType));
    }

    private void handleEnergyMessage(String message, Player player, BreakableBlockType breakableBlockType) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(text("Cancelled", NamedTextColor.YELLOW));
            return;
        }

        try {
            var energy = Integer.parseInt(message);
            breakableBlockType.energy(energy);
            breakableBlockTypes.update(breakableBlockType);
        } catch (NumberFormatException e) {
            player.sendMessage(text("Invalid energy. Try again.", NamedTextColor.RED));
            MessageHandler.handleNextMessage(player, m -> handleEnergyMessage(m, player, breakableBlockType));
        }
    }
}
