package org.romanchi;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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
}
