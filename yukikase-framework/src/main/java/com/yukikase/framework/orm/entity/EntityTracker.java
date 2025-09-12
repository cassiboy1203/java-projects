package com.yukikase.framework.orm.entity;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Singleton;

import java.util.*;

@Singleton
@Component
public class EntityTracker {
    private final Map<EntityKey, Map<String, Object>> snapshots = new WeakHashMap<>();

    public void snapshot(Object entity) {
        var snapshot = new HashMap<String, Object>();
        for (var field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                snapshot.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException ignored) {
            }
            field.setAccessible(false);
        }
        snapshots.put(new EntityKey(entity), snapshot);
    }

    public Map<String, Object> getSnapshot(Object entity) {
        return snapshots.get(entity);
    }

    public List<String> getChangedFields(Object entity) {
        var changedFields = new ArrayList<String>();
        var snapshot = snapshots.get(new EntityKey(entity));
        if (snapshot == null) {
            return changedFields;
        }

        for (var field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                var currentValue = field.get(entity);
                var oldValue = snapshot.get(field.getName());
                if (!Objects.equals(currentValue, oldValue)) {
                    changedFields.add(field.getName());
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return changedFields;
    }
}
