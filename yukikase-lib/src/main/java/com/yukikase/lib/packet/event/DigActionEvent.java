package com.yukikase.lib.packet.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class DigActionEvent extends PacketEvent {
    private final Action action;
    private final Location location;
    private final Face face;

    public DigActionEvent(Player player, Action action,
                          Location location,
                          Face face) {
        super(player);
        this.action = action;
        this.location = location;
        this.face = face;
    }

    @Override
    public int getPacketId() {
        return 0x28;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.Play.Client.BLOCK_DIG;
    }

    @Override
    public PacketContainer write(PacketContainer packet) {
        return null;
    }

    public static PacketEvent map(com.comphenix.protocol.events.PacketEvent packetEvent) {
        var packet = packetEvent.getPacket();
        var player = packetEvent.getPlayer();
        var action = Action.valueOf(packet.getPlayerDigTypes().read(0));
        var blockPositionModifier = packet.getBlockPositionModifier().read(0);
        var face = Face.valueOf(packet.getDirections().read(0));

        return new DigActionEvent(player, action, blockPositionModifier.toLocation(player.getWorld()), face);
    }

    public Action action() {
        return action;
    }

    public Location location() {
        return location;
    }

    public Face face() {
        return face;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DigActionEvent) obj;
        return Objects.equals(this.player, that.player) &&
                Objects.equals(this.action, that.action) &&
                Objects.equals(this.location, that.location) &&
                Objects.equals(this.face, that.face);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, action, location, face);
    }

    @Override
    public String toString() {
        return "DigActionEvent[" +
                "player=" + player + ", " +
                "action=" + action + ", " +
                "location=" + location + ", " +
                "face=" + face + ']';
    }


    public enum Action {
        START(0),
        CANCEL(1),
        FINISH(2),
        DROP_STACK(3),
        DROP_ITEM(4),
        SHOOT_ARROW(5),
        SWAP_ITEM_IN_HAND(6);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        public static Action valueOf(int value) {
            for (Action action : Action.values()) {
                if (action.value == value) {
                    return action;
                }
            }

            throw new IllegalArgumentException("Unknown action value: " + value);
        }

        public static Action valueOf(EnumWrappers.PlayerDigType value) {
            switch (value) {
                case START_DESTROY_BLOCK -> {
                    return Action.START;
                }
                case STOP_DESTROY_BLOCK -> {
                    return Action.FINISH;
                }
                case ABORT_DESTROY_BLOCK -> {
                    return Action.CANCEL;
                }
                case DROP_ALL_ITEMS -> {
                    return Action.DROP_STACK;
                }
                case DROP_ITEM -> {
                    return Action.DROP_ITEM;
                }
                case RELEASE_USE_ITEM -> {
                    return Action.SHOOT_ARROW;
                }
                case SWAP_HELD_ITEMS -> {
                    return Action.SWAP_ITEM_IN_HAND;
                }
                default -> throw new IllegalArgumentException("Unknown action value: " + value);
            }
        }
    }

    public enum Face {
        BOTTOM((byte) 0), // -Y
        TOP((byte) 1), // +Y
        NORTH((byte) 2), // -Z
        SOUTH((byte) 3), // +Z
        WEST((byte) 4), // -X
        EAST((byte) 5); // +X

        private final byte value;

        Face(byte value) {
            this.value = value;
        }

        public static Face valueOf(byte value) {
            for (Face face : Face.values()) {
                if (face.value == value) {
                    return face;
                }
            }

            throw new IllegalArgumentException("Unknown face value: " + value);
        }

        public static Face valueOf(EnumWrappers.Direction value) {
            switch (value) {
                case UP -> {
                    return BOTTOM;
                }
                case DOWN -> {
                    return TOP;
                }
                case NORTH -> {
                    return NORTH;
                }
                case SOUTH -> {
                    return SOUTH;
                }
                case WEST -> {
                    return WEST;
                }
                case EAST -> {
                    return EAST;
                }
                default -> throw new IllegalArgumentException("Unknown face value: " + value);
            }
        }
    }
}
