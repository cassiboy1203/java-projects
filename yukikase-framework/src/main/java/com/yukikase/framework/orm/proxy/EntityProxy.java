package com.yukikase.framework.orm.proxy;

import com.yukikase.framework.orm.entity.EntityFactory;
import com.yukikase.framework.orm.entity.EntitySet;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class EntityProxy {

    public static <T> T wrap(Class<T> clazz, EntityFactory entityFactory, String fieldName, Object foreignKey) {
        var getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        var handler = new EntityHandler<T>(foreignKey, entityFactory.get(clazz));

        try {
            return new ByteBuddy(ClassFileVersion.JAVA_V21)
                    .subclass(clazz)
                    .method(named(fieldName).or(named(getMethodName)))
                    .intercept(MethodDelegation.to(handler))
                    .make()
                    .load(clazz.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to lazy load entity of type " + clazz.getName());
        }
    }

    private static class EntityHandler<T> {
        private final Object foreignKey;
        private final EntitySet<T> entitySet;
        private T entity;

        EntityHandler(Object foreignKey, EntitySet<T> entitySet) {
            this.foreignKey = foreignKey;
            this.entitySet = entitySet;
        }

        @RuntimeType
        public Object intercept(@Origin Method method, @AllArguments Object[] args) {
            if (entity == null) {
                var entity = entitySet.get(foreignKey);
            }
            return entity;
        }
    }
}
