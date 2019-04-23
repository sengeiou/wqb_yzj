package com.wqb.service.jzsy.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.jzsy.JzsyService;
import com.wqb.service.proof.ProofService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service("jzsyService")
public class JzsyServiceImpl implements JzsyService {
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicDocumentsService tBasicDocumentsService;
    @Autowired
    VatService vatService;
    @Autowired
    ProofService proofService;
    @Autowired
    VatDao vatDao;

    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public List<Voucher> doJzsy(User user, Account account) throws BusinessException {
        /*
         * 借：主营业务收入 6001    贷：其他业务收入 6051     贷：投资收益 6111   贷：营业外收入 6301
         *        贷：本年利润 6403
         */
        try {
            vatService.subinit(user, account);
            int rowIndex = 1;
            String busDate = account.getUseLastPeriod();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            Map<String, Object> map5 = new HashMap<>();
            map5.put("rowIndex", rowIndex);
            map5.put("totalAmount", new BigDecimal(0.0));
            map5.put("totalJe", new BigDecimal(0.0));
            map5.put("flg", 0);
            Voucher voucher = new Voucher();
            List<VoucherBody> bodyList = new ArrayList<VoucherBody>();
            List<Voucher> returnList = new ArrayList<Voucher>();
            // 凭证头
            VoucherHead vouchHead = new VoucherHead();
            String uuid = UUIDUtils.getUUID();
            vouchHead.setPeriod(busDate);
            vouchHead.setVouchID(uuid);
            vouchHead.setVcDate(new Date());
            vouchHead.setAccountID(account.getAccountID());
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
            vouchHead.setVoucherNO(maxVoucherNo);
            vouchHead.setCreateDate(System.currentTimeMillis());
            vouchHead.setCreatepsn(user.getUserName());
            vouchHead.setCreatePsnID(user.getUserID());
            vouchHead.setSource(4);
            Map<String, List<TBasicSubjectMessage>> sySub = proofService.getSub(param);
            if (sySub == null) {
                return null;
            }
            BigDecimal totalAmount = new BigDecimal(0.0);
            BigDecimal totalSy = new BigDecimal(0.0);
            // 以前年度损益调整
            List<TBasicSubjectMessage> list01 = sySub.get("6901");
            BigDecimal balance = new BigDecimal(0);
            if (list01 != null && list01.size() > 0) {
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
                for (TBasicSubjectMessage tsm : list01) {
                    if (tsm != null) {
                        BigDecimal endBalnce = StringUtil.bigSubtract(tsm.getEndingBalanceCredit(),
                                tsm.getEndingBalanceDebit());
                        if (endBalnce.compareTo(BigDecimal.ZERO) != 0) {
                            map5.put("flg", 1);
                            VoucherBody vb1 = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), "结转本期损益",
                                    tsm.getFullName(), endBalnce.doubleValue(), null, "1", "6901", null);
                            VoucherBody vb2 = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), null,
                                    "利润分配_未分配利润", null, endBalnce.doubleValue(), "2", ss.getSubCode(), null);
                            bodyList.add(vb1);
                            bodyList.add(vb2);
                            totalSy = totalSy.add(endBalnce);
                            map5.put("rowIndex", rowIndex);
                        }
                    }
                }
            }

            generateVo(sySub.get("6001"), bodyList, map5, uuid, 1); // 主营业务收入
            generateVo(sySub.get("6051"), bodyList, map5, uuid, 1); // 其他业务收入
            generateVo(sySub.get("6111"), bodyList, map5, uuid, 1); // 投资收益
            generateVo(sySub.get("6301"), bodyList, map5, uuid, 1); // 营业外收入
            // 借 本年利润
            totalAmount = (BigDecimal) ((map5.get("totalAmount") == null) ? new BigDecimal(0)
                    : map5.get("totalAmount"));

            if (totalAmount.doubleValue() != 0.0 && bodyList.size() > 0) {
                rowIndex = (int) map5.get("rowIndex");
                VoucherBody vb = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), null, "本年利润", null,
                        totalAmount.doubleValue(), "2", "4103", null);
                map5.put("rowIndex", rowIndex);
                bodyList.add(vb);
            }
            // 借：本年利润 4103
            // 贷：主营业务成本 6401 其他业务成本 6402   营业外支出 6711  销售费用6601  管理费用6602  
            // 财务费用6603   营业税金及附加6403   所得税费用6801 
            BigDecimal totalJe = new BigDecimal(0);
            int ind = (int) map5.get("rowIndex");
            map5.put("rowIndex", ind + 1);
            generateVo(sySub.get("6401"), bodyList, map5, uuid, 2); // 主营业务成本
            generateVo(sySub.get("6402"), bodyList, map5, uuid, 2); // 其他业务成本
            generateVo(sySub.get("6403"), bodyList, map5, uuid, 2); // 营业税金及附加
            generateVo(sySub.get("6711"), bodyList, map5, uuid, 2); // 营业外支出
            generateVo(sySub.get("6601"), bodyList, map5, uuid, 3); // 销售费用
            generateVo(sySub.get("6602"), bodyList, map5, uuid, 3); // 管理费用
            generateVo(sySub.get("6603"), bodyList, map5, uuid, 3); // 财务费用
            generateVo(sySub.get("6701"), bodyList, map5, uuid, 2); // 资产减值损失
            generateVo(sySub.get("6801"), bodyList, map5, uuid, 2); // 所得税费用
            totalJe = (BigDecimal) ((map5.get("totalJe") == null) ? new BigDecimal(0) : map5.get("totalJe"));
            if (bodyList == null || bodyList.size() == 0) {
                return null;
            }
            if (totalJe.doubleValue() != 0.0 && bodyList.size() > 0) {
                Integer flg1 = (Integer) map5.get("flg");
                String vc1 = flg1 == 0 ? "结转本期损益" : null;
                VoucherBody vb1 = vatService.createVouchBody(uuid, String.valueOf(ind), vc1, "本年利润",
                        totalJe.doubleValue(), null, "1", "4103", null);
                bodyList.add(vb1);
            } else {
                // return returnList;
            }
            BigDecimal countAmount = totalAmount.add(totalJe).add(totalSy);
            vouchHead.setTotalCredit(countAmount.doubleValue());
            vouchHead.setTotalDbit(countAmount.doubleValue());
            voucherHeadDao.insertVouchHead(vouchHead);
            voucher.setVoucherHead(vouchHead);
            voucher.setVoucherBodyList(bodyList);
            boolean bool = vatService.checkVouch(param, voucher);
            if (bool) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            }
            return returnList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private void generateVo(List<TBasicSubjectMessage> list, List<VoucherBody> bodyList, Map<String, Object> map5,
                            String uuid, int type) throws BusinessException {
        if (null != list && list.size() > 0) {
            int rowIndex = (int) map5.get("rowIndex");
            BigDecimal totalAmount = (BigDecimal) map5.get("totalAmount");
            BigDecimal totalJe = (BigDecimal) map5.get("totalJe");
            for (TBasicSubjectMessage tsm : list) {
                if (tsm != null) {
                    if (StringUtil.bigSubtract(tsm.getEndingBalanceDebit(), tsm.getEndingBalanceCredit())
                            .doubleValue() != 0.0) {
                        if (type == 1) {
                            Integer flg = (Integer) map5.get("flg");
                            String vc = null;
                            if (flg == 0) {
                                vc = "结转本期损益";
                                map5.put("flg", 1);
                            }
                            BigDecimal endBalnce = StringUtil.bigSubtract(tsm.getEndingBalanceCredit(),
                                    tsm.getEndingBalanceDebit());
                            VoucherBody vb = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), vc,
                                    tsm.getFullName(), endBalnce.doubleValue(), null, "1", tsm.getSubCode(), null);
                            bodyList.add(vb);
                            totalAmount = totalAmount.add(endBalnce);
                        }
                        if (type == 2) {
                            BigDecimal endBalnce = StringUtil.bigSubtract(tsm.getEndingBalanceDebit(),
                                    tsm.getEndingBalanceCredit());
                            VoucherBody vb = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), null,
                                    tsm.getFullName(), null, endBalnce.doubleValue(), "2", tsm.getSubCode(), null);
                            bodyList.add(vb);
                            totalJe = totalJe.add(endBalnce);
                        }
                        if (type == 3) {
                            String subCode = tsm.getSubCode();
                            Integer cd = Integer.parseInt(subCode.substring(0, 4));
                            String vcsubject = cd == 6602 ? "管理费用" : (cd == 6603 ? ("财务费用") : ("销售费用"));
                            String subName = subCode.length() > 7 ? (tsm.getFullName())
                                    : (tsm.getSubCode().length() == 7 ? (vcsubject + "_" + tsm.getSubName())
                                    : (vcsubject + ""));
                            BigDecimal endBalnce = StringUtil.bigSubtract(tsm.getEndingBalanceDebit(),
                                    tsm.getEndingBalanceCredit());
                            VoucherBody vb = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), null, subName,
                                    null, endBalnce.doubleValue(), "2", tsm.getSubCode(), null);
                            bodyList.add(vb);
                            totalJe = totalJe.add(endBalnce);
                        }
                    }
                }
            }
            map5.put("rowIndex", rowIndex);
            map5.put("totalAmount", totalAmount);
            map5.put("totalJe", totalJe);
        }
    }

}
