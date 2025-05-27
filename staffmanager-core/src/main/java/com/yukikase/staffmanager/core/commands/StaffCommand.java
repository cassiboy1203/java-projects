package com.yukikase.staffmanager.core.commands;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;
import com.yukikase.lib.annotations.Permission;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.staffmanager.core.staffmode.IStaffMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Component
@Permission(IStaffMode.PERMISSION)
public class StaffCommand implements ICommand {
    public static final String NAME = "staff";

    private final IStaffMode staffMode;

    @Inject
    public StaffCommand(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player player) {
            return staffMode.toggleStaffMode(player);
        }

        return false;
    }
}
