package com.wqb.dao.documents.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.documents.TBasicDocumentsMapper;
import com.wqb.model.TBasicDocuments;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Service("tBasicDocumentsMapper")
public class TBasicDocumentsMapperImpl implements TBasicDocumentsMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicDocumentsMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int deleteByPrimaryKey(String pkDocumentsId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(TBasicDocuments record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(TBasicDocuments record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TBasicDocuments selectByPrimaryKey(String pkDocumentsId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicDocuments record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(TBasicDocuments record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addTicketsCost(List<TBasicDocuments> tBasicDocumentslist) {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.insert("documents.addTicketsCost", tBasicDocumentslist);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public List<TBasicDocuments> queryDocumentsList(TBasicDocuments tBasicDocuments) {
        SqlSession sqlSession = null;
        List<TBasicDocuments> results = new ArrayList<TBasicDocuments>();
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.selectList("documents.queryDocumentsList", tBasicDocuments);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public List<TBasicDocuments> querySalesDocumentsList(TBasicDocuments tBasicDocuments) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TBasicDocuments> list = sqlSession.selectList("documents.queryDocumentsList", tBasicDocuments);
            if (list != null) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return Collections.emptyList();
    }

    @Override
    public int deleteDocumentsList(@Param("tBasicDocumentsList") List<TBasicDocuments> tBasicDocumentsList) {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.delete("documents.deleteDocumentsList", tBasicDocumentsList);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

}
