package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TrackSub 实体类
 * 2018-03-28 lch
 */


public class TrackSub implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8732470609161741036L;
    private String tr_sub_id;    //
    private String sub_name;    //科目名称
    private String sub_code;    //科目编码
    private BigDecimal amount;    //金额
    private String direction;    //方向
    private String accountID;    //账套
    private String period;    //期间
    private String des;    //期间
    private Date updateDate;    //期间

    public void setTr_sub_id(String tr_sub_id) {
        this.tr_sub_id = tr_sub_id;
    }

    public String getTr_sub_id() {
        return tr_sub_id;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "TrackSub [sub_name=" + sub_name + ", sub_code=" + sub_code + ", amount=" + amount + ", direction="
                + direction + ", accountID=" + accountID + ", period=" + period + ", des=" + des + "]";
    }


}

