package com.wqb.service.jzqnjlr.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.jzqnjlr.JzqnjlrService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service("jzqnjlrService")
public class JzqnjlrServiceImpl implements JzqnjlrService {
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;

    // @Transactional(rollbackFor = BusinessException.class)
    public Voucher doJzqnjlr(User user, Account account) throws BusinessException {
        try {
            String busDate = account.getUseLastPeriod();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            inits(account.getAccountID(), busDate, user.getUserID());
            param.put("subName", "本年利润");
            param.put("subjectID", 4103);
            List<TBasicSubjectMessage> list1 = tBasicSubjectMessageMapper.querySubjectByName(param);
            if (list1 == null || list1.size() == 0) {
                return null;
            }
            // BigDecimal jfAmount = list1.get(0).getEndingBalanceDebit();
            // BigDecimal dfAmount = list1.get(0).getCurrentAmountCredit();
            // 贷： 本年利润 例1： 500（本年利润贷方1000 - 本年利润借方500 ） 例2： -500（本年利润贷方500 -
            // 本年利润借方1000）
            // 借: 未分配利润 例1： 500 例2： -500
            BigDecimal jfAmount = list1.get(0).getEndingBalanceDebit();
            BigDecimal dfAmount = list1.get(0).getEndingBalanceCredit();
            double amount = 0.0;
            if (jfAmount != null && dfAmount != null) {
                amount = dfAmount.doubleValue() - jfAmount.doubleValue();
            } else {
                if (jfAmount == null && dfAmount != null) {
                    amount = dfAmount.doubleValue();
                }
                if (jfAmount != null && dfAmount == null) {
                    amount = 0 - jfAmount.doubleValue();
                }
                if (jfAmount == null && dfAmount == null) {
                    amount = 0.0;
                }
            }
            Voucher voucher = new Voucher();
            // 构造分录1
            // 借：本年利润
            VoucherHead vouchHead = new VoucherHead();
            String uuid = UUIDUtils.getUUID();
            vouchHead.setVouchID(uuid);
            vouchHead.setPeriod(busDate);
            vouchHead.setVcDate(new Date());
            vouchHead.setAccountID(account.getAccountID());
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
            vouchHead.setVoucherNO(maxVoucherNo);
            vouchHead.setCreateDate(System.currentTimeMillis());
            vouchHead.setCreatepsn(user.getUserName());
            vouchHead.setCreatePsnID(user.getUserID());
            vouchHead.setSource(10);
            List<VoucherBody> list = new ArrayList<VoucherBody>();
            VoucherBody body1 = new VoucherBody();
            body1.setVouchAID(UUIDUtils.getUUID());
            body1.setVouchID(uuid);
            body1.setPeriod(busDate);
            body1.setAccountID(account.getAccountID());
            body1.setVcabstact("结转本年利润");
            body1.setVcsubject("本年利润");
            body1.setSubjectID("4103");
            body1.setDirection("1");
            body1.setDebitAmount(amount);
            body1.setRowIndex("1");
            VoucherBody body2 = new VoucherBody();
            body2.setVouchAID(UUIDUtils.getUUID());
            body2.setVouchID(uuid);
            body2.setPeriod(busDate);
            body2.setAccountID(account.getAccountID());

            Map<String, Object> sbParam = new HashMap<String, Object>();
            String sbName = "未分配利润";
            sbParam.put("subCode", "4104");// 其他应收款
            sbParam.put("subName", sbName);
            sbParam.put("accountID", account.getAccountID());
            sbParam.put("period", busDate);
            sbParam.put("busDate", busDate);
            List<TBasicSubjectMessage> sbList = tBasicSubjectMessageMapper.querySubject(sbParam);
            TBasicSubjectMessage ss = null;
            if (sbList != null && sbList.size() == 1) {
                ss = sbList.get(0);
            } else {
                throw new BusinessException("系统缺失本年利润_未分配利润科目,结转损益失败!");
            }

            body2.setSubjectID(ss.getSubCode());
            body2.setDirection("2");
            body2.setCreditAmount(amount);
            body2.setRowIndex("2");
            body2.setVcsubject("利润分配_未分配利润");
            vouchHead.setTotalCredit(amount);
            vouchHead.setTotalDbit(amount);
            voucher.setVoucherHead(vouchHead);
            list.add(body1);
            list.add(body2);
            if (amount != 0) {
                voucherHeadDao.insertVouchHead(vouchHead);
                voucherBodyDao.insertVouchBody(body1);
                voucherBodyDao.insertVouchBody(body2);
                voucher.setVoucherBodyList(list);
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                }
            }
            return voucher;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
    }

    private void inits(String accoutID, String busDate, String userID) {
        vatService.subinit(accoutID, busDate, userID, userID);
    }
}
