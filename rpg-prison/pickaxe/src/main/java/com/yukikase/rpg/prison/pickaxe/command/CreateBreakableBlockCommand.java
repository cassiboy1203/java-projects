package com.yukikase.rpg.prison.pickaxe.command;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlock;
import com.yukikase.rpg.prison.pickaxe.entity.BreakableBlockType;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
public class CreateBreakableBlockCommand implements ICommand {

    private final EntitySet<BreakableBlock> breakableBlocks;
    private final EntitySet<BreakableBlockType> breakableBlockTypes;

    @Inject
    public CreateBreakableBlockCommand(EntitySet<BreakableBlock> breakableBlocks, EntitySet<BreakableBlockType> breakableBlockTypes) {
        this.breakableBlocks = breakableBlocks;
        this.breakableBlockTypes = breakableBlockTypes;
    }

    @Override
    public String name() {
        return "createbreakableblock";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            var location = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            var type = breakableBlockTypes.get(args[3]);
            var block = new BreakableBlock(location, type);

            breakableBlocks.add(block);
            return true;
        }
        return false;
    }
}
