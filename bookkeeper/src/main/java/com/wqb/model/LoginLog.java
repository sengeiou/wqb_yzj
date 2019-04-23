package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class LoginLog implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8652562775079275891L;
    private String loginLogID;
    private String userID;
    private String loginUser;
    private String userName;
    private Date loginTime;
    private String loginIP;
    private Date createDate;

    public String getLoginLogID() {
        return loginLogID;
    }

    public void setLoginLogID(String loginLogID) {
        this.loginLogID = loginLogID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
