package com.wqb.service.simallBusiness.Impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.arch.ArchDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.simallBusiness.TaxPayment;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TaxPaymentImpl
 * @Description: 计提税金
 * @date 2018年4月13日 下午4:49:03
 */
@Service("taxPayment")
public class TaxPaymentImpl implements TaxPayment {
    @Autowired
    ArchDao archDao;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    VatService vatService;
    @Autowired
    VatDao vatDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    /**
     * @return
     * @throws BusinessException
     * @Title: TaxPaymentVouch
     * @Description: 计提税金
     */
    @Override
    public Map<String, Object> TaxPaymentVouch(User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Voucher voucher = new Voucher();
            Voucher voucher2 = new Voucher();
            // 企业性质 1：生产型2：贸易型3：服务型
            Integer accountType = account.getCompanyType();
            String busDate = account.getUseLastPeriod();
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            // param.put("busDate", DateUtil.getLastMonth(busDate));
            List<VoucherBody> bodyList = voucherBodyDao.queryArchVouch(param);

            // VoucherHead head = null;
            // boolean canAdd = false;
            // if (null != bodyList && bodyList.size() > 0) {
            // for (int i = 0; i < bodyList.size(); i++) {
            // VoucherBody voucherBody = bodyList.get(i);
            // head = voucherHeadDao.queryVouHByID(voucherBody.getVouchID());
            // String period = head.getPeriod();
            // if (period.equals(DateUtil.getLastMonth(busDate))) {
            // canAdd = true;// 上月计提过
            // break;
            // }
            // }
            // }
            //
            // if (!canAdd) {
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
            // 未计提过薪资 需从科目中抓取
            // 根据科目名称模糊查询科目
            String lastDate = DateUtil.getLastMonth(busDate);
            param.put("lastDate", busDate);
            String subjectID1 = "6001";
            String subName1 = "主营业务收入";

            param.put("subjectID", subjectID1);
            param.put("subName", subName1);
            List<TBasicSubjectMessage> list1 = tBasicSubjectMessageMapper.selectLastArch(param);
            // 主营业务收入金额
            double amount1 = 0;
            if (null != list1 && list1.size() > 0) {
                BigDecimal ending_balance_debit = list1.get(0).getEndingBalanceDebit();
                if (null != ending_balance_debit) {
                    amount1 = ending_balance_debit.doubleValue();
                }
                BigDecimal ending_balance_credit = list1.get(0).getEndingBalanceCredit();
                if (null != ending_balance_credit) {
                    amount1 = ending_balance_credit.doubleValue();
                }
            }
            if (amount1 == 0) {
                result.put("code", 1);
                result.put("msg", "主营业务无收入 不需要计提");
                return result;
            }

            double amount2 = 0;
            List<TBasicSubjectMessage> list2 = new ArrayList<TBasicSubjectMessage>();
            double amount3 = 0;
            List<TBasicSubjectMessage> list3 = new ArrayList<TBasicSubjectMessage>();
            double amount4 = 0;
            List<TBasicSubjectMessage> list4 = new ArrayList<TBasicSubjectMessage>();
            double amount5 = 0;
            List<TBasicSubjectMessage> list5 = new ArrayList<TBasicSubjectMessage>();
            double amount7 = 0;
            List<TBasicSubjectMessage> list7 = new ArrayList<TBasicSubjectMessage>();
            double amount8 = 0;
            List<TBasicSubjectMessage> list8 = new ArrayList<TBasicSubjectMessage>();
            double amount9 = 0;
            List<TBasicSubjectMessage> list9 = new ArrayList<TBasicSubjectMessage>();

            if (amount1 < 90000) {
                // 借：应交税费 - 减免税
                // 贷：营业外收入

                // 借：应交税费 - 减免税
                String subjectID2 = "2221";
                String subName2 = "免税";
                param.put("subjectID", subjectID2);
                param.put("subName", subName2);
                list2 = tBasicSubjectMessageMapper.selectLastArch2(param);
                if (list2.size() == 0) {
                    subName2 = "减征";
                    param.put("subjectID", subjectID2);
                    param.put("subName", subName2);
                    list2 = tBasicSubjectMessageMapper.selectLastArch2(param);
                }

                if (null != list2 && list2.size() > 0) {
                    BigDecimal ending_balance_debit = list2.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount2 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list2.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount2 = ending_balance_credit.doubleValue();
                    }
                }
                if (amount2 != 0) {
                    // 贷：营业外收入
                    String subjectID3 = "6301";
                    String subName3 = "营业外收入";
                    param.put("subjectID", subjectID3);
                    param.put("subName", subName3);
                    list3 = tBasicSubjectMessageMapper.selectLastArch(param);
                    if (null != list3 && list3.size() > 0) {
                        BigDecimal ending_balance_debit = list3.get(0).getEndingBalanceDebit();
                        if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                            amount3 = ending_balance_debit.doubleValue();
                        }
                        BigDecimal ending_balance_credit = list3.get(0).getEndingBalanceCredit();
                        if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                            amount3 = ending_balance_credit.doubleValue();
                        }
                    }

                    // 创建凭证
                    VoucherHead vouchHead = new VoucherHead();
                    String uuid = UUIDUtils.getUUID();
                    vouchHead.setVouchID(uuid);
                    vouchHead.setPeriod(busDate);
                    vouchHead.setVcDate(new Date());
                    vouchHead.setAccountID(account.getAccountID());
                    vouchHead.setVoucherNO(0);
                    vouchHead.setCreateDate(System.currentTimeMillis());
                    vouchHead.setCreatepsn(user.getUserName());
                    vouchHead.setCreatePsnID(user.getUserID());
                    // `source` int(11) DEFAULT NULL COMMENT '来源0:进项凭证1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据 7.结转成本 9销项凭证 10结转全年净利润  13结转增值税 14 结转附赠税 15结转企业所得税',
                    // 反计提时 删除凭证时 用到
                    vouchHead.setSource(18);
                    vouchHead.setVoucherNO(maxVoucherNo);
                    List<VoucherBody> list = new ArrayList<VoucherBody>();
                    VoucherBody body1 = null;
                    int row = 0;
                    if (amount2 != 0) {
                        body1 = new VoucherBody();
                        body1.setVcabstact("计提税金");
                        body1.setVouchAID(UUIDUtils.getUUID());
                        body1.setVouchID(uuid);
                        body1.setPeriod(busDate);
                        body1.setAccountID(account.getAccountID());
                        if (list2 != null && list2.size() > 0) {
                            body1.setVcsubject(list2.get(0).getFullName());
                            body1.setSubjectID(list2.get(0).getSubCode());
                        } else {
                            body1.setDes("2221_减免税科目未找到");
                            body1.setIsproblem("1");
                        }

                        body1.setDirection("1");
                        body1.setDebitAmount(amount2);
                        row++;
                        body1.setRowIndex(row + "");
                    }
                    VoucherBody body2 = null;
                    if (amount3 != 0) {
                        body2 = new VoucherBody();
                        body2.setVouchAID(UUIDUtils.getUUID());
                        body2.setVouchID(uuid);
                        body2.setPeriod(busDate);
                        body2.setAccountID(account.getAccountID());

                        if (list3 != null && list3.size() > 0) {
                            body2.setVcsubject(list3.get(0).getFullName());
                            body2.setSubjectID(list3.get(0).getSubCode());
                        } else {
                            body2.setDes("6301_营业外收入  科目未找到");
                            body2.setIsproblem("1");
                        }

                        body2.setDirection("2");
                        body2.setCreditAmount(amount2);
                        row++;
                        body2.setRowIndex(row + "");
                    } else {
                        body2 = new VoucherBody();
                        body2.setVouchAID(UUIDUtils.getUUID());
                        body2.setVouchID(uuid);
                        body2.setPeriod(busDate);
                        body2.setAccountID(account.getAccountID());
                        if (list3 != null && list3.size() > 0) {
                            body2.setVcsubject(list3.get(0).getFullName());
                            body2.setSubjectID(list3.get(0).getSubCode());
                        } else {
                            body2.setDes("6301_营业外收入  科目未找到");
                            body2.setIsproblem("1");
                        }
                        body2.setDirection("2");
                        body2.setCreditAmount(amount2);
                        row++;
                        body2.setRowIndex(row + "");
                    }
                    if (body1 != null) {
                        vouchHead.setTotalCredit(amount2);
                        vouchHead.setTotalDbit(amount2);
                        voucherHeadDao.insertVouchHead(vouchHead);
                        voucher.setVoucherHead(vouchHead);
                        voucherBodyDao.insertVouchBody(body1);
                        list.add(body1);
                    }
                    if (body1 != null && body2 != null) {
                        voucherBodyDao.insertVouchBody(body2);
                        list.add(body2);
                        voucher.setVoucherBodyList(list);
                        boolean bool = vatService.checkVouch(param, voucher);
                        if (bool) {
                            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                        }
                    }
                }

            } else if (amount1 > 90000) {
                // 1.借：应交税费 – 应交增值税
                // 贷：应交税费 – 未交增值税
                // 2.借：营业税金及附加
                // 贷：应交税费-城建税
                // -教育税附加
                // -地方教育税附加

                // 借：营业税金及附加
                // 贷：应交税费-城建税

                // 借：应交税费 - 减免税
                String subjectID2 = "2221";
                String subName2 = "免税";
                param.put("subjectID", subjectID2);
                param.put("subName", subName2);
                list2 = tBasicSubjectMessageMapper.selectLastArch2(param);
                if (list2.size() == 0) {
                    subName2 = "减征";
                    param.put("subjectID", subjectID2);
                    param.put("subName", subName2);
                    list2 = tBasicSubjectMessageMapper.selectLastArch2(param);
                }

                if (null != list2 && list2.size() > 0) {
                    BigDecimal ending_balance_debit = list2.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount2 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list2.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount2 = ending_balance_credit.doubleValue();
                    }
                }
                if (amount2 != 0) {
                    // 贷：营业外收入
                    String subjectID3 = "2221";
                    String subName3 = "未交增值税";
                    param.put("subjectID", subjectID3);
                    param.put("subName", subName3);
                    list3 = tBasicSubjectMessageMapper.selectLastArch2(param);
                    if (null != list3 && list3.size() > 0) {
                        BigDecimal ending_balance_debit = list3.get(0).getEndingBalanceDebit();
                        if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                            amount3 = ending_balance_debit.doubleValue();
                        }
                        BigDecimal ending_balance_credit = list3.get(0).getEndingBalanceCredit();// 1674397624.31000000
                        if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) { // 1.67439761408E9
                            amount3 = ending_balance_credit.doubleValue();
                        }
                    }

                    // 创建凭证
                    VoucherHead vouchHead = new VoucherHead();
                    String uuid = UUIDUtils.getUUID();
                    vouchHead.setVouchID(uuid);
                    vouchHead.setPeriod(busDate);
                    vouchHead.setVcDate(new Date());
                    vouchHead.setAccountID(account.getAccountID());
                    vouchHead.setVoucherNO(0);
                    vouchHead.setCreateDate(System.currentTimeMillis());
                    vouchHead.setCreatepsn(user.getUserName());
                    vouchHead.setCreatePsnID(user.getUserID());
                    vouchHead.setSource(18);
                    vouchHead.setVoucherNO(maxVoucherNo);
                    List<VoucherBody> list = new ArrayList<VoucherBody>();
                    VoucherBody body1 = null;
                    int row = 0;
                    if (amount2 != 0) {
                        body1 = new VoucherBody();
                        body1.setVcabstact("计提税金");
                        body1.setVouchAID(UUIDUtils.getUUID());
                        body1.setVouchID(uuid);
                        body1.setPeriod(busDate);
                        body1.setAccountID(account.getAccountID());
                        if (list2 != null && list2.size() > 0) {
                            body1.setVcsubject(list2.get(0).getFullName());
                            body1.setSubjectID(list2.get(0).getSubCode());
                        } else {
                            body1.setDes("2221_减免税科目未找到");
                            body1.setIsproblem("1");
                        }
                        body1.setDirection("1");
                        body1.setDebitAmount(amount2);
                        row++;
                        body1.setRowIndex(row + "");
                    }
                    VoucherBody body2 = null;
                    if (amount3 != 0) {
                        body2 = new VoucherBody();
                        body2.setVouchAID(UUIDUtils.getUUID());
                        body2.setVouchID(uuid);
                        body2.setPeriod(busDate);
                        body2.setAccountID(account.getAccountID());

                        if (list3 != null && list3.size() > 0) {
                            body2.setVcsubject(list3.get(0).getFullName());
                            body2.setSubjectID(list3.get(0).getSubCode());
                        } else {
                            body2.setDes("6301_营业外收入  科目未找到");
                            body2.setIsproblem("1");
                        }
                        body2.setDirection("2");
                        body2.setCreditAmount(amount3);
                        row++;
                        body2.setRowIndex(row + "");
                    }
                    if (body1 != null) {
                        vouchHead.setTotalCredit(amount1);
                        vouchHead.setTotalDbit(amount1);
                        voucherHeadDao.insertVouchHead(vouchHead);
                        voucher.setVoucherHead(vouchHead);
                        voucherBodyDao.insertVouchBody(body1);
                        list.add(body1);
                    }
                    if (body2 != null) {
                        voucherBodyDao.insertVouchBody(body2);
                        list.add(body2);
                    }
                    voucher.setVoucherBodyList(list);
                    boolean bool = vatService.checkVouch(param, voucher);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                    }
                }
            }

            // 附加税
            Integer maxVoucherNo2 = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
            // 2221 应交税费 – 未交增值税 * 7% = 城建税
            String subjectID6 = "2221";
            String subName6 = "未交增值税";
            param.put("subjectID", subjectID6);
            param.put("subName", subName6);
            list7 = tBasicSubjectMessageMapper.selectLastArch2(param);
            if (null != list7 && list7.size() > 0) {
                BigDecimal ending_balance_debit = list7.get(0).getEndingBalanceDebit();
                if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                    amount7 = ending_balance_debit.doubleValue();
                }
                BigDecimal ending_balance_credit = list7.get(0).getEndingBalanceCredit();// 1674397614.08000000
                if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                    amount7 = ending_balance_credit.doubleValue(); // 1.67439761408E9
                }
            }
            if (amount1 < 100000) {
                // 主营业务收入低于10万 附加税 只交 城建税
                // 1.借：6403 营业税金及附加
                // 2.贷：2221应交税费-城建税
                String subjectID4 = "6403";
                String subName4 = "税金及附加";
                param.put("subjectID", subjectID4);
                param.put("subName", subName4);
                list4 = tBasicSubjectMessageMapper.selectLastArch2(param);
                // if (null != list4 && list4.size() > 0)
                // {
                // BigDecimal ending_balance_debit =
                // list4.get(0).getEndingBalanceDebit();
                // if (null != ending_balance_debit)
                // {
                // amount4 = ending_balance_debit.doubleValue();
                // }
                // BigDecimal ending_balance_credit =
                // list4.get(0).getEndingBalanceCredit();
                // if (null != ending_balance_credit)
                // {
                // amount4 = ending_balance_credit.doubleValue();
                // }
                // }
                amount4 = amount7 * 0.07;
                // 2.贷：2221应交税费-城建税
                String subjectID5 = "2221";
                String subName5 = "城";
                param.put("subjectID", subjectID5);
                param.put("subName", subName5);
                list5 = tBasicSubjectMessageMapper.selectLastArch2(param);
                // if (null != list5 && list5.size() == 0)
                // {
                // result.put("msg", "无城建税请手动添加");
                // return result;

                // BigDecimal ending_balance_debit =
                // list5.get(0).getEndingBalanceDebit();
                // if (null != ending_balance_debit)
                // {
                // amount5 = ending_balance_debit.doubleValue();
                // }
                // BigDecimal ending_balance_credit =
                // list5.get(0).getEndingBalanceCredit();
                // if (null != ending_balance_credit)
                // {
                // amount5 = ending_balance_credit.doubleValue();
                // }
                // }
                amount5 = amount7 * 0.07;
                // 创建凭证
                VoucherHead vouchHead2 = new VoucherHead();
                String uuid1 = UUIDUtils.getUUID();
                vouchHead2.setVouchID(uuid1);
                vouchHead2.setPeriod(busDate);
                vouchHead2.setVcDate(new Date());
                vouchHead2.setAccountID(account.getAccountID());
                vouchHead2.setVoucherNO(0);
                vouchHead2.setCreateDate(System.currentTimeMillis());
                vouchHead2.setCreatepsn(user.getUserName());
                vouchHead2.setCreatePsnID(user.getUserID());
                vouchHead2.setSource(18);
                vouchHead2.setVoucherNO(maxVoucherNo2);
                List<VoucherBody> list6 = new ArrayList<VoucherBody>();
                VoucherBody body4 = null;
                int row4 = 0;
                if (amount4 != 0) {
                    body4 = new VoucherBody();
                    body4.setVcabstact("计提附增税");
                    body4.setVouchAID(UUIDUtils.getUUID());
                    body4.setVouchID(uuid1);
                    body4.setPeriod(busDate);
                    body4.setAccountID(account.getAccountID());
                    if (list4 != null && list4.size() > 0) {
                        body4.setSubjectID(list4.get(0).getSubCode());
                        body4.setVcsubject(list4.get(0).getFullName());
                    } else {
                        body4.setDes("6403_ 营业税金及附加   科目未找到");
                        body4.setIsproblem("1");
                    }
                    body4.setDirection("1");
                    body4.setDebitAmount(amount4);
                    row4++;
                    body4.setRowIndex(row4 + "");
                }
                VoucherBody body5 = null;
                if (amount5 != 0) {
                    body5 = new VoucherBody();
                    body5.setVouchAID(UUIDUtils.getUUID());
                    body5.setVouchID(uuid1);
                    body5.setPeriod(busDate);
                    body5.setAccountID(account.getAccountID());
                    if (list5 != null && list5.size() > 0) {
                        body5.setVcsubject(list5.get(0).getFullName());
                        body5.setSubjectID(list5.get(0).getSubCode());
                    } else {
                        body5.setDes("2221_城建税科目未找到");
                        body5.setIsproblem("1");
                    }
                    body5.setDirection("2");
                    body5.setCreditAmount(amount5);
                    row4++;
                    body5.setRowIndex(row4 + "");
                }
                if (body4 != null) {
                    vouchHead2.setTotalCredit(amount5);
                    vouchHead2.setTotalDbit(amount4);
                    voucherHeadDao.insertVouchHead(vouchHead2);
                    voucher2.setVoucherHead(vouchHead2);
                    voucherBodyDao.insertVouchBody(body4);
                    list6.add(body4);
                }
                if (body5 != null) {
                    voucherBodyDao.insertVouchBody(body5);
                    list6.add(body5);
                }
                // if(list6.size() != 0)
                // {
                voucher2.setVoucherBodyList(list6);

                // }
            }
            // 附加税
            if (amount1 > 100000) { // 106796.11
                // 1.借：营业税金及附加
                // 2贷：应交税费-城建税
                // -教育税附加
                // -地方教育税附加
                String subjectID4 = "6403";
                String subName4 = "税金及附加";
                param.put("subjectID", subjectID4);
                param.put("subName", subName4);
                list4 = tBasicSubjectMessageMapper.selectLastArch2(param);// edb
                // 788839129.85600000
                // if (null != list4 && list4.size() > 0)
                // {
                // BigDecimal ending_balance_debit =
                // list4.get(0).getEndingBalanceDebit();
                // if (null != ending_balance_debit)
                // {
                // amount4 = ending_balance_debit.doubleValue();
                // }
                // BigDecimal ending_balance_credit =
                // list4.get(0).getEndingBalanceCredit();
                // if (null != ending_balance_credit)
                // {
                // amount4 = ending_balance_credit.doubleValue();
                // }
                // }
                // amount4 = amount7 * 0.3 * 0.07;
                // 贷： 2221 应交税费 – 未交增值税 * 30% * 7% = 城建税
                String subjectID5 = "2221";
                String subName5 = "城";
                param.put("subjectID", subjectID5);
                param.put("subName", subName5);
//				String subjectID5 = "2221";
//				String subName5 = "城建税";
//				param.put("subjectID", subjectID5);
//				param.put("subName", subName5);
                list5 = tBasicSubjectMessageMapper.selectLastArch2(param);// cact
                // 473409987.18840003
                // cadt224.27000000
                if (null != list5 && list5.size() > 0) {
                    BigDecimal ending_balance_debit = list5.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount5 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list5.get(0).getEndingBalanceCredit();// 473409762.91840005
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount5 = ending_balance_credit.doubleValue();// 4.7340976291840005E8
                    }
                }
                amount5 = amount7 * 0.07 + amount5;

                // 2221 应交税费 – 未交增值税 * 30% * 3% = 教育税附加
                String subjectID8 = "2221";
                String subName8 = "教育附加";
                param.put("subjectID", subjectID8);
                param.put("subName", subName8);
                list8 = tBasicSubjectMessageMapper.selectLastArch2(param);
                if (list8.size() == 0) {
                    subName8 = "教育费附加";
                    param.put("subjectID", subjectID8);
                    param.put("subName", subName8);
                    list8 = tBasicSubjectMessageMapper.selectLastArch2(param);
                }
                if (list8.size() > 1) {
                    for (int i = 0; i < list8.size(); i++) {
                        if (list8.get(i).getSubName().contains("地方"))
                            list8.remove(i);
                    }
//					for (TBasicSubjectMessage tBasicSubjectMessage : list8)
//					{
//						String subName = tBasicSubjectMessage.getSubName();
//						if(subName.contains("地方"))
//						{
//							list8.remove(tBasicSubjectMessage);
//						}
//					}
                }
                if (null != list8 && list8.size() > 0) {
                    BigDecimal ending_balance_debit = list8.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount8 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list8.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount8 = ending_balance_credit.doubleValue();
                    }
                }
                amount8 = amount7 * 0.03 + amount8;

                // 按照 ：2221未交增值税 百分之30交稅
                // 2221 应交税费 – 未交增值税 * 30% * 7% = 城建税
                // 2221 应交税费 – 未交增值税 * 30% * 3% = 教育税附加
                // 2221 应交税费 – 未交增值税 * 30% * 2% = 地方教育税附加
                // 1.借：营业税金及附加
                // 2贷：应交税费-城建税
                // -教育税附加
                // -地方教育税附加
                String subjectID9 = "2221";
                String subName9 = "地方教育";
                param.put("subjectID", subjectID9);
                param.put("subName", subName9);
                list9 = tBasicSubjectMessageMapper.selectLastArch2(param);
                if (null != list9 && list9.size() > 0) {
                    BigDecimal ending_balance_debit = list9.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount9 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list9.get(0).getEndingBalanceCredit();// 698435557.50400010
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount9 = ending_balance_credit.doubleValue(); // 6.984355575040001E8
                    }
                } else {
                    result.put("msg", "地方教育税科目没找到");
                    return result;
                }
                amount9 = amount7 * 0.02 + amount9;
                amount4 = amount5 + amount7 + amount9;
                // 创建凭证
                VoucherHead vouchHead2 = new VoucherHead();
                String uuid1 = UUIDUtils.getUUID();
                vouchHead2.setVouchID(uuid1);
                vouchHead2.setPeriod(busDate);
                vouchHead2.setVcDate(new Date());
                vouchHead2.setAccountID(account.getAccountID());
                vouchHead2.setVoucherNO(0);
                vouchHead2.setCreateDate(System.currentTimeMillis());
                vouchHead2.setCreatepsn(user.getUserName());
                vouchHead2.setCreatePsnID(user.getUserID());
                vouchHead2.setSource(18);
                vouchHead2.setVoucherNO(maxVoucherNo2);
                List<VoucherBody> list6 = new ArrayList<VoucherBody>();
                VoucherBody body4 = null;
                int row4 = 0;
                if (amount4 != 0) {
                    body4 = new VoucherBody();
                    body4.setVcabstact("计提税金");
                    body4.setVouchAID(UUIDUtils.getUUID());
                    body4.setVouchID(uuid1);
                    body4.setPeriod(busDate);
                    body4.setAccountID(account.getAccountID());
                    if (list4 != null && list4.size() > 0) {
                        body4.setVcsubject(list4.get(0).getFullName());
                        body4.setSubjectID(list4.get(0).getSubCode());
                    } else {
                        body4.setDes("6403_ 营业税金及附加   科目未找到");
                        body4.setIsproblem("1");
                    }

                    body4.setDirection("1");
                    body4.setDebitAmount(amount5 + amount8 + amount9);
                    row4++;
                    body4.setRowIndex(row4 + "");
                }
                VoucherBody body5 = null;
                if (amount5 != 0) {
                    body5 = new VoucherBody();
                    body5.setVouchAID(UUIDUtils.getUUID());
                    body5.setVouchID(uuid1);
                    body5.setPeriod(busDate);
                    body5.setAccountID(account.getAccountID());
                    if (list5 != null && list5.size() > 0) {
                        body5.setVcsubject(list5.get(0).getFullName());
                        body5.setSubjectID(list5.get(0).getSubCode());
                    } else {
                        body5.setDes("2221_城建税科目未找到");
                        body5.setIsproblem("1");
                    }

                    body5.setDirection("2");
                    body5.setCreditAmount(amount5);
                    row4++;
                    body5.setRowIndex(row4 + "");
                }
                VoucherBody body8 = null;
                if (amount8 != 0) {
                    body8 = new VoucherBody();
                    body8.setVouchAID(UUIDUtils.getUUID());
                    body8.setVouchID(uuid1);
                    body8.setPeriod(busDate);
                    body8.setAccountID(account.getAccountID());
                    if (list8 != null && list8.size() > 0) {
                        body8.setVcsubject(list8.get(0).getFullName());
                        body8.setSubjectID(list8.get(0).getSubCode());
                    } else {
                        // body8.setVcsubject("");
                        // body8.setSubjectID("");
                        // body8.setVcabstact("教育附加税");
                        body8.setDes("2221_教育附加税科目未找到");
                        body8.setIsproblem("1");
                    }
                    body8.setDirection("2");
                    body8.setCreditAmount(amount8);
                    row4++;
                    body8.setRowIndex(row4 + "");
                }
                VoucherBody body9 = null;
                if (amount9 != 0) {
                    body9 = new VoucherBody();
                    body9.setVouchAID(UUIDUtils.getUUID());
                    body9.setVouchID(uuid1);
                    body9.setPeriod(busDate);
                    body9.setAccountID(account.getAccountID());
                    if (list9 != null && list9.size() > 0) {
                        body9.setVcsubject(list9.get(0).getFullName());
                        body9.setSubjectID(list9.get(0).getSubCode());
                    } else {
                        body9.setDes("2221_税金及附加科目未找到");
                        body9.setIsproblem("1");
                    }

                    body9.setDirection("2");
                    body9.setCreditAmount(amount9);
                    row4++;
                    body9.setRowIndex(row4 + "");
                }
                if (body4 != null) {
                    vouchHead2.setTotalCredit(amount5 + amount8 + amount9);
                    vouchHead2.setTotalDbit(amount5 + amount8 + amount9);
                    voucherHeadDao.insertVouchHead(vouchHead2);
                    voucher2.setVoucherHead(vouchHead2);
                    voucherBodyDao.insertVouchBody(body4);
                    list6.add(body4);
                }
                if (body5 != null) {
                    voucherBodyDao.insertVouchBody(body5);
                    list6.add(body5);
                }
                if (body8 != null) {
                    voucherBodyDao.insertVouchBody(body8);
                    list6.add(body8);
                }
                if (body9 != null) {
                    voucherBodyDao.insertVouchBody(body9);
                    list6.add(body9);
                }
                voucher2.setVoucherBodyList(list6);
            }
            if (voucher2.getVoucherHead() == null
                    || (voucher2.getVoucherBodyList() != null && voucher2.getVoucherBodyList().size() < 2)) {

            } else {
                boolean bool = vatService.checkVouch(param, voucher2);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher2);
                }
            }
            // } else {
            // return null;
            // }
            // tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            result.put("code", 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }

    @Override
    public Voucher sobretaxaVouch(HttpSession session) throws BusinessException {
        try {
            Voucher voucher = new Voucher();
            Voucher voucher2 = new Voucher();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            // 企业性质 1：生产型2：贸易型3：服务型
            Integer accountType = account.getCompanyType();
            String busDate = (String) sessionMap.get("busDate");
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            // param.put("busDate", DateUtil.getLastMonth(busDate));
            List<VoucherBody> bodyList = voucherBodyDao.queryArchVouch(param);

            VoucherHead head = null;
            boolean canAdd = false;
            if (null != bodyList && bodyList.size() > 0) {
                for (int i = 0; i < bodyList.size(); i++) {
                    VoucherBody voucherBody = bodyList.get(i);
                    head = voucherHeadDao.queryVouHByID(voucherBody.getVouchID());
                    String period = head.getPeriod();
                    if (period.equals(DateUtil.getLastMonth(busDate))) {
                        canAdd = true;// 上月计提过
                        break;
                    }
                }
            }

            if (!canAdd) {
                Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                // 未计提过薪资 需从科目中抓取
                // 根据科目名称模糊查询科目
                String lastDate = DateUtil.getLastMonth(busDate);
                param.put("lastDate", busDate);
                String subjectID1 = "6001";
                String subName1 = "主营业务收入";

                param.put("subjectID", subjectID1);
                param.put("subName", subName1);
                List<TBasicSubjectMessage> list1 = tBasicSubjectMessageMapper.selectLastArch(param);
                // 主营业务收入金额
                double amount1 = 0;
                if (null != list1 && list1.size() > 0) {
                    BigDecimal ending_balance_debit = list1.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount1 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list1.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount1 = ending_balance_credit.doubleValue();
                    }
                }
                if (amount1 == 0) {
                    return null;
                }

                double amount4 = 0;
                List<TBasicSubjectMessage> list4 = new ArrayList<TBasicSubjectMessage>();
                double amount5 = 0;
                List<TBasicSubjectMessage> list5 = new ArrayList<TBasicSubjectMessage>();
                double amount7 = 0;
                List<TBasicSubjectMessage> list7 = new ArrayList<TBasicSubjectMessage>();

                // 附加税
                Integer maxVoucherNo2 = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                // 2221 应交税费 – 未交增值税 * 30% * 7% = 城建税
                String subjectID6 = "2221";
                String subName6 = "未交增值税";
                param.put("subjectID", subjectID6);
                param.put("subName", subName6);
                list7 = tBasicSubjectMessageMapper.selectLastArch2(param);
                if (null != list7 && list7.size() > 0) {
                    BigDecimal ending_balance_debit = list7.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit && new BigDecimal(0).compareTo(ending_balance_debit) != 0) {
                        amount7 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list7.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit && new BigDecimal(0).compareTo(ending_balance_credit) != 0) {
                        amount7 = ending_balance_credit.doubleValue();
                    }
                }
                if (amount1 < 300000) {
                    // 1.借：应交税费 – 应交增值税
                    // 贷：应交税费 – 未交增值税
                    // 2.借：营业税金及附加
                    // 贷：应交税费-城建税
                    // -教育税附加
                    // -地方教育税附加

                    // 借：应交税费 - 减免税
                    // 贷：营业外收入

                    // 1.借：6403 营业税金及附加
                    // 2.贷：2221应交税费-城建税
                    String subjectID4 = "6403";
                    String subName4 = "税金及附加";
                    param.put("subjectID", subjectID4);
                    param.put("subName", subName4);
                    list4 = tBasicSubjectMessageMapper.selectLastArch2(param);
                    // if (null != list4 && list4.size() > 0)
                    // {
                    // BigDecimal ending_balance_debit =
                    // list4.get(0).getEndingBalanceDebit();
                    // if (null != ending_balance_debit)
                    // {
                    // amount4 = ending_balance_debit.doubleValue();
                    // }
                    // BigDecimal ending_balance_credit =
                    // list4.get(0).getEndingBalanceCredit();
                    // if (null != ending_balance_credit)
                    // {
                    // amount4 = ending_balance_credit.doubleValue();
                    // }
                    // }
                    amount4 = amount7 * 0.3 * 0.07;
                    // 贷：营业外收入
//					String subjectID5 = "2221";
//					String subName5 = "城建税";
//					param.put("subjectID", subjectID5);
//					param.put("subName", subName5);
                    String subjectID5 = "2221";
                    String subName5 = "城";
                    param.put("subjectID", subjectID5);
                    param.put("subName", subName5);
                    list5 = tBasicSubjectMessageMapper.selectLastArch2(param);
                    // if (null != list5 && list5.size() > 0)
                    // {
                    // BigDecimal ending_balance_debit =
                    // list5.get(0).getEndingBalanceDebit();
                    // if (null != ending_balance_debit)
                    // {
                    // amount5 = ending_balance_debit.doubleValue();
                    // }
                    // BigDecimal ending_balance_credit =
                    // list5.get(0).getEndingBalanceCredit();
                    // if (null != ending_balance_credit)
                    // {
                    // amount5 = ending_balance_credit.doubleValue();
                    // }
                    // }
                    amount5 = amount7 * 0.3 * 0.07;
                    // 创建凭证
                    VoucherHead vouchHead2 = new VoucherHead();
                    String uuid1 = UUIDUtils.getUUID();
                    vouchHead2.setVouchID(uuid1);
                    vouchHead2.setPeriod(busDate);
                    vouchHead2.setVcDate(new Date());
                    vouchHead2.setAccountID(account.getAccountID());
                    vouchHead2.setVoucherNO(0);
                    vouchHead2.setCreateDate(System.currentTimeMillis());
                    vouchHead2.setCreatepsn(user.getUserName());
                    vouchHead2.setCreatePsnID(user.getUserID());
                    vouchHead2.setSource(18);
                    vouchHead2.setVoucherNO(maxVoucherNo2);
                    List<VoucherBody> list6 = new ArrayList<VoucherBody>();
                    VoucherBody body4 = null;
                    int row4 = 0;
                    if (amount4 != 0) {
                        body4 = new VoucherBody();
                        body4.setVcabstact("计提附加税");
                        body4.setVouchAID(UUIDUtils.getUUID());
                        body4.setVouchID(uuid1);
                        body4.setPeriod(busDate);
                        body4.setAccountID(account.getAccountID());
                        if (list4 != null && list4.size() > 0) {
                            body4.setSubjectID(list4.get(0).getSubCode());
                            body4.setVcsubject(list4.get(0).getFullName());
                        } else {
                            body4.setDes("6403_ 营业税金及附加   科目未找到");
                            body4.setIsproblem("1");
                        }
                        body4.setDirection("1");
                        body4.setDebitAmount(amount4);
                        row4++;
                        body4.setRowIndex(row4 + "");
                    }
                    VoucherBody body5 = null;
                    if (amount5 != 0) {
                        body5 = new VoucherBody();
                        body5.setVouchAID(UUIDUtils.getUUID());
                        body5.setVouchID(uuid1);
                        body5.setPeriod(busDate);
                        body5.setAccountID(account.getAccountID());
                        if (list5 != null && list5.size() > 0) {
                            body5.setVcsubject(list5.get(0).getFullName());
                            body5.setSubjectID(list5.get(0).getSubCode());
                        } else {
                            body5.setDes("2221_城建税科目未找到");
                            body5.setIsproblem("1");
                        }
                        body5.setDirection("2");
                        body5.setCreditAmount(amount5);
                        row4++;
                        body5.setRowIndex(row4 + "");
                    }
                    if (body4 != null) {
                        vouchHead2.setTotalCredit(amount5);
                        vouchHead2.setTotalDbit(amount4);
                        voucherHeadDao.insertVouchHead(vouchHead2);
                        voucher2.setVoucherHead(vouchHead2);
                        voucherBodyDao.insertVouchBody(body4);
                        list6.add(body4);
                    }
                    if (body5 != null) {
                        voucherBodyDao.insertVouchBody(body5);
                        list6.add(body5);
                    }
                    voucher2.setVoucherBodyList(list6);
                }
                // 附加税
                if (amount1 > 300000) {
                    // 1.借：应交税费 – 应交增值税
                    // 贷：应交税费 – 未交增值税
                    // 2.借：营业税金及附加
                    // 贷：应交税费-城建税
                    // -教育税附加
                    // -地方教育税附加

                    // 借：应交税费 - 减免税
                    // 贷：营业外收入

                    // 1.借：6403 营业税金及附加
                    // 2.贷：2221应交税费-城建税
                    String subjectID4 = "6403";
                    String subName4 = "税金及附加";
                    param.put("subjectID", subjectID4);
                    param.put("subName", subName4);
                    list4 = tBasicSubjectMessageMapper.selectLastArch2(param);
                    // if (null != list4 && list4.size() > 0)
                    // {
                    // BigDecimal ending_balance_debit =
                    // list4.get(0).getEndingBalanceDebit();
                    // if (null != ending_balance_debit)
                    // {
                    // amount4 = ending_balance_debit.doubleValue();
                    // }
                    // BigDecimal ending_balance_credit =
                    // list4.get(0).getEndingBalanceCredit();
                    // if (null != ending_balance_credit)
                    // {
                    // amount4 = ending_balance_credit.doubleValue();
                    // }
                    // }
                    amount4 = amount7 * 0.3 * 0.07;
                    // 贷：营业外收入
                    String subjectID5 = "2221";
                    String subName5 = "城建税";
                    param.put("subjectID", subjectID5);
                    param.put("subName", subName5);
                    list5 = tBasicSubjectMessageMapper.selectLastArch2(param);
                    // if (null != list5 && list5.size() > 0)
                    // {
                    // BigDecimal ending_balance_debit =
                    // list5.get(0).getEndingBalanceDebit();
                    // if (null != ending_balance_debit)
                    // {
                    // amount5 = ending_balance_debit.doubleValue();
                    // }
                    // BigDecimal ending_balance_credit =
                    // list5.get(0).getEndingBalanceCredit();
                    // if (null != ending_balance_credit)
                    // {
                    // amount5 = ending_balance_credit.doubleValue();
                    // }
                    // }
                    amount5 = amount7 * 0.3 * 0.07;

                    // 创建凭证
                    VoucherHead vouchHead2 = new VoucherHead();
                    String uuid1 = UUIDUtils.getUUID();
                    vouchHead2.setVouchID(uuid1);
                    vouchHead2.setPeriod(busDate);
                    vouchHead2.setVcDate(new Date());
                    vouchHead2.setAccountID(account.getAccountID());
                    vouchHead2.setVoucherNO(0);
                    vouchHead2.setCreateDate(System.currentTimeMillis());
                    vouchHead2.setCreatepsn(user.getUserName());
                    vouchHead2.setCreatePsnID(user.getUserID());
                    vouchHead2.setSource(18);
                    vouchHead2.setVoucherNO(maxVoucherNo2);
                    List<VoucherBody> list6 = new ArrayList<VoucherBody>();
                    VoucherBody body4 = null;
                    int row4 = 0;
                    if (amount4 != 0) {
                        body4 = new VoucherBody();
                        body4.setVcabstact("计提附加税");
                        body4.setVouchAID(UUIDUtils.getUUID());
                        body4.setVouchID(uuid1);
                        body4.setPeriod(busDate);
                        body4.setAccountID(account.getAccountID());
                        if (list4 != null && list4.size() > 0) {
                            body4.setSubjectID(list4.get(0).getSubCode());
                            body4.setVcsubject(list4.get(0).getFullName());
                        } else {
                            body4.setDes("6403_ 营业税金及附加   科目未找到");
                            body4.setIsproblem("1");
                        }
                        body4.setDirection("1");
                        body4.setDebitAmount(amount4);
                        row4++;
                        body4.setRowIndex(row4 + "");
                    }
                    VoucherBody body5 = null;
                    if (amount5 != 0) {
                        body5 = new VoucherBody();
                        body5.setVouchAID(UUIDUtils.getUUID());
                        body5.setVouchID(uuid1);
                        body5.setPeriod(busDate);
                        body5.setAccountID(account.getAccountID());
                        if (list5 != null && list5.size() > 0) {
                            body5.setVcsubject(list5.get(0).getFullName());
                            body5.setSubjectID(list5.get(0).getSubCode());
                        } else {
                            body5.setDes("2221_城建税科目未找到");
                            body5.setIsproblem("1");
                        }
                        body5.setDirection("2");
                        body5.setCreditAmount(amount5);
                        row4++;
                        body5.setRowIndex(row4 + "");
                    }
                    if (body4 != null) {
                        vouchHead2.setTotalCredit(amount4);
                        vouchHead2.setTotalDbit(amount5);
                        voucherHeadDao.insertVouchHead(vouchHead2);
                        voucher2.setVoucherHead(vouchHead2);
                        voucherBodyDao.insertVouchBody(body4);
                        list6.add(body4);
                    }
                    if (body5 != null) {
                        voucherBodyDao.insertVouchBody(body5);
                        list6.add(body5);
                    }
                    voucher2.setVoucherBodyList(list6);
                }
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher2);
                }
            } else {
                return null;
            }
            // tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            return voucher;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }
}
