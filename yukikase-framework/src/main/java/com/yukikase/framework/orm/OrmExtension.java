package com.yukikase.framework.orm;


import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.injection.InjectorExtension;

import java.util.HashSet;
import java.util.Set;

public class OrmExtension implements InjectorExtension {

    private final Set<Class<?>> entities = new HashSet<>();

    @Override
    public void onScan(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Entity.class)) {
            entities.add(clazz);
        }
    }

    public Set<Class<?>> getEntities() {
        return entities;
    }
}
