package com.wqb.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.commons.constant.CacheNames;
import com.wqb.commons.dto.Menu;
import com.wqb.commons.dto.MenuTree;
import com.wqb.commons.toolkit.VoucherHelpers;
import com.wqb.commons.vo.Journal;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.*;
import com.wqb.domains.invoice.Invoice;
import com.wqb.domains.invoice.InvoiceBody;
import com.wqb.domains.invoice.InvoiceHeader;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.subject.SubjectBook;
import com.wqb.domains.voucher.*;
import com.wqb.services.BankAccountService;
import com.wqb.services.JournalService;
import com.wqb.services.StockGoodsService;
import com.wqb.services.invoice.InvoiceService;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectBookService;
import com.wqb.services.subject.SubjectService;
import com.wqb.services.voucher.VoucherItemService;
import com.wqb.services.voucher.VoucherRuleService;
import com.wqb.services.voucher.VoucherService;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.wqb.commons.emun.PaymentType.YH;
import static com.wqb.commons.emun.SubjectItem.KCSP;
import static com.wqb.commons.emun.SubjectItem.YCL;
import static com.wqb.commons.toolkit.MyCollectors.singleResultGroupingBy;
import static java.util.stream.Collectors.*;

/**
 * 收支服务实现类
 *
 * @author WQB
 * @since 2019-03-19 10:49
 */
@Service
public class JournalServiceImpl implements JournalService {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private VoucherItemService voucherItemService;

    @Autowired
    private VoucherRuleService voucherRuleService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SubjectBookService subjectBookService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    @Autowired
    private StockGoodsService stockGoodsService;

    @Override
    public Map<String, Object> getMenus() {
        List<Menu> nodes = voucherItemService.prepareMenuTreeFormCache();

        if (nodes == null || nodes.size() != 4) {
            throw new WqbException("取数据异常");
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("incomes", nodes.get(0));
        result.put("expenditures", nodes.get(1));
        result.put("salary", nodes.get(2));
        result.put("dealings", nodes.get(3));

        return result;
    }

    /**
     * 获取收支信息，先批量取出涉及的所有信息，为了方便整合数据进行分组
     *
     * @param page
     * @param accountId
     * @param period
     * @return
     */
    @Override
    public IPage<Journal> getJournals(Page<Journal> page, String accountId, String period) {
        // 系统的科目
        List<Subject> subjects = subjectService.selectAllFormCache();
        // 系统的凭证项目对应的规则
        List<VoucherRule> voucherRules = voucherRuleService.selectAllFormCache();
        // 系统的凭证项目菜单树
        List<Menu> menuTree = voucherItemService.prepareMenuTreeFormCache();

        // 涉及的凭证
        IPage<Voucher> voucherIPage = voucherService.selectPagingList(
                new Page<>(page.getCurrent(), page.getSize()), accountId, period);
        List<Voucher> vouchers = voucherIPage.getRecords();

        if (CollectionUtils.isEmpty(vouchers)) {
            return page;
        }

        // 凭证id集合
        List<String> voucherIds = vouchers.stream()
                .map(voucher -> voucher.getVoucherHeader().getVouchID())
                .collect(toList());

        // 涉及的科目余额明细
        List<SubjectBook> subjectBooks = subjectBookService.selectListByVoucherIds(voucherIds);

        // 涉及的科目余额
        List<String> subjectCodes = subjectBooks.stream().map(SubjectBook::getSubCode).collect(toList());
        List<SubjectBalance> subjectBalances =
                subjectBalanceService.selectCurrentListByCodes(accountId, period, subjectCodes);

        // 涉及的发票信息
        List<Invoice> invoices = invoiceService.selectListByVoucherIds(voucherIds);

        // 涉及的库存
        List<String> subjectCodesOfStockGoods = subjectCodes.stream()
                // 过滤库存原材料得科目代码和商品并且code是扩展过得
                .filter(s -> Stream.of(KCSP, YCL).anyMatch(item ->
                        s.length() > item.getCode().length() && s.startsWith(item.getCode())
                ))
                .collect(toList());
        List<StockGoods> stockGoods = new ArrayList<>();
        if (!subjectCodesOfStockGoods.isEmpty()) {
            stockGoods.addAll(stockGoodsService.selectCurrentStockGoodsListByCodes(accountId, period));
        }

        // 按凭证ID分组的发票
        Map<String, Invoice> invoiceOfGroupedByVoucherId = new HashMap<>();
        // 发票不为空
        if (CollectionUtils.isNotEmpty(invoices)) {
            // 按凭证id分组的发票
            invoiceOfGroupedByVoucherId =
                    invoices.stream().collect(
                            singleResultGroupingBy(invoice -> invoice.getInvoiceHeader().getVouchID()));
        }

        // 按科目代码分组的科目余额
        Map<String, SubjectBalance> subjectBalancesOfGroupedByCode =
                subjectBalances.stream().collect(singleResultGroupingBy(SubjectBalance::getSubCode));

        // 按科目代码分组的库存
        Map<String, StockGoods> stockGoodsListOfGroupedByCode = new HashMap<>();
        if (!stockGoods.isEmpty()) {
            stockGoodsListOfGroupedByCode = stockGoods.stream().collect(singleResultGroupingBy(StockGoods::getSubCode));
        }

        // 按科目代码分组的科目
        Map<String, Subject> subjectsOfGroupedByCode =
                subjects.stream().collect(singleResultGroupingBy(Subject::getCode));

        // 按项目D分组规则
        Map<Integer, List<VoucherRule>> voucherRulesOfGroupedByItemId =
                voucherRules.stream().collect(groupingBy(VoucherRule::getItemId));

        Map<String, Invoice> finalInvoiceOfGroupedByVoucherId = invoiceOfGroupedByVoucherId;
        Map<String, StockGoods> finalStockGoodsListOfGroupedByCode = stockGoodsListOfGroupedByCode;

        List<Journal> journals = vouchers.stream()
                .map(voucher -> mergeInfo(voucher, subjectCodes, menuTree,
                        subjectBalancesOfGroupedByCode,
                        finalInvoiceOfGroupedByVoucherId,
                        finalStockGoodsListOfGroupedByCode,
                        subjectsOfGroupedByCode,
                        voucherRulesOfGroupedByItemId)
                )
                .collect(toList());

        Page<Journal> journalPage = new Page<>(voucherIPage.getCurrent(), voucherIPage.getSize());
        journalPage.setRecords(journals);
        journalPage.setTotal(voucherIPage.getTotal());
        journalPage.setSize(voucherIPage.getSize());
        return journalPage;
    }


    private Journal mergeInfo(Voucher voucher, List<String> subjectCodes,  List<Menu> menuTree,
                              Map<String, SubjectBalance> subjectBalancesOfGroupedByCode,
                              Map<String, Invoice> invoiceOfGroupedByVoucherId,
                              Map<String, StockGoods> stockGoodsListOfGroupedByCode,
                              Map<String, Subject> subjectsOfGroupedByCode,
                              Map<Integer, List<VoucherRule>> voucherRulesOfGroupedByItemId) {

        VoucherBody firstVoucherBody = VoucherHelpers.getVoucherFirstBody(voucher);
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String vouchId = voucherHeader.getVouchID();

        Journal journal = new Journal();

        ArrayList<Journal.SubjectRecord> subjectRecords = new ArrayList<>();
        voucher.getVoucherBodies().forEach(voucherBody -> {
            Journal.SubjectRecord subjectRecord = journal.new SubjectRecord();
            subjectRecord.setDirection(Integer.valueOf(voucherBody.getDirection()));
            if (Objects.equals(voucherBody.getDirection(), Voucher.DIRECTION_DEBIT)) {
                subjectRecord.setAmount(BigDecimals.toAmount(voucherBody.getDebitAmount()));
            } else {
                subjectRecord.setAmount(BigDecimals.toAmount(voucherBody.getCreditAmount()));
            }
            subjectRecord.setNumber(BigDecimals.of(voucherBody.getNumber()).intValue());
            subjectRecord.setSubjectName(voucherBody.getVcsubject());
            SubjectBalance subjectBalance = checkNotNull(
                    subjectBalancesOfGroupedByCode.get(voucherBody.getSubjectID()),
                    "通过subjectCode：%s 找不到项目", voucherBody.getSubjectID());
            subjectRecord.setOriginalName(subjectBalance.getSubName());
            subjectRecords.add(subjectRecord);
        });

        ArrayList<Journal.InvoiceRecord> invoiceRecords = new ArrayList<>();
        Invoice invoice = invoiceOfGroupedByVoucherId.get(vouchId);
        if (invoice != null) {
            InvoiceHeader invoiceHeader = invoice.getInvoiceHeader();
            invoice.getInvoiceBodies().forEach(invoiceBody -> {
                Journal.InvoiceRecord invoiceRecord = journal.new InvoiceRecord();
                invoiceRecord.setAmount(BigDecimals.toAmount(invoiceBody.getNamount()));
                invoiceRecord.setTaxAmount(BigDecimals.toAmount(invoiceBody.getTaxAmount()));
                invoiceRecord.setTaxRate(invoiceBody.getTaxRate().toString());
                invoiceRecord.setTotal(BigDecimals.toAmount(invoiceBody.getNtaxAmount()));
                invoiceRecord.setCode(invoiceHeader.getInvoiceCode());
                invoiceRecord.setNumber(invoiceHeader.getInvoiceNumber());
                invoiceRecord.setType(invoiceHeader.getInvoiceType());
                invoiceRecord.setDate(invoiceHeader.getInvoiceDate());
                invoiceRecord.setName(invoiceHeader.getInvoiceName());
                invoiceRecords.add(invoiceRecord);
            });
        }

        ArrayList<Journal.GoodsRecord> goodsRecords = new ArrayList<>();
        if (MapUtils.isNotEmpty(stockGoodsListOfGroupedByCode)) {
            subjectCodes.stream()
                    // 过滤库存原材料得科目代码和商品并且code是扩展过得
                    .filter(s -> Stream.of(KCSP, YCL).anyMatch(item ->
                        s.length() > item.getCode().length() && s.startsWith(item.getCode())
                    ))
                    .forEach(code -> {
                        StockGoods stockGoods = stockGoodsListOfGroupedByCode.get(code);
                        Journal.GoodsRecord goodsRecord = journal.new GoodsRecord();
                        goodsRecord.setName(stockGoods.getComName());
                        goodsRecord.setSpec(stockGoods.getSpec());
                        goodsRecord.setNameAndSpec(stockGoods.getComNameSpec());
                        goodsRecord.setQcNumber(BigDecimals.of(stockGoods.getQcBalancenum()).intValue() + "");
                        goodsRecord.setQcAmount(BigDecimals.toAmount(stockGoods.getQcBalanceamount()));
                        goodsRecord.setQcPrice(BigDecimals.toAmount(stockGoods.getQcBalanceprice()));
                        goodsRecord.setBqReceivedNumber(BigDecimals.of(stockGoods.getBqIncomenum()).intValue() + "");
                        goodsRecord.setBqReceivedAmount(BigDecimals.toAmount(stockGoods.getBqIncomeamount()));
                        goodsRecord.setBqReceivedPrice(BigDecimals.toAmount(stockGoods.getBqIncomeprice()));
                        goodsRecord.setBqSendNumber(BigDecimals.of(stockGoods.getBqIssuenum()).intValue() + "");
                        goodsRecord.setBqSendAmount(BigDecimals.toAmount(stockGoods.getBqIssueamount()));
                        goodsRecord.setBqSendPrice(BigDecimals.toAmount(stockGoods.getBqIssueprice()));
                        goodsRecord.setQmNumber(BigDecimals.of(stockGoods.getQmBalancenum()).intValue() + "");
                        goodsRecord.setQmAmount(BigDecimals.toAmount(stockGoods.getQmBalanceamount()));
                        goodsRecord.setQmPrice(BigDecimals.toAmount(stockGoods.getQmBalanceprice()));
                        goodsRecords.add(goodsRecord);
                    });
        }


        journal.setSubjectRecords(subjectRecords);
        journal.setInvoiceRecords(invoiceRecords);
        journal.setGoodsRecords(goodsRecords);

        String itemName = backwardTakeItemName(voucher.getVoucherBodies(), menuTree,
                subjectsOfGroupedByCode, voucherRulesOfGroupedByItemId);

        journal.setName(itemName);
        journal.setRemark(voucherHeader.getDes());
        journal.setAmount(BigDecimals.toAmount(voucherHeader.getTotalCredit()));
        journal.setAmountSymbol(firstVoucherBody.getDirection());
        journal.setSummary(firstVoucherBody.getVcabstact());
        journal.setDate(voucher.getVoucherHeader().getVcDate());
        return journal;
    }

    /**
     * 反向推导项目名
     *
     * @param voucherBodies
     * @param menuTree
     * @param subjectsOfGroupedByCode
     * @param voucherRulesOfGroupedByItemId
     * @return
     */
    private String backwardTakeItemName(List<VoucherBody> voucherBodies, List<Menu> menuTree,
                                        Map<String, Subject> subjectsOfGroupedByCode,
                                        Map<Integer, List<VoucherRule>> voucherRulesOfGroupedByItemId) {
        List<Subject> collectedSubjects = new ArrayList<>(voucherBodies.size());
        voucherBodies.forEach(voucherBody -> {
            subjectsOfGroupedByCode.entrySet().stream()
                    .filter(entry -> {
                        // 科目表里的都是标准的科目代码
                        String standardCode = entry.getKey();

                        if (Objects.equals(Subject.EXTENDABLE, entry.getValue().getExtend())) {
                            // 凭证里的像银行存款、应收应付等是会扩展的科目代码
                            String extendedCode = voucherBody.getSubjectID();
                            return StringUtils.startsWith(extendedCode, standardCode);
                        }

                        String extendedCode = voucherBody.getSubjectID();
                        return Objects.equals(extendedCode, standardCode);
                    })
                    .findFirst()
                    .ifPresent(entry -> {
                        collectedSubjects.add(entry.getValue());
                    });
        });

        for (Map.Entry<Integer, List<VoucherRule>> entry : voucherRulesOfGroupedByItemId.entrySet()) {
            Integer voucherItemId = entry.getKey();
            List<VoucherRule> voucherRules = entry.getValue();

            if (voucherRuleService.subjectsBelongsToRules(voucherRules, collectedSubjects)) {
                Menu menu = MenuTree.findTreeNode(menuTree, node -> node.getId().equals(voucherItemId));
                checkNotNull(menu, "通过itemId：%s 找不到项目", voucherItemId);
                return String.valueOf(menu.getFullName());
            }
        }

        return null;
    }


    @Override
    @CacheEvict(cacheNames = CacheNames.JOURNALS, key = "#p0 + '_' + #p1")
    public void deleteCachedJournals(String accountId, String period){}

    @Override
    public void createJournal(JournalItem journalItem, User user, Account account) {
        // 获取凭证规则
        List<VoucherRule> voucherRules = prepareVoucherRules(journalItem);
        if (CollectionUtils.isEmpty(voucherRules)) {
            throw new WqbException("没有该凭证规则");
        }
        voucherService.generateVoucher(journalItem, voucherRules, user, account);
    }

    @Override
    public void updateJournal(JournalItem journalItem, User user, Account account) {
        // 获取凭证规则
        List<VoucherRule> voucherRules = prepareVoucherRules(journalItem);
        if (CollectionUtils.isEmpty(voucherRules)) {
            throw new WqbException("没有该凭证规则");
        }
        voucherService.modifyVoucher(journalItem, voucherRules, user, account);
    }

    @Override
    public void deleteJournal(JournalItem journalItem, User user, Account account) {
        // 获取凭证规则
        List<VoucherRule> voucherRules = prepareVoucherRules(journalItem);
        if (CollectionUtils.isEmpty(voucherRules)) {
            throw new WqbException("没有该凭证规则");
        }
        voucherService.destroyVoucher(journalItem, voucherRules, user, account);
    }

    /**
     * 准备凭证规则
     *
     * @param journalItem
     * @return
     */
    private List<VoucherRule> prepareVoucherRules(JournalItem journalItem) {
        // 根据id获取对应的凭证项目
        Integer itemId = journalItem.getItemId();
        VoucherItem voucherItem = voucherItemService.selectOneByIdFormCache(itemId);
        if (voucherItem == null) {
            throw new BizException("参数有误，itemId无效");
        }

        // 支付方式检查
        // 从系统的凭证项目菜单树获取支持的支付方式
        if (journalItem.getPaymentType() == null) {
            journalItem.setPaymentType(0);
        }
        Integer paymentType = journalItem.getPaymentType();
        List<Menu> menuTree = voucherItemService.prepareMenuTreeFormCache();
        Menu menu = MenuTree.findTreeNode(menuTree, node -> node.getId().equals(itemId));
        Map<Integer, Menu.Payment> payments = menu.getPayments();
        Menu.Payment payment = payments.get(paymentType);

        // 没有支付方式
        if (payment == null) {
            throw new BizException("参数有误，不支持的支付类型，请填写: " + payments.keySet().toString());
        }

        // 没有收支目标
        if (journalItem.getTarget() == null) {
            // 该支付方式需要target来扩展科目
            if (payment.isMustGoods()) {
                throw new BizException("参数有误，target不能为空，请填写库存商品名称");
            }
            if (payment.isMustCustomer()) {
                throw new BizException("参数有误，target不能为空，请填写客户名称");
            }
        }

        // 银行卡信息检查
        if (Objects.equals(YH.getType(), paymentType)) {
            String bankId = journalItem.getBankId();
            if (StringUtils.isBlank(bankId)) {
                throw new BizException("参数有误，bankId不能为空");
            }
            BankAccount bankAccount = bankAccountService.getById(bankId);
            if (bankAccount == null) {
                throw new BizException("参数有误, bankId无效");
            }

            journalItem.setBankAccount(bankAccount);
        }

        // 尝试获取发票或者收据信息
        if (journalItem.supportInvoice()) {
            String invoiceId = journalItem.getInvoiceId();
            Invoice invoice = invoiceService.selectById(invoiceId);
            if (invoice == null) {
                throw new BizException("参数有误, invoiceId无效");
            }
            journalItem.setInvoice(invoice);
            BigDecimal positiveTotalTaxAmount = invoiceService.getPositiveTotalTaxAmount(invoice);
            BigDecimal positiveTotalNamount = invoiceService.getPositiveTotalNamount(invoice);
            BigDecimal negativeTotalTaxAmount = invoiceService.getNegativeTotalTaxAmount(invoice);
            BigDecimal negativeTotalNamount = invoiceService.getNegativeTotalNamount(invoice);
            BigDecimal totalTaxAmount;
            BigDecimal totalNamount;

            // 正数
            if (positiveTotalTaxAmount.compareTo(BigDecimal.ZERO) > 0
                    || positiveTotalNamount.compareTo(BigDecimal.ZERO) > 0) {
                totalTaxAmount = positiveTotalTaxAmount;
                totalNamount = positiveTotalNamount;
                // 复数
            } else if (negativeTotalTaxAmount.compareTo(BigDecimal.ZERO) < 0
                    || negativeTotalNamount.compareTo(BigDecimal.ZERO) < 0) {
                totalTaxAmount = negativeTotalTaxAmount;
                totalNamount = negativeTotalNamount;
                // 发票上的金额为总金额和税额都是0 直接忽略
            } else {
                throw new BizException("参数有误，发票金额和税额为零");
            }

            InvoiceBody invoiceBody = invoice.getInvoiceBodies().get(0);
            journalItem.setNumber(BigDecimals.safeDivide(totalNamount, invoiceBody.getNprice()));
            journalItem.setAmount(totalNamount);
            journalItem.setTaxAmount(totalTaxAmount);
        } else {
            Receipt receipt = journalItem.getReceipt();
            // 收据数量
            if (payment.isMustGoods() && Objects.equals(receipt.getNumber(), BigDecimal.ZERO)) {
                throw new BizException("参数有误，涉及库存时number不能为0");
            }
            // 收据金额
            if (Objects.equals(receipt.getAmount(), BigDecimal.ZERO)) {
                throw new BizException("参数有误，无发票信息和收据信息");
            }

            journalItem.setNumber(receipt.getNumber());
            journalItem.setAmount(receipt.getAmount());
            journalItem.setTaxAmount(receipt.getTaxAmount());
        }

        return voucherRuleService.selectListByItemIdAndTypeFormCache(itemId, paymentType);
    }
}
