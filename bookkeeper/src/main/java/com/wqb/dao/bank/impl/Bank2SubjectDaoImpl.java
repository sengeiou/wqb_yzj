package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.Bank2SubjectDao;
import com.wqb.model.Bank2Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("bank2SubjectDao")
public class Bank2SubjectDaoImpl implements Bank2SubjectDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(Bank2SubjectDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Bank2Subject> queryBank2Subject(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Bank2Subject> list = null;
        try {
            list = sqlSession.selectList("bank2Subject.queryBank2Subject", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public int addBank2Subject(Bank2Subject b2s) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int count = 0;
        try {
            count = sqlSession.insert("bank2Subject.addBank2Subject", b2s);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public int updBank2Subject(Bank2Subject b2s) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int count = 0;
        try {
            count = sqlSession.update("bank2Subject.updBank2Subject", b2s);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;

    }

    @Override
    public List<Bank2Subject> queryBank2SubjectByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Bank2Subject> list = null;
        try {
            list = sqlSession.selectList("bank2Subject.queryBank2SubjectByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<Bank2Subject> queryByBankAccount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Bank2Subject> list = null;
        try {
            list = sqlSession.selectList("bank2Subject.queryByBankAccount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }
}
