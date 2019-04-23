package com.wqb.model;

import java.io.Serializable;

public class DzPay implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1207392408779323424L;
    //扣费ID
    private String id;
    //账套ID
    private String accountID;
    //期间
    private String period;
    //备注
    private String des;
    //写入时间
    private long recordTime;
    //管理员ID
    private String adminID;
    //账务类型
    private Integer zwType;
    //结账月份
    private String zwMonth;

    //平台用户ID
    private String ptID;
    //0:尚未付款 1:付款成功2:付款失败
    private Integer payResult;

    public Integer getPayResult() {
        return payResult;
    }

    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }

    public String getPtID() {
        return ptID;
    }

    public void setPtID(String ptID) {
        this.ptID = ptID;
    }

    public String getZwMonth() {
        return zwMonth;
    }

    public void setZwMonth(String zwMonth) {
        this.zwMonth = zwMonth;
    }

    public Integer getZwType() {
        return zwType;
    }

    public void setZwType(Integer zwType) {
        this.zwType = zwType;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}
