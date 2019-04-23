package com.wqb.model.vomodel;

import com.wqb.model.Account;
import com.wqb.model.User;

import java.io.Serializable;

public class UserLoingVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2730207212429110073L;
    private User user;
    private Account account;
    private String busDate;
    private String ip;

    public UserLoingVo(User user, Account account, String busDate, String ip) {
        super();
        this.user = user;
        this.account = account;
        this.busDate = busDate;
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getBusDate() {
        return busDate;
    }

    public void setBusDate(String busDate) {
        this.busDate = busDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "UserLoingVo [user=" + user + ", account=" + account + ", busDate=" + busDate + ", ip=" + ip + "]";
    }


}
