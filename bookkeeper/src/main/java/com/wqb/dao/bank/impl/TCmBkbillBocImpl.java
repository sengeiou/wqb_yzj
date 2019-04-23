package com.wqb.dao.bank.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bank.TCmBkbillBocMapper;
import com.wqb.model.bank.TCmBkbillBoc;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("tCmBkbillBocMapper")
public class TCmBkbillBocImpl implements TCmBkbillBocMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TCmBkbillBocImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    //	@Override
    //	public int deleteByPrimaryKey(String pkBkbillBoc)
    //	{
    //		// TODO Auto-generated method stub
    //		return 0;
    //	}
    //
    @Override
    public int insert(TCmBkbillBoc tCmBkbillBoc) {
        int insert = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            insert = sqlSession.insert("bkbillBoc.insert", tCmBkbillBoc);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;
    }
    //
    //	@Override
    //	public int insertSelective(TCmBkbillBoc record)
    //	{
    //		// TODO Auto-generated method stub
    //		return 0;
    //	}
    //
    //	@Override
    //	public TCmBkbillBoc selectByPrimaryKey(String pkBkbillBoc)
    //	{
    //		// TODO Auto-generated method stub
    //		return null;
    //	}
    //
    //	@Override
    //	public int updateByPrimaryKeySelective(TCmBkbillBoc record)
    //	{
    //		// TODO Auto-generated method stub
    //		return 0;
    //	}
    //
    //	@Override
    //	public int updateByPrimaryKey(TCmBkbillBoc record)
    //	{
    //		// TODO Auto-generated method stub
    //		return 0;
    //	}
    //
    //	@Override
    //	public void uploadTCmBillBoc(MultipartFile bkillBocExcel)
    //	{
    //		// TODO Auto-generated method stub
    //
    //	}

    @Override
    public int uploadTCmBillBocList(List<TCmBkbillBoc> tCmBkbillBocList) {
        int insert = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            insert = sqlSession.insert("bkbillBoc.uploadTCmBillBocList", tCmBkbillBocList);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;
    }

    @Override
    public List<TCmBkbillBoc> queryTCmBkbillBocList(TCmBkbillBoc tCmBkbillBoc) {
        SqlSession sqlSession = null;
        List<TCmBkbillBoc> tCmBkbillBocs = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            tCmBkbillBocs = sqlSession.selectList("bkbillBoc.queryTCmBkbillBocList", tCmBkbillBoc);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return tCmBkbillBocs;
    }

    @Override
    public int deleteTCmbkbillBocAll(TCmBkbillBoc tCmBkbillBoc) {
        int insert = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            insert = sqlSession.insert("bkbillBoc.deleteTCmbkbillBocAll", tCmBkbillBoc);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;
    }

    @Override
    public int deleteTCmBkbillBocByBkbillBoc(String pkBkbillBoc) {
        int insert = 0;
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            insert = sqlSession.insert("bkbillBoc.deleteTCmbkbillBocByPkBkbillBoc", pkBkbillBoc);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;
    }

    @Override
    public void deleteByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("bkbillBoc.deleteByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    //中国银行分页查询
    @Override
    public List<TCmBkbillBoc> queryBankBill(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TCmBkbillBoc> list = sqlSession.selectList("bkbillBoc.queryBankBill", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<TCmBkbillBoc> queryAll(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TCmBkbillBoc> list = sqlSession.selectList("bkbillBoc.queryAll", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<TCmBkbillBoc> queryBkbillBocByReferenceNumber(List<TCmBkbillBoc> tCmBkbillBocList) {
        SqlSession sqlSession = null;
        List<TCmBkbillBoc> selectList = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            selectList = sqlSession.selectList("bkbillBoc.queryBkbillBocByReferenceNumber", tCmBkbillBocList);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return selectList;
    }

    @Override
    public void delVouchID(String vouchID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            sqlSession.update("bkbillBoc.delVouchID", vouchID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void addVouchID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("bkbillBoc.addVouchID", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("bkbillBoc.updByID", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
}
