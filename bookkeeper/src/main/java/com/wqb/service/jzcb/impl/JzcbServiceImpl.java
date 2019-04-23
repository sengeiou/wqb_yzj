package com.wqb.service.jzcb.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.jzcb.JzcbService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 结转成本业务层
 *
 * @author zhushuyuan
 */
@Component
@Service("jzcbService")
public class JzcbServiceImpl implements JzcbService {
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    //@Transactional
    @Override
    public Map<String, Object> doJzcb(User user, Account account, Voucher sfVoucher, Voucher zjVouch)
            throws BusinessException {
        String busDate = account.getUseLastPeriod();
        vatService.subinit(user, account);
        //inits(account.getAccountID(), busDate, user.getUserID());
        // 企业性质 1：生产型2：贸易型3：服务型
        Integer accountType = account.getCompanyType();
        Voucher voucher = new Voucher();
        List<VoucherBody> list = new ArrayList<VoucherBody>();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("busDate", busDate);
        param.put("accountID", account.getAccountID());
        // inits(account.getAccountID(), busDate, user.getUserID());
        double totalNamount = 0;
        // 获取进项发票主表信息
        List<InvoiceHead> jxList = invoiceDao.queryJxInvoiceH2Voucher(param);
        if (null != jxList && jxList.size() > 0) {
            // 构造凭证主表
            VoucherHead voucherHead = new VoucherHead();
            String voucherHID = UUIDUtils.getUUID();
            boolean flag = false;
            for (int i = 0; i < jxList.size(); i++) {
                InvoiceHead invoiceHead = jxList.get(i);
                String invoiceHID = invoiceHead.getInvoiceHID();
                List<InvoiceBody> invBodyList = invoiceDao.queryInvByHid(invoiceHID);
                if (invBodyList != null && invBodyList.size() > 0) {
                    InvoiceBody invoiceBody = invBodyList.get(0);
                    double namount = invoiceBody.getNamount();
                    String comName = invoiceBody.getComName();
                    totalNamount = totalNamount + namount;
                    // 构造凭证子表
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setVouchID(voucherHID);
                    vouchBody.setDirection("2");
                    vouchBody.setCreditAmount(namount);
                    String subName = "库存商品_" + comName;
                    String subjectID = null;
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1405", "7");
                    if (subjectMessage != null) {
                        subjectID = subjectMessage.getSub_code();
                    } else {
                        subjectID = vatService.getNumber("1405", "7", "1405000");
                        vatService.createSub(subjectID, "1405", comName, subName);
                    }
                    vouchBody.setSubjectID(subjectID);
                    vouchBody.setVcsubject(subName);
                    vouchBody.setRowIndex((i + 2) + "");
                    vouchBody.setAccountID(account.getAccountID());
                    vouchBody.setUserID(user.getUserID());
                    vouchBody.setUpdatePsnID(user.getUserID());
                    vouchBody.setUpdatePsn(user.getUserName());
                    vouchBody.setUpdatedate(new Date());
                    flag = true;
                    voucherBodyDao.insertVouchBody(vouchBody);
                    list.add(vouchBody);
                }
            }
            if (flag) {
                // 构造凭证子表
                VoucherBody vouchBody = new VoucherBody();
                vouchBody.setVouchAID(UUIDUtils.getUUID());
                vouchBody.setVouchID(voucherHID);
                vouchBody.setDirection("1");
                vouchBody.setDebitAmount(totalNamount);
                vouchBody.setSubjectID("6401");
                vouchBody.setVcsubject("主营业务成本");
                vouchBody.setRowIndex("1");
                vouchBody.setAccountID(account.getAccountID());
                vouchBody.setUserID(user.getUserID());
                vouchBody.setUpdatePsnID(user.getUserID());
                vouchBody.setUpdatePsn(user.getUserName());
                vouchBody.setUpdatedate(new Date());
                voucherBodyDao.insertVouchBody(vouchBody);
                list.add(vouchBody);
                voucherHead.setVouchID(voucherHID);
                voucherHead.setVcDate(new Date());
                voucherHead.setCreatePsnID(user.getUserID());
                voucherHead.setCreateDate(System.currentTimeMillis());
                voucherHead.setCreatepsn(user.getUserName());
                voucherHead.setVoucherNO(0);
                voucherHead.setSource(0);
                voucherHead.setAccountID(account.getAccountID());
                voucherHead.setPeriod(busDate);
                voucherHead.setTotalDbit(totalNamount);
                voucherHead.setTotalCredit(totalNamount);
                voucherHeadDao.insertVouchHead(voucherHead);
                voucher.setVoucherBodyList(list);
                voucher.setVoucherHead(voucherHead);
                // 统一变更科目
                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            }
        }
        if (accountType == 1) {// 生产型(无法精准计算)
            /*
             * 1.生产成本(生产型企业） 借：生产成本 贷：原材料XXX
             */
            // 构造凭证头
            VoucherHead voucherHead = new VoucherHead();
            String vouchID = UUIDUtils.getUUID();
            voucherHead.setVouchID(vouchID);
            // 获取销项发票字表列表
            voucherHead.setVouchID(vouchID);
            voucherHead.setCreatePsnID(user.getUserID());
            voucherHead.setCreateDate(System.currentTimeMillis());
            voucherHead.setCreatepsn(user.getUserName());
            voucherHead.setVoucherNO(0);
            voucherHead.setAccountID(account.getAccountID());
            voucherHead.setSource(0);
            voucherHead.setVcDate(new Date());
            voucherHead.setPeriod(busDate);
            voucherHead.setAuditStatus(0);
            voucherHead.setVouchFlag(0);
            voucherHead.setTotalCredit(totalNamount);
            voucherHead.setTotalDbit(totalNamount);
            // 写入凭证主表
            voucherHeadDao.insertVouchHead(voucherHead);
            VoucherBody vouchBody1 = new VoucherBody();
            vouchBody1.setVouchAID(UUIDUtils.getUUID());
            vouchBody1.setVouchID(vouchID);
            vouchBody1.setDirection("1");
            vouchBody1.setDebitAmount(totalNamount);
            vouchBody1.setSubjectID("5001");
            vouchBody1.setVcsubject("生产成本");
            vouchBody1.setRowIndex("1");
            vouchBody1.setAccountID(account.getAccountID());
            vouchBody1.setUserID(user.getUserID());
            vouchBody1.setUpdatePsnID(user.getUserID());
            vouchBody1.setUpdatePsn(user.getUserName());
            vouchBody1.setUpdatedate(new Date());
            // 写入凭证字表
            voucherBodyDao.insertVouchBody(vouchBody1);
            VoucherBody vouchBody2 = new VoucherBody();
            vouchBody2.setVouchAID(UUIDUtils.getUUID());
            vouchBody2.setVouchID(vouchID);
            vouchBody2.setDirection("2");
            vouchBody2.setDebitAmount(totalNamount);
            String subName = "xxx";
            String subjectID = null;
            SubjectMessage subjectMessage = vatService.querySub(subName, "1403", "7");
            if (subjectMessage != null) {
                subjectID = subjectMessage.getSub_code();
            } else {
                subjectID = vatService.getNumber("1403", "7", "1403000");
                vatService.createSub(subjectID, "1403", subName, "原材料_" + subName);
            }
            vouchBody2.setSubjectID(subjectID);
            vouchBody2.setVcsubject("原材料_" + subName);
            vouchBody2.setRowIndex("2");
            vouchBody2.setAccountID(account.getAccountID());
            vouchBody2.setUserID(user.getUserID());
            vouchBody2.setUpdatePsnID(user.getUserID());
            vouchBody2.setUpdatePsn(user.getUserName());
            vouchBody2.setUpdatedate(new Date());
            // 写入凭证字表
            voucherBodyDao.insertVouchBody(vouchBody2);
            Voucher voucher1 = new Voucher();
            List<VoucherBody> bodyList = new ArrayList<VoucherBody>();
            bodyList.add(vouchBody1);
            bodyList.add(vouchBody2);
            voucher1.setVoucherHead(voucherHead);
            voucher1.setVoucherBodyList(bodyList);
            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher1);
            /*
             * 借：库存商品XXX 贷：生产成本---工资 生产成本---原材料XXX 制造费用
             */
            // 构造凭证头
            VoucherHead voucherHead2 = new VoucherHead();
            String vouchID2 = UUIDUtils.getUUID();
            voucherHead2.setVouchID(vouchID2);
            // 获取销项发票字表列表
            voucherHead2.setVouchID(vouchID);
            voucherHead2.setCreatePsnID(user.getUserID());
            voucherHead2.setCreateDate(System.currentTimeMillis());
            voucherHead2.setCreatepsn(user.getUserName());
            voucherHead2.setVoucherNO(0);
            voucherHead2.setAccountID(account.getAccountID());
            voucherHead2.setSource(0);
            voucherHead2.setVcDate(new Date());
            voucherHead2.setPeriod(busDate);
            voucherHead2.setAuditStatus(0);
            voucherHead2.setVouchFlag(0);

            // 构造库存商品XXX分录
            VoucherBody vouchBody3 = new VoucherBody();
            vouchBody3.setVouchAID(UUIDUtils.getUUID());
            vouchBody3.setVouchID(vouchID2);
            vouchBody3.setDirection("1");
            // vouchBody3.setDebitAmount(totalNamount);
            String subjectName = "xxx";
            String subID = null;
            SubjectMessage subjectMessage1 = vatService.querySub(subjectName, "1405", "7");
            if (subjectMessage != null) {
                subID = subjectMessage1.getSub_code();
            } else {
                subID = vatService.getNumber("1405", "7", "1405000");
                vatService.createSub(subID, "1405", subjectName, "库存商品_" + subjectName);
            }
            vouchBody3.setSubjectID(subID);
            vouchBody3.setVcsubject("库存商品_" + subjectName);
            vouchBody3.setRowIndex("1");
            vouchBody3.setAccountID(account.getAccountID());
            vouchBody3.setUserID(user.getUserID());
            vouchBody3.setUpdatePsnID(user.getUserID());
            vouchBody3.setUpdatePsn(user.getUserName());
            vouchBody3.setUpdatedate(new Date());
            // 构造分录 贷：生产成本---工资
            VoucherBody vouchBody4 = new VoucherBody();
            vouchBody4.setVouchAID(UUIDUtils.getUUID());
            vouchBody4.setVouchID(vouchID2);
            vouchBody4.setDirection("2");
            double sfgz = sfVoucher.getVoucherHead().getTotalCredit();
            vouchBody4.setCreditAmount(sfgz);
            String subName1 = "工资";
            String subID1 = null;
            SubjectMessage subjectMessage2 = vatService.querySub(subName1, "5001", "7");
            if (subjectMessage2 != null) {
                subID1 = subjectMessage2.getSub_code();
            } else {
                subID1 = vatService.getNumber("5001", "7", "5001000");
                vatService.createSub(subID, "5001", subName1, "生产成本_" + subjectName);
            }
            vouchBody4.setSubjectID(subID1);
            vouchBody4.setVcsubject("生产成本_" + subName1);
            vouchBody4.setRowIndex("2");
            vouchBody4.setAccountID(account.getAccountID());
            vouchBody4.setUserID(user.getUserID());
            vouchBody4.setUpdatePsnID(user.getUserID());
            vouchBody4.setUpdatePsn(user.getUserName());
            vouchBody4.setUpdatedate(new Date());
            // 构造分录 生产成本---原材料XXX
            VoucherBody vouchBody5 = new VoucherBody();
            vouchBody5.setVouchAID(UUIDUtils.getUUID());
            vouchBody5.setVouchID(vouchID2);
            vouchBody5.setDirection("2");
            vouchBody5.setCreditAmount(totalNamount);
            String subName2 = "XXX";
            String subID2 = null;
            SubjectMessage subjectMessage3 = vatService.querySub(subName2, "1403", "7");
            if (null != subjectMessage3) {
                subID2 = subjectMessage3.getSub_code();
            } else {
                subID2 = vatService.getNumber("1403", "7", "1403000");
                vatService.createSub(subID2, "1403", subName2, "原材料_" + subjectName);
            }
            vouchBody5.setSubjectID(subID2);
            vouchBody5.setVcsubject("原材料_" + subName2);
            vouchBody5.setRowIndex("3");
            vouchBody5.setAccountID(account.getAccountID());
            vouchBody5.setUserID(user.getUserID());
            vouchBody5.setUpdatePsnID(user.getUserID());
            vouchBody5.setUpdatePsn(user.getUserName());
            vouchBody5.setUpdatedate(new Date());
            // 构造分录 生产成本---制造费用
            VoucherBody vouchBody6 = new VoucherBody();
            vouchBody6.setVouchAID(UUIDUtils.getUUID());
            vouchBody6.setVouchID(vouchID2);
            vouchBody6.setDirection("2");
            double zje = zjVouch.getVoucherHead().getTotalCredit();
            vouchBody6.setCreditAmount(zje);
            String subName3 = "制造费用";
            String subID3 = null;
            SubjectMessage subjectMessage4 = vatService.querySub(subName3, "5001", "7");
            if (subjectMessage4 != null) {
                subID3 = subjectMessage4.getSub_code();
            } else {
                subID3 = vatService.getNumber("5001", "7", "5001000");
                vatService.createSub(subID3, "5001", subName3, "生产成本_" + subName3);
            }
            vouchBody6.setSubjectID(subID3);
            vouchBody6.setVcsubject("生产成本_" + subName3);
            vouchBody6.setRowIndex("4");
            vouchBody6.setAccountID(account.getAccountID());
            vouchBody6.setUserID(user.getUserID());
            vouchBody6.setUpdatePsnID(user.getUserID());
            vouchBody6.setUpdatePsn(user.getUserName());
            vouchBody6.setUpdatedate(new Date());

            voucherHead2.setTotalCredit(sfgz + totalNamount + zje);
            voucherHead2.setTotalDbit(sfgz + totalNamount + zje);
            vouchBody3.setDebitAmount(sfgz + totalNamount + zje);
            voucherHeadDao.insertVouchHead(voucherHead2);
            voucherBodyDao.insertVouchBody(vouchBody4);
            voucherBodyDao.insertVouchBody(vouchBody5);
            voucherBodyDao.insertVouchBody(vouchBody6);

            Voucher vou = new Voucher();
            List<VoucherBody> list1 = new ArrayList<VoucherBody>();
            vou.setVoucherHead(voucherHead2);
            list1.add(vouchBody4);
            list1.add(vouchBody5);
            list1.add(vouchBody6);
            vou.setVoucherBodyList(list1);
            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, vou);

        }
        return null;
    }


}
