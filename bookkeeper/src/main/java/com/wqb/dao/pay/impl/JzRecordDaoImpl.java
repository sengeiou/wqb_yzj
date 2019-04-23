package com.wqb.dao.pay.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.pay.JzRecordDao;
import com.wqb.model.JzRecord;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("jzRecordDao")
public class JzRecordDaoImpl implements JzRecordDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(JzRecordDaoImpl.class);

    @Override
    public List<JzRecord> queryJzRecord(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<JzRecord> list = null;
        try {
            list = sqlSession.selectList("jzRecord.queryJzRecord", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;

    }

    @Override
    public void addJzRecord(JzRecord jzRecord) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("jzRecord.addJzRecord", jzRecord);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<JzRecord> queryOldJz(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<JzRecord> list = null;
        try {
            list = sqlSession.selectList("jzRecord.queryOldJz", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

}
