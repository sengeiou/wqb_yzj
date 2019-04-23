package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class IndexEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6612371215580606158L;
    // 企业名称
    private String comName;
    // 客户名称
    private String cusName;
    // 手机号码
    private String phone;
    // 任务名称
    private String rwType;
    // 启用日期
    private String qyDate;
    // 结束日期
    private String jsDate;
    // 处理人
    private String clr;
    // 状态
    private String statu;
    // 创建时间
    private Date createDate;

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRwType() {
        return rwType;
    }

    public void setRwType(String rwType) {
        this.rwType = rwType;
    }

    public String getQyDate() {
        return qyDate;
    }

    public void setQyDate(String qyDate) {
        this.qyDate = qyDate;
    }

    public String getJsDate() {
        return jsDate;
    }

    public void setJsDate(String jsDate) {
        this.jsDate = jsDate;
    }

    public String getClr() {
        return clr;
    }

    public void setClr(String clr) {
        this.clr = clr;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
