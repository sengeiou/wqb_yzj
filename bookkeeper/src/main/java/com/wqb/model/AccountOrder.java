package com.wqb.model;

import java.io.Serializable;

//用户再商城购买云记账或者云代账同步数据到做账系统 bean


public class AccountOrder implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5236366548726217260L;
    // 商城平台系统ID  商城系统的代码：XSC
    private String sourceCode;
    // 用户ID
    private String userid;
    //手机号
    private String mobile;
    //密码
    private String password;
    //邮箱
    private String email;
    //订单编号
    private String orderNumber;
    //用户类型  (云记账1、云代账2)
    private String productJ;
    //购买类型    购买1、续费2
    private String productType;
    //产品类型  1小规模、2一般纳税人  3智能云代账
    private String ssType;
    //下单时间
    private String SuccessTime;
    //时间周期  所购买周期如12个月，24个月等(统一用月份，若云记账是年则传值前转化为月份)
    private String month;
    //账套数  云记账1、云代账以实际 商品为准（例如500家）
    private Integer account;
    //金额
    private Double sprice;
    //产品编号
    private String productsn;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductJ() {
        return productJ;
    }

    public void setProductJ(String productJ) {
        this.productJ = productJ;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }

    public String getSuccessTime() {
        return SuccessTime;
    }

    public void setSuccessTime(String successTime) {
        SuccessTime = successTime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Double getSprice() {
        return sprice;
    }

    public void setSprice(Double sprice) {
        this.sprice = sprice;
    }

    public String getProductsn() {
        return productsn;
    }

    public void setProductsn(String productsn) {
        this.productsn = productsn;
    }

    @Override
    public String toString() {
        return "AccountOrder [sourceCode=" + sourceCode + ", userid=" + userid + ", mobile=" + mobile + ", password="
                + password + ", email=" + email + ", orderNumber=" + orderNumber + ", productJ=" + productJ
                + ", productType=" + productType + ", ssType=" + ssType + ", SuccessTime=" + SuccessTime + ", month="
                + month + ", account=" + account + ", sprice=" + sprice + ", productsn=" + productsn + "]";
    }


}
