package com.wqb.service.voucher;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.VoucherHead;

import java.util.List;
import java.util.Map;

public interface VoucherHeadService {
    // 查询最大凭证号
    Integer queryMaxVouchNO(String accountID, String period) throws BusinessException;

    // 插入凭证头信息
    Integer insertVouchHead(VoucherHead vouchHead) throws BusinessException;

    // 凭证断号整理从新制作
    void updatevoucherNo(Map<String, Object> param) throws BusinessException;

    // 获取凭证最大号自增1
    Integer getMaxVoucherNo(Map<String, Object> param) throws BusinessException;

    List<VoucherHead> queryInvAndBankVou(Map<String, Object> param) throws BusinessException;

    //lch 凭证汇总
    List<TBasicSubjectMessage> queryVouSummary(Map<String, Object> param) throws BusinessException;

    int queryCountVouch(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return
     * @throws BusinessException int    返回类型
     * @Title: queryCountVouch2
     * @Description: 查询凭证总数--如果有凭证不能初始化科目余额表
     * @date 2018年7月23日  下午5:08:50
     * @author SiLiuDong 司氏旭东
     */
    int queryCountVouch2(Map<String, Object> param) throws BusinessException;
}
