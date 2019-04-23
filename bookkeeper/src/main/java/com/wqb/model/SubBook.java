package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SubBook 实体类
 * 2018-07-30 lch
 */


public class SubBook implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5966158542313841096L;
    private Integer sub_bk_id;    //主键
    private String accountID;    //账套
    private String period;    //期间
    private String vouchID;    //凭证主键
    private String vouchAID;    //凭证体主键
    private Integer vouchNum;    //凭证号
    private String vcabstact;    //摘要
    private String sub_code;    //科目编码
    private String sub_name;    //科目名称
    private BigDecimal debitAmount;    //金额
    private BigDecimal creditAmount;    //金额
    private BigDecimal blanceAmount;    //金额
    private String direction;    //科目方向
    private Date updateDate;    //更新时间
    private Long up_date;    //时间戳
    private Integer isEndSubCode;    //是否为更新末级科目

    public Integer getSub_bk_id() {
        return sub_bk_id;
    }

    public void setSub_bk_id(Integer sub_bk_id) {
        this.sub_bk_id = sub_bk_id;
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

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchAID(String vouchAID) {
        this.vouchAID = vouchAID;
    }

    public String getVouchAID() {
        return vouchAID;
    }

    public Integer getVouchNum() {
        return vouchNum;
    }

    public void setVouchNum(Integer vouchNum) {
        this.vouchNum = vouchNum;
    }

    public void setVcabstact(String vcabstact) {
        this.vcabstact = vcabstact;
    }

    public String getVcabstact() {
        return vcabstact;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getBlanceAmount() {
        return blanceAmount;
    }

    public void setBlanceAmount(BigDecimal blanceAmount) {
        this.blanceAmount = blanceAmount;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getUp_date() {
        return up_date;
    }

    public void setUp_date(Long up_date) {
        this.up_date = up_date;
    }

    public Integer getIsEndSubCode() {
        return isEndSubCode;
    }

    public void setIsEndSubCode(Integer isEndSubCode) {
        this.isEndSubCode = isEndSubCode;
    }

    @Override
    public String toString() {
        return "SubBook [sub_bk_id=" + sub_bk_id + ", accountID=" + accountID + ", period=" + period + ", vouchID="
                + vouchID + ", vouchAID=" + vouchAID + ", vouchNum=" + vouchNum + ", vcabstact=" + vcabstact
                + ", sub_code=" + sub_code + ", sub_name=" + sub_name + ", debitAmount=" + debitAmount
                + ", creditAmount=" + creditAmount + ", blanceAmount=" + blanceAmount + ", direction=" + direction
                + ", updateDate=" + updateDate + ", up_date=" + up_date + ", isEndSubCode=" + isEndSubCode + "]";
    }


}

