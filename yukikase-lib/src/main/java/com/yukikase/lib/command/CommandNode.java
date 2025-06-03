package com.yukikase.lib.command;

import com.yukikase.lib.exceptions.InvalidCommandException;
import com.yukikase.lib.permission.Permission;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandNode {
    final Map<String, CommandNode> children;
    private final String command;
    private final String subcommand;
    private final Permission permission;
    private final Method method;
    private final boolean isRoot;
    private int depth;

    public CommandNode(String command, String subcommand, Permission permission, Method method) {
        this(command, subcommand, permission, method, false);
    }

    public CommandNode(String command, String subcommand, Permission permission, Method method, boolean isRoot) {
        this.command = command;
        this.subcommand = subcommand;
        this.permission = permission;
        this.method = method;
        this.isRoot = isRoot;
        this.children = new HashMap<>();
        this.depth = -1;
    }

    public int getDepth() {
        return isRoot ? 0 : depth;
    }

    private void setDepth(int depth) {
        this.depth = depth;
    }

    public Method getMethod() {
        return method;
    }

    public String getSubcommand() {
        return subcommand;
    }

    public String getCommand() {
        return command;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getName(boolean calledFromRoot) {
        if (calledFromRoot)
            return command;
        return subcommand;
    }

    public void addChild(CommandNode child) {
        if (this.equals(child))
            return;

        child.setDepth(depth + 1);
        children.put(child.getName(isRoot), child);
    }

    public CommandNode getNodeToExecute(String alias, String[] args) {
        if (children.isEmpty())
            return this;

        if (isRoot) {
            var child = children.getOrDefault(alias, null);
            if (child != null)
                return child.getNodeToExecute(alias, args);

            throw new InvalidCommandException(String.format(InvalidCommandException.COMMAND_NOT_FOUND, alias));
        }

        if (args.length == 0)
            return this;

        var child = children.getOrDefault(args[0], null);
        return child != null ? child.getNodeToExecute(alias, Arrays.copyOfRange(args, 1, args.length)) : this;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CommandNode that)) return false;

        return command.equals(that.command) && subcommand.equals(that.subcommand) && Objects.equals(method, that.method);
    }

    @Override
    public String toString() {
        return "CommandNode{" +
                "command='" + command + '\'' +
                ", subcommand='" + subcommand + '\'' +
                ", isRoot=" + isRoot +
                ", depth=" + depth +
                '}';
    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + subcommand.hashCode();
        result = 31 * result + Objects.hashCode(method);
        return result;
    }
}
