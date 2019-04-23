package com.wqb.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat sdfMoth = new SimpleDateFormat("yyyy-MM");

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    // 转换成年份
    public static String getYear2(Date date) {
        return sdfYear.format(date);
    }

    public static String getTime1(Date date) {
        return sdfDay.format(date);
    }

    public static String getMoth2(Date date) {
        if (date == null)
            return null;
        return sdfMoth.format(date);

    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    public static String getTime(Date date) {
        return sdfTime.format(date);
    }

    /**
     * 获取YYYY-MM格式
     *
     * @return
     */
    public static String getMonth() {
        return sdfMoth.format(new Date());
    }

    /**
     * 获取YYYY-MM格式
     *
     * @return
     */
    public static Date getMonth(String date) {

        try {
            return sdfMoth.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws @author luguosui
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期2
     *
     * @return
     */
    public static Date fomatToDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        if (date.indexOf("/") > 0) {
            fmt = new SimpleDateFormat("yyyy/MM/dd");
        }
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date fomatToDate1(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期3
     *
     * @return
     */
    public static Date fomatDateMM(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期4
     *
     * @return
     */
    public static Date fomatToDate4(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期5
     *
     * @return
     */
    public static Date fomatToDate5(String str) {

        if (str == null || str.trim().length() == 0) {
            return null;
        }
        try {
            return sdfDays.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24))
                    / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    public static void main(String[] args) {
//		System.out.println(getDays());
//		System.out.println(getAfterDayWeek("3"));
//		System.out.println(getLastMonth("2017-01"));
//		System.out.println(getNextMonth("2017-12"));
//
//		System.out.println(getSysLastMonth()+"\t"+"ssssssssssss");

        String moth2 = getMoth2(new Date());
        System.out.println(getLastMonth(new Date()));
        String nextNMonth3 = getNextNMonth3(new Date(), -8);
        System.out.println(nextNMonth3);

    }

    /**
     * 获取某年某月有多少天
     *
     * @param date
     * @return
     */
    public static int getDayofMonth(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        int day = cal.getActualMaximum(Calendar.DATE);
        return day;
    }

    public static Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    // 获取上个月
    public static String getLastMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date time = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.add(Calendar.MONTH, -1);// 月份减一
            Date dateTemp = cal.getTime();
            return sdf.format(dateTemp);
        } catch (ParseException e) {
            return null;
        }
    }

    // 获取下个月
    public static String getNextMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date time = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.add(Calendar.MONTH, 1);// 月份加1
            Date dateTemp = cal.getTime();
            return sdf.format(dateTemp);
        } catch (ParseException e) {
            return null;
        }
    }

    // 获取N个月之后的日期
    public static String getNextNMonth1(Date date, Integer n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, n);// 月份加1
            Date dateTemp = cal.getTime();
            return sdf.format(dateTemp);
        } catch (Exception e) {
            return null;
        }
    }

    // 获取N个月之后的日期
    public static Date getNextNMonth2(Date date, Integer n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, n);// 月份加1
            Date dateTemp = cal.getTime();
            return dateTemp;
        } catch (Exception e) {
            return null;
        }
    }

    // 获取N个月之后的日期
    public static String getNextNMonth3(Date date, Integer n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, n);// 月份加1
            Date dateTemp = cal.getTime();
            return sdf.format(dateTemp);
        } catch (Exception e) {
            return null;
        }
    }

    // 根据参数获取不同的时间格式
    public static Map<String, String> getDate2(String type) {
        Map<String, String> map = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(1); // 获取年
        int month = cal.get(2) + 1; // 获取月
        int day = cal.getActualMaximum(5); // 获取日
        String str1 = null;
        String str2 = null;
        switch (type) {
            case "1":
                str1 = year + "-" + month + "-01";
                str2 = year + "-" + month + "-" + day;
                map.put("beginTime", str1);
                map.put("endTime", str2);
                break;
            case "2":
                str1 = year + "-" + month;
                map.put("yearMoth", str1);
                break;
            case "3":
                map.put("moth", month + "");
                break;
            default:
                break;
        }
        return map;
    }

    /**
     * @param accountPeriod
     * @return String 返回类型
     * @Title: newAccountPeriod
     * @Description: 月份加1
     * @date 2018年1月25日 下午4:10:45
     * @author SiLiuDong 司氏旭东
     */
    public static String getDateAdd(String date) {
        Date parse = null;
        try {
            parse = sdfMoth.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        instance.add(instance.MONTH, 1);
        Date time = instance.getTime();
        String format = sdfMoth.format(time);
        return format;
    }

    /**
     * 获取系统时间的上个月时间
     *
     * @return
     */
    public static String getSysLastMonth() {
        String sysMonth = sdfMoth.format(new Date());
        String time = getLastMonth(sysMonth);
        return time;
    }

    /**
     * 时间的上个月时间
     *
     * @return
     */
    public static String getLastMonth(Date date) {
        String sysMonth = sdfMoth.format(date);
        String time = getLastMonth(sysMonth);
        return time;
    }

}
