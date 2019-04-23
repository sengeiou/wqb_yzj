package com.wqb.dao.progress.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Progress;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("ProgressDao")
public class ProgressDaoImpl implements ProgressDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(ProgressDaoImpl.class);

    @Override
    public List<Progress> queryProgress(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Progress> list = sqlSession.selectList("progress.queryProgress", map);
            return list;
        } catch (Exception e) {
            logger.error("ProgressDaoImpl【queryProgress】获取用户进度记录异常!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Integer addProgress(Progress pro) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        int count = 0;
        try {
            count = sqlSession.insert("progress.addProgress", pro);
            return count;
        } catch (Exception e) {
            logger.error("ProgressDaoImpl【queryProgress】获取用户进度记录异常!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void chgProgress(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("progress.chgProgress", param);

        } catch (Exception e) {
            logger.error("ProgressDaoImpl【chgProgress】变更用户进度记录异常!", e);

        } finally {
            sqlSession.close();
        }

    }
}
