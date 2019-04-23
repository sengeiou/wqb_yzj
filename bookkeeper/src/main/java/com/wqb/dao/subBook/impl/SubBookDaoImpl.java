package com.wqb.dao.subBook.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.subBook.SubBookDao;
import com.wqb.model.StatusPeriod;
import com.wqb.model.SubBook;
import com.wqb.model.vomodel.RedisSub;
import com.wqb.model.vomodel.SubBookMessageVo;
import com.wqb.model.vomodel.SubMessageVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("SubBookDao")
public class SubBookDaoImpl implements SubBookDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(SubBookDaoImpl.class);

    //添加科目更新记录
    @Override
    public int insertSubBookBath(List<SubBook> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            int insert = sqlSession.insert("subBook.insertSubBookBath", list);
            return insert;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SubBook> querySubBook(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<SubBook> list = sqlSession.selectList("subBook.querySubBook", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {

            sqlSession.close();

        }
    }

    //根据条件查询总账
    @Override
    public List<SubMessageVo> queryLedger(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubMessageVo> list = sqlSession.selectList("subBook.queryLedger", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();

        }
    }

    //查询明细账科目 本期合计 本年累计
    @Override
    public List<SubMessageVo> querySubToDetailAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<SubMessageVo> list = sqlSession.selectList("subBook.querySubToDetailAcc", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();

        }
    }

    //根据条件查询总数
    @Override
    public int querySubBookCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("subBook.querySubBookCount", param);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    //查询最大期间一条数据
    public List<SubBook> queryOneMaxPeriod(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubBook> list = sqlSession.selectList("subBook.queryOneMaxPeriod", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();

        }
    }

    //单个删除
    @Override
    public int delSubBook(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("subBook.delSubBook", param);
            return delete;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //一键删除
    @Override
    public int delSubBookBath(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("subBook.delSubBookBath", param);
            return delete;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int delSubBookBath2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("subBook.delSubBookBath2", param);
            return delete;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //查询期间范围
    @Override
    public List<StatusPeriod> getRangePeiod(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<StatusPeriod> list = sqlSession.selectList("subBook.getRangePeiod", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    //查询所有科目数据
    public List<SubMessageVo> querySubAll(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubMessageVo> list = sqlSession.selectList("subBook.querySubAll", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public SubMessageVo queryDetailSub2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubMessageVo> list = sqlSession.selectList("subBook.queryDetailSub2", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upSubvoucherNo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("subBook.upSubvoucherNo", param);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //更新明细账
    @Override
    public int upSubBlanceAmount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("subBook.upSubBlanceAmount", param);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //根据凭证id查询明细账
    @Override
    public List<SubBook> queryByVouchID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubBook> list = sqlSession.selectList("subBook.queryByVouchID", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delSubBook2(Map<Object, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("subBook.delSubBook2", param);
            return delete;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public List<SubBook> queryByCodes(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubBook> list = sqlSession.selectList("subBook.queryByCodes", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upSubBlanceAmount2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("subBook.upSubBlanceAmount2", param);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SubBook> queryByConditions(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubBook> list = sqlSession.selectList("subBook.queryByConditions", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();

        }
    }

    @Override
    public List<SubBookMessageVo> queryAllSubBook(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubBookMessageVo> list = sqlSession.selectList("subBook.queryAllSubBook", map);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<RedisSub> queryDetailSubMessage(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<RedisSub> list = sqlSession.selectList("subBook.queryDetailSubMessage", map);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


}
