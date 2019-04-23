package com.wqb.service.proof;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.Voucher;
import com.wqb.model.VoucherBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//增值税结转
public interface ProofService {


    public boolean checkCode(Voucher voucher, Integer type);

    public boolean checkSubcodeByCb(List<VoucherBody> list, Integer type) throws Exception;

    public Voucher merageVo(Voucher voucher) throws Exception;

    public void upComm(TBasicSubjectMessage mess) throws BusinessException;

    public Map<String, List<TBasicSubjectMessage>> getSub(Map<String, Object> param) throws BusinessException;

    public boolean searchCode(List<VoucherBody> list, String subcode);

    public Map<String, Double> getGoodNum(List<VoucherBody> list, String subcode);

    public Double jfdfNum(List<VoucherBody> list, String subcode, Integer type);

    public Double getjfNum(List<VoucherBody> list, String subcode);

    public Double getdfNum(List<VoucherBody> list, String subcode);

    public BigDecimal getjfMoney(List<VoucherBody> list, String subcode);

    public BigDecimal getdfMoney(List<VoucherBody> list, String subcode);

    public boolean checkIsCbjz(List<VoucherBody> list) throws Exception;

    public boolean checkIsIncome(List<VoucherBody> list) throws Exception;


}
