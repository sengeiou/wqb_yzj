package com.wqb.dao.attached.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.attached.AttachedDao;
import com.wqb.model.VoucherHead;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("attachedDao")
public class AttachedDaoImpl implements AttachedDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(AttachedDaoImpl.class);

    //根据id查询凭证头
    @Override
    public VoucherHead queryVoucherHeadById(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        VoucherHead voch = null;
        try {
            voch = sqlSession.selectOne("atta.queryVoucherHeadById", id);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return voch;
    }

}
