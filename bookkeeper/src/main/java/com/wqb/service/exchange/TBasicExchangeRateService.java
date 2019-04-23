package com.wqb.service.exchange;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicExchangeRate;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TBasicExchangeRateService {

    Map<String, Object> deleteByPrimaryKey(Map<String, Object> param, HttpSession session) throws BusinessException;

    List<TBasicExchangeRate> queryExchangeRate(HttpSession session) throws BusinessException;

    /**
     * @param tBasicExchangeRate
     * @param session
     * @return
     * @throws BusinessException List<TBasicExchangeRate>    返回类型
     * @Title: queryExeRateByCuyAbbe
     * @Description: 初始化自动添加币别
     * @date 2017年12月27日  下午5:23:22
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicExchangeRate> queryExeRateByCuyAbbe(String typeOfCurrency, HttpSession session) throws BusinessException;

    Map<String, Object> insertExchangeRate(TBasicExchangeRate tBasicExchangeRate, HttpSession session) throws BusinessException;

    Map<String, Object> deleteExchangeRateAll(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @param pkExchangeRateId
     * @return Map<String, Object>    返回类型
     * @Title: delExchangeByExchangeId
     * @Description: 根据 汇率主键 删除汇率
     * @date 2018年1月22日  下午4:13:03
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> delExchangeByExchangeId(HttpSession session, String pkExchangeRateId) throws BusinessException;

    /**
     * @param tBasicExchangeRate
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: updateExchangeByStr
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2018年1月22日  下午6:15:56
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> updateExchangeByStr(TBasicExchangeRate tBasicExchangeRate, HttpSession session) throws BusinessException;

    Map<String, Object> updateExchangeRate(HttpSession session, TBasicExchangeRate tBasicExchangeRate) throws BusinessException;

    ;
}
