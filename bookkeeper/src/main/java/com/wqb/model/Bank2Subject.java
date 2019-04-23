package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行账户与科目映射实体类
 *
 * @author zhushuyuan
 */
public class Bank2Subject implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4772156247354699343L;
    // 主键
    private String id;
    // 账套ID
    private String accountID;
    // 银行账户
    private String bankAccount;
    // 银行科目主键
    private String subID;
    // 银行名称
    private String bankName;
    // 银行类型
    private String bankType;
    // 币种
    private String currency;
    // 科目名称
    private String subName;
    // 科目全名
    private String subFullName;
    // 科目编码
    private String subCode;

    // 创建者ID
    private String createID;
    // 创建者手机号
    private String createTel;
    // 创建人名字
    private String createName;
    // 创建时间
    private Date createTime;

    public String getCreateID() {
        return createID;
    }

    public void setCreateID(String createID) {
        this.createID = createID;
    }

    public String getCreateTel() {
        return createTel;
    }

    public void setCreateTel(String createTel) {
        this.createTel = createTel;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
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

    public String getSubFullName() {
        return subFullName;
    }

    public void setSubFullName(String subFullName) {
        this.subFullName = subFullName;
    }

}
