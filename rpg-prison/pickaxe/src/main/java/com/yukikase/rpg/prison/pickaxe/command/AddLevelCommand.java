package com.yukikase.rpg.prison.pickaxe.command;

import com.mojang.brigadier.Command;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.annotations.command.Alias;
import com.yukikase.lib.annotations.command.Argument;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import org.bukkit.entity.Player;

@Component
public class AddLevelCommand implements ICommand {

    private final IPickaxes pickaxes;

    @Inject
    public AddLevelCommand(IPickaxes pickaxes) {
        this.pickaxes = pickaxes;
    }

    @Override
    public String name() {
        return "addlevel";
    }

    @Alias
    public int onCommand(Player player, @Argument("level") int level) {
        var pickaxeItem = pickaxes.getPickaxeInInventory(player);
        var inventory = player.getInventory();
        if (pickaxeItem != null) {
            inventory.remove(pickaxeItem);
        }
        pickaxes.addLevel(player, level);
        inventory.addItem(pickaxes.item(player));

        return Command.SINGLE_SUCCESS;
    }
}
