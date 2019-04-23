package com.wqb.common;

public class BusinessException extends RuntimeException {
    private String msg;

    /**
     * 自定义业务异常类
     */
    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
