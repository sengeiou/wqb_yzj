package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TBasicExchangeRate implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -100157117495164015L;

    /**
     * 汇率主键
     */
    private String pkExchangeRateId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 币别编号(外键)
     */
    private String currencyRateNo;

    /**
     * 币别全名
     */
    private String currencyFullName;

    /**
     * 币别简写
     */
    private String currencyAbbreviateName;

    /**
     * 月初 外币汇率
     */
    private BigDecimal initCurrencyRate;

    /**
     * 月末 外币汇率
     */
    private BigDecimal endingCurrencyRate;

    /**
     * 汇率时间（用于以后扩展具体哪个时间）
     */
    private Date exchangeRateDate;

    /**
     * 备注
     */
    private String exchangeRateRemarks;

    /**
     * 创建人ID
     */
    private String createPsnId;

    /**
     * 创建人名称
     */
    private String createPsn;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改人ID
     */
    private String updatePsnId;

    /**
     * 修改人名称
     */
    private String updatePsn;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 做帐日期
     */
    private Date accountDate;

    public String getPkExchangeRateId() {
        return pkExchangeRateId;
    }

    /**
     * @param pkExchangeRateId 汇率主键
     */
    public void setPkExchangeRateId(String pkExchangeRateId) {
        this.pkExchangeRateId = pkExchangeRateId == null ? null : pkExchangeRateId.trim();
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

    public String getCurrencyRateNo() {
        return currencyRateNo;
    }

    public void setCurrencyRateNo(String currencyRateNo) {
        this.currencyRateNo = currencyRateNo == null ? null : currencyRateNo.trim();
    }

    public String getCurrencyFullName() {
        return currencyFullName;
    }

    public void setCurrencyFullName(String currencyFullName) {
        this.currencyFullName = currencyFullName == null ? null : currencyFullName.trim();
    }

    public String getCurrencyAbbreviateName() {
        return currencyAbbreviateName;
    }

    public void setCurrencyAbbreviateName(String currencyAbbreviateName) {
        this.currencyAbbreviateName = currencyAbbreviateName == null ? null : currencyAbbreviateName.trim();
    }

    public BigDecimal getInitCurrencyRate() {
        return initCurrencyRate;
    }

    public void setInitCurrencyRate(BigDecimal initCurrencyRate) {
        this.initCurrencyRate = initCurrencyRate;
    }

    public BigDecimal getEndingCurrencyRate() {
        return endingCurrencyRate;
    }

    public void setEndingCurrencyRate(BigDecimal endingCurrencyRate) {
        this.endingCurrencyRate = endingCurrencyRate;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getExchangeRateRemarks() {
        return exchangeRateRemarks;
    }

    public void setExchangeRateRemarks(String exchangeRateRemarks) {
        this.exchangeRateRemarks = exchangeRateRemarks == null ? null : exchangeRateRemarks.trim();
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public Date getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(Date accountDate) {
        this.accountDate = accountDate;
    }
}
