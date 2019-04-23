package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 银行账户和科目映射表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-20
 */
@TableName("t_basic_bankaccount2subject")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 银行账户
     */
    @TableField("bankAccount")
    private String bankAccount;

    /**
     * 银行名称
     */
    @TableField("bankName")
    private String bankName;

    /**
     * 银行类型
     */
    @TableField("bankType")
    private String bankType;

    /**
     * 币种
     */
    private String currency;

    /**
     * 银行科目主键
     */
    @TableField("subID")
    private String subID;

    /**
     * 银行科目编码
     */
    @TableField("subCode")
    private String subCode;

    /**
     * 科目名称
     */
    @TableField("subName")
    private String subName;

    /**
     * 科目全名
     */
    @TableField("subFullName")
    private String subFullName;

    /**
     * 创建人
     */
    @TableField("createID")
    private String createID;

    /**
     * 创建人
     */
    @TableField("createName")
    private String createName;

    /**
     * 创建时间戳
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 创建者手机号
     */
    @TableField("createTel")
    private String createTel;

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
    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }
    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
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
    public String getCreateID() {
        return createID;
    }

    public void setCreateID(String createID) {
        this.createID = createID;
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
    public String getCreateTel() {
        return createTel;
    }

    public void setCreateTel(String createTel) {
        this.createTel = createTel;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
        "id=" + id +
        ", accountID=" + accountID +
        ", bankAccount=" + bankAccount +
        ", bankName=" + bankName +
        ", bankType=" + bankType +
        ", currency=" + currency +
        ", subID=" + subID +
        ", subCode=" + subCode +
        ", subName=" + subName +
        ", subFullName=" + subFullName +
        ", createID=" + createID +
        ", createName=" + createName +
        ", createTime=" + createTime +
        ", createTel=" + createTel +
        "}";
    }
}
