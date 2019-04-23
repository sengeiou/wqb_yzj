package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 司氏旭东
 * @ClassName: TBasicMeasure
 * @Description: 数量单位实体类
 * @date 2017年12月20日 上午10:01:03
 */
public class TBasicMeasure implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5213536062178339340L;

    /**
     * 数量单位主键
     */
    private String pkMeasureId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 帐套id
     */
    private String accountId;

    /**
     * 计量单位符号
     */
    private String measUnitSymbol;

    /**
     * 计量单位名称
     */
    private String measUnitName;

    /**
     * 计量单位类型
     */
    private String measUnitType;

    /**
     * 备注说明
     */
    private String measUnitRemarks;

    /**
     * 创建人ID
     */
    private String createPsnId;

    /**
     * 创建人
     */
    private String createPsn;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人ID
     */
    private String updatePsnId;

    /**
     * 修改人
     */
    private String updatePsn;

    /**
     * 修改日期
     */
    private Date updateDate;

    public String getPkMeasureId() {
        return pkMeasureId;
    }

    public void setPkMeasureId(String pkMeasureId) {
        this.pkMeasureId = pkMeasureId == null ? null : pkMeasureId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getMeasUnitSymbol() {
        return measUnitSymbol;
    }

    public void setMeasUnitSymbol(String measUnitSymbol) {
        this.measUnitSymbol = measUnitSymbol == null ? null : measUnitSymbol.trim();
    }

    public String getMeasUnitName() {
        return measUnitName;
    }

    public void setMeasUnitName(String measUnitName) {
        this.measUnitName = measUnitName == null ? null : measUnitName.trim();
    }

    public String getMeasUnitType() {
        return measUnitType;
    }

    public void setMeasUnitType(String measUnitType) {
        this.measUnitType = measUnitType == null ? null : measUnitType.trim();
    }

    public String getMeasUnitRemarks() {
        return measUnitRemarks;
    }

    public void setMeasUnitRemarks(String measUnitRemarks) {
        this.measUnitRemarks = measUnitRemarks == null ? null : measUnitRemarks.trim();
    }

    public String getCreatePsnId() {
        return createPsnId;
    }

    public void setCreatePsnId(String createPsnId) {
        this.createPsnId = createPsnId == null ? null : createPsnId.trim();
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn == null ? null : createPsn.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdatePsnId() {
        return updatePsnId;
    }

    public void setUpdatePsnId(String updatePsnId) {
        this.updatePsnId = updatePsnId == null ? null : updatePsnId.trim();
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn == null ? null : updatePsn.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
