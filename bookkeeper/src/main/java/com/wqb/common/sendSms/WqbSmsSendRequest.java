package com.wqb.common.sendSms;

/**
 * @author tianyh
 * @date 2017年4月15日 下午3:41:44
 * @Title: SmsSingleRequest
 * @ClassName: SmsSingleRequest
 * @Description:普通短信发送
 */
public class WqbSmsSendRequest {
    /**
     * 用户账号，必填
     */
    private String account;
    /**
     * 用户密码，必填
     */
    private String password;
    /**
     * 短信内容。长度不能超过536个字符，必填
     */
    private String msg;

    /**
     * 机号码。多个手机号码使用英文逗号分隔，必填
     */
    private String phone;


    /**
     * 定时发送短信时间。格式为yyyyMMddHHmm，值小于或等于当前时间则立即发送，默认立即发送，选填
     */
    private String sendtime;
    /**
     * 是否需要状态报告（默认false），选填
     */
    private String report;
    /**
     * 下发短信号码扩展码，纯数字，建议1-3位，选填
     */
    private String extend;
    /**
     * 该条短信在您业务系统内的ID，如订单号或者短信发送记录流水号，选填
     */
    private String uid;

    public WqbSmsSendRequest() {

    }

    public WqbSmsSendRequest(String msg, String phone) {
        super();
        this.account = "N2080702";
        this.password = "i97EbGlRs";
        this.msg = msg;
        this.phone = phone;
    }

    public WqbSmsSendRequest(String account, String password, String msg, String phone, String sendtime) {
        super();
        this.account = account;
        this.password = password;
        this.msg = msg;
        this.phone = phone;
        this.sendtime = sendtime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
