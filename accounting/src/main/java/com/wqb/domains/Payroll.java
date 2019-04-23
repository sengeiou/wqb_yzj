package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人员薪资档案表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_wa_arch")
public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "archID", type = IdType.UUID)
    private String archID;

    /**
     * 期间（做账日期）
     */
    private String acperiod;

    /**
     * 工资发放月份
     */
    @TableField("archDate")
    private String archDate;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 所属部门
     */
    @TableField("acDepartment")
    private String acDepartment;

    /**
     * 人员编码
     */
    @TableField("acCode")
    private String acCode;

    /**
     * 人员姓名
     */
    @TableField("acName")
    private String acName;

    /**
     * 基本工资
     */
    @TableField("basePay")
    private Double basePay;

    /**
     * 岗位津贴
     */
    private Double subsidy;

    /**
     * 平时加班
     */
    @TableField("overtimeFree")
    private Double overtimeFree;

    /**
     * 周末加班
     */
    @TableField("overtimeWeekend")
    private Double overtimeWeekend;

    /**
     * 其他补贴
     */
    @TableField("otherFree")
    private Double otherFree;

    /**
     * 应发工资
     */
    @TableField("payAble")
    private Double payAble;

    /**
     * 扣社保
     */
    private Double socialfree;

    /**
     * 个税
     */
    @TableField("taxFree")
    private Double taxFree;

    /**
     * 扣款合计
     */
    @TableField("totalCharged")
    private Double totalCharged;

    /**
     * 扣公积金
     */
    private Double provident;

    /**
     * 扣水电费
     */
    private Double utilities;

    /**
     * 其它扣款
     */
    private Double deduction;

    /**
     * 考勤天数
     */
    @TableField("attendanceDays")
    private Double attendanceDays;

    /**
     * 实际出勤
     */
    @TableField("attendanceActual")
    private Double attendanceActual;

    /**
     * 实发工资
     */
    private Double realwages;

    /**
     * 导入时间
     */
    @TableField("importDate")
    private Date importDate;

    /**
     * 操作人ID
     */
    @TableField("createpsnID")
    private String createpsnID;

    public String getArchID() {
        return archID;
    }

    public void setArchID(String archID) {
        this.archID = archID;
    }
    public String getAcperiod() {
        return acperiod;
    }

    public void setAcperiod(String acperiod) {
        this.acperiod = acperiod;
    }
    public String getArchDate() {
        return archDate;
    }

    public void setArchDate(String archDate) {
        this.archDate = archDate;
    }
    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getAcDepartment() {
        return acDepartment;
    }

    public void setAcDepartment(String acDepartment) {
        this.acDepartment = acDepartment;
    }
    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }
    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }
    public Double getBasePay() {
        return basePay;
    }

    public void setBasePay(Double basePay) {
        this.basePay = basePay;
    }
    public Double getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(Double subsidy) {
        this.subsidy = subsidy;
    }
    public Double getOvertimeFree() {
        return overtimeFree;
    }

    public void setOvertimeFree(Double overtimeFree) {
        this.overtimeFree = overtimeFree;
    }
    public Double getOvertimeWeekend() {
        return overtimeWeekend;
    }

    public void setOvertimeWeekend(Double overtimeWeekend) {
        this.overtimeWeekend = overtimeWeekend;
    }
    public Double getOtherFree() {
        return otherFree;
    }

    public void setOtherFree(Double otherFree) {
        this.otherFree = otherFree;
    }
    public Double getPayAble() {
        return payAble;
    }

    public void setPayAble(Double payAble) {
        this.payAble = payAble;
    }
    public Double getSocialfree() {
        return socialfree;
    }

    public void setSocialfree(Double socialfree) {
        this.socialfree = socialfree;
    }
    public Double getTaxFree() {
        return taxFree;
    }

    public void setTaxFree(Double taxFree) {
        this.taxFree = taxFree;
    }
    public Double getTotalCharged() {
        return totalCharged;
    }

    public void setTotalCharged(Double totalCharged) {
        this.totalCharged = totalCharged;
    }
    public Double getProvident() {
        return provident;
    }

    public void setProvident(Double provident) {
        this.provident = provident;
    }
    public Double getUtilities() {
        return utilities;
    }

    public void setUtilities(Double utilities) {
        this.utilities = utilities;
    }
    public Double getDeduction() {
        return deduction;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }
    public Double getAttendanceDays() {
        return attendanceDays;
    }

    public void setAttendanceDays(Double attendanceDays) {
        this.attendanceDays = attendanceDays;
    }
    public Double getAttendanceActual() {
        return attendanceActual;
    }

    public void setAttendanceActual(Double attendanceActual) {
        this.attendanceActual = attendanceActual;
    }
    public Double getRealwages() {
        return realwages;
    }

    public void setRealwages(Double realwages) {
        this.realwages = realwages;
    }
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }
    public String getCreatepsnID() {
        return createpsnID;
    }

    public void setCreatepsnID(String createpsnID) {
        this.createpsnID = createpsnID;
    }

    @Override
    public String toString() {
        return "Payroll{" +
        "archID=" + archID +
        ", acperiod=" + acperiod +
        ", archDate=" + archDate +
        ", accountID=" + accountID +
        ", acDepartment=" + acDepartment +
        ", acCode=" + acCode +
        ", acName=" + acName +
        ", basePay=" + basePay +
        ", subsidy=" + subsidy +
        ", overtimeFree=" + overtimeFree +
        ", overtimeWeekend=" + overtimeWeekend +
        ", otherFree=" + otherFree +
        ", payAble=" + payAble +
        ", socialfree=" + socialfree +
        ", taxFree=" + taxFree +
        ", totalCharged=" + totalCharged +
        ", provident=" + provident +
        ", utilities=" + utilities +
        ", deduction=" + deduction +
        ", attendanceDays=" + attendanceDays +
        ", attendanceActual=" + attendanceActual +
        ", realwages=" + realwages +
        ", importDate=" + importDate +
        ", createpsnID=" + createpsnID +
        "}";
    }
}
