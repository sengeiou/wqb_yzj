package com.wqb.dao.periodStatus;

import com.wqb.common.BusinessException;
import com.wqb.model.StatusPeriod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component

public interface PeriodStatusDao {

    List<StatusPeriod> queryStatus(Map<String, Object> param) throws BusinessException;

    void updStatu1(Map<String, Object> param) throws BusinessException;

    void updStatu2(Map<String, Object> param) throws BusinessException;

    void updStatu3(Map<String, Object> param) throws BusinessException;

    void insertPeriodStatu(StatusPeriod statusPeriod) throws BusinessException;

    void updStatuJz(Map<String, Object> param) throws BusinessException;

    void updStatu4(Map<String, Object> param) throws BusinessException;

    void updStatu5(Map<String, Object> param) throws BusinessException;

    int updStatuIsDetection(Map<String, Object> param) throws BusinessException;

    int updStatuByCondition(Map<String, Object> param) throws BusinessException;

    List<StatusPeriod> queryPeriodStatuBath(Map<String, Object> param) throws BusinessException;

    void updstatusJt(Map<String, Object> param) throws BusinessException;


    int queryJz(Map<String, Object> param) throws BusinessException;

    List<StatusPeriod> queryStatusByCondition(Map<String, Object> param) throws BusinessException;


}
