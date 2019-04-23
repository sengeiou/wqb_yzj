package com.wqb.dao.vat.impl;


import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.RedisSub;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//增值税结转

@Service("VatDao")
public class VatDaoImpl implements VatDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(VatDaoImpl.class);

    @Override
    //计算进销税额结果
    public InvoiceBody queryTax(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        InvoiceBody invoiceBody = null;

        try {
            invoiceBody = sqlSession.selectOne("vat.queryTax", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return invoiceBody;
    }

    //查询二级科目应交增值税是否存在
    @Override
    public SubjectMessage querySubjectVatTwo(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<SubjectMessage> sub = sqlSession.selectList("vat.querySubjectVatTwo", map);
            if (sub != null && sub.size() > 0) {
                return sub.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;

    }

    //新科目添加
    @Override
    public int insertSubject(SubjectMessage sub) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            int insert = sqlSession.insert("vat.insertSubject", sub);
            return insert;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //增值税留底科目本期金额更新2
    @Override
    public int updateSubjectMap(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            int update = sqlSession.update("vat.updateSubjectMap", map);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //增值税留底科目本期金额更新
    @Override
    public void updateSubject(SubjectMessage sub) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            sqlSession.update("vat.updateSubject", sub);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //根据主键查询科目
    @Override
    public SubjectMessage queryByID(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SubjectMessage result = null;
        try {
            result = sqlSession.selectOne("vat.queryByiD", id);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return result;

    }

    //添加凭证子表
    @Override
    public void insertVoBody(VoucherBody voBody) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("vat.insertVoBody", voBody);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //添加凭证主表
    @Override
    public void insertVoHead(VoucherHead voHead) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("vat.insertVoHead", voHead);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //期末结账记录添加
    @Override
    public void isnertStatusPeriod(StatusPeriod sta) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("vat.isnertStatusPeriod", sta);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public StatusPeriod queryVatSta(StatusPeriod sta) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatusPeriod> list = null;
        try {
            list = sqlSession.selectList("vat.queryVatSta", sta);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    @Override
    //查询凭体
    public List<VoucherBody> queryVoBody(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherBody> list = sqlSession.selectList("vat.queryVoBody", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

        return null;
    }

    @Override
    // 查询凭证头
    public VoucherHead queryVoHeahder(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        VoucherHead vheader = null;
        try {
            vheader = sqlSession.selectOne("vat.queryVoHeahder", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return vheader;
    }

    //根据ID查询凭证头
    @Override
    public VoucherHead queryVoHeahderById(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        VoucherHead vheader = null;
        try {
            vheader = sqlSession.selectOne("vat.queryVoHeahderById", id);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return vheader;
    }

    //根据ID查询凭证子表
    @Override
    public VoucherBody queryVoBodyById(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        VoucherBody vbody = null;
        try {
            vbody = sqlSession.selectOne("vat.queryVoBodyById", id);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return vbody;
    }

    @Override
    //查询经销项
    public List<InvoiceBody> queryAllInvoce(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<InvoiceBody> list = sqlSession.selectList("vat.queryAllInvoce", map);
            List<InvoiceBody> arr = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (
                        int i = 0; i < list.size(); i++) {
                    InvoiceBody invoiceBody = list.get(i);
                    String comName = invoiceBody.getComName(); //产品名称  ：调光电源
                    String spec = invoiceBody.getSpec(); //规格： EUP75T-1H12V-0
                    if (comName != null && isContainChinese(comName)) { //判断名称不为空并且包含中文
                        invoiceBody.setComName(comName + "-" + spec); //拼接名称加上规格 ： 调光电源-EUP75T-1H12V-0
                        arr.add(invoiceBody);
                    }
                }
                if (arr != null && arr.size() > 0) {
                    return arr;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    //按照code查询科目
    @Override
    public SubjectMessage querySubByCode(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("vat.querySubByCode", map);
            if (list != null && list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public VoucherHead queryVoBySource(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("vat.queryVoBySource", map);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public VoucherHead queryVoBySource2(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("vat.queryVoBySource2", map);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

        return null;
    }

    //测试数据1 satrt 测试完可以删除

    @Override
    public List<User> getAllUser() throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> list = sqlSession.selectList("vat.getAllUser");

            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<Account> getAccByUid(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("vat.getAccByUid", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public void upAccSource(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("vat.upAccSource", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    /******* 测试数据 *******/
    //测试数据1 end  测试完可以删除

    //一键生成凭证 查询所有科目
    @Override
    public List<SubjectMessage> querySubByVo(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SubjectMessage> list = null;
        try {
            list = sqlSession.selectList("vat.querySubByVo", map);
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //更新科目
    @Override
    public int upSubVo(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.upSubVo", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //一键检查凭证更新
    @Override
    public int upVouch(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("vat.upVouch", map);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return num;
    }

    //一键检查凭证更新
    @Override
    public int upVouchBody(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("vat.upVouchBody", map);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public SubjectMessage querySubById(String subID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<SubjectMessage> list = null;
        Map<String, String> map = new HashMap<>();
        map.put("subID", subID);
        try {
            list = sqlSession.selectList("vat.querySubById", map);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public int updateCommBySub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("commodity.updateCommBySub", map);

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return num;

    }

    @Override
    public int chgSubByVo(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("commodity.updateCommBySub", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return num;
    }

    // 1
    @Override
    public int chgEndBlanceDebit(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.chgEndBlanceDebit", list);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //2
    @Override
    public List<String> getEndBlanceSub(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> arr = sqlSession.selectList("vat.getEndBlanceSub", list);
            if (arr != null && arr.size() > 0) {
                return arr;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //3
    @Override
    public int chgEndBlanceCredit(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.chgEndBlanceCredit", list);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upSubByVo(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.upSubByVo", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<TBasicSubjectMessage> querySunYiSub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> arr = sqlSession.selectList("subMessage.querySunYiSub", map);
            if (arr != null && arr.size() > 0) {
                return arr;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException("VatDaoImpl-querySunYiSub-787" + e.getMessage());
        } finally {
            sqlSession.close();
        }
    }

    //查询科目
    @Override
    public List<SubjectMessage> querySubjectVatThree(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("vat.querySubjectVatThree", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }


    @Override
    //把账套中文名称转换成拼音
    public List<Account> queryAccAll() throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("vat.queryAccAll");
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public int upAccCompanyName(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.upAccCompanyName", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<VoucherHead> queryCbjzVo2(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<VoucherHead> list = sqlSession.selectList("vat.queryCbjzVo2", map);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upVoTypeByImport(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.upVoTypeByImport", map);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<RedisSub> queryRedisSub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<RedisSub> list = sqlSession.selectList("vat.queryRedisSub", map);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<RedisSub> queryRedisSubByCondition(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<RedisSub> list = sqlSession.selectList("vat.queryRedisSubByCondition", map);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //根据主键查询
    @Override
    public TBasicSubjectMessage queryTBasicSubjectMessageById(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> res = sqlSession.selectList("vat.queryTBasicSubjectMessageById", id);
            if (res == null || res.isEmpty()) {
                return null;
            }
            if (res.size() > 1) {
                throw new BusinessException("queryTBasicSubjectMessageById res.size()>1");
            }
            return res.get(0);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //删除科目更新数据
    @Override
    public int chgSubAmountByDelete2(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.chgSubAmountByDelete2", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int chgSubAmountByDelete3(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.update("vat.chgSubAmountByDelete3", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //根据主键查询科目
    @Override
    public List<SubjectMessage> querySubjectMessageByPkid(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("vat.querySubjectMessageByPkid", map);
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
    public List<TBasicSubjectMessage> queryTBasicSubjectMessageById2(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSession.selectList("vat.queryTBasicSubjectMessageById2", map);
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
    public List<VoucherHead> queryNextOrPreviousVb(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<VoucherHead> list = sqlSession.selectList("vat.queryNextOrPreviousVb", map);
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
    public int queryCountSubjectMessage(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            int num = sqlSession.selectOne("vat.queryCountSubjectMessage", map);
            return num;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<SubjectMessage> queryProfit(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<SubjectMessage> list = sqlSession.selectList("vat.queryProfit", map);
            if (list != null && list.size() > 0) {
                for (SubjectMessage sub : list) {
                    sub.setCurrent_amount_debit(StringUtil.bigDecimalIsNull(sub.getCurrent_amount_debit()));
                    sub.setCurrent_amount_credit(StringUtil.bigDecimalIsNull(sub.getCurrent_amount_credit()));
                    sub.setYear_amount_debit(StringUtil.bigDecimalIsNull(sub.getYear_amount_debit()));
                    sub.setYear_amount_credit(StringUtil.bigDecimalIsNull(sub.getYear_amount_credit()));
                }
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
    public int queryProfitCount(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("vat.queryProfitCount", map);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


}
