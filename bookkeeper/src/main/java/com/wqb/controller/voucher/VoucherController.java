package com.wqb.controller.voucher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.PageSub;
import com.wqb.model.vomodel.RedisSub;
import com.wqb.service.UserService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.bank.BankService;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.invoice.InvoiceService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.stateTrack.StateTrackService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherHeadService;
import com.wqb.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/voucher")
public class VoucherController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(VoucherController.class);
    @Autowired
    VoucherService voucherService;
    @Autowired
    VoucherHeadService vouHeadService;
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    VatService vatService;
    @Autowired
    AssetsService assetsService;
    @Autowired
    VoucherHeadService voucherHeadService;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    BankService bankService;
    @Autowired
    JedisClient jedisClient;
    @Autowired
    StateTrackService stateTrackService;
    // 单据 业务层
    @Autowired
    TBasicDocumentsService tBasicDocumentsService;
    @Autowired
    UserDao userDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    ProgressDao progressDao;
    private static Properties properties = null;

    @Autowired
    private UserService userService;

    static {
        InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/address.properties");
        properties = new Properties();
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 一键生成凭证
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/createVoucher")
    @ResponseBody
    Map<String, Object> createVoucher() {
        Map<String, Object> result = new HashMap<String, Object>();

        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        String period = account.getUseLastPeriod();

        try {

            String accountID = account.getAccountID();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", period);
            param.put("period", period);
            param.put("cv", 1);
            List<Progress> proList = progressDao.queryProgress(param);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getCv() == 1) {
                    result.put("code", "1");
                    result.put("message", "fail");
                    result.put("info", "一键生成凭证正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(param); // 正在生成凭证记录
            List<StatusPeriod> statuList = periodStatusDao.queryStatus(param);
            if (null != statuList && statuList.size() > 0) {
                StatusPeriod sp = statuList.get(0);
                if (sp.getIsCreateVoucher() == 1) {
                    result.put("message", "fail");
                    result.put("code", "1");
                    result.put("info", "当前会计期间已生成凭证,请按F5刷新界面查看最新进度");
                    return result;
                }
            }
            vatService.resetCache(param);
            List<RedisSub> allSub = vatService.getAllSub(accountID, period);
            if (allSub != null && allSub.size() < 90) {
                result.put("message", "fail");
                result.put("code", "1");
                result.put("info", "您当前缺失一级会计科目,请仔细检查");
                return result;
            }

            // 查询发票凭证和银行对账单凭证
            List<VoucherHead> hList = voucherHeadService.queryInvAndBankVou(param);

            int ssType = account.getSsType();
            // 一般纳税人
            if (ssType == 0) {
                // 检查发票映射是否已经完成
                Map<String, Object> statuMap = invoiceService.queryInvoiceMappingStatu(accountID, period);
                if (statuMap.get("code") != null && statuMap.get("code").toString().equals("0")) {
                    return retCode(statuMap.get("invoiceType").toString());
                }

                voucherService.createVoucher(user, account); // 一键生成进销项 银行凭证
            } else {


                //2019-02-25 小规模需求变更   可以导入销项发票

                /*************************/
                // 检查发票映射是否已经完成
                Map<String, Object> statuMap = invoiceService.queryOutPutInvoiceMappingStatu(accountID, period);
                if (statuMap.get("code") != null && statuMap.get("code").toString().equals("0")) {
                    //有导入销项发票但是还没有完成映射，必须让客户完成映射，小面的流程不能再去进行
                    return retCode(statuMap.get("invoiceType").toString());
                }

                //小规模销项生成凭证
                invoiceService.invoiceOutOutGenerateVouch(user, account);

                /*************************/


                bankService.bankBill2Vouch(user, account); // 小规模
            }
            param.put("isCreateVoucher", 1);
            periodStatusDao.updStatu1(param); // 记录凭证生成状态
            hList = voucherHeadService.queryInvAndBankVou(param); // 查询凭证头部等于手工凭证的
            // select * from t_vouch_H where accountID=#{accountID} and
            // period=#{busDate} and source!=5;

            // 单据生成凭证
            Map<String, Object> documentsToVoucher = tBasicDocumentsService.documentsToVoucher(user, account);

            param.put("isCreateVoucher", 1);
            periodStatusDao.updStatu1(param);

            result.put("message", "success");
            result.put("info", "一键生成凭证成功!");
            result.put("code", "0");
            param.put("accountID", getAccount().getAccountID());// 賬套ID
            param.put("period", getUserDate());

            return result;

        } catch (BusinessException e) {
            result.put("message", "fail");
            result.put("info", e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("请先完善对账单科目信息!")) {
                result.put("code", "9999");
            } else if (e.getMessage() != null && e.getMessage().contains("请前往银行设置,添加银行账号与科目的映射!")) {
                result.put("code", "-9999");
            }
            logger.error("VoucherController【createVoucher】 一键生成凭证异常!", e);
            return result;
        } catch (Exception e) {
            result.put("code", "1");
            result.put("message", "fail");
            result.put("info", e.getMessage());
            logger.error("VoucherController【createVoucher】 一键生成凭证异常!", e);
            return result;
        } finally {
            resetCache(account, period);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("period", getUserDate());
            param.put("cv", 0);
            try {
                progressDao.chgProgress(param);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }

    }


    // 手动新增凭证
    @SuppressWarnings("unused")
    @RequestMapping("/saveVoucher")
    @ResponseBody
    Map<String, Object> saveVoucher(String voucherHead, String voucherBodyList) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> rtMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        vatService.subinit(user, account);
        String busDate = account.getUseLastPeriod();
        try {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Map<String, String> headMap = gson.fromJson(voucherHead, type);
            Voucher voucher = new Voucher();
            if (null == headMap) {
                rtMap.put("msg", "null == headMap");
                rtMap.put("code", "2");
                return null;
            }

            // queryVouchByNo
            Integer vouchNO = Integer.parseInt(headMap.get("voucherNO").toString());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            param.put("voucherNo", vouchNO);
            Integer maxVoucherNo = vatService.getMaxVoucherNo(account.getAccountID(), busDate);
            if (vouchNO >= maxVoucherNo) {
                // System.out.println("ok");
            } else {
                List<VoucherHead> list = voucherHeadDao.queryVouchByNo(param);
                if (list != null && list.size() > 0) {
                    logger.error("VoucherController【saveVoucher】 保存凭证异常!");
                    result.put("msg", "添加失败,凭证号不允许重复");
                    result.put("code", "1");
                    return result;
                }
            }

            // 计算下一张最大的凭证号
            Integer nextMaxVoucherNo = maxVoucherNo;
            if (vouchNO >= maxVoucherNo) {
                nextMaxVoucherNo = vouchNO + 1;
            }

            VoucherHead vouHead = new VoucherHead();
            vouHead.setAccountID(account.getAccountID());
            vouHead.setPeriod(busDate);
            vouHead.setAuditStatus(0);
            vouHead.setCreateDate(System.currentTimeMillis());
            vouHead.setCreatepsn(user.getUserName());
            vouHead.setCreatePsnID(user.getUserID());
            vouHead.setVcDate(new Date());
            vouHead.setVouchFlag(0);
            vouHead.setVoucherNO(vouchNO);
            vouHead.setVouchFlag(Integer.parseInt(headMap.get("source")));
            vouHead.setSource(5);
            vouHead.setTotalCredit(Double.parseDouble(headMap.get("totalCredit").toString()));
            vouHead.setTotalCredit(Double.parseDouble(headMap.get("totalDbit").toString()));
            voucher.setVoucherHead(vouHead);

            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(voucherBodyList, type2);
            List<VoucherBody> list = new ArrayList<VoucherBody>();

            Map<String, String> bodyMap = new HashMap<String, String>();
            for (JsonObject jsonObject : jsonObjects) {
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                bodyMap = gson.fromJson(jsonObject, type3);
                VoucherBody voucherBody = new VoucherBody();
                voucherBody.setVcabstact(bodyMap.get("vcabstact"));
                voucherBody.setVcunit(bodyMap.get("vcunit"));
                voucherBody.setVcsubject(bodyMap.get("vcsubject"));
                voucherBody.setVcunitID(null);
                voucherBody.setRowIndex(bodyMap.get("rowIndex"));
                voucherBody.setDebitAmount(Double.parseDouble(bodyMap.get("debitAmount")));
                voucherBody.setCreditAmount(Double.parseDouble(bodyMap.get("creditAmount")));
                voucherBody.setSubjectID(bodyMap.get("subjectID"));
                voucherBody.setDirection(bodyMap.get("direction"));
                Object number = bodyMap.get("number");
                if (number != null && !"".equals(number)) {
                    voucherBody.setNumber(Double.parseDouble(number.toString()));
                }
                Object price = bodyMap.get("price");
                if (price != null && !"".equals(price)) {
                    voucherBody.setPrice(Double.parseDouble(price.toString()));
                }

                list.add(voucherBody);
            }

            voucher.setVoucherBodyList(list);

            // 材料串户 盘点损益类型凭证 商品也在贷方
            Map<String, Object> map = vatService.addVbQueryCbjz(voucher);
            if (map != null && map.get("code").toString().equals("1")) {
                return map;
            }
            Map<String, Object> rtSub = voucherService.insertVoucher(voucher, getSession());

            @SuppressWarnings("unchecked")
            List<PageSub> subPage = (List<PageSub>) rtSub.get("data");

            boolean bool = (boolean) rtSub.get("ckv");

            if (bool == false) {
                rtMap.put("code", "9");
            } else {
                rtMap.put("code", "0");
            }

            rtMap.put("msg", "success");
            rtMap.put("data", subPage);
            rtMap.put("nextMaxVoucherNo", nextMaxVoucherNo);

            return rtMap;
        } catch (Exception e) {
            resetCache(account, busDate);
            rtMap.put("msg", "保存凭证异常!");
            rtMap.put("info", e.getMessage());
            rtMap.put("code", "100");
            return rtMap;
        }
    }

    // 修正凭证(编辑凭证第一步)
    @RequestMapping("/toEditVoucher")
    @ResponseBody
    Map<String, Object> toEditVoucher(String vouchID, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Voucher voucher = voucherService.queryVouById(vouchID);
            // 查询是否结转
            Account account = getAccount();
            String userDate = getUserDate();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("busDate", userDate);
            hashMap.put("accountID", account.getAccountID());
            List<StatusPeriod> stalist = periodStatusDao.queryStatus(hashMap);
            if (stalist == null || (stalist != null && stalist.get(0) == null)) {
                throw new BusinessException("期间状态异常");
            }

            result.put("periodStatus", stalist.get(0)); // 期间状态 如果已经结转不能再修改
            result.put("voucher", voucher);
            result.put("code", "0");
            return result;
        } catch (BusinessException e) {
            logger.error("VoucherController【toEditVoucher】 获取凭证信息异常!", e);
            result.put("code", "1");
            result.put("info", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("VoucherController【toEditVoucher】 获取凭证信息异常!", e);
            result.put("code", "2");
            result.put("info", e.getMessage());
            return result;
        }
    }

    // 修正修改凭证(编辑凭证第二步)
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/updVoucher")
    @ResponseBody
    Map<String, Object> updVoucher(String voucherHead, String voucherBodyList) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
        User user = (User) sessionMap.get("user");
        String busDate = (String) sessionMap.get("busDate");
        Account account = (Account) sessionMap.get("account");
        try {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Map<String, String> headMap = gson.fromJson(voucherHead, type);
            Voucher voucher = new Voucher();
            if (null != headMap) {
                Integer vouchNO = Integer.parseInt(headMap.get("voucherNO").toString());
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("accountID", account.getAccountID());
                param.put("period", busDate);
                param.put("voucherNo", vouchNO);
                param.put("vouchID", headMap.get("vouchID"));

                Integer maxVoucherNo = vatService.getMaxVoucherNo(account.getAccountID(), busDate);
                if (maxVoucherNo == null) {
                    throw new BusinessException("saveVoucher getMaxVoucherNo null");
                }
                if (vouchNO >= maxVoucherNo) {
                    System.out.println("ok");
                } else {
                    List<VoucherHead> list = voucherHeadDao.queryVouchByNo(param);
                    if (list != null && list.size() > 0) {
                        String vid = headMap.get("vouchID");
                        VoucherHead head2 = list.get(0);
                        // 修改保存的不是同一张凭证号不允许
                        if (!head2.getVouchID().equals(vid)) {
                            result.put("msg", "保存失败,凭证号不允许重复");
                            result.put("code", "1");
                            return result;
                        }
                    }
                }
                VoucherHead vouHead = new VoucherHead();
                vouHead.setVouchID(headMap.get("vouchID"));
                vouHead.setAccountID(account.getAccountID());
                vouHead.setPeriod(busDate);
                vouHead.setAuditStatus(0);
                vouHead.setCreateDate(System.currentTimeMillis());
                vouHead.setCreatepsn(user.getUserName());
                vouHead.setCreatePsnID(user.getUserID());
                vouHead.setVcDate(new Date());
                vouHead.setVouchFlag(0);
                vouHead.setVoucherNO(Integer.parseInt(headMap.get("voucherNO").toString()));
                vouHead.setVouchFlag(Integer.parseInt(headMap.get("source")));
                vouHead.setSource(Integer.parseInt(headMap.get("source")));
                vouHead.setTotalCredit(Double.parseDouble(headMap.get("totalCredit").toString()));
                vouHead.setTotalDbit(Double.parseDouble(headMap.get("totalDbit").toString()));
                voucher.setVoucherHead(vouHead);
            }
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(voucherBodyList, type2);
            List<VoucherBody> list = new ArrayList<VoucherBody>();

            Map<String, String> bodyMap = new HashMap<String, String>();
            for (JsonObject jsonObject : jsonObjects) {
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                bodyMap = gson.fromJson(jsonObject, type3);
                VoucherBody voucherBody = new VoucherBody();
                voucherBody.setVcabstact(bodyMap.get("vcabstact"));
                voucherBody.setVcunit(bodyMap.get("vcunit"));
                voucherBody.setVcsubject(bodyMap.get("vcsubject"));
                voucherBody.setVcunitID(null);
                voucherBody.setRowIndex(bodyMap.get("rowIndex"));
                Object o1 = bodyMap.get("debitAmount");
                if (null != o1) {
                    voucherBody.setDebitAmount(Double.parseDouble(o1.toString()));
                }
                Object o2 = bodyMap.get("creditAmount");
                if (null != o2) {
                    voucherBody.setCreditAmount(Double.parseDouble(o2.toString()));
                }

                voucherBody.setSubjectID(bodyMap.get("subjectID"));
                voucherBody.setDirection(bodyMap.get("direction"));
                Object number = bodyMap.get("number");
                if (number != null && !"".equals(number)) {
                    voucherBody.setNumber(Double.parseDouble(number.toString()));
                }
                Object price = bodyMap.get("price");
                if (price != null && !"".equals(price)) {
                    voucherBody.setPrice(Double.parseDouble(price.toString()));
                }

                list.add(voucherBody);
            }
            voucher.setVoucherBodyList(list);

            Map<String, Object> res = voucherService.updVoucher(voucher, getSession());
            List<PageSub> data = (List<PageSub>) res.get("data");
            boolean bool = (boolean) res.get("ckv");
            result.put("msg", "success");
            result.put("data", data);
            if (bool == false) {
                result.put("code", "9");
            } else {
                result.put("code", "0");
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            resetCache(account, busDate);
            result.put("msg", "修改凭证异常");
            result.put("code", "100");
            result.put("info", e.getMessage());
            return result;
        }
    }

    // 凭证单个删除
    @RequestMapping("/delVoucher")
    @ResponseBody
    Map<String, Object> delVoucher(@RequestParam(value = "vouchIDs", required = true) String vouchIDs) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        vatService.subinit(user, account);
        param.put("vouchIDs", vouchIDs);
        try {
            Map<String, Object> rtMap = voucherService.deleteVoucher(param, getSession());
            return rtMap;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("code", "2");
            return result;
        }
    }

    // 一键删除凭证
    @SuppressWarnings("unchecked")
    @RequestMapping("/oneKeyDelVouch")
    @ResponseBody
    Map<String, Object> oneKeyDelVouch(@RequestParam(value = "type", required = true) Integer type) {
        // type 1 一键创建 2 一键结转
        // 销项目进项需要重新
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        vatService.subinit(user, account);
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        String busDate = account.getUseLastPeriod();
        param.put("accountID", account.getAccountID());
        param.put("busDate", busDate);
        param.put("period", busDate);
        param.put("state_type", type);
        try {
            List<StatusPeriod> statuslist = periodStatusService.queryStatus(param);
            if (null != statuslist && statuslist.size() > 0) {
                StatusPeriod statusPeriod = statuslist.get(0);
                Integer isCarryState = statusPeriod.getIsCarryState();
                if (1 == isCarryState) {
                    result.put("success", "fail");
                    result.put("info", "凭证已结转,不让再批量删除!");
                    return result;
                }
            }
            voucherService.oneKeyDelVouch(param); // 一键删除

            param.put("isCreateVoucher", 0);

            periodStatusDao.updStatu1(param); // 是否已一键生成凭证
            periodStatusDao.updStatu4(param); // 是否结转


            vatService.resetCache(param);

            result.put("message", "success");
            result.put("success", "true");
            result.put("info", "一键删除凭证成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            resetCache(account, busDate);
            result.put("success", "fail");
            result.put("info", e.getMessage());
            return result;
        }

    }

    private void resetCache(Account acc, String busDate) {
        if (acc == null || StringUtil.isEmpty(acc.getAccountID()) || StringUtil.isEmpty(busDate)) {
            return;
        }
        try {
            if (vatService.getChg() == true) {
                vatService.resetCache(acc.getAccountID(), busDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, Object> retCode(String str) {
        Map<String, Object> hashMap = new HashMap<>();
        String jx = Integer.valueOf(str) == 1 ? "进" : "销";
        hashMap.put("info", "<span style='color:red;'>检测到您导入的" + jx + "项发票还没有对映射的科目进行确认,请先确认再操作</span>");
        hashMap.put("code", "2");
        hashMap.put("invoiceType", str);
        return hashMap;
    }

    /**
     * 凭证审核与反审核(批量)
     *
     * @param auditStatus
     * @param vouchIDs
     * @return
     */
    @RequestMapping("/chgVouchStatu")
    @ResponseBody
    Map<String, Object> chgVouchStatu(String auditStatus, String vouchIDs) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            voucherService.chgVouchStatus(auditStatus, vouchIDs);
        } catch (BusinessException e) {
            result.put("success", "fail");
            logger.error("VoucherController【chgVouchStatu】 批量审核,反审核凭证异常!", e);
            return result;
        }
        result.put("success", "true");
        return result;
    }

    /**
     * 分页查询凭证 凭证列表页
     *
     * @return
     */
    @RequestMapping("/queryAllVoucher")
    @ResponseBody
    Map<String, Object> queryAllVoucher(String source) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> result = null;
        try {
            HttpSession session = getSession();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            if (sessionMap == null) {
                throw new BusinessException("您的身份已过期");
            }
            Account account = (Account) sessionMap.get("account");
            if (account == null) {
                throw new BusinessException("您的身份已过期");
            }
            String busDate = (String) sessionMap.get("busDate");

            if (null != source && !"".equals(source)) {
                param.put("source", source);
            }
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("curPage", getRequest().getParameter("curPage"));
            result = voucherService.queryAllVoucher(param);
            if (null != result) {
                result.put("success", "true");
                return result;
            } else {
                returnMap.put("success", "true");
                returnMap.put("message", "暂无数据");
                return returnMap;
            }
        } catch (Exception e) {
            logger.error("VoucherController【queryAllVoucher】 分页查询凭证异常!", e);
            returnMap.put("success", "fail");
            returnMap.put("message", e.getMessage());
            return result;
        }
    }

    // 添加凭证自动获取最大凭证号
    @RequestMapping("/queryMaxNor")
    @ResponseBody
    Map<String, Object> queryMaxNor() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            String period = getUserDate();
            String accountID = getAccount().getAccountID();
            param.put("period", period);
            param.put("accountID", accountID);
            Integer maxVoucherNo = vouHeadService.getMaxVoucherNo(param);
            returnMap.put("success", "true");
            returnMap.put("maxVoucherNo", maxVoucherNo);
            return returnMap;
        } catch (BusinessException e) {
            logger.error("VoucherController【queryMaxNor】 获取最大凭证号异常!", e);
            returnMap.put("success", "fail");
            returnMap.put("message", e.getMessage());
            return returnMap;
        } catch (Exception e) {
            logger.error("VoucherController【queryMaxNor】 获取最大凭证号异常!", e);
            returnMap.put("success", "fail");
            returnMap.put("message", e.getMessage());
            return returnMap;
        }
    }

    // 添加凭证的页面
    @RequestMapping("/importView")
    public ModelAndView init(ModelAndView modelAndView) {
        modelAndView.setViewName("voucher/import");
        return modelAndView;
    }

    // 修改凭证的页面
    @RequestMapping("/editView")
    public ModelAndView editView(ModelAndView modelAndView) {
        modelAndView.setViewName("voucher/edit");
        return modelAndView;
    }

    @RequestMapping("/searchView")
    public ModelAndView searchView(ModelAndView modelAndView) {
        modelAndView.setViewName("voucher/search");
        return modelAndView;
    }

    @RequestMapping("/createView")
    public ModelAndView createView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("voucher/create");
        return mav;
    }

    // 预览页面
    @RequestMapping("/printPreview")
    public ModelAndView printPreview() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("voucher/printPreview");
        return mav;
    }

    // 查询修正的凭证
    @RequestMapping("/queryRevisedVoucher")
    @ResponseBody
    Map<String, Object> queryRevisedVoucher() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            String period = getUserDate();
            String accountID = getAccount().getAccountID();
            param.put("accountID", accountID);
            param.put("busDate", period);
            param.put("period", period);
            param.put("curPage", getRequest().getParameter("curPage"));
            Map<String, Object> map = voucherService.queryRevisedVoucher(param);
            if (map == null) {
                result.put("success", "true");
                result.put("message", "没有需要修复的凭证");
                return result;
            } else {
                return map;
            }
        } catch (BusinessException e) {
            logger.error("VoucherController【queryRevisedVoucher】 查询需修正凭证异常!", e);
            result.put("success", "fail");
            result.put("message", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("VoucherController【queryRevisedVoucher】 查询需修正凭证异常!", e);
            result.put("success", "fail");
            result.put("message", e.getMessage());
            return result;
        }
    }

    /**
     * 一键审核
     */
    @RequestMapping("/oneKeyCheckVoucher")
    @ResponseBody
    Map<String, Object> oneKeyCheckVoucher(String auditStatus) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String busDate = (String) sessionMap.get("busDate");
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("auditStatus", auditStatus);
            param.put("isCheck", auditStatus);

            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", account.getAccountID());
            para.put("busDate", busDate);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(para);
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);

                if (sp.getIsCheck() == 1) {
                    if (sp.getIsCreateVoucher() == 0) {
                        result.put("code", -1);
                        result.put("msg", "反审核失败，请先一键生成凭证。。。");
                        result.put("success", "fail");
                        result.put("info", "反审核失败，请先一键生成凭证。。。");
                        return result;
                    }
                    if (sp.getIsCarryState() == 0) {
                        result.put("code", -1);
                        result.put("msg", "反审核失败，请先结转。。。");
                        result.put("success", "fail");
                        result.put("info", "反审核失败，请先结转。。。");
                        return result;
                    }

                    Integer isDetection = sp.getIsDetection();
                    if (isDetection == 1) {
                        result.put("code", -1);
                        result.put("msg", "已经检查通过，不能反审核。。。");
                        result.put("success", "fail");
                        result.put("info", "已经检查通过，不能反审核。。。");
                        return result;
                    }
                    if (sp.getIsJz() == 1) {
                        result.put("code", -1);
                        result.put("msg", "已经结账，不能反审核。。。");
                        result.put("success", "fail");
                        result.put("info", "已经结账，不能反审核。。。");
                        return result;
                    }

                    voucherService.oneKeyCheckVoucher(param);
                    periodStatusDao.updStatu5(param);
                    result.put("code", 0);
                    result.put("msg", "反审核成功。。。");
                    result.put("success", "true");
                    result.put("info", "反审核成功。。。");
                    return result;
                }
                if (sp.getIsCreateVoucher() == 0) {
                    result.put("code", -1);
                    result.put("msg", "审核失败，请先一键生成凭证。。。");
                    result.put("success", "fail");
                    result.put("info", "审核失败，请先一键生成凭证。。。");
                    return result;
                }
                if (sp.getIsCarryState() == 0) {
                    result.put("code", -1);
                    result.put("msg", "审核失败，请先结转。。。");
                    result.put("success", "fail");
                    result.put("info", "审核失败，请先结转。。。");
                    return result;
                }
                Integer isDetection = sp.getIsDetection();
                if (isDetection == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经检查通过，不能审核。。。");
                    result.put("success", "fail");
                    result.put("info", "已经检查通过，不能审核。。。");
                    return result;
                }
                if (sp.getIsJz() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结账，不能审核。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结账，不能审核。。。");
                    return result;
                }
            }

            voucherService.oneKeyCheckVoucher(param);
            periodStatusDao.updStatu5(param);
        } catch (BusinessException e) {
            result.put("success", "fail");
            result.put("info", e.getMessage());
            logger.error("VoucherController【chgVouchStatu】 一键审核凭证异常!", e);
            return result;
        }
        result.put("success", "true");
        if ("0".equals(auditStatus)) {
            result.put("info", "一键反审核凭证成功");
        } else {
            result.put("info", "一键审核凭证成功");
        }
        return result;
    }

    // 凭证汇总
    @RequestMapping("/voucherSummary")
    @ResponseBody
    Map<String, Object> queryVouSummary() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");

            Account account = (Account) sessionMap.get("account");
            String period = (String) sessionMap.get("busDate");
            String accountID = account.getAccountID();
            // 查询凭证总数
            param.put("accountID", accountID);
            param.put("period", period);
            int num = voucherHeadDao.queryCountVouch(param);
            List<TBasicSubjectMessage> list = vouHeadService.queryVouSummary(param);
            BigDecimal total_jf = new BigDecimal(0);
            BigDecimal total_df = new BigDecimal(0);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    TBasicSubjectMessage sub = list.get(i);
                    BigDecimal currentAmountDebit = sub.getCurrentAmountDebit();
                    currentAmountDebit = currentAmountDebit == null ? new BigDecimal(0) : currentAmountDebit;
                    total_jf = total_jf.add(currentAmountDebit);
                    BigDecimal currentAmountCredit = sub.getCurrentAmountCredit();
                    currentAmountCredit = currentAmountCredit == null ? new BigDecimal(0) : currentAmountCredit;
                    total_df = total_df.add(currentAmountCredit);
                }
            }
            result.put("code", 1);
            result.put("info", list);
            result.put("num", num);
            result.put("dfAmount", total_df);
            result.put("jfAmount", total_jf);
            return result;
        } catch (BusinessException e) {
            result.put("code", 0);
            result.put("info", e.getMessage());
            logger.error("VoucherController【voucherSummary】 凭证汇总异常!", e);
            return result;
        } catch (Exception e) {
            result.put("code", 0);
            result.put("info", e.getMessage());
            logger.error("VoucherController【voucherSummary】 凭证汇总异常!", e);
            return result;
        }

    }

    // 获取凭证打印范围
    @RequestMapping("/getPrintVoucherRange")
    @ResponseBody
    Map<String, Object> getPrintVoucherRange() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Account account = getAccount();
            String period = getUserDate();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", period);
            Object obj = voucherHeadDao.getPrintVoucherRange(param);
            if (obj != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> pa = (Map<String, Object>) obj;
                String minNo = pa.get("minNo").toString();
                String maxNo = pa.get("maxNo").toString();
                result.put("minNo", Integer.parseInt(minNo));
                result.put("maxNo", Integer.parseInt(maxNo));
            }
            result.put("success", "true");
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("VoucherController【getPrintVoucherRange】 获取凭证号范围异常!", e);
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("VoucherController【getPrintVoucherRange】 获取凭证号范围异常!", e);
            result.put("success", "false");
            return result;
        }
        return result;
    }

    // 凭证打印
    @RequestMapping("/printVoucher")
    @ResponseBody
    Map<String, Object> printVoucher(String begin, String end, String isAll) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            Account account = getAccount();
            String period = getUserDate();
            param.put("accountID", account.getAccountID());
            param.put("period", period);
            if ("0".equals(isAll)) {
                // 查询全部
            } else if ("1".equals(isAll)) {
                // 查询范围内的
                param.put("begin", begin);
                param.put("end", end);
            }
            List<VoucherHead> headList = voucherHeadDao.getRangeVoucher(param);
            List<Voucher> rList = new ArrayList<Voucher>();
            if (null != headList && headList.size() > 0) {
                for (int i = 0; i < headList.size(); i++) {
                    Voucher voucher = new Voucher();
                    VoucherHead vh = headList.get(i);
                    String vouchID = vh.getVouchID();
                    List<VoucherBody> bList = voucherBodyDao.queryVouBodyByHID(vouchID);
                    voucher.setVoucherBodyList(bList);
                    voucher.setVoucherHead(vh);
                    rList.add(voucher);
                }
            }
            result.put("success", "true");
            result.put("list", rList);
            return result;
        } catch (BusinessException e) {
            result.put("success", "fail");
            result.put("message", "打印凭证异常!");
            return result;
        } catch (Exception e) {
            result.put("success", "fail");
            result.put("message", "打印凭证异常!");
            return result;
        }
    }

    @RequestMapping("/invoice2vouch")
    @ResponseBody
    Map<String, Object> invoice2vouch(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = userService.getCurrentUser();
            Account account = userService.getCurrentAccount(user);
            invoiceService.invoice2vouch(user, account);
            return result;
        } catch (BusinessException e) {
            result.put("message", e.getMessage());
            e.printStackTrace();
            return result;
        } catch (Exception e) {
            result.put("message", e.getMessage());
            e.printStackTrace();
            return result;
        }
    }

    @RequestMapping(value = "/downPrintPlugIn")
    public void downPrintPlugIn(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response,
                PathUtil.getClasspath() + Constrants.FILEPATHFILE + "CLodop_Setup_for_Win32NT.exe",
                "print-" + DateUtil.getDays() + ".exe");

    }

    @RequestMapping(value = "/downPrintActiveX")
    public void downPrintActiveX(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "install_lodop32.exe",
                "print-" + DateUtil.getDays() + ".exe");

    }

    @RequestMapping(value = "/downPrintActiveXBit")
    public void downPrintActiveXBit(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "install_lodop64.exe",
                "print-" + DateUtil.getDays() + ".exe");

    }

    // 导入凭证 序时簿导入
    @RequestMapping(value = "/uploadVoucher")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(required = false) MultipartFile voucherFile,
                                      HttpServletRequest request, ModelMap model) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                System.out.println("1111");
                Map<String, Object> map = voucherService.uploadVoucher(voucherFile, request);
                // map.put("success", "true");
                return map;
            } catch (Exception e) {
                result.put("success", "false");
                result.put("message", "导入失败");
                return result;
            }
        } catch (Exception e) {
            logger.error("导入失败", e);
            result.put("success", "false");
            result.put("message", "导入失败");
            return result;
        }
    }

    @RequestMapping(value = "/uploadVoucherAttach")
    @ResponseBody
    public Map<String, Object> uploadVoucherAttach(@RequestParam(required = false) MultipartFile attachFile,
                                                   HttpServletRequest request, ModelMap model) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                Map<String, Object> map = voucherService.uploadVoucherAttach(attachFile, request);
                // map.put("success", "true");
                return map;
            } catch (Exception e) {
                result.put("success", "false");
                result.put("message", "导入失败");
                return result;
            }
        } catch (Exception e) {
            logger.error("导入失败", e);
            result.put("success", "false");
            result.put("message", "导入失败");
            return result;
        }
    }

    // 查询附件
    @RequestMapping(value = "/queryAttach")
    @ResponseBody
    public Map<String, Object> queryAttach(String vouchID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                List<Attach> list = voucherService.queryAttach(vouchID);
                if (list == null || list.size() == 0) {
                    result.put("success", "false");
                    result.put("message", "该凭证尚无附件!");
                    return result;
                }
                result.put("success", "true");
                result.put("url", properties.getProperty("wqb_url_voucher"));
                result.put("list", list);
                return result;
            } catch (Exception e) {
                logger.error("获取凭证附件失败", e);
                result.put("success", "false");
                result.put("message", "获取凭证附件异常");
                return result;
            }
        } catch (Exception e) {
            logger.error("获取凭证附件失败", e);
            result.put("success", "false");
            result.put("message", "获取凭证附件异常");
            return result;
        }
    }

    // 删除附件
    @RequestMapping(value = "/delAttach")
    @ResponseBody
    public Map<String, Object> delAttach(String attachID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                HttpSession session = getSession();
                Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
                User user = (User) sessionMap.get("user");
                Account account = (Account) sessionMap.get("account");
                Integer companyType = account.getCompanyType();
                String busDate = sessionMap.get("busDate").toString();
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", account.getAccountID());
                pa.put("period", busDate);
                pa.put("attachID", attachID);
                voucherService.delAttach(pa);
                result.put("success", "true");
                return result;
            } catch (Exception e) {
                logger.error("删除凭证附件失败", e);
                result.put("success", "false");
                result.put("message", "删除凭证附件异常");
                return result;
            }
        } catch (Exception e) {
            logger.error("删除凭证附件失败", e);
            result.put("success", "false");
            result.put("message", "删除凭证附件异常");
            return result;
        }
    }

    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Map<String, Object> getUserInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                // String gswPhone = "13923454485";
                String gswPhone = "13600000001";
                User user = getUser();
                if (user.getUserType() == 2) {
                    if (gswPhone.equals(user.getLoginUser())) {
                        result.put("success", "true");
                    } else {
                        result.put("success", "true");
                    }
                } else if (user.getUserType() == 5) {
                    String parentID = user.getParentUser();
                    User use = userDao.queryUserById(parentID);
                    if ("13923454485".equals(use.getLoginUser())) {
                        result.put("success", "true");
                    } else {
                        result.put("success", "true");
                    }
                } else if (user.getUserType() == 3 || user.getUserType() == 6) {
                    result.put("success", "true");
                }
                return result;
            } catch (Exception e) {
                result.put("success", "false");
                return result;
            }
        } catch (Exception e) {
            logger.error("获取管理员手机号是否能使用凭证导入功能异常", e);
            result.put("success", "false");
            result.put("msg", "获取管理员手机号是否能使用凭证导入功能异常");
            return result;
        }
    }

    @RequestMapping(value = "/getHistoryBill")
    @ResponseBody
    Map<String, Object> getHistoryBill(String source, String vouchID) {
        // '来源0:进项凭证1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据 7.结转成本 9销项凭证 10结转全年净利润
        // 13结转增值税 14 结转附赠税 15结转企业所得税 18 小规模计提税金',
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("vouchID", vouchID);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if ("0".equals(source) || "9".equals(source)) {
                List<InvoiceHead> headList = invoiceDao.queryByVouchID(param);
                if (null != headList && headList.size() > 0) {
                    for (int i = 0; i < headList.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        InvoiceHead invoiceHead = headList.get(i);
                        String invoiceHID = headList.get(i).getInvoiceHID();
                        List<InvoiceBody> bodyList = invoiceDao.queryInvByHid(invoiceHID);
                        map.put("invoiceHead", invoiceHead);
                        map.put("bodyList", bodyList);
                        list.add(map);
                    }
                }
            } else if ("1".equals(source)) {

            }
            result.put("success", "true");
            result.put("list", list);
        } catch (Exception e) {
            logger.error("获取凭证原始票据信息异常", e);
            result.put("success", "false");
            result.put("msg", "获取凭证原始票据信息异常");
        }
        return result;
    }


    // 查询全部凭证
    @RequestMapping(value = "/queryAllVouchno")
    @ResponseBody
    Map<String, Object> queryAllVouchno() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("busDate", getUserDate());
            List<VoucherHead> headList = voucherHeadDao.queryAllVouch(param);
            result.put("list", headList);
            result.put("success", "true");
        } catch (BusinessException e) {
            logger.error("获取所有凭证号异常!");
            result.put("success", "false");
            result.put("msg", "获取所有凭证号异常");
            return result;
        } catch (Exception e) {
            logger.error("获取所有凭证号异常!");
            result.put("success", "false");
            result.put("msg", "获取所有凭证号异常");
            return result;
        }

        return result;
    }


    /**
     * 复制凭证
     *
     * @return
     */
    @RequestMapping(value = "/copyVoucher")
    @ResponseBody
    Map<String, Object> copyVoucher() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("user", getUser());
            String accountID = getAccount().getAccountID();
            String userDate = getUserDate();
            param.put("accountID", accountID);
            param.put("busDate", userDate);
            param.put("period", userDate);
            String vouchIDOld = getRequest().getParameter("vouchID");
            param.put("vouchID", vouchIDOld);
            param.put("period", getUserDate());
            Voucher voucher = voucherService.copyVoucher(param);

            List<String> vbCode = StringUtil.getVbCode(voucher.getVoucherBodyList());
            List<PageSub> pageSubList = vatService.getPageSubByCodes(accountID, userDate, vbCode);
            result.put("data", pageSubList);
            result.put("voucher", voucher);
            result.put("success", "true");
            result.put("code", "0");
        } catch (BusinessException e) {
            logger.error("复制粘贴凭证异常!");
            result.put("success", "false");
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;
        } catch (Exception e) {
            logger.error("复制粘贴凭证异常!");
            result.put("success", "false");
            result.put("msg", e.getMessage());
            result.put("code", "2");
            return result;
        }
        return result;
    }

    //复制凭证  支持跨月复制


    //1 点击复制
    // 根据凭证id查询要复制的凭证数据返回给前端
    @SuppressWarnings({"unused", "unchecked"})
    @RequestMapping(value = "/getCopyOldVoucher")
    @ResponseBody
    Map<String, Object> getCopyOldVoucher(String vouchID) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            Map<String, Object> data = voucherService.copyVoucher2(vouchID, accountID, period);
            if (data == null) {
                res.put("msg", "未查询到凭证");
                res.put("code", "4");
                return res;
            }
            List<VoucherBody> vbList = (List<VoucherBody>) data.get("vbs");

            //查询默认做账期间返回给前端
            String qijian = voucherService.queryJzPeriod(accountID, period);

            res.put("vbList", vbList);
            res.put("msg", "操作成功");
            res.put("qj", qijian);
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "2");
            return res;
        }

    }


    //保持复制后的
    @SuppressWarnings("unused")
    @RequestMapping(value = "/saveCopyVoucher")
    @ResponseBody
    Map<String, Object> saveCopyVoucher(String vouchID, String voucherNo, String changeDate) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {

            if (StringUtil.isEmpty(vouchID)) {
                res.put("msg", "vouchID is empty");
                res.put("code", "0");
                return res;
            }
            List<VoucherBody> list = voucherBodyDao.queryVouBodyByHID(vouchID);
            if (list == null || list.size() == 0) {
                res.put("msg", "list==null");
                res.put("code", "0");
                return res;
            }

            String accountID = getAccount().getAccountID();
            String period = getUserDate();

            Map<String, Object> data = voucherService.saveCopyVoucher(accountID, period, list, voucherNo, changeDate);
            if (data == null || data.size() == 0 || !data.get("code").equals("0")) {
                if (data != null && data.get("msg") != null) {
                    res.put("msg", data.get("msg"));
                } else {
                    res.put("msg", "saveCopyVoucher ,data==null || data.size()==0 || !data.get(code).equals(0)");
                }
                if (data != null && data.get("code") != null) {
                    res.put("code", data.get("code"));
                } else {
                    res.put("code", "6");
                }
                return res;
            }
            res.put("msg", "粘贴成功");
            res.put("vouchID", data.get("vouchID"));
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", e.getMessage());
            res.put("code", "999");
            return res;
        }

    }


    //复制凭证更改期间
    @RequestMapping(value = "/copyVoucherChangeDate")
    @ResponseBody
    Map<String, Object> copyVoucherChangeDate(@RequestParam(required = true) String changeDate) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            @SuppressWarnings("unused")
            Map<String, Object> param = new HashMap<String, Object>();

            String accountID = getAccount().getAccountID();
            String period = getUserDate();

            Date fomatDate = DateUtil.fomatDate(changeDate);
            if (fomatDate == null) {
                res.put("msg", "时间格式错误");
                res.put("code", "1");
                return res;
            }
            //转换成yyyy-mm字符串格式
            period = DateUtil.getMoth2(fomatDate);

            //获取账套期间最大的凭证号
            Integer maxVoucherNo = vatService.getMaxVoucherNo(accountID, period);

            res.put("msg", "sucess");
            res.put("nextVoucherNo", maxVoucherNo);
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "2");
            return res;
        }

    }


}
