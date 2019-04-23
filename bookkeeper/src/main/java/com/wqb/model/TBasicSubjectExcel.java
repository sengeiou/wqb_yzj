package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectExcel
 * @Description: 金蝶Excel导出科目余额表
 * @date 2017年12月25日 上午11:06:49
 */
public class TBasicSubjectExcel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2641990456812548171L;

    /**
     * 主键
     */
    private String pkSubExcelId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 期间
     */
    private String period;

    /**
     * 科目代码
     */
    private String subCode;

    /**
     * 科目名称
     */
    private String subName;

    /**
     * 币别
     */
    private String typeOfCurrency;

    /**
     * 期初余额(借方)
     */
    private BigDecimal initDebitBalance;

    /**
     * 期初余额(贷方)
     */
    private BigDecimal initCreditBalance;

    /**
     * 本期发生额(借方)
     */
    private BigDecimal currentAmountDebit;

    /**
     * 本期发生额(贷方)
     */
    private BigDecimal currentAmountCredit;

    /**
     * 本年累计发生额(借方)
     */
    private BigDecimal yearAmountDebit;

    /**
     * 本年累计发生额(贷方)
     */
    private BigDecimal yearAmountCredit;

    /**
     * 期末余额(借方)
     */
    private BigDecimal endingBalanceDebit;

    /**
     * 期末余额(贷方)
     */
    private BigDecimal endingBalanceCredit;

    /**
     * 是否多个同级(0无，1有)
     */
    private String isMultipleSiblings;

    /**
     * 同级编码(一个银行多个外币时用到)
     */
    private String siblingsCoding;

    /**
     * 同级科目名称
     */
    private String siblingsSubName;

    /**
     * 上级编码(1级为0，二级取前4位）
     */
    private String superiorCoding;

    /**
     * 上传文件的地址和文件名（用于取出上传的文件）
     */
    private String fileUrl;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 时间戳
     */
    private String updateTimestamp;

    /**
     * 是否已经匹配到系统科目(0否，1是)
     */
    private String isMatching;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private String debitCreditDirection;

    /**
     * 是否有余额 (1:无余额 2:有余额 )
     */
    private String hasBalance;

    public String getHasBalance() {
        return hasBalance;
    }

    public void setHasBalance(String hasBalance) {
        this.hasBalance = hasBalance;
    }

    public String getPkSubExcelId() {
        return pkSubExcelId;
    }

    public void setPkSubExcelId(String pkSubExcelId) {
        this.pkSubExcelId = pkSubExcelId == null ? null : pkSubExcelId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode == null ? null : subCode.trim();
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName == null ? null : subName.trim();
    }

    public String getTypeOfCurrency() {
        return typeOfCurrency;
    }

    public void setTypeOfCurrency(String typeOfCurrency) {
        this.typeOfCurrency = typeOfCurrency == null ? null : typeOfCurrency.trim();
    }

    public BigDecimal getInitDebitBalance() {
        return initDebitBalance;
    }

    public void setInitDebitBalance(BigDecimal initDebitBalance) {
        this.initDebitBalance = initDebitBalance;
    }

    public BigDecimal getInitCreditBalance() {
        return initCreditBalance;
    }

    public void setInitCreditBalance(BigDecimal initCreditBalance) {
        this.initCreditBalance = initCreditBalance;
    }

    public BigDecimal getCurrentAmountDebit() {
        return currentAmountDebit;
    }

    public void setCurrentAmountDebit(BigDecimal currentAmountDebit) {
        this.currentAmountDebit = currentAmountDebit;
    }

    public BigDecimal getCurrentAmountCredit() {
        return currentAmountCredit;
    }

    public void setCurrentAmountCredit(BigDecimal currentAmountCredit) {
        this.currentAmountCredit = currentAmountCredit;
    }

    public BigDecimal getYearAmountDebit() {
        return yearAmountDebit;
    }

    public void setYearAmountDebit(BigDecimal yearAmountDebit) {
        this.yearAmountDebit = yearAmountDebit;
    }

    public BigDecimal getYearAmountCredit() {
        return yearAmountCredit;
    }

    public void setYearAmountCredit(BigDecimal yearAmountCredit) {
        this.yearAmountCredit = yearAmountCredit;
    }

    public BigDecimal getEndingBalanceDebit() {
        return endingBalanceDebit;
    }

    public void setEndingBalanceDebit(BigDecimal endingBalanceDebit) {
        this.endingBalanceDebit = endingBalanceDebit;
    }

    public BigDecimal getEndingBalanceCredit() {
        return endingBalanceCredit;
    }

    public void setEndingBalanceCredit(BigDecimal endingBalanceCredit) {
        this.endingBalanceCredit = endingBalanceCredit;
    }

    public String getIsMultipleSiblings() {
        return isMultipleSiblings;
    }

    public void setIsMultipleSiblings(String isMultipleSiblings) {
        this.isMultipleSiblings = isMultipleSiblings == null ? null : isMultipleSiblings.trim();
    }

    public String getSiblingsCoding() {
        return siblingsCoding;
    }

    public void setSiblingsCoding(String siblingsCoding) {
        this.siblingsCoding = siblingsCoding == null ? null : siblingsCoding.trim();
    }

    public String getSuperiorCoding() {
        return superiorCoding;
    }

    public String getSiblingsSubName() {
        return siblingsSubName;
    }

    public void setSiblingsSubName(String siblingsSubName) {
        this.siblingsSubName = siblingsSubName;
    }

    public void setSuperiorCoding(String superiorCoding) {
        this.superiorCoding = superiorCoding == null ? null : superiorCoding.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp == null ? null : updateTimestamp.trim();
    }

    public String getIsMatching() {
        return isMatching;
    }

    public void setIsMatching(String isMatching) {
        this.isMatching = isMatching == null ? null : isMatching.trim();
    }

    public String getDebitCreditDirection() {
        return debitCreditDirection;
    }

    public void setDebitCreditDirection(String debitCreditDirection) {
        this.debitCreditDirection = debitCreditDirection == null ? null : debitCreditDirection.trim();
    }
}
