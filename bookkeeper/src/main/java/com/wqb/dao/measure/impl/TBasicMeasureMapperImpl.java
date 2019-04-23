package com.wqb.dao.measure.impl;


import com.wqb.common.Log4jLogger;
import com.wqb.dao.measure.TBasicMeasureMapper;
import com.wqb.model.TBasicMeasure;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 司氏旭东
 * @ClassName: TBasicMeasureMapperImpl
 * @Description: 数量单位数据持久层实现
 * @date 2017年12月20日 上午10:05:20
 */
@Component
@Service("tBasicMeasureMapper")
public class TBasicMeasureMapperImpl implements TBasicMeasureMapper {

    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicMeasureMapperImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int deleteByPrimaryKey(String pkMeasureId) {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.insert("measure.deleteByPrimaryKey", pkMeasureId);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public int insertMeasure(TBasicMeasure tBasicMeasure) {
        SqlSession sqlSession = null;
        int results = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            results = sqlSession.insert("measure.insertMeasure", tBasicMeasure);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public List<TBasicMeasure> queryMeasure(TBasicMeasure tBasicMeasure) {
        SqlSession sqlSession = null;
        List<TBasicMeasure> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("measure.queryMeasure", tBasicMeasure);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public int updateMeasure(TBasicMeasure tBasicMeasure) {
        SqlSession sqlSession = null;
        int update = 0;
        try {
            sqlSession = sqlSessionFactory.openSession();
            update = sqlSession.update("measure.updateMeasure", tBasicMeasure);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return update;
    }

    @Override
    public List<TBasicMeasure> queryMeasureBySymbolOrName(TBasicMeasure tBasicMeasure) {
        SqlSession sqlSession = null;
        List<TBasicMeasure> selectList = new ArrayList<TBasicMeasure>();
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("measure.queryMeasureBySymbolOrName", tBasicMeasure);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }
}
