package com.yukikase.lib.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yukikase.lib.annotations.command.Argument;
import com.yukikase.lib.interfaces.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandNode {
    private final String name;
    private Method method;
    private final Map<String, CommandNode> children;
    private SenderType senderType;
    private String permission;

    public CommandNode(String name) {
        this.name = name;
        this.children = new HashMap<>();
    }

    public CommandNode getOrCreateChild(String name) {
        return children.computeIfAbsent(name, CommandNode::new);
    }

    public String name() {
        return name;
    }

    public Method method() {
        return method;
    }

    public void method(Method method) {
        this.method = method;
    }

    public void permission(String permission) {
        this.permission = permission;
    }

    public Collection<CommandNode> children() {
        return children.values();
    }

    public LiteralArgumentBuilder<CommandSourceStack> toPaperCommandNode(ICommand command) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var root = Commands.literal(name);
        for (var child : children.values()) {
            root.then(child.toPaperCommandNode(command));
        }
        root = addArguments(root, command);

        return root;
    }

    private LiteralArgumentBuilder<CommandSourceStack> addArguments(LiteralArgumentBuilder<CommandSourceStack> root, ICommand command) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var params = method.getParameters();
        if (params.length == 0) throw new IllegalArgumentException("No arguments specified");
        if (!CommandSender.class.isAssignableFrom(params[0].getType()))
            throw new IllegalArgumentException("First argument is not a CommandSender");

        if (Player.class.isAssignableFrom(params[0].getType())) this.senderType = SenderType.PLAYER;
        else this.senderType = SenderType.COMMAND_SENDER;

        if (params.length == 1) return root.executes(getExecutor(command));

        ArgumentBuilder<CommandSourceStack, ?> child = null;
        var childIsOptional = false;

        for (var i = params.length - 1; i > 0; i--) {
            var param = params[i];
            Argument argument = null;
            if (param.isAnnotationPresent(Argument.class)) {
                argument = param.getAnnotation(Argument.class);
            }
            var paramName = argument != null
                    ? argument.value()
                    : param.getName();
            var type = toPaperArgumentType(param.getType(), argument);
            var argNode = Commands.argument(paramName, type);

            if (argument != null && argument.suggestions().length > 0) {
                var suggestions = argument.suggestions();
                argNode.suggests((ctx, builder) -> {
                    Arrays.stream(suggestions).filter(s -> s.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                            .forEach(builder::suggest);
                    return builder.buildFuture();
                });
            } else if (argument != null && !argument.completer().equals(DummySuggestionProvider.class)) {
                var completer = argument.completer().getConstructor().newInstance();
                argNode.suggests(completer);
            }

            if (childIsOptional || i == params.length - 1) {
                argNode.executes(getExecutor(command));
            }

            if (argument != null && argument.optional()) {
                if (i < params.length - 1 && !childIsOptional) {
                    throw new IllegalArgumentException("Optional arguments need to be at the end of the method");
                }
                childIsOptional = true;
            } else {
                childIsOptional = false;
            }

            if (child != null) {
                argNode.then(child);
            }

            child = argNode;
        }

        root.then(child);
        if (childIsOptional) {
            root.executes(getExecutor(command));
        }

        return root;
    }

    private Command<CommandSourceStack> getExecutor(ICommand commandInstance) {
        return ctx -> {
            try {
                var sender = ctx.getSource().getSender();
                if (senderType == SenderType.PLAYER) {
                    if (sender instanceof Player player) {
                        if (!player.hasPermission(permission)) {
                            // TODO: handle error message
                            return 0;
                        }
                        return executeMethod(player, ctx, commandInstance);
                    }
                    return 0;
                }

                return executeMethod(sender, ctx, commandInstance);

            } catch (Exception e) {
                //TODO: handle exceptions
                return 0;
            }
        };
    }

    private int executeMethod(CommandSender sender, CommandContext<CommandSourceStack> ctx, ICommand commandInstance) throws InvocationTargetException, IllegalAccessException, CommandSyntaxException {
        var arguments = new Object[method.getParameterCount()];
        arguments[0] = sender;
        var parameters = method.getParameters();
        for (var i = 1; i < parameters.length; i++) {
            var param = parameters[i];
            Argument argument = null;
            if (param.isAnnotationPresent(Argument.class)) {
                argument = param.getAnnotation(Argument.class);
            }
            var paramName = argument != null ? param.getAnnotation(Argument.class).value() : param.getName();
            Object value = null;
            if (ctx.getNodes().stream().anyMatch(n -> n.getNode().getName().equals(paramName))) {
                value = resolveArgument(ctx, paramName, param.getType());
            }

            arguments[i] = value;
        }

        return (int) method.invoke(commandInstance, arguments);
    }

    private Object resolveArgument(CommandContext<CommandSourceStack> ctx, String argName, Type argType) throws CommandSyntaxException {
        if (argType.equals(Player.class)) {
            var resolver = ctx.getArgument(argName, PlayerSelectorArgumentResolver.class);
            var value = resolver.resolve(ctx.getSource());
            return value.getFirst();
        } else {
            return ctx.getArgument(argName, (Class<?>) argType);
        }
    }

    private ArgumentType<?> toPaperArgumentType(Type type, Argument argument) {
        if (type.equals(boolean.class) || type.equals(Boolean.class)) return BoolArgumentType.bool();
        if (type.equals(int.class) || type.equals(Integer.class)) {
            if (argument != null) {
                return IntegerArgumentType.integer(argument.intMin(), argument.intMax());
            } else {
                return IntegerArgumentType.integer();
            }
        }
        if (type.equals(long.class) || type.equals(Long.class)) {
            if (argument != null) {
                return LongArgumentType.longArg(argument.longMin(), argument.longMax());
            } else {
                return LongArgumentType.longArg();
            }
        }
        if (type.equals(float.class) || type.equals(Float.class)) {
            if (argument != null) {
                return FloatArgumentType.floatArg(argument.floatMin(), argument.floatMax());
            } else {
                return FloatArgumentType.floatArg();
            }
        }
        if (type.equals(double.class) || type.equals(Double.class)) {
            if (argument != null) {
                return DoubleArgumentType.doubleArg(argument.doubleMin(), argument.doubleMax());
            } else {
                return DoubleArgumentType.doubleArg();
            }
        }
        if (type.equals(String.class)) {
            if (argument != null) {
                return switch (argument.stringType()) {
                    case WORD -> StringArgumentType.word();
                    case STRING -> StringArgumentType.string();
                    case GREEDY -> StringArgumentType.greedyString();
                };
            } else {
                return StringArgumentType.string();
            }
        }
        if (type.equals(Player.class)) return ArgumentTypes.player();
        if (type.equals(Location.class)) return ArgumentTypes.blockPosition();

        throw new IllegalArgumentException("Unknown argument type: " + type);
    }

    private static enum SenderType {
        COMMAND_SENDER,
        PLAYER
    }
}
