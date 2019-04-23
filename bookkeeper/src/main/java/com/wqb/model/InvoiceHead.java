package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class InvoiceHead implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7803005950214072338L;
    // 主键
    private String invoiceHID;
    // 会计期间
    private Date period;
    // 发票类型
    private String invoiceType;
    // 发票编码
    private String invoiceCode;
    // 发票号码
    private String invoiceNumber;
    // 发票名称
    private String invoiceName;
    // 发票状态
    private String invoiceState;
    // 发票日期
    private Date invoiceDate;
    // 制票人
    private String invoicePerson;
    // 购方税号
    private String buyTaxno;

    // 购方公司名称
    private String buyCorp;
    // 购方账户
    private String buyBankno;
    // 地址
    private String address;
    // 销方公司名称
    private String saleCorp;
    // 销方税号
    private String saleTaxno;
    // 销方账户
    private String saleBankno;
    // 总金额
    private double totalAmount;
    // 生成凭证主键
    private String vouchID;
    // 审核人
    private String auditPsn;
    // 附件
    private String fj;
    // 发票说明
    private String invoiceDes;
    // 修改人ID
    private String updatePsnID;
    // 修改人
    private String updatePsn;
    // 修改时间
    private Date updatedate;
    // 创建人ID
    private String createPsnID;
    // 创建人
    private String createpsn;
    // 归属账套(外键)
    private String accountID;
    // 用户(外键)
    private String userID;
    // 确认日期
    private Date invoice_confirmdate;

    private String sureType;

    // 地址电话
    private String addressPhone;
    // 商品编码版本号
    private String productVersion;
    // 单据号
    private String billNo;

    private String comName;

    private double namount;
    private double taxAmount;
    private String spec;
    private double nprice;
    private double nnumber;
    private double taxRate;
    private String measure;
    // 导入时间
    private long importDate;

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(long importDate) {
        this.importDate = importDate;
    }

    public double getNamount() {
        return namount;
    }

    public void setNamount(double namount) {
        this.namount = namount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public double getNprice() {
        return nprice;
    }

    public void setNprice(double nprice) {
        this.nprice = nprice;
    }

    public double getNnumber() {
        return nnumber;
    }

    public void setNnumber(double nnumber) {
        this.nnumber = nnumber;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getSureType() {
        return sureType;
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setSureType(String sureType) {
        this.sureType = sureType;
    }

    public String getInvoiceHID() {
        return invoiceHID;
    }

    public void setInvoiceHID(String invoiceHID) {
        this.invoiceHID = invoiceHID;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
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

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(String invoiceState) {
        this.invoiceState = invoiceState;
    }

    public String getInvoicePerson() {
        return invoicePerson;
    }

    public void setInvoicePerson(String invoicePerson) {
        this.invoicePerson = invoicePerson;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getAuditPsn() {
        return auditPsn;
    }

    public void setAuditPsn(String auditPsn) {
        this.auditPsn = auditPsn;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getInvoiceDes() {
        return invoiceDes;
    }

    public void setInvoiceDes(String invoiceDes) {
        this.invoiceDes = invoiceDes;
    }

    public String getUpdatePsnID() {
        return updatePsnID;
    }

    public void setUpdatePsnID(String updatePsnID) {
        this.updatePsnID = updatePsnID;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }

    public String getCreatepsn() {
        return createpsn;
    }

    public void setCreatepsn(String createpsn) {
        this.createpsn = createpsn;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getInvoice_confirmdate() {
        return invoice_confirmdate;
    }

    public void setInvoice_confirmdate(Date invoice_confirmdate) {
        this.invoice_confirmdate = invoice_confirmdate;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
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

    @Override
    public String toString() {
        return "InvoiceHead [invoiceHID=" + invoiceHID + ", period=" + period + ", invoiceType=" + invoiceType
                + ", invoiceCode=" + invoiceCode + ", invoiceNumber=" + invoiceNumber + ", invoiceName=" + invoiceName
                + ", invoiceState=" + invoiceState + ", invoiceDate=" + invoiceDate + ", invoicePerson=" + invoicePerson
                + ", buyTaxno=" + buyTaxno + ", buyCorp=" + buyCorp + ", buyBankno=" + buyBankno + ", address="
                + address + ", saleCorp=" + saleCorp + ", saleTaxno=" + saleTaxno + ", saleBankno=" + saleBankno
                + ", totalAmount=" + totalAmount + ", vouchID=" + vouchID + ", auditPsn=" + auditPsn + ", fj=" + fj
                + ", invoiceDes=" + invoiceDes + ", updatePsnID=" + updatePsnID + ", updatePsn=" + updatePsn
                + ", updatedate=" + updatedate + ", createPsnID=" + createPsnID + ", createpsn=" + createpsn
                + ", accountID=" + accountID + ", userID=" + userID + ", invoice_confirmdate=" + invoice_confirmdate
                + ", sureType=" + sureType + ", addressPhone=" + addressPhone + ", productVersion=" + productVersion
                + ", billNo=" + billNo + ", comName=" + comName + ", namount=" + namount + ", taxAmount=" + taxAmount
                + ", spec=" + spec + ", nprice=" + nprice + ", nnumber=" + nnumber + ", taxRate=" + taxRate
                + ", measure=" + measure + ", importDate=" + importDate + "]";
    }


}
