package com.yukikase.lib.packet.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class BlockBreakAnimationEvent extends PacketEvent {
    private final int entityId;
    private final Location location;
    private final int stage;

    public BlockBreakAnimationEvent(int entityId, Location location, int stage,
                                    Player player) {
        super(player);
        this.entityId = entityId;
        this.location = location;
        this.stage = stage;
    }

    @Override
    public int getPacketId() {
        return 0x05;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.Play.Server.BLOCK_BREAK_ANIMATION;
    }

    public static BlockBreakAnimationEvent map(com.comphenix.protocol.events.PacketEvent packetEvent) {
        var packet = packetEvent.getPacket();
        var enityId = packet.getIntegers().read(0);
        var player = packetEvent.getPlayer();
        var location = packet.getBlockPositionModifier().read(0).toLocation(player.getWorld());
        var stage = packet.getIntegers().read(1);

        return new BlockBreakAnimationEvent(enityId, location, stage, player);
    }

    public PacketContainer write(PacketContainer packet) {
        packet.getIntegers().write(0, entityId);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.getIntegers().write(1, stage);

        return packet;
    }

    public int entityId() {
        return entityId;
    }

    public Location location() {
        return location;
    }

    public int stage() {
        return stage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BlockBreakAnimationEvent) obj;
        return this.entityId == that.entityId &&
                Objects.equals(this.location, that.location) &&
                this.stage == that.stage &&
                Objects.equals(this.player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, location, stage, player);
    }

    @Override
    public String toString() {
        return "BlockBreakAnimationEvent[" +
                "entityId=" + entityId + ", " +
                "location=" + location + ", " +
                "stage=" + stage + ", " +
                "player=" + player + ']';
    }


}
