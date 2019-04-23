package com.wqb.dao.arch.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.arch.ArchDao;
import com.wqb.model.Arch;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("archDao")
public class ArchDaoImpl implements ArchDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(ArchDaoImpl.class);

    @Override
    // 工资添加
    public void insertArch(Arch arch) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("archs.insertArch", arch);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // 批量添加
    @Override
    public int insertBath(List<Arch> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int insert = 0;
        try {
            insert = sqlSession.insert("archs.insertBath", list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;

    }

    // 查询是否有重复的数据
    @Override
    public List<Arch> queryByCodeAndAcperiod(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Arch> list = null;
        try {
            list = sqlSession.selectList("archs.queryByCodeAndAcperiod", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 综合查询 分页
    @Override
    public List<Arch> queryListPage(Map<String, Object> map) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Arch> list = null;
        try {
            list = sqlSession.selectList("archs.queryListPage", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    // 删除
    public void delById(Map<String, String> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("archs.delById", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 批量删除
    @Override
    public void delBathById(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("archs.delBathById", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<Arch> query2vouch(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Arch> list = null;
        try {
            list = sqlSession.selectList("archs.query2vouch", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> queryDepart(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Map<String, Object>> list = null;
        try {
            // sd 水电费 qtkk其它扣款 grsds个人所得税 gjj公积金 sb扣社保 sf实发工资 yf应发工资
            // select sum(utilities) as sd, sum(deduction) as qtkk,sum(taxFree)
            // as grsds ,sum(provident) as gjj, sum(socialfree) as
            // sb,acDepartment,sum(realwages) as sf,sum(payAble) as yf from
            // t_wa_arch where accountID = #{accountID} and acperiod like
            // CONCAT('%','${busDate}','%' ) group by acDepartment;

            list = sqlSession.selectList("archs.queryDepart", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 查询期间薪资总数量
    @Override
    public int queryCount(Map<String, Object> param) throws BusinessException {

        int res = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            res = sqlSession.selectOne("archs.queryCount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return res;
    }

    // 查询做账期间所有工资月份
    @Override
    public String queryArchDate(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String res = null;
        try {
            res = sqlSession.selectOne("archs.queryArchDate", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return res;
    }

    @Override
    public List<String> querycode(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            list = sqlSession.selectList("archs.querycode", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public Object queryFfArchData(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object obj = null;
        try {
            obj = sqlSession.selectOne("archs.queryFfArchData", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return obj;
    }

    @Override
    public List<Arch> queryArchData(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Arch> list = null;
        try {
            list = sqlSession.selectList("archs.queryArchData", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

}
