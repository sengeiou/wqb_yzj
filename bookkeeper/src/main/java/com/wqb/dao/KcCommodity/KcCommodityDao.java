package com.wqb.dao.KcCommodity;

import com.wqb.common.BusinessException;
import com.wqb.model.InvoiceBody;
import com.wqb.model.KcCommodity;
import com.wqb.model.SubjectMessage;
import com.wqb.model.vomodel.KcCommodityVo;

import java.util.List;
import java.util.Map;

public interface KcCommodityDao {
    // 初始化数据
    int insertCommodity(KcCommodity commodity) throws BusinessException;

    // 查询是否初始导入过数据
    Integer queryCommodity(Map<String, String> map) throws BusinessException;

    // 查询库存表所有数据
    List<KcCommodity> queryCommodityAll(Map<String, Object> param) throws BusinessException;

    List<KcCommodity> queryCommodityAll2(Map<String, Object> param) throws BusinessException;

    // 发票子表 根据商品名称规格进行分组 获取本期分组后的商品总金额 和总数量
    Map<String, InvoiceBody> queryNUmAndMount(Map<String, Object> map) throws BusinessException;

    List<KcCommodity> queryBysubCode(Map<String, Object> map) throws BusinessException;

    void updKcByVouch(KcCommodity kcCommodity) throws BusinessException;

    //<update id="updateCommodity" parameterType="com.wqb.model.KcCommodity">
    void updateCommodity(KcCommodity kcCommodity) throws BusinessException;

    SubjectMessage querySubByImpoerSubCode(Map<String, Object> map) throws BusinessException;

    //更新库存商品期末结存数量金额单价
    int updateQmAmountNumPrice(Map<String, Object> param) throws BusinessException;

    //库存商品列表页 参数科目编码 商品名称
    List<KcCommodity> queryCommodityList(Map<String, Object> param) throws BusinessException;

    //获取库存列表页总数
    Integer queryCommodityCount(Map<String, Object> param) throws BusinessException;

    //删除下个期间的库存数据
    void delCommodityAll(Map<String, Object> param) throws BusinessException;

    public Map<String, KcCommodity> queryCommodityMap(Map<String, Object> param) throws BusinessException;

    //初始化再次导入先删除之前的数据
    void delInitCommodity(Map<String, Object> param) throws BusinessException;

    int querySubLevel(String accountID) throws BusinessException;

    //查询库存金额
    KcCommodity queryCommAmount(Map<String, Object> map) throws BusinessException;

    //查询本期发出金额 本期收入金额是否大于0
    KcCommodity queryAmountByCondition(Map<String, Object> param) throws BusinessException;

    //根据条件查询库存
    List<KcCommodity> queryCommByCondition(Map<String, Object> param) throws BusinessException;

    //查询导入科目编码
    List<SubjectMessage> queryExcelCode(Map<String, Object> param) throws BusinessException;

    int insertCommBath(List<KcCommodity> list) throws BusinessException;

    List<SubjectMessage> queryAllSubByImpoerCode(Map<String, Object> param) throws BusinessException;

    int upAllKcc(Map<String, Object> param) throws BusinessException;

    int upAllSub(Map<String, Object> param) throws BusinessException;

    int delCommodityById(Map<String, Object> param) throws BusinessException;


    String queryAllSub14(Map<String, Object> param) throws BusinessException;

    List<KcCommodityVo> queryCommodityToVoucher(Map<String, Object> map) throws BusinessException;

    List<KcCommodity> commodityGenerateVoucher(Map<String, Object> param) throws BusinessException;


}
