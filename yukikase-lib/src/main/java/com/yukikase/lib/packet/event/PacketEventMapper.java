package com.yukikase.lib.packet.event;

import com.comphenix.protocol.PacketType;

public final class PacketEventMapper {
    private PacketEventMapper() {
    }

    public static PacketEvent map(com.comphenix.protocol.events.PacketEvent packetEvent, Class<?> toClass) {
        PacketEvent event = null;
        if (toClass.equals(DigActionEvent.class)) {
            event = DigActionEvent.map(packetEvent);
        } else if (toClass.equals(BlockBreakAnimationEvent.class)) {
            event = BlockBreakAnimationEvent.map(packetEvent);
        } else {
            throw new IllegalArgumentException("Unknown event type: " + toClass.getName());
        }
        if (packetEvent.isReadOnly()) {
            event.setReadOnly(true);
        }
        return event;
    }

    public static PacketType getPacketType(Class<?> eventClass) {
        if (eventClass.equals(DigActionEvent.class)) {
            return PacketType.Play.Client.BLOCK_DIG;
        }
        if (eventClass.equals(BlockBreakAnimationEvent.class)) {
            return PacketType.Play.Server.BLOCK_BREAK_ANIMATION;
        }

        throw new IllegalArgumentException("Unknown packet event class: " + eventClass.getName());
    }
}
