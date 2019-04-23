package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * KcCommodityJx 实体类
 * 2018-02-26 lch
 */


public class KcCommodityJx implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2213414312317321644L;
    private String comID;    //主键
    private String accountID;    //账套ID
    private String period;    //期间
    private String comName;    //商品名称
    private String direction;    //借贷方向
    private Double balanceNum;    //结存数量
    private BigDecimal balancePrice;    //结存单价
    private BigDecimal balanceAmount;    //结存金额
    private BigDecimal qcAmount;    //期初金额
    private Double qcNum;    //期初数量
    private String updatePsnID;    //修改人ID
    private String updatePsn;    //修改人
    private Date updatedate;    //修改时间
    private String createPsnID;    //创建人ID
    private Date createDate;    //创建时间
    private String createPsn;    //创建人
    private String des;    //说明备注
    private Date balanceDate;    //结存时间

    public void setComID(String comID) {
        this.comID = comID;
    }

    public String getComID() {
        return comID;
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

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComName() {
        return comName;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setBalanceNum(Double balanceNum) {
        this.balanceNum = balanceNum;
    }

    public Double getBalanceNum() {
        return balanceNum;
    }

    public void setBalancePrice(BigDecimal balancePrice) {
        this.balancePrice = balancePrice;
    }

    public BigDecimal getBalancePrice() {
        return balancePrice;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setQcAmount(BigDecimal qcAmount) {
        this.qcAmount = qcAmount;
    }

    public BigDecimal getQcAmount() {
        return qcAmount;
    }

    public Double getQcNum() {
        return qcNum;
    }

    public void setQcNum(Double qcNum) {
        this.qcNum = qcNum;
    }

    public void setUpdatePsnID(String updatePsnID) {
        this.updatePsnID = updatePsnID;
    }

    public String getUpdatePsnID() {
        return updatePsnID;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }

    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn;
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    @Override
    public String toString() {
        return "KcCommodityJx [comID=" + comID + ", accountID=" + accountID + ", period=" + period + ", comName="
                + comName + ", direction=" + direction + ", balanceNum=" + balanceNum + ", balancePrice=" + balancePrice
                + ", balanceAmount=" + balanceAmount + ", qcAmount=" + qcAmount + ", qcNum=" + qcNum + ", updatePsnID="
                + updatePsnID + ", updatePsn=" + updatePsn + ", updatedate=" + updatedate + ", createPsnID="
                + createPsnID + ", createDate=" + createDate + ", createPsn=" + createPsn + ", des=" + des
                + ", balanceDate=" + balanceDate + "]";
    }


}

