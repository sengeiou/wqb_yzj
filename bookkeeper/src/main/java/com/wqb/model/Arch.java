package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Arch 实体类
 * 2018-01-04 lch
 */
public class Arch implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1199510669606734359L;
    private String archID;    //主键
    private String acperiod;    //期间（做账日期）
    private String archDate;    //工资发放月份
    private String accountID;    //账套ID
    private String acDepartment;    //所属部门
    private String acCode;    //人员编码
    private String acName;    //人员姓名
    private Double basePay;    //基本工资
    private Double subsidy;    //岗位津贴
    private Double overtimeFree;    //平时加班
    private Double overtimeWeekend;    //周末加班
    private Double otherFree;    //其他补贴
    private Double payAble;    //应发工资
    private Double socialfree;    //扣社保
    private Double taxFree;    //个税
    private Double totalCharged;    //扣款合计
    private Double provident;    //扣公积金
    private Double utilities;    //扣水电费
    private Double deduction;    //其它扣款
    private Double attendanceDays;    //考勤天数
    private Double attendanceActual;    //实际出勤
    private Double realwages;    //实发工资
    private Date importDate;    //导入时间
    private String createpsnID;    //操作人ID

    public void setArchID(String archID) {
        this.archID = archID;
    }

    public String getArchID() {
        return archID;
    }

    public void setAcperiod(String acperiod) {
        this.acperiod = acperiod;
    }

    public String getAcperiod() {
        return acperiod;
    }

    public void setArchDate(String archDate) {
        this.archDate = archDate;
    }

    public String getArchDate() {
        return archDate;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAcDepartment(String acDepartment) {
        this.acDepartment = acDepartment;
    }

    public String getAcDepartment() {
        return acDepartment;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcName() {
        return acName;
    }

    public void setBasePay(Double basePay) {
        this.basePay = basePay;
    }

    public Double getBasePay() {
        return basePay;
    }

    public void setSubsidy(Double subsidy) {
        this.subsidy = subsidy;
    }

    public Double getSubsidy() {
        return subsidy;
    }

    public void setOvertimeFree(Double overtimeFree) {
        this.overtimeFree = overtimeFree;
    }

    public Double getOvertimeFree() {
        return overtimeFree;
    }

    public void setOvertimeWeekend(Double overtimeWeekend) {
        this.overtimeWeekend = overtimeWeekend;
    }

    public Double getOvertimeWeekend() {
        return overtimeWeekend;
    }

    public void setOtherFree(Double otherFree) {
        this.otherFree = otherFree;
    }

    public Double getOtherFree() {
        return otherFree;
    }

    public void setPayAble(Double payAble) {
        this.payAble = payAble;
    }

    public Double getPayAble() {
        return payAble;
    }

    public void setSocialfree(Double socialfree) {
        this.socialfree = socialfree;
    }

    public Double getSocialfree() {
        return socialfree;
    }

    public void setTaxFree(Double taxFree) {
        this.taxFree = taxFree;
    }

    public Double getTaxFree() {
        return taxFree;
    }

    public void setTotalCharged(Double totalCharged) {
        this.totalCharged = totalCharged;
    }

    public Double getTotalCharged() {
        return totalCharged;
    }

    public void setProvident(Double provident) {
        this.provident = provident;
    }

    public Double getProvident() {
        return provident;
    }

    public void setUtilities(Double utilities) {
        this.utilities = utilities;
    }

    public Double getUtilities() {
        return utilities;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    public Double getDeduction() {
        return deduction;
    }

    public void setAttendanceDays(Double attendanceDays) {
        this.attendanceDays = attendanceDays;
    }

    public Double getAttendanceDays() {
        return attendanceDays;
    }

    public void setAttendanceActual(Double attendanceActual) {
        this.attendanceActual = attendanceActual;
    }

    public Double getAttendanceActual() {
        return attendanceActual;
    }

    public void setRealwages(Double realwages) {
        this.realwages = realwages;
    }

    public Double getRealwages() {
        return realwages;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setCreatepsnID(String createpsnID) {
        this.createpsnID = createpsnID;
    }

    public String getCreatepsnID() {
        return createpsnID;
    }

    @Override
    public String toString() {
        return "Arch [archID=" + archID + ", acperiod=" + acperiod + ", archDate=" + archDate + ", accountID="
                + accountID + ", acDepartment=" + acDepartment + ", acCode=" + acCode + ", acName=" + acName
                + ", basePay=" + basePay + ", subsidy=" + subsidy + ", overtimeFree=" + overtimeFree
                + ", overtimeWeekend=" + overtimeWeekend + ", otherFree=" + otherFree + ", payAble=" + payAble
                + ", socialfree=" + socialfree + ", taxFree=" + taxFree + ", totalCharged=" + totalCharged
                + ", provident=" + provident + ", utilities=" + utilities + ", deduction=" + deduction
                + ", attendanceDays=" + attendanceDays + ", attendanceActual=" + attendanceActual + ", realwages="
                + realwages + ", importDate=" + importDate + ", createpsnID=" + createpsnID + "]";
    }


}

