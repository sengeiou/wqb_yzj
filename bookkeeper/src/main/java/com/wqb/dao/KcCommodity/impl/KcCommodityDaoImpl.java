package com.wqb.dao.KcCommodity.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.model.InvoiceBody;
import com.wqb.model.KcCommodity;
import com.wqb.model.SubjectMessage;
import com.wqb.model.vomodel.KcCommodityVo;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository("kcCommodityDao")
public class KcCommodityDaoImpl implements KcCommodityDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(KcCommodityDaoImpl.class);

    // 初始数据导入
    @Override
    public int insertCommodity(KcCommodity commodity) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int insert = sqlSession.insert("commodity.insertCommodity", commodity);
            return insert;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    // 查询是库存商品表是否初始导入过数据
    @Override
    public Integer queryCommodity(Map<String, String> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer num = null;
        try {
            num = sqlSession.selectOne("commodity.queryCommodity", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    // 查询库存表所有数据
    @Override
    public List<KcCommodity> queryCommodityAll(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {

            List<KcCommodity> list = sqlSession.selectList("commodity.queryCommodityAll", param);
            //select * from t_kc_commodity where period = #{period} and accountID =#{accountID
            if (list != null && !list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return Collections.emptyList();
    }


    // 查询库存表所有数据
    @Override
    public List<KcCommodity> queryCommodityAll2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<KcCommodity> list = sqlSession.selectList("commodity.queryCommodityAll2", param);
            if (list != null && !list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            throw new BusinessException("查询数量金额表异常");
        } finally {
            sqlSession.close();
        }
        return null;
    }

    // 查询库存表所有数据 一水平名称作为KEY返回map集合
    @Override
    public Map<String, KcCommodity> queryCommodityMap(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<KcCommodity> list = null;
        Map<String, KcCommodity> commMap = new HashMap<>();
        try {
            list = sqlSession.selectList("commodity.queryCommodityAll", param);

            if (list != null && list.size() > 0) {
                for (
                        int i = 0; i < list.size(); i++) {
                    KcCommodity comm = list.get(i);
                    if (comm != null && comm.getComNameSpec() != null) {
                        String comNameSpec = comm.getComNameSpec();
                        commMap.put(comNameSpec, comm); // comNameSpec = 控硅调光电源-100W,24V

                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return commMap;
    }

    @Override
    // 发票子表 根据商品名称规格进行分组 获取本期分组后的商品总金额 和总数量
    public Map<String, InvoiceBody> queryNUmAndMount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<InvoiceBody> list = null;
        Map<String, InvoiceBody> map = new HashedMap<>();
        try {
            list = sqlSession.selectList("commodity.queryNUmAndMount", param);
            if (list != null && list.size() > 0) {
                for (
                        int i = 0; i < list.size(); i++) {
                    InvoiceBody invoiceBody = list.get(i);
                    String comName = invoiceBody.getComName(); // 产品名称 ：调光电源
                    String spec = invoiceBody.getSpec(); // 规格： EUP75T-1H12V-0
                    if (!StringUtil.isEmpty(comName) && isContainChinese(comName)) {
                        if (!StringUtil.isEmptyWithTrim(spec)) {
                            comName = comName + "-" + spec;
                        }
                        map.put(comName, invoiceBody);
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

    // 判断是否包含中文
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    //查询科目根据科目编码
    public List<KcCommodity> queryBysubCode(Map<String, Object> map) throws BusinessException {
        // select * from t_kc_commodity where accountID =#{accountID} and period=#{busDate} and  sub_code=#{subjectID};
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<KcCommodity> list = null;
        try {
            list = sqlSession.selectList("commodity.queryBysubCode", map);
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
    public void updKcByVouch(KcCommodity kcCommodity) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("commodity.updKcByVouch", kcCommodity);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updateCommodity(KcCommodity kcCommodity) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("commodity.updateCommodity", kcCommodity);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    //根据导入的库存编码查询科目
    @Override
    public SubjectMessage querySubByImpoerSubCode(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SubjectMessage sub = null;
        try {
            sub = sqlSession.selectOne("commodity.querySubByImpoerSubCode", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return sub;
    }

    @Override
    //更新库存商品期末结存数量金额单价
    public int updateQmAmountNumPrice(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            int update = sqlSession.update("commodity.updateQmAmountNumPrice", param);
            return update;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    //库存商品列表页
    public List<KcCommodity> queryCommodityList(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<KcCommodity> list = null;
        try {
            list = sqlSession.selectList("commodity.queryCommodityList", param);
            if (list != null && !list.isEmpty()) {
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
    //查询库存列表页总数
    public Integer queryCommodityCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer res = 0;
        try {
            res = sqlSession.selectOne("commodity.queryCommodityCount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        return res;
    }

    //删除下个期间的数据
    @Override
    public void delCommodityAll(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("commodity.delCommodityAll", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    //初始化导入先删除之前的数据再导入
    @Override
    public void delInitCommodity(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("commodity.delInitCommodity", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    //查询二级科目长度
    @Override
    public int querySubLevel(String accountID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", accountID);
        int a = -1;
        try {
            ////select excel_import_code from t_basic_subject_message where  account_id=#{accountID}  and code_level = 2 limit 1
            String ss = sqlSession.selectOne("commodity.querySubLevel", map);
            if (ss != null) {
                return ss.length();
            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            sqlSession.close();
        }
        return a;
    }

    @Override
    public KcCommodity queryCommAmount(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        KcCommodity kc = null;
        try {
            kc = sqlSession.selectOne("commodity.queryCommAmount", map);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            sqlSession.close();
        }
        return kc;
    }

    @Override
    public KcCommodity queryAmountByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        KcCommodity kcc = null;
        try {
            List<KcCommodity> list = sqlSession.selectList("commodity.queryAmountByCondition", param);
            if (list != null & list.size() > 0) {
                kcc = list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return kcc;
    }

    //根据条件查询库存
    @Override
    public List<KcCommodity> queryCommByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            List<KcCommodity> list = sqlSession.selectList("commodity.queryCommByCondition", param);
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
    public List<SubjectMessage> queryExcelCode(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("commodity.queryExcelCode", param);
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

    //期间转移
    @Override
    public int insertCommBath(List<KcCommodity> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.insert("commodity.insertCommBath", list);
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
    public List<SubjectMessage> queryAllSubByImpoerCode(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("commodity.queryAllSubByImpoerCode", param);
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

    //一键删除 清理库存
    @Override
    public int upAllKcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("commodity.upAllKcc", param);
            return update;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    //一键删除 清理科目余额表
    @Override
    public int upAllSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("commodity.upAllSub", param);
            return update;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int delCommodityById(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("commodity.delCommodityById", param);
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public String queryAllSub14(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> list = sqlSession.selectList("commodity.queryAllSub14", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            throw new BusinessException("queryAllSub14 error");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<KcCommodityVo> queryCommodityToVoucher(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<KcCommodityVo> list = sqlSession.selectList("commodity.queryCommodityToVoucher", map);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException("queryCommodityToVoucher error");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<KcCommodity> commodityGenerateVoucher(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<KcCommodity> list = sqlSession.selectList("commodity.commodityGenerateVoucher", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException("commodityGenerateVoucher error");
        } finally {
            sqlSession.close();
        }
    }

}
