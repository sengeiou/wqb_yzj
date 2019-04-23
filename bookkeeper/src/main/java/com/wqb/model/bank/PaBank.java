package com.wqb.model.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平安银行 实体类 2018-01-11 lch
 */

public class PaBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4930644225864757373L;
    private String paBillID; // 平安银行对账单主键
    private String accountID; // 账套ID
    private String vouchID; // 做账主凭证ID
    private String period; // 会计期间
    private Date transaction_time; // 交易日期
    private String accountNumber; // 账号
    private BigDecimal debitAmount; // 借
    private BigDecimal creditAmount; // 贷
    private BigDecimal balance; // 账户余额
    private String dfAccountNumber; // 对方账户
    private String dfAccountName; // 对方账户名称
    private String transactionID; // 交易流水号
    private String reference; // 摘要
    private String purpose; // 用途
    private long importDate; // 导入时间
    private String bankType; // 银行名称
    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键F
    private String sysSubjectID;
    // 科目全名
    private String sysSubjectFullName;

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

    public void setPaBillID(String paBillID) {
        this.paBillID = paBillID;
    }

    public String getPaBillID() {
        return paBillID;
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

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setDfAccountNumber(String dfAccountNumber) {
        this.dfAccountNumber = dfAccountNumber;
    }

    public String getDfAccountNumber() {
        return dfAccountNumber;
    }

    public void setDfAccountName(String dfAccountName) {
        this.dfAccountName = dfAccountName;
    }

    public String getDfAccountName() {
        return dfAccountName;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
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

    @Override
    public String toString() {
        return "PaBank [paBillID=" + paBillID + ", accountID=" + accountID + ", vouchID=" + vouchID + ", period="
                + period + ", transaction_time=" + transaction_time + ", accountNumber=" + accountNumber
                + ", debitAmount=" + debitAmount + ", creditAmount=" + creditAmount + ", balance=" + balance
                + ", dfAccountNumber=" + dfAccountNumber + ", dfAccountName=" + dfAccountName + ", transactionID="
                + transactionID + ", reference=" + reference + ", purpose=" + purpose + ", importDate=" + importDate
                + ", bankType=" + bankType + "]";
    }

}
