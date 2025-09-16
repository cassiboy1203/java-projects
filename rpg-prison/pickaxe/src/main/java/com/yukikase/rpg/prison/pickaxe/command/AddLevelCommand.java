package com.yukikase.rpg.prison.pickaxe.command;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import org.bukkit.command.CommandSender;
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

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            var levelToAdd = Integer.parseInt(args[0]);

            var pickaxeItem = pickaxes.getPickaxeInInventory(player);
            var inventory = player.getInventory();
            if (pickaxeItem != null) {
                inventory.remove(pickaxeItem);
            }
            pickaxes.addLevel(player, levelToAdd);
            inventory.addItem(pickaxes.item(player));

            return true;
        }
        return false;
    }
}
