package com.wqb.model;

import java.io.Serializable;

public class FirstPageEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3465337519387072400L;
    private String cusName;
    private String telephone;
    private String period;
    private String clName;
    private String statu;
    private Integer cv;
    private Integer cs;
    private Integer jz;
    private double jd;
    private String accountID;
    private String ableDate;
    private String disableDate;
    private String createDate;
    private String operation;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Integer getCv() {
        return cv;
    }

    public void setCv(Integer cv) {
        this.cv = cv;
    }

    public Integer getCs() {
        return cs;
    }

    public void setCs(Integer cs) {
        this.cs = cs;
    }

    public Integer getJz() {
        return jz;
    }

    public void setJz(Integer jz) {
        this.jz = jz;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getClName() {
        return clName;
    }

    public void setClName(String clName) {
        this.clName = clName;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public String getAbleDate() {
        return ableDate;
    }

    public void setAbleDate(String ableDate) {
        this.ableDate = ableDate;
    }

    public String getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(String disableDate) {
        this.disableDate = disableDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "FirstPageEntity [cusName=" + cusName + ", telephone=" + telephone + ", period=" + period + ", clName="
                + clName + ", statu=" + statu + ", cv=" + cv + ", cs=" + cs + ", jz=" + jz + ", jd=" + jd
                + ", accountID=" + accountID + ", ableDate=" + ableDate + ", disableDate=" + disableDate
                + ", createDate=" + createDate + ", operation=" + operation + "]";
    }


}
