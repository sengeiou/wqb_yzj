package com.wqb.services.impl;

import com.wqb.commons.emun.StockGoodsDirection;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.commons.toolkit.SubjectCodeUtils;
import com.wqb.commons.toolkit.VoucherHelpers;
import com.wqb.domains.StockGoods;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.services.StockGoodsService;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.impl.subject.SubjectBalanceServiceImpl;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.wqb.commons.emun.SubjectItem.KCSP;
import static com.wqb.commons.emun.SubjectItem.YCL;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 库存商品表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
@Service
public class StockGoodsServiceImpl extends BaseServiceImpl<StockGoods> implements StockGoodsService {

    @Autowired
    private SubjectBalanceServiceImpl subjectBalanceService;

    @Override
    public StockGoods selectCurrentStockGoodsBySubjectCode(String accountId, String period, String subjectCode) {
        StockGoods stockGoods = new StockGoods();
        stockGoods.setAccountID(accountId);
        stockGoods.setPeriod(period);
        stockGoods.setSubCode(subjectCode);
        return getOne(stockGoods);
    }

    @Override
    public List<StockGoods> selectCurrentStockGoodsListByCodes(String accountId, String period) {
        StockGoods stockGoods = new StockGoods();
        stockGoods.setAccountID(accountId);
        stockGoods.setPeriod(period);
        return list(stockGoods);
    }

    @Override
    public void changeStockGoodsForCreateVoucher(Voucher voucher) {
        try {
            createStockGoodsForCreateThisVoucher(voucher);
        } catch (Exception e) {
            throw new WqbException("创建凭证时，" + e.getMessage());
        }
    }

    @Override
    public void changeStockGoodsForUpdateVoucher(Voucher oldVoucher, Voucher newVoucher) {
        try {
            deleteStockGoodsForDeleteThisVoucher(oldVoucher);
        } catch (Exception e) {
            throw new WqbException("更新凭证时，" + e.getMessage());
        }
        try {
            createStockGoodsForCreateThisVoucher(newVoucher);
        } catch (Exception e) {
            throw new WqbException("更新凭证时，" + e.getMessage());
        }
    }

    @Override
    public void changeStockGoodsForDeleteVoucher(Voucher oldVoucher) {
        try {
            deleteStockGoodsForDeleteThisVoucher(oldVoucher);
        } catch (Exception e) {
            throw new WqbException("删除凭证，" + e.getMessage());
        }
    }

    /**
     * 根据这个凭证创建库存
     *
     * @param voucher
     */
    private void createStockGoodsForCreateThisVoucher(Voucher voucher) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String accountId =  voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();
        VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
            String subjectCode = voucherBody.getSubjectID();
            // 非库存科目跳过
            if (!subjectCode.startsWith(KCSP.getCode()) && !subjectCode.startsWith(YCL.getCode())) {
                return;
            }
            // 取库存商品
            StockGoods stockGoods = selectCurrentStockGoodsBySubjectCode(accountId, period, subjectCode);
            // 获取科目余额信息
            SubjectBalance subjectBalance =
                    subjectBalanceService.selectCurrentOneFormCache(accountId, period, subjectCode);

            boolean isDebit = Objects.equals(voucherBody.getDirection(), Voucher.DIRECTION_DEBIT);

            // 有一重情况该商品有科目 但是库存却没有数据。如果是手动添加的凭证又是进项的话，给它增加一条库存数据
            if (stockGoods == null) {
                if (isDebit) {
                    // 新商品第一次添加才会走这里。比如进项新商品原来没有库存，修改凭证的时候新增科目保存这个凭证。
                    // 那么就会根据科目创建这个商品库存。还有手工录入凭证，第一添加新商品，创建了一个科目。也会走这里
                    if (!save(buildStockGoodsOfDebitDirection(voucherBody, subjectBalance))) {
                        throw new WqbException("保存借方库存失败");
                    }
                } else {
                    // 比如退货 借 主营业务成本 -15713.1
                    // 贷 库存商品 - 光机 -8362.15 -395.00 21.17
                    // 没有期初 本期发出 但是期末都是整正数 可以考虑入库
                    if (!save(buildStockGoodsOfCreditDirection(voucherBody, subjectBalance))) {
                        throw new WqbException("保存代付库存失败");
                    }
                }
            } else {
                switch (SubjectItem.codeOf(SubjectCodeUtils.getTopCodeBySubjectCode(subjectCode))) {
                    // 库存商品
                    case KCSP:
                        BigDecimal changeAmount = VoucherHelpers.getVoucherBodyAmountByDirection(voucherBody);
                        BigDecimal number = BigDecimals.of(voucherBody.getNumber());
                        StockGoods updatedStockGoods = rebuildStockGoodsFromBase(voucherBody.getDirection(),
                                number, changeAmount, stockGoods);
                        if (!updateById(updatedStockGoods)) {
                            throw new WqbException("修改商品库存失败");
                        }
                        break;
                    // 原材料
                    case YCL:
                        throw new WqbException("暂时不支持生产型企业");
                    default:
                        throw new WqbException("不支持的凭证科目代码：" + subjectCode);
                }
            }
        });
    }

    /**
     * 根据这个凭证删除库存
     *
     * @param oldVoucher
     */
    private void deleteStockGoodsForDeleteThisVoucher(Voucher oldVoucher) {
        VoucherHeader voucherHeader = oldVoucher.getVoucherHeader();
        String accountId =  voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();

        List<Serializable> deletedIds = new ArrayList<>();
        List<StockGoods> updatedList = new ArrayList<>();

        // 先处理旧凭证对应的库存
        VoucherHelpers.processBody(oldVoucher, (voucherBody, code) -> {
            String subjectCode = voucherBody.getSubjectID();
            // 非库存科目跳过
            if (!subjectCode.startsWith(KCSP.getCode()) && !subjectCode.startsWith(YCL.getCode())) {
                return;
            }
            // 取库存商品
            StockGoods stockGoods = selectCurrentStockGoodsBySubjectCode(accountId, period, subjectCode);
            checkNotNull(stockGoods, "更新科目代码为：%s 的科目库存时未找到库存信息", subjectCode);

            boolean notUpdated = Objects.equals(stockGoods.getCreateDate(), stockGoods.getUpdatedate());
            // 未更新直接删除
            if (notUpdated) {
                deletedIds.add(stockGoods.getComID());

                // 已更新，将数量和金额取反来消除增加的数量金额
            } else {
                BigDecimal changeAmount = VoucherHelpers.getVoucherBodyAmountByDirection(voucherBody);
                BigDecimal number = BigDecimals.of(voucherBody.getNumber());
                updatedList.add(rebuildStockGoodsFromBase(voucherBody.getDirection(), number.negate(),
                        changeAmount.negate(), stockGoods));
            }
        });

        if (!deletedIds.isEmpty() && !removeByIds(deletedIds)) {
            throw new WqbException("删除库存失败：" + deletedIds);
        }

        if (!updatedList.isEmpty() && !updateBatchById(updatedList)) {
            List<String> ids = updatedList.stream()
                    .map(StockGoods::getComID)
                    .collect(toList());
            throw new WqbException("更新库存失败：" + ids);
        }
    }

    private StockGoods buildStockGoodsOfDebitDirection(VoucherBody voucherBody, SubjectBalance subjectBalance) {
        BigDecimal number = BigDecimals.of(voucherBody.getNumber());
        BigDecimal debitAmount =  BigDecimals.of(voucherBody.getDebitAmount());
        BigDecimal price = BigDecimals.of(voucherBody.getPrice());

        StockGoods stockGoods = buildNewStockGoods(voucherBody, subjectBalance);
        // 说明备注
        stockGoods.setDes(subjectBalance.getSubName()  + "凭证补录新增");

        // 本期收入数量
        stockGoods.setBqIncomenum(number.doubleValue());
        // 本期收入金额
        stockGoods.setBqIncomeamount(debitAmount);
        // 本期收入价格;
        stockGoods.setBqIncomeprice(price);

        // 本年累计收入数量
        stockGoods.setTotalIncomenum(number.doubleValue());
        // 本年累计收入金额
        stockGoods.setTotalIncomeamount(debitAmount);

        // 期末结存数量
        stockGoods.setQmBalancenum(number.doubleValue());
        // 期末结存金额
        stockGoods.setQmBalanceamount(debitAmount);
        // 期末结存单价
        stockGoods.setQmBalanceprice(BigDecimals.safeDivide(debitAmount, number, 8, RoundingMode.HALF_UP));

        Date now = new Date();
        stockGoods.setCreateDate(now);
        stockGoods.setUpdatedate(now);
        stockGoods.setImportDate(now);

        return stockGoods;
    }

    private StockGoods buildStockGoodsOfCreditDirection(VoucherBody voucherBody, SubjectBalance subjectBalance) {
        BigDecimal number = BigDecimals.of(voucherBody.getNumber());
        BigDecimal creditAmount =  BigDecimals.of(voucherBody.getCreditAmount());
        BigDecimal price = BigDecimals.of(voucherBody.getPrice());

        // 退货处理应为负数
        if (creditAmount.compareTo(BigDecimal.ZERO) >= 0 || number.compareTo(BigDecimal.ZERO) >= 0) {
            throw new BizException("当前无库存，退货商品数量和金额应为负");
        }

        StockGoods stockGoods = buildNewStockGoods(voucherBody, subjectBalance);
        // 说明备注
        stockGoods.setDes(subjectBalance.getSubName());

        // 本期发出数量
        stockGoods.setBqIssuenum(number.doubleValue());
        // 本期发出金额
        stockGoods.setBqIssueamount(creditAmount);
        // 本期发出单价
        stockGoods.setBqIssueprice(price);

        // 本年累计发出数量
        stockGoods.setTotalIssuenum(number.doubleValue());
        // 本年累计发出金额
        stockGoods.setTotalIssueamount(creditAmount);

        // 期末结存数量 取绝对值
        stockGoods.setQmBalancenum(number.abs().doubleValue());
        // 期末结存金额 取绝对值
        stockGoods.setQmBalanceamount(creditAmount.abs());
        // 期末结存单价
        stockGoods.setQmBalanceprice(BigDecimals.safeDivide(creditAmount, number, 8, RoundingMode.HALF_UP));

        Date now = new Date();
        stockGoods.setCreateDate(now);
        stockGoods.setUpdatedate(now);
        stockGoods.setImportDate(now);

        return stockGoods;
    }

    private StockGoods buildNewStockGoods(VoucherBody voucherBody, SubjectBalance subjectBalance) {
        StockGoods stockGoods = new StockGoods();
        stockGoods.setAccountID(voucherBody.getAccountID());
        stockGoods.setPeriod(voucherBody.getPeriod());
        stockGoods.setSubCode(voucherBody.getSubjectID());

        // 商品名称规格 科目名称 是全名称 库存商品_彩电_C450
        String[] strings = extractGoodsNameAndSpec(subjectBalance.getFullName(), subjectBalance.getCodeLevel());
        stockGoods.setComName(strings[0]);
        stockGoods.setSpec(strings[1]);
        stockGoods.setComNameSpec(strings[2]);
        //科目名称
        stockGoods.setSubComname(subjectBalance.getFullName());
        return stockGoods;
    }

    /**
     * 获取更新的库存信息
     *
     * @param direction
     * @param number
     * @param changeAmount
     * @param originalData
     * @return
     */
    private StockGoods rebuildStockGoodsFromBase(String direction, BigDecimal number ,BigDecimal changeAmount,
                                                 StockGoods originalData) {
        // 获取本期收入数量
        BigDecimal bqIncomeNum = BigDecimals.of(originalData.getBqIncomenum());
        // 获取本期收入金额
        BigDecimal bqIncomeAmount = BigDecimals.of(originalData.getBqIncomeamount());
        // 获取本期发出数量
        BigDecimal bqIssueNum = BigDecimals.of(originalData.getBqIssuenum());
        // 获取本期发出金额
        BigDecimal bqIssueAmount = BigDecimals.of(originalData.getBqIssueamount());
        // 获取期末结余数量
        BigDecimal qmBalanceNum = BigDecimals.of(originalData.getQmBalancenum());
        // 获取期末结余金额
        BigDecimal qmBalanceAmount = BigDecimals.of(originalData.getQmBalanceamount());
        // 获取累计收入数量
        BigDecimal totalIncomeNum = BigDecimals.of(originalData.getTotalIncomenum());
        // 获取累计收入金额
        BigDecimal totalIncomeAmount = BigDecimals.of(originalData.getTotalIncomeamount());
        // 获取累计发出数量
        BigDecimal totalIssueNum = BigDecimals.of(originalData.getTotalIssuenum());
        // 获取累计发出金额
        BigDecimal totalIssueAmount = BigDecimals.of(originalData.getTotalIssueamount());

        StockGoods updated = new StockGoods();
        updated.setComID(originalData.getComID());

        // 借方向
        if (Objects.equals(direction, Voucher.DIRECTION_DEBIT)) {
            // 增加凭证数量
            updated.setBqIncomenum(bqIncomeNum.add(number).doubleValue());
            // 增加凭证金额
            updated.setBqIncomeamount(bqIncomeAmount.add(number));

            BigDecimal newQmBalanceNum = qmBalanceNum.add(number);
            BigDecimal newQmBalanceAmount = qmBalanceAmount.add(changeAmount);
            // 增加期末结存数量
            updated.setQmBalancenum(newQmBalanceNum.doubleValue());
            // 增加期末结存金额
            updated.setQmBalanceamount(newQmBalanceAmount);
            // 计算期末结存单价
            updated.setQmBalanceprice(BigDecimals.safeDivide(newQmBalanceAmount, newQmBalanceNum, 8, RoundingMode.HALF_UP));

            // 增加本年累计收入数量
            updated.setTotalIncomenum(totalIncomeNum.add(number).doubleValue());
            // 增加本年累计收入金额
            updated.setTotalIncomeamount(totalIncomeAmount.add(changeAmount));
            // 库存商品借贷方向
            updated.setBalanceDirection(StockGoodsDirection.amountOf(qmBalanceAmount.doubleValue()).getName());

            // 贷方向
        } else {
            // 1 处理是成本结转这种情况
            if (isCBJZ()) {
                // 设置本期发出数量
                updated.setBqIssuenum(number.doubleValue());
                // 设置本期发出金额
                updated.setBqIssueamount(changeAmount);

                BigDecimal newQmBalanceNum = qmBalanceNum.subtract(number);
                BigDecimal newQmBalanceAmount = qmBalanceAmount.subtract(changeAmount);
                // 减少期末数量
                updated.setQmBalancenum(newQmBalanceNum.doubleValue());
                // 减少期末结余金额
                updated.setQmBalanceamount(newQmBalanceAmount);
                // 计算本期单价
                updated.setQmBalanceprice(
                        BigDecimals.safeDivide(newQmBalanceAmount, newQmBalanceNum, 8, RoundingMode.HALF_UP));

                // 增加累计发出数量
                updated.setTotalIssuenum(totalIssueNum.add(number).doubleValue());
                // 增加累计发出金额
                updated.setTotalIssueamount(totalIssueAmount.add(changeAmount));

                //  2 处理是不是成本结转这种情况
                // 销售：changeAmount >= 0 退货：changeAmount < 0
            } else {
                // 增加本期数量
                updated.setBqIssuenum(bqIssueNum.add(number).doubleValue());
                // 增加本期金额
                updated.setBqIssueamount(bqIssueAmount.add(changeAmount));

                BigDecimal newQmBalanceNum = qmBalanceNum.subtract(number);
                BigDecimal newQmBalanceAmount = qmBalanceAmount.subtract(changeAmount);
                // 减少期末数量
                updated.setQmBalancenum(newQmBalanceNum.doubleValue());
                // 减少期末结余金额
                updated.setQmBalanceamount(newQmBalanceAmount);
                updated.setQcBalanceprice(
                        BigDecimals.safeDivide(newQmBalanceAmount, newQmBalanceNum, 8, RoundingMode.HALF_UP));

                // 增加累计数量
                updated.setTotalIssuenum(totalIssueNum.add(number).doubleValue());
                // 增加累计金额
                updated.setTotalIssueamount(totalIssueAmount.add(changeAmount));
            }
        }

        return updated;
    }


    private boolean isCBJZ() {
        return false;
    }

    /**
     * 返回商品名称规格  0名称 1规格 2名称 + 规格
     *
     * @param fullName
     * @param codeLevel
     * @return
     */
    private String[] extractGoodsNameAndSpec(String fullName, Integer codeLevel) {
        if (StringUtils.isBlank(fullName)) {
            return new String[3];
        }

        // 0号下标是科目名称 库存商品_这样的
        List<String> strings = Arrays.stream(StringUtils.split(fullName, "_"))
                .filter(StringUtils::isNotBlank)
                .skip(1)
                .collect(Collectors.toList());

        if (strings.isEmpty()) {
            throw new WqbException("非法科目名称：" + fullName);
        }

        int size = strings.size();
        if (size == 1) {
            return new String[]{strings.get(0), null, strings.get(0)};
        }
        if (size == 2 && Objects.equals(codeLevel, 3)) {
            return new String[]{strings.get(0), strings.get(1), StringUtils.join(strings, "-")};
        }
        // 避免这种情况 库存商品_库存商品_超薄风管式温湿平衡型
        if (size == 3 && Objects.equals(codeLevel, 3)) {
            return new String[]{strings.get(0), strings.get(2), strings.get(0) + "-" + strings.get(2)};
        }

        return new String[]{
                strings.get(0),
                StringUtils.join(strings, "-", 1, size - 1),
                StringUtils.join(strings, "-")};
    }
}
