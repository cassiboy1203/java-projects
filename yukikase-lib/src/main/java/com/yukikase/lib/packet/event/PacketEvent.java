package com.yukikase.lib.packet.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

public abstract class PacketEvent {

    protected final Player player;
    private boolean cancelled;
    private boolean readOnly;

    protected PacketEvent(Player player) {
        this.player = player;
    }

    public final Player player() {
        return player;
    }

    public abstract int getPacketId();

    public abstract PacketType getPacketType();

    public abstract PacketContainer write(PacketContainer packet);

    public final boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
