package com.wqb.commons.emun;

/**
 * @author WQB
 * @since 2019-03-25 16:40
 */
public enum StockGoodsDirection {

    /**
     * 借方向
     */
    DEBIT("借"),


    /**
     * 贷方向
     */
    CREDIT("贷"),

    /**
     * 平
     */
    EQUAL("平");

    private String name;

    StockGoodsDirection(String name) {
        this.name = name;
    }

    public static StockGoodsDirection amountOf(Double d) {
        if (d == null) {
            return null;
        }
        if (d.compareTo(0.00001) <= 0 ) {
            return EQUAL;
        } else {
            return DEBIT;
        }
    }

    public String getName() {
        return name;
    }
}
