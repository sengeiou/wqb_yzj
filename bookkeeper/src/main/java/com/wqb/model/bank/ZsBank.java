package com.wqb.model.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ZsBank 实体类 2018-01-11 lch
 */

public class ZsBank implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9209041965819357055L;
    private String zsBillID; // 招商银行对账单主键
    private String accountID; // 账套ID
    private String vouchID; // 做账主凭证ID
    private String period; // 会计期间
    private Date transaction_time; // 交易日
    private String transaction_MinutesSeconds; // 交易时间
    private Date fromTheDay; // 起息日
    private String transactionType; // 交易类型
    private BigDecimal debitAmount; // 借方金额
    private BigDecimal creditAmount; // 贷方金额
    private BigDecimal balance; // 余额
    private String reference; // 摘要
    private String transactionID; // 交易流水号
    private String processNumber; // 流程实例号
    private String businessName; // 业务名称
    private String purpose; // 用途
    private String businessReferenceNumber; // 业务参考号
    private String businessReference; // 业务摘要
    private String otherReference; // 其它摘要
    private String receiverPayBranchName; // 收/付方分行名
    private String receiverPayName; // 收/付方名称
    private String receiverPayAccount; // 收/付方帐号
    private String receiverPayBankNumber; // 收/付方开户行行号
    private String receiverPayBankName; // 收/付方开户行名
    private String receiverPayBankAddress; // 收/付方开户行地址
    private String parentChildAccountName; // 母/子公司帐号分行名
    private String parentChildAccount; // 母/子公司帐号
    private String parentChildName; // 母/子公司名称
    private String informationSign; // 信息标志
    private String isAnnex; // 有否附件信息
    private String imprintSign; // 冲帐标志
    private String expandReference; // 扩展摘要
    private String transactionAnalysisCode; // 交易分析码
    private String ticketNumber; // 票据号
    private String businessPaymentNumber; // 商务支付订单号
    private String internalNumber; // 内部编号
    private long importDate; // 导入时间
    private String bankType; // 银行名称

    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键F
    private String sysSubjectID;
    // 科目全名
    private String sysSubjectFullName;

    // 银行账户
    private String bankAccount;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSysSubjectCode() {
        return sysSubjectCode;
    }

    public void setSysSubjectCode(String sysSubjectCode) {
        this.sysSubjectCode = sysSubjectCode;
    }

    public String getSysSubjectName() {
        return sysSubjectName;
    }

    public void setSysSubjectName(String sysSubjectName) {
        this.sysSubjectName = sysSubjectName;
    }

    public String getSysSubjectID() {
        return sysSubjectID;
    }

    public void setSysSubjectID(String sysSubjectID) {
        this.sysSubjectID = sysSubjectID;
    }

    public String getSysSubjectFullName() {
        return sysSubjectFullName;
    }

    public void setSysSubjectFullName(String sysSubjectFullName) {
        this.sysSubjectFullName = sysSubjectFullName;
    }

    public void setZsBillID(String zsBillID) {
        this.zsBillID = zsBillID;
    }

    public String getZsBillID() {
        return zsBillID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setTransaction_time(Date transaction_time) {
        this.transaction_time = transaction_time;
    }

    public Date getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_MinutesSeconds(String transaction_MinutesSeconds) {
        this.transaction_MinutesSeconds = transaction_MinutesSeconds;
    }

    public String getTransaction_MinutesSeconds() {
        return transaction_MinutesSeconds;
    }

    public void setFromTheDay(Date fromTheDay) {
        this.fromTheDay = fromTheDay;
    }

    public Date getFromTheDay() {
        return fromTheDay;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setBusinessReferenceNumber(String businessReferenceNumber) {
        this.businessReferenceNumber = businessReferenceNumber;
    }

    public String getBusinessReferenceNumber() {
        return businessReferenceNumber;
    }

    public void setBusinessReference(String businessReference) {
        this.businessReference = businessReference;
    }

    public String getBusinessReference() {
        return businessReference;
    }

    public void setOtherReference(String otherReference) {
        this.otherReference = otherReference;
    }

    public String getOtherReference() {
        return otherReference;
    }

    public void setReceiverPayBranchName(String receiverPayBranchName) {
        this.receiverPayBranchName = receiverPayBranchName;
    }

    public String getReceiverPayBranchName() {
        return receiverPayBranchName;
    }

    public void setReceiverPayName(String receiverPayName) {
        this.receiverPayName = receiverPayName;
    }

    public String getReceiverPayName() {
        return receiverPayName;
    }

    public void setReceiverPayAccount(String receiverPayAccount) {
        this.receiverPayAccount = receiverPayAccount;
    }

    public String getReceiverPayAccount() {
        return receiverPayAccount;
    }

    public void setReceiverPayBankNumber(String receiverPayBankNumber) {
        this.receiverPayBankNumber = receiverPayBankNumber;
    }

    public String getReceiverPayBankNumber() {
        return receiverPayBankNumber;
    }

    public void setReceiverPayBankName(String receiverPayBankName) {
        this.receiverPayBankName = receiverPayBankName;
    }

    public String getReceiverPayBankName() {
        return receiverPayBankName;
    }

    public void setReceiverPayBankAddress(String receiverPayBankAddress) {
        this.receiverPayBankAddress = receiverPayBankAddress;
    }

    public String getReceiverPayBankAddress() {
        return receiverPayBankAddress;
    }

    public void setParentChildAccountName(String parentChildAccountName) {
        this.parentChildAccountName = parentChildAccountName;
    }

    public String getParentChildAccountName() {
        return parentChildAccountName;
    }

    public void setParentChildAccount(String parentChildAccount) {
        this.parentChildAccount = parentChildAccount;
    }

    public String getParentChildAccount() {
        return parentChildAccount;
    }

    public void setParentChildName(String parentChildName) {
        this.parentChildName = parentChildName;
    }

    public String getParentChildName() {
        return parentChildName;
    }

    public void setInformationSign(String informationSign) {
        this.informationSign = informationSign;
    }

    public String getInformationSign() {
        return informationSign;
    }

    public void setIsAnnex(String isAnnex) {
        this.isAnnex = isAnnex;
    }

    public String getIsAnnex() {
        return isAnnex;
    }

    public void setImprintSign(String imprintSign) {
        this.imprintSign = imprintSign;
    }

    public String getImprintSign() {
        return imprintSign;
    }

    public void setExpandReference(String expandReference) {
        this.expandReference = expandReference;
    }

    public String getExpandReference() {
        return expandReference;
    }

    public void setTransactionAnalysisCode(String transactionAnalysisCode) {
        this.transactionAnalysisCode = transactionAnalysisCode;
    }

    public String getTransactionAnalysisCode() {
        return transactionAnalysisCode;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setBusinessPaymentNumber(String businessPaymentNumber) {
        this.businessPaymentNumber = businessPaymentNumber;
    }

    public String getBusinessPaymentNumber() {
        return businessPaymentNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(long importDate) {
        this.importDate = importDate;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankType() {
        return bankType;
    }
}
