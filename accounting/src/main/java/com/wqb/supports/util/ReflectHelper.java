package com.wqb.supports.util;

import org.springframework.cglib.core.ReflectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * 反射助手
 *
 * @author Shoven
 * @date 2018-10-12 15:24
 */
public class ReflectHelper {

    private static final Charset ORIGINAL_CHARSET = ISO_8859_1;

    /**
     * 字符串编码转化（避免中文乱码）
     *
     * @param obj     实体对象
     * @param charset 字符集
     */
    public static void convertCharacterEncoding(Object obj, Charset charset) throws Exception{
        if (obj == null) {
            return;
        }

        PropertyDescriptor[] beanProperties = ReflectUtils.getBeanProperties(obj.getClass());

        for (PropertyDescriptor beanProperty : beanProperties) {
            if (beanProperty.getPropertyType().isAssignableFrom(String.class)) {
                Method readMethod = beanProperty.getReadMethod();
                Method writeMethod = beanProperty.getWriteMethod();

                Object invoke = readMethod.invoke(obj);
                if (invoke == null) {
                    continue;
                }

                String writeString = new String(invoke.toString().getBytes(ORIGINAL_CHARSET), charset);
                writeMethod.invoke(obj, writeString);
            }
        }
    }

    /**
     * map转pojo
     *
     * @param map
     * @param beanClass
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }

        return obj;
    }

    /**
     * pojo转map
     *
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null) {
            return null;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        Map<String, Object> map = new HashMap<>();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        return map;
    }

    /**
     * 设置对象的属性
     *
     * @param obj    实例对象
     * @param name   属性名
     * @param value  值
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     */
    public static void setProperty(Object obj, String name, Object value) throws Exception {
        // 属性不存在抛异常
        obj.getClass().getDeclaredField(name);
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
        PropertyDescriptor[] beanProperties = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor beanProperty : beanProperties) {
            if (beanProperty.getName().equals(name)) {
                Method writeMethod = beanProperty.getWriteMethod();
                writeMethod.invoke(obj, value);
            }
        }
    }

    /**
     * 设置对象的属性
     *
     * @param obj   实例对象
     * @param name  属性名
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     */
    public static Object getProperty(Object obj, String name) throws Exception {
        // 属性不存在抛异常
        Field declaredField = obj.getClass().getDeclaredField(name);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }


    public static String getPropertyNameByAnnotation(Class clazz, Class<? extends Annotation> annotationClass) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field.getName();
            }
        }

        throw new RuntimeException(clazz.getSimpleName() + "类的属性没有@"+ annotationClass.getSimpleName() +"注解");
    }
}
