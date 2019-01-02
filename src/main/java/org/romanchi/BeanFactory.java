package org.romanchi;

import com.sun.org.apache.xpath.internal.Arg;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanFactory {

    public Object createBean(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object bean = constructor.newInstance();
        return bean;
    }

    public Object createBean(Class clazz, Object...args) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        int constructorArgsLength = args.length;
        Class[] constructorArgsTypes = new Class[constructorArgsLength];
        for(int i = 0; i < constructorArgsLength; i++){
            constructorArgsTypes[i] = args[i].getClass();
        }
        Object bean = null;
        try {
            Constructor constructor = clazz.getDeclaredConstructor(constructorArgsTypes);
            constructor.setAccessible(true);
            bean = constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } finally {
            return bean;
        }
    }
}
