package com.wqb.model;

import java.io.Serializable;

public class JzAmount implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -84793970929250945L;
    // 主键ID
    private String id;
    // 总共合计金额
    private double totalAmount;
    // 可用金额
    private double validAmount;
    // 管理员ID
    private String adminID;
    // 平台ID
    private String ptID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getValidAmount() {
        return validAmount;
    }

    public void setValidAmount(double validAmount) {
        this.validAmount = validAmount;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getPtID() {
        return ptID;
    }

    public void setPtID(String ptID) {
        this.ptID = ptID;
    }

}
