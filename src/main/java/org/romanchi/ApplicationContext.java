package org.romanchi;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private Map<String, Object> storage = new HashMap<String, Object>(); // performance

    BeanFactory beanFactory = new BeanFactory();

    public Object getBean(String beanName, ScopeType scopeType)  {
        Object createdBean = null;
        try {
            if(scopeType == ScopeType.PROTOTYPE){
                Class beanClass = Class.forName(beanName);
                createdBean = beanFactory.createBean(beanClass);
                return createdBean;
            }
            if(storage.containsKey(beanName)){
                return storage.get(beanName);
            }
            // create bean
            Class beanClass = Class.forName(beanName);
            createdBean = beanFactory.createBean(beanClass);
            storage.put(beanName, createdBean);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return createdBean;
    }

    public <T> T getBean(String beanName, Class<T> tClass, ScopeType scopeType)  {
        Object obj = getBean(beanName, scopeType);
        if (tClass.isInstance(obj)) {
            return tClass.cast(obj);
        }

        throw new ClassCastException(beanName);
    }
    public <T> T getBean(Class<T> tClass, ScopeType scopeType)  {
        String beanName = tClass.getCanonicalName();

        /*if(tClass.isInterface()){
            beanName = tClass.getCanonicalName();
        }else{
            if(tClass.getInterfaces().length == 1){
                beanName = tClass.getInterfaces()[0].getCanonicalName();
            }else {
                beanName = tClass.getCanonicalName();
            }
        }
        String beanName = tClass.getCanonicalName();*/
        return (T) getBean(beanName, scopeType);
    }



    public void setBean(String beanName, Object bean){
        storage.put(beanName,bean);
    }

    public Object getBean(String beanName)  {
        return getBean(beanName, ScopeType.SINGLETON);
    }
    public <T> T getBean(String beanName, Class<T> tClass) {
        return getBean(beanName, tClass, ScopeType.SINGLETON);
    }
    public <T> T getBean(Class<T> tClass) {
        return getBean(tClass, ScopeType.SINGLETON);
    }

    public boolean containsBean(String beanName){
        return storage.containsKey(beanName);
    }
    public boolean containsBean(Class beanClass){
        return containsBean(beanClass.getCanonicalName());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Context:{\n");
        for(String key:storage.keySet()){
            Object bean = storage.get(key);
            s.append(key + " : " + storage.get(key).hashCode() + "=[");
            Field[] beanFields = bean.getClass().getDeclaredFields();
            for(Field beanField:beanFields){
                try {
                    beanField.setAccessible(true);
                    Object beanFieldValue = beanField.get(bean);
                    if(beanFieldValue !=null){
                        s.append(beanField.getName() + " : " + beanFieldValue.hashCode());
                    }else{
                        s.append(beanField.getName() + " : null");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            s.append("]\n");
        }
        s.append("}");
        return s.toString();
    }
}
