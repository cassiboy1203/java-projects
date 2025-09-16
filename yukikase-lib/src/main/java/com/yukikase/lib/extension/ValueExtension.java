package com.yukikase.lib.extension;

import com.yukikase.framework.anotations.injection.Value;
import com.yukikase.framework.injection.InjectorExtension;
import com.yukikase.lib.YukikasePlugin;

public class ValueExtension implements InjectorExtension {
    private final YukikasePlugin plugin;

    public ValueExtension(YukikasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public <T> T onAfterCreate(Class<?> clazz, T instance) {
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                var valueKey = field.getAnnotation(Value.class).value();
                var config = plugin.getConfig();
                var value = config.get(valueKey);
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(instance, value);
                    } catch (IllegalAccessException ignored) {
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }

        return instance;
    }
}
