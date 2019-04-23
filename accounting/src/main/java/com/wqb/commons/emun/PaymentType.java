package com.wqb.commons.emun;

/**
 * @author Shoven
 * @since 2019-04-11 17:17
 */
public enum PaymentType {
    /**
     * 现金 1
     */
    XJ(1, "现金"),

    /**
     * 银行 2
     */
    YH(2, "银行"),

    /**
     * 应收账款 3
     */
    YI_S(3, "应收账款"),

    /**
     * 应付账款 4
     */
    YI_F(4, "应付账款"),

    /**
     * 预收账款 5
     */
    YU_S(5, "预收账款"),

    /**
     * 预付账款 6
     */
    YU_F(6, "预付账款"),

    /**
     * 无付款方式
     */
    NONE(0, null);

    PaymentType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;

    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PaymentType typeOf(Integer type) {
        for (PaymentType paymentType : values()) {
            if (paymentType.getType().equals(type)) {
                return paymentType;
            }
        }
        return NONE;
    }
}
