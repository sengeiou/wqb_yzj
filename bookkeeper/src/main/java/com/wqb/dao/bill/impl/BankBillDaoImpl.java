package com.wqb.dao.bill.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.bill.BankBillDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("bankBillDao")
public class BankBillDaoImpl implements BankBillDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(BankBillDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void updBillVouByVouID(String vouchID) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("bankBill.updBillVouByVouID", vouchID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
