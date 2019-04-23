package com.wqb.service.common;

import com.wqb.common.BusinessException;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Account;
import com.wqb.model.Progress;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.model.vomodel.UserLoingVo;
import com.wqb.service.log.service.LoginLogService;
import com.wqb.service.periodStatus.PeriodStatusService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class loingThread implements Runnable {

    Account account;
    User user;
    String busDate;
    String ip;
    ProgressDao progressDao;
    LoginLogService loginLogService;
    PeriodStatusService periodStatusService;
    UserLoingVo userLoingVo;

    public loingThread(UserLoingVo userVo) {
        this.userLoingVo = userVo;
        this.account = userVo.getAccount();
        this.user = userVo.getUser();
        this.busDate = userVo.getBusDate();
        this.ip = userVo.getIp();
        this.progressDao = SpringApplicationContextHolder.getBean("ProgressDao");
        this.loginLogService = SpringApplicationContextHolder.getBean("loginLogService");
        this.periodStatusService = SpringApplicationContextHolder.getBean("periodStatusService");
    }


    private void insertLog() throws BusinessException {
        Map<String, Object> param = new HashMap<>();
        String loginLogID = UUIDUtils.getUUID();
        param.put("userID", user.getUserID());
        param.put("loginLogID", loginLogID);
        param.put("loginUser", user.getLoginUser());
        param.put("loginTime", new Date());
        param.put("userName", user.getUserName());
        param.put("createDate", new Date());
        param.put("loginIP", ip);
        loginLogService.addLoginLog(param);
    }

    private void insertStatusPeriod() throws BusinessException {
        StatusPeriod statusPeriod = new StatusPeriod();
        statusPeriod.setPeriodID(UUIDUtils.getUUID());
        statusPeriod.setCreatePsn(user.getUserName());
        statusPeriod.setCreatePsnID(user.getUserID());
        statusPeriod.setIsCarryState(0);
        statusPeriod.setIsCreateVoucher(0);
        statusPeriod.setIsJz(0);
        statusPeriod.setIsJt(0);
        statusPeriod.setPeriod(busDate);
        statusPeriod.setAccountID(account.getAccountID());
        periodStatusService.insertPeriodStatu(statusPeriod);
    }

    private int insertProgress() throws BusinessException {
        Progress pro = new Progress();
        pro.setId(UUIDUtils.getUUID());
        pro.setAccountID(account.getAccountID());
        pro.setPeriod(busDate);
        pro.setCv(0);
        pro.setJt(0);
        pro.setUnJt(0);
        pro.setCarryState(0);
        pro.setUnCarryState(0);
        pro.setJz(0);
        Integer num = progressDao.addProgress(pro);
        return num;
    }

    private Map<String, Object> reMap() {
        Map<String, Object> pa = new HashMap<String, Object>();
        pa.put("accountID", account.getAccountID());
        pa.put("busDate", busDate);
        pa.put("period", busDate);
        return pa;
    }


    @Override
    public void run() {
        try {
            //添加日志
            try {
                insertLog();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (account != null) {
                Map<String, Object> pa = reMap();
                try {
                    List<StatusPeriod> list = periodStatusService.queryStatus(pa);
                    if (list == null || list.isEmpty()) {
                        insertStatusPeriod();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    List<Progress> proList = progressDao.queryProgress(pa);
                    if (proList == null || proList.isEmpty()) {
                        insertProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
