package com.yukikase.diframework;

public interface Injector {
    void registerSingleton(Class<?> clazz, Object instance);

    void registerClass(Class<?> superClass, Class<?> clazz, String name);

    <T> T getInstance(Class<T> clazz);

    <T> T getInstance(Class<T> clazz, String name);

    <T> T[] getInstances(Class<T> clazz);
}
