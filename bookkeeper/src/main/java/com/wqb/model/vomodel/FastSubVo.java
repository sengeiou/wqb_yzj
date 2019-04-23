package com.wqb.model.vomodel;

import java.io.Serializable;

//快速查询科目
public class FastSubVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9214077941088479400L;

    private String sub_code;

    private String sub_name;

    private String superior_coding; //上级编码

    private Integer code_level;

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }


    public String getSuperior_coding() {
        return superior_coding;
    }

    public void setSuperior_coding(String superior_coding) {
        this.superior_coding = superior_coding;
    }

    public Integer getCode_level() {
        return code_level;
    }

    public void setCode_level(Integer code_level) {
        this.code_level = code_level;
    }

    @Override
    public String toString() {
        return "FastSubVo [sub_code=" + sub_code + ", sub_name=" + sub_name + ", superior_coding=" + superior_coding
                + ", code_level=" + code_level + "]";
    }


}
