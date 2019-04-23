package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

public class BankBill implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5663314185137866714L;
    // 主键ID
    private String id;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 凭证号
    private String vouchID;
    // 导入时间
    private long busDate;
    // 交易日期
    private Date transactionDate;
    // 摘要
    private String des;
    // 收入金额
    private Double debitAmount;
    // 支出金额
    private Double creditAmount;
    // 账户余额
    private Double accountBalance;
    // 对方户名
    private String dfAccountName;
    // 对方账号
    private String dfAccountNumber;
    // 凭证号码
    private String voucherNo;
    // 备注
    private String bz;
    // 银行类型
    private String bankType;
    // 科目主键
    private String subID;
    // 科目名称
    private String subName;
    // 科目代码
    private String subCode;
    // 银行类型标识
    private Integer intBankType;
    // 科目全名
    private String subFullName;
    // 是否社保
    private Boolean isSb;
    // 是否公积金
    private Boolean isGjj;

    public Boolean getIsSb() {
        return isSb;
    }

    public void setIsSb(Boolean isSb) {
        this.isSb = isSb;
    }

    public Boolean getIsGjj() {
        return isGjj;
    }

    public void setIsGjj(Boolean isGjj) {
        this.isGjj = isGjj;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSubFullName() {
        return subFullName;
    }

    public void setSubFullName(String subFullName) {
        this.subFullName = subFullName;
    }

    public Integer getIntBankType() {
        return intBankType;
    }

    public void setIntBankType(Integer intBankType) {
        this.intBankType = intBankType;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
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

    public long getBusDate() {
        return busDate;
    }

    public void setBusDate(long busDate) {
        this.busDate = busDate;
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

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
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

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Override
    public String toString() {
        return "BankBill [id=" + id + ", accountID=" + accountID + ", period=" + period + ", vouchID=" + vouchID
                + ", busDate=" + busDate + ", transactionDate=" + transactionDate + ", des=" + des + ", debitAmount="
                + debitAmount + ", creditAmount=" + creditAmount + ", accountBalance=" + accountBalance
                + ", dfAccountName=" + dfAccountName + ", dfAccountNumber=" + dfAccountNumber + ", voucherNo="
                + voucherNo + ", bz=" + bz + ", bankType=" + bankType + "]";
    }

}
