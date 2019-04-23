package com.wqb.model.vomodel;

import java.io.Serializable;

//页面查询全部科目
public class TempVo implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -5864642202752303327L;
    //{"zy":"购买钢材","shuCode":"1405002","subName":"库存商品_钢材","number":"100","price":"18.88","dir":"2","amount":"1888"}
    private String zy;
    private String subCode;
    private String subName; //科目全名
    private String number;
    private String price;
    private String dir;
    private String amount;

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TempVo [zy=" + zy + ", subCode=" + subCode + ", subName=" + subName + ", number=" + number + ", price="
                + price + ", dir=" + dir + ", amount=" + amount + "]";
    }


}
