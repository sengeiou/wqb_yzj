package com.wqb.service.documents.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.common.SubjectUtils;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.documents.TBasicDocumentsMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.vat.VatService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TBasicDocumentsServiceImpl
 * @Description: 单据业务实现类
 * @date 2018年3月14日 下午2:44:00
 */
// @Transactional
@Service("tBasicDocumentsService")
public class TBasicDocumentsServiceImpl implements TBasicDocumentsService {
    // 单据
    @Autowired
    TBasicDocumentsMapper tBasicDocumentsMapper;

    @Autowired
    VatService vatService;

    // 凭证头
    @Autowired
    VoucherHeadDao voucherHeadDao;

    // 凭证体
    @Autowired
    VoucherBodyDao voucherBodyDao;

    // 科目信息表
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addTicketsCost(User user, Account account,
                                              TBasicSubjectMessageService tBasicSubjectMessageService) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
            List<TBasicSubjectMessage> tBasicSubjectMessageList = (List) querySubMessage.get("subMessage");
            // 获取用户信息
            @SuppressWarnings("unchecked")
            String userId = user.getUserID();// 用户id
            String accountId = account.getAccountID();// 账套id
            String busDate = account.getUseLastPeriod();

            String subName1001 = null;
            String fullName1001 = null;
            String pkSubId1001 = null;
            String subCode1001 = null;
            List<TBasicDocuments> tBasicDocumentslist = new ArrayList<TBasicDocuments>();
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                TBasicDocuments tBasicDocuments = new TBasicDocuments();
                String subCode = tBasicSubjectMessage.getSubCode();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (subCode.startsWith("1001")) {
                    subName1001 = tBasicSubjectMessage.getSubName();
                    fullName1001 = tBasicSubjectMessage.getFullName();
                    pkSubId1001 = tBasicSubjectMessage.getPkSubId();
                    subCode1001 = tBasicSubjectMessage.getSubCode();
                }
                if (StringUtils.isNotBlank(pkSubId1001)) {
                    if (subCode.startsWith("6601") && codeLevel > 1) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = tBasicSubjectMessage.getPkSubId();
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = tBasicSubjectMessage.getSubName();
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = tBasicSubjectMessage.getFullName();
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName1001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId1001);
                        tBasicDocuments.setSubNameCredit(subName1001);
                        tBasicDocuments.setSubCodeCredit(subCode1001);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }
                    if (subCode.startsWith("6602") && codeLevel > 1) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = tBasicSubjectMessage.getPkSubId();
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = tBasicSubjectMessage.getSubName();
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = tBasicSubjectMessage.getFullName();
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName1001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId1001);
                        tBasicDocuments.setSubNameCredit(subName1001);
                        tBasicDocuments.setSubCodeCredit(subCode1001);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }
                }
            }
            int no = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentslist);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param session
     * @param querySbujectBalance
     * @return
     * @Title: ticketsCostRow
     * @Description: 动态获取单据-费用票列（6601，6602）
     * @see com.wqb.service.documents.TBasicDocumentsService#ticketsCostRow(javax.servlet.http.HttpSession,
     * java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> ticketsCostRow(HttpSession session, Map<String, Object> querySbujectBalance) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (querySbujectBalance.get("subMessages") != null
                    && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
                List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance
                        .get("subMessages");

                SubjectUtils subjectUtils = new SubjectUtils();
                @SuppressWarnings("static-access")
                List<TBasicSubjectMessage> mjSubMessage = subjectUtils.getMjSub(tBasicSubjectMessages);
                List<TBasicSubjectMessage> TBasicSubjectMessageList = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> TBasicSubjectMessageList1001 = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> TBasicSubjectMessageList6601 = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> TBasicSubjectMessageList6602 = new ArrayList<TBasicSubjectMessage>();
//				List<TBasicSubjectMessage> mjSub = getMjSub(session, "1001");
//				List<TBasicSubjectMessage> mjSub2 = getMjSub(session, "6601");
//				List<TBasicSubjectMessage> mjSub3 = getMjSub(session, "6602");
//				TBasicSubjectMessageList.addAll(mjSub);
//				TBasicSubjectMessageList.addAll(mjSub2);
//				TBasicSubjectMessageList.addAll(mjSub3);
                for (TBasicSubjectMessage tBasicSubjectMessage : mjSubMessage) {
                    String subCode = tBasicSubjectMessage.getSubCode();
                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("1001")) {
                        TBasicSubjectMessageList1001.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("6601") && codeLevel > 1) {

                        TBasicSubjectMessageList6601.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("6602") && codeLevel > 1) {
                        TBasicSubjectMessageList6602.add(tBasicSubjectMessage);
                    }
                }
                TBasicSubjectMessageList.addAll(TBasicSubjectMessageList1001);
                TBasicSubjectMessageList.addAll(TBasicSubjectMessageList6601);
                TBasicSubjectMessageList.addAll(TBasicSubjectMessageList6602);
                result.put("ticketsCostRow", TBasicSubjectMessageList);
                result.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<TBasicSubjectMessage> lastStage(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        if (tBasicSubjectMessageList != null && tBasicSubjectMessageList.size() > 0) {
            for (int i = 0; i < tBasicSubjectMessageList.size(); i++) {
                TBasicSubjectMessage sub = tBasicSubjectMessageList.get(i);
                String sub_code = sub.getSubCode();
            }
        }
        return tBasicSubjectMessageList;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addTicketsCostList(HttpSession session, List<Map<String, Object>> subjectMessages)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");

            String subName1001 = null;
            String fullName1001 = null;
            String pkSubId1001 = null;
            String subCode1001 = null;
            List<TBasicDocuments> tBasicDocumentslist = new ArrayList<TBasicDocuments>();
            for (Map<String, Object> map : subjectMessages) {
                TBasicDocuments tBasicDocuments = new TBasicDocuments();
                String subCode = (String) map.get("subCode");
                BigDecimal amountDebit = new BigDecimal("0");
                Object object = map.get("amountDebit");
                if (object != "" && object != null) {
                    try {
                        BigDecimal amountDebit2 = new BigDecimal(object.toString());
                        amountDebit = amountDebit2.compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal("0.0")
                                : amountDebit2;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (subCode.startsWith("1001")) {
                    subName1001 = (String) map.get("subName");
                    fullName1001 = (String) map.get("fullName");
                    pkSubId1001 = (String) map.get("pkSubId");
                    subCode1001 = (String) map.get("subCode");
                }
                if (StringUtils.isNotBlank(pkSubId1001)) {
                    if (subCode.startsWith("6601") && amountDebit.signum() != 0) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = (String) map.get("pkSubId");
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = (String) map.get("subName");
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = (String) map.get("fullName");
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName1001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId1001);
                        tBasicDocuments.setSubNameCredit(subName1001);
                        tBasicDocuments.setSubCodeCredit(subCode1001);
                        tBasicDocuments.setAmountCredit(amountDebit);
                        tBasicDocuments.setAmountDebit(amountDebit);
                        String documentsType = (String) map.get("documentsType");
                        tBasicDocuments.setDocumentsType(documentsType);
                        tBasicDocumentslist.add(tBasicDocuments);

                    }
                    if (subCode.startsWith("6602") && amountDebit.signum() != 0) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = (String) map.get("pkSubId");
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = (String) map.get("subName");
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = (String) map.get("fullName");
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName1001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId1001);
                        tBasicDocuments.setSubNameCredit(subName1001);
                        tBasicDocuments.setSubCodeCredit(subCode1001);
                        tBasicDocuments.setAmountCredit(amountDebit);
                        tBasicDocuments.setAmountDebit(amountDebit);
                        String documentsType = (String) map.get("documentsType");
                        tBasicDocuments.setDocumentsType(documentsType);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }

                }
            }
            int no = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentslist);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> queryDocumentsList(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String accountPeriod = (String) sessionMap.get("busDate");
            TBasicDocuments tBasicDocuments = new TBasicDocuments();
            tBasicDocuments.setAccountId(accountId);
            tBasicDocuments.setAccountPeriod(accountPeriod);
            List<TBasicDocuments> queryDocumentsList = tBasicDocumentsMapper.queryDocumentsList(tBasicDocuments);

            result.put("queryDocumentsList", queryDocumentsList);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<TBasicDocuments> querySalesDocumentsList(String accountId, String period) throws BusinessException {
        TBasicDocuments tBasicDocuments = new TBasicDocuments();
        tBasicDocuments.setAccountId(accountId);
        tBasicDocuments.setAccountPeriod(period);
        return  tBasicDocumentsMapper.querySalesDocumentsList(tBasicDocuments);
    }

    //一键结转 费用票  【单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)】 生成凭证
    @SuppressWarnings({"static-access", "unchecked"})
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> documentsToVoucher(User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HashMap<String, Object> param = new HashMap<>();
            // 获取用户信息

            String userId = user.getUserID();// 用户id
            String userName = user.getUserName();// 用户名称
            String accountId = account.getAccountID();// 账套id
            String accountPeriod = account.getUseLastPeriod();
            param.put("userId", userId);
            param.put("accountID", accountId);
            param.put("busDate", accountPeriod);

            TBasicDocuments tBasicDocuments = new TBasicDocuments();
            tBasicDocuments.setAccountId(accountId);
            tBasicDocuments.setAccountPeriod(accountPeriod);

            //查询账套 费用票 数据
            List<TBasicDocuments> queryDocumentsList = tBasicDocumentsMapper.queryDocumentsList(tBasicDocuments);

            HashSet<Object> hashSet = new HashSet<>();
            for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                // 单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                if (tBasicDocuments2.getDocumentsType().equals("1")) {
                    String pkSubIdCtedit = tBasicDocuments2.getPkSubIdCtedit();
                    hashSet.add(pkSubIdCtedit);
                }
            }

            for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                if (!tBasicDocuments2.getDocumentsType().equals("1")) {
                    // 单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                    Voucher voucher = new Voucher();
                    List<VoucherBody> voucherBodyList = new ArrayList<VoucherBody>();
                    Integer queryMaxVouchNO = voucherHeadDao.queryMaxVouchNO(accountId, accountPeriod);

                    // 贷方金额
                    BigDecimal amountCredit = tBasicDocuments2.getAmountCredit();
                    double amountCreditDouble = amountCredit.doubleValue();
                    // 借方金额
                    BigDecimal amountDebit = tBasicDocuments2.getAmountDebit();
                    double amountDebitDouble = amountDebit.doubleValue();
                    // 税金
                    BigDecimal taxAmount = new BigDecimal("0");
                    taxAmount = tBasicDocuments2.getTaxAmount() == null ? new BigDecimal("0") : tBasicDocuments2.getTaxAmount(); //税额
                    double taxAmountDouble = taxAmount.doubleValue();

                    Date date = new Date();
                    VoucherHead voucherHead = new VoucherHead();
                    String uuid = UUIDUtils.getUUID();
                    // 凭证头主键
                    voucherHead.setVouchID(uuid);
                    // 生成日期
                    voucherHead.setVcDate(date);
                    // 备注
                    voucherHead.setDes("单据生成");
                    // 创建人ID
                    voucherHead.setCreatePsnID(userId);
                    // 创建人
                    voucherHead.setCreatepsn(userName);
                    // 创建日期
                    voucherHead.setCreateDate(System.currentTimeMillis());
                    // 账套ID
                    voucherHead.setAccountID(accountId);
                    // 期间
                    voucherHead.setPeriod(accountPeriod);
                    // 审核状态(0:未审核1:审核)
                    voucherHead.setAuditStatus(0);
                    // 来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
                    voucherHead.setSource(6);
                    // 凭证贷方金额合计
                    voucherHead.setTotalCredit(amountCreditDouble + taxAmountDouble);
                    // 凭证借方金额合计
                    voucherHead.setTotalDbit(amountDebitDouble);
                    // 0:非模凭证1:模板凭证
                    voucherHead.setVouchFlag(0);
                    // 凭证号
                    voucherHead.setVoucherNO(queryMaxVouchNO + 1);

                    //插入凭证头
                    voucherHeadDao.insertVouchHead(voucherHead);

                    VoucherBody voucherBody = new VoucherBody();
                    String uuid1 = UUIDUtils.getUUID();
                    // 主键
                    voucherBody.setVouchAID(uuid1);
                    // 凭证主表主键
                    voucherBody.setVouchID(uuid);
                    // 分录号
                    voucherBody.setRowIndex("1");
                    // 摘要

                    String zy = StringUtil.getZY(tBasicDocuments2);
                    voucherBody.setVcabstact(StringUtil.getZY(tBasicDocuments2));
                    // 科目名称
                    voucherBody.setVcsubject(tBasicDocuments2.getFullNameDebit());
                    // 计量单位ID
                    String documentsUnitid = StringUtil.objToStr(tBasicDocuments2.getDocumentsUnitid());

                    voucherBody.setVcunitID(documentsUnitid);

                    // 计量单位
                    voucherBody.setVcunit(tBasicDocuments2.getDocumentsUnit());
                    // 期间
                    voucherBody.setPeriod(accountPeriod);
                    // 借方金额
                    voucherBody.setDebitAmount(amountDebitDouble);
                    // 贷方金额
                    voucherBody.setCreditAmount(null);
                    // 账套ID
                    voucherBody.setAccountID(accountId);
                    // 租户ID
                    voucherBody.setUserID(userId);
                    // 方向(1:借2:贷)
                    voucherBody.setDirection("1");
                    // 科目主键 (是科目代码)
                    voucherBody.setSubjectID(tBasicDocuments2.getSubCodeDebit());
                    // 修改人ID
                    voucherBody.setUpdatePsnID(tBasicDocuments2.getPkSubIdDebit());
                    // 修改人
                    voucherBody.setUpdatePsn(uuid);
                    // 修改时间
                    voucherBody.setUpdatedate(date);
                    // 数量
                    BigDecimal documentsNumber = StringUtil.bigDecimalIsNull(tBasicDocuments2.getDocumentsNumber());
                    double number = documentsNumber.doubleValue();
                    // 单价
                    BigDecimal documentsDecimal = StringUtil.bigDecimalIsNull(tBasicDocuments2.getDocumentsDecimal());
                    double price = documentsDecimal.doubleValue();

                    voucherBody.setNumber(number);
                    voucherBody.setPrice(price);
                    // 是否有问题1有问题2没问题
                    voucherBody.setIsproblem("2");
                    // 备注
                    voucherBody.setDes("单据新增 - 借方");
                    voucherBodyDao.insertVouchBody(voucherBody);

                    // 如果税金 大于零 表示有值
                    if (taxAmount.signum() > 0) {
                        // 主键
                        VoucherBody voucherBody3 = new VoucherBody();
                        voucherBody3.setVouchAID(UUIDUtils.getUUID());
                        // 凭证主表主键
                        voucherBody3.setVouchID(uuid);
                        // 分录号
                        voucherBody3.setRowIndex("2");
                        // 摘要
                        voucherBody3.setVcabstact(null);
                        // 科目名称
                        voucherBody3.setVcsubject(tBasicDocuments2.getFullNameTax());
                        // 计量单位ID
                        if (StringUtils.isNotBlank(documentsUnitid)) {
                            voucherBody.setVcunitID(documentsUnitid);
                        }
                        // 计量单位
                        voucherBody3.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 期间
                        voucherBody3.setPeriod(accountPeriod);
                        // 贷方金额
                        voucherBody3.setCreditAmount(taxAmountDouble);
                        // 借方金额
                        voucherBody3.setDebitAmount(null);
                        // 账套ID
                        voucherBody3.setAccountID(accountId);
                        // 租户ID
                        voucherBody3.setUserID(userId);
                        // 方向(1:借2:贷)
                        voucherBody3.setDirection("2");
                        // 科目主键 (是科目代码)
                        voucherBody3.setSubjectID(tBasicDocuments2.getSubCodeTax());
                        // 修改人ID
                        voucherBody3.setUpdatePsnID(tBasicDocuments2.getPkSubIdCtedit());
                        // 修改人
                        voucherBody3.setUpdatePsn(uuid);
                        // 修改时间
                        voucherBody3.setUpdatedate(date);
                        // 数量

                        voucherBody.setNumber(null);

                        voucherBody3.setPrice(null);
                        // 是否有问题 1有问题2没问题
                        voucherBody3.setIsproblem("2");
                        // 备注
                        voucherBody3.setDes("单据新增 - 贷方 ");

                        //插入税金凭证体
                        voucherBodyDao.insertVouchBody(voucherBody3);

                        VoucherBody voucherBody2 = new VoucherBody();
                        String uuid3 = UUIDUtils.getUUID();
                        // 主键
                        voucherBody2.setVouchAID(uuid3);
                        // 凭证主表主键
                        voucherBody2.setVouchID(uuid);
                        // 分录号
                        voucherBody2.setRowIndex("3");
                        // 摘要
                        voucherBody2.setVcabstact(null);
                        // 科目名称
                        voucherBody2.setVcsubject(tBasicDocuments2.getFullNameCredit());
                        // 计量单位ID
                        if (StringUtils.isNotBlank(documentsUnitid)) {
                            voucherBody.setVcunitID(documentsUnitid);
                        }
                        // 计量单位
                        voucherBody2.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 期间
                        voucherBody2.setPeriod(accountPeriod);
                        // 贷方金额
                        voucherBody2.setCreditAmount(amountDebitDouble - taxAmountDouble);
                        // 借方金额
                        // voucherBody2.setDebitAmount(amountDebitDouble);
                        // 账套ID
                        voucherBody2.setAccountID(accountId);
                        // 租户ID
                        voucherBody2.setUserID(userId);
                        // 方向(1:借2:贷)
                        voucherBody2.setDirection("2");
                        // 科目主键 (是科目代码)
                        voucherBody2.setSubjectID(tBasicDocuments2.getSubCodeCredit());
                        // 修改人ID
                        voucherBody2.setUpdatePsnID(tBasicDocuments2.getPkSubIdCtedit());
                        // 修改人
                        voucherBody2.setUpdatePsn(uuid);
                        // 修改时间
                        voucherBody2.setUpdatedate(date);
                        // 数量 单价
                        voucherBody.setNumber(number);
                        voucherBody2.setPrice(price);
                        // 是否有问题 1有问题2没问题
                        voucherBody2.setIsproblem("2");
                        // 备注
                        voucherBody2.setDes("单据新增 - 贷方 ");

                        voucherBodyDao.insertVouchBody(voucherBody2);
                        voucherBodyList.add(voucherBody);
                        voucherBodyList.add(voucherBody2);
                        voucherBodyList.add(voucherBody3);
                        voucher.setVoucherHead(voucherHead);
                        voucher.setVoucherBodyList(voucherBodyList);
                        // 科目联动更新
                        boolean bool = vatService.checkVouch(param, voucher);
                        if (bool) {
                            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                        }

                    } else {
                        VoucherBody voucherBody2 = new VoucherBody();
                        String uuid2 = UUIDUtils.getUUID();
                        // 主键
                        voucherBody2.setVouchAID(uuid2);
                        // 凭证主表主键
                        voucherBody2.setVouchID(uuid);
                        // 分录号
                        voucherBody2.setRowIndex("2");
                        // 摘要
                        voucherBody2.setVcabstact(null);
                        // 科目名称
                        voucherBody2.setVcsubject(tBasicDocuments2.getFullNameCredit());
                        // 计量单位ID
                        if (StringUtils.isNotBlank(documentsUnitid)) {
                            voucherBody.setVcunitID(documentsUnitid);
                        }
                        // 计量单位
                        voucherBody2.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 期间
                        voucherBody2.setPeriod(accountPeriod);
                        // 贷方金额
                        voucherBody2.setCreditAmount(amountDebitDouble);
                        // 借方金额
                        voucherBody2.setDebitAmount(null);
                        // 账套ID
                        voucherBody2.setAccountID(accountId);
                        // 租户ID
                        voucherBody2.setUserID(userId);
                        // 方向(1:借2:贷)
                        voucherBody2.setDirection("2");
                        // 科目主键 (是科目代码)
                        voucherBody2.setSubjectID(tBasicDocuments2.getSubCodeCredit());
                        // 修改人ID
                        voucherBody2.setUpdatePsnID(userId);
                        // 修改人
                        voucherBody2.setUpdatePsn(userId);
                        // 修改时间
                        voucherBody2.setUpdatedate(date);
                        // 数量
                        voucherBody.setNumber(number);
                        // 单价
                        voucherBody2.setPrice(price);
                        // 是否有问题 1有问题2没问题
                        voucherBody2.setIsproblem("2");
                        // 备注
                        voucherBody2.setDes("单据新增 - 贷方 ");

                        voucherBodyDao.insertVouchBody(voucherBody2);
                        voucherBodyList.add(voucherBody);
                        voucherBodyList.add(voucherBody2);
                        voucher.setVoucherHead(voucherHead);
                        voucher.setVoucherBodyList(voucherBodyList);
                        // 科目联动更新
                        boolean bool = vatService.checkVouch(param, voucher);
                        if (bool) {
                            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                        }

                    }

                }

            }

            List<Voucher> voucherList2 = new ArrayList<Voucher>();

            for (Object object : hashSet) {
                Voucher voucher2 = new Voucher();
                // 贷方科目id
                String pkSubIdCtedit = object.toString();
                List<VoucherBody> voucherBodyList = new ArrayList<VoucherBody>();
                // 凭证体
                VoucherBody voucherBody = new VoucherBody();
                // 凭证头
                VoucherHead voucherHead = new VoucherHead();
                // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                BigDecimal amountCreditTotal = new BigDecimal("0");
                // 凭证头ID
                String vouchId = new UUIDUtils().getUUID();
                // 凭证头主键
                voucherHead.setVouchID(vouchId);
                // 生成日期
                voucherHead.setVcDate(new Date());
                // 备注
                voucherHead.setDes("单据生成-采购");
                // 创建人ID
                voucherHead.setCreatePsnID(userId);
                // 创建人
                voucherHead.setCreatepsn(userName);
                // 创建日期
                voucherHead.setCreateDate(System.currentTimeMillis());
                // 账套ID
                voucherHead.setAccountID(accountId);
                // 期间
                voucherHead.setPeriod(accountPeriod);
                // 审核状态(0:未审核1:审核)
                voucherHead.setAuditStatus(0);
                // 来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
                voucherHead.setSource(6);
                // 0:非模凭证1:模板凭证
                voucherHead.setVouchFlag(0);

                // 贷方摘要
                String subNameCredit = null;
                String fullNameCredit = null;
                // 贷方科目编码
                String subCodeCredit = null;
                int rowIndex = 1;
                // 得到每个借方分录
                for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                    // 贷方科目id
                    String pkSubIdCtedit2 = tBasicDocuments2.getPkSubIdCtedit();
                    VoucherBody voucherBody2 = new VoucherBody();

                    if (tBasicDocuments2.getDocumentsType().equals("1") && pkSubIdCtedit.equals(pkSubIdCtedit2)) {
                        // 借方金额
                        BigDecimal amountDebit = tBasicDocuments2.getAmountDebit();
                        double amountDebitDouble = amountDebit.doubleValue();
                        // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                        amountCreditTotal = amountCreditTotal.add(amountDebit);
                        // 帐套id
                        voucherBody2.setAccountID(accountId);
                        // 借方金额
                        voucherBody2.setDebitAmount(amountDebitDouble);
                        // 帐套期间
                        voucherBody2.setPeriod(accountPeriod);
                        // 凭证主表主键
                        voucherBody2.setVouchID(vouchId);
                        // 分录号
                        String valueOf = String.valueOf(rowIndex);
                        voucherBody2.setRowIndex(valueOf);

                        // 摘要
                        if (rowIndex == 1) {
                            voucherBody2.setVcabstact(StringUtil.getZY(tBasicDocuments2));
                        } else {
                            voucherBody2.setVcabstact(null);
                        }

                        // 科目名称
                        voucherBody2.setVcsubject(tBasicDocuments2.getFullNameDebit());
                        // 计量单位ID
                        String documentsUnitid = StringUtil.objToStr(tBasicDocuments2.getDocumentsUnitid());

                        voucherBody2.setVcunitID(documentsUnitid);

                        // 计量单位
                        voucherBody2.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 借方科目id
                        voucherBody2.setSubjectID(tBasicDocuments2.getPkSubIdDebit());
                        // 方向(1:借2:贷)
                        voucherBody2.setDirection("1");
                        // 科目主键 (是科目代码)
                        voucherBody2.setSubjectID(tBasicDocuments2.getSubCodeDebit());
                        // 修改人ID
                        voucherBody2.setUpdatePsnID(tBasicDocuments2.getPkSubIdDebit());
                        // 修改人
                        voucherBody2.setUpdatePsn(userId);
                        // 修改时间
                        voucherBody2.setUpdatedate(new Date());
                        // 数量
                        BigDecimal documentsNumber = StringUtil.bigDecimalIsNull(tBasicDocuments2.getDocumentsNumber());

                        voucherBody2.setNumber(documentsNumber.doubleValue());
                        // 单价
                        BigDecimal documentsDecimal = StringUtil.bigDecimalIsNull(tBasicDocuments2.getDocumentsDecimal());
                        voucherBody2.setPrice(documentsDecimal.doubleValue());
                        // 是否有问题1有问题2没问题
                        voucherBody2.setIsproblem("2");
                        // 凭证分录id
                        voucherBody2.setVouchAID(new UUIDUtils().getUUID());
                        // 采购借方
                        voucherBody2.setDes("采购-借方");

                        voucherBodyList.add(voucherBody2);
                        rowIndex++;

                        // 贷方就一个 设置贷方数据
                        fullNameCredit = tBasicDocuments2.getFullNameCredit();
                        // 贷方科目编码
                        subCodeCredit = tBasicDocuments2.getSubCodeCredit();
                        subNameCredit = tBasicDocuments2.getSubNameCredit();
                        Integer insertVouchBody2 = voucherBodyDao.insertVouchBody(voucherBody2);
                    }
                }
                // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                double amountCreditTotalDouble = amountCreditTotal.doubleValue();
                // 凭证分录id
                voucherBody.setVouchAID(new UUIDUtils().getUUID());
                voucherBody.setCreditAmount(amountCreditTotalDouble);
                // 贷方科目编码
                voucherBody.setSubjectID(pkSubIdCtedit);
                // 备注 采购-贷方
                voucherBody.setDes("采购-贷方");
                // 科目期间
                voucherBody.setPeriod(accountPeriod);
                // 帐套id
                voucherBody.setAccountID(accountId);
                // 凭证主表主键
                voucherBody.setVouchID(vouchId);
                // 分录号
                voucherBody.setRowIndex(String.valueOf(rowIndex));
                // 摘要
                voucherBody.setVcabstact(null);
                // 科目名称
                voucherBody.setVcsubject(fullNameCredit);
                // 期间
                voucherBody.setPeriod(accountPeriod);
                // 方向(1:借2:贷)
                voucherBody.setDirection("2");
                // 科目主键 (是科目代码)
                voucherBody.setSubjectID(subCodeCredit);
                // 修改人ID
                voucherBody.setUpdatePsnID(userId);
                // 修改人
                voucherBody.setUpdatePsn(userId);
                // 修改时间
                voucherBody.setUpdatedate(new Date());
                // 是否有问题1有问题2没问题
                voucherBody.setIsproblem("2");
                Integer insertVouchBody2 = voucherBodyDao.insertVouchBody(voucherBody);

                voucherBodyList.add(voucherBody);

                // 插入凭证头
                // 凭证贷方金额合计
                voucherHead.setTotalCredit(amountCreditTotalDouble);
                // 凭证借方金额合计
                voucherHead.setTotalDbit(amountCreditTotalDouble);
                // 凭证号
                Integer queryMaxVouchNO = voucherHeadDao.queryMaxVouchNO(accountId, accountPeriod);
                voucherHead.setVoucherNO(queryMaxVouchNO + 1);
                Integer insertVouchHead = voucherHeadDao.insertVouchHead(voucherHead);

                voucher2.setVoucherBodyList(voucherBodyList);
                voucher2.setVoucherHead(voucherHead);
                voucherList2.add(voucher2);
                // 科目联动更新
                boolean bool = vatService.checkVouch(param, voucher2);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher2);
                }
            }
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: documentsToVouchers
     * @Description: 采购生成凭证
     * @date 2018年4月1日 下午5:13:25
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("static-access")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> documentsToVouchers(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HashMap<String, Object> param = new HashMap<>();
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            String userName = user.getUserName();// 用户名称
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String accountPeriod = (String) sessionMap.get("busDate");
            param.put("userId", userId);
            param.put("accountID", accountId);
            param.put("busDate", accountPeriod);

            TBasicDocuments tBasicDocuments = new TBasicDocuments();
            tBasicDocuments.setAccountId(accountId);
            tBasicDocuments.setAccountPeriod(accountPeriod);

            List<TBasicDocuments> queryDocumentsList = tBasicDocumentsMapper.queryDocumentsList(tBasicDocuments);

            for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                if (!tBasicDocuments2.getDocumentsType().equals("1")) {
                    // 单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                    Voucher voucher = new Voucher();
                    List<VoucherBody> voucherBodyList = new ArrayList<VoucherBody>();
                    Integer queryMaxVouchNO = voucherHeadDao.queryMaxVouchNO(accountId, accountPeriod);

                    // 贷方金额
                    BigDecimal amountCredit = tBasicDocuments2.getAmountCredit();
                    double amountCreditDouble = amountCredit.doubleValue();
                    // 借方金额
                    BigDecimal amountDebit = tBasicDocuments2.getAmountDebit();
                    double amountDebitDouble = amountDebit.doubleValue();
                    Date date = new Date();
                    VoucherHead voucherHead = new VoucherHead();
                    String uuid = UUIDUtils.getUUID();
                    // 凭证头主键
                    voucherHead.setVouchID(uuid);
                    // 生成日期
                    voucherHead.setVcDate(date);
                    // 备注
                    voucherHead.setDes("单据生成");
                    // 创建人ID
                    voucherHead.setCreatePsnID(userId);
                    // 创建人
                    voucherHead.setCreatepsn(userName);
                    // 创建日期
                    voucherHead.setCreateDate(System.currentTimeMillis());
                    // 账套ID
                    voucherHead.setAccountID(accountId);
                    // 期间
                    voucherHead.setPeriod(accountPeriod);
                    // 审核状态(0:未审核1:审核)
                    voucherHead.setAuditStatus(0);
                    // 来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
                    voucherHead.setSource(6);
                    // 凭证贷方金额合计
                    voucherHead.setTotalCredit(amountCreditDouble);
                    // 凭证借方金额合计
                    voucherHead.setTotalDbit(amountDebitDouble);
                    // 0:非模凭证1:模板凭证
                    voucherHead.setVouchFlag(0);
                    // 凭证号
                    voucherHead.setVoucherNO(queryMaxVouchNO + 1);
                    Integer insertVouchHead = voucherHeadDao.insertVouchHead(voucherHead);
                    if (insertVouchHead == 1) {
                        VoucherBody voucherBody = new VoucherBody();
                        String uuid1 = UUIDUtils.getUUID();
                        // 主键
                        voucherBody.setVouchAID(uuid1);
                        // 凭证主表主键
                        voucherBody.setVouchID(uuid);
                        // 分录号
                        voucherBody.setRowIndex("1");
                        // 摘要
                        voucherBody.setVcabstact(tBasicDocuments2.getFullNameDebit());
                        // 科目名称
                        voucherBody.setVcsubject(tBasicDocuments2.getFullNameDebit());
                        // 计量单位ID
                        String documentsUnitid = tBasicDocuments2.getDocumentsUnitid();
                        if (StringUtils.isNotBlank(documentsUnitid)) {
                            voucherBody.setVcunitID(documentsUnitid);
                        }
                        // 计量单位
                        voucherBody.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 期间
                        voucherBody.setPeriod(accountPeriod);
                        // 借方金额
                        voucherBody.setDebitAmount(amountDebitDouble);
                        // 贷方金额
                        // voucherBody.setCreditAmount(creditAmount);
                        // 账套ID
                        voucherBody.setAccountID(accountId);
                        // 租户ID
                        voucherBody.setUserID(userId);
                        // 方向(1:借2:贷)
                        voucherBody.setDirection("1");
                        // 科目主键 (是科目代码)
                        voucherBody.setSubjectID(tBasicDocuments2.getSubCodeDebit());
                        // 修改人ID
                        voucherBody.setUpdatePsnID(tBasicDocuments2.getPkSubIdDebit());
                        // 修改人
                        voucherBody.setUpdatePsn(uuid);
                        // 修改时间
                        voucherBody.setUpdatedate(date);
                        // 数量
                        BigDecimal documentsNumber = tBasicDocuments2.getDocumentsNumber();
                        double number = 0.0;
                        if (documentsNumber != null && !(documentsNumber.compareTo(new BigDecimal("0")) == 0)) {
                            number = documentsNumber.doubleValue();
                        }
                        voucherBody.setNumber(number);
                        // 单价
                        BigDecimal documentsDecimal = tBasicDocuments2.getDocumentsDecimal();
                        double price = 0.0;
                        if (documentsDecimal != null && !(documentsDecimal.compareTo(new BigDecimal("0")) == 0)) {
                            price = documentsDecimal.doubleValue();
                        }
                        voucherBody.setPrice(price);
                        // 是否有问题1有问题2没问题
                        voucherBody.setIsproblem("2");
                        // 备注
                        voucherBody.setDes("单据新增 - 借方");
                        Integer insertVouchBody = voucherBodyDao.insertVouchBody(voucherBody);
                        if (insertVouchBody == 1) {
                            VoucherBody voucherBody2 = new VoucherBody();
                            String uuid2 = UUIDUtils.getUUID();
                            // 主键
                            voucherBody2.setVouchAID(uuid2);
                            // 凭证主表主键
                            voucherBody2.setVouchID(uuid);
                            // 分录号
                            voucherBody2.setRowIndex("1");
                            // 摘要
                            voucherBody2.setVcabstact(tBasicDocuments2.getFullNameCredit());
                            // 科目名称
                            voucherBody2.setVcsubject(tBasicDocuments2.getFullNameCredit());
                            // 计量单位ID
                            if (StringUtils.isNotBlank(documentsUnitid)) {
                                voucherBody.setVcunitID(documentsUnitid);
                            }
                            // 计量单位
                            voucherBody2.setVcunit(tBasicDocuments2.getDocumentsUnit());
                            // 期间
                            voucherBody2.setPeriod(accountPeriod);
                            // 贷方金额
                            voucherBody2.setCreditAmount(amountDebitDouble);
                            // 借方金额
                            // voucherBody2.setDebitAmount(amountDebitDouble);
                            // 账套ID
                            voucherBody2.setAccountID(accountId);
                            // 租户ID
                            voucherBody2.setUserID(userId);
                            // 方向(1:借2:贷)
                            voucherBody2.setDirection("2");
                            // 科目主键 (是科目代码)
                            voucherBody2.setSubjectID(tBasicDocuments2.getSubCodeCredit());
                            // 修改人ID
                            voucherBody2.setUpdatePsnID(tBasicDocuments2.getPkSubIdCtedit());
                            // 修改人
                            voucherBody2.setUpdatePsn(uuid);
                            // 修改时间
                            voucherBody2.setUpdatedate(date);
                            // 数量
                            if (documentsNumber != null && !(documentsNumber.compareTo(new BigDecimal("0")) == 0)) {
                                number = documentsNumber.doubleValue();
                            }
                            voucherBody.setNumber(number);
                            // 单价
                            if (documentsDecimal != null && !(documentsDecimal.compareTo(new BigDecimal("0")) == 0)) {
                                price = documentsDecimal.doubleValue();
                            }
                            voucherBody2.setPrice(price);
                            // 是否有问题 1有问题2没问题
                            voucherBody2.setIsproblem("2");
                            // 备注
                            voucherBody2.setDes("单据新增 - 贷方 ");

                            Integer insertVouchBody2 = voucherBodyDao.insertVouchBody(voucherBody2);
                            voucherBodyList.add(voucherBody);
                            voucherBodyList.add(voucherBody2);
                            voucher.setVoucherHead(voucherHead);
                            voucher.setVoucherBodyList(voucherBodyList);
                            // 科目联动更新
                            boolean bool = vatService.checkVouch(param, voucher);
                            if (bool) {
                                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                            }
                        }
                    }
                }
            }

            HashSet<Object> hashSet = new HashSet<>();
            for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                if (tBasicDocuments2.getDocumentsType().equals("1")) {
                    String pkSubIdCtedit = tBasicDocuments2.getPkSubIdCtedit();
                    hashSet.add(pkSubIdCtedit);
                }
            }

            List<Voucher> voucherList2 = new ArrayList<Voucher>();

            // [8ef85642e220421a90f8f7d2efaac09c,
            // 5f7bd1aac1a14ed5a266fc5040317ecc]
            for (Object object : hashSet) {
                Voucher voucher2 = new Voucher();
                // 贷方科目id
                String pkSubIdCtedit = object.toString();
                List<VoucherBody> voucherBodyList = new ArrayList<VoucherBody>();
                // 凭证体
                VoucherBody voucherBody = new VoucherBody();
                // 凭证头
                VoucherHead voucherHead = new VoucherHead();
                // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                BigDecimal amountCreditTotal = new BigDecimal("0");
                // 凭证头ID
                String vouchId = new UUIDUtils().getUUID();
                // 凭证头主键
                voucherHead.setVouchID(vouchId);
                // 生成日期
                voucherHead.setVcDate(new Date());
                // 备注
                voucherHead.setDes("单据生成-采购");
                // 创建人ID
                voucherHead.setCreatePsnID(userId);
                // 创建人
                voucherHead.setCreatepsn(userName);
                // 创建日期
                voucherHead.setCreateDate(System.currentTimeMillis());
                // 账套ID
                voucherHead.setAccountID(accountId);
                // 期间
                voucherHead.setPeriod(accountPeriod);
                // 审核状态(0:未审核1:审核)
                voucherHead.setAuditStatus(0);
                // 来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
                voucherHead.setSource(6);
                // 0:非模凭证1:模板凭证
                voucherHead.setVouchFlag(0);

                // 贷方摘要
                String subNameCredit = null;
                String fullNameCredit = null;
                // 贷方科目编码
                String subCodeCredit = null;
                // 得到每个借方分录
                for (TBasicDocuments tBasicDocuments2 : queryDocumentsList) {
                    // 贷方科目id
                    String pkSubIdCtedit2 = tBasicDocuments2.getPkSubIdCtedit();
                    VoucherBody voucherBody2 = new VoucherBody();

                    if (tBasicDocuments2.getDocumentsType().equals("1") && pkSubIdCtedit.equals(pkSubIdCtedit2)) {
                        // 借方金额
                        BigDecimal amountDebit = tBasicDocuments2.getAmountDebit();
                        double amountDebitDouble = amountDebit.doubleValue();
                        // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                        amountCreditTotal = amountCreditTotal.add(amountDebit);
                        // 帐套id
                        voucherBody2.setAccountID(accountId);
                        // 借方金额
                        voucherBody2.setDebitAmount(amountDebitDouble);
                        // 帐套期间
                        voucherBody2.setPeriod(accountPeriod);
                        // 凭证主表主键
                        voucherBody2.setVouchID(vouchId);
                        // 分录号
                        voucherBody2.setRowIndex("1");
                        // 摘要
                        voucherBody2.setVcabstact(tBasicDocuments2.getFullNameDebit());
                        // 科目名称
                        voucherBody2.setVcsubject(tBasicDocuments2.getFullNameDebit());
                        // 计量单位ID
                        String documentsUnitid = tBasicDocuments2.getDocumentsUnitid();
                        if (StringUtils.isNotBlank(documentsUnitid)) {
                            voucherBody2.setVcunitID(documentsUnitid);
                        }
                        // 计量单位
                        voucherBody2.setVcunit(tBasicDocuments2.getDocumentsUnit());
                        // 借方科目id
                        voucherBody2.setSubjectID(tBasicDocuments2.getPkSubIdDebit());
                        // 方向(1:借2:贷)
                        voucherBody2.setDirection("1");
                        // 科目主键 (是科目代码)
                        voucherBody2.setSubjectID(tBasicDocuments2.getSubCodeDebit());
                        // 修改人ID
                        voucherBody2.setUpdatePsnID(tBasicDocuments2.getPkSubIdDebit());
                        // 修改人
                        voucherBody2.setUpdatePsn(userId);
                        // 修改时间
                        voucherBody2.setUpdatedate(new Date());
                        // 数量
                        BigDecimal documentsNumber = tBasicDocuments2.getDocumentsNumber();
                        double number = 0.0;
                        if (documentsNumber != null && !(documentsNumber.compareTo(new BigDecimal("0")) == 0)) {
                            number = documentsNumber.doubleValue();
                        }
                        voucherBody2.setNumber(number);
                        // 单价
                        BigDecimal documentsDecimal = tBasicDocuments2.getDocumentsDecimal();
                        double price = 0.0;
                        if (documentsDecimal != null && !(documentsDecimal.compareTo(new BigDecimal("0")) == 0)) {
                            price = documentsDecimal.doubleValue();
                        }
                        voucherBody2.setPrice(price);
                        // 是否有问题1有问题2没问题
                        voucherBody2.setIsproblem("2");
                        // 凭证分录id
                        voucherBody2.setVouchAID(new UUIDUtils().getUUID());
                        // 采购借方
                        voucherBody2.setDes("采购-借方");

                        voucherBodyList.add(voucherBody2);

                        // 贷方就一个 设置贷方数据
                        fullNameCredit = tBasicDocuments2.getFullNameCredit();
                        // 贷方科目编码
                        subCodeCredit = tBasicDocuments2.getSubCodeCredit();
                        subNameCredit = tBasicDocuments2.getSubNameCredit();
                        Integer insertVouchBody2 = voucherBodyDao.insertVouchBody(voucherBody2);
                    }
                }
                // 贷方金额（贷方金额 和 贷方金额总和，借方金额总和 都是这个值）
                double amountCreditTotalDouble = amountCreditTotal.doubleValue();
                // 凭证分录id
                voucherBody.setVouchAID(new UUIDUtils().getUUID());
                voucherBody.setCreditAmount(amountCreditTotalDouble);
                // 贷方科目编码
                voucherBody.setSubjectID(pkSubIdCtedit);
                // 备注 采购-贷方
                voucherBody.setDes("采购-贷方");
                // 科目期间
                voucherBody.setPeriod(accountPeriod);
                // 帐套id
                voucherBody.setAccountID(accountId);
                // 凭证主表主键
                voucherBody.setVouchID(vouchId);
                // 分录号
                voucherBody.setRowIndex("2");
                // 摘要
                voucherBody.setVcabstact(fullNameCredit);
                // 科目名称
                voucherBody.setVcsubject(fullNameCredit);
                // 期间
                voucherBody.setPeriod(accountPeriod);
                // 方向(1:借2:贷)
                voucherBody.setDirection("2");
                // 科目主键 (是科目代码)
                voucherBody.setSubjectID(subCodeCredit);
                // 修改人ID
                voucherBody.setUpdatePsnID(userId);
                // 修改人
                voucherBody.setUpdatePsn(userId);
                // 修改时间
                voucherBody.setUpdatedate(new Date());
                // 是否有问题1有问题2没问题
                voucherBody.setIsproblem("2");
                Integer insertVouchBody2 = voucherBodyDao.insertVouchBody(voucherBody);

                voucherBodyList.add(voucherBody);

                // 插入凭证头
                // 凭证贷方金额合计
                voucherHead.setTotalCredit(amountCreditTotalDouble);
                // 凭证借方金额合计
                voucherHead.setTotalDbit(amountCreditTotalDouble);
                // 凭证号
                Integer queryMaxVouchNO = voucherHeadDao.queryMaxVouchNO(accountId, accountPeriod);
                voucherHead.setVoucherNO(queryMaxVouchNO + 1);
                Integer insertVouchHead = voucherHeadDao.insertVouchHead(voucherHead);

                voucher2.setVoucherBodyList(voucherBodyList);
                voucher2.setVoucherHead(voucherHead);
                voucherList2.add(voucher2);
                // 科目联动更新
                boolean bool = vatService.checkVouch(param, voucher2);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher2);
                }
            }
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteDocuments(HttpSession session, String param) {

        return null;
    }

    /**
     * @param session
     * @param querySbujectBalance
     * @return
     * @Title: procurementInventoryRow
     * @Description: (非 JavaDoc) 获取采购库存商品科目列表
     * @see com.wqb.service.documents.TBasicDocumentsService#procurementInventoryRow(javax.servlet.http.HttpSession,
     * java.util.Map)
     */
    @Override
    public Map<String, Object> procurementInventoryRow(HttpSession session, Map<String, Object> querySbujectBalance) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (querySbujectBalance.get("subMessages") != null
                    && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
                List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance
                        .get("subMessages");
                List<TBasicSubjectMessage> debitList = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> creditList = new ArrayList<TBasicSubjectMessage>();
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                    String subCode = tBasicSubjectMessage.getSubCode();
                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    // 库存商品
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("1405") && codeLevel > 1) {
                        debitList.add(tBasicSubjectMessage);
                    }
                }
                result.put("debitList", debitList);
                result.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @param querySbujectBalance
     * @return
     * @Title: procurementRawMaterialsRow
     * @Description: (非 JavaDoc) 获取采购原材料科目列表
     * @see com.wqb.service.documents.TBasicDocumentsService#procurementRawMaterialsRow(javax.servlet.http.HttpSession,
     * java.util.Map)
     */
    @Override
    public Map<String, Object> procurementRawMaterialsRow(HttpSession session,
                                                          Map<String, Object> querySbujectBalance) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (querySbujectBalance.get("subMessages") != null
                    && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
                List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance
                        .get("subMessages");
                List<TBasicSubjectMessage> debitList = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> creditList = new ArrayList<TBasicSubjectMessage>();
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                    String subCode = tBasicSubjectMessage.getSubCode();
                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    // 库存商品
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("1403") && codeLevel > 1) {
                        debitList.add(tBasicSubjectMessage);
                    }
                }
                result.put("debitList", debitList);
                result.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @param querySbujectBalance
     * @param creditSubCode
     * @return
     * @throws BusinessException
     * @Title: procurementCreditRow
     * @Description: (非 JavaDoc) 获取采购贷方科目列表 （应付，预付，现金，银行）
     * @see com.wqb.service.documents.TBasicDocumentsService#procurementCreditRow(javax.servlet.http.HttpSession,
     * java.util.Map, java.lang.String)
     */
    @Override
    public Map<String, Object> procurementCreditRow(HttpSession session, Map<String, Object> querySbujectBalance,
                                                    String creditSubCode) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (querySbujectBalance.get("subMessages") != null
                    && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
                List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance
                        .get("subMessages");
                List<TBasicSubjectMessage> debitList = new ArrayList<TBasicSubjectMessage>();
                List<TBasicSubjectMessage> creditList = new ArrayList<TBasicSubjectMessage>();
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                    String subCode = tBasicSubjectMessage.getSubCode();
                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    // 应付账款
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith(creditSubCode) && codeLevel > 1) {
                        creditList.add(tBasicSubjectMessage);
                    }
                }
                result.put("creditList", creditList);
                result.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @param querySbujectBalance
     * @return
     * @throws BusinessException
     * @Title: salesCreditRow
     * @Description: 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款）
     * 1122下级科目（匹配未交增值税科目）有税金要添加
     * @see com.wqb.service.documents.TBasicDocumentsService#salesCreditRow(javax.servlet.http.HttpSession,
     * java.util.Map)
     */
    @Override
    public Map<String, Object> salesCreditRow(HttpSession session, Map<String, Object> querySbujectBalance)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (querySbujectBalance.get("subMessages") != null
                    && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
                List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance
                        .get("subMessages");
                List<TBasicSubjectMessage> TBasicSubjectMessageList = new ArrayList<TBasicSubjectMessage>();
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                    String subCode = tBasicSubjectMessage.getSubCode();
                    String subName = tBasicSubjectMessage.getSubName();

                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("6001")) {
                        TBasicSubjectMessageList.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("1001")) {
                        TBasicSubjectMessageList.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("1122") && codeLevel > 1) {
                        TBasicSubjectMessageList.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("2203") && codeLevel > 1) {
                        TBasicSubjectMessageList.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isNotBlank(subCode) && subCode.startsWith("2221") && codeLevel > 1) {
                        if (StringUtils.isNotBlank(subName) && subName.equals("未交增值税")) {
                            TBasicSubjectMessageList.add(tBasicSubjectMessage);
                        }
                        if (StringUtils.isNotBlank(subName) && subName.contains("免税")) {
                            TBasicSubjectMessageList.add(tBasicSubjectMessage);
                        }
                    }
                }
                result.put("ticketsCostRow", TBasicSubjectMessageList);
                result.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 待删除
     **/
    @Override
    public Map<String, Object> addsalesCreditList(User user, Account account,
                                                  TBasicSubjectMessageService tBasicSubjectMessageService) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
            List<TBasicSubjectMessage> tBasicSubjectMessageList = (List) querySubMessage.get("subMessage");
            // 获取用户信息
            @SuppressWarnings("unchecked")
            String userId = user.getUserID();// 用户id
            String accountId = account.getAccountID();// 账套id
            String busDate = account.getUseLastPeriod();

            String subName6001 = null;
            String fullName6001 = null;
            String pkSubId6001 = null;
            String subCode6001 = null;
            List<TBasicDocuments> tBasicDocumentslist = new ArrayList<TBasicDocuments>();
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                String subCode = tBasicSubjectMessage.getSubCode();
                if (subCode.startsWith("6001")) {
                    subName6001 = tBasicSubjectMessage.getSubName();
                    fullName6001 = tBasicSubjectMessage.getFullName();
                    pkSubId6001 = tBasicSubjectMessage.getPkSubId();
                    subCode6001 = tBasicSubjectMessage.getSubCode();
                }
            }
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                TBasicDocuments tBasicDocuments = new TBasicDocuments();
                String subCode = tBasicSubjectMessage.getSubCode();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                // if (subCode.startsWith("6001"))
                // {
                // subName6001 = tBasicSubjectMessage.getSubName();
                // fullName6001 = tBasicSubjectMessage.getFullName();
                // pkSubId6001 = tBasicSubjectMessage.getPkSubId();
                // subCode6001 = tBasicSubjectMessage.getSubCode();
                // }
                if (StringUtils.isNotBlank(pkSubId6001)) {
                    if (subCode.startsWith("1001") && codeLevel > 1) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = tBasicSubjectMessage.getPkSubId();
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = tBasicSubjectMessage.getSubName();
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = tBasicSubjectMessage.getFullName();
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("2");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName6001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId6001);
                        tBasicDocuments.setSubNameCredit(subName6001);
                        tBasicDocuments.setSubCodeCredit(subCode6001);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }
                    if (subCode.startsWith("1122") && codeLevel > 1) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = tBasicSubjectMessage.getPkSubId();
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = tBasicSubjectMessage.getSubName();
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = tBasicSubjectMessage.getFullName();
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName6001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId6001);
                        tBasicDocuments.setSubNameCredit(subName6001);
                        tBasicDocuments.setSubCodeCredit(subCode6001);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }
                    if (subCode.startsWith("2203") && codeLevel > 1) {
                        String uuid = UUIDUtils.getUUID();
                        tBasicDocuments.setPkDocumentsId(uuid);
                        tBasicDocuments.setAccountPeriod(busDate);
                        Date date = new Date();
                        tBasicDocuments.setAccountId(accountId);
                        tBasicDocuments.setUserId(userId);
                        tBasicDocuments.setCreatePsn(userId);
                        tBasicDocuments.setCreateDate(date);
                        String pkSubIdDebit = tBasicSubjectMessage.getPkSubId();
                        tBasicDocuments.setPkSubIdDebit(pkSubIdDebit);
                        String subName = tBasicSubjectMessage.getSubName();
                        tBasicDocuments.setSubNameDebit(subName);
                        String fullNameDebit = tBasicSubjectMessage.getFullName();
                        tBasicDocuments.setSummary(fullNameDebit);
                        tBasicDocuments.setProjectName(fullNameDebit);
                        tBasicDocuments.setDocumentsType("4");
                        tBasicDocuments.setSubCodeDebit(subCode);
                        tBasicDocuments.setFullNameDebit(fullNameDebit);
                        tBasicDocuments.setFullNameCredit(fullName6001);
                        tBasicDocuments.setPkSubIdCtedit(pkSubId6001);
                        tBasicDocuments.setSubNameCredit(subName6001);
                        tBasicDocuments.setSubCodeCredit(subCode6001);
                        tBasicDocumentslist.add(tBasicDocuments);
                    }
                }
            }
            int no = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentslist);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException
     * @Title: addProcurementInventoryList
     * @Description: 添加 采购库存商品 集合
     * @see com.wqb.service.documents.TBasicDocumentsService#addProcurementInventoryList(javax.servlet.http.HttpSession,
     * java.lang.String, java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addProcurementInventoryList(HttpSession session, String tBasicSubjectMessage,
                                                           String tBasicSubjectMessageList) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicDocuments> tBasicDocumentsList = new ArrayList<TBasicDocuments>();
        try {
            Gson gson = new Gson();
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");

            /** tBasicSubjectMessage 解析 单个json对象 **/
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            // json 对象转换成 map
            // tBasicSubjectMessage =
            // {"pkSubId":"de4e17a883c649008d19f5bddaed83c7","subCode":"2202001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"北京网聘咨询有限公司深圳分公司","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":0,"yearAmountCredit":0,"endingBalanceDebit":0,"excelImportCode":"2202001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"2202","superiorCoding":"2202","fullName":"应付账款_北京网聘咨询有限公司深圳分公司","updateDate":1521770370000,"updateTimestamp":"1521770369767","category":"2","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"2","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null}
            Map<String, String> tBasicSubjectMessageMap = gson.fromJson(tBasicSubjectMessage, type);

            // 贷方科目主键
            String pkSubIdCtedit = tBasicSubjectMessageMap.get("pkSubId");
            // 贷方科目代码
            String subCodeCredit = tBasicSubjectMessageMap.get("subCode");
            // 贷方科目名称
            String subNameCredit = tBasicSubjectMessageMap.get("subName");
            // 贷方科目完整名称
            String fullNameCredit = tBasicSubjectMessageMap.get("fullName");

            // 税金
            BigDecimal taxAmount = new BigDecimal("0");
            String taxAmountString = tBasicSubjectMessageMap.get("taxAmount");
            if (StringUtils.isNotBlank(taxAmountString)) {
                BigDecimal taxAmountBigDecimal = new BigDecimal(taxAmountString);
                taxAmount = taxAmount.add(taxAmountBigDecimal);
            }

            /** tBasicSubjectMessageList 解析 json 数组对象 **/
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            // json 对象转换成 ArrayList
            // tBasicSubjectMessageList =
            // [{"pkSubId":"66fc83664ead4eab975570de0751a20f","subCode":"1405001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"电源","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":1389334.86,"yearAmountCredit":1460124.92,"endingBalanceDebit":0,"excelImportCode":"1405001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"1405","superiorCoding":"1405","fullName":"库存商品_电源","updateDate":1521770370000,"updateTimestamp":"1521770369715","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"1","amountDebit":null,"documentsType":1,"documentsUnit":"亩","documentsUnitId":"00df22d4e84f4652aae1146c964599f5","documentsDecimal":"1","documentsNumber":"1","documentsAmount":"1","taxAmount":null}]
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(tBasicSubjectMessageList, type2);
            int size = jsonObjects.size();
            BigDecimal sizeBigDecimal = new BigDecimal(size);
            taxAmount = taxAmount.divide(sizeBigDecimal, 8, BigDecimal.ROUND_HALF_UP);
            // 遍历 json 集合
            for (JsonObject jsonObject : jsonObjects) {
                // 解析 单个json对象
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                // json 对象转换成 map
                Map<String, String> tBasicSubjectMessageListMap = gson.fromJson(jsonObject, type3);

                TBasicDocuments tBasicDocuments = new TBasicDocuments();

                UUIDUtils uuidUtils = new UUIDUtils();
                // 单据主键
                tBasicDocuments.setPkDocumentsId(uuidUtils.getUUID());
                // 用户id
                tBasicDocuments.setUserId(userId);
                // 帐套id
                tBasicDocuments.setAccountId(accountId);
                // 会计期间
                tBasicDocuments.setAccountPeriod(busDate);
                // 摘要
                tBasicDocuments.setSummary("添加 采购库存商品 集合");
                // 项目名称
                tBasicDocuments.setProjectName(tBasicSubjectMessageListMap.get("fullName"));
                // 贷方金额
                BigDecimal amountCredit = new BigDecimal("0");
                String amountCreditString = tBasicSubjectMessageListMap.get("documentsAmount");
                if (StringUtils.isNotBlank(amountCreditString)) {
                    BigDecimal amountCreditBigDecimal = new BigDecimal(amountCreditString);
                    amountCredit = amountCredit.add(amountCreditBigDecimal);
                }
                tBasicDocuments.setAmountCredit(amountCredit);
                // 贷方科目主键
                tBasicDocuments.setPkSubIdCtedit(pkSubIdCtedit);
                // 贷方科目代码
                tBasicDocuments.setSubCodeCredit(subCodeCredit);
                // 贷方科目名称
                tBasicDocuments.setSubNameCredit(subNameCredit);
                // 贷方科目完整名称
                tBasicDocuments.setFullNameCredit(fullNameCredit);

                // 税金
                tBasicDocuments.setTaxAmount(taxAmount);

                // 计量单位 documentsUnit
                String documentsUnit = tBasicSubjectMessageListMap.get("documentsUnit");
                tBasicDocuments.setDocumentsUnit(documentsUnit);

                // 计量单位ID documentsUnitId
                String documentsUnitid = tBasicSubjectMessageListMap.get("documentsUnitId");
                tBasicDocuments.setDocumentsUnitid(documentsUnitid);

                // 数量 documentsNumber
                BigDecimal documentsNumber = new BigDecimal("0");
                String documentsNumberString = tBasicSubjectMessageListMap.get("documentsNumber");
                if (StringUtils.isNotBlank(documentsNumberString)) {
                    BigDecimal documentsNumberBigDecimal = new BigDecimal(documentsNumberString);
                    documentsNumber = documentsNumber.add(documentsNumberBigDecimal);
                }
                tBasicDocuments.setDocumentsNumber(documentsNumber);

                // 单价(国际单位) documentsDecimal
                BigDecimal documentsDecimal = new BigDecimal("0");
                String documentsDecimalString = tBasicSubjectMessageListMap.get("documentsDecimal");
                if (StringUtils.isNotBlank(documentsDecimalString)) {
                    BigDecimal documentsDecimalBigDecimal = new BigDecimal(documentsDecimalString);
                    documentsDecimal = documentsDecimal.add(documentsDecimalBigDecimal);
                }
                tBasicDocuments.setDocumentsDecimal(documentsDecimal);

                // 借方金额
                BigDecimal amountDebit = new BigDecimal("0");
                String amountDebitString = tBasicSubjectMessageListMap.get("documentsAmount");
                if (StringUtils.isNotBlank(amountDebitString)) {
                    BigDecimal amountDebitBigDecimal = new BigDecimal(amountDebitString);
                    amountDebit = amountDebit.add(amountDebitBigDecimal);
                }
                tBasicDocuments.setAmountDebit(amountDebit);
                // 借方科目主键
                tBasicDocuments.setPkSubIdDebit(tBasicSubjectMessageListMap.get("pkSubId"));
                // 借方科目代码
                tBasicDocuments.setSubCodeDebit(tBasicSubjectMessageListMap.get("subCode"));
                // 借方科目名称
                tBasicDocuments.setSubNameDebit(tBasicSubjectMessageListMap.get("subName"));
                // 借方科目完整名称
                tBasicDocuments.setFullNameDebit(tBasicSubjectMessageListMap.get("fullName"));
                // '单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                tBasicDocuments.setDocumentsType("1");
                // 单据子类别
                // tBasicDocuments.setDocumentsSonType(documentsSonType);
                // 凭证号
                // tBasicDocuments.setVoucherNumber(voucherNumber);
                // 创建人
                tBasicDocuments.setCreatePsn(userId);
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simpleDateFormat.format(date);
                // 创建时间
                tBasicDocuments.setCreateDate(date);
                // 更新人
                // tBasicDocuments.setUpdatePsn(updatePsn);
                // 更新时间
                // tBasicDocuments.setUpdateDate(updateDate);
                // 更新时间戳
                // tBasicDocuments.setUpdateTimestamp(format);
                tBasicDocumentsList.add(tBasicDocuments);
            }
            int addTicketsCost = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentsList);
            result.put("code", 1);
            result.put("addTicketsCost", addTicketsCost);
            result.put("tBasicDocumentsList", tBasicDocumentsList);
            BigDecimal bigDecimaltaxAmount = new BigDecimal("0");
            bigDecimaltaxAmount = taxAmount.multiply(sizeBigDecimal);
            result.put("bigDecimaltaxAmount", bigDecimaltaxAmount);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addProcurementRawMaterialsList(HttpSession session, String tBasicSubjectMessage,
                                                              String tBasicSubjectMessageList) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicDocuments> tBasicDocumentsList = new ArrayList<TBasicDocuments>();
        try {
            Gson gson = new Gson();
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");

            /** tBasicSubjectMessage 解析 单个json对象 **/
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            // json 对象转换成 map
            // tBasicSubjectMessage =
            // {"pkSubId":"de4e17a883c649008d19f5bddaed83c7","subCode":"2202001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"北京网聘咨询有限公司深圳分公司","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":0,"yearAmountCredit":0,"endingBalanceDebit":0,"excelImportCode":"2202001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"2202","superiorCoding":"2202","fullName":"应付账款_北京网聘咨询有限公司深圳分公司","updateDate":1521770370000,"updateTimestamp":"1521770369767","category":"2","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"2","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null}
            Map<String, String> tBasicSubjectMessageMap = gson.fromJson(tBasicSubjectMessage, type);

            // 贷方科目主键
            String pkSubIdCtedit = tBasicSubjectMessageMap.get("pkSubId");
            // 贷方科目代码
            String subCodeCredit = tBasicSubjectMessageMap.get("subCode");
            // 贷方科目名称
            String subNameCredit = tBasicSubjectMessageMap.get("subName");
            // 贷方科目完整名称
            String fullNameCredit = tBasicSubjectMessageMap.get("fullName");

            // 税金
            BigDecimal taxAmount = new BigDecimal("0");
            String taxAmountString = tBasicSubjectMessageMap.get("taxAmount");
            if (StringUtils.isNotBlank(taxAmountString)) {
                BigDecimal taxAmountBigDecimal = new BigDecimal(taxAmountString);
                taxAmount = taxAmount.add(taxAmountBigDecimal);
            }

            /** tBasicSubjectMessageList 解析 json 数组对象 **/
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            // json 对象转换成 ArrayList
            // tBasicSubjectMessageList =
            // [{"pkSubId":"66fc83664ead4eab975570de0751a20f","subCode":"1405001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"电源","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":1389334.86,"yearAmountCredit":1460124.92,"endingBalanceDebit":0,"excelImportCode":"1405001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"1405","superiorCoding":"1405","fullName":"库存商品_电源","updateDate":1521770370000,"updateTimestamp":"1521770369715","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"1","amountDebit":null,"documentsType":1,"documentsUnit":"亩","documentsUnitId":"00df22d4e84f4652aae1146c964599f5","documentsDecimal":"1","documentsNumber":"1","documentsAmount":"1","taxAmount":null}]
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(tBasicSubjectMessageList, type2);
            int size = jsonObjects.size();
            BigDecimal sizeBigDecimal = new BigDecimal(size);
            taxAmount = taxAmount.divide(sizeBigDecimal, 8, BigDecimal.ROUND_HALF_UP);
            // 遍历 json 集合
            for (JsonObject jsonObject : jsonObjects) {
                // 解析 单个json对象
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                // json 对象转换成 map
                Map<String, String> tBasicSubjectMessageListMap = gson.fromJson(jsonObject, type3);

                TBasicDocuments tBasicDocuments = new TBasicDocuments();

                UUIDUtils uuidUtils = new UUIDUtils();
                // 单据主键
                tBasicDocuments.setPkDocumentsId(uuidUtils.getUUID());
                // 用户id
                tBasicDocuments.setUserId(userId);
                // 帐套id
                tBasicDocuments.setAccountId(accountId);
                // 会计期间
                tBasicDocuments.setAccountPeriod(busDate);
                // 摘要
                tBasicDocuments.setSummary("添加 采购库存商品 集合");
                // 项目名称
                tBasicDocuments.setProjectName(tBasicSubjectMessageListMap.get("fullName"));
                // 贷方金额
                BigDecimal amountCredit = new BigDecimal("0");
                String amountCreditString = tBasicSubjectMessageListMap.get("documentsAmount");
                if (StringUtils.isNotBlank(amountCreditString)) {
                    BigDecimal amountCreditBigDecimal = new BigDecimal(amountCreditString);
                    amountCredit = amountCredit.add(amountCreditBigDecimal);
                }
                tBasicDocuments.setAmountCredit(amountCredit);
                // 贷方科目主键
                tBasicDocuments.setPkSubIdCtedit(pkSubIdCtedit);
                // 贷方科目代码
                tBasicDocuments.setSubCodeCredit(subCodeCredit);
                // 贷方科目名称
                tBasicDocuments.setSubNameCredit(subNameCredit);
                // 贷方科目完整名称
                tBasicDocuments.setFullNameCredit(fullNameCredit);

                // 税金
                tBasicDocuments.setTaxAmount(taxAmount);

                // 计量单位 documentsUnit
                String documentsUnit = tBasicSubjectMessageListMap.get("documentsUnit");
                tBasicDocuments.setDocumentsUnit(documentsUnit);

                // 计量单位ID documentsUnitId
                String documentsUnitid = tBasicSubjectMessageListMap.get("documentsUnitId");
                tBasicDocuments.setDocumentsUnitid(documentsUnitid);

                // 数量 documentsNumber
                BigDecimal documentsNumber = new BigDecimal("0");
                String documentsNumberString = tBasicSubjectMessageListMap.get("documentsNumber");
                if (StringUtils.isNotBlank(documentsNumberString)) {
                    BigDecimal documentsNumberBigDecimal = new BigDecimal(documentsNumberString);
                    documentsNumber = documentsNumber.add(documentsNumberBigDecimal);
                }
                tBasicDocuments.setDocumentsNumber(documentsNumber);

                // 单价(国际单位) documentsDecimal
                BigDecimal documentsDecimal = new BigDecimal("0");
                String documentsDecimalString = tBasicSubjectMessageListMap.get("documentsDecimal");
                if (StringUtils.isNotBlank(documentsDecimalString)) {
                    BigDecimal documentsDecimalBigDecimal = new BigDecimal(documentsDecimalString);
                    documentsDecimal = documentsDecimal.add(documentsDecimalBigDecimal);
                }
                tBasicDocuments.setDocumentsDecimal(documentsDecimal);

                // 借方金额
                BigDecimal amountDebit = new BigDecimal("0");
                String amountDebitString = tBasicSubjectMessageListMap.get("documentsAmount");
                if (StringUtils.isNotBlank(amountDebitString)) {
                    BigDecimal amountDebitBigDecimal = new BigDecimal(amountDebitString);
                    amountDebit = amountDebit.add(amountDebitBigDecimal);
                }
                tBasicDocuments.setAmountDebit(amountDebit);
                // 借方科目主键
                tBasicDocuments.setPkSubIdDebit(tBasicSubjectMessageListMap.get("pkSubId"));
                // 借方科目代码
                tBasicDocuments.setSubCodeDebit(tBasicSubjectMessageListMap.get("subCode"));
                // 借方科目名称
                tBasicDocuments.setSubNameDebit(tBasicSubjectMessageListMap.get("subName"));
                // 借方科目完整名称
                tBasicDocuments.setFullNameDebit(tBasicSubjectMessageListMap.get("fullName"));
                // '单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                tBasicDocuments.setDocumentsType("1");
                // 单据子类别
                // tBasicDocuments.setDocumentsSonType(documentsSonType);
                // 凭证号
                // tBasicDocuments.setVoucherNumber(voucherNumber);
                // 创建人
                tBasicDocuments.setCreatePsn(userId);
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simpleDateFormat.format(date);
                // 创建时间
                tBasicDocuments.setCreateDate(date);
                // 更新人
                // tBasicDocuments.setUpdatePsn(updatePsn);
                // 更新时间
                // tBasicDocuments.setUpdateDate(updateDate);
                // 更新时间戳
                // tBasicDocuments.setUpdateTimestamp(format);
                tBasicDocumentsList.add(tBasicDocuments);
            }
            int addTicketsCost = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentsList);
            result.put("code", 1);
            result.put("addTicketsCost", addTicketsCost);
            result.put("tBasicDocumentsList", tBasicDocumentsList);
            BigDecimal bigDecimaltaxAmount = new BigDecimal("0");
            bigDecimaltaxAmount = taxAmount.multiply(sizeBigDecimal);
            result.put("bigDecimaltaxAmount", bigDecimaltaxAmount);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param session
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException
     * @Title: addsalesCreditList
     * @Description: 添加 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款） 集合
     * @see com.wqb.service.documents.TBasicDocumentsService#addsalesCreditList(javax.servlet.http.HttpSession,
     * java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addsalesCreditList(HttpSession session, String tBasicSubjectMessageList)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicDocuments> tBasicDocumentsList = new ArrayList<TBasicDocuments>();
        try {
            Gson gson = new Gson();
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");
            //
            // /** tBasicSubjectMessage 解析 单个json对象 **/
            // java.lang.reflect.Type type = new TypeToken<HashMap<String,
            // String>>(){}.getType();
            // // json 对象转换成 map
            // // tBasicSubjectMessage =
            // {"pkSubId":"de4e17a883c649008d19f5bddaed83c7","subCode":"2202001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"北京网聘咨询有限公司深圳分公司","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":0,"yearAmountCredit":0,"endingBalanceDebit":0,"excelImportCode":"2202001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"2202","superiorCoding":"2202","fullName":"应付账款_北京网聘咨询有限公司深圳分公司","updateDate":1521770370000,"updateTimestamp":"1521770369767","category":"2","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"2","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null}
            // Map<String, String> tBasicSubjectMessageMap =
            // gson.fromJson(tBasicSubjectMessage, type);

            // 主营业务收入 贷方科目主键
            String pkSubIdCtedit6001 = "";
            // 主营业务收入 贷方科目代码
            String subCodeCredit6001 = "";
            // 主营业务收入 贷方科目名称
            String subNameCredit6001 = "";
            // 主营业务收入 贷方科目完整名称
            String fullNameCredit6001 = "";

            // 未交增值税 贷方科目主键
            String pkSubIdCteditVATUnpaid = "";
            // 未交增值税 贷方科目代码
            String subCodeCreditVATUnpaid = "";
            // 未交增值税 贷方科目名称
            String subNameCreditVATUnpaid = "";
            // 未交增值税 贷方科目完整名称
            String fullNameCreditVATUnpaid = "";

            // 减免税额 贷方科目主键
            String pkSubIdCteditTaxReliefs = "";
            // 减免税额 贷方科目代码
            String subCodeCreditTaxReliefs = "";
            // 减免税额 贷方科目名称
            String subNameCreditTaxReliefs = "";
            // 减免税额 贷方科目完整名称
            String fullNameCreditTaxReliefs = "";

            // 税金
            // BigDecimal taxAmount = new BigDecimal("0");
            // String taxAmountString =
            // tBasicSubjectMessageMap.get("taxAmount");
            // if(StringUtils.isNotBlank(taxAmountString))
            // {
            // BigDecimal taxAmountBigDecimal = new BigDecimal(taxAmountString);
            // taxAmount = taxAmount.add(taxAmountBigDecimal);
            // }

            /** tBasicSubjectMessageList 解析 json 数组对象 **/
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            // json 对象转换成 ArrayList
            // tBasicSubjectMessageList =
            // [{"pkSubId":"66fc83664ead4eab975570de0751a20f","subCode":"1405001","userId":"ce644c3f88684b8b8d9c85667bd0e1f6","accountId":"cf184a0eed3842b5810de150ddffec90","accountPeriod":"2018-02","excelImportPeriod":"11","subName":"电源","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":1389334.86,"yearAmountCredit":1460124.92,"endingBalanceDebit":0,"excelImportCode":"1405001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"1405","superiorCoding":"1405","fullName":"库存商品_电源","updateDate":1521770370000,"updateTimestamp":"1521770369715","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"启用","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"1","amountDebit":null,"documentsType":1,"documentsUnit":"亩","documentsUnitId":"00df22d4e84f4652aae1146c964599f5","documentsDecimal":"1","documentsNumber":"1","documentsAmount":"1","taxAmount":null}]
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(tBasicSubjectMessageList, type2);
            // int size = jsonObjects.size();
            // BigDecimal sizeBigDecimal = new BigDecimal(size);
            // taxAmount = taxAmount.divide(sizeBigDecimal, 8,
            // BigDecimal.ROUND_HALF_UP);

            for (JsonObject jsonObject : jsonObjects) {
                // 解析 单个json对象
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                // json 对象转换成 map
                Map<String, String> tBasicSubjectMessageListMap = gson.fromJson(jsonObject, type3);
                if (tBasicSubjectMessageListMap.get("subCode").equals("6001")) {
                    // 主营业务收入 贷方科目主键
                    pkSubIdCtedit6001 = tBasicSubjectMessageListMap.get("pkSubId");
                    // 主营业务收入 贷方科目代码
                    subCodeCredit6001 = tBasicSubjectMessageListMap.get("subCode");
                    // 主营业务收入 贷方科目名称
                    subNameCredit6001 = tBasicSubjectMessageListMap.get("subName");
                    // 主营业务收入 贷方科目完整名称
                    fullNameCredit6001 = tBasicSubjectMessageListMap.get("fullName");
                }

                if (tBasicSubjectMessageListMap.get("subCode").contains("2221")) {
                    if (StringUtils.isNotBlank(tBasicSubjectMessageListMap.get("subName"))
                            && tBasicSubjectMessageListMap.get("subName").equals("未交增值税")) {
                        // 未交增值税 贷方科目主键
                        pkSubIdCteditVATUnpaid = tBasicSubjectMessageListMap.get("pkSubId");
                        // 未交增值税 贷方科目代码
                        subCodeCreditVATUnpaid = tBasicSubjectMessageListMap.get("subCode");
                        // 未交增值税 贷方科目名称
                        subNameCreditVATUnpaid = tBasicSubjectMessageListMap.get("subName");
                        // 未交增值税 贷方科目完整名称
                        fullNameCreditVATUnpaid = tBasicSubjectMessageListMap.get("fullName");
                    }
                    if (StringUtils.isNotBlank(tBasicSubjectMessageListMap.get("subName"))
                            && tBasicSubjectMessageListMap.get("subName").contains("免税")) {
                        // 减免税额 贷方科目主键
                        pkSubIdCteditTaxReliefs = tBasicSubjectMessageListMap.get("pkSubId");
                        // 减免税额 贷方科目代码
                        subCodeCreditTaxReliefs = tBasicSubjectMessageListMap.get("subCode");
                        // 减免税额 贷方科目名称
                        subNameCreditTaxReliefs = tBasicSubjectMessageListMap.get("subName");
                        // 减免税额 贷方科目完整名称
                        fullNameCreditTaxReliefs = tBasicSubjectMessageListMap.get("fullName");
                    }
                }
            }

            // 遍历 json 集合
            for (JsonObject jsonObject : jsonObjects) {
                // 解析 单个json对象
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                // json 对象转换成 map
                Map<String, String> tBasicSubjectMessageListMap = gson.fromJson(jsonObject, type3);
                String subCode = tBasicSubjectMessageListMap.get("subCode");
                if (!subCode.contains("6001") && !subCode.contains("2221")) {
                    TBasicDocuments tBasicDocuments = new TBasicDocuments();

                    UUIDUtils uuidUtils = new UUIDUtils();
                    // 单据主键
                    tBasicDocuments.setPkDocumentsId(uuidUtils.getUUID());
                    // 用户id
                    tBasicDocuments.setUserId(userId);
                    // 帐套id
                    tBasicDocuments.setAccountId(accountId);
                    // 会计期间
                    tBasicDocuments.setAccountPeriod(busDate);
                    // 摘要
                    tBasicDocuments.setSummary("添加 销售 集合");
                    // 项目名称
                    tBasicDocuments.setProjectName(tBasicSubjectMessageListMap.get("fullName"));

                    // 贷方科目主键
                    tBasicDocuments.setPkSubIdCtedit(pkSubIdCtedit6001);
                    // 贷方科目代码
                    tBasicDocuments.setSubCodeCredit(subCodeCredit6001);
                    // 贷方科目名称
                    tBasicDocuments.setSubNameCredit(subNameCredit6001);
                    // 贷方科目完整名称
                    tBasicDocuments.setFullNameCredit(fullNameCredit6001);

                    // 借方金额
                    BigDecimal amountDebit = new BigDecimal("0");
                    // String amountDebitString =
                    // tBasicSubjectMessageListMap.get("amountDebit");
                    String amountDebitString = tBasicSubjectMessageListMap.get("documentsAmount");
                    if (StringUtils.isNotBlank(amountDebitString)) {
                        BigDecimal amountDebitBigDecimal = new BigDecimal(amountDebitString);
                        amountDebit = amountDebit.add(amountDebitBigDecimal);
                    }
                    tBasicDocuments.setAmountDebit(amountDebit);

                    // 税金
                    BigDecimal taxAmount = new BigDecimal("0");
                    String taxAmountString = tBasicSubjectMessageListMap.get("taxAmount");
                    if (StringUtils.isNotBlank(taxAmountString)) {
                        BigDecimal taxAmountBigDecimal = new BigDecimal(taxAmountString);
                        taxAmount = taxAmount.add(taxAmountBigDecimal);
                        // 1. 未交增值税 2.免税
                        if (tBasicSubjectMessageListMap.get("taxAmountType").equals("1")) {

                            // 未交增值税 贷方科目主键
                            tBasicDocuments.setPkSubIdTax(pkSubIdCteditVATUnpaid);
                            // 未交增值税 贷方科目代码
                            tBasicDocuments.setSubCodeTax(subCodeCreditVATUnpaid);
                            // 未交增值税 贷方科目名称
                            tBasicDocuments.setSubNameTax(subNameCreditVATUnpaid);
                            // 未交增值税 贷方科目完整名称
                            tBasicDocuments.setFullNameTax(fullNameCreditVATUnpaid);

                        }
                        // 1. 未交增值税 2.免税
                        if (tBasicSubjectMessageListMap.get("taxAmountType").equals("2")) {
                            // 减免税额 贷方科目主键
                            tBasicDocuments.setPkSubIdTax(pkSubIdCteditTaxReliefs);
                            // 减免税额 贷方科目代码
                            tBasicDocuments.setSubCodeTax(subCodeCreditTaxReliefs);
                            // 减免税额 贷方科目名称
                            tBasicDocuments.setSubNameTax(subNameCreditTaxReliefs);
                            // 减免税额 贷方科目完整名称
                            tBasicDocuments.setFullNameTax(fullNameCreditTaxReliefs);
                        }
                        tBasicDocuments.setTaxAmount(taxAmount);
                    }

                    // 贷方金额
                    // BigDecimal amountCredit = new BigDecimal("0");
                    // String amountCreditString =
                    // tBasicSubjectMessageListMap.get("amountDebit");
                    // if (StringUtils.isNotBlank(amountCreditString))
                    // {
                    // BigDecimal amountCreditBigDecimal = new
                    // BigDecimal(amountCreditString);
                    // amountCredit = amountCredit.add(amountCreditBigDecimal);
                    // }
                    // 贷方金额 借方 减去税金 等于 贷方金额
                    tBasicDocuments.setAmountCredit(amountDebit.subtract(taxAmount));

                    // 计量单位 documentsUnit
                    String documentsUnit = tBasicSubjectMessageListMap.get("documentsUnit");
                    tBasicDocuments.setDocumentsUnit(documentsUnit);

                    // 计量单位ID documentsUnitId
                    String documentsUnitid = tBasicSubjectMessageListMap.get("documentsUnitId");
                    tBasicDocuments.setDocumentsUnitid(documentsUnitid);

                    // 数量 documentsNumber
                    BigDecimal documentsNumber = new BigDecimal("0");
                    String documentsNumberString = tBasicSubjectMessageListMap.get("documentsNumber");
                    if (StringUtils.isNotBlank(documentsNumberString)) {
                        BigDecimal documentsNumberBigDecimal = new BigDecimal(documentsNumberString);
                        documentsNumber = documentsNumber.add(documentsNumberBigDecimal);
                    }
                    tBasicDocuments.setDocumentsNumber(documentsNumber);

                    // 单价(国际单位) documentsDecimal
                    BigDecimal documentsDecimal = new BigDecimal("0");
                    String documentsDecimalString = tBasicSubjectMessageListMap.get("documentsDecimal");
                    if (StringUtils.isNotBlank(documentsDecimalString)) {
                        BigDecimal documentsDecimalBigDecimal = new BigDecimal(documentsDecimalString);
                        documentsDecimal = documentsDecimal.add(documentsDecimalBigDecimal);
                    }
                    tBasicDocuments.setDocumentsDecimal(documentsDecimal);

                    // 借方科目主键
                    tBasicDocuments.setPkSubIdDebit(tBasicSubjectMessageListMap.get("pkSubId"));
                    // 借方科目代码
                    tBasicDocuments.setSubCodeDebit(tBasicSubjectMessageListMap.get("subCode"));
                    // 借方科目名称
                    tBasicDocuments.setSubNameDebit(tBasicSubjectMessageListMap.get("subName"));
                    // 借方科目完整名称
                    tBasicDocuments.setFullNameDebit(tBasicSubjectMessageListMap.get("fullName"));
                    // '单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
                    tBasicDocuments.setDocumentsType("2");
                    // 单据子类别
                    // tBasicDocuments.setDocumentsSonType(documentsSonType);
                    // 凭证号
                    // tBasicDocuments.setVoucherNumber(voucherNumber);
                    // 创建人
                    tBasicDocuments.setCreatePsn(userId);
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = simpleDateFormat.format(date);
                    // 创建时间
                    tBasicDocuments.setCreateDate(date);
                    // 更新人
                    // tBasicDocuments.setUpdatePsn(updatePsn);
                    // 更新时间
                    // tBasicDocuments.setUpdateDate(updateDate);
                    // 更新时间戳
                    // tBasicDocuments.setUpdateTimestamp(format);
                    tBasicDocumentsList.add(tBasicDocuments);
                }
            }
            int addTicketsCost = tBasicDocumentsMapper.addTicketsCost(tBasicDocumentsList);
            result.put("code", 1);
            result.put("addTicketsCost", addTicketsCost);
            result.put("tBasicDocumentsList", tBasicDocumentsList);
            BigDecimal bigDecimaltaxAmount = new BigDecimal("0");
            // bigDecimaltaxAmount = taxAmount.multiply(sizeBigDecimal);
            result.put("bigDecimaltaxAmount", bigDecimaltaxAmount);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteDocumentsList(HttpSession session, String tBasicDocumentss)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicDocuments> tBasicDocumentsList = new ArrayList<TBasicDocuments>();
        try {
            Gson gson = new Gson();
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");

            /** tBasicSubjectMessageList 解析 json 数组对象 **/
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            // json 对象转换成 ArrayList
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(tBasicDocumentss, type2);

            // 遍历 json 集合
            for (JsonObject jsonObject : jsonObjects) {
                // 解析 单个json对象
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                // json 对象转换成 map
                Map<String, String> tBasicSubjectMessageListMap = gson.fromJson(jsonObject, type3);
                TBasicDocuments tBasicDocuments = new TBasicDocuments();
                // 单据主键
                tBasicDocuments.setPkDocumentsId(tBasicSubjectMessageListMap.get("pkDocumentsId"));
                // 用户id
                tBasicDocuments.setUserId(userId);
                // 帐套id
                tBasicDocuments.setAccountId(accountId);
                // 会计期间
                tBasicDocuments.setAccountPeriod(busDate);
                tBasicDocumentsList.add(tBasicDocuments);
            }
            int no = tBasicDocumentsMapper.deleteDocumentsList(tBasicDocumentsList);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    @Override
    public List<TBasicSubjectMessage> getMjSub(HttpSession session, String subCode) throws BusinessException {
        List<TBasicSubjectMessage> resultList = new ArrayList<TBasicSubjectMessage>();
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        // User user = (User) sessionMap.get("user"); // 获取user信息
        // String userId = user.getUserID();// 用户id
        Account account = (Account) sessionMap.get("account"); // 获取帐套信息
        String accountId = account.getAccountID();// 账套id
        String busDate = (String) sessionMap.get("busDate");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountID", accountId);
        param.put("lastDate", busDate);
        param.put("subjectID", subCode);
        List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.selectLastArch2(param);
        if (subList != null && subList.size() > 0) {
            for (int i = 0; i < subList.size(); i++) {
                TBasicSubjectMessage sub = subList.get(i);
                String sub_code = sub.getSubCode();
                if (null != sub_code && !"".equals(sub_code)) {
                    param.put("subjectID", sub_code);
                    List<TBasicSubjectMessage> sList = tBasicSubjectMessageMapper.selectLastArch2(param);
                    if (sList != null && sList.get(0) != null && sList.size() == 1) {
                        resultList.add(sList.get(0));
                    } else {
                        continue;
                    }
                }
            }
        }
        return resultList;
    }
}
