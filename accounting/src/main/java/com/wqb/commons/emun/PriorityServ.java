package com.wqb.commons.emun;


/**
* @Author: Ben
* @Date: 2019-04-02
* @Description: 接口服务优先级
*/
public enum PriorityServ {

    GUO_PIAO(1, 3, "GuoPiaoService"),

    BAIDU(2, 3, "BaiduService");


    //优先级
    private Integer priority;

    //重试次数
    private Integer retryCount;

    private String beanName;

    PriorityServ(Integer priority, Integer retryCount,String beanName) {
        this.priority = priority;
        this.retryCount = retryCount;
        this.beanName = beanName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }}
