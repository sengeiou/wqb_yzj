package com.wqb.commons.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wqb.commons.emun.PaymentType;
import com.wqb.domains.BankAccount;
import com.wqb.domains.Receipt;
import com.wqb.domains.invoice.Invoice;
import com.wqb.domains.invoice.InvoiceHeader;
import com.wqb.domains.voucher.VoucherItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收支流水项目
 *
 * @author Shoven
 * @since 2019-03-19 10:01
 */
public class JournalItem implements Serializable {
    /**
     * 流水id
     */
    private Long journalId;

    /**
     * 凭证id
     */
    private String voucherId;

    /**
     * 对应凭证项目 id
     *
     * @see VoucherItem
     */
    @NotNull
    private Integer itemId;

    /**
     * 付款方式
     * @see PaymentType
     */
    private Integer paymentType;

    /**
     * 目标名称，可以是应收应付的客户名称，商品名称
     */
    private String target;


    /**
     * 备注
     */
    private String remark;

    /**
     * 发票id
     *
     * @see InvoiceHeader
     */
    private String invoiceId;

    /**
     * 发票对象
     */
    private Invoice invoice;

    /**
     * 收据id
     */
    private String receiptId;

    /**
     * 收据对象 (自行设置，无需传参)
     */
    private Receipt receipt;

    /**
     * 银行卡id
     */
    private String bankId;

    /**
     * 银行卡对象 (自行设置，无需传参)
     */
    private BankAccount bankAccount;

    /**
     * 数量 (自行设置，无需传参)
     */
    private BigDecimal number;


    /**
     * 金额 (自行设置，无需传参)
     */
    private BigDecimal amount;

    /**
     * 税额 (自行设置，无需传参)
     */
    private BigDecimal taxAmount;

    /**
     * 日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    @Past
    private Date date;


    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean supportInvoice() {
        return StringUtils.isNotBlank(invoiceId);
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}

