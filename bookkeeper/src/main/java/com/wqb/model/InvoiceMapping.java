package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * InvoiceMapping 实体类
 * 2018-11-07 lch
 */


public class InvoiceMapping implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3996940561850559159L;
    private String mapping_id;    //主键
    private String invoiceHID;    //发票主表(外键)
    private String invoiceBID;    //发票子表(外键)
    private String invoiceCode;    //发票编码
    private String invoiceNumber;    //发票号码
    private String invoiceType;    //1进项2销项
    private String accountID;    //账套ID
    private String period;    //期间
    private String mapping_sub_code;    //映射科目编码
    private BigDecimal price;    //商品单价
    private BigDecimal amount;    //商品金额
    private BigDecimal number;    //商品数量
    private String comName;    //商品名称
    private String spec;    //商品规格
    private String comNameSpec;    //商品名称与规格
    private String measure;    //计量单位
    private Date createDate;    //修改时间
    private Integer sourt;    //排序
    private String buyCorp;    //购方公司
    private String saleCorp;    //买方公司

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getInvoiceHID() {
        return invoiceHID;
    }

    public void setInvoiceHID(String invoiceHID) {
        this.invoiceHID = invoiceHID;
    }

    public String getInvoiceBID() {
        return invoiceBID;
    }

    public void setInvoiceBID(String invoiceBID) {
        this.invoiceBID = invoiceBID;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
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

    public String getMapping_sub_code() {
        return mapping_sub_code;
    }

    public void setMapping_sub_code(String mapping_sub_code) {
        this.mapping_sub_code = mapping_sub_code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSourt() {
        return sourt;
    }

    public void setSourt(Integer sourt) {
        this.sourt = sourt;
    }

    public String getBuyCorp() {
        return buyCorp;
    }

    public void setBuyCorp(String buyCorp) {
        this.buyCorp = buyCorp;
    }

    public String getSaleCorp() {
        return saleCorp;
    }

    public void setSaleCorp(String saleCorp) {
        this.saleCorp = saleCorp;
    }

    @Override
    public String toString() {
        return "InvoiceMapping [mapping_id=" + mapping_id + ", invoiceHID=" + invoiceHID + ", invoiceBID=" + invoiceBID
                + ", invoiceCode=" + invoiceCode + ", invoiceNumber=" + invoiceNumber + ", invoiceType=" + invoiceType
                + ", accountID=" + accountID + ", period=" + period + ", mapping_sub_code=" + mapping_sub_code
                + ", price=" + price + ", amount=" + amount + ", number=" + number + ", comName=" + comName + ", spec="
                + spec + ", comNameSpec=" + comNameSpec + ", measure=" + measure + ", createDate=" + createDate
                + ", sourt=" + sourt + ", buyCorp=" + buyCorp + ", saleCorp=" + saleCorp + "]";
    }


}

