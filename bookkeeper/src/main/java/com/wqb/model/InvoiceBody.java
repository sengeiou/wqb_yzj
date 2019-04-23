package com.wqb.model;

import java.io.Serializable;

/**
 * 发票子表 实体
 */


public class InvoiceBody implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8692555060273071472L;
    // 主键
    private String invoiceBID;
    // 发票主表主键(外键)
    private String invoiceHID;
    // 商品名称
    private String comName;
    // 规格
    private String spec;
    // 计量单位ID(外键)
    private String measureID;
    // 计量单位
    private String measure;
    // 单价
    private Double nprice;
    // 数量
    private Double nnumber;
    // 金额
    private Double namount;
    // 税额
    private Double taxAmount;
    // 价税合计
    private Double ntaxAmount;
    // 税种类别
    private String taxClass;
    // 创建人ID
    private String createPsnID;
    // 创建人
    private String createPsn;
    // 用户
    private String userID;
    // 隶属账套
    private String accountID;
    // 备注
    private String des;
    // 税率
    private String taxRate;
    // 税收分类编码
    private String taxTypeCode;
    // 商品名称规格
    private String comNameSpec;
    // 发票类型
    private String invoiceType;
    // 期间
    private String period;
    //映射科目
    private String sub_code;
    //映射科目全名
    private String sub_full_name;

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    public String getInvoiceBID() {
        return invoiceBID;
    }

    public void setInvoiceBID(String invoiceBID) {
        this.invoiceBID = invoiceBID;
    }

    public String getInvoiceHID() {
        return invoiceHID;
    }

    public void setInvoiceHID(String invoiceHID) {
        this.invoiceHID = invoiceHID;
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

    public String getMeasureID() {
        return measureID;
    }

    public void setMeasureID(String measureID) {
        this.measureID = measureID;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getNprice() {
        return nprice;
    }

    public void setNprice(Double nprice) {
        this.nprice = nprice;
    }

    public Double getNnumber() {
        return nnumber;
    }

    public void setNnumber(Double nnumber) {
        this.nnumber = nnumber;
    }

    public Double getNamount() {
        return namount;
    }

    public void setNamount(Double namount) {
        this.namount = namount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getNtaxAmount() {
        return ntaxAmount;
    }

    public void setNtaxAmount(Double ntaxAmount) {
        this.ntaxAmount = ntaxAmount;
    }

    public String getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
    }

    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
    }


    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_full_name() {
        return sub_full_name;
    }

    public void setSub_full_name(String sub_full_name) {
        this.sub_full_name = sub_full_name;
    }

    @Override
    public String toString() {
        return "InvoiceBody [invoiceBID=" + invoiceBID + ", invoiceHID=" + invoiceHID + ", period=" + period + ", comName=" + comName
                + ", spec=" + spec + ",  sub_code=" + sub_code + ",sub_full_name=" + sub_full_name + ", measure=" + measure + ", nprice=" + nprice + ", nnumber=" + nnumber
                + ", namount=" + namount + ", taxAmount=" + taxAmount + ", ntaxAmount=" + ntaxAmount + ", des=" + des
                + ", taxRate=" + taxRate + ", comNameSpec=" + comNameSpec + ", invoiceType=" + invoiceType + "]";
    }


}
