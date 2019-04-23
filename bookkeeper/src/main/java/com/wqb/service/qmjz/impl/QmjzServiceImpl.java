package com.wqb.service.qmjz.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.dao.assets.AssetsRecordDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.arch.ArchService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.attached.AttachedService;
import com.wqb.service.jzcb.JzcbService;
import com.wqb.service.jzqnjlr.JzqnjlrService;
import com.wqb.service.jzsy.JzsyService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.qmjz.QmjzService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//原来一键结转改为 一键计提
@Component
@Service("qmjzService")
public class QmjzServiceImpl implements QmjzService {
    @Autowired
    ArchService archService;
    @Autowired
    VatService vatService;
    @Autowired
    AttachedService attachedService;
    @Autowired
    AssetsService assetsService;
    @Autowired
    JzcbService jzcbService;
    @Autowired
    JzsyService jzsyService;
    @Autowired
    JzqnjlrService jzqnjlrService;
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    AssetsRecordDao assetsRecordDao;
    @Autowired
    VoucherHeadService vouHeadService;

    @Autowired
    AssetsDao assetsDao;

    @Autowired
    VatDao vatDao;

    @Autowired
    TBasicSubjectMappingMiddleMapper subjectMiddleMapping;


    //一键计提      原来的 期末结转
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> qmjz(Map<String, Object> param, User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            String busDate = account.getUseLastPeriod();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date bdDate = sdf.parse(busDate + "-00");
            Map<String, String> mappingSubCode = vatService.getAllMappingSubCode(account.getAccountID());
            param.put("mappingCode", mappingSubCode);

            // 一、发放工资
            //archService.arch2vouch4(session);
            // 二、计提工资
            archService.arch2vouch3(user, account);
            // 三.计提增值税
            vatService.zzsCarryover(param);
            // 四.结转附增税
            vatService.fjsCarryover(param);
            // 五、计提本月固定资产折旧。
            assetsService.assets2vouch(user, account);
            // 六、结转销售成本
            vatService.cbCarryover(param);

            // 7， 8， 9 操作移到 一键结转
            // 七：计提企业所得税
            // vatService.jtCarryover(param);
            // 八、将本月收入结转到本年利润。(结转损溢)
            // jzsyService.doJzsy(session);
            // 九、结转全年净利润。 
            // jzqnjlrService.doJzqnjlr(session);

            List<VoucherHead> headList = voucherHeadDao.queryAllVouch(param);
            if (null != headList && headList.size() > 0) {
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", account.getAccountID());
                pa.put("busDate", busDate);
                pa.put("isJt", 1);
                // 更新一键计提状态
                periodStatusDao.updstatusJt(pa);
            } else {
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", account.getAccountID());
                pa.put("busDate", busDate);
                pa.put("isJt", 1);
                periodStatusDao.updstatusJt(pa);
                result.put("message", "无凭证数据,请仔细核查!");
            }
            result.put("success", "true");

            return result;
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("success", "fail");
            throw new BusinessException(e);
        } catch (Exception e) {
            result.put("success", "fail");
            throw new BusinessException(e);
        }

    }

    /**
     * 反计提功能
     */
    @Override

    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> unQmjz(User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String busDate = account.getUseLastPeriod();
            String accountID = account.getAccountID();
            vatService.subinit(user, account);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("busDate", busDate);
            param.put("accountID", accountID);

            List<StatusPeriod> statuList = periodStatusService.queryStatus(param);
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);
                // 是否结转 0否1是
                if (sp.getIsJt() == 0) {
                    result.put("code", -1);
                    result.put("msg", "未计提，无法反计提。。。");
                    result.put("success", "fail");
                    result.put("info", "未计提，无法反计提。。。");
                    return result;
                }
                // 是否已一键生成凭证
                if (sp.getIsCreateVoucher() == 0) {
                    result.put("code", -1);
                    result.put("msg", "未一键生成凭证，无法反计提。。。");
                    result.put("success", "fail");
                    result.put("info", "未一键生成凭证，无法反计提。。。");
                    return result;
                }
                // 0:未审核1:审核
                if (sp.getIsCarryState() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结转，无法反计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结转，无法反计提。。。");
                    return result;
                }
                // 是否检查通过（0否1是）
                Integer isDetection = sp.getIsDetection();
                if (isDetection == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经检查通过，无法反计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经检查通过，无法反计提。。。");
                    return result;
                }

                if (sp.getIsJz() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结账，无法反计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结账，无法反计提。。。");
                    return result;
                }
            }

            // 一:删除 结转销售成本凭证 7
            String source = "7";
            param.put("source", source);
            List<VoucherHead> cbVouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != cbVouchList && cbVouchList.size() > 0) {
                VoucherHead vh = cbVouchList.get(0);
                // String deleteFlag = vh.getIsproblem();// 1有问题2没问题
                String vouchHid = vh.getVouchID();
                List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchHid);
                Voucher voucher = new Voucher();
                voucher.setVoucherHead(vh);
                voucher.setVoucherBodyList(bodyList);
                voucherHeadDao.deleteVouHeadByID(vouchHid);
                voucherBodyDao.deleteVouBodyByID(vouchHid);
                param.put("ufjz", "ufjz");
                param.put("delCbjz", "delCbjz");
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool == false) {
                    throw new BusinessException("ufjz delcbjz error  " + bool);
                }
                if (bool) {
                    // 没问题
                    tBasicSubjectMessageMapper.chgSubAmountByDelete(param, voucher);
                }
                param.remove("ufjz");
                param.remove("delCbjz");
            }
            // 二:删除 计提本月固定资产折旧 2
            source = "2";
            param.put("source", source);
            List<VoucherHead> gdzcVouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != gdzcVouchList && gdzcVouchList.size() > 0) {
                VoucherHead vh = gdzcVouchList.get(0);
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
            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("period", busDate);
            pa.put("accountID", accountID);
            List<AssetsRecord> recordList = assetsRecordDao.queryAssetsRecord(param);
            if (null != recordList && recordList.size() > 0) {
                for (int i = 0; i < recordList.size(); i++) {
                    String assetsID = recordList.get(i).getAssetsID();
                    // 根据固定资产主键跟新数据
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("accountID", accountID);
                    par.put("ssyzje", recordList.get(i).getSsyzje());
                    par.put("assetsID", assetsID);
                    assetsDao.updAssetsByID(par);
                    par.put("period", busDate);
                    assetsRecordDao.delAssetsRecord(par);
                }
            }
            // 三:删除结转增值税
            source = "13";
            param.put("source", source);
            List<VoucherHead> fzsVouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != fzsVouchList && fzsVouchList.size() > 0) {
                for (int i = 0; i < fzsVouchList.size(); i++) {
                    VoucherHead vh = fzsVouchList.get(0);
                    String deleteFlag = vh.getIsproblem();// 1有问题2没问题
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
            }

            // 三:删除留抵税额凭证
            source = "131";
            param.put("source", source);
            List<VoucherHead> ldVouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != ldVouchList && ldVouchList.size() > 0) {
                for (int i = 0; i < ldVouchList.size(); i++) {
                    VoucherHead vh = ldVouchList.get(0);
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
            }
            // 四:删除附增税
            source = "14";
            param.put("source", source);
            List<VoucherHead> zzsVouchList = voucherHeadDao.queryUnQmjzVouch(param);

            if (account.getSsType() == 1) {
                for (int i = 0; i < zzsVouchList.size(); i++) {
                    VoucherHead vh = zzsVouchList.get(0);
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

            } else {
                if (null != zzsVouchList && zzsVouchList.size() > 0) {

                    VoucherHead vh = zzsVouchList.get(0);
                    String deleteFlag = vh.getIsproblem();// 1有问题2没问题
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
                Map<String, Object> par = new HashMap<String, Object>();
                par.put("period", busDate);
                par.put("accountID", accountID);
                par.put("type", 1);
                InvoiceBody tax_j = vatDao.queryTax(par); // 进项税额
                par.put("type", 2);
                InvoiceBody tax_x = vatDao.queryTax(par); // 销项税额
                if (tax_j != null && null != tax_x) {
                    double a = tax_j.getTaxAmount();
                    double b = tax_x.getTaxAmount();
                    double tax = b - a;
                    String direction = tax > 0 ? "1" : "2";
                    SubjectMessage subjectJx = vatService.querySub("进项税", "2221", "10", "1");
                    SubjectMessage subjectXx = vatService.querySub("销项税", "2221", "10", "1");
                    SubjectMessage subjectLd = vatService.querySub("留抵税", "2221", "7", "1");
                    // 不交增值税情况
                    if (fzsVouchList == null && zzsVouchList == null) {
                        // 留底科目金额回退
                        vatService.upSubAmount(a, "1", subjectJx.getSub_code());
                        vatService.upSubAmount(b, "2", subjectXx.getSub_code());
                        if (null != subjectLd) {
                            vatService.upSubAmount(tax, direction, subjectLd.getSub_code());
                        }
                    } else {
                        Map<String, Object> p = new HashMap<String, Object>();
                        p.put("period", busDate);
                        if (null != subjectLd) {
                            p.put("subCode", subjectLd.getSub_code());
                            p.put("accountID", accountID);
                            tBasicSubjectMessageMapper.unLdAmount(p);
                        }
                    }
                }
            }


            // 五:删除计提
            source = "19";
            param.put("source", source);
            List<VoucherHead> jtxzVouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != jtxzVouchList && jtxzVouchList.size() > 0) {
                for (int i = 0; i < jtxzVouchList.size(); i++) {
                    VoucherHead vh = jtxzVouchList.get(i);
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
            }

            // 五:小规模税金反计提
            source = "18";
            param.put("source", source);
            List<VoucherHead> simallTax18VouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != simallTax18VouchList && simallTax18VouchList.size() > 0) {
                for (int i = 0; i < simallTax18VouchList.size(); i++) {
                    VoucherHead vh = simallTax18VouchList.get(i);
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
            }
            source = "8";
            param.put("source", source);
            List<VoucherHead> simallTax8VouchList = voucherHeadDao.queryUnQmjzVouch(param);
            if (null != simallTax8VouchList && simallTax8VouchList.size() > 0) {
                for (int i = 0; i < simallTax8VouchList.size(); i++) {
                    VoucherHead vh = simallTax8VouchList.get(i);
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
            }
            param.put("isJt", "0");
            periodStatusDao.updstatusJt(param);
            param.put("period", busDate);
            result.put("success", "true");
            return result;
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("success", "fail");
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            throw new BusinessException(e);
        }
    }
}
