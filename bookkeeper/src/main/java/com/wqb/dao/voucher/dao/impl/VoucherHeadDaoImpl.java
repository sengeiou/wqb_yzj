package com.wqb.dao.voucher.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.VoucherBody;
import com.wqb.model.VoucherHead;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("voucherHeadDao")
public class VoucherHeadDaoImpl implements VoucherHeadDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(VoucherHeadDaoImpl.class);

    /**
     * 查询凭证最大号
     */
    @Override
    public Integer queryMaxVouchNO(String accountID, String period) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int no = 0;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", accountID);
            param.put("period", period);
            Object selectOne = sqlSession.selectOne("voucher.queryMaxVouchNO", param);
            if (selectOne != null) {
                no = (int) selectOne;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return no;
    }

    /**
     * 插入凭证头信息
     */
    @Override
    public Integer insertVouchHead(VoucherHead vouchHead) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = 0;
        try {
            count = sqlSession.insert("voucher.insertVouchHead", vouchHead);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public VoucherHead queryVouHByID(String vouchID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("voucher.queryVouHByID", vouchID);
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
    public void updVouHByID(VoucherHead voucherHead) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.updVouHByID", voucherHead);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteVouHeadByID(String voucherID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // delete from t_vouch_h where vouchID=#{vouchID}
        try {
            sqlSession.update("voucher.deleteVouHeadByID", voucherID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // lch 凭证断号整理查询所有凭证
    @Override
    public List<VoucherHead> queryVHByUserIdAndVouchID(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 根据登录用户ID和账套ID查询全部凭证
            List<VoucherHead> list = sqlSession.selectList("voucher.queryVHByUserIdAndVouchID", param);
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

    // lch 凭证断号整理voucherNo凭证号重新制作
    @Override
    public void updatevoucherNo(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 根据登录用户userID和账套accountID查询全部凭证
            sqlSession.update("voucher.updatevoucherNo", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // 凭证审核与反审核
    @Override
    public void chgVouchStatu(String auditStatus, String vouchID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("auditStatus", auditStatus);
            param.put("vouchID", vouchID);
            sqlSession.update("voucher.chgVouchStatus", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
    // 分页查询所有凭证

    @Override
    public List<VoucherHead> queryVouHead(Map<String, Object> param) throws BusinessException {
        // select * from t_vouch_h where accountID=#{accountID} and period like
        // CONCAT('','${busDate}','%' ) order by voucherNo,source
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherHead> list = null;
        try {
            list = sqlSession.selectList("voucher.queryVouHead", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // lch 获取凭证最大号自增1
    @Override
    public Integer getMaxVoucherNo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer maxNo = 0;
        try {
            maxNo = sqlSession.selectOne("voucher.getMaxVoucherNo", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return maxNo;
    }

    @Override
    // 凭证金额更新
    public void chgVouchAmount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.chgVouchAmount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<VoucherHead> queryInvAndBankVou(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherHead> list = null;
        try {
            list = sqlSession.selectList("voucher.queryInvAndBankVou", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public List<VoucherHead> queryAllVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherHead> list = null;
        try {
            list = sqlSession.selectList("voucher.queryAllVouch", param);
            // select * from t_vouch_H where accountID=#{accountID} and
            // period=#{busDate};
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void oneKeyCheckVoucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.oneKeyCheckVoucher", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // lch 查询是否有结转销售成本
    @Override
    public List<VoucherBody> queryjzCarry(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherBody> list = null;
        try {
            list = sqlSession.selectList("voucher.queryjzCarry", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    // lch 检查评主体是否有问题
    @Override
    public void upVouBodyById(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.upVouBodyById", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // lch 检查凭证头
    @Override
    public void upVouHeadByCheckId(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.upVouHeadByCheckId", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    // lch 凭证汇总
    @Override
    public List<TBasicSubjectMessage> queryVouSummary(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSession.selectList("voucher.queryVouSummary", param);
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

    // 更新凭证修改次数 字段删除了,凌成辉删除 2018-12-03
    // @Override
    // public void upModify(Map<String, Object> param) throws BusinessException
    // {
    // SqlSession sqlSession = sqlSessionFactory.openSession();
    // try {
    // sqlSession.update("voucher.upModify", param);
    // } catch (Exception e) {
    // logger.error(e);
    // e.printStackTrace();
    // } finally {
    // sqlSession.close();
    // }
    // }

    @Override
    public List<VoucherHead> queryUnQmjzVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherHead> list = null;
        try {
            // select * from t_vouch_h where accountID=#{accountID} and period =
            // #{busDate} and source=#{source}
            list = sqlSession.selectList("voucher.queryUnQmjzVouch", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 凭证检查更新
    @Override
    public void upVouHeadByCheckIdCall(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.upVouHeadByCheckIdCall", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    //// 查询凭证总数
    public int queryCountVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = 0;
        try {
            num = sqlSession.selectOne("voucher.queryCountVouch", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public List<VoucherHead> queryImportVoucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> headList = sqlSession.selectList("voucher.queryImportVoucher", param);
            return headList;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<VoucherHead> queryRevisedVoucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> headList = sqlSession.selectList("voucher.queryRevisedVoucher", param);
            return headList;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public void updAttachID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.selectList("voucher.updAttachID", param);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<VoucherHead> queryAttachByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> headList = sqlSession.selectList("voucher.queryAttachByID", param);
            return headList;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<VoucherHead> queryVouchByAttachID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> headList = sqlSession.selectList("voucher.queryVouchByAttachID", param);
            return headList;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    // 查询成本结转凭证
    @Override
    public VoucherBody queryCbjzVo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /*
         * select * from t_vouch_b where vouchID in( select vouchID from
         * t_vouch_h where accountID = #{accountID} and period = #{period} and
         * source in(5,11) ) and vcabstact like CONCAT('%','成本','%' ) and
         * vcabstact like CONCAT('%','结转','%' ) limit 1
         */

        try {
            List<VoucherBody> list = sqlSession.selectList("vat.queryCbjzVo", param);
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

    /**
     * @param param
     * @return int 返回类型
     * @Title: queryCountVouch2
     * @Description: 查询凭证总数--如果有凭证不能初始化科目余额表
     * @date 2018年7月23日 下午5:08:21
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int queryCountVouch2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = 0;
        try {
            num = sqlSession.selectOne("voucher.queryCountVouch2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public List<VoucherBody> queryVoBody(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherBody> list = sqlSession.selectList("voucher.queryVoBody", param);
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
    public List<VoucherHead> queryVouchByNo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("voucher.queryVouchByNo", param);
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
    public List<Map<String, Object>> queryImportVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Map<String, Object>> list = sqlSession.selectList("voucher.queryImportVouch", param);
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
    public void updVouchAmount(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("voucher.updVouchAmount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<VoucherHead> queryProblemVouch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("voucher.queryProblemVouch", param);
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
    public List<VoucherHead> querySameVouchNo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("voucher.querySameVouchNo", param);
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

    /*
     * 查询断号凭证
     */
    public List<Integer> queryVoucherBrokenNo(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Integer> list = new ArrayList<Integer>();
        try {
            list = sqlSession.selectList("voucher.queryVoucherBrokenNo", param);
            if (!list.isEmpty() && list.size() > 0) {
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
    public Object getPrintVoucherRange(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object obj = null;
        try {
            obj = sqlSession.selectOne("voucher.getPrintVoucherRange", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return obj;
    }

    @Override
    public List<VoucherHead> getRangeVoucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<VoucherHead> list = new ArrayList<VoucherHead>();
        try {
            list = sqlSession.selectList("voucher.getRangeVoucher", param);
            if (!list.isEmpty() && list.size() > 0) {
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
}
