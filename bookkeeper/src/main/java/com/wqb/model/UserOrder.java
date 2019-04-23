package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * UserOrder 实体类
 * 2018-12-06 lch
 */


public class UserOrder implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3740149469718670234L;
    private String id;    //主键
    private String user_id;    //用户表id 外键
    private String platformId;    //平台id
    private String userid;    //来自平台用户id
    private String orderNumber;    //订单编号
    private String produceNumber;    //产品编号
    private String userType;    //用户类型 1云记账  2云代账
    private String buyType;    //购买类型  1购买 2续费
    private String companyType;    //公司类型 1小规模、2一般纳税人   3智能云代账
    private String mobile;    //手机号
    private String password;    //登录密码(des加密存储)
    private String successTime;    //下单时间  支付成功的时间
    private Integer accountNum;    //账套数
    private String month;    //时间周期 统一用月份
    private Double price;    //金额 以订单金额为准
    private String email;    //邮箱
    private Date synchronizeTime;    //同步时间
    private String des;    //描述

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setProduceNumber(String produceNumber) {
        this.produceNumber = produceNumber;
    }

    public String getProduceNumber() {
        return produceNumber;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setAccountNum(Integer accountNum) {
        this.accountNum = accountNum;
    }

    public Integer getAccountNum() {
        return accountNum;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSynchronizeTime(Date synchronizeTime) {
        this.synchronizeTime = synchronizeTime;
    }

    public Date getSynchronizeTime() {
        return synchronizeTime;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    @Override
    public String toString() {
        return "UserOrder [id=" + id + ", user_id=" + user_id + ", platformId=" + platformId + ", userid=" + userid
                + ", orderNumber=" + orderNumber + ", produceNumber=" + produceNumber + ", userType=" + userType
                + ", buyType=" + buyType + ", companyType=" + companyType + ", mobile=" + mobile + ", password="
                + password + ", successTime=" + successTime + ", accountNum=" + accountNum + ", month=" + month
                + ", price=" + price + ", email=" + email + ", synchronizeTime=" + synchronizeTime + ", des=" + des
                + "]";
    }


}

