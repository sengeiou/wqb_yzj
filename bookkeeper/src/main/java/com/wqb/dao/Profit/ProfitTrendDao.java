package com.wqb.dao.Profit;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.ProfitTrendVo;
import com.wqb.model.StatusPeriod;

import java.util.List;
import java.util.Map;

public interface ProfitTrendDao {

    //查询利润趋势图
    List<ProfitTrendVo> queryProfitTrend(Map<String, Object> param) throws BusinessException;

    //查询账套状态
    List<StatusPeriod> queryAccountStaus(Map<String, Object> param) throws BusinessException;

    //查询账套启用期间
    Account queryAccount(Map<String, Object> param) throws BusinessException;

    //查询利润分析
    ProfitTrendVo queryprofitAnalyze(Map<String, Object> param) throws BusinessException;


}
