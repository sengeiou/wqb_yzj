package com.wqb.model;

import java.io.Serializable;

public class JzRecord implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3804605805352354103L;
    // 主键ID
    private String id;
    // 具体做账期间
    private String period;
    // 平台ID
    private String ptID;
    // 账务日期
    private String zwMonth;

    public String getZwMonth() {
        return zwMonth;
    }

    public void setZwMonth(String zwMonth) {
        this.zwMonth = zwMonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPtID() {
        return ptID;
    }

    public void setPtID(String ptID) {
        this.ptID = ptID;
    }

}
