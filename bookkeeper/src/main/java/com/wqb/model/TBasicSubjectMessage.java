package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMessage
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年3月10日 下午4:54:41
 */
public class TBasicSubjectMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8126870442021274449L;

    /**
     * 主键
     */
    private String pkSubId;

    /**
     * 科目编码
     */
    private String subCode;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 做帐的真实期间 年 - 月(帐套启用年-月份）
     */
    private String accountPeriod;

    /**
     * 导入的期间只有月份
     */
    private String excelImportPeriod;

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
     * excel导入的编码
     */
    private String excelImportCode;

    /**
     * 期末余额(贷方)
     */
    private BigDecimal endingBalanceCredit;

    /**
     * 是否多个同级(0无，1有)
     */
    private String isMultipleSiblings;

    /**
     * excel导入的同级编码(一个银行多个外币时用到)
     */
    private String excelImportSiblingsCoding;

    /**
     * 同级编码(一个银行多个外币时用到)
     */
    private String siblingsCoding;

    /**
     * 同级科目名称(一个银行多个外币时用到)
     */
    private String siblingsSubName;

    /**
     * excel导入的上级编码(1级为0，二级取前4位）
     */
    private String excelImportSuperiorCoding;

    /**
     * 上级编码(1级为0，二级取前4位）
     */
    private String superiorCoding;

    /**
     * 科目完整名称
     */
    private String fullName;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 时间戳
     */
    private String updateTimestamp;

    /**
     * 类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)
     */
    private String category;

    /**
     * 科目来源（导入,新增）
     */
    private String subSource;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 计量单位ID
     */
    private BigDecimal unitId;

    /**
     * 单价(国际单位)
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 金额=数量*金额
     */
    private BigDecimal amount;

    /**
     * 启用状态
     */
    private String state;

    /**
     * 修改者
     */
    private String mender;

    /**
     * 计量单位主键
     */
    private String fkTBasicMeasureId;

    /**
     * 计量单位核算状态(0关闭，1开启）
     */
    private Integer measureState;

    /**
     * 汇率设置主键
     */
    private String fkExchangeRateId;

    /**
     * 外币设置状态(0关闭，1开启）
     */
    private Integer exchangeRateState;

    /**
     * 编码级别
     */
    private Integer codeLevel;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private String debitCreditDirection;

    /**
     * 单据录入金额 amount_debit 只在单据页面用到  不存储于数据库
     **/
    private BigDecimal amountDebit;

    /**
     * 单据录类型 documentsType 只在单据页面用到  不存储于数据库
     **/
    private String documentsType;

    /**
     * 计量单位 documentsUnit 只在单据页面用到  不存储于数据库
     **/
    private String documentsUnit;

    /**
     * 计量单位ID documentsUnitId 只在单据页面用到  不存储于数据库
     **/
    private String documentsUnitId;

    /**
     * 单价(国际单位) documentsDecimal 只在单据页面用到  不存储于数据库
     **/
    private String documentsDecimal;

    /**
     * 数量 documentsNumber 只在单据页面用到  不存储于数据库
     **/
    private String documentsNumber;

    /**
     * 金额=数量*金额  documentsAmount 只在单据页面用到  不存储于数据库
     **/
    private String documentsAmount;

    /**
     * 税额  taxAmount 只在单据页面用到  不存储于数据库
     **/
    private String taxAmount;

    /**
     * 税额类型  taxAmountType 只在单据页面用到  1.未交增值税   2.免税  不存储于数据库
     **/
    private String taxAmountType;

    public String getTaxAmountType() {
        return taxAmountType;
    }

    public void setTaxAmountType(String taxAmountType) {
        this.taxAmountType = taxAmountType;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDocumentsUnit() {
        return documentsUnit;
    }

    public void setDocumentsUnit(String documentsUnit) {
        this.documentsUnit = documentsUnit;
    }

    public String getDocumentsUnitId() {
        return documentsUnitId;
    }

    public void setDocumentsUnitId(String documentsUnitId) {
        this.documentsUnitId = documentsUnitId;
    }

    public String getDocumentsDecimal() {
        return documentsDecimal;
    }

    public void setDocumentsDecimal(String documentsDecimal) {
        this.documentsDecimal = documentsDecimal;
    }

    public String getDocumentsNumber() {
        return documentsNumber;
    }

    public void setDocumentsNumber(String documentsNumber) {
        this.documentsNumber = documentsNumber;
    }

    public String getDocumentsAmount() {
        return documentsAmount;
    }

    public void setDocumentsAmount(String documentsAmount) {
        this.documentsAmount = documentsAmount;
    }

    public String getDocumentsType() {
        return documentsType;
    }

    public void setDocumentsType(String documentsType) {
        this.documentsType = documentsType;
    }

    public BigDecimal getAmountDebit() {
        return amountDebit;
    }

    public void setAmountDebit(BigDecimal amountDebit) {
        this.amountDebit = amountDebit;
    }

    public String getPkSubId() {
        return pkSubId;
    }

    public void setPkSubId(String pkSubId) {
        this.pkSubId = pkSubId == null ? null : pkSubId.trim();
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode == null ? null : subCode.trim();
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

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod == null ? null : accountPeriod.trim();
    }

    public String getExcelImportPeriod() {
        return excelImportPeriod;
    }

    public void setExcelImportPeriod(String excelImportPeriod) {
        this.excelImportPeriod = excelImportPeriod == null ? null : excelImportPeriod.trim();
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

    public String getExcelImportCode() {
        return excelImportCode;
    }

    public void setExcelImportCode(String excelImportCode) {
        this.excelImportCode = excelImportCode == null ? null : excelImportCode.trim();
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

    public String getExcelImportSiblingsCoding() {
        return excelImportSiblingsCoding;
    }

    public void setExcelImportSiblingsCoding(String excelImportSiblingsCoding) {
        this.excelImportSiblingsCoding = excelImportSiblingsCoding == null ? null : excelImportSiblingsCoding.trim();
    }

    public String getSiblingsCoding() {
        return siblingsCoding;
    }

    public void setSiblingsCoding(String siblingsCoding) {
        this.siblingsCoding = siblingsCoding == null ? null : siblingsCoding.trim();
    }

    public String getExcelImportSuperiorCoding() {
        return excelImportSuperiorCoding;
    }

    public void setExcelImportSuperiorCoding(String excelImportSuperiorCoding) {
        this.excelImportSuperiorCoding = excelImportSuperiorCoding == null ? null : excelImportSuperiorCoding.trim();
    }

    public String getSuperiorCoding() {
        return superiorCoding;
    }

    public void setSuperiorCoding(String superiorCoding) {
        this.superiorCoding = superiorCoding == null ? null : superiorCoding.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getSubSource() {
        return subSource;
    }

    public void setSubSource(String subSource) {
        this.subSource = subSource == null ? null : subSource.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public BigDecimal getUnitId() {
        return unitId;
    }

    public void setUnitId(BigDecimal unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender == null ? null : mender.trim();
    }

    public String getFkTBasicMeasureId() {
        return fkTBasicMeasureId;
    }

    public void setFkTBasicMeasureId(String fkTBasicMeasureId) {
        this.fkTBasicMeasureId = fkTBasicMeasureId == null ? null : fkTBasicMeasureId.trim();
    }

    public Integer getMeasureState() {
        return measureState;
    }

    public void setMeasureState(Integer measureState) {
        this.measureState = measureState;
    }

    public String getFkExchangeRateId() {
        return fkExchangeRateId;
    }

    public void setFkExchangeRateId(String fkExchangeRateId) {
        this.fkExchangeRateId = fkExchangeRateId == null ? null : fkExchangeRateId.trim();
    }

    public Integer getExchangeRateState() {
        return exchangeRateState;
    }

    public void setExchangeRateState(Integer exchangeRateState) {
        this.exchangeRateState = exchangeRateState;
    }

    public Integer getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(Integer codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getDebitCreditDirection() {
        return debitCreditDirection;
    }

    public void setDebitCreditDirection(String debitCreditDirection) {
        this.debitCreditDirection = debitCreditDirection == null ? null : debitCreditDirection.trim();
    }

    public String getSiblingsSubName() {
        return siblingsSubName;
    }

    public void setSiblingsSubName(String siblingsSubName) {
        this.siblingsSubName = siblingsSubName;
    }

}
