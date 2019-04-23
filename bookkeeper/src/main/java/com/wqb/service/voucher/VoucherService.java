package com.wqb.service.voucher;

import com.wqb.common.BusinessException;
import com.wqb.model.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface VoucherService {
    Map<String, Object> insertVoucher(Voucher voucher, HttpSession session) throws BusinessException;

    // 根据凭证头查询凭证(凭证编辑第一步)
    Voucher queryVouById(String vouchID) throws BusinessException;

    // 凭证编辑第二步
    Map<String, Object> updVoucher(Voucher voucher, HttpSession session) throws BusinessException;

    // 删除凭证
    Map<String, Object> deleteVoucher(Map<String, Object> param, HttpSession session) throws BusinessException;

    void createVoucher(User user, Account account) throws BusinessException;

    // 凭证批量审核与反审核
    void chgVouchStatus(String auditStatus, String vouchIDs) throws BusinessException;

    // 分页查询所有凭证
    Map<String, Object> queryAllVoucher(Map<String, Object> param) throws BusinessException;

    // 变更科目金额(适用于生成凭证操作)
    Map<String, Object> chgSubAmountByCreate(Voucher voucher) throws BusinessException;

    Map<String, Object> queryRevisedVoucher(Map<String, Object> param) throws BusinessException;

    // 一键删除凭证
    void oneKeyDelVouch(Map<String, Object> param) throws BusinessException;

    // 一键审核凭证
    void oneKeyCheckVoucher(Map<String, Object> param) throws BusinessException;

    Map<String, Object> uploadVoucher(MultipartFile file, HttpServletRequest request) throws BusinessException;

    Map<String, Object> uploadVoucherAttach(MultipartFile file, HttpServletRequest request) throws BusinessException;

    List<Attach> queryAttach(String vouchID) throws BusinessException;

    void delAttach(Map<String, Object> param) throws BusinessException;

    List<VoucherBody> queryVouBySubname(Map<String, Object> param) throws BusinessException;

    Voucher copyVoucher(Map<String, Object> param) throws BusinessException;

    Map<String, Object> copyVoucher2(String vouchID, String accountID, String period) throws BusinessException;

    Map<String, Object> saveCopyVoucher(String accountID, String period, List<VoucherBody> arr, String voucherNo, String saveToDate) throws BusinessException;

    String queryJzPeriod(String accountID, String period) throws BusinessException;
}
