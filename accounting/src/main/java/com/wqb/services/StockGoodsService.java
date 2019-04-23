package com.wqb.services;

import com.wqb.domains.StockGoods;
import com.wqb.domains.voucher.Voucher;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 * 库存商品表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
public interface StockGoodsService extends BaseService<StockGoods> {

    /**
     * 获取当前套账的科目代码对应的库存商品
     *
     * @param accountId
     * @param period
     * @param subjectCode
     * @return
     */
    StockGoods selectCurrentStockGoodsBySubjectCode(String accountId, String period, String subjectCode);


    /**
     * 为了创建凭证变更库存（创建或修改）
     *
     * @param voucher
     */
    void changeStockGoodsForCreateVoucher(Voucher voucher);

    /**
     * 为了创建凭证变更库存（创建、修改或删除）
     * @param oldVoucher
     * @param newVoucher
     */
    void changeStockGoodsForUpdateVoucher(Voucher oldVoucher, Voucher newVoucher);

    /**
     * 为了创建凭证变更库存（修改或删除）
     *
     * @param oldVoucher
     */
    void changeStockGoodsForDeleteVoucher(Voucher oldVoucher);

    /**
     * 获取当前套账所有库存商品
     *
     * @param accountId
     * @param period
     * @return
     */
    List<StockGoods> selectCurrentStockGoodsListByCodes(String accountId, String period);
}
