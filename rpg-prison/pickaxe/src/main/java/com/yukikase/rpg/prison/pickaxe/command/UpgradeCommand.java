package com.yukikase.rpg.prison.pickaxe.command;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.IPickaxes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class UpgradeCommand implements ICommand {

    private final IPickaxes pickaxes;

    @Inject
    public UpgradeCommand(IPickaxes pickaxes) {
        this.pickaxes = pickaxes;
    }

    @Override
    public String name() {
        return "upgrade";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            var pickaxeItem = pickaxes.getPickaxeInInventory(player);
            var inventory = player.getInventory();
            if (pickaxeItem != null) {
                inventory.remove(pickaxeItem);
            }
            pickaxes.upgrade(player);
            inventory.addItem(pickaxes.item(player));

            return true;
        }
        return false;
    }
}
