package com.wqb.service.log.service.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.log.login.LoginLogDao;
import com.wqb.service.log.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
@Service("loginLogService")
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    LoginLogDao loginLogDao;

    public void addLoginLog(Map<String, Object> param) throws BusinessException {
        loginLogDao.addLoginLog(param);
    }
}
