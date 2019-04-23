package com.wqb.commons.emun;


/**
* @Author: Ben
* @Date: 2019-04-01
* @Description: 报税操作名称
*/
public enum TaxAction {
    /**
     * 发票识别 InvoiceORC
     */
    INOVICE_ORC("发票识别", "getVatTicket"),
    /**
     * 查询五险一金 queryWuXianYiJin
     */
    WuXianYiJin("查询五险一金", "queryWuXianYiJin"),

    /**
     * 空
     */
    NONE(null, null);

    private String name;

    private String code;

    TaxAction(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static TaxAction codeOf(String code) {
        for (TaxAction action : values()) {
            if (action.getCode().equals(code)) {
                return action;
            }
        }
        return NONE;
    }
}
