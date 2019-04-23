package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.NyBankDao;
import com.wqb.model.bank.NyBank;
import com.wqb.model.bank.NyBankNew;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("nyBankDao")
public class NyBankDaoImpl implements NyBankDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(NyBankDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    // 建设银行数据导入
    public void insertNyBank(NyBank nyBank) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("nyBank.insertNyBank", nyBank);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // 检查是否含有重复数据导入
    @Override
    public List<NyBank> querySame(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<NyBank> list = sqlSession.selectList("nyBank.querySame", param);
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
            sqlSession.delete("nyBank.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<NyBank> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<NyBank> list = null;
        try {
            list = sqlSession.selectList("nyBank.queryBankBill", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<NyBank> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<NyBank> list = sqlSession.selectList("nyBank.queryAll", param);
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
            sqlSession.update("nyBank.delVouchID", vouchID);
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
            sqlSession.update("nyBank.addVouchID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 查询是否有重复数据导入
    @Override
    public List<String> querytranID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> list = sqlSession.selectList("nyBank.querytranID", param);
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
    public void insertNyBankNew(NyBankNew nyBankNew) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("nyBankNew.insertNyBankNew", nyBankNew);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<NyBankNew> queryBankBillNew(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<NyBankNew> list = sqlSession.selectList("nyBankNew.queryBankBillNew", param);
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
    public void deleteNewByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("nyBankNew.deleteNewByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<NyBankNew> queryNewAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<NyBankNew> list = sqlSession.selectList("nyBankNew.queryNewAll", param);
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
    public void updByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("nyBank.updByID", param);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    public void updByIDNew(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("nyBankNew.updByIDNew", param);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
