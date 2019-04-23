package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class SljeInit implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 820598043252756356L;
    // 数量金额式表主键
    private String id;
    // 期间
    private Date period;
    // 科目代码
    private String subNumber;
    // 科目名称
    private String suName;
    // 借贷方向
    private String jdDirect;
    // 期初结存数量
    private Double qcCount;
    // 期初结存单价
    private Double qcPrice;
    // 期初结存金额
    private Double qcAmount;
    // 本期收入数量
    private Double bqsrCount;
    // 本期收入金额
    private Double bqsrAmount;
    // 本期发出数量
    private Double bqfcCount;
    // 本期发出金额
    private Double bqfcAmount;
    // 本年累计收入数量
    private Double bnljsrCount;
    // 本年累计收入金额
    private Double bnljsrAmount;
    // 本年累计发出数量
    private Double bnljfcCount;
    // 本年累计发出金额
    private Double bnljfcAmount;
    // 余额借贷方向
    private String balanceJDDirect;
    // 期末结存数量
    private Double qmjcCount;
    // 期末结存单价
    private Double qmjcPrice;
    // 期末结存金额
    private Double qmjcAmount;
    // 账套ID
    private String accountID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getSubNumber() {
        return subNumber;
    }

    public void setSubNumber(String subNumber) {
        this.subNumber = subNumber;
    }

    public String getSuName() {
        return suName;
    }

    public void setSuName(String suName) {
        this.suName = suName;
    }

    public String getJdDirect() {
        return jdDirect;
    }

    public void setJdDirect(String jdDirect) {
        this.jdDirect = jdDirect;
    }

    public Double getQcCount() {
        return qcCount;
    }

    public void setQcCount(Double qcCount) {
        this.qcCount = qcCount;
    }

    public Double getQcPrice() {
        return qcPrice;
    }

    public void setQcPrice(Double qcPrice) {
        this.qcPrice = qcPrice;
    }

    public Double getQcAmount() {
        return qcAmount;
    }

    public void setQcAmount(Double qcAmount) {
        this.qcAmount = qcAmount;
    }

    public Double getBqsrCount() {
        return bqsrCount;
    }

    public void setBqsrCount(Double bqsrCount) {
        this.bqsrCount = bqsrCount;
    }

    public Double getBqsrAmount() {
        return bqsrAmount;
    }

    public void setBqsrAmount(Double bqsrAmount) {
        this.bqsrAmount = bqsrAmount;
    }

    public Double getBqfcCount() {
        return bqfcCount;
    }

    public void setBqfcCount(Double bqfcCount) {
        this.bqfcCount = bqfcCount;
    }

    public Double getBqfcAmount() {
        return bqfcAmount;
    }

    public void setBqfcAmount(Double bqfcAmount) {
        this.bqfcAmount = bqfcAmount;
    }

    public Double getBnljsrCount() {
        return bnljsrCount;
    }

    public void setBnljsrCount(Double bnljsrCount) {
        this.bnljsrCount = bnljsrCount;
    }

    public Double getBnljsrAmount() {
        return bnljsrAmount;
    }

    public void setBnljsrAmount(Double bnljsrAmount) {
        this.bnljsrAmount = bnljsrAmount;
    }

    public Double getBnljfcCount() {
        return bnljfcCount;
    }

    public void setBnljfcCount(Double bnljfcCount) {
        this.bnljfcCount = bnljfcCount;
    }

    public Double getBnljfcAmount() {
        return bnljfcAmount;
    }

    public void setBnljfcAmount(Double bnljfcAmount) {
        this.bnljfcAmount = bnljfcAmount;
    }

    public String getBalanceJDDirect() {
        return balanceJDDirect;
    }

    public void setBalanceJDDirect(String balanceJDDirect) {
        this.balanceJDDirect = balanceJDDirect;
    }

    public Double getQmjcCount() {
        return qmjcCount;
    }

    public void setQmjcCount(Double qmjcCount) {
        this.qmjcCount = qmjcCount;
    }

    public Double getQmjcPrice() {
        return qmjcPrice;
    }

    public void setQmjcPrice(Double qmjcPrice) {
        this.qmjcPrice = qmjcPrice;
    }

    public Double getQmjcAmount() {
        return qmjcAmount;
    }

    public void setQmjcAmount(Double qmjcAmount) {
        this.qmjcAmount = qmjcAmount;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

}
