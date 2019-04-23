package com.wqb.model;

import java.io.Serializable;

public class Smart2new implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 主键
    private String id;
    // 小企业会计准则科目编码
    private String smartSubCode;
    // 小企业会计准则科目名称
    private String smartSubName;
    // 新会计准则科目编码
    private String newSubCode;
    // 新会计准则科目名称
    private String newSubName;
    // 备注
    private String mark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmartSubCode() {
        return smartSubCode;
    }

    public void setSmartSubCode(String smartSubCode) {
        this.smartSubCode = smartSubCode;
    }

    public String getSmartSubName() {
        return smartSubName;
    }

    public void setSmartSubName(String smartSubName) {
        this.smartSubName = smartSubName;
    }

    public String getNewSubCode() {
        return newSubCode;
    }

    public void setNewSubCode(String newSubCode) {
        this.newSubCode = newSubCode;
    }

    public String getNewSubName() {
        return newSubName;
    }

    public void setNewSubName(String newSubName) {
        this.newSubName = newSubName;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
