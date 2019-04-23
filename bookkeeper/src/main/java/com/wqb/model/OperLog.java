package com.wqb.model;

import java.io.Serializable;

public class OperLog implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3762660366507311590L;
    // 主键
    private String operLogID;
    // 外键（当前用户ID）
    private String userID;
    // 操作的账套
    private String accountID;
    // 登录用户名
    private String loginUser;
    // 用户名昵称
    private String userName;
    // 操作模块ID
    private String moduleID;
    // 模块名称
    private String moduleName;
    // 操作模块路径
    private String operModuleUrl;
    // 操作信息
    private String actionInfo;
    // 备注
    private String des;
    // 创建日期
    private String createDate;
    //账套名称
    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOperLogID() {
        return operLogID;
    }

    public void setOperLogID(String operLogID) {
        this.operLogID = operLogID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOperModuleUrl() {
        return operModuleUrl;
    }

    public void setOperModuleUrl(String operModuleUrl) {
        this.operModuleUrl = operModuleUrl;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
