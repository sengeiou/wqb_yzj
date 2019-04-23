package com.wqb.model;

import java.io.Serializable;

/**
 * 任务进度实体类
 *
 * @author zhushuyuan
 */
public class Progress implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4356721254921824600L;
    // 主键ID
    private String id;
    // 账套ID
    private String accountID;
    // 期间
    private String period;
    // 是否正在一键生成凭证
    private Integer cv;
    // 是否正在计提
    private Integer jt;
    // 是否正在反计提
    private Integer unJt;
    // 是否正在结转
    private Integer carryState;
    // 是否正在反结转
    private Integer unCarryState;
    // 是否正在结账
    private Integer jz;

    public Integer getUnJt() {
        return unJt;
    }

    public void setUnJt(Integer unJt) {
        this.unJt = unJt;
    }

    public Integer getUnCarryState() {
        return unCarryState;
    }

    public void setUnCarryState(Integer unCarryState) {
        this.unCarryState = unCarryState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getCv() {
        return cv;
    }

    public void setCv(Integer cv) {
        this.cv = cv;
    }

    public Integer getJt() {
        return jt;
    }

    public void setJt(Integer jt) {
        this.jt = jt;
    }

    public Integer getCarryState() {
        return carryState;
    }

    public void setCarryState(Integer carryState) {
        this.carryState = carryState;
    }

    public Integer getJz() {
        return jz;
    }

    public void setJz(Integer jz) {
        this.jz = jz;
    }

}
