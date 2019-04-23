package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Test01 测试用可删除 实体类
 * 2018-04-20 lch
 */

public class Test01 implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4939238032509505057L;
    private Integer id;    //主键
    private String username;    //
    private String sex;    //
    private Date birthday;    //
    private BigDecimal price;    //
    private Double num;    //

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Double getNum() {
        return num;
    }
}

