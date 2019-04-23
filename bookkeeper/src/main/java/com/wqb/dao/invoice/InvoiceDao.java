package com.wqb.dao.invoice;

import com.wqb.common.BusinessException;
import com.wqb.model.InvoiceBody;
import com.wqb.model.InvoiceHead;

import java.util.List;
import java.util.Map;

public interface InvoiceDao {
    // 不定项多条件查询

    // 删除发票对凭证的引用
    void updInvoiceByVouID(String vouchID) throws BusinessException;

    // 导入发票主表
    void insertInvocieH(InvoiceHead invoiceHead) throws BusinessException;

    // 导入发票字表
    void insertInvoiceB(InvoiceBody invoiceBody) throws BusinessException;

    // 根据发票类型和发票号码查询数据库(避免倒入时存在重复数据)
    List<InvoiceHead> querySame(Map<String, Object> param) throws BusinessException;

    // 不定项多条件查询导入发票
    List<InvoiceHead> queryInvoiceH(Map<String, Object> param) throws BusinessException;

    // 批量删除发票信息(删除发票头表)
    int deleteInvoiceH(Map<String, Object> param) throws BusinessException;

    // 删除发票字表
    int deleteInvoiceB(Map<String, Object> param) throws BusinessException;

    // 查询进项发票主表（生成凭证）
    List<InvoiceHead> queryJxInvoiceH2Voucher(Map<String, Object> param) throws BusinessException;

    // 查询销项发票主表（生成凭证）
    List<InvoiceHead> queryXxInvoiceH2Voucher(Map<String, Object> param) throws BusinessException;

    List<InvoiceBody> queryInvByHid(String invoiceHID) throws BusinessException;

    // 生成凭证后反写凭证主键到发票
    void updateInvoiceVouID(String invoiceHID, String vouchID) throws BusinessException;

    // 查询销项发票有多少购方公司
    List<String> queryHBuyCorp(Map<String, Object> param) throws BusinessException;

    // 根据购方公司查询主表主键
    List<String> queryHIDByBuyCorp(Map<String, Object> param) throws BusinessException;

    //
    List<Map<String, Object>> queryAmountByHID(String invoiceHID) throws BusinessException;

    // 发票子表 根据商品名称规格进行分组 获取本期分组后的商品总金额 和总数量
    Map<String, InvoiceBody> queryNUmAndMount(Map<String, Object> map) throws BusinessException;

    // 查询进项销项
    List<InvoiceBody> queryInvoiceGroupComName(Map<String, Object> param) throws BusinessException;

    // 统计销项进项每个商品的总数量总价格
    List<InvoiceBody> queryAmountByComName(Map<String, Object> param) throws BusinessException;

    // 统计销项总数
    int queryInvobCount(Map<String, Object> param) throws BusinessException;

    int delFaPiao1(Map<String, Object> qerMap) throws BusinessException;

    int delFaPiao2(Map<String, Object> qerMap) throws BusinessException;

    List<InvoiceHead> queryByVouchID(Map<String, Object> param) throws BusinessException;

    int editInvoice(InvoiceBody vb) throws BusinessException;


    List<InvoiceHead> queryInvoiceHAll(Map<String, Object> param) throws BusinessException;

    List<InvoiceBody> queryInvoiceBAll(Map<String, Object> param) throws BusinessException;

    InvoiceBody queryInvoiceByBid(Map<String, Object> param) throws BusinessException;

    InvoiceHead queryInvoiceByHid(Map<String, Object> param) throws BusinessException;


    int queryInvoiceBCountByHid(Map<String, Object> param) throws BusinessException;

    int deleteInvoiceHBath(List<String> list) throws BusinessException;


}
