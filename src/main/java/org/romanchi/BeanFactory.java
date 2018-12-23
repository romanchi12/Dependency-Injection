package org.romanchi;

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
        Constructor[] constructors = clazz.getConstructors();
        Object bean = null;
        for(Constructor constructor:constructors){
            Class[] parametersTypes = constructor.getParameterTypes();
            if(parametersTypes.length==args.length){
                try {
                    constructor.setAccessible(true);
                    bean = constructor.newInstance(args);
                }catch (InstantiationException e) {
                        continue;
                }
            }
        }
        if(bean==null){
            throw new InstantiationException("No such constructor present");
        }
        return bean;
    }
}
