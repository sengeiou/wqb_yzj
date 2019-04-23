package com.wqb.service.user;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface UserService {
    Map<String, Object> getSuperComplementKpi(String period) throws BusinessException;
}
