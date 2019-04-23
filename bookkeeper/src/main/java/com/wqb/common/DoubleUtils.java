package com.wqb.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author 司氏旭东
 * @ClassName: DoubleUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年7月26日 下午10:08:32
 */
public class DoubleUtils {
    /**
     * @param d
     * @return double 返回类型
     * @Title: formatDouble
     * @Description: 四舍五入, 保留两位小数
     * @date 2018年7月26日 下午10:08:36
     * @author SiLiuDong 司氏旭东
     */
    public static double formatDouble(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    /**
     * 将数据保留两位小数
     */
    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }

    public static String getTwoString(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        if (".00".equals(yearString)) {
            yearString = "0.00";
        }
        if (yearString.startsWith(".")) {
            yearString = "0" + yearString;
        }
        return yearString;
    }

    public static String getNumber(double num) {

        // 输出数字，发现结果为1.234567890127891E9，自动转成了科学计数法
        //System.out.println("转换前：" + num);
        // 新建数字格式对象
        NumberFormat nf = NumberFormat.getInstance();
        // 保留小数位2位
        nf.setMaximumFractionDigits(2);
        // 是否保留千分位
        nf.setGroupingUsed(false);
        // 输出数字，结果为1234567890.1279 ，已可以正常显示，
        return nf.format(num);
    }
}
