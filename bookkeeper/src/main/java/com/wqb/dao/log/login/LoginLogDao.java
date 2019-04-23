package com.wqb.dao.log.login;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface LoginLogDao {
    void addLoginLog(Map<String, Object> param) throws BusinessException;
}
