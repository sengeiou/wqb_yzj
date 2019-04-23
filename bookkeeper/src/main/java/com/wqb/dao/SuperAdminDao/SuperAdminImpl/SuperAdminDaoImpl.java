package com.wqb.dao.SuperAdminDao.SuperAdminImpl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.SuperAdminDao.SuperAdminDao;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 超级管理员页面扇形图做账统计数量 开发时间：2018-07-21
 *
 * @author tangsheng
 */
@Component
@Service("superAdminDao")
public class SuperAdminDaoImpl implements SuperAdminDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(SuperAdminDaoImpl.class);

    @Override
    public int getQueryCountQuantity0(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int maxNo = 0;
        try {
            maxNo = sqlSession.selectOne("superAdmin.getQueryCountQuantity0", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return maxNo;
    }

    // 通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int maxNo = 0;
        try {
            maxNo = sqlSession.selectOne("superAdmin.getQueryCountQuantity1", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return maxNo;
    }

    // 通过账套ID和做账期间以及条件：已完成结账--1 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int maxNo = 0;
        try {
            maxNo = sqlSession.selectOne("superAdmin.getQueryCountQuantity2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return maxNo;
    }

    // 通过账套ID和做账期间以及条件：生成凭证-1但是没有结账--0 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity3(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int maxNo = 0;
        try {
            maxNo = sqlSession.selectOne("superAdmin.getQueryCountQuantity3", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return maxNo;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public List<Account> queryStatusCustomer0(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("superAdmin.queryStatusCustomer0", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public List<StatusPeriod> queryStatusCustomer1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatusPeriod> list = null;
        try {
            list = sqlSession.selectList("superAdmin.queryStatusCustomer1", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public List<StatusPeriod> queryStatusCustomer2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatusPeriod> list = null;
        try {
            list = sqlSession.selectList("superAdmin.queryStatusCustomer2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public StatusPeriod queryStatusCustomer4(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        StatusPeriod list = null;
        try {
            list = sqlSession.selectOne("superAdmin.queryStatusCustomer4", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public int queryStatusCustomer5(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int list = 0;
        try {
            list = sqlSession.selectOne("superAdmin.queryStatusCustomer5", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public List<Account> queryStatusCustomer3(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("superAdmin.queryStatusCustomer3", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public Object getPtActiveCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("superAdmin.getPtActiveCount", param);
            return obj;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getPtDzAccZz(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getPtDzAccZz", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getPtJzAccZz(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getPtJzAccZz", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getByXzQy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getByXzQy", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getByTyQy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getByTyQy", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getPtTotalAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getPtTotalAcc", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getPtTotalTyAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getPtTotalTyAcc", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getByXzDlCom(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getByXzDlCom", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getDlCom(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getDlCom", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getTotalQyByTime(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object list = sqlSession.selectOne("superAdmin.getTotalQyByTime", param);
            return list;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }
}
