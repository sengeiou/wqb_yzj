package com.wqb.commons.emun;
/**
* @Author: Ben
* @Date: 2019-04-03
* @Description: 票据类型
*/
public enum TicketType {

    Invoice("增值税发票", "invoice"),
    Train("火车票", "train"),
    Plane("飞机票", "plane"),
    Manual("其他票", "manual"),


    /**
     * 空
     */
    NONE(null, null);

    private String name;

    private String code;

    TicketType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static TicketType codeOf(String code) {
        for (TicketType ticketType : values()) {
            if (ticketType.getCode().equals(code)) {
                return ticketType;
            }
        }
        return NONE;
    }
}
