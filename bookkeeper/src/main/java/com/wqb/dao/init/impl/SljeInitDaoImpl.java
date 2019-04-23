package com.wqb.dao.init.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.init.SljeInitDao;
import com.wqb.model.SljeInit;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("sljeInitDao")
public class SljeInitDaoImpl implements SljeInitDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(SljeInitDaoImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void insertSljeInit(SljeInit sljeInit) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("sljeInit.insertSljeInit", sljeInit);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SljeInit> querySlje(String accountID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SljeInit> list = null;
        try {
            list = sqlSession.selectList("sljeInit.querySlje", accountID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<SljeInit> querySljeBySubNumber(String accountID, String subNumber) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SljeInit> list = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", accountID);
            param.put("subNumber", subNumber);
            list = sqlSession.selectList("sljeInit.querySljeBySubNumber", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }
}
