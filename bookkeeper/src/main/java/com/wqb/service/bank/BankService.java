package com.wqb.service.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;
import com.wqb.model.bank.BankBill;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface BankService {
    Map<String, Object> uploadBankBill(MultipartFile file, HttpServletRequest request) throws BusinessException;

    public void deleteByID(String billIDs) throws BusinessException;

    List<BankBill> queryBankBill(Map<String, Object> param) throws BusinessException;

    public void queryPage(Map<String, Object> param) throws BusinessException;

    // 银行对账单生成凭证
    List<Voucher> bankBill2Vouch(User user, Account account) throws BusinessException;

    // 生成凭证
    public Voucher createVouch(Map<String, Object> param) throws BusinessException;

    // 测试
    public List<Voucher> bankVouch(HttpSession session) throws BusinessException;

    // 字典获取一级科目ID和名称
    public String getSubject(String desc, String remark, String subName, String flag, String accountID, String busDate,
                             String bankName) throws BusinessException;

    Map<String, Object> updBank(String infos) throws BusinessException;

}
