package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class Inventory implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5478497086442360737L;
    // 进销存主键
    private String id;
    // 商品名称
    private String comName;
    // 规格
    private String spec;
    // 计量单位ID
    private String measureID;
    // 计量单位
    private String measure;
    // 单价
    private double nprice;
    // 数量
    private double nnumber;
    // 金额
    private double namount;
    // 税额
    private double taxAmount;
    // 价税合计
    private double ntaxAmount;
    // 税种类别
    private String taxClass;
    // 创建人ID
    private String createPsnID;
    // 创建人
    private String createPsn;
    // 隶属账套
    private String accountID;
    // 税率
    private String taxRate;
    // 备注
    private String des;
    // 期间
    private String period;
    // 发票类型(1:进项发票2:销项发票)
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
    // 生成凭证主键
    private String vouchID;
    // 审核人
    private String auditPsn;
    // 附件
    private String fj;
    // 发票说明
    private String invoiceDes;
    // 创建时间
    private Date createDate;
    // 确认日期
    private Date invoice_confirmdate;
    // 商品编码版本号
    private String productVersion;
    // 单据号
    private String billNo;
    // 地址电话
    private String addressPhone;
    // 认证方式
    private String sureType;
    // 摘要
    private String remark;
    // 汇率
    private double hl;
    // 币别
    private String bb;
    // 币别主键
    private String bbID;
    // 初始数量(收入数量)
    private double cssl;
    // 初始单价(收入单价[原币])
    private double csdjyb;
    // 初始单价(收入单价)
    private double csdj;
    // 初始金额(原币)
    private double csjeyb;
    // 初始金额
    private double csje;
    // 发出数量
    private double fcsl;
    // 发出单价(原币)
    private double fcdjyb;
    // 发出单价
    private double fcdj;
    // 发出金额(原币)
    private double fcjeyb;
    // 发出金额
    private double fcje;
    // 借贷方向
    private String jdfx;
    // 结存数量
    private double jcsl;
    // 结存单价
    private double jcdj;
    // 结存金额
    private double jcje;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getNtaxAmount() {
        return ntaxAmount;
    }

    public void setNtaxAmount(double ntaxAmount) {
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getInvoice_confirmdate() {
        return invoice_confirmdate;
    }

    public void setInvoice_confirmdate(Date invoice_confirmdate) {
        this.invoice_confirmdate = invoice_confirmdate;
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

    public String getSureType() {
        return sureType;
    }

    public void setSureType(String sureType) {
        this.sureType = sureType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getHl() {
        return hl;
    }

    public void setHl(double hl) {
        this.hl = hl;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getBbID() {
        return bbID;
    }

    public void setBbID(String bbID) {
        this.bbID = bbID;
    }

    public double getCssl() {
        return cssl;
    }

    public void setCssl(double cssl) {
        this.cssl = cssl;
    }

    public double getCsdjyb() {
        return csdjyb;
    }

    public void setCsdjyb(double csdjyb) {
        this.csdjyb = csdjyb;
    }

    public double getCsdj() {
        return csdj;
    }

    public void setCsdj(double csdj) {
        this.csdj = csdj;
    }

    public double getCsjeyb() {
        return csjeyb;
    }

    public void setCsjeyb(double csjeyb) {
        this.csjeyb = csjeyb;
    }

    public double getCsje() {
        return csje;
    }

    public void setCsje(double csje) {
        this.csje = csje;
    }

    public double getFcsl() {
        return fcsl;
    }

    public void setFcsl(double fcsl) {
        this.fcsl = fcsl;
    }

    public double getFcdjyb() {
        return fcdjyb;
    }

    public void setFcdjyb(double fcdjyb) {
        this.fcdjyb = fcdjyb;
    }

    public double getFcdj() {
        return fcdj;
    }

    public void setFcdj(double fcdj) {
        this.fcdj = fcdj;
    }

    public double getFcjeyb() {
        return fcjeyb;
    }

    public void setFcjeyb(double fcjeyb) {
        this.fcjeyb = fcjeyb;
    }

    public double getFcje() {
        return fcje;
    }

    public void setFcje(double fcje) {
        this.fcje = fcje;
    }

    public String getJdfx() {
        return jdfx;
    }

    public void setJdfx(String jdfx) {
        this.jdfx = jdfx;
    }

    public double getJcsl() {
        return jcsl;
    }

    public void setJcsl(double jcsl) {
        this.jcsl = jcsl;
    }

    public double getJcdj() {
        return jcdj;
    }

    public void setJcdj(double jcdj) {
        this.jcdj = jcdj;
    }

    public double getJcje() {
        return jcje;
    }

    public void setJcje(double jcje) {
        this.jcje = jcje;
    }

}
