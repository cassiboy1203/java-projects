package com.yukikase.framework.injection;

import com.yukikase.framework.Tuple;
import com.yukikase.framework.anotations.injection.*;
import com.yukikase.framework.exceptions.BeanInstantiationException;
import com.yukikase.framework.exceptions.BeanNotFoundException;
import com.yukikase.framework.exceptions.ClassNotABeanException;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.framework.orm.entity.OrmLiteEntitySet;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import static com.yukikase.framework.TypeUtil.convertValue;

public final class DefaultInjector implements Injector {

    private final Logger logger = Logger.getLogger(DefaultInjector.class.getName());

    final Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap;
    final List<Class<?>> configurations;
    final Map<Class<?>, Object> singletons;
    final Set<InjectorExtension> extensions;

    public DefaultInjector() {
        this.diMap = new HashMap<>();
        this.singletons = new HashMap<>();
        this.configurations = new ArrayList<>();
        this.extensions = new HashSet<>();
    }

    DefaultInjector(Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap) {
        this(diMap, new HashMap<>(), new ArrayList<>());
    }

    DefaultInjector(Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap, Map<Class<?>, Object> singletons) {
        this(diMap, singletons, new ArrayList<>());
    }

    DefaultInjector(Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap, List<Class<?>> configurations) {
        this(diMap, new HashMap<>(), configurations);
    }

    DefaultInjector(Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap, Map<Class<?>, Object> singletons, List<Class<?>> configurations) {
        this.diMap = diMap;
        this.singletons = singletons;
        this.configurations = configurations;
        this.extensions = new LinkedHashSet<>();
    }

    @Override
    public void start(Class<?>... mainClasses) {
        var classes = getClasses(mainClasses);

        for (var clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                registerClass(clazz);
            } else if (clazz.isAnnotationPresent(Configuration.class)) {
                configurations.add(clazz);
            }

            for (var extension : extensions) {
                extension.onScan(clazz);
            }
        }
    }

    @Override
    public void registerSingleton(Class<?> clazz, Object instance) {
        registerClass(clazz);
        singletons.put(clazz, instance);
    }

    @Override
    public void registerClass(Class<?> superClass, Class<?> clazz, String name) {
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

        var name = clazz.getSimpleName();

        if (clazz.isAnnotationPresent(Qualifier.class)) {
            name = clazz.getAnnotation(Qualifier.class).value();
        }

        registerClass(clazz, clazz, name);

        if (superClass != null && !superClass.equals(Object.class)) {
            registerClass(superClass, clazz, name);
        }

        for (var iFace : interfaces) {
            registerClass(iFace, clazz, name);
        }
    }

    List<Class<?>> getClasses(Class<?>... mainClasses) {
        var classes = new ArrayList<Class<?>>();

        for (var mainClass : mainClasses) {

            try (var stream = new ZipInputStream(new FileInputStream(mainClass.getProtectionDomain().getCodeSource().getLocation().getPath()))) {
                for (var entry = stream.getNextEntry(); entry != null; entry = stream.getNextEntry()) {
                    try {
                        if (entry.getName().endsWith(".class")) {
                            classes.add(Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replaceAll("/", ".")));
                        }
                    } catch (NoClassDefFoundError | IllegalAccessError e) {
                        continue;
                        //TODO: change this method so there are no errors here
                    }

                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return classes;
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(Class<T> clazz, String name) {
        var dis = diMap.get(clazz);
        for (var extension : extensions) {
            var extensionDis = extension.onBeforeGetInstance(clazz, name);
            if (dis == null) {
                dis = extensionDis;
            } else {
                dis.addAll(extensionDis);
            }
        }

        Class<?> instanceClass;

        if (dis == null) {
            throw new BeanNotFoundException(String.format(BeanNotFoundException.EC_NO_BEANS_FOUND_OF_TYPE, clazz.getSimpleName()));
        } else if (dis.size() == 1) {
            instanceClass = dis.stream().findFirst().get().v();
        } else if (name != null) {
            var tuple = dis.stream().filter(t -> t.t().equals(name)).findFirst().orElse(null);
            if (tuple == null)
                throw new BeanNotFoundException(String.format(BeanNotFoundException.EC_NO_BEANS_FOUND_WITH_NAME, clazz.getSimpleName(), name));

            instanceClass = tuple.v();
        } else {
            throw new BeanNotFoundException();
        }

        if (instanceClass.isAnnotationPresent(Singleton.class) || singletons.containsKey(instanceClass)) {
            var instance = singletons.get(instanceClass);
            if (instance == null) {
                instance = createInstance(instanceClass);
                singletons.put(instanceClass, instance);
            }
            return (T) instance;
        }
        return createInstance(instanceClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] getInstances(Class<T> clazz) {
        var dis = diMap.get(clazz);

        if (dis == null) {
            return (T[]) new Object[0];
        }

        var instanceClasses = dis.stream().map(Tuple::v).toList();
        var instances = new ArrayList<T>();

        for (var instanceClass : instanceClasses) {
            instances.add((T) getInstance(instanceClass));
        }

        T[] instanceArray = (T[]) Array.newInstance(clazz, instances.size());
        return instances.toArray(instanceArray);
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<?> instanceClass) {
        var constructors = instanceClass.getConstructors();
        var methods = instanceClass.getMethods();

        var constructor = getConstructor(constructors, instanceClass);

        var parameters = constructor.getParameters();
        var args = new Object[parameters.length];

        for (var i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            if (EntitySet.class.isAssignableFrom(parameter.getType())) {
                var paramType = parameter.getParameterizedType();
                if (paramType instanceof ParameterizedType pType) {
                    Class<?> listElementClass = (Class<?>) pType.getActualTypeArguments()[0];

                    args[i] = new OrmLiteEntitySet<>(listElementClass, getInstance(IDatabaseConnector.class));
                }
            } else if (parameter.isAnnotationPresent(Qualifier.class)) {
                var parameterName = parameter.getAnnotation(Qualifier.class).value();
                args[i] = getInstance(parameter.getType(), parameterName);
            } else {
                args[i] = getInstance(parameter.getType());
            }
        }

        try {
            var instance = (T) constructor.newInstance(args);

            for (var field : instanceClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Value.class)) {
                    var valueKey = field.getAnnotation(Value.class).value();
                    var value = System.getProperty(valueKey);
                    if (value == null) {
                        value = System.getenv(valueKey);
                    }

                    if (value != null) {
                        field.setAccessible(true);
                        try {
                            field.set(instance, convertValue(value, field.getType()));
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                }
            }

            for (var method : methods) {
                if (!method.isAnnotationPresent(Inject.class) && !method.isAnnotationPresent(Register.class))
                    continue;

                invokeMethod(method, instance);
            }

            for (var ext : extensions) {
                instance = ext.onAfterCreate(instanceClass, instance);
            }

            return instance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new BeanInstantiationException(instanceClass.getName());
        }
    }

    private Constructor<?> getConstructor(Constructor<?>[] constructors, Class<?> instanceClass) {
        Constructor<?> constructor = null;

        for (var _constructor : constructors) {
            if (!_constructor.isAnnotationPresent(Inject.class))
                continue;

            constructor = _constructor;
            break;
        }

        if (constructor == null) {
            try {
                constructor = instanceClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new ClassNotABeanException(instanceClass.getName());
            }
        }
        return constructor;
    }

    @Override
    public void runConfigurations() {
        for (var configuration : this.configurations) {
            runConfiguration(configuration);
        }
    }

    @Override
    public void registerExtension(InjectorExtension extension) {
        extensions.add(extension);
    }

    private void runConfiguration(Class<?> clazz) {
        for (var method : clazz.getMethods()) {
            //TODO: check methods for beans
            try {
                if (method.isAnnotationPresent(Register.class)) {
                    invokeMethod(method, clazz.getConstructor().newInstance());
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                logger.severe(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void invokeMethod(Method method, Object instance) throws InvocationTargetException, IllegalAccessException {
        if (method.getParameterCount() <= 0) {

            method.invoke(instance);
        } else {
            var methodParameters = method.getParameters();
            Object[] args = new Object[methodParameters.length];

            for (var i = 0; i < methodParameters.length; i++) {
                var parameter = methodParameters[i];
                var parameterRawType = parameter.getType();

                if (List.class.isAssignableFrom(parameterRawType)) {
                    var paramType = parameter.getParameterizedType();
                    if (paramType instanceof ParameterizedType pType) {
                        Class<?> listElementClass = (Class<?>) pType.getActualTypeArguments()[0];

                        args[i] = Arrays.asList(getInstances(listElementClass));
                    }
                } else if (parameterRawType.isArray()) {
                    Class<?> componentType = parameterRawType.getComponentType();

                    args[i] = getInstances(componentType);
                } else if (EntitySet.class.isAssignableFrom(parameterRawType)) {
                    var paramType = parameter.getParameterizedType();
                    if (paramType instanceof ParameterizedType pType) {
                        Class<?> listElementClass = (Class<?>) pType.getActualTypeArguments()[0];

                        args[i] = new OrmLiteEntitySet<>(listElementClass, getInstance(IDatabaseConnector.class));
                    }
                } else {
                    args[i] = getInstance(parameterRawType);
                }
            }

            method.invoke(instance, args);
        }
    }
}
