package com.yukikase.diframework;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Qualifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipInputStream;

public final class Injector {

    final Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap;

    public Injector() {
        this.diMap = new HashMap<>();
    }

    void start(Class<?> mainClass) {
        var classes = getClasses(mainClass);

        for (var clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                registerClass(clazz);
            }
        }
    }

    private void registerDi(Class<?> superClass, Class<?> clazz, String name) {
        Set<Tuple<String, Class<?>>> set;
        if (!diMap.containsKey(superClass)) {
            set = new HashSet<>();
        } else {
            set = diMap.get(superClass);
        }


        set.add(new Tuple<>(name, clazz));
        diMap.put(superClass, set);
    }

    private void registerClass(Class<?> clazz) {
        var interfaces = clazz.getInterfaces();
        var superClass = clazz.getSuperclass();

        var name = clazz.getName();

        if (clazz.isAnnotationPresent(Qualifier.class)) {
            name = clazz.getAnnotation(Qualifier.class).value();
        }

        registerDi(clazz, clazz, name);

        if (superClass != null && !superClass.equals(Object.class)) {
            registerDi(superClass, clazz, name);
        }

        for (var iFace : interfaces) {
            registerDi(iFace, clazz, name);
        }
    }

    List<Class<?>> getClasses(Class<?> mainClass) {
        var classes = new ArrayList<Class<?>>();

        try (var stream = new ZipInputStream(new FileInputStream(mainClass.getProtectionDomain().getCodeSource().getLocation().getPath()))) {
            for (var entry = stream.getNextEntry(); entry != null; entry = stream.getNextEntry()) {
                if (entry.getName().endsWith(".class")) {
                    classes.add(Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replaceAll("/", ".")));
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }
}
