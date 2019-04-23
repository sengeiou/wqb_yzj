package com.wqb.dao.businessTax.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.businessTax.BusinessTaxDao;
import com.wqb.model.SubjectMessage;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("businessTaxDao")
public class BusinessTaxDaoImpl implements BusinessTaxDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(BusinessTaxDaoImpl.class);

    // 测试
    @Override
    public SubjectMessage queryBusinessTax() throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SubjectMessage sub = null;
        try {
            sub = (SubjectMessage) sqlSession.selectOne("businessTax.querySub");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return sub;
    }

}
