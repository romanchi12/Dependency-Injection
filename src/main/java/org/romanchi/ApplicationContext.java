package org.romanchi;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private Map<String, Object> storage = new HashMap<String, Object>(); // performance

    BeanFactory beanFactory = new BeanFactory();

    public Object getBean(String beanName, ScopeType scopeType) throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if(scopeType == ScopeType.PROTOTYPE){
            Class beanClass = Class.forName(beanName);
            Object createdBean = beanFactory.createBean(beanClass);
            return createdBean;
        }
        if(storage.containsKey(beanName)){
            return storage.get(beanName);
        }
        // create bean
        Class beanClass = Class.forName(beanName);
        Object createdBean = beanFactory.createBean(beanClass);
        storage.put(beanName, createdBean);
        return createdBean;
    }

    public <T> T getBean(String beanName, Class<T> tClass, ScopeType scopeType) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Object obj = getBean(beanName, scopeType);
        if (tClass.isInstance(obj)) {
            return tClass.cast(obj);
        }

        throw new ClassCastException(beanName);
    }
    public <T> T getBean(Class<T> tClass, ScopeType scopeType) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        String beanName = tClass.getCanonicalName();
        return (T) getBean(beanName, scopeType);
    }



    public void setBean(String beanName, Object bean){
        storage.put(beanName,bean);
    }

    public Object getBean(String beanName) throws InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return getBean(beanName, ScopeType.SINGLETON);
    }
    public <T> T getBean(String beanName, Class<T> tClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
        return getBean(beanName, tClass, ScopeType.SINGLETON);
    }
    public <T> T getBean(Class<T> tClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException{
        return getBean(tClass, ScopeType.SINGLETON);
    }
}
