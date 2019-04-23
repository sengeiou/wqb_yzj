package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户信息表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @TableId(value = "customID", type = IdType.UUID)
    private String customID;

    /**
     * 客户名称
     */
    @TableField("cusName")
    private String cusName;

    /**
     * 营业性质
     */
    @TableField("busNature")
    private String busNature;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 客户详细地址
     */
    @TableField("cusAddress")
    private String cusAddress;

    /**
     * 客户电话号码
     */
    @TableField("cusPhone")
    private String cusPhone;

    /**
     * 隶属于谁ID
     */
    @TableField("belongPersonID")
    private String belongPersonID;

    /**
     * 隶属人昵称
     */
    @TableField("belongPerName")
    private String belongPerName;

    /**
     * 创建人ID
     */
    @TableField("createPersionID")
    private String createPersionID;

    /**
     * 创建人昵称
     */
    @TableField("createPerName")
    private String createPerName;

    /**
     * 公司ID（天眼查的公司id）
     */
    private String id;

    /**
     * 创建时间
     */
    @TableField("createDate")
    private Date createDate;

    /**
     * 纳税人识别号
     */
    private String taxNum;

    /**
     * 绑定手机号
     */
    private String bindingPhone;

    /**
     * 国税密码
     */
    private String stateTaxCode;

    /**
     * 地税密码
     */
    private String landTaxCode;

    /**
     * 实名账号
     */
    private String realAccount;

    /**
     * 实名账号密码
     */
    private String realAccountCode;

    /**
     * 期间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date period;

    /**
     * 统一信用代码
     */
    private String comTyxydm;

    /**
     * 三证合一 0：否 1：是
     */
    private Integer threeAndOne;

    /**
     * 科技型中小企业0：否 1：是
     */
    private Integer comKjxzxqy;

    /**
     * 高新技术企业0：否 1：是
     */
    private Integer comGxjsqy;

    /**
     * 技术入股递延纳税事项0：否 1：是
     */
    private Integer comJsrgynssx;

    /**
     * 企业性质(1：生产型2：贸易型3：服务型4：混合型5：物业)
     */
    @TableField("companyType")
    private Integer companyType;

    /**
     * 一般纳税人（传0）   小规模（传1）
     */
    @TableField("ssType")
    private Integer ssType;

    /**
     * 会计准则(1:2007企业会计准则2:2013小企业会计准则3:新会计准则)
     */
    private Integer accstandards;

    /**
     * 记账本位币(1:人民币)
     */
    private Integer jzbwb;

    /**
     * 企业规模
     */
    private Integer comScale;

    /**
     * 城市区域
     */
    private String comCityArea;

    /**
     * 总分机构
     */
    private String comComcode;

    /**
     * 缴费方式0：微信缴税 1：银行缴税
     */
    private String payMethod;

    /**
     * 增值税0：否 1：是
     */
    private String incrementTax;

    /**
     * 企业所得税0：否 1：是
     */
    private String incomeTax;

    /**
     * 附征税0：否 1：是
     */
    private String attachmentTax;

    /**
     * 印花税0：否 1：是
     */
    private String stampDutyTax;

    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 与税务局数据同步  0：未同步  1：已同步
     */
    private String dataSynchro;

    private String province;

    @TableField("provinceName")
    private String provinceName;

    private String city;

    @TableField("cityName")
    private String cityName;

    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }
    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
    public String getBusNature() {
        return busNature;
    }

    public void setBusNature(String busNature) {
        this.busNature = busNature;
    }
    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }
    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }
    public String getBelongPersonID() {
        return belongPersonID;
    }

    public void setBelongPersonID(String belongPersonID) {
        this.belongPersonID = belongPersonID;
    }
    public String getBelongPerName() {
        return belongPerName;
    }

    public void setBelongPerName(String belongPerName) {
        this.belongPerName = belongPerName;
    }
    public String getCreatePersionID() {
        return createPersionID;
    }

    public void setCreatePersionID(String createPersionID) {
        this.createPersionID = createPersionID;
    }
    public String getCreatePerName() {
        return createPerName;
    }

    public void setCreatePerName(String createPerName) {
        this.createPerName = createPerName;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }
    public String getBindingPhone() {
        return bindingPhone;
    }

    public void setBindingPhone(String bindingPhone) {
        this.bindingPhone = bindingPhone;
    }
    public String getStateTaxCode() {
        return stateTaxCode;
    }

    public void setStateTaxCode(String stateTaxCode) {
        this.stateTaxCode = stateTaxCode;
    }
    public String getLandTaxCode() {
        return landTaxCode;
    }

    public void setLandTaxCode(String landTaxCode) {
        this.landTaxCode = landTaxCode;
    }
    public String getRealAccount() {
        return realAccount;
    }

    public void setRealAccount(String realAccount) {
        this.realAccount = realAccount;
    }
    public String getRealAccountCode() {
        return realAccountCode;
    }

    public void setRealAccountCode(String realAccountCode) {
        this.realAccountCode = realAccountCode;
    }
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }
    public String getComTyxydm() {
        return comTyxydm;
    }

    public void setComTyxydm(String comTyxydm) {
        this.comTyxydm = comTyxydm;
    }
    public Integer getThreeAndOne() {
        return threeAndOne;
    }

    public void setThreeAndOne(Integer threeAndOne) {
        this.threeAndOne = threeAndOne;
    }
    public Integer getComKjxzxqy() {
        return comKjxzxqy;
    }

    public void setComKjxzxqy(Integer comKjxzxqy) {
        this.comKjxzxqy = comKjxzxqy;
    }
    public Integer getComGxjsqy() {
        return comGxjsqy;
    }

    public void setComGxjsqy(Integer comGxjsqy) {
        this.comGxjsqy = comGxjsqy;
    }
    public Integer getComJsrgynssx() {
        return comJsrgynssx;
    }

    public void setComJsrgynssx(Integer comJsrgynssx) {
        this.comJsrgynssx = comJsrgynssx;
    }
    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }
    public Integer getSsType() {
        return ssType;
    }

    public void setSsType(Integer ssType) {
        this.ssType = ssType;
    }
    public Integer getAccstandards() {
        return accstandards;
    }

    public void setAccstandards(Integer accstandards) {
        this.accstandards = accstandards;
    }
    public Integer getJzbwb() {
        return jzbwb;
    }

    public void setJzbwb(Integer jzbwb) {
        this.jzbwb = jzbwb;
    }
    public Integer getComScale() {
        return comScale;
    }

    public void setComScale(Integer comScale) {
        this.comScale = comScale;
    }
    public String getComCityArea() {
        return comCityArea;
    }

    public void setComCityArea(String comCityArea) {
        this.comCityArea = comCityArea;
    }
    public String getComComcode() {
        return comComcode;
    }

    public void setComComcode(String comComcode) {
        this.comComcode = comComcode;
    }
    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
    public String getIncrementTax() {
        return incrementTax;
    }

    public void setIncrementTax(String incrementTax) {
        this.incrementTax = incrementTax;
    }
    public String getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(String incomeTax) {
        this.incomeTax = incomeTax;
    }
    public String getAttachmentTax() {
        return attachmentTax;
    }

    public void setAttachmentTax(String attachmentTax) {
        this.attachmentTax = attachmentTax;
    }
    public String getStampDutyTax() {
        return stampDutyTax;
    }

    public void setStampDutyTax(String stampDutyTax) {
        this.stampDutyTax = stampDutyTax;
    }
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getDataSynchro() {
        return dataSynchro;
    }

    public void setDataSynchro(String dataSynchro) {
        this.dataSynchro = dataSynchro;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "Customer{" +
        "customID=" + customID +
        ", cusName=" + cusName +
        ", busNature=" + busNature +
        ", accountID=" + accountID +
        ", cusAddress=" + cusAddress +
        ", cusPhone=" + cusPhone +
        ", belongPersonID=" + belongPersonID +
        ", belongPerName=" + belongPerName +
        ", createPersionID=" + createPersionID +
        ", createPerName=" + createPerName +
        ", id=" + id +
        ", createDate=" + createDate +
        ", taxNum=" + taxNum +
        ", bindingPhone=" + bindingPhone +
        ", stateTaxCode=" + stateTaxCode +
        ", landTaxCode=" + landTaxCode +
        ", realAccount=" + realAccount +
        ", realAccountCode=" + realAccountCode +
        ", period=" + period +
        ", comTyxydm=" + comTyxydm +
        ", threeAndOne=" + threeAndOne +
        ", comKjxzxqy=" + comKjxzxqy +
        ", comGxjsqy=" + comGxjsqy +
        ", comJsrgynssx=" + comJsrgynssx +
        ", companyType=" + companyType +
        ", ssType=" + ssType +
        ", accstandards=" + accstandards +
        ", jzbwb=" + jzbwb +
        ", comScale=" + comScale +
        ", comCityArea=" + comCityArea +
        ", comComcode=" + comComcode +
        ", payMethod=" + payMethod +
        ", incrementTax=" + incrementTax +
        ", incomeTax=" + incomeTax +
        ", attachmentTax=" + attachmentTax +
        ", stampDutyTax=" + stampDutyTax +
        ", bankName=" + bankName +
        ", bankAccount=" + bankAccount +
        ", dataSynchro=" + dataSynchro +
        ", province=" + province +
        ", provinceName=" + provinceName +
        ", city=" + city +
        ", cityName=" + cityName +
        "}";
    }
}
