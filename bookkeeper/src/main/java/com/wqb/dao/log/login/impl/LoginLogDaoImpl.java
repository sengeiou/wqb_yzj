package com.wqb.dao.log.login.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.log.login.LoginLogDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
@Service("loginLogDao")
public class LoginLogDaoImpl implements LoginLogDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private static Logger logger = LoggerFactory.getLogger(LoginLogDaoImpl.class);

    @Override
    public void addLoginLog(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("loginLog.addLoginLog", param);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            sqlSession.close();
        }
    }
}
