package com.wqb.model;

import java.io.Serializable;

/**
 * UserAccount 实体类
 * 2018-06-26 lch
 */


public class UserAccount implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3364665592002834804L;
    private String mg_id;    //主键
    private String userID;    //管理员id
    private String accountID;    //账套
    private String user_phone;    //账套
    private Long lastTime;    //最后切换账套时间

    public String getMg_id() {
        return mg_id;
    }

    public void setMg_id(String mg_id) {
        this.mg_id = mg_id;
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

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "UserAccount [mg_id=" + mg_id + ", userID=" + userID + ", accountID=" + accountID + ", user_phone="
                + user_phone + ", lastTime=" + lastTime + "]";
    }


}

