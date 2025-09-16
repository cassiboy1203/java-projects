package com.yukikase.framework.injection;

import com.yukikase.framework.Tuple;

import java.util.Set;

public interface InjectorExtension {
    default void onScan(Class<?> clazz) {
    }

    default Set<Tuple<String, Class<?>>> onBeforeGetInstance(Class<?> clazz, String name) {
        return Set.of();
    }

    default <T> T onAfterCreate(Class<?> clazz, T instance) {
        return instance;
    }
}
