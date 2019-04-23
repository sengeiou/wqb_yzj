package com.wqb.dao.tempType.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.tempType.TempTypeDao;
import com.wqb.model.SubjectMessage;
import com.wqb.model.TempType;
import com.wqb.model.vomodel.TempVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class TempTypeDaoImpl implements TempTypeDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(TempTypeDao.class);


    @Override
    public Integer insertTempType(TempType tmp) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int insert = sqlSession.insert("tempType.insertTempType", tmp);
            return insert;
        } catch (Exception e) {
            throw new BusinessException(e + ",VbContent.lenght = " + tmp.getVbContent().length() + ",+temp=" + tmp.getVbContent());
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<TempType> queryTemp(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TempType> list = sqlSession.selectList("tempType.queryTemp", map);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int delTem(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("tempType.delTem", map);
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upTem(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("tempType.upTem", map);
            return update;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int queryTempCount(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int selectOne = sqlSession.selectOne("tempType.queryTempCount", map);
            return selectOne;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> queryGroupSub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Map<String, String> res = (Map<String, String>) sqlSession.selectOne("tempType.queryGroupSub", map);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SubjectMessage> queryAllSub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("tempType.queryAllSub", map);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int querySubCount(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int selectOne = sqlSession.selectOne("tempType.querySubCount", map);
            return selectOne;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int queryCountVB(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int selectOne = sqlSession.selectOne("tempType.queryCountVB", map);
            return selectOne;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<TempVo> querySalaryVB(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TempVo> list = sqlSession.selectList("tempType.querySalaryVB", map);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public List<TempVo> queryGoodsVB(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TempVo> list = sqlSession.selectList("tempType.queryGoodsVB", map);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


}
