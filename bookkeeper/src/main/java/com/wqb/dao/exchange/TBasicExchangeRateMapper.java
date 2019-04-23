package com.wqb.dao.exchange;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicExchangeRate;

import java.util.List;

public interface TBasicExchangeRateMapper {

    int insertExchangeRate(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    int queryExeRateByCuyAbbe(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    List<TBasicExchangeRate> queryExchangeRate(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    int deleteExchangeRateAll(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    int delExchangeByExchangeId(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    int updateExchangeRate(TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

}
