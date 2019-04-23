package com.wqb.domains.tax;

import java.math.BigDecimal;
import java.util.Date;

public class BaiduVatTicke {

    private String InvoiceType;//发票种类名称
    private String InvoiceCode;//发票代码
    private String InvoiceNum;//发票号码
    private Date InvoiceDate;//开票日期
    private BigDecimal TotalAmount;//合计金额
    private BigDecimal TotalTax;//合计税额
    private BigDecimal AmountInFiguers;//	价税合计(小写)
    private String AmountInWords;//价税合计(大写)
    private String CheckCode;//校验码
    private String SellerName;//销售方名称
    private String SellerRegisterNum;//销售方纳税人识别号
    private String SellerBank;//销售方银行
    private String SellerAddress;//销售方地址
    private String PurchaserName;//购方名称
    private String PurchaserRegisterNum;//购方纳税人识别号
    private String PurchaserBank;//购方银行
    private String PurchaserAddress;//购方地址
    private String CommodityName;//	货物名称
    private String CommodityType;//	规格型号
    private String CommodityUnit;//	单位
    private String CommodityNum;//	数量
    private String CommodityPrice;//	单价
    private String CommodityAmount;//	金额
    private String CommodityTaxRate;//	税率
    private String CommodityTax;//	税额

    public String getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        InvoiceType = invoiceType;
    }

    public String getInvoiceCode() {
        return InvoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        InvoiceCode = invoiceCode;
    }

    public String getInvoiceNum() {
        return InvoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        InvoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public BigDecimal getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        TotalAmount = totalAmount;
    }

    public BigDecimal getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        TotalTax = totalTax;
    }

    public BigDecimal getAmountInFiguers() {
        return AmountInFiguers;
    }

    public void setAmountInFiguers(BigDecimal amountInFiguers) {
        AmountInFiguers = amountInFiguers;
    }

    public String getAmountInWords() {
        return AmountInWords;
    }

    public void setAmountInWords(String amountInWords) {
        AmountInWords = amountInWords;
    }

    public String getCheckCode() {
        return CheckCode;
    }

    public void setCheckCode(String checkCode) {
        CheckCode = checkCode;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerRegisterNum() {
        return SellerRegisterNum;
    }

    public void setSellerRegisterNum(String sellerRegisterNum) {
        SellerRegisterNum = sellerRegisterNum;
    }

    public String getSellerBank() {
        return SellerBank;
    }

    public void setSellerBank(String sellerBank) {
        SellerBank = sellerBank;
    }

    public String getSellerAddress() {
        return SellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        SellerAddress = sellerAddress;
    }

    public String getPurchaserName() {
        return PurchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        PurchaserName = purchaserName;
    }

    public String getPurchaserRegisterNum() {
        return PurchaserRegisterNum;
    }

    public void setPurchaserRegisterNum(String purchaserRegisterNum) {
        PurchaserRegisterNum = purchaserRegisterNum;
    }

    public String getPurchaserBank() {
        return PurchaserBank;
    }

    public void setPurchaserBank(String purchaserBank) {
        PurchaserBank = purchaserBank;
    }

    public String getPurchaserAddress() {
        return PurchaserAddress;
    }

    public void setPurchaserAddress(String purchaserAddress) {
        PurchaserAddress = purchaserAddress;
    }

    public String getCommodityName() {
        return CommodityName;
    }

    public void setCommodityName(String commodityName) {
        CommodityName = commodityName;
    }

    public String getCommodityType() {
        return CommodityType;
    }

    public void setCommodityType(String commodityType) {
        CommodityType = commodityType;
    }

    public String getCommodityUnit() {
        return CommodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        CommodityUnit = commodityUnit;
    }

    public String getCommodityNum() {
        return CommodityNum;
    }

    public void setCommodityNum(String commodityNum) {
        CommodityNum = commodityNum;
    }

    public String getCommodityPrice() {
        return CommodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        CommodityPrice = commodityPrice;
    }

    public String getCommodityAmount() {
        return CommodityAmount;
    }

    public void setCommodityAmount(String commodityAmount) {
        CommodityAmount = commodityAmount;
    }

    public String getCommodityTaxRate() {
        return CommodityTaxRate;
    }

    public void setCommodityTaxRate(String commodityTaxRate) {
        CommodityTaxRate = commodityTaxRate;
    }

    public String getCommodityTax() {
        return CommodityTax;
    }

    public void setCommodityTax(String commodityTax) {
        CommodityTax = commodityTax;
    }
}
