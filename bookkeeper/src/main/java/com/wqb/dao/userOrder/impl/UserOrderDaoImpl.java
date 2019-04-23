package com.wqb.dao.userOrder.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.userOrder.UserOrderDao;
import com.wqb.model.UserOrder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@Service("UserOrderDao")
public class UserOrderDaoImpl implements UserOrderDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(UserOrderDaoImpl.class);


    @Override
    public int insertUserOrder(UserOrder userOrder) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int insert = sqlSession.insert("userOrder.insertUserOrder", userOrder);
            return insert;
        } catch (Exception e) {
            logger.error("UserOrderDaoImpl【insertUserOrder】添加用户异常", e);
            throw new BusinessException("insertUserOrder error");
        } finally {
            sqlSession.close();
        }
    }


}
