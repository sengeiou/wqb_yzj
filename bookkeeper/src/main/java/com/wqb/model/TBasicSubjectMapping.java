package com.wqb.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class TBasicSubjectMapping implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8256074236827224881L;

    /**
     * 科目关系映射主键
     */
    private Integer pkSubMappingId;

    /**
     * 映射科目编码
     */
    private String subMappingCode;

    /**
     * 映射科目名称
     */
    private String subMappingName;

    /**
     * 相似名称
     */
    private String similarName;

    /**
     * 小规模—贸易型
     */
    private Short smallScaleTrading;

    /**
     * 一般纳税人—贸易型
     */
    private Short generalTaxpayerTrading;

    /**
     * 小规模—生产型
     */
    private Short smallScaleProduction;

    /**
     * 一般纳税人—生产型
     */
    private Short generalTaxpayerProduction;

    /**
     * 小规模—进出口
     */
    private Short smallScaleImportAndExport;

    /**
     * 一般纳税人—进出口
     */
    private Short generalTaxpayerImportAndExport;

    /**
     * 小规模—高新
     */
    private Short smallScaleHighTech;

    /**
     * 一般纳税人—高新
     */
    private Short generalTaxpayerHighTech;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date createDate;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date updateDate;

    /**
     * 更新时间戳
     */
    private String updateTimestamp;

    /**
     * 用作map key值时必须重写hashCode
     *
     * @return
     * @Title: hashCode
     * @Description: (非 JavaDoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((createPerson == null) ? 0 : createPerson.hashCode());
        result = prime * result + ((generalTaxpayerHighTech == null) ? 0 : generalTaxpayerHighTech.hashCode());
        result = prime * result
                + ((generalTaxpayerImportAndExport == null) ? 0 : generalTaxpayerImportAndExport.hashCode());
        result = prime * result + ((generalTaxpayerProduction == null) ? 0 : generalTaxpayerProduction.hashCode());
        result = prime * result + ((generalTaxpayerTrading == null) ? 0 : generalTaxpayerTrading.hashCode());
        result = prime * result + ((pkSubMappingId == null) ? 0 : pkSubMappingId.hashCode());
        result = prime * result + ((similarName == null) ? 0 : similarName.hashCode());
        result = prime * result + ((smallScaleHighTech == null) ? 0 : smallScaleHighTech.hashCode());
        result = prime * result + ((smallScaleImportAndExport == null) ? 0 : smallScaleImportAndExport.hashCode());
        result = prime * result + ((smallScaleProduction == null) ? 0 : smallScaleProduction.hashCode());
        result = prime * result + ((smallScaleTrading == null) ? 0 : smallScaleTrading.hashCode());
        result = prime * result + ((subMappingCode == null) ? 0 : subMappingCode.hashCode());
        result = prime * result + ((subMappingName == null) ? 0 : subMappingName.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        result = prime * result + ((updatePerson == null) ? 0 : updatePerson.hashCode());
        result = prime * result + ((updateTimestamp == null) ? 0 : updateTimestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TBasicSubjectMapping other = (TBasicSubjectMapping) obj;
        if (createDate == null) {
            if (other.createDate != null)
                return false;
        } else if (!createDate.equals(other.createDate))
            return false;
        if (createPerson == null) {
            if (other.createPerson != null)
                return false;
        } else if (!createPerson.equals(other.createPerson))
            return false;
        if (generalTaxpayerHighTech == null) {
            if (other.generalTaxpayerHighTech != null)
                return false;
        } else if (!generalTaxpayerHighTech.equals(other.generalTaxpayerHighTech))
            return false;
        if (generalTaxpayerImportAndExport == null) {
            if (other.generalTaxpayerImportAndExport != null)
                return false;
        } else if (!generalTaxpayerImportAndExport.equals(other.generalTaxpayerImportAndExport))
            return false;
        if (generalTaxpayerProduction == null) {
            if (other.generalTaxpayerProduction != null)
                return false;
        } else if (!generalTaxpayerProduction.equals(other.generalTaxpayerProduction))
            return false;
        if (generalTaxpayerTrading == null) {
            if (other.generalTaxpayerTrading != null)
                return false;
        } else if (!generalTaxpayerTrading.equals(other.generalTaxpayerTrading))
            return false;
        if (pkSubMappingId == null) {
            if (other.pkSubMappingId != null)
                return false;
        } else if (!pkSubMappingId.equals(other.pkSubMappingId))
            return false;
        if (similarName == null) {
            if (other.similarName != null)
                return false;
        } else if (!similarName.equals(other.similarName))
            return false;
        if (smallScaleHighTech == null) {
            if (other.smallScaleHighTech != null)
                return false;
        } else if (!smallScaleHighTech.equals(other.smallScaleHighTech))
            return false;
        if (smallScaleImportAndExport == null) {
            if (other.smallScaleImportAndExport != null)
                return false;
        } else if (!smallScaleImportAndExport.equals(other.smallScaleImportAndExport))
            return false;
        if (smallScaleProduction == null) {
            if (other.smallScaleProduction != null)
                return false;
        } else if (!smallScaleProduction.equals(other.smallScaleProduction))
            return false;
        if (smallScaleTrading == null) {
            if (other.smallScaleTrading != null)
                return false;
        } else if (!smallScaleTrading.equals(other.smallScaleTrading))
            return false;
        if (subMappingCode == null) {
            if (other.subMappingCode != null)
                return false;
        } else if (!subMappingCode.equals(other.subMappingCode))
            return false;
        if (subMappingName == null) {
            if (other.subMappingName != null)
                return false;
        } else if (!subMappingName.equals(other.subMappingName))
            return false;
        if (updateDate == null) {
            if (other.updateDate != null)
                return false;
        } else if (!updateDate.equals(other.updateDate))
            return false;
        if (updatePerson == null) {
            if (other.updatePerson != null)
                return false;
        } else if (!updatePerson.equals(other.updatePerson))
            return false;
        if (updateTimestamp == null) {
            if (other.updateTimestamp != null)
                return false;
        } else if (!updateTimestamp.equals(other.updateTimestamp))
            return false;
        return true;
    }

    public final Integer getPkSubMappingId() {
        return pkSubMappingId;
    }

    public final void setPkSubMappingId(final Integer pkSubMappingId) {
        this.pkSubMappingId = pkSubMappingId;
    }

    public final String getSubMappingCode() {
        return subMappingCode;
    }

    public final void setSubMappingCode(final String subMappingCode) {
        this.subMappingCode = subMappingCode == null ? null : subMappingCode.trim();
    }

    public final String getSubMappingName() {
        return subMappingName;
    }

    public final void setSubMappingName(final String subMappingName) {
        this.subMappingName = subMappingName == null ? null : subMappingName.trim();
    }

    public final String getSimilarName() {
        return similarName;
    }

    public final void setSimilarName(final String similarName) {
        this.similarName = similarName == null ? null : similarName.trim();
    }

    public final Short getSmallScaleTrading() {
        return smallScaleTrading;
    }

    public final void setSmallScaleTrading(final Short smallScaleTrading) {
        this.smallScaleTrading = smallScaleTrading;
    }

    public final Short getGeneralTaxpayerTrading() {
        return generalTaxpayerTrading;
    }

    public final void setGeneralTaxpayerTrading(final Short generalTaxpayerTrading) {
        this.generalTaxpayerTrading = generalTaxpayerTrading;
    }

    public final Short getSmallScaleProduction() {
        return smallScaleProduction;
    }

    public final void setSmallScaleProduction(final Short smallScaleProduction) {
        this.smallScaleProduction = smallScaleProduction;
    }

    public final Short getGeneralTaxpayerProduction() {
        return generalTaxpayerProduction;
    }

    public final void setGeneralTaxpayerProduction(final Short generalTaxpayerProduction) {
        this.generalTaxpayerProduction = generalTaxpayerProduction;
    }

    public final Short getSmallScaleImportAndExport() {
        return smallScaleImportAndExport;
    }

    public final void setSmallScaleImportAndExport(final Short smallScaleImportAndExport) {
        this.smallScaleImportAndExport = smallScaleImportAndExport;
    }

    public final Short getGeneralTaxpayerImportAndExport() {
        return generalTaxpayerImportAndExport;
    }

    public final void setGeneralTaxpayerImportAndExport(final Short generalTaxpayerImportAndExport) {
        this.generalTaxpayerImportAndExport = generalTaxpayerImportAndExport;
    }

    public final Short getSmallScaleHighTech() {
        return smallScaleHighTech;
    }

    public final void setSmallScaleHighTech(final Short smallScaleHighTech) {
        this.smallScaleHighTech = smallScaleHighTech;
    }

    public final Short getGeneralTaxpayerHighTech() {
        return generalTaxpayerHighTech;
    }

    public final void setGeneralTaxpayerHighTech(final Short generalTaxpayerHighTech) {
        this.generalTaxpayerHighTech = generalTaxpayerHighTech;
    }

    public final String getCreatePerson() {
        return createPerson;
    }

    public final void setCreatePerson(final String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public final Date getCreateDate() {
        return createDate;
    }

    public final void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public final String getUpdatePerson() {
        return updatePerson;
    }

    public final void setUpdatePerson(final String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    public final Date getUpdateDate() {
        return updateDate;
    }

    public final void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

    public final String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public final void setUpdateTimestamp(final String updateTimestamp) {
        this.updateTimestamp = updateTimestamp == null ? null : updateTimestamp.trim();
    }
}
