package com.wqb.dao.exchange.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.exchange.TBasicExchangeRateMapper;
import com.wqb.model.TBasicExchangeRate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service("tBasicExchangeRateMapper")
public class TBasicExchangeRateMapperImpl implements TBasicExchangeRateMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicExchangeRateMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int insertExchangeRate(TBasicExchangeRate tBasicExchangeRate) {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.insert("exchangeRate.insertExchangeRate", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public int queryExeRateByCuyAbbe(TBasicExchangeRate tBasicExchangeRate) {
        int results = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.selectOne("exchangeRate.queryExeRateByCuyAbbe", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public List<TBasicExchangeRate> queryExchangeRate(TBasicExchangeRate tBasicExchangeRate) {
        List<TBasicExchangeRate> results = new ArrayList<TBasicExchangeRate>();
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.selectList("exchangeRate.queryExchangeRate", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public int deleteExchangeRateAll(TBasicExchangeRate tBasicExchangeRate) throws BusinessException {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.delete("exchangeRate.deleteExchangeRateAll", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public int delExchangeByExchangeId(TBasicExchangeRate tBasicExchangeRate) throws BusinessException {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.delete("exchangeRate.delExchangeByExchangeId", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public int updateExchangeRate(TBasicExchangeRate tBasicExchangeRate) {
        SqlSession sqlSession = null;
        int result = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            result = sqlSession.delete("exchangeRate.updateExchangeRate", tBasicExchangeRate);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }
}
