package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

//固定资产实体

public class Assets implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8926498520910478823L;
    // 29
    private String assetsID; // 主键
    private String picture; // 图片(存储图片内容)
    private String asCode; // 资产编码
    private String asName; // 资产名称
    private Double asvalue; // 原值
    private Integer asyears; // 年限
    private String department; // 使用部门
    private String asState; // 资产状态
    private Double initdepreciation; // 期初已折旧金额decimal(10,0)
    private Double residualrate; // 折旧率decimal(10,0)
    private Double netvalue; // 剩余折旧金额
    private Date inputPeriod; // 录入日期datetime
    private Date updateDate; // 修改时间datetime
    private String accountID; // 账套ID
    private String updatePsnID; // 修改人ID
    private String updatePsn; // 修改人
    private String createPsnID; // 创建人ID
    private Date createDate; // 创建时间datetime
    private String createPsn; // 创建人
    private String des; // 说明备注
    private String dmethod; // 折旧方法
    private String dsubject; // 折旧摊销科目
    private String costsubject; // 成本费用科目
    private Integer usedyears; // 已使用年限int 11
    private Date useddate; // 使用日期 datetime
    private String sourceway; // 增加资产方式
    private String gdsubject; // 固定资产科目
    private String gdStatus; // 是否计提varchar(4)

    private String asModel; // 型号
    private String asCategory; // 类别
    private String asPosition; // 存放地点
    private String asManufactor; // 生产厂家
    private Date asManufactorDate; // 生产日期
    private Date asAccountDatea; // 入账日期
    private String isBeforeUse; // 入账前已开始使用( 1是 2 否)
    private Date asBeforeUseDate; // 入账前开始使用日期
    private Double asEstimatePeriod; // 预计使用期间(工作总量：月为单位)
    private Double asAddDeprecia; // 累计折旧
    private Double asCumulativeImpairment; // 累计减值准备
    private Double asWorth; // 净值
    private Double asNetSalvage; // 预计净残值
    private Double asUseDepreciaValue; // 用于折旧计算的原值
    private Double asDepreciaPeriod; // 用于折旧计算的预计使用期间(工作总量)
    private Double asExpectedPeriod; // 预计剩余折旧期间数(工作总量)
    private String asCumulativeSubject; // 累计折旧科目
    private String asEconomicUse; // 经济用途
    private String asDepreciaSubject; // 折旧费用科目
    private String importFlg; // 是否导入
    private Double taxRate; // 应交税费
    private String vouchID; // 应交税费

    // 折旧明细属性
    // 主键(折旧主键)
    private String zjid;
    // 实际月折旧额
    private String ssyzje;
    // 折旧期间
    private String period;
    // 月折旧率
    private String yjzl;

    private Double qmSum;
    private Double bqSum;

    public Double getQmSum() {
        return qmSum;
    }

    public void setQmSum(Double qmSum) {
        this.qmSum = qmSum;
    }

    public Double getBqSum() {
        return bqSum;
    }

    public void setBqSum(Double bqSum) {
        this.bqSum = bqSum;
    }

    public String getZjid() {
        return zjid;
    }

    public void setZjid(String zjid) {
        this.zjid = zjid;
    }

    public String getSsyzje() {
        return ssyzje;
    }

    public void setSsyzje(String ssyzje) {
        this.ssyzje = ssyzje;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYjzl() {
        return yjzl;
    }

    public void setYjzl(String yjzl) {
        this.yjzl = yjzl;
    }

    public String getImportFlg() {
        return importFlg;
    }

    public void setImportFlg(String importFlg) {
        this.importFlg = importFlg;
    }

    public String getAssetsID() {
        return assetsID;
    }

    public void setAssetsID(String assetsID) {
        this.assetsID = assetsID;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAsCode() {
        return asCode;
    }

    public void setAsCode(String asCode) {
        this.asCode = asCode;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public Double getAsvalue() {
        return asvalue;
    }

    public void setAsvalue(Double asvalue) {
        this.asvalue = asvalue;
    }

    public Integer getAsyears() {
        return asyears;
    }

    public void setAsyears(Integer asyears) {
        this.asyears = asyears;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAsState() {
        return asState;
    }

    public void setAsState(String asState) {
        this.asState = asState;
    }

    public Double getInitdepreciation() {
        return initdepreciation;
    }

    public void setInitdepreciation(Double initdepreciation) {
        this.initdepreciation = initdepreciation;
    }

    public Double getResidualrate() {
        return residualrate;
    }

    public void setResidualrate(Double residualrate) {
        this.residualrate = residualrate;
    }

    public Double getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(Double netvalue) {
        this.netvalue = netvalue;
    }

    public Date getInputPeriod() {
        return inputPeriod;
    }

    public void setInputPeriod(Date inputPeriod) {
        this.inputPeriod = inputPeriod;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
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

    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDmethod() {
        return dmethod;
    }

    public void setDmethod(String dmethod) {
        this.dmethod = dmethod;
    }

    public String getDsubject() {
        return dsubject;
    }

    public void setDsubject(String dsubject) {
        this.dsubject = dsubject;
    }

    public String getCostsubject() {
        return costsubject;
    }

    public void setCostsubject(String costsubject) {
        this.costsubject = costsubject;
    }

    public Integer getUsedyears() {
        return usedyears;
    }

    public void setUsedyears(Integer usedyears) {
        this.usedyears = usedyears;
    }

    public Date getUseddate() {
        return useddate;
    }

    public void setUseddate(Date useddate) {
        this.useddate = useddate;
    }

    public String getSourceway() {
        return sourceway;
    }

    public void setSourceway(String sourceway) {
        this.sourceway = sourceway;
    }

    public String getGdsubject() {
        return gdsubject;
    }

    public void setGdsubject(String gdsubject) {
        this.gdsubject = gdsubject;
    }

    public String getGdStatus() {
        return gdStatus;
    }

    public void setGdStatus(String gdStatus) {
        this.gdStatus = gdStatus;
    }

    public String getAsModel() {
        return asModel;
    }

    public void setAsModel(String asModel) {
        this.asModel = asModel;
    }

    public String getAsCategory() {
        return asCategory;
    }

    public void setAsCategory(String asCategory) {
        this.asCategory = asCategory;
    }

    public String getAsPosition() {
        return asPosition;
    }

    public void setAsPosition(String asPosition) {
        this.asPosition = asPosition;
    }

    public String getAsManufactor() {
        return asManufactor;
    }

    public void setAsManufactor(String asManufactor) {
        this.asManufactor = asManufactor;
    }

    public Date getAsManufactorDate() {
        return asManufactorDate;
    }

    public void setAsManufactorDate(Date asManufactorDate) {
        this.asManufactorDate = asManufactorDate;
    }

    public Date getAsAccountDatea() {
        return asAccountDatea;
    }

    public void setAsAccountDatea(Date asAccountDatea) {
        this.asAccountDatea = asAccountDatea;
    }

    public String getIsBeforeUse() {
        return isBeforeUse;
    }

    public void setIsBeforeUse(String isBeforeUse) {
        this.isBeforeUse = isBeforeUse;
    }

    public Date getAsBeforeUseDate() {
        return asBeforeUseDate;
    }

    public void setAsBeforeUseDate(Date asBeforeUseDate) {
        this.asBeforeUseDate = asBeforeUseDate;
    }

    public Double getAsEstimatePeriod() {
        return asEstimatePeriod;
    }

    public void setAsEstimatePeriod(Double asEstimatePeriod) {
        this.asEstimatePeriod = asEstimatePeriod;
    }

    public Double getAsAddDeprecia() {
        return asAddDeprecia;
    }

    public void setAsAddDeprecia(Double asAddDeprecia) {
        this.asAddDeprecia = asAddDeprecia;
    }

    public Double getAsCumulativeImpairment() {
        return asCumulativeImpairment;
    }

    public void setAsCumulativeImpairment(Double asCumulativeImpairment) {
        this.asCumulativeImpairment = asCumulativeImpairment;
    }

    public Double getAsWorth() {
        return asWorth;
    }

    public void setAsWorth(Double asWorth) {
        this.asWorth = asWorth;
    }

    public Double getAsNetSalvage() {
        return asNetSalvage;
    }

    public void setAsNetSalvage(Double asNetSalvage) {
        this.asNetSalvage = asNetSalvage;
    }

    public Double getAsUseDepreciaValue() {
        return asUseDepreciaValue;
    }

    public void setAsUseDepreciaValue(Double asUseDepreciaValue) {
        this.asUseDepreciaValue = asUseDepreciaValue;
    }

    public Double getAsDepreciaPeriod() {
        return asDepreciaPeriod;
    }

    public void setAsDepreciaPeriod(Double asDepreciaPeriod) {
        this.asDepreciaPeriod = asDepreciaPeriod;
    }

    public Double getAsExpectedPeriod() {
        return asExpectedPeriod;
    }

    public void setAsExpectedPeriod(Double asExpectedPeriod) {
        this.asExpectedPeriod = asExpectedPeriod;
    }

    public String getAsCumulativeSubject() {
        return asCumulativeSubject;
    }

    public void setAsCumulativeSubject(String asCumulativeSubject) {
        this.asCumulativeSubject = asCumulativeSubject;
    }

    public String getAsEconomicUse() {
        return asEconomicUse;
    }

    public void setAsEconomicUse(String asEconomicUse) {
        this.asEconomicUse = asEconomicUse;
    }

    public String getAsDepreciaSubject() {
        return asDepreciaSubject;
    }

    public void setAsDepreciaSubject(String asDepreciaSubject) {
        this.asDepreciaSubject = asDepreciaSubject;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    @Override
    public String toString() {
        return "Assets [assetsID=" + assetsID + ", picture=" + picture + ", asCode=" + asCode + ", asName=" + asName
                + ", asvalue=" + asvalue + ", asyears=" + asyears + ", department=" + department + ", asState="
                + asState + ", initdepreciation=" + initdepreciation + ", residualrate=" + residualrate + ", netvalue="
                + netvalue + ", inputPeriod=" + inputPeriod + ", updateDate=" + updateDate + ", accountID=" + accountID
                + ", updatePsnID=" + updatePsnID + ", updatePsn=" + updatePsn + ", createPsnID=" + createPsnID
                + ", createDate=" + createDate + ", createPsn=" + createPsn + ", des=" + des + ", dmethod=" + dmethod
                + ", dsubject=" + dsubject + ", costsubject=" + costsubject + ", usedyears=" + usedyears + ", useddate="
                + useddate + ", sourceway=" + sourceway + ", gdsubject=" + gdsubject + ", gdStatus=" + gdStatus
                + ", asModel=" + asModel + ", asCategory=" + asCategory + ", asPosition=" + asPosition
                + ", asManufactor=" + asManufactor + ", asManufactorDate=" + asManufactorDate + ", asAccountDatea="
                + asAccountDatea + ", isBeforeUse=" + isBeforeUse + ", asBeforeUseDate=" + asBeforeUseDate
                + ", asEstimatePeriod=" + asEstimatePeriod + ", asAddDeprecia=" + asAddDeprecia
                + ", asCumulativeImpairment=" + asCumulativeImpairment + ", asWorth=" + asWorth + ", asNetSalvage="
                + asNetSalvage + ", asUseDepreciaValue=" + asUseDepreciaValue + ", asDepreciaPeriod=" + asDepreciaPeriod
                + ", asExpectedPeriod=" + asExpectedPeriod + ", asCumulativeSubject=" + asCumulativeSubject
                + ", asEconomicUse=" + asEconomicUse + ", asDepreciaSubject=" + asDepreciaSubject + ", importFlg="
                + importFlg + ", taxRate=" + taxRate + ", vouchID=" + vouchID + "]";
    }

}
