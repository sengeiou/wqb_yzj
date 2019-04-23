package com.wqb.model.vomodel;

import java.io.Serializable;
import java.util.Date;

public class AccStatusPeriod implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6917852213786422L;
    // 主键
    private String accountID;
    // 用户ID(外键)
    private String userID;
    // 客户ID
    private String customerID;
    // 公司名称
    private String companyName;
    // 期间
    private Date period;

    // 最后一次使用时间[当前登陆用户默认使用的账套设置为最后一次使用的账套，如若是第一次登陆，随机指定一个账套
    private Date lastTime;

    //statusperiod
    // 账套状态（1:启用2:禁用）
    private Integer statu;
    private String source;
    private String speriod;  //statusperiod 记录期间状态


    // 是否已一键生成凭证（0：未 1：已生成）
    private Integer isCreateVoucher;
    // 是否结账(0：未结账1:已结账)',
    private Integer isJz;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getIsCreateVoucher() {
        return isCreateVoucher;
    }

    public void setIsCreateVoucher(Integer isCreateVoucher) {
        this.isCreateVoucher = isCreateVoucher;
    }

    public Integer getIsJz() {
        return isJz;
    }

    public void setIsJz(Integer isJz) {
        this.isJz = isJz;
    }

    public String getSperiod() {
        return speriod;
    }

    public void setSperiod(String speriod) {
        this.speriod = speriod;
    }

    @Override
    public String toString() {
        return "AccStatusPeriod [accountID=" + accountID + ", userID=" + userID + ", customerID=" + customerID
                + ", companyName=" + companyName + ", period=" + period + ", lastTime=" + lastTime + ", statu=" + statu
                + ", source=" + source + ", speriod=" + speriod + ", isCreateVoucher=" + isCreateVoucher + ", isJz="
                + isJz + "]";
    }


}
