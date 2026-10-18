package com.youfu;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigApplicationContext {
    Map<String, BeanDefinition> beanDefinitions;
    Map<String, Object> beans;

    public AnnotationConfigApplicationContext(String pack) {
        beanDefinitions = new HashMap<>();
        beans = new HashMap<>();

        // 拿到指定包下所有文件
        Set<Class<?>> classes = MyTools.getClasses(pack);

        // 让Container管理bean definition
        for (Class<?> clazz : classes) {
            Annotation componentAnnotation = clazz.getAnnotation(Component.class);
            if (componentAnnotation != null) {
                BeanDefinition beanDefinition = new BeanDefinition(clazz.getSimpleName(), clazz);
                beanDefinitions.put(clazz.getSimpleName(), beanDefinition);
            }
        }
    }

    public Object getBean(String name) throws NoSuchMethodException {
        if (beans.containsKey(name)) {
            return beans.get(name);
        } else {
            Class<?> clazz = beanDefinitions.get(Character.toUpperCase(name.charAt(0)) + name.substring(1)).clazz;
            Component componentAnnotation = clazz.getAnnotation(Component.class);

            String key;
            if (!componentAnnotation.value().isEmpty()) {
                key = componentAnnotation.value();
            } else {
                String clazzName = clazz.getSimpleName();
                key = Character.toLowerCase(clazzName.charAt(0)) + clazzName.substring(1);
            }

            Object bean = createBean(clazz);
            beans.put(key, bean);
            return bean;
        }
    }

    public Object createBean(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object obj = null;
        try {
            obj = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value();
                if (field.getType() == int.class) {
                    try {
                        Method method = clazz.getDeclaredMethod(methodName, int.class);
                        method.invoke(obj, Integer.parseInt(value));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (field.getType() == String.class) {
                    try {
                        Method method = clazz.getDeclaredMethod(methodName, String.class);
                        method.invoke(obj, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Autowired autoWiredAnnotation = field.getAnnotation(Autowired.class);
            if (autoWiredAnnotation != null) {
                Method method = clazz.getDeclaredMethod(methodName, field.getType());
                try {
                    method.invoke(obj, getBean(field.getName()));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return obj;
    }
    @Override
    public String toString() {
        return "IoCContainer{" +
                "beanDefinitions=" + beanDefinitions +
                '}';
    }
}
