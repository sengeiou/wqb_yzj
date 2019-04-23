package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

/**
 * JsBank 实体类 2018-01-11 lch
 */
public class JsBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1007532442344117481L;
    private String jsBillID; // 建设银行对账单主键
    private String accountID; // 账套ID
    private String vouchID; // 做账主凭证ID
    private String period; // 会计期间
    private Date transaction_time; // 交易时间
    private Double jffsAmount; // 借方发生额/元(支取)
    private Double dffsAmount; // 贷方发生额/元(收入)
    private Double balance; // 余额
    private String typeCurrency; // 币种
    private String dfAccountName; // 对方户名
    private String dfAccountNumber; // 对方账号
    private String khhName; // 对方开户机构
    private Date transaction_date; // 记账日期
    private String reference; // 摘要
    private String remarks; // 备注
    private String transactionID; // 账户明细编号-交易流水号
    private String companyTransactionID; // 企业流水号
    private String vouchType; // 凭证种类
    private String vouchNo; // 凭证号
    private String glAccount; // 关联账户
    private long importDate; // 导入时间
    private String bankType; // 银行名称

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

    public void setJsBillID(String jsBillID) {
        this.jsBillID = jsBillID;
    }

    public String getJsBillID() {
        return jsBillID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setTransaction_time(Date transaction_time) {
        this.transaction_time = transaction_time;
    }

    public Date getTransaction_time() {
        return transaction_time;
    }

    public Double getJffsAmount() {
        return jffsAmount;
    }

    public void setJffsAmount(Double jffsAmount) {
        this.jffsAmount = jffsAmount;
    }

    public Double getDffsAmount() {
        return dffsAmount;
    }

    public void setDffsAmount(Double dffsAmount) {
        this.dffsAmount = dffsAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setTypeCurrency(String typeCurrency) {
        this.typeCurrency = typeCurrency;
    }

    public String getTypeCurrency() {
        return typeCurrency;
    }

    public void setDfAccountName(String dfAccountName) {
        this.dfAccountName = dfAccountName;
    }

    public String getDfAccountName() {
        return dfAccountName;
    }

    public void setDfAccountNumber(String dfAccountNumber) {
        this.dfAccountNumber = dfAccountNumber;
    }

    public String getDfAccountNumber() {
        return dfAccountNumber;
    }

    public void setKhhName(String khhName) {
        this.khhName = khhName;
    }

    public String getKhhName() {
        return khhName;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setCompanyTransactionID(String companyTransactionID) {
        this.companyTransactionID = companyTransactionID;
    }

    public String getCompanyTransactionID() {
        return companyTransactionID;
    }

    public void setVouchType(String vouchType) {
        this.vouchType = vouchType;
    }

    public String getVouchType() {
        return vouchType;
    }

    public void setVouchNo(String vouchNo) {
        this.vouchNo = vouchNo;
    }

    public String getVouchNo() {
        return vouchNo;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(long importDate) {
        this.importDate = importDate;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankType() {
        return bankType;
    }
}
