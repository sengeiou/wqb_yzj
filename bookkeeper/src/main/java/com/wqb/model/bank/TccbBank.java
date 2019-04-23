package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

/**
 * 中信银行对账单
 *
 * @author zhushuyuan
 */
public class TccbBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1429548784909803437L;
    // 中信银行对账单主键
    private String zxBillID;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 导入时间
    private long busDate;
    // 交易时间
    private Date transactionDate;
    // 起息时间
    private Date carryInterest;
    // 主机交易号
    private String masterTransactionNum;
    // 柜员交易号
    private String gyTransaction;
    // 被冲账标志
    private String bczFlag;
    // 非金融标识
    private String fjrFlag;
    // 借方金额
    private double debitAmount;
    // 贷方金额
    private double creditAmount;
    // 账户余额
    private double accountBalance;
    // 现转标识
    private String xzFlag;
    // 摘要代码1
    private String descCode1;
    // 摘要代码2
    private String descCode2;
    // 摘要
    private String des;
    // 制单人ID
    private String createPresonID;
    // 制单操作员姓名
    private String ceeatePersonName;
    // 复核ID
    private String checkID;
    // 复核员姓名
    private String checkPersonName;
    // 账号
    private String accountNO;
    // 外行账户名称
    private String whAccountName;
    // 外行开户行名称
    private String whkhhName;
    // 交易时间
    private String transactionTime;
    // 总交易流水号
    private String transactionID;
    // 备注
    private String bz;
    // 退票标识
    private String tpFlag;
    // 退票日期
    private Date tpDate;
    // 退票场次
    private String tpcc;
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

    public String getZxBillID() {
        return zxBillID;
    }

    public void setZxBillID(String zxBillID) {
        this.zxBillID = zxBillID;
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getCarryInterest() {
        return carryInterest;
    }

    public void setCarryInterest(Date carryInterest) {
        this.carryInterest = carryInterest;
    }

    public String getMasterTransactionNum() {
        return masterTransactionNum;
    }

    public void setMasterTransactionNum(String masterTransactionNum) {
        this.masterTransactionNum = masterTransactionNum;
    }

    public String getBczFlag() {
        return bczFlag;
    }

    public void setBczFlag(String bczFlag) {
        this.bczFlag = bczFlag;
    }

    public String getFjrFlag() {
        return fjrFlag;
    }

    public void setFjrFlag(String fjrFlag) {
        this.fjrFlag = fjrFlag;
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

    public String getXzFlag() {
        return xzFlag;
    }

    public void setXzFlag(String xzFlag) {
        this.xzFlag = xzFlag;
    }

    public String getDescCode1() {
        return descCode1;
    }

    public void setDescCode1(String descCode1) {
        this.descCode1 = descCode1;
    }

    public String getDescCode2() {
        return descCode2;
    }

    public void setDescCode2(String descCode2) {
        this.descCode2 = descCode2;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCreatePresonID() {
        return createPresonID;
    }

    public void setCreatePresonID(String createPresonID) {
        this.createPresonID = createPresonID;
    }

    public String getCeeatePersonName() {
        return ceeatePersonName;
    }

    public void setCeeatePersonName(String ceeatePersonName) {
        this.ceeatePersonName = ceeatePersonName;
    }

    public String getCheckID() {
        return checkID;
    }

    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }

    public String getCheckPersonName() {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

    public String getWhAccountName() {
        return whAccountName;
    }

    public void setWhAccountName(String whAccountName) {
        this.whAccountName = whAccountName;
    }

    public String getWhkhhName() {
        return whkhhName;
    }

    public void setWhkhhName(String whkhhName) {
        this.whkhhName = whkhhName;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getTpFlag() {
        return tpFlag;
    }

    public void setTpFlag(String tpFlag) {
        this.tpFlag = tpFlag;
    }

    public Date getTpDate() {
        return tpDate;
    }

    public void setTpDate(Date tpDate) {
        this.tpDate = tpDate;
    }

    public String getGyTransaction() {
        return gyTransaction;
    }

    public void setGyTransaction(String gyTransaction) {
        this.gyTransaction = gyTransaction;
    }

    public String getTpcc() {
        return tpcc;
    }

    public void setTpcc(String tpcc) {
        this.tpcc = tpcc;
    }

}
