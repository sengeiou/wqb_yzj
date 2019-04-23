package com.wqb.supports.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimals工具
 *
 * @author Shoven
 * @since 2019-03-27 9:48
 */
public class BigDecimals {

    /**
     * BigDecimal的加法运算封装
     *
     * @param b1
     * @param bn
     * @return
     */
    public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
        b1 = of(b1);
        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.add(of(b));
            }
        }
        return b1;
    }



    /**
     * BigDecimal的减法运算封装
     *
     * @param b1
     * @param bn
     * @return
     */
    public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
        b1 = of(b1);
        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.subtract(of(b));
            }
        }
        return b1;
    }

    /**
     * BigDecimal的除法运算封装
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal safeDivide(BigDecimal b1, BigDecimal b2) {
        if (null == b1) {
            return BigDecimal.ZERO;
        }
        return b1.divide(of(b2));
    }


    /**
     * BigDecimal的除法运算封装
     *
     * @param b1
     * @param b2
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal safeDivide(BigDecimal b1, BigDecimal b2, int scale, RoundingMode roundingMode) {
        if (null == b1) {
            return BigDecimal.ZERO;
        }
        return b1.divide(of(b2), scale, roundingMode);
    }


    /**
     * BigDecimal的乘法运算封装
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal safeMultiply(BigDecimal b1, BigDecimal b2) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }

        return b1.multiply(b2);
    }

    /**
     * 判断是否相等，只判断是否有数值意义 此处 0 == null
     *
     * @param b1
     * @param b2
     * @return
     */
    public static boolean equals(BigDecimal b1, BigDecimal b2) {
        return of(b1).equals(of(b2));
    }

    /**
     * 是否所有都相等，同上
     *
     * @param b1
     * @param bn
     * @return
     */
    public static boolean allEquals(BigDecimal b1, BigDecimal... bn) {
        b1 = of(b1);
        if (null != bn) {
            for (BigDecimal b : bn) {
                if (!b1.equals(of(b))) {
                    return false;
                }
            }
        }
        return b1.equals(BigDecimal.ZERO);
    }

    /**
     * @param val
     * @return
     */
    public static String toAmount(Float val) {
        return of(val).setScale(2).toString();
    }

    /**
     * @param val
     * @return
     */
    public static String toAmount(Double val) {
        return of(val).setScale(2).toString();
    }

    /**
     * @param val
     * @return
     */
    public static String toAmount(String val) {
        return of(val).setScale(2).toString();
    }

    /**
     * @param val
     * @return
     */
    public static String toAmount(BigDecimal val) {
        return of(val).setScale(2).toString();
    }

    /**
     * @param val
     * @return
     */
    public static BigDecimal of(Float val) {
        return val == null ? BigDecimal.ZERO : new BigDecimal(val.toString());
    }

    /**
     * @param val
     * @return
     */
    public static BigDecimal of(Double val) {
        return val == null ? BigDecimal.ZERO : BigDecimal.valueOf(val);
    }

    /**
     * @param val
     * @return
     */
    public static BigDecimal of(String val) {
        return !NumberUtils.isDigits(val) ? BigDecimal.ZERO : new BigDecimal(val);
    }

    /**
     * @param val
     * @return
     */
    public static BigDecimal of(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
