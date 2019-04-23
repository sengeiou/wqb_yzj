package com.wqb.dao.subject.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.subject.TBasicSubjectParentMapper;
import com.wqb.model.TBasicSubjectParent;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("tBasicSubjectParentMapper")
public class TBasicSubjectParentMapperImpl implements TBasicSubjectParentMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectParentMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    // 导入excel初始化科目
    @Override
    public void insertSubParent(TBasicSubjectParent subParent) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("subParent.insertSubParent", subParent);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<TBasicSubjectParent> querySubParent(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectParent> selectList = null;
        try {
            selectList = sqlSession.selectList("subParent.querySubParent", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    //查询科目方向
    @Override
    public String querySubCode(String str) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String code = null;
        try {
            code = sqlSession.selectOne("subParent.querySubCode", str);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return code;
    }
}
