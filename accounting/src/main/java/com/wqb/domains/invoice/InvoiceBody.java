package com.wqb.domains.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 发票子表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_fa_invoice_b")
public class InvoiceBody implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "invoiceBID", type = IdType.UUID)
    private String invoiceBID;

    /**
     * 发票主表主键(外键)
     */
    @TableField("invoiceHID")
    private String invoiceHID;

    /**
     * 期间
     */
    private String period;

    /**
     * 税率
     */
    @TableField("taxRate")
    private BigDecimal taxRate;

    /**
     * 价税合计
     */
    @TableField("ntaxAmount")
    private BigDecimal ntaxAmount;

    /**
     * 税额
     */
    @TableField("taxAmount")
    private BigDecimal taxAmount;

    /**
     * 金额
     */
    private BigDecimal namount;

    /**
     * 数量
     */
    private BigDecimal nnumber;

    /**
     * 商品名称
     */
    @TableField("comName")
    private String comName;

    /**
     * 规格
     */
    private String spec;

    /**
     * 商品名称规格
     */
    @TableField("comNameSpec")
    private String comNameSpec;

    /**
     * 1进项2销项
     */
    @TableField("invoiceType")
    private String invoiceType;

    /**
     * 隶属账套
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 计量单位ID(外键)
     */
    @TableField("measureID")
    private String measureID;

    /**
     * 计量单位
     */
    private String measure;

    /**
     * 单价
     */
    private BigDecimal nprice;

    /**
     * 税种类别
     */
    @TableField("taxClass")
    private String taxClass;

    /**
     * 修改人ID
     */
    @TableField("updatePsnID")
    private String updatePsnID;

    /**
     * 修改人
     */
    @TableField("updatePsn")
    private String updatePsn;

    /**
     * 修改时间
     */
    private Date updatedate;

    /**
     * 创建人ID
     */
    @TableField("createPsnID")
    private String createPsnID;

    /**
     * 创建人
     */
    @TableField("CREATEpsn")
    private String CREATEpsn;

    /**
     * 用户
     */
    @TableField("userID")
    private String userID;

    /**
     * 税收分类编码
     */
    @TableField("taxTypeCode")
    private String taxTypeCode;

    /**
     * 备注
     */
    private String des;

    /**
     * 映射科目
     */
    private String subCode;

    /**
     * 映射科目全名
     */
    private String subFullName;

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
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    public BigDecimal getNtaxAmount() {
        return ntaxAmount;
    }

    public void setNtaxAmount(BigDecimal ntaxAmount) {
        this.ntaxAmount = ntaxAmount;
    }
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    public BigDecimal getNamount() {
        return namount;
    }

    public void setNamount(BigDecimal namount) {
        this.namount = namount;
    }
    public BigDecimal getNnumber() {
        return nnumber;
    }

    public void setNnumber(BigDecimal nnumber) {
        this.nnumber = nnumber;
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
    public BigDecimal getNprice() {
        return nprice;
    }

    public void setNprice(BigDecimal nprice) {
        this.nprice = nprice;
    }
    public String getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
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
    public String getCREATEpsn() {
        return CREATEpsn;
    }

    public void setCREATEpsn(String CREATEpsn) {
        this.CREATEpsn = CREATEpsn;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
    public String getSubFullName() {
        return subFullName;
    }

    public void setSubFullName(String subFullName) {
        this.subFullName = subFullName;
    }

    @Override
    public String toString() {
        return "InvoiceBody{" +
        "invoiceBID=" + invoiceBID +
        ", invoiceHID=" + invoiceHID +
        ", period=" + period +
        ", taxRate=" + taxRate +
        ", ntaxAmount=" + ntaxAmount +
        ", taxAmount=" + taxAmount +
        ", namount=" + namount +
        ", nnumber=" + nnumber +
        ", comName=" + comName +
        ", spec=" + spec +
        ", comNameSpec=" + comNameSpec +
        ", invoiceType=" + invoiceType +
        ", accountID=" + accountID +
        ", measureID=" + measureID +
        ", measure=" + measure +
        ", nprice=" + nprice +
        ", taxClass=" + taxClass +
        ", updatePsnID=" + updatePsnID +
        ", updatePsn=" + updatePsn +
        ", updatedate=" + updatedate +
        ", createPsnID=" + createPsnID +
        ", CREATEpsn=" + CREATEpsn +
        ", userID=" + userID +
        ", taxTypeCode=" + taxTypeCode +
        ", des=" + des +
        ", subCode=" + subCode +
        ", subFullName=" + subFullName +
        "}";
    }
}
