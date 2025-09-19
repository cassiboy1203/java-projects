package com.yukikase.lib.interfaces;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.yukikase.lib.permission.Permission;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.List;

public interface ICommand {
    String name();

    default Permission permission() {
        return null;
    }

    default List<LiteralCommandNode<CommandSourceStack>> overrideCommandNode() {
        return null;
    }
}
