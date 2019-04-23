package com.wqb.domains.subject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * EXCEL科目档案表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_basic_subject_message")
public class SubjectBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String pkSubId;

    /**
     * 科目编码
     */
    private String subCode;

    /**
     * 父级编码  (1级为0，二级取前4位）
     */
    private String superiorCoding;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 做帐的真实期间 年 - 月(帐套启用年-月份）
     */
    private String accountPeriod;

    /**
     * 科目名称
     */
    private String subName;

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
     * 期末余额(借方)
     */
    private BigDecimal endingBalanceDebit;

    /**
     * 期末余额(贷方)
     */
    private BigDecimal endingBalanceCredit;

    /**
     * 本年累计发生额(借方)
     */
    private BigDecimal yearAmountDebit;

    /**
     * 本年累计发生额(贷方)
     */
    private BigDecimal yearAmountCredit;

    /**
     * 是否多个同级(0无，1有)
     */
    private String isMultipleSiblings;

    /**
     * excel导入的编码
     */
    private String excelImportCode;

    /**
     * excel导入的同级编码(一个银行多个外币时用到)
     */
    private String excelImportSiblingsCoding;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 导入的期间只有月份
     */
    private String excelImportPeriod;

    /**
     * 币别
     */
    private String typeOfCurrency;

    private String siblingsSubName;

    /**
     * excel导入的上级编码(1级为0，二级取前4位）
     */
    private String excelImportSuperiorCoding;

    /**
     * 科目完整名称
     */
    private String fullName;

    /**
     * 创建时间
     */
    private Date createTime;

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
     * 同级编码(一个银行多个外币时用到)
     */
    private String siblingsCoding;

    /**
     * 启用状态(0禁用，1启用）
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
    @TableField("exchange_rate__state")
    private Integer exchangeRateState;

    /**
     * 编码级别
     */
    private Integer codeLevel;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private String debitCreditDirection;

    public String getPkSubId() {
        return pkSubId;
    }

    public void setPkSubId(String pkSubId) {
        this.pkSubId = pkSubId;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSuperiorCoding() {
        return superiorCoding;
    }

    public void setSuperiorCoding(String superiorCoding) {
        this.superiorCoding = superiorCoding;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
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

    public String getIsMultipleSiblings() {
        return isMultipleSiblings;
    }

    public void setIsMultipleSiblings(String isMultipleSiblings) {
        this.isMultipleSiblings = isMultipleSiblings;
    }

    public String getExcelImportCode() {
        return excelImportCode;
    }

    public void setExcelImportCode(String excelImportCode) {
        this.excelImportCode = excelImportCode;
    }

    public String getExcelImportSiblingsCoding() {
        return excelImportSiblingsCoding;
    }

    public void setExcelImportSiblingsCoding(String excelImportSiblingsCoding) {
        this.excelImportSiblingsCoding = excelImportSiblingsCoding;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExcelImportPeriod() {
        return excelImportPeriod;
    }

    public void setExcelImportPeriod(String excelImportPeriod) {
        this.excelImportPeriod = excelImportPeriod;
    }

    public String getTypeOfCurrency() {
        return typeOfCurrency;
    }

    public void setTypeOfCurrency(String typeOfCurrency) {
        this.typeOfCurrency = typeOfCurrency;
    }

    public String getSiblingsSubName() {
        return siblingsSubName;
    }

    public void setSiblingsSubName(String siblingsSubName) {
        this.siblingsSubName = siblingsSubName;
    }

    public String getExcelImportSuperiorCoding() {
        return excelImportSuperiorCoding;
    }

    public void setExcelImportSuperiorCoding(String excelImportSuperiorCoding) {
        this.excelImportSuperiorCoding = excelImportSuperiorCoding;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        this.updateTimestamp = updateTimestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubSource() {
        return subSource;
    }

    public void setSubSource(String subSource) {
        this.subSource = subSource;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getSiblingsCoding() {
        return siblingsCoding;
    }

    public void setSiblingsCoding(String siblingsCoding) {
        this.siblingsCoding = siblingsCoding;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public String getFkTBasicMeasureId() {
        return fkTBasicMeasureId;
    }

    public void setFkTBasicMeasureId(String fkTBasicMeasureId) {
        this.fkTBasicMeasureId = fkTBasicMeasureId;
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
        this.fkExchangeRateId = fkExchangeRateId;
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
        this.debitCreditDirection = debitCreditDirection;
    }

    @Override
    public String toString() {
        return "SubjectBalance{" +
                "pkSubId=" + pkSubId +
                ", subCode=" + subCode +
                ", superiorCoding=" + superiorCoding +
                ", accountId=" + accountId +
                ", accountPeriod=" + accountPeriod +
                ", subName=" + subName +
                ", initDebitBalance=" + initDebitBalance +
                ", initCreditBalance=" + initCreditBalance +
                ", currentAmountDebit=" + currentAmountDebit +
                ", currentAmountCredit=" + currentAmountCredit +
                ", endingBalanceDebit=" + endingBalanceDebit +
                ", endingBalanceCredit=" + endingBalanceCredit +
                ", yearAmountDebit=" + yearAmountDebit +
                ", yearAmountCredit=" + yearAmountCredit +
                ", isMultipleSiblings=" + isMultipleSiblings +
                ", excelImportCode=" + excelImportCode +
                ", excelImportSiblingsCoding=" + excelImportSiblingsCoding +
                ", userId=" + userId +
                ", excelImportPeriod=" + excelImportPeriod +
                ", typeOfCurrency=" + typeOfCurrency +
                ", siblingsSubName=" + siblingsSubName +
                ", excelImportSuperiorCoding=" + excelImportSuperiorCoding +
                ", fullName=" + fullName +
                ", updateDate=" + updateDate +
                ", updateTimestamp=" + updateTimestamp +
                ", category=" + category +
                ", subSource=" + subSource +
                ", unit=" + unit +
                ", unitId=" + unitId +
                ", price=" + price +
                ", number=" + number +
                ", amount=" + amount +
                ", siblingsCoding=" + siblingsCoding +
                ", state=" + state +
                ", mender=" + mender +
                ", fkTBasicMeasureId=" + fkTBasicMeasureId +
                ", measureState=" + measureState +
                ", fkExchangeRateId=" + fkExchangeRateId +
                ", exchangeRateState=" + exchangeRateState +
                ", codeLevel=" + codeLevel +
                ", debitCreditDirection=" + debitCreditDirection +
                "}";
    }
}
