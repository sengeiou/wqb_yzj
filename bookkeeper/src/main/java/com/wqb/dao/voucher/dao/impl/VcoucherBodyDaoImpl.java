package com.wqb.dao.voucher.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.model.VoucherBody;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service("voucherBodyDao")
public class VcoucherBodyDaoImpl implements VoucherBodyDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(VcoucherBodyDaoImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public Integer insertVouchBody(VoucherBody vouchBody) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = 0;
        try {
            count = sqlSession.insert("voucher.insertVouchBody", vouchBody);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public List<VoucherBody> queryVouBodyByHID(String vouchID) throws BusinessException {
        //// select * from t_vouch_b where vouchID=? order by rowIndex+0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> list = null;
        try {
            list = sqlSession.selectList("voucher.queryVouBodyByHID", vouchID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public Integer updVouBodyByID(String vouchAId) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = 0;
        try {
            count = sqlSession.update("voucher.updVouBodyByID", vouchAId);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public void deleteVouBodyByID(String vouAId) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("voucher.deleteVouBodyByID", vouAId);
            ////delete from t_vouch_b where vouchID=#{vouchID};
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<VoucherBody> queryArchVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> list = null;
        try {//select * from t_vouch_b where vcabstact='计提本月工资' and accountID=#{accountID};
            list = sqlSession.selectList("voucher.queryArchVouch", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<VoucherBody> queryUnjzVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> list = null;
        try {
            list = sqlSession.selectList("voucher.queryUnjzVouch", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //插入凭证体
    @Override
    public int insertVouchBatch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = -1;
        try {
            count = sqlSession.insert("voucher.insertVouchBatch", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public List<VoucherBody> queryVouBySubname(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> result = new ArrayList<VoucherBody>();
        try {
            result = sqlSession.selectList("voucher.queryVouBySubname", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

    @Override
    public int updeVbByAddSubMessage(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int count = sqlSession.update("voucher.updeVbByAddSubMessage", param);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询工资是否计提
     * querySalary
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    @Override
    public List<VoucherBody> querySalary(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> result = new ArrayList<VoucherBody>();
        try {
            result = sqlSession.selectList("voucher.querySalary", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return result;
    }

}
