package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * StateTrack 实体类
 * 2018-06-04 lch
 */


public class StateTrack implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1992643856861913177L;
    private String st_id;    //
    private Integer isdel;    //是否一键删除 0否1是
    private Integer type;    //类型
    private String period;    //期间
    private String accountID;    //账套id
    private String userID;    //操作者
    private String des;    //备注
    private Date delDate;    //删除时间
    private Integer del_num;    //类型

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    public String getSt_id() {
        return st_id;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public Integer getDel_num() {
        return del_num;
    }

    public void setDel_num(Integer del_num) {
        this.del_num = del_num;
    }


}

