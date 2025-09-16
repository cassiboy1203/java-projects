package com.yukikase.framework.injection;

public interface Injector {
    void start(Class<?>... mainClasses);

    void registerSingleton(Class<?> clazz, Object instance);

    void registerClass(Class<?> superClass, Class<?> clazz, String name);

    <T> T getInstance(Class<T> clazz);

    <T> T getInstance(Class<T> clazz, String name);

    <T> T[] getInstances(Class<T> clazz);

    void runConfigurations();

    void registerExtension(InjectorExtension extension);
}
