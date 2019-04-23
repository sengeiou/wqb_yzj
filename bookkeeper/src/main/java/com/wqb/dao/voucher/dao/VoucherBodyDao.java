package com.wqb.dao.voucher.dao;

import com.wqb.common.BusinessException;
import com.wqb.model.VoucherBody;

import java.util.List;
import java.util.Map;

public interface VoucherBodyDao {
    // 插入凭证体
    Integer insertVouchBody(VoucherBody vouchBody) throws BusinessException;

    List<VoucherBody> queryVouBodyByHID(String voucherID) throws BusinessException;

    Integer updVouBodyByID(String vouchAId) throws BusinessException;

    // 删除凭证分录
    void deleteVouBodyByID(String vouAId) throws BusinessException;

    List<VoucherBody> queryArchVouch(Map<String, Object> param) throws BusinessException;

    List<VoucherBody> queryUnjzVouch(Map<String, Object> param) throws BusinessException;

    int insertVouchBatch(Map<String, Object> param) throws BusinessException;

    List<VoucherBody> queryVouBySubname(Map<String, Object> param);


    int updeVbByAddSubMessage(Map<String, Object> param) throws BusinessException;

    /**
     * 查询工资是否计提
     * querySalary
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    List<VoucherBody> querySalary(Map<String, Object> param) throws BusinessException;
}
