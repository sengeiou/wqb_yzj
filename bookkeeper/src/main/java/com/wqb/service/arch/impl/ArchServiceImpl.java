package com.wqb.service.arch.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.arch.ArchDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.arch.ArchService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

//薪资业务层
@Component
@Service("archService")
public class ArchServiceImpl implements ArchService {
    private List<Arch> linkedList = new ArrayList<>();
    private Map<String, Object> query = new HashMap<>();
    private String str;
    private static Properties properties;

    @Autowired
    ArchDao archDao;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    VatService vatService;
    @Autowired
    VatDao vatDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    AssetsService assetsService;

    static {
        InputStream inputStream = ArchServiceImpl.class.getResourceAsStream("/config/assets.properties");

        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    // 批量添加
    public Map<String, Object> insertBath(List<Map<String, Object>> list, Map<String, String> map)
            throws BusinessException {

        Map<String, Object> linkMap = new HashMap<>();

        try {
            // 时间
            Map<String, Object> row1 = list.get(0);
            String archDate = (String) row1.get("map1");
            if (StringUtil.isEmptyWithTrim(archDate)) {
                linkMap.put("fail", "第1行工资日期不能为空,请下载模板按照正确格式填写日期!");
                return linkMap;
            }

            // 工资日期
            if (DateUtil.fomatDateMM(archDate) == null) {
                linkMap.put("fail", "第1行工资日期格式不正确,请下载模板按照正确格式填写日期!!");
                return linkMap;
            }

            List<String> codeList = new ArrayList<>();
            List<String> listTem = new ArrayList<>();
            for (int i = 2; i < list.size(); i++) {

                String code = (String) list.get(i).get("map1");
                if (StringUtil.isEmpty(code)) {
                    continue;
                }
                code = code.replace(" ", "");
                if (code.contains("合计")) {
                    continue;
                }
                listTem.add(code);
                boolean contains = codeList.contains(code);
                if (!contains) {
                    codeList.add(code);
                } else {
                    String s = "员工编码为" + code + "与其它员工冲突,编码不能重复,请按要求导入!";
                    linkMap.put("fail", s);
                    return linkMap;
                }
                // 判断部门是否正确
                String department = (String) list.get(i).get("map0");
                if (StringUtil.isEmpty(department)) {
                    continue;
                }
                if (!getValue(department)) {
                    linkMap.put("fail", "第" + (i + 2) + "行,部门名称必须是管理部[门]，生产部[门]，销售部[门]，请下载模板导入!");
                    return linkMap;
                }

            }

            // 一个期间可以可以导入多个月份数据
            /*
             * Map<String, Object> qrMap = new HashMap<>();
             * qrMap.put("accountID", map.get("accountID"));// 账套id
             * qrMap.put("acperiod", map.get("account_period")); // 做账期间
             * qrMap.put("archDate", archDate); // 工资发放日期
             *
             * List<String> listCode = archDao.querycode(qrMap);
             * if(listCode!=null&&listCode.size()>0){ for (int j = 0; j <
             * listTem.size(); j++) { String code = listTem.get(j);
             *
             * boolean contains = listCode.contains(code); if(contains){ String
             * msg = code+"员工本期已经导入过请勿再次导入,请仔细检查!"; linkMap.put("fail", msg);
             * return linkMap; } } }
             */

            int count = 0;
            String acperiod = map.get("account_period");
            for (int i = 2; i < list.size(); i++) {
                Arch arch = new Arch();
                // 获取一行固定资产数据
                Map<String, Object> archMap = list.get(i);
                // 如果是倒数第二行行并且第二个单元格包含'合计'不添加
                String acCode = (String) archMap.get("map1");

                if (StringUtil.isEmpty(acCode)) {
                    str = "第" + (i + 1) + "行员工编码不能为空,请仔细核查!";
                    linkMap.put("fail", str);
                    return linkMap;
                }

                if (acCode.contains("合计")) {
                    break;
                }

                HashMap<String, Object> map3 = new HashMap<>();
                map3.put("acCode", acCode); // 员工编码
                map3.put("acperiod", acperiod); // 做账期间
                map3.put("archDate", archDate); // 工资发放日期
                map3.put("accountID", map.get("accountID"));// 账套id

                List<Arch> listArch = archDao.queryByCodeAndAcperiod(map3);

                if (listArch != null && listArch.size() > 0) {
                    str = "第" + (i + 1) + "行数据,员工编码为" + acCode + "已经导入,请仔细核查!";
                    linkMap.put("fail", str);
                    return linkMap;
                }

                arch.setArchID(UUIDUtils.getUUID()); // 主键
                arch.setAccountID(map.get("accountID")); // 设置账套ID
                arch.setAcperiod(acperiod); // 做账期间
                arch.setArchDate(archDate); // 工资发放月份
                arch.setAcDepartment((String) archMap.get("map0")); // 部门
                arch.setAcCode(acCode); // 人员编码
                arch.setAcName((String) archMap.get("map2")); // 姓名

                // 考勤天数
                str = (String) archMap.get("map3");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setAttendanceDays(0.0);
                } else {
                    arch.setAttendanceDays(Double.valueOf(str.replaceAll(",", "")));
                }

                // 实际出勤
                str = (String) archMap.get("map4");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setAttendanceActual(0.0);
                } else {
                    arch.setAttendanceActual(Double.valueOf(str.replaceAll(",", "")));
                }

                // 基本工资
                str = (String) archMap.get("map5");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setBasePay(0.0);
                } else {
                    arch.setBasePay(Double.valueOf(str.replaceAll(",", "")));
                }

                // 岗位津贴
                str = (String) archMap.get("map6");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setSubsidy(0.0);
                } else {
                    arch.setSubsidy(Double.valueOf(str.replaceAll(",", "")));
                }

                // 平时加班
                str = (String) archMap.get("map7");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setOvertimeFree(0.0);
                } else {
                    arch.setOvertimeFree(Double.valueOf(str.replaceAll(",", "")));
                }

                // 周末加班
                str = (String) archMap.get("map8");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setOvertimeWeekend(0.0);
                } else {
                    arch.setOvertimeWeekend(Double.valueOf(str.replaceAll(",", "")));
                }

                // 其他补贴
                str = (String) archMap.get("map9");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setOtherFree(0.0);
                } else {
                    arch.setOtherFree(Double.valueOf(str.replaceAll(",", "")));
                }

                // 应发工资
                str = (String) archMap.get("map10");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setPayAble(0.0);
                } else {
                    arch.setPayAble(Double.valueOf(str.replaceAll(",", "")));
                }

                // 扣社保
                str = (String) archMap.get("map11");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setSocialfree(0.0);
                } else {
                    arch.setSocialfree(Double.valueOf(str.replaceAll(",", "")));
                }

                // 扣公积金
                str = (String) archMap.get("map12");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setProvident(0.0);
                } else {
                    arch.setProvident(Double.valueOf(str.replaceAll(",", "")));
                }

                // 扣水电费
                str = (String) archMap.get("map13");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setUtilities(0.0);
                    ;
                } else {
                    arch.setUtilities(Double.valueOf(str.replaceAll(",", "")));
                }

                // 其它扣款
                str = (String) archMap.get("map14");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setDeduction(0.0);
                } else {
                    arch.setDeduction(Double.valueOf(str.replaceAll(",", "")));
                }

                // 扣款合计
                str = (String) archMap.get("map16");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setTotalCharged(0.0);
                } else {
                    arch.setTotalCharged(Double.valueOf(str.replaceAll(",", "")));
                }

                // 个税
                str = (String) archMap.get("map15");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setTaxFree(0.0);
                } else {
                    arch.setTaxFree(Double.valueOf(str.replaceAll(",", "")));
                }

                // 实发工资
                str = (String) archMap.get("map17");
                if (StringUtil.isEmptyWithTrim(str)) {
                    arch.setRealwages(0.0);
                } else {
                    arch.setRealwages(Double.valueOf(str.replaceAll(",", "")));
                }
                // 导入时间
                arch.setImportDate(new Date());
                // 操作人
                arch.setCreatepsnID(map.get("userID"));
                // 添加到数据看来
                archDao.insertArch(arch);
                count++;
            }
            linkMap.put("success", "导入完毕，共导入" + count + "条数据。");
            return linkMap;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    // 添加
    // @Transactional
    public Map<String, Object> insertArch(List<Map<String, Object>> list, Map<String, String> map)
            throws BusinessException {
        Map<String, Object> mp = new HashMap<>();

        return mp;

    }

    // 列表页 综合查询 根据名字和区间 每页十条
    @Override
    public Page<Arch> queryListPage(Map<String, Object> map) throws BusinessException {

        try {
            // 通过（当前页、每页显示条数、总条数） 初始化分页信息
            Page<Arch> page = new Page<>();
            // 根据查询条件算出所需数据的总条数
            int cout = archDao.queryCount(map);
            int pageSize = page.getPageSize(); // 每页显示条数
            pageSize = 10;
            // 计算总页数
            int pageTotal = cout % pageSize > 0 ? cout / pageSize + 1 : cout / pageSize;
            // 当前页
            Integer currentPage = (Integer) map.get("currentPage");
            page.setCurrentPage(currentPage);
            // 总页数
            page.setPageTotal(pageTotal);
            // 总条数
            page.setRecordTotal(cout);
            // 起始页
            Integer start = (currentPage - 1) * pageSize;
            map.put("start", start); // 起始页
            map.put("size", pageSize);
            // 根据查询条件查询固定资产数据
            List<Arch> queryListPage = archDao.queryListPage(map);

            // 分页内容
            page.setContent(queryListPage);
            return page;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // 删除
    // @Transactional
    @Override
    public void delById(Map<String, String> map) throws BusinessException {
        archDao.delById(map);
    }

    // 批量删除
    @Override
    // @Transactional
    public void delBathById(Map<String, Object> map) throws BusinessException {
        archDao.delBathById(map);

    }

    /**
     * 薪资计提
     */
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void arch2vouch1(HttpSession session) throws BusinessException {
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            // 企业性质 1：生产型2：贸易型3：服务型
            Integer accountType = account.getCompanyType();
            String busDate = (String) sessionMap.get("busDate");
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            List<Map<String, Object>> departList = archDao.queryDepart(param);
            //// select sum(utilities) as sd, sum(deduction) as
            //// qtkk,sum(taxFree) as grsds ,sum(provident) as gjj,
            //// sum(socialfree) as sb,
            // acDepartment,sum(realwages) as sf,sum(payAble) as yf from
            //// t_wa_arch
            // where accountID = #{accountID} and acperiod like
            //// CONCAT('%','${busDate}','%' ) group by acDepartment;

            Voucher voucher = new Voucher();
            VoucherHead vouchHead = null;
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            List<VoucherBody> vouBodyList = new ArrayList<VoucherBody>();
            // List<Arch> list = archDao.query2vouch(param);
            if (null != departList && departList.size() > 0) {
                String vouchID = UUIDUtils.getUUID();
                double totalYfAmount = 0.0; // 实付金额
                double totalSfAmount = 0.0; // 应付金额
                double totalQtkk = 0.0; // 其它扣款
                double totalGrsds = 0.0; // 个人所得税
                double totalGjj = 0.0; // 公积金
                double totalSb = 0.0; // 社保
                double totalSd = 0.0; // 水电
                for (Map<String, Object> departMap1 : departList) {
                    // String acDepartment =
                    // departMap1.get("acDepartment").toString();
                    double yf = (double) departMap1.get("yf");
                    double sf = (double) departMap1.get("sf");
                    double qtkk = (double) departMap1.get("qtkk");
                    // 个人所得税
                    double grsds = (double) departMap1.get("grsds");
                    // 公积金
                    double gjj = (double) departMap1.get("gjj");
                    // 社保
                    double sb = (double) departMap1.get("sb");
                    // 水电费
                    double sd = (double) departMap1.get("sd");
                    totalYfAmount = totalYfAmount + yf;
                    totalSfAmount = totalSfAmount + sf;
                    totalQtkk = totalQtkk + qtkk;
                    totalGrsds = totalGrsds + grsds;
                    totalGjj = totalGjj + gjj;
                    totalSb = totalSb + sb;
                    totalSd = totalSd + sd;
                }
                if (totalYfAmount != 0.0) {
                    Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                    // 构造凭证头
                    vouchHead = new VoucherHead();
                    vouchHead.setVouchID(vouchID);
                    vouchHead.setPeriod(busDate);
                    vouchHead.setVcDate(new Date());
                    vouchHead.setAccountID(account.getAccountID());
                    vouchHead.setSource(3);
                    vouchHead.setVoucherNO(maxVoucherNo);
                    vouchHead.setCreateDate(System.currentTimeMillis());
                    vouchHead.setCreatepsn(user.getUserName());
                    vouchHead.setCreatePsnID(user.getUserID());
                    vouchHead.setTotalCredit(totalYfAmount);
                    vouchHead.setTotalDbit(totalYfAmount);
                    // 插入凭证主表
                    voucherHeadDao.insertVouchHead(vouchHead);

                    voucher.setVoucherHead(vouchHead);
                }
                int fl = 1;
                for (int i = 0; i < departList.size(); i++) {
                    Map<String, Object> departMap = departList.get(i);
                    String acDepartment = (String) departMap.get("acDepartment");
                    // 应发工资
                    double yf = (double) departMap.get("yf");
                    // 实发工资
                    double sf = (double) departMap.get("sf");
                    // 其他扣款
                    double qtkk = (double) departMap.get("qtkk");
                    // 个人所得税
                    double grsds = (double) departMap.get("grsds");
                    // 公积金
                    double gjj = (double) departMap.get("gjj");
                    // 社保
                    double sb = (double) departMap.get("sb");

                    // 计提到生产成本
                    if (yf != 0 && acDepartment.contains("生产部")) {
                        // 构造凭证分录
                        VoucherBody vouchBody = new VoucherBody();
                        vouchBody.setVouchID(vouchID);
                        vouchBody.setPeriod(busDate);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        vouchBody.setAccountID(account.getAccountID());
                        // 科目名称
                        String subName = "生产部人员工资";
                        // vouchBody.setVcsubject("生产成本_行政后勤人员工资");
                        vouchBody.setDebitAmount(sf);
                        vouchBody.setDirection("1");
                        SubjectMessage subjectMessage = vatService.querySub(subName, "5001", "7");
                        String subject = null;
                        SubjectMessage sm = null;
                        if (null != subjectMessage) {
                            subject = subjectMessage.getSub_code();
                        } else {
                            // SubjectMessage subtMessage =
                            // vatService.querySub(null, "6602", "7");
                            subject = vatService.getNumber("5001", "7", "5001000");
                            sm = vatService.createSub(subject, "5001", subName, "生产成本_生产部人员工资");
                            // vatDao.insertSubject(sm);
                        }
                        vouchBody.setVcsubject("生产成本_生产部人员工资");
                        vouchBody.setSubjectID(subject);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        vouchBody.setVcabstact("计提本月工资");
                        vouchBody.setRowIndex(fl + "");
                        fl++;
                        // 插入凭证子表
                        voucherBodyDao.insertVouchBody(vouchBody);
                        vouBodyList.add(vouchBody);
                    }

                    // 计提到管理费用
                    if (yf != 0 && acDepartment.contains("管理")) {
                        // 构造凭证分录
                        VoucherBody vouchBody = new VoucherBody();
                        vouchBody.setVouchID(vouchID);
                        vouchBody.setPeriod(busDate);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        vouchBody.setAccountID(account.getAccountID());
                        // 科目名称
                        String subName = "行政人员工资";
                        vouchBody.setVcsubject("管理费用_行政人员工资");
                        vouchBody.setDebitAmount(sf);
                        vouchBody.setDirection("1");
                        SubjectMessage subjectMessage = vatService.querySub(subName, "6602", "7");
                        String subject = null;
                        SubjectMessage sm = null;
                        if (null != subjectMessage) {
                            subject = subjectMessage.getSub_code();
                        } else {
                            // SubjectMessage subtMessage =
                            // vatService.querySub(null, "6602", "7");
                            subject = vatService.getNumber("6602", "7", "6602000");
                            sm = vatService.createSub(subject, "6602", subName, "管理费用_行政人员工资");
                            // vatDao.insertSubject(sm);
                        }
                        vouchBody.setVcsubject("管理费用_行政人员工资");
                        vouchBody.setSubjectID(subject);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        if (fl == 1) {
                            vouchBody.setVcabstact("计提本月工资");
                        }
                        vouchBody.setRowIndex(fl + "");
                        fl++;
                        // 插入凭证子表
                        voucherBodyDao.insertVouchBody(vouchBody);
                        vouBodyList.add(vouchBody);
                    }

                    // 计提到销售费用
                    if (yf != 0 && acDepartment.contains("销售")) {
                        // 构造凭证分录
                        VoucherBody vouchBody = new VoucherBody();
                        vouchBody.setVouchID(vouchID);
                        vouchBody.setPeriod(busDate);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        vouchBody.setAccountID(account.getAccountID());
                        // 科目名称
                        String subName = "销售部人员工资";
                        vouchBody.setDebitAmount(sf);
                        vouchBody.setDirection("1");
                        SubjectMessage subjectMessage = vatService.querySub(subName, "6601", "7");
                        String subject = null;
                        SubjectMessage sm = null;
                        if (null != subjectMessage) {
                            subject = subjectMessage.getSub_code();
                        } else {
                            subject = vatService.getNumber("6601", "7", "6601000");
                            sm = vatService.createSub(subject, "6601", subName, "销售费用_销售部人员工资");
                        }
                        vouchBody.setVcsubject("销售费用_销售部人员工资");
                        vouchBody.setSubjectID(subject);
                        vouchBody.setVouchAID(UUIDUtils.getUUID());
                        if (fl == 1) {
                            vouchBody.setVcabstact("计提本月工资");
                        }
                        vouchBody.setRowIndex(fl + "");
                        fl++;
                        // 插入凭证子表
                        voucherBodyDao.insertVouchBody(vouchBody);
                        vouBodyList.add(vouchBody);
                    }

                }
                // 代扣社保
                if (totalSb != 0.0) {
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "代扣社保";
                    vouchBody.setDebitAmount(totalSb);
                    vouchBody.setDirection("1");
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1221", "7");
                    String subject = null;
                    SubjectMessage sm = null;
                    if (null != subjectMessage) {
                        subject = subjectMessage.getSub_code();
                    } else {
                        subject = vatService.getNumber("1221", "7", "1221000");
                        sm = vatService.createSub(subject, "1221", subName, "其它应收款_代扣社保");
                    }
                    vouchBody.setVcsubject("其它应收款_代扣社保");
                    vouchBody.setSubjectID(subject);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    // vouchBody.setVcabstact("计提薪资" + busDate);
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    voucherBodyDao.insertVouchBody(vouchBody);
                    vouBodyList.add(vouchBody);
                }
                // 代扣公积金
                if (totalGjj != 0.0) {
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "代扣公积金";
                    vouchBody.setDebitAmount(totalGjj);
                    vouchBody.setDirection("1");
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1221", "7");
                    String subject = null;
                    SubjectMessage sm = null;
                    if (null != subjectMessage) {
                        subject = subjectMessage.getSub_code();
                    } else {
                        subject = vatService.getNumber("1221", "7", "1221000");
                        sm = vatService.createSub(subject, "1221", subName, "其它应收款_代扣公积金");
                    }
                    vouchBody.setVcsubject("其它应收款_代扣公积金");
                    vouchBody.setSubjectID(subject);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    // vouchBody.setVcabstact("计提薪资" + busDate);
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    voucherBodyDao.insertVouchBody(vouchBody);
                    vouBodyList.add(vouchBody);
                }
                // 个人所得税
                if (totalGrsds != 0.0) {
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "个人所得税";
                    vouchBody.setDebitAmount(totalGrsds);
                    vouchBody.setDirection("1");
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1221", "7");
                    String subject = null;
                    if (null != subjectMessage) {
                        subject = subjectMessage.getSub_code();
                    } else {
                        subject = vatService.getNumber("1221", "7", "1221000");
                        SubjectMessage sm = vatService.createSub(subject, "1221", subName, "其它应收款_个人所得税");
                    }
                    vouchBody.setVcsubject("其它应收款_个人所得税");
                    vouchBody.setSubjectID(subject);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    // vouchBody.setVcabstact("计提薪资" + busDate);
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    voucherBodyDao.insertVouchBody(vouchBody);
                    vouBodyList.add(vouchBody);
                }
                // 水电
                if (totalSd != 0.0) {
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "水电费";
                    vouchBody.setDebitAmount(totalSd);
                    vouchBody.setDirection("1");
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1221", "7");
                    String subject = null;
                    if (null != subjectMessage) {
                        subject = subjectMessage.getSub_code();
                    } else {
                        subject = vatService.getNumber("1221", "7", "1221000");
                        SubjectMessage sm = vatService.createSub(subject, "1221", subName, "其它应收款_水电");
                    }
                    vouchBody.setVcsubject("其它应收款_水电");
                    vouchBody.setSubjectID(subject);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    // vouchBody.setVcabstact("计提薪资" + busDate);
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    voucherBodyDao.insertVouchBody(vouchBody);
                    vouBodyList.add(vouchBody);
                }

                // 罚金/其它扣款
                if (totalQtkk != 0.0) {
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "罚金/其它扣款";
                    vouchBody.setDebitAmount(totalQtkk);
                    vouchBody.setDirection("1");
                    SubjectMessage subjectMessage = vatService.querySub(subName, "1221", "7");
                    String subject = null;
                    if (null != subjectMessage) {
                        subject = subjectMessage.getSub_code();
                    } else {
                        subject = vatService.getNumber("1221", "7", "1221000");
                        vatService.createSub(subject, "1221", subName, "其它应收款_罚金/其它扣款");
                    }
                    vouchBody.setVcsubject("其它应收款_罚金/其它扣款");
                    vouchBody.setSubjectID(subject);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    // vouchBody.setVcabstact("计提薪资" + busDate);
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    voucherBodyDao.insertVouchBody(vouchBody);
                    vouBodyList.add(vouchBody);
                }
                // 构造凭证分录
                VoucherBody vouchBody1 = new VoucherBody();
                vouchBody1.setVouchID(vouchID);
                vouchBody1.setPeriod(busDate);
                vouchBody1.setVouchAID(UUIDUtils.getUUID());
                vouchBody1.setAccountID(account.getAccountID());
                String subName = "工资";
                SubjectMessage subjectMessage = vatService.querySub(subName, "2211", "7");
                String subject = null;
                if (null != subjectMessage) {
                    subject = subjectMessage.getSub_code();
                } else {
                    subject = "2211";
                }
                // 科目名称
                if ("2211".equals(subject)) {
                    vouchBody1.setVcsubject("应付职工薪酬");
                } else {
                    vouchBody1.setVcsubject("应付职工薪酬_工资");
                }
                vouchBody1.setCreditAmount(totalYfAmount); // 计提职工薪酬等于应发工资
                vouchBody1.setDirection("2");
                vouchBody1.setSubjectID(subject);
                vouchBody1.setVouchAID(UUIDUtils.getUUID());
                // vouchBody1.setVcabstact("计提薪资" + busDate);
                vouchBody1.setRowIndex(fl + "");
                // 插入凭证字表
                vouBodyList.add(vouchBody1);
                voucher.setVoucherBodyList(vouBodyList);
                double totalDebit = 0.0;
                double totalCredit = 0.0;
                if (vouBodyList != null && vouBodyList.size() > 0) {
                    for (int s = 0; s < vouBodyList.size(); s++) {
                        VoucherBody vb = vouBodyList.get(s);
                        String direction = vb.getDirection();
                        if ("1".equals(direction)) {
                            double debitAmount = vb.getDebitAmount();
                            totalDebit = totalDebit + debitAmount;
                        } else if ("2".equals(direction)) {
                            double creditAmount = vb.getCreditAmount();
                            totalCredit = totalCredit + creditAmount;
                        }
                    }
                }
                if (Math.abs(totalDebit - totalCredit) > 0.001) {
                    vouchBody1.setDes("薪资计提借贷不平衡,请仔细检查原始EXCEL数据");
                    vouchBody1.setIsproblem("1");
                }
                Map<String, Object> param1 = new HashMap<String, Object>();
                param1.put("accountID", account.getAccountID());
                param1.put("busDate", busDate);
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                }
                voucherBodyDao.insertVouchBody(vouchBody1);
            }
        } catch (BusinessException e) {
            System.out.println(e);
            throw new BusinessException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    /*
     * private void inits(String accoutID, String busDate, String userID) {
     * VatServiceImpl vatService = (VatServiceImpl) this.vatService;
     * vatService.setAccount_id(accoutID); vatService.setPeriod(busDate);
     * vatService.setUser_id(userID); }
     */
    @Override
    public List<Map<String, Object>> queryDepart(HttpSession session) throws BusinessException {
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        Account account = (Account) sessionMap.get("account");
        String busDate = (String) sessionMap.get("busDate");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountID", account.getAccountID());
        // param.put("busDate", DateUtil.getLastMonth(busDate));
        param.put("busDate", busDate);
        return archDao.queryDepart(param);
    }

    // 发放工资
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public Voucher arch2vouch2(HttpSession session) throws BusinessException {
        try {
            Voucher voucher = new Voucher();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            // 企业性质 1：生产型2：贸易型3：服务型
            Integer accountType = account.getCompanyType();
            String busDate = (String) sessionMap.get("busDate");
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("period", busDate);
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            // param.put("busDate", DateUtil.getLastMonth(busDate));
            // List<VoucherBody> bodyList =
            // voucherBodyDao.queryArchVouch(param);

            VoucherHead head = null;
            boolean canAdd = false;
            /*
             * if (null != bodyList && bodyList.size() > 0) { for (int i = 0; i
             * < bodyList.size(); i++) { VoucherBody voucherBody =
             * bodyList.get(i); head =
             * voucherHeadDao.queryVouHByID(voucherBody.getVouchID());
             * if(head!=null){ String period = head.getPeriod(); if
             * (period.equals(DateUtil.getLastMonth(busDate))) { canAdd =
             * true;// 上月计提过 break; } }
             *
             * } }
             */
            if (!canAdd) {
                Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                // 未计提过薪资 需从科目中抓取
                // 根据科目名称模糊查询科目
                // String lastDate = DateUtil.getLastMonth(busDate);
                param.put("lastDate", busDate);
                String subjectID1 = "2211";
                String subName1 = "工资";

                param.put("subjectID", subjectID1);
                param.put("subName", subName1);
                List<TBasicSubjectMessage> list1 = tBasicSubjectMessageMapper.selectLastArch(param);
                if (!(list1 != null && list1.size() > 0 && list1.get(0).getPkSubId() != null)) {
                    subName1 = "应付职工薪酬";
                    param.put("subjectID", subjectID1);
                    param.put("subName", subName1);
                    list1 = tBasicSubjectMessageMapper.selectLastArch(param);
                }

                // 应付职工薪酬金额
                double amount1 = 0;
                if (null != list1 && list1.size() > 0) {
                    BigDecimal ending_balance_debit = list1.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit) {
                        amount1 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list1.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit) {
                        amount1 = ending_balance_credit.doubleValue();
                    }
                }
                if (amount1 == 0) {
                    return null;
                }
                // 贷：其它应收款---代扣社保

                String subjectID2 = "1221";
                String subName2 = "社保";

                param.put("subjectID", subjectID2);
                param.put("subName", subName2);
                List<TBasicSubjectMessage> list2 = tBasicSubjectMessageMapper.selectLastArch(param);
                double amount2 = 0;
                if (null != list2 && list2.size() > 0) {
                    BigDecimal ending_balance_debit = list2.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit) {
                        amount2 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list2.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit) {
                        amount2 = ending_balance_credit.doubleValue();
                    }
                }

                // 其它应收款---代扣公积金
                String subjectID3 = "1221";
                String subName3 = "公积金";

                param.put("subjectID", subjectID3);
                param.put("subName", subName3);
                List<TBasicSubjectMessage> list3 = tBasicSubjectMessageMapper.selectLastArch(param);
                double amount3 = 0;
                if (null != list3 && list3.size() > 0) {
                    BigDecimal ending_balance_debit = list3.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit) {
                        amount3 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list3.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit) {
                        amount3 = ending_balance_credit.doubleValue();
                    }
                }
                // 应交税费----个人所得税
                String subjectID4 = "2221";
                String subName4 = "个人所得税";

                param.put("subjectID", subjectID4);
                param.put("subName", subName4);
                List<TBasicSubjectMessage> list4 = tBasicSubjectMessageMapper.selectLastArch(param);
                double amount4 = 0;
                if (null != list4 && list4.size() > 0) {
                    BigDecimal ending_balance_debit = list4.get(0).getEndingBalanceDebit();
                    if (null != ending_balance_debit) {
                        amount4 = ending_balance_debit.doubleValue();
                    }
                    BigDecimal ending_balance_credit = list4.get(0).getEndingBalanceCredit();
                    if (null != ending_balance_credit) {
                        amount4 = ending_balance_credit.doubleValue();
                    }
                }
                // 创建凭证
                VoucherHead vouchHead = new VoucherHead();
                String uuid = UUIDUtils.getUUID();
                vouchHead.setVouchID(uuid);
                vouchHead.setPeriod(busDate);
                vouchHead.setVcDate(new Date());
                vouchHead.setAccountID(account.getAccountID());
                vouchHead.setVoucherNO(0);
                vouchHead.setCreateDate(System.currentTimeMillis());
                vouchHead.setCreatepsn(user.getUserName());
                vouchHead.setCreatePsnID(user.getUserID());
                vouchHead.setSource(3);
                vouchHead.setVoucherNO(maxVoucherNo);
                List<VoucherBody> list = new ArrayList<VoucherBody>();
                VoucherBody body1 = null;
                int row = 0;
                if (amount1 != 0) {
                    body1 = new VoucherBody();
                    body1.setVcabstact("发放上月薪资");
                    body1.setVouchAID(UUIDUtils.getUUID());
                    body1.setVouchID(uuid);
                    body1.setPeriod(busDate);
                    body1.setAccountID(account.getAccountID());

                    body1.setVcsubject(list1.get(0).getFullName());
                    body1.setSubjectID(list1.get(0).getSubCode());
                    body1.setDirection("1");
                    body1.setDebitAmount(amount1);
                    row++;
                    body1.setRowIndex(row + "");
                }
                VoucherBody body2 = null;
                if (amount2 != 0) {
                    body2 = new VoucherBody();
                    body2.setVouchAID(UUIDUtils.getUUID());
                    body2.setVouchID(uuid);
                    body2.setPeriod(busDate);
                    body2.setAccountID(account.getAccountID());
                    body2.setVcsubject("其它应收款_代扣社保");
                    body2.setSubjectID(list2.get(0).getSubCode());
                    body2.setDirection("2");
                    body2.setCreditAmount(amount2);
                    row++;
                    body2.setRowIndex(row + "");
                }
                VoucherBody body3 = null;
                if (amount3 != 0) {
                    body3 = new VoucherBody();
                    body3.setVouchAID(UUIDUtils.getUUID());
                    body3.setVouchID(uuid);
                    body3.setPeriod(busDate);
                    body3.setAccountID(account.getAccountID());
                    body3.setVcsubject("其它应收款_代扣公积金");
                    body3.setSubjectID(list3.get(0).getSubCode());
                    body3.setDirection("2");
                    body3.setCreditAmount(amount3);
                    row++;
                    body3.setRowIndex(row + "");
                }
                VoucherBody body4 = null;
                if (amount4 != 0) {
                    body4 = new VoucherBody();
                    body4.setVouchAID(UUIDUtils.getUUID());
                    body4.setVouchID(uuid);
                    body4.setPeriod(busDate);
                    body4.setAccountID(account.getAccountID());
                    body4.setVcsubject("应交税费_个人所得税");
                    body4.setSubjectID(list4.get(0).getSubCode());
                    body4.setDirection("2");
                    body4.setCreditAmount(amount4);
                    row++;
                    body4.setRowIndex(row + "");
                }
                VoucherBody body5 = null;
                if (row != 0) {
                    body5 = new VoucherBody();
                    body5.setVouchAID(UUIDUtils.getUUID());
                    body5.setVouchID(uuid);
                    body5.setPeriod(busDate);
                    body5.setAccountID(account.getAccountID());
                    body5.setVcsubject("库存现金");
                    body5.setSubjectID("1001");
                    body5.setDirection("2");
                    body5.setCreditAmount(amount1 - amount2 - amount3 - amount4);
                    row++;
                    body5.setRowIndex(row + "");
                }
                if (body1 != null) {
                    vouchHead.setTotalCredit(amount1);
                    vouchHead.setTotalDbit(amount1);
                    voucherHeadDao.insertVouchHead(vouchHead);
                    voucher.setVoucherHead(vouchHead);
                    voucherBodyDao.insertVouchBody(body1);
                    list.add(body1);
                }
                if (body2 != null) {
                    voucherBodyDao.insertVouchBody(body2);
                    list.add(body2);
                }
                if (body3 != null) {
                    voucherBodyDao.insertVouchBody(body3);
                    list.add(body3);
                }
                if (body4 != null) {
                    voucherBodyDao.insertVouchBody(body4);
                    list.add(body4);
                }
                if (body5 != null) {
                    voucherBodyDao.insertVouchBody(body5);
                    list.add(body5);
                }
                voucher.setVoucherBodyList(list);
            }
            boolean bool = vatService.checkVouch(param, voucher);
            if (bool) {
                tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            }
            return voucher;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    // 查询做账期间所有工资月份
    @Override
    public String queryArchDate(Map<String, Object> param) throws BusinessException {
        return archDao.queryArchDate(param);
    }

    public boolean getValue(String key) {

        boolean flg = false;
        try {
            Set<Object> keySet = properties.keySet();
            for (Object object : keySet) {
                String asname = object.toString();
                if (asname.equals(key)) {
                    flg = true;
                    break;
                } else {
                    asname = new String(asname.getBytes("ISO-8859-1"), "UTF-8");
                    if (asname.equals(key)) {
                        flg = true;
                        break;
                    }
                }
            }
            return flg;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 薪资计提
     */
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void arch2vouch3(User user, Account account) throws BusinessException {
        try {
            // 企业性质 1：生产型2：贸易型3：服务型
            String busDate = account.getUseLastPeriod();
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            List<Map<String, Object>> departList = archDao.queryDepart(param);
            Voucher voucher = new Voucher();
            VoucherHead vouchHead = null;
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            List<VoucherBody> vBodyList = new ArrayList<VoucherBody>();
            String vouchID = UUIDUtils.getUUID();
            int fl = 1;
            double totalYf = 0;
            for (int i = 0; i < departList.size(); i++) {
                Map<String, Object> departMap = departList.get(i);
                String acDepartment = (String) departMap.get("acDepartment");
                // 应发工资
                double yf = (double) departMap.get("yf");
                // 计提到生产成本
                if (yf != 0 && acDepartment.contains("生产")) {
                    totalYf = totalYf + yf;
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "工资";
                    vouchBody.setDebitAmount(yf);
                    vouchBody.setDirection("1");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("subCode", "5001");
                    para.put("subName", subName);
                    para.put("accountID", account.getAccountID());
                    para.put("period", busDate);
                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                    TBasicSubjectMessage subjectMessage = null;
                    if (subList != null && subList.size() == 1) {
                        subjectMessage = subList.get(0);
                    }
                    if (subjectMessage != null) {
                        vouchBody.setVcsubject("生成成本_" + subjectMessage.getSubName());
                        vouchBody.setSubjectID(subjectMessage.getSubCode());
                    } else {
                        vouchBody.setVcsubject("");
                        vouchBody.setSubjectID("");
                        vouchBody.setVcabstact("生成成本");
                    }
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setVcabstact("计提本月工资");
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    // voucherBodyDao.insertVouchBody(vouchBody);
                    vBodyList.add(vouchBody);
                }
                // 计提到劳务成本
                if (yf != 0 && acDepartment.contains("服务")) {
                    totalYf = totalYf + yf;
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "工资";
                    vouchBody.setDebitAmount(yf);
                    vouchBody.setDirection("1");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("subCode", "5201");
                    para.put("subName", subName);
                    para.put("accountID", account.getAccountID());
                    para.put("period", busDate);
                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                    TBasicSubjectMessage subjectMessage = null;
                    if (subList != null && subList.size() == 1) {
                        subjectMessage = subList.get(0);
                    }
                    if (subjectMessage != null) {
                        vouchBody.setVcsubject("劳务成本_" + subjectMessage.getSubName());
                        vouchBody.setSubjectID(subjectMessage.getSubCode());
                    } else {
                        vouchBody.setVcsubject("");
                        vouchBody.setSubjectID("");
                        vouchBody.setVcabstact("劳务成本");
                    }
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    if (fl == 1) {
                        vouchBody.setVcabstact("计提本月工资");
                    }

                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    // voucherBodyDao.insertVouchBody(vouchBody);
                    vBodyList.add(vouchBody);
                }
                // 计提到管理费用
                if (yf != 0 && acDepartment.contains("管理")) {
                    totalYf = totalYf + yf;
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "工资";
                    // vouchBody.setVcsubject("管理费用_工资");
                    vouchBody.setDebitAmount(yf);
                    vouchBody.setDirection("1");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("subCode", "6602");
                    para.put("subName", subName);
                    para.put("accountID", account.getAccountID());
                    para.put("period", busDate);
                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                    TBasicSubjectMessage subjectMessage = null;
                    if (subList != null && subList.size() == 1) {
                        subjectMessage = subList.get(0);
                    }
                    if (subjectMessage != null) {
                        vouchBody.setVcsubject("管理费用_" + subjectMessage.getSubName());
                        vouchBody.setSubjectID(subjectMessage.getSubCode());

                    } else {
                        vouchBody.setVcsubject("");
                        vouchBody.setSubjectID("");
                        vouchBody.setVcabstact("管理费用");
                    }
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    if (fl == 1) {
                        vouchBody.setVcabstact("计提本月工资");
                    }
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    // voucherBodyDao.insertVouchBody(vouchBody);
                    vBodyList.add(vouchBody);
                }
                // 计提到销售费用
                if (yf != 0 && acDepartment.contains("销售")) {
                    totalYf = totalYf + yf;
                    // 构造凭证分录
                    VoucherBody vouchBody = new VoucherBody();
                    vouchBody.setVouchID(vouchID);
                    vouchBody.setPeriod(busDate);
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    vouchBody.setAccountID(account.getAccountID());
                    // 科目名称
                    String subName = "工资";
                    vouchBody.setDebitAmount(yf);
                    vouchBody.setDirection("1");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("subCode", "6601");
                    para.put("subName", subName);
                    para.put("accountID", account.getAccountID());
                    para.put("period", busDate);
                    List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                    TBasicSubjectMessage subjectMessage = null;
                    if (subList != null && subList.size() == 1) {
                        subjectMessage = subList.get(0);
                    }
                    if (subjectMessage != null) {
                        vouchBody.setVcsubject("销售费用_" + subjectMessage.getSubName());
                        vouchBody.setSubjectID(subjectMessage.getSubCode());
                    } else {
                        vouchBody.setVcsubject("");
                        vouchBody.setSubjectID("");
                        vouchBody.setVcabstact("销售费用");
                    }
                    vouchBody.setVouchAID(UUIDUtils.getUUID());
                    if (fl == 1) {
                        vouchBody.setVcabstact("计提本月工资");
                    }
                    vouchBody.setRowIndex(fl + "");
                    fl++;
                    // 插入凭证子表
                    // voucherBodyDao.insertVouchBody(vouchBody);
                    vBodyList.add(vouchBody);
                }
                fl++;
            }
            if (vBodyList.size() > 0) {
                // 应付职工薪酬—工资
                Map<String, String> mappingCode = vatService.getAllMappingSubCode(account.getAccountID());

                String[] arr = {"工资"};
                Map<String, SubjectMessage> subMap = vatService.getSubMessageByCode(account.getAccountID(), mappingCode, arr);

                SubjectMessage subjectMessage = subMap.get("工资");

                VoucherBody vouchBody = new VoucherBody();
                vouchBody.setVouchID(vouchID);
                vouchBody.setPeriod(busDate);
                vouchBody.setVouchAID(UUIDUtils.getUUID());
                vouchBody.setAccountID(account.getAccountID());
                vouchBody.setCreditAmount(totalYf);
                vouchBody.setDirection("2");

                vouchBody.setVcsubject(subjectMessage.getFull_name());
                vouchBody.setSubjectID(subjectMessage.getSub_code());

                vouchBody.setRowIndex(fl + "");
                vBodyList.add(vouchBody);
                Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                vouchHead = new VoucherHead();
                vouchHead.setVouchID(vouchID);
                vouchHead.setPeriod(busDate);
                vouchHead.setVcDate(new Date());
                vouchHead.setAccountID(account.getAccountID());
                vouchHead.setSource(19);
                vouchHead.setVoucherNO(maxVoucherNo);
                vouchHead.setCreateDate(System.currentTimeMillis());
                vouchHead.setCreatepsn(user.getUserName());
                vouchHead.setCreatePsnID(user.getUserID());
                vouchHead.setTotalCredit(totalYf);
                vouchHead.setTotalDbit(totalYf);
            }
            if (vouchHead != null) {
                voucherHeadDao.insertVouchHead(vouchHead);
                for (int i = 0; i < vBodyList.size(); i++) {
                    voucherBodyDao.insertVouchBody(vBodyList.get(i));
                }
                voucher.setVoucherHead(vouchHead);
                voucher.setVoucherBodyList(vBodyList);
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 发放工资
     *
     * @param session
     * @throws BusinessException
     */

    @Override
    public void arch2vouch4(HttpSession session) throws BusinessException {
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            // 企业性质 1：生产型2：贸易型3：服务型
            String busDate = (String) sessionMap.get("busDate");
            // String date = DateUtil.getLastMonth(busDate);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("period", busDate);
            String subName = "工资";
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("subCode", "2211");
            para.put("subjectID", "2211");
            para.put("subName", subName);
            para.put("accountID", account.getAccountID());
            para.put("period", busDate);
            para.put("busDate", busDate);
            List<TBasicSubjectMessage> li = tBasicSubjectMessageMapper.querySubByAccAndCode(para);
            if (li == null || li.get(0) == null || li.get(0).getEndingBalanceCredit().doubleValue() == 0) {
                // 应付职工薪酬没有期末余额贷,不用发放
                return;
            } else {
                VoucherHead vouchHead = new VoucherHead();
                String uuid = UUIDUtils.getUUID();
                vouchHead.setVouchID(uuid);
                vouchHead.setPeriod(busDate);
                vouchHead.setVcDate(new Date());
                vouchHead.setAccountID(account.getAccountID());
                vouchHead.setVoucherNO(0);
                vouchHead.setCreateDate(System.currentTimeMillis());
                vouchHead.setCreatepsn(user.getUserName());
                vouchHead.setCreatePsnID(user.getUserID());
                vouchHead.setSource(3);
                Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                vouchHead.setVoucherNO(maxVoucherNo);
                vouchHead.setTotalCredit(li.get(0).getEndingBalanceCredit().doubleValue());
                vouchHead.setTotalDbit(li.get(0).getEndingBalanceCredit().doubleValue());
                List<TBasicSubjectMessage> subList = tBasicSubjectMessageMapper.querySubject(para);
                TBasicSubjectMessage subMessage = null;
                if (subList != null && subList.size() == 1) {
                    subMessage = subList.get(0);
                }
                // 借：应付职工薪酬-工资100
                int fl = 1;
                VoucherBody vouchBody = new VoucherBody();
                vouchBody.setVouchID(uuid);
                vouchBody.setPeriod(busDate);
                vouchBody.setVouchAID(UUIDUtils.getUUID());
                vouchBody.setAccountID(account.getAccountID());
                vouchBody.setDebitAmount(li.get(0).getEndingBalanceCredit().doubleValue());
                vouchBody.setDirection("1");
                if (subMessage != null) {
                    vouchBody.setVcsubject("应付职工薪酬_" + subMessage.getSubName());
                    vouchBody.setSubjectID(subMessage.getSubCode());
                } else {
                    vouchBody.setVcsubject("");
                    vouchBody.setSubjectID("");
                }
                vouchBody.setVcabstact("发放薪资");
                vouchBody.setRowIndex(fl + "");

                // 查询上月薪资单
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", account.getAccountID());
                pa.put("period", DateUtil.getLastMonth(busDate));
                List<Arch> archList = archDao.queryArchData(pa);
                if (archList == null || archList.size() == 0) {
                    // 上月没薪资单
                    VoucherBody vb1 = new VoucherBody();
                    vb1.setVouchID(uuid);
                    vb1.setPeriod(busDate);
                    vb1.setVouchAID(UUIDUtils.getUUID());
                    vb1.setAccountID(account.getAccountID());
                    vb1.setCreditAmount(li.get(0).getEndingBalanceCredit().doubleValue());
                    vb1.setDirection("2");
                    vb1.setSubjectID("");
                    vb1.setVcsubject("");
                    vb1.setRowIndex(2 + "");
                    Voucher voucher = new Voucher();
                    voucher.setVoucherHead(vouchHead);
                    List<VoucherBody> list = new ArrayList<VoucherBody>();
                    list.add(vouchBody);
                    list.add(vb1);
                    voucher.setVoucherBodyList(list);
                    voucherHeadDao.insertVouchHead(vouchHead);
                    voucherBodyDao.insertVouchBody(vouchBody);
                    voucherBodyDao.insertVouchBody(vb1);
                    vatService.checkVouch(param, voucher);
                } else {
                    // 上月有薪资单
                    double sb = 0;
                    double gjj = 0;
                    double gs = 0;
                    if (null != archList && archList.size() > 0) {
                        Map<String, Object> p = new HashMap<String, Object>();
                        p.put("accountID", account.getAccountID());
                        p.put("busDate", DateUtil.getLastMonth(busDate));
                        p.put("period", DateUtil.getLastMonth(busDate));
                        Object obj = archDao.queryFfArchData(p);
                        Map<String, Object> objMap = (Map<String, Object>) obj;
                        sb = Double.parseDouble(objMap.get("sb").toString());
                        gjj = Double.parseDouble(objMap.get("gjj").toString());
                        gs = Double.parseDouble(objMap.get("gs").toString());
                    }
                    // 社保
                    Map<String, Object> sbParam = new HashMap<String, Object>();
                    String sbName = "社保";
                    sbParam.put("subCode", "1221");// 其他应收款
                    sbParam.put("subName", sbName);
                    sbParam.put("accountID", account.getAccountID());
                    sbParam.put("period", busDate);
                    sbParam.put("busDate", busDate);
                    List<TBasicSubjectMessage> sbList = tBasicSubjectMessageMapper.querySubject(sbParam);
                    if (sbList == null) {
                        sbName = "社会保险";
                        sbParam.put("subName", sbName);
                        sbList = tBasicSubjectMessageMapper.querySubject(sbParam);
                    }
                    VoucherBody vb1 = new VoucherBody();
                    vb1.setVouchID(uuid);
                    vb1.setPeriod(busDate);
                    vb1.setVouchAID(UUIDUtils.getUUID());
                    vb1.setAccountID(account.getAccountID());
                    vb1.setCreditAmount(sb);
                    vb1.setDirection("2");
                    vb1.setSubjectID("");
                    vb1.setVcsubject("");
                    vb1.setVcabstact("社保(个人)");
                    vb1.setRowIndex("2");
                    if (null != sbList && sbList.size() == 1) {
                        vb1.setSubjectID(sbList.get(0).getSubCode());
                        vb1.setVcsubject(sbList.get(0).getFullName());
                    }

                    // 公积金
                    Map<String, Object> gjjParam = new HashMap<String, Object>();
                    String gjjName = "公积金";
                    gjjParam.put("subCode", "1221");// 其他应收款
                    gjjParam.put("subName", gjjName);
                    gjjParam.put("accountID", account.getAccountID());
                    gjjParam.put("period", busDate);
                    gjjParam.put("busDate", busDate);
                    List<TBasicSubjectMessage> gjjList = tBasicSubjectMessageMapper.querySubject(gjjParam);
                    if (gjjList == null) {
                        gjjName = "公共积金";
                        gjjParam.put("subName", sbName);
                        gjjList = tBasicSubjectMessageMapper.querySubject(gjjParam);
                    }
                    VoucherBody vb2 = new VoucherBody();
                    vb2.setVouchID(uuid);
                    vb2.setPeriod(busDate);
                    vb2.setVouchAID(UUIDUtils.getUUID());
                    vb2.setAccountID(account.getAccountID());
                    vb2.setCreditAmount(gjj);
                    vb2.setDirection("2");
                    vb2.setSubjectID("");
                    vb2.setVcsubject("");
                    vb2.setVcabstact("公积金(个人)");
                    vb2.setRowIndex("3");
                    if (null != gjjList && gjjList.size() == 1) {
                        vb2.setSubjectID(gjjList.get(0).getSubCode());
                        vb2.setVcsubject(gjjList.get(0).getFullName());
                    }

                    // 个税
                    Map<String, Object> gsParam = new HashMap<String, Object>();
                    String gsName = "个人所得税";
                    gsParam.put("subCode", "2221");// 个人所得税
                    gsParam.put("subName", gsName);
                    gsParam.put("accountID", account.getAccountID());
                    gsParam.put("period", busDate);
                    gsParam.put("busDate", busDate);
                    List<TBasicSubjectMessage> gsList = tBasicSubjectMessageMapper.querySubject(gsParam);
                    if (gsList == null) {
                        gsName = "个税";
                        gsParam.put("subName", gsName);
                        gsList = tBasicSubjectMessageMapper.querySubject(gsParam);
                    }
                    VoucherBody vb3 = new VoucherBody();
                    vb3.setVouchID(uuid);
                    vb3.setPeriod(busDate);
                    vb3.setVouchAID(UUIDUtils.getUUID());
                    vb3.setAccountID(account.getAccountID());
                    vb3.setCreditAmount(gs);
                    vb3.setDirection("2");
                    vb3.setSubjectID("");
                    vb3.setVcsubject("");
                    vb3.setVcabstact("个人所得税");
                    vb3.setRowIndex("4");
                    if (null != gsList && gsList.size() == 1) {
                        vb3.setSubjectID(gsList.get(0).getSubCode());
                        vb3.setVcsubject(gsList.get(0).getFullName());
                    }

                    // 库存现金/银行存款
                    VoucherBody vb4 = new VoucherBody();
                    vb4.setVouchID(uuid);
                    vb4.setPeriod(busDate);
                    vb4.setVouchAID(UUIDUtils.getUUID());
                    vb4.setAccountID(account.getAccountID());
                    vb4.setCreditAmount(li.get(0).getEndingBalanceCredit().doubleValue() - sb - gjj - gs);
                    vb4.setDirection("2");
                    vb4.setSubjectID("");
                    vb4.setVcsubject("");
                    vb4.setVcabstact("库存现金/银行存款");
                    vb4.setRowIndex("5");
                    vb4.setSubjectID("");
                    vb4.setVcsubject("");

                    Voucher voucher = new Voucher();
                    voucher.setVoucherHead(vouchHead);
                    List<VoucherBody> list = new ArrayList<VoucherBody>();
                    list.add(vouchBody);
                    list.add(vb1);
                    list.add(vb2);
                    list.add(vb3);
                    list.add(vb4);
                    voucher.setVoucherBodyList(list);

                    voucherHeadDao.insertVouchHead(vouchHead);
                    for (int i = 0; i < list.size(); i++) {
                        voucherBodyDao.insertVouchBody(list.get(i));
                    }

                    boolean bool = vatService.checkVouch(param, voucher);
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                    }
                    /*
                     * // 缴纳社保 VoucherHead vh = new VoucherHead(); String uid =
                     * UUIDUtils.getUUID();
                     *
                     * vh.setVouchID(uid); vh.setPeriod(busDate);
                     * vh.setVcDate(new Date());
                     * vh.setAccountID(account.getAccountID());
                     * vh.setCreateDate(System.currentTimeMillis());
                     * vh.setCreatepsn(user.getUserName());
                     * vh.setCreatePsnID(user.getUserID()); vh.setSource(3);
                     * Integer maxVouchNo =
                     * voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                     * vh.setVoucherNO(maxVouchNo);
                     *
                     * // 管理费用-社会保险（单位）公司缴纳部分 Map<String, Object> dwsbParam =
                     * new HashMap<String, Object>(); String dwsbName = "社保";
                     * dwsbParam.put("subCode", "6602");// 社保
                     * dwsbParam.put("subName", dwsbName);
                     * dwsbParam.put("accountID", account.getAccountID());
                     * dwsbParam.put("period", busDate);
                     * dwsbParam.put("busDate", busDate);
                     * List<TBasicSubjectMessage> dwsbList =
                     * tBasicSubjectMessageMapper.querySubject(dwsbParam); if
                     * (dwsbList == null) { dwsbName = "社会保险";
                     * dwsbParam.put("subName", dwsbName); dwsbList =
                     * tBasicSubjectMessageMapper.querySubject(dwsbParam); }
                     *
                     * VoucherBody vb5 = new VoucherBody(); vb5.setVouchID(uid);
                     * vb5.setPeriod(busDate);
                     * vb5.setVouchAID(UUIDUtils.getUUID());
                     * vb5.setAccountID(account.getAccountID());
                     * vb5.setDebitAmount(0.0); vb5.setDirection("1"); if
                     * (dwsbList != null && dwsbList.size() == 1) {
                     * vb5.setSubjectID(dwsbList.get(0).getSubCode());
                     * vb5.setVcsubject(dwsbList.get(0).getFullName()); } else {
                     * vb5.setSubjectID(""); vb5.setVcsubject(""); }
                     *
                     * vb5.setVcabstact("社会保险（单位）公司缴纳部分"); vb5.setRowIndex("1");
                     *
                     * Map<String, Object> dwgjjParam = new HashMap<String,
                     * Object>(); String dwgjjName = "公积金";
                     * dwgjjParam.put("subCode", "6602");// 社保
                     * dwgjjParam.put("subName", dwgjjName);
                     * dwgjjParam.put("accountID", account.getAccountID());
                     * dwgjjParam.put("period", busDate);
                     * dwgjjParam.put("busDate", busDate);
                     * List<TBasicSubjectMessage> dwgjjList =
                     * tBasicSubjectMessageMapper.querySubject(dwgjjParam); if
                     * (dwgjjList == null) { dwgjjName = "公共积金";
                     * dwgjjParam.put("subName", dwsbName); dwgjjList =
                     * tBasicSubjectMessageMapper.querySubject(dwgjjParam); } //
                     * 管理费用-公积金（单位） 公司缴纳部分4 VoucherBody vb6 = new VoucherBody();
                     * vb6.setVouchID(uid); vb6.setPeriod(busDate);
                     * vb6.setVouchAID(UUIDUtils.getUUID());
                     * vb6.setAccountID(account.getAccountID());
                     * vb6.setDebitAmount(0.0); if (dwgjjList != null &&
                     * dwgjjList.size() == 1) {
                     * vb6.setSubjectID(dwgjjList.get(0).getSubCode());
                     * vb6.setVcsubject(dwgjjList.get(0).getFullName()); } else
                     * { vb6.setSubjectID(""); vb6.setVcsubject(""); }
                     * vb6.setDirection("1");
                     *
                     * vb6.setVcabstact("公积金（单位）  公司缴纳部分");
                     * vb6.setRowIndex("2");
                     *
                     * // 社保 if (sbList == null) { sbName = "社会保险";
                     * sbParam.put("subName", sbName); sbList =
                     * tBasicSubjectMessageMapper.querySubject(sbParam); }
                     * VoucherBody vb7 = new VoucherBody(); vb7.setVouchID(uid);
                     * vb7.setPeriod(busDate);
                     * vb7.setVouchAID(UUIDUtils.getUUID());
                     * vb7.setAccountID(account.getAccountID());
                     * vb7.setDebitAmount(sb); vb7.setDirection("1");
                     * vb7.setSubjectID(""); vb7.setVcsubject("");
                     * vb7.setVcabstact("社保(个人)"); vb7.setRowIndex("3"); if
                     * (null != sbList && sbList.size() == 1) {
                     * vb7.setSubjectID(sbList.get(0).getSubCode());
                     * vb7.setVcsubject(sbList.get(0).getFullName()); } // 公积金
                     * if (gjjList == null) { gjjName = "公共积金";
                     * gjjParam.put("subName", sbName); gjjList =
                     * tBasicSubjectMessageMapper.querySubject(gjjParam); }
                     * VoucherBody vb8 = new VoucherBody(); vb8.setVouchID(uid);
                     * vb8.setPeriod(busDate);
                     * vb8.setVouchAID(UUIDUtils.getUUID());
                     * vb8.setAccountID(account.getAccountID());
                     * vb8.setDebitAmount(gjj); vb8.setDirection("1");
                     * vb8.setSubjectID(""); vb8.setVcsubject("");
                     * vb8.setVcabstact("公积金(个人)"); vb8.setRowIndex("4"); if
                     * (null != gjjList && gjjList.size() == 1) {
                     * vb8.setSubjectID(gjjList.get(0).getSubCode());
                     * vb8.setVcsubject(gjjList.get(0).getFullName()); } // 个税
                     * if (gsList == null) { gsName = "个税";
                     * gsParam.put("subName", gsName); gsList =
                     * tBasicSubjectMessageMapper.querySubject(gsParam); }
                     * VoucherBody vb9 = new VoucherBody(); vb9.setVouchID(uid);
                     * vb9.setPeriod(busDate);
                     * vb9.setVouchAID(UUIDUtils.getUUID());
                     * vb9.setAccountID(account.getAccountID());
                     * vb9.setDebitAmount(gs); vb9.setDirection("1");
                     * vb9.setSubjectID(""); vb9.setVcsubject("");
                     * vb9.setVcabstact("个人所得税"); vb9.setRowIndex("5"); if (null
                     * != gsList && gsList.size() == 1) {
                     * vb9.setSubjectID(gsList.get(0).getSubCode());
                     * vb9.setVcsubject(gsList.get(0).getFullName()); }
                     *
                     * VoucherBody vb10 = new VoucherBody();
                     * vb10.setVouchID(uid); vb10.setPeriod(busDate);
                     * vb10.setVouchAID(UUIDUtils.getUUID());
                     * vb10.setAccountID(account.getAccountID());
                     * vb10.setCreditAmount(0.0); vb10.setDirection("2");
                     * vb10.setSubjectID(""); vb10.setVcsubject("");
                     * vb10.setVcabstact("银行存款"); vb10.setRowIndex("6");
                     *
                     * Voucher vouch = new Voucher(); vouch.setVoucherHead(vh);
                     * List<VoucherBody> vbL = new ArrayList<VoucherBody>();
                     * vbL.add(vb5); vbL.add(vb6); vbL.add(vb7); vbL.add(vb8);
                     * vbL.add(vb9); vbL.add(vb10);
                     * vouch.setVoucherBodyList(vbL); //
                     * vouch.setVoucherHead(vh);
                     * voucherHeadDao.insertVouchHead(vh); for (int j = 0; j <
                     * vbL.size(); j++) {
                     * voucherBodyDao.insertVouchBody(vbL.get(j)); } boolean boo
                     * = vatService.checkVouch(param, vouch); if (boo) {
                     * tBasicSubjectMessageMapper.chgSubAmountByCreate(param,
                     * vouch); }
                     */
                }
            }

        } catch (Exception e) {
            throw new BusinessException("发放薪资异常");
        }
    }
}
