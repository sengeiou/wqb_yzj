package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

public class IcbcBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4758060219370622153L;
    // 工商银行对账单主键
    private String icbcBillID;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 导入时间
    private long busDate;
    // 日期
    private Date rqDate;
    // 交易类型
    private String transactionType;
    // 凭证种类
    private String vouchType;
    // 凭证号
    private String vouchNo;
    // 对方账号
    private String dfAccount;
    // 摘要
    private String des;
    // 借方发生额
    private double jffsAmount;
    // 贷方发生额
    private double dffsAmount;
    // 余额
    private double balance;
    // 对方户名
    private String dfAccountName;
    // 凭证号
    private String vouchID;
    // 银行类型
    private String bankType;
    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键
    private String sysSubjectID;
    //科目全名
    private String sysSubjectFullName;
    //银行账户
    private String bankAccount;


    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSysSubjectFullName() {
        return sysSubjectFullName;
    }

    public void setSysSubjectFullName(String sysSubjectFullName) {
        this.sysSubjectFullName = sysSubjectFullName;
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

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getIcbcBillID() {
        return icbcBillID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDfAccountName() {
        return dfAccountName;
    }

    public void setDfAccountName(String dfAccountName) {
        this.dfAccountName = dfAccountName;
    }

    public void setIcbcBillID(String icbcBillID) {
        this.icbcBillID = icbcBillID;
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

    public long getBusDate() {
        return busDate;
    }

    public void setBusDate(long busDate) {
        this.busDate = busDate;
    }

    public Date getRqDate() {
        return rqDate;
    }

    public void setRqDate(Date rqDate) {
        this.rqDate = rqDate;
    }

    public String getVouchType() {
        return vouchType;
    }

    public void setVouchType(String vouchType) {
        this.vouchType = vouchType;
    }

    public String getVouchNo() {
        return vouchNo;
    }

    public void setVouchNo(String vouchNo) {
        this.vouchNo = vouchNo;
    }

    public String getDfAccount() {
        return dfAccount;
    }

    public void setDfAccount(String dfAccount) {
        this.dfAccount = dfAccount;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public double getJffsAmount() {
        return jffsAmount;
    }

    public void setJffsAmount(double jffsAmount) {
        this.jffsAmount = jffsAmount;
    }

    public double getDffsAmount() {
        return dffsAmount;
    }

    public void setDffsAmount(double dffsAmount) {
        this.dffsAmount = dffsAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
