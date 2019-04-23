package com.wqb.dao.subject.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.subject.SubjectDao;
import com.wqb.model.SubExcel;
import com.wqb.model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("SubjectDao")
public class SubjectDaoImpl implements SubjectDao {
    @Autowired
    SqlSessionFactory SqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(SubjectDaoImpl.class);

    // 更新科目贷方金额
    @Override
    public Integer udpSubDAmt(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        Integer count = 0;
        try {
            count = sqlSession.update("subject.udpSubDAmt", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    // 更新科目贷方金额
    @Override
    public Integer udpSubCAmt(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        Integer count = 0;
        try {
            count = sqlSession.update("subject.udpSubCAmt", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    // 获取凭证下拉科目
    @Override
    public List<Subject> queryVouSubject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        List<Subject> list = null;
        try {
            list = sqlSession.selectList("subject.queryVouSubject", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 获取指定用户名下所有科目信息
    @Override
    public List<Subject> querySysSub(String userID, String accountID) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        List<Subject> list = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userID", userID);
            param.put("accountID", accountID);
            list = sqlSession.selectList("subject.querySysSub", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 获取导入的EXCEl所有科目
    @Override
    public List<SubExcel> queryExcelSub(String userID, String accountID) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        List<SubExcel> list = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userID", userID);
            param.put("accountID", accountID);
            list = sqlSession.selectList("subject.queryExcelSub", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<Subject> querySubNumber(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        List<Subject> list = null;
        try {
            list = sqlSession.selectList("subject.querySubNumber", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<Subject> selectMax7Sub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = SqlSessionFactory.openSession();
        List<Subject> list = null;
        try {
            list = sqlSession.selectList("subject.selectMax7Sub", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }
}
