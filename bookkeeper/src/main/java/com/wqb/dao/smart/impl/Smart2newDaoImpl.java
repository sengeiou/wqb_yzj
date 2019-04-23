package com.wqb.dao.smart.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.smart.Smart2newDao;
import com.wqb.model.Smart2new;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service("smart2newDao")
public class Smart2newDaoImpl implements Smart2newDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(Smart2newDaoImpl.class);

    @Override
    public List<Smart2new> queryAllSmart2New() throws BusinessException {
        SqlSession sqlsession = null;
        List<Smart2new> list = null;
        try {
            sqlsession = sqlSessionFactory.openSession();
            list = sqlsession.selectList("queryAllSmart2New");
            return list;
        } catch (Exception e) {
            logger.error("查询小企业会计准则科目与新会计准则科目映射异常" + e);
            throw new BusinessException(e);
        } finally {
            sqlsession.close();
        }
    }
}
