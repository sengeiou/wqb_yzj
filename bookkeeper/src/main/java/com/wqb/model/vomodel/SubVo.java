package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -304387410076722245L;
    //private String sub_id;
    private String sub_code;
    //private String sub_dir;
    //private String sub_name;
    private BigDecimal jf_amount;
    private BigDecimal df_amount;

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public BigDecimal getJf_amount() {
        return jf_amount;
    }

    public void setJf_amount(BigDecimal jf_amount) {
        this.jf_amount = jf_amount;
    }

    public BigDecimal getDf_amount() {
        return df_amount;
    }

    public void setDf_amount(BigDecimal df_amount) {
        this.df_amount = df_amount;
    }

    @Override
    public String toString() {
        return "SubVo [sub_code=" + sub_code + ", jf_amount=" + jf_amount + ", df_amount=" + df_amount + "]";
    }

}
