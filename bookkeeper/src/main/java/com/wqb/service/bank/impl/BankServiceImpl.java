package com.wqb.service.bank.impl;

import com.wqb.common.*;
import com.wqb.dao.arch.ArchDao;
import com.wqb.dao.bank.*;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.model.bank.*;
import com.wqb.service.bank.BankService;
import com.wqb.service.bank.TCmBkbillBocService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

@Service("bankService")
public class BankServiceImpl implements BankService {

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    TccbBankDao tccbBankDao;
    @Autowired
    SzrcbBankBillDao szrcbBankBillDao;
    @Autowired
    BcmDao bcmDao;
    @Autowired
    Bcm1Dao bcm1Dao;
    @Autowired
    TCmBkbillBocService tCmBkbillBocService;
    @Autowired
    IcbcBankDao icbcBankDao;
    @Autowired
    JsBankDao jsBankDao;
    @Autowired
    NyBankDao nyBankDao;
    @Autowired
    PaBankDao paBankDao;
    @Autowired
    ZsBankDao zsBankDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    TCmBkbillBocMapper tCmBkbillBocMapper;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadDao voucherHeadDao;

    @Autowired
    VoucherBodyDao voucherBodyDao;

    @Autowired
    ArchDao archDao;

    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    Bank2SubjectDao bank2SubjectDao;

    // private static final ThreadLocal<List<Voucher>> bankLocal = new
    // ThreadLocal<List<Voucher>>();

    private static final ThreadLocal<List<String>> vouchIDS = new ThreadLocal<List<String>>();

    // List<String> bankVo = new ArrayList<>();

    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> uploadBankBill(MultipartFile file, HttpServletRequest request) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (file != null && !file.isEmpty()) {
                HttpSession session = request.getSession();
                Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
                User user = (User) sessionMap.get("user");
                Account account = (Account) sessionMap.get("account");
                String accountID = account.getAccountID();
                String busDate = sessionMap.get("busDate").toString();
                // String filePath = PathUtil.getClasspath() +
                // Constrants.FILEPATHFILE;
                String filePath = filePaths + "/" + user.getUserID() + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath, "dzd-" + System.currentTimeMillis());
                // list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                File target = new File(filePath, fileName);
                FileInputStream is = new FileInputStream(target);
                ExcelReader excelReader = new ExcelReader();
                String[] title = excelReader.readExcelTitle(is);

                String s = title[0];

                // 获取银行账户和科目的映射
                /*
                 * Map<String, Object> pa = new HashMap<String, Object>();
                 * pa.put("account", account.getAccountID()); List<Bank2Subject>
                 * bslist = bank2SubjectDao.queryBank2Subject(pa); Map<String,
                 * Object> b2sMap = new HashMap<String, Object>(); for (int i =
                 * 0; i < bslist.size(); i++) { Bank2Subject b2s =
                 * bslist.get(i); String bankAccount = b2s.getBankAccount();
                 * String subCode = b2s.getSubCode(); String subName =
                 * b2s.getSubName(); String subFullName = b2s.getSubFullName();
                 * String subID = b2s.getSubID(); b2sMap.put(bankAccount,
                 * subCode + "#" + subName + "#" + subFullName + "#" + subID); }
                 */
                vatService.subinit(accountID, busDate, user.getUserID(), user.getUserName());
                if (s.contains("活期账户交易明细查询结果")) {
                    // 中信银行
                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    String bankAccountStr = excelReader.readExcelContent(is, 0, 0);
                    String bankAccount = StringUtil.getNumberFromString(bankAccountStr);
                    if (null != bankAccount && !"".equals(bankAccount)) {
                        // 先查询能否创建科目
                        Map<String, Object> pa1 = new HashMap<String, Object>();
                        pa1.put("accountID", accountID);
                        pa1.put("busDate", busDate);
                        pa1.put("bankAccount", bankAccount);
                        List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                        String subID = null;
                        String subject = null;
                        if (li1 == null || li1.isEmpty()) {
                            // 创建科目
                            subject = vatService.getNumber("1002", "7", "1002000");
                            SubjectMessage sm = vatService.createSub(subject, "1002", "中信银行" + bankAccount,
                                    "银行存款_" + "中信银行" + bankAccount);
                            subID = sm.getPk_sub_id();
                        } else {
                            subID = li1.get(0).getPkSubId();
                            subject = li1.get(0).getSubCode();
                        }
                        // 自动创建银行账户和科目的映射
                        Bank2Subject b2s = new Bank2Subject();
                        // 主键
                        b2s.setId(UUIDUtils.getUUID());
                        // 账套ID
                        b2s.setAccountID(accountID);
                        // 银行账户
                        b2s.setBankAccount(bankAccount);
                        // 银行科目主键
                        b2s.setSubID(subID);
                        // 银行名称
                        b2s.setBankName("中信银行");
                        // 银行类型
                        b2s.setBankType("中信银行");
                        // 币种
                        b2s.setCurrency("人民币");
                        // 科目名称
                        b2s.setSubName("中信银行" + bankAccount);
                        // 科目全名
                        b2s.setSubFullName("银行存款_" + "中信银行" + bankAccount);
                        // 科目编码
                        b2s.setSubCode(subject);
                        // 创建者ID
                        b2s.setCreateID(user.getUserID());
                        // 创建者手机号
                        b2s.setCreateTel(user.getLoginUser());
                        // 创建人名字
                        b2s.setCreateName(user.getUserName());
                        // 创建时间
                        b2s.setCreateTime(new Date());
                        Map<String, Object> pa = new HashMap<String, Object>();
                        pa.put("bankAccount", bankAccount);
                        List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                        boolean canAdd = true;
                        if (li == null || li.isEmpty()) {
                            bank2SubjectDao.addBank2Subject(b2s);
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                if (li.get(i).getAccountID().equals(accountID)) {
                                    canAdd = false;
                                }
                            }
                            if (canAdd) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            }
                        }

                    } else {
                        throw new BusinessException("请检查您导入的中信银行对账单,请先完善银行账户信息");
                    }
                    if (null != list && list.size() > 0) {
                        for (int j = 1; j < list.size(); j++) {
                            Map<String, Object> map = list.get(j);
                            // 账套ID
                            // String accountID = account.getAccountID();
                            // 会计期间
                            String period = busDate;
                            // 总交易流水号
                            String transactionID = map.get("map21") == null ? null : map.get("map21").toString();
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", accountID);
                            param.put("period", period);
                            param.put("transactionID", transactionID);
                            List<TccbBank> dataList = tccbBankDao.querySame(param);
                            if (dataList != null && dataList.size() > 0) {
                                result.put("success", "fail");
                                result.put("message", "数据不允许重复导入");
                                return result;
                            }
                        }

                        for (int i = 1; i < list.size(); i++) {
                            Map<String, Object> map = list.get(i);
                            // 中信银行对账单主键
                            String zxBillID = UUIDUtils.getUUID();
                            // 账套ID
                            // String accountID = account.getAccountID();
                            // 会计期间
                            String period = busDate;
                            // 导入时间
                            long importDate = System.currentTimeMillis();
                            // 交易时间
                            Date transactionDate = map.get("map0") == null ? null
                                    : DateUtil.fomatDate(map.get("map0").toString());
                            // 起息时间
                            Date carryInterest = map.get("map1") == null ? null
                                    : DateUtil.fomatDate(map.get("map1").toString());
                            // 主机交易号
                            String masterTransactionNum = map.get("map2") == null ? null : map.get("map2").toString();
                            // 柜员交易号
                            String gyTransaction = map.get("map3") == null ? null : map.get("map3").toString();
                            // 被冲账标志
                            String bczFlag = map.get("map4") == null ? null : map.get("map4").toString();
                            // 非金融标识
                            String fjrFlag = map.get("map5") == null ? null : map.get("map5").toString();
                            // 借方金额
                            double debitAmount = map.get("map6") == null ? 0.0
                                    : Double.parseDouble(map.get("map6").toString().replaceAll(",", ""));
                            // 贷方金额
                            double creditAmount = map.get("map7") == null ? 0.0
                                    : Double.parseDouble(map.get("map7").toString().replaceAll(",", ""));
                            // 账户余额
                            double accountBalance = map.get("map8") == null ? 0.0
                                    : Double.parseDouble(map.get("map8").toString().replaceAll(",", ""));
                            // 现转标识
                            String xzFlag = map.get("map9") == null ? null : map.get("map9").toString();
                            // 摘要代码1
                            String descCode1 = map.get("map10") == null ? null : map.get("map10").toString();
                            // 摘要代码2
                            String descCode2 = map.get("map11") == null ? null : map.get("map11").toString();
                            // 摘要
                            String des = map.get("map12") == null ? null : map.get("map12").toString();
                            // 制单人ID
                            String createPresonID = map.get("map13") == null ? null : map.get("map13").toString();
                            // 制单操作员姓名
                            String ceeatePersonName = map.get("map14") == null ? null : map.get("map14").toString();
                            // 复核ID
                            String checkID = map.get("map15") == null ? null : map.get("map15").toString();
                            // 复核员姓名
                            String checkPersonName = map.get("map16") == null ? null : map.get("map16").toString();
                            // 账号
                            String accountNO = map.get("map17") == null ? null : map.get("map17").toString();
                            // 外行账户名称
                            String whAccountName = map.get("map18") == null ? null : map.get("map18").toString();
                            // 外行开户行名称
                            String whkhhName = map.get("map19") == null ? null : map.get("map19").toString();
                            // 交易时间
                            String transactionTime = map.get("map20") == null ? null : map.get("map20").toString();
                            // 总交易流水号
                            String transactionID = map.get("map21") == null ? null : map.get("map21").toString();
                            // 备注
                            String bz = map.get("map22") == null ? null : map.get("map22").toString();
                            // 退票标识
                            String tpFlag = map.get("map23") == null ? null : map.get("map23").toString();
                            // 退票日期
                            Date tpDate = map.get("map24") == null || "".equals(map.get("map24").toString().trim())
                                    ? null : DateUtil.fomatDate(map.get("map24").toString());
                            // 退票场次
                            String tpcc = map.get("map25") == null ? null : map.get("map25").toString();
                            String flag = "0";
                            if (debitAmount != 0.0) {
                                flag = "1";
                            }
                            if (creditAmount != 0.0) {
                                flag = "2";
                            }

                            // 构造中信银行实体
                            String subjectInfo = getSubject(des, bz, whAccountName, flag, accountID, busDate, null);
                            TccbBank tccbBank = new TccbBank();
                            tccbBank.setZxBillID(zxBillID);
                            tccbBank.setAccountID(accountID);
                            tccbBank.setPeriod(period);
                            tccbBank.setBusDate(importDate + i);
                            tccbBank.setTransactionDate(transactionDate);
                            tccbBank.setCarryInterest(carryInterest);
                            tccbBank.setMasterTransactionNum(masterTransactionNum);
                            tccbBank.setGyTransaction(gyTransaction);
                            tccbBank.setBczFlag(bczFlag);
                            tccbBank.setFjrFlag(fjrFlag);
                            tccbBank.setDebitAmount(debitAmount);
                            tccbBank.setCreditAmount(creditAmount);
                            tccbBank.setAccountBalance(accountBalance);
                            tccbBank.setXzFlag(xzFlag);
                            tccbBank.setDescCode1(descCode1);
                            tccbBank.setDescCode2(descCode2);
                            tccbBank.setDes(des);
                            tccbBank.setCreatePresonID(createPresonID);
                            tccbBank.setCeeatePersonName(ceeatePersonName);
                            tccbBank.setCheckID(checkID);
                            tccbBank.setCheckPersonName(checkPersonName);
                            tccbBank.setAccountNO(accountNO);
                            tccbBank.setWhAccountName(whAccountName);
                            tccbBank.setWhkhhName(whkhhName);
                            tccbBank.setTransactionTime(transactionTime);
                            tccbBank.setTransactionID(transactionID);
                            tccbBank.setBz(bz);
                            tccbBank.setTpFlag(tpFlag);
                            tccbBank.setTpDate(tpDate);
                            tccbBank.setTpcc(tpcc);
                            if (subjectInfo != null) {
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = subjectInfos[0];
                                String subName = subjectInfos[subjectInfos.length - 1];
                                // 在一级科目下查询指定科目名称的科目
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", period);
                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    tccbBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    tccbBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    tccbBank.setSysSubjectName(subList.get(0).getSubName());
                                    tccbBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            System.out.println(bankAccount.length());
                            tccbBank.setBankAccount("" + bankAccount.trim().replace(" ", ""));
                            tccbBankDao.insertTccb(tccbBank);
                        }
                    }
                    // System.out.println(list);
                    // tccbBankDao.insertTccb(tccbBank);

                } else if (s.equals("深圳农村商业银行单位活期交易明细")) {
                    // 农商行
                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    String bankAccount = excelReader.readExcelContent(is, 1, 1);
                    System.out.println(bankAccount);
                    if (null != bankAccount && !"".equals(bankAccount)) {
                        // 先查询能否创建科目
                        Map<String, Object> pa1 = new HashMap<String, Object>();
                        pa1.put("accountID", accountID);
                        pa1.put("busDate", busDate);
                        pa1.put("bankAccount", bankAccount);
                        List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                        String subID = null;
                        String subject = null;
                        if (li1 == null || li1.isEmpty()) {
                            // 创建科目
                            subject = vatService.getNumber("1002", "7", "1002000");
                            SubjectMessage sm = vatService.createSub(subject, "1002", "深圳农村商业银行" + bankAccount,
                                    "银行存款_" + "深圳农村商业银行" + bankAccount);
                            subID = sm.getPk_sub_id();
                        } else {
                            subID = li1.get(0).getPkSubId();
                            subject = li1.get(0).getSubCode();
                        }
                        // 自动创建银行账户和科目的映射
                        Bank2Subject b2s = new Bank2Subject();
                        // 主键
                        b2s.setId(UUIDUtils.getUUID());
                        // 账套ID
                        b2s.setAccountID(accountID);
                        // 银行账户
                        b2s.setBankAccount(bankAccount);
                        // 银行科目主键
                        b2s.setSubID(subID);
                        // 银行名称
                        b2s.setBankName("深圳农村商业银行");
                        // 银行类型
                        b2s.setBankType("深圳农村商业银行");
                        // 币种
                        b2s.setCurrency("人民币");
                        // 科目名称
                        b2s.setSubName("深圳农村商业银行" + bankAccount);
                        // 科目全名
                        b2s.setSubFullName("银行存款_" + "深圳农村商业银行" + bankAccount);
                        // 科目编码
                        b2s.setSubCode(subject);
                        // 创建者ID
                        b2s.setCreateID(user.getUserID());
                        // 创建者手机号
                        b2s.setCreateTel(user.getLoginUser());
                        // 创建人名字
                        b2s.setCreateName(user.getUserName());
                        // 创建时间
                        b2s.setCreateTime(new Date());

                        Map<String, Object> pa = new HashMap<String, Object>();
                        pa.put("bankAccount", bankAccount);
                        List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                        boolean canAdd = true;
                        if (li == null || li.isEmpty()) {
                            bank2SubjectDao.addBank2Subject(b2s);
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                if (li.get(i).getAccountID().equals(accountID)) {
                                    canAdd = false;
                                }
                            }
                            if (canAdd) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            }
                        }
                    } else {
                        throw new BusinessException("请检查您导入的深圳农村商业银行对账单,请先完善银行账户信息");
                    }
                    for (int j = 4; j < list.size(); j++) {
                        Map<String, Object> map = list.get(j);
                        // 序号
                        String rowNumber = map.get("map0").toString();
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("period", busDate);
                        param.put("rowNumber", rowNumber);
                        List<SzrcbBank> bankList = szrcbBankBillDao.querySame(param);
                        if (null != bankList && bankList.size() > 0) {
                            result.put("success", "fail");
                            result.put("message", "数据不允许重复导入");
                            return result;
                        }
                    }

                    for (int i = 4; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        // 深农商行对账单主键
                        String szrcbBillID = UUIDUtils.getUUID();
                        // 账套ID
                        // String accountID = account.getAccountID();
                        // 会计期间
                        String period = busDate;
                        // 导入时间
                        long importDate = System.currentTimeMillis();
                        // 序号
                        String rowNumber = map.get("map0").toString();
                        if ("收入合计：".equals(rowNumber)) {
                            break;
                        }
                        // 交易日期
                        Date transactionDate = "".equals(map.get("map1")) ? null
                                : DateUtil.fomatDate(map.get("map1").toString());
                        // 摘要
                        String des = map.get("map2").toString();
                        // 收入金额
                        double debitAmount = "".equals(map.get("map3")) ? 0.0
                                : Double.parseDouble(map.get("map3").toString().replaceAll(",", ""));
                        // 支出金额
                        double creditAmount = "".equals(map.get("map4")) ? 0.0
                                : Double.parseDouble(map.get("map4").toString().replaceAll(",", ""));
                        // 账户余额
                        double accountBalance = "".equals(map.get("map5")) ? 0.0
                                : Double.parseDouble(map.get("map5").toString().replaceAll(",", ""));
                        // 对方户名
                        String dfAccountName = map.get("map6").toString();
                        // 对方账号
                        String dfAccountNumber = map.get("map7").toString();
                        // 凭证类型
                        String voucherType = map.get("map8").toString();
                        // 凭证号码
                        String voucherNo = map.get("map9").toString();
                        // 交易机构
                        String transjg = map.get("map10").toString();
                        // 备注
                        String bz = map.get("map11").toString();

                        String flag = "0";
                        if (debitAmount != 0.0) {

                            flag = "2";
                        }
                        if (creditAmount != 0.0) {
                            flag = "1";
                        }
                        String subjectInfo = getSubject(des, bz, dfAccountName, flag, account.getAccountID(), busDate,
                                "深圳农村商业银行");

                        // 构造深农商行实体类
                        SzrcbBank szrcb = new SzrcbBank();
                        szrcb.setSzrcbBillID(szrcbBillID);
                        szrcb.setAccountID(accountID);
                        szrcb.setPeriod(period);
                        szrcb.setBusDate(importDate + i);
                        szrcb.setRowNumber(rowNumber);
                        szrcb.setDes(des);
                        szrcb.setTransactionDate(transactionDate);
                        szrcb.setDebitAmount(debitAmount);
                        szrcb.setCreditAmount(creditAmount);
                        szrcb.setAccountBalance(accountBalance);
                        szrcb.setDfAccountName(dfAccountName);
                        szrcb.setDfAccountNumber(dfAccountNumber);
                        szrcb.setVoucherType(voucherType);
                        szrcb.setVoucherNo(voucherNo);
                        szrcb.setTransjg(transjg);
                        szrcb.setBz(bz);
                        szrcb.setBankAccount(bankAccount);

                        if (subjectInfo != null) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("period", busDate);
                            String[] subjectInfos = subjectInfo.split("_");
                            String subCode = null;
                            String subName = null;
                            if (subjectInfos.length == 2) {
                                param.put("subCode", subjectInfos[0]);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null) {
                                    List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                    if (li != null && li.size() == 1) {
                                        subCode = li.get(0).getSubCode();
                                        subName = li.get(0).getSubName();
                                    }
                                }
                            } else {
                                subCode = subjectInfos[0];
                                subName = subjectInfos[subjectInfos.length - 1];
                            }

                            param.put("subCode", subCode);
                            param.put("subName", subName);
                            List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                            if (subList != null && subList.size() == 1) {
                                szrcb.setSysSubjectID(subList.get(0).getPkSubId());
                                szrcb.setSysSubjectCode(subList.get(0).getSubCode());
                                szrcb.setSysSubjectName(subList.get(0).getSubName());
                                szrcb.setSysSubjectFullName(subList.get(0).getFullName());
                            }
                        }
                        szrcb.setBankAccount(bankAccount);
                        szrcbBankBillDao.insertSzrcb(szrcb);
                    }
                } else if (s.contains("查询账号:") || s.contains("查询账号：")) {
                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    String bankAccountStr = null;
                    String bankAccount = null;
                    System.out.println(bankAccount);
                    if (s.contains("查询账号:")) {
                        bankAccountStr = excelReader.readExcelContent(is, 0, 1);
                        bankAccount = StringUtil.getNumberFromString(bankAccountStr);

                        if (null != bankAccount && !"".equals(bankAccount)) {
                            // 先查询能否创建科目
                            Map<String, Object> pa1 = new HashMap<String, Object>();
                            pa1.put("accountID", accountID);
                            pa1.put("busDate", busDate);
                            pa1.put("bankAccount", bankAccount);
                            List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                            String subID = null;
                            String subject = null;
                            if (li1 == null || li1.isEmpty()) {

                                // 创建科目
                                subject = vatService.getNumber("1002", "7", "1002000");
                                SubjectMessage sm = vatService.createSub(subject, "1002", "交通银行" + bankAccount,
                                        "银行存款_" + "交通银行" + bankAccount);
                                subID = sm.getPk_sub_id();
                            } else {
                                subID = li1.get(0).getPkSubId();
                                subject = li1.get(0).getSubCode();
                            }
                            // 自动创建银行账户和科目的映射
                            Bank2Subject b2s = new Bank2Subject();
                            // 主键
                            b2s.setId(UUIDUtils.getUUID());
                            ;
                            // 账套ID
                            b2s.setAccountID(accountID);
                            // 银行账户
                            b2s.setBankAccount(bankAccount);
                            // 银行科目主键
                            b2s.setSubID(subID);
                            // 银行名称
                            b2s.setBankName("交通银行");
                            // 银行类型
                            b2s.setBankType("交通银行");
                            // 币种
                            b2s.setCurrency("人民币");
                            // 科目名称
                            b2s.setSubName("交通银行" + bankAccount);
                            // 科目全名
                            b2s.setSubFullName("银行存款_" + "交通银行" + bankAccount);
                            // 科目编码
                            b2s.setSubCode(subject);
                            // 创建者ID
                            b2s.setCreateID(user.getUserID());
                            // 创建者手机号
                            b2s.setCreateTel(user.getLoginUser());
                            // 创建人名字
                            b2s.setCreateName(user.getUserName());
                            // 创建时间
                            b2s.setCreateTime(new Date());
                            Map<String, Object> pa = new HashMap<String, Object>();
                            pa.put("bankAccount", bankAccount);
                            List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                            boolean canAdd = true;
                            if (li == null || li.isEmpty()) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            } else {
                                for (int i = 0; i < li.size(); i++) {
                                    if (li.get(i).getAccountID().equals(accountID)) {
                                        canAdd = false;
                                    }
                                }
                                if (canAdd) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                }
                            }

                        } else {
                            throw new BusinessException("请检查您导入的交通银行对账单,请先完善银行账户信息");
                        }
                        // 交通银行
                        for (int i = 2; i < list.size(); i++) {
                            // Map<String, Object> map = list.get(i);
                            // String accountID = account.getAccountID();
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("busDate", busDate);
                            param.put("accountID", accountID);
                            param.put("bankType", "交通银行");
                            List<BcmBank> sameList = bcmDao.querySame(param);
                            if (sameList != null && sameList.size() > 0) {
                                result.put("success", "fail");
                                result.put("message", "您本月已导入过数据,如需重新导,请先批量删除本月数据!");
                                return result;
                            }
                        }

                        for (int i = 2; i < list.size(); i++) {
                            Map<String, Object> map = list.get(i);
                            // 交通银行对账单主键
                            String bcmBillID = UUIDUtils.getUUID();
                            // 账套ID
                            // String accountID = account.getAccountID();
                            // 会计期间
                            String period = busDate;
                            // 导入时间
                            long importDate = System.currentTimeMillis();
                            if ("借方交易笔数:".equals(map.get("map0").toString())) {
                                break;
                            }
                            // 交易时间
                            Date transactionTime = map.get("map0") == null
                                    || "".equals(map.get("map0").toString().trim()) ? null
                                    : DateUtil.fomatToDate1(map.get("map0").toString());
                            // 摘要
                            String des = map.get("map1").toString();
                            // 凭证种类
                            String vouchType = map.get("map2").toString();
                            // 凭证号码
                            String vouchNo = map.get("map3").toString();
                            // 企业业务编号
                            String qyywNumber = map.get("map4").toString();
                            // 发生额
                            double fse = "".equals(map.get("map5")) ? 0.0
                                    : Double.parseDouble(map.get("map5").toString().replaceAll(",", ""));
                            // 币种
                            String bz = map.get("map6").toString();
                            // 余额
                            double banalce = "".equals(map.get("map7")) ? 0.0
                                    : Double.parseDouble(map.get("map7").toString().replaceAll(",", ""));
                            // 对方账号
                            String dfAccount = map.get("map8").toString();
                            // 对方户名
                            String dfAccountName = map.get("map9").toString();
                            // 借贷标志
                            String jdFlag = map.get("map10").toString();
                            // 卡号
                            String cardNumber = map.get("map11").toString();

                            String flag = "0";
                            if ("借".equals(jdFlag)) {

                                flag = "1";
                            } else {
                                flag = "2";
                            }
                            String subjectInfo = getSubject(des, bz, dfAccountName, flag, account.getAccountID(),
                                    busDate, "交通银行");
                            BcmBank bcmBank = new BcmBank();
                            bcmBank.setBcmBillID(bcmBillID);
                            bcmBank.setAccountID(accountID);
                            bcmBank.setPeriod(period);
                            bcmBank.setBusDate(importDate + i);
                            bcmBank.setTransactionTime(transactionTime);
                            bcmBank.setDes(des);
                            bcmBank.setVouchType(vouchType);
                            bcmBank.setVouchNo(vouchNo);
                            bcmBank.setQyywNumber(qyywNumber);
                            bcmBank.setFse(fse);
                            bcmBank.setBz(bz);
                            bcmBank.setBanalce(banalce);
                            bcmBank.setDfAccount(dfAccount);
                            bcmBank.setDfAccountName(dfAccountName);
                            bcmBank.setJdFlag(jdFlag);
                            bcmBank.setCardNumber(cardNumber);
                            if (subjectInfo != null) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    param.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    bcmBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    bcmBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    bcmBank.setSysSubjectName(subList.get(0).getSubName());
                                    bcmBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            bcmBank.setBankAccount(bankAccount);
                            bcmDao.insertBcm(bcmBank);
                        }
                    }
                    if (s.contains("查询账号：")) {
                        // 交通银行新模板(谭显众提供)
                        bankAccountStr = excelReader.readExcelContent(is, 0, 0);
                        bankAccount = StringUtil.getNumberFromString(bankAccountStr);
                        if (null != bankAccount && !"".equals(bankAccount)) {
                            // 创建科目
                            Map<String, Object> pa1 = new HashMap<String, Object>();
                            pa1.put("accountID", accountID);
                            pa1.put("busDate", busDate);
                            pa1.put("bankAccount", bankAccount);
                            List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                            String subID = null;
                            String subject = null;
                            if (li1 == null || li1.isEmpty()) {

                                subject = vatService.getNumber("1002", "7", "1002000");
                                SubjectMessage sm = vatService.createSub(subject, "1002", "交通银行" + bankAccount,
                                        "银行存款_" + "交通银行" + bankAccount);
                                subID = sm.getPk_sub_id();
                            } else {
                                subID = li1.get(0).getPkSubId();
                                subject = li1.get(0).getSubCode();
                            }
                            // 自动创建银行账户和科目的映射
                            Bank2Subject b2s = new Bank2Subject();
                            // 主键
                            b2s.setId(UUIDUtils.getUUID());
                            ;
                            // 账套ID
                            b2s.setAccountID(accountID);
                            // 银行账户
                            b2s.setBankAccount(bankAccount);
                            // 银行科目主键
                            b2s.setSubID(subID);
                            // 银行名称
                            b2s.setBankName("交通银行");
                            // 银行类型
                            b2s.setBankType("交通银行");
                            // 币种
                            b2s.setCurrency("人民币");
                            // 科目名称
                            b2s.setSubName("交通银行" + bankAccount);
                            // 科目全名
                            b2s.setSubFullName("银行存款_" + "交通银行" + bankAccount);
                            // 科目编码
                            b2s.setSubCode(subject);
                            // 创建者ID
                            b2s.setCreateID(user.getUserID());
                            // 创建者手机号
                            b2s.setCreateTel(user.getLoginUser());
                            // 创建人名字
                            b2s.setCreateName(user.getUserName());
                            // 创建时间
                            b2s.setCreateTime(new Date());
                            Map<String, Object> pa = new HashMap<String, Object>();
                            pa.put("bankAccount", bankAccount);
                            List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                            boolean canAdd = true;
                            if (li == null || li.isEmpty()) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            } else {
                                for (int i = 0; i < li.size(); i++) {
                                    if (li.get(i).getAccountID().equals(accountID)) {
                                        canAdd = false;
                                    }
                                }
                                if (canAdd) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                }
                            }
                        } else {
                            throw new BusinessException("请检查您导入的交通银行对账单,请先完善银行账户信息");
                        }
                        for (int i = 2; i < list.size(); i++) {
                            // Map<String, Object> map = list.get(i);
                            // String transactionTime =
                            // map.get("map0").toString();
                            // String accountID = account.getAccountID();
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("busDate", busDate);
                            param.put("accountID", accountID);
                            param.put("bankType", "交通银行");
                            List<BcmBank1> sameList = bcm1Dao.querySame(param);
                            if (sameList != null && sameList.size() > 0) {
                                result.put("success", "fail");
                                result.put("message", "您本月已导入过数据,如需重新导,请先批量删除本月数据!");
                                return result;
                            }
                        }
                        for (int i = 1; i < list.size(); i++) {
                            Map<String, Object> map = list.get(i);
                            // 交通银行对账单主键
                            String bcmBillID = UUIDUtils.getUUID();
                            // 账套ID
                            // String accountID = account.getAccountID();
                            // 会计期间
                            String period = busDate;
                            // 导入时间
                            long importDate = System.currentTimeMillis();
                            if ("借方交易笔数:".equals(map.get("map0").toString())) {
                                break;
                            }
                            // 交易时间
                            Date transactionTime = map.get("map0") == null
                                    || "".equals(map.get("map0").toString().trim()) ? null
                                    : DateUtil.fomatToDate5(map.get("map0").toString());
                            if ("借方交易笔数:".equals(transactionTime)) {
                                break;
                            }
                            // 摘要
                            String des = map.get("map1").toString();
                            // 余额
                            double banalce = "".equals(map.get("map2")) ? 0.0
                                    : Double.parseDouble(map.get("map2").toString().replaceAll(",", ""));
                            // 对方账号
                            String dfAccount = map.get("map3").toString();
                            // 对方户名
                            String dfAccountName = map.get("map4").toString();

                            // 凭证种类
                            String vouchType = map.get("map5").toString();
                            // 凭证号码
                            String vouchNo = map.get("map6").toString();
                            // 企业业务编号
                            String qyywNumber = map.get("map7").toString();
                            // 转账金额
                            double zzje = "".equals(map.get("map8")) ? 0.0
                                    : Double.parseDouble(map.get("map8").toString().replaceAll(",", ""));
                            // 借贷标志
                            String jdFlag = map.get("map9").toString();

                            String flag = "0";
                            if ("借".equals(jdFlag)) {

                                flag = "1";
                            } else {
                                flag = "2";
                            }
                            String subjectInfo = getSubject(des, null, dfAccountName, flag, account.getAccountID(),
                                    busDate, "交通银行");
                            BcmBank1 bcm1Bank = new BcmBank1();
                            bcm1Bank.setBcmBillID(bcmBillID);
                            bcm1Bank.setAccountID(accountID);
                            bcm1Bank.setPeriod(period);
                            bcm1Bank.setBusDate(importDate + i);
                            bcm1Bank.setTransactionTime(transactionTime);
                            bcm1Bank.setDes(des);
                            bcm1Bank.setVouchType(vouchType);
                            bcm1Bank.setVouchNo(vouchNo);
                            bcm1Bank.setQyywNumber(qyywNumber);
                            bcm1Bank.setZzje(zzje);

                            bcm1Bank.setBanalce(banalce);
                            bcm1Bank.setDfAccount(dfAccount);
                            bcm1Bank.setDfAccountName(dfAccountName);
                            bcm1Bank.setJdFlag(jdFlag);
                            if (subjectInfo != null) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    param.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    bcm1Bank.setSysSubjectID(subList.get(0).getPkSubId());
                                    bcm1Bank.setSysSubjectCode(subList.get(0).getSubCode());
                                    bcm1Bank.setSysSubjectName(subList.get(0).getSubName());
                                    bcm1Bank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            bcm1Bank.setBankAccount(bankAccount);
                            bcm1Dao.insertBcm(bcm1Bank);
                        }
                    }

                } else if (s.equals("查询账号[ Inquirer account number ]")) {
                    // 中国银行
                    Map<String, Object> uploadTCmBkbillBoc = tCmBkbillBocService.uploadTCmBkbillBoc(file,
                            request.getSession());
                    String success = (String) uploadTCmBkbillBoc.get("success");
                    if (null != success) {
                        if (success.equals("fail")) {
                            result.putAll(uploadTCmBkbillBoc);
                            return result;
                        }
                    }
                } else if (s.equals("账号: ")) {
                    // 工商银行
                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    String bankAccount = excelReader.readExcelContent(is, 0, 1);
                    BigDecimal bd = new BigDecimal(bankAccount);
                    bankAccount = bd.toPlainString();

                    if (null != bankAccount && !"".equals(bankAccount)) {
                        // 创建科目
                        Map<String, Object> pa1 = new HashMap<String, Object>();
                        pa1.put("accountID", accountID);
                        pa1.put("busDate", busDate);
                        pa1.put("bankAccount", bankAccount);
                        List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                        String subID = null;
                        String subject = null;
                        if (li1 == null || li1.isEmpty()) {
                            subject = vatService.getNumber("1002", "7", "1002000");
                            SubjectMessage sm = vatService.createSub(subject, "1002", "工商银行" + bankAccount,
                                    "银行存款_" + "工商银行" + bankAccount);
                            subID = sm.getPk_sub_id();
                        } else {
                            subID = li1.get(0).getPkSubId();
                            subject = li1.get(0).getSubCode();
                        }
                        // 自动创建银行账户和科目的映射
                        Bank2Subject b2s = new Bank2Subject();
                        // 主键
                        b2s.setId(UUIDUtils.getUUID());
                        // 账套ID
                        b2s.setAccountID(accountID);
                        // 银行账户
                        b2s.setBankAccount(bankAccount);
                        // 银行科目主键
                        b2s.setSubID(subID);
                        // 银行名称
                        b2s.setBankName("工商银行");
                        // 银行类型
                        b2s.setBankType("工商银行");
                        // 币种
                        b2s.setCurrency("人民币");
                        // 科目名称
                        b2s.setSubName("工商银行" + bankAccount);
                        // 科目全名
                        b2s.setSubFullName("银行存款_" + "工商银行" + bankAccount);
                        // 科目编码
                        b2s.setSubCode(subject);
                        // 创建者ID
                        b2s.setCreateID(user.getUserID());
                        // 创建者手机号
                        b2s.setCreateTel(user.getLoginUser());
                        // 创建人名字
                        b2s.setCreateName(user.getUserName());
                        // 创建时间
                        b2s.setCreateTime(new Date());
                        Map<String, Object> pa = new HashMap<String, Object>();
                        pa.put("bankAccount", bankAccount);
                        List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                        boolean canAdd = true;
                        if (li == null || li.isEmpty()) {
                            bank2SubjectDao.addBank2Subject(b2s);
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                if (li.get(i).getAccountID().equals(accountID)) {
                                    canAdd = false;
                                }
                            }
                            if (canAdd) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            }
                        }
                    } else {
                        throw new BusinessException("请检查您导入的工商银行对账单,请先完善银行账户信息");
                    }
                    if (list != null && list.size() > 0) {
                        for (int i = 1; i < list.size(); i++) {
                            // Map<String, Object> map = list.get(i);
                            // String transactionTime =
                            // map.get("map0").toString();
                            // String accountID = account.getAccountID();
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("busDate", busDate);
                            param.put("accountID", accountID);
                            param.put("bankType", "工商银行");
                            // 小规模
                            if (account.getSsType() == 1) {
                                // if(busDate)
                                // List<IcbcBank> sameList =
                                // icbcBankDao.querySame(param);
                                // if (sameList != null && sameList.size() > 0)
                                // {
                                // result.put("success", "fail");
                                // result.put("message",
                                // "您本月已导入过数据,如需重新导,请先批量删除本月数据!");
                                // return result;
                                // }
                            } else {
                                List<IcbcBank> sameList = icbcBankDao.querySame(param);
                                if (sameList != null && sameList.size() > 0) {
                                    result.put("success", "fail");
                                    result.put("message", "您本月已导入过数据,如需重新导,请先批量删除本月数据!");
                                    return result;
                                }
                            }
                        }
                        for (int k = 1; k < list.size(); k++) {
                            Map<String, Object> map = list.get(k);
                            String icbcBillID = UUIDUtils.getUUID();
                            // String accountID = account.getAccountID();
                            String period = busDate;
                            long importDate = System.currentTimeMillis();
                            String rqDate = map.get("map0").toString();
                            if (rqDate.contains("截止日期:")) {
                                break;
                            }
                            String transactionType = map.get("map1").toString();

                            if (transactionType.equals("交易类型")) {
                                continue;
                            }

                            String vouchType = map.get("map2").toString();
                            String vouchNo = map.get("map3").toString();
                            String dfAccountName = map.get("map4").toString();

                            String dfAccount = map.get("map5").toString();
                            String des = null == map.get("map6") ? "" : map.get("map6").toString();
                            Object jffsAmountObj = map.get("map7");
                            double jffsAmount;
                            if (null == jffsAmountObj || "".equals(jffsAmountObj)) {
                                jffsAmount = 0;
                            } else {
                                jffsAmount = Double.parseDouble(jffsAmountObj.toString().replaceAll(",", "").trim());
                            }
                            Object dffsAmountObj = map.get("map8");
                            double dffsAmount;
                            if (null == dffsAmountObj || "".equals(dffsAmountObj)) {
                                dffsAmount = 0;
                            } else {
                                dffsAmount = Double.parseDouble(dffsAmountObj.toString().replaceAll(",", "").trim());
                            }
                            Object balanceObj = map.get("map9");
                            double balance;
                            if (null == balanceObj) {
                                balance = 0;
                            } else {
                                balance = Double.parseDouble(balanceObj.toString().replaceAll(",", "").trim());
                            }
                            String zy = des;
                            String bz = null;

                            String flag = "0";
                            if (jffsAmount != 0.0) {
                                flag = "1";
                            }
                            if (dffsAmount != 0.0) {
                                flag = "2";
                            }
                            String subjectInfo = getSubject(zy, bz, dfAccountName, flag, account.getAccountID(),
                                    busDate, "工商银行");

                            IcbcBank icbcBank = new IcbcBank();
                            icbcBank.setIcbcBillID(icbcBillID);
                            icbcBank.setAccountID(accountID);
                            icbcBank.setPeriod(period);
                            icbcBank.setBusDate(importDate + k);
                            icbcBank.setRqDate(DateUtil.fomatToDate(rqDate));
                            icbcBank.setTransactionType(transactionType);
                            icbcBank.setVouchType(vouchType);
                            icbcBank.setVouchNo(vouchNo);
                            icbcBank.setDfAccountName(dfAccountName);
                            icbcBank.setDfAccount(dfAccount);
                            icbcBank.setJffsAmount(jffsAmount);
                            icbcBank.setDes(des);
                            icbcBank.setDffsAmount(dffsAmount);
                            icbcBank.setBalance(balance);
                            icbcBank.setBankAccount(bankAccount);
                            if (subjectInfo != null) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    param.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    icbcBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    icbcBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    icbcBank.setSysSubjectName(subList.get(0).getSubName());
                                    icbcBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            icbcBankDao.insertIcbc(icbcBank);
                        }
                    }
                } else if (s.equals("中国建设银行") || s.equals("账号")) {
                    if (s.equals("中国建设银行")) {
                        // 建设银行
                        List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                        String bankAccount = list.get(2).get("map1").toString();
                        if (null != bankAccount && !"".equals(bankAccount)) {
                            // 创建科目
                            Map<String, Object> pa1 = new HashMap<String, Object>();
                            pa1.put("accountID", accountID);
                            pa1.put("busDate", busDate);
                            pa1.put("bankAccount", bankAccount);
                            List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                            String subID = null;
                            String subject = null;
                            if (li1 == null || li1.isEmpty()) {
                                subject = vatService.getNumber("1002", "7", "1002000");
                                SubjectMessage sm = vatService.createSub(subject, "1002", "中国建设银行" + bankAccount,
                                        "银行存款_" + "中国建设银行" + bankAccount);
                                subID = sm.getPk_sub_id();
                            } else {
                                subID = li1.get(0).getPkSubId();
                                subject = li1.get(0).getSubCode();
                            }

                            // 自动创建银行账户和科目的映射
                            Bank2Subject b2s = new Bank2Subject();
                            // 主键
                            b2s.setId(UUIDUtils.getUUID());
                            // 账套ID
                            b2s.setAccountID(accountID);
                            // 银行账户
                            b2s.setBankAccount(bankAccount);
                            // 银行科目主键
                            b2s.setSubID(subID);
                            // 银行名称
                            b2s.setBankName("中国建设银行");
                            // 银行类型
                            b2s.setBankType("中国建设银行");
                            // 币种
                            b2s.setCurrency("人民币");
                            // 科目名称
                            b2s.setSubName("中国建设银行" + bankAccount);
                            // 科目全名
                            b2s.setSubFullName("银行存款_" + "中国建设银行" + bankAccount);
                            // 科目编码
                            b2s.setSubCode(subject);
                            // 创建者ID
                            b2s.setCreateID(user.getUserID());
                            // 创建者手机号
                            b2s.setCreateTel(user.getLoginUser());
                            // 创建人名字
                            b2s.setCreateName(user.getUserName());
                            // 创建时间
                            b2s.setCreateTime(new Date());
                            Map<String, Object> pa = new HashMap<String, Object>();
                            pa.put("bankAccount", bankAccount);
                            List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                            boolean canAdd = true;
                            if (li == null || li.isEmpty()) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            } else {
                                for (int i = 0; i < li.size(); i++) {
                                    if (li.get(i).getAccountID().equals(accountID)) {
                                        canAdd = false;
                                    }
                                }
                                if (canAdd) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                }
                            }
                        } else {
                            throw new BusinessException("请检查您导入的中国建设银行对账单,请先完善银行账户信息");
                        }
                        boolean isNumber = StringUtil.isNumeric(bankAccount);
                        if (!isNumber) {
                            bankAccount = list.get(0).get("map1").toString();
                        }
                        int d = 6;
                        if (list != null && list.size() > 0) {

                            for (int j = 5; j < list.size(); j++) {
                                Map<String, Object> map = list.get(j);

                                if (map == null || (map != null && map.isEmpty())) {
                                    continue;
                                }
                                Object object = map.get("map0");
                                if (object != null) {
                                    String jysj = String.valueOf(object);
                                    jysj = jysj.replace(" ", "");
                                    if (jysj.equals("交易时间")) {
                                        d = j + 1;
                                    }
                                }

                                // 账户明细编号-交易流水号
                                String transactionID = StringUtil.objToStr(map.get("map11"));
                                if (transactionID == null) {
                                    continue;
                                }
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                param.put("transactionID", transactionID);
                                List<JsBank> bankList = jsBankDao.querySame(param);
                                if (null != bankList && bankList.size() > 0) {
                                    result.put("success", "fail");
                                    result.put("message", "数据不允许重复导入");
                                    return result;
                                }
                            }

                            // 账套ID
                            // String accountID = account.getAccountID();
                            for (int k = d; k < list.size(); k++) {
                                Map<String, Object> map = list.get(k);
                                if (map == null || (map != null && map.isEmpty())) {
                                    continue;
                                }
                                String jsBillID = UUIDUtils.getUUID();
                                JsBank jsBank = new JsBank();
                                jsBank.setJsBillID(jsBillID);
                                jsBank.setAccountID(accountID);
                                jsBank.setPeriod(busDate);
                                jsBank.setVouchID(null);
                                // 交易时间
                                jsBank.setTransaction_time(DateUtil.fomatToDate4((String) map.get("map0")));

                                if (StringUtil.objToDouble(map.get("map1")) == null
                                        && StringUtil.objToDouble(map.get("map2")) == null) {
                                    continue;
                                }
                                // 借方发生额
                                jsBank.setJffsAmount(StringUtil.objToDouble(map.get("map1")));
                                // 贷方发生额
                                jsBank.setDffsAmount(StringUtil.objToDouble(map.get("map2")));
                                // 余额
                                jsBank.setBalance(StringUtil.objToDouble(map.get("map3")));
                                // 币种
                                jsBank.setTypeCurrency(StringUtil.objToStr(map.get("map4")));
                                // 对方户名
                                jsBank.setDfAccountName(StringUtil.objToStr(map.get("map5")));
                                // 对方账号
                                jsBank.setDfAccountNumber(StringUtil.objToStr(map.get("map6")));
                                // 对方开户机构
                                jsBank.setKhhName(StringUtil.objToStr(map.get("map7")));
                                // 记账日期
                                jsBank.setTransaction_date(DateUtil.fomatToDate5((String) map.get("map8")));
                                // 摘要
                                jsBank.setReference(StringUtil.objToStr(map.get("map9")));
                                // 备注
                                jsBank.setRemarks(StringUtil.objToStr(map.get("map10")));
                                // 账户明细编号-交易流水号
                                jsBank.setTransactionID(StringUtil.objToStr(map.get("map11")));
                                // 企业流水号
                                jsBank.setCompanyTransactionID(StringUtil.objToStr(map.get("map12")));
                                // 凭证种类
                                jsBank.setVouchType(StringUtil.objToStr(map.get("map13")));
                                // 凭证号
                                jsBank.setVouchNo(StringUtil.objToStr(map.get("map14")));
                                // 关联账户
                                jsBank.setGlAccount(StringUtil.objToStr(map.get("map15")));
                                // 导入时间
                                jsBank.setImportDate(System.currentTimeMillis() + k);
                                jsBank.setBankType(Constrants.BANK_NAME_JS);

                                String flag = "0";
                                double debitAmount = StringUtil.objToDouble(map.get("map1"));
                                if (debitAmount != 0.0) {
                                    flag = "1";
                                }
                                double creditAmount = StringUtil.objToDouble(map.get("map2"));
                                if (creditAmount != 0.0) {
                                    flag = "2";
                                }
                                String subjectInfo = getSubject(StringUtil.objToStr(map.get("map9")),
                                        StringUtil.objToStr(map.get("map10")), StringUtil.objToStr(map.get("map5")),
                                        flag, account.getAccountID(), busDate, "建设银行");
                                if (subjectInfo != null) {
                                    Map<String, Object> param = new HashMap<String, Object>();
                                    param.put("accountID", account.getAccountID());
                                    param.put("period", busDate);
                                    String[] subjectInfos = subjectInfo.split("_");
                                    String subCode = null;
                                    String subName = null;
                                    if (subjectInfos.length == 2) {
                                        param.put("subCode", subjectInfos[0]);
                                        List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper
                                                .querySubject(param);
                                        if (subList != null) {
                                            List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                            if (li != null && li.size() == 1) {
                                                subCode = li.get(0).getSubCode();
                                                subName = li.get(0).getSubName();
                                            }
                                        }
                                    } else {
                                        subCode = subjectInfos[0];
                                        subName = subjectInfos[subjectInfos.length - 1];
                                    }

                                    param.put("subCode", subCode);
                                    param.put("subName", subName);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null && subList.size() == 1) {
                                        jsBank.setSysSubjectID(subList.get(0).getPkSubId());
                                        jsBank.setSysSubjectCode(subList.get(0).getSubCode());
                                        jsBank.setSysSubjectName(subList.get(0).getSubName());
                                        jsBank.setSysSubjectFullName(subList.get(0).getFullName());
                                    }
                                }
                                jsBank.setBankAccount(bankAccount);
                                jsBankDao.insertJsBank(jsBank);
                            }
                        }
                    }
                    if (s.equals("账号")) {
                        // 建设银行新模板 暂时不支持
                        result.put("success", "fail");
                        result.put("info", "请仔细检查银行模板!");
                        return result;
                    }
                } else if (s.contains("农业银行") || s.contains("账户明细")) {
                    if (s.contains("农业银行")) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("busDate", busDate);
                        List<String> tranIDs = nyBankDao.querytranID(param);

                        List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);

                        if (list == null) {
                            result.put("success", "fail");
                            result.put("message", "没有数据导入");
                            return result;
                        }

                        if (tranIDs != null && tranIDs.size() > 0) {
                            if (list != null) {
                                for (int j = 2; j < list.size(); j++) {
                                    Map<String, Object> map = list.get(j);
                                    // 交易流水号
                                    String transactionID = StringUtil.objToStr(map.get("map5"));
                                    if (transactionID != null) {
                                        if (tranIDs.contains(transactionID)) {
                                            result.put("success", "fail");
                                            result.put("message", "数据不允许重复导入 交易流水号:" + transactionID);
                                            return result;
                                        }
                                    }
                                }
                            }
                        }
                        // 账套ID
                        // String accountID = account.getAccountID();
                        for (int k = 2; k < list.size(); k++) {
                            Map<String, Object> map = list.get(k);
                            NyBank nyBank = new NyBank();
                            String nyBillID = UUIDUtils.getUUID();
                            nyBank.setNyBillID(nyBillID);
                            nyBank.setAccountID(accountID);
                            nyBank.setPeriod(busDate);
                            nyBank.setVouchID(null);
                            nyBank.setBankType(Constrants.BANK_NAME_NY); // 农业银行

                            String accountNum = StringUtil.objToStr1(map.get("map0"));
                            if (accountNum == null) {
                                break;
                            }
                            String bankAccount = StringUtil.objToStr(map.get("map0").toString());
                            if (null != bankAccount && !"".equals(bankAccount)) {
                                // 创建科目
                                Map<String, Object> pa1 = new HashMap<String, Object>();
                                pa1.put("accountID", accountID);
                                pa1.put("busDate", busDate);
                                pa1.put("bankAccount", bankAccount);
                                List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                                String subID = null;
                                String subject = null;
                                if (li1 == null || li1.isEmpty()) {
                                    subject = vatService.getNumber("1002", "7", "1002000");
                                    SubjectMessage sm = vatService.createSub(subject, "1002", "农业银行" + bankAccount,
                                            "银行存款_" + "农业银行" + bankAccount);
                                    subID = sm.getPk_sub_id();
                                } else {
                                    subID = li1.get(0).getPkSubId();
                                    subject = li1.get(0).getSubCode();
                                }
                                // 自动创建银行账户和科目的映射
                                Bank2Subject b2s = new Bank2Subject();
                                // 主键
                                b2s.setId(UUIDUtils.getUUID());
                                // 账套ID
                                b2s.setAccountID(accountID);
                                // 银行账户
                                b2s.setBankAccount(bankAccount);
                                // 银行科目主键
                                b2s.setSubID(subID);
                                // 银行名称
                                b2s.setBankName("农业银行");
                                // 银行类型
                                b2s.setBankType("农业银行");
                                // 币种
                                b2s.setCurrency("人民币");
                                // 科目名称
                                b2s.setSubName("农业银行" + bankAccount);
                                // 科目全名
                                b2s.setSubFullName("银行存款_" + "农业银行" + bankAccount);
                                // 科目编码
                                b2s.setSubCode(subject);
                                // 创建者ID
                                b2s.setCreateID(user.getUserID());
                                // 创建者手机号
                                b2s.setCreateTel(user.getLoginUser());
                                // 创建人名字
                                b2s.setCreateName(user.getUserName());
                                // 创建时间
                                b2s.setCreateTime(new Date());
                                Map<String, Object> pa = new HashMap<String, Object>();
                                pa.put("bankAccount", bankAccount);
                                List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                                boolean canAdd = true;
                                if (li == null || li.isEmpty()) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                } else {
                                    for (int i = 0; i < li.size(); i++) {
                                        if (li.get(i).getAccountID().equals(accountID)) {
                                            canAdd = false;
                                            break;
                                        }
                                    }
                                    if (canAdd) {
                                        bank2SubjectDao.addBank2Subject(b2s);
                                    }
                                }
                            } else {
                                throw new BusinessException("请检查您导入的农业银行对账单,请先完善银行账户信息");
                            }
                            // 账号
                            nyBank.setAccountNum(StringUtil.objToStr(map.get("map0").toString()));
                            // 币种
                            nyBank.setTypeCurrency(StringUtil.objToStr(map.get("map1")));
                            // 凭证号
                            nyBank.setVouchNo(StringUtil.objToStr(map.get("map2")));
                            // 日期
                            Date date1 = DateUtil.fomatToDate5((String) map.get("map3"));
                            nyBank.setTransaction_time(date1);
                            // 交易代码
                            nyBank.setJycode(StringUtil.objToStr(map.get("map4")));
                            // 交易流水号
                            nyBank.setTransactionID(StringUtil.objToStr(map.get("map5")));
                            // 交易柜员
                            nyBank.setTransactionEmp(StringUtil.objToStr(map.get("map6")));
                            // 借贷方
                            nyBank.setJfdf(StringUtil.objToStr(map.get("map7")));
                            // 借方发生额
                            BigDecimal fsAmount = StringUtil.objToBigDecimal(map.get("map8"));
                            if (fsAmount == null) {
                                fsAmount = new BigDecimal("0.00");
                            }
                            nyBank.setFsAmount(fsAmount);
                            // 账户余额
                            BigDecimal accountBalance = StringUtil.objToBigDecimal(map.get("map9"));
                            if (accountBalance == null) {
                                accountBalance = new BigDecimal("0.00");
                            }
                            nyBank.setAccountBalance(accountBalance);

                            // 对方账号
                            nyBank.setDfAccountNumber(StringUtil.objToStr(map.get("map10")));
                            // 对方户名
                            nyBank.setDfAccountName(StringUtil.objToStr(map.get("map11")));
                            // 对方行名
                            nyBank.setDfBankName(StringUtil.objToStr(map.get("map12")));
                            // 摘要
                            nyBank.setReference(StringUtil.objToStr(map.get("map13")));

                            // 附言
                            nyBank.setNotes(StringUtil.objToStr(map.get("map14")));

                            // 导入者
                            nyBank.setUserID(user.getUserID());
                            // 导入时间戳
                            nyBank.setAddTime(new Date().getTime());
                            nyBank.setAddTime(System.currentTimeMillis() + 1);
                            String jfdf = StringUtil.objToStr(map.get("map7")).replace(" ", "");
                            String flag = "0";
                            if (fsAmount.doubleValue() != 0.0) {
                                if ("借".equals(jfdf)) {
                                    flag = "2";
                                } else {
                                    flag = "1";
                                }
                            }
                            String subjectInfo = getSubject(StringUtil.objToStr(map.get("map13")),
                                    StringUtil.objToStr(map.get("map14")), StringUtil.objToStr(map.get("map11")), flag,
                                    account.getAccountID(), busDate, "中国农业银行");
                            if (subjectInfo != null) {
                                Map<String, Object> para = new HashMap<String, Object>();
                                para.put("accountID", account.getAccountID());
                                para.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    para.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                para.put("subCode", subCode);
                                para.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                                if (subList != null && subList.size() == 1) {
                                    nyBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    nyBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    nyBank.setSysSubjectName(subList.get(0).getSubName());
                                    nyBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            nyBankDao.insertNyBank(nyBank);
                        }
                    } else {
                        // 新模板(官网下载)
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("busDate", busDate);
                        String bankAccountStr = excelReader.readExcelContent(is, 0, 0);
                        String bankAccount = StringUtil.getNumberFromString(bankAccountStr);
                        if (null != bankAccount && !"".equals(bankAccount)) {

                            // 创建科目
                            Map<String, Object> pa1 = new HashMap<String, Object>();
                            pa1.put("accountID", accountID);
                            pa1.put("busDate", busDate);
                            pa1.put("bankAccount", bankAccount);
                            List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                            String subID = null;
                            String subject = null;
                            if (li1 == null || li1.isEmpty()) {

                                // 创建科目
                                subject = vatService.getNumber("1002", "7", "1002000");
                                SubjectMessage sm = vatService.createSub(subject, "1002", "农业银行" + bankAccount,
                                        "银行存款_" + "农业银行" + bankAccount);
                                subID = sm.getPk_sub_id();
                            } else {
                                subID = li1.get(0).getPkSubId();
                                subject = li1.get(0).getSubCode();
                            }
                            // 自动创建银行账户和科目的映射
                            Bank2Subject b2s = new Bank2Subject();
                            // 主键
                            b2s.setId(UUIDUtils.getUUID());
                            // 账套ID
                            b2s.setAccountID(accountID);
                            // 银行账户
                            b2s.setBankAccount(bankAccount);
                            // 银行科目主键
                            b2s.setSubID(subID);
                            // 银行名称
                            b2s.setBankName("农业银行");
                            // 银行类型
                            b2s.setBankType("农业银行");
                            // 币种
                            b2s.setCurrency("人民币");
                            // 科目名称
                            b2s.setSubName("农业银行" + bankAccount);
                            // 科目全名
                            b2s.setSubFullName("银行存款_" + "农业银行" + bankAccount);
                            // 科目编码
                            b2s.setSubCode(subject);
                            // 创建者ID
                            b2s.setCreateID(user.getUserID());
                            // 创建者手机号
                            b2s.setCreateTel(user.getLoginUser());
                            // 创建人名字
                            b2s.setCreateName(user.getUserName());
                            // 创建时间
                            b2s.setCreateTime(new Date());
                            Map<String, Object> pa = new HashMap<String, Object>();
                            pa.put("bankAccount", bankAccount);
                            List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                            boolean canAdd = true;
                            if (li == null || li.isEmpty()) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            } else {
                                for (int i = 0; i < li.size(); i++) {
                                    if (li.get(i).getAccountID().equals(accountID)) {
                                        canAdd = false;
                                    }
                                }
                                if (canAdd) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                }
                            }
                        } else {
                            throw new BusinessException("请检查您导入的农业银行对账单,请先完善银行账户信息");
                        }
                        // {map0=交易日期, map1=交易时间戳, map2=收入金额, map3=支出金额,
                        // map4=本次余额, map5=手续费总额, map6=交易方式, map7=交易行名,
                        // map8=交易类别, map9=对方省市, map10=对方账号, map11=对方户名,
                        // map12=交易说明, map13=交易摘要, map14=交易附言}
                        List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                        // {map0=交易时间, map1=收入金额, map2=支出金额, map3=账户余额,
                        // map4=交易行名, map5=对方省市, map6=对方账号, map7=对方户名,
                        // map8=交易用途}
                        boolean isMx = false;
                        if (list != null && list.size() > 0) {
                            Map<String, Object> titleMap = list.get(0);
                            Object jysjc = titleMap.get("map1");
                            if (jysjc != null && jysjc.toString().contains("交易时间戳")) {
                                isMx = true;
                            }
                        }
                        if (isMx) {
                            for (int i = 1; i < list.size(); i++) {
                                Object jyrqObj = list.get(i).get("map0");
                                Date transactionTime = null;
                                if (jyrqObj != null && !"".equals(jyrqObj)) {
                                    // 汇总收入金额
                                    if (jyrqObj.toString().contains("汇总收入金额")) {
                                        break;
                                    }
                                    transactionTime = DateUtil.fomatToDate5(jyrqObj.toString().trim());
                                }
                                Object jysscObj = list.get(i).get("map1");
                                String transactionStamp = null;
                                if (jysscObj != null && !"".equals(jysscObj)) {
                                    transactionStamp = jysscObj.toString().trim();
                                }
                                Object srjeObj = list.get(i).get("map2");
                                Double srAmount = null;
                                if (null != srjeObj && !"".equals(srjeObj)) {
                                    srAmount = Double.parseDouble(srjeObj.toString().replaceAll(",", ""));
                                }
                                Object zcjeObj = list.get(i).get("map3");
                                Double zcAmount = null;
                                if (null != zcjeObj && !"".equals(zcjeObj)) {
                                    zcAmount = Double.parseDouble(zcjeObj.toString().trim().replaceAll(",", ""));
                                }
                                Object zcyeObj = list.get(i).get("map4");
                                Double bcAmount = null;
                                if (null != zcyeObj && !"".equals(zcyeObj)) {
                                    bcAmount = Double.parseDouble(zcyeObj.toString().trim().replaceAll(",", ""));
                                }
                                Object sxfzeObj = list.get(i).get("map5");
                                Double sxfAmont = null;
                                if (sxfzeObj != null && !"".equals(sxfzeObj)) {
                                    sxfAmont = Double.parseDouble(sxfzeObj.toString().trim().replaceAll(",", ""));
                                }
                                Object jyfsObj = list.get(i).get("map6");
                                String jyType = null;
                                if (jyfsObj != null && !"".equals(jyfsObj)) {
                                    jyType = jyfsObj.toString().trim();
                                }
                                Object jyhmObj = list.get(i).get("map7");
                                String jyhm = null;
                                if (null != jyhmObj && !"".equals(jyhmObj)) {
                                    jyhm = jyhmObj.toString().trim();
                                }
                                Object jylbObj = list.get(i).get("map8");
                                String jylb = null;
                                if (null != jylbObj && !"".equals(jylbObj)) {
                                    jylb = jylbObj.toString().trim();
                                }
                                Object dfssObj = list.get(i).get("map9");
                                String dfss = null;
                                if (null != dfssObj && !"".equals(dfssObj)) {
                                    dfss = dfssObj.toString().trim();
                                }
                                Object dfzhObj = list.get(i).get("map10");
                                String dfzh = null;
                                if (null != dfzhObj && !"".equals(dfzhObj)) {
                                    dfzh = dfzhObj.toString().trim();
                                }
                                Object dfhmObj = list.get(i).get("map11");
                                String dfhm = null;
                                if (null != dfhmObj && !"".equals(dfhmObj)) {
                                    dfhm = dfhmObj.toString().trim();
                                }
                                Object jysmObj = list.get(i).get("map12");
                                String jysm = null;
                                if (null != jysmObj && !"".equals(jysmObj)) {
                                    jysm = jysmObj.toString().trim();
                                }
                                Object jyzyObj = list.get(i).get("map13");
                                String jyzy = null;
                                if (null != jyzyObj && !"".equals(jyzyObj)) {
                                    jyzy = jyzyObj.toString().trim();
                                }
                                Object jyfyObj = list.get(i).get("map14");
                                String jyfy = null;
                                if (null != jyfyObj && !"".equals(jyfyObj)) {
                                    jyfy = jyfyObj.toString().trim();
                                }
                                NyBankNew nyBankNew = new NyBankNew();
                                nyBankNew.setId(UUIDUtils.getUUID());
                                nyBankNew.setAccountID(accountID);
                                nyBankNew.setPeriod(busDate);
                                nyBankNew.setVouchID("");
                                nyBankNew.setTransactionTime(transactionTime);
                                // nyBankNew.setTransactionStamp(transactionStamp);
                                nyBankNew.setSrAmount(srAmount);
                                nyBankNew.setZcAmount(zcAmount);
                                nyBankNew.setBcAmount(bcAmount);
                                nyBankNew.setSxfAmont(sxfAmont);
                                nyBankNew.setJyType(jyType);
                                nyBankNew.setJyhm(jyhm);
                                nyBankNew.setJylb(jylb);
                                nyBankNew.setDfss(dfss);
                                nyBankNew.setDfzh(dfzh);
                                nyBankNew.setDfhm(dfhm);
                                nyBankNew.setJysm(jysm);
                                nyBankNew.setJyzy(jyzy);
                                nyBankNew.setJyfy(jyfy);
                                nyBankNew.setAddTime(new Date().getTime() + 1);
                                double fsAmount = 0;
                                String jfdf = "借";
                                // 匹配规则

                                if (srAmount == 0) {
                                    fsAmount = zcAmount;
                                    jfdf = "贷";
                                }
                                if (zcAmount == 0) {
                                    fsAmount = srAmount;
                                }

                                String flag = "0";
                                if (fsAmount != 0.0) {
                                    if ("借".equals(jfdf)) {
                                        flag = "2";
                                    } else {
                                        flag = "1";
                                    }
                                }
                                String subjectInfo = getSubject(jyzy, jyfy, dfhm, flag, account.getAccountID(), busDate,
                                        "中国农业银行");
                                if (subjectInfo != null) {
                                    Map<String, Object> para = new HashMap<String, Object>();
                                    para.put("accountID", account.getAccountID());
                                    para.put("period", busDate);
                                    String[] subjectInfos = subjectInfo.split("_");
                                    String subCode = null;
                                    String subName = null;
                                    if (subjectInfos.length == 2) {
                                        para.put("subCode", subjectInfos[0]);
                                        List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper
                                                .querySubject(para);
                                        if (subList != null) {
                                            List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                            if (li != null && li.size() == 1) {
                                                subCode = li.get(0).getSubCode();
                                                subName = li.get(0).getSubName();
                                            }
                                        }
                                    } else {
                                        subCode = subjectInfos[0];
                                        subName = subjectInfos[subjectInfos.length - 1];
                                    }

                                    para.put("subCode", subCode);
                                    para.put("subName", subName);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                                    if (subList != null && subList.size() == 1) {
                                        nyBankNew.setSysSubjectID(subList.get(0).getPkSubId());
                                        nyBankNew.setSysSubjectCode(subList.get(0).getSubCode());
                                        nyBankNew.setSysSubjectName(subList.get(0).getSubName());
                                        nyBankNew.setSysSubjectFullName(subList.get(0).getFullName());
                                    }
                                }
                                nyBankNew.setBankAccount(bankAccount);
                                nyBankDao.insertNyBankNew(nyBankNew);
                            }
                        } else {
                            for (int i = 1; i < list.size(); i++) {
                                Object jyssObj = list.get(i).get("map0");
                                Date transactionTime = null;
                                if (null != jyssObj && !"".equals(jyssObj)) {
                                    if (jyssObj.toString().contains("汇总收入金额")) {
                                        break;
                                    }
                                    transactionTime = DateUtil.fomatToDate5(jyssObj.toString().trim());
                                }
                                Object srjeObj = list.get(i).get("map1");
                                Double srAmount = null;
                                if (null != srjeObj && !"".equals(srjeObj)) {
                                    srAmount = Double.parseDouble(srjeObj.toString().replaceAll(",", ""));
                                }
                                Object zcjeObj = list.get(i).get("map2");
                                Double zcAmount = null;
                                if (null != zcjeObj && !"".equals(zcjeObj)) {
                                    zcAmount = Double.parseDouble(zcjeObj.toString().trim().replaceAll(",", ""));
                                }
                                Object zhyeObj = list.get(i).get("map3");
                                Double zhAmount = null;
                                if (null != zhyeObj && !"".equals(zhyeObj)) {
                                    zhAmount = Double.parseDouble(zhyeObj.toString().trim().replaceAll(",", ""));
                                }
                                Object jyhmObj = list.get(i).get("map4");
                                String jyhm = null;
                                if (null != jyhmObj && !"".equals(jyhmObj)) {
                                    jyhm = jyhmObj.toString().trim();
                                }
                                Object dfssObj = list.get(i).get("map5");
                                String dfss = null;
                                if (dfssObj != null && !"".equals(dfssObj)) {
                                    dfss = dfssObj.toString().trim();
                                }
                                Object dfzhObj = list.get(i).get("map6");
                                String dfzh = null;
                                if (dfzhObj != null && !"".equals(dfzhObj)) {
                                    dfzh = dfzhObj.toString().trim();
                                }
                                Object dfhmObj = list.get(i).get("map7");
                                String dfhm = null;
                                if (null != dfhmObj && !"".equals(dfhmObj)) {
                                    dfhm = dfhmObj.toString().trim();
                                }
                                Object jyytObj = list.get(i).get("map8");
                                String jyyt = null;
                                if (jyytObj != null && !"".equals(jyytObj)) {
                                    jyyt = jyytObj.toString().trim();
                                }
                                NyBankNew nn = new NyBankNew();
                                nn.setId(UUIDUtils.getUUID());
                                nn.setAccountID(accountID);
                                nn.setPeriod(busDate);
                                nn.setTransactionTime(transactionTime);
                                nn.setSrAmount(srAmount);
                                nn.setZcAmount(zcAmount);
                                nn.setZhAmount(zhAmount);
                                nn.setJyhm(jyhm);
                                nn.setDfss(dfss);
                                nn.setDfzh(dfzh);
                                nn.setDfhm(dfhm);
                                nn.setJyyt(jyyt);
                                nn.setAddTime(new Date().getTime() + 1);
                                double fsAmount = 0;
                                String jfdf = "借";
                                // 匹配规则

                                if (srAmount == 0) {
                                    fsAmount = zcAmount;
                                    jfdf = "贷";
                                }
                                if (zcAmount == 0) {
                                    fsAmount = srAmount;
                                }

                                String flag = "0";
                                if (fsAmount != 0.0) {
                                    if ("借".equals(jfdf)) {
                                        flag = "2";
                                    } else {
                                        flag = "1";
                                    }
                                }
                                String subjectInfo = getSubject(jyyt, null, dfhm, flag, account.getAccountID(), busDate,
                                        "中国农业银行");
                                if (subjectInfo != null) {
                                    Map<String, Object> para = new HashMap<String, Object>();
                                    para.put("accountID", account.getAccountID());
                                    para.put("period", busDate);
                                    String[] subjectInfos = subjectInfo.split("_");
                                    String subCode = null;
                                    String subName = null;
                                    if (subjectInfos.length == 2) {
                                        para.put("subCode", subjectInfos[0]);
                                        List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper
                                                .querySubject(para);
                                        if (subList != null) {
                                            List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                            if (li != null && li.size() == 1) {
                                                subCode = li.get(0).getSubCode();
                                                subName = li.get(0).getSubName();
                                            }
                                        }
                                    } else {
                                        subCode = subjectInfos[0];
                                        subName = subjectInfos[subjectInfos.length - 1];
                                    }

                                    para.put("subCode", subCode);
                                    para.put("subName", subName);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                                    if (subList != null && subList.size() == 1) {
                                        nn.setSysSubjectID(subList.get(0).getPkSubId());
                                        nn.setSysSubjectCode(subList.get(0).getSubCode());
                                        nn.setSysSubjectName(subList.get(0).getSubName());
                                        nn.setSysSubjectFullName(subList.get(0).getFullName());
                                    }
                                }
                                nn.setBankAccount(bankAccount);
                                nyBankDao.insertNyBankNew(nn);
                            }
                        }
                    }
                }

                /************ 农业银行导入 **************/

                // 平安银行
                else if (s.equals("交易日期")) {

                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    if (list != null && list.size() > 0) {
                        for (int j = 1; j < list.size(); j++) {
                            Map<String, Object> map = list.get(j);
                            // 交易流水号
                            String transactionID = StringUtil.objToStr(map.get("map7"));
                            if (transactionID == null) {
                                continue;
                            }
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("period", busDate);
                            param.put("transactionID", transactionID);
                            List<PaBank> bankList = paBankDao.querySame(param);
                            if (null != bankList && bankList.size() > 0) {
                                result.put("success", "fail");
                                result.put("message", "数据不允许重复导入");
                                return result;
                            }
                        }

                        // 账套ID
                        // String accountID = account.getAccountID();
                        for (int k = 1; k < list.size(); k++) {
                            Map<String, Object> map = list.get(k);
                            String paBillID = UUIDUtils.getUUID();
                            PaBank paBank = new PaBank();
                            paBank.setPaBillID(paBillID);
                            paBank.setAccountID(accountID);
                            paBank.setPeriod(busDate);
                            paBank.setVouchID(null);
                            // 交易日期
                            paBank.setTransaction_time(DateUtil.fomatToDate5((String) map.get("map0")));
                            // 账号
                            paBank.setAccountNumber(StringUtil.objToStr(map.get("map1")));

                            String bankAccount = StringUtil.objToStr(map.get("map1"));
                            if (null != bankAccount && !"".equals(bankAccount)) {
                                // 创建科目
                                Map<String, Object> pa1 = new HashMap<String, Object>();
                                pa1.put("accountID", accountID);
                                pa1.put("busDate", busDate);
                                pa1.put("bankAccount", bankAccount);
                                List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                                String subID = null;
                                String subject = null;
                                if (li1 == null || li1.isEmpty()) {

                                    subject = vatService.getNumber("1002", "7", "1002000");
                                    SubjectMessage sm = vatService.createSub(subject, "1002", "平安银行" + bankAccount,
                                            "银行存款_" + "平安银行" + bankAccount);
                                    subID = sm.getPk_sub_id();
                                } else {
                                    subID = li1.get(0).getPkSubId();
                                    subject = li1.get(0).getSubCode();
                                }
                                // 自动创建银行账户和科目的映射
                                Bank2Subject b2s = new Bank2Subject();
                                // 主键
                                b2s.setId(UUIDUtils.getUUID());
                                // 账套ID
                                b2s.setAccountID(accountID);
                                // 银行账户
                                b2s.setBankAccount(bankAccount);
                                // 银行科目主键
                                b2s.setSubID(subID);
                                // 银行名称
                                b2s.setBankName("平安银行");
                                // 银行类型
                                b2s.setBankType("平安银行");
                                // 币种
                                b2s.setCurrency("人民币");
                                // 科目名称
                                b2s.setSubName("平安银行" + bankAccount);
                                // 科目全名
                                b2s.setSubFullName("银行存款_" + "平安银行" + bankAccount);
                                // 科目编码
                                b2s.setSubCode(subject);
                                // 创建者ID
                                b2s.setCreateID(user.getUserID());
                                // 创建者手机号
                                b2s.setCreateTel(user.getLoginUser());
                                // 创建人名字
                                b2s.setCreateName(user.getUserName());
                                // 创建时间
                                b2s.setCreateTime(new Date());
                                Map<String, Object> pa = new HashMap<String, Object>();
                                pa.put("bankAccount", bankAccount);
                                List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                                boolean canAdd = true;
                                if (li == null || li.isEmpty()) {
                                    bank2SubjectDao.addBank2Subject(b2s);
                                } else {
                                    for (int i = 0; i < li.size(); i++) {
                                        if (li.get(i).getAccountID().equals(accountID)) {
                                            canAdd = false;
                                        }
                                    }
                                    if (canAdd) {
                                        bank2SubjectDao.addBank2Subject(b2s);
                                    }
                                }
                            } else {
                                throw new BusinessException("请检查您导入的平安银行对账单,请先完善银行账户信息");
                            }
                            // 借方发生额
                            BigDecimal debitAmount = StringUtil.objToBigDecimal(map.get("map2"));
                            if (debitAmount == null) {
                                debitAmount = new BigDecimal("0.00");
                            }
                            paBank.setDebitAmount(debitAmount);
                            // 贷方发生额
                            BigDecimal creditAmount = StringUtil.objToBigDecimal(map.get("map3"));
                            if (creditAmount == null) {
                                creditAmount = new BigDecimal("0.00");
                            }
                            paBank.setCreditAmount(creditAmount);
                            // 账户余额
                            paBank.setBalance(StringUtil.objToBigDecimal(map.get("map4")));
                            // 对方账号
                            paBank.setDfAccountNumber(StringUtil.objToStr(map.get("map5")));
                            // 对方账户名称
                            paBank.setDfAccountName(StringUtil.objToStr(map.get("map6")));
                            // 交易流水号
                            paBank.setTransactionID(StringUtil.objToStr(map.get("map7")));
                            // 摘要
                            paBank.setReference(StringUtil.objToStr(map.get("map9")));
                            // 用途
                            paBank.setPurpose(StringUtil.objToStr(map.get("map10")));
                            // 导入时间
                            paBank.setImportDate(System.currentTimeMillis() + k);
                            paBank.setBankType(Constrants.BANK_NAME_PA);
                            String flag = "0";
                            if (debitAmount.compareTo(BigDecimal.ZERO) != 0) {
                                flag = "1";
                            }
                            if (creditAmount.compareTo(BigDecimal.ZERO) != 0) {
                                flag = "2";
                            }
                            String subjectInfo = getSubject(StringUtil.objToStr(map.get("map9")),
                                    StringUtil.objToStr(map.get("map10")), StringUtil.objToStr(map.get("map6")), flag,
                                    account.getAccountID(), busDate, "平安银行");
                            if (subjectInfo != null) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    param.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    paBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    paBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    paBank.setSysSubjectName(subList.get(0).getSubName());
                                    paBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            paBankDao.insertPaBank(paBank);
                        }
                    }

                } else if ("招商银行".equals(s)) {
                    // 招商银行
                    List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 0, 0);
                    String bankAccount = list.get(1).get("map5").toString();
                    if (null != bankAccount && !"".equals(bankAccount)) {
                        // 创建科目

                        Map<String, Object> pa1 = new HashMap<String, Object>();
                        pa1.put("accountID", accountID);
                        pa1.put("busDate", busDate);
                        pa1.put("bankAccount", bankAccount);
                        List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                        String subID = null;
                        String subject = null;
                        if (li1 == null || li1.isEmpty()) {

                            subject = vatService.getNumber("1002", "7", "1002000");
                            SubjectMessage sm = vatService.createSub(subject, "1002", "招商银行" + bankAccount,
                                    "银行存款_" + "招商银行" + bankAccount);
                            subID = sm.getPk_sub_id();
                        } else {
                            subID = li1.get(0).getPkSubId();
                            subject = li1.get(0).getSubCode();
                        }
                        // 自动创建银行账户和科目的映射
                        Bank2Subject b2s = new Bank2Subject();
                        // 主键
                        b2s.setId(UUIDUtils.getUUID());
                        // 账套ID
                        b2s.setAccountID(accountID);
                        // 银行账户
                        b2s.setBankAccount(bankAccount);
                        // 银行科目主键
                        b2s.setSubID(subID);
                        // 银行名称
                        b2s.setBankName("招商银行");
                        // 银行类型
                        b2s.setBankType("招商银行");
                        // 币种
                        b2s.setCurrency("人民币");
                        // 科目名称
                        b2s.setSubName("招商银行" + bankAccount);
                        // 科目全名
                        b2s.setSubFullName("银行存款_" + "招商银行" + bankAccount);
                        // 科目编码
                        b2s.setSubCode(subject);
                        // 创建者ID
                        b2s.setCreateID(user.getUserID());
                        // 创建者手机号
                        b2s.setCreateTel(user.getLoginUser());
                        // 创建人名字
                        b2s.setCreateName(user.getUserName());
                        // 创建时间
                        b2s.setCreateTime(new Date());
                        Map<String, Object> pa = new HashMap<String, Object>();
                        pa.put("bankAccount", bankAccount);
                        List<Bank2Subject> li = bank2SubjectDao.queryByBankAccount(pa);
                        boolean canAdd = true;
                        if (li == null || li.isEmpty()) {
                            bank2SubjectDao.addBank2Subject(b2s);
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                if (li.get(i).getAccountID().equals(accountID)) {
                                    canAdd = false;
                                }
                            }
                            if (canAdd) {
                                bank2SubjectDao.addBank2Subject(b2s);
                            }
                        }
                    } else {
                        throw new BusinessException("请检查您导入的招商银行对账单,请先完善银行账户信息");
                    }
                    System.out.println(bankAccount);
                    // String bankAccount = excelReader.readExcelContent(is, 3,
                    // 5);
                    // System.out.println(bankAccount);
                    if (list != null && list.size() > 0) {
                        Map<String, Object> s1 = list.get(6);
                        // Map<String, Object> s2 = list.get(7);
                        for (int j = 7; j < list.size(); j++) {
                            Map<String, Object> map = list.get(j);
                            // 交易流水号
                            String transactionID = StringUtil.objToStr(map.get("map8"));
                            if (transactionID == null) {
                                continue;
                            }
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("period", busDate);
                            param.put("transactionID", transactionID);
                            List<ZsBank> bankList = zsBankDao.querySame(param);
                            if (null != bankList && bankList.size() > 0) {
                                result.put("success", "fail");
                                result.put("message", "数据不允许重复导入");
                                return result;
                            }
                        }

                        // 账套ID
                        // String accountID = account.getAccountID();
                        for (int k = 7; k < list.size(); k++) {
                            Map<String, Object> map = list.get(k);
                            String paBillID = UUIDUtils.getUUID();
                            ZsBank zsBank = new ZsBank();
                            zsBank.setZsBillID(paBillID);
                            zsBank.setAccountID(accountID);
                            zsBank.setPeriod(busDate);
                            zsBank.setVouchID(null);
                            // 交易日期
                            zsBank.setTransaction_time(DateUtil.fomatToDate5((String) map.get("map0")));
                            // 交易时间
                            zsBank.setTransaction_MinutesSeconds(StringUtil.objToStr(map.get("map1")));
                            // 起息日
                            zsBank.setFromTheDay(DateUtil.fomatToDate5((String) map.get("map2")));
                            // 交易类型
                            zsBank.setTransactionType(StringUtil.objToStr(map.get("map3")));
                            // 借方发生额
                            zsBank.setDebitAmount(StringUtil.objToBigDecimal(map.get("map4")));
                            // 贷方发生额
                            zsBank.setCreditAmount(StringUtil.objToBigDecimal(map.get("map5")));
                            // 余额
                            zsBank.setBalance(StringUtil.objToBigDecimal(map.get("map6")));
                            // 摘要
                            zsBank.setReference(StringUtil.objToStr(map.get("map7")));
                            // 交易流水号
                            zsBank.setTransactionID(StringUtil.objToStr(map.get("map8")));
                            // 流程实例号
                            zsBank.setProcessNumber(StringUtil.objToStr(map.get("map9")));
                            // 业务名称
                            zsBank.setBusinessName(StringUtil.objToStr(map.get("map10")));
                            // 用途
                            zsBank.setPurpose(StringUtil.objToStr(map.get("map11")));
                            // 业务参考号
                            zsBank.setBusinessReferenceNumber(StringUtil.objToStr(map.get("map12")));
                            // 业务摘要
                            zsBank.setBusinessReference(StringUtil.objToStr(map.get("map13")));
                            // 其它摘要
                            zsBank.setOtherReference(StringUtil.objToStr(map.get("map14")));
                            // 收/付方分行名
                            zsBank.setReceiverPayBranchName(StringUtil.objToStr(map.get("map15")));
                            // 收/付方名称
                            zsBank.setReceiverPayName(StringUtil.objToStr(map.get("map16")));
                            // 收/付方帐号
                            zsBank.setReceiverPayAccount(StringUtil.objToStr(map.get("map17")));
                            // 收/付方开户行行号
                            zsBank.setReceiverPayBankNumber(StringUtil.objToStr(map.get("map18")));
                            // 收/付方开户行名
                            zsBank.setReceiverPayBankName(StringUtil.objToStr(map.get("map19")));
                            // 收/付方开户行地址
                            zsBank.setReceiverPayBankAddress(StringUtil.objToStr(map.get("map20")));
                            // 母/子公司帐号分行名'
                            zsBank.setParentChildAccountName(StringUtil.objToStr(map.get("map21")));
                            // 母/子公司帐号
                            zsBank.setParentChildAccount(StringUtil.objToStr(map.get("map22")));
                            // 母/子公司名称
                            zsBank.setParentChildName(StringUtil.objToStr(map.get("map23")));
                            // 信息标志
                            zsBank.setInformationSign(StringUtil.objToStr(map.get("map24")));
                            // 有否附件信息
                            zsBank.setIsAnnex(StringUtil.objToStr(map.get("map25")));
                            // 冲帐标志
                            zsBank.setImprintSign(StringUtil.objToStr(map.get("map26")));
                            // 扩展摘要
                            zsBank.setExpandReference(StringUtil.objToStr(map.get("map27")));
                            // 交易分析码
                            zsBank.setTransactionAnalysisCode(StringUtil.objToStr(map.get("map28")));
                            // 票据号
                            zsBank.setTicketNumber(StringUtil.objToStr(map.get("map29")));
                            // 商务支付订单号
                            zsBank.setBusinessPaymentNumber(StringUtil.objToStr(map.get("map30")));
                            // 内部编号
                            zsBank.setInternalNumber(StringUtil.objToStr(map.get("map31")));
                            // 导入时间
                            zsBank.setImportDate(System.currentTimeMillis() + k);
                            zsBank.setBankType(Constrants.BANK_NAME_ZS);
                            String flag = "0";
                            BigDecimal debitAmount = new BigDecimal(StringUtil.objToStr(map.get("map4")) != null
                                    ? StringUtil.objToStr(map.get("map4")) : "0");
                            if (null != debitAmount && debitAmount.compareTo(BigDecimal.ZERO) != 0) {

                                flag = "1";
                            }
                            BigDecimal creditAmount = new BigDecimal(StringUtil.objToStr(map.get("map5")) != null
                                    ? StringUtil.objToStr(map.get("map5")) : "0");
                            if (null != creditAmount && creditAmount.compareTo(BigDecimal.ZERO) != 0) {

                                flag = "2";
                            }
                            String subjectInfo = getSubject(StringUtil.objToStr(map.get("map7")),
                                    StringUtil.objToStr(map.get("map13")), StringUtil.objToStr(map.get("map16")), flag,
                                    account.getAccountID(), busDate, "招商银行");
                            if (subjectInfo != null) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("accountID", account.getAccountID());
                                param.put("period", busDate);
                                String[] subjectInfos = subjectInfo.split("_");
                                String subCode = null;
                                String subName = null;
                                if (subjectInfos.length == 2) {
                                    param.put("subCode", subjectInfos[0]);
                                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                    if (subList != null) {
                                        List<TBasicSubjectMessage> li = SubjectUtils.getMjSub(subList);
                                        if (li != null && li.size() == 1) {
                                            subCode = li.get(0).getSubCode();
                                            subName = li.get(0).getSubName();
                                        }
                                    }
                                } else {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[subjectInfos.length - 1];
                                }

                                param.put("subCode", subCode);
                                param.put("subName", subName);
                                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(param);
                                if (subList != null && subList.size() == 1) {
                                    zsBank.setSysSubjectID(subList.get(0).getPkSubId());
                                    zsBank.setSysSubjectCode(subList.get(0).getSubCode());
                                    zsBank.setSysSubjectName(subList.get(0).getSubName());
                                    zsBank.setSysSubjectFullName(subList.get(0).getFullName());
                                }
                            }
                            zsBank.setBankAccount(bankAccount);
                            zsBankDao.insertZsBank(zsBank);
                        }
                    }

                } else {
                    result.put("success", "fail");
                    result.put("info", "请仔细检查银行模板!");
                    return result;
                }
            }
            result.put("success", "true");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }

    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void deleteByID(String billIDs) throws BusinessException {
        try {
            String bills = "('" + billIDs.replaceAll("\\,", "','") + "')";
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("bills", bills);
            // 删除中信银行对账单
            tccbBankDao.deleteByID(param);
            // 删除深农商行银行对账单
            szrcbBankBillDao.deleteByID(param);
            // 删除交通银行对账单
            bcmDao.deleteByID(param);
            bcm1Dao.deleteByID(param);
            // 删除工商银行对账单
            icbcBankDao.deleteByID(param);
            // 删除建设银行对账单
            jsBankDao.deleteByID(param);
            // 删除招行对账单
            zsBankDao.deleteByID(param);
            // 删除平安银行对账单
            paBankDao.deleteByID(param);
            // 删除中国银行对账单
            tCmBkbillBocMapper.deleteByID(param);
            // 删除农业银行对账单
            nyBankDao.deleteByID(param);
            nyBankDao.deleteNewByID(param);

        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public List<BankBill> queryBankBill(Map<String, Object> param) throws BusinessException {
        try {
            String bankType = param.get("bankType").toString();
            Object curPageObj = param.get("curPage");
            Integer maxPage = Integer.parseInt(param.get("maxPage").toString().trim());
            Integer curPage = null;
            if (null != curPageObj) {
                curPage = Integer.parseInt(curPageObj.toString());
                param.put("begin", (curPage - 1) * maxPage);
            }
            List<BankBill> bankList = new ArrayList<BankBill>();
            // List<BankBill> returnList = new ArrayList<BankBill>();
            if ("全部".equals(bankType) || "中信银行".equals(bankType)) {
                // 添加中信银行
                List<TccbBank> ccbList = tccbBankDao.queryBankBill(param);
                if (null != ccbList && ccbList.size() > 0) {
                    for (int i = 0; i < ccbList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        TccbBank tccbBank = ccbList.get(i);
                        bankBill.setId(tccbBank.getZxBillID());
                        bankBill.setAccountID(tccbBank.getAccountID());
                        bankBill.setPeriod(tccbBank.getPeriod());
                        bankBill.setVouchID(tccbBank.getVouchID());
                        bankBill.setBusDate(tccbBank.getBusDate());
                        bankBill.setTransactionDate(tccbBank.getTransactionDate());
                        bankBill.setDes(tccbBank.getDes());
                        bankBill.setDebitAmount(tccbBank.getDebitAmount());
                        bankBill.setCreditAmount(tccbBank.getCreditAmount());
                        bankBill.setAccountBalance(tccbBank.getAccountBalance());
                        bankBill.setDfAccountName(tccbBank.getWhAccountName());
                        bankBill.setDfAccountNumber(tccbBank.getAccountNO());
                        bankBill.setBz(tccbBank.getBz());
                        bankBill.setBankType(tccbBank.getBankType());
                        bankBill.setIntBankType(1);
                        bankBill.setSubID(tccbBank.getSysSubjectID());
                        bankBill.setSubName(tccbBank.getSysSubjectName());
                        bankBill.setSubCode(tccbBank.getSysSubjectCode());
                        bankBill.setSubFullName(tccbBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            if ("全部".equals(bankType) || "深圳农商行".equals(bankType)) {
                // 添加深农商行
                List<SzrcbBank> szrcbList = szrcbBankBillDao.queryBankBill(param);
                if (null != szrcbList && szrcbList.size() > 0) {
                    for (int i = 0; i < szrcbList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        SzrcbBank szrcbBank = szrcbList.get(i);
                        bankBill.setId(szrcbBank.getSzrcbBillID());
                        bankBill.setAccountID(szrcbBank.getAccountID());
                        bankBill.setPeriod(szrcbBank.getPeriod());
                        bankBill.setVouchID(szrcbBank.getVouchID());
                        bankBill.setBusDate(szrcbBank.getBusDate());
                        bankBill.setTransactionDate(szrcbBank.getTransactionDate());
                        bankBill.setDes(szrcbBank.getDes());
                        bankBill.setDebitAmount(szrcbBank.getDebitAmount());
                        bankBill.setCreditAmount(szrcbBank.getCreditAmount());
                        bankBill.setAccountBalance(szrcbBank.getAccountBalance());
                        bankBill.setDfAccountName(szrcbBank.getDfAccountName());
                        bankBill.setDfAccountNumber(szrcbBank.getDfAccountNumber());
                        bankBill.setBz(szrcbBank.getBz());
                        bankBill.setBankType(szrcbBank.getBankType());
                        bankBill.setIntBankType(2);

                        bankBill.setSubID(szrcbBank.getSysSubjectID());
                        bankBill.setSubName(szrcbBank.getSysSubjectName());
                        bankBill.setSubCode(szrcbBank.getSysSubjectCode());
                        bankBill.setSubFullName(szrcbBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            // 添加工商银行
            if ("全部".equals(bankType) || "工商银行".equals(bankType)) {
                List<IcbcBank> icbcList = icbcBankDao.queryBankBill(param);
                if (null != icbcList && icbcList.size() > 0) {
                    for (int i = 0; i < icbcList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        IcbcBank icbcBank = icbcList.get(i);
                        bankBill.setId(icbcBank.getIcbcBillID());
                        bankBill.setAccountID(icbcBank.getAccountID());
                        bankBill.setPeriod(icbcBank.getPeriod());
                        bankBill.setVouchID(icbcBank.getVouchID());
                        bankBill.setBusDate(icbcBank.getBusDate());
                        bankBill.setTransactionDate(icbcBank.getRqDate());
                        bankBill.setDes(icbcBank.getDes());
                        bankBill.setDebitAmount(icbcBank.getJffsAmount());
                        bankBill.setCreditAmount(icbcBank.getDffsAmount());
                        bankBill.setAccountBalance(icbcBank.getBalance());
                        bankBill.setDfAccountName(icbcBank.getDfAccountName());
                        bankBill.setDfAccountNumber(icbcBank.getDfAccount());
                        bankBill.setBankType(icbcBank.getBankType());
                        bankBill.setSubID(icbcBank.getSysSubjectID());
                        bankBill.setSubName(icbcBank.getSysSubjectName());
                        bankBill.setSubCode(icbcBank.getSysSubjectCode());
                        // bankBill.setBz(icbcBank.getBz());
                        bankBill.setIntBankType(3);

                        bankBill.setSubID(icbcBank.getSysSubjectID());
                        bankBill.setSubName(icbcBank.getSysSubjectName());
                        bankBill.setSubCode(icbcBank.getSysSubjectCode());
                        bankBill.setSubFullName(icbcBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            // 添加交通银行 t_cm_bkbill_bcm
            if ("全部".equals(bankType) || "交通银行".equals(bankType)) {
                List<BcmBank> bcmList = bcmDao.queryBankBill(param);
                if (null != bcmList && bcmList.size() > 0) {
                    for (int i = 0; i < bcmList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        BcmBank bcmBank = bcmList.get(i);
                        bankBill.setId(bcmBank.getBcmBillID());
                        bankBill.setAccountID(bcmBank.getAccountID());
                        bankBill.setPeriod(bcmBank.getPeriod());
                        bankBill.setVouchID(bcmBank.getVouchID());
                        bankBill.setBusDate(bcmBank.getBusDate());
                        bankBill.setTransactionDate(bcmBank.getTransactionTime());
                        bankBill.setDes(bcmBank.getDes());
                        String jdFlag = bcmBank.getJdFlag();
                        if ("借".equals(jdFlag)) {
                            bankBill.setDebitAmount(bcmBank.getFse());
                            bankBill.setCreditAmount(0.0);
                        } else if ("贷".equals(jdFlag)) {
                            bankBill.setCreditAmount(bcmBank.getFse());
                            bankBill.setDebitAmount(0.0);
                        }
                        bankBill.setAccountBalance(bcmBank.getBanalce());
                        bankBill.setDfAccountName(bcmBank.getDfAccountName());
                        bankBill.setDfAccountNumber(bcmBank.getDfAccount());
                        bankBill.setBankType(bcmBank.getBankType());
                        // bankBill.setBz(icbcBank.getBz());
                        bankBill.setIntBankType(4);
                        bankBill.setSubID(bcmBank.getSysSubjectID());
                        bankBill.setSubName(bcmBank.getSysSubjectName());
                        bankBill.setSubCode(bcmBank.getSysSubjectCode());
                        bankBill.setSubFullName(bcmBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);

                    }
                }

                // 添加交通银行 t_cm_bkbill_bcm1
                List<BcmBank1> bcmList1 = bcm1Dao.queryBankBill(param);
                if (null != bcmList1 && bcmList1.size() > 0) {
                    for (int i = 0; i < bcmList1.size(); i++) {
                        BankBill bankBill = new BankBill();
                        BcmBank1 bcmBank = bcmList1.get(i);
                        bankBill.setId(bcmBank.getBcmBillID());
                        bankBill.setAccountID(bcmBank.getAccountID());
                        bankBill.setPeriod(bcmBank.getPeriod());
                        bankBill.setVouchID(bcmBank.getVouchID());
                        bankBill.setBusDate(bcmBank.getBusDate());
                        bankBill.setTransactionDate(bcmBank.getTransactionTime());
                        bankBill.setDes(bcmBank.getDes());
                        String jdFlag = bcmBank.getJdFlag();
                        if ("借".equals(jdFlag)) {
                            bankBill.setDebitAmount(bcmBank.getZzje());
                            bankBill.setCreditAmount(0.0);
                        } else if ("贷".equals(jdFlag)) {
                            bankBill.setCreditAmount(bcmBank.getZzje());
                            bankBill.setDebitAmount(0.0);
                        }
                        bankBill.setAccountBalance(bcmBank.getBanalce());
                        bankBill.setDfAccountName(bcmBank.getDfAccountName());
                        bankBill.setDfAccountNumber(bcmBank.getDfAccount());
                        bankBill.setBankType(bcmBank.getBankType());
                        // bankBill.setBz(icbcBank.getBz());
                        bankBill.setIntBankType(5);

                        bankBill.setSubID(bcmBank.getSysSubjectID());
                        bankBill.setSubName(bcmBank.getSysSubjectName());
                        bankBill.setSubCode(bcmBank.getSysSubjectCode());
                        bankBill.setSubFullName(bcmBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }

            // 添加建设银行
            if ("全部".equals(bankType) || "建设银行".equals(bankType)) {
                List<JsBank> jsList = jsBankDao.queryBankBill(param);
                if (null != jsList && jsList.size() > 0) {
                    for (int i = 0; i < jsList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        JsBank jsBank = jsList.get(i);
                        bankBill.setId(jsBank.getJsBillID());
                        bankBill.setAccountID(jsBank.getAccountID());
                        bankBill.setPeriod(jsBank.getPeriod());
                        bankBill.setVouchID(jsBank.getVouchID());
                        bankBill.setBusDate(jsBank.getImportDate());
                        bankBill.setTransactionDate(jsBank.getTransaction_date());
                        bankBill.setDes(jsBank.getReference());
                        bankBill.setAccountBalance(jsBank.getBalance() == null ? 0 : jsBank.getBalance());
                        if (null == jsBank.getDfAccountName() || "null".equals(jsBank.getDfAccountName())) {
                            bankBill.setDfAccountName("");
                        } else {
                            bankBill.setDfAccountName(jsBank.getDfAccountName());
                        }
                        bankBill.setDfAccountNumber(jsBank.getDfAccountNumber());
                        if (null != jsBank.getJffsAmount()) {
                            bankBill.setDebitAmount(jsBank.getJffsAmount());
                        } else {
                            bankBill.setDebitAmount(new Double(0));
                        }
                        if (null != jsBank.getDffsAmount()) {
                            bankBill.setCreditAmount(jsBank.getDffsAmount());
                        } else {
                            bankBill.setCreditAmount(new Double(0));
                        }
                        bankBill.setBankType(jsBank.getBankType());
                        bankBill.setIntBankType(6);

                        bankBill.setSubID(jsBank.getSysSubjectID());
                        bankBill.setSubName(jsBank.getSysSubjectName());
                        bankBill.setSubCode(jsBank.getSysSubjectCode());
                        bankBill.setSubFullName(jsBank.getSysSubjectFullName());
                        bankBill.setBz(jsBank.getRemarks());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            // 平安银行分页
            if ("全部".equals(bankType) || "平安银行".equals(bankType)) {
                List<PaBank> paList = paBankDao.queryBankBill(param);
                if (null != paList && paList.size() > 0) {
                    for (int i = 0; i < paList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        PaBank paBank = paList.get(i);
                        bankBill.setId(paBank.getPaBillID());
                        bankBill.setAccountID(paBank.getAccountID());
                        bankBill.setPeriod(paBank.getPeriod());
                        bankBill.setVouchID(paBank.getVouchID());
                        bankBill.setBusDate(paBank.getImportDate());
                        bankBill.setTransactionDate(paBank.getTransaction_time());
                        String zy = paBank.getPurpose();
                        if (null == zy) {
                            zy = "";
                        }
                        bankBill.setDes(zy);
                        bankBill.setAccountBalance(StringUtil.bigDecimalToDou(
                                paBank.getBalance() == null ? new BigDecimal(0) : paBank.getBalance()));
                        if (null == paBank.getDfAccountName() || "null".equals(paBank.getDfAccountName())) {
                            bankBill.setDfAccountName("");
                        } else {
                            bankBill.setDfAccountName(paBank.getDfAccountName());
                        }
                        bankBill.setDfAccountNumber(paBank.getDfAccountNumber());
                        bankBill.setDebitAmount(StringUtil.bigDecimalToDou(paBank.getDebitAmount()));
                        bankBill.setCreditAmount(StringUtil.bigDecimalToDou(paBank.getCreditAmount()));
                        bankBill.setBankType(Constrants.BANK_NAME_PA);
                        bankBill.setIntBankType(7);

                        bankBill.setSubID(paBank.getSysSubjectID());
                        bankBill.setSubName(paBank.getSysSubjectName());
                        bankBill.setSubCode(paBank.getSysSubjectCode());
                        bankBill.setSubFullName(paBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            // 招商银行分页
            if ("全部".equals(bankType) || "招商银行".equals(bankType)) {
                List<ZsBank> zsList = zsBankDao.queryBankBill(param);
                if (null != zsList && zsList.size() > 0) {
                    for (int i = 0; i < zsList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        ZsBank zsBank = zsList.get(i);
                        bankBill.setId(zsBank.getZsBillID());
                        bankBill.setAccountID(zsBank.getAccountID());
                        bankBill.setPeriod(zsBank.getPeriod());
                        bankBill.setVouchID(zsBank.getVouchID());
                        bankBill.setBusDate(zsBank.getImportDate());
                        bankBill.setTransactionDate(zsBank.getTransaction_time());
                        String reference = zsBank.getReference();
                        if (null == reference) {
                            bankBill.setDes("");
                        } else {
                            bankBill.setDes(zsBank.getReference());
                        }
                        bankBill.setAccountBalance(StringUtil.bigDecimalToDou(
                                zsBank.getBalance() == null ? new BigDecimal(0) : zsBank.getBalance()));
                        if (null == zsBank.getReceiverPayName() || "null".equals(zsBank.getReceiverPayName())) {
                            bankBill.setDfAccountName("");
                        } else {
                            bankBill.setDfAccountName(zsBank.getReceiverPayName());
                        }
                        bankBill.setDfAccountNumber(zsBank.getReceiverPayAccount());
                        if (zsBank.getDebitAmount() == null) {
                            bankBill.setDebitAmount(0.0);
                        } else {
                            bankBill.setDebitAmount(StringUtil.bigDecimalToDou(zsBank.getDebitAmount()));
                        }

                        if (zsBank.getCreditAmount() == null) {
                            bankBill.setCreditAmount(0.0);
                        } else {
                            bankBill.setCreditAmount(StringUtil.bigDecimalToDou(zsBank.getCreditAmount()));
                        }
                        bankBill.setBankType(Constrants.BANK_NAME_ZS);
                        bankBill.setIntBankType(8);

                        bankBill.setSubID(zsBank.getSysSubjectID());
                        bankBill.setSubName(zsBank.getSysSubjectName());
                        bankBill.setSubCode(zsBank.getSysSubjectCode());
                        bankBill.setSubFullName(zsBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }
            if ("全部".equals(bankType) || "中国银行".equals(bankType)) {
                // 中国银行分页
                List<TCmBkbillBoc> zgList = tCmBkbillBocMapper.queryBankBill(param);
                if (null != zgList && zgList.size() > 0) {
                    for (int i = 0; i < zgList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        TCmBkbillBoc zgBank = zgList.get(i);
                        bankBill.setId(zgBank.getPkBkbillBoc());
                        bankBill.setAccountID(zgBank.getAccountId());
                        bankBill.setPeriod(zgBank.getPeriod());
                        bankBill.setVouchID(zgBank.getVouchID());
                        bankBill.setBusDate(zgBank.getAccountDate());// 做帐时间
                        bankBill.setTransactionDate(zgBank.getTransactionDate());
                        bankBill.setDes(zgBank.getReference());
                        if (null != zgBank.getAfterTransactionBalance()) {
                            bankBill.setAccountBalance(
                                    StringUtil.objToDouble(zgBank.getAfterTransactionBalance() == null ? 0.0
                                            : zgBank.getAfterTransactionBalance()));// 交易后余额
                        } else {
                            bankBill.setAccountBalance(0.0);
                        }

                        bankBill.setDfAccountName(zgBank.getPayerName());
                        bankBill.setDfAccountNumber(zgBank.getDebitAccountNo());
                        if (null != zgBank.getTradeAmount() && !zgBank.getTradeAmount().contains("-")) {
                            bankBill.setDebitAmount(StringUtil.objToDouble(zgBank.getTradeAmount()));
                        } else {
                            bankBill.setDebitAmount(0.0);
                        }
                        if (null != zgBank.getTradeAmount() && zgBank.getTradeAmount().contains("-")) {
                            bankBill.setCreditAmount(
                                    StringUtil.objToDouble(zgBank.getTradeAmount().replaceAll("-", "")));
                        } else {
                            bankBill.setCreditAmount(0.0);
                        }
                        bankBill.setBz(zgBank.getRemark());
                        bankBill.setBankType(Constrants.BANK_NAME_ZG);
                        bankBill.setIntBankType(9);

                        bankBill.setSubID(zgBank.getSysSubjectID());
                        bankBill.setSubName(zgBank.getSysSubjectName());
                        bankBill.setSubCode(zgBank.getSysSubjectCode());
                        bankBill.setSubFullName(zgBank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }

            /********************************
             * 农业银行start
             *************************************/
            if ("全部".equals(bankType) || "中国农业银行".equals(bankType)) {
                // 农业分页
                List<NyBank> nyList = nyBankDao.queryBankBill(param);
                if (null != nyList && nyList.size() > 0) {
                    for (int i = 0; i < nyList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        NyBank bank = nyList.get(i);
                        bankBill.setId(bank.getNyBillID());
                        bankBill.setAccountID(bank.getAccountID());
                        bankBill.setPeriod(bank.getPeriod());
                        bankBill.setVouchID(bank.getVouchID());
                        bankBill.setBusDate(bank.getAddTime());// 做帐时间
                        bankBill.setTransactionDate(bank.getTransaction_time());
                        String desc = bank.getNotes() == null ? "" : bank.getNotes().trim();
                        while (desc.endsWith("　")) {
                            desc = desc.substring(0, desc.length() - 1).trim();
                        }
                        bankBill.setDes(desc);
                        bankBill.setDfAccountName(bank.getDfAccountName() == null ? "" : bank.getDfAccountName());
                        bankBill.setDfAccountNumber(bank.getDfAccountNumber() == null ? "" : bank.getDfAccountName());
                        bankBill.setAccountBalance(StringUtil.bigDecimalIsNull(bank.getAccountBalance()).doubleValue());
                        String jfdf = bank.getJfdf();
                        BigDecimal fsAmount = bank.getFsAmount();
                        if ("借".equals(jfdf)) {
                            bankBill.setDebitAmount(StringUtil.bigDecimalIsNull(fsAmount).doubleValue());
                            bankBill.setCreditAmount(0.0);
                        } else {
                            bankBill.setDebitAmount(0.0);
                            bankBill.setCreditAmount(StringUtil.bigDecimalIsNull(fsAmount).doubleValue());
                        }
                        bankBill.setBankType(Constrants.BANK_NAME_NY);
                        bankBill.setIntBankType(10);

                        bankBill.setSubID(bank.getSysSubjectID());
                        bankBill.setSubName(bank.getSysSubjectName());
                        bankBill.setSubCode(bank.getSysSubjectCode());
                        bankBill.setSubFullName(bank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        String reference = bank.getReference();
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (null != reference && (reference.contains("社保") || reference.contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (null != reference && (reference.contains("公积金") || reference.contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }

                        bankList.add(bankBill);
                    }
                }
                List<NyBankNew> nyNewList = nyBankDao.queryBankBillNew(param);
                if (null != nyNewList && nyNewList.size() > 0) {
                    for (int i = 0; i < nyNewList.size(); i++) {
                        BankBill bankBill = new BankBill();
                        NyBankNew bank = nyNewList.get(i);
                        bankBill.setId(bank.getId());
                        bankBill.setAccountID(bank.getAccountID());
                        bankBill.setPeriod(bank.getPeriod());
                        bankBill.setVouchID(bank.getVouchID());
                        bankBill.setBusDate(bank.getAddTime());// 做帐时间
                        bankBill.setTransactionDate(bank.getTransactionTime());
                        if (null == bank.getJyzy()) {
                            bankBill.setDes("");
                        } else {
                            bankBill.setDes(bank.getJyzy().trim());
                        }
                        bankBill.setDfAccountName(bank.getDfhm() == null ? "" : bank.getDfhm());
                        bankBill.setDfAccountNumber(bank.getDfzh());
                        if (bank.getZhAmount() != 0) {
                            bankBill.setAccountBalance(bank.getZhAmount());
                        } else if (bank.getBcAmount() != 0) {
                            bankBill.setAccountBalance(bank.getBcAmount());
                        } else {
                            bankBill.setAccountBalance(new Double(0));
                        }
                        bankBill.setDebitAmount(bank.getSrAmount());
                        bankBill.setCreditAmount(bank.getZcAmount());
                        bankBill.setBankType(Constrants.BANK_NAME_NY);
                        bankBill.setIntBankType(11);

                        bankBill.setSubID(bank.getSysSubjectID());
                        bankBill.setSubName(bank.getSysSubjectName());
                        bankBill.setSubCode(bank.getSysSubjectCode());
                        bankBill.setSubFullName(bank.getSysSubjectFullName());
                        bankBill.setIsSb(false);
                        bankBill.setIsGjj(false);
                        if (bankBill.getDes() != null
                                && (bankBill.getDes().contains("社保") || bankBill.getDes().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("社保") || bankBill.getBz().contains("社会保险"))) {
                            bankBill.setIsSb(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getBz().contains("公积金") || bankBill.getBz().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        } else if (bankBill.getBz() != null
                                && (bankBill.getDes().contains("公积金") || bankBill.getDes().contains("公共积金"))) {
                            bankBill.setIsGjj(true);
                        }
                        bankList.add(bankBill);
                    }
                }
            }

            /******************************** 农业银行end *************************************/
            return bankList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }

    // 银行分页
    @Override
    public void queryPage(Map<String, Object> param) {
        Object curPageObj = param.get("curPage");
        Integer maxPage = Integer.parseInt(param.get("maxPage").toString().trim());
        Integer curPage = null;
        if (null != curPageObj) {
            curPage = Integer.parseInt(curPageObj.toString());
            param.put("begin", (curPage - 1) * maxPage);
        }
        List<TCmBkbillBoc> zgList = null;
        try {
            zgList = tCmBkbillBocMapper.queryBankBill(param);
            if (null != zgList && zgList.size() > 0) {
                for (int i = 0; i < zgList.size(); i++) {
                    BankBill bankBill = new BankBill();
                    TCmBkbillBoc zgBank = zgList.get(i);
                    bankBill.setId(zgBank.getPkBkbillBoc());
                    bankBill.setAccountID(zgBank.getAccountId());
                    bankBill.setPeriod(zgBank.getAccountId());
                    bankBill.setVouchID(zgBank.getVouchID());
                    bankBill.setBusDate(zgBank.getAccountDate());// 做帐时间
                    bankBill.setTransactionDate(zgBank.getTransactionDate());
                    bankBill.setDes(zgBank.getReference());
                    bankBill.setAccountBalance(StringUtil.objToDouble(zgBank.getAfterTransactionBalance() == null
                            ? new BigDecimal(0) : zgBank.getAfterTransactionBalance()));// 交易后余额
                    bankBill.setDfAccountName(zgBank.getPayerName());
                    bankBill.setDfAccountNumber(zgBank.getDebitAccountNo());
                    bankBill.setDebitAmount(StringUtil.objToDouble(zgBank.getTotalNumbersOfDebitedPayments()));
                    bankBill.setCreditAmount(StringUtil.objToDouble(zgBank.getTotalCreditAmountOfPayments()));
                    bankBill.setBankType(Constrants.BANK_NAME_ZG);
                }
            }

        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    /*************** 一键银行凭证创建 satrt ************************************/

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public List<Voucher> bankBill2Vouch(User user, Account account) throws BusinessException {
        List<Voucher> returnList = new ArrayList<Voucher>();
        // boolean isArch = false;
        // boolean isGjj = false;
        // boolean isSb = false;
        int gjjFlag = 1;
        int sbFlag = 1;
        int archFlag = 1;
        double gjjAmount = 0;
        double sbAmount = 0;
        String bankAccountTemp = null;
        try {
            String period = account.getUseLastPeriod();
            Map<String, String> param = new HashMap<String, String>();
            Map<String, Object> upParam = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", period);
            param.put("period", period);
            upParam.putAll(param);
            vatService.subinit(user, account);
            Map<String, Object> p = new HashMap<String, Object>();
            p.put("accountID", account.getAccountID());
            List<Bank2Subject> bsList = bank2SubjectDao.queryBank2Subject(p);
            Map<String, String> bsMap = new HashMap<String, String>();
            if (bsList != null) {
                for (int i = 0; i < bsList.size(); i++) {
                    Bank2Subject bs = bsList.get(i);
                    String bankAccount = bs.getBankAccount();
                    String subID = bs.getSubID();
                    String subCode = bs.getSubCode();
                    String subName = bs.getSubName();
                    String subFullName = bs.getSubFullName();
                    bsMap.put(bankAccount.trim(), subID + "|" + subCode + "|" + subName + "|" + subFullName);
                }
            }

            // 1 中信银行
            List<TccbBank> ccbList = tccbBankDao.queryAll(param); // 银行对账单
            if (null != ccbList && ccbList.size() > 0) {
                for (TccbBank tccbBank : ccbList) {
                    String zy = tccbBank.getDes();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = tccbBank.getBankAccount();
                    }

                    String bz = tccbBank.getBz();
                    if (bz != null && (bz.contains("工资") || bz.contains("薪水") || bz.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = tccbBank.getBankAccount();
                    }

                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", tccbBank.getSysSubjectCode());
                    pa.put("subFullName", tccbBank.getSysSubjectFullName());
                    pa.put("bankAccount", tccbBank.getBankAccount());
                    if (bsMap.get(tccbBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    double debitAmount = tccbBank.getDebitAmount();
                    double creditAmount = tccbBank.getCreditAmount();
                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount;
                    } else if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount;
                    }

                    String dfAccountName = tccbBank.getWhAccountName();
                    if (!StringUtil.isEmpty(zy)) {
                        pa.put("zy", zy);
                    } else if (!StringUtil.isEmpty(bz)) {
                        pa.put("zy", bz);
                    } else if (!StringUtil.isEmpty(dfAccountName)) {
                        pa.put("zy", dfAccountName);
                    }
                    String flag = "0";
                    if (debitAmount != 0.0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "1");
                        flag = "1";
                    }
                    if (creditAmount != 0.0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "2");
                        flag = "2";
                    }
                    String subjectInfo = getSubject(zy, bz, dfAccountName, flag, account.getAccountID(), period,
                            "中信银行");
                    if (null != subjectInfo) {
                        pa.put("subjectInfo", subjectInfo);
                    }
                    pa.put("subName", tccbBank.getDes());
                    pa.put("dfAccountName", tccbBank.getWhAccountName());
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap); // 创建凭证体和凭证头
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID(); // 凭证头主键
                    String bankID = tccbBank.getZxBillID(); // 中信银行对账单主键
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    tccbBankDao.addVouchID(par); // 保存凭证主键到银行
                    // update t_cm_bkbill_ccb set vouchID =#{vouchID} where
                    // zxBillID=#{bankID};
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            // 2 工商银行
            List<IcbcBank> icbcList = icbcBankDao.queryAll(param);
            if (null != icbcList && icbcList.size() > 0) {
                for (IcbcBank icbcBank : icbcList) {
                    String zy = icbcBank.getDes();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = icbcBank.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", icbcBank.getSysSubjectCode());
                    pa.put("subFullName", icbcBank.getSysSubjectFullName());
                    pa.put("bankAccount", icbcBank.getBankAccount());
                    if (bsMap.get(icbcBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    double jffsAmount = icbcBank.getJffsAmount();
                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = jffsAmount;
                    } else if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = jffsAmount;
                    }

                    double dffsAmount = icbcBank.getDffsAmount();
                    if (jffsAmount != 0.0) {
                        pa.put("amount", jffsAmount);
                        pa.put("flag", "1");
                    }
                    if (dffsAmount != 0.0) {
                        pa.put("amount", dffsAmount);
                        pa.put("flag", "2");
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = icbcBank.getIcbcBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    icbcBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }

            /***************************
             * 农业银行start
             *******************************************************/
            // 3 农业银行凭证
            List<NyBank> nyList = nyBankDao.queryAll(param);
            if (null != nyList && nyList.size() > 0) {
                for (NyBank nyBank : nyList) {
                    // 摘要
                    String reference = nyBank.getReference();
                    // 附言
                    String notes = nyBank.getNotes();
                    if (reference != null
                            && (reference.contains("工资") || reference.contains("薪水") || reference.contains("薪资"))) {
                        // isArch = true;
                        bankAccountTemp = nyBank.getAccountNum();
                    } else if (notes != null
                            && (notes.contains("工资") || notes.contains("薪水") || notes.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = nyBank.getAccountNum();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", nyBank.getSysSubjectCode());
                    pa.put("subFullName", nyBank.getSysSubjectFullName());
                    pa.put("bankAccount", nyBank.getAccountNum());
                    if (bsMap.get(nyBank.getAccountNum()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    // 匹配规则
                    BigDecimal fs = StringUtil.bigDecimalIsNull(nyBank.getFsAmount());
                    double fsAmount = fs.doubleValue();

                    String jfdf = nyBank.getJfdf().replace(" ", "");
                    if (reference != null && (reference.contains("公积金") || reference.contains("公共积金")
                            || reference.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    } else if (notes != null
                            && (notes.contains("公积金") || notes.contains("公积金") || notes.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    } else if (reference != null && (reference.contains("社保") || reference.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    } else if (notes != null && (notes.contains("社保") || notes.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    }
                    if (fsAmount != 0.0) {
                        if ("借".equals(jfdf)) {
                            pa.put("amount", fsAmount);
                            pa.put("flag", "2");
                        } else {
                            pa.put("amount", fsAmount);
                            pa.put("flag", "1");

                        }
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = nyBank.getNyBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    nyBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }

            List<NyBankNew> nyNewList = nyBankDao.queryNewAll(param);
            if (null != nyNewList && nyNewList.size() > 0) {
                for (NyBankNew nyBankNew : nyNewList) {
                    // 用途
                    String yt = nyBankNew.getJyyt();
                    // 说明
                    String sm = nyBankNew.getJysm();
                    // 摘要
                    String zy = nyBankNew.getJyzy();
                    // 附言
                    String fy = nyBankNew.getJyfy();
                    if (yt != null && (yt.contains("工资") || yt.contains("薪水") || yt.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = nyBankNew.getBankAccount();
                    } else if (sm != null && (sm.contains("工资") || sm.contains("薪水") || sm.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = nyBankNew.getBankAccount();
                    } else if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = nyBankNew.getBankAccount();
                    } else if (fy != null && (fy.contains("工资") || fy.contains("薪水") || fy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = nyBankNew.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", nyBankNew.getSysSubjectCode());
                    pa.put("subFullName", nyBankNew.getSysSubjectFullName());
                    pa.put("bankAccount", nyBankNew.getBankAccount());
                    if (bsMap.get(nyBankNew.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    double fsAmount = 0;
                    String jfdf = "借";
                    // 匹配规则
                    double srAmount = nyBankNew.getSrAmount();
                    double zcAmount = nyBankNew.getZcAmount();
                    if (srAmount == 0) {
                        fsAmount = zcAmount;
                        jfdf = "贷";
                    }
                    if (zcAmount == 0) {
                        fsAmount = srAmount;
                    }
                    if (fsAmount != 0.0) {
                        if ("借".equals(jfdf)) {
                            pa.put("amount", fsAmount);
                            pa.put("flag", "2");

                        } else {
                            pa.put("amount", fsAmount);
                            pa.put("flag", "1");
                        }
                    }
                    if (yt != null && (yt.contains("公积金") || yt.contains("公共积金") || yt.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    } else if (sm != null && (sm.contains("公积金") || sm.contains("公共积金") || sm.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    } else if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    } else if (fy != null && (fy.contains("公积金") || fy.contains("公共积金") || fy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = fsAmount;
                    }
                    if (yt != null && (yt.contains("社保") || yt.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    } else if (sm != null && (sm.contains("社保") || sm.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    } else if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    } else if (fy != null && (fy.contains("社保") || fy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = fsAmount;
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = nyBankNew.getId();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    nyBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }

            /************************************
             * 农业银行end
             **********************************************/

            // 4 建设银行
            List<JsBank> jsList = jsBankDao.queryAll(param);
            if (null != jsList && jsList.size() > 0) {
                for (JsBank jsBank : jsList) {
                    String reference = jsBank.getReference();
                    String remarks = jsBank.getRemarks();
                    if (reference != null
                            && (reference.contains("工资") || reference.contains("薪水") || reference.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = jsBank.getBankAccount();
                    } else if (remarks != null
                            && (remarks.contains("工资") || remarks.contains("薪水") || remarks.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = jsBank.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", jsBank.getSysSubjectCode());
                    pa.put("subFullName", jsBank.getSysSubjectFullName());
                    pa.put("bankAccount", jsBank.getBankAccount());
                    if (bsMap.get(jsBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    double debitAmount = jsBank.getJffsAmount() == null ? 0 : jsBank.getJffsAmount();
                    double creditAmount = jsBank.getDffsAmount() == null ? 0 : jsBank.getDffsAmount();

                    if (reference != null && (reference.contains("公积金") || reference.contains("公共积金")
                            || reference.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount;

                    } else if (remarks != null
                            && (remarks.contains("公积金") || remarks.contains("公共积金") || remarks.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount;
                    }

                    if (reference != null && (reference.contains("社保") || reference.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount;

                    } else if (remarks != null && (remarks.contains("社保") || remarks.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount;
                    }

                    if (debitAmount != 0.0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "1");
                    }
                    if (creditAmount != 0.0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "2");

                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = jsBank.getJsBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    jsBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }

            // 5.1 交通银行
            List<BcmBank> bcmList = bcmDao.queryAll(param);
            if (null != bcmList && bcmList.size() > 0) {
                for (BcmBank bcmBank : bcmList) {
                    String zy = bcmBank.getDes();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bcmBank.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", bcmBank.getSysSubjectCode());
                    pa.put("subFullName", bcmBank.getSysSubjectFullName());
                    pa.put("bankAccount", bcmBank.getBankAccount());
                    String jdFlag = bcmBank.getJdFlag();
                    double amount = bcmBank.getFse();
                    if ("借".equals(jdFlag)) {
                        pa.put("amount", amount);
                        pa.put("flag", "1");
                    } else {
                        pa.put("amount", amount);
                        pa.put("flag", "2");
                    }
                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = amount;
                    }
                    if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = amount;
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = bcmBank.getBcmBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    bcmDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            // 5.2 交通银行1
            List<BcmBank1> bcm1List = bcm1Dao.queryAll(param);
            if (null != bcm1List && bcm1List.size() > 0) {
                for (BcmBank1 bcmBank1 : bcm1List) {
                    String zy = bcmBank1.getDes();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bcmBank1.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", bcmBank1.getSysSubjectCode());
                    pa.put("subFullName", bcmBank1.getSysSubjectFullName());
                    pa.put("bankAccount", bcmBank1.getBankAccount());
                    if (bsMap.get(bcmBank1.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    String jdFlag = bcmBank1.getJdFlag();
                    double amount = bcmBank1.getZzje();
                    if ("借".equals(jdFlag)) {
                        pa.put("amount", amount);
                        pa.put("flag", "1");
                    } else {
                        pa.put("amount", amount);
                        pa.put("flag", "2");
                    }

                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = amount;
                    } else if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = amount;
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = bcmBank1.getBcmBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    bcm1Dao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }

            // 6 深圳农村商业银行
            List<SzrcbBank> szrcList = szrcbBankBillDao.queryAll(param);
            if (null != szrcList && szrcList.size() > 0) {
                for (SzrcbBank szrcbBank : szrcList) {
                    String zy = szrcbBank.getDes();
                    String bz = szrcbBank.getBz();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = szrcbBank.getBankAccount();
                    } else if (bz != null && (bz.contains("工资") || bz.contains("薪水") || bz.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = szrcbBank.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", szrcbBank.getSysSubjectCode());
                    pa.put("subFullName", szrcbBank.getSysSubjectFullName());
                    pa.put("bankAccount", szrcbBank.getBankAccount());
                    if (bsMap.get(szrcbBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    double debitAmount = szrcbBank.getDebitAmount();
                    double creditAmount = szrcbBank.getCreditAmount();
                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount;
                    } else if (bz != null && (bz.contains("公积金") || bz.contains("公共积金") || bz.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount;
                    } else if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount;
                    } else if (bz != null && (bz.contains("社保") || bz.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount;
                    }

                    if (debitAmount != 0.0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "2");
                    }
                    if (creditAmount != 0.0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "1");
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = szrcbBank.getSzrcbBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    szrcbBankBillDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            // 7 平安银行
            List<PaBank> paList = paBankDao.queryAll(param);
            if (null != paList && paList.size() > 0) {
                for (PaBank paBank : paList) {
                    // 摘要
                    String feference = paBank.getReference();
                    // 用途
                    String purpose = paBank.getPurpose();
                    if (feference != null
                            && (feference.contains("工资") || feference.contains("薪水") || feference.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = paBank.getAccountNumber();
                    } else if (purpose != null
                            && (purpose.contains("工资") || purpose.contains("薪水") || purpose.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = paBank.getAccountNumber();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", paBank.getSysSubjectCode());
                    pa.put("subFullName", paBank.getSysSubjectFullName());
                    pa.put("bankAccount", paBank.getAccountNumber());
                    if (bsMap.get(paBank.getAccountNumber()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    BigDecimal debitAmount = paBank.getDebitAmount(); // 借
                    BigDecimal creditAmount = paBank.getCreditAmount(); // 贷

                    if (feference != null && (feference.contains("公积金") || feference.contains("公共积金")
                            || feference.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (purpose != null
                            && (purpose.contains("公积金") || purpose.contains("公积金") || purpose.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (feference != null && (feference.contains("社保") || feference.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (purpose != null && (purpose.contains("社保") || purpose.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    }
                    if (debitAmount.compareTo(BigDecimal.ZERO) != 0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "1");
                    }
                    if (creditAmount.compareTo(BigDecimal.ZERO) != 0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "2");
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = paBank.getPaBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    paBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            // 9 招商银行
            List<ZsBank> zsList = zsBankDao.queryAll(param);
            if (null != zsList && zsList.size() > 0) {
                for (ZsBank zsBank : zsList) {
                    // 摘要
                    String zy = zsBank.getReference();
                    // 用途
                    String yt = zsBank.getPurpose();
                    // 业务摘要
                    String ywzy = zsBank.getBusinessReference();
                    // 其他摘要
                    String qtzy = zsBank.getOtherReference();
                    // 扩展摘要
                    String kzzy = zsBank.getExpandReference();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = zsBank.getBankAccount();
                    } else if (yt != null && (yt.contains("工资") || yt.contains("薪水") || yt.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = zsBank.getBankAccount();
                    } else if (ywzy != null && (ywzy.contains("工资") || ywzy.contains("薪水") || ywzy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = zsBank.getBankAccount();
                    } else if (qtzy != null && (qtzy.contains("工资") || qtzy.contains("薪水") || qtzy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = zsBank.getBankAccount();
                    } else if (kzzy != null && (kzzy.contains("工资") || kzzy.contains("薪水") || kzzy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = zsBank.getBankAccount();
                    }
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", zsBank.getSysSubjectCode());
                    pa.put("subFullName", zsBank.getSysSubjectFullName());
                    pa.put("bankAccount", zsBank.getBankAccount());
                    if (bsMap.get(zsBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    BigDecimal debitAmount = zsBank.getDebitAmount();
                    BigDecimal creditAmount = zsBank.getCreditAmount();
                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (yt != null && (yt.contains("公积金") || yt.contains("公共积金") || yt.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (ywzy != null
                            && (ywzy.contains("公积金") || ywzy.contains("公共积金") || ywzy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (qtzy != null
                            && (qtzy.contains("公积金") || qtzy.contains("公共积金") || qtzy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (kzzy != null
                            && (kzzy.contains("公积金") || kzzy.contains("公共积金") || kzzy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    }

                    if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (yt != null && (yt.contains("社保") || yt.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (ywzy != null && (ywzy.contains("社保") || ywzy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (qtzy != null && (qtzy.contains("社保") || qtzy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    } else if (kzzy != null && (kzzy.contains("社保") || kzzy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = debitAmount == null ? 0 : debitAmount.doubleValue();
                    }

                    if (null != debitAmount && debitAmount.compareTo(BigDecimal.ZERO) != 0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "1");
                    }
                    if (null != creditAmount && creditAmount.compareTo(BigDecimal.ZERO) != 0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "2");
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    Voucher voucher = createVouch(pa, bsMap);
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = zsBank.getZsBillID();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    zsBankDao.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            // 10 中国银行
            List<TCmBkbillBoc> bocList = tCmBkbillBocMapper.queryAll(param);
            if (null != bocList && bocList.size() > 0) {
                for (TCmBkbillBoc bocBank : bocList) {
                    Map<String, Object> pa = new HashMap<String, Object>();
                    // transaction_type
                    // 中国银行是根据交易类型判断凭证方向
                    // 往账 银行存款是贷方 来账 银行存款是借方
                    // 摘要
                    String zy = bocBank.getReference();
                    // 用途
                    String yt = bocBank.getPurpose();
                    // 交易附言
                    String remark = bocBank.getRemark();
                    // 备注
                    String remarks = bocBank.getRemarks();
                    if (zy != null && (zy.contains("工资") || zy.contains("薪水") || zy.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bocBank.getBankAccount();
                    } else if (yt != null && (yt.contains("工资") || yt.contains("薪水") || yt.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bocBank.getBankAccount();
                    } else if (remark != null
                            && (remark.contains("工资") || remark.contains("薪水") || remark.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bocBank.getBankAccount();
                    } else if (remarks != null
                            && (remarks.contains("工资") || remarks.contains("薪水") || remarks.contains("薪资"))) {
                        // isArch = true;
                        archFlag++;
                        bankAccountTemp = bocBank.getBankAccount();
                    }
                    String transactionType = bocBank.getTransactionType();
                    pa.put("account", account);
                    pa.put("busDate", period);
                    pa.put("user", user);
                    pa.put("subCode", bocBank.getSysSubjectCode());
                    pa.put("subFullName", bocBank.getSysSubjectFullName());
                    pa.put("bankAccount", bocBank.getBankAccount());

                    if (bsMap.get(bocBank.getBankAccount()) == null) {
                        throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                    }
                    String amountStr = bocBank.getTradeAmount(); // 交易金额

                    if (zy != null && (zy.contains("公积金") || zy.contains("公共积金") || zy.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (yt != null && (yt.contains("公积金") || yt.contains("公共积金") || yt.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (remark != null
                            && (remark.contains("公积金") || remark.contains("公共积金") || remark.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (remarks != null
                            && (remarks.contains("公积金") || remarks.contains("公共积金") || remarks.contains("住房积金"))) {
                        gjjFlag++;
                        // isGjj = true;
                        gjjAmount = Math.abs(Double.parseDouble(amountStr));
                    }

                    if (zy != null && (zy.contains("社保") || zy.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (yt != null && (yt.contains("社保") || yt.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (remark != null && (remark.contains("社保") || remark.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = Math.abs(Double.parseDouble(amountStr));
                    } else if (remarks != null && (remarks.contains("社保") || remarks.contains("社会保险"))) {
                        sbFlag++;
                        // isSb = true;
                        sbAmount = Math.abs(Double.parseDouble(amountStr));
                    }

                    // 往账 银行存款是贷方 来账 银行存款是借方
                    // flag 一借二贷

                    if (transactionType.indexOf("往账") > -1) {
                        pa.put("amount", Math.abs(Double.parseDouble(amountStr)));
                        pa.put("flag", "1");

                    } else {
                        pa.put("amount", Math.abs(Double.parseDouble(amountStr.replaceAll("-", ""))));
                        pa.put("flag", "2");
                    }
                    // pa.put("isArch", isArch);
                    // pa.put("isGjj", isGjj);
                    // pa.put("isSb", isSb);
                    long begin = System.currentTimeMillis();
                    Voucher voucher = createVouch(pa, bsMap);
                    long end = System.currentTimeMillis();
                    System.out.println((double) (end - begin) / 1000);
                    // 耗时0.32-0.37
                    // isArch = false;
                    // isGjj = false;
                    // isSb = false;
                    if (voucher == null) {
                        continue;
                    }
                    String vouchID = voucher.getVoucherHead().getVouchID();
                    String bankID = bocBank.getPkBkbillBoc();
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("vouchID", vouchID);
                    par.put("bankID", bankID);
                    tCmBkbillBocMapper.addVouchID(par);
                    returnList.add(voucher);
                }
                // upBankSub(upParam);
                upBankVouch();
            }
            /*
             * if (archFlag >= 2) { throw new
             * BusinessException("请仔细检查银行对账单,工资存在多条记录!"); }
             */
            /*
             * if (gjjFlag > 2 || sbFlag > 2) { throw new
             * BusinessException("请仔细检查银行对账单,社保和公积金存在多条记录!"); } else {
             * Map<String, Object> param1 = new HashMap<String, Object>();
             * param.put("accountID", account.getAccountID());
             * param.put("busDate", busDate); param.put("period", busDate);
             * String subName = "工资"; Map<String, Object> para = new
             * HashMap<String, Object>(); para.put("subCode", "2211");
             * para.put("subjectID", "2211"); para.put("subName", subName);
             * para.put("accountID", account.getAccountID()); para.put("period",
             * busDate); para.put("busDate", busDate);
             * List<TBasicSubjectMessage> li =
             * tBasicSubjectMessageMapper.querySubByAccAndCode(para); if (li ==
             * null || li.get(0) == null || li.get(0).getEndingBalanceCredit()
             * == null || li.get(0).getEndingBalanceCredit().doubleValue() == 0)
             * { // 应付职工薪酬没有期末余额贷,不用发放 } else { VoucherHead vouchHead = new
             * VoucherHead(); String uuid = UUIDUtils.getUUID();
             * vouchHead.setVouchID(uuid); vouchHead.setPeriod(busDate);
             * vouchHead.setVcDate(new Date());
             * vouchHead.setAccountID(account.getAccountID());
             * vouchHead.setVoucherNO(0);
             * vouchHead.setCreateDate(System.currentTimeMillis());
             * vouchHead.setCreatepsn(user.getUserName());
             * vouchHead.setCreatePsnID(user.getUserID());
             * vouchHead.setSource(3); Integer maxVoucherNo =
             * voucherHeadDao.getMaxVoucherNo(para); // 凭证号
             * vouchHead.setVoucherNO(maxVoucherNo);
             * vouchHead.setTotalCredit(li.get(0).getEndingBalanceCredit().
             * doubleValue());
             * vouchHead.setTotalDbit(li.get(0).getEndingBalanceCredit().
             * doubleValue()); List<TBasicSubjectMessage> subList =
             * tBasicSubjectMessageMapper.querySubject(para);
             * TBasicSubjectMessage subMessage = null; if (subList != null &&
             * subList.size() == 1) { subMessage = subList.get(0); } //
             * 借：应付职工薪酬-工资100 int fl = 1; VoucherBody vouchBody = new
             * VoucherBody(); vouchBody.setVouchID(uuid);
             * vouchBody.setPeriod(busDate);
             * vouchBody.setVouchAID(UUIDUtils.getUUID());
             * vouchBody.setAccountID(account.getAccountID());
             * vouchBody.setDebitAmount(li.get(0).getEndingBalanceCredit().
             * doubleValue()); vouchBody.setDirection("1"); if (subMessage !=
             * null) { vouchBody.setVcsubject("应付职工薪酬_" +
             * subMessage.getSubName());
             * vouchBody.setSubjectID(subMessage.getSubCode()); } else {
             * vouchBody.setVcsubject(""); vouchBody.setSubjectID(""); }
             * vouchBody.setVcabstact("发放薪资"); vouchBody.setRowIndex(fl + "");
             *
             * // 查询上月薪资单 Map<String, Object> pa = new HashMap<String,
             * Object>(); pa.put("accountID", account.getAccountID());
             * pa.put("period", DateUtil.getLastMonth(busDate)); List<Arch>
             * archList = archDao.queryArchData(pa); if (archList == null ||
             * archList.size() == 0) { // 上月没薪资单 // 其他应收款-社会保险（个人） Map<String,
             * Object> sbParam = new HashMap<String, Object>(); String sbName =
             * "社保"; sbParam.put("subCode", "1221");// 其他应收款
             * sbParam.put("subName", sbName); sbParam.put("accountID",
             * account.getAccountID()); sbParam.put("period", busDate);
             * sbParam.put("busDate", busDate); List<TBasicSubjectMessage>
             * sbList = tBasicSubjectMessageMapper.querySubject(sbParam); if
             * (sbList == null) { sbName = "社会保险"; sbParam.put("subName",
             * sbName); sbList =
             * tBasicSubjectMessageMapper.querySubject(sbParam); } VoucherBody
             * vb2 = new VoucherBody(); vb2.setVouchID(uuid);
             * vb2.setPeriod(busDate); vb2.setVouchAID(UUIDUtils.getUUID());
             * vb2.setAccountID(account.getAccountID());
             * vb2.setCreditAmount(0.0); vb2.setDirection("2");
             * vb2.setSubjectID(""); vb2.setVcsubject("");
             * vb2.setVcabstact("社保(个人)"); vb2.setRowIndex("2"); if (null !=
             * sbList && sbList.size() == 1) {
             * vb2.setSubjectID(sbList.get(0).getSubCode());
             * vb2.setVcsubject(sbList.get(0).getFullName()); } //
             * 其他应收款-公积金（个人）4 Map<String, Object> gjjParam = new HashMap<String,
             * Object>(); String gjjName = "公积金"; gjjParam.put("subCode",
             * "1221");// 其他应收款 gjjParam.put("subName", gjjName);
             * gjjParam.put("accountID", account.getAccountID());
             * gjjParam.put("period", busDate); gjjParam.put("busDate",
             * busDate); List<TBasicSubjectMessage> gjjList =
             * tBasicSubjectMessageMapper.querySubject(gjjParam); if (gjjList ==
             * null) { gjjName = "公共积金"; gjjParam.put("subName", gjjName);
             * gjjList = tBasicSubjectMessageMapper.querySubject(gjjParam); }
             * VoucherBody vb3 = new VoucherBody(); vb3.setVouchID(uuid);
             * vb3.setPeriod(busDate); vb3.setVouchAID(UUIDUtils.getUUID());
             * vb3.setAccountID(account.getAccountID());
             * vb3.setCreditAmount(0.0); vb3.setDirection("2");
             * vb3.setSubjectID(""); vb3.setVcsubject("");
             * vb3.setVcabstact("公积金(个人)"); vb3.setRowIndex("3"); if (null !=
             * gjjList && gjjList.size() == 1) {
             * vb3.setSubjectID(gjjList.get(0).getSubCode());
             * vb3.setVcsubject(gjjList.get(0).getFullName()); }
             *
             * // 应交税费-应交个人所得税 2 // 个税 Map<String, Object> gsParam = new
             * HashMap<String, Object>(); String gsName = "个人所得税";
             * gsParam.put("subCode", "2221");// 个人所得税 gsParam.put("subName",
             * gsName); gsParam.put("accountID", account.getAccountID());
             * gsParam.put("period", busDate); gsParam.put("busDate", busDate);
             * List<TBasicSubjectMessage> gsList =
             * tBasicSubjectMessageMapper.querySubject(gsParam); if (gsList ==
             * null) { gsName = "个税"; gsParam.put("subName", gsName); gsList =
             * tBasicSubjectMessageMapper.querySubject(gsParam); } VoucherBody
             * vb4 = new VoucherBody(); vb4.setVouchID(uuid);
             * vb4.setPeriod(busDate); vb4.setVouchAID(UUIDUtils.getUUID());
             * vb4.setAccountID(account.getAccountID());
             * vb4.setCreditAmount(0.0); vb4.setDirection("2");
             * vb4.setSubjectID(""); vb4.setVcsubject("");
             * vb4.setVcabstact("个人所得税"); vb4.setRowIndex("4"); if (null !=
             * gsList && gsList.size() == 1) {
             * vb4.setSubjectID(gsList.get(0).getSubCode());
             * vb4.setVcsubject(gsList.get(0).getFullName()); }
             *
             * // 银行存款/库存现金 86
             *
             * VoucherBody vb5 = new VoucherBody(); vb5.setVouchID(uuid);
             * vb5.setPeriod(busDate); vb5.setVouchAID(UUIDUtils.getUUID());
             * vb5.setAccountID(account.getAccountID());
             * vb5.setCreditAmount(0.0); vb5.setDirection("2");
             * vb5.setSubjectID(""); vb5.setVcsubject("");
             * vb5.setVcabstact("库存现金/银行存款"); vb5.setRowIndex("5");
             * vb5.setSubjectID(""); vb5.setVcsubject("");
             *
             * Voucher voucher = new Voucher();
             * voucher.setVoucherHead(vouchHead); List<VoucherBody> list = new
             * ArrayList<VoucherBody>(); list.add(vouchBody); list.add(vb2);
             * list.add(vb3); list.add(vb4); list.add(vb5);
             *
             * voucher.setVoucherBodyList(list);
             * voucherHeadDao.insertVouchHead(vouchHead); //
             * voucherBodyDao.insertVouchBody(vouchBody); for (int s = 0; s <
             * list.size(); s++) { voucherBodyDao.insertVouchBody(list.get(s));
             * }
             *
             * vatService.checkVouch(pa, voucher); Map<String, Object> dwsbParam
             * = new HashMap<String, Object>(); String dwsbName = "社保";
             * dwsbParam.put("subCode", "6602");// 社保 dwsbParam.put("subName",
             * dwsbName); dwsbParam.put("accountID", account.getAccountID());
             * dwsbParam.put("period", busDate); dwsbParam.put("busDate",
             * busDate); List<TBasicSubjectMessage> dwsbList =
             * tBasicSubjectMessageMapper.querySubject(dwsbParam); if (dwsbList
             * == null) { dwsbName = "社会保险"; dwsbParam.put("subName", dwsbName);
             * dwsbList = tBasicSubjectMessageMapper.querySubject(dwsbParam); }
             *
             * String uid = UUIDUtils.getUUID(); VoucherBody vb10 = new
             * VoucherBody(); vb10.setVouchID(uid); vb10.setPeriod(busDate);
             * vb10.setVouchAID(UUIDUtils.getUUID());
             * vb10.setAccountID(account.getAccountID());
             *
             * vb10.setDebitAmount(0.0);
             *
             * vb10.setDirection("1"); if (dwsbList != null && dwsbList.size()
             * == 1) { vb10.setSubjectID(dwsbList.get(0).getSubCode());
             * vb10.setVcsubject(dwsbList.get(0).getFullName()); } else {
             * vb10.setSubjectID(""); vb10.setVcsubject(""); }
             *
             * vb10.setVcabstact("社会保险（单位）公司缴纳部分"); vb10.setRowIndex("1");
             *
             * Map<String, Object> dwgjjParam = new HashMap<String, Object>();
             * String dwgjjName = "公积金"; dwgjjParam.put("subCode", "6602");// 社保
             * dwgjjParam.put("subName", dwgjjName); dwgjjParam.put("accountID",
             * account.getAccountID()); dwgjjParam.put("period", busDate);
             * dwgjjParam.put("busDate", busDate); List<TBasicSubjectMessage>
             * dwgjjList = tBasicSubjectMessageMapper.querySubject(dwgjjParam);
             * if (dwgjjList == null) { dwgjjName = "公共积金";
             * dwgjjParam.put("subName", dwsbName); dwgjjList =
             * tBasicSubjectMessageMapper.querySubject(dwgjjParam); } //
             * 管理费用-公积金（单位） 公司缴纳部分 VoucherBody vb6 = new VoucherBody();
             * vb6.setVouchID(uid); vb6.setPeriod(busDate);
             * vb6.setVouchAID(UUIDUtils.getUUID());
             * vb6.setAccountID(account.getAccountID());
             *
             * vb6.setDebitAmount(0.0);
             *
             * if (dwgjjList != null && dwgjjList.size() == 1) {
             * vb6.setSubjectID(dwgjjList.get(0).getSubCode());
             * vb6.setVcsubject(dwgjjList.get(0).getFullName()); } else {
             * vb6.setSubjectID(""); vb6.setVcsubject(""); }
             * vb6.setDirection("1");
             *
             * vb6.setVcabstact("公积金（单位）  公司缴纳部分"); vb6.setRowIndex("2");
             *
             * // 社保 if (sbList == null) { sbName = "社会保险";
             * sbParam.put("subName", sbName); sbList =
             * tBasicSubjectMessageMapper.querySubject(sbParam); } VoucherBody
             * vb7 = new VoucherBody(); vb7.setVouchID(uid);
             * vb7.setPeriod(busDate); vb7.setVouchAID(UUIDUtils.getUUID());
             * vb7.setAccountID(account.getAccountID());
             * vb7.setDebitAmount(0.0); vb7.setDirection("1");
             * vb7.setSubjectID(""); vb7.setVcsubject("");
             * vb7.setVcabstact("社保(个人)"); vb7.setRowIndex("3"); if (null !=
             * sbList && sbList.size() == 1) {
             * vb7.setSubjectID(sbList.get(0).getSubCode());
             * vb7.setVcsubject(sbList.get(0).getFullName()); } // 公积金 if
             * (gjjList == null) { gjjName = "公共积金"; gjjParam.put("subName",
             * sbName); gjjList =
             * tBasicSubjectMessageMapper.querySubject(gjjParam); } VoucherBody
             * vb8 = new VoucherBody(); vb8.setVouchID(uid);
             * vb8.setPeriod(busDate); vb8.setVouchAID(UUIDUtils.getUUID());
             * vb8.setAccountID(account.getAccountID());
             * vb8.setDebitAmount(0.0); vb8.setDirection("1");
             * vb8.setSubjectID(""); vb8.setVcsubject("");
             * vb8.setVcabstact("公积金(个人)"); vb8.setRowIndex("4"); if (null !=
             * gjjList && gjjList.size() == 1) {
             * vb8.setSubjectID(gjjList.get(0).getSubCode());
             * vb8.setVcsubject(gjjList.get(0).getFullName()); } // 个税 if
             * (gsList == null) { gsName = "个税"; gsParam.put("subName", gsName);
             * gsList = tBasicSubjectMessageMapper.querySubject(gsParam); }
             * VoucherBody vb9 = new VoucherBody(); vb9.setVouchID(uid);
             * vb9.setPeriod(busDate); vb9.setVouchAID(UUIDUtils.getUUID());
             * vb9.setAccountID(account.getAccountID());
             * vb9.setDebitAmount(0.0); vb9.setDirection("1");
             * vb9.setSubjectID(""); vb9.setVcsubject("");
             * vb9.setVcabstact("个人所得税"); vb9.setRowIndex("5"); if (null !=
             * gsList && gsList.size() == 1) {
             * vb9.setSubjectID(gsList.get(0).getSubCode());
             * vb9.setVcsubject(gsList.get(0).getFullName()); }
             *
             * VoucherBody vb11 = new VoucherBody(); vb11.setVouchID(uid);
             * vb11.setPeriod(busDate); vb11.setVouchAID(UUIDUtils.getUUID());
             * vb11.setAccountID(account.getAccountID());
             *
             * vb11.setCreditAmount(0.0); Map<String, Object> par = new
             * HashMap<String, Object>(); par.put("bankAccount",
             * bankAccountTemp); List<Bank2Subject> bank2SubList =
             * bank2SubjectDao.queryByBankAccount(par); if (bank2SubList != null
             * && bank2SubList.size() > 0) {
             * vb11.setSubjectID(bank2SubList.get(0).getSubCode());
             * vb11.setVcsubject(bank2SubList.get(0).getSubFullName()); } else {
             * vb11.setSubjectID(""); vb11.setVcsubject(""); }
             * vb11.setDirection("2"); vb11.setVcabstact("银行存款");
             * vb11.setRowIndex("6"); VoucherHead vh = new VoucherHead();
             *
             * vh.setVouchID(uid); vh.setPeriod(busDate); vh.setVcDate(new
             * Date()); vh.setAccountID(account.getAccountID());
             * vh.setCreateDate(System.currentTimeMillis());
             * vh.setCreatepsn(user.getUserName());
             * vh.setCreatePsnID(user.getUserID()); vh.setSource(3); Integer
             * maxVouchNo = voucherHeadDao.getMaxVoucherNo(para); // 凭证号
             * vh.setVoucherNO(maxVouchNo); Voucher vouch = new Voucher();
             * vouch.setVoucherHead(vh); List<VoucherBody> vbL = new
             * ArrayList<VoucherBody>(); // vbL.add(vb5); vbL.add(vb6);
             * vbL.add(vb7); vbL.add(vb8); vbL.add(vb9); vbL.add(vb10);
             * vbL.add(vb11); vouch.setVoucherBodyList(vbL); //
             *
             * vouch.setVoucherHead(vh); voucherHeadDao.insertVouchHead(vh); for
             * (int j = 0; j < vbL.size(); j++) {
             * voucherBodyDao.insertVouchBody(vbL.get(j)); } boolean boo =
             * vatService.checkVouch(para, vouch); if (boo) {
             * tBasicSubjectMessageMapper.chgSubAmountByCreate(para, vouch); }
             *
             * } else { // 上月有薪资单 double sb = 0; double gjj = 0; double gs = 0;
             * if (null != archList && archList.size() > 0) { Map<String,
             * Object> p1 = new HashMap<String, Object>(); p1.put("accountID",
             * account.getAccountID()); p1.put("busDate",
             * DateUtil.getLastMonth(busDate)); p1.put("period",
             * DateUtil.getLastMonth(busDate)); Object obj =
             * archDao.queryFfArchData(p1); Map<String, Object> objMap =
             * (Map<String, Object>) obj; sb =
             * Double.parseDouble(objMap.get("sb").toString()); gjj =
             * Double.parseDouble(objMap.get("gjj").toString()); gs =
             * Double.parseDouble(objMap.get("gs").toString()); } // 社保
             * Map<String, Object> sbParam = new HashMap<String, Object>();
             * String sbName = "社保"; sbParam.put("subCode", "1221");// 其他应收款
             * sbParam.put("subName", sbName); sbParam.put("accountID",
             * account.getAccountID()); sbParam.put("period", busDate);
             * sbParam.put("busDate", busDate); List<TBasicSubjectMessage>
             * sbList = tBasicSubjectMessageMapper.querySubject(sbParam); if
             * (sbList == null) { sbName = "社会保险"; sbParam.put("subName",
             * sbName); sbList =
             * tBasicSubjectMessageMapper.querySubject(sbParam); } VoucherBody
             * vb1 = new VoucherBody(); vb1.setVouchID(uuid);
             * vb1.setPeriod(busDate); vb1.setVouchAID(UUIDUtils.getUUID());
             * vb1.setAccountID(account.getAccountID());
             * vb1.setCreditAmount(sb); vb1.setDirection("2");
             * vb1.setSubjectID(""); vb1.setVcsubject("");
             * vb1.setVcabstact("社保(个人)"); vb1.setRowIndex("2"); if (null !=
             * sbList && sbList.size() == 1) {
             * vb1.setSubjectID(sbList.get(0).getSubCode());
             * vb1.setVcsubject(sbList.get(0).getFullName()); }
             *
             * // 公积金 Map<String, Object> gjjParam = new HashMap<String,
             * Object>(); String gjjName = "公积金"; gjjParam.put("subCode",
             * "1221");// 其他应收款 gjjParam.put("subName", gjjName);
             * gjjParam.put("accountID", account.getAccountID());
             * gjjParam.put("period", busDate); gjjParam.put("busDate",
             * busDate); List<TBasicSubjectMessage> gjjList =
             * tBasicSubjectMessageMapper.querySubject(gjjParam); if (gjjList ==
             * null) { gjjName = "公共积金"; gjjParam.put("subName", gjjName);
             * gjjList = tBasicSubjectMessageMapper.querySubject(gjjParam); }
             * VoucherBody vb2 = new VoucherBody(); vb2.setVouchID(uuid);
             * vb2.setPeriod(busDate); vb2.setVouchAID(UUIDUtils.getUUID());
             * vb2.setAccountID(account.getAccountID());
             * vb2.setCreditAmount(gjj); vb2.setDirection("2");
             * vb2.setSubjectID(""); vb2.setVcsubject("");
             * vb2.setVcabstact("公积金(个人)"); vb2.setRowIndex("3"); if (null !=
             * gjjList && gjjList.size() == 1) {
             * vb2.setSubjectID(gjjList.get(0).getSubCode());
             * vb2.setVcsubject(gjjList.get(0).getFullName()); }
             *
             * // 个税 Map<String, Object> gsParam = new HashMap<String,
             * Object>(); String gsName = "个人所得税"; gsParam.put("subCode",
             * "2221");// 个人所得税 gsParam.put("subName", gsName);
             * gsParam.put("accountID", account.getAccountID());
             * gsParam.put("period", busDate); gsParam.put("busDate", busDate);
             * List<TBasicSubjectMessage> gsList =
             * tBasicSubjectMessageMapper.querySubject(gsParam); if (gsList ==
             * null) { gsName = "个税"; gsParam.put("subName", gsName); gsList =
             * tBasicSubjectMessageMapper.querySubject(gsParam); } VoucherBody
             * vb3 = new VoucherBody(); vb3.setVouchID(uuid);
             * vb3.setPeriod(busDate); vb3.setVouchAID(UUIDUtils.getUUID());
             * vb3.setAccountID(account.getAccountID());
             * vb3.setCreditAmount(gs); vb3.setDirection("2");
             * vb3.setSubjectID(""); vb3.setVcsubject("");
             * vb3.setVcabstact("个人所得税"); vb3.setRowIndex("4"); if (null !=
             * gsList && gsList.size() == 1) {
             * vb3.setSubjectID(gsList.get(0).getSubCode());
             * vb3.setVcsubject(gsList.get(0).getFullName()); }
             *
             * // 库存现金/银行存款 VoucherBody vb4 = new VoucherBody();
             * vb4.setVouchID(uuid); vb4.setPeriod(busDate);
             * vb4.setVouchAID(UUIDUtils.getUUID());
             * vb4.setAccountID(account.getAccountID());
             * vb4.setCreditAmount(li.get(0).getEndingBalanceCredit().
             * doubleValue() - sb - gjj - gs); vb4.setDirection("2");
             * vb4.setSubjectID(""); vb4.setVcsubject("");
             * vb4.setVcabstact("库存现金/银行存款"); vb4.setRowIndex("5");
             * vb4.setSubjectID(""); vb4.setVcsubject("");
             *
             * Voucher voucher = new Voucher();
             * voucher.setVoucherHead(vouchHead); List<VoucherBody> list = new
             * ArrayList<VoucherBody>(); list.add(vouchBody); list.add(vb1);
             * list.add(vb2); list.add(vb3); list.add(vb4);
             * voucher.setVoucherBodyList(list);
             *
             * voucherHeadDao.insertVouchHead(vouchHead); for (int i = 0; i <
             * list.size(); i++) { voucherBodyDao.insertVouchBody(list.get(i));
             * }
             *
             * boolean bool = vatService.checkVouch(para, voucher); if (bool) {
             * tBasicSubjectMessageMapper.chgSubAmountByCreate(para, voucher); }
             *
             * // 缴纳社保 VoucherHead vh = new VoucherHead(); String uid =
             * UUIDUtils.getUUID();
             *
             * vh.setVouchID(uid); vh.setPeriod(busDate); vh.setVcDate(new
             * Date()); vh.setAccountID(account.getAccountID());
             * vh.setCreateDate(System.currentTimeMillis());
             * vh.setCreatepsn(user.getUserName());
             * vh.setCreatePsnID(user.getUserID()); vh.setSource(3); Integer
             * maxVouchNo = voucherHeadDao.getMaxVoucherNo(para); // 凭证号
             * vh.setVoucherNO(maxVouchNo);
             *
             * // 管理费用-社会保险（单位）公司缴纳部分 Map<String, Object> dwsbParam = new
             * HashMap<String, Object>(); String dwsbName = "社保";
             * dwsbParam.put("subCode", "6602");// 社保 dwsbParam.put("subName",
             * dwsbName); dwsbParam.put("accountID", account.getAccountID());
             * dwsbParam.put("period", busDate); dwsbParam.put("busDate",
             * busDate); List<TBasicSubjectMessage> dwsbList =
             * tBasicSubjectMessageMapper.querySubject(dwsbParam); if (dwsbList
             * == null) { dwsbName = "社会保险"; dwsbParam.put("subName", dwsbName);
             * dwsbList = tBasicSubjectMessageMapper.querySubject(dwsbParam); }
             *
             * VoucherBody vb5 = new VoucherBody(); vb5.setVouchID(uid);
             * vb5.setPeriod(busDate); vb5.setVouchAID(UUIDUtils.getUUID());
             * vb5.setAccountID(account.getAccountID()); if (sbAmount != 0.0) {
             * vb5.setDebitAmount(sbAmount - sb); } else {
             * vb5.setDebitAmount(0.0); }
             *
             * vb5.setDirection("1"); if (dwsbList != null && dwsbList.size() ==
             * 1) { vb5.setSubjectID(dwsbList.get(0).getSubCode());
             * vb5.setVcsubject(dwsbList.get(0).getFullName()); } else {
             * vb5.setSubjectID(""); vb5.setVcsubject(""); }
             *
             * vb5.setVcabstact("社会保险（单位）公司缴纳部分"); vb5.setRowIndex("1");
             *
             * Map<String, Object> dwgjjParam = new HashMap<String, Object>();
             * String dwgjjName = "公积金"; dwgjjParam.put("subCode", "6602");// 社保
             * dwgjjParam.put("subName", dwgjjName); dwgjjParam.put("accountID",
             * account.getAccountID()); dwgjjParam.put("period", busDate);
             * dwgjjParam.put("busDate", busDate); List<TBasicSubjectMessage>
             * dwgjjList = tBasicSubjectMessageMapper.querySubject(dwgjjParam);
             * if (dwgjjList == null) { dwgjjName = "公共积金";
             * dwgjjParam.put("subName", dwsbName); dwgjjList =
             * tBasicSubjectMessageMapper.querySubject(dwgjjParam); } //
             * 管理费用-公积金（单位） 公司缴纳部分 VoucherBody vb6 = new VoucherBody();
             * vb6.setVouchID(uid); vb6.setPeriod(busDate);
             * vb6.setVouchAID(UUIDUtils.getUUID());
             * vb6.setAccountID(account.getAccountID()); if (gjjAmount != 0.0) {
             * vb6.setDebitAmount(gjjAmount - gjj); } else {
             * vb6.setDebitAmount(0.0); }
             *
             * if (dwgjjList != null && dwgjjList.size() == 1) {
             * vb6.setSubjectID(dwgjjList.get(0).getSubCode());
             * vb6.setVcsubject(dwgjjList.get(0).getFullName()); } else {
             * vb6.setSubjectID(""); vb6.setVcsubject(""); }
             * vb6.setDirection("1");
             *
             * vb6.setVcabstact("公积金（单位）  公司缴纳部分"); vb6.setRowIndex("2");
             *
             * // 社保 if (sbList == null) { sbName = "社会保险";
             * sbParam.put("subName", sbName); sbList =
             * tBasicSubjectMessageMapper.querySubject(sbParam); } VoucherBody
             * vb7 = new VoucherBody(); vb7.setVouchID(uid);
             * vb7.setPeriod(busDate); vb7.setVouchAID(UUIDUtils.getUUID());
             * vb7.setAccountID(account.getAccountID()); vb7.setDebitAmount(sb);
             * vb7.setDirection("1"); vb7.setSubjectID("");
             * vb7.setVcsubject(""); vb7.setVcabstact("社保(个人)");
             * vb7.setRowIndex("3"); if (null != sbList && sbList.size() == 1) {
             * vb7.setSubjectID(sbList.get(0).getSubCode());
             * vb7.setVcsubject(sbList.get(0).getFullName()); } // 公积金 if
             * (gjjList == null) { gjjName = "公共积金"; gjjParam.put("subName",
             * sbName); gjjList =
             * tBasicSubjectMessageMapper.querySubject(gjjParam); } VoucherBody
             * vb8 = new VoucherBody(); vb8.setVouchID(uid);
             * vb8.setPeriod(busDate); vb8.setVouchAID(UUIDUtils.getUUID());
             * vb8.setAccountID(account.getAccountID());
             * vb8.setDebitAmount(gjj); vb8.setDirection("1");
             * vb8.setSubjectID(""); vb8.setVcsubject("");
             * vb8.setVcabstact("公积金(个人)"); vb8.setRowIndex("4"); if (null !=
             * gjjList && gjjList.size() == 1) {
             * vb8.setSubjectID(gjjList.get(0).getSubCode());
             * vb8.setVcsubject(gjjList.get(0).getFullName()); } // 个税 if
             * (gsList == null) { gsName = "个税"; gsParam.put("subName", gsName);
             * gsList = tBasicSubjectMessageMapper.querySubject(gsParam); }
             * VoucherBody vb9 = new VoucherBody(); vb9.setVouchID(uid);
             * vb9.setPeriod(busDate); vb9.setVouchAID(UUIDUtils.getUUID());
             * vb9.setAccountID(account.getAccountID()); vb9.setDebitAmount(gs);
             * vb9.setDirection("1"); vb9.setSubjectID("");
             * vb9.setVcsubject(""); vb9.setVcabstact("个人所得税");
             * vb9.setRowIndex("5"); if (null != gsList && gsList.size() == 1) {
             * vb9.setSubjectID(gsList.get(0).getSubCode());
             * vb9.setVcsubject(gsList.get(0).getFullName()); }
             *
             * VoucherBody vb10 = new VoucherBody(); vb10.setVouchID(uid);
             * vb10.setPeriod(busDate); vb10.setVouchAID(UUIDUtils.getUUID());
             * vb10.setAccountID(account.getAccountID()); if (sbAmount != 0 &&
             * gjjAmount != 0) { vb10.setCreditAmount(sbAmount + gjjAmount +
             * gs); vh.setTotalCredit(sbAmount + gjjAmount + gs);
             * vh.setTotalDbit(sbAmount + gjjAmount + gs); } else {
             * vb10.setCreditAmount(0.0); } vb10.setDirection("2"); Map<String,
             * Object> par = new HashMap<String, Object>();
             * par.put("bankAccount", bankAccountTemp); List<Bank2Subject>
             * bank2SubList = bank2SubjectDao.queryByBankAccount(par); if
             * (bank2SubList != null && bank2SubList.size() > 0) {
             * vb10.setSubjectID(bank2SubList.get(0).getSubCode());
             * vb10.setVcsubject(bank2SubList.get(0).getSubFullName()); } else {
             * vb10.setSubjectID(""); vb10.setVcsubject(""); }
             * vb10.setVcabstact("银行存款"); vb10.setRowIndex("6");
             *
             * Voucher vouch = new Voucher(); vouch.setVoucherHead(vh);
             * List<VoucherBody> vbL = new ArrayList<VoucherBody>();
             * vbL.add(vb5); vbL.add(vb6); vbL.add(vb7); vbL.add(vb8);
             * vbL.add(vb9); vbL.add(vb10); vouch.setVoucherBodyList(vbL); //
             * vouch.setVoucherHead(vh); voucherHeadDao.insertVouchHead(vh); for
             * (int j = 0; j < vbL.size(); j++) {
             * voucherBodyDao.insertVouchBody(vbL.get(j)); } boolean boo =
             * vatService.checkVouch(para, vouch); if (boo) {
             * tBasicSubjectMessageMapper.chgSubAmountByCreate(para, vouch); }
             *
             * } } }
             */
            // Map<String, Object> pa = new HashMap<String, Object>();
            // pa.put("accountID", account.getAccountID());
            // pa.put("busDate", busDate);

            // periodStatusDao.updStatu1(pa);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }

        return returnList;
    }

    /*
     * @Transactional(rollbackFor = BusinessException.class) public void
     * upBankVouch() throws BusinessException { try { if
     * (!this.bankVo.isEmpty()) { Map<String, Object> vmap = new HashMap<>();
     * vmap.put("vhid", this.bankVo); vatDao.upVouchBody(vmap);
     * vatDao.upVouch(vmap); } this.bankVo.clear(); } catch (Exception e) {
     * this.bankVo.clear(); e.printStackTrace(); throw new BusinessException();
     * } }
     */

    public void upBankVouch() throws BusinessException {
        List<String> list = vouchIDS.get();
        if (list != null && list.size() > 0) {
            Map<String, Object> vmap = new HashMap<>();
            vmap.put("vhid", list);
            vatDao.upVouchBody(vmap);
            vatDao.upVouch(vmap);
            list.clear();
        }
    }

    /*
     * public void upBankSub(Map<String, Object> upParam) throws
     * BusinessException { if (!getBankLocal().isEmpty()) {
     * vatService.upSub(getBankLocal(), upParam); } // 使用完需要清除，否则有可能会造成内存泄漏。内存池
     * 里的 本地线程 可能永远会存在。 getBankLocal().clear(); }
     */

    /****************** 银行创建凭证方法 **************************/
    public Voucher createVouch(Map<String, Object> param) throws BusinessException {
        // 构造凭证
        VoucherHead vouchHead = new VoucherHead();
        String vouchID = UUIDUtils.getUUID();
        String busDate = param.get("busDate").toString();
        Account account = (Account) param.get("account");
        User user = (User) param.get("user");
        Object flagObj = param.get("flag");
        vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
        if (null == flagObj) {
            return null;
        }
        String flag = param.get("flag").toString();
        double amount = Double.parseDouble(param.get("amount").toString());
        // String dfAccountName = null;
        Object dfAccountNameObj = param.get("dfAccountName");
        Object subjectInfoObj = param.get("subjectInfo");
        Object zyObj = param.get("zy");
        String fullName = null;
        String subName = null;
        String subjectID = null;
        String sjSubjectID = null;
        boolean needCreate = true;
        if (subjectInfoObj != null) { // 1122_应收账款_货款
            String[] subjectInfo = subjectInfoObj.toString().split("_");
            if (subjectInfo.length == 2) {
                // 库存现金
                /*
                 * if (desc.contains("备用金")) { return "1001_库存现金"; } else if
                 * (desc.contains("存款")) { return "1001_库存现金"; } else if
                 * (desc.contains("现金")) { return "1001_库存现金"; }
                 */
                subjectID = subjectInfo[0];
                if ("1001".equals(subjectID)) {
                    fullName = subjectInfo[1];
                    subName = subjectInfo[1];
                    needCreate = false;
                }
            } else if (subjectInfo.length == 3) {
                sjSubjectID = subjectInfo[0]; // 1122
                fullName = subjectInfo[1] + "_" + subjectInfo[2]; // 应收账款_货款
                subName = subjectInfo[2]; // 货款
            } else if (subjectInfo.length == 1) {
                subName = subjectInfo[0];
            }
        }
        inits(account.getAccountID(), busDate, user.getUserID());
        vouchHead.setPeriod(busDate);
        vouchHead.setVouchID(vouchID);
        vouchHead.setVcDate(new Date());
        vouchHead.setAccountID(account.getAccountID());
        // vouchHead.setSource(2);
        vouchHead.setVoucherNO(0);
        vouchHead.setCreateDate(System.currentTimeMillis());
        vouchHead.setCreatepsn(user.getUserName());
        vouchHead.setCreatePsnID(user.getUserID());
        vouchHead.setTotalCredit(amount);
        vouchHead.setTotalDbit(amount);
        vouchHead.setSource(1);
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("accountID", account.getAccountID());
        par.put("period", busDate);
        int maxNO = voucherHeadDao.getMaxVoucherNo(par);
        vouchHead.setVoucherNO(maxNO);
        // 构造子凭证1
        VoucherBody vouchBody1 = new VoucherBody();
        vouchBody1.setVouchAID(UUIDUtils.getUUID());
        vouchBody1.setVouchID(vouchID);
        vouchBody1.setPeriod(busDate);
        vouchBody1.setAccountID(account.getAccountID());

        // 构造子凭证2
        VoucherBody vouchBody2 = new VoucherBody();
        vouchBody2.setVouchAID(UUIDUtils.getUUID());
        vouchBody2.setVouchID(vouchID);
        vouchBody2.setPeriod(busDate);
        vouchBody2.setAccountID(account.getAccountID());
        if ("1".equals(flag)) {// 1借2贷
            vouchBody1.setRowIndex("1");
            vouchBody1.setDebitAmount(amount);
            vouchBody1.setDirection("1");
            if (null == fullName) {
                vouchBody1.setVcsubject("");
            } else {
                vouchBody1.setVcsubject(fullName);
            }
            if (subjectID == null && subName != null) {
                if (needCreate) {
                    SubjectMessage subjectMessage = vatService.querySub(subName, sjSubjectID, "7");
                    if (null != subjectMessage) {
                        subjectID = subjectMessage.getSub_code();
                    } else if (null != sjSubjectID) {
                        subjectID = vatService.getNumber(sjSubjectID, "7", sjSubjectID + "000");
                        vatService.createSub(subjectID, sjSubjectID, subName, fullName);
                    }
                }
            }
            if (null != subjectID) {
                vouchBody1.setSubjectID(subjectID);
            } else {
                vouchBody1.setSubjectID("");
            }
            Object subNameObj = param.get("mz");
            String sName = null;
            String subCode3 = null;
            if (null != subNameObj) {
                sName = subNameObj.toString();
            }
            if (sName != null) {
                SubjectMessage subjectMessage2 = vatService.querySub(sName, "1002", "7", "1");
                if (subjectMessage2 != null) {
                    subCode3 = subjectMessage2.getSub_code();
                    sName = subjectMessage2.getSub_name();
                } else {
                    subCode3 = vatService.getNumber("1002", "7", "1002000");
                    subjectMessage2 = vatService.createSub(subCode3, "1002", sName, "银行存款_" + sName);
                }
                vouchBody2.setVcsubject("银行存款_" + sName);
            }
            vouchBody2.setSubjectID(subCode3);

            vouchBody2.setDirection("2");

            vouchBody2.setCreditAmount(amount);
            vouchBody2.setRowIndex("2");

        } else {
            vouchBody1.setRowIndex("2");
            vouchBody1.setCreditAmount(amount);
            vouchBody1.setDirection("2");
            if (null != fullName) {
                vouchBody1.setVcsubject(fullName);
            } else {
                vouchBody1.setVcsubject("");
            }
            // sjSubjectID = subjectInfo[0]; //1122
            // fullName = subjectInfo[1] + "_" + subjectInfo[2]; //应收账款_货款
            // subName = subjectInfo[2]; //货款
            if (subjectID == null && subName != null) {
                if (needCreate) {
                    SubjectMessage subjectMessage = vatService.querySub(subName, sjSubjectID, "7");
                    if (null != subjectMessage) {
                        subjectID = subjectMessage.getSub_code();
                    } else if (sjSubjectID != null) {
                        subjectID = vatService.getNumber(sjSubjectID, "7", sjSubjectID + "000");
                        vatService.createSub(subjectID, sjSubjectID, subName, fullName);
                    }
                }
            }
            if (subjectID != null) {
                vouchBody1.setSubjectID(subjectID);
            } else {
                vouchBody1.setSubjectID("");
                vouchBody1.setIsproblem("1");
                vouchHead.setIsproblem("1");
            }

            Object subNameObj = param.get("mz");
            String sName = null;
            String subCode3 = null;
            if (null != subNameObj) {
                sName = subNameObj.toString();
            }
            if (sName != null) {
                SubjectMessage subjectMessage2 = vatService.querySub(sName, "1002", "7", "1");
                if (subjectMessage2 != null) {
                    subCode3 = subjectMessage2.getSub_code();
                    sName = subjectMessage2.getSub_name();
                } else {
                    subCode3 = vatService.getNumber("1002", "7", "1002000");
                    subjectMessage2 = vatService.createSub(subCode3, "1002", sName, "银行存款_" + sName);
                }
                vouchBody2.setVcsubject("银行存款_" + sName);
            }
            vouchBody2.setSubjectID(subCode3);
            vouchBody2.setDirection("1");
            vouchBody2.setDebitAmount(amount);
            vouchBody2.setRowIndex("1");
        }

        // vouchBody1.setVcabstact(desc);
        String rowIndex = vouchBody1.getRowIndex();
        if ("1".equals(rowIndex)) {
            if ("1".equals(flag)) {
                vouchBody1.setVcabstact("付款");
            } else {
                vouchBody1.setVcabstact("收款");
            }
        } else {
            if ("1".equals(flag)) {
                vouchBody2.setVcabstact("付款");
            } else {
                vouchBody2.setVcabstact("收款");
            }
        }
        vouchBody2.setIsproblem("2");
        voucherHeadDao.insertVouchHead(vouchHead);
        // voucherBodyDao.insertVouchBody(vouchBody1);
        // voucherBodyDao.insertVouchBody(vouchBody2);
        if (vouchHead != null) {
            VoucherBody[] arr = {vouchBody1, vouchBody2};
            vatService.insertVouchBatch(arr);
        }
        Voucher voucher = new Voucher();
        voucher.setVoucherHead(vouchHead);
        List<VoucherBody> list = new ArrayList<VoucherBody>();
        list.add(vouchBody1);
        list.add(vouchBody2);
        voucher.setVoucherBodyList(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountID", account.getAccountID());
        map.put("busDate", busDate);
        // boolean canChg = (null != vouchBody1.getSubjectID() &&
        // !"".equals(vouchBody1.getSubjectID())
        // && vouchBody2.getSubjectID() != null &&
        // !"".equals(vouchBody2.getSubjectID()));

        /*
         * if (canChg) { tBasicSubjectMessageMapper.chgSubAmountByCreate(map,
         * voucher); //更新到科目 }
         */
        map.put("vhid", getVouchIDS());
        // map.put("vhid", this.bankVo);
        boolean bool = vatService.checkVouch(map, voucher);
        if (bool) {
            // 3 getBankLocal().add(voucher);
            // 2 this.bankVo.add(voucher);
            // 恢复第一种方法
            // 需要做明细账
            tBasicSubjectMessageMapper.chgSubAmountByCreate(map, voucher);
            // // 更新到科目
        }
        return voucher;
    }

    private void inits(String accoutID, String busDate, String userID) {

        vatService.subinit(accoutID, busDate, userID, userID);
    }

    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    public List<Voucher> bankVouch(HttpSession session) throws BusinessException {
        List<Voucher> returnList = new ArrayList<Voucher>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            User user = (User) sessionMap.get("user");
            String busDate = sessionMap.get("busDate").toString();
            Map<String, String> param = new HashMap<String, String>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            // 中信银行
            List<TccbBank> ccbList = tccbBankDao.queryAll(param);
            if (null != ccbList && ccbList.size() > 0) {
                for (TccbBank tccbBank : ccbList) {
                    Map<String, Object> pa = new HashMap<String, Object>();
                    pa.put("account", account);
                    pa.put("busDate", busDate);
                    pa.put("user", user);
                    double debitAmount = tccbBank.getDebitAmount();
                    double creditAmount = tccbBank.getCreditAmount();
                    if (debitAmount != 0.0) {
                        pa.put("amount", debitAmount);
                        pa.put("flag", "1");
                    }
                    if (creditAmount != 0.0) {
                        pa.put("amount", creditAmount);
                        pa.put("flag", "2");
                    }
                    pa.put("subName", tccbBank.getDes());
                    pa.put("dfAccountName", tccbBank.getWhAccountName());
                    Voucher voucher = createVouch(pa);
                    if (null != voucher) {
                        returnList.add(voucher);
                    }
                }
            }

        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
        return returnList;
    }

    @Override
    // desc 摘要 remark附言 dfAccountName对方户名
    public String getSubject(String desc, String remark, String dfAccountName, String flag, String accountID,
                             String busDate, String bankName) throws BusinessException {

        // 根据公司名查询科目
        if (null != dfAccountName && !"".equals(dfAccountName)) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", accountID);
            param.put("busDate", busDate);
            param.put("subName", dfAccountName);
            List<TBasicSubjectMessage> list = tBasicSubjectMessageMapper.querySubjectByName(param);
            if (null != list && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    TBasicSubjectMessage tBasicSubjectMessage = list.get(j);
                    if (null != tBasicSubjectMessage) {
                        String sub_code = tBasicSubjectMessage.getSubCode();
                        if ("2".equals(flag)) {
                            if (null != sub_code && (sub_code.startsWith("1122") || sub_code.startsWith("1221"))) {
                                String full_name = tBasicSubjectMessage.getFullName().replaceAll(" ", "");
                                return sub_code + "_" + full_name;
                            }
                        } else {
                            if (null != sub_code && (sub_code.startsWith("2202") || sub_code.startsWith("2241"))) {
                                String full_name = tBasicSubjectMessage.getFullName().replaceAll(" ", "");
                                return sub_code + "_" + full_name;
                            }
                        }
                    }
                }
            }
        }
        if (desc != null) {
            // 财务费用
            if (desc.contains("手续") || desc.contains("自助交易费")) {
                return "6603_财务费用_手续费";
            } else if (desc.contains("对公")) {
                return "6603_财务费用_对公费用";
            } else if (desc.contains("利息") || desc.contains("结息") || desc.contains("活期结算")) {
                return "6603_财务费用_利息费";
            } else if (desc.contains("账户管理费")) {
                return "6603_财务费用_账户管理费";
            } else if (desc.contains("服务费")) {
                return "6603_财务费用_服务费";
            }
            // 管理费用
            if (desc.contains("社保") || desc.contains("社会保险")) {
                return "6602_管理费用_社保";
            } else if (desc.contains("公积金") || desc.contains("住房积金")) {
                return "6602_管理费用_公积金";
            } else if (desc.contains("差旅费")) {
                return "6602_管理费用_差旅费";
            } else if (desc.contains("保险金") || desc.contains("保险费")) {
                return "6602_管理费用_保险金";
            } else if (desc.contains("房租水电")) {
                return "6602_管理费用_房租水电费";
            } else if (desc.contains("工资")) {
                return "6602_管理费用_工资";
            } else if (desc.contains("电信")) {
                return "6602_管理费用_电信";
            } else if (desc.contains("电话")) {
                return "6602_管理费用_电话";
            } else if (desc.contains("劳务费")) {
                return "6602_管理费用_劳务费";
            }
            // 应交税费
            if (desc.contains("税")) {
                // return "2221_应交税费_税款";
            }
            /*
             * if (desc.contains("纳税款")) { return "2221_应交税费_纳税款"; } else if
             * (desc.contains("国家税务局")) { return "2221_应交税费_国家税务局"; } else if
             * (desc.contains("代扣税")) { return "2221_应交税费_代扣税"; }
             */
            // 应收应付
            /*
             * if (desc.contains("货款")) { if ("1".equals(flag)) { return
             * "2202_应付账款_货款"; } else if ("2".equals(flag)) { return
             * "1122_应收账款_货款"; } }
             */
            // 其他应收应付
            if (desc.contains("借款")) {
                return "2241_其他应付_借款";
            } else if (desc.contains("还款")) {
                return "1221_其他应收_还款";
            }

            // 库存现金
            if (desc.contains("备用金")) {
                return "1001_库存现金";
            } else if (desc.contains("存款")) {
                return "1001_库存现金";
            } else if (desc.contains("现金")) {
                return "1001_库存现金";
            }
        }
        if (null != remark) {
            // 财务费用
            if (remark.contains("手续") || remark.contains("自助交易费")) {
                return "6603_财务费用_手续费";
            } else if (remark.contains("对公")) {
                return "6603_财务费用_对公费用";
            } else if (remark.contains("利息") || remark.contains("结息") || remark.contains("活期结算")) {
                return "6603_财务费用_利息费";
            } else if (remark.contains("账户管理费")) {
                return "6603_财务费用_账户管理费";
            } else if (remark.contains("服务费")) {
                return "6603_财务费用_服务费";
            }
            // 管理费用
            if (remark.contains("社保") || remark.contains("社会保险")) {
                return "6602_管理费用_社保";
            } else if (remark.contains("公积金") || remark.contains("住房积金")) {
                return "6602_管理费用_公积金";
            } else if (remark.contains("差旅费")) {
                return "6602_管理费用_差旅费";
            } else if (remark.contains("保险金") || remark.contains("保险费")) {
                return "6602_管理费用_保险费";
            } else if (remark.contains("房租水电")) {
                return "6602_管理费用_房租水电费";
            } else if (remark.contains("工资")) {
                return "6602_管理费用_工资";
            } else if (remark.contains("电信")) {
                return "6602_管理费用_电信";
            } else if (remark.contains("电话")) {
                return "6602_管理费用_电话";
            } else if (remark.contains("劳务费")) {
                return "6602_管理费用_劳务费";
            }
            // 应交税费
            if (remark.contains("税")) {
                System.out.println("11");
                // return "2221_应交税费_税款";
            }
            /*
             * if (remark.contains("纳税款")) { return "2221_应交税费_纳税款"; } else if
             * (remark.contains("国家税务局")) { return "2221_应交税费_国家税务局"; } else if
             * (remark.contains("代扣税")) { return "2221_应交税费_代扣税"; }
             */
            // 应收应付
            /*
             * if (remark.contains("货款")) { if ("1".equals(flag)) { return
             * "2202_应付账款_货款"; } else if ("2".equals(flag)) { return
             * "1122_应收账款_货款"; } }
             */

            // 应收应付
            if (remark.contains("借款")) {
                return "2241_其他应付_借款";
            } else if (remark.contains("还款")) {
                return "1221_其他应收_还款";
            }

            // 库存现金
            if (remark.contains("备用金")) {
                return "1001_库存现金";
            } else if (remark.contains("存款")) {
                return "1001_库存现金";
            } else if (remark.contains("现金")) {
                return "1001_库存现金";
            }
        }

        return null;
    }

    // vouchIDS
    /*
     * public static List<Voucher> getBankLocal() { List<Voucher> bk =
     * bankLocal.get(); if (bk == null) { bk = new ArrayList<>();
     * bankLocal.set(bk); } return bk; }
     */

    public static List<String> getVouchIDS() {
        List<String> bk = vouchIDS.get();
        if (bk == null) {
            bk = new ArrayList<String>();
            vouchIDS.set(bk);
        }
        return bk;
    }

    @Override
    public Map<String, Object> updBank(String infos) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String[] oneInfos = infos.split("\\|\\[\\]\\|");
            for (int i = 0; i < oneInfos.length; i++) {
                String[] oneInfo = oneInfos[i].split("\\#\\@\\!");
                // 主键
                String id = oneInfo[0];
                // String[] subInfo = oneInfo[1].split("\\|\\{\\}\\|");
                String[] subInfo = oneInfo[1].split("\\(\\|\\)");
                // 科目ID
                String subID = null;
                StringBuffer subIDSb = new StringBuffer();
                // 科目编码
                String subCode = null;
                StringBuffer subCodeSb = new StringBuffer();
                // 科目名称
                String subName = null;
                StringBuffer subNameSb = new StringBuffer();
                // 科目全名
                String subFullName = null;
                StringBuffer subFullNameSb = new StringBuffer();
                String subAmount = null;
                StringBuffer subAmountSb = new StringBuffer();
                if (subInfo.length == 1) {
                    String[] sub = subInfo[0].split("\\|\\{\\}\\|");
                    subID = sub[0];
                    subCode = sub[1];
                    subName = sub[2];
                    subFullName = sub[3];
                } else if (subInfo.length > 1) {
                    for (int j = 0; j < subInfo.length; j++) {
                        String[] subInfos = subInfo[j].split("\\|\\{\\}\\|");
                        // String[] subInf = subInfos[j].split("\\|");
                        subIDSb.append(subInfos[0]).append("|");
                        if (j != subInfo.length - 1) {
                            subCodeSb.append(subInfos[1]).append("{[|]}");
                        } else {
                            subCodeSb.append(subInfos[1]);
                        }
                        subNameSb.append(subInfos[2]).append("|");
                        subFullNameSb.append(subInfos[3]).append("|");
                    }
                    subID = subIDSb.toString().substring(0, subIDSb.toString().length() - 1);
                    // subCode = subCodeSb.toString().substring(0,
                    // subCodeSb.toString().length() -
                    // 1);
                    subName = subNameSb.toString().substring(0, subNameSb.toString().length() - 1);
                    subFullName = subFullNameSb.toString().substring(0, subFullNameSb.toString().length() - 1);
                    subCode = subCodeSb.toString();
                }

                // 银行库表类型
                String intBankType = oneInfo[2];
                // 根据银行类型库表，主键修改银行对账单表
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("id", id);
                param.put("sysSubjectCode", subCode);
                param.put("sysSubjectName", subName);
                param.put("sysSubjectID", subID);
                param.put("sysSubjectFullName", subFullName);
                if ("1".equals(intBankType)) {
                    // 中信银行
                    tccbBankDao.updByID(param);
                } else if ("2".equals(intBankType)) {
                    // 深农行
                    szrcbBankBillDao.updByID(param);
                } else if ("3".equals(intBankType)) {
                    // 工商银行
                    icbcBankDao.updByID(param);
                } else if ("4".equals(intBankType)) {
                    // 交通银行 t_cm_bkbill_bcm
                    bcmDao.updByID(param);
                } else if ("5".equals(intBankType)) {
                    // 交通银行 t_cm_bkbill_bcm1
                    bcm1Dao.updByID(param);
                } else if ("6".equals(intBankType)) {
                    // 建设银行
                    jsBankDao.updByID(param);
                } else if ("7".equals(intBankType)) {
                    // 平安银行
                    paBankDao.updByID(param);
                } else if ("8".equals(intBankType)) {
                    // 招商银行
                    zsBankDao.updByID(param);
                } else if ("9".equals(intBankType)) {
                    // 中国银行
                    tCmBkbillBocMapper.updByID(param);
                } else if ("10".equals(intBankType)) {
                    // 农业银行 t_cm_bkbill_ny
                    nyBankDao.updByID(param);
                } else if ("11".equals(intBankType)) {
                    // 农业银行t_cm_bkbill_ny_new
                    nyBankDao.updByIDNew(param);
                }
            }
            result.put("success", "true");
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            throw new BusinessException("保存银行对账单信息异常!");
        }
    }

    /****************** 银行创建凭证方法 **************************/
    public Voucher createVouch1(Map<String, Object> param, Map<String, String> bsMap) throws BusinessException {
        // 构造凭证
        VoucherHead vouchHead = new VoucherHead();
        VoucherBody bankVb = new VoucherBody();
        VoucherBody vb = new VoucherBody();
        String vouchID = UUIDUtils.getUUID();
        String busDate = param.get("busDate").toString();
        Account account = (Account) param.get("account");
        User user = (User) param.get("user");
        vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
        String flag = param.get("flag").toString();
        double amount = Double.parseDouble(param.get("amount").toString());
        // String bankAccount = param.get("bankAccount").toString().trim();
        vouchHead.setPeriod(busDate);
        vouchHead.setVouchID(vouchID);
        vouchHead.setVcDate(new Date());
        vouchHead.setAccountID(account.getAccountID());
        // vouchHead.setSource(2);
        vouchHead.setVoucherNO(0);
        vouchHead.setCreateDate(System.currentTimeMillis());
        vouchHead.setCreatepsn(user.getUserName());
        vouchHead.setCreatePsnID(user.getUserID());
        vouchHead.setTotalCredit(amount);
        vouchHead.setTotalDbit(amount);
        vouchHead.setSource(1);
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("accountID", account.getAccountID());
        par.put("period", busDate);
        int maxNO = voucherHeadDao.getMaxVoucherNo(par);
        vouchHead.setVoucherNO(maxNO);
        Object bankAccountObj = param.get("bankAccount");
        String subCode = param.get("subCode").toString();
        String subFullName = param.get("subFullName").toString();
        String bankAccount = null;
        String bankAccountInfo = null;
        bankVb.setVouchAID(UUIDUtils.getUUID());
        bankVb.setVouchID(vouchID);
        bankVb.setPeriod(busDate);
        bankVb.setAccountID(account.getAccountID());
        bankVb.setDirection("1".equals(flag) ? "2" : "1");
        vb.setPeriod(busDate);
        if ("1".equals(flag)) {
            bankVb.setRowIndex("2");
            bankVb.setCreditAmount(amount);
            vb.setVcabstact("付款");
            vb.setRowIndex("1");
            vb.setDebitAmount(amount);
            vb.setDirection("1");
        } else {
            bankVb.setRowIndex("1");
            bankVb.setDebitAmount(amount);
            bankVb.setVcabstact("收款");
            if (subCode.startsWith("6603")) {
                vb.setRowIndex("2");
                vb.setDebitAmount(0 - amount);
                // vb.setVcabstact("收款");
                vb.setDirection("1");
            } else {
                vb.setRowIndex("2");
                vb.setCreditAmount(amount);
                vb.setDirection("2");
            }
        }
        if (null != bankAccountObj) {
            bankAccount = bankAccountObj.toString();
        }
        if (bankAccount != null) {
            bankAccountInfo = bsMap.get(bankAccount);
            if (bankAccountInfo == null) {
                throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
            }
            bankVb.setSubjectID(bankAccountInfo.split("\\|")[1]);
            bankVb.setVcsubject(bankAccountInfo.split("\\|")[3]);
        }
        vb.setVouchAID(UUIDUtils.getUUID());
        vb.setVouchID(vouchID);
        vb.setPeriod(busDate);
        vb.setAccountID(account.getAccountID());
        vb.setSubjectID(subCode);
        vb.setVcsubject(subFullName);

        Voucher voucher = new Voucher();
        List<VoucherBody> bodyList = new ArrayList<VoucherBody>();
        bodyList.add(bankVb);
        bodyList.add(vb);
        voucher.setVoucherBodyList(bodyList);
        voucher.setVoucherHead(vouchHead);
        voucherHeadDao.insertVouchHead(vouchHead);
        voucherBodyDao.insertVouchBody(bankVb);
        voucherBodyDao.insertVouchBody(vb);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountID", account.getAccountID());
        map.put("busDate", busDate);
        boolean bool = vatService.checkVouch(map, voucher);
        if (bool) {
            tBasicSubjectMessageMapper.chgSubAmountByCreate(map, voucher);
        }
        return voucher;
    }

    /****************** 银行创建凭证方法 **************************/
    public Voucher createVouch(Map<String, Object> param, Map<String, String> bsMap) throws BusinessException {
        // 首先判断是否是发放薪资的凭证
        long begin = System.currentTimeMillis();
        /*
         * Object archObj = param.get("isArch"); Object sbObj =
         * param.get("isSb"); Object gjjObj = param.get("isGjj"); if (sbObj !=
         * null && "true".equals(sbObj.toString())) { return null; } else if
         * (gjjObj != null && "true".equals(gjjObj.toString())) { return null; }
         * else if (archObj != null && "true".equals(archObj.toString())) {
         * return null; } else {
         */
        // 构造凭证
        VoucherHead vouchHead = new VoucherHead();
        VoucherBody bankVb = new VoucherBody();
        String vouchID = UUIDUtils.getUUID();
        String busDate = param.get("busDate").toString();
        Account account = (Account) param.get("account");
        User user = (User) param.get("user");
        vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
        String flag = param.get("flag").toString();
        double amount = Double.parseDouble(param.get("amount").toString());
        // String bankAccount = param.get("bankAccount").toString().trim();
        vouchHead.setPeriod(busDate);
        vouchHead.setVouchID(vouchID);
        vouchHead.setVcDate(new Date());
        vouchHead.setAccountID(account.getAccountID());
        // vouchHead.setSource(2);
        vouchHead.setVoucherNO(0);
        vouchHead.setCreateDate(System.currentTimeMillis());
        vouchHead.setCreatepsn(user.getUserName());
        vouchHead.setCreatePsnID(user.getUserID());
        vouchHead.setTotalCredit(amount);
        vouchHead.setTotalDbit(amount);
        vouchHead.setSource(1);
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("accountID", account.getAccountID());
        par.put("period", busDate);
        int maxNO = voucherHeadDao.getMaxVoucherNo(par);
        vouchHead.setVoucherNO(maxNO);
        Object bankAccountObj = param.get("bankAccount");
        String bankAccount = null;
        String bankAccountInfo = null;
        Object subCodeObj = param.get("subCode");
        if (subCodeObj == null) {
            throw new BusinessException("请先完善对账单科目信息!");
        }
        String subCodeStr = param.get("subCode").toString();
        String subFullNameStr = param.get("subFullName").toString();
        String[] subFullNameInfo = subFullNameStr.split("\\|");
        bankVb.setVouchAID(UUIDUtils.getUUID());
        bankVb.setVouchID(vouchID);
        bankVb.setPeriod(busDate);
        bankVb.setAccountID(account.getAccountID());
        bankVb.setDirection("1".equals(flag) ? "2" : "1");
        String[] subCodess = subCodeStr.split("\\{\\[\\|\\]\\}");
        String subCodes = subCodess[0];
        String[] subCodeInfo = subCodes.split("\\|");
        boolean isSingle = false;
        if (subCodeInfo.length == 1) {
            isSingle = true;
        }
        Voucher voucher = new Voucher();
        List<VoucherBody> vbList = new ArrayList<VoucherBody>();
        int rowIndex = 1;
        if ("1".equals(flag)) {
            for (int i = 0; i < subCodess.length; i++) {
                // 科目金额
                String subAmount = null;
                if (isSingle) {
                    subAmount = amount + "";
                } else {
                    subAmount = subCodess[i].split("\\|")[1];
                }
                // 科目全名
                String subFullName = subFullNameInfo[i];
                VoucherBody vb = new VoucherBody();
                vb.setVouchAID(UUIDUtils.getUUID());
                vb.setVouchID(vouchID);
                vb.setPeriod(busDate);
                vb.setAccountID(account.getAccountID());
                vb.setRowIndex(rowIndex + "");
                if (rowIndex == 1) {
                    vb.setVcabstact("付款");
                }
                vb.setDebitAmount(Double.parseDouble(subAmount));
                vb.setDirection("1");
                vb.setSubjectID(subCodess[i].split("\\|")[0]);
                vb.setVcsubject(subFullName);
                rowIndex++;
                vbList.add(vb);
            }
            bankVb.setRowIndex(rowIndex + "");
            bankVb.setCreditAmount(amount);
            if (null != bankAccountObj) {
                bankAccount = bankAccountObj.toString();
            }
            if (bankAccount != null) {
                bankAccountInfo = bsMap.get(bankAccount);
                if (bankAccountInfo == null) {
                    throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                }
                bankVb.setSubjectID(bankAccountInfo.split("\\|")[1]);
                bankVb.setVcsubject(bankAccountInfo.split("\\|")[3]);
            }
            vbList.add(bankVb);

        } else {
            bankVb.setVcabstact("收款");
            bankVb.setRowIndex(rowIndex + "");
            bankVb.setDebitAmount(amount);
            if (null != bankAccountObj) {
                bankAccount = bankAccountObj.toString();
            }
            if (bankAccount != null) {
                bankAccountInfo = bsMap.get(bankAccount);
                if (bankAccountInfo == null) {
                    throw new BusinessException("请前往银行设置,添加银行账号与科目的映射!");
                }
                bankVb.setSubjectID(bankAccountInfo.split("\\|")[1]);
                bankVb.setVcsubject(bankAccountInfo.split("\\|")[3]);
            }
            for (int i = 0; i < subCodess.length; i++) {
                rowIndex++;
                // 科目编码
                String[] subCode1 = subCodess[i].split("\\|");
                String subCode = subCode1[0];
                // 科目金额
                // 科目金额
                String subAmount = null;
                if (isSingle) {
                    subAmount = amount + "";
                } else {
                    subAmount = subCodess[i].split("\\|")[1];
                }
                // 科目全名
                String subFullName = subFullNameInfo[i];
                VoucherBody vb = new VoucherBody();
                vb.setVouchAID(UUIDUtils.getUUID());
                vb.setVouchID(vouchID);
                vb.setPeriod(busDate);
                vb.setAccountID(account.getAccountID());
                vb.setRowIndex(rowIndex + "");
                if (subCode.startsWith("6603")) {
                    vb.setDebitAmount(0 - Double.parseDouble(subAmount));
                    vb.setDirection("1");
                } else {
                    vb.setCreditAmount(Double.parseDouble(subAmount));
                    vb.setDirection("2");
                }
                vb.setSubjectID(subCode);
                vb.setVcsubject(subFullName);
                vbList.add(vb);
            }
            vbList.add(bankVb);
        }
        voucherHeadDao.insertVouchHead(vouchHead);
        for (int s = 0; s < vbList.size(); s++) {
            voucherBodyDao.insertVouchBody(vbList.get(s));
        }
        voucher.setVoucherBodyList(vbList);
        voucher.setVoucherHead(vouchHead);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountID", account.getAccountID());
        map.put("busDate", busDate);
        long end0 = System.currentTimeMillis();
        boolean bool = vatService.checkVouch(map, voucher);
        long end1 = System.currentTimeMillis();

        if (bool) {
            tBasicSubjectMessageMapper.chgSubAmountByCreate(map, voucher);
        }
        long end2 = System.currentTimeMillis();
        System.out.println((double) (end0 - begin) / 1000);
        System.out.println((double) (end1 - end0) / 1000);
        System.out.println((double) (end2 - end1) / 1000);

        return voucher;
        // }
    }
}
