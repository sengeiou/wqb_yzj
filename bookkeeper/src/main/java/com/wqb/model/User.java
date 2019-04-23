package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1294450715341905505L;
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
    // 登录密码(MD5加密存储)
    private String pasword;
    // 账号状态（1:启用、2:禁用）
    private Integer state;
    // 备注
    private String des;
    // 生效日期
    private Date ableDate;
    // 失效日期
    private Date disableDate;
    // 修改日期
    private Date updateDate;
    // 修改人
    private String updatePsn;
    // 创建日期
    private Date createDate;
    // 创建人
    private String createPsn;
    // 职位
    private String jod;
    // 生日
    private String birthday;
    // 性别(1:男2:女)
    private int gender;
    // 手机
    private String phone;
    // 真实姓名
    private String realname;
    // 地址
    private String address;
    // 电话
    private String telphone;
    // 邮编
    private String postCode;
    // 邮箱
    private String email;
    // 所属公司名称
    private String corp;
    // 身份证号
    private String IDcard;
    // 平台ID
    private String Id;
    // 试用类型(0:非试用用户 1：试用用户)
    private Integer type;
    // 权限分数100:唯一超管 99:临时超管
    private Integer power;
    // 登录用户的sessionID
    private String sessionID;
    //公司类型   1小规模 2一般纳税人
    private String companyType;
    //账套数
    private Integer accountNum;  //账套数
    //初始密码
    private String initPassword;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getJod() {
        return jod;
    }

    public void setJod(String jod) {
        this.jod = jod;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getIDcard() {
        return IDcard;
    }

    public void setIDcard(String iDcard) {
        IDcard = iDcard;
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

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getAbleDate() {
        return ableDate;
    }

    public void setAbleDate(Date ableDate) {
        this.ableDate = ableDate;
    }

    public Date getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn;
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

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }


    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }


    public Integer getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(Integer accountNum) {
        this.accountNum = accountNum;
    }


    public String getInitPassword() {
        return initPassword;
    }

    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }


    @Override
    public String toString() {
        return "User [userID=" + userID + ", loginUser=" + loginUser + ", userName=" + userName + ", userType="
                + userType + ", parentUser=" + parentUser + ", pasword=" + pasword + ", state=" + state + ", des=" + des
                + ", ableDate=" + ableDate + ", disableDate=" + disableDate + ", phone=" + phone + ", realname="
                + realname + ", type=" + type + ", sessionID=" + sessionID + ", companyType=" + companyType
                + ", accountNum=" + accountNum + ", initPassword=" + initPassword + "]";
    }

    public boolean equals(Object o) {
        // 如果地址一样，则两个对象相同
        if (this == o) {
            return true;
        }
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        // 如果两个对象是同一类型，则比较其属性值是否都相同。如果都相同，则说明两个对象也相同；否则，说明这两个对象不相同。
        if (o instanceof User) {
            User co = (User) o;
            boolean b = (this.userID.equals(co.userID));
            return b;
        }
        return false;
    }

    /**
     * 重写hashcode()方法，以用户userID为依据
     *
     * @return
     */
    public int hashCode() {
        return Integer.parseInt(loginUser);
    }

}
