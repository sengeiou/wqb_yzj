package com.wqb.dao.customer.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.customer.CustomerDao;
import com.wqb.model.Customer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerDao")
public class CustomerDaoImpl implements CustomerDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(CustomerDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public Customer queryCusByAcc(String accountID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Customer> list = null;
        try {
            list = sqlSession.selectList("customer.queryCusByAcc", accountID);
            if (list != null && list.size() > 0) {
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

    @Override
    public List<Customer> queryCusBath(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Customer> cusList = null;
        try {
            cusList = sqlSession.selectList("customer.queryCusBath", list);
            if (cusList != null && cusList.size() > 0) {
                return cusList;
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
