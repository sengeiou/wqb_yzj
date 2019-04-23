package com.wqb.model.vomodel;

import java.io.Serializable;
import java.util.Date;

public class InvoiceMappingVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2751109930012332172L;
    // 发票编码
    private String invoiceCode;
    // 发票号码
    private String invoiceNumber;
    // 发票名称
    private String invoiceName;
    // 发票日期
    private Date invoiceDate;
    // 购方税号
    private String buyTaxno;

    // 购方公司名称
    private String buyCorp;
    // 购方账户
    private String buyBankno;
    // 销方公司名称
    private String saleCorp;
    // 销方税号
    private String saleTaxno;
    // 销方账户
    private String saleBankno;

    // 地址电话
    private String addressPhone;
    // 商品编码版本号
    private String productVersion;

    // 商品编码版本号
    // 导入时间
    private long importDate;

    /***********************/

    // 主键
    private String invoiceBID;
    // 发票主表主键(外键)
    private String invoiceHID;
    // 商品名称
    private String comName;
    // 规格
    private String spec;
    //商品名称规格
    private String comNameSpec;

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

    // 隶属账套
    private String accountID;
    // 税率
    private String taxRate;
    // 税收分类编码
    private String taxTypeCode;

    // 发票类型
    private String invoiceType;
    // 期间
    private String period;


    /***********************/
    private Integer sourt;

    // 映射科目
    private String mappingSubCode;
    //映射科目名称
    private String mappingSubName;


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

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getBuyTaxno() {
        return buyTaxno;
    }

    public void setBuyTaxno(String buyTaxno) {
        this.buyTaxno = buyTaxno;
    }

    public String getBuyCorp() {
        return buyCorp;
    }

    public void setBuyCorp(String buyCorp) {
        this.buyCorp = buyCorp;
    }

    public String getBuyBankno() {
        return buyBankno;
    }

    public void setBuyBankno(String buyBankno) {
        this.buyBankno = buyBankno;
    }

    public String getSaleCorp() {
        return saleCorp;
    }

    public void setSaleCorp(String saleCorp) {
        this.saleCorp = saleCorp;
    }

    public String getSaleTaxno() {
        return saleTaxno;
    }

    public void setSaleTaxno(String saleTaxno) {
        this.saleTaxno = saleTaxno;
    }

    public String getSaleBankno() {
        return saleBankno;
    }

    public void setSaleBankno(String saleBankno) {
        this.saleBankno = saleBankno;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
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

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

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

    public Integer getSourt() {
        return sourt;
    }

    public void setSourt(Integer sourt) {
        this.sourt = sourt;
    }

    public String getMappingSubCode() {
        return mappingSubCode;
    }

    public void setMappingSubCode(String mappingSubCode) {
        this.mappingSubCode = mappingSubCode;
    }

    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
    }

    public String getMappingSubName() {
        return mappingSubName;
    }

    public void setMappingSubName(String mappingSubName) {
        this.mappingSubName = mappingSubName;
    }

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(long importDate) {
        this.importDate = importDate;
    }

    @Override
    public String toString() {
        return "InvoiceMappingVo [invoiceCode=" + invoiceCode + ", invoiceNumber=" + invoiceNumber + ", buyCorp="
                + buyCorp + ", saleCorp=" + saleCorp + ", invoiceBID=" + invoiceBID + ", invoiceHID=" + invoiceHID
                + ", comName=" + comName + ", spec=" + spec + ", comNameSpec=" + comNameSpec + ", nnumber=" + nnumber
                + ", namount=" + namount + ", sourt=" + sourt + ", mappingSubCode="
                + mappingSubCode + ", mappingSubName=" + mappingSubName + "]";
    }


}
