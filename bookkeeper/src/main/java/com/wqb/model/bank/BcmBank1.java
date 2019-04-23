package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

/**
 * 交通银行新模板(谭显众提供)
 *
 * @author zhushuyuan
 */
public class BcmBank1 implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4946729895851841028L;
    // 交通银行对账单主键
    private String bcmBillID;
    // 账套ID
    private String accountID;
    // 会计期间
    private String period;
    // 导入时间
    private long busDate;
    // 交易时间
    private Date transactionTime;
    // 摘要
    private String des;
    // 余额
    private double banalce;
    // 对方账号
    private String dfAccount;
    // 对方户名
    private String dfAccountName;
    // 凭证种类
    private String vouchType;
    // 凭证号码
    private String vouchNo;
    // 企业业务编号
    private String qyywNumber;
    // 发生额
    private double zzje;
    // 借贷标志
    private String jdFlag;
    // 凭证主键
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

    public String getBcmBillID() {
        return bcmBillID;
    }

    public void setBcmBillID(String bcmBillID) {
        this.bcmBillID = bcmBillID;
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

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public double getBanalce() {
        return banalce;
    }

    public void setBanalce(double banalce) {
        this.banalce = banalce;
    }

    public String getDfAccount() {
        return dfAccount;
    }

    public void setDfAccount(String dfAccount) {
        this.dfAccount = dfAccount;
    }

    public String getDfAccountName() {
        return dfAccountName;
    }

    public void setDfAccountName(String dfAccountName) {
        this.dfAccountName = dfAccountName;
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

    public String getQyywNumber() {
        return qyywNumber;
    }

    public void setQyywNumber(String qyywNumber) {
        this.qyywNumber = qyywNumber;
    }

    public double getZzje() {
        return zzje;
    }

    public void setZzje(double zzje) {
        this.zzje = zzje;
    }

    public String getJdFlag() {
        return jdFlag;
    }

    public void setJdFlag(String jdFlag) {
        this.jdFlag = jdFlag;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
}
