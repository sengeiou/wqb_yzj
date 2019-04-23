package com.wqb.controller.converter;


import com.wqb.common.StringUtil;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义的日期转换器
 *
 * @title CustomDateConverter.java
 * S:SOURCE 源数据类型
 * T:tareget :要转换成的目标的数据类型   Date:必须和POJO中的类型一致
 */

public class CustomDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (StringUtil.isEmptyWithTrim(source)) {
            return null;
        }
        String s1 = null;
        if (source.indexOf(':') > 0) {
            s1 = "yyyy-MM-dd HH:mm:ss";
        } else if (source.lastIndexOf('-') > 5) {
            s1 = "yyyy-MM-dd";
        } else if (source.lastIndexOf('-') < 5) {
            s1 = "yyyy-MM";
        }
        SimpleDateFormat format = new SimpleDateFormat(s1);
        try {
            Date date = format.parse(source);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
