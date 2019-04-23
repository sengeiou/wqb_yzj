package com.wqb.service.log.service;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface LoginLogService {
    void addLoginLog(Map<String, Object> param) throws BusinessException;
}
