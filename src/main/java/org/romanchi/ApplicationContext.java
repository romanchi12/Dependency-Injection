package org.romanchi;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private Map<String, Object> storage = new HashMap<String, Object>(); // performance

    BeanFactory beanFactory = new BeanFactory();

    public Object getBean(String beanName) throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if(storage.containsKey(beanName)){
            return storage.get(beanName);
        }
        // create bean
        Class beanClass = Class.forName(beanName);
        Object createdBean = beanFactory.createBean(beanClass);
        storage.put(beanName, createdBean);
        return createdBean;
    }

    public <T> T getBean(String beanName, Class<T> tClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Object obj = getBean(beanName);

        if (tClass.isInstance(obj)) {
            return tClass.cast(obj);
        }

        throw new ClassCastException(beanName);
    }
    public <T> T getBean(Class<T> tClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        String beanName = tClass.getCanonicalName();
        return (T) getBean(beanName);
    }

    public void setBean(String beanName, Object bean){
        storage.put(beanName,bean);
    }
}
