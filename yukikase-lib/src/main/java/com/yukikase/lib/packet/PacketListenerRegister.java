package com.yukikase.lib.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.annotations.packet.PacketListener;
import com.yukikase.lib.packet.event.PacketEventMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Configuration
public class PacketListenerRegister {

    @Register
    public void registerPacketListeners(YukikasePlugin plugin, List<IPacketListener> packetListeners) {
        var manager = ProtocolLibrary.getProtocolManager();
        for (var packetListener : packetListeners) {
            registerPacketListener(packetListener, manager, plugin);
        }
    }

    private void registerPacketListener(IPacketListener packetListener, ProtocolManager manager, YukikasePlugin plugin) {
        var clazz = packetListener.getClass();

        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PacketListener.class)) {
                var priority = method.getAnnotation(PacketListener.class).value().getListenerPriority();
                var parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("PacketListener method must contain exactly one parameter");
                }
                if (parameterTypes[0].isAssignableFrom(com.yukikase.lib.packet.event.PacketEvent.class)) {
                    throw new IllegalArgumentException("The parameter must implement com.yukikase.lib.packet.event.PacketEvent");
                }

                var packetEventType = parameterTypes[0];
                var packetType = PacketEventMapper.getPacketType(packetEventType);

                manager.addPacketListener(new PacketAdapter(plugin, priority, packetType) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (!packetType.isClient()) {
                            return;
                        }

                        var packetEvent = PacketEventMapper.map(event, packetEventType);
                        try {
                            method.invoke(packetListener, packetEvent);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        if (packetEvent.isCancelled()) {
                            event.setCancelled(true);
                        }
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (!packetType.isServer()) {
                            return;
                        }

                        var packetEvent = PacketEventMapper.map(event, packetEventType);
                        try {
                            method.invoke(packetListener, packetEvent);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        if (packetEvent.isCancelled()) {
                            event.setCancelled(true);
                        }
                        if (packetEvent.isReadOnly()) {
                            event.setReadOnly(true);
                        }
                    }
                });
            }
        }
    }
}
