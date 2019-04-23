package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

public class SzrcbBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4553699661630082525L;
    // 中信银行对账单主键
    private String szrcbBillID;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 导入时间
    private long busDate;
    // 序号
    private String rowNumber;
    // 交易日期
    private Date transactionDate;
    // 摘要
    private String des;
    // 收入金额
    private double debitAmount;
    // 支出金额
    private double creditAmount;
    // 账户余额
    private double accountBalance;
    // 对方户名
    private String dfAccountName;
    // 对方账号
    private String dfAccountNumber;
    // 凭证类型
    private String voucherType;
    // 凭证号码
    private String voucherNo;
    // 交易机构
    private String transjg;
    // 备注
    private String bz;
    // 凭证ID
    private String vouchID;
    // 银行类型
    private String bankType;
    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键
    private String sysSubjectID;
    // 科目全名
    private String sysSubjectFullName;
    // 银行账户
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

    public String getSzrcbBillID() {
        return szrcbBillID;
    }

    public void setSzrcbBillID(String szrcbBillID) {
        this.szrcbBillID = szrcbBillID;
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

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getDfAccountName() {
        return dfAccountName;
    }

    public void setDfAccountName(String dfAccountName) {
        this.dfAccountName = dfAccountName;
    }

    public String getDfAccountNumber() {
        return dfAccountNumber;
    }

    public void setDfAccountNumber(String dfAccountNumber) {
        this.dfAccountNumber = dfAccountNumber;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getTransjg() {
        return transjg;
    }

    public void setTransjg(String transjg) {
        this.transjg = transjg;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
