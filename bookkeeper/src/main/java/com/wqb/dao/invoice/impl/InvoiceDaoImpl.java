package com.wqb.dao.invoice.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.dao.KcCommodity.impl.KcCommodityDaoImpl;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.model.InvoiceBody;
import com.wqb.model.InvoiceHead;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("invoiceDao")
public class InvoiceDaoImpl implements InvoiceDao {

    private static Log4jLogger logger = Log4jLogger.getLogger(KcCommodityDaoImpl.class);

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void updInvoiceByVouID(String vouchID) throws BusinessException {
        // update t_fa_invoice_h set vouchID='' where
        // vouchID=#{vouchID};
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("invoice.updInvoiceByVouID", vouchID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertInvocieH(InvoiceHead invoiceHead) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            sqlSession.insert("invoice.insertInvocieH", invoiceHead);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertInvoiceB(InvoiceBody invoiceBody) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("invoice.insertInvoiceB", invoiceBody);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<InvoiceHead> querySame(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceHead> list = null;
        try {
            list = sqlSession.selectList("invoice.querySame", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<InvoiceHead> queryInvoiceH(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceHead> list = null;
        try {
            list = sqlSession.selectList("invoice.queryInvoiceH", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public int deleteInvoiceH(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            int delete = sqlSession.delete("invoice.deleteInvoiceH", param);
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
    public int deleteInvoiceB(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("invoice.deleteInvoiceB", param);
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
    public List<InvoiceHead> queryJxInvoiceH2Voucher(Map<String, Object> param) throws BusinessException {
        // from t_fa_invoice_h where accountID=#{accountID} and invoiceType=1
        // and period like CONCAT('%','${busDate}','%' );
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceHead> list = null;
        try {
            list = sqlSession.selectList("invoice.queryJxInvoiceH2Voucher", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<InvoiceHead> queryXxInvoiceH2Voucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceHead> list = null;
        try {
            list = sqlSession.selectList("invoice.queryXxInvoiceH2Voucher", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<InvoiceBody> queryInvByHid(String invoiceHID) throws BusinessException {
        // select * from t_fa_invoice_b where invoiceHID=#{invoiceHID};
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceBody> list = null;
        try {
            list = sqlSession.selectList("invoice.queryInvByHid", invoiceHID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void updateInvoiceVouID(String invoiceHID, String vouchID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("invoiceHID", invoiceHID);
            param.put("vouchID", vouchID);
            sqlSession.update("invoice.updateInvoiceVouID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<String> queryHBuyCorp(Map<String, Object> param) throws BusinessException {

        /*
         * select buyCorp from t_fa_invoice_h where accountID=#{accountID} and
         * period like CONCAT('','${busDate}','%' ) and invoiceType=2 group by
         * buyCorp
         */

        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            list = sqlSession.selectList("invoice.queryHBuyCorp", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<String> queryHIDByBuyCorp(Map<String, Object> param) throws BusinessException {

        // select invoiceHID from t_fa_invoice_h where accountID=#{accountID}
        // and period like CONCAT('','${busDate}','%' ) and invoiceType=2 and
        // buyCorp=#{buyCorp};

        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            list = sqlSession.selectList("invoice.queryHIDByBuyCorp", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> queryAmountByHID(String invoiceHID) throws BusinessException {
        // select sum(namount) as je,sum(taxAmount) as se from t_fa_invoice_b
        // where invoiceHID = #{invoiceHID} ;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Map<String, Object>> list = null;
        try {
            list = sqlSession.selectList("invoice.queryAmountByHID", invoiceHID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    // 发票子表 根据comNameSpec进行分组 获取本期分组后的商品总金额 和总数量
    public Map<String, InvoiceBody> queryNUmAndMount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceBody> list = null;
        Map<String, InvoiceBody> map = new HashedMap<>();
        try {
            list = sqlSession.selectList("commodity.queryNUmAndMount", param);

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    InvoiceBody invoiceBody = list.get(i);
                    if (invoiceBody == null) {
                        continue;
                    }
                    if (!StringUtil.isEmpty(invoiceBody.getComNameSpec())) {
                        map.put(invoiceBody.getComNameSpec(), invoiceBody);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return map;

    }

    // 根据商品名称与规格对进项销项分组
    public List<InvoiceBody> queryInvoiceGroupComName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceBody> list = null;
        try {
            list = sqlSession.selectList("commodity.queryNUmAndMount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;

    }

    // 根据商品名称统计进项数量与金额
    @Override
    public List<InvoiceBody> queryAmountByComName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceBody> list = null;
        try {
            list = sqlSession.selectList("commodity.queryAmountByComName", param);
            /*
             * select sum(nnumber) nnumber,sum(namount) namount se from
             * t_fa_invoice_b and accountID=#{accountID} and period=#{busDate}
             * <where> <if test="comNameSpec!=null"> and
             * comNameSpec=#{comNameSpec} </if> <if test="invoiceType!=null">
             * and invoiceType=#{invoiceType} </if> </where>
             */
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public int queryInvobCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = 0;
        try {
            num = sqlSession.selectOne("invoice.queryInvobCount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    // 删除所有发票
    @Override
    public int delFaPiao1(Map<String, Object> qerMap) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.delete("invoice.delFaPiao1", qerMap);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public int delFaPiao2(Map<String, Object> qerMap) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.delete("invoice.delFaPiao2", qerMap);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public List<InvoiceHead> queryByVouchID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceHead> list = null;
        try {
            list = sqlSession.selectList("invoice.queryByVouchID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public int editInvoice(InvoiceBody vb) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("invoice.editInvoice", vb);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }

    @Override
    public List<InvoiceHead> queryInvoiceHAll(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceHead> list = sqlSession.selectList("invoice.queryInvoiceHAll", param);
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
    public List<InvoiceBody> queryInvoiceBAll(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceBody> list = sqlSession.selectList("invoice.queryInvoiceBAll", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public InvoiceBody queryInvoiceByBid(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceBody> list = sqlSession.selectList("invoice.queryInvoiceByBid", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
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
    public int queryInvoiceBCountByHid(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("invoice.queryInvoiceBCountByHid", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int deleteInvoiceHBath(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            int delete = sqlSession.delete("invoice.deleteInvoiceHBath", list);
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
    public InvoiceHead queryInvoiceByHid(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceHead> list = sqlSession.selectList("invoice.queryInvoiceByHid", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
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
