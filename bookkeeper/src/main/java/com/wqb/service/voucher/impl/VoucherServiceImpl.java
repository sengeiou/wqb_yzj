package com.wqb.service.voucher.impl;

import com.wqb.common.*;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.dao.assets.AssetsRecordDao;
import com.wqb.dao.attach.AttachDao;
import com.wqb.dao.bank.*;
import com.wqb.dao.bill.BankBillDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.SubjectDao;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.PageSub;
import com.wqb.service.UserService;
import com.wqb.service.arch.ArchService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.bank.BankService;
import com.wqb.service.bank.TCmBkbillBocService;
import com.wqb.service.invoice.InvoiceService;
import com.wqb.service.proof.ProofService;
import com.wqb.service.stateTrack.StateTrackService;
import com.wqb.service.subBook.SubBookService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Component
@Service("voucherService")
public class VoucherServiceImpl implements VoucherService {
    @Value("${filePaths}")
    private String filePaths;

    @Value("${filePaths_voucher}")
    private String filePaths_voucher;

    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    BankBillDao bankBillDao;
    @Autowired
    PeriodStatusDao periodStatuDao;
    @Autowired
    AssetsService assetsService;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    ArchService archService;
    @Autowired
    ProofService proofService;
    @Autowired
    StateTrackService stateTrackService;
    @Autowired
    VatService vatService;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    BankService bankService;
    @Autowired
    SubBookService subBookService;
    @Autowired
    TccbBankDao tccbBankDao;
    @Autowired
    KcCommodityDao kcCommodityDao;
    @Autowired
    SzrcbBankBillDao szrcbBankBillDao;
    @Autowired
    BcmDao bcmDao;
    @Autowired
    Bcm1Dao bcm1Dao;
    @Autowired
    TCmBkbillBocService tCmBkbillBocService;
    @Autowired
    IcbcBankDao icbcBankDao;
    @Autowired
    JsBankDao jsBankDao;
    @Autowired
    PaBankDao paBankDao;
    @Autowired
    ZsBankDao zsBankDao;
    @Autowired
    NyBankDao nyBankDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    TCmBkbillBocMapper tCmBkbillBocMapper;
    @Autowired
    AttachDao attachDao;
    @Autowired
    AssetsRecordDao assetsRecordDao;
    @Autowired
    AssetsDao assetsDao;
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    TBasicSubjectMappingMiddleMapper subjectMiddleMapping;

    @Autowired
    private UserService userService;

    @SuppressWarnings("unchecked")
    @Override
    /**
     * 手动录制凭证
     */
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> insertVoucher(Voucher voucher, HttpSession session) throws BusinessException {
        try {

            Map<String, Object> res = new HashMap<>();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            String busDate = (String) sessionMap.get("busDate");
            String accountID = account.getAccountID();

            Map<String, Object> map1 = new HashMap<>();
            map1.put("accountID", accountID);
            map1.put("period", busDate);

            VoucherHead vouHead = voucher.getVoucherHead();
            List<VoucherBody> list = voucher.getVoucherBodyList();
            String uuid = UUIDUtils.getUUID();
            vouHead.setVouchID(uuid);

            // 0:非模凭证1:模板凭证
            vouHead.setVouchFlag(0);
            // 审核状态(0:未审核1:审核)
            vouHead.setAuditStatus(0);
            vouHead.setUserID(user.getUserID());
            vouHead.setAccountID(accountID);

            Double totalDrebit = 0.0;
            Double totalCrebit = 0.0;

            vouHead.setTotalCredit(totalCrebit);
            vouHead.setTotalDbit(totalDrebit);
            vouHead.setCreateDate(System.currentTimeMillis());
            vouHead.setCreatePsnID(user.getUserID());
            vouHead.setCreatepsn(user.getUserName());

            boolean b = proofService.checkIsCbjz(list); // 检查添加的凭证是否为成本结转
            if (b == true) {
                vouHead.setVoucherType(57); // 手工添加的成本
            }
            // 来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
            vouHead.setSource(5);
            voucherHeadDao.insertVouchHead(vouHead);
            for (int i = 0; i < list.size(); i++) {
                VoucherBody vouBody = list.get(i);
                // 过滤空行
                if (vouBody.getDebitAmount() == 0 && vouBody.getCreditAmount() == 0) {
                    continue;
                }
                vouBody.setVouchAID(UUIDUtils.getUUID());
                vouBody.setVouchID(uuid);
                vouBody.setUserID(user.getUserID());
                vouBody.setPeriod(busDate);
                vouBody.setAccountID(account.getAccountID());
                vouBody.setRowIndex(String.valueOf(i + 1)); // 设置分录序号
                int count = voucherBodyDao.insertVouchBody(vouBody);
                if (count == 0) {
                    throw new BusinessException("请仔细检查页面数据,出现了异常!");
                }
                String dir = vouBody.getDirection();
                if (dir != null && dir.equals("1")) {
                    totalDrebit = totalDrebit + StringUtil.doubleIsNull(vouBody.getDebitAmount());
                } else if (dir != null && dir.equals("2")) {
                    totalCrebit = totalCrebit + StringUtil.doubleIsNull(vouBody.getCreditAmount());
                }
            }

            // 更新凭证头
            Map<String, Object> voMap = new HashMap<>();

            voMap.put("isproblem", "2");
            voMap.put("totalCredit", totalCrebit);
            voMap.put("totalDbit", totalDrebit);
            voMap.put("vouchID", uuid);
            voucherHeadDao.chgVouchAmount(voMap);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("companyType", account.getCompanyType());

            param.put("rtSub", "saveVoucher");

            if (b == true) {
                param.put("addCbjz", "addCbjz"); // 标识添加的凭证为销售成本结转
            }
            boolean bool = vatService.checkVouch(param, voucher);
            if (bool == true) {

                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);

                List<PageSub> subPage = (List<PageSub>) param.get("saveVoucherToPage");
                if (subPage == null || subPage.isEmpty()) {
                    throw new BusinessException("param.get(saveVoucherToPage) ==null");
                }
                res.put("data", subPage);
            }
            res.put("ckv", bool);
            res.put("code", "0");
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /**
     * 凭证编辑第一步
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Voucher queryVouById(String vouchID) throws BusinessException {
        try {

            Voucher voucher = new Voucher();
            VoucherHead vouHead = voucherHeadDao.queryVouHByID(vouchID);
            List<VoucherBody> list = voucherBodyDao.queryVouBodyByHID(vouchID);
            voucher.setVoucherHead(vouHead);
            voucher.setVoucherBodyList(list);
            return voucher;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /**
     * 凭证编辑第二步
     */
    @SuppressWarnings({"unchecked", "unused"})
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public Map<String, Object> updVoucher(Voucher voucher, HttpSession session) throws BusinessException {
        Map<String, Object> rtMap = new HashMap<>();
        try {

            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            String busDate = (String) sessionMap.get("busDate");
            vatService.subinit(user, account);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            param.put("updVoucher", "updVoucher");

            String vouchID = voucher.getVoucherHead().getVouchID();
            // 修改前的凭证
            VoucherHead vouHeadOld = voucherHeadDao.queryVouHByID(vouchID);

            // 判断凭证是否结账
            String vou_period = vouHeadOld.getPeriod();
            if (StringUtil.isEmpty(vou_period)) {
                throw new BusinessException("期间不能为空");
            }
            if (!vou_period.equals(busDate)) {
                throw new BusinessException("凭证修改期间必须是当前期间并且未结转,请检查凭证期间是否与当前期间一致");
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("busDate", vou_period);
            hashMap.put("accountID", account.getAccountID());
            List<StatusPeriod> stalist = periodStatusDao.queryStatus(hashMap);
            if (stalist == null || (stalist != null && stalist.get(0) == null)) {
                throw new BusinessException("期间状态异常");
            }
            Integer isJz = stalist.get(0).getIsJz();
            if (isJz != null && isJz != 0) {
                throw new BusinessException("已结账不能修改");
            }

            Integer source = vouHeadOld.getSource(); // 凭证来源
            Integer voucherType = vouHeadOld.getVoucherType(); // 凭证类型

            String isproblem = vouHeadOld.getIsproblem(); // 原凭证isproblem

            List<VoucherBody> listOld = voucherBodyDao.queryVouBodyByHID(vouchID);
            Voucher vouchOld = new Voucher();
            vouchOld.setVoucherBodyList(listOld);
            vouchOld.setVoucherHead(vouHeadOld);

            if (source != null && source == 7) {
                param.put("modiyCbjz", "modiyCbjz");
            } else {
                if (voucherType != null && (voucherType == 57 || voucherType == 117)) {
                    // 57手工凭证 117序时薄导入的凭证
                    param.put("modiyCbjz", "modiyCbjz");
                }
            }

            for (int i = 0; i < listOld.size(); i++) {
                voucherBodyDao.deleteVouBodyByID(listOld.get(i).getVouchID());
            }
            voucherHeadDao.deleteVouHeadByID(vouHeadOld.getVouchID());

            Voucher newVouch = new Voucher();

            if (!StringUtil.isEmpty(isproblem) && "2".equals(isproblem)) {
                // 完整的凭证修改需要先删除 凭证里面原来的金额或者数量， 然后再添加新的
                tBasicSubjectMessageMapper.chgSubAmountByDelete(param, vouchOld);
            }
            VoucherHead voucherHead = voucher.getVoucherHead();

            voucherHead.setPeriod(vou_period);
            voucherHead.setSource(source);
            voucherHead.setVoucherType(voucherType);
            voucherHeadDao.insertVouchHead(voucherHead);
            List<VoucherBody> newBodyList = voucher.getVoucherBodyList();

            List<VoucherBody> tempList = new ArrayList<VoucherBody>();
            for (int j = 0; j < newBodyList.size(); j++) {
                VoucherBody body = newBodyList.get(j);
                body.setRowIndex(String.valueOf(j + 1));
                String uuid = UUIDUtils.getUUID();
                body.setVouchAID(uuid);
                if (body.getVouchID() == null) {
                    body.setVouchID(vouchID);
                }
                body.setPeriod(vou_period);
                body.setAccountID(account.getAccountID());
                tempList.add(body);
            }
            for (int k = 0; k < tempList.size(); k++) {
                voucherBodyDao.insertVouchBody(tempList.get(k));
            }
            newVouch.setVoucherHead(voucher.getVoucherHead());
            newVouch.setVoucherBodyList(tempList);

            boolean bool = vatService.checkVouch(param, newVouch);
            if (bool) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, newVouch);

                List<VoucherBody> arrVb = new ArrayList<>();
                arrVb.addAll(listOld);
                arrVb.addAll(tempList);
                List<String> vbCode = StringUtil.getVbCode(arrVb);
                List<PageSub> pageSubList = vatService.getPageSubByCodes(account.getAccountID(), busDate, vbCode);
                rtMap.put("data", pageSubList);

            }
            rtMap.put("code", "0");
            rtMap.put("ckv", bool);
            return rtMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = BusinessException.class)
    // 一键生成凭证 service
    @Override
    public void createVoucher(User user, Account account) throws BusinessException {
        try {
            long start1 = System.currentTimeMillis();
            // 检测科目映射是否已经完成
            checkMappingSub(account);

            // 查询出账套下会计期间内的所有发票
            invoiceService.invoice2vouch(user, account);

            // 固定资产折旧生成凭证
            // assetsService.assets2vouch(session);
            // 薪资生成凭证(计提应发工资)
            // archService.arch2vouch1(session);
            // 发放工资
            // archService.arch2vouch2(session);
            // 银行对账单生成凭证
            bankService.bankBill2Vouch(user, account);
        } catch (BusinessException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    private void checkMappingSub(Account account) throws BusinessException {
        try {
            // 查询用户初始科目映射数据
            List<TBasicSubjectMappingMiddle> subMappingList = subjectMiddleMapping
                    .querySubMappingMiddleByAccId(account.getAccountID());
            if (subMappingList == null || subMappingList.size() == 0) {
                throw new BusinessException("未检查到科目初始化映射,请确认是否已经对科目进行映射处理 voucherServoce checkMappingSub");
            }
            for (TBasicSubjectMappingMiddle tb : subMappingList) {
                String systemSubName = tb.getSubMappingName(); // 系统科目名称
                String userCode = tb.getSubMessageCode(); // 用户映射 后的科目编码
                if (StringUtil.isEmpty(systemSubName) || StringUtil.isEmpty(userCode)) {
                    throw new BusinessException("检查到中间映射表系统科目名称或者用户科目编码为空   " + tb);
                }
            }
            List<String> baseSubName = assetsService.getToBaseSub();

            for (String sb : baseSubName) {
                boolean flg = false;
                for (TBasicSubjectMappingMiddle mappingSub : subMappingList) {
                    String subMappingName = mappingSub.getSubMappingName();
                    if (sb.equals(subMappingName)) {
                        flg = true;
                        break;
                    }
                }
                if (flg == false) {
                    throw new BusinessException("检测到科目【" + sb + "】还没有进行科目初始化映射,请先把科目映射完成");
                }
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void chgVouchStatus(String auditStatus, String vouchIDs) throws BusinessException {
        try {
            if (null == vouchIDs || "".equals(vouchIDs)) {
                return;
            } else {
                String[] vouchID = vouchIDs.split(",");
                for (int i = 0; i < vouchID.length; i++) {
                    voucherHeadDao.chgVouchStatu(auditStatus, vouchID[i]);
                }
            }
        } catch (BusinessException e) {
            throw new BusinessException();
        }
    }

    /**
     * 分页查询凭证(凭证序时簿)
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> queryAllVoucher(Map<String, Object> param) throws BusinessException {
        try {

            Map<String, Object> result = new HashMap<String, Object>();
            Integer maxPage = 100;
            Integer curPage = Integer.parseInt(param.get("curPage").toString());
            List<VoucherHead> totalHeadList = voucherHeadDao.queryVouHead(param);
            param.put("begin", (curPage - 1) * maxPage);
            param.put("maxPage", maxPage);
            List<VoucherHead> headList = voucherHeadDao.queryVouHead(param);
            getClass();
            List<Voucher> list = new ArrayList<Voucher>();
            if (null != headList && headList.size() > 0) {
                for (VoucherHead voucherHead : headList) {
                    Voucher voucher = new Voucher();
                    String vouchID = voucherHead.getVouchID();
                    List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchID);
                    voucher.setVoucherHead(voucherHead);
                    voucher.setVoucherBodyList(bodyList);
                    list.add(voucher);
                }
            }
            if (null != totalHeadList && totalHeadList.size() > 0) {
                result.put("totalCount", totalHeadList.size());
                result.put("list", list);
                result.put("maxPage", maxPage);
                return result;
            }
            return null;
        } catch (BusinessException e) {
            throw new BusinessException();
        }
    }

    /**
     * 生成凭证时 变更相应的科目金额
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> chgSubAmountByCreate(Voucher voucher) throws BusinessException {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            VoucherHead vouchHead = voucher.getVoucherHead();
            List<VoucherBody> vouchBodyList = voucher.getVoucherBodyList();
            if (vouchBodyList != null && vouchBodyList.size() > 0) {
                for (VoucherBody vouchBody : vouchBodyList) {
                    Map<String, Object> param = new HashMap<String, Object>();
                    // 账套ID
                    String accountID = vouchHead.getAccountID();
                    // 期间
                    String period = vouchHead.getPeriod();
                    // 借贷方向
                    String direction = vouchBody.getDirection();
                    // 科目ID
                    String subjectID = vouchBody.getSubjectID();
                    // 借
                    double jfAmount = vouchBody.getDebitAmount();
                    // 贷方金额
                    double dfAmount = vouchBody.getCreditAmount();
                    param.put("accountID", accountID);
                    param.put("period", period);
                    param.put("direction", direction);
                    param.put("subjectID", subjectID);
                    param.put("jfAmount", jfAmount);
                    param.put("dfAmount", dfAmount);
                    // tBasicSubjectMessageMapper.chgSubAmountByCreate(param);
                }
            }
            result.put("success", "true");
            return result;

        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void oneKeyCheckVoucher(Map<String, Object> param) throws BusinessException {
        try {
            voucherHeadDao.oneKeyCheckVoucher(param);
        } catch (BusinessException e) {
            throw new BusinessException();
        } catch (Exception e) {
            throw new BusinessException();
        }

    }

    @Override
    // 查询需要修正的凭证
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> queryRevisedVoucher(Map<String, Object> param) throws BusinessException {
        try {

            Map<String, Object> result = new HashMap<String, Object>();
            Integer maxPage = 50;
            Integer curPage = Integer.parseInt(param.get("curPage").toString());
            param.put("isProblem", 1);
            List<VoucherHead> totalProblemList = voucherHeadDao.queryRevisedVoucher(param);
            param.put("begin", (curPage - 1) * maxPage);
            param.put("maxPage", maxPage);
            List<VoucherHead> problemList = voucherHeadDao.queryRevisedVoucher(param);
            List<Voucher> list = new ArrayList<Voucher>();
            if (null != problemList && problemList.size() > 0) {
                for (VoucherHead vh : problemList) {
                    Voucher voucher = new Voucher();
                    List<VoucherBody> vbList = voucherBodyDao.queryVouBodyByHID(vh.getVouchID());
                    voucher.setVoucherBodyList(vbList);
                    voucher.setVoucherHead(vh);
                    list.add(voucher);
                }
            }
            if (null != totalProblemList && totalProblemList.size() > 0) {
                result.put("totalCount", totalProblemList.size());
                result.put("list", list); // 返回修正的凭证
                result.put("maxPage", maxPage);
                result.put("success", "true");
                return result;
            }
            return null;
        } catch (BusinessException e) {
            throw new BusinessException();
        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    @SuppressWarnings("unused")
    @Override
    // 一键删除凭证
    @Transactional(rollbackFor = BusinessException.class)
    public void oneKeyDelVouch(Map<String, Object> param) throws BusinessException {
        try {

            // 一键清除明细账
            int delnum = subBookService.delSubBookBath2(param);
            List<VoucherHead> headList = voucherHeadDao.queryAllVouch(param);
            List<Voucher> fullVoucherList = new ArrayList<Voucher>(); // 定义 完整凭证
            List<Voucher> emptyVoucherList = new ArrayList<Voucher>(); // 定义 空凭证
            if (null == headList && headList.isEmpty()) {
                return;
            }
            // 遍历所有凭证头
            for (VoucherHead vouchHead : headList) {
                Voucher voucher = new Voucher();
                String vouchID = vouchHead.getVouchID();
                String isproblem = vouchHead.getIsproblem();
                List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchID);
                if (null != bodyList && bodyList.size() > 0) {
                    if (isproblem != null && isproblem.equals("2")) {
                        voucher.setVoucherHead(vouchHead);
                        voucher.setVoucherBodyList(bodyList);
                        fullVoucherList.add(voucher);
                    } else {
                        voucher.setVoucherHead(vouchHead);
                        voucher.setVoucherBodyList(bodyList);
                        emptyVoucherList.add(voucher);
                    }
                } else {
                    voucherHeadDao.deleteVouHeadByID(vouchID);
                }
            }

            // 完整凭证
            if (fullVoucherList != null && fullVoucherList.size() > 0) {
                for (Voucher voucher : fullVoucherList) {
                    VoucherHead vouchHead = voucher.getVoucherHead();
                    String voucheID = vouchHead.getVouchID(); // 凭证头
                    @SuppressWarnings("unused")
                    List<VoucherBody> list = voucher.getVoucherBodyList();
                    Object sourceObj = voucher.getVoucherHead().getSource();
                    int source = 100;
                    if (sourceObj != null) {
                        source = voucher.getVoucherHead().getSource();
                    }
                    voucherBodyDao.deleteVouBodyByID(voucheID);
                    voucherHeadDao.deleteVouHeadByID(voucheID);

                    if (0 == source) {// 发票
                        invoiceDao.updInvoiceByVouID(voucheID);
                    } else if (1 == source) {// 银行
                        tccbBankDao.delVouchID(voucheID);
                        szrcbBankBillDao.delVouchID(voucheID);
                        bcmDao.delVouchID(voucheID);
                        bcm1Dao.delVouchID(voucheID);
                        icbcBankDao.delVouchID(voucheID);
                        jsBankDao.delVouchID(voucheID);
                        paBankDao.delVouchID(voucheID);
                        zsBankDao.delVouchID(voucheID);
                        tCmBkbillBocMapper.delVouchID(voucheID);
                    }

                }
            }
            // 来源0:进项凭证1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据 7.结转成本 9销项凭证
            // 不完整凭证
            if (emptyVoucherList != null && emptyVoucherList.size() > 0) {
                for (Voucher voucher : emptyVoucherList) {
                    VoucherHead vouchHead = voucher.getVoucherHead();
                    String voucheID = vouchHead.getVouchID();
                    Integer source = voucher.getVoucherHead().getSource();
                    voucherBodyDao.deleteVouBodyByID(voucheID);
                    voucherHeadDao.deleteVouHeadByID(voucheID);
                    if (0 == source) {// 发票
                        invoiceDao.updInvoiceByVouID(voucheID);
                    } else if (1 == source) {// 银行
                        tccbBankDao.delVouchID(voucheID);
                        szrcbBankBillDao.delVouchID(voucheID);
                        bcmDao.delVouchID(voucheID);
                        bcm1Dao.delVouchID(voucheID);
                        icbcBankDao.delVouchID(voucheID);
                        jsBankDao.delVouchID(voucheID);
                        paBankDao.delVouchID(voucheID);
                        zsBankDao.delVouchID(voucheID);
                        tCmBkbillBocMapper.delVouchID(voucheID);
                    }
                }
            }

            // 更新科目 清除本期 期末变为期初

            int upAllKcc = kcCommodityDao.upAllKcc(param);
            int upAllSub = kcCommodityDao.upAllSub(param);

        } catch (BusinessException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    // 删除单个凭证
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> deleteVoucher(Map<String, Object> param, HttpSession session) throws BusinessException {

        Map<String, Object> hashMap = new HashMap<>();
        try {

            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            String busDate = sessionMap.get("busDate").toString();
            param.put("userID", user.getUserID());
            param.put("period", busDate);
            param.put("busDate", busDate);
            param.put("accountID", account.getAccountID());
            String vh_id = param.get("vouchIDs").toString();

            Voucher voucher = new Voucher();
            List<VoucherBody> list = voucherBodyDao.queryVouBodyByHID(vh_id);
            VoucherHead vouchHead = voucherHeadDao.queryVouHByID(vh_id);
            voucher.setVoucherBodyList(list);
            voucher.setVoucherHead(vouchHead);
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("accountID", account.getAccountID());
            param1.put("busDate", busDate);

            // 标志 返回给前端变化的科目数据
            param1.put("rtSub", "deleteVoucher");

            String isproblem = vouchHead.getIsproblem();

            if ("2".equals(isproblem)) {
                boolean bool = checkModiyjxpz(voucher);
                if (bool == true) {
                    param1.put("delCbjz", "delCbjz");
                    tBasicSubjectMessageMapper.chgSubAmountByDelete(param1, voucher);
                    param1.remove("delCbjz");
                } else {
                    tBasicSubjectMessageMapper.chgSubAmountByDelete(param1, voucher);
                }

                //// 获取 返回给前端变化的科目数据
                List<PageSub> pageSubList = (List<PageSub>) param1.get("deleteVoucherToPage");
                if (pageSubList == null || pageSubList.isEmpty()) {
                    throw new BusinessException("deleteVoucher  param.get(deleteVoucherToPage) == null");
                }

                hashMap.put("data", pageSubList);
            }

            // 凭证分录都没有问题删除科目金额

            int source = -100;
            if (vouchHead.getSource() != null) {
                source = vouchHead.getSource();
            } else {
                source = -100;
            }
            // 0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证
            voucherBodyDao.deleteVouBodyByID(vh_id); // 删除凭证主表
            voucherHeadDao.deleteVouHeadByID(vh_id); // 删除凭证子表

            // 更新 发票,银行对账单,工资,固定资产对凭证的引用 ,把凭证id置空
            if (0 == source || 9 == source) {// 发票
                invoiceDao.updInvoiceByVouID(vh_id);
            } else if (1 == source) {// 银行
                tccbBankDao.delVouchID(vh_id);
                szrcbBankBillDao.delVouchID(vh_id);
                bcmDao.delVouchID(vh_id);
                bcm1Dao.delVouchID(vh_id);
                icbcBankDao.delVouchID(vh_id);
                jsBankDao.delVouchID(vh_id);
                paBankDao.delVouchID(vh_id);
                zsBankDao.delVouchID(vh_id);
                nyBankDao.delVouchID(vh_id);
                tCmBkbillBocMapper.delVouchID(vh_id);
            }
            // 回退固定资产折旧和删除折旧明细
            if (2 == source) {
                param.put("source", "2");
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("period", busDate);
                pa.put("accountID", account.getAccountID());
                List<AssetsRecord> recordList = assetsRecordDao.queryAssetsRecord(param);
                if (null != recordList && recordList.size() > 0) {
                    for (int i = 0; i < recordList.size(); i++) {
                        String assetsID = recordList.get(i).getAssetsID();
                        // 根据固定资产主键跟新数据
                        Map<String, Object> par = new HashMap<String, Object>();
                        par.put("accountID", account.getAccountID());
                        par.put("ssyzje", recordList.get(i).getSsyzje());
                        par.put("assetsID", assetsID);
                        assetsDao.updAssetsByID(par);
                        par.put("period", busDate);
                        assetsRecordDao.delAssetsRecord(par);
                    }
                }
            }
            hashMap.put("code", "0");
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @SuppressWarnings("unused")
    public boolean checkModiyjxpz(Voucher voucher) throws Exception {
        VoucherHead vh = voucher.getVoucherHead();
        List<VoucherBody> vbList = voucher.getVoucherBodyList();
        Integer voucherType = vh.getVoucherType();
        Integer source = vh.getSource();
        if (source != null && source == 7) {
            return true;
        } else {
            if (voucherType != null && (voucherType == 57 || voucherType == 117)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)

    public Map<String, Object> uploadVoucher(MultipartFile file, HttpServletRequest request) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> vhid = new ArrayList<>();
        List<Voucher> vchList = new ArrayList<>();
        try {
            if (file != null && !file.isEmpty()) {
                User user = userService.getCurrentUser();
                Account account = userService.getCurrentAccount(user);
                vatService.subinit(user, account);
                String accountID = account.getAccountID();
                String busDate = account.getUseLastPeriod();
                Map<String, Object> repeatPa = new HashMap<String, Object>();
                Map<String, Object> par = new HashMap<String, Object>();
                repeatPa.put("accountID", accountID);
                repeatPa.put("period", busDate);
                repeatPa.put("source", 11);
                par.put("accountID", accountID);
                par.put("period", busDate);
                par.put("busDate", busDate);
                List<VoucherHead> repeatList = voucherHeadDao.queryImportVoucher(repeatPa);
                if (null != repeatList && repeatList.size() > 0 && repeatList.get(0) != null
                        && repeatList.get(0).getVouchID() != null && !"".equals(repeatList.get(0).getVouchID())) {
                    result.put("success", "false");
                    result.put("message", "系统当期已导入过凭证,请先删除完毕才能再次导入!");
                    return result;
                }

                String filePath = filePaths + "/" + user.getUserID() + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath, "voucher-" + System.currentTimeMillis());
                List<Map<String, Object>> voucherList = ReadExcal.readExcel(filePath, fileName, 0, 0);
                if (null != voucherList && voucherList.size() > 0) {
                    // 日期
                    String rqKey = null;
                    // 摘要
                    String zyKey = null;
                    // 科目编码
                    String subjectIDKey = null;
                    // 科目名称
                    String subjectFullNameKey = null;
                    // 借方金额
                    String debitAmountKey = null;
                    // 贷方金额
                    String creditAmountKey = null;
                    // 数量
                    String slKey = null;
                    // 单价
                    String djKey = null;
                    Map<String, Object> titleMap = voucherList.get(0);
                    Set<String> set = titleMap.keySet();
                    Iterator<String> it = set.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        if (obj == null || "".equals(obj.toString().trim())) {
                            continue;
                        } else {
                            String key = obj.toString();
                            String value = titleMap.get(key) == null ? "" : titleMap.get(key).toString().trim();
                            if ("日期".equals(value)) {
                                rqKey = key;
                            } else if ("摘要".equals(value)) {
                                zyKey = key;
                            } else if ("科目代码".equals(value)) {
                                subjectIDKey = key;
                            } else if ("科目名称".equals(value)) {
                                subjectFullNameKey = key;
                            } else if ("借方金额".equals(value)) {
                                debitAmountKey = key;
                            } else if ("贷方金额".equals(value)) {
                                creditAmountKey = key;
                            } else if ("数量".equals(value)) {
                                slKey = key;
                            } else if ("单价".equals(value)) {
                                djKey = key;
                            }
                        }
                    }
                    if (rqKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失日期一栏!");
                        return result;
                    }
                    if (zyKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失摘要一栏!");
                        return result;
                    }
                    if (subjectIDKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失科目代码一栏!");
                        return result;
                    }
                    if (subjectFullNameKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失科目名称一栏!");
                        return result;
                    }

                    if (debitAmountKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失借方金额一栏!");
                        return result;
                    }

                    if (creditAmountKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失贷方金额一栏!");
                        return result;
                    }

                    if (slKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失数量一栏!");
                        return result;
                    }

                    if (djKey == null) {
                        result.put("success", "fasle");
                        result.put("message", "请检查列头,缺失单价一栏!");
                        return result;
                    }

                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("accountID", account.getAccountID());
                    param.put("busDate", busDate);
                    param.put("period", busDate);
                    VoucherHead voucherHead = null;
                    boolean flag = true;
                    String message = "请仔细检查科目编码【";
                    Set<String> subSet = new HashSet<String>();
                    List<TBasicSubjectMessage> sysList = tBasicSubjectMessageMapper.queryAllSubject(param);
                    if (sysList != null && sysList.size() > 0 && sysList.get(0) != null) {
                        for (int i = 0; i < voucherList.size(); i++) {
                            boolean problem = false;
                            Map<String, Object> map = voucherList.get(i);
                            String subjectID = null;
                            Object subjectIDObj = map.get(subjectIDKey);
                            subjectID = subjectIDObj.toString().trim();
                            if (null == subjectID || "".equals(subjectID)) {
                                result.put("success", "false");
                                result.put("message", "请仔细检查科目编码,有存在为空的情况,导入失败");
                                return result;
                            }
                            Object periodObj = map.get(rqKey);
                            if ("日期".equals(periodObj)) {
                                continue;
                            }
                            if (periodObj == null || "".equals(periodObj)) {
                                Map<String, Object> tempMap = null;
                                for (int j = i - 1; j > 0; j--) {
                                    tempMap = voucherList.get(j);
                                    {
                                        Object obj = tempMap.get(rqKey);
                                        if (obj == null || "".equals(obj)) {
                                            continue;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                periodObj = tempMap.get(rqKey);
                            }
                            if (periodObj != null && !"".equals(periodObj)) {
                                String qj = periodObj.toString().trim().substring(0, 7);
                                if (!busDate.equals(qj.replaceAll("/", "-"))) {
                                    continue;
                                }
                            }
                            param.put("subjectID", subjectID);
                            if (sysList != null && sysList.size() > 0) {
                                for (int a = 0; a < sysList.size(); a++) {
                                    if (sysList.get(a).getSubCode().trim().equals(subjectID.trim())) {
                                        problem = true;
                                        break;
                                    }
                                }
                                if (!problem) {
                                    flag = false;
                                    subSet.add(subjectID + ",");
                                }
                            }
                        }
                    } else {
                        result.put("success", "fasle");
                        result.put("message", "请先初始化科目!");
                        return result;
                    }
                    if (!flag) {
                        List<String> li = new ArrayList<>(subSet);
                        Collections.sort(li);
                        Iterator it1 = li.iterator();
                        while (it1.hasNext()) {
                            message += it1.next().toString();
                        }
                        result.put("message", message.substring(0, message.length() - 1) + "】,请您手动修改,添加");
                        return result;
                    }
                    StringBuffer errorMsg = new StringBuffer();
                    for (int i = 0; i < voucherList.size(); i++) {
                        Map<String, Object> map = voucherList.get(i);
                        Object periodObj = map.get(rqKey);
                        String sl = null;
                        Object slObj = map.get(slKey);
                        if (null != slObj && !"".equals(slObj)) {
                            sl = slObj.toString().trim();
                        }
                        String dj = null;
                        Object djObj = map.get(djKey);
                        if (null != djObj && !"".equals(djObj)) {
                            dj = djObj.toString().trim();
                        }
                        String subjectID = null;
                        Object subjectIDObj = map.get(subjectIDKey);
                        if (null != subjectIDObj && !"".equals(subjectIDObj)) {
                            subjectID = subjectIDObj.toString();
                        }
                        if ("日期".equals(periodObj)) {
                            continue;
                        } else if (subjectID.startsWith("1403") || subjectID.startsWith("1405")) {
                            if ((sl == null || "".equals(sl)) || (null == dj || "".equals(dj))) {
                                errorMsg.append("【" + (i + 1) + "】,");
                            }
                        }
                    }
                    if (!"".equals(errorMsg.toString())) {
                        errorMsg.deleteCharAt(errorMsg.length() - 1).append("行,出现了数量或单价为空的情况,请先修复再尝试导入");
                        result.put("success", "false");
                        result.put("message", "因涉及到原材料或库存商品,第" + errorMsg.toString());
                        return result;
                    }

                    boolean in = false;
                    for (int i = 0; i < voucherList.size(); i++) {
                        Map<String, Object> map = voucherList.get(i);
                        String period = null;
                        Object periodObj = map.get(rqKey);
                        String zy = null;
                        Object zyObj = map.get(zyKey);
                        String subjectID = null;
                        Object subjectIDObj = map.get(subjectIDKey);
                        String subjectFullName = null;
                        Object subjectFullNameObj = map.get(subjectFullNameKey);
                        String debitAmount = null;
                        Object debitAmountObj = map.get(debitAmountKey);
                        String creditAmount = null;
                        Object creditAmountObj = map.get(creditAmountKey);
                        String sl = null;
                        Object slObj = map.get(slKey);
                        if (null != slObj && !"".equals(slObj)) {
                            sl = slObj.toString().trim();
                        }
                        String dj = null;
                        Object djObj = map.get(djKey);
                        if (null != djObj && !"".equals(djObj)) {
                            dj = djObj.toString().trim();
                        }
                        if (subjectIDObj != null && !"".equals(subjectIDObj)) {
                            if ("日期".equals(periodObj)) {
                                continue;
                            } else {
                                VoucherBody vb = new VoucherBody();
                                vb.setRowIndex((i + 1) + "");
                                if (null != periodObj && !"".equals(periodObj)) {
                                    // 需要新建凭证头
                                    voucherHead = new VoucherHead();
                                    String vouchID = UUIDUtils.getUUID();
                                    voucherHead.setVouchID(vouchID);
                                    Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                                    voucherHead.setVoucherNO(maxVoucherNo);
                                    voucherHead.setSource(11);
                                    voucherHead.setDes("导入凭证");
                                    voucherHead.setAccountID(accountID);
                                    period = periodObj.toString().trim().substring(0, 7);
                                    if (!busDate.equals(period.replaceAll("/", "-"))) {
                                        continue;
                                    }
                                    in = true;
                                    voucherHead.setPeriod(period.replaceAll("/", "-"));
                                    voucherHead.setVcDate(new Date());
                                    voucherHead.setCreatePsnID(user.getUserID());
                                    voucherHead.setCreateDate(System.currentTimeMillis() + i);
                                    voucherHeadDao.insertVouchHead(voucherHead);

                                    vb.setVouchID(vouchID);

                                    vb.setVouchAID(UUIDUtils.getUUID());
                                    vb.setPeriod(period.replaceAll("/", "-"));

                                    vb.setAccountID(accountID);
                                    // vb.setRowIndex(i + "");
                                    vb.setSubjectID(subjectIDObj.toString().trim());
                                    if (debitAmountObj != null && !"".equals(debitAmountObj)
                                            && !"0".equals(debitAmountObj)) {
                                        vb.setDebitAmount(Double.parseDouble(debitAmountObj.toString().trim()));
                                        vb.setDirection("1");
                                    } else {
                                        vb.setDebitAmount(new Double(0));
                                    }
                                    if (null != creditAmountObj && !"".equals(creditAmountObj)
                                            && !"0".equals(creditAmountObj)) {
                                        vb.setCreditAmount(Double.parseDouble(creditAmountObj.toString().trim()));
                                        vb.setDirection("2");
                                    } else {
                                        vb.setCreditAmount(new Double(0));
                                    }
                                    if (null == sl || "".equals(sl)) {
                                        sl = "0";
                                    }
                                    if (slObj != null && !"".equals(slObj)) {
                                        sl = slObj.toString().trim().replaceAll(",", "");
                                    }
                                    vb.setNumber(Double.parseDouble(sl));
                                    vb.setDes("序时簿导入");
                                    // vb.setDirection("1");
                                    if (zyObj != null && !"".equals(zyObj)) {
                                        vb.setVcabstact(zyObj.toString().trim());
                                    }
                                    if (null == dj || "".equals(dj)) {
                                        dj = "0";
                                    }
                                    if (djObj != null && !"".equals(djObj)) {
                                        dj = djObj.toString().trim().replaceAll(",", "");
                                    }
                                    vb.setPrice(Double.parseDouble(dj));
                                    vb.setVcsubject(subjectFullNameObj.toString().trim().replaceAll(" ", "")
                                            .replaceAll("-", "_"));
                                    /*
                                     * if (vb.getSubjectID().startsWith("1403")
                                     * || vb.getSubjectID().startsWith("1405"))
                                     * { if ((vb.getNumber() == null ||
                                     * vb.getNumber() == 0) && (vb.getPrice() ==
                                     * null || vb.getPrice() == 0)) {
                                     * vb.setNumber(new Double(1)); Double
                                     * amount = vb.getDebitAmount(); if (amount
                                     * == null || amount == 0) { amount =
                                     * vb.getCreditAmount(); }
                                     * vb.setPrice(amount); } else if
                                     * ((vb.getNumber() == null ||
                                     * vb.getNumber() == 0) && (vb.getPrice() !=
                                     * null && vb.getPrice() != 0)) { Double
                                     * amount = vb.getDebitAmount(); if (amount
                                     * == null || amount == 0) { amount =
                                     * vb.getCreditAmount(); }
                                     * vb.setNumber(amount / vb.getPrice()); }
                                     * else if ((vb.getNumber() != null &&
                                     * vb.getNumber() != 0) && (vb.getPrice() ==
                                     * null || vb.getPrice() == 0)) { Double
                                     * amount = vb.getDebitAmount(); if (amount
                                     * == null || amount == 0) { amount =
                                     * vb.getCreditAmount(); }
                                     * vb.setPrice(amount / vb.getNumber()); } }
                                     */
                                    if (vb.getSubjectID().startsWith("1403") || vb.getSubjectID().startsWith("1405")) {
                                        if ("1".equals(vb.getDirection()) && vb.getNumber() != null
                                                && vb.getNumber() != 0) {
                                            vb.setPrice(vb.getDebitAmount() / vb.getNumber());
                                        } else if ("2".equals(vb.getDirection()) && vb.getNumber() != null
                                                && vb.getNumber() != 0) {
                                            vb.setPrice(vb.getCreditAmount() / vb.getNumber());
                                        }
                                    }
                                    voucherBodyDao.insertVouchBody(vb);

                                } else {
                                    // 倒着追溯到上一个不为空的记录
                                    // 上一个不为空的对象
                                    Map<String, Object> tempMap = null;
                                    for (int j = i - 1; j > 0; j--) {
                                        tempMap = voucherList.get(j);
                                        {
                                            Object obj = tempMap.get(rqKey);
                                            if (obj == null || "".equals(obj)) {
                                                continue;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    periodObj = tempMap.get(rqKey);
                                    period = periodObj.toString().trim().substring(0, 7);
                                    if (!busDate.equals(period.replaceAll("/", "-"))) {
                                        continue;
                                    }
                                    vb.setVouchAID(UUIDUtils.getUUID());
                                    vb.setPeriod(period.replaceAll("/", "-"));
                                    vb.setAccountID(accountID);
                                    vb.setVouchID(voucherHead.getVouchID());
                                    // vb.setRowIndex("1");
                                    vb.setSubjectID(subjectIDObj.toString().trim());
                                    if (debitAmountObj != null && !"".equals(debitAmountObj)
                                            && !"0".equals(debitAmountObj)) {
                                        vb.setDebitAmount(Double.parseDouble(debitAmountObj.toString().trim()));
                                        vb.setDirection("1");
                                    } else {
                                        vb.setDebitAmount(new Double(0));
                                    }
                                    if (null != creditAmountObj && !"".equals(creditAmountObj)
                                            && !"0".equals(creditAmountObj)) {
                                        vb.setCreditAmount(Double.parseDouble(creditAmountObj.toString().trim()));
                                        vb.setDirection("2");
                                    } else {
                                        vb.setCreditAmount(new Double(0));
                                    }
                                    if (null == sl || "".equals(sl)) {
                                        sl = "0";
                                    }
                                    if (slObj != null && !"".equals(slObj)) {
                                        sl = slObj.toString().trim().replaceAll(",", "");
                                    }
                                    vb.setNumber(Double.parseDouble(sl));
                                    vb.setDes("序时簿导入");
                                    // vb.setDirection("1");
                                    if (zyObj != null && !"".equals(zyObj)) {
                                        vb.setVcabstact(zyObj.toString().trim());
                                    }
                                    if (null == dj || "".equals(dj)) {
                                        dj = "0";
                                    }
                                    if (djObj != null && !"".equals(djObj)) {
                                        dj = djObj.toString().trim().replaceAll(",", "");
                                    }
                                    vb.setPrice(Double.parseDouble(dj));
                                    vb.setVcsubject(subjectFullNameObj.toString().trim().replaceAll(" ", "").replaceAll("-", "_"));

                                    if (vb.getSubjectID().startsWith("1403") || vb.getSubjectID().startsWith("1405")) {
                                        if ("1".equals(vb.getDirection()) && vb.getNumber() != null
                                                && vb.getNumber() != 0) {
                                            vb.setPrice(vb.getDebitAmount() / vb.getNumber());
                                        } else if ("2".equals(vb.getDirection()) && vb.getNumber() != null
                                                && vb.getNumber() != 0) {
                                            vb.setPrice(vb.getCreditAmount() / vb.getNumber());
                                        }
                                    }
                                    voucherBodyDao.insertVouchBody(vb);
                                    // 开始写入库
                                }
                            }
                        } else {
                            break;
                        }
                    }
                    if (!in) {
                        result.put("success", "false");
                        result.put("message", "请仔细核查凭证叙时薄的日期,没有当前做账月份【" + busDate + "】的数据!");
                        return result;
                    }
                }

                // 读出来
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", accountID);
                pa.put("period", busDate);
                pa.put("source", 11);
                pa.put("vhid", vhid);
                pa.put("importVo", "importVo");
                List<VoucherHead> headList = voucherHeadDao.queryImportVoucher(pa);
                List<Voucher> vouchList = null;
                if (null != headList && headList.size() > 0 && headList.get(0).getVouchID() != null
                        && !"".equals(headList.get(0).getVouchID())) {
                    vouchList = new ArrayList<Voucher>();
                    for (int s = 0; s < headList.size(); s++) {
                        Voucher voucher = new Voucher();
                        VoucherHead vh = headList.get(s);
                        String vouchID = vh.getVouchID();
                        List<VoucherBody> bodyList = voucherBodyDao.queryVouBodyByHID(vouchID);
                        voucher.setVoucherHead(vh);
                        voucher.setVoucherBodyList(bodyList);
                        vouchList.add(voucher);
                    }
                }
                pa.put("busDate", busDate);


                if (vouchList != null) {
                    for (int k = 0; k < vouchList.size(); k++) {
                        Voucher vouch = vouchList.get(k);
                        boolean flg1 = vatService.checkVouch(pa, vouch);
                        if (flg1) {
                            tBasicSubjectMessageMapper.chgSubAmountByCreate(pa, vouch);
                        }
                    }
                }

                if (!vhid.isEmpty()) {
                    vatDao.upVouchBody(pa);
                    vatDao.upVouch(pa);
                    vhid.clear();
                }
                // 变更凭证头金额
                Map<String, Object> parm = new HashMap<String, Object>();
                parm.put("accountID", account.getAccountID());
                parm.put("period", busDate);
                List<Map<String, Object>> list = voucherHeadDao.queryImportVouch(parm);
                if (null != list && list.size() > 0) {
                    for (int s = 0; s < list.size(); s++) {
                        Map<String, Object> m = list.get(s);
                        String vouchID = m.get("vouchID").toString();
                        double sd = Double.parseDouble(m.get("sd").toString());
                        Map<String, Object> cs = new HashMap<String, Object>();
                        cs.put("vouchID", vouchID);
                        cs.put("sd", sd);
                        voucherHeadDao.updVouchAmount(cs);
                    }
                }
                // 检查是否凭证里面是否有包含成本结转的凭证,有就更新字段标识
                int num = upImportVoucherHead(vouchList);

                // 修改状态 （一键生成,结转,一键审核,凭证检查）
                // 一键生成凭证
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("isCreateVoucher", 1);
                param.put("accountID", accountID);
                param.put("busDate", busDate);
                periodStatuDao.updStatu1(param);
                // 一键计提
                // param.put("isJt", 1);
                // periodStatuDao.updstatusJt(param);
                // 一键结转
                // periodStatuDao.updStatu2(param);
                // 一键检查
                // param.put("isDetection", 1);
                // periodStatuDao.updStatuIsDetection(param);
                // 一键结账
                // param.put("isJz", 1);
                // periodStatuDao.updStatu3(param);
                // 一键审核
                // Map<String, Object> par = new HashMap<String, Object>();
                // par.put("accountID", accountID);
                // par.put("busDate", busDate);
                // par.put("isCheck", 1);
                // periodStatuDao.updStatu5(par);
                // 一键审核凭证
                // Map<String, Object> p = new HashMap<String, Object>();
                // p.put("accountID", account.getAccountID());
                // p.put("busDate", busDate);
                // p.put("auditStatus", 1);
                // oneKeyCheckVoucher(p);
                // 变更凭证检查状态
                // Map<String, Object> pm = new HashMap<String, Object>();
                // pm.put("accountID", accountID);
                // pm.put("busDate", busDate);
                // pm.put("isDetection", 1);
                // periodStatuDao.updStatuIsDetection(pm);
                result.put("success", "true");
                return result;

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
        return null;
    }

    @Transactional(rollbackFor = BusinessException.class)
    private int upImportVoucherHead(List<Voucher> vouchList) throws Exception {
        if (vouchList == null) {
            return 0;
        }
        for (int i = 0; i < vouchList.size(); i++) {
            Voucher voucher = vouchList.get(i);
            if (voucher == null) {
                continue;
            }
            VoucherHead voucherHead = voucher.getVoucherHead();
            List<VoucherBody> voucherBodyList = voucher.getVoucherBodyList();
            if (voucherHead == null || voucherBodyList == null) {
                continue;
            }
            String vouchID = voucherHead.getVouchID();
            if (StringUtil.isEmpty(vouchID)) {
                continue;
            }
            if (proofService.checkIsCbjz(voucherBodyList)) {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("vouchID", vouchID);
                hashMap.put("voucherType", 117);
                return vatDao.upVoTypeByImport(hashMap);
            }
        }
        return 0;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public Map<String, Object> uploadVoucherAttach(MultipartFile file, HttpServletRequest request)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (file != null && !file.isEmpty()) {
                User user = userService.getCurrentUser();
                Account account = userService.getCurrentAccount(user);
                vatService.subinit(user, account);
                String accountID = account.getAccountID();
                String busDate = account.getUseLastPeriod();
                String filePath = filePaths_voucher + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath, "attach-" + System.currentTimeMillis());
                String vouchID = request.getParameter("vouchID");
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("vouchID", vouchID);

                Attach attach = new Attach();
                String uuid = UUIDUtils.getUUID();
                attach.setId(uuid);
                attach.setAttachSuffix(fileName.split("\\.")[1]);
                attach.setAttachUrl(accountID + "/" + busDate + "/");
                attach.setImportDate(System.currentTimeMillis());
                attach.setSource(0);
                attach.setAttachName(fileName);
                attachDao.addAttach(attach);
                VoucherHead voucherHead = voucherHeadDao.queryVouHByID(vouchID);
                String attachIds = voucherHead.getAttachID();

                if (attachIds != null && !"".equals(attachIds)) {
                    String[] attachs = attachIds.split("\\#");
                    if (attachs.length >= 10) {
                        result.put("success", "false");
                        result.put("message", "上传失败,单张凭证最多只支持10张图片附件!");
                        return result;
                    }
                    param.put("attachID", attachIds + "#" + uuid);
                } else {
                    param.put("attachID", uuid);
                }
                voucherHeadDao.updAttachID(param);
                result.put("success", "true");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            throw new BusinessException();
        }
        return result;
    }

    @Override
    public List<Attach> queryAttach(String vouchID) throws BusinessException {

        List<Attach> list = new ArrayList<Attach>();
        try {
            VoucherHead vh = voucherHeadDao.queryVouHByID(vouchID);
            String attachIDStr = vh.getAttachID();
            if (attachIDStr == null || "".endsWith(attachIDStr)) {
                return null;
            }
            String[] attachIDs = attachIDStr.split("#");
            if (null != attachIDs && attachIDs.length > 0) {
                for (int i = 0; i < attachIDs.length; i++) {
                    List<Attach> attachList = attachDao.queryByID(attachIDs[i]);
                    if (null != attachList && !attachList.isEmpty()) {
                        Attach attach = attachList.get(0);
                        list.add(attach);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return list;
    }

    @Override
    public List<VoucherBody> queryVouBySubname(Map<String, Object> param) throws BusinessException {
        List<VoucherBody> list = new ArrayList<VoucherBody>();
        try {
            if (voucherBodyDao.queryVouBySubname(param) != null) {
                list = voucherBodyDao.queryVouBySubname(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void delAttach(Map<String, Object> param) throws BusinessException {
        List<VoucherHead> list = voucherHeadDao.queryVouchByAttachID(param);
        String attac = param.get("attachID").toString();
        if (list != null && list.size() > 0) {
            for (int s = 0; s < list.size(); s++) {
                VoucherHead vh = list.get(s);
                String attachID = vh.getAttachID();
                Map<String, Object> par = new HashMap<String, Object>();
                par.put("vouchID", vh.getVouchID());
                if (attachID.contains(attac + "#")) {

                    par.put("attachID", attachID.replaceAll(attac + "#", ""));
                } else {
                    par.put("attachID", attachID.replaceAll(attac, ""));
                }
                voucherHeadDao.updAttachID(par);

            }
        }
        attachDao.delByID(attac);
    }

    @Override
    public Voucher copyVoucher(Map<String, Object> param) throws BusinessException {
        String vouchIDOld = param.get("vouchID").toString();
        User user = (User) param.get("user");
        VoucherHead vh = voucherHeadDao.queryVouHByID(vouchIDOld);
        String sysPeriod = param.get("period").toString();
        String vouchPeriod = vh.getPeriod();
        if (!sysPeriod.equals(vouchPeriod)) {
            throw new BusinessException("复制失败,系统只允许复制粘贴当前会计期间的凭证!");
        }
        List<VoucherBody> vbList = voucherBodyDao.queryVouBodyByHID(vouchIDOld);
        Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param);
        String uuid = UUIDUtils.getUUID();
        vh.setVouchID(uuid);
        vh.setCreateDate(System.currentTimeMillis());
        vh.setCreatePsnID(user.getUserID());
        vh.setCreatepsn(user.getLoginUser());
        vh.setVoucherNO(maxVoucherNo);
        List<VoucherBody> list = new ArrayList<VoucherBody>();
        if (vbList != null && vbList.size() > 0) {
            voucherHeadDao.insertVouchHead(vh);
            for (int i = 0; i < vbList.size(); i++) {
                VoucherBody vb = vbList.get(i);

                vb.setVouchAID(UUIDUtils.getUUID());
                vb.setVouchID(uuid);
                voucherBodyDao.insertVouchBody(vbList.get(i));
                list.add(vb);
            }
        }
        Voucher voucher = new Voucher();
        voucher.setVoucherHead(vh);
        voucher.setVoucherBodyList(list);
        boolean bool = vatService.checkVouch(param, voucher);
        if (bool) {
            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
        }
        return voucher;
    }

    @Override
    public Map<String, Object> copyVoucher2(String vouchID, String accountID, String period) throws BusinessException {


        Map<String, Object> hashMap = new HashMap<>();

        List<VoucherBody> vbList = voucherBodyDao.queryVouBodyByHID(vouchID);
        if (vbList == null || vbList.isEmpty()) {
            return null;
        }

        List<VoucherBody> arr = new ArrayList<>();

        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);


        //生产新的凭证,作为结果集返回给前端
        for (int i = 0; i < vbList.size(); i++) {
            VoucherBody vb = vbList.get(i);

            VoucherBody voucherBody = new VoucherBody();

            voucherBody.setDirection(vb.getDirection()); //方向
            voucherBody.setCreditAmount(vb.getCreditAmount()); //贷方金额
            voucherBody.setDebitAmount(vb.getDebitAmount());//借方金额

            voucherBody.setSubjectID(vb.getSubjectID()); //科目编码
            voucherBody.setVcsubject(vb.getVcsubject());  //科目名称

            voucherBody.setRowIndex(vb.getRowIndex()); //分录号

            //如果复制的是商品 会有数量与金额
            voucherBody.setPrice(vb.getPrice());    //价格
            voucherBody.setNumber(vb.getNumber());  //数量

            //添加到返回结果集当中去
            arr.add(voucherBody);

        }

        hashMap.put("vbs", arr);

        return hashMap;

    }

    @SuppressWarnings({"unused", "unchecked"})
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> saveCopyVoucher(String accountID, String period, List<VoucherBody> arr, String voucherNo, String saveToDate) throws BusinessException {

        try {

            Map<String, Object> rtMap = new HashMap<>();
            //saveToDate   yyyy-mm-dd
            boolean flg = false;
            //saveToDate是真正复制的期间
            Date fomatDate = DateUtil.fomatDate(saveToDate);
            //转换成yyyy-mm字符串格式
            String period2 = DateUtil.getMoth2(fomatDate);
            //判断复制的凭证有没有跨月，跨月的话默认为falsem,，没有跨月为true
            if (period.equals(period2)) {
                flg = true;
            }

            //查询复制过去的账套期间是否已经做账
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountID);

            hashMap.put("period", period2);
            int count = vatDao.queryCountSubjectMessage(hashMap);
            if (count < 90) {
                rtMap.put("code", "5");
                rtMap.put("msg", "复制凭证操作异常,请检查期间科目是否存在");
                return rtMap;
            }

            hashMap.put("busDate", period2);
            List<StatusPeriod> stalist = periodStatusDao.queryStatus(hashMap);
            if (stalist != null && stalist.size() > 0) {
                Integer isCarryState = stalist.get(0).getIsCarryState(); //已结转不允许再复制凭证
                if (isCarryState != null && isCarryState == 1) {
                    rtMap.put("code", "3");
                    rtMap.put("msg", "已结转的期间不允许添加新的凭证");
                    return rtMap;
                }
            }

            String relAccPeriod = period2;

            //1a生成凭证头
            VoucherHead vh = new VoucherHead();

            vh.setVouchID(UUIDUtils.getUUID());
            vh.setVoucherNO(Integer.valueOf(voucherNo)); //凭证号
            vh.setTotalDbit(null); //凭证借方金额
            vh.setTotalCredit(null); //凭证借方金额
            vh.setIsproblem("2");//1有问题2没问题
            vh.setSource(5); //凭证来源 5手工凭证
            if (flg == false) {
                //跨月复制凭证
                vh.setDes("跨月凭证复制，从" + period + "复制到" + saveToDate);
            } else {
                vh.setDes("凭证复制，从" + period + "复制到" + period);
            }
            vh.setAccountID(accountID);
            vh.setPeriod(relAccPeriod); //复制的期间
            vh.setVcDate(fomatDate);//生成日期
            vh.setUpdatedate(new Date());
            vh.setUpdatePsnID(null);
            vh.setUserID(null);
            vh.setCreateDate(System.currentTimeMillis());
            vh.setVouchFlag(0);
            vh.setVoucherType(null);//凭证类型2 标记手工添加的结转成本

            vh.setAttachID(null); //附件ID

            //2a生成凭证体

            //定义借方总金额
            Double totalJF = 0.0;

            for (int i = 0; i < arr.size(); i++) {
                VoucherBody vb = arr.get(i);

                vb.setVouchID(vh.getVouchID());  //凭证头id 关联外键
                vb.setVouchAID(UUIDUtils.getUUID());  //主键id
                vb.setPeriod(relAccPeriod); //复制的真正期间 跨月的话期间与当前期间不一致
                vb.setAccountID(accountID);
                vb.setUpdatedate(new Date());
                vb.setDes("复制凭证");

                String direction = vb.getDirection();
                if (!StringUtil.isEmpty(direction) && direction.equals("1")) {
                    totalJF += vb.getDebitAmount();
                }
            }

            //对凭证头的借贷金额进项重新赋值
            vh.setTotalDbit(totalJF); //凭证借方金额
            vh.setTotalCredit(totalJF); //凭证借方金额

            //把凭证头插入数据库
            Integer num = voucherHeadDao.insertVouchHead(vh);

            //把凭证体添加到数据库
            vatService.insertVouchBatch(arr);

            Voucher voucher = new Voucher();
            voucher.setVoucherHead(vh);
            voucher.setVoucherBodyList(arr);

            Map<String, Object> param = new HashMap<>();

            param.put("period", relAccPeriod);
            param.put("busDate", relAccPeriod);
            param.put("accountID", accountID);

            param.put("rtSub", "saveVoucher");

            boolean bool = vatService.checkVouch(param, voucher);
            if (bool == true) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                List<PageSub> subPage = (List<PageSub>) param.get("saveVoucherToPage");
                if (subPage == null || subPage.isEmpty()) {
                    throw new BusinessException("param.get(saveVoucherToPage) ==null");
                }

                //取消返回data 前台自己重新加载缓存
			/*	if(flg==true){
					rtMap.put("data", subPage);
					rtMap.put("isCurrentPeriod", true);
				}else{
					//跨月复制不返回变化的数据
					rtMap.put("data", null);
					rtMap.put("isCurrentPeriod", false);
				}*/

            }
            rtMap.put("code", "0");
            rtMap.put("vouchID", vh.getVouchID());
            return rtMap;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }


    }

    @SuppressWarnings("unused")
    @Override
    public String queryJzPeriod(String accountID, String period) throws BusinessException {

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("accountID", accountID);
            param.put("isJz", 0);
            param.put("order", "1");
            List<StatusPeriod> list = periodStatusDao.queryStatusByCondition(param);
            if (list == null || list.get(0) == null) {
                return period;
            }
            String per = list.get(0).getPeriod();
            return per;
        } catch (Exception e) {
            throw new BusinessException(e);
        }

    }


}
