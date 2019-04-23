package com.wqb.commons.vo;

/**
 * Created by Administrator on 2017/2/23.
 */
public class JyqqcsVO {

    private String dealid; //交易id
    private String clientid; //服务id
    private String clientip; //应用ip
    private String serviceid; //服务id
    private String asynserviceid; //回调服务id
    private String bwlx; //报文类型
    private String jhms;
    private String body; //业务报文
    private String token;

    public String getDealid() {
        return dealid;
    }

    public void setDealid(String dealid) {
        this.dealid = dealid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getAsynserviceid() {
        return asynserviceid;
    }

    public void setAsynserviceid(String asynserviceid) {
        this.asynserviceid = asynserviceid;
    }

    public String getBwlx() {
        return bwlx;
    }

    public void setBwlx(String bwlx) {
        this.bwlx = bwlx;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getJhms() {
        return jhms;
    }

    public void setJhms(String jhms) {
        this.jhms = jhms;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
}
