package com.wqb.service.bank.impl;

import com.wqb.common.*;
import com.wqb.dao.bank.Bank2SubjectDao;
import com.wqb.dao.bank.TCmBkbillBocMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.model.*;
import com.wqb.model.bank.TCmBkbillBoc;
import com.wqb.service.bank.BankService;
import com.wqb.service.bank.TCmBkbillBocService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("tCmBkbillBocService")
public class TCmBkbillBocServiceImpl implements TCmBkbillBocService {

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    TCmBkbillBocMapper tCmBkbillBocMapper;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    BankService bankService;
    @Autowired
    VatService vatService;
    @Autowired
    Bank2SubjectDao bank2SubjectDao;

    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> uploadTCmBkbillBoc(MultipartFile bkillBocExcel, HttpSession session) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        int no = 0;
        // 获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) userDate.get("user");
        Account account = (Account) userDate.get("account");
        String busDate = (String) userDate.get("busDate");
        // 用户id
        String userId = user.getUserID();
        // 账套id
        String accountId = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
            result.put("message", "fail");
        }
        Map<String, String> map = new HashMap<>();
        map.put("userID", userId);
        map.put("accountID", accountId);
        map.put("userName", user.getUserName());
        try {
            if (null != bkillBocExcel) {
                // 1获取文件上传路径
                // String filePath = PathUtil.getClasspath() +
                // Constrants.FILEPATHFILE; //
                // wtpwebapps/wqb/WEB-INF/classes/../../files/
                String filePath = filePaths + "/" + user.getUserID() + "/" + accountId + "/" + busDate + "/";
                // 2获取文件名
                String fileName = FileUpload.fileUp(bkillBocExcel, filePath,
                        "BkillBoc-" + user.getUserID() + "-" + System.currentTimeMillis());
                // 3读取excel表格数据
                List<Object> list = ObjectExcelRead.readExcel(filePath, fileName, 9, 0, 0);
                File file = new File(filePath, fileName);
                FileInputStream is = new FileInputStream(file);
                ExcelReader excelReader = new ExcelReader();
                String bankAccount = excelReader.readExcelContent(is, 1, 1);
                System.out.println(bankAccount);
                vatService.subinit(accountId, busDate, userId, user.getUserName());
                if (null != bankAccount && !"".equals(bankAccount)) {
                    Map<String, Object> pa1 = new HashMap<String, Object>();
                    pa1.put("accountID", accountId);
                    pa1.put("busDate", busDate);
                    pa1.put("bankAccount", bankAccount);
                    List<TBasicSubjectMessage> li1 = tBasicSubjectMessageMapper.queryBankSub(pa1);
                    String subID = null;
                    String subject = null;
                    if (li1 == null || li1.isEmpty()) {
                        // 创建科目
                        subject = vatService.getNumber("1002", "7", "1002000");
                        SubjectMessage sm = vatService.createSub(subject, "1002", "中国银行" + bankAccount,
                                "银行存款_" + "中国银行" + bankAccount);
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
                    b2s.setAccountID(accountId);
                    // 银行账户
                    b2s.setBankAccount(bankAccount);
                    // 银行科目主键
                    b2s.setSubID(subID);
                    // 银行名称
                    b2s.setBankName("中国银行");
                    // 银行类型
                    b2s.setBankType("中国银行");
                    // 币种
                    b2s.setCurrency("人民币");
                    // 科目名称
                    b2s.setSubName("中国银行" + bankAccount);
                    // 科目全名
                    b2s.setSubFullName("银行存款_" + "中国银行" + bankAccount);
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
                            if (li.get(i).getAccountID().equals(accountId)) {
                                canAdd = false;
                            }
                        }
                        if (canAdd) {
                            bank2SubjectDao.addBank2Subject(b2s);
                        }
                    }
                } else {
                    throw new BusinessException("请检查您导入的中国银行对账单,请先完善银行账户信息");
                }
                List<TCmBkbillBoc> tCmBkbillBocList = new ArrayList<TCmBkbillBoc>();

                // 循环遍历每一行excel表数据
                for (int i = 0; i < list.size(); i++) {
                    TCmBkbillBoc tCmBkbillBoc = new TCmBkbillBoc();
                    Map bkbillBocMap = (Map) list.get(i);

                    String pkBkbillBoc = UUIDUtils.getUUID();
                    tCmBkbillBoc.setPkBkbillBoc(pkBkbillBoc); // 中国银行主键

                    tCmBkbillBoc.setAccountId(accountId);// 账套ID
                    tCmBkbillBoc.setUserId(userId);// 用户ID

                    /** 创建人ID */
                    tCmBkbillBoc.setCreatePsnId(userId);

                    /** 创建人 */
                    tCmBkbillBoc.setCreatePsn(userId);

                    // 做帐期间
                    tCmBkbillBoc.setPeriod(busDate);

                    // 更新時間
                    tCmBkbillBoc.setUpdateDate(new Date());

                    String transactionType = bkbillBocMap.get("var0") == null ? null
                            : bkbillBocMap.get("var0").toString(); // 交易类型
                    if ("".equals(transactionType) || transactionType == null) {
                        break;
                    }
                    tCmBkbillBoc.setTransactionType(transactionType);

                    String businessType = bkbillBocMap.get("var1") == null ? null : bkbillBocMap.get("var1").toString(); // 业务类型[
                    // Business
                    // type
                    // ]

                    tCmBkbillBoc.setBusinessType(businessType);

                    String accountHoldingBankNumberOfPayer = bkbillBocMap.get("var2") == null ? null
                            : bkbillBocMap.get("var2").toString(); // 付款人开户行号[
                    // Account
                    // holding
                    // bank
                    // number of
                    // payer ]
                    tCmBkbillBoc.setAccountHoldingBankNumberOfPayer(accountHoldingBankNumberOfPayer);

                    String payerAccountBank = bkbillBocMap.get("var3") == null ? null
                            : bkbillBocMap.get("var3").toString(); // 付款人开户行名[
                    // Payer
                    // account
                    // bank ]
                    tCmBkbillBoc.setPayerAccountBank(payerAccountBank);

                    String debitAccountNo = bkbillBocMap.get("var4") == null ? null
                            : bkbillBocMap.get("var4").toString(); // 付款人账号[
                    // Debit
                    // Account
                    // No. ]
                    tCmBkbillBoc.setDebitAccountNo(debitAccountNo);

                    String payersName = bkbillBocMap.get("var5") == null ? null : bkbillBocMap.get("var5").toString(); // 付款人名称[
                    // Payer's
                    // Name
                    // ]
                    tCmBkbillBoc.setPayerName(payersName);

                    String accountHoldingBankNumberOfBeneficiary = bkbillBocMap.get("var6") == null ? null
                            : bkbillBocMap.get("var6").toString(); // 收款人开户行行号[
                    // Account
                    // holding
                    // bank
                    // number of
                    // beneficiary
                    // ]
                    tCmBkbillBoc.setAccountHoldingBankNumberOfBeneficiary(accountHoldingBankNumberOfBeneficiary);

                    // 收款人开户行名[ Beneficiary account bank ]
                    String beneficiaryAccountBank = bkbillBocMap.get("var7") == null ? null
                            : bkbillBocMap.get("var7").toString();
                    tCmBkbillBoc.setBeneficiaryAccountBank(beneficiaryAccountBank);

                    // 收款人账号[ Payee's Account Number ]
                    String payeeAccountNumber = bkbillBocMap.get("var8") == null ? null
                            : bkbillBocMap.get("var8").toString();
                    tCmBkbillBoc.setPayeeAccountNumber(payeeAccountNumber);

                    // 收款人名称[ Payee's Name ]
                    String payeeName = bkbillBocMap.get("var9") == null ? null : bkbillBocMap.get("var9").toString();
                    tCmBkbillBoc.setPayeeName(payeeName);

                    // 交易日期[ Transaction Date ]
                    String transactionDate = bkbillBocMap.get("var10") == null ? null
                            : bkbillBocMap.get("var10").toString();

                    SimpleDateFormat sDFTransactionDate = new SimpleDateFormat("yyyyMMdd");
                    Date transactionDateParse = sDFTransactionDate.parse(transactionDate);
                    tCmBkbillBoc.setTransactionDate(transactionDateParse);

                    // 交易时间[ Transaction time ]
                    String transactionTime = bkbillBocMap.get("var11") == null ? null
                            : bkbillBocMap.get("var11").toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date transactionTimeParse = simpleDateFormat.parse(transactionTime);
                    tCmBkbillBoc.setTransactionTime(transactionTimeParse);

                    // 交易货币[ Trade Currency ]
                    String typeOfCurrency = bkbillBocMap.get("var12") == null ? null
                            : bkbillBocMap.get("var12").toString();
                    tCmBkbillBoc.setTypeOfCurrency(typeOfCurrency);

                    // 交易金额[ Trade Amount ]
                    String tradeAmount = bkbillBocMap.get("var13") == null ? null
                            : bkbillBocMap.get("var13").toString();
                    tCmBkbillBoc.setTradeAmount(tradeAmount);

                    // 交易后余额[ After-transaction balance ]
                    String afterTransactionBalance = bkbillBocMap.get("var14") == null ? null
                            : bkbillBocMap.get("var14").toString();
                    tCmBkbillBoc.setAfterTransactionBalance(afterTransactionBalance);

                    // 银行类型
                    tCmBkbillBoc.setBankType(Constrants.BANK_NAME_ZG);

                    // 起息日期[ Value Date ]
                    String valueDate = bkbillBocMap.get("var15") == null ? null : bkbillBocMap.get("var15").toString();
                    SimpleDateFormat sDFValueDate = new SimpleDateFormat("yyyyMMdd");
                    Date valueDateParse = sDFValueDate.parse(valueDate);
                    tCmBkbillBoc.setValueDate(valueDateParse);

                    // 汇率[ Exchange rate ]
                    String exchangeRate = bkbillBocMap.get("var16") == null ? null
                            : bkbillBocMap.get("var16").toString();
                    tCmBkbillBoc.setExchangeRate(exchangeRate);

                    // 交易流水号[ Transaction reference number ]
                    String transactionReferenceNumber = bkbillBocMap.get("var17") == null ? null
                            : bkbillBocMap.get("var17").toString();
                    tCmBkbillBoc.setTransactionReferenceNumber(transactionReferenceNumber);

                    // 客户申请号[ Online Banking Transaction Ref.(Bank Ref.) ]
                    String onlineBankingTransactionRef = bkbillBocMap.get("var18") == null ? null
                            : bkbillBocMap.get("var18").toString();
                    tCmBkbillBoc.setOnlineBankingTransactionRef(onlineBankingTransactionRef);

                    // 客户业务编号[ Customer Transaction Ref.(Customer Ref.) ]
                    String customerTransactionRef = bkbillBocMap.get("var19") == null ? null
                            : bkbillBocMap.get("var19").toString();
                    tCmBkbillBoc.setCustomerTransactionRef(customerTransactionRef);

                    // 凭证类型[ Voucher type ]
                    String voucherType = bkbillBocMap.get("var20") == null ? null
                            : bkbillBocMap.get("var20").toString();
                    tCmBkbillBoc.setVoucherType(voucherType);

                    // 凭证号码[ Voucher number ]
                    String voucherNumber = bkbillBocMap.get("var21") == null ? null
                            : bkbillBocMap.get("var21").toString();
                    tCmBkbillBoc.setVoucherNumber(voucherNumber);

                    // 记录标识号[ Record ID ]
                    String recordId = bkbillBocMap.get("var22") == null ? null : bkbillBocMap.get("var22").toString();
                    tCmBkbillBoc.setRecordId(recordId);

                    // 摘要[ Reference ]
                    String reference = bkbillBocMap.get("var23") == null ? null : bkbillBocMap.get("var23").toString();
                    tCmBkbillBoc.setReference(reference);

                    // 用途[ Purpose ]
                    String purpose = bkbillBocMap.get("var24") == null ? null : bkbillBocMap.get("var24").toString();
                    tCmBkbillBoc.setPurpose(purpose);

                    // 交易附言[ Remark ]
                    String remark = bkbillBocMap.get("var25") == null ? null : bkbillBocMap.get("var25").toString();
                    tCmBkbillBoc.setRemark(remark);

                    // 备注[ Remarks ]
                    String remarks = bkbillBocMap.get("var26") == null ? null : bkbillBocMap.get("var26").toString();
                    tCmBkbillBoc.setRemarks(remarks);
                    tCmBkbillBoc.setAccountDate(System.currentTimeMillis() + i);
                    // account_date

                    // no = tCmBkbillBocMapper.insert(tCmBkbillBoc);

                    String amountStr = tradeAmount; // 交易金额

                    String dfAccountName = null;
                    if (amountStr.contains("-")) {
                        dfAccountName = payeeName; // 收款人名称
                    } else {
                        dfAccountName = payersName;
                    }

                    // 往账 银行存款是贷方 来账 银行存款是借方
                    // flag 一借二贷
                    String flag = "0";
                    if (transactionType.indexOf("往账") > -1) {

                        flag = "1";
                    } else {

                        flag = "2";
                    }

                    String subjectInfo = bankService.getSubject(reference, remark, dfAccountName, flag, accountId,
                            busDate, "中国银行");
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
                                if (subList.size() == 1) {
                                    subCode = subjectInfos[0];
                                    subName = subjectInfos[1];
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
                            tCmBkbillBoc.setSysSubjectID(subList.get(0).getPkSubId());
                            tCmBkbillBoc.setSysSubjectCode(subList.get(0).getSubCode());
                            tCmBkbillBoc.setSysSubjectName(subList.get(0).getSubName());
                            tCmBkbillBoc.setSysSubjectFullName(
                                    subList.get(0).getFullName() == null ? "" : subList.get(0).getFullName());
                        }
                    }
                    tCmBkbillBoc.setBankAccount(bankAccount);
                    tCmBkbillBocList.add(tCmBkbillBoc);

                }
                List<TCmBkbillBoc> tCmBkbillBocs = tCmBkbillBocMapper.queryBkbillBocByReferenceNumber(tCmBkbillBocList);
                if (tCmBkbillBocs.size() > 0) {
                    result.put("tCmBkbillBocs", tCmBkbillBocs);
                    result.put("message", "有 " + tCmBkbillBocs.size() + "条数据重复！！！");
                    result.put("success", "fail");
                    return result;
                } else {
                    // 4银行excel添加到数据库
                    no = tCmBkbillBocMapper.uploadTCmBillBocList(tCmBkbillBocList);
                    result.put("code", 1);
                    result.put("message", "success");
                    result.put("no", no);
                }

            }
            result.put("message", "上传成功。。。");
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result.put("success", "fail");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            result.put("message", "上传失败请检查excel格式");
        }
        return result;
    }

    @Override
    public Map<String, Object> queryTCmBkbillBocList(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        // 获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) userDate.get("user");
        Account account = (Account) userDate.get("account");
        // 用户id
        String userId = user.getUserID();
        // 账套id
        String accountId = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
            result.put("message", "fail");
        }
        TCmBkbillBoc tCmBkbillBoc = new TCmBkbillBoc();
        tCmBkbillBoc.setAccountId(accountId);
        tCmBkbillBoc.setUserId(userId);

        List<TCmBkbillBoc> tCmBkbillBocs = tCmBkbillBocMapper.queryTCmBkbillBocList(tCmBkbillBoc);
        result.put("code", 1);
        result.put("List", tCmBkbillBocs.size());
        result.put("tCmBkbillBocs", tCmBkbillBocs);
        return result;
    }

    @Override
    public Map<String, Object> deleteTCmbkbillBocAll(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        // 获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) userDate.get("user");
        Account account = (Account) userDate.get("account");
        // 用户id
        String userId = user.getUserID();
        // 账套id
        String accountId = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
            result.put("message", "fail");
        }
        TCmBkbillBoc tCmBkbillBoc = new TCmBkbillBoc();
        tCmBkbillBoc.setAccountId(accountId);
        tCmBkbillBoc.setUserId(userId);

        int no = tCmBkbillBocMapper.deleteTCmbkbillBocAll(tCmBkbillBoc);
        result.put("code", 1);
        result.put("no", no);
        return result;
    }

    @Override
    // @Transactional
    public Map<String, Object> deleteTCmBkbillBocByBkbillBoc(HttpSession session, String pkBkbillBoc)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            int no = tCmBkbillBocMapper.deleteTCmBkbillBocByBkbillBoc(pkBkbillBoc);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            result.put("message", "TCmBkbillBocServiceImpl.deleteTCmBkbillBocByBkbillBoc()出错啦");
        }
        return result;
    }
}
