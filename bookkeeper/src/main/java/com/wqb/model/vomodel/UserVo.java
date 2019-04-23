package com.wqb.model.vomodel;

import java.io.Serializable;

public class UserVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 414787227045674068L;
    // 主键
    private String userID;
    // 登录用户名
    private String loginUser;
    // 昵称
    private String userName;
    // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端)3:C端管理员4:内部用户5:B端员工用户6:C端员工用户）
    private Integer userType;
    // 父级ID（由谁添加的信息（最近一层父级））
    private String parentUser;
    // 试用类型(0:非试用用户 1：试用用户)
    private Integer type;
    //公司类型   1小规模 2一般纳税人
    private String companyType;

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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getParentUser() {
        return parentUser;
    }

    public void setParentUser(String parentUser) {
        this.parentUser = parentUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    @Override
    public String toString() {
        return "UserVo [userID=" + userID + ", loginUser=" + loginUser + ", userName=" + userName + ", userType="
                + userType + ", parentUser=" + parentUser + ", type=" + type + ", companyType=" + companyType + "]";
    }


}
