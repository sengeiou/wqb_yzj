package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.PaBankDao;
import com.wqb.model.bank.PaBank;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("paBankDao")
public class PaBankDaoImpl implements PaBankDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(PaBankDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    // 平安银行数据导入
    public void insertPaBank(PaBank paBank) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("paBank.insertPaBank", paBank);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // 检查是否含有重复数据导入
    @Override
    public List<PaBank> querySame(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<PaBank> list = sqlSession.selectList("paBank.querySame", param);
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
    public void deleteByID(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("paBank.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // 平安分页查询
    @Override
    public List<PaBank> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<PaBank> list = sqlSession.selectList("paBank.queryBankBill", param);
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
    public List<PaBank> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<PaBank> list = sqlSession.selectList("paBank.queryAll", param);
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
            sqlSession.update("paBank.delVouchID", vouchID);
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
            sqlSession.update("paBank.addVouchID", param);
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
            sqlSession.update("paBank.updByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
