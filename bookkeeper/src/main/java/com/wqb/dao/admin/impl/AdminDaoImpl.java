package com.wqb.dao.admin.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.admin.AdminDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: AdminDaoImpl
 * @Description: 管理员首页
 * @date 2018年7月25日 上午10:22:07
 */
@Component
@Service("adminDao")
public class AdminDaoImpl implements AdminDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(AdminDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: accountCount
     * @Description: 做帐统计
     * @date 2018年7月25日  上午10:12:50
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> accountCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = sqlSession.selectOne("AdminDao.accountCount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: corpOverview
     * @Description: 企业总览
     * @date 2018年7月25日  上午10:13:28
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> corpOverview(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
//		int selectOne = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        try {

//			selectOne = sqlSession.selectOne("AdminDao.corpOverview", param);
//			result.put("selectOne", selectOne);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: acctList
     * @Description: 做帐列表
     * @date 2018年7月25日  上午10:13:05
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> acctList(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = sqlSession.selectOne("AdminDao.acctList", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: staffCorpScale
     * @Description: 各财务人员负责企业比例图
     * @date 2018年7月25日  上午10:12:15
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> staffCorpScale(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> result = new HashMap<String, Object>();
        List<Object> staffCorpList = new ArrayList<>();
        try {
            staffCorpList = sqlSession.selectList("admin.staffCorpScale", param);
            result.put("staffCorpList", staffCorpList);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: effectiveCorpTrend
     * @Description: 有效企业趋势图
     * @date 2018年7月25日  上午10:12:39
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> effectiveCorpTrend(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> result = new HashMap<String, Object>();
        int selectOne = 0;
        try {
            selectOne = sqlSession.selectOne("admin.effectiveCorpTrend", param);
            result.put("collect", selectOne);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }
}
