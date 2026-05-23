package com.yukikase.rpg.prison.pickaxe.command;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static com.yukikase.lib.gui.TextHelper.text;

@Component
public class ExtractCommand implements ICommand {

    public static final NamespacedKey ENERGY_NAMESPACE = new NamespacedKey("pickaxe", "energy");

    private final IPickaxes pickaxes;

    @Inject
    public ExtractCommand(IPickaxes pickaxes) {
        this.pickaxes = pickaxes;
    }

    @Override
    public String name() {
        return "extract";
    }

    @Alias
    public int onCommand(Player player) {
        var amountExtracted = pickaxes.extractAll(player);

        giveExtractedItem(player, amountExtracted);

        return Command.SINGLE_SUCCESS;
    }

    @Alias
    public int onCommandWithArg(Player player, int amount) {
        var amountExtracted = pickaxes.extract(player, amount);

        giveExtractedItem(player, amountExtracted);

        return Command.SINGLE_SUCCESS;
    }

    private void giveExtractedItem(Player player, int amount) {
        if (amount <= 0) return;

        var extractedItem = new ItemStack(Material.LIGHT_BLUE_DYE);
        var meta = extractedItem.getItemMeta();
        meta.customName(text(String.valueOf(amount), NamedTextColor.WHITE).append(text(" Raw Energy", NamedTextColor.AQUA)));
        var data = meta.getPersistentDataContainer();
        data.set(ENERGY_NAMESPACE, PersistentDataType.INTEGER, amount);
        extractedItem.setItemMeta(meta);

        player.getInventory().addItem(extractedItem);
    }
}
