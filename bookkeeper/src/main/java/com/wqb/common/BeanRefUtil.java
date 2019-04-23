package com.wqb.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanRefUtil {

    public static void setFieldValue(Object bean, Map<String, String> valMap) throws BusinessException {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        String fieldKeyName = null;
        String fieldType = null;
        for (Field field : fields) {
            try {
                String fieldSetName = parSetName(field.getName());  //fieldSetName= setName
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName,
                        field.getType());    //field.getType() class java.lang.String
                fieldKeyName = field.getName(); //name
                String value = valMap.get(fieldKeyName);
                fieldType = field.getType().getSimpleName();   //String
                if ("String".equals(fieldType)) {
                    fieldSetMet.invoke(bean, value);
                } else if ("Date".equals(fieldType)) {
                    Date temp = parseDate(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                    if (null != value && !"".equals(value)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else {
                        fieldSetMet.invoke(bean, value);
                    }
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    if (null != value && !"".equals(value)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        fieldSetMet.invoke(bean, value);
                    }
                } else if ("Double".equalsIgnoreCase(fieldType) || "double".equalsIgnoreCase(fieldType)) {
                    if (null != value && !"".equals(value)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        fieldSetMet.invoke(bean, value);
                    }
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    if (null != value && !"".equals(value)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        fieldSetMet.invoke(bean, false);
                    }
                } else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
                    if (null != value && !"".equals(value)) {
                        BigDecimal temp = new BigDecimal(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        fieldSetMet.invoke(bean, value);
                    }
                } else {
                    fieldSetMet.invoke(bean, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(fieldKeyName + "----------" + fieldType);
                continue;
            }
        }
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else if (datestr.indexOf('d') > 0) {
                fmtstr = "yyyy-MM-dd";
            } else if (datestr.indexOf('d') < 0) {
                fmtstr = "yyyy-MM";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转化为String
     */
    public static String fmtDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods
     * @param fieldSetMet
     * @return boolean
     */
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase() + fieldName.substring(startIndex + 1);
    }


    public static Map<String, String> convMap(Map<String, Object> parm) {
        HashMap<String, String> map = new HashMap<>();
        if (parm != null) {
            for (String key : parm.keySet()) {
                map.put(key, parm.get(key) + "");
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }


}
