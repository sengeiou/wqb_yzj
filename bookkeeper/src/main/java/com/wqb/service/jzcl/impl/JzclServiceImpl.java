package com.wqb.service.jzcl.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.jzcl.JzclService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.report.TBasicBalanceSheetService;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("jzclService")
public class JzclServiceImpl implements JzclService {
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    TBasicBalanceSheetService tBasicBalanceSheetService;
    @Autowired
    TBasicIncomeStatementService tBasicIncomeStatementService;

    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    VatService vatService;

    private static Log4jLogger logger = Log4jLogger.getLogger(JzclServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> doJzcl(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        Account account = (Account) sessionMap.get("account");
        String busDate = (String) sessionMap.get("busDate");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountID", account.getAccountID());
        param.put("busDate", busDate);
        List<VoucherHead> list = voucherHeadDao.queryAllVouch(param);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                VoucherHead vouchHead = list.get(i);
                // `auditStatus` int(11) DEFAULT NULL COMMENT '审核状态(0:未审核1:审核)',
                Integer auditStatus = vouchHead.getAuditStatus();
                if (0 == auditStatus) {
                    result.put("success", "fail");
                    result.put("info", "存在尚未审核的凭证,请务必先审核完再结账");
                    return result;
                }
            }
        } else {
            result.put("success", "fail");
            result.put("info", "请仔细核对原始数据,系统尚无凭证");
            return result;
        }
        periodStatusDao.updStatu2(param);
        sessionMap.put("isCarryState", 1);
        session.setAttribute("userDate", sessionMap);
        result.put("success", "true");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public Map<String, Object> unJzcl(User user, Account account, boolean isUnQmjz) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String busDate = account.getUseLastPeriod();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            param.put("busDate", busDate);

            // 判断本月结转 状态，如果未结转不能进行反结账
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", account.getAccountID());
            para.put("busDate", busDate);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(para);

            // 如果isUnQmjz是 true 进入下面反结转
            if (isUnQmjz) {
                logger.info(String.format("进去unJzcl反结转" + new Date()));
                if (!statuList.isEmpty() && statuList.size() == 1) {
                    StatusPeriod sp = statuList.get(0);
                    // `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
                    if (sp.getIsCarryState() == 0) {
                        result.put("code", -1);
                        result.put("msg", "本期未结转，无法反结转。。。");
                        result.put("success", "fail");
                        result.put("info", "本期未结转，无法反结转。。。");
                        return result;
                    }
                }

                logger.info(String.format("进去>>>>>>>>>>>>>>>> 二.删除结转损益凭证和全年净利润凭证" + new Date()));
                // 二.删除结转损益凭证和全年净利润凭证
                String source = "4";
                param.put("source", source);
                List<VoucherHead> syVouchList = voucherHeadDao.queryUnQmjzVouch(param);
                if (null != syVouchList && syVouchList.size() > 0) {
                    VoucherHead vh = syVouchList.get(0);
                    // String deleteFlag = vh.getIsproblem();// 1有问题2没问题
                    String vouchHid = vh.getVouchID();
                    List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchHid);
                    Voucher voucher = new Voucher();
                    voucher.setVoucherHead(vh);
                    voucher.setVoucherBodyList(bodyList);
                    voucherHeadDao.deleteVouHeadByID(vouchHid);
                    voucherBodyDao.deleteVouBodyByID(vouchHid);
                    boolean bool = vatService.checkVouch(param, voucher);
                    if (bool) {
                        // 没问题
                        tBasicSubjectMessageMapper.chgSubAmountByDelete(param, voucher);
                    }
                }

                logger.info(String.format("结束>>>>>>>>>>>>>>>> 二.删除结转损益凭证和全年净利润凭证" + new Date()));

                logger.info(String.format("进入删除 10 类凭证>>>>>>>>>>>>>>>>" + new Date()));
                source = "10";
                param.put("source", source);
                List<VoucherHead> lrVouchList = voucherHeadDao.queryUnQmjzVouch(param);
                if (null != lrVouchList && lrVouchList.size() > 0) {
                    VoucherHead vh = lrVouchList.get(0);
                    // String deleteFlag = vh.getIsproblem();// 1有问题2没问题
                    String vouchHid = vh.getVouchID();
                    List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchHid);
                    Voucher voucher = new Voucher();
                    voucher.setVoucherHead(vh);
                    voucher.setVoucherBodyList(bodyList);
                    voucherHeadDao.deleteVouHeadByID(vouchHid);
                    voucherBodyDao.deleteVouBodyByID(vouchHid);
                    boolean bool = vatService.checkVouch(param, voucher);
                    if (bool) {
                        // 没问题
                        tBasicSubjectMessageMapper.chgSubAmountByDelete(param, voucher);
                    }
                }
                logger.info(String.format("结束删除 10 类凭证>>>>>>>>>>>>>>>>" + new Date()));

                logger.info(String.format("进入更新凭证>>>>>>>>>>>>>>>>" + new Date()));
                periodStatusDao.updStatu4(param);
                logger.info(String.format("结束更新凭证>>>>>>>>>>>>>>>>" + new Date()));
            } else if (!isUnQmjz) {  // 如果isUnQmjz是 false 进入下面反结帐
                logger.info(String.format("进去unJzcl反结账" + new Date()));

                // 判断下个月 状态，如果已经生成凭证本月不能反结账
                Map<String, Object> para2 = new HashMap<String, Object>();
                para2.put("accountID", account.getAccountID());
                // 下一个月份
                para2.put("busDate", DateUtil.getDateAdd(busDate));
                List<StatusPeriod> statuList2 = periodStatusService.queryStatus(para2);
                if (statuList2 != null && statuList2.size() == 1) {
                    StatusPeriod sp = statuList2.get(0);
                    // `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
                    if (sp.getIsJz() == 1) {
                        result.put("code", -1);
                        result.put("msg", "下一期间已经结账，本期无法反结账。。。");
                        result.put("success", "fail");
                        result.put("info", "下一期间已经结账，本期无法反结账。。。");
                        return result;
                    }

                    // 是否检查通过（0否1是）
                    Integer isDetection = sp.getIsDetection();
                    if (isDetection == 1) {
                        result.put("code", -1);
                        result.put("msg", "下一期间已检测通过，本期无法反结账。。。");
                        result.put("success", "fail");
                        result.put("info", "下一期间已检测通过，本期无法反结账。。。");
                        return result;
                    }

                    // `isCarryState` int(4) DEFAULT NULL COMMENT '是否结转（0否1是）',
                    if (sp.getIsCarryState() == 1) {
                        result.put("code", -1);
                        result.put("msg", "下一期间已结转，本期无法反结账。。。");
                        result.put("success", "fail");
                        result.put("info", "下一期间已结转，本期无法反结账。。。");
                        return result;
                    }

                    // `isCreateVoucher` int(2) DEFAULT NULL COMMENT
                    // '是否已一键生成凭证（0：未 1：已生成）',
                    if (sp.getIsCreateVoucher() == 1) {
                        result.put("code", -1);
                        result.put("msg", "下一期间凭证已经生成，本期无法反结账。。。");
                        result.put("success", "fail");
                        result.put("info", "下一期间凭证已经生成，本期无法反结账。。。");
                        return result;
                    }
                }

                if (!statuList.isEmpty() && statuList.size() == 1) {
                    StatusPeriod sp = statuList.get(0);
                    // `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
                    if (sp.getIsJz() == 0) {
                        result.put("code", -1);
                        result.put("msg", "本期未结账，无法反结账。。。");
                        result.put("success", "fail");
                        result.put("info", "本期未结账，无法反结账。。。");
                        return result;
                    }
                }

                logger.info(String.format(" 反结账之后 删除生成的报表" + new Date()));
                // 反结账之后 删除生成的报表
                tBasicBalanceSheetService.deleteBalanceSheet(user, account);
                tBasicIncomeStatementService.deleteIncomeStatrment(user, account);

                // 三.删除已经写入下一期的科目
                String nextPeriod = DateUtil.getNextMonth(busDate);


                param.put("nextPeriod", nextPeriod);
                tBasicSubjectMessageMapper.delUnjzSub(param);

                vatService.delCache(account.getAccountID(), nextPeriod);

                // 四.修改结转状态
                // param.put("isJz", "0");
                param.put("isJz", 0);
                periodStatusDao.updStatu3(param);
            }
            result.put("success", "true");
            return result;
        } catch (BusinessException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
