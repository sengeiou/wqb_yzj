package com.wqb.dao.invoice.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.invoice.InvoiceMappingDao;
import com.wqb.model.InvoiceMappingrecord;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("invoiceMappingDao")
public class InvoiceMappingDaoImpl implements InvoiceMappingDao {
    private static Log4jLogger logger = Log4jLogger.getLogger(InvoiceMappingDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public int insertMappingrecord(InvoiceMappingrecord mappingrecord) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int insert = sqlSession.insert("invoiceMapping.insertMappingrecord", mappingrecord);
            return insert;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("insertMappingrecord 查询异常");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public InvoiceMappingrecord queryMappingrecord(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceMappingrecord> list = sqlSession.selectList("invoiceMapping.queryMappingrecord", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("insertMappingrecord 查询异常");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int updateMappingrecord(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("invoiceMapping.updateMappingrecord", param);
            return update;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("updateMappingrecord 更新异常");
        } finally {
            sqlSession.close();
        }
    }

}
