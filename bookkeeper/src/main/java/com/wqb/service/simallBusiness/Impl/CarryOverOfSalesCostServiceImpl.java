package com.wqb.service.simallBusiness.Impl;

import com.wqb.common.*;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.arch.ArchDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.receipt.ReceiptDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.simallBusiness.CarryOverOfSalesCostService;
import com.wqb.service.vat.VatService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.wqb.common.MyCollectors.singleResultGroupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author 司氏旭东
 * @ClassName: CarryOverOfSalesCostServiceImpl
 * @Description: 销售成本结转
 * @date 2018年4月13日 下午4:48:19
 */
@Service("carryOverOfSalesCostService")
public class CarryOverOfSalesCostServiceImpl implements CarryOverOfSalesCostService {

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

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private ReceiptDao receiptDao;

    @Autowired
    private KcCommodityDao commodityDao;

    /**
     * @return
     * @throws BusinessException
     * @Title: CarryOverOfSalesCostVoucher
     * @Description: 结转销售成本
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Voucher CarryOverOfSalesCostVoucher(User user, Account account) throws BusinessException {
        try {
            // 主营业务收入的百分之八十 + 主营业务成本余额
            //取主营业务收入  * 80%  = 主营业务成本
            //结转成本 （服务业）
            //借： 6401主营业务成本
            //贷： 2211应付职工薪酬
            //生产型、贸易型：
            //借：主营业务成本
            //贷：库存商品-XXXX
            //  accountType 企业性质 1：生产型2：贸易型3：服务型

            String busDate = account.getUseLastPeriod();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param);

            // 未计提过薪资 需从科目中抓取
            // 根据科目名称模糊查询科目
            param.put("lastDate", busDate);
            param.put("subjectID", "6001");
            param.put("subName", "主营业务收入");
            // 主营业务收入金额
            TBasicSubjectMessage incomeSubjectBalance = getSubjectBalance(param);

            double incomeAmount = getSubjectBalanceAmount(incomeSubjectBalance);
            if (incomeAmount == 0) {
                return null;
            }

            param.put("subjectID", "6401");
            param.put("subName", "主营业务成本");
            TBasicSubjectMessage primeCostSubjectBalance = getSubjectBalance(param);
            double primeCostAmount = getSubjectBalanceAmount(primeCostSubjectBalance);

            primeCostAmount = primeCostAmount + (incomeAmount * 0.8);
            if (primeCostAmount == 0) {
                return null;
            }

            if (account.getCompanyType() == 3) {

                // 结转成本 （服务业）
                // 借： 6401主营业务成本
                // 贷： 5201劳务成本
                int row = 0;
                // 创建凭证
                VoucherHead vouchHead = new VoucherHead();
                String uuid = UUIDUtils.getUUID();
                vouchHead.setVouchID(uuid);
                vouchHead.setPeriod(busDate);
                vouchHead.setVcDate(new Date());
                vouchHead.setCurrency("RMB");
                vouchHead.setCurrencyID(null);
                vouchHead.setAccountID(account.getAccountID());
                vouchHead.setVoucherNO(0);
                vouchHead.setCreateDate(System.currentTimeMillis());
                vouchHead.setCreatepsn(user.getUserName());
                vouchHead.setCreatePsnID(user.getUserID());
                vouchHead.setSource(7);
                vouchHead.setVoucherNO(maxVoucherNo);
                vouchHead.setIsproblem("2");
                List<VoucherBody> list = new ArrayList<VoucherBody>();
                vouchHead.setTotalCredit(primeCostAmount);
                vouchHead.setTotalDbit(primeCostAmount);

                VoucherBody body1 = new VoucherBody();
                body1.setVcabstact("结转劳务成本");
                body1.setVouchAID(UUIDUtils.getUUID());
                body1.setVouchID(uuid);
                body1.setPeriod(busDate);
                body1.setAccountID(account.getAccountID());
                body1.setVcsubject(primeCostSubjectBalance.getFullName());
                body1.setSubjectID(primeCostSubjectBalance.getSubCode());
                body1.setDirection("1");
                body1.setDebitAmount(primeCostAmount);
                body1.setRowIndex(++ row + "");

                // 贷： 5201劳务成本
                VoucherBody body2 = new VoucherBody();
                body2.setVouchAID(UUIDUtils.getUUID());
                body2.setVouchID(uuid);
                body2.setPeriod(busDate);
                body2.setAccountID(account.getAccountID());

                HashMap<String, Object> para = new HashMap<>();
                para.put("subCode", "5201");
                para.put("subName", "工资");
                para.put("accountID", account.getAccountID());
                para.put("period", busDate);
                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                TBasicSubjectMessage laborSubjectBalance = subList.get(0);
                if (laborSubjectBalance != null) {
                    body2.setVcsubject("劳务成本_" + laborSubjectBalance.getSubName());
                    body2.setSubjectID(laborSubjectBalance.getSubCode());
                } else {
                    body2.setVcsubject("");
                    body2.setSubjectID("");
                    body2.setVcabstact("劳务成本");
                }
                body2.setDirection("2");
                body2.setCreditAmount(primeCostAmount);
                body2.setRowIndex(++ row + "");

                voucherHeadDao.insertVouchHead(vouchHead);
                voucherBodyDao.insertVouchBody(body1);
                voucherBodyDao.insertVouchBody(body2);
                list.add(body1);
                list.add(body2);
                Voucher voucher = new Voucher();
                voucher.setVoucherHead(vouchHead);
                voucher.setVoucherBodyList(list);

                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            } else  {
                String accountId = account.getAccountID();
                String period = account.getUseLastPeriod();

                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("accountID", accountId);
                hashMap.put("period", period);
                hashMap.put("invoiceType", "2");

                List<InvoiceBody> vbList = invoiceDao.queryInvoiceBAll(hashMap).stream()
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
                List<InvoiceBody> sumInvoiceBody = vatService.sumInvoiceBody(vbList);

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
                Map<String, KcCommodity> kccMap = commodityDao.queryCommodityAll(hashMap).stream()
                        .filter(kc -> StringUtils.isNotBlank(kc.getSub_code())
                                && (kc.getSub_code().startsWith("1405") || kc.getSub_code().startsWith("1403")))
                        .filter(kcCommodity -> subjectCodes.contains(kcCommodity.getSub_code()))
                        .collect(singleResultGroupingBy(KcCommodity::getSub_code));

                if (kccMap.isEmpty()) {
                    return null;
                }

                carryOverCostOfInvoiceAndReceipt(kccMap, accumulation, account, user, maxVoucherNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
        return null;
    }

    private void carryOverCostOfInvoiceAndReceipt(Map<String, KcCommodity> kccMap, Map<String,
            Map<String, Object>> accumulation, Account account, User user, Integer maxVoucherNo) {
        // 创建主凭证
        VoucherHead voHead = new VoucherHead();
        String id = UUIDUtils.getUUID();
        voHead.setVouchID(id);
        voHead.setAccountID(account.getAccountID());
        voHead.setVcDate(new Date());
        voHead.setCurrency("RMB");
        voHead.setCurrencyID(null);
        voHead.setUpdatePsn(user.getUserName());
        voHead.setUpdatedate(new Date());
        voHead.setUpdatePsnID(user.getUserID());
        voHead.setCreatepsn(user.getUserName());
        voHead.setCreatePsnID(user.getUserID());
        voHead.setCreateDate(System.currentTimeMillis());
        voHead.setPeriod(account.getUseLastPeriod());
        voHead.setVouchFlag(1);
        voHead.setVoucherNO(maxVoucherNo);
        voHead.setSource(7);
        voHead.setDes("结转销售成本");
        voHead.setTotalCredit(0.0);
        voHead.setTotalDbit( 0.0);
        vatDao.insertVoHead(voHead);

        VoucherHead voucherHead = vatDao.queryVoHeahderById(id);
        String voucherId = voucherHead.getVouchID();

        // 更新数量金额表
        List<Map<String, Object>> upKccList = new ArrayList<>();
        List<VoucherBody> voucherBodies = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 对销项发票里面的所有商品 结算成本
        // 定义分录行号
        int count = 1;
        for (Map.Entry<String, Map<String, Object>> entry : accumulation.entrySet()) {
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
            voBody.setPeriod(account.getUseLastPeriod());
            voBody.setVcunitID(null);
            voBody.setDebitAmount(null);
            voBody.setAccountID(account.getAccountID());
            voBody.setVcabstact("结转销售成本");
            voBody.setPrice(avgPrice.doubleValue());
            voBody.setIsproblem("2");
            voBody.setDes("");
            voBody.setNumber(bqIssuenum.doubleValue());
            voBody.setCreditAmount(cbAmount.multiply(new BigDecimal("0.8")).doubleValue());
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
        SubjectMessage zycbSub = vatService.querySubByCode("6401");
        VoucherBody voBody = vatService.createVouchBody(voucherId, "1", "结转销售成本", zycbSub.getFull_name(),
                totalAmount.doubleValue(), null, "1", zycbSub.getSub_code(), null);

        voucherBodies.add(voBody);

        Map<String, Object> m1 = new HashMap<>();
        m1.put("vouchID", voucherId);
        m1.put("totalCredit", totalAmount);
        m1.put("totalDbit", totalAmount);
        m1.put("isproblem", 2);
        // 更新 凭证头 金额 记录凭证转态
        voucherHeadDao.chgVouchAmount(m1);

        double newTotalAmount = totalAmount.multiply(new BigDecimal("0.8")).doubleValue();
        voucherHead.setTotalCredit(newTotalAmount);
        voucherHead.setTotalDbit(newTotalAmount);

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
        jzcbMap.put("accountID", account.getAccountID());
        jzcbMap.put("busDate", account.getUseLastPeriod());
        // 科目更新 cbjz标识的作用 如果有
        // cbjz标识chgSubAmountByCreate方法只会去更新凭证里面的科目不更新数量金额表
        jzcbMap.put("cbjz", "cbjz");
        tBasicSubjectMessageMapper.chgSubAmountByCreate(jzcbMap, voucher);
        jzcbMap.remove("cbjz");
    }

    private TBasicSubjectMessage getSubjectBalance(Map<String, Object> param) {
        List<TBasicSubjectMessage> list = tBasicSubjectMessageMapper.selectLastArch(param);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        throw new WqbException("找不到科目余额信息：" + param.toString());
    }

    private double getSubjectBalanceAmount(TBasicSubjectMessage subjectBalance) {
        BigDecimal endingBalanceDebit = subjectBalance.getEndingBalanceDebit();
        if (null != endingBalanceDebit) {
            return endingBalanceDebit.doubleValue();
        }
        return 0;
    }
}
