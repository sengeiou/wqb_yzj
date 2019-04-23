package com.wqb.service.ProfitTrend;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface ProfitTrendService {
    //查询利润趋势图
    Map<String, Map<String, Object>> queryProfitTrend(Map<String, Object> parem) throws BusinessException;

    //查询利润分析
    Map<String, Object> queryprofitAnalyze(Map<String, Object> param) throws BusinessException;

}
