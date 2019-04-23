package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 账套信息表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_basic_account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "accountID", type = IdType.UUID)
    private String accountID;

    /**
     * 用户ID(外键)
     */
    @TableField("userID")
    private String userID;

    /**
     * 客户ID
     */
    @TableField("customID")
    private String customID;

    /**
     * 公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 启用期间
     */
    private Date period;

    /**
     * 会计准则(3:新会计准则,1小企业会计准则2政府民间非营利组织4村集体会计准则)
     */
    private Integer accstandards;

    /**
     * 科目方案(会计准则)
     */
    private String calculate;

    /**
     * 修改人ID
     */
    @TableField("updatepsnID")
    private String updatepsnID;

    /**
     * 修改人
     */
    private String updatepsn;

    /**
     * 修改时间
     */
    private Date updatedate;

    /**
     * 创建人ID
     */
    @TableField("createpsnID")
    private String createpsnID;

    /**
     * 创建人
     */
    private String createpsn;

    /**
     * 创建时间
     */
    @TableField("createDate")
    private Date createDate;

    /**
     * 备注
     */
    private String des;

    /**
     * 最后一次使用时间[当前登陆用户默认使用的账套设置为最后一次使用的账套，如若是第一次登陆，随机指定一个账套]
     */
    @TableField("lastTime")
    private Date lastTime;

    /**
     * 记录账套最后使用期间
     */
    @TableField("useLastPeriod")
    private String useLastPeriod;

    /**
     * 企业性质(1：生产型2：贸易型3：服务型)
     */
    @TableField("companyType")
    private Integer companyType;

    /**
     * 国税密码
     */
    private String gsmm;

    /**
     * 统一信用代码
     */
    private String tyxydm;

    /**
     * 记账本位币(1:人民币)
     */
    private Integer jzbwb;

    /**
     * 账套状态（0:新生成1:启用2:禁用）
     */
    private Integer statu;

    /**
     * 公司纳税人类别 ：一般纳税人（传0）   小规模（传1）
     */
    @TableField("ssType")
    private Integer ssType;

    /**
     * 初始化状态(0没有初始化，1已经初始化)
     */
    private Integer initialStates;

    /**
     * 账套类型  暂时未启用
     */
    private String type;

    /**
     * 来源  来自那一个管理员
     */
    private String source;

    @TableField("chgStatuTime")
    private Date chgStatuTime;

    /**
     * 企业名称首字母拼接
     */
    @TableField("companyNamePinYin")
    private String companyNamePinYin;

    /**
     * 映射状态（0.未映射 1.已映射）
     */
    private Integer mappingStates;

    /**
     * 科目级别 默认4级
     */
    private Integer level;

    /**
     * 科目规则(4-3-3-3 默认)
     */
    private String rule;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getCustomID() {
        return customID;
    }

    public void setCustomID(String customID) {
        this.customID = customID;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }
    public Integer getAccstandards() {
        return accstandards;
    }

    public void setAccstandards(Integer accstandards) {
        this.accstandards = accstandards;
    }
    public String getCalculate() {
        return calculate;
    }

    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }
    public String getUpdatepsnID() {
        return updatepsnID;
    }

    public void setUpdatepsnID(String updatepsnID) {
        this.updatepsnID = updatepsnID;
    }
    public String getUpdatepsn() {
        return updatepsn;
    }

    public void setUpdatepsn(String updatepsn) {
        this.updatepsn = updatepsn;
    }
    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
    public String getCreatepsnID() {
        return createpsnID;
    }

    public void setCreatepsnID(String createpsnID) {
        this.createpsnID = createpsnID;
    }
    public String getCreatepsn() {
        return createpsn;
    }

    public void setCreatepsn(String createpsn) {
        this.createpsn = createpsn;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
    public String getUseLastPeriod() {
        return useLastPeriod;
    }

    public void setUseLastPeriod(String useLastPeriod) {
        this.useLastPeriod = useLastPeriod;
    }
    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }
    public String getGsmm() {
        return gsmm;
    }

    public void setGsmm(String gsmm) {
        this.gsmm = gsmm;
    }
    public String getTyxydm() {
        return tyxydm;
    }

    public void setTyxydm(String tyxydm) {
        this.tyxydm = tyxydm;
    }
    public Integer getJzbwb() {
        return jzbwb;
    }

    public void setJzbwb(Integer jzbwb) {
        this.jzbwb = jzbwb;
    }
    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }
    public Integer getSsType() {
        return ssType;
    }

    public void setSsType(Integer ssType) {
        this.ssType = ssType;
    }
    public Integer getInitialStates() {
        return initialStates;
    }

    public void setInitialStates(Integer initialStates) {
        this.initialStates = initialStates;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public Date getChgStatuTime() {
        return chgStatuTime;
    }

    public void setChgStatuTime(Date chgStatuTime) {
        this.chgStatuTime = chgStatuTime;
    }
    public String getCompanyNamePinYin() {
        return companyNamePinYin;
    }

    public void setCompanyNamePinYin(String companyNamePinYin) {
        this.companyNamePinYin = companyNamePinYin;
    }
    public Integer getMappingStates() {
        return mappingStates;
    }

    public void setMappingStates(Integer mappingStates) {
        this.mappingStates = mappingStates;
    }
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "Account{" +
        "accountID=" + accountID +
        ", userID=" + userID +
        ", customID=" + customID +
        ", companyName=" + companyName +
        ", period=" + period +
        ", accstandards=" + accstandards +
        ", calculate=" + calculate +
        ", updatepsnID=" + updatepsnID +
        ", updatepsn=" + updatepsn +
        ", updatedate=" + updatedate +
        ", createpsnID=" + createpsnID +
        ", createpsn=" + createpsn +
        ", createDate=" + createDate +
        ", des=" + des +
        ", lastTime=" + lastTime +
        ", useLastPeriod=" + useLastPeriod +
        ", companyType=" + companyType +
        ", gsmm=" + gsmm +
        ", tyxydm=" + tyxydm +
        ", jzbwb=" + jzbwb +
        ", statu=" + statu +
        ", ssType=" + ssType +
        ", initialStates=" + initialStates +
        ", type=" + type +
        ", source=" + source +
        ", chgStatuTime=" + chgStatuTime +
        ", companyNamePinYin=" + companyNamePinYin +
        ", mappingStates=" + mappingStates +
        ", level=" + level +
        ", rule=" + rule +
        "}";
    }
}
