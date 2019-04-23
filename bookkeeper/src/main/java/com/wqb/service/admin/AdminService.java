package com.wqb.service.admin;

import com.wqb.common.BusinessException;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: AdminService
 * @Description: 管理员首页
 * @date 2018年7月25日 上午9:33:03
 */
public interface AdminService {

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: accountCount
     * @Description: 做帐统计
     * @date 2018年7月25日  上午9:30:27
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> accountCount(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: corpOverview
     * @Description: 企业总览
     * @date 2018年7月25日  上午9:30:52
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> corpOverview(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: acctList
     * @Description: 做帐列表
     * @date 2018年7月25日  上午9:30:46
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> acctList(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: staffCorpScale
     * @Description: 各财务人员负责企业比例图
     * @date 2018年7月25日  上午9:30:38
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> staffCorpScale(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: effectiveCorpTrend
     * @Description: 有效企业趋势图
     * @date 2018年7月25日  上午9:30:33
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> effectiveCorpTrend(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: effectiveCorpTrendNew
     * @Description: 有效企业趋势图(SQL查询)
     * @date 2018年8月1日  下午4:26:59
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> effectiveCorpTrendNew(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: staffCorpScaleNew
     * @Description: 各财务人员负责企业比例图(SQL查询)
     * @date 2018年8月1日  下午4:27:45
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> staffCorpScaleNew(HttpSession session) throws BusinessException;
}
