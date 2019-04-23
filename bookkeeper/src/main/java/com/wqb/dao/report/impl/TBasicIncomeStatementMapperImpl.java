package com.wqb.dao.report.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.report.TBasicIncomeStatementMapper;
import com.wqb.model.TBasicIncomeStatement;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class TBasicIncomeStatementMapperImpl implements TBasicIncomeStatementMapper {

    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicBalanceSheetMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int deleteByPrimaryKey(String pkIncomeStatementId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(TBasicIncomeStatement record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(TBasicIncomeStatement record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TBasicIncomeStatement selectByPrimaryKey(String pkIncomeStatementId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicIncomeStatement record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(TBasicIncomeStatement record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addIncomeStatement(TBasicIncomeStatement tBasicIncomeStatement) {

        SqlSession sqlSession = null;
        int no = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.insert("incomeStatement.addIncomeStatement", tBasicIncomeStatement);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public List<TBasicIncomeStatement> queryIncomeStatrment(TBasicIncomeStatement tBasicIncomeStatement) {
        SqlSession sqlSession = null;
        List<TBasicIncomeStatement> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("incomeStatement.queryIncomeStatrment", tBasicIncomeStatement);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public int deleteIncomeStatrment(TBasicIncomeStatement tBasicIncomeStatement) {
        SqlSession sqlSession = null;
        int no = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.insert("incomeStatement.deleteIncomeStatrment", tBasicIncomeStatement);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    //app接口
    public List<TBasicIncomeStatement> queryIncomeStatrment2(TBasicIncomeStatement tBasicIncomeStatement) {
        SqlSession sqlSession = null;
        List<TBasicIncomeStatement> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("incomeStatement.queryIncomeStatrment2", tBasicIncomeStatement);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

}
