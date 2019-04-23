package com.wqb.dao.admin;

import com.wqb.common.BusinessException;

import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: AdminDao
 * @Description: 管理员首页
 * @date 2018年7月25日 上午10:03:12
 */
public interface AdminDao {
    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: accountCount
     * @Description: 做帐统计
     * @date 2018年7月25日  上午10:12:50
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> accountCount(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: corpOverview
     * @Description: 企业总览
     * @date 2018年7月25日  上午10:13:28
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> corpOverview(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: acctList
     * @Description: 做帐列表
     * @date 2018年7月25日  上午10:13:05
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> acctList(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: staffCorpScale
     * @Description: 各财务人员负责企业比例图
     * @date 2018年7月25日  上午10:12:15
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> staffCorpScale(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: effectiveCorpTrend
     * @Description: 有效企业趋势图
     * @date 2018年7月25日  上午10:12:39
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> effectiveCorpTrend(Map<String, Object> param) throws BusinessException;
}
