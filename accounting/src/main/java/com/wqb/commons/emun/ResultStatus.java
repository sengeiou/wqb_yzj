package com.wqb.commons.emun;
/**
* @Author: Ben
* @Date: 2019-04-03
* @Description: 接口返回结果状态
*/
public enum ResultStatus {

    SUCCESS("成功", "success"),
    FAIL("失败", "fail"),
    PROCESSING("处理中", "processing"),

    /**
     * 空
     */
    NONE(null, null);

    private String name;

    private String code;

    ResultStatus(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static ResultStatus codeOf(String code) {
        for (ResultStatus resultStatus : values()) {
            if (resultStatus.getCode().equals(code)) {
                return resultStatus;
            }
        }
        return NONE;
    }
}
