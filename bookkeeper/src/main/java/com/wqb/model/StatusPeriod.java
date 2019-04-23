package com.wqb.model;

import java.io.Serializable;

public class StatusPeriod implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4453400651278923589L;
    // 主键
    private String periodID;
    // 期间
    private String period;
    // 是否已一键生成凭证（0：未 1：已生成）
    private Integer isCreateVoucher;
    // 是否计提
    private Integer isJt;

    // 是否结转（0否1是）
    private Integer isCarryState;
    // 创建人ID
    private String createPsnID;
    // 创建人
    private String createPsn;
    // 账套ID
    private String accountID;
    // `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
    private Integer isJz;

    private Integer isCheck;

    // 风险检测 是否检测通过0:未通过1:通过
    private Integer isDetection;

    public Integer getIsDetection() {
        return isDetection;
    }

    public void setIsDetection(Integer isDetection) {
        this.isDetection = isDetection;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getIsJz() {
        return isJz;
    }

    public void setIsJz(Integer isJz) {
        this.isJz = isJz;
    }

    public String getPeriodID() {
        return periodID;
    }

    public void setPeriodID(String periodID) {
        this.periodID = periodID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getIsCreateVoucher() {
        return isCreateVoucher;
    }

    public void setIsCreateVoucher(Integer isCreateVoucher) {
        this.isCreateVoucher = isCreateVoucher;
    }

    public Integer getIsCarryState() {
        return isCarryState;
    }

    public void setIsCarryState(Integer isCarryState) {
        this.isCarryState = isCarryState;
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

    public Integer getIsJt() {
        return isJt;
    }

    public void setIsJt(Integer isJt) {
        this.isJt = isJt;
    }

}
