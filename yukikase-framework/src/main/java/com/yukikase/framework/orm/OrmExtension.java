package com.yukikase.framework.orm;


import com.j256.ormlite.table.DatabaseTable;
import com.yukikase.framework.injection.InjectorExtension;

import java.util.ArrayList;
import java.util.List;

public class OrmExtension implements InjectorExtension {
    private final List<Class<?>> entities = new ArrayList<>();

    @Override
    public void onScan(Class<?> clazz) {
        if (clazz.isAnnotationPresent(DatabaseTable.class)) {
            entities.add(clazz);
        }
    }

    public List<Class<?>> getEntities() {
        return entities;
    }
}
