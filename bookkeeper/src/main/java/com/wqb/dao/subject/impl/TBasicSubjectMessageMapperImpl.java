package com.wqb.dao.subject.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subBook.SubBookDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.track.dao.TrackCommDao;
import com.wqb.dao.track.dao.TrackSubDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.PageSub;
import com.wqb.model.vomodel.ParentCodeMapping;
import com.wqb.model.vomodel.RedisSub;
import com.wqb.service.vat.VatService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Component
@Service("tBasicSubjectMessageMapper")
public class TBasicSubjectMessageMapperImpl implements TBasicSubjectMessageMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMessageMapperImpl.class);
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    KcCommodityDao kcCommodityDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    SubBookDao subBookDao;
    @Autowired
    TrackCommDao trackCommDao;
    @Autowired
    TrackSubDao trackSubDao;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    VatService vatService;

    /**
     * @param tBasicSubjectMessage
     * @return int 返回类型
     * @Title: addSubMessage
     * @Description: 添加系统科目
     * @date 2017年12月21日 下午3:45:43
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int addSubMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        int num = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            num = sqlSesion.insert("subMessage.addSubMessage", tBasicSubjectMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("subMessage.addSubMessage错误");
        } finally {
            sqlSesion.close();
        }
        return num;
    }

    /**
     * @param tBasicSubjectMessagelist
     * @return int 返回类型
     * @Title: tBasicSubjectMessagelist
     * @Description: 添加系统科目集合
     * @date 2017年12月21日 下午3:46:56
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int addSubMessageList(List<TBasicSubjectMessage> tBasicSubjectMessagelist) {
        SqlSession sqlSesion = null;
        int r = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            r = sqlSesion.insert("subMessage.addSubMessageList", tBasicSubjectMessagelist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return r;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessage
     * @Description: 查询系统中该账套的全部科目
     * @date 2017年12月21日 下午3:53:03
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessage", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageBySubCode
     * @Description: 根据科目编码查询系统中的科目
     * @date 2017年12月21日 下午3:53:50
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageBySubCode(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessageBySubCode", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageBySubName
     * @Description: 根据科目名称查询系统中的科目
     * @date 2017年12月21日 下午3:54:56
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageBySubName(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessageBySubName", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: updateMessage
     * @Description: 更新系统中的科目
     * @date 2017年12月21日 下午3:55:47
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> updateMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.updateMessage", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return int 返回类型
     * @Title: deleteMessageByPrimaryKey
     * @Description: 根据主键删除系统中的科目
     * @date 2018年1月6日 下午12:37:46
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int deleteMessageByPrimaryKey(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        int no = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            no = sqlSesion.delete("subMessage.deleteMessageByPrimaryKey", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return no;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: deleteMessageAll
     * @Description: 删除账套在系统中全部科目
     * @date 2017年12月21日 下午3:57:19
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> deleteMessageAll(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.deleteMessageAll", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: deleteMessageByAcctperiod
     * @Description: 根据帐套id删除系统本期间中的科目
     * @date 2018年7月7日 下午6:11:39
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> deleteMessageByAcctperiod(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.deleteMessageByAcctperiod", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    /**
     * 更新 新增凭证 保存修正凭证 更新科目，还有1405库存商品
     */
    public void chgSubAmountByCreate(Map<String, Object> param, Voucher voucher) throws BusinessException {
        SqlSession sqlSesSion = sqlSessionFactory.openSession();
        try {

            VoucherHead voucherHead = voucher.getVoucherHead();

            List<VoucherBody> list = voucher.getVoucherBodyList();

            List<VoucherBody> listBody = new ArrayList<>();

            List<SubBook> listSubBook = new ArrayList<>();
            List<VoucherBody> arrVB = new ArrayList<>();
            Map<String, SubjectMessage> map1 = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            List<String> map3 = new ArrayList<String>();
            List<String> map4 = new ArrayList<String>();


            Map<String, SubjectMessage> recordingUpSub = new HashMap<>();
            param.put("recordingUpSub", recordingUpSub);

            if (null == param.get("busDate")) {
                param.put("busDate", param.get("period"));
            } else {
                param.put("period", param.get("busDate"));
            }

            if (list != null && list.size() > 0) {
                Map<String, Object> subMap = getSubMess3(list, param, 1);
                if (subMap != null && !subMap.isEmpty()) {
                    map1 = (Map<String, SubjectMessage>) subMap.get("map1");
                    map2 = (Map<String, String>) subMap.get("map2");
                    map3 = (List<String>) subMap.get("map3");
                    map4 = (List<String>) subMap.get("map4");
                }

                String zy = getZy(list);
                if (StringUtil.isEmpty(zy)) {
                    throw new BusinessException(param + "摘要为空");
                }
                param.put("sub_zy", zy);

                for (int i = 0; i < list.size(); i++) {
                    VoucherBody vb = list.get(i);
                    if (vb == null) {
                        continue;
                    }
                    String direction = vb.getDirection().trim();
                    String subjectID = vb.getSubjectID();
                    param.put("direction", direction); // 分录方向
                    param.put("subjectID", subjectID); // 分录科目

                    // 好的成本结转凭证 销项进项生成凭证 不需要操作库存表
                    // 普通库存商品凭证修改（发票生成的进销项凭证） 一键成本结转错误后的凭证修改（发出数量大于库存 没有数量）
                    // 修改都是先删除后新增
                    // if (StringUtil.objEmpty(param.get("cbjz")) &&
                    // StringUtil.objEmpty(param.get("jx_pz")) ){
                    if (StringUtil.objEmpty(param.get("cbjz"))) {
                        if (!StringUtil.isEmptyWithTrim(subjectID)
                                && (subjectID.startsWith("1405") || subjectID.startsWith("1403"))) {
                            List<KcCommodity> kcList = kcCommodityDao.queryBysubCode(param);
                            // 有一重情况该商品有科目 但是库存却没有数据。如果是手动添加的凭证又是进项的话，给它增加一条库存数据
                            if (kcList == null) {
                                if (direction.equals("1")) {
                                    createKcCommdity(param, vb); // 新商品第一次添加才会走这里。比如进项新商品原来没有库存，修改凭证的时候新增科目保存这个凭证。
                                    // 那么就会根据科目创建这个商品库存。还有手工录入凭证，第一添加新商品，创建了一个科目。也会走这里
                                } else if (direction.equals("2")) {
                                    // 比如退货 借 主营业务成本 -15713.1
                                    // 贷 库存商品 - 光机 -8362.15 -395.00 21.17
                                    // 没有期初 本期发出 但是期末都是整正数 可以考虑入库
                                    createKcCommdity2(param, vb);
                                }
                            }
                            if (null != kcList && kcList.size() > 0) {
                                KcCommodity kc = kcList.get(0);
                                // 期初数量
                                Double qc_balanceNum = StringUtil.doubleIsNull(kc.getQc_balanceNum());
                                // 期初金额
                                BigDecimal qc_balanceAmount = StringUtil.bigDecimalIsNull(kc.getQc_balanceAmount());
                                // 本期收入数量
                                Double bq_incomeNum = StringUtil.doubleIsNull(kc.getBq_incomeNum());
                                // 本期收入金额
                                BigDecimal bq_incomeAmount = StringUtil.bigDecimalIsNull(kc.getBq_incomeAmount());
                                // 本期发出数量
                                Double bq_issueNum = StringUtil.doubleIsNull(kc.getBq_issueNum());
                                // 本期发出金额
                                BigDecimal bq_issueAmount = StringUtil.bigDecimalIsNull(kc.getBq_issueAmount());

                                BigDecimal bq_issuePrice = StringUtil.bigDecimalIsNull(kc.getBq_issuePrice());

                                // 本年累计收入数量
                                Double total_incomeNum = StringUtil.doubleIsNull(kc.getTotal_incomeNum());
                                // 本年累计收入金额
                                BigDecimal total_incomeAmount = StringUtil.bigDecimalIsNull(kc.getTotal_incomeAmount());
                                // 本年累计发出数量
                                Double total_issueNum = StringUtil.doubleIsNull(kc.getTotal_issueNum());
                                // 本年累计发出金额
                                BigDecimal total_issueAmount = StringUtil.bigDecimalIsNull(kc.getTotal_issueAmount());
                                // 期末结存数量
                                Double qm_balanceNum = StringUtil.doubleIsNull(kc.getQm_balanceNum());
                                // 期末结存单价
                                BigDecimal qm_balancePrice = StringUtil.bigDecimalIsNull(kc.getQm_balancePrice());
                                // 期末结存金额
                                BigDecimal qm_balanceAmount = StringUtil.bigDecimalIsNull(kc.getQm_balanceAmount());

                                Map<String, Object> commmap = new HashMap<>();

                                Object importVo = param.get("importVo"); // 是否有导入凭证

                                // 1贸易型的
                                // 库存商品购入的时候在借方
                                // 库存商品结转的时候在借方
                                // 2生产型
                                // 库存商品购入的时候在借方
                                // 库存商品完工的时候在借方
                                // 库存商品结转的时候在贷方
                                /************** 库存商品 start ********************/

                                if (subjectID.startsWith("1405")) {
                                    if ("1".equals(direction)) {
                                        // ** 凭证录入的数量 **
                                        Double number = StringUtil.doubleIsNull(vb.getNumber());
                                        // ** 凭证金额 **
                                        Double amount = StringUtil.doubleIsNull(vb.getDebitAmount());

                                        Double bq_incomeNum_new = bq_incomeNum + number;
                                        BigDecimal bq_incomeAmount_new = bq_incomeAmount.add(new BigDecimal(amount));

                                        /*** 增加凭证数量 ****/
                                        kc.setBq_incomeNum(bq_incomeNum_new);
                                        /** 增加凭证金额 ***/
                                        kc.setBq_incomeAmount(bq_incomeAmount_new);

                                        /*** 计算期末结余数量 ****/
                                        Double qm_balanceNum_new = qm_balanceNum + number;
                                        /*** 计算期末结余金额 ****/
                                        BigDecimal qm_balanceAmount_new = qm_balanceAmount.add(new BigDecimal(amount));

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        kc.setTotal_incomeNum(total_incomeNum + number);
                                        kc.setTotal_incomeAmount(total_incomeAmount.add(new BigDecimal(amount)));

                                        kc.setBalance_direction(StringUtil.comDir(qm_balanceAmount_new)); // 库存商品借贷方向

                                        kcCommodityDao.updKcByVouch(kc); // 更新
                                        // commmap.put("amount", amount);

                                    } else if ("2".equals(direction)) {
                                        // 1 处理是成本结转这种情况 addCbjz手工添加 modiyCbjz编辑
                                        // importCbjz导入
                                        if (param.get("addCbjz") != null || param.get("modiyCbjz") != null
                                                || param.get("importCbjz") != null) {
                                            // ** 凭证录入的数量 **
                                            Double number = StringUtil.doubleIsNull(vb.getNumber());
                                            // ** 凭证借方金额 **
                                            Double amount = StringUtil.doubleIsNull(vb.getCreditAmount());
                                            Double price = StringUtil.doubleIsNull(vb.getPrice()); // 成本单价

                                            /************** satrt ********************/

                                            BigDecimal cb = new BigDecimal(amount); // 成本结转凭证修改
                                            // 凭证的金额即是这个商品的成本

                                            Double qm_balanceNum_new = qm_balanceNum - number; /*** 期末结余数量 ****/
                                            BigDecimal qm_balanceAmount_new = qm_balanceAmount
                                                    .subtract(cb); /*** 期末结余金额 ****/

                                            kc.setQm_balanceNum(qm_balanceNum_new);
                                            kc.setQm_balanceAmount(qm_balanceAmount_new);

                                            kc.setBq_issueNum(number);
                                            kc.setBq_issueAmount(cb);

                                            kc.setTotal_issueNum(total_issueNum + number);
                                            kc.setTotal_issueAmount(total_issueAmount.add(cb));

                                            kc.setQm_balancePrice(
                                                    StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                            // commmap.put("amount", amount); //
                                            // 记录库存
                                            kcCommodityDao.updKcByVouch(kc);

                                        } else {

                                            // 2 处理是不是成本结转这种情况

                                            // ** 凭证录入的销售数量 **
                                            Double number = StringUtil.doubleIsNull(vb.getNumber());
                                            // ** 凭证贷方销售金额 **
                                            BigDecimal amount = new BigDecimal(
                                                    StringUtil.doubleIsNull(vb.getCreditAmount()));
                                            Double price = StringUtil.doubleIsNull(vb.getPrice()); // 销售单价

                                            if (amount.compareTo(new BigDecimal(0)) >= 0.0) {
                                                System.out.println("销售 ");
                                            } else {
                                                System.out.println("退货 ");
                                            }

                                            /************** satrt ********************/
                                            Double qm_balanceNum_new = qm_balanceNum - number; /*** 新的期末结余数量 ****/
                                            BigDecimal qm_balanceAmount_new = qm_balanceAmount
                                                    .subtract(amount); /*** 新的期末结余金额 ****/

                                            kc.setQm_balanceNum(qm_balanceNum_new);
                                            kc.setQm_balanceAmount(qm_balanceAmount_new);

                                            Double bq_issueNum_new = bq_issueNum + number;
                                            BigDecimal bq_issueAmount_new = bq_issueAmount.add(amount);

                                            kc.setBq_issueNum(bq_issueNum_new); /*** 新的发出数量 ****/
                                            kc.setBq_issueAmount(bq_issueAmount_new); /*** 新的发出金额 ****/

                                            kc.setTotal_issueNum(total_issueNum + number);
                                            kc.setTotal_issueAmount(total_issueAmount.add(amount));

                                            kc.setQm_balancePrice(
                                                    StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                            kcCommodityDao.updKcByVouch(kc);
                                        }
                                    }
                                }
                                // 2原材料
                                /************** 原材料 start ********************/
                                if (subjectID.startsWith("1403")) {
                                    // 购入 或者 手工添加以存在库存的
                                    if ("1".equals(direction)) {
                                        // ** 凭证录入的数量 **
                                        Double number = StringUtil.doubleIsNull(vb.getNumber());
                                        /** 凭证金额 **/
                                        Double amount = StringUtil.doubleIsNull(vb.getDebitAmount());
                                        Double price = StringUtil.doubleIsNull(vb.getPrice());

                                        Double bq_incomeNum_new = bq_incomeNum + number;
                                        BigDecimal bq_incomeAmount_new = bq_incomeAmount.add(new BigDecimal(amount));

                                        kc.setBq_incomeNum(bq_incomeNum_new); /*** 增加凭证数量 ****/
                                        kc.setBq_incomeAmount(bq_incomeAmount_new); /** 增加凭证金额 ***/
                                        kc.setBq_incomePrice(new BigDecimal(price));

                                        Double qm_balanceNum_new = qm_balanceNum + number;
                                        BigDecimal qm_balanceAmount_new = qm_balanceAmount.add(new BigDecimal(amount));

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        kc.setTotal_incomeNum(total_incomeNum + number);
                                        kc.setTotal_incomeAmount(total_incomeAmount.add(new BigDecimal(amount)));

                                        kc.setBalance_direction(StringUtil.comDir(qm_balanceAmount_new));

                                        kcCommodityDao.updKcByVouch(kc); // 更新
                                    } else if ("2".equals(direction)) {
                                        // 领料
                                        // ** 凭证录入的数量 **
                                        Double number = StringUtil.doubleIsNull(vb.getNumber());
                                        // ** 凭证借方金额 **
                                        BigDecimal amount = new BigDecimal(
                                                StringUtil.doubleIsNull(vb.getCreditAmount()));

                                        // 计算原材料本期发出数量与金额
                                        Double bq_issueNum_new = bq_issueNum + number;
                                        BigDecimal bq_issueAmount_new = bq_issueAmount.add(amount);

                                        kc.setBq_issueNum(bq_issueNum_new);
                                        kc.setBq_issueAmount(bq_issueAmount_new);

                                        // 计算本期期末结余数量与金额
                                        Double qm_balanceNum_new = qm_balanceNum - number;
                                        BigDecimal qm_balanceAmount_new = qm_balanceAmount.subtract(amount);

                                        kc.setQm_balanceNum(qm_balanceNum_new); // 期末结余数量
                                        kc.setQm_balanceAmount(qm_balanceAmount_new); // 期末结余金额

                                        kc.setTotal_issueNum(total_issueNum + number);
                                        kc.setTotal_issueAmount(total_issueAmount.add(amount));

                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        kcCommodityDao.updKcByVouch(kc);
                                        // commmap.put("amount", amount); //
                                        // 记录库存
                                    }
                                }
                            }
                        }
                    }

                    if ("1".equals(direction)) {
                        param.put("amount", vb.getDebitAmount());
                    } else if ("2".equals(direction)) {
                        param.put("amount", vb.getCreditAmount());
                    }
                    if (StringUtil.isEmpty(subjectID)) {
                        throw new BusinessException(subjectID + "--" + vb.getVcabstact() + " 科目为空");
                    }

                    if (param.get("amount") == null || ((Double) param.get("amount")) == 0.0) {
                        continue;
                    }

                    if (map2.isEmpty()) {
                        throw new BusinessException(subjectID + "chgSubAmountByCreate:map2 未查询到科目");
                    }

                    param.put("account_id", param.get("accountID"));
                    param.put("account_period", param.get("busDate"));

                    Integer subLength = subjectID.trim().length();
                    Integer jc = (subLength - 4) / 3;
                    arrVB.add(vb); // 凭证分录集合
                    Map<String, Map<String, Object>> balanceMap = new HashMap<>();
                    for (int j = 0; j <= jc; j++) {
                        String curSubjectID = subjectID.substring(0, subLength - 3 * j);
                        SubjectMessage sub = map1.get(curSubjectID);
                        if (sub != null) {
                            Map<String, Object> reMap = updateSubjest(sub, param);
                            map1.remove(curSubjectID);
                            balanceMap.put(curSubjectID, reMap);
                        } else {
                            String pk_id = map2.get(curSubjectID);
                            if (!StringUtil.isEmpty(pk_id)) {
                                SubjectMessage subMessage = vatDao.queryByID(pk_id);
                                Map<String, Object> reMap = updateSubjest(subMessage, param);
                                balanceMap.put(curSubjectID, reMap);
                            } else {
                                throw new BusinessException(
                                        "chgSubAmountByCreate  String pk_id=map2.get(curSubjectID) pk_id is null code="
                                                + curSubjectID);
                            }
                        }
                    }
                    addSubBook(vb, param, listSubBook, voucherHead, balanceMap);
                }
                Object objs = param.get("updVoucher");
                if (objs == null && !listSubBook.isEmpty()) {
                    subBookDao.insertSubBookBath(listSubBook);
                } else if (objs != null && objs.toString().equals("updVoucher")) {
                    if (!arrVB.isEmpty()) {
                        detailAccountModify(param, voucherHead, arrVB, map2);
                    }
                }

                upCaheSub(recordingUpSub, param.get("accountID").toString(), param.get("busDate").toString());


                if (param.get("rtSub") != null && param.get("rtSub").equals("saveVoucher")) {
                    rtChgSubToPage(arrVB, param, recordingUpSub);
                }


                param.remove("amount");
                param.remove("sub_zy");
                param.remove("subjectID");
                param.remove("direction");
                param.remove("updVoucher");
                param.remove("recordingUpSub");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesSion.close();
        }
    }

    private void rtChgSubToPage(List<VoucherBody> arrVB, Map<String, Object> param,
                                Map<String, SubjectMessage> recordingUpSub) throws BusinessException {

        if (recordingUpSub == null || recordingUpSub.isEmpty()) {
            throw new BusinessException("chgSubAmountByCreate rtChgSubToPage srecordingUpSub==null");
        }
        if (arrVB == null || arrVB.isEmpty()) {
            throw new BusinessException("chgSubAmountByCreate rtChgSubToPage arrVB==null");
        }
        ParentCodeMapping parentCodeMapping = new ParentCodeMapping();

        List<PageSub> arr = new ArrayList<>();
        BigDecimal zero1 = new BigDecimal(0);
        for (VoucherBody vb : arrVB) {
            String code = vb.getSubjectID();
            SubjectMessage subjectMessage = recordingUpSub.get(code);
            if (subjectMessage == null) {
                throw new BusinessException("chgSubAmountByCreate rtChgSubToPage subjectMessage==null  code=" + code);
            }
            PageSub pageSub = new PageSub();
            pageSub.setPkSubId(subjectMessage.getPk_sub_id());
            pageSub.setSubCode(subjectMessage.getSub_code());
            pageSub.setSubName(subjectMessage.getSub_name());
            pageSub.setFullName(subjectMessage.getFull_name());
            pageSub.setEndingBalanceCredit(subjectMessage.getEnding_balance_credit() == null ? zero1
                    : subjectMessage.getEnding_balance_credit());
            pageSub.setEndingBalanceDebit(subjectMessage.getEnding_balance_debit() == null ? zero1
                    : subjectMessage.getEnding_balance_debit());
            pageSub.setDir(parentCodeMapping.getDir(subjectMessage.getSub_code()));
            arr.add(pageSub);
        }
        param.put("saveVoucherToPage", arr);
    }

    private void upCaheSub(Map<String, SubjectMessage> recordingUpSub, String accountID, String period)
            throws Exception {

        List<RedisSub> cacheList = vatService.getAllSub(accountID, period);
        if (cacheList == null || cacheList.isEmpty()) {
            throw new BusinessException("chgSubAmountByCreate upCaheSub vatService.getRedisSubMessList is null");
        }
        List<RedisSub> chgSubList = StringUtil.subjectMessageToredisSub(recordingUpSub.values());
        cacheList.removeAll(chgSubList);
        cacheList.addAll(chgSubList);
        String str = vatService.setCahceSub(accountID, period, cacheList);
        if (vatService.getChg() == false) {
            vatService.setChg(true);
        }
    }


    @SuppressWarnings("unused")
    private void addSubBook(VoucherBody vb, Map<String, Object> param, List<SubBook> subBookList, VoucherHead vh,
                            Map<String, Map<String, Object>> balanceMap) throws BusinessException {

        String vouchID = vh.getVouchID();
        Integer voucherNO = vh.getVoucherNO();

        String vouchAID = vb.getVouchAID();
        String sub_code = vb.getSubjectID();
        String dir = vb.getDirection();

        Double amount = (Double) param.get("amount");
        BigDecimal bg_amount = new BigDecimal(amount);

        String accountID = (String) param.get("accountID");
        String period = (String) param.get("busDate");
        String zy = (String) param.get("sub_zy");

        int len = sub_code.length();

        for (int i = 0; i < len / 3; i++) {

            String curr_code = sub_code.substring(0, 4 + (i * 3));

            Map<String, Object> map = balanceMap.get(curr_code);
            // up_code 从updateSubjest 传递过来的科目编码 此编码一定与curr_code 相同
            String up_code = (String) map.get("up_code");

            if (!up_code.equals(curr_code)) {
                String sss = "Class: " + this.getClass().getName() + "method: "
                        + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:"
                        + Thread.currentThread().getStackTrace()[1].getLineNumber();
                // throw new
                // BusinessException(sss+"--"+sub_code+"vouchID:"+vouchID+",摘要"+zy);
            }

            SubBook subBook = new SubBook();

            String uuid = UUIDUtils.getUUID();


            subBook.setAccountID(accountID);
            subBook.setPeriod(period);

            subBook.setVouchID(vouchID);

            subBook.setVouchAID(vouchAID);

            subBook.setVouchNum(voucherNO);

            subBook.setVcabstact(zy);


            subBook.setSub_code(curr_code);


            String sub_name = (String) map.get("blanceSubName");
            subBook.setSub_name(sub_name);

            BigDecimal blanceAmount = (BigDecimal) map.get("blanceAmount");
            subBook.setBlanceAmount(blanceAmount);


            Integer sub_dir = (Integer) map.get("blanceDir");
            subBook.setDirection(String.valueOf(sub_dir));


            if (dir.equals("1")) {
                subBook.setDebitAmount(bg_amount);
                subBook.setCreditAmount(null);
            } else {
                subBook.setDebitAmount(null);
                subBook.setCreditAmount(bg_amount);
            }


            subBook.setUpdateDate(new Date());

            subBook.setUp_date(System.currentTimeMillis());

            if (curr_code.equals(sub_code)) {
                subBook.setIsEndSubCode(1);
            } else {
                subBook.setIsEndSubCode(0);
            }

            subBookList.add(subBook);

        }

    }

    @SuppressWarnings("unused")
    private Map<String, Object> getSubMess3(List<VoucherBody> list, Map<String, Object> param, int tp)
            throws BusinessException {
        try {

            String methodStr = tp == 1 ? "chgSubAmountByCreate" : "chgSubAmountByDelete";

            List<String> vbCodeList = new ArrayList<>();

            Set<String> set01 = new HashSet<>();
            for (VoucherBody vb : list) {
                if (vb != null) {
                    List<String> codes = StringUtil.getCodes(vb.getSubjectID());
                    vbCodeList.add(vb.getSubjectID());
                    set01.addAll(codes);
                }
            }

            if (set01.isEmpty()) {
                throw new BusinessException(methodStr + " getSubMess3 set01.isEmpty()");
            }

            String accountID = (String) param.get("accountID");
            String busDate = (String) param.get("busDate");
            List<String> codeList = new ArrayList<>();
            codeList.addAll(set01);

            List<RedisSub> redisSub_list = vatService.getAllSub(accountID, busDate);
            if (redisSub_list == null) {
                throw new BusinessException(methodStr + " getSubMess3 vatService.getRedisSubMessList is null");
            }

            List<SubjectMessage> listSub = StringUtil.redisSubTosubjectMessage(redisSub_list);

            for (String oneCode : codeList) {
                boolean flg = false;
                for (SubjectMessage oneSub : listSub) {
                    String sub_code1 = oneSub.getSub_code();
                    if (oneCode.equals(sub_code1)) {
                        flg = true;
                        break;
                    }
                }

                if (flg == false) {
                    throw new BusinessException(methodStr + " getSubMess3 oneCode [ " + oneCode + "]not in listSub");
                }
            }

            Map<String, SubjectMessage> map1 = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            Collections.sort(codeList);


            Map<String, Object> retMap = new HashMap<>();

            for (SubjectMessage sub : listSub) {
                String sub_code = sub.getSub_code();
                String pk_sub_id = sub.getPk_sub_id();
                map1.put(sub_code, sub);
                map2.put(sub_code, pk_sub_id);
            }
            retMap.put("map1", map1);
            retMap.put("map2", map2);
            retMap.put("map3", codeList);
            retMap.put("map4", vbCodeList);

            return retMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @SuppressWarnings("unused")
    private Map<String, Object> getSubMess(List<VoucherBody> list, Map<String, Object> param) throws BusinessException {
        try {
            if (list == null || (list != null && list.size() == 0))
                return null;
            Set<String> set01 = new HashSet<>();
            for (VoucherBody vb : list) {
                if (vb == null)
                    // 固定资产凭证未检查 凭证有空的
                    continue;
                String sub_code = vb.getSubjectID();
                if (!StringUtil.isEmpty(sub_code)) {
                    int len = sub_code.length();
                    for (int i = 0; i < len / 3; i++) {
                        String new_code = sub_code.substring(0, 4 + (i * 3));
                        set01.add(new_code);
                    }
                }
            }
            List<SubjectMessage> listSub = null;
            if (!set01.isEmpty()) {
                List<String> codeList = new ArrayList<>();
                codeList.addAll(set01);
                Map<String, Object> map = new HashMap<>();
                map.put("codes", codeList);
                map.put("accountID", param.get("accountID"));
                map.put("period", param.get("busDate"));
                listSub = vatDao.querySubByVo(map);
            }

            Map<String, SubjectMessage> map1 = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            Map<String, Object> hashMap = new HashMap<>();

            if (listSub != null && listSub.size() > 0) {
                for (SubjectMessage sub : listSub) {
                    String sub_code = sub.getSub_code();
                    String pk_sub_id = sub.getPk_sub_id();
                    map1.put(sub_code, sub);
                    map2.put(sub_code, pk_sub_id);
                }
                hashMap.put("map1", map1);
                hashMap.put("map2", map2);
                return hashMap;
            }

            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    //// 有科目 没有库存的 退货 处理 注意 在贷方
    @SuppressWarnings("unused")
    private void createKcCommdity2(Map<String, Object> param, VoucherBody vb) throws BusinessException {
        /*** 来自凭证数量 ***/
        Double nnumber = StringUtil.doubleIsNull(vb.getNumber());
        /*** 来自凭证贷方金额 ***/
        Double namount = StringUtil.doubleIsNull(vb.getCreditAmount());
        /*** 来自凭证的价格 ***/
        Double price = vb.getPrice(); // 价格

        if (namount >= 0) {
            // 没有库存 , 发出数量又大于0, 这个凭证肯定有问题
            System.out.println("大于0");
            System.out.println("错误");
            throw new BusinessException(vb.getVcsubject() + "库存数量为0,请先添加库存");
        } else {
            // 对于本期发生数量小于0的凭证，如果是导入凭证会出现问题。 需要判断期末结余 数量 和 金额 ，如果是大于0可以放行。小于0 不允许
            // 如果不是导入 凭证创建会自动检查，让客户自己修正
            System.out.println("正确"); // 期末大于0
            System.out.println("小于0 的情况 比如退货");
        }

        if (nnumber >= 0) {
            System.out.println("大于0");
            throw new BusinessException(vb.getVcsubject() + "库存数量为0,请先添加库存");
        } else {
            // 金额小于0 那么数量肯定小于0
            System.out.println("小于0 的情况 比如退货");
        }
        String companyType = String.valueOf(param.get("companyType")); // 公司性质：
        // 1生产型
        // ，2服务型

        String subjectID = vb.getSubjectID();
        Map<String, Object> map = new HashMap<>();
        map.put("sub_code", subjectID);
        map.put("account_id", param.get("accountID"));
        map.put("account_period", param.get("busDate"));
        SubjectMessage sub = vatDao.querySubByCode(map);
        if (sub == null) {
            // 凭证有科目编码但是在科目表却没找到 不正常
            return;
        }
        // 新增库存商品设置
        String fullSubName = sub.getFull_name(); // 库存商品 _恒压调光电源 _MUL
        // String jq_SubName = fullSubName.substring(5);

        Integer code_level = sub.getCode_level();
        String arr[] = fullSubName.split("_");

        if (code_level > 3) {
            throw new BusinessException("createKcCommdity2" + subjectID + "-" + fullSubName + "不能超过三级");
        }
        /****** 商品名称 ********/
        String comNameSpec = "";

        // 定义库存对象
        KcCommodity comm = new KcCommodity();
        comm.setComID(UUIDUtils.getUUID()); // 主键
        comm.setAccountID(param.get("accountID").toString()); // 账套ID
        comm.setPeriod(param.get("busDate").toString()); // 做账期间
        comm.setStartPeriod(null); // 起始期间
        comm.setEndPeriod(null); // 截止期间
        comm.setImportSubcode(null); // 导入的科目代码

        comm.setSub_comName(sub.getFull_name()); // 科目名称 是全名称 库存商品_彩电_C450

        // 设置商品名字与规格字段
        if (arr.length == 2) { // 二级 arr[0] arr[1]
            comm.setComName(arr[1]);
            comm.setSpec(null);
            comNameSpec = arr[1];
        }

        if (arr.length == 3 && code_level == 3) { // 三级 //二级 arr[0] arr[1]
            // arr[2]
            comm.setComName(arr[1]);
            comm.setSpec(arr[2]);
            comNameSpec = arr[1] + "-" + arr[2];
        }
        // 库存商品_库存商品_超薄风管式温湿平衡型 S11_FQRP71AAPN
        if (arr.length == 4 && code_level == 3) { // 特殊情况 商品里面包含下划线‘_’.
            comm.setComName(arr[1]);
            String full_name = sub.getFull_name(); // 库存商品_库存商品_超薄风管式温湿平衡型
            // S11_FQRP71AAPN
            int indexOf = full_name.indexOf("_", 5); // 从第二个 下划线 _ 一直截取到最后面 ->
            // 得到 '库存商品_库存商品_'
            String pec = full_name.substring(indexOf + 1); // 在截取 超薄风管式温湿平衡型
            // S11_FQRP71AAPN
            comm.setSpec(pec);
            comNameSpec = arr[1] + "-" + pec;
        }

        comm.setDirection(null);// 借贷方向
        comm.setQc_balanceNum(0.0); // 期初结存数量
        comm.setQc_balancePrice(new BigDecimal(0));// 期初结存单价
        comm.setQc_balanceAmount(new BigDecimal(0));// 期初结存金额

        comm.setCreatePsnID(null);
        comm.setCreateDate(new Date());
        comm.setCreatePsn(null);
        comm.setUpdatePsnID(null);
        comm.setUpdatedate(new Date());
        comm.setUpdatePsn(null);
        comm.setDes(sub.getSub_name());// 说明备注
        comm.setImportDate(new Date());// 导入时间
        comm.setBalanceDate(null);// 结转时间
        comm.setVcunit(null);// 计量单位

        comm.setBq_issueNum(nnumber);// 本期发出数量
        comm.setBq_issueAmount(new BigDecimal(namount));// 本期发出金额
        comm.setBq_issuePrice(price == null ? null : new BigDecimal(price));
        comm.setTotal_issueNum(nnumber); // 本年累计发出数量
        comm.setTotal_issueAmount(new BigDecimal(namount));// 本年累计发出金额

        /***** 商品名称规格 *******/
        comm.setComNameSpec(comNameSpec);

        comm.setBq_incomeNum(0.0);// 本期收入数量
        comm.setBq_incomeAmount(new BigDecimal(0));// 本期收入金额
        comm.setBq_incomePrice(new BigDecimal(0)); // 本期收入价格

        comm.setTotal_incomeNum(0.0);// 本年累计收入数量
        comm.setTotal_incomeAmount(new BigDecimal(0));// 本年累计收入金额

        comm.setBalance_direction(null); // 余额借贷方向

        double num = Math.abs(nnumber); // 把负数变为正数
        comm.setQm_balanceNum(num); // 期末结存数量

        BigDecimal amo = new BigDecimal(Math.abs(namount)); // 把负数变为正数
        comm.setQm_balanceAmount(amo);// 期末结存金额

        // 期末结存单价
        comm.setQm_balancePrice(StringUtil.bigDivide(amo, num));

        comm.setSub_code(vb.getSubjectID()); // ** 挂科目 **

        // 更新到库存表
        kcCommodityDao.insertCommodity(comm);

    }

    // 有科目 没有库存的进项 处理
    private void createKcCommdity(Map<String, Object> param, VoucherBody vb) throws BusinessException {

        /*** 来自凭证数量 ***/
        Double nnumber = StringUtil.doubleIsNull(vb.getNumber());
        /*** 来自凭证金额 ***/
        Double namount = StringUtil.doubleIsNull(vb.getDebitAmount());
        /*** 来自凭证的价格 ***/
        Double price = vb.getPrice(); // 价格

        String companyType = String.valueOf(param.get("companyType")); // 公司性质：
        // 1生产型
        // ，2服务型

        String subjectID = vb.getSubjectID();
        Map<String, Object> map = new HashMap<>();
        map.put("sub_code", subjectID);
        map.put("account_id", param.get("accountID"));
        map.put("account_period", param.get("busDate"));
        SubjectMessage sub = vatDao.querySubByCode(map);
        if (sub == null)
            return;
        // comm.setSub_comName("库存商品_"+comNameSpec.replace("@#&", "_"));
        // 新增库存商品设置
        String fullSubName = sub.getFull_name(); // 库存商品 _恒压调光电源 _MULtidim-15V-0
        // String jq_SubName = fullSubName.substring(5);
        Integer code_level = sub.getCode_level();

        String arr[] = fullSubName.split("_");
        // 库存商品_库存商品_超薄风管式温湿平衡型 S11_FQRP71AAPN eroor
        if (code_level > 3) {
            throw new BusinessException("createKcCommdity--" + subjectID + "_" + fullSubName + "不能超过三级");
        }
        /****** 商品名称 ********/
        String comNameSpec = "";

        // 定义库存对象
        KcCommodity comm = new KcCommodity();
        comm.setComID(UUIDUtils.getUUID()); // 主键
        comm.setAccountID(param.get("accountID").toString()); // 账套ID
        comm.setPeriod(param.get("busDate").toString()); // 做账期间
        comm.setStartPeriod(null); // 起始期间
        comm.setEndPeriod(null); // 截止期间
        comm.setImportSubcode(null); // 导入的科目代码

        comm.setSub_comName(sub.getFull_name()); // 科目名称 是全名称 库存商品_彩电_C450

        // 设置商品名字与规格字段
        if (arr.length == 2) { // 二级 arr[0] arr[1]
            comm.setComName(arr[1]);
            comm.setSpec(null);
            comNameSpec = arr[1];
        }

        if (arr.length == 3 && code_level == 3) { // 三级 //二级 arr[0] arr[1]
            // arr[2]
            comm.setComName(arr[1]);
            comm.setSpec(arr[2]);
            comNameSpec = arr[1] + "-" + arr[2];
        }
        // 库存商品_库存商品_超薄风管式温湿平衡型 S11_FQRP71AAPN
        if (arr.length == 4 && code_level == 3) { // 特殊情况 商品里面包含下划线‘_’.
            comm.setComName(arr[1]);
            String full_name = sub.getFull_name(); // 库存商品_库存商品_超薄风管式温湿平衡型
            // S11_FQRP71AAPN
            int indexOf = full_name.indexOf("_", 5); // 从第二个 下划线 _ 一直截取到最后面 ->
            // 得到 '库存商品_库存商品_'
            String pec = full_name.substring(indexOf + 1); // 在截取 超薄风管式温湿平衡型
            // S11_FQRP71AAPN
            comm.setSpec(pec);
            comNameSpec = arr[1] + "-" + pec;
        }

        comm.setDirection(null);// 借贷方向
        comm.setQc_balanceNum(0.0); // 期初结存数量
        comm.setQc_balancePrice(new BigDecimal(0));// 期初结存单价
        comm.setQc_balanceAmount(new BigDecimal(0));// 期初结存金额
        comm.setCreatePsnID(null);
        comm.setCreateDate(new Date());
        comm.setCreatePsn(null);
        comm.setUpdatePsnID(null);
        comm.setUpdatedate(new Date());
        comm.setUpdatePsn(null);
        comm.setDes(sub.getSub_name() + "凭证补录新增");// 说明备注
        comm.setImportDate(new Date());// 导入时间
        comm.setBalanceDate(null);// 结转时间
        comm.setVcunit(null);// 计量单位

        comm.setBq_issueNum(null);// 本期发出数量
        comm.setBq_issueAmount(null);// 本期发出金额
        comm.setBq_issuePrice(null);

        /***** 商品名称规格 *******/
        comm.setComNameSpec(comNameSpec);

        comm.setBq_incomeNum(nnumber);// 本期收入数量
        comm.setBq_incomeAmount(new BigDecimal(namount));// 本期收入金额
        comm.setBq_incomePrice(price == null ? null : new BigDecimal(price)); // 本期收入价格

        comm.setTotal_incomeNum(nnumber);// 本年累计收入数量
        comm.setTotal_incomeAmount(new BigDecimal(namount));// 本年累计收入金额

        comm.setTotal_issueNum(0.0); // 本年累计发出数量
        comm.setTotal_issueAmount(new BigDecimal(0));// 本年累计发出金额

        comm.setBalance_direction(null); // 余额借贷方向

        comm.setQm_balanceNum(nnumber); // 期末结存数量
        comm.setQm_balanceAmount(new BigDecimal(namount));// 期末结存金额

        // 期末结存单价
        comm.setQm_balancePrice(StringUtil.bigDivide(new BigDecimal(namount), nnumber));

        comm.setSub_code(vb.getSubjectID()); // ** 挂科目 **

        // 更新到库存表
        kcCommodityDao.insertCommodity(comm);

        // 一键生成凭证 更新进项入库
        if (subjectID.startsWith("1405")) {
            // upCommJx(param, comm);
        }

    }

    // 修改条件 一键生成凭证并且是进项修改
    private void upCommJx(Map<String, Object> param, KcCommodity kc) throws BusinessException {
        Map<String, Object> m1 = new HashMap<>();
        m1.put("busDate", param.get("busDate"));
        m1.put("accountID", param.get("accountID"));
        List<StatusPeriod> statuList = periodStatusDao.queryStatus(m1);
        // select * from t_status_period where period=#{busDate} and
        // accountID=#{accountID};
        if (statuList != null && statuList.size() > 0) {
            Integer isCreateVoucher = statuList.get(0).getIsCreateVoucher();
            if (isCreateVoucher != null && isCreateVoucher == 1)
                if (kc.getBq_issueAmount() == null
                        || (kc.getBq_issueAmount() != null && kc.getBq_issueAmount().compareTo(BigDecimal.ZERO) == 0)) {
                    // 根据商品名称查询是否有进项，如果有进项，并且进项有没有入库的话，那么可以直接把该销项商品入库
                    // 这样做的好处是可以减少成本结转的时候找不到销项从而需要再次去修正的问题
                    m1.put("comNameSpec", kc.getComNameSpec());
                    m1.put("invoiceType", "2");
                    List<InvoiceBody> listBody = invoiceDao.queryAmountByComName(m1);
                    if (listBody != null && listBody.size() > 0) {
                        InvoiceBody invoiceBody = listBody.get(0);
                        if (invoiceBody == null)
                            return;
                        Double nnumber = invoiceBody.getNnumber();
                        Double namount = invoiceBody.getNamount();
                        KcCommodity k1 = new KcCommodity();
                        k1.setComID(kc.getComID());
                        if (nnumber != null && namount != null) {
                            BigDecimal total_issueAmount = StringUtil.bigDecimalIsNull(kc.getTotal_issueAmount());
                            Double total_issueNum = StringUtil.doubleIsNull(kc.getTotal_issueNum());

                            k1.setBq_issueNum(nnumber);
                            k1.setBq_issueAmount(new BigDecimal(namount));

                            k1.setTotal_issueNum(total_issueNum + nnumber);
                            k1.setTotal_issueAmount(total_issueAmount.add(new BigDecimal(namount)));
                            if (nnumber != null && nnumber != 0.0) {
                                double a = namount / nnumber;
                                k1.setBq_issuePrice(new BigDecimal(a));
                            }
                            kcCommodityDao.updateCommodity(k1);
                        }
                    }
                }
        }
    }

    @Override
    public Map<String, Object> updateSubjest(SubjectMessage sub, Map<String, Object> map) throws BusinessException {
        Map<String, Object> subMap = new HashMap<>();
        BigDecimal amount = new BigDecimal((Double) map.get("amount"));
        // amount = amount.setScale(8, BigDecimal.ROUND_UP);

        SubjectMessage new_SubMesage = StringUtil.copySubMesage(sub);

        if (map.get("direction").toString().equals("1")) {
            BigDecimal current_amount_debit;
            if (sub.getCurrent_amount_debit() == null) {
                current_amount_debit = new BigDecimal(0);
            } else {
                current_amount_debit = StringUtil.bigDecimalIsNull(sub.getCurrent_amount_debit());
            }
            subMap.put("current_amount_debit", amount.add(current_amount_debit));
            BigDecimal ending_balance_debit = StringUtil.bigDecimalIsNull(sub.getEnding_balance_debit());
            subMap.put("ending_balance_debit", amount.add(ending_balance_debit));
            BigDecimal ending_balance_credit = StringUtil.bigDecimalIsNull(sub.getEnding_balance_credit());
            subMap.put("ending_balance_credit", ending_balance_credit);
            BigDecimal year_amount_debit = StringUtil.bigDecimalIsNull(sub.getYear_amount_debit());
            subMap.put("year_amount_debit", amount.add(year_amount_debit));

            new_SubMesage.setCurrent_amount_debit(amount.add(current_amount_debit));
            new_SubMesage.setYear_amount_debit(amount.add(year_amount_debit));

        } else {
            BigDecimal current_amount_credit = StringUtil.bigDecimalIsNull(sub.getCurrent_amount_credit());
            subMap.put("current_amount_credit", amount.add(current_amount_credit));
            BigDecimal ending_balance_credit = StringUtil.bigDecimalIsNull(sub.getEnding_balance_credit());
            subMap.put("ending_balance_credit", amount.add(ending_balance_credit));
            BigDecimal ending_balance_debit = StringUtil.bigDecimalIsNull(sub.getEnding_balance_debit());
            subMap.put("ending_balance_debit", ending_balance_debit);
            BigDecimal year_amount_credit = StringUtil.bigDecimalIsNull(sub.getYear_amount_credit());
            subMap.put("year_amount_credit", amount.add(year_amount_credit));

            new_SubMesage.setCurrent_amount_credit(amount.add(current_amount_credit));
            new_SubMesage.setYear_amount_credit(amount.add(year_amount_credit));
        }


        String dir = new ParentCodeMapping().getDir(sub.getSub_code());
        Integer subDirection = Integer.valueOf(dir);
        BigDecimal qmdDebit = (BigDecimal) subMap.get("ending_balance_debit");
        BigDecimal qmCredit = (BigDecimal) subMap.get("ending_balance_credit");
        BigDecimal debit = qmdDebit.subtract(qmCredit);
        if (debit.compareTo(BigDecimal.ZERO) > 0) {
            subMap.put("ending_balance_debit", debit);
            subMap.put("ending_balance_credit", new BigDecimal(0));

            new_SubMesage.setEnding_balance_debit(debit);
            new_SubMesage.setEnding_balance_credit(new BigDecimal(0));

        } else {
            subMap.put("ending_balance_debit", new BigDecimal(0));
            subMap.put("ending_balance_credit", debit.abs());

            new_SubMesage.setEnding_balance_debit(new BigDecimal(0));
            new_SubMesage.setEnding_balance_credit(debit.abs());
        }

        @SuppressWarnings("unchecked")

        Map<String, SubjectMessage> recordingUpSub = (Map<String, SubjectMessage>) map.get("recordingUpSub");
        recordingUpSub.put(new_SubMesage.getSub_code(), new_SubMesage); // 有重复科目就会覆盖

        subMap.put("update_date", new Date());
        subMap.put("update_timestamp", System.currentTimeMillis());
        subMap.put("pk_sub_id", sub.getPk_sub_id());
        vatDao.updateSubjectMap(subMap);

        BigDecimal blanceAmount = new BigDecimal(0);
        // 明细账期末余额 。记录每次科目更新之后的期末余额 ， 有正负大小之分的期末余额
        if (subDirection == 1) {
            blanceAmount = qmdDebit.subtract(qmCredit);
        } else {
            blanceAmount = qmCredit.subtract(qmdDebit);
        }

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("blanceAmount", blanceAmount);
        reMap.put("blanceSubName", sub.getSub_name());
        reMap.put("blanceDir", subDirection);
        reMap.put("up_code", sub.getSub_code());
        return reMap;

    }


    @Override
    public void changeSubAmount(Map<String, Object> param) throws BusinessException {
        List<TBasicSubjectMessage> list = querySubByAccAndCode(param);
        if (null != list && list.size() > 0) {
            TBasicSubjectMessage sub = list.get(0);
            String id = sub.getPkSubId();
            double qmd = StringUtil.bigDecimalIsNull(sub.getEndingBalanceDebit()).doubleValue();
            double qmc = StringUtil.bigDecimalIsNull(sub.getEndingBalanceCredit()).doubleValue();
            String flag = null;
            double amount = qmd - qmc;
            if (amount > 0) {
                flag = "1";
            } else if (amount < 0) {
                amount = qmc - qmd;
                flag = "2";
            }
            if (amount == 0.0) {
                flag = "0";
            }
            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("flag", flag);
            pa.put("amount", amount);
            pa.put("pkSubId", id);
            updQmAmount(pa);
        }

    }

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    public void chgSubAmountByDelete(Map<String, Object> param, Voucher voucher) throws BusinessException {
        SqlSession sqlSesSion = sqlSessionFactory.openSession();
        try {
            List<VoucherBody> list = voucher.getVoucherBodyList();
            VoucherHead vh = voucher.getVoucherHead();
            Map<String, SubjectMessage> map1 = new HashMap<>();
            Map<String, String> map2 = new HashMap<>();
            List<String> map3 = new ArrayList<String>();
            List<String> map4 = new ArrayList<String>();

            if (null == param.get("busDate")) {
                param.put("busDate", param.get("period"));
            } else {
                param.put("period", param.get("busDate"));
            }

            List<VoucherBody> arrVB = new ArrayList<>();
            if (list != null && list.size() > 0) {

                Map<String, Object> subMap = getSubMess3(list, param, 2);
                if (subMap != null && !subMap.isEmpty()) {
                    map1 = (Map<String, SubjectMessage>) subMap.get("map1");
                    map2 = (Map<String, String>) subMap.get("map2");
                    map3 = (List<String>) subMap.get("map3");
                    map4 = (List<String>) subMap.get("map4");
                }

                for (int i = 0; i < list.size(); i++) {
                    VoucherBody vb = list.get(i);
                    String direction = vb.getDirection();
                    String subjectID = vb.getSubjectID();
                    param.put("direction", direction);
                    param.put("subjectID", subjectID);
                    // fjz(反结转) deleteVoucher(单张删除) yjdelVo(一键删除)
                    // if (param.get("fjz") == null){
                    if (!StringUtil.isEmptyWithTrim(subjectID)
                            && (subjectID.startsWith("1405") || subjectID.startsWith("1403"))) {
                        List<KcCommodity> kcList = kcCommodityDao.queryBysubCode(param);
                        if (null != kcList && kcList.size() > 0) {
                            KcCommodity kc = kcList.get(0);
                            // 期初结余数量
                            Double qc_balanceNum = StringUtil.doubleIsNull(kc.getQc_balanceNum());
                            BigDecimal qc_balanceAmount = StringUtil.bigDecimalIsNull(kc.getQc_balanceAmount());
                            // 本期收入数量
                            Double bq_incomeNum = StringUtil.doubleIsNull(kc.getBq_incomeNum());
                            // 本期收入金额
                            BigDecimal bq_incomeAmount = StringUtil.bigDecimalIsNull(kc.getBq_incomeAmount());
                            // 本期发出数量
                            Double bq_issueNum = StringUtil.doubleIsNull(kc.getBq_issueNum());
                            // 本期发出金额
                            BigDecimal bq_issueAmount = StringUtil.bigDecimalIsNull(kc.getBq_issueAmount());
                            // 本年累计收入数量
                            Double total_incomeNum = StringUtil.doubleIsNull(kc.getTotal_incomeNum());
                            // 本年累计收入金额
                            BigDecimal total_incomeAmount = StringUtil.bigDecimalIsNull(kc.getTotal_incomeAmount());
                            // 本年累计发出数量
                            Double total_issueNum = StringUtil.doubleIsNull(kc.getTotal_issueNum());
                            // 本年累计发出金额
                            BigDecimal total_issueAmount = StringUtil.bigDecimalIsNull(kc.getTotal_issueAmount());
                            // 期末结存数量
                            Double qm_balanceNum = StringUtil.doubleIsNull(kc.getQm_balanceNum());
                            // 期末结存单价
                            BigDecimal qm_balancePrice = StringUtil.bigDecimalIsNull(kc.getQm_balancePrice());
                            // 期末结存金额
                            BigDecimal qm_balanceAmount = kc.getQm_balanceAmount();

                            /**************************
                             * 库存商品 satrt
                             *******************************/

                            if (subjectID.startsWith("1405")) {
                                if ("1".equals(direction)) {

                                    /*** 凭证数量 ****/
                                    Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                    /*** 凭证金额 ****/
                                    BigDecimal amount = new BigDecimal(StringUtil.doubleIsNull(vb.getDebitAmount())); // 借方金额

                                    /*** 减去凭证数量 减去凭证金额 ****/
                                    Double bq_incomeNum_new = bq_incomeNum - number;
                                    BigDecimal bq_incomeAmount_new = bq_incomeAmount.subtract(amount);

                                    kc.setBq_incomeNum(bq_incomeNum_new);
                                    kc.setBq_incomeAmount(bq_incomeAmount_new);

                                    Double qm_balanceNum_new = qm_balanceNum - number;
                                    BigDecimal qm_balanceAmount_new = qm_balanceAmount.subtract(amount);

                                    kc.setQm_balanceNum(qm_balanceNum_new);
                                    kc.setQm_balanceAmount(qm_balanceAmount_new);

                                    kc.setQm_balancePrice(
                                            StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                    kc.setTotal_incomeNum(total_incomeNum - number);
                                    kc.setTotal_incomeAmount(total_incomeAmount.subtract(amount));

                                    kcCommodityDao.updKcByVouch(kc);

                                } else if ("2".equals(direction)) {
                                    // 1 处理是成本结转这种情况
                                    // 1修改逻辑 先删除就旧的再添加新的 ,所以修改也要判断。
                                    // 2删除单个凭证逻辑直接删除
                                    if (param.get("modiyCbjz") != null || param.get("delCbjz") != null) {

                                        /*** 凭证数量 ****/
                                        Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                        /*** 凭证金额 ****/
                                        BigDecimal amount = new BigDecimal(
                                                StringUtil.doubleIsNull(vb.getCreditAmount())); // 贷方金额

                                        System.out.println("bq_issueNum = " + bq_issueNum);
                                        System.out.println("bq_issueAmount = " + bq_issueAmount);

                                        kc.setBq_issueNum(0.0); /*** 本期数量置为0 ****/
                                        kc.setBq_issueAmount(new BigDecimal(0.0)); /*** 本期金额置为0 ****/

                                        Double qm_balanceNum_new = qc_balanceNum + bq_incomeNum; /*** 新的期末数量 ****/
                                        BigDecimal qm_balanceAmount_new = qc_balanceAmount
                                                .add(bq_incomeAmount); /*** 新的期末金额 ****/

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        double new_total_issueNum = total_issueNum - bq_issueNum;
                                        kc.setTotal_issueNum(new_total_issueNum);
                                        BigDecimal new_total_issueAmount = total_issueAmount.subtract(bq_issueAmount);
                                        kc.setTotal_issueAmount(new_total_issueAmount);

                                        kcCommodityDao.updKcByVouch(kc);

                                    } else {
                                        // 2处理 不是成本结转
                                        /*** 凭证数量 ****/
                                        Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                        /*** 凭证金额 ****/
                                        BigDecimal amount = new BigDecimal(
                                                StringUtil.doubleIsNull(vb.getCreditAmount())); // 贷方金额

                                        Double qm_balanceNum_new = qm_balanceNum + number; /*** 新的期末数量 ****/

                                        BigDecimal qm_balanceAmount_new = qm_balanceAmount
                                                .add(amount); /*** 新的期末金额 ****/

                                        // 如果是手工添加的销售凭证 期末价格不应该等于用户填写的贷方金额
                                        // double total_bq_incomeNum =
                                        // qc_balanceNum + bq_incomeNum;
                                        // //本期收入总数
                                        // BigDecimal total_bq_incomeAmount =
                                        // qc_balanceAmount.add(bq_incomeAmount);
                                        // BigDecimal pingjun_price =
                                        // StringUtil.bigDivide(total_bq_incomeAmount,
                                        // total_bq_incomeNum);

                                        // BigDecimal cb_kcc =
                                        // pingjun_price.multiply(new
                                        // BigDecimal(number));

                                        // BigDecimal qm_balanceAmount_new =
                                        // qm_balanceAmount.add(cb_kcc);

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        // 本期发出数量金额减去要删除的
                                        Double bq_issueNum_new = bq_issueNum - number;
                                        BigDecimal bq_issueAmount_new = bq_issueAmount.subtract(amount);

                                        kc.setBq_issueNum(bq_issueNum_new); /*** 新的发出数量 ****/
                                        kc.setBq_issueAmount(bq_issueAmount_new); /*** 新的发出金额 ****/

                                        kc.setTotal_issueNum(total_issueNum - number);
                                        kc.setTotal_issueAmount(total_issueAmount.subtract(amount));

                                        kcCommodityDao.updKcByVouch(kc);

                                    }
                                }
                            }

                            /**************************
                             * 库存商品 end
                             *******************************/

                            /**************************
                             * 原材料 satrt
                             *******************************/

                            // 原材料凭证删除操作
                            if (subjectID.startsWith("1403")) {
                                if ("1".equals(direction)) {

                                    /*** 凭证数量 ****/
                                    Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                    /*** 凭证金额 ****/
                                    BigDecimal amount = new BigDecimal(StringUtil.doubleIsNull(vb.getDebitAmount())); // 借方金额

                                    Double bq_incomeNum_new = bq_incomeNum - number;
                                    BigDecimal bq_incomeAmount_new = bq_incomeAmount.subtract(amount);

                                    kc.setBq_incomeNum(bq_incomeNum_new);
                                    kc.setBq_incomeAmount(bq_incomeAmount_new);

                                    Double qm_balanceNum_new = qm_balanceNum - number;
                                    BigDecimal qm_balanceAmount_new = qm_balanceAmount.subtract(amount);

                                    kc.setQm_balanceNum(qm_balanceNum_new);
                                    kc.setQm_balanceAmount(qm_balanceAmount_new);

                                    kc.setQm_balancePrice(
                                            StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                    kc.setTotal_incomeNum(total_incomeNum - number);
                                    kc.setTotal_incomeAmount(total_incomeAmount.subtract(amount));

                                    kcCommodityDao.updKcByVouch(kc);

                                } else if ("2".equals(direction)) {

                                    // 1 处理是成本结转这种情况
                                    // 1修改逻辑 先删除就旧的再添加新的 ,所以修改也要判断。
                                    // 2删除单个凭证逻辑直接删除
                                    if (param.get("modiyCbjz") != null || param.get("delCbjz") != null) {

                                        /*** 凭证数量 ****/
                                        Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                        /*** 凭证金额 ****/
                                        BigDecimal amount = new BigDecimal(
                                                StringUtil.doubleIsNull(vb.getCreditAmount())); // 贷方金额

                                        System.out.println("bq_issueNum = " + bq_issueNum);
                                        System.out.println("bq_issueAmount = " + bq_issueAmount);

                                        kc.setBq_issueNum(0.0); /*** 本期数量置为0 ****/
                                        kc.setBq_issueAmount(new BigDecimal(0.0)); /*** 本期金额置为0 ****/

                                        Double qm_balanceNum_new = qc_balanceNum + bq_incomeNum; /*** 新的期末数量 ****/
                                        BigDecimal qm_balanceAmount_new = qc_balanceAmount
                                                .add(bq_incomeAmount); /*** 新的期末金额 ****/

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        double new_total_issueNum = total_issueNum - bq_issueNum;
                                        kc.setTotal_issueNum(new_total_issueNum);
                                        BigDecimal new_total_issueAmount = total_issueAmount.subtract(bq_issueAmount);
                                        kc.setTotal_issueAmount(new_total_issueAmount);

                                        kcCommodityDao.updKcByVouch(kc);

                                    } else {

                                        /*** 凭证数量 ****/
                                        Double number = StringUtil.doubleIsNull(vb.getNumber()); // 来自凭证的数量
                                        /*** 凭证金额 ****/
                                        BigDecimal amount = new BigDecimal(
                                                StringUtil.doubleIsNull(vb.getCreditAmount())); // 贷方金额

                                        // 本期发出数量金额减去要删除的
                                        Double bq_issueNum_new = bq_issueNum - number;
                                        BigDecimal bq_issueAmount_new = bq_issueAmount.subtract(amount);

                                        kc.setBq_issueNum(bq_issueNum_new);
                                        kc.setBq_issueAmount(bq_issueAmount_new);

                                        Double qm_balanceNum_new = qm_balanceNum + number;
                                        BigDecimal qm_balanceAmount_new = qm_balanceAmount.add(amount);

                                        kc.setQm_balanceNum(qm_balanceNum_new);
                                        kc.setQm_balanceAmount(qm_balanceAmount_new);
                                        kc.setQm_balancePrice(
                                                StringUtil.bigDivide(qm_balanceAmount_new, qm_balanceNum_new));

                                        kc.setTotal_issueNum(total_issueNum - number);
                                        kc.setTotal_issueAmount(total_issueAmount.subtract(amount));

                                        kcCommodityDao.updKcByVouch(kc);
                                    }

                                }
                            }

                            /**************************
                             * 原材料 end
                             *******************************/
                        }
                    }

                    if ("1".equals(direction))
                        param.put("amount", vb.getDebitAmount());
                    else if ("2".equals(direction))
                        param.put("amount", vb.getCreditAmount());
                    if (StringUtil.isEmpty(subjectID)) {
                        continue;
                    }
                    arrVB.add(vb);
                    /*
                     * Integer subLength = subjectID.trim().length(); Integer jc
                     * = (subLength - 4) / 3; for (int j = 0; j <= jc; j++) {
                     * String curSubjectID = subjectID.substring(0, subLength -
                     * 3 * j); param.put("subjectID", curSubjectID);
                     * sqlSesSion.update("subMessage.chgSubAmountByDelete",
                     * param); changeSubAmount(param);
                     * param.remove("subjectID"); }
                     */

                    List<String> codes = StringUtil.getCodes(subjectID);
                    List<String> ids = new ArrayList<>();
                    for (String oneCode : codes) {
                        String pk_id = map2.get(oneCode);
                        if (StringUtil.isEmpty(pk_id)) {
                            throw new BusinessException(
                                    "chgSubAmountByDelete map2.get(oneCode) pk_id is null,oneCode=" + oneCode);
                        }
                        ids.add(pk_id);
                    }
                    Map<String, Object> upParam = new HashMap<>();
                    upParam.put("amount", param.get("amount"));
                    upParam.put("vb_dir", direction);
                    upParam.put("ids", ids);
                    if (ids == null || ids.isEmpty()) {
                        throw new BusinessException("chgSubAmountByDelete ids==null || ids.isEmpty()");
                    }
                    int upNum = vatDao.chgSubAmountByDelete2(upParam);
                    if (upNum == 0 || upNum > ids.size()) {
                        throw new BusinessException(
                                "chgSubAmountByDelete  vatDao.chgSubAmountByDelete2(upParam) upNum==0 || upNum>ids.size() upNum == " + upNum);
                    }
                }

                upQmAmountAndCache(param, map2, map3, map4);


                if (!arrVB.isEmpty()) {
                    detailAccountdel(param, vh, arrVB);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesSion.close();
        }
    }


    @SuppressWarnings("unused")
    private void upQmAmountAndCache(Map<String, Object> param, Map<String, String> map2, List<String> map3,
                                    List<String> vbCodeList) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        for (String code : map3) {
            String pkid = map2.get(code);
            if (StringUtil.isEmpty(pkid)) {
                throw new BusinessException("upQmAmountAndCache StringUtil.isEmpty(pkid) ");
            }
            sb.append("\"").append(pkid).append("\"").append(",");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        Map<String, Object> qureMap = new HashMap<>();
        qureMap.put("codes3", sb.toString());
        int upNum = vatDao.chgSubAmountByDelete3(qureMap);
        if (upNum == 0 || upNum > map3.size()) {

            throw new BusinessException("upQmAmountAndCache upNum==0 || upNum>map3.size() upNum== " + upNum);
        }
        List<RedisSub> list3 = vatDao.queryRedisSubByCondition(qureMap);
        if (list3.size() != map3.size()) {
            throw new BusinessException("chgSubAmountByDelete list3.size()!=map3.size()");
        }

        String accountID = param.get("accountID").toString();
        String period = param.get("busDate").toString();
        List<RedisSub> cacheList = vatService.getAllSub(accountID, period);
        cacheList.removeAll(list3);
        cacheList.addAll(list3);
        String set = vatService.setCahceSub(accountID, period, cacheList);
        if (vatService.getChg() == false) {
            vatService.setChg(true);
        }
        rtChgSubToPage2(param, list3, vbCodeList);
    }

    private void rtChgSubToPage2(Map<String, Object> param, List<RedisSub> list, List<String> vbCodeList)
            throws BusinessException {

        List<RedisSub> arr = new ArrayList<>();

        for (String vbCode : vbCodeList) {
            boolean flg = false;
            for (RedisSub redisSub : list) {
                if (redisSub.getSub_code().equals(vbCode)) {
                    arr.add(redisSub);
                    flg = true;
                    break;
                }
            }
            if (flg == false) {
                throw new BusinessException(
                        "chgSubAmountByDelete upQmAmountAndCache rtChgSubToPage2 flg==false vbCode=" + vbCode);
            }
        }

        ParentCodeMapping parentCodeMapping = new ParentCodeMapping();
        BigDecimal zero1 = new BigDecimal(0);

        List<PageSub> rtPageSub = new ArrayList<>();
        for (RedisSub sub : arr) {
            PageSub pageSub = new PageSub();
            pageSub.setPkSubId(sub.getPk_sub_id());
            pageSub.setSubCode(sub.getSub_code());
            pageSub.setSubName(sub.getSub_name());
            pageSub.setFullName(sub.getFull_name());
            pageSub.setEndingBalanceCredit(
                    sub.getEnding_balance_credit() == null ? zero1 : sub.getEnding_balance_credit());
            pageSub.setEndingBalanceDebit(
                    sub.getEnding_balance_debit() == null ? zero1 : sub.getEnding_balance_debit());
            pageSub.setDir(parentCodeMapping.getDir(sub.getSub_code()));
            rtPageSub.add(pageSub);
        }

        param.put("deleteVoucherToPage", rtPageSub);
        System.out.println();

    }


    @SuppressWarnings("unused")
    private void upQmAmount(Map<String, Object> param, Map<String, String> map2, List<String> map3)
            throws BusinessException {
        List<String> idList2 = new ArrayList<>();
        for (String sub_code : map3) {
            String pkId = map2.get(sub_code);
            idList2.add(pkId);
        }
        Map<String, Object> chgMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (String pkid : idList2) {
            sb.append("\"").append(pkid).append("\"").append(",");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        chgMap.put("codes3", sb.toString());
        List<RedisSub> list3 = vatDao.queryRedisSubByCondition(chgMap);
        if (list3.size() != map3.size()) {
            throw new BusinessException("chgSubAmountByDelete upQmAmount list3.size()!=map3.size()");
        }

        BigDecimal zero = new BigDecimal(0);
        for (RedisSub redisSub : list3) {
            String sub_code = redisSub.getSub_code();

            BigDecimal qmdDebit = StringUtil.bigDecimalIsNull(redisSub.getEnding_balance_debit());
            BigDecimal qmCredit = StringUtil.bigDecimalIsNull(redisSub.getEnding_balance_credit());

            BigDecimal debit = qmdDebit.subtract(qmCredit);

            Map<String, Object> subMap2 = new HashMap<>();

            if (debit.compareTo(BigDecimal.ZERO) > 0) {
                subMap2.put("ending_balance_debit", debit);
                subMap2.put("ending_balance_credit", zero);
            } else {
                subMap2.put("ending_balance_debit", zero);
                subMap2.put("ending_balance_credit", debit.abs());
            }

            subMap2.put("update_date", new Date());
            subMap2.put("update_timestamp", System.currentTimeMillis());
            subMap2.put("pk_sub_id", redisSub.getPk_sub_id());

            int updates = vatDao.updateSubjectMap(subMap2);

            if (debit.compareTo(BigDecimal.ZERO) > 0) {
                redisSub.setEnding_balance_debit(debit);
                redisSub.setEnding_balance_credit(zero);
            } else {
                redisSub.setEnding_balance_debit(zero);
                redisSub.setEnding_balance_credit(debit.abs());
            }
        }

        String accountID = param.get("accountID").toString();
        String period = param.get("busDate").toString();
        List<RedisSub> cacheList = vatService.getAllSub(accountID, period);
        cacheList.removeAll(list3);
        cacheList.addAll(list3);
        String set = vatService.setCahceSub(accountID, period, cacheList);
        // System.out.println(set);
    }


    private void detailAccountdel(Map<String, Object> param, VoucherHead vh, List<VoucherBody> arrVB)
            throws BusinessException {
        try {

            Map<String, Object> upSubMap = new HashMap<>();
            String vouchID = vh.getVouchID();
            ParentCodeMapping parentCode = new ParentCodeMapping();
            Integer voucherNO = vh.getVoucherNO();
            upSubMap.put("accountID", param.get("accountID"));
            upSubMap.put("period", param.get("busDate"));
            upSubMap.put("vouchNum", voucherNO);


            for (int i = 0; i < arrVB.size(); i++) {
                VoucherBody vb = arrVB.get(i);

                String sub_code = vb.getSubjectID();

                String vb_dir = vb.getDirection();
                String dir = parentCode.getDir(sub_code.substring(0, 4));
                List<String> codes = StringUtil.getCodes(sub_code);
                upSubMap.put("codes", codes);

                Double vb_amount = 0.0;
                Double jf_amount = 0.0;
                Double df_amount = 0.0;

                if (vb_dir.equals("1")) {
                    jf_amount = vb.getDebitAmount();
                } else if (vb_dir.equals("2")) {
                    df_amount = vb.getCreditAmount();
                }
                if (dir.equals("1")) {
                    vb_amount = jf_amount - df_amount;
                } else if (dir.equals("2")) {
                    vb_amount = df_amount - jf_amount;
                }

                upSubMap.put("amount", new BigDecimal(vb_amount));

                subBookDao.upSubBlanceAmount(upSubMap);
            }

            upSubMap.put("vouchID", vouchID);
            subBookDao.delSubBook(upSubMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @SuppressWarnings({"unused", "unchecked"})

    private void detailAccountModify(Map<String, Object> param, VoucherHead vh, List<VoucherBody> arrVB,
                                     Map<String, String> idMap) throws BusinessException {
        try {
            String accountID = param.get("accountID").toString();
            String period = param.get("busDate").toString();
            String sub_zy = param.get("sub_zy").toString();
            ParentCodeMapping parentCode = new ParentCodeMapping();

            Integer voucherNO = vh.getVoucherNO(); // 凭证号


            for (int i = 0; i < arrVB.size(); i++) {
                /******** 新的分录 **********/
                VoucherBody vb = arrVB.get(i);

                String sub_code = vb.getSubjectID(); // 科目编码
                String vb_dir = vb.getDirection(); // 凭证方向
                Double debitAmount = vb.getDebitAmount(); // 借方金额
                Double creditAmount = vb.getCreditAmount();// 贷方金额

                // List<String> codes = StringUtil.getCodes(sub_code);
                List<String> codes = new StringUtil.getCodes2().getCode(sub_code);
                String sub_dir = parentCode.getDir(sub_code);


                BigDecimal ling = new BigDecimal(0);
                BigDecimal jf, df; // 定义借方，贷方 金额
                jf = df = ling;
                if (vb_dir.equals("1")) {
                    jf = new BigDecimal(debitAmount);
                } else if (vb_dir.equals("2")) {
                    df = new BigDecimal(creditAmount);
                }

                /************* 定义明细余额 ***************/
                BigDecimal balance_money = ling;

                if (sub_dir.equals("1")) {
                    balance_money = jf.subtract(df);
                } else {
                    balance_money = df.subtract(jf);
                }
                HashMap<String, List<SubBook>> code_map = new HashMap<>();

                Map<String, Object> map4 = new HashMap<>();
                map4.put("accountID", param.get("accountID"));
                map4.put("period", param.get("busDate"));
                map4.put("codes", codes);
                map4.put("vouchNum2", voucherNO);

                // 根据分解后的科目查询 凭证号小于或者等于1405 1405011的明细账数据
                // 目的是确定上一条明细账的余额，得到余额就可以计算要插入的这条数据的 期末余额
                // and vouchNum <= #{vouchNum2} 当前凭证号是13 则查询小于等于13的凭证
                List<SubBook> list = subBookDao.queryByConditions(map4);
                if (list != null && list.size() > 0) {
                    for (String code : codes) {
                        List<SubBook> arr = new ArrayList<>();
                        for (SubBook subBook : list) {
                            if (subBook.getSub_code().equals(code)) {
                                arr.add(subBook);
                            }
                        }
                        if (arr.size() > 0) {
                            Collections.sort(arr, new Comparator<SubBook>() {
                                @Override
                                public int compare(SubBook paramT1, SubBook paramT2) {
                                    int a = paramT1.getVouchNum().compareTo(paramT2.getVouchNum());
                                    int b = paramT1.getSub_bk_id().compareTo(paramT2.getSub_bk_id());
                                    return a == 0 ? b : a;
                                }
                            });
                            code_map.put(code, arr);
                        }
                    }
                }

                // 遍历分录科目集合
                for (int j = 0; j < codes.size(); j++) {

                    String fen_jie_code = codes.get(j);

                    List<SubBook> fen_jie_code_list = code_map.get(fen_jie_code);

                    // 定义要插入的明细账数据
                    SubBook subBook = new SubBook();

                    subBook.setAccountID(accountID);
                    subBook.setPeriod(period);

                    // 凭证主键
                    subBook.setVouchID(vh.getVouchID());
                    // 凭证体主键
                    subBook.setVouchAID(vb.getVouchAID());
                    // 凭证号
                    subBook.setVouchNum(voucherNO);
                    // 摘要
                    subBook.setVcabstact(sub_zy);
                    // 科目编码
                    subBook.setSub_code(fen_jie_code);
                    // 科目借贷方向
                    subBook.setDirection(sub_dir);

                    if (vb_dir.equals("1")) {
                        subBook.setDebitAmount(jf);
                        subBook.setCreditAmount(null);
                    } else {
                        subBook.setDebitAmount(null);
                        subBook.setCreditAmount(df);
                    }
                    subBook.setUpdateDate(new Date());
                    subBook.setUp_date(System.currentTimeMillis());
                    // 真正更新的末级科目
                    subBook.setIsEndSubCode(fen_jie_code.equals(sub_code) == true ? 1 : 0);

                    List<SubBook> insertlist = new ArrayList<>();
                    // 存在历史数据
                    if (fen_jie_code_list != null && fen_jie_code_list.size() > 0) {
                        // 遍历这个科目的本期间的所有明细账，找出这个科目的上一条明细位置
                        // before_subBook 必须是上一张明细账数据 否则计算余额会出现错误
                        SubBook before_subBook = fen_jie_code_list.get(fen_jie_code_list.size() - 1);

                        BigDecimal before_blanceAmount = StringUtil.bigDecimalIsNull(before_subBook.getBlanceAmount());

                        subBook.setSub_name(before_subBook.getSub_name());

                        BigDecimal current_blanceAmount = before_blanceAmount.add(balance_money);
                        subBook.setBlanceAmount(current_blanceAmount); // 余额

                    } else {
                        // 没有历史明细数据的 余额使用科目余额表的期初数据
                        // TBasicSubjectMessage tb = new TBasicSubjectMessage();
                        // tb.setSubCode(fen_jie_code);
                        // tb.setAccountId(accountID);
                        // tb.setAccountPeriod(period);
                        // List<TBasicSubjectMessage> subList =
                        // querySubMessageBySubcode2(tb);

                        Map<String, Object> map6 = new HashMap<>();
                        String pk_sub_id = idMap.get(fen_jie_code);
                        if (StringUtil.isEmpty(pk_sub_id)) {
                            String ss = Thread.currentThread().getStackTrace()[1].toString();
                            throw new BusinessException("pk_sub_id is null  fen_jie_code = " + fen_jie_code + "," + ss);
                        }
                        map6.put("pk_sub_id", pk_sub_id);
                        TBasicSubjectMessage tBasicSubjectMessage = vatService.queryTBasicSubjectMessageById(pk_sub_id);
                        List<TBasicSubjectMessage> subList = new ArrayList<>();
                        subList.add(tBasicSubjectMessage);

                        if (subList == null || subList.isEmpty() || subList.get(0) == null) {
                            String ss = Thread.currentThread().getStackTrace()[1].toString();
                            throw new BusinessException(
                                    "修改凭证  pk_sub_id =" + pk_sub_id + ",fen_jie_code = " + fen_jie_code + "," + ss);
                        }

                        subBook.setSub_name(subList.get(0).getSubName());
                        BigDecimal init_debit = StringUtil.bigDecimalIsNull(subList.get(0).getInitDebitBalance());
                        BigDecimal init_credit = StringUtil.bigDecimalIsNull(subList.get(0).getInitCreditBalance());
                        BigDecimal init_amount = null;
                        if (sub_dir.equals("1")) {
                            init_amount = init_debit.subtract(init_credit);
                        } else {
                            init_amount = init_credit.subtract(init_debit);
                        }
                        // 注意 明细账余额分正负
                        BigDecimal qimo_amount = init_amount.add(balance_money); // 结存余额
                        subBook.setBlanceAmount(qimo_amount);
                    }

                    insertlist.add(subBook);
                    subBookDao.insertSubBookBath(insertlist);
                    upSubBlanceAnount2(accountID, period, voucherNO, balance_money, fen_jie_code);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private int upSubBlanceAnount2(String accountID, String period, Integer voucherNO, BigDecimal money, String code)
            throws BusinessException {
        Map<String, Object> map5 = new HashMap<>();
        map5.put("accountID", accountID);
        map5.put("period", period);
        map5.put("vouchNum", voucherNO);
        map5.put("sub_code", code);
        map5.put("amount", money);
        return subBookDao.upSubBlanceAmount2(map5);
    }

    private void rmvSub(Map<String, Object> param, Integer voucherNO, List<VoucherBody> rmList)
            throws BusinessException {
        Map<String, Object> map5 = new HashMap<>();
        map5.put("accountID", param.get("accountID"));
        map5.put("period", param.get("busDate"));
        map5.put("vouchNum", voucherNO); // 当前凭证号
        ParentCodeMapping parentCode = new ParentCodeMapping();

        for (int i = 0; i < rmList.size(); i++) {
            VoucherBody vb = rmList.get(i);
            String sub_code = vb.getSubjectID();
            Double creditAmount = vb.getCreditAmount();
            Double debitAmount = vb.getDebitAmount();
            String direction = vb.getDirection();
            String dir = parentCode.getDir(sub_code);
            Double money = 0.0; // 定义余额

            if (direction.equals("1")) {
                money = debitAmount;
                if (dir.equals("2")) {
                    money = 0 - money;
                }
            } else if (direction.equals("2")) {
                money = creditAmount;
                if (dir.equals("1")) {
                    money = 0 - money;
                }
            }
            List<String> codes = StringUtil.getCodes(sub_code);
            map5.put("codes", codes);

            int num = subBookDao.upSubBlanceAmount(map5);
            System.out.println(num);

            Map<Object, Object> delmap = new HashMap<>();
            delmap.put("vouchAID", vb.getVouchAID()); // 根据 凭证分录 id 删除明细账
            int num1 = subBookDao.delSubBook2(delmap);
            System.out.println(num1);
        }
    }

    /**
     * @param tBasicSubjectMessage
     * @return String 返回类型
     * @Title: querySubMessageMaxSubCode
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月12日 下午2:02:05
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public String querySubMessageMaxSubCode(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        String subMessageSubCode = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            subMessageSubCode = sqlSesion.selectOne("subMessage.querySubMessageMaxSubCode", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return subMessageSubCode;
    }

    @Override
    public List<TBasicSubjectMessage> querySubByIDAndName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.querySubByIDAndName", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public List<TBasicSubjectMessage> queryAllSubject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.queryAllSubject", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }

    }

    /**
     * @param subMessageMap
     * @return
     * @throws BusinessException
     * @Title: queryLedgerByParameters
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @see com.wqb.dao.subject.TBasicSubjectMessageMapper#queryLedgerByParameters(java.util.Map)
     */
    @Override
    public List<TBasicSubjectMessage> queryLedgerByParameters(Map<String, String> subMessageMap)
            throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> selectList = sqlSesion.selectList("subMessage.queryLedgerByParameters",
                    subMessageMap);
            return selectList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }

    }

    @Override
    public List<TBasicSubjectMessage> querySubjectByName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.querySubjectByName", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    /**
     * @param map
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageByCategory
     * @Description: 根据科目名称或代码 和 科目类别查询科目
     * @date 2018年1月23日 上午10:02:30
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageByCategory(Map<String, Object> map) {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        List<TBasicSubjectMessage> list = null;
        try {
            list = sqlSesion.selectList("subMessage.querySubMessageByCategory", map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return list;

    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageListMoney
     * @Description: 查询真实金额
     * @date 2018年1月25日 下午7:25:04
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageListMoney(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessageListMoney", tBasicSubjectMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageLevel
     * @Description: 指定查询科目级别科目编码名称不为空
     * @date 2018年1月31日 上午9:42:28
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageLevel(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessageLevel", tBasicSubjectMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @Override
    public int deleteMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        SqlSession sqlSesion = null;
        int no = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            no = sqlSesion.delete("subMessage.deleteMessage", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return no;
    }

    @Override
    public List<TBasicSubjectMessage> querySubByAccAndCode(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.querySubByAccAndCode", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void updQmAmount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.update("subMessage.updQmAmount", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    /**
     * @param parameters
     * @return
     * @Title: querySbujectBalance
     * @Description: 查询科目余额表
     * @see com.wqb.dao.subject.TBasicSubjectMessageMapper#querySbujectBalance(java.util.Map)
     */
    @Override
    public List<TBasicSubjectMessage> querySbujectBalance(Map<String, String> parameters) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySbujectBalance", parameters);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @Override
    public void chgSySubAmount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.update("subMessage.chgSySubAmount", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void updQnjlr(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            sqlSesion.update("subMessage.updQnjlr", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void updQmAmount1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.update("subMessage.updQmAmount1", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void updQmAmount2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.update("subMessage.updQmAmount2", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public List<TBasicSubjectMessage> selectLastArch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.selectLastArch", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public List<TBasicSubjectMessage> selectLastArch2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.selectLastArch2", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    // 结转销售成本更新对应的科目
    public void updatejzcb(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("subMessage.updatejzcb", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<TBasicSubjectMessage> queryFuzzySubByName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            List<TBasicSubjectMessage> list = sqlSesion.selectList("subMessage.queryFuzzySubByName", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void delUnjzSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.delete("subMessage.delUnjzSub", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }

    }

    @Override
    public void unLdAmount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        try {
            sqlSesion.update("subMessage.unLdAmount", param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSesion.close();
        }
    }

    /**
     * @param map
     * @return
     * @throws BusinessException int 返回类型
     * @Title: querySubjectByCode
     * @Description: 根据科目代码查询 以此科目代码开头的科目有几条
     * @date 2018年6月1日 下午4:49:21
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public int querySubjectByCode(Map<String, Object> map) {
        SqlSession sqlSesion = sqlSessionFactory.openSession();
        int no = 0;
        try {
            no = sqlSesion.selectOne("subMessage.querySubjectByCode", map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return no;
    }

    /**
     * @param tBasicSubjectMessage
     * @return TBasicSubjectMessage 返回类型
     * @Title: querySubMessageBySubcode
     * @Description: 根据科目编码、期间、帐套id查询科目信息
     * @date 2018年7月20日 下午3:44:16
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageBySubcode2(TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubMessageBySubCode2", tBasicSubjectMessage);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    private String getZy(List<VoucherBody> list) {
        for (VoucherBody bb : list) {
            if (bb != null) {
                String vc = bb.getVcabstact();
                if (!StringUtil.isEmpty(vc)) {
                    return vc;
                }
            }
        }
        return null;
    }

    @Override
    public List<TBasicSubjectMessage> querySubject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySubject", param);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @Override
    public List<TBasicSubjectMessage> querySysBankSubject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySysBankSubject", param);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    /**
     * @param param
     * @return
     * @Title: querySbuMessageByMapping
     * @Description: 根据科目名称、科目代码、期间查询科目信息
     * @see com.wqb.dao.subject.TBasicSubjectMessageMapper#querySbuMessageByMapping(java.util.Map)
     */
    @Override
    public List<TBasicSubjectMessage> querySbuMessageByMapping(Map<String, Object> param) {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> selectList = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            selectList = sqlSesion.selectList("subMessage.querySbuMessageByMapping", param);
        } finally {
            sqlSesion.close();
        }
        return selectList;
    }

    @Override
    public boolean isLastStage(Map<String, Object> parameters) {
        SqlSession sqlSesion = null;
        int count = 0;
        boolean lastStage = false;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            count = sqlSesion.selectOne("subMessage.isLastStage", parameters);
            if (count == 1) {
                lastStage = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return lastStage;
    }

    @Override
    public TBasicSubjectMessage querySubMessageByPkSubId(String pkSubId) throws BusinessException {
        SqlSession sqlSesion = null;
        TBasicSubjectMessage tm = null;
        List<TBasicSubjectMessage> tmList = new ArrayList<TBasicSubjectMessage>();
        try {
            sqlSesion = sqlSessionFactory.openSession();
            tmList = sqlSesion.selectList("subMessage.querySubMessageByPkSubId", pkSubId);
            if (tmList != null && tmList.size() > 0) {
                tm = tmList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return tm;
    }

    @Override
    public void delSubMessageByPkSubId(String pkSubId) throws BusinessException {
        SqlSession sqlSesion = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            sqlSesion.delete("subMessage.delSubMessageByPkSubId", pkSubId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
    }

    @Override
    public void chgSubAmountByDeleteSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            sqlSesion.update("subMessage.chgSubAmountByDeleteSub", param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }

    }

    @Override
    public void chgSubAmountByAddSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            sqlSesion.update("subMessage.chgSubAmountByAddSub", param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }


    }

    @Override
    public List<TBasicSubjectMessage> queryBankSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSesion = null;
        List<TBasicSubjectMessage> list = null;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            list = sqlSesion.selectList("subMessage.queryBankSub", param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSesion.close();
        }
        return list;
    }
}
