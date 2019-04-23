package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 收据表
 * </p>
 *
 * @author Shoven
 * @since 2019-04-15
 */
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收据主键
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 帐套id
     */
    private String accountId;

    /**
     * 会计期间
     */
    private String accountPeriod;

    /**
     * 收据摘要
     */
    private String summary;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 税率%
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 合计金额
     */
    private BigDecimal totalAmount;

    /**
     * 收据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据 5其他单据)
     */
    private Integer type;

    /**
     * 收据日期
     */
    private Date date;

    /**
     * 凭证id
     */
    private String voucherId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 科目代码
     */
    private String subjectCode;

    /**
     * 科目名
     */
    private String subjectName;

    /**
     * 科目全名
     */
    private String subjectFullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectFullName() {
        return subjectFullName;
    }

    public void setSubjectFullName(String subjectFullName) {
        this.subjectFullName = subjectFullName;
    }


    @Override
    public String toString() {
        return "Receipt{" +
        "id=" + id +
        ", userId=" + userId +
        ", accountId=" + accountId +
        ", accountPeriod=" + accountPeriod +
        ", summary=" + summary +
        ", unitName=" + unitName +
        ", number=" + number +
        ", unitPrice=" + unitPrice +
        ", taxRate=" + taxRate +
        ", taxAmount=" + taxAmount +
        ", totalAmount=" + totalAmount +
        ", type=" + type +
        ", date=" + date +
        ", voucherId=" + voucherId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", subjectCode=" + subjectCode +
        ", subjectName=" + subjectName +
        ", subjectFullName=" + subjectFullName +
        "}";
    }
}
