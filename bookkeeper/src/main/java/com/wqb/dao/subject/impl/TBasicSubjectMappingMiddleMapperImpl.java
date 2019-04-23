package com.wqb.dao.subject.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.model.TBasicSubjectMappingMiddle;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service("TBasicSubjectMappingMiddleMapper")
public class TBasicSubjectMappingMiddleMapperImpl implements TBasicSubjectMappingMiddleMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingMiddleMapperImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int deleteByPrimaryKey(Integer pkSubMappingMiddleId) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.delete("subjectMappingMiddle.deleteByPrimaryKey", pkSubMappingMiddleId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.deleteByPriamryKey错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int insert(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMappingMiddle.insert", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.insert错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int insertSelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMappingMiddle.insertSelective", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.insertSelective错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public TBasicSubjectMappingMiddle selectByPrimaryKey(Integer pkSubMappingMiddleId) {
        SqlSession sqlSesion = null;
        TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            tBasicSubjectMappingMiddle = sqlSesion.selectOne("subjectMappingMiddle.selectByPrimaryKey", pkSubMappingMiddleId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.selectByPrimaryKey错误");
        } finally {
            sqlSesion.close();
        }
        return tBasicSubjectMappingMiddle;
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.update("subjectMappingMiddle.updateByPrimaryKeySelective", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.updateByPrimaryKeySelective错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int updateByPrimaryKey(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.update("subjectMappingMiddle.updateByPrimaryKey", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.updateByPrimaryKey错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public List<TBasicSubjectMappingMiddle> querySubMappingMiddleByAccId(String accountId) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMappingMiddle> SubMappingMiddleList = new ArrayList<TBasicSubjectMappingMiddle>();
        try {
            sqlSesion = sqlSessionFactory.openSession();
            SubMappingMiddleList = sqlSesion.selectList("subjectMappingMiddle.querySubMappingMiddleByAccId", accountId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.querySubMappingMiddleByAccId错误");
        } finally {
            sqlSesion.close();
        }
        return SubMappingMiddleList;
    }

    @Override
    public int insertList(List<TBasicSubjectMappingMiddle> subMappingMiddleList) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMappingMiddle.insertList", subMappingMiddleList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.insertList错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public int saveOrUpdate(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subjectMappingMiddle.saveOrUpdate", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.saveOrUpdate错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    @Override
    public TBasicSubjectMappingMiddle querySubMappingMiddle(Map<String, String> subMappingMap) {
        SqlSession sqlSesion = null;
        TBasicSubjectMappingMiddle subMappingMiddle = new TBasicSubjectMappingMiddle();
        try {
            sqlSesion = sqlSessionFactory.openSession();
            subMappingMiddle = sqlSesion.selectOne("subjectMappingMiddle.querySubMappingMiddle", subMappingMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.querySubMappingMiddle错误");
        } finally {
            sqlSesion.close();
        }
        return subMappingMiddle;
    }

    @Override
    public List<TBasicSubjectMappingMiddle> validationSubMappingMiddle(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMappingMiddle> SubMappingMiddleList = new ArrayList<TBasicSubjectMappingMiddle>();
        try {
            sqlSesion = sqlSessionFactory.openSession();
            SubMappingMiddleList = sqlSesion.selectList("subjectMappingMiddle.validationSubMappingMiddle", tBasicSubjectMappingMiddle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.validationSubMappingMiddle错误");
        } finally {
            sqlSesion.close();
        }
        return SubMappingMiddleList;
    }

    /**
     * 根据 账套id 删除映射信息表
     *
     * @param accountId
     * @return
     */
    public int deleteByAccountId(String accountId) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.delete("subjectMappingMiddle.deleteByAccountId", accountId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("subjectMappingMiddle.deleteByAccountId错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

}
