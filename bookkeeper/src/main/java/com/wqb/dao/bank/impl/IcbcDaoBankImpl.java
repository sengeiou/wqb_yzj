package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.IcbcBankDao;
import com.wqb.model.bank.IcbcBank;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("icbcBankDao")
public class IcbcDaoBankImpl implements IcbcBankDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(IcbcDaoBankImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void insertIcbc(IcbcBank icbcBank) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            sqlSession.insert("icbc.insertIcbc", icbcBank);
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

            sqlSession.delete("icbc.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<IcbcBank> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<IcbcBank> list = null;
        try {
            list = sqlSession.selectList("icbc.queryBankBill", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<IcbcBank> querySame(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<IcbcBank> list = null;
        try {
            list = sqlSession.selectList("icbc.querySame", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<IcbcBank> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            List<IcbcBank> list = sqlSession.selectList("icbc.queryAll", param);
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

            sqlSession.update("icbc.delVouchID", vouchID);
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
            sqlSession.update("icbc.addVouchID", param);
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
            sqlSession.update("icbc.updByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
}
