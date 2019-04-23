package com.wqb.service.vat.impl;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.*;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.attached.AttachedDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.receipt.ReceiptDao;
import com.wqb.dao.subject.SubjectDao;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.subject.TBasicSubjectParentMapper;
import com.wqb.dao.track.dao.StateTrackDao;
import com.wqb.dao.track.dao.TrackSubDao;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.*;
import com.wqb.service.KcCommodity.KcCommodityService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.proof.ProofService;
import com.wqb.service.vat.VatService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wqb.common.MyCollectors.singleResultGroupingBy;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Component
@Service("vatService")
public class VatServiceImpl implements VatService {
    private static ThreadLocal<String> period_acc = new ThreadLocal<String>();
    private static ThreadLocal<String> account_id = new ThreadLocal<String>();
    private static ThreadLocal<String> user_id = new ThreadLocal<String>();
    private static ThreadLocal<String> user_name = new ThreadLocal<String>();
    private static ThreadLocal<Boolean> chg = new ThreadLocal<Boolean>();
    @Autowired
    VatDao vatDao;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    AttachedDao attachedDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    StateTrackDao stateTrackDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ProofService proofService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    KcCommodityDao commodityDao;
    @Autowired
    TrackSubDao trackSubDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    TBasicSubjectParentMapper tBasicSubjectParentMapper;
    @Autowired
    TBasicSubjectMappingMiddleMapper subjectMiddleMapping;
    @Autowired
    JedisClient jedisClient;
    @Autowired
    KcCommodityService kcCommodityService;
    @Autowired
    AssetsService assetsService;

    @Autowired
    AccountDao accountDao;

    @Autowired
    private TBasicDocumentsService documentsService;

    @Autowired
    private ReceiptDao receiptDao;


    @SuppressWarnings("unused")
    @Override
    public boolean getChg() {
        Boolean boolean1 = chg.get();
        if (boolean1 == null) {
            return false;
        }
        return boolean1;
    }

    @Override
    public void setChg(boolean bool) {
        chg.set(bool);
    }

    private String setRedisSubMessList(String accountID, String period, List<RedisSub> list) {
        String json = new Gson().toJson(list);
        return jedisClient.set(getKey(accountID, period), json);
    }

    private String upRedisSubMessList(String accountID, String period) throws BusinessException {
        List<RedisSub> list = queryRedisSub(accountID, period);
        return setRedisSubMessList(accountID, period, list);
    }

    @Override
    public String resetCache(Map<String, Object> param) throws BusinessException {
        String accountID = (String) param.get("accountID");
        String period = (String) (param.get("busDate") != null ? param.get("busDate") : param.get("period"));
        return resetCache(accountID, period);
    }

    @Override
    public String resetCache(String accountID, String period) throws BusinessException {
        if (StringUtil.isEmpty(period) || StringUtil.isEmpty(accountID)) {
            throw new BusinessException(" resetData Map accountID ==null or period==null");
        }
        return upRedisSubMessList(accountID, period);
    }

    @Override
    public Long delCache(String accountID, String period) throws BusinessException {
        if (StringUtil.isEmpty(period) || StringUtil.isEmpty(accountID)) {
            throw new BusinessException(" delCache Map accountID ==null or period==null");
        }
        return jedisClient.del(getKey(accountID, period));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String resetCache(HttpSession session) throws BusinessException {
        Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
        Account account = (Account) userDate.get("account");
        return resetCache(account.getAccountID(), userDate.get("busDate").toString());
    }

    @Override
    public String resetCache() throws BusinessException {
        return resetCache(account_id.get(), period_acc.get());
    }

    private String addRedisSubMessList(String accountID, String period, RedisSub sub) throws BusinessException {
        try {
            String json = jedisClient.get(getKey(accountID, period));
            List<RedisSub> list = null;
            if (StringUtil.isEmpty(json)) {
                list = queryRedisSub(accountID, period);
                if (list == null) {
                    throw new BusinessException(" addRedisSubMessList queryRedisSub(accountID, period) ==nullr");
                }
                if (list.indexOf(sub) < 0) {
                    throw new BusinessException(" addRedisSubMessList list.indexOf(sub)<0");
                }
            } else {
                list = new Gson().fromJson(json, new TypeToken<List<RedisSub>>() {
                }.getType());
                list.remove(sub);
                list.add(sub);
            }
            return setRedisSubMessList(accountID, period, list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    private List<RedisSub> getRedisSubMessList(String accountID, String period) throws BusinessException {
        try {
            String json = jedisClient.get(getKey(accountID, period));
            List<RedisSub> arr = new ArrayList<>();
            if (StringUtil.isEmpty(json) || "null".equals(json.toLowerCase())) {
                List<RedisSub> list = queryRedisSub(accountID, period);
                if (list == null || list.isEmpty()) {
                    throw new BusinessException(" setRedisSubMessList queryRedisSub(accountID, period) ==null ");
                }
                setRedisSubMessList(accountID, period, list);
                arr = list;
            } else {
                arr = new Gson().fromJson(json, new TypeToken<List<RedisSub>>() {
                }.getType());
            }
            if (arr == null || arr.isEmpty()) {
                throw new BusinessException("vatService getRedisSubMessList arr == null or arr.isEmpty() arr=" + arr);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    public String getOneSubId(String accountID, String period, String code) throws BusinessException {
        RedisSub oneSub = getOneSub(accountID, period, code);
        if (oneSub == null) {
            throw new BusinessException("getOneRedisSub code=" + code + "accountID = " + accountID + ",period = " + period);
        }
        return oneSub.getPk_sub_id();
    }

    @Override
    public Map<String, String> getAllSubId(String accountID, String period) throws BusinessException {
        List<RedisSub> list = getRedisSubMessList(accountID, period);
        Iterator<RedisSub> iterator = list.iterator();
        Map<String, String> map = new HashMap<>();
        while (iterator.hasNext()) {
            RedisSub redisSub = (RedisSub) iterator.next();
            map.put(redisSub.getSub_code(), redisSub.getPk_sub_id());
        }
        return map;
    }

    @Override
    public List<RedisSub> getAllSub(String accountID, String period) throws BusinessException {
        return getRedisSubMessList(accountID, period);
    }

    @Override
    public String getSubJson(String accountID, String period) {
        String key = getKey(accountID, period);
        return jedisClient.get(key);
    }

    @Override
    public String setCahceSub(String accountID, String period, List<RedisSub> list) {
        return setRedisSubMessList(accountID, period, list);
    }

    @Override
    public String addSubToCache(String accountID, String period, RedisSub sub) throws BusinessException {
        return addRedisSubMessList(accountID, period, sub);
    }

    @Override
    public RedisSub getOneSub(String accountID, String period, String code) throws BusinessException {
        List<RedisSub> list = getRedisSubMessList(accountID, period);
        Iterator<RedisSub> iterator = list.iterator();
        RedisSub sub = null;
        while (iterator.hasNext()) {
            RedisSub redisSub = (RedisSub) iterator.next();
            if (redisSub.getSub_code().equals(code)) {
                sub = redisSub;
                break;
            }
        }
        return sub;
    }

    @Override
    public List<PageSub> getPageSubByCodes(String accountID, String period, List<String> codes) throws BusinessException {
        List<RedisSub> list = getRedisSubMessList(accountID, period);
        Iterator<RedisSub> iterator = list.iterator();
        Map<String, RedisSub> map = new HashMap<>();
        while (iterator.hasNext()) {
            RedisSub redisSub = (RedisSub) iterator.next();
            map.put(redisSub.getSub_code(), redisSub);
        }
        ParentCodeMapping pp = new ParentCodeMapping();
        List<PageSub> arr = new ArrayList<>();
        BigDecimal zero1 = new BigDecimal(0);
        for (String subCode : codes) {
            RedisSub redisSub = map.get(subCode);
            if (redisSub == null) {
                throw new BusinessException("vatservice getPageSubByCodes redisSub==null");
            }
            PageSub pageSub = new PageSub();
            pageSub.setPkSubId(redisSub.getPk_sub_id());
            pageSub.setSubName(redisSub.getSub_name());
            pageSub.setSubCode(redisSub.getSub_code());
            pageSub.setFullName(redisSub.getFull_name());
            pageSub.setDir(pp.getDir(redisSub.getSub_code()));
            pageSub.setEndingBalanceCredit(redisSub.getEnding_balance_credit() == null ? zero1 : redisSub.getEnding_balance_credit());
            pageSub.setEndingBalanceDebit(redisSub.getEnding_balance_debit() == null ? zero1 : redisSub.getEnding_balance_debit());

            arr.add(pageSub);
        }
        return arr;
    }


    public boolean removeRedisSub(String accountID, String period, String subCode) throws BusinessException {
        List<RedisSub> allSub = getAllSub(accountID, period);
        RedisSub sub = null;
        for (RedisSub redisSub : allSub) {
            if (redisSub.getSub_code().equals(subCode)) {
                sub = redisSub;
                break;
            }
        }
        if (sub != null) {
            boolean remove = allSub.remove(sub);
            setRedisSubMessList(accountID, period, allSub);
            return remove;
        }
        return false;
    }


    @Override
    public boolean removeCacheSub(String accountID, String period, String subCode) throws BusinessException {
        return removeRedisSub(accountID, period, subCode);
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    //结转增值税
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> zzsCarryover(Map<String, Object> param) throws BusinessException {
        Map<String, Object> res = new HashMap<>();
        String subcode2 = null;
        String subcode = null;
        Double tax = 0.00;
        subinit(param);
        Double a = 0.0;
        Double b = 0.0;
        BigDecimal initDebitCurrtLiuDi = new BigDecimal(0);
        Integer maxVoucherNo = getMaxVoucherNo();

        // 应交税费_应交增值税
        //应交税费_应交增值税_进项税
        //应交税费_应交增值税_销项税
        // 应交税费_应交增值税_进项税转出
        //应交税费_应交增值税_留抵税

        // 应交税费_应交增值税_转出未交增值税
        //// 应交税费_未交增值税

        Map<String, String> mappingCode = (Map<String, String>) param.get("mappingCode");

        String[] arr = {"未交增值税", "进项税", "销项税", "进项税转出", "留抵税", "转出未交增值税"};
        Map<String, SubjectMessage> subMap = getSubMessageByCode(account_id.get(), mappingCode, arr);

        try {
            SubjectMessage subject1 = querySubByCode("2221");
            SubjectMessage subjecjx = subMap.get("进项税");
            SubjectMessage subjecxx = subMap.get("销项税");
            SubjectMessage subjecjxzc = subMap.get("进项税转出");

            BigDecimal zc = new BigDecimal(0);

            zc = StringUtil.bigSubtract(subjecjxzc.getEnding_balance_credit(), subjecjxzc.getEnding_balance_debit());
            Double jxzc = zc.doubleValue();


            a = StringUtil.bigDecimalIsNull(subjecjx.getEnding_balance_debit()).doubleValue();

            b = StringUtil.bigDecimalIsNull(subjecxx.getEnding_balance_credit()).doubleValue();

            SubjectMessage subject3 = subMap.get("留抵税");

            String jx_name = subjecjx.getSub_name();
            String xx_name = subjecxx.getSub_name();
            String liudi_name = subject3.getSub_name();
            String jxzc_name = subjecjxzc.getSub_name();

            if (subject3 != null && subject3.getEnding_balance_debit() != null)
                initDebitCurrtLiuDi = subject3.getEnding_balance_debit();

            tax = b - a - initDebitCurrtLiuDi.doubleValue() + jxzc;

            if (tax < 0.0) {
                tax = b - a + jxzc;

                Voucher voucher1 = new Voucher();

                VoucherHead voHead1 = new VoucherHead();
                voHead1.setVouchID(UUIDUtils.getUUID());
                voHead1.setAccountID(account_id.get());
                voHead1.setVcDate(new Date());
                voHead1.setCurrency("人民币");
                voHead1.setDes("结转本月增值税");
                voHead1.setCurrencyID(null);// 币别ID
                voHead1.setUpdatePsn(user_name.get());
                voHead1.setUpdatedate(new Date());
                voHead1.setUpdatePsnID(user_id.get());
                voHead1.setCreatepsn(user_name.get());
                voHead1.setCreatePsnID(user_id.get());
                voHead1.setCreateDate(System.currentTimeMillis());
                voHead1.setCheckedDate(new Date());
                voHead1.setPeriod(period_acc.get());
                voHead1.setSource(131);
                voHead1.setTotalCredit(tax);
                voHead1.setTotalDbit(tax);
                voHead1.setVouchFlag(1);
                voHead1.setVoucherNO(getMaxVoucherNo());

                String vouchID1 = voHead1.getVouchID();

                vatDao.insertVoHead(voHead1);
                List<VoucherBody> list1 = new ArrayList<>();

                for (int i = 1; i <= 4; i++) {
                    // 构建凭证子表
                    VoucherBody voBody = new VoucherBody();
                    voBody.setVouchAID(UUIDUtils.getUUID());
                    voBody.setVouchID(vouchID1);
                    voBody.setRowIndex(String.valueOf(i));
                    voBody.setUpdatePsn(user_name.get());
                    voBody.setUpdatePsnID(user_id.get());
                    voBody.setUpdatedate(new Date());
                    voBody.setPrice(null);
                    voBody.setNumber(null);
                    voBody.setPeriod(period_acc.get());
                    voBody.setVcunit("RMB");
                    voBody.setVcunitID(null);
                    voBody.setAccountID(account_id.get());
                    voBody.setVcabstact("结转本月增值税");
                    if (i == 1) {
                        voBody.setVcsubject(subjecxx.getFull_name());
                        voBody.setSubjectID(subjecxx.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(b);
                        voBody.setCreditAmount(null);
                        if (b != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list1.add(voBody);
                        }
                    } else if (i == 2) {
                        voBody.setVcsubject(subjecjxzc.getFull_name());
                        voBody.setSubjectID(subjecjxzc.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(jxzc);
                        voBody.setCreditAmount(null);
                        if (jxzc != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list1.add(voBody);
                        }
                    } else if (i == 3) {
                        voBody.setVcsubject(subject3.getFull_name());
                        voBody.setSubjectID(subject3.getSub_code());
                        //// 小于0越来越多 销项-进项+进项转出 小于0 说明亏本 亏本就不要缴税，可以抵充留底税
                        if (tax < 0) {
                            voBody.setDirection("1");
                            voBody.setDebitAmount(Math.abs(tax));
                            voBody.setCreditAmount(null);
                        } else {
                            //	// 大于0越来越小
                            voBody.setDirection("2");
                            voBody.setDebitAmount(null);
                            voBody.setCreditAmount(Math.abs(tax));
                        }
                        if (tax != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list1.add(voBody);
                        }
                    } else if (i == 4) {
                        voBody.setVcsubject(subjecjx.getFull_name());
                        voBody.setSubjectID(subjecjx.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(a);
                        if (a != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list1.add(voBody);
                        }
                    }
                }

                // 进项为0 销项为0 转出为0处理
                if (!list1.isEmpty()) {
                    voucher1.setVoucherHead(voHead1);
                    voucher1.setVoucherBodyList(list1);
                    Map<String, Object> m1 = new HashMap<>();
                    m1.put("accountID", account_id.get());
                    m1.put("busDate", period_acc.get());

                    boolean bool = checkVouch(m1, voucher1);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher1);
                    }
                } else {
                    voucherHeadDao.deleteVouHeadByID(voHead1.getVouchID());
                }


                /************************************/

                res.put("message", "success");
                return res;

            }

            if (tax > 0) {

                // 销售税额 贷 1000
                // 进项税额 借 800
                // 留底税额
                // 留底税额 借 200


                // SubjectMessage subjecOut = querySub("转出未交增值税", supperCode,
                // "10");
                SubjectMessage subjecOut = subMap.get("转出未交增值税");
                // SubjectMessage subNoSubmit = querySub("未交增值税", "2221", "7");
                SubjectMessage subNoSubmit = subMap.get("未交增值税");


                Voucher voucher = new Voucher();

                VoucherHead voHead = new VoucherHead();
                voHead.setVouchID(UUIDUtils.getUUID());
                voHead.setAccountID(account_id.get());
                voHead.setVcDate(new Date());
                voHead.setCurrency("人民币");
                voHead.setDes("结转本月增值税");
                voHead.setCurrencyID(null);
                voHead.setUpdatePsn(user_name.get());
                voHead.setUpdatedate(new Date());
                voHead.setUpdatePsnID(user_id.get());
                voHead.setCreatepsn(user_name.get());
                voHead.setCreatePsnID(user_id.get());
                voHead.setCreateDate(System.currentTimeMillis());
                voHead.setCheckedDate(new Date());
                voHead.setPeriod(period_acc.get());
                voHead.setSource(13);
                voHead.setTotalCredit(tax);
                voHead.setTotalDbit(tax);
                voHead.setVouchFlag(1);
                voHead.setVoucherNO(getMaxVoucherNo());

                String vouchID = voHead.getVouchID();
                voucher.setVoucherHead(voHead);
                vatDao.insertVoHead(voHead);
                List<VoucherBody> list = new ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    VoucherBody voBody = new VoucherBody();
                    voBody.setVouchAID(UUIDUtils.getUUID());
                    voBody.setVouchID(vouchID);
                    voBody.setRowIndex(i + "");
                    voBody.setUpdatePsn(user_name.get());
                    voBody.setUpdatePsnID(user_id.get());
                    voBody.setUpdatedate(new Date());
                    voBody.setPrice(null);
                    voBody.setNumber(null);
                    voBody.setPeriod(period_acc.get());
                    voBody.setVcunit("RMB");
                    voBody.setVcunitID(null);
                    voBody.setAccountID(account_id.get());
                    voBody.setVcabstact("结转本月增值税");
                    if (i == 1) {
                        voBody.setVcsubject(subjecxx.getFull_name()); //// 应交税费_应交增值税_销项税
                        voBody.setSubjectID(subjecxx.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(b);
                        voBody.setCreditAmount(null);
                        if (b != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 2) {
                        voBody.setVcsubject(subjecjxzc.getFull_name()); //// 应交税费_应交增值税_进项税转出
                        voBody.setSubjectID(subjecjxzc.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(jxzc);
                        voBody.setCreditAmount(null);
                        if (jxzc != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 3) {
                        voBody.setVcsubject(subjecjx.getFull_name()); //// 应交税费_应交增值税_进项税
                        voBody.setSubjectID(subjecjx.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(a);
                        if (a != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 4) {
                        voBody.setVcsubject(subject3.getFull_name()); //// 应交税费_应交增值税_留底税
                        voBody.setSubjectID(subject3.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(initDebitCurrtLiuDi.doubleValue());
                        if (initDebitCurrtLiuDi.doubleValue() != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 5) {
                        // 转出未交增值税
                        // 贷
                        // 应交税费_应交增值税_转出未交增值税
                        //// 这个是临时凭证 跟第六条分录是反的
                        voBody.setVcsubject(subjecOut.getFull_name());
                        voBody.setSubjectID(subjecOut.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(tax);
                        if (tax != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 6) {
                        // 借
                        // 转出未交增值税
                        // 应交税费_应交增值税_转出未交增值税
                        voBody.setVcsubject(subjecOut.getFull_name());
                        voBody.setSubjectID(subjecOut.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(tax);
                        voBody.setCreditAmount(null);
                        if (tax != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 7) {
                        // 贷
                        // 应交税费_未交增值税
                        voBody.setVcsubject(subNoSubmit.getFull_name());
                        voBody.setSubjectID(subNoSubmit.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(tax);
                        if (tax != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    }
                }
                if (!list.isEmpty()) {
                    voucher.setVoucherBodyList(list);
                    Map<String, Object> m1 = new HashMap<>();
                    m1.put("accountID", account_id.get());
                    m1.put("busDate", period_acc.get());
                    // 科目联动更新
                    boolean bool = checkVouch(m1, voucher);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher);
                    }
                } else {
                    voucherHeadDao.deleteVouHeadByID(voHead.getVouchID());
                }

                res.put("result", voucher);
                res.put("message", "success");
                return res;
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();

            throw new BusinessException(e);
        }
        res.put("message", "success");
        return res;
    }


    @SuppressWarnings({"unused", "unchecked"})
    @Override
    //小规模结转增值税业务逻辑
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> smallZzsCarryover(Map<String, Object> param) throws BusinessException {
        try {

            String accountID = (String) param.get("accountID");
            String period = (String) param.get("period");
            Map<String, String> mappingCode = getAllMappingSubCode(accountID);

            String[] arr = {"应交增值税", "未交增值税", "主营业务收入"};

            //"营业外收入"科目， 没有映射需要单独查询   6301
            SubjectMessage yywsr_subjectMessage = querySubByCode("6301");
            if (yywsr_subjectMessage == null) {
                throw new BusinessException("vatservice smallZzsCarryover yywsr_subjectMessage is null");
            }

            Map<String, SubjectMessage> subMap = getSubMessageByCode(accountID, mappingCode, arr);

            SubjectMessage yjzzs_subjectMessage = subMap.get("应交增值税");
            SubjectMessage wjzzs_subjectMessage = subMap.get("未交增值税");
            SubjectMessage zyywsr_subjectMessage = subMap.get("主营业务收入");


            //计算主营业务收入 期末贷 余额
            BigDecimal ending_balance_credit = StringUtil.bigSubtract(zyywsr_subjectMessage.getEnding_balance_credit(), zyywsr_subjectMessage.getEnding_balance_debit());


            double zysr_amount = ending_balance_credit.doubleValue();

            //第一种 主营业务收入不超过30万  生成凭证
            if (zysr_amount < 300000) {
                Voucher voucher = new Voucher();

                //借：应交税费--应交增值税3
                //贷： 营业外收入    3

                //计算应交增值税贷方余额  结转到营业外收入
                BigDecimal yjzzs_ending_balance_credit = StringUtil.bigSubtract(yjzzs_subjectMessage.getEnding_balance_credit(), yjzzs_subjectMessage.getEnding_balance_debit());

                VoucherHead voHead = new VoucherHead();
                voHead.setVouchID(UUIDUtils.getUUID());
                voHead.setAccountID(account_id.get());
                voHead.setVcDate(new Date());
                voHead.setCurrency("人民币");
                voHead.setDes("结转本月增值税");
                voHead.setCurrencyID(null);
                voHead.setUpdatePsn(user_name.get());
                voHead.setUpdatedate(new Date());
                voHead.setUpdatePsnID(user_id.get());
                voHead.setCreatepsn(user_name.get());
                voHead.setCreatePsnID(user_id.get());
                voHead.setCreateDate(System.currentTimeMillis());
                voHead.setCheckedDate(new Date());
                voHead.setPeriod(period_acc.get());
                voHead.setSource(13);
                voHead.setTotalCredit(yjzzs_ending_balance_credit.doubleValue());
                voHead.setTotalDbit(yjzzs_ending_balance_credit.doubleValue());
                voHead.setVouchFlag(1);
                voHead.setVoucherNO(getMaxVoucherNo());

                String vouchID = voHead.getVouchID();
                voucher.setVoucherHead(voHead);
                vatDao.insertVoHead(voHead);
                List<VoucherBody> list = new ArrayList<>();
                for (int i = 1; i <= 2; i++) {
                    VoucherBody voBody = new VoucherBody();
                    voBody.setVouchAID(UUIDUtils.getUUID());
                    voBody.setVouchID(vouchID);
                    voBody.setRowIndex(i + "");
                    voBody.setUpdatePsn(user_name.get());
                    voBody.setUpdatePsnID(user_id.get());
                    voBody.setUpdatedate(new Date());
                    voBody.setPrice(null);
                    voBody.setNumber(null);
                    voBody.setPeriod(period_acc.get());
                    voBody.setVcunit("RMB");
                    voBody.setVcunitID(null);
                    voBody.setAccountID(account_id.get());
                    voBody.setVcabstact("结转本月增值税");
                    if (i == 1) {
                        voBody.setVcsubject(yjzzs_subjectMessage.getFull_name()); //// 应交税费_应交增值税
                        voBody.setSubjectID(yjzzs_subjectMessage.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(yjzzs_ending_balance_credit.doubleValue());
                        voBody.setCreditAmount(null);
                        if (yjzzs_ending_balance_credit.doubleValue() != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 2) {
                        voBody.setVcsubject(yywsr_subjectMessage.getFull_name()); //// 6301 营业外收入
                        voBody.setSubjectID(yywsr_subjectMessage.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(yjzzs_ending_balance_credit.doubleValue());
                        if (yjzzs_ending_balance_credit.doubleValue() != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    }

                }
                if (!list.isEmpty()) {
                    voucher.setVoucherBodyList(list);
                    Map<String, Object> m1 = new HashMap<>();
                    m1.put("accountID", account_id.get());
                    m1.put("busDate", period_acc.get());
                    // 科目联动更新
                    boolean bool = checkVouch(m1, voucher);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher);
                    }
                } else {
                    voucherHeadDao.deleteVouHeadByID(voHead.getVouchID());
                }
            } else {
                //第二种主营业务收入超过30万元需要交税分录如下：(取贷方余额判断)

                //a增值税 b附增税


                //a结转增值税凭证：
                //借：应交税费--应交增值税     3（金额）
                //贷： 应交税费--未交增值税    3

                //b结转附增税凭证：
                ///借： 营业税金及附加     3（金额）
                //公式计算  营业税金及附加 =应交增值税*0.12

                //贷： 应交税费——应交城市维护建设税
                //贷： 应交税费——教育费附加
                //贷： 应交税费—— 地方教育费附加


                //c交税时： 不需要系统生成凭证 ，会计自己录
                //借：应交税费--未交增值税 3
                //贷：银行存款           3

                Voucher voucher = new Voucher();

                //第一种 主营业务收入不超过30万  生成凭证
                //借：应交税费--应交增值税3
                //贷： 营业外收入    3


                //计算应交增值税贷方余额  结转到未交增值税
                BigDecimal yjzzs_ending_balance_credit = StringUtil.bigSubtract(yjzzs_subjectMessage.getEnding_balance_credit(), yjzzs_subjectMessage.getEnding_balance_debit());

                VoucherHead voHead = new VoucherHead();
                voHead.setVouchID(UUIDUtils.getUUID());
                voHead.setAccountID(account_id.get());
                voHead.setVcDate(new Date());
                voHead.setCurrency("人民币");
                voHead.setDes("结转本月增值税");
                voHead.setCurrencyID(null);
                voHead.setUpdatePsn(user_name.get());
                voHead.setUpdatedate(new Date());
                voHead.setUpdatePsnID(user_id.get());
                voHead.setCreatepsn(user_name.get());
                voHead.setCreatePsnID(user_id.get());
                voHead.setCreateDate(System.currentTimeMillis());
                voHead.setCheckedDate(new Date());
                voHead.setPeriod(period_acc.get());
                voHead.setSource(13);
                voHead.setTotalCredit(yjzzs_ending_balance_credit.doubleValue());
                voHead.setTotalDbit(yjzzs_ending_balance_credit.doubleValue());
                voHead.setVouchFlag(1);
                voHead.setVoucherNO(getMaxVoucherNo());

                String vouchID = voHead.getVouchID();
                voucher.setVoucherHead(voHead);
                vatDao.insertVoHead(voHead);
                List<VoucherBody> list = new ArrayList<>();
                for (int i = 1; i <= 2; i++) {
                    VoucherBody voBody = new VoucherBody();
                    voBody.setVouchAID(UUIDUtils.getUUID());
                    voBody.setVouchID(vouchID);
                    voBody.setRowIndex(i + "");
                    voBody.setUpdatePsn(user_name.get());
                    voBody.setUpdatePsnID(user_id.get());
                    voBody.setUpdatedate(new Date());
                    voBody.setPrice(null);
                    voBody.setNumber(null);
                    voBody.setPeriod(period_acc.get());
                    voBody.setVcunit("RMB");
                    voBody.setVcunitID(null);
                    voBody.setAccountID(account_id.get());
                    voBody.setVcabstact("结转本月增值税");
                    if (i == 1) {
                        voBody.setVcsubject(yjzzs_subjectMessage.getFull_name()); //// 应交税费_应交增值税
                        voBody.setSubjectID(yjzzs_subjectMessage.getSub_code());
                        voBody.setDirection("1");
                        voBody.setDebitAmount(yjzzs_ending_balance_credit.doubleValue());
                        voBody.setCreditAmount(null);
                        if (yjzzs_ending_balance_credit.doubleValue() != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    } else if (i == 2) {
                        voBody.setVcsubject(wjzzs_subjectMessage.getFull_name()); //// 应交税费_未交增值税
                        voBody.setSubjectID(wjzzs_subjectMessage.getSub_code());
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        voBody.setCreditAmount(yjzzs_ending_balance_credit.doubleValue());
                        if (yjzzs_ending_balance_credit.doubleValue() != 0.0) {
                            vatDao.insertVoBody(voBody);
                            list.add(voBody);
                        }
                    }
                }

                if (!list.isEmpty()) {
                    voucher.setVoucherBodyList(list);
                    Map<String, Object> m1 = new HashMap<>();
                    m1.put("accountID", account_id.get());
                    m1.put("busDate", period_acc.get());
                    // 科目联动更新
                    boolean bool = checkVouch(m1, voucher);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher);
                    }
                } else {
                    voucherHeadDao.deleteVouHeadByID(voHead.getVouchID());
                }

                //如果 有应交增值税的话 ，那么 就要 结转附增税

                if (yjzzs_ending_balance_credit.doubleValue() != 0.0) {

                    //附赠税结转

                    String[] arr2 = {"应交增值税", "应交城市维护建设税", "教育费附加", "地方教育费附加"};
                    Map<String, SubjectMessage> subMap2 = getSubMessageByCode(accountID, mappingCode, arr2);

                    SubjectMessage yjzzs = subMap2.get("应交增值税");  //应交增值税已经结转到了 应交税费_未交增值税贷方，计算附加税的时候应该取未交增值税贷方的值

                    //b结转附增税凭证：
                    ///借： 营业税金及附加     3（金额）
                    //公式计算  营业税金及附加 =应交增值税*0.12

                    //贷： 应交税费——应交城市维护建设税
                    //贷： 应交税费——教育费附加
                    //贷： 应交税费—— 地方教育费附加

                    //计算应交增值税 取借方余额
                    String totalDbit = yjzzs_ending_balance_credit.toString();

                    SubjectMessage subCity2 = subMap2.get("应交城市维护建设税");
                    SubjectMessage subEducation2 = subMap2.get("教育费附加");
                    SubjectMessage subLocalEducation2 = subMap2.get("地方教育费附加");

                    String mheader12 = StringUtil.multiplys(totalDbit, "0.12");
                    String mheader7 = StringUtil.multiplys(totalDbit, "0.07");
                    String mheader3 = StringUtil.multiplys(totalDbit, "0.03");
                    String mheader2 = StringUtil.multiplys(totalDbit, "0.02");


                    SubjectMessage mainBussiness = querySubByCode("6001");

                    SubjectMessage otherBussiness = querySubByCode("6051");

                    BigDecimal current_mainBussiness_amount_credit = StringUtil.bigDecimalIsNull(mainBussiness.getCurrent_amount_credit());
                    String mainBussinessAmountCredit = current_mainBussiness_amount_credit.toString();

                    BigDecimal current_otherBussiness_amount_credit = StringUtil.bigDecimalIsNull(otherBussiness.getCurrent_amount_credit());
                    String otherBussinessAmountCredit = current_otherBussiness_amount_credit.toString();

                    BigDecimal getSum = current_mainBussiness_amount_credit.add(current_otherBussiness_amount_credit);

                    Voucher voucher2 = new Voucher();

                    VoucherHead vohead = new VoucherHead();

                    vohead.setVouchID(UUIDUtils.getUUID());
                    vohead.setAccountID(account_id.get());
                    vohead.setVcDate(new Date());
                    vohead.setCurrency("人民币");
                    vohead.setDes(period_acc.get().split("-")[1] + "月份结转本月增值税附加税"); // 备注
                    vohead.setCurrencyID(null);// 币别ID
                    vohead.setUpdatePsn(user_id.get());// 修改人
                    vohead.setUpdatedate(new Date());// 修改时间
                    vohead.setUpdatePsnID(user_id.get());
                    vohead.setCreatepsn(user_id.get()); // 创建人
                    vohead.setCreatePsnID(user_id.get()); // 创建人ID
                    vohead.setCreateDate(System.currentTimeMillis());// 创建日期
                    vohead.setPeriod(period_acc.get()); // 期间
                    vohead.setSource(14);
                    vohead.setTotalCredit(Double.valueOf(mheader12));
                    vohead.setTotalDbit(Double.valueOf(mheader12));
                    vohead.setVouchFlag(1);
                    vohead.setVoucherNO(getMaxVoucherNo());
                    vatDao.insertVoHead(vohead);
                    List<VoucherBody> list2 = new ArrayList<>();
                    for (int i = 1; i <= 4; i++) {

                        VoucherBody voBody = new VoucherBody();
                        voBody.setVouchAID(UUIDUtils.getUUID());
                        voBody.setVouchID(vohead.getVouchID());
                        voBody.setRowIndex(i + "");
                        voBody.setUpdatePsn(user_name.get());
                        voBody.setUpdatePsnID(user_id.get());
                        voBody.setUpdatedate(new Date());
                        voBody.setPrice(null); // 单价
                        voBody.setNumber(null); // 数量
                        voBody.setPeriod(period_acc.get());// 期间
                        voBody.setVcunit("RMB"); // 计量单位
                        voBody.setVcunitID(null); // 计量单位ID
                        voBody.setAccountID(account_id.get()); // 账套ID
                        voBody.setVcabstact("结转本月增值税附加税"); // 摘要
                        voBody.setDirection("2");
                        voBody.setDebitAmount(null);
                        if (i == 1) {
                            voBody.setVcsubject("营业税金及附加");
                            voBody.setDebitAmount(Double.parseDouble(mheader12));
                            voBody.setCreditAmount(null);
                            voBody.setDirection("1");
                            voBody.setSubjectID("6403");
                            if (voBody.getDebitAmount() > 0.0) {
                                vatDao.insertVoBody(voBody);
                                list2.add(voBody);
                            }
                        } else if (i == 2) {
                            voBody.setVcsubject(subCity2.getFull_name());
                            voBody.setCreditAmount(Double.parseDouble(mheader7));
                            voBody.setSubjectID(subCity2.getSub_code());
                            if (voBody.getCreditAmount() > 0.0) {
                                vatDao.insertVoBody(voBody);
                                list2.add(voBody);
                            }
                        } else if (i == 3) {
                            voBody.setVcsubject(subEducation2.getFull_name());
                            voBody.setCreditAmount(Double.parseDouble(mheader3));
                            voBody.setSubjectID(subEducation2.getSub_code());
                            if (voBody.getCreditAmount() > 0.0) {
                                vatDao.insertVoBody(voBody);
                                list2.add(voBody);
                            }
                        } else if (i == 4) {
                            voBody.setVcsubject(subLocalEducation2.getFull_name());
                            voBody.setCreditAmount(Double.parseDouble(mheader2));
                            voBody.setSubjectID(subLocalEducation2.getSub_code());
                            if (voBody.getCreditAmount() > 0.0) {
                                vatDao.insertVoBody(voBody);
                                list2.add(voBody);
                            }
                        }
                    }

                    voucher2.setVoucherHead(vohead);
                    voucher2.setVoucherBodyList(list2);

                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("accountID", account_id.get());
                    hashMap.put("busDate", period_acc.get());
                    boolean bool = checkVouch(hashMap, voucher2);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(hashMap, voucher2);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    @Transactional
    public void upSubAmount(Double amount, String direction, String sub_code) throws BusinessException {
        if (amount != null && amount != 0.0 && !StringUtil.isEmpty(sub_code)) {
            HashMap<String, Object> subMap = new HashMap<>();
            subMap.put("amount", amount);
            subMap.put("direction", direction);
            subMap.put("sub_code", sub_code);
            updateSub(subMap);
        }
    }

    // 更新数据库字段 source type
    //@Transactional(rollbackFor = BusinessException.class)
    public void modifyAcc() throws BusinessException {
        try {
            int count = 0;
            List<User> userList = vatDao.getAllUser();
            if (userList != null && userList.size() > 0)
                for (int i = 0; i < userList.size(); i++) {
                    System.out.println("..........  " + i);
                    User user = userList.get(i);
                    // loginUser 手机号 但是 admin sld abc 要排除
                    if (user != null && user.getUserType() != null && user.getLoginUser().length() > 6) {
                        String userID = user.getUserID();
                        Integer userType = user.getUserType();
                        String parentId = user.getParentUser();

                        String type = userType.toString(); // 账套类型等于用户类型
                        String source = null;
                        if (userType == 3 || userType == 2)
                            source = userID;
                        if (userType == 5 || userType == 6)
                            source = parentId;
                        Map<String, Object> mp1 = new HashMap<>();
                        mp1.put("userID", userID);

                        List<Account> accList = vatDao.getAccByUid(mp1);

                        if (accList != null && accList.size() > 0)
                            for (int j = 0; j < accList.size(); j++) {

                                Account account = accList.get(j);

                                if (account != null) {
                                    Map<String, Object> map = new HashMap<>();
                                    if (source != null && source != "") {
                                        String accountID = account.getAccountID();
                                        map.put("accountID", accountID);
                                        map.put("source", source);
                                        map.put("type", type);
                                        vatDao.upAccSource(map);
                                        count++;
                                        System.out.println("uid = " + userID + " ,,, " + "aid = " + accountID + " OK");
                                    }
                                }
                            }
                    }
                }
            System.out.println("一共更新条数。。。。。。  " + count);
            System.out.println("总条数。。。");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }// 更新数据库字段 source type


    @Override
    public void subinit(Map<String, Object> param) {
        subinit((String) param.get("accountID"), (String) param.get("period"), (String) param.get("userID"),
                (String) param.get("userName"));
    }

    @Override
    public void subinit(User user, Account account) {
        subinit(account.getAccountID(), account.getUseLastPeriod(), user.getUserID(), user.getUserName());
    }

    @SuppressWarnings("static-access")
    @Override
    public void subinit(String accountID, String budaDate, String userID, String userName) {
        account_id.set(accountID);
        period_acc.set(budaDate);
        user_id.set(userID);
        user_name.set(userName == null ? userID : userName);

    }


    @Override
    public String getNumber(String superCode, String len, String num) throws BusinessException {
        SubjectMessage sub = querySub(null, superCode, len);
        String MaxCode = sub != null ? sub.getSub_code() : num;
        BigDecimal bigDecimal = new BigDecimal(MaxCode);
        BigDecimal add = bigDecimal.add(new BigDecimal("1"));
        DecimalFormat format = new DecimalFormat("0");
        return format.format(add);
    }

    @Override
    public SubjectMessage querySub(String subName, String code, String len) throws BusinessException {
        return querySub(subName, code, len, null);
    }

    @Override
    public SubjectMessage querySub(String subName, String code, String len, String flag) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("sub_name", subName);
        map.put("sub_code", code);
        map.put("sub_length", len);
        map.put("account_period", period_acc.get());
        map.put("account_id", account_id.get());
        map.put("flag", flag);
        return vatDao.querySubjectVatTwo(map);
    }

    @Override
    @Transactional
    public void updateSubjest(SubjectMessage sub, Map<String, Object> map) throws BusinessException {
        Map<String, Object> updateSubjest = tBasicSubjectMessageMapper.updateSubjest(sub, map);
    }

    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    // 测试方法
    public Map<String, Object> test1() throws BusinessException {
        String uuid = null;
        Map<String, Object> result = new HashMap<>();
        String accountID = "bb31106948194fce97afec2f58f1392a";
        String period = "2018-02";
        try {
            Map<String, Object> par = new HashMap<>();
            par.put("pk_sub_id", "2cc64e65ced24376abc9b23dc7bf43bb");
            SubjectMessage submMessage = vatDao.queryByID("2cc64e65ced24376abc9b23dc7bf43bb");


            SubjectMessage sub = new SubjectMessage();


            BigDecimal default_val = null;
            // 这里一定要注册默认值，使用null也可以
            BigDecimalConverter bd = new BigDecimalConverter(default_val);
            ConvertUtils.register(bd, java.math.BigDecimal.class);
            BeanUtils.copyProperties(sub, submMessage);

            //新增科目后的主键
            uuid = UUIDUtils.getUUID();
            sub.setPk_sub_id(uuid);
            sub.setSub_name("宝安平安支行");
            sub.setFull_name("银行存款_平安银行_宝安平安支行");
            sub.setSub_code("1002001001");

            int insertSubject = vatDao.insertSubject(sub);


            //添加到缓存里面去
            RedisSub ss = StringUtil.subjectMessageToredisSub(sub);
            String res = addRedisSubMessList(accountID, period, ss);
            System.out.println(res);

            //这里制造错误
            int a = 5;
            int c = a / 0;

            System.out.println("error");
            return result;
            // 测试抛异常 是否回滚插入的sql
        } catch (Exception e) {
            e.printStackTrace();
            SubjectMessage submMessage = vatDao.queryByID(uuid); //回滚之后再查询是否数据已经保存到数据库里面去了

            Map<String, Object> par = new HashMap<>();
            par.put("sub_code", "1002001");
            par.put("account_id", accountID);
            par.put("account_period", period);

            List<SubjectMessage> list = vatDao.querySubjectVatThree(par);
            if (list != null) {
                for (SubjectMessage sss : list) {
                    System.out.println(sss.getSub_code() + "---" + sss.getSub_name());
                }
            }


            List<RedisSub> redisSubMessList = getRedisSubMessList("bb31106948194fce97afec2f58f1392a", "2018-02");
            RedisSub re = null;
            for (RedisSub redisSub : redisSubMessList) {
                if (redisSub.getSub_code().equals("1002001001")) {
                    re = redisSub;
                    System.out.println(redisSub);
                }
            }
            System.out.println(re);
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    public Map<String, Object> ret(Integer code, String msg) {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("code", code.toString());
        res.put("msg", msg);
        return res;
    }

    public List<KcCommodity> queryCommBysubCode(String code, String accountiD, String period) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", accountiD);
        map.put("busDate", period);
        map.put("subjectID", code);
        return commodityDao.queryBysubCode(map);
    }

    @Override
    // 查询凭证头
    public List<VoucherBody> queryVoBody(Map<String, Object> map) throws BusinessException {
        return vatDao.queryVoBody(map);
    }

    @Override
    // 查询凭证体
    public VoucherHead queryVoHeahder(Map<String, Object> map) throws BusinessException {
        return vatDao.queryVoHeahder(map);
    }


    @Override
    // @Transactional
    public SubjectMessage createSub(String subCode, String parentCode, String subName, String fullName)
            throws BusinessException {
        return createSub(subCode, parentCode, subName, fullName, null, null, null);
    }


    @Override
    // @Transactional
    public SubjectMessage createNewSub(String parentCode, String subName, String fullName, String period, String aid,
                                       String flg) throws BusinessException {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("sub_code", parentCode);
            map.put("account_period", period);
            map.put("account_id", aid);
            SubjectMessage sub = vatDao.querySubjectVatTwo(map);
            String defaultCode = parentCode + "000";
            String maxCode = sub != null ? sub.getSub_code() : defaultCode;
            BigDecimal bigMaxCode = new BigDecimal(maxCode);
            BigDecimal add = bigMaxCode.add(new BigDecimal("1"));
            DecimalFormat format = new DecimalFormat("0");
            String new_code = format.format(add);
            SubjectMessage createSub = createSub(new_code, parentCode, subName, fullName);
            return createSub;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    // @Transactional
    public SubjectMessage createSub(String subCode, String parentCode, String subName, String fullName,
                                    String subSource, String unit, String number) throws BusinessException {
        SubjectMessage subject = new SubjectMessage();
        Map<String, String> sub = new HashMap<>();
        String id = UUIDUtils.getUUID();
        sub.put("pk_sub_id", id);
        sub.put("sub_code", subCode);
        sub.put("sub_name", subName);
        sub.put("superior_coding", parentCode);
        sub.put("full_name", fullName);

        String times = DateUtil.getTime();


        sub.put("user_id", user_id.get());
        sub.put("account_id", account_id.get());
        sub.put("account_period", period_acc.get());
        sub.put("excel_import_period", null);
        sub.put("type_of_currency", null);
        sub.put("init_credit_balance", "0");
        sub.put("init_debit_balance", "0");
        sub.put("current_amount_debit", "0");
        sub.put("current_amount_credit", "0");
        sub.put("year_amount_credit", "0");
        sub.put("year_amount_debit", "0");
        sub.put("ending_balance_credit", "0");
        sub.put("ending_balance_debit", "0");
        sub.put("excel_import_code", "");
        sub.put("is_multiple_siblings", "0");
        sub.put("siblings_coding", "");
        subSource = subSource == null ? period_acc.get() + "@" + times
                : subSource + "@" + period_acc.get() + "@" + times;
        sub.put("sub_source", subSource + "@新增");
        unit = unit == null ? "RMB" : unit;
        sub.put("unit", unit);
        sub.put("unit_id", null);
        sub.put("price", null);
        sub.put("number", number);
        sub.put("amount", "0");
        sub.put("state", "1");
        sub.put("mender", user_id.get());
        sub.put("category", subCode.substring(0, 1));
        sub.put("update_date", times);
        sub.put("update_timestamp", Long.toString(new Date().getTime()));
        sub.put("fk_t_basic_measure_id", "");
        sub.put("measure_state", "0");
        sub.put("fk_exchange_rate_id", null);
        sub.put("exchange_rate__state", "0");
        int len = (subCode.length() - 4) == 0 ? 1 : ((subCode.length() - 4) / 3 + 1);
        sub.put("code_level", String.valueOf(len));
        String direction = new ParentCodeMapping().getDir(subCode.substring(0, 4));
        sub.put("debit_credit_direction", direction);
        BeanRefUtil.setFieldValue(subject, sub);
        vatDao.insertSubject(subject);
        SubjectMessage subjectMessage = vatDao.queryByID(id);
        RedisSub redisSub = StringUtil.subjectMessageToredisSub(subjectMessage);
        addRedisSubMessList(account_id.get(), period_acc.get(), redisSub);

        return subjectMessage;
    }


    @Override
    public SubjectMessage querySubjectMessageById(String id) throws BusinessException {
        return vatDao.queryByID(id);
    }


    public TBasicSubjectMessage queryTBasicSubjectMessageById(String id) throws BusinessException {
        return vatDao.queryTBasicSubjectMessageById(id);
    }


    @Override
    @Transactional
    public VoucherHead createVoucherHead(Integer source, String des, Double credit, Double dbit)
            throws BusinessException {
        VoucherHead voHead = new VoucherHead();
        String id = UUIDUtils.getUUID();
        voHead.setVouchID(id);
        voHead.setAccountID(account_id.get());
        voHead.setVcDate(new Date());
        voHead.setCurrency("RMB");
        voHead.setCurrencyID(null);
        voHead.setUpdatePsn(user_name.get());
        voHead.setUpdatedate(new Date());
        voHead.setUpdatePsnID(user_id.get());
        voHead.setCreatepsn(user_name.get());
        voHead.setCreatePsnID(user_id.get());
        voHead.setCreateDate(System.currentTimeMillis());
        voHead.setPeriod(period_acc.get());
        voHead.setVouchFlag(1);
        voHead.setVoucherNO(getMaxVoucherNo());
        voHead.setSource(source);
        voHead.setDes(des);
        voHead.setTotalCredit(credit);
        voHead.setTotalDbit(dbit);
        vatDao.insertVoHead(voHead);
        return vatDao.queryVoHeahderById(id);
    }


    @Override
    @Transactional
    public VoucherBody createVouchBody(String vouchID, String rowIndex, String vcabstact, String vcsubject,
                                       Double debitAmount, Double creditAmount, String direction, String subCode, String vcunit)
            throws BusinessException {
        VoucherBody voBody = new VoucherBody();
        String id = UUIDUtils.getUUID();
        voBody.setVouchAID(id);
        voBody.setVouchID(vouchID);
        voBody.setRowIndex(rowIndex);
        voBody.setVcabstact(vcabstact);
        voBody.setVcsubject(vcsubject);
        voBody.setPeriod(period_acc.get());
        voBody.setVcunit(vcunit);
        voBody.setVcunitID(null);
        voBody.setDebitAmount(debitAmount);
        voBody.setCreditAmount(creditAmount);
        voBody.setAccountID(account_id.get());
        voBody.setDirection(direction);
        voBody.setSubjectID(subCode);
        voBody.setUpdatePsn(user_name.get());
        voBody.setUpdatePsnID(user_id.get());
        voBody.setUpdatedate(new Date());
        voBody.setPrice(null);
        voBody.setNumber(null);
        vatDao.insertVoBody(voBody);
        return vatDao.queryVoBodyById(id);
    }


    @SuppressWarnings("unused")
    @Override
    //附加税结转
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> fjsCarryover(Map<String, Object> param) throws BusinessException {

        subinit(param);
        String subcode = null;
        SubjectMessage maxSub2 = null;
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> vcq = new HashMap<>();
        vcq.put("accountID", account_id.get());
        vcq.put("period", period_acc.get());
        Map<String, String> mappingCode = (Map<String, String>) param.get("mappingCode");

        try {
            String[] arr = {"未交增值税", "应交城市维护建设税", "教育费附加", "地方教育费附加"};
            Map<String, SubjectMessage> subMap = getSubMessageByCode(account_id.get(), mappingCode, arr);

            SubjectMessage wjzzs = subMap.get("未交增值税");


            BigDecimal Current_amount_credit = StringUtil.bigDecimalIsNull(wjzzs.getCurrent_amount_credit());

            if (Current_amount_credit.compareTo(BigDecimal.ZERO) == 0) {
                result.put("result", "本期没有附加税结转");
                result.put("message", "fail");
                return result;
            }
            String totalDbit = Current_amount_credit.toString();


            SubjectMessage subCity2 = subMap.get("应交城市维护建设税");
            SubjectMessage subEducation2 = subMap.get("教育费附加");
            SubjectMessage subLocalEducation2 = subMap.get("地方教育费附加");

            String mheader12 = StringUtil.multiplys(totalDbit, "0.12");
            String mheader7 = StringUtil.multiplys(totalDbit, "0.07");
            String mheader3 = StringUtil.multiplys(totalDbit, "0.03");
            String mheader2 = StringUtil.multiplys(totalDbit, "0.02");


            SubjectMessage mainBussiness = querySubByCode("6001");

            SubjectMessage otherBussiness = querySubByCode("6051");

            BigDecimal current_mainBussiness_amount_credit = StringUtil.bigDecimalIsNull(mainBussiness.getCurrent_amount_credit());
            String mainBussinessAmountCredit = current_mainBussiness_amount_credit.toString();

            BigDecimal current_otherBussiness_amount_credit = StringUtil.bigDecimalIsNull(otherBussiness.getCurrent_amount_credit());
            String otherBussinessAmountCredit = current_otherBussiness_amount_credit.toString();

            BigDecimal getSum = current_mainBussiness_amount_credit.add(current_otherBussiness_amount_credit);


            Voucher voucher = new Voucher();

            VoucherHead vohead = new VoucherHead();

            vohead.setVouchID(UUIDUtils.getUUID());
            vohead.setAccountID(account_id.get());
            vohead.setVcDate(new Date());
            vohead.setCurrency("人民币");
            vohead.setDes(period_acc.get().split("-")[1] + "月份结转本月增值税附加税"); // 备注
            vohead.setCurrencyID(null);// 币别ID
            vohead.setUpdatePsn(user_id.get());// 修改人
            vohead.setUpdatedate(new Date());// 修改时间
            vohead.setUpdatePsnID(user_id.get());
            vohead.setCreatepsn(user_id.get()); // 创建人
            vohead.setCreatePsnID(user_id.get()); // 创建人ID
            vohead.setCreateDate(System.currentTimeMillis());// 创建日期
            vohead.setPeriod(period_acc.get()); // 期间
            vohead.setSource(14);
            vohead.setTotalCredit(Double.valueOf(mheader12));
            vohead.setTotalDbit(Double.valueOf(mheader12));
            vohead.setVouchFlag(1);
            vohead.setVoucherNO(getMaxVoucherNo());
            vatDao.insertVoHead(vohead);
            List<VoucherBody> list = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {

                VoucherBody voBody = new VoucherBody();
                voBody.setVouchAID(UUIDUtils.getUUID());
                voBody.setVouchID(vohead.getVouchID());
                voBody.setRowIndex(i + "");
                voBody.setUpdatePsn(user_name.get());
                voBody.setUpdatePsnID(user_id.get());
                voBody.setUpdatedate(new Date());
                voBody.setPrice(null); // 单价
                voBody.setNumber(null); // 数量
                voBody.setPeriod(period_acc.get());// 期间
                voBody.setVcunit("RMB"); // 计量单位
                voBody.setVcunitID(null); // 计量单位ID
                voBody.setAccountID(account_id.get()); // 账套ID
                voBody.setVcabstact("结转本月增值税附加税"); // 摘要
                voBody.setDirection("2");
                voBody.setDebitAmount(null);
                if (i == 1) {
                    voBody.setVcsubject("营业税金及附加");
                    voBody.setDebitAmount(Double.parseDouble(mheader12));
                    voBody.setCreditAmount(null);
                    voBody.setDirection("1");
                    voBody.setSubjectID("6403");
                    if (voBody.getDebitAmount() > 0.0) {
                        vatDao.insertVoBody(voBody);
                        list.add(voBody);
                    }
                } else if (i == 2) {
                    voBody.setVcsubject(subCity2.getFull_name());
                    voBody.setCreditAmount(Double.parseDouble(mheader7));
                    voBody.setSubjectID(subCity2.getSub_code());
                    if (voBody.getCreditAmount() > 0.0) {
                        vatDao.insertVoBody(voBody);
                        list.add(voBody);
                    }
                } else if (i == 3) {
                    voBody.setVcsubject(subEducation2.getFull_name());
                    voBody.setCreditAmount(Double.parseDouble(mheader3));
                    voBody.setSubjectID(subEducation2.getSub_code());
                    if (voBody.getCreditAmount() > 0.0) {
                        vatDao.insertVoBody(voBody);
                        list.add(voBody);
                    }
                } else if (i == 4) {
                    voBody.setVcsubject(subLocalEducation2.getFull_name());
                    voBody.setCreditAmount(Double.parseDouble(mheader2));
                    voBody.setSubjectID(subLocalEducation2.getSub_code());
                    if (voBody.getCreditAmount() > 0.0) {
                        vatDao.insertVoBody(voBody);
                        list.add(voBody);
                    }
                }
            }

            voucher.setVoucherHead(vohead);
            voucher.setVoucherBodyList(list);

            vcq.clear();
            vcq.put("accountID", account_id.get());
            vcq.put("busDate", period_acc.get());
            boolean bool = checkVouch(vcq, voucher);
            if (bool) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(vcq, voucher);
            }
            result.put("result", "结转成功");
            result.put("message", "success");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @SuppressWarnings({"unchecked", "unused"})
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> jtCarryover(Map<String, Object> param) throws BusinessException {
        HashMap<String, Object> map = new HashMap<>();
        String date = param.get("period").toString();
        String month = date.substring(5);
        if (month.indexOf("3") != -1 || month.indexOf("6") != -1 || month.indexOf("9") != -1 || month.indexOf("12") != -1) {
            System.out.println("success");
        } else {
            return ret(1, "计提没有到时间,计提时间为3月或者6月或者9月或者12月");
        }
        try {
            subinit(param);
            Map<String, String> mappingCode = (Map<String, String>) param.get("mappingCode");
            String[] arr = {"应交所得税"};
            Map<String, SubjectMessage> subMap = getSubMessageByCode(account_id.get(), mappingCode, arr);


            SubjectMessage sdsfySub = querySubByCode("6801");

            SubjectMessage zysrSub = querySubByCode("6001");
            BigDecimal zysrCurrent = StringUtil.bigDecimalIsNull(zysrSub.getCurrent_amount_credit());

            SubjectMessage qtsrSub = querySubByCode("6051");
            BigDecimal qtsrCurrent = StringUtil.bigDecimalIsNull(qtsrSub.getCurrent_amount_credit());

            SubjectMessage zycbSub = querySubByCode("6401");
            BigDecimal zycbCurrent = StringUtil.bigDecimalIsNull(zycbSub.getCurrent_amount_debit());

            SubjectMessage qtcbSub = querySubByCode("6402");
            BigDecimal qtcbCurrent = StringUtil.bigDecimalIsNull(qtcbSub.getCurrent_amount_debit());


            SubjectMessage cwfySub = querySubByCode("6603");
            BigDecimal cwfyCurrent = StringUtil.bigDecimalIsNull(cwfySub.getCurrent_amount_debit());

            SubjectMessage glfySub = querySubByCode("6602");
            BigDecimal glfyCurrent = StringUtil.bigDecimalIsNull(glfySub.getCurrent_amount_debit());

            SubjectMessage xxfySub = querySubByCode("6601");
            BigDecimal xxfyCurrent = StringUtil.bigDecimalIsNull(xxfySub.getCurrent_amount_debit());

            SubjectMessage yywsrSub = querySubByCode("6301");
            BigDecimal yywsrCurrent = StringUtil.bigDecimalIsNull(yywsrSub.getCurrent_amount_credit());

            SubjectMessage yywzcSub = querySubByCode("6711");
            BigDecimal yywzcCurrent = StringUtil.bigDecimalIsNull(yywzcSub.getCurrent_amount_debit());
            /*******************************************************************/


            SubjectMessage yjsfSub = querySubByCode("2221");

            SubjectMessage yjsdsSub = subMap.get("应交所得税");

            String subcode = yjsdsSub.getSub_code();


            BigDecimal sr = zysrCurrent.add(qtsrCurrent);

            BigDecimal cb = zycbCurrent.add(qtcbCurrent);

            BigDecimal qjfy = cwfyCurrent.add(glfyCurrent).add(xxfyCurrent);

            BigDecimal sdstotal = (sr.subtract(cb).subtract(qjfy).add(yywsrCurrent).subtract(yywzcCurrent))
                    .multiply(new BigDecimal(0.25));

            Double totalDbit = sdstotal.doubleValue();
            if (totalDbit == 0.0 || totalDbit == 0.00) {
                return ret(1, "所得税费用为0,本期没有计提企业所得税 结转");
            }
            if (totalDbit < 0) {
                return ret(1, "所得税费用为负数!");
            }

            VoucherHead voHeader = createVoucherHead(15, "企业所得税计提可以根据需要修改", totalDbit, totalDbit);
            String vcabstact = "";
            if (month.indexOf("3") != -1) {
                vcabstact = "第一季度企业所得税计提";
            } else if (month.indexOf("6") != -1) {
                vcabstact = "第二季度企业所得税计提";
            } else if (month.indexOf("9") != -1) {
                vcabstact = "第三季度企业所得税计提";
            } else if (month.indexOf("12") != -1) {
                vcabstact = "第四季度企业所得税计提";
            }

            VoucherBody v1 = createVouchBody(voHeader.getVouchID(), "1", vcabstact, "所得税费用", totalDbit, null, "1",
                    "6801", "RMB");

            VoucherBody v2 = createVouchBody(voHeader.getVouchID(), "2", vcabstact, "企业所得税", null, totalDbit, "2",
                    subcode, "RMB");

            Voucher voucher = new Voucher();
            List<VoucherBody> list = new ArrayList<>();
            list.add(v1);
            list.add(v2);
            voucher.setVoucherHead(voHeader);
            voucher.setVoucherBodyList(list);
            Map<String, Object> m1 = new HashMap<>();
            m1.put("accountID", account_id.get());
            m1.put("busDate", period_acc.get());
            boolean bool = checkVouch(m1, voucher);
            if (bool) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher);
            }
            return ret(0, "success");
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Transactional(rollbackFor = BusinessException.class)
    public void toNextKcCommPeriod(Map<String, Object> param) throws BusinessException {
        try {

            String accountID = param.get("accountID").toString();
            String period = param.get("period").toString();
            if (StringUtil.isEmpty(accountID) || StringUtil.isEmpty(period))
                throw new BusinessException("toNextKcCommPeriod 期间不存在或者账套不存在");

            Map<String, Object> querymap = new HashMap<>();

            String nextPeriod = DateUtil.getNextMonth(period);

            querymap.put("period", nextPeriod);
            querymap.put("accountID", accountID);

            List<KcCommodity> kccList = commodityDao.queryCommodityAll(querymap);
            if (kccList != null && kccList.size() > 0)
                commodityDao.delCommodityAll(querymap);
            querymap.put("period", period);

            List<KcCommodity> arr = new ArrayList<>();
            List<KcCommodity> kccommList = commodityDao.queryCommodityAll(querymap);

            if (kccommList != null && !kccommList.isEmpty()) {
                for (int k = 0; k < kccommList.size(); k++) {
                    KcCommodity comm = kccommList.get(k);
                    comm.setComID(UUIDUtils.getUUID()); // 主键
                    comm.setStartPeriod(null);
                    comm.setEndPeriod(null);
                    comm.setImportSubcode(null);
                    comm.setQc_balanceAmount(comm.getQm_balanceAmount());
                    comm.setQc_balanceNum(comm.getQm_balanceNum());
                    comm.setQc_balancePrice(comm.getQm_balancePrice());

                    comm.setBq_incomeNum(0.0);
                    comm.setBq_incomeAmount(new BigDecimal(0));
                    comm.setBq_issueNum(0.0);
                    comm.setBq_issueAmount(new BigDecimal(0));

                    comm.setPeriod(nextPeriod);

                    Date date = new Date();
                    comm.setUpdatedate(date);
                    comm.setUpdatePsn(null);
                    comm.setUpdatePsnID(null);
                    comm.setDes(period + "销售成本结转");
                    comm.setImportDate(null);
                    comm.setBalanceDate(date);

                    arr.add(comm);
                }
                //
                int num = commodityDao.insertCommBath(arr);
                if (num < 0) {
                    String sss = "Class: " + this.getClass().getName() + " method: "
                            + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:"
                            + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(sss + "toNextKcCommPeriod 数量金额表转移失败");
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    //@Transactional(rollbackFor = BusinessException.class)
    public void updateSub(Map<String, Object> param) throws BusinessException {
        try {
            // 找到科目
            if (StringUtil.objEmpty(param.get("sub_code")))
                return;
            String code = param.get("sub_code").toString();
            int length = code.length();
            for (int j = 0; j < (length / 3); j++) {
                SubjectMessage subMess = querySubByCode(code.substring(0, length - (j * 3)));
                tBasicSubjectMessageMapper.updateSubjest(subMess, param);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    public SubjectMessage querySubByCode(String sub_code) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("sub_code", sub_code);
        map.put("account_id", account_id.get());
        map.put("account_period", period_acc.get());
        return vatDao.querySubByCode(map);
    }

    @Override
    public Integer getMaxVoucherNo() throws BusinessException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("accountID", account_id.get());
        hashMap.put("period", period_acc.get());
        return voucherHeadDao.getMaxVoucherNo(hashMap);
    }

    @Override
    public Integer getMaxVoucherNo(String accountID, String period) throws BusinessException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("accountID", accountID);
        hashMap.put("period", period);
        return voucherHeadDao.getMaxVoucherNo(hashMap);
    }


    public Voucher delEmptyObject(Voucher vo) {
        List<VoucherBody> list = vo.getVoucherBodyList();
        if (vo.getVoucherHead() == null || list == null)
            return null;
        if (list.size() > 0) {
            Iterator<VoucherBody> it = list.iterator();
            while (it.hasNext()) {
                VoucherBody vb = it.next();
                if (vb == null)
                    it.remove();
            }
        }
        if (list.isEmpty())
            return null;
        return vo;
    }


    @SuppressWarnings("unused")
    @Override
    // param accountID busDate
    @Transactional(rollbackFor = BusinessException.class)
    public boolean checkVouch(Map<String, Object> param, Voucher voucher) throws BusinessException {
        try {

            Voucher vo = delEmptyObject(voucher);
            if (vo == null)
                throw new BusinessException("凭证异常");
            String period = null;
            String accountID = param.get("accountID").toString();

            if (param.get("busDate") == null)
                period = param.get("period").toString();
            else
                period = param.get("busDate").toString();


            VoucherHead voucherHead = voucher.getVoucherHead();
            List<VoucherBody> vouchBodyList = voucher.getVoucherBodyList();

            // Map<String, Object> upMap = new HashMap<>();
            List<Map<String, Object>> upList = new ArrayList<>();

            @SuppressWarnings("unchecked")
            List<String> vhid = (List<String>) param.get("vhid");

            String str = null;
            int flg = 0;

            boolean a = true;
            boolean b = true;

            Double totalDebitAmount = 0.0;
            Double totalCreditAmount = 0.0;

            Integer source = voucherHead.getSource();

            for (int i = 0; i < vouchBodyList.size(); i++) {
                VoucherBody voucherBody = vouchBodyList.get(i);
                if ((voucherBody.getDebitAmount() == null || voucherBody.getDebitAmount() == 0.0)
                        && (voucherBody.getCreditAmount() == null || voucherBody.getCreditAmount() == 0.0)) {
                    flg = 1;
                    str = "分录借方和贷方必须有一方有值";
                    // break;
                }
                int n = 0;
                String des = null;

                String subjectID = voucherBody.getSubjectID();
                if (StringUtil.isEmptyWithTrim(subjectID)) {
                    n++;
                    str = "科目不存在";
                    des = des == null ? str : (des + ";" + str);
                }

                String direction = voucherBody.getDirection();
                if (StringUtil.isEmptyWithTrim(direction)) {
                    n++;
                    str = "方向必须为借或者贷";
                    des = des == null ? str : (des + ";" + str);
                }

                if (!StringUtil.isEmptyWithTrim(direction))
                    if (direction.equals("1")) {

                        Double debitAmount = voucherBody.getDebitAmount();
                        Double creditAmount = voucherBody.getCreditAmount();
                        if (debitAmount == null && creditAmount == null) {
                            a = false;
                            n++;
                            str = "借方金额不能为空";
                            des = des == null ? str : (des + ";" + str);
                        } else {
                            if (debitAmount == null)
                                debitAmount = new Double(0);
                            totalDebitAmount = totalDebitAmount + debitAmount;
                        }
                    } else if (direction.equals("2")) {

                        Double creditAmount = voucherBody.getCreditAmount();
                        Double debitAmount = voucherBody.getDebitAmount();
                        if (creditAmount == null && debitAmount == null) {
                            b = false;
                            n++;
                            str = "贷方金额不能为空";
                            des = des == null ? str : (des + ";" + str);
                        } else {

                            if (creditAmount == null)
                                creditAmount = new Double(0);
                            totalCreditAmount = totalCreditAmount + creditAmount;
                        }
                    }

                String vcsubject = voucherBody.getVcsubject();
                if (StringUtil.isEmptyWithTrim(vcsubject)) {
                    n++;
                    str = "科目名称不能为空";
                    des = des == null ? str : (des + ";" + str);
                }

                if (!StringUtil.isEmptyWithTrim(vcsubject) && vcsubject.indexOf("未知商品") > -1) {
                    n++;
                    str = "未知科目";
                    des = des == null ? str : (des + ";" + str);
                }

                // 5 检查是否是来自库存商品
                // 5.1成本结转不需要检查
                if (!StringUtil.isEmptyWithTrim(direction) && !StringUtil.isEmptyWithTrim(subjectID)) {
                    // 1 贸易型
                    if (subjectID.startsWith("1405")) {
                        // ** 凭证数量 **
                        Double number = voucherBody.getNumber();

                        String vcabstact = voucherBody.getVcabstact(); // 摘要
                        String subName = voucherBody.getVcsubject(); // 科目名称

                        // 进项不需要检查。 一键生成凭证库存商品数量是从进项发票获取的 。
                        // 修正凭证和手工添加凭证是是前台获取的
                        if (direction.equals("1")) {
                            if (number == null) {
                                n++;
                                str = subjectID + "收入数量不能为空";
                                des = des == null ? str : (des + ";" + str);
                            }

                            // 凭证上 有购入商品 或者 退货
                            // ，这个商品也有科目，如果是第一次购买这个商品的话，那么这个商品在数量金额表还不存在。
                            // 在更新科目时查询不到会自动创建 可以再这里为这个商品在数量金额表创建一个空的记录

                            /***** 相当于销出 需要计算收入是否小于发出 ********/
                            // 待改进 。。。。 收入数量可以小于0 比如退货 ，但是期末数量不能为负数

                            // 第一次购进 没有库存记录 添加一个空的记录
                            if (number != null) {

                                List<KcCommodity> commodity = queryCommBysubCode(subjectID, accountID, period);
                                if (commodity == null || (commodity != null && commodity.size() == 0)) { // 没有库存
                                    // 退货处理
                                    Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                    Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);
                                    if (jfNum < dfNum) {
                                        n++;
                                        str = subjectID + "该商品在数量金额表不存在,请仔细检查该商品信息"; // 可能有科目没有库存这种情况？怎么处理
                                        des = des == null ? str : (des + ";" + str);
                                    }
                                }

                                if (commodity != null && commodity.size() > 0)
                                    // 借方小于0 发出
                                    if (number != null && number < 0.0) {
                                        KcCommodity jx_comm = commodity.get(0);
                                        double qc_balanceNum = StringUtil.doubleIsNull(jx_comm.getQc_balanceNum());// 期初结余数量
                                        double bq_incomeNum = StringUtil.doubleIsNull(jx_comm.getBq_incomeNum()); // 本期收入数量
                                        double bq_issueNum = StringUtil.doubleIsNull(jx_comm.getBq_issueNum()); // 本期发出数量

                                        Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                        Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);

                                        double qimo_num = qc_balanceNum + bq_incomeNum + jfNum - bq_issueNum - dfNum;
                                        if (qimo_num < 0) {
                                            n++;
                                            str = subjectID + "收入总数量不能小于发出数量" + ",期初结余数量:" + qc_balanceNum + ",本期收入"
                                                    + bq_incomeNum + ",凭证收入" + jfNum + ",本期发出总数"
                                                    + (dfNum + bq_issueNum);
                                            des = des == null ? str : (des + ";" + str);
                                        }
                                    }
                            }
                        }

                        // 检查销项 修改凭证才会进入
                        if (direction.equals("2")) {
                            // 发出数量判断
                            if (number == null) {
                                n++;
                                str = subjectID + "发出数量不能为空";
                                des = des == null ? str : (des + ";" + str);
                            }

                            if (number != null) {
                                // 一键生成凭证系统自定义科目在库存表查询是null 改进
                                List<KcCommodity> commodity = queryCommBysubCode(subjectID, accountID, period);
                                // 1 该商品没有库存的处理方法
                                if (commodity == null || (commodity != null && commodity.size() == 0)) {
                                    // 一个凭证里面有同样的商品处理 既有这个商品的借方也有这个商品的贷方
                                    Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                    Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);
                                    if (jfNum < dfNum) {
                                        n++;
                                        str = subjectID + "该商品在数量金额表没有发现,请先购进该商品"; // 可能有科目没有库存这种情况？怎么处理
                                        des = des == null ? str : (des + ";" + str);
                                    }
                                }

                                // 1 该商品有库存的处理方法
                                if (commodity != null && commodity.size() > 0) {
                                    KcCommodity kcCommodity = commodity.get(0);
                                    double qc_balanceNum = StringUtil.doubleIsNull(kcCommodity.getQc_balanceNum());// 期初结余数量
                                    double bq_incomeNum = StringUtil.doubleIsNull(kcCommodity.getBq_incomeNum()); // 本期收入数量
                                    BigDecimal qc_balanceAmount = StringUtil
                                            .bigDecimalIsNull(kcCommodity.getQc_balanceAmount());// 期初结余金额
                                    BigDecimal incomeAmount = StringUtil
                                            .bigDecimalIsNull(kcCommodity.getBq_incomeAmount()); // 本期收入金额

                                    double bq_issueNum = StringUtil.doubleIsNull(kcCommodity.getBq_issueNum()); // 本期发出数量

                                    // 收入总数量小于等于0 有问题，这个时候就要再判断
                                    // 发出数量是否小于0（发出数量小于0 说明还有退货 退货可以放到进货）
                                    Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                    Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);

                                    double flg_bq_issueNum = bq_issueNum; // 标记发出数量查看

                                    if (param.get("modiyCbjz") != null || param.get("addCbjz") != null || param.get("ufjz") != null) {
                                        // 成本结转修正只需要判断凭证数量是否大于库存,不需要之前的发出数量
                                        bq_issueNum = 0.0;
                                    }
                                    double qimo_num = qc_balanceNum + bq_incomeNum + jfNum - bq_issueNum - dfNum;
                                    boolean absCompare = StringUtil.absCompare(new BigDecimal(qimo_num));
                                    if (qimo_num < 0 && absCompare == false) {
                                        //double abs = Math.abs(qimo_num);
                                        n++;
                                        str = subjectID + "发出数量不能大于库存,期初结余数量" + qc_balanceNum + ",本期收入数量" + bq_incomeNum + ",本期发出数量" + bq_issueNum + ",分录发出数量" + number;
                                        des = des == null ? str : (des + ";" + str);

                                    }

                                }
                            }
                        }
                    }

                    // 2生产型
                    /***************** 原材料 入库start *****************/
                    if (subjectID.startsWith("1403")) {

                        // ** 凭证数量 **
                        Double number = voucherBody.getNumber();

                        String vcabstact = voucherBody.getVcabstact(); // 摘要
                        String subName = voucherBody.getVcsubject(); // 科目名称

                        // 原材料 入库
                        if (direction.equals("1")) {
                            if (number == null) {
                                n++;
                                str = subjectID + "收入数量不能为空";
                                des = des == null ? str : (des + ";" + str);
                            }

                            if (number != null) {
                                List<KcCommodity> commodity = queryCommBysubCode(subjectID, accountID, period);
                                if (commodity == null || (commodity != null && commodity.size() == 0)) {
                                    Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                    Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);
                                    if (jfNum < dfNum) {
                                        n++;
                                        str = subjectID + "收入数量不能小于发出数量或者不能为空";
                                        des = des == null ? str : (des + ";" + str);
                                    }
                                }

                                if (commodity != null && commodity.size() > 0) {
                                    KcCommodity jx_comm = commodity.get(0);
                                    double qc_balanceNum = StringUtil.doubleIsNull(jx_comm.getQc_balanceNum());// 期初结余数量
                                    double bq_incomeNum = StringUtil.doubleIsNull(jx_comm.getBq_incomeNum()); // 本期收入数量
                                    double bq_issueNum = StringUtil.doubleIsNull(jx_comm.getBq_issueNum()); // 本期发出数量

                                    Double jfNum = proofService.getjfNum(vouchBodyList, subjectID);
                                    Double dfNum = proofService.getdfNum(vouchBodyList, subjectID);

                                    double qimo_num = qc_balanceNum + bq_incomeNum + jfNum - dfNum - bq_issueNum;
                                    if (qimo_num < 0) {
                                        n++;
                                        str = subjectID + "收入总数量不能小于发出数量" + ",期初结余数量:" + qc_balanceNum + ",本期收入"
                                                + bq_incomeNum + ",凭证收入" + number + ",本期发出总数" + bq_issueNum;
                                        des = des == null ? str : (des + ";" + str);
                                    }
                                }
                            }

                        }

                        if (direction.equals("2")) {
                            // 领料数量为空
                            if (number == null) {
                                n++;
                                str = subjectID + "出库数量不能为空";
                                des = des == null ? str : (des + ";" + str);
                            }
                            // 不为空
                            if (number != null) {
                                // 领料 出库
                                List<KcCommodity> commodity = queryCommBysubCode(subjectID, accountID, period);
                                // 1 不存在库存商品的情况
                                if (commodity == null) {
                                    // 一个凭证里面有同样的商品处理
                                    Double jfNum = proofService.jfdfNum(vouchBodyList, subjectID, 1);
                                    Double dfNum = proofService.jfdfNum(vouchBodyList, subjectID, 2);
                                    if (jfNum < dfNum) {
                                        n++;
                                        str = subjectID + "收入数量不能小于发出数量或者不能为空";
                                        des = des == null ? str : (des + ";" + str);
                                    }
                                }

                                // 2 存在库存商品情况
                                if (commodity != null && commodity.size() > 0) {

                                    KcCommodity kcCommodity = commodity.get(0);
                                    double qc_balanceNum = StringUtil.doubleIsNull(kcCommodity.getQc_balanceNum());// 期初结余数量
                                    // BigDecimal qc_balanceAmount =
                                    // StringUtil.bigDecimalIsNull(kcCommodity.getQc_balanceAmount());//期初结余金额

                                    double bq_incomeNum = StringUtil.doubleIsNull(kcCommodity.getBq_incomeNum()); // 本期收入数量
                                    // BigDecimal bq_incomeAmount =
                                    // StringUtil.bigDecimalIsNull(kcCommodity.getBq_incomeAmount());
                                    // //本期收入金额

                                    double bq_issueNum = StringUtil.doubleIsNull(kcCommodity.getBq_issueNum()); // 本期发出数量

                                    // 如果还有借方商品的话 计算借方商品数量 1405 2
                                    Double jfNum = proofService.jfdfNum(vouchBodyList, subjectID, 1);
                                    Double dfNum = proofService.jfdfNum(vouchBodyList, subjectID, 2);

                                    if (param.get("modiyCbjz") != null || param.get("addCbjz") != null || param.get("ufjz") != null) {
                                        // 成本结转修正只需要判断凭证数量是否大于库存,不需要之前的发出数量
                                        bq_issueNum = 0.0;
                                    }

                                    double qimo_num = qc_balanceNum + bq_incomeNum + jfNum - bq_issueNum - dfNum;
                                    boolean absCompare = StringUtil.absCompare(new BigDecimal(qimo_num));
                                    if (qimo_num < 0 && absCompare == false) {
                                        n++;
                                        str = subjectID + "发出数量不能大于库存,期初结余数量" + qc_balanceNum + ",本期收入数量" + bq_incomeNum
                                                + ",本期发出数量" + bq_issueNum + ",凭证发出数量" + number;
                                        des = des == null ? str : (des + ";" + str);
                                    }

                                }
                            }
                        }
                    }

                    /***************** 原材料 入库end *****************/
                }

                Map<String, Object> upMap = new HashMap<>();
                if (n > 0) {
                    flg++;
                    upMap.put("isproblem", "1");
                    upMap.put("des", des);
                } else {
                    upMap.put("isproblem", "2");
                    upMap.put("des", "");
                }
                upMap.put("vouchAID", voucherBody.getVouchAID());
                if (vhid == null)
                    voucherHeadDao.upVouBodyById(upMap);
                else
                    upList.add(upMap);
            }

            Map<String, Object> upMapHead = new HashMap<>();
            str = null;
            String strDes = null;

            BigDecimal debitAmount = new BigDecimal(totalDebitAmount);
            BigDecimal creditAmount = new BigDecimal(totalCreditAmount);

            BigDecimal subtract = debitAmount.subtract(creditAmount);
            double abs = subtract.abs().doubleValue();

            if (abs < 0.0001)
                System.out.println("借贷平衡" + abs);
            else {
                flg++;

                str = "借贷不平衡 借【" + debitAmount + "】,贷【" + creditAmount + "】,差值【" + abs + "】";
                strDes = strDes == null ? str : (strDes + ";" + str);
            }

            if (!a || !b) {
                flg++;
                str = "不能全部为借或者贷";
                strDes = strDes == null ? str : (strDes + ";" + str);
            }

            if (flg > 0) {
                upMapHead.put("isproblem", "1");
                upMapHead.put("des", strDes);
            } else {
                upMapHead.put("isproblem", "2");
                upMapHead.put("des", "");
            }
            String vouchID = voucherHead.getVouchID();
            upMapHead.put("vouchID", vouchID);

            if (vhid == null)
                voucherHeadDao.upVouHeadByCheckId(upMapHead);
            else if (flg > 0) {
                voucherHeadDao.upVouHeadByCheckId(upMapHead);
                if (!upList.isEmpty())
                    for (Map<String, Object> map : upList)
                        voucherHeadDao.upVouBodyById(map);
            } else
                vhid.add(vouchID);
            return flg == 0;

        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    @Override
    public Map<String, Object> addVbQueryCbjz(Voucher voucher) throws BusinessException {
        try {

            Voucher vo = delEmptyObject(voucher);
            VoucherHead vh = vo.getVoucherHead();
            List<VoucherBody> list = vo.getVoucherBodyList();

            Map<String, Object> map = new HashMap<>();
            map.put("period", period_acc.get());
            map.put("accountID", account_id.get());

            boolean a = false;


            if (proofService.checkIsCbjz(list) == true) {
                a = true;
            }

            if (a == true) {
                List<VoucherHead> list2 = vatDao.queryCbjzVo2(map);
                if (list2 != null && !list2.isEmpty()) {
                    Integer voucherNO = list2.get(0).getVoucherNO();
                    return ret(1, "系统检测到" + voucherNO + "号凭证已经结转了销售成本,请勿重新添加");
                }
            }
            return ret(0, "success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void upSub(List<Voucher> vo, Map<String, Object> param) throws BusinessException {
        Map<String, SubVo> mapVo = new HashMap<>();
        Map<String, Object> qrMap = new HashMap<>();
        Map<String, SubjectMessage> subMap = new HashMap<>();
        if (period_acc.get() == null || account_id.get() == null)
            throw new BusinessException("sorry,期间或者账套不存在upSub");
        try {

            for (int i = 0; i < vo.size(); i++) {
                Voucher voucher = vo.get(i);
                if (voucher == null)
                    continue;
                List<VoucherBody> vbList = vo.get(i).getVoucherBodyList();
                if (vbList == null || (vbList != null && vbList.isEmpty()))
                    continue;

                for (int j = 0; j < vbList.size(); j++) {
                    VoucherBody vb = vbList.get(j);
                    if (vb == null)
                        continue;
                    String dir = vb.getDirection();
                    String code = vb.getSubjectID();

                    Double a = StringUtil.doubleIsNull(vb.getDebitAmount());
                    Double b = StringUtil.doubleIsNull(vb.getCreditAmount());

                    BigDecimal debitAmount = new BigDecimal(a);
                    BigDecimal creditAmount = new BigDecimal(b);

                    if (StringUtil.isEmpty(dir) || StringUtil.isEmpty(code) || (a == null && b == null))
                        throw new BusinessException(
                                "更新科目异常:科目方向 " + dir + ";科目编码: " + code + ";借方金额:" + a + "贷方金额:" + b);
                    // continue;
                    int length = code.length();
                    for (int k = 0; k < (length / 3); k++) {
                        String new_code = code.substring(0, length - (k * 3));
                        SubVo subVo = mapVo.get(new_code);
                        if (subVo != null) {
                            if (dir.equals("1")) {
                                BigDecimal jf_amount = subVo.getJf_amount();
                                BigDecimal add = jf_amount.add(debitAmount);
                                subVo.setJf_amount(add);
                            } else {
                                BigDecimal df_amount = subVo.getDf_amount();
                                BigDecimal add = df_amount.add(creditAmount);
                                subVo.setDf_amount(add);
                            }
                        } else {
                            SubVo subVoNew = new SubVo();
                            if (dir.equals("1")) {
                                subVoNew.setJf_amount(debitAmount);
                                subVoNew.setDf_amount(new BigDecimal(0));
                            } else {
                                subVoNew.setDf_amount(creditAmount);
                                subVoNew.setJf_amount(new BigDecimal(0));
                            }
                            subVoNew.setSub_code(new_code);
                            mapVo.put(new_code, subVoNew);
                        }
                    }
                }
            }

            if (mapVo.isEmpty())
                return;
            Set<String> keySet = mapVo.keySet();

            List<String> arrayList = new ArrayList<>();
            arrayList.addAll(keySet);
            qrMap.put("codes", arrayList);
            qrMap.put("accountID", account_id.get());
            qrMap.put("period", period_acc.get());

            List<SubjectMessage> list = vatDao.querySubByVo(qrMap);
            if (list != null) {
                for (SubjectMessage subMess : list)
                    subMap.put(subMess.getSub_code(), subMess);
                qrMap.remove("codes");
                Set<Entry<String, SubVo>> entrySet = mapVo.entrySet();
                for (Entry<String, SubVo> entry : entrySet) {
                    String key = entry.getKey();
                    SubVo val = entry.getValue();
                    BigDecimal jf_amount = StringUtil.bigDecimalIsNull(val.getJf_amount());
                    BigDecimal df_amount = StringUtil.bigDecimalIsNull(val.getDf_amount());

                    SubjectMessage subject = subMap.get(key);

                    if (subject == null) {
                        qrMap.put("sub_code", key);
                        subject = vatDao.querySubByCode(qrMap);
                    }

                    if (subject == null)
                        throw new BusinessException("创建凭证更新科目异常");

                    String pk_sub_id = subject.getPk_sub_id();

                    BigDecimal current_amount_debit = StringUtil.bigDecimalIsNull(subject.getCurrent_amount_debit());
                    BigDecimal current_amount_credit = StringUtil.bigDecimalIsNull(subject.getCurrent_amount_credit());

                    BigDecimal year_amount_debit = StringUtil.bigDecimalIsNull(subject.getYear_amount_debit());
                    BigDecimal year_amount_credit = StringUtil.bigDecimalIsNull(subject.getYear_amount_credit());

                    BigDecimal ending_balance_debit = StringUtil.bigDecimalIsNull(subject.getEnding_balance_debit());
                    BigDecimal ending_balance_credit = StringUtil.bigDecimalIsNull(subject.getEnding_balance_credit());

                    BigDecimal new_current_amount_debit = current_amount_debit.add(jf_amount);
                    BigDecimal new_current_amount_credit = current_amount_credit.add(df_amount);

                    BigDecimal new_year_amount_debit = year_amount_debit.add(jf_amount);
                    BigDecimal new_year_amount_credit = year_amount_credit.add(df_amount);

                    BigDecimal new_ending_balance_debit = ending_balance_debit.add(jf_amount);
                    BigDecimal new_ending_balance_credit = ending_balance_credit.add(df_amount);

                    BigDecimal subtract = new_ending_balance_debit.subtract(new_ending_balance_credit);

                    BigDecimal new_qm_amount_jf = new BigDecimal(0);
                    BigDecimal new_qm_amount_df = new BigDecimal(0);

                    if (subtract.compareTo(BigDecimal.ZERO) > 0)
                        new_qm_amount_jf = subtract;
                    else
                        new_qm_amount_df = subtract.abs();

                    Map<String, Object> upSubMap = new HashMap<>();
                    upSubMap.put("pk_sub_id", pk_sub_id);
                    upSubMap.put("current_amount_debit", new_current_amount_debit);
                    upSubMap.put("current_amount_credit", new_current_amount_credit);
                    upSubMap.put("year_amount_debit", new_year_amount_debit);
                    upSubMap.put("year_amount_credit", new_year_amount_credit);
                    upSubMap.put("ending_balance_debit", new_qm_amount_jf);
                    upSubMap.put("ending_balance_credit", new_qm_amount_df);

                    int num = vatDao.upSubVo(upSubMap);
                    System.out.println(num);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void insertVouchBatch(VoucherBody... vbs) throws BusinessException {
        try {
            insertVouchBatch(Arrays.asList(vbs));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void insertVouchBatch(List<VoucherBody> list) throws BusinessException {
        try {
            Map<String, Object> map5 = new HashMap<>();
            map5.put("list", list);
            int num = voucherBodyDao.insertVouchBatch(map5);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    @Override
    public KcCommodity queryQmNum(Map<String, Object> param) throws BusinessException {
        List<KcCommodity> list = commodityDao.queryCommByCondition(param);
        if (list != null)
            return list.get(0);
        return null;
    }


    @Override
    public Map<String, String> getAllMappingSubCode(String accountID) throws BusinessException {
        HashMap<String, String> map = new HashMap<>();
        map.put("进项税转出", "2221007");
        map.put("应交所得税", "2221004");
        map.put("未交增值税", "2221001001");
        map.put("教育费附加", "2221003");
        map.put("应交城市维护建设税", "2221002");
        map.put("转出未交增值税", "2221001004");
        map.put("工资", "2211001");
        map.put("地方教育费附加", "2221005");
        map.put("留抵税", "2221001005");
        map.put("销项税", "2221001003");
        map.put("未分配利润", "4104001");
        map.put("主营业务收入", "6001");
        map.put("进项税", "2221001002");
        return map;
    }


    @Override
    public Map<String, SubjectMessage> getSubMessageByCode(String accID, Map<String, String> subMapping, String[] arr) throws BusinessException {
        Account acc = accountDao.queryAccByID(accID);

        List<String> baseSubNames = acc.getSsType() == 0
                ? assetsService.getToBaseSub()
                : assetsService.getToSmallBaseSub();

        Set<String> keySet = subMapping.keySet();
        if (baseSubNames.stream().noneMatch(keySet::contains)) {
            throw new BusinessException("检测到还没有进行科目初始化映射,请先把科目映射完成");
        }

        List<String> codeList = Stream.of(arr).map(subMapping::get).collect(toList());
        if (arr.length != codeList.size()) {
            throw new BusinessException("vatservice getSubMessageByCode arr.length!=codeList.size() 映射中间表可能有重复科目");
        }

        Map<String, String> idMap = getAllSubId(accID, period_acc.get());

        List<String> ids = new ArrayList<>();
        for (String code : codeList) {
            String id = idMap.get(code);
            if (StringUtil.isEmpty(id)) {
                throw new BusinessException("vatservice getSubMessageByCode idMap.get(code) is null code = " + code);
            }
            ids.add(id);
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("ids", ids);
        List<SubjectMessage> list = vatDao.querySubjectMessageByPkid(queryMap);

        if (list == null || list.isEmpty() || list.size() != arr.length) {
            throw new BusinessException("VatServiceImpl getSubMessageByCode list==null || list.isEmpty() arr=" + subMapping + "异常");
        }
        for (SubjectMessage ss : list) {
            if (ss == null) {
                throw new BusinessException("VatServiceImpl getSubMessageByCode ss==null=" + subMapping + "异常");
            }
        }

        Map<String, String> map = new HashMap<>();
        for (Entry<String, String> entry : subMapping.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }

        Map<String, SubjectMessage> returnedMap = new HashMap<>();
        for (SubjectMessage sm : list) {
            returnedMap.put(map.get(sm.getSub_code()), sm);
        }
        return returnedMap;
    }


    // 成本结转
    // 2018-11.019 第三版
    // 修改 进销项发票导入与数量金额表脱离
    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> cbCarryover(Map<String, Object> param) throws BusinessException {
        // 定义方法返回结果集合
        Map<String, Object> res = new HashMap<>();
        subinit(param);
        String accountId = (String)param.get("accountID");
        String period = (String)param.get("period");
        try {
            // 查询是否在 自动结转前 是否有手动去添加结转成本,(导入序时薄也不需要再结转成本)
            VoucherBody vh2 = voucherHeadDao.queryCbjzVo(param);
            if (vh2 != null) {
                res.put("msg", "系统已经结转了成本，请修改或者删除已经存在的凭证");
                return res;
            }

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountId);
            hashMap.put("period", period);
            hashMap.put("invoiceType", "2");

            List<InvoiceBody> xxList = invoiceDao.queryInvoiceBAll(hashMap);
            List<InvoiceBody> vbList = xxList.stream()
                    .filter(body -> StringUtils.isNotBlank(body.getSub_code())
                            && (body.getSub_code().startsWith("1405") || body.getSub_code().startsWith("1403")))
                    .collect(toList());

            // 收据销售收入票
            List<Receipt> receipts = receiptDao.selectSalesList(accountId, period);
            if (vbList.isEmpty() && receipts.isEmpty()) {
                return null;
            }

            Map<String, Map<String, Object>> accumulation = new HashMap<>();

            //合计发票金额
            List<InvoiceBody> sumInvoiceBody = sumInvoiceBody(vbList);

            sumInvoiceBody.forEach(invoiceBody -> {
                String subjectCode = invoiceBody.getSub_code();
                BigDecimal amount = BigDecimals.of(invoiceBody.getNamount());
                BigDecimal number = BigDecimals.of(invoiceBody.getNnumber());
                Map<String, Object>  elem = accumulation.get(subjectCode);
                if (elem == null) {
                    elem = new HashMap<>();
                    elem.put("subjectCode", subjectCode);
                    elem.put("number", number);
                    elem.put("amount", amount);
                    elem.put("subjectFullName",invoiceBody.getSub_full_name());
                } else {
                    elem.put("number", BigDecimals.safeAdd((BigDecimal)elem.get("number"), amount));
                    elem.put("amount", BigDecimals.safeAdd((BigDecimal)elem.get("amount"), number));
                }
                accumulation.put(subjectCode, elem);
            });

            receipts.forEach(receipt -> {
                // 销售票借方
                String subjectCode = receipt.getSubjectCode();
                BigDecimal amount = receipt.getAmount();
                BigDecimal number = receipt.getNumber();
                Map<String, Object>  elem = accumulation.get(subjectCode);
                if (elem == null) {
                    elem = new HashMap<>();
                    elem.put("subjectCode", subjectCode);
                    elem.put("number", number);
                    elem.put("amount", amount);
                    elem.put("subjectFullName",receipt.getSubjectFullName());
                } else {
                    elem.put("number", BigDecimals.safeAdd((BigDecimal)elem.get("number"), amount));
                    elem.put("amount", BigDecimals.safeAdd((BigDecimal)elem.get("amount"), number));
                }
                accumulation.put(subjectCode, elem);
            });

            Set<String> subjectCodes = accumulation.keySet();

            // 查询数量金额表商品数据
            List<KcCommodity> kccList = commodityDao.queryCommodityAll(hashMap);
            Map<String, KcCommodity> kccMap = kccList.stream()
                    .filter(kc -> StringUtils.isNotBlank(kc.getSub_code())
                            && (kc.getSub_code().startsWith("1405") || kc.getSub_code().startsWith("1403")))
                    .filter(kcCommodity -> subjectCodes.contains(kcCommodity.getSub_code()))
                    .collect(singleResultGroupingBy(KcCommodity::getSub_code));

            if (kccMap.isEmpty()) {
                return null;
            }

            carryOverCostOfInvoiceAndReceipt(kccMap, accumulation);

            res.put("message", "success");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private void carryOverCostOfInvoiceAndReceipt(Map<String, KcCommodity> kccMap, Map<String,
            Map<String, Object>> accumulation) {
        // 创建主凭证
        VoucherHead voucherHead = createVoucherHead(7, "结转销售成本", 0.0, 0.0);
        String voucherId = voucherHead.getVouchID();

        // 更新数量金额表
        List<Map<String, Object>> upKccList = new ArrayList<>();
        List<VoucherBody> voucherBodies = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 对销项发票里面的所有商品 结算成本
        // 定义分录行号
        int count = 1;
        for (Entry<String, Map<String, Object>> entry : accumulation.entrySet()) {
            Map<String, Object> elem = entry.getValue();

            KcCommodity kcCommodity = kccMap.get(entry.getKey());

            if (kcCommodity == null) {
                continue;
            }

            // 库存商品本期发出金额
            BigDecimal kccBqIssueamount = BigDecimals.of(kcCommodity.getBq_issueAmount());
            // 库存商品本期发出数量
            BigDecimal kccBqIssuenum = BigDecimals.of(kcCommodity.getBq_issueNum());

            // 发票金额
            BigDecimal namount = BigDecimals.of((BigDecimal) elem.get("amount"));
            // 发票数量
            BigDecimal nnumber = BigDecimals.of((BigDecimal) elem.get("number"));

            //本期发出总数量
            BigDecimal bqIssuenum = kccBqIssuenum.add(nnumber);
            //本期发出总金额
            BigDecimal bqIssueamount = kccBqIssueamount.add(namount);

            // 期初数量
            BigDecimal qcBalancenum = BigDecimals.of(kcCommodity.getQc_balanceNum());
            // 期初金额
            BigDecimal qcBalanceamount = BigDecimals.of(kcCommodity.getQc_balanceAmount());
            // 本期收入金额
            BigDecimal bqIncomeamount = BigDecimals.of(kcCommodity.getBq_incomeAmount());
            // 本期收入数量
            BigDecimal bqIncomenum = BigDecimals.of(kcCommodity.getBq_incomeNum());

            // 收入总金额
            BigDecimal totalBqIncomeamount = bqIncomeamount.add(qcBalanceamount);
            // 收入总数
            BigDecimal totalBqIncomenum = bqIncomenum.add(qcBalancenum);

            // 如果 收入总数量为0 或者 收入总金额为0, 跳过无需结转
            if (totalBqIncomeamount.compareTo(BigDecimal.ZERO) == 0 || totalBqIncomenum.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            if (bqIssuenum.compareTo(totalBqIncomenum) > 0) {
                throw new BusinessException("发出数量大于收入数量,请检查");
            }

            // 平均价 本期收入金额/本期收入数量
            BigDecimal avgPrice = totalBqIncomeamount.divide(totalBqIncomenum, 8, BigDecimal.ROUND_UP);
            // 商品销售成本 = 发出数量*平均价
            BigDecimal cbAmount = bqIssuenum.multiply(avgPrice);
            // 期末结存金额  收入总金额 - 成本
            BigDecimal qmBalanceamount = totalBqIncomeamount.subtract(cbAmount);
            // 期末结数量 = 收入总数 - 发出数量
            BigDecimal qmBalancenum = totalBqIncomenum.subtract(bqIssuenum);

            Map<String, Object> kcMap = new HashMap<>();
            kcMap.put("qm_balanceAmount", qmBalanceamount);
            kcMap.put("qm_balanceNum", qmBalancenum.doubleValue());

            if (StringUtil.absCompare(qmBalanceamount)) {
                kcMap.put("qm_balancePrice", 0);
                kcMap.put("balance_direction", "平");
            } else {
                // 期末结存单价 = 平均价 avgPrice
                kcMap.put("qm_balancePrice", avgPrice);
                kcMap.put("balance_direction", "借");
            }

            // 1 本期发出总数 double
            kcMap.put("bq_issueNum", bqIssuenum.doubleValue());
            // 2 发出总金额
            kcMap.put("bq_issueAmount", bqIssueamount);
            // 3 本年累计发出数量
            kcMap.put("total_issueNum",bqIssuenum.add(BigDecimals.of(kcCommodity.getTotal_issueNum())).doubleValue());
            // 4 本年累计发出金额
            kcMap.put("total_issueAmount", BigDecimals.of(kcCommodity.getTotal_issueAmount()).add(namount));
            // 更新条件
            kcMap.put("comID", kcCommodity.getComID());
            upKccList.add(kcMap);

            VoucherBody voBody = new VoucherBody();
            voBody.setVouchAID(UUIDUtils.getUUID());
            voBody.setVouchID(voucherId);
            voBody.setPeriod(period_acc.get());
            voBody.setVcunitID(null);
            voBody.setDebitAmount(null);
            voBody.setAccountID(account_id.get());
            voBody.setVcabstact("结转销售成本");
            voBody.setPrice(avgPrice.doubleValue());
            voBody.setIsproblem("2");
            voBody.setDes("");
            voBody.setNumber(bqIssuenum.doubleValue());
            voBody.setCreditAmount(cbAmount.doubleValue());
            voBody.setVcsubject((String)elem.get("subjectFullName"));
            voBody.setDirection("2");
            voBody.setSubjectID(kcCommodity.getSub_code());
            voBody.setRowIndex(String.valueOf(count));

            vatDao.insertVoBody(voBody);
            voucherBodies.add(voBody);

            // 分录号自增
            count ++;
            // 累计每个商品的成本
            totalAmount = totalAmount.add(cbAmount);
        }

        // 主营业务成本 不能写死
        SubjectMessage zycbSub = querySubByCode("6401");
        VoucherBody voBody = createVouchBody(voucherId, "1", "结转销售成本", zycbSub.getFull_name(),
                totalAmount.doubleValue(), null, "1", zycbSub.getSub_code(), null);

        voucherBodies.add(voBody);

        Map<String, Object> m1 = new HashMap<>();
        m1.put("vouchID", voucherId);
        m1.put("totalCredit", totalAmount);
        m1.put("totalDbit", totalAmount);
        m1.put("isproblem", 2);
        // 更新 凭证头 金额 记录凭证转态
        voucherHeadDao.chgVouchAmount(m1);

        voucherHead.setTotalCredit(totalAmount.doubleValue());
        voucherHead.setTotalDbit(totalAmount.doubleValue());

        // 如果不存在有问题的销项商品，更新凭证数据到科目与中去。否则只创造一个有问题的凭证
        // 1 更新库存
        if (!upKccList.isEmpty()) {
            for (Map<String, Object> map : upKccList) {
                commodityDao.updateQmAmountNumPrice(map);
            }
        }

        // 2 更新科目
        // 材料串户 盘点损失 商品 在借方贷方都有
        // 如果是材料串户 盘点损失 只能是手工凭证，一键结转没有这样的凭证
        // 首先判断凭证类型 是不是成本结转 可以看摘要
        // 1如果不是 成本结转的话 贷方商品数量与金额需要累加到发出数量与金额里面去，期末结余也需要重新计算
        // 2如果是成本结转的话， 按照正常流程执行。 金额就是商品的成本
        Voucher voucher = new Voucher();
        voucher.setVoucherHead(voucherHead);
        voucher.setVoucherBodyList(voucherBodies);

        Map<String, Object> jzcbMap = new HashMap<>();
        jzcbMap.put("accountID", account_id.get());
        jzcbMap.put("busDate", period_acc.get());
        // 科目更新 cbjz标识的作用 如果有
        // cbjz标识chgSubAmountByCreate方法只会去更新凭证里面的科目不更新数量金额表
        jzcbMap.put("cbjz", "cbjz");
        tBasicSubjectMessageMapper.chgSubAmountByCreate(jzcbMap, voucher);
        jzcbMap.remove("cbjz");
    }

    @SuppressWarnings("unused")
    @Override
    //步骤 销项生成凭证之后 销项发票1405 1403 开头的 科目 入库
    public int xxCommodityStorage(String accountID, String period) throws BusinessException {

        //1 先查询所有销项发票商品
        //2合并商品入库
        //3更新本期 期末 数据
        try {

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountID);
            hashMap.put("period", period);
            hashMap.put("invoiceType", "2");
            List<InvoiceBody> xxList = invoiceDao.queryInvoiceBAll(hashMap);
            if (xxList == null || xxList.isEmpty()) {
                return -1;
            }
            List<InvoiceBody> sumInvoiceBody = sumInvoiceBody(xxList);

            // 查询数量金额表所有商品数据
            List<KcCommodity> kccList = commodityDao.queryCommodityAll(hashMap);

            if (kccList == null || kccList.isEmpty()) {
                throw new BusinessException("数量金额表没有查询到任何数据");
            }

            Map<String, KcCommodity> kcc_map = new HashMap<>();
            for (KcCommodity kcCommodity2 : kccList) {
                String sub_code = kcCommodity2.getSub_code();
                if (!sub_code.startsWith("1405")) {
                    kcc_map.put(sub_code, kcCommodity2);
                }
            }
            if (kcc_map.isEmpty()) {
                return 0;
            }

            int n = 0;

            for (int i = 0; i < sumInvoiceBody.size(); i++) {
                InvoiceBody invoiceBody = sumInvoiceBody.get(i);

                String sub_code = invoiceBody.getSub_code();
                Double nnumber = StringUtil.doubleIsNull(invoiceBody.getNnumber());
                Double namount = StringUtil.doubleIsNull(invoiceBody.getNamount());

                KcCommodity kcc = kcc_map.get(sub_code);
                if (kcc == null) {
                    continue;
                }
                if (nnumber == 0.0) {
                    throw new BusinessException("xxCommodityStorage 销项入库  case 【nnumber==0.0】  sub_code=" + sub_code + "异常");
                }

                Double bq_issueNum = StringUtil.doubleIsNull(kcc.getBq_issueNum());
                BigDecimal bq_issueAmount = StringUtil.bigDecimalIsNull(kcc.getBq_issueAmount());

                Double total_issueNum = StringUtil.doubleIsNull(kcc.getTotal_issueNum());
                BigDecimal total_issueAmount = StringUtil.bigDecimalIsNull(kcc.getTotal_issueAmount());

                Double qm_balanceNum = StringUtil.doubleIsNull(kcc.getQm_balanceNum());
                BigDecimal qm_balanceAmount = StringUtil.bigDecimalIsNull(kcc.getQm_balanceAmount());

                Map<String, Object> qmMap = new HashMap<>();

                Double bn_total_issueNum = total_issueNum + nnumber;
                BigDecimal bn_total_issueAmount = total_issueAmount.add(new BigDecimal(namount));

                Double bq_total_issueNum = bq_issueNum + nnumber;
                BigDecimal bq_total_issueAmount = bq_issueAmount.add(new BigDecimal(namount));

                double new_qm_balanceNum = qm_balanceNum - nnumber;
                BigDecimal new_qm_balanceAmount = qm_balanceAmount.subtract(new BigDecimal(namount));

                qmMap.put("qm_balanceNum", new_qm_balanceNum); // Double
                qmMap.put("qm_balanceAmount", new_qm_balanceAmount); // BigDecimal

                boolean m = StringUtil.absCompare(new_qm_balanceAmount);// 期末金额小于0.0001
                // 为true
                if (qm_balanceNum == 0.0 || m == true) {
                    qmMap.put("qm_balancePrice", 0);
                    qmMap.put("balance_direction", "平");
                } else {
                    BigDecimal bigDivide = StringUtil.bigDivide(new_qm_balanceAmount, new_qm_balanceNum);
                    String comDir = StringUtil.comDir(bigDivide);
                    qmMap.put("qm_balancePrice", bigDivide);
                    qmMap.put("balance_direction", comDir);
                }

                // 1 本期发出总数
                qmMap.put("bq_issueNum", bq_total_issueNum); // double
                // 2 发出总金额
                qmMap.put("bq_issueAmount", bq_total_issueAmount); // BigDecimals
                // 3 本年累计发出数量
                qmMap.put("total_issueNum", bn_total_issueNum); // Double
                // 4 本年累计发出金额
                qmMap.put("total_issueAmount", bn_total_issueAmount); // BigDecimal
                // 更新条件
                qmMap.put("comID", kcc.getComID());

                int num = commodityDao.updateQmAmountNumPrice(qmMap);
                ++n;
            }
            return n;
        } catch (Exception e) {

            throw new BusinessException(e);
        }

    }

    @SuppressWarnings("unused")
    @Override
    public List<InvoiceBody> sumInvoiceBody(List<InvoiceBody> vbList) {

        Map<String, InvoiceBody> map = new HashMap<>();
        for (InvoiceBody vb : vbList) {
            String sub_code = vb.getSub_code();
            InvoiceBody invoiceBody = map.get(sub_code);
            Double curr_nnumber = StringUtil.doubleIsNull(vb.getNnumber());
            Double curr_namount = StringUtil.doubleIsNull(vb.getNamount());

            if (invoiceBody != null) {
                //发票里面有相同的商品
                Double nnumber = StringUtil.doubleIsNull(invoiceBody.getNnumber());
                Double namount = StringUtil.doubleIsNull(invoiceBody.getNamount());

                Double new_num = curr_nnumber + nnumber;
                Double new_amount = curr_namount + namount;

                invoiceBody.setNnumber(new_num); //累计数量
                invoiceBody.setNamount(new_amount);    //累计金额
                System.out.println();
            } else {
                map.put(sub_code, vb);
            }
        }
        List<InvoiceBody> arr = new ArrayList<>();
        arr.addAll(map.values());
        return arr;
    }

    @Override
    public List<RedisSub> queryRedisSub(Map<String, Object> map) throws BusinessException {
        return vatDao.queryRedisSub(map);
    }

    @Override
    public String getKey(String accountID, String period) {
        return new StringBuilder().append("sub").append("@").append(accountID).append("@").append(period).toString();
    }

    @Override
    public List<RedisSub> queryRedisSub(String accountID, String period) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", accountID);
        map.put("period", period);
        return queryRedisSub(map);
    }

    @Override
    public List<RedisSub> querySubToPage(String accountID, String period, List<VoucherBody> list) throws BusinessException {
        if (StringUtil.isEmpty(accountID) || StringUtil.isEmpty(period)) {
            throw new BusinessException("vatservice querySubToPage StringUtil.isEmpty(accountID) || StringUtil.isEmpty(period)");
        }
        if (list == null || list.isEmpty()) {
            throw new BusinessException("vatservice querySubToPage list==null || list.isEmpty()");
        }
        Set<String> hashSet = new HashSet<>();
        for (VoucherBody vb : list) {
            if (vb != null && !StringUtil.isEmpty(vb.getSubjectID())) {
                hashSet.add(vb.getSubjectID());
            }
        }
        List<RedisSub> rtSub = new ArrayList<>();

        Map<String, RedisSub> temMap = new HashMap<>();

        List<RedisSub> allSub = getAllSub(accountID, period);
        if (allSub == null || allSub.isEmpty()) {
            throw new BusinessException("vatservice querySubToPage allSub==null || allSub.isEmpty()");
        }

        for (RedisSub redisSub : allSub) {
            temMap.put(redisSub.getSub_code(), redisSub);
        }
        for (String vbCode : hashSet) {
            RedisSub redisSub = temMap.get(vbCode);
            if (redisSub == null) {
                throw new BusinessException("vatservice querySubToPage temMap.get(vbCode) == null vbCode = " + vbCode);
            }
            rtSub.add(redisSub);
        }
        return rtSub;
    }


    @Override
    public List<RedisSub> querySubToPage(Map<String, Object> param, List<VoucherBody> list) throws BusinessException {
        if (StringUtil.objEmpty(param.get("accountID")) || StringUtil.objEmpty(param.get("period"))) {
            throw new BusinessException("vatservice querySubToPage StringUtil.objEmpty(param.get(accountID)) || StringUtil.objEmpty(param.get(period))");
        }
        List<RedisSub> res = querySubToPage(param.get("accountID").toString(), param.get("period").toString(), list);
        return res;


    }


    @SuppressWarnings("unused")
    @Override
    public List<PageSub> queryAllSubToPage(String accountID, String period, String type) throws BusinessException {
        try {
            if (StringUtil.isEmpty(accountID) || StringUtil.isEmpty(period)) {
                throw new BusinessException(" vat service queryAllSubToPage accountID || period is null");
            }
            List<RedisSub> allSub = getAllSub(accountID, period);
            if (allSub == null || allSub.isEmpty()) {
                throw new BusinessException(" vatService allSub==null");
            }

            List<PageSub> arr = new ArrayList<>();

            if (type == null) {
                List<RedisSub> arr1 = new ArrayList<>();
                for (RedisSub sub1 : allSub) {
                    List<RedisSub> temList = new ArrayList<>();
                    for (RedisSub sub2 : allSub) {
                        if (sub2.getSub_code().startsWith(sub1.getSub_code())) {
                            temList.add(sub2);
                            if (temList.size() > 1) {
                                break;
                            }
                        }
                    }
                    if (temList.size() == 1) {
                        arr1.add(temList.get(0));
                    }
                }
                BigDecimal zero1 = new BigDecimal(0);
                Iterator<RedisSub> iterator = arr1.iterator();
                ParentCodeMapping parentMapping = new ParentCodeMapping();
                while (iterator.hasNext()) {
                    RedisSub redisSub = (RedisSub) iterator.next();
                    PageSub pageSub = new PageSub();
                    pageSub.setPkSubId(redisSub.getPk_sub_id());
                    pageSub.setSubCode(redisSub.getSub_code());
                    pageSub.setSubName(redisSub.getSub_name());
                    pageSub.setFullName(redisSub.getFull_name());
                    pageSub.setEndingBalanceCredit(redisSub.getEnding_balance_credit() == null ? zero1 : redisSub.getEnding_balance_credit());
                    pageSub.setEndingBalanceDebit(redisSub.getEnding_balance_debit() == null ? zero1 : redisSub.getEnding_balance_debit());
                    pageSub.setDir(parentMapping.getDir(redisSub.getSub_code()));
                    arr.add(pageSub);
                }
            } else {
                BigDecimal zero1 = new BigDecimal(0);
                Iterator<RedisSub> iterator = allSub.iterator();
                ParentCodeMapping parentMapping = new ParentCodeMapping();
                while (iterator.hasNext()) {
                    RedisSub redisSub = (RedisSub) iterator.next();
                    PageSub pageSub = new PageSub();
                    pageSub.setSubCode(redisSub.getSub_code());
                    pageSub.setSubName(redisSub.getSub_name());
                    pageSub.setFullName(redisSub.getFull_name());
                    pageSub.setDir(parentMapping.getDir(redisSub.getSub_code()));
                    arr.add(pageSub);
                }
            }


            Collections.sort(arr, new Comparator<PageSub>() {
                @Override
                public int compare(PageSub paramT1, PageSub paramT2) {
                    return paramT1.getSubCode().compareTo(paramT2.getSubCode());
                }
            });
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }

    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addSubMessage(String accountID, String period, String subCode, String subName,
                                             String fullName) throws BusinessException {
        try {
            Map<String, Object> rtMap = new HashMap<>();
            int len = subCode.length() / 3;
            String dir = new ParentCodeMapping().getDir(subCode.substring(0, 4));

            String parentCode = subCode.substring(0, subCode.length() - 3);//父级
            List<RedisSub> allSub = getAllSub(accountID, period);
            if (allSub == null || allSub.isEmpty()) {
                throw new BusinessException("vatervice addSubMessage allSub is null");
            }
            int length = subCode.length();
            boolean flg = false;
            String str = null;

            for (RedisSub redisSub : allSub) {
                String sub_code = redisSub.getSub_code();
                if (sub_code.startsWith(parentCode)) {
                    if (sub_code.length() == length) {
                        String sub_name = redisSub.getSub_name();
                        if (sub_name.equals(subName)) {
                            flg = true;
                            str = sub_code;
                            break;
                        }
                    }
                }
            }
            if (flg == true) {
                rtMap.put("msg", "检查到您要添加的的科目名称[" + subName + "]已经被" + str + "使用,请更换其它的名称！");
                rtMap.put("code", "4");
                return rtMap;
            }
            String pkid = null;
            for (RedisSub redisSub : allSub) {
                if (redisSub.getSub_code().equals(parentCode)) {
                    pkid = redisSub.getPk_sub_id();
                }
            }
            if (pkid == null) {
                rtMap.put("msg", "vatervice addSubMessage parentCode pkid is null");
                rtMap.put("code", "4");
                return rtMap;
            }

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("pk_sub_id", pkid);

            List<TBasicSubjectMessage> list = vatDao.queryTBasicSubjectMessageById2(hashMap);
            if (list == null || list.size() != 1) {
                rtMap.put("msg", "vatervice addSubMessage queryTBasicSubjectMessageById2 list is null");
                rtMap.put("code", "4");
                return rtMap;
            }
            //开始新增科目
            TBasicSubjectMessage parentSubjectMessage = list.get(0);
            if (parentSubjectMessage.getAccountId() == null || parentSubjectMessage.getAccountId().equals("")) {
                rtMap.put("msg", "vatervice addSubMessage parentSubjectMessage.getAccountId()==null");
                rtMap.put("code", "6");
                return rtMap;
            }
            if (parentSubjectMessage.getAccountPeriod() == null || parentSubjectMessage.getAccountPeriod().equals("")) {
                rtMap.put("msg", "vatervice addSubMessage parentSubjectMessage.getAccountPeriod()==null");
                rtMap.put("code", "7");
                return rtMap;
            }
            if (subCode.substring(subCode.length() - 3).equals("001")) {


                TBasicSubjectMessage sonSubjectMessage = new TBasicSubjectMessage();

                String uuid = UUIDUtils.getUUID();

                sonSubjectMessage.setPkSubId(uuid);
                sonSubjectMessage.setSubCode(subCode);
                sonSubjectMessage.setSubName(subName);
                sonSubjectMessage.setFullName(fullName);

                sonSubjectMessage.setInitCreditBalance(parentSubjectMessage.getInitCreditBalance());
                sonSubjectMessage.setInitDebitBalance(parentSubjectMessage.getInitDebitBalance());
                sonSubjectMessage.setCurrentAmountCredit(parentSubjectMessage.getCurrentAmountCredit());
                sonSubjectMessage.setCurrentAmountDebit(parentSubjectMessage.getCurrentAmountDebit());
                sonSubjectMessage.setYearAmountCredit(parentSubjectMessage.getYearAmountCredit());
                sonSubjectMessage.setYearAmountDebit(parentSubjectMessage.getYearAmountDebit());
                sonSubjectMessage.setEndingBalanceCredit(parentSubjectMessage.getEndingBalanceCredit());
                sonSubjectMessage.setEndingBalanceDebit(parentSubjectMessage.getEndingBalanceDebit());

                sonSubjectMessage.setDebitCreditDirection(dir);
                sonSubjectMessage.setCodeLevel(len);
                sonSubjectMessage.setCategory(parentSubjectMessage.getCategory());
                sonSubjectMessage.setSubSource("手动新增" + DateUtil.getTime());
                sonSubjectMessage.setUnit(parentSubjectMessage.getUnit());
                sonSubjectMessage.setUnitId(parentSubjectMessage.getUnitId());
                sonSubjectMessage.setUpdateDate(new Date());

                sonSubjectMessage.setSuperiorCoding(parentCode);
                sonSubjectMessage.setUserId(parentSubjectMessage.getUserId());
                sonSubjectMessage.setAccountId(parentSubjectMessage.getAccountId());
                sonSubjectMessage.setAccountPeriod(parentSubjectMessage.getAccountPeriod());

                int num = tBasicSubjectMessageMapper.addSubMessage(sonSubjectMessage);
                if (num == 0) {
                    throw new BusinessException("vatervice addSubMessage tBasicSubjectMessageMapper.addSubMessage is error");
                }
                kcCommodityService.updateKccommddityByAddSubMessage(sonSubjectMessage, "1");

                BigDecimal zero1 = new BigDecimal(0);
                PageSub pageSub = new PageSub();
                pageSub.setPkSubId(uuid);
                pageSub.setSubName(subName);
                pageSub.setFullName(fullName);
                pageSub.setDir(dir);
                pageSub.setEndingBalanceCredit(sonSubjectMessage.getEndingBalanceCredit() == null ? zero1 : sonSubjectMessage.getEndingBalanceCredit());
                pageSub.setEndingBalanceDebit(sonSubjectMessage.getEndingBalanceDebit() == null ? zero1 : sonSubjectMessage.getEndingBalanceDebit());

                rtMap.put("code", "0");
                rtMap.put("data", pageSub);
                rtMap.put("msg", "新增科目成功！");
                return rtMap;
            } else {
                TBasicSubjectMessage sonSubjectMessage = new TBasicSubjectMessage();

                String uuid = UUIDUtils.getUUID();

                sonSubjectMessage.setPkSubId(uuid);
                sonSubjectMessage.setSubCode(subCode);
                sonSubjectMessage.setSubName(subName);
                sonSubjectMessage.setFullName(fullName);


                sonSubjectMessage.setDebitCreditDirection(dir);
                sonSubjectMessage.setCodeLevel(len);
                sonSubjectMessage.setCategory(parentSubjectMessage.getCategory());
                sonSubjectMessage.setSubSource("手动新增" + DateUtil.getTime());
                sonSubjectMessage.setUnit(parentSubjectMessage.getUnit());
                sonSubjectMessage.setUnitId(parentSubjectMessage.getUnitId());
                sonSubjectMessage.setUpdateDate(new Date());

                sonSubjectMessage.setSuperiorCoding(parentCode);
                sonSubjectMessage.setUserId(parentSubjectMessage.getUserId());
                sonSubjectMessage.setAccountId(parentSubjectMessage.getAccountId());
                sonSubjectMessage.setAccountPeriod(parentSubjectMessage.getAccountPeriod());

                int num = tBasicSubjectMessageMapper.addSubMessage(sonSubjectMessage);
                kcCommodityService.updateKccommddityByAddSubMessage(sonSubjectMessage, "2");
                if (num == 0) {
                    throw new BusinessException("vatervice addSubMessage tBasicSubjectMessageMapper.addSubMessage is error");
                }
                BigDecimal zero1 = new BigDecimal(0);
                PageSub pageSub = new PageSub();
                pageSub.setPkSubId(uuid);
                pageSub.setSubName(subName);
                pageSub.setFullName(fullName);
                pageSub.setDir(dir);
                pageSub.setEndingBalanceCredit(sonSubjectMessage.getEndingBalanceCredit() == null ? zero1 : sonSubjectMessage.getEndingBalanceCredit());
                pageSub.setEndingBalanceDebit(sonSubjectMessage.getEndingBalanceDebit() == null ? zero1 : sonSubjectMessage.getEndingBalanceDebit());


                rtMap.put("code", "0");
                rtMap.put("data", pageSub);
                rtMap.put("msg", "新增科目成功！");
                return rtMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public void errResetCache(Account acc, String busDate) {
        if (acc == null || StringUtil.isEmpty(acc.getAccountID()) || StringUtil.isEmpty(busDate)) {
            return;
        }
        try {
            boolean chg = getChg();
            if (chg == true) {
                resetCache(acc.getAccountID(), busDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> queryNextOrPrevious(String accountID, String period, String voucherNo, String type1, String type2) throws BusinessException {
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("period", period);
        if (type1.equals("2")) {
            param.put("isproblem", "1");
        }
        String str = "";
        if (type2.equals("nextVb")) {
            str = "下";
            param.put("nextNoucherNo", voucherNo);
        } else {
            str = "上";
            param.put("previousVoucherNo", voucherNo);
        }

        Map<String, Object> map = new HashMap<>();
        List<VoucherHead> vh = vatDao.queryNextOrPreviousVb(param);

        if (vh == null || vh.isEmpty()) {
            map.put("code", "5");
            map.put("msg", "未查询到" + str + "一张凭证,请检查" + str + "一张凭证是否存在");
            return map;
        }
        String vouchID = vh.get(0).getVouchID();
        Map<String, Object> queryVbMap = new HashMap<>();
        queryVbMap.put("vouchID", vouchID);
        List<VoucherBody> list = vatDao.queryVoBody(queryVbMap);

        Voucher voucher = new Voucher();
        voucher.setVoucherHead(vh.get(0));
        voucher.setVoucherBodyList(list);

        map.put("code", "0");
        map.put("data", voucher);
        map.put("msg", "success");
        return map;
    }


    @Override
    public Map<String, Object> goinAddVoucherPage(Account account, String period, User user) throws BusinessException {

        Map<String, Object> map = new HashMap<>();

        String accountID = account.getAccountID();

        map.put("busDate", period);
        map.put("accountID", account.getAccountID());
        List<StatusPeriod> stalist = periodStatusDao.queryStatus(map);
        if (stalist == null || (stalist != null && stalist.get(0) == null)) {
            map.put("code", "1");
            map.put("msg", "期间状态异常");
            return map;
        }
        //是否结转 结转后是不允许 新增凭证
        Integer carryState = stalist.get(0).getIsCarryState();
        if (carryState != null && carryState == 1) {
            map.put("code", "2");
            map.put("msg", "已结转不允许再新增凭证");
            return map;
        }
        Integer maxVoucherNo = getMaxVoucherNo(accountID, period);

	/*	String gswPhone = "13600000001";
		if (user.getUserType() == 2) {
			if (gswPhone.equals(user.getLoginUser())) {
				map.put("importVoucher", "true");
			} else {
				map.put("importVoucher", "true");
			}
		} else if (user.getUserType() == 5) {
			String parentID = user.getParentUser();
			User use = userDao.queryUserById(parentID);
			if ("13923454485".equals(use.getLoginUser())) {
				map.put("importVoucher", "true");
			} else {
				map.put("importVoucher", "true");
			}
		} else if (user.getUserType() == 3 || user.getUserType() == 6) {
			map.put("importVoucher", "true");
		}*/
        map.clear();
        map.put("code", "0");
        map.put("maxVoucherNo", maxVoucherNo);
        map.put("isCarryState", carryState);
        map.put("importVoucher", "true");

        return map;

    }

    @SuppressWarnings("unused")
    @Override
    public List<KcCommodityVo> queryCommodityToVoucher(Account account, String period) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", account.getAccountID());
        map.put("period", period);
        List<KcCommodityVo> list = commodityDao.queryCommodityToVoucher(map);
        return list;
    }


    @SuppressWarnings("unused")
    @Override
    public Voucher commodityGenerateVoucher(String accountID, String period, String comids) throws BusinessException {

        String[] split = comids.split(",");
        List<String> idsList = Arrays.asList(split);

        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("period", period);
        param.put("ids", idsList);

        List<KcCommodity> list = commodityDao.commodityGenerateVoucher(param);

        if (list == null || list.isEmpty()) {
            return null;
        }

        List<KcCommodity> arr = new ArrayList<>();
        for (KcCommodity kcCommodity : list) {
            Double qm_balanceNum = StringUtil.doubleIsNull(kcCommodity.getQm_balanceNum());
            BigDecimal qm_balanceAmount = StringUtil.bigDecimalIsNull(kcCommodity.getQm_balanceAmount());
            if (qm_balanceNum != 0) {
                arr.add(kcCommodity);
            }
        }
        if (arr == null || arr.isEmpty()) {
            return null;
        }
        Integer maxVoucherNo = getMaxVoucherNo(accountID, period);

        Voucher voucher = new Voucher();

        List<VoucherBody> vbList = new ArrayList<>();

        BigDecimal totalAmount = new BigDecimal(0);

        int num = 1;
        for (int i = 0; i < arr.size(); i++) {
            KcCommodity comm = arr.get(i);

            String sub_code = comm.getSub_code();
            String sub_comName = comm.getSub_comName();

            Double qm_balanceNum = StringUtil.doubleIsNull(comm.getQm_balanceNum());
            BigDecimal qm_balanceAmount = StringUtil.bigDecimalIsNull(comm.getQm_balanceAmount());

            totalAmount = totalAmount.add(qm_balanceAmount);

            VoucherBody voucherBody = new VoucherBody();

            BigDecimal price_bigDivide = StringUtil.bigDivide(qm_balanceAmount, qm_balanceNum);

            voucherBody.setAccountID(accountID);
            voucherBody.setCreditAmount(StringUtil.bigDecimalToDou(qm_balanceAmount));
            voucherBody.setDebitAmount(null);
            voucherBody.setDes(null);
            voucherBody.setDirection("2");  //贷方
            voucherBody.setIsproblem("2");
            voucherBody.setNumber(qm_balanceNum);
            voucherBody.setPeriod(period);

            voucherBody.setPrice(StringUtil.bigDecimalToDou(price_bigDivide));
            voucherBody.setRowIndex(String.valueOf(++num));
            voucherBody.setSubjectID(sub_code);
            voucherBody.setVcabstact("销售成本结转");
            voucherBody.setVcsubject(sub_comName);
            voucherBody.setVouchAID(null);
            voucherBody.setVcunitID(null);

            vbList.add(voucherBody);
            System.out.println(111);
        }

        VoucherBody voucherBody = new VoucherBody();


        voucherBody.setAccountID(accountID);
        voucherBody.setCreditAmount(null);
        voucherBody.setDebitAmount(StringUtil.bigDecimalToDou(totalAmount));
        voucherBody.setDes(null);
        voucherBody.setDirection("1"); //借方
        voucherBody.setIsproblem("2");
        voucherBody.setNumber(null);
        voucherBody.setPeriod(period);

        voucherBody.setPrice(null);
        voucherBody.setRowIndex("1");
        voucherBody.setSubjectID("6401");
        voucherBody.setVcabstact("销售成本结转");
        voucherBody.setVcsubject("主营业务成本");
        voucherBody.setVouchAID(null);
        voucherBody.setVcunitID(null);

        vbList.add(0, voucherBody);

        VoucherHead voucherHead = new VoucherHead();
        voucherHead.setAccountID(accountID);
        voucherHead.setAttachID(null);
        voucherHead.setAuditStatus(null);
        voucherHead.setCheckedDate(null);
        voucherHead.setCreateDate(System.currentTimeMillis());
        voucherHead.setCreatepsn(null);
        voucherHead.setCreatePsnID(null);
        voucherHead.setCurrency(null);
        voucherHead.setCurrencyID(null);
        voucherHead.setDes("");
        voucherHead.setIsproblem("2");
        voucherHead.setPeriod(period);
        voucherHead.setSource(57);
        voucherHead.setTotalCredit(null);
        voucherHead.setTotalDbit(StringUtil.bigDecimalToDou(totalAmount));
        voucherHead.setVoucherNO(maxVoucherNo);
        voucherHead.setVoucherType(2);
        voucherHead.setVouchID(null);

        voucher.setVoucherBodyList(vbList);
        voucher.setVoucherHead(voucherHead);

        return voucher;
    }


}
