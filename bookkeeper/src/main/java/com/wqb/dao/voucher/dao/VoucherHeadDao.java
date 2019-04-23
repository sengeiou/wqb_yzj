package com.wqb.dao.voucher.dao;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.VoucherBody;
import com.wqb.model.VoucherHead;

import java.util.List;
import java.util.Map;

public interface VoucherHeadDao {
    // 查询最大凭证号
    Integer queryMaxVouchNO(String accountID, String period) throws BusinessException;

    // 插入凭证头信息
    Integer insertVouchHead(VoucherHead vouchHead) throws BusinessException;

    // 根据凭证头主键查询凭证头
    VoucherHead queryVouHByID(String vouchID) throws BusinessException;

    void updVouHByID(VoucherHead voucherHead) throws BusinessException;

    void deleteVouHeadByID(String voucherID) throws BusinessException;

    // 凭证断号整理查询 （用户ID 账套ID 创建时间）
    List<VoucherHead> queryVHByUserIdAndVouchID(Map<String, Object> param) throws BusinessException;

    // 凭证断号整理更新voucherNo凭证号
    void updatevoucherNo(Map<String, String> param) throws BusinessException;

    // 凭证审核与反审核
    public void chgVouchStatu(String auditStatus, String vouchID) throws BusinessException;

    // 查看凭证（分页查询）
    public List<VoucherHead> queryVouHead(Map<String, Object> param) throws BusinessException;

    // 获取凭证最大号自增1
    Integer getMaxVoucherNo(Map<String, Object> param) throws BusinessException;

    // 凭证金额更新
    void chgVouchAmount(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryInvAndBankVou(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryAllVouch(Map<String, Object> param) throws BusinessException;

    void oneKeyCheckVoucher(Map<String, Object> param) throws BusinessException;

    // 查询是否有结转销售
    List<VoucherBody> queryjzCarry(Map<String, Object> param) throws BusinessException;

    // lch 检查凭证体是否有问题
    void upVouBodyById(Map<String, Object> param) throws BusinessException;

    // lch 检查凭证头是否更新
    void upVouHeadByCheckId(Map<String, Object> param) throws BusinessException;

    // 凭证汇总
    List<TBasicSubjectMessage> queryVouSummary(Map<String, Object> param) throws BusinessException;

    // 更新凭证修改次数 字段删除了,凌成辉删除 2018-12-03 -->
    // void upModify(Map<String, Object> param) throws BusinessException;

    // 根据source查询需要反结转的凭证列表
    List<VoucherHead> queryUnQmjzVouch(Map<String, Object> param) throws BusinessException;

    // 凭证检查更新
    void upVouHeadByCheckIdCall(Map<String, Object> param) throws BusinessException;

    // 查询凭证总数
    int queryCountVouch(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryImportVoucher(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryRevisedVoucher(Map<String, Object> param) throws BusinessException;

    void updAttachID(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryAttachByID(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryVouchByAttachID(Map<String, Object> param) throws BusinessException;

    VoucherBody queryCbjzVo(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return int 返回类型
     * @Title: queryCountVouch2
     * @Description: 查询凭证总数--如果有凭证不能初始化科目余额表
     * @date 2018年7月23日 下午5:08:21
     * @author SiLiuDong 司氏旭东
     */
    int queryCountVouch2(Map<String, Object> param) throws BusinessException;

    List<VoucherBody> queryVoBody(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryVouchByNo(Map<String, Object> param) throws BusinessException;

    List<Map<String, Object>> queryImportVouch(Map<String, Object> param) throws BusinessException;

    void updVouchAmount(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryProblemVouch(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> querySameVouchNo(Map<String, Object> param) throws BusinessException;

    /*
     * 查询断号凭证
     */
    List<Integer> queryVoucherBrokenNo(Map<String, Object> param) throws BusinessException;

    // 获取凭证号范围
    Object getPrintVoucherRange(Map<String, Object> param) throws BusinessException;

    // 获取范围内的凭证
    List<VoucherHead> getRangeVoucher(Map<String, Object> param) throws BusinessException;

}
