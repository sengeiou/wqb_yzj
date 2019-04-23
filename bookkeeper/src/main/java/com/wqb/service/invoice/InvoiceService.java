package com.wqb.service.invoice;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.InvoiceHead;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface InvoiceService {
    // 进项发票写入到数据库
    List<Map<String, Object>> jxInvoice2Data(List<Map<String, Object>> list, HttpSession session) throws BusinessException;

    // 销项发票写入到数据库
    List<Map<String, Object>> xxInvoice2Data(List<Map<String, Object>> list, HttpSession session) throws BusinessException;

    // 不定项多条件查询发票
    List<InvoiceHead> queryInvoice(Map<String, Object> param, HttpSession session) throws BusinessException;

    // 批量删除发票
    void deleteInvoice(String invoiceHIDs, String type) throws BusinessException;

    void invoice2vouch(User user, Account account) throws BusinessException;

    void delFaPiao(Map<String, Object> qerMap) throws BusinessException;

    void saveInvoiceMapping(Map<String, Object> param) throws BusinessException;

    public int upMappingrecord(String accountID, String period, String invoiceType, int type) throws BusinessException;

    public Map<String, Object> queryInvoiceMapping(Map<String, Object> qerMap) throws BusinessException;

    public Map<String, Object> queryInvoiceMappingStatu(String accountID, String period) throws BusinessException;

    public Map<String, Object> queryInvoiceMapping(String accountID, String period, String invoiceType) throws BusinessException;

    public Map<String, Object> queryOutPutInvoiceMappingStatu(String accountID, String period) throws BusinessException;

    public void invoiceOutOutGenerateVouch(User user, Account account) throws BusinessException;

}
