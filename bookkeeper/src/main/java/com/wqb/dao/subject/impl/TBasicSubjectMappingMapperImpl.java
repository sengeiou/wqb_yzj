package com.wqb.dao.subject.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.subject.TBasicSubjectMappingMapper;
import com.wqb.model.TBasicMeasure;
import com.wqb.model.TBasicSubjectMapping;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("TBasicSubjectMappingMapper")
public class TBasicSubjectMappingMapperImpl implements TBasicSubjectMappingMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int uploadSubMappingList(List<TBasicSubjectMapping> tBasicSubjectMappingList) {
        SqlSession sqlSesion = null;
        int results = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            results = sqlSesion.insert("subjectMapping.updateSubMappingList", tBasicSubjectMappingList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return results;
    }

    @Override
    public int deleteByAccountId(TBasicMeasure tBasicMeasure) {
        SqlSession sqlSesion = null;
        int results = 0;
        try {
//			sqlSesion = sqlSessionFactory.openSession();
//			results = sqlSesion.insert("subjectMapping.deleteByAccountId", tBasicMeasure);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			sqlSesion.close();
        }
        return results;
    }

    @Override
    public int deleteByPrimaryKey(Integer pkSubMappingId) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.delete("subjectMapping.deleteByPrimaryKey", pkSubMappingId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.deleteByPrimaryKey--delete错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int insert(TBasicSubjectMapping tBasicSubjectMapping) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMapping.insert", tBasicSubjectMapping);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.insert--insert错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int insertSelective(TBasicSubjectMapping tBasicSubjectMapping) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMapping.insertSelective", tBasicSubjectMapping);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.subjectMapping--insertSelective错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public TBasicSubjectMapping selectByPrimaryKey(Integer pkSubMappingId) {
        SqlSession sqlSesion = null;
        TBasicSubjectMapping tBasicSubjectMapping = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            Object selectOne = sqlSesion.selectOne("subjectMapping.selectByPrimaryKey", pkSubMappingId);
            tBasicSubjectMapping = (TBasicSubjectMapping) selectOne;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.selectByPrimaryKey--selectOne错误");
        } finally {
            sqlSesion.close();
        }
        return tBasicSubjectMapping;
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicSubjectMapping tBasicSubjectMapping) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.update("subjectMapping.updateByPrimaryKeySelective", tBasicSubjectMapping);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.subjectMapping--updateByPrimaryKeySelective错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int updateByPrimaryKey(TBasicSubjectMapping tBasicSubjectMapping) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.update("subjectMapping.updateByPrimaryKey", tBasicSubjectMapping);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.subjectMapping--updateByPrimaryKey错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int deleteAll() {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.delete("subjectMapping.deleteAll");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.deleteAll--deleteAll错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public List<TBasicSubjectMapping> querySubMappingList(Integer accountType) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMapping> selectList = new ArrayList<TBasicSubjectMapping>();
        try {
            sqlSesion = sqlSessionFactory.openSession();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountType", accountType);
            selectList = sqlSesion.selectList("subjectMapping.querySubMappingList", param);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMapping.querySubMappingList--selectList错误");
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @Override
    public int deleteMeasureList(List<TBasicSubjectMapping> tBasicSubMappingList) {
        SqlSession sqlSesion = null;
        int results = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            results = sqlSesion.delete("subjectMapping.querySubMappingList", tBasicSubMappingList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return results;
    }

}
