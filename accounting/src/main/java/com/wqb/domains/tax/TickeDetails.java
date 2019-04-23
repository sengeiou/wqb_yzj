package com.wqb.domains.tax;

import java.math.BigDecimal;
import java.util.List;

public class TickeDetails {
    private String invoiceCode;        //发票代码
    private String invoiceNumber;        //发票号码
    private String billingDate;        //开票日期 格式YYYYMMDD
    private String sellerTaxName;    //销方名称
    private String sellerTaxNumber;    //销方税号
    private String sellerAddressPhone;    //销方地址电话
    private String sellerBankAccount;    //销方开户行及账号
    private String buyerTaxName;    //购方名称
    private String buyerTaxNumber;    //购方税号
    private String buyerAddressPhone;    //购方地址电话
    private String buyerBankAccount;    //购方开户行及账号
    private String checkCode;        //校验码
    private BigDecimal tax;        //合计税额
    private BigDecimal amount;        //价税合计
    private String machineCode;    //机器编号
    private BigDecimal amountExcludeTax;        //金额合计
    private String invoiceStatus;        //作废标志 Y作废 N未作废
    private String invoiceType;        //发票类型 01 专票 04 普票 10 电⼦票
    private String receiptName;        //收款人
    private String reviewerName;        //复核人
    private String operatorName;    //开票人
    private Boolean financialChapter;    //是否有章 true/false
    private List<Goods> goods;    //货物列表

    @Override
    public String toString() {
        return "TickeDetails{" +
                "invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", billingDate='" + billingDate + '\'' +
                ", sellerTaxName='" + sellerTaxName + '\'' +
                ", sellerTaxNumber='" + sellerTaxNumber + '\'' +
                ", sellerAddressPhone='" + sellerAddressPhone + '\'' +
                ", sellerBankAccount='" + sellerBankAccount + '\'' +
                ", buyerTaxName='" + buyerTaxName + '\'' +
                ", buyerTaxNumber='" + buyerTaxNumber + '\'' +
                ", buyerAddressPhone='" + buyerAddressPhone + '\'' +
                ", buyerBankAccount='" + buyerBankAccount + '\'' +
                ", checkCode='" + checkCode + '\'' +
                ", tax=" + tax +
                ", amount=" + amount +
                ", machineCode='" + machineCode + '\'' +
                ", amountExcludeTax=" + amountExcludeTax +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", receiptName='" + receiptName + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", financialChapter=" + financialChapter +
                ", goods=" + goods +
                '}';
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

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getSellerTaxName() {
        return sellerTaxName;
    }

    public void setSellerTaxName(String sellerTaxName) {
        this.sellerTaxName = sellerTaxName;
    }

    public String getSellerTaxNumber() {
        return sellerTaxNumber;
    }

    public void setSellerTaxNumber(String sellerTaxNumber) {
        this.sellerTaxNumber = sellerTaxNumber;
    }

    public String getSellerAddressPhone() {
        return sellerAddressPhone;
    }

    public void setSellerAddressPhone(String sellerAddressPhone) {
        this.sellerAddressPhone = sellerAddressPhone;
    }

    public String getSellerBankAccount() {
        return sellerBankAccount;
    }

    public void setSellerBankAccount(String sellerBankAccount) {
        this.sellerBankAccount = sellerBankAccount;
    }

    public String getBuyerTaxName() {
        return buyerTaxName;
    }

    public void setBuyerTaxName(String buyerTaxName) {
        this.buyerTaxName = buyerTaxName;
    }

    public String getBuyerTaxNumber() {
        return buyerTaxNumber;
    }

    public void setBuyerTaxNumber(String buyerTaxNumber) {
        this.buyerTaxNumber = buyerTaxNumber;
    }

    public String getBuyerAddressPhone() {
        return buyerAddressPhone;
    }

    public void setBuyerAddressPhone(String buyerAddressPhone) {
        this.buyerAddressPhone = buyerAddressPhone;
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public BigDecimal getAmountExcludeTax() {
        return amountExcludeTax;
    }

    public void setAmountExcludeTax(BigDecimal amountExcludeTax) {
        this.amountExcludeTax = amountExcludeTax;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Boolean getFinancialChapter() {
        return financialChapter;
    }

    public void setFinancialChapter(Boolean financialChapter) {
        this.financialChapter = financialChapter;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}
