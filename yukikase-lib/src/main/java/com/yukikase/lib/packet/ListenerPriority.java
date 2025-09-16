package com.yukikase.lib.packet;

public enum ListenerPriority {
    LOWEST(com.comphenix.protocol.events.ListenerPriority.LOWEST),
    LOW(com.comphenix.protocol.events.ListenerPriority.LOW),
    NORMAL(com.comphenix.protocol.events.ListenerPriority.NORMAL),
    HIGH(com.comphenix.protocol.events.ListenerPriority.HIGH),
    HIGHEST(com.comphenix.protocol.events.ListenerPriority.HIGHEST),
    MONITOR(com.comphenix.protocol.events.ListenerPriority.MONITOR),
    ;

    private com.comphenix.protocol.events.ListenerPriority listenerPriority;

    ListenerPriority(com.comphenix.protocol.events.ListenerPriority listenerPriority) {
        this.listenerPriority = listenerPriority;
    }

    public com.comphenix.protocol.events.ListenerPriority getListenerPriority() {
        return listenerPriority;
    }
}
