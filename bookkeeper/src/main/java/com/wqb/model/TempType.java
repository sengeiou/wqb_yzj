package com.wqb.model;

import java.util.Date;

//模板类型
public class TempType {

    private Integer tempID;
    private String accountID;
    private Integer parentID;
    private String tempName;
    private String assistName;
    private Integer tempSoure; //模板来源 0 内置 1 自加
    private Integer saveAmount; //0 不保持  1 保持
    private String vbContent; //josn 凭证体
    private Date updateA;
    private Long updateB;
    private String des;

    public Integer getTempID() {
        return tempID;
    }

    public void setTempID(Integer tempID) {
        this.tempID = tempID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getAssistName() {
        return assistName;
    }

    public void setAssistName(String assistName) {
        this.assistName = assistName;
    }

    public Integer getTempSoure() {
        return tempSoure;
    }

    public void setTempSoure(Integer tempSoure) {
        this.tempSoure = tempSoure;
    }

    public Integer getSaveAmount() {
        return saveAmount;
    }

    public void setSaveAmount(Integer saveAmount) {
        this.saveAmount = saveAmount;
    }

    public String getVbContent() {
        return vbContent;
    }

    public void setVbContent(String vbContent) {
        this.vbContent = vbContent;
    }

    public Date getUpdateA() {
        return updateA;
    }

    public void setUpdateA(Date updateA) {
        this.updateA = updateA;
    }

    public Long getUpdateB() {
        return updateB;
    }

    public void setUpdateB(Long updateB) {
        this.updateB = updateB;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "TempType [tempID=" + tempID + ", accountID=" + accountID + ", parentID=" + parentID + ", tempName="
                + tempName + ", assistName=" + assistName + ", tempSoure=" + tempSoure + ", saveAmount=" + saveAmount
                + ", vbContent=" + vbContent + ", updateA=" + updateA + ", updateB=" + updateB + ", des=" + des + "]";
    }


}
