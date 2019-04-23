package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.BcmDao;
import com.wqb.model.bank.BcmBank;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("bcmDao")
public class BcmDaoImpl implements BcmDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(BcmDaoImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void insertBcm(BcmBank bcmBank) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.insert("bcm.insertBcm", bcmBank);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.delete("bcm.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<BcmBank> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<BcmBank> list = null;
        try {
            list = sqlSession.selectList("bcm.queryBankBill", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<BcmBank> querySame(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<BcmBank> list = null;
        try {
            list = sqlSession.selectList("bcm.querySame", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<BcmBank> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<BcmBank> list = sqlSession.selectList("bcm.queryAll", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public void delVouchID(String vouchID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.update("bcm.delVouchID", vouchID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void addVouchID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.update("bcm.addVouchID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.update("bcm.updByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
}
