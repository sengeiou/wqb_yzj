package com.wqb.model.bank;

import java.io.Serializable;
import java.util.Date;

public class TCmBkbillBoc implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1557117916678872280L;

    /**
     * 中国银行主键
     */
    private String pkBkbillBoc;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 查询账号[ Inquirer account number ]
     */
    private String inquirerAccountNumber;

    /**
     * 总笔数[Total number ]
     */
    private String totalNumber;

    /**
     * 借方发生总笔数[ Total Numbers of Debited Payments ]
     */
    private String totalNumbersOfDebitedPayments;

    /**
     * 借方发生总额[ Total Debit Amount of Payments ]
     */
    private String totalDebitAmountOfPayments;

    /**
     * 贷方发生总笔数[ Total Numbers of Credited Payments ]
     */
    private String totalNumbersOfCreditedPayments;

    /**
     * 贷方发生总额[ Total Credit Amount of Payments ]
     */
    private String totalCreditAmountOfPayments;

    /**
     * 查询时间范围[ Time Range ]
     */
    private String timeRange;

    /**
     * 交易类型[ Transaction Type ]
     */
    private String transactionType;

    /**
     * 业务类型[ Business type ]
     */
    private String businessType;

    /**
     * 付款人开户行号[ Account holding bank number of payer ]
     */
    private String accountHoldingBankNumberOfPayer;

    /**
     * 付款人开户行名[ Payer account bank ]
     */
    private String payerAccountBank;

    /**
     * 付款人账号[ Debit Account No. ]
     */
    private String debitAccountNo;

    /**
     * 付款人名称[ Payer's Name ]
     */
    private String payerName;

    /**
     * 收款人开户行行号[ Account holding bank number of beneficiary ]
     */
    private String accountHoldingBankNumberOfBeneficiary;

    /**
     * 收款人开户行名[ Beneficiary account bank ]
     */
    private String beneficiaryAccountBank;

    /**
     * 收款人账号[ Payee's Account Number ]
     */
    private String payeeAccountNumber;

    /**
     * 收款人名称[ Payee's Name ]
     */
    private String payeeName;

    /**
     * 交易日期[ Transaction Date ]
     */
    private Date transactionDate;

    /**
     * 交易时间[ Transaction time ]
     */
    private Date transactionTime;

    /**
     * 交易货币[ Trade Currency ]币别
     */
    private String typeOfCurrency;

    /**
     * 交易金额[ Trade Amount ]
     */
    private String tradeAmount;

    /**
     * 交易后余额[ After-transaction balance ]
     */
    private String afterTransactionBalance;

    /**
     * 起息日期[ Value Date ]
     */
    private Date valueDate;

    /**
     * 汇率[ Exchange rate ]
     */
    private String exchangeRate;

    /**
     * 交易流水号[ Transaction reference number ]
     */
    private String transactionReferenceNumber;

    /**
     * 客户申请号[ Online Banking Transaction Ref.(Bank Ref.) ]
     */
    private String onlineBankingTransactionRef;

    /**
     * 客户业务编号[ Customer Transaction Ref.(Customer Ref.) ]
     */
    private String customerTransactionRef;

    /**
     * 凭证类型[ Voucher type ]
     */
    private String voucherType;

    /**
     * 凭证号码[ Voucher number ]
     */
    private String voucherNumber;

    /**
     * 记录标识号[ Record ID ]
     */
    private String recordId;

    /**
     * 摘要[ Reference ]
     */
    private String reference;

    /**
     * 用途[ Purpose ]
     */
    private String purpose;

    /**
     * 交易附言[ Remark ]
     */
    private String remark;

    /**
     * 备注[ Remarks ]
     */
    private String remarks;

    /**
     * 修改人ID
     */
    private String updatePsnId;

    /**
     * 修改人
     */
    private String updatePsn;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 创建人ID
     */
    private String createPsnId;

    /**
     * 创建人
     */
    private String createPsn;

    /**
     * 银行科目
     */
    private String subjBank;

    /**
     * 关联凭证
     */
    private String pkVoucher;

    /**
     * 凭证号
     */
    private String voucher;

    /**
     * 账户对应科目名称
     */
    private String hidAccount;

    /**
     * 账户对应科目主键
     */
    private String hidPkAccount;

    /**
     * 做帐时间
     */
    private long accountDate;

    /**
     * 中国银行
     */
    private String bankType;

    /**
     * 凭证主键
     */
    private String vouchID;

    /**
     * 会计期间
     */
    private String period;

    // 对应的科目编码
    private String sysSubjectCode;
    // 对应的科目名称
    private String sysSubjectName;
    // 对应的科目主键F
    private String sysSubjectID;
    // 科目全名
    private String sysSubjectFullName;
    //银行账户
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

    public String getPkBkbillBoc() {
        return pkBkbillBoc;
    }

    public void setPkBkbillBoc(String pkBkbillBoc) {
        this.pkBkbillBoc = pkBkbillBoc == null ? null : pkBkbillBoc.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getInquirerAccountNumber() {
        return inquirerAccountNumber;
    }

    public void setInquirerAccountNumber(String inquirerAccountNumber) {
        this.inquirerAccountNumber = inquirerAccountNumber == null ? null : inquirerAccountNumber.trim();
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber == null ? null : totalNumber.trim();
    }

    public String getTotalNumbersOfDebitedPayments() {
        return totalNumbersOfDebitedPayments;
    }

    public void setTotalNumbersOfDebitedPayments(String totalNumbersOfDebitedPayments) {
        this.totalNumbersOfDebitedPayments = totalNumbersOfDebitedPayments == null ? null
                : totalNumbersOfDebitedPayments.trim();
    }

    public String getTotalDebitAmountOfPayments() {
        return totalDebitAmountOfPayments;
    }

    public void setTotalDebitAmountOfPayments(String totalDebitAmountOfPayments) {
        this.totalDebitAmountOfPayments = totalDebitAmountOfPayments == null ? null : totalDebitAmountOfPayments.trim();
    }

    public String getTotalNumbersOfCreditedPayments() {
        return totalNumbersOfCreditedPayments;
    }

    public void setTotalNumbersOfCreditedPayments(String totalNumbersOfCreditedPayments) {
        this.totalNumbersOfCreditedPayments = totalNumbersOfCreditedPayments == null ? null
                : totalNumbersOfCreditedPayments.trim();
    }

    public String getTotalCreditAmountOfPayments() {
        return totalCreditAmountOfPayments;
    }

    public void setTotalCreditAmountOfPayments(String totalCreditAmountOfPayments) {
        this.totalCreditAmountOfPayments = totalCreditAmountOfPayments == null ? null
                : totalCreditAmountOfPayments.trim();
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange == null ? null : timeRange.trim();
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType == null ? null : transactionType.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getAccountHoldingBankNumberOfPayer() {
        return accountHoldingBankNumberOfPayer;
    }

    public void setAccountHoldingBankNumberOfPayer(String accountHoldingBankNumberOfPayer) {
        this.accountHoldingBankNumberOfPayer = accountHoldingBankNumberOfPayer == null ? null
                : accountHoldingBankNumberOfPayer.trim();
    }

    public String getPayerAccountBank() {
        return payerAccountBank;
    }

    public void setPayerAccountBank(String payerAccountBank) {
        this.payerAccountBank = payerAccountBank == null ? null : payerAccountBank.trim();
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo == null ? null : debitAccountNo.trim();
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName == null ? null : payerName.trim();
    }

    public String getAccountHoldingBankNumberOfBeneficiary() {
        return accountHoldingBankNumberOfBeneficiary;
    }

    public void setAccountHoldingBankNumberOfBeneficiary(String accountHoldingBankNumberOfBeneficiary) {
        this.accountHoldingBankNumberOfBeneficiary = accountHoldingBankNumberOfBeneficiary == null ? null
                : accountHoldingBankNumberOfBeneficiary.trim();
    }

    public String getBeneficiaryAccountBank() {
        return beneficiaryAccountBank;
    }

    public void setBeneficiaryAccountBank(String beneficiaryAccountBank) {
        this.beneficiaryAccountBank = beneficiaryAccountBank == null ? null : beneficiaryAccountBank.trim();
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber == null ? null : payeeAccountNumber.trim();
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName == null ? null : payeeName.trim();
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTypeOfCurrency() {
        return typeOfCurrency;
    }

    public void setTypeOfCurrency(String typeOfCurrency) {
        this.typeOfCurrency = typeOfCurrency == null ? null : typeOfCurrency.trim();
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount == null ? null : tradeAmount.trim();
    }

    public String getAfterTransactionBalance() {
        return afterTransactionBalance;
    }

    public void setAfterTransactionBalance(String afterTransactionBalance) {
        this.afterTransactionBalance = afterTransactionBalance == null ? null : afterTransactionBalance.trim();
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate == null ? null : exchangeRate.trim();
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber == null ? null : transactionReferenceNumber.trim();
    }

    public String getOnlineBankingTransactionRef() {
        return onlineBankingTransactionRef;
    }

    public void setOnlineBankingTransactionRef(String onlineBankingTransactionRef) {
        this.onlineBankingTransactionRef = onlineBankingTransactionRef == null ? null
                : onlineBankingTransactionRef.trim();
    }

    public String getCustomerTransactionRef() {
        return customerTransactionRef;
    }

    public void setCustomerTransactionRef(String customerTransactionRef) {
        this.customerTransactionRef = customerTransactionRef == null ? null : customerTransactionRef.trim();
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType == null ? null : voucherType.trim();
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber == null ? null : voucherNumber.trim();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference == null ? null : reference.trim();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose == null ? null : purpose.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getUpdatePsnId() {
        return updatePsnId;
    }

    public void setUpdatePsnId(String updatePsnId) {
        this.updatePsnId = updatePsnId == null ? null : updatePsnId.trim();
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn == null ? null : updatePsn.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreatePsnId() {
        return createPsnId;
    }

    public void setCreatePsnId(String createPsnId) {
        this.createPsnId = createPsnId == null ? null : createPsnId.trim();
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn == null ? null : createPsn.trim();
    }

    public String getSubjBank() {
        return subjBank;
    }

    public void setSubjBank(String subjBank) {
        this.subjBank = subjBank == null ? null : subjBank.trim();
    }

    public String getPkVoucher() {
        return pkVoucher;
    }

    public void setPkVoucher(String pkVoucher) {
        this.pkVoucher = pkVoucher == null ? null : pkVoucher.trim();
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher == null ? null : voucher.trim();
    }

    public String getHidAccount() {
        return hidAccount;
    }

    public void setHidAccount(String hidAccount) {
        this.hidAccount = hidAccount == null ? null : hidAccount.trim();
    }

    public String getHidPkAccount() {
        return hidPkAccount;
    }

    public void setHidPkAccount(String hidPkAccount) {
        this.hidPkAccount = hidPkAccount == null ? null : hidPkAccount.trim();
    }

    public long getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(long accountDate) {
        this.accountDate = accountDate;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String banktype) {
        this.bankType = banktype == null ? null : banktype.trim();
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }
}
