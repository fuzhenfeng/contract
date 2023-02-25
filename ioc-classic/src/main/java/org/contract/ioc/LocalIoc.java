package org.contract.ioc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.ImmutableUtils;
import org.contract.common.InitException;
import org.contract.common.StringUtils;
import org.contract.config.Config;
import org.contract.config.Environment;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LocalIoc implements Ioc {
    private final static Logger log = LogManager.getLogger(LocalIoc.class);

    private final Map<String, Map<String, Class<? extends InstanceDefinition>>> classes = new ConcurrentHashMap<>(256);
    private final Map<String, Map<String, Object>> singletonObjects = new ConcurrentHashMap<>(256);
    private Environment environment;
    private Config config;

    @Override
    public void init(Config config, Environment environment) {
        this.config = config;
        this.environment = environment;
    }

    @Override
    public void registerByDirectory(String layer, String path) throws InitException {
        File file = new File(path);
        Iterator<File> files = Arrays.stream(file.listFiles()).iterator();
        ClassLoader classLoader = environment.getClassLoader();
        try {
            while (files.hasNext()) {
                File next = files.next();
                if(!next.isFile() || !next.getName().contains(".class"))
                    continue;
                String className = getClassName(next.getPath(), classLoader);
                InstanceDefinition<? extends InstanceDefinition> instanceDefinition = getInstanceDefinition(classLoader, className);

                doRegister(layer, instanceDefinition);
            }

            applyPropertyValues(layer, classLoader);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InitException("register ioc error");
        }
    }

    @Override
    public <T> void register(String layer, InstanceDefinition<T> instanceDefinition, String name) throws InitException {
        doRegister(layer, instanceDefinition, name);
    }

    @Override
    public List<Class> getClasses(String layer) {
        List<Class> collect = getClassCache(layer).entrySet().stream().map(entity -> entity.getValue()).collect(Collectors.toList());
        return ImmutableUtils.list(collect);
    }

    @Override
    public List<Object> getInstances(String layer) {
        List<Object> collect = getInstantCache(layer).entrySet().stream().map(entity -> entity.getValue()).collect(Collectors.toList());
        return ImmutableUtils.list(collect);
    }

    private void applyPropertyValues(String layer, ClassLoader classLoader) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Queue<Map.Entry<String, Object>> queue = copyInstant(layer);

        while (!queue.isEmpty()) {
            Map.Entry<String, Object> next = queue.poll();

            String key = next.getKey();
            Object value = next.getValue();
            Class aClass = getClassCache(layer).get(key);

            for (Field field: aClass.getDeclaredFields()) {
                String typeName = field.getGenericType().getTypeName();
                if(filterByClassName(typeName))
                    continue;
                Object fieldInstant = getInstantCache(layer).get(typeName);
                if(fieldInstant == null) {
                    InstanceDefinition instanceDefinition = getInstanceDefinition(classLoader, typeName);
                    if(instanceDefinition == null)
                        continue;
                    doRegister(layer, instanceDefinition);

                    queue.offer(buildEntity(instanceDefinition));
                }

                field.setAccessible(true);
                field.set(value, getInstantCache(layer).get(typeName));
            }
        }
    }

    private Queue<Map.Entry<String, Object>> copyInstant(String layer) {
        Iterator<Map.Entry<String, Object>> instances = getInstantCache(layer).entrySet().iterator();
        Queue<Map.Entry<String, Object>> queue = new LinkedList<>();
        while (instances.hasNext()) {
            queue.add(instances.next());
        }
        return queue;
    }

    private Map.Entry<String, Object> buildEntity(InstanceDefinition instanceDefinition) {
        HashMap<String, Object> newEntity = new HashMap<>(1);
        newEntity.put(instanceDefinition.getDefinition().getClass().getName(), instanceDefinition.getDefinition());
        return newEntity.entrySet().iterator().next();
    }

    private Map<String, Class<? extends InstanceDefinition>> getClassCache(String layer) {
        return classes.get(layer);
    }

    private Map<String, Object> getInstantCache(String layer) {
        return singletonObjects.get(layer);
    }

    private void doRegister(String layer, InstanceDefinition instanceDefinition, String name) {
        Object definition = instanceDefinition.getDefinition();
        Class aClass = definition.getClass();
        cache(layer, definition, aClass, name);
    }

    private void doRegister(String layer, InstanceDefinition instanceDefinition) {
        Object definition = instanceDefinition.getDefinition();
        Class aClass = definition.getClass();
        String name = aClass.getName();
        cache(layer, definition, aClass, name);
    }

    private void cache(String layer, Object definition, Class aClass, String name) {
        synchronized (this.classes) {
            if (getClassCache(layer) == null) {
                classes.put(layer, new ConcurrentHashMap<>());
                singletonObjects.put(layer, new ConcurrentHashMap<>());
            }
            if (!getClassCache(layer).containsKey(name)) {
                getClassCache(layer).put(name, aClass);
                getInstantCache(layer).put(name, definition);
            }
        }
    }

    private boolean filterByClassName(String name) {
        if(StringUtils.isBlank(name) || !name.startsWith(config.getAppConfig().getAppRealmName())) {
            return true;
        }
        return false;
    }

    private String getClassName(String name, ClassLoader classLoader) {
        String path = classLoader.getResource(StringUtils.EMPTY).getPath()
                .replace("file:", StringUtils.EMPTY);
        return name
                .replace(path, StringUtils.EMPTY)
                .replace(".class", StringUtils.EMPTY)
                .replace(StringUtils.LINUX_SEPARATOR, StringUtils.REALM_SEPARATOR);
    }

    private InstanceDefinition getInstanceDefinition(ClassLoader classLoader, String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = classLoader.loadClass(className);
        if(aClass.isInterface() || aClass.isAnnotation() || aClass.isAnonymousClass() || aClass.isArray() || aClass.isEnum())
            return null;
        Constructor<?> constructor = aClass.getDeclaredConstructor((Class[]) null);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        LocalInstance localInstance = new LocalInstance(instance);
        return localInstance;
    }
}
