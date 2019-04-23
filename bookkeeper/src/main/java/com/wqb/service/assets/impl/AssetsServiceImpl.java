package com.wqb.service.assets.impl;

import com.wqb.common.*;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.dao.assets.AssetsRecordDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

//固定资产业务层
@SuppressWarnings("unused")
@Component
@Service("assetsService")
public class AssetsServiceImpl implements AssetsService {

    private static final String String = null;
    public static Properties properties;
    private String str = null;
    public static List<String> baseSub = new ArrayList<>();
    public static List<String> smallBaseSub = new ArrayList<>();
    private List<Assets> linkedList = new LinkedList<>();

    @Autowired
    AssetsDao assetsDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    AssetsRecordDao assetsRecordDao;

    @SuppressWarnings("static-access")
    public Properties getToProperties() {
        return this.properties;
    }

    @SuppressWarnings("static-access")
    public List<String> getToBaseSub() {
        return this.baseSub;
    }

    @SuppressWarnings("static-access")
    public List<String> getToSmallBaseSub() {
        return this.smallBaseSub;
    }

    // 读取配置文件
    static {

        InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/assets.properties");
        InputStream inStreamSub = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/baseSystemSubject.properties");
        InputStream smallInStreamSub = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/smallBaseSystemSubject.properties");


        try {
            properties = new Properties();
            properties.load(inStream);
            Properties propertiesSub = new Properties();
            propertiesSub.load(inStreamSub);
            Enumeration<Object> keys = propertiesSub.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                baseSub.add(key);
            }

            propertiesSub = new Properties();
            propertiesSub.load(smallInStreamSub);
            Enumeration<Object> small_keys = propertiesSub.keys();
            while (small_keys.hasMoreElements()) {
                String key = (String) small_keys.nextElement();
                smallBaseSub.add(key);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(InputStream inStream) {
        Properties pp = new Properties();
        try {
            pp.load(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pp;
    }

    // excel表上传

    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public Map<String, Object> insertAssert(List<Map<String, Object>> list, Map<String, String> map)
            throws BusinessException {

        Map<String, Object> linkMap = new LinkedHashMap<>();
        try {
            List<String> codeList = new ArrayList<>();
            List<String> listTem = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {

                String code = (String) list.get(i).get("map0");
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
                    String s = "第" + (i + 1) + "行资产编码" + code + "与其它数据冲突,资产编码不能重复,请仔细检查!";
                    linkMap.put("fail", s);
                    return linkMap;
                }
            }

            Integer count = 0;
            // 循环遍历每一行excel表数据
            ArrayList<Assets> arrayList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                // 定义固定资产对象
                Assets assets = new Assets();
                // 获取一行固定资产数据
                Map<String, Object> assetsMap = list.get(i);

                // 根据code去除重复值导入
                String asCode = (String) assetsMap.get("map0");

                if (StringUtil.isEmpty(asCode)) {
                    linkMap.put("fail", "第" + (i + 2) + "资产编码不能为空,请下载模板导入!");
                    return linkMap;
                }
                // 判断入账日期不能为空-司刘东20180926
                String asrzrq = (String) assetsMap.get("map9");
                if (StringUtil.isEmpty(asrzrq)) {
                    if (!asCode.contains("合计")) {
                        linkMap.put("fail", "第" + (i + 2) + "入账日期不能为空,请下载模板导入!");
                        return linkMap;
                    }
                }
                asCode = asCode.replace(" ", "");
                // 如果是合计结尾 终止读取
                if (asCode.contains("合计")) {
                    break;
                }
                // 资产编码检验
                if (StringUtil.isEmptyWithTrim(asCode)) {
                    linkMap.put("fail", "第" + (i + 2) + "固定资产代码不能为空,请下载模板导入!");
                    return linkMap;
                }
                // 资产名称检验
                String asName = (String) assetsMap.get("map1");
                if (StringUtil.isEmpty(asName)) {
                    linkMap.put("fail", "第" + (i + 2) + "资产名称不能为空,请下载模板导入!");
                    return linkMap;
                }
                // 使用部门检验
                String department = (String) assetsMap.get("map4"); // 使用部门必填
                if (StringUtil.isEmptyWithTrim(department)) {
                    linkMap.put("fail", "第" + (i + 2) + "使用部门不能为空,,请下载模板导入!");
                    return linkMap;
                }

                if (!getValue(department)) {
                    linkMap.put("fail", "第" + (i + 2) + "行,部门名称必须是管理部[门]，生产部[门]，销售部[门]，其中一项，请下载模板导入!");
                    return linkMap;
                }

                assets.setAccountID((String) map.get("accountID"));
                assets.setAssetsID(UUIDUtils.getUUID()); // 设置主键
                assets.setAccountID(map.get("accountID")); // 设置账套ID
                // assets.setUserID(map.get("userID")); //设置用户ID

                assets.setAsCode(asCode); // 资产编码 代码 判别重复数据
                // 查询数据库是否已经存在
                if (assetsDao.queryByCode(assets) != null) {
                    // 重复数据
                    linkMap.put("fail", "第" + (i + 2) + "行数据已经添加到数据库,请勿重复添加!");
                    return linkMap;
                }
                assets.setAsName(asName); // 资产名称
                str = (String) assetsMap.get("map15");// 原值
                // 所有字段为date double 的字段都做非空判断，以免导入出错
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsvalue(null);
                } else {
                    assets.setAsvalue(Double.valueOf(str.replaceAll(",", "")));// 原值
                    // 1,456.12
                    // 去逗号
                }
                assets.setAsyears(null); // 年限 ?
                assets.setDepartment(department); // 使用部门
                assets.setAsState((String) assetsMap.get("map5")); // 资产状态 使用情况
                assets.setInitdepreciation(0.00); // 期初已折旧金额 ?double
                assets.setResidualrate(0.00); // 折旧率 ? double
                assets.setNetvalue(0.00); // 剩余折旧金额 ? double
                assets.setInputPeriod(new Date()); // 录入日期 ?
                assets.setUpdateDate(new Date()); // 修改时间 ?
                assets.setUpdatePsnID(map.get("userID")); // 修改id?
                assets.setUpdatePsn(map.get("userName")); // 修改人 ?
                assets.setCreatePsnID(map.get("userID")); // 创建id ?
                assets.setCreateDate(new Date()); // 创建时间 ?
                assets.setCreatePsn(map.get("userName")); // 创建人 ?
                assets.setDes((String) assetsMap.get("map26")); // 说明备注
                assets.setDmethod((String) assetsMap.get("map13")); // 折旧方法
                assets.setDsubject(""); // 折旧摊销科目
                assets.setCostsubject(""); // 成本费用科目?
                assets.setUsedyears(null); // 已使用年限 int
                assets.setUseddate(null);// 使用日期
                assets.setSourceway((String) assetsMap.get("map12")); // 增加资产方式
                // 不能为空
                assets.setGdsubject((String) assetsMap.get("map23")); // 固定资产科目
                assets.setGdStatus("2"); // 是否计提,添加默认不计提。
                assets.setAsModel((String) assetsMap.get("map2"));// 型号
                assets.setAsCategory((String) assetsMap.get("map3"));// 类别
                assets.setAsPosition((String) assetsMap.get("map6"));// 存放地点
                assets.setAsManufactor((String) assetsMap.get("map7"));// 生产厂家
                assets.setAsManufactorDate(DateUtil.fomatToDate((String) assetsMap.get("map8")));// 生产日期
                assets.setAsAccountDatea(DateUtil.fomatToDate((String) assetsMap.get("map9")));// 入账日期
                assets.setIsBeforeUse((String) assetsMap.get("map10"));// 入账前已开始使用
                // 1时2否
                assets.setAsBeforeUseDate(DateUtil.fomatToDate((String) assetsMap.get("map11")));// 入账前开始使用日期

                str = (String) assetsMap.get("map14");// 预计使用期间(工作总量)
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsEstimatePeriod(null);
                } else {
                    assets.setAsEstimatePeriod(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map16");// 累计折旧

                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsAddDeprecia(null);
                } else {
                    assets.setAsAddDeprecia(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map17");// 累计减值准备
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsCumulativeImpairment(null);
                } else {
                    assets.setAsCumulativeImpairment(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map18");// 净值
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsWorth(null);
                } else {
                    assets.setAsWorth(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map19");// 预计净残值
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsNetSalvage(null);
                } else {
                    assets.setAsNetSalvage(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map20"); // 用于折旧计算的原值
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsUseDepreciaValue(null);
                } else {
                    assets.setAsUseDepreciaValue(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map21"); // 用于折旧计算的预计使用期间(工作总量)
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsDepreciaPeriod(null);
                } else {
                    assets.setAsDepreciaPeriod(Double.valueOf(str.replaceAll(",", "")));
                }

                str = (String) assetsMap.get("map22"); // 预计剩余折旧期间数(工作总量)
                if (StringUtil.isEmptyWithTrim(str)) {
                    assets.setAsExpectedPeriod(null);
                } else {
                    assets.setAsExpectedPeriod(Double.valueOf(str.replaceAll(",", "")));
                }

                assets.setAsCumulativeSubject((String) assetsMap.get("map24"));// 累计折旧科目
                assets.setAsEconomicUse((String) assetsMap.get("map25"));// 经济用途
                assets.setAsDepreciaSubject((String) assetsMap.get("map27"));// 折旧费用科目
                assets.setImportFlg("1"); // 是否导入 1导入 2手动添加
                assets.setTaxRate(null); // 应交税费
                assets.setVouchID(null); // 凭证号

                // 把这一行数据插入到数据库
                assetsDao.insertAssert(assets);
                // linkedList.add(assets);
                count++;
            }
            // assetsDao.insertBath(linkedList);
            linkMap.put("succss", "导入完毕，共导入" + count + "条数据。");
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
    // 固定资产添加1
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addAssets(Assets assets, Map<String, String> map) throws BusinessException {
        try {
            Map<String, String> res = new HashMap<>();
            String assetsID = UUIDUtils.getUUID();
            assets.setAssetsID(assetsID); // 设置主键
            assets.setPicture(null); // 图片
            assets.setAsyears(null); // 年限 ?
            assets.setInitdepreciation(null);// 期初已折旧金额
            assets.setResidualrate(null);// 折旧率
            assets.setNetvalue(null);// 剩余折旧金额
            assets.setInputPeriod(new Date());// 录入日期
            assets.setUpdateDate(new Date());// 修改时间
            assets.setAccountID(map.get("accountID"));// 账套ID
            assets.setUpdatePsnID(map.get("userID"));// 修改人ID
            assets.setUpdatePsn(map.get("userName"));// 修改人
            assets.setCreatePsnID(map.get("userID"));// 创建人ID
            assets.setCreatePsn(map.get("userName"));// 创建人ID
            assets.setCreateDate(new Date());// 创建时间
            assets.setDsubject(null);// 折旧摊销科目
            assets.setCostsubject(null);// 成本费用科目
            assets.setUsedyears(null);// 已使用年限
            assets.setUseddate(null);// 使用日期
            assets.setGdStatus("2");// 是否计提(1:是,2:否,初始否
            assets.setImportFlg("2"); // 是否导入1 是 2 否
            // assets.setTaxRate(null); //应交税费
            assets.setVouchID(null); // 凭证号
            Integer addAssert = assetsDao.addAssert(assets);
            // 应收税费
            Double taxRate = assets.getTaxRate();
            if (addAssert == 1) {
                init(map);

                // 固定资产 1601
                // 应交税费 2221
                // 应收账款 2202

                // 分录一 固定资产-苹果手机 （资产名称） 借
                // 分录二 应交税费-应交增值税-进项税 借
                // 分录三 应付账款-联想电脑(供货方) 厂家是必填项 贷
                String asName = assets.getAsName(); // 资产名称 二级科目
                String asManufactor = assets.getAsManufactor(); // 供货商
                Double asvalue = assets.getAsvalue(); // 原值

                SubjectMessage gd2 = vatService.querySub(asName, "1601", "7");
                if (gd2 == null) {
                    String subcode = vatService.getNumber("1601", "7", "1601000");
                    gd2 = vatService.createSub(subcode, "1601", asName, "固定资产_" + asName);
                }

                String[] arr = {"进项税"};
                Map<String, String> mappingCode = vatService.getAllMappingSubCode(map.get("accountID").toString());

                Map<String, SubjectMessage> subMap = vatService.getSubMessageByCode(map.get("accountID").toString(), mappingCode, arr);

                SubjectMessage jx_sub = subMap.get("进项税");

                // asManufactor 厂家
                SubjectMessage yf2 = vatService.querySub(asManufactor, "2202", "7");
                if (yf2 == null) {
                    String subcode = vatService.getNumber("2202", "7", "2202000");
                    yf2 = vatService.createSub(subcode, "2202", asManufactor, "应付账款_" + asManufactor, "固定资产新增凭证", null,
                            null);
                }

                Voucher voucher = new Voucher();
                List<VoucherBody> vbList = new ArrayList<>();

                Double totalCredit = asvalue + taxRate; // 凭证贷金额合计

                VoucherHead voucherHead = vatService.createVoucherHead(0, "固定资产添加", totalCredit, totalCredit);

                voucher.setVoucherHead(voucherHead); // 反写固定资产凭证

                String vouchID = voucherHead.getVouchID();

                String zhaiyao = "应付" + yf2.getSub_name() + "账款"; // 摘要 科目名称
                // 分录1
                String rowIndex = "1";// 分录号
                String vcsubject = gd2.getFull_name(); // 科目全名称
                Double debitAmount = asvalue;// 借方金额 原值
                Double creditAmount = null;// 贷方金额
                String direction = "1";// 方向(1:借2:贷)
                String subCode = gd2.getSub_code();// 科目编码
                VoucherBody vouchBody1 = vatService.createVouchBody(vouchID, rowIndex, zhaiyao, vcsubject, debitAmount,
                        creditAmount, direction, subCode, "RMB");

                vbList.add(vouchBody1); // 反写凭证到科目

                // 分录2
                rowIndex = "2";// 分录号
                vcsubject = jx_sub.getFull_name(); // 科目全名称
                debitAmount = taxRate;// 借方金额 = 已经税费
                creditAmount = null;// 贷方金额
                direction = "1";// 方向(1:借2:贷)
                subCode = jx_sub.getSub_code();// 科目编码
                VoucherBody vouchBody2 = vatService.createVouchBody(vouchID, rowIndex, null, vcsubject, debitAmount,
                        creditAmount, direction, subCode, "RMB");
                vbList.add(vouchBody2); // 反写凭证到科目

                // 分录3
                rowIndex = "3";// 分录号
                vcsubject = yf2.getFull_name(); // 科目全名称
                debitAmount = null; // 借方金额
                creditAmount = asvalue + taxRate;
                // 贷方金额 （原值 + 应交税费）
                direction = "2";// 方向(1:借2:贷)
                subCode = yf2.getSub_code();// 科目编码
                VoucherBody vouchBody3 = vatService.createVouchBody(vouchID, rowIndex, null, vcsubject, debitAmount,
                        creditAmount, direction, subCode, "RMB");
                vbList.add(vouchBody3); // 反写凭证到科目

                voucher.setVoucherBodyList(vbList);

                Map<String, Object> m1 = new HashMap<>();
                m1.put("accountID", map.get("accountID"));
                m1.put("busDate", map.get("period"));
                m1.put("period", map.get("period"));
                // 科目联动更新
                boolean bool = vatService.checkVouch(m1, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(m1, voucher);
                }

                // 更新凭证号到固定资产
                map.put("assetsID", assetsID);
                map.put("vouchID", vouchID);
                assetsDao.updAddAssets(map);

                return comeBack("添加成功", "success");
            }
            return comeBack("添加失败", "fail");
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    public Page<Assets> listAssets(Map<String, Object> map) throws BusinessException {
        try {
            // 通过（当前页、每页显示条数、总条数） 初始化分页信息
            Page<Assets> page = new Page<Assets>();
            // 根据查询条件算出所需数据的总条数
            int cout = assetsDao.queryCount(map);
            int pageSize = page.getPageSize(); // 每页显示条数
            pageSize = 30;
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
            List<Assets> listAssets = assetsDao.queryAssertPage(map);
            // 分页内容
            page.setContent(listAssets);
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
    public void deleteByAsId(Map<java.lang.String, Object> map) throws BusinessException {

        try {
            assetsDao.deleteByAsId(map);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 批量删除
    // @Transactional
    @Override
    public void delAll(Map<String, Object> map) throws BusinessException {
        try {
            assetsDao.delAll(map);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public Voucher assets2vouch(User user, Account account) throws BusinessException {
        try {
            // 新增凭证后 附带变更科目
            Voucher voucher = new Voucher();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String busDate = account.getUseLastPeriod();
            Date bdDate = sdf.parse(busDate + "-00");
            // 获取账套名下固定资产
            Map<String, Object> accParam = new HashMap<String, Object>();
            List<Assets> glList = new ArrayList<Assets>();
            List<Assets> xsList = new ArrayList<Assets>();
            List<Assets> scList = new ArrayList<Assets>();
            accParam.put("accountID", account.getAccountID());
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            List<Assets> assList = assetsDao.queryAssByAcc(accParam);

            Map<String, Object> querymap = new HashMap<>();
            querymap.put("accountID", account.getAccountID());
            querymap.put("period", busDate);
            double wclz = 0.01;
            if (null != assList && assList.size() > 0) {
                for (int i = 0; i < assList.size(); i++) {
                    Assets assets = assList.get(i);
                    Date rzDate = assets.getAsAccountDatea();
                    String rzDateStr = DateUtil.getTime1(rzDate).trim();
                    // 固定资产入账当月不计提
                    if (rzDateStr.contains(busDate) || rzDate.after(bdDate)) {
                        continue;
                    }
                    if ("管理部".equals(assets.getDepartment()) || "管理部门".equals(assets.getDepartment())) {
                        glList.add(assets);
                    }
                    if ("销售部".equals(assets.getDepartment()) || "业务部".equals(assets.getDepartment())
                            || "销售部门".equals(assets.getDepartment()) || "业务部门".equals(assets.getDepartment())) {
                        xsList.add(assets);
                    }
                    if ("生产部".equals(assets.getDepartment()) || "生产部门".equals(assets.getDepartment())) {
                        scList.add(assets);
                    }
                }
            }
            double totalGlf = 0.0;
            double totalXsf = 0.0;
            double totalZzf = 0.0;
            // 折旧到管理费用
            if (glList.size() > 0) {
                for (Assets glAssets : glList) {
                    double ssyzje = 0;
                    // 原值
                    double asvalue = glAssets.getAsvalue();
                    // 累计折旧
                    double asAddDeprecia = glAssets.getAsAddDeprecia() == null ? 0 : glAssets.getAsAddDeprecia();
                    // 预计净残值
                    double yjjcz = 0;
                    if (null != glAssets.getAsNetSalvage()) {
                        yjjcz = glAssets.getAsNetSalvage();
                    }
                    // 预计净残值率=预计净残值/原值
                    double yjjczl = yjjcz / asvalue;
                    // 月折旧率
                    if (glAssets.getAsDepreciaPeriod() == null || glAssets.getAsDepreciaPeriod() == 0.0) {
                        continue;
                    }
                    double yzjl = (1 - yjjczl) / glAssets.getAsDepreciaPeriod();
                    // 月折旧额
                    double yzje = yzjl * asvalue;
                    BigDecimal b = new BigDecimal(yzje);
                    yzje = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    // 预计剩余折旧期间数(工作总量)
                    double asExpectedPeriod = glAssets.getAsExpectedPeriod();
                    // 定义误差 1(月折旧额有除不尽的问题) 1以内默认为折旧完
                    double sjwc = Math.abs(asvalue - asAddDeprecia - yzje - yjjcz) / asvalue;
                    if (Math.abs(asAddDeprecia + yjjcz - asvalue) < 0.01) {
                        continue;
                    }
                    if (sjwc > wclz) {
                        // 正常折旧
                        totalGlf = totalGlf + yzje;
                        // 开始折旧
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("assetsID", glAssets.getAssetsID());
                        param.put("yzje", yzje);
                        param.put("asExpectedPeriod", glAssets.getAsExpectedPeriod());
                        // 固定资产主键
                        param.put("assetsID", glAssets.getAssetsID());
                        // 月折旧额
                        param.put("yzje", yzje);
                        assetsDao.updAssets(param);
                        ssyzje = yzje;
                    } else {
                        // 该期预完全折完
                        if (sjwc == 0) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", glAssets.getAssetsID());
                            totalGlf = totalGlf + yzje;
                            assetsDao.updAssets1(param);
                            ssyzje = yzje;
                        } else {
                            // 该期预彻底折完 （但是有少量残余需特殊处理）
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", glAssets.getAssetsID());
                            totalGlf = totalGlf + (asvalue - asAddDeprecia);
                            assetsDao.updAssets1(param);
                            ssyzje = asvalue - asAddDeprecia;
                        }
                    }
                    if (ssyzje != 0) {
                        // 折旧记录在案
                        AssetsRecord assetsRecord = new AssetsRecord();
                        assetsRecord.setZjid(UUIDUtils.getUUID());
                        assetsRecord.setAssetsID(glAssets.getAssetsID());
                        assetsRecord.setSsyzje(ssyzje);
                        assetsRecord.setPeriod(busDate);
                        assetsRecord.setAccountID(account.getAccountID());
                        assetsRecordDao.insertAssetsRecord(assetsRecord);
                    }
                }

            }
            // 折旧到销售费用
            if (xsList.size() > 0) {
                for (Assets xsAssets : xsList) {
                    double ssyzje = 0;
                    // 原值
                    double asvalue = xsAssets.getAsvalue();
                    // 累计折旧
                    double asAddDeprecia = 0;
                    if (null != xsAssets.getAsAddDeprecia()) {
                        asAddDeprecia = xsAssets.getAsAddDeprecia();
                    }
                    // 预计净残值
                    double yjjcz = 0;
                    if (null != xsAssets.getAsNetSalvage()) {
                        yjjcz = xsAssets.getAsNetSalvage();
                    }

                    // 预计净残值率=预计残值/原值
                    double yjjczl = yjjcz / asvalue;
                    // 月折旧率
                    double yzjl = (1 - yjjczl) / xsAssets.getAsDepreciaPeriod();
                    // 月折旧额
                    double yzje = yzjl * asvalue;
                    BigDecimal b = new BigDecimal(yzje);
                    yzje = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    // 预计剩余折旧期间数(工作总量)
                    double asExpectedPeriod = xsAssets.getAsExpectedPeriod();
                    // 定义误差 (月折旧额有除不尽的问题) 1以内默认为折旧完
                    double sjwc = Math.abs(asvalue - asAddDeprecia - yzje - yjjcz) / asvalue;

                    if (Math.abs(asAddDeprecia + yjjcz - asvalue) < 0.01) {
                        continue;
                    }
                    if (sjwc > wclz) {
                        // 正常折旧
                        totalXsf = totalXsf + yzje;
                        // 开始折旧
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("assetsID", xsAssets.getAssetsID());
                        param.put("yzje", yzje);
                        param.put("asExpectedPeriod", xsAssets.getAsExpectedPeriod());
                        // 固定资产主键
                        param.put("assetsID", xsAssets.getAssetsID());
                        // 月折旧额
                        param.put("yzje", yzje);
                        assetsDao.updAssets(param);
                        ssyzje = yzje;
                    } else {
                        // 该期预完全折完
                        if (sjwc == 0) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", xsAssets.getAssetsID());
                            totalXsf = totalXsf + yzje;
                            assetsDao.updAssets1(param);
                            ssyzje = yzje;
                        } else {
                            // 该期预彻底折完 （但是有少量残余需特殊处理）
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", xsAssets.getAssetsID());
                            totalXsf = totalXsf + (asvalue - asAddDeprecia);
                            assetsDao.updAssets1(param);
                            ssyzje = asvalue - asAddDeprecia;
                        }
                    }
                    if (ssyzje != 0) {
                        // 折旧记录在案
                        AssetsRecord assetsRecord = new AssetsRecord();
                        assetsRecord.setZjid(UUIDUtils.getUUID());
                        assetsRecord.setAssetsID(xsAssets.getAssetsID());
                        assetsRecord.setSsyzje(ssyzje);
                        assetsRecord.setPeriod(busDate);
                        assetsRecord.setAccountID(account.getAccountID());
                        assetsRecordDao.insertAssetsRecord(assetsRecord);
                    }

                }

            }
            // 折旧到制造费用
            if (scList.size() > 0) {
                for (Assets scAssets : scList) {
                    double ssyzje = 0;
                    // 原值
                    double asvalue = scAssets.getAsvalue();
                    // 累计折旧
                    double asAddDeprecia = scAssets.getAsAddDeprecia() == null ? 0 : scAssets.getAsAddDeprecia();
                    //// 预计净残值
                    double yjjcz = scAssets.getAsNetSalvage() == null ? 0 : scAssets.getAsNetSalvage();
                    // 预计剩余折旧期间数(工作总量)
                    double asExpectedPeriod = scAssets.getAsExpectedPeriod();
                    // 预计净残值率=预计残值/原值
                    double yjjczl = yjjcz / asvalue;
                    // 月折旧率
                    double yzjl = (1 - yjjczl) / scAssets.getAsDepreciaPeriod();
                    // 月折旧额
                    double yzje = yzjl * asvalue;
                    BigDecimal b = new BigDecimal(yzje);
                    yzje = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    // 定义误差 (月折旧额有除不尽的问题)
                    double sjwc = Math.abs(asvalue - asAddDeprecia - yzje - yjjcz) / asvalue;
                    if (Math.abs(asAddDeprecia + yjjcz - asvalue) < 0.01) {
                        continue;
                    }
                    if (sjwc > wclz) {
                        // 正常折旧
                        totalZzf = totalZzf + yzje;
                        // 开始折旧
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", account.getAccountID());
                        param.put("assetsID", scAssets.getAssetsID());
                        param.put("yzje", yzje);
                        param.put("asExpectedPeriod", scAssets.getAsExpectedPeriod());
                        // 固定资产主键
                        param.put("assetsID", scAssets.getAssetsID());
                        // 月折旧额
                        param.put("yzje", yzje);
                        assetsDao.updAssets(param);
                        ssyzje = yzje;
                    } else {
                        // 该期预完全折完
                        if (sjwc == 0) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", scAssets.getAssetsID());
                            totalZzf = totalZzf + yzje;
                            assetsDao.updAssets1(param);
                            ssyzje = yzje;
                        } else {
                            // 该期预彻底折完 （但是有少量残余需特殊处理）
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("accountID", account.getAccountID());
                            param.put("assetsID", scAssets.getAssetsID());
                            totalZzf = totalZzf + (asvalue - asAddDeprecia);
                            assetsDao.updAssets1(param);
                            ssyzje = asvalue - asAddDeprecia;
                        }
                    }
                    if (ssyzje != 0) {
                        // 折旧记录在案
                        AssetsRecord assetsRecord = new AssetsRecord();
                        assetsRecord.setZjid(UUIDUtils.getUUID());
                        assetsRecord.setAssetsID(scAssets.getAssetsID());
                        assetsRecord.setSsyzje(ssyzje);
                        assetsRecord.setPeriod(busDate);
                        assetsRecord.setAccountID(account.getAccountID());
                        assetsRecordDao.insertAssetsRecord(assetsRecord);
                    }

                }

            }
            // 构造凭证头
            VoucherHead vouchHead = new VoucherHead();
            vouchHead.setAccountID(account.getAccountID());
            String vouchID = UUIDUtils.getUUID();
            vouchHead.setVouchID(vouchID);
            vouchHead.setVcDate(new Date());
            vouchHead.setAccountID(account.getAccountID());
            vouchHead.setSource(2);
            vouchHead.setTotalCredit(totalGlf + totalXsf + totalZzf);
            vouchHead.setTotalDbit(totalGlf + totalXsf + totalZzf);
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(querymap); // 凭证号
            vouchHead.setVoucherNO(maxVoucherNo);
            vouchHead.setCreateDate(System.currentTimeMillis());
            vouchHead.setCreatepsn(user.getUserName());
            vouchHead.setCreatePsnID(user.getUserID());
            vouchHead.setPeriod(busDate);
            VoucherBody vouchBody1 = null;
            VoucherBody vouchBody2 = null;
            VoucherBody vouchBody3 = null;
            VoucherBody vouchBody4 = null;
            boolean flag = false;
            // String subName = "折旧费";
            String subName = "折旧";
            if (totalGlf > 0) {
                // 构造凭证分录1
                vouchBody1 = new VoucherBody();
                vouchBody1.setVouchAID(UUIDUtils.getUUID());
                vouchBody1.setVouchID(vouchID);
                vouchBody1.setAccountID(account.getAccountID());
                vouchBody1.setPeriod(busDate);
                // 科目名称
                Map<String, Object> zjParam = new HashMap<String, Object>();

                zjParam.put("subCode", "6602");// 管理费用折旧费
                zjParam.put("subName", subName);
                zjParam.put("accountID", account.getAccountID());
                zjParam.put("period", busDate);
                zjParam.put("busDate", busDate);
                List<TBasicSubjectMessage> zjList = tBasicSubjectMessageMapper.querySubject(zjParam);

                if (zjList != null && zjList.size() == 1) {
                    vouchBody1.setVcsubject(zjList.get(0).getFullName());
                    vouchBody1.setSubjectID(zjList.get(0).getSubCode());
                    vouchBody1.setVcabstact("计提折旧(管理费用折旧)");
                } else {
                    vouchBody1.setVcsubject("");
                    vouchBody1.setSubjectID("");
                    vouchBody1.setVcabstact("计提折旧(管理费用折旧)");
                }
                vouchBody1.setDebitAmount(totalGlf);
                vouchBody1.setDirection("1");
                vouchBody1.setVouchAID(UUIDUtils.getUUID());

                flag = true;

            }
            if (totalXsf > 0) {
                // 构造凭证分录2
                vouchBody2 = new VoucherBody();
                vouchBody2.setVouchAID(UUIDUtils.getUUID());
                vouchBody2.setVouchID(vouchID);
                vouchBody2.setAccountID(account.getAccountID());
                vouchBody2.setPeriod(busDate);
                // 科目名称

                Map<String, Object> zjParam = new HashMap<String, Object>();

                zjParam.put("subCode", "6601");// 销售费用折旧费
                zjParam.put("subName", subName);
                zjParam.put("accountID", account.getAccountID());
                zjParam.put("period", busDate);
                zjParam.put("busDate", busDate);
                List<TBasicSubjectMessage> zjList = tBasicSubjectMessageMapper.querySubject(zjParam);
                if (zjList != null && zjList.size() == 1) {
                    vouchBody2.setVcsubject(zjList.get(0).getFullName());
                    vouchBody2.setSubjectID(zjList.get(0).getSubCode());
                    vouchBody2.setVcabstact("计提折旧(销售费用折旧)");
                } else {
                    vouchBody2.setVcsubject("");
                    vouchBody2.setSubjectID("");
                    vouchBody2.setVcabstact("计提折旧(销售费用折旧)");
                }
                vouchBody2.setDebitAmount(totalXsf);
                vouchBody2.setDirection("1");

                vouchBody2.setVouchAID(UUIDUtils.getUUID());
                if (!flag) {
                    vouchBody2.setVcabstact("计提折旧(销售费用折旧)");
                }

                flag = true;
            }
            if (totalZzf > 0) {
                // 构造凭证分录3
                vouchBody3 = new VoucherBody();
                vouchBody3.setVouchAID(UUIDUtils.getUUID());
                vouchBody3.setVouchID(vouchID);
                vouchBody3.setAccountID(account.getAccountID());
                vouchBody3.setPeriod(busDate);
                // 科目名称
                Map<String, Object> zjParam = new HashMap<String, Object>();

                zjParam.put("subCode", "5101");// 制造费用折旧费
                zjParam.put("subName", subName);
                zjParam.put("accountID", account.getAccountID());
                zjParam.put("period", busDate);
                zjParam.put("busDate", busDate);
                List<TBasicSubjectMessage> zjList = tBasicSubjectMessageMapper.querySubject(zjParam);
                if (zjList != null && zjList.size() == 1) {
                    vouchBody3.setVcsubject(zjList.get(0).getFullName());
                    vouchBody3.setSubjectID(zjList.get(0).getSubCode());
                    if (!flag) {
                        vouchBody3.setVcabstact("计提折旧(制造费用折旧)");
                    }
                } else {
                    vouchBody3.setVcsubject("");
                    vouchBody3.setSubjectID("");
                    if (!flag) {
                        vouchBody3.setVcabstact("计提折旧(制造费用折旧)");
                    }
                }

                vouchBody3.setDebitAmount(totalZzf);
                vouchBody3.setDirection("1");

                vouchBody3.setVouchAID(UUIDUtils.getUUID());

                flag = true;
            }
            if (flag) {
                Map<String, Object> sbParam = new HashMap<String, Object>();
                vouchBody4 = new VoucherBody();
                String sbName = "累计折旧";
                sbParam.put("subCode", "1602");
                sbParam.put("subName", sbName);
                sbParam.put("accountID", account.getAccountID());
                sbParam.put("period", busDate);
                sbParam.put("busDate", busDate);
                List<TBasicSubjectMessage> sbList = tBasicSubjectMessageMapper.querySysBankSubject(sbParam);
                List<TBasicSubjectMessage> mjSubList = SubjectUtils.getMjSub(sbList);
                if (mjSubList != null && mjSubList.size() == 1) {
                    vouchBody4.setVcsubject(mjSubList.get(0).getFullName());
                    vouchBody4.setSubjectID(mjSubList.get(0).getSubCode());
                } else {
                    vouchBody4.setVcsubject("");
                    vouchBody4.setSubjectID("");
                }

                vouchBody4.setVouchAID(UUIDUtils.getUUID());
                vouchBody4.setVouchID(vouchID);
                vouchBody4.setAccountID(account.getAccountID());
                vouchBody4.setRowIndex("2");

                vouchBody4.setCreditAmount(totalGlf + totalXsf + totalZzf);
                vouchBody4.setDirection("2");

                vouchBody4.setPeriod(busDate);
                voucherHeadDao.insertVouchHead(vouchHead);
            }
            int fl = 1;
            if (vouchBody1 != null) {
                vouchBody1.setRowIndex(fl + "");
                voucherBodyDao.insertVouchBody(vouchBody1);
                fl++;
            }
            if (vouchBody2 != null) {
                vouchBody2.setRowIndex(fl + "");
                voucherBodyDao.insertVouchBody(vouchBody2);
                fl++;
            }
            if (vouchBody3 != null) {
                vouchBody3.setRowIndex(fl + "");
                voucherBodyDao.insertVouchBody(vouchBody3);
                fl++;
            }
            if (vouchBody4 != null) {
                vouchBody4.setRowIndex(fl + "");
                voucherBodyDao.insertVouchBody(vouchBody4);
                fl++;
            }

            List<VoucherBody> list = new ArrayList<VoucherBody>();
            if (vouchBody1 != null) {
                list.add(vouchBody1);
            }
            if (vouchBody2 != null) {
                list.add(vouchBody2);
            }
            if (vouchBody3 != null) {
                list.add(vouchBody3);
            }
            if (vouchBody4 != null) {
                list.add(vouchBody4);
            }
            voucher.setVoucherHead(vouchHead);
            voucher.setVoucherBodyList(list);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);

            VoucherHead vh = voucher.getVoucherHead();
            if (null != vh && list.size() > 0) {
                boolean bool = vatService.checkVouch(param, voucher);
                if (bool) {
                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                }
            }
            return voucher;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    // 定资产添加2
    public Map<String, Object> addAssets(Map<String, Object> map) throws BusinessException {

        return null;
    }

    private Map<String, Object> comeBack(Object res, String flg) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", res);
        result.put("message", flg);
        return result;
    }

    // 查询所有科目树形结构
    @Override
    public List<SubjectMessage> queryAllSubTree(Map<String, Object> param) throws BusinessException {
        List<SubjectMessage> queryAllSub = assetsDao.queryAllSub(param);
        return queryAllSub;

    }

    // 查询所有科目
    @Override
    public Map<String, Object> queryAllSub(Map<String, Object> param) throws BusinessException {
        List<SubjectMessage> queryAllSub = assetsDao.queryAllSub(param);
        List<SubjectMessage> arr1 = new ArrayList<>();
        List<SubjectMessage> arr2 = new ArrayList<>();
        List<SubjectMessage> arr3 = new ArrayList<>();
        List<SubjectMessage> arr4 = new ArrayList<>();
        List<SubjectMessage> arr5 = new ArrayList<>();
        List<SubjectMessage> arr6 = new ArrayList<>();
        if (queryAllSub != null && queryAllSub.size() > 0) {
            for (SubjectMessage sub : queryAllSub) {
                if (sub == null)
                    continue;
                String code = sub.getSub_code().substring(0, 1);
                switch (code) {
                    case "1":
                        arr1.add(sub);
                        break;
                    case "2":
                        arr2.add(sub);
                        break;
                    case "3":
                        arr3.add(sub);
                        break;
                    case "4":
                        arr4.add(sub);
                        break;
                    case "5":
                        arr5.add(sub);
                        break;
                    case "6":
                        arr6.add(sub);
                        break;
                    default:
                        arr1.add(sub);
                        break;
                }
            }
        }

        Map map1 = new TreeMap<String, String>(new MapKeyComparator());
        Map map2 = new TreeMap<String, String>(new MapKeyComparator());
        Map map3 = new TreeMap<String, String>(new MapKeyComparator());
        Map map4 = new TreeMap<String, String>(new MapKeyComparator());
        Map map5 = new TreeMap<String, String>(new MapKeyComparator());
        Map map6 = new TreeMap<String, String>(new MapKeyComparator());
        Map<String, Object> reval = new HashMap<>();
        setReval(arr1, reval, map1, "code1");
        setReval(arr2, reval, map2, "code2");
        setReval(arr3, reval, map3, "code3");
        setReval(arr4, reval, map4, "code4");
        setReval(arr5, reval, map5, "code5");
        setReval(arr6, reval, map6, "code6");
        /*
         * Map map6 = new TreeMap<String,String>( new Comparator<String>(){
         *
         * @Override public int compare(String paramT1, String paramT2) { return
         * paramT1.compareTo(paramT2); } } );
         */
        return reval;
    }

    private void setReval(List<SubjectMessage> arr, Map<String, Object> reval, Map map, String code) {
        if (arr != null && arr.size() > 0) {
            SubjectMessage sub = null;
            for (int i = 0; i < arr.size(); i++) {
                sub = arr.get(i);
                map.put(sub.getSub_code(), sub.getSub_name());
            }
            reval.put(code, map);
        }
    }

    // 查询总数
    // @Transactional(rollbackFor = BusinessException.class)
    public Integer queryCount(Map<String, Object> map) throws BusinessException {
        try {
            int num = assetsDao.queryCount(map);
            if (num < 0) {
                throw new BusinessException("查询异常");
            }
            return num;
        } catch (BusinessException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    // 再次导入删除全部数据
    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    public int delAllAss(Map<String, Object> map) throws BusinessException {
        try {
            int num = assetsDao.delAllAss(map);
            if (num < 0) {
                throw new BusinessException("查询异常");
            }
            return num;
        } catch (BusinessException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    // 固定资产详情查看
    @Override
    public Assets queryAssById(Map<String, Object> param) throws BusinessException {
        return assetsDao.queryAssById(param);
    }

    // 检查固定资产编码asCode资产编码 asName资产名称
    @Override
    public Assets checkSub(Map<String, String> map) throws BusinessException {

        return assetsDao.checkSub(map);

    }

    public void init(Map<String, String> map) {
        vatService.subinit(map.get("accountID"), map.get("period"), map.get("userID"), map.get("userName"));
    }

    // 测试
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void deladd() throws BusinessException {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("pk_sub_id", "88888888");
            map.put("user_id", "101");
            map.put("account_id", "901");
            assetsDao.add1(map);
            int aa = 5 / 0;
            deladd("nih");
            System.out.println("add................");

            assetsDao.del1("c70b106fa72042618f0a3900110dcf09");
            int k = 5 / 0;
            System.out.println("del...................");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // //手动回滚
        }
    }

    // 测试
    // @Transactional
    @Override
    public void deladd(String id) throws BusinessException {

        /*
         * Map<String, String> mp = new HashMap<>(); mp.put("pk_sub_id",
         * "999999999"); mp.put("user_id", "101"); mp.put("account_id", "901");
         * assetsDao.add1(mp); System.out.println(00000000000);
         */

        // update user set price = price + #{amount} where id = #{id}
        Map<String, Object> map = new HashMap<>();
        BigDecimal amount = new BigDecimal("10");
        map.put("amount", amount);
        map.put("id", id);
        assetsDao.updateUserTest(map);
        System.out.println("user..........");

    }

}

// 比较器类
class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
