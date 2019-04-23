package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

public class NyBankNew implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3005820438221899546L;
    // 记录主键
    private String id;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 凭证ID
    private String vouchID;
    // 交易日期
    private Date transactionTime;
    // 交易时间戳
    private String transactionStamp;
    // 收入金额
    private double srAmount;
    // 支出金额
    private double zcAmount;
    // 账户余额
    private double zhAmount;
    // 本次余额
    private double bcAmount;
    // 手续费总额
    private double sxfAmont;
    // 交易方式
    private String jyType;
    // 交易行名
    private String jyhm;
    // 交易类别
    private String jylb;
    // 对方省市
    private String dfss;
    // 对方账号
    private String dfzh;
    // 对方户名
    private String dfhm;
    // 交易用途
    private String jyyt;
    // 交易说明
    private String jysm;
    // 交易摘要
    private String jyzy;
    // 交易附言
    private String jyfy;
    // 导入时间戳
    private Long addTime;
    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键F
    private String sysSubjectID;
    // 科目全名
    private String sysSubjectFullName;
    //银行账户
    private String bankAccount;


    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSysSubjectCode() {
        return sysSubjectCode;
    }

    public void setSysSubjectCode(String sysSubjectCode) {
        this.sysSubjectCode = sysSubjectCode;
    }

    public String getSysSubjectName() {
        return sysSubjectName;
    }

    public void setSysSubjectName(String sysSubjectName) {
        this.sysSubjectName = sysSubjectName;
    }

    public String getSysSubjectID() {
        return sysSubjectID;
    }

    public void setSysSubjectID(String sysSubjectID) {
        this.sysSubjectID = sysSubjectID;
    }

    public String getSysSubjectFullName() {
        return sysSubjectFullName;
    }

    public void setSysSubjectFullName(String sysSubjectFullName) {
        this.sysSubjectFullName = sysSubjectFullName;
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

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionStamp() {
        return transactionStamp;
    }

    public void setTransactionStamp(String transactionStamp) {
        this.transactionStamp = transactionStamp;
    }

    public double getSrAmount() {
        return srAmount;
    }

    public void setSrAmount(double srAmount) {
        this.srAmount = srAmount;
    }

    public double getZcAmount() {
        return zcAmount;
    }

    public void setZcAmount(double zcAmount) {
        this.zcAmount = zcAmount;
    }

    public double getZhAmount() {
        return zhAmount;
    }

    public void setZhAmount(double zhAmount) {
        this.zhAmount = zhAmount;
    }

    public double getBcAmount() {
        return bcAmount;
    }

    public void setBcAmount(double bcAmount) {
        this.bcAmount = bcAmount;
    }

    public double getSxfAmont() {
        return sxfAmont;
    }

    public void setSxfAmont(double sxfAmont) {
        this.sxfAmont = sxfAmont;
    }

    public String getJyType() {
        return jyType;
    }

    public void setJyType(String jyType) {
        this.jyType = jyType;
    }

    public String getJyhm() {
        return jyhm;
    }

    public void setJyhm(String jyhm) {
        this.jyhm = jyhm;
    }

    public String getJylb() {
        return jylb;
    }

    public void setJylb(String jylb) {
        this.jylb = jylb;
    }

    public String getDfss() {
        return dfss;
    }

    public void setDfss(String dfss) {
        this.dfss = dfss;
    }

    public String getDfzh() {
        return dfzh;
    }

    public void setDfzh(String dfzh) {
        this.dfzh = dfzh;
    }

    public String getDfhm() {
        return dfhm;
    }

    public void setDfhm(String dfhm) {
        this.dfhm = dfhm;
    }

    public String getJyyt() {
        return jyyt;
    }

    public void setJyyt(String jyyt) {
        this.jyyt = jyyt;
    }

    public String getJysm() {
        return jysm;
    }

    public void setJysm(String jysm) {
        this.jysm = jysm;
    }

    public String getJyzy() {
        return jyzy;
    }

    public void setJyzy(String jyzy) {
        this.jyzy = jyzy;
    }

    public String getJyfy() {
        return jyfy;
    }

    public void setJyfy(String jyfy) {
        this.jyfy = jyfy;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

}
