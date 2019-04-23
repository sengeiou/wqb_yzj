package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class Subject implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8186325634610798748L;
    // 主键
    private String subjectID;
    // 用户ID
    private String userID;
    // 账套
    private String accountID;
    // 科目编码
    private String sunumber;
    // 科目名称
    private String suname;
    // 科目完整名称
    private String fullname;
    // 科目类型
    private String sutype;
    // 科目方向(1:借2:贷)
    private String sudirect;
    // 期初余额
    private double initBalace;
    // 借方金额
    private double totalDebit;
    // 贷方金额
    private double totalCredit;
    // 期末余额
    private double qmBalace;
    // 启用期间
    private Date beginPeriod;
    // 余额方向(1:借2:贷)
    private Integer balanceDirect;
    // 显示名称
    private String dispName;
    // 金额=数量*金额
    private double amount;
    // 数量
    private double number;
    // 单价(国际单位)
    private double price;
    // 计量单位(国际单位)
    private String unit;
    // 期初借方余额
    private double initJFBalance;
    // 期初贷方余额
    private double initDFBalance;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getSunumber() {
        return sunumber;
    }

    public void setSunumber(String sunumber) {
        this.sunumber = sunumber;
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSutype() {
        return sutype;
    }

    public void setSutype(String sutype) {
        this.sutype = sutype;
    }

    public String getSudirect() {
        return sudirect;
    }

    public void setSudirect(String sudirect) {
        this.sudirect = sudirect;
    }

    public double getInitBalace() {
        return initBalace;
    }

    public void setInitBalace(double initBalace) {
        this.initBalace = initBalace;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getQmBalace() {
        return qmBalace;
    }

    public void setQmBalace(double qmBalace) {
        this.qmBalace = qmBalace;
    }

    public Date getBeginPeriod() {
        return beginPeriod;
    }

    public void setBeginPeriod(Date beginPeriod) {
        this.beginPeriod = beginPeriod;
    }

    public Integer getBalanceDirect() {
        return balanceDirect;
    }

    public void setBalanceDirect(Integer balanceDirect) {
        this.balanceDirect = balanceDirect;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getInitJFBalance() {
        return initJFBalance;
    }

    public void setInitJFBalance(double initJFBalance) {
        this.initJFBalance = initJFBalance;
    }

    public double getInitDFBalance() {
        return initDFBalance;
    }

    public void setInitDFBalance(double initDFBalance) {
        this.initDFBalance = initDFBalance;
    }

}
