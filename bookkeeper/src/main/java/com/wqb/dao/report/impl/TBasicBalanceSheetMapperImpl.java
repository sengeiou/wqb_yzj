package com.wqb.dao.report.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.report.TBasicBalanceSheetMapper;
import com.wqb.model.TBasicBalanceSheet;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("tBasicBalanceSheetMapper")
public class TBasicBalanceSheetMapperImpl implements TBasicBalanceSheetMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicBalanceSheetMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int addBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet) {
        SqlSession sqlSession = null;
        int no = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.insert("balanceSheet.addBalanceSheet", tBasicBalanceSheet);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public List<TBasicBalanceSheet> queryBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet) {
        SqlSession sqlSession = null;
        List<TBasicBalanceSheet> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("balanceSheet.queryBalanceSheet", tBasicBalanceSheet);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public Map<String, String> queryBalanceSheetMap(TBasicBalanceSheet tBasicBalanceSheet) {
        SqlSession sqlSession = null;
        Map<String, String> balanceSheetMap = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            balanceSheetMap =
                    sqlSession.selectMap("balanceSheet.queryBalanceSheetMap", tBasicBalanceSheet, "pkBalanceSheet");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return balanceSheetMap;
    }

    @Override
    public int deleteBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet) {
        SqlSession sqlSession = null;
        int no = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            no = sqlSession.delete("balanceSheet.deleteBalanceSheet", tBasicBalanceSheet);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public List<TBasicBalanceSheet> queryBalanceSheet2(TBasicBalanceSheet tBasicBalanceSheet) {
        SqlSession sqlSession = null;
        List<TBasicBalanceSheet> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("balanceSheet.queryBalanceSheet2", tBasicBalanceSheet);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

}
