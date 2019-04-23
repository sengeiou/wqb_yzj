package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -241774428284949877L;
    // 客户ID
    private String customID;
    // 客户名称
    private String cusName;
    // 营业性质
    private String busNature;
    // 账套ID
    private String accountID;
    // 客户详细地址
    private String cusAddress;
    // 客户电话号码
    private String cusPhone;
    // 隶属于谁ID
    private String belongPersonID;
    // 隶属人昵称
    private String belongPerName;
    // 创建人ID
    private String createPersionID;
    // 创建人昵称
    private String createPerName;
    //公司ID
    private String id;//cusId

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 创建时间
    private Date createDate;

    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getBusNature() {
        return busNature;
    }

    public void setBusNature(String busNature) {
        this.busNature = busNature;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getBelongPersonID() {
        return belongPersonID;
    }

    public void setBelongPersonID(String belongPersonID) {
        this.belongPersonID = belongPersonID;
    }

    public String getBelongPerName() {
        return belongPerName;
    }

    public void setBelongPerName(String belongPerName) {
        this.belongPerName = belongPerName;
    }

    public String getCreatePersionID() {
        return createPersionID;
    }

    public void setCreatePersionID(String createPersionID) {
        this.createPersionID = createPersionID;
    }

    public String getCreatePerName() {
        return createPerName;
    }

    public void setCreatePerName(String createPerName) {
        this.createPerName = createPerName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
