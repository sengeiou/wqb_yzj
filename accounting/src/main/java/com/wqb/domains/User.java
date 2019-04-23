package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户登录信息表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键（同租户ID）
     */
    @TableId(value = "userID", type = IdType.UUID)
    private String userID;

    /**
     * 登录用户名(手机号)
     */
    @TableField("loginUser")
    private String loginUser;

    /**
     * 名称
     */
    @TableField("userName")
    private String userName;

    /**
     * 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
     */
    @TableField("userType")
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    /**
     * 0:正式用户 1：试用用户
     */
    private Integer type;

    /**
     * 父级ID（由谁添加的信息（最近一层父级））
     */
    @TableField("parentUser")
    private String parentUser;

    /**
     * 登录密码(des加密存储)
     */
    @JsonIgnore
    private String pasword;

    /**
     * 账号状态（0:新建1:启用、2:禁用）
     */
    private Integer state;

    /**
     * 账套数
     */
    @TableField("accountNum")
    private Integer accountNum;

    /**
     * 公司类型 1小规模 2一般纳税人
     */
    @TableField("companyType")
    private String companyType;

    /**
     * 生效日期
     */
    @TableField("ableDate")
    private Date ableDate;

    /**
     * 失效日期
     */
    @TableField("disableDate")
    private Date disableDate;

    /**
     * 备注
     */
    private String des;

    /**
     * 修改日期
     */
    @TableField("updateDate")
    private Date updateDate;

    /**
     * 修改人
     */
    @TableField("updatePsn")
    private String updatePsn;

    /**
     * 创建日期
     */
    @TableField("createDate")
    private Date createDate;

    /**
     * 创建人
     */
    @TableField("createPsn")
    private String createPsn;

    /**
     * 职位
     */
    private String jod;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别(1:男2:女)
     */
    private Integer gender;

    /**
     * 手机
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 邮编
     */
    @TableField("postCode")
    private String postCode;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 所属公司名称
     */
    private String corp;

    /**
     * 平台用户ID
     */
    private String id;

    /**
     * 身份证号
     */
    @TableField("IDcard")
    private String IDcard;

    /**
     * 权限分数    99  :平台管理员
     */
    private Integer power;

    /**
     * sessionID
     */
    @TableField("sessionID")
    private String sessionID;

    /**
     * 初始密码 随机生成
     */
    @TableField("initPassword")
    private String initPassword;

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
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
    public Integer getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(Integer accountNum) {
        this.accountNum = accountNum;
    }
    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
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
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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
    public String getJod() {
        return jod;
    }

    public void setJod(String jod) {
        this.jod = jod;
    }
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getIDcard() {
        return IDcard;
    }

    public void setIDcard(String IDcard) {
        this.IDcard = IDcard;
    }
    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getInitPassword() {
        return initPassword;
    }

    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }

    @Override
    public String toString() {
        return "User{" +
        "userID=" + userID +
        ", loginUser=" + loginUser +
        ", userName=" + userName +
        ", userType=" + userType +
        ", type=" + type +
        ", parentUser=" + parentUser +
        ", pasword=" + pasword +
        ", state=" + state +
        ", accountNum=" + accountNum +
        ", companyType=" + companyType +
        ", ableDate=" + ableDate +
        ", disableDate=" + disableDate +
        ", des=" + des +
        ", updateDate=" + updateDate +
        ", updatePsn=" + updatePsn +
        ", createDate=" + createDate +
        ", createPsn=" + createPsn +
        ", jod=" + jod +
        ", birthday=" + birthday +
        ", gender=" + gender +
        ", phone=" + phone +
        ", realname=" + realname +
        ", address=" + address +
        ", telphone=" + telphone +
        ", postCode=" + postCode +
        ", email=" + email +
        ", corp=" + corp +
        ", id=" + id +
        ", IDcard=" + IDcard +
        ", power=" + power +
        ", sessionID=" + sessionID +
        ", initPassword=" + initPassword +
        "}";
    }
}
