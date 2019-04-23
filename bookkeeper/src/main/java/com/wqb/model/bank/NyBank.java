package com.wqb.model.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * NyBank 实体类 2018-04-19 lch
 */

public class NyBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1519244608322867105L;
    private String nyBillID; // 农行主键
    private String accountID; // 账套ID
    private String period; // 会计期间
    private String vouchID; // 做账主凭证ID
    private String bankType; // 银行类型
    private String accountNum; // 账号
    private String typeCurrency; // 币种
    private String vouchNo; // 凭证号
    private Date transaction_time; // 日期
    private String jycode; // 交易代码
    private String transactionID; // 流水号
    private String transactionEmp; // 交易柜员
    private String jfdf; // 借贷方
    private BigDecimal fsAmount; // 发生金额
    private BigDecimal accountBalance; // 账户余额
    private String dfAccountNumber; // 对方账号
    private String dfAccountName; // 对方户名
    private String dfBankName; // 对方行名
    private String reference; // 摘要
    private String notes; // 附言
    private String userID; // 导入者
    private Long addTime; // 导入时间戳

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

    public void setNyBillID(String nyBillID) {
        this.nyBillID = nyBillID;
    }

    public String getNyBillID() {
        return nyBillID;
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

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setTypeCurrency(String typeCurrency) {
        this.typeCurrency = typeCurrency;
    }

    public String getTypeCurrency() {
        return typeCurrency;
    }

    public void setVouchNo(String vouchNo) {
        this.vouchNo = vouchNo;
    }

    public String getVouchNo() {
        return vouchNo;
    }

    public void setTransaction_time(Date transaction_time) {
        this.transaction_time = transaction_time;
    }

    public Date getTransaction_time() {
        return transaction_time;
    }

    public void setJycode(String jycode) {
        this.jycode = jycode;
    }

    public String getJycode() {
        return jycode;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionEmp(String transactionEmp) {
        this.transactionEmp = transactionEmp;
    }

    public String getTransactionEmp() {
        return transactionEmp;
    }

    public void setJfdf(String jfdf) {
        this.jfdf = jfdf;
    }

    public String getJfdf() {
        return jfdf;
    }

    public void setFsAmount(BigDecimal fsAmount) {
        this.fsAmount = fsAmount;
    }

    public BigDecimal getFsAmount() {
        return fsAmount;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
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

    public void setDfBankName(String dfBankName) {
        this.dfBankName = dfBankName;
    }

    public String getDfBankName() {
        return dfBankName;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getAddTime() {
        return addTime;
    }

    @Override
    public String toString() {
        return "NyBank [nyBillID=" + nyBillID + ", accountID=" + accountID + ", period=" + period + ", vouchID="
                + vouchID + ", bankType=" + bankType + ", accountNum=" + accountNum + ", typeCurrency=" + typeCurrency
                + ", vouchNo=" + vouchNo + ", transaction_time=" + transaction_time + ", jycode=" + jycode
                + ", transactionID=" + transactionID + ", transactionEmp=" + transactionEmp + ", jfdf=" + jfdf
                + ", fsAmount=" + fsAmount + ", accountBalance=" + accountBalance + ", dfAccountNumber="
                + dfAccountNumber + ", dfAccountName=" + dfAccountName + ", dfBankName=" + dfBankName + ", reference="
                + reference + ", notes=" + notes + ", userID=" + userID + ", addTime=" + addTime + "]";
    }

}
