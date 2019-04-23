package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1104884499842620951L;
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
    // 会计准则(1:2007企业会计准则2:2013小企业会计准则3:新会计准则)
    private Integer accstandards;
    // 科目方案(会计准则)
    private String calculate;
    // 修改人ID
    private String updatepsnID;
    // 修改人
    private String updatepsn;
    // 修改时间
    private Date updatedate;
    // 创建人ID
    private String createpsnID;
    // 创建人
    private String createpsn;
    // 创建时间
    private Date createDate;
    // 备注
    private String des;
    // 最后一次使用时间[当前登陆用户默认使用的账套设置为最后一次使用的账套，如若是第一次登陆，随机指定一个账套
    private Date lastTime;
    // 最后使用期间
    private String useLastPeriod;
    // 账套状态（1:启用2:禁用）
    private Integer statu;
    //企业性质(1：生产型2：贸易型3：服务型)
    private Integer companyType;
    private Integer ssType;
    private String type;
    private String source;
    private String companyNamePinYin;

    //tangsheng--超级管理员登陆后，扇形图统计数量的账套客户显示新增的转换存储字段begin
    private String statuperiod;
    private String phoneNumber;
    private String agencyCompany;
    private String agent;
    private String schedule;
    private String statutype;
    /**
     * 初始化状态(0没有初始化，1已经初始化)
     */
    private Integer initialStates;

    /**
     * 是否映射状态（0.未映射 1.已映射）
     */
    private Integer mappingStates;

    //tangsheng--超级管理员登陆后，扇形图统计数量的账套客户显示新增的转换存储字段begin

    public Integer getMappingStates() {
        return mappingStates;
    }

    public void setMappingStates(Integer mappingStates) {
        this.mappingStates = mappingStates;
    }

    // 帐套修改时间
    private Date chgStatuTime;

    public Date getChgStatuTime() {
        return chgStatuTime;
    }

    public void setChgStatuTime(Date chgStatuTime) {
        this.chgStatuTime = chgStatuTime;
    }

    public Integer getSsType() {
        return ssType;
    }

    public String getStatuperiod() {
        return statuperiod;
    }

    public void setStatuperiod(String statuperiod) {
        this.statuperiod = statuperiod;
    }

    public String getStatutype() {
        return statutype;
    }

    public void setStatutype(String statutype) {
        this.statutype = statutype;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAgencyCompany() {
        return agencyCompany;
    }

    public void setAgencyCompany(String agencyCompany) {
        this.agencyCompany = agencyCompany;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setSsType(Integer ssType) {
        this.ssType = ssType;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

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

    public Integer getAccstandards() {
        return accstandards;
    }

    public void setAccstandards(Integer accstandards) {
        this.accstandards = accstandards;
    }

    public String getCalculate() {
        return calculate;
    }

    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }

    public String getUpdatepsnID() {
        return updatepsnID;
    }

    public void setUpdatepsnID(String updatepsnID) {
        this.updatepsnID = updatepsnID;
    }

    public String getUpdatepsn() {
        return updatepsn;
    }

    public void setUpdatepsn(String updatepsn) {
        this.updatepsn = updatepsn;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getCreatepsnID() {
        return createpsnID;
    }

    public void setCreatepsnID(String createpsnID) {
        this.createpsnID = createpsnID;
    }

    public String getCreatepsn() {
        return createpsn;
    }

    public void setCreatepsn(String createpsn) {
        this.createpsn = createpsn;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public Integer getInitialStates() {
        return initialStates;
    }

    public void setInitialStates(Integer initialStates) {
        this.initialStates = initialStates;
    }


    public String getUseLastPeriod() {
        return useLastPeriod;
    }

    public void setUseLastPeriod(String useLastPeriod) {
        this.useLastPeriod = useLastPeriod;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getCompanyNamePinYin() {
        return companyNamePinYin;
    }

    public void setCompanyNamePinYin(String companyNamePinYin) {
        this.companyNamePinYin = companyNamePinYin;
    }

    @Override
    public String toString() {
        return "Account [accountID=" + accountID + ", userID=" + userID + ", customerID=" + customerID
                + ", companyName=" + companyName + ", period=" + period + ", accstandards=" + accstandards
                + ", createpsnID=" + createpsnID + ", createDate=" + createDate + ", des=" + des + ", statu=" + statu
                + ", companyType=" + companyType + ", ssType=" + ssType + ", initialStates=" + initialStates + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
        result = prime * result + ((userID == null) ? 0 : userID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (accountID == null) {
            if (other.accountID != null)
                return false;
        } else if (!accountID.equals(other.accountID))
            return false;
        if (userID == null) {
            if (other.userID != null)
                return false;
        } else if (!userID.equals(other.userID))
            return false;
        return true;
    }


}
