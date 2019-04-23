package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.SzrcbBankBillDao;
import com.wqb.model.bank.SzrcbBank;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("szrcbBankBillDao")
public class SzrcbBankBillDaoImpl implements SzrcbBankBillDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(SzrcbBankBillDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void insertSzrcb(SzrcbBank szrcbBank) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("szrcb.insertSzrcb", szrcbBank);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SzrcbBank> querySame(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SzrcbBank> list = null;
        try {
            list = sqlSession.selectList("szrcb.querySame", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void deleteByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            sqlSession.delete("szrcb.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SzrcbBank> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SzrcbBank> list = null;
        try {
            list = sqlSession.selectList("szrcb.queryBankBill", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<SzrcbBank> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            List<SzrcbBank> list = sqlSession.selectList("szrcb.queryAll", param);
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

            sqlSession.update("szrcb.delVouchID", vouchID);
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

            sqlSession.update("szrcb.addVouchID", param);
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

            sqlSession.update("szrcb.updByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
}
