package com.wqb.domains.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 发票主表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_fa_invoice_h")
public class InvoiceHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "invoiceHID", type = IdType.UUID)
    private String invoiceHID;

    /**
     * 会计期间
     */
    private Date period;

    /**
     * 发票类型(1:进项发票2:销项发票)
     */
    @TableField("invoiceType")
    private String invoiceType;

    /**
     * 发票编码
     */
    @TableField("invoiceCode")
    private String invoiceCode;

    /**
     * 发票号码
     */
    @TableField("invoiceNumber")
    private String invoiceNumber;

    /**
     * 发票名称
     */
    @TableField("invoiceName")
    private String invoiceName;

    /**
     * 发票状态
     */
    @TableField("invoiceState")
    private String invoiceState;

    /**
     * 发票日期
     */
    @TableField("invoiceDate")
    private Date invoiceDate;

    /**
     * 制票人
     */
    @TableField("invoicePerson")
    private String invoicePerson;

    /**
     * 购方税号
     */
    @TableField("buyTaxno")
    private String buyTaxno;

    /**
     * 购方公司名称
     */
    @TableField("buyCorp")
    private String buyCorp;

    /**
     * 购方账户
     */
    @TableField("buyBankno")
    private String buyBankno;

    /**
     * 地址
     */
    private String address;

    /**
     * 销方公司名称
     */
    @TableField("saleCorp")
    private String saleCorp;

    /**
     * 销方税号
     */
    @TableField("saleTaxno")
    private String saleTaxno;

    /**
     * 销方账户
     */
    @TableField("saleBankno")
    private String saleBankno;

    /**
     * 生成凭证主键
     */
    @TableField("vouchID")
    private String vouchID;

    /**
     * 审核人
     */
    @TableField("auditPsn")
    private String auditPsn;

    /**
     * 附件
     */
    private String fj;

    /**
     * 发票说明
     */
    @TableField("invoiceDes")
    private String invoiceDes;

    /**
     * 创建人ID
     */
    @TableField("createPsnID")
    private String createPsnID;

    /**
     * 创建时间
     */
    @TableField("createDate")
    private Date createDate;

    /**
     * 创建人
     */
    private String createpsn;

    /**
     * 归属账套(外键)
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 确认日期
     */
    private Date invoiceConfirmdate;

    /**
     * 商品编码版本号
     */
    @TableField("productVersion")
    private String productVersion;

    /**
     * 单据号
     */
    @TableField("billNo")
    private String billNo;

    /**
     * 地址电话
     */
    @TableField("addressPhone")
    private String addressPhone;

    @TableField("importDate")
    private Long importDate;

    /**
     * 认证方式
     */
    @TableField("sureType")
    private String sureType;

    public String getInvoiceHID() {
        return invoiceHID;
    }

    public void setInvoiceHID(String invoiceHID) {
        this.invoiceHID = invoiceHID;
    }
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
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
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    public String getInvoicePerson() {
        return invoicePerson;
    }

    public void setInvoicePerson(String invoicePerson) {
        this.invoicePerson = invoicePerson;
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
    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public Date getInvoiceConfirmdate() {
        return invoiceConfirmdate;
    }

    public void setInvoiceConfirmdate(Date invoiceConfirmdate) {
        this.invoiceConfirmdate = invoiceConfirmdate;
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
    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }
    public Long getImportDate() {
        return importDate;
    }

    public void setImportDate(Long importDate) {
        this.importDate = importDate;
    }
    public String getSureType() {
        return sureType;
    }

    public void setSureType(String sureType) {
        this.sureType = sureType;
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" +
        "invoiceHID=" + invoiceHID +
        ", period=" + period +
        ", invoiceType=" + invoiceType +
        ", invoiceCode=" + invoiceCode +
        ", invoiceNumber=" + invoiceNumber +
        ", invoiceName=" + invoiceName +
        ", invoiceState=" + invoiceState +
        ", invoiceDate=" + invoiceDate +
        ", invoicePerson=" + invoicePerson +
        ", buyTaxno=" + buyTaxno +
        ", buyCorp=" + buyCorp +
        ", buyBankno=" + buyBankno +
        ", address=" + address +
        ", saleCorp=" + saleCorp +
        ", saleTaxno=" + saleTaxno +
        ", saleBankno=" + saleBankno +
        ", vouchID=" + vouchID +
        ", auditPsn=" + auditPsn +
        ", fj=" + fj +
        ", invoiceDes=" + invoiceDes +
        ", createPsnID=" + createPsnID +
        ", createDate=" + createDate +
        ", createpsn=" + createpsn +
        ", accountID=" + accountID +
        ", invoiceConfirmdate=" + invoiceConfirmdate +
        ", productVersion=" + productVersion +
        ", billNo=" + billNo +
        ", addressPhone=" + addressPhone +
        ", importDate=" + importDate +
        ", sureType=" + sureType +
        "}";
    }
}
