package com.wqb.dao.periodStatus.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.model.StatusPeriod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service("periodStatusDao")
public class PeriodStatusDaoImpl implements PeriodStatusDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(PeriodStatusDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    // 根据期间查询做账状态
    @Override
    public List<StatusPeriod> queryStatus(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatusPeriod> list = new ArrayList<StatusPeriod>();
        try {
            // select * from t_status_period where period=#{busDate} and
            // accountID=#{accountID};
            list = sqlSession.selectList("periodStatu.queryStatus", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void updStatu1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("periodStatu.updStatu1", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updStatu2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("periodStatu.updStatu2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void insertPeriodStatu(StatusPeriod statusPeriod) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("periodStatu.insertPeriodStatu", statusPeriod);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updStatu3(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("periodStatu.updStatu3", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updStatuJz(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("periodStatu.updStatuJz", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updStatu4(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("periodStatu.updStatu4", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updStatu5(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("periodStatu.updStatu5", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int updStatuIsDetection(Map<String, Object> param) throws BusinessException {
        int no = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.update("periodStatu.updStatuIsDetection", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    // 更新删除状态
    @Override
    public int updStatuByCondition(Map<String, Object> param) throws BusinessException {
        int no = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.update("periodStatu.updStatuByCondition", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public List<StatusPeriod> queryPeriodStatuBath(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatusPeriod> list = new ArrayList<StatusPeriod>();
        try {
            list = sqlSession.selectList("periodStatu.queryPeriodStatuBath", param);
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void updstatusJt(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.update("periodStatu.updstatusJt", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * @param param
     * @throws BusinessException void    返回类型
     * @Title: queryJz
     * @Description: 查询已完成记账的数量
     * @date 2018年7月25日  下午8:49:58
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int queryJz(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = null;
        int selectOne = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectOne = sqlSession.selectOne("periodStatu.queryJz", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectOne;
    }

    @Override
    public List<StatusPeriod> queryStatusByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<StatusPeriod> list = sqlSession.selectList("periodStatu.queryStatusByCondition", param);
            if (list == null || list.get(0) == null) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }
}
