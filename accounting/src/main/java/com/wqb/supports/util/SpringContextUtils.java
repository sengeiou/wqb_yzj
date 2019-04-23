package com.wqb.supports.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Shoven
 * @date 2018-09-30 10:32
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getContext() {
        checkApplicationContext();
        return applicationContext;
    }


    /**
     * 通过name取得Bean, 自动转型为所赋值对象的类型 该类型的bean在IOC容器中也必须是唯一的
     *
     * @param name bean的name或id
     * @param <T> 要加载的Bean的类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过class取得Bean,该类型的bean在IOC容器中也必须是唯一的
     *
     * @param clazz 要加载的Bean的class
     * @param <T> 要加载的Bean的类型
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name和class取得bean，比较适合当类型不唯一时，再通过id或者name来获取bean
     *
     * @param name bean的name或id
     * @param clazz 要加载的Bean的class
     * @param <T> 要加载的Bean的类型
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(name, clazz);
    }


    private static void checkApplicationContext() {
        Assert.notNull(applicationContext, "applicaitonContext 未注入");
    }

}
