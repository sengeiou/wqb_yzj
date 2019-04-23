package com.wqb.model;

import java.io.Serializable;

public class AssetsRecord implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3537609315653262971L;
    // 主键(折旧主键)
    private String zjid;
    // 固定资产主键标识
    private String assetsID;
    // 实际月折旧额
    private double ssyzje;
    // 折旧期间
    private String period;
    // 账套ID
    private String accountID;

    public String getZjid() {
        return zjid;
    }

    public void setZjid(String zjid) {
        this.zjid = zjid;
    }

    public String getAssetsID() {
        return assetsID;
    }

    public void setAssetsID(String assetsID) {
        this.assetsID = assetsID;
    }

    public double getSsyzje() {
        return ssyzje;
    }

    public void setSsyzje(double ssyzje) {
        this.ssyzje = ssyzje;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

}
