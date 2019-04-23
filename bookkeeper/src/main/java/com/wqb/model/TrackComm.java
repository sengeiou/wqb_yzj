package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TrackComm 实体类
 * 2018-03-28 lch
 */


public class TrackComm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3129048149307143967L;
    private String tr_comm_id;    //
    private String sub_code;    //科目编码
    private String comNameSpec;    //商品名称规格
    private BigDecimal amount;    //金额
    private BigDecimal number;    //
    private String direction;    //方向
    private Date upDate;    //更新时间
    private String accountID;    //账套
    private String period;    //期间
    private String des;    //期间

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setTr_comm_id(String tr_comm_id) {
        this.tr_comm_id = tr_comm_id;
    }

    public String getTr_comm_id() {
        return tr_comm_id;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
    }

    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public Date getUpDate() {
        return upDate;
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

    @Override
    public String toString() {
        return "TrackComm [sub_code=" + sub_code + ", comNameSpec=" + comNameSpec + ", amount=" + amount + ", number="
                + number + ", direction=" + direction + ", des=" + des + "]";
    }


}

