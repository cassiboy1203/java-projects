package com.yukikase.lib.annotations.packet;


import com.yukikase.lib.packet.ListenerPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketListener {
    ListenerPriority value() default ListenerPriority.NORMAL;
}
