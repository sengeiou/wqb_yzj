package com.wqb.dao.firstPage.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.firstPage.FirstPageDao;
import com.wqb.model.FirstPageEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("firstPageDao")
public class FirstPageImpl implements FirstPageDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(FirstPageImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public List<FirstPageEntity> queryFirstPage(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<FirstPageEntity> list = null;
        try {
            list = sqlSession.selectList("first.queryFirstPage", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }
}
