package com.wqb.dao.subexcel.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.subexcel.TBasicSubjectExcelMapper;
import com.wqb.model.TBasicSubjectExcel;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("tBasicSubjectExcelMapper")
public class TBasicSubjectExcelMapperImpl implements TBasicSubjectExcelMapper {

    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectExcelMapperImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    // 导入excel初始化科目
    @Override
    public void uploadSubExcel(TBasicSubjectExcel subExcel) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("balanceSheet.uploadSubExcel", subExcel);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubExcel
     * @Description: 查询已导入的初始化excel数据
     * @date 2017年12月7日 下午3:12:45
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectExcel> querySubExcel(TBasicSubjectExcel tBasicSubjectExcel) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> selectList = null;
        try {
            selectList = sqlSession.selectList("subExcel.querySubExcel", tBasicSubjectExcel);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectExcel
     * @return List<TBasicSubjectExcel> 返回类型
     * @Title: querySubExcelMoney
     * @Description: 查询系统科目真实金额
     * @date 2018年1月25日 下午8:27:41
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectExcel> querySubExcelMoney(TBasicSubjectExcel tBasicSubjectExcel) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> selectList = null;
        try {
            selectList = sqlSession.selectList("subExcel.querySubExcelMoney", tBasicSubjectExcel);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public List<TBasicSubjectExcel> querySubByisMatching(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> selectList = null;
        try {
            selectList = sqlSession.selectList("subExcel.querySubByisMatching", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public List<TBasicSubjectExcel> querySubBySubSode(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> selectList = null;
        try {
            selectList = sqlSession.selectList("subExcel.querySubBySubSode", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public List<TBasicSubjectExcel> updateSubExcel(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> selectList = null;
        try {
            selectList = sqlSession.selectList("subExcel.updateSubExcel", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public int deleteSubExcelAll(TBasicSubjectExcel tBasicSubjectExcel) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int no = 0;
        try {
            no = sqlSession.delete("subExcel.deleteSubExcelAll", tBasicSubjectExcel);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public int uploadSubExcelList(List<TBasicSubjectExcel> tBasicSubjectExcelList) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int no = 0;
        try {
            no = sqlSession.insert("subExcel.uploadSubExcelList", tBasicSubjectExcelList);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    @Override
    public TBasicSubjectExcel getExcelSubByPKID(String pkSubExcelID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<TBasicSubjectExcel> list = null;
        try {
            list = sqlSession.selectList("subExcel.getExcelSubByPKID", pkSubExcelID);
            if (list != null && list.size() == 1) {
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }
}
