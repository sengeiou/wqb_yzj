package com.wqb.service.periodStatus;

import com.wqb.common.BusinessException;
import com.wqb.model.StatusPeriod;

import java.util.List;
import java.util.Map;

public interface PeriodStatusService {
    List<StatusPeriod> queryStatus(Map<String, Object> param) throws BusinessException;

    void insertPeriodStatu(StatusPeriod statusPeriod) throws BusinessException;

    String queryAccStatus(String accountID, String account_period) throws BusinessException;

    int updStatuByCondition(Map<String, Object> param) throws BusinessException;
}
