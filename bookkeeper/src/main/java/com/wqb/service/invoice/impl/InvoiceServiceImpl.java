package com.wqb.service.invoice.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.invoice.InvoiceMappingDao;
import com.wqb.dao.subject.SubjectDao;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.InvoiceMappingVo;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.invoice.InvoiceService;
import com.wqb.service.stateTrack.StateTrackService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

@Component
@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    VatService vatService;
    @Autowired
    StateTrackService stateTrackService;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;
    @Autowired
    KcCommodityDao commodityDao;
    @Autowired
    InvoiceMappingDao invoiceMappingDao;
    @Autowired
    TBasicSubjectMappingMiddleMapper subjectMiddleMapping;
    @Autowired
    AssetsService assetsService;

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    // 进项导入
    public List<Map<String, Object>> jxInvoice2Data(List<Map<String, Object>> list, HttpSession session)
            throws BusinessException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            Integer companyType = account.getCompanyType();
            String busDate = sessionMap.get("busDate").toString();
            String buyTaxno = list.get(1).get("map1").toString();
            Map<String, InvoiceBody> invoBodyMap = new HashMap<>();

            List<Map<String, Object>> mappingList = new ArrayList<>();
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            Object rzrqObj = list.get(1).get("map4");
            if (null != rzrqObj) {
                String rzrq = rzrqObj.toString().trim();
                if (!rzrq.equals(busDate.replaceAll("-", ""))) {
                    throw new BusinessException("请仔细检查发票认证月份和当前会计期间是否匹配!");
                }
            }
            // 校验EXCEL数据本身是否重复
            for (int i = 3; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (!StringUtil.objEmpty(list.get(i).get("map2"))) {
                        if (list.get(i).get("map2").toString().equals(list.get(j).get("map2").toString())) {
                            String ss = "第" + (i - 3 + 1) + "行数据与第" + (j - 3 + 1) + "行数据(发票号码)重复,请仔细核查!";
                            // throw new BusinessException(ss);
                        }
                    }
                }
            }

            // 校验是否与数据库数据冲突
            for (int k = 3; k < list.size(); k++) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("invoiceType", "1");
                param.put("accountID", account.getAccountID());
                param.put("invoiceNumber", list.get(k).get("map2") == null ? null : list.get(k).get("map2").toString());
                param.put("busDate", busDate);
                List<InvoiceHead> temp = invoiceDao.querySame(param);
                if (null != temp && temp.size() > 0) {
                    // throw new BusinessException("已存在重复数据,不允许导入");
                }
            }

            int type = 2;
            Set<Entry<String, Object>> entrySet = list.get(2).entrySet();
            for (Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                if (value != null) {
                    boolean a1 = value.toString().trim().replace(" ", "").contains("认证方式");
                    boolean a2 = value.toString().trim().replace(" ", "").contains("认证日期");
                    if (a1 || a2) {
                        type = 1;
                        break;
                    }
                }
            }

            // 进项发票从第三行开始读
            for (int i = 3; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);

                String invoiceCode = null;
                String invoiceNumber = null;
                String invoiceDate = null;
                String saleTaxno = null;
                String saleCorp = null;
                String amount = null;
                String taxAmount = null;
                String nnumber = null;
                String price = null;
                String sureType = null;
                String invoice_confirmdateStr = null;
                String invoiceName = null;
                String invoiceState = null;
                String comName = null;
                String measure = null;

                if (type == 1) {

                    invoiceCode = map.get("map1") == null ? null : map.get("map1").toString();// 发票代码
                    invoiceNumber = map.get("map2") == null ? null : map.get("map2").toString();// 发票号码
                    invoiceDate = map.get("map3") == null ? null : map.get("map3").toString();// 开票日期
                    saleTaxno = map.get("map4") == null ? null : map.get("map4").toString();// 销方税号

                    if (StringUtil.isEmpty(invoiceCode)) {
                        break;
                    }
                    saleCorp = StringUtil.getInvoiceStr(map.get("map5"));// 销方名称

                    amount = StringUtil.getInvoiceStr(map.get("map6"));// 金额

                    taxAmount = StringUtil.getInvoiceStr(map.get("map7"));// 税额

                    sureType = StringUtil.getInvoiceStr(map.get("map8"));// 认证方式

                    // 导入的时候要修改确认日期为当前期间 进项统计税额以确认日期为主
                    invoice_confirmdateStr = StringUtil.getInvoiceStr(map.get("map9"));// 确认/认证日期

                    invoiceName = StringUtil.getInvoiceStr(map.get("map10"));// 发票类型
                    invoiceState = StringUtil.getInvoiceStr(map.get("map11"));// 发票状态

                    // map12 map13 map14 map15 这些数据原来是没有的，用户可以自己添加
                    comName = StringUtil.getInvoiceStr2(map.get("map12"));// 商品名称
                    nnumber = StringUtil.getInvoiceStr(map.get("map13"));// 数量
                    measure = StringUtil.getInvoiceStr(map.get("map14"));// 单位
                    price = StringUtil.getInvoiceStr(map.get("map15"));// 单价

                } else {

                    invoiceCode = map.get("map1") == null ? null : map.get("map1").toString();// 发票代码
                    invoiceNumber = map.get("map2") == null ? null : map.get("map2").toString();// 发票号码
                    invoiceDate = map.get("map3") == null ? null : map.get("map3").toString();// 开票日期
                    saleTaxno = null;

                    if (StringUtil.isEmpty(invoiceCode)) {
                        break;
                    }
                    saleCorp = StringUtil.getInvoiceStr(map.get("map4"));// 销方名称

                    amount = StringUtil.getInvoiceStr(map.get("map5"));// 金额

                    taxAmount = StringUtil.getInvoiceStr(map.get("map6"));// 税额

                    invoiceState = StringUtil.getInvoiceStr(map.get("map7"));// 发票状态

                    invoiceName = StringUtil.getInvoiceStr(map.get("map8"));// 发票类型

                    // map9 map10 map11 map12 这些数据原来是没有的，用户可以自己添加
                    comName = StringUtil.getInvoiceStr2(map.get("map9"));// 商品名称
                    nnumber = StringUtil.getInvoiceStr(map.get("map10"));// 数量
                    measure = StringUtil.getInvoiceStr(map.get("map11"));// 单位
                    price = StringUtil.getInvoiceStr(map.get("map12"));// 单价

                    sureType = null; // 认证方式
                    invoice_confirmdateStr = null; // 确认/认证日期
                }

                if ("小计".equals(comName)) {
                    continue;
                }

                // 构造参数做入库操作
                InvoiceHead invoiceHead = new InvoiceHead();
                String invoiceHid = UUIDUtils.getUUID();
                invoiceHead.setInvoiceHID(invoiceHid);// 主表主键

                invoiceHead.setPeriod(sdf.parse(busDate));
                invoiceHead.setInvoiceType("1");
                invoiceHead.setInvoiceCode(invoiceCode);
                invoiceHead.setInvoiceNumber(invoiceNumber);
                invoiceHead.setInvoiceName(invoiceName);
                invoiceHead.setInvoiceState(invoiceState);
                invoiceHead.setInvoiceDate(DateUtil.fomatDate(invoiceDate));
                invoiceHead.setSaleCorp(saleCorp);
                // 制票人
                invoiceHead.setInvoicePerson("");
                invoiceHead.setBuyTaxno(buyTaxno);
                invoiceHead.setBuyCorp(user.getCorp());
                invoiceHead.setSaleTaxno(saleTaxno);
                // 销方账户
                invoiceHead.setSaleBankno("");
                // 凭证生成主键
                invoiceHead.setVouchID("");
                // 审核人
                invoiceHead.setAuditPsn("");
                // 附件
                invoiceHead.setFj("");
                // 发票说明
                invoiceHead.setInvoiceDes("");
                // 修改人ID
                invoiceHead.setUpdatePsnID("");
                // 修改人
                invoiceHead.setUpdatePsn("");
                // 修改时间
                invoiceHead.setUpdatedate(null);
                // 创建人ID
                invoiceHead.setCreatePsnID(user.getUserID());
                // 创建人
                invoiceHead.setCreatepsn(user.getUserName());
                // 归属账套(外键)
                invoiceHead.setAccountID(account.getAccountID());
                // 用户(外键)
                invoiceHead.setUserID(user.getUserID());
                // 确认日期
                if (null != invoice_confirmdateStr) {
                    invoiceHead.setInvoice_confirmdate(DateUtil.fomatDate(invoice_confirmdateStr));
                }
                // 认证方式
                invoiceHead.setSureType(sureType);
                invoiceHead.setImportDate(System.currentTimeMillis());
                // 构造发票字表
                InvoiceBody invoiceBody = new InvoiceBody();
                invoiceBody.setInvoiceBID(UUIDUtils.getUUID());// 主键
                invoiceBody.setInvoiceHID(invoiceHid);// 发票主表主键(外键)

                // 进项发票没有规格内容 暂时设置为null
                String spec = null;

                invoiceBody.setComName(comName);// 商品名称
                invoiceBody.setSpec(spec);
                String comNameSpec = ""; // 名称规格
                if (!StringUtil.isEmpty(comName) && !StringUtil.isEmpty(spec)) {
                    comNameSpec = comName + "-" + spec;
                    // 可控硅调光电源-100W,24V 拼名称与规格
                } else {
                    comNameSpec = comName;
                }
                invoiceBody.setComNameSpec(comNameSpec); // 关联库存表

                invoiceBody.setMeasureID("");// 计量单位ID(外键)
                invoiceBody.setMeasure(measure);// 计量单位
                if (null != price) {
                    invoiceBody.setNprice(Double.parseDouble(price));// 单价
                }

                // 价税合计
                if (amount == null && taxAmount == null) {
                    invoiceBody.setNtaxAmount(null);
                } else {
                    invoiceBody.setNtaxAmount(StringUtil.objToDoubleIsNuLL(amount) + StringUtil.objToDoubleIsNuLL(taxAmount));
                }

                invoiceBody.setNnumber(nnumber == null ? null : Double.parseDouble(nnumber));
                invoiceBody.setNamount(amount == null ? null : Double.parseDouble(amount));
                invoiceBody.setTaxAmount(taxAmount == null ? null : Double.parseDouble(taxAmount));

                invoiceBody.setTaxClass("");// 税种类别
                invoiceBody.setCreatePsnID(user.getUserID());// 创建人ID
                invoiceBody.setCreatePsn(user.getUserName());// 创建人
                invoiceBody.setUserID(user.getUserID());// 用户
                invoiceBody.setAccountID(account.getAccountID());
                invoiceBody.setDes("进项导入");// 备注
                invoiceBody.setInvoiceType("1");
                invoiceBody.setPeriod(busDate);

                // 改变 导入发票不予数量金额表发生关系。商品入库只与凭证挂钩
                if (comNameSpec != null) {
                    // statisticsGoods(invoBodyMap, amount, nnumber, comName,
                    // spec, comNameSpec);
                }

                invoiceDao.insertInvocieH(invoiceHead);
                invoiceDao.insertInvoiceB(invoiceBody);

                getInvoiceMapping(invoiceHead, invoiceBody, mappingList);

            }

            // 把导进来的进项商品添加到库存商品
            if (!invoBodyMap.isEmpty()) {
                // vatService.updateKcCommodity21(invoBodyMap);
            }
            if (!mappingList.isEmpty()) {
                mappingSubcode(account, busDate, mappingList);
            }

            return mappingList;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        // return "success";
    }

    // 映射科目
    private void mappingSubcode(Account acc, String busDate, List<Map<String, Object>> mappingList)
            throws BusinessException {
        // 1 查询数量金额表的所有科目
        // 2根据每一条发票的商品名称进行匹配，发票里面的商品名称与数量金额表的名称一样的则科目就算匹配到
        Map<String, Object> map = new HashMap<>();
        map.put("period", busDate);
        map.put("accountID", acc.getAccountID());

        List<KcCommodity> list = commodityDao.queryCommodityAll2(map);
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> maps : mappingList) {
                List<InvoiceMappingVo> mappList = (List<InvoiceMappingVo>) maps.get("date");
                for (int i = 0; i < mappList.size(); i++) {
                    InvoiceMappingVo vo = mappList.get(i);
                    String comNameSpec1 = vo.getComNameSpec();
                    if (StringUtil.isEmpty(comNameSpec1)) {
                        continue;
                    }
                    for (KcCommodity comm : list) {
                        String comNameSpec2 = comm.getComNameSpec();
                        String sub_code = comm.getSub_code();
                        String sub_comName = comm.getSub_comName();
                        if (!StringUtil.isEmpty(comNameSpec1) && !StringUtil.isEmpty(comNameSpec2)) {
                            if (comNameSpec1.equals(comNameSpec2)) {
                                vo.setMappingSubCode(sub_code);
                                vo.setMappingSubName(sub_comName);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    private void getInvoiceMapping(InvoiceHead invoiceHead, InvoiceBody invoiceBody,
                                   List<Map<String, Object>> mappingList2) throws BusinessException {

        InvoiceMappingVo inovice = new InvoiceMappingVo();

        // inovice.setAccountID(invoiceBody.getAccountID());
        // inovice.setPeriod(invoiceBody.getPeriod());

        inovice.setInvoiceCode(invoiceHead.getInvoiceCode());// 发票编码
        inovice.setInvoiceNumber(invoiceHead.getInvoiceNumber());// 发票号码
        // inovice.setInvoiceName(invoiceHead.getInvoiceName());// 发票名称
        inovice.setInvoiceDate(invoiceHead.getInvoiceDate());// 发票日期

        inovice.setImportDate(invoiceHead.getImportDate());

        // inovice.setBuyTaxno(invoiceHead.getBuyTaxno());// 购方税号
        inovice.setBuyCorp(invoiceHead.getBuyCorp());// 购方公司名称
        // inovice.setBuyBankno(invoiceHead.getBuyBankno());// 购方账户

        inovice.setSaleCorp(invoiceHead.getSaleCorp());// 销方公司名称
        // inovice.setSaleTaxno(invoiceHead.getSaleTaxno());// 销方税号
        // inovice.setSaleBankno(invoiceHead.getSaleBankno());// 销方账户

        // inovice.setAddressPhone(invoiceHead.getAddressPhone());// 地址电话
        // inovice.setProductVersion(invoiceHead.getProductVersion());// 商品编码版本号

        inovice.setInvoiceHID(invoiceBody.getInvoiceHID()); // 发票主表主键
        inovice.setInvoiceBID(invoiceBody.getInvoiceBID()); // 发票表子建

        inovice.setComName(invoiceBody.getComName());// 商品名称
        inovice.setSpec(invoiceBody.getSpec()); // 规格
        inovice.setComNameSpec(invoiceBody.getComNameSpec()); // 商品名称-规格

        inovice.setMeasure(invoiceBody.getMeasure()); // 计量单位
        inovice.setNprice(invoiceBody.getNprice()); // 单价
        inovice.setNnumber(invoiceBody.getNnumber()); // 数量
        inovice.setNamount(invoiceBody.getNamount()); // 金额
        inovice.setTaxRate(invoiceBody.getTaxRate());// 税率
        inovice.setTaxAmount(invoiceBody.getTaxAmount());// 价税合计

        // inovice.setTaxTypeCode(invoiceBody.getTaxTypeCode());// 税收分类编码

        inovice.setInvoiceType(invoiceBody.getInvoiceType()); // 发票类型

        // inovice.setSourt(mappingList.size()+1); //排序
        String invoiceHID = invoiceBody.getInvoiceHID();
        String invoiceCode = invoiceHead.getInvoiceCode();// 发票编码
        String invoiceNumber = invoiceHead.getInvoiceNumber();// 发票号码
        long importDate = invoiceHead.getImportDate();
        boolean aa = false;
        if (!mappingList2.isEmpty()) {
            for (Map<String, Object> map : mappingList2) {
                String invoiceHID2 = (String) map.get("invoiceHID");
                if (invoiceHID2.equals(invoiceHID)) {
                    String invoiceNumber2 = map.get("invoiceNumber").toString();
                    String invoiceCode2 = map.get("invoiceCode").toString();
                    if (!invoiceCode.equals(invoiceCode2)) {
                        throw new BusinessException("getInvoiceMapping 导入发票编码不一致");
                    }
                    if (!invoiceNumber.equals(invoiceNumber2)) {
                        throw new BusinessException("getInvoiceMapping 导入发票号码不一致");
                    }

                    List<InvoiceMappingVo> list = (List<InvoiceMappingVo>) map.get("date");
                    inovice.setSourt(list.size() + 1);
                    list.add(inovice);
                    aa = true;
                    break;
                }
            }
        }

        if (aa == false) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("invoiceHID", invoiceHID);
            hashMap.put("invoiceCode", invoiceHead.getInvoiceCode());
            hashMap.put("invoiceNumber", invoiceHead.getInvoiceNumber());
            hashMap.put("importDate", invoiceHead.getImportDate());
            List<InvoiceMappingVo> list = new ArrayList<>();
            list.add(inovice);
            hashMap.put("date", list);
            mappingList2.add(hashMap);
        }

    }

    // 销项导入
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public List<Map<String, Object>> xxInvoice2Data(List<Map<String, Object>> list, HttpSession session)
            throws BusinessException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            String busDate = sessionMap.get("busDate").toString();
            Map<String, InvoiceBody> invoBodyMap = new HashMap<>();
            List<Map<String, Object>> mappingList = new ArrayList<>();
            vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
            // 校验是否与数据库数据冲突
            for (int k = 1; k < list.size(); k++) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("busDate", busDate);
                param.put("invoiceType", "2");
                param.put("accountID", account.getAccountID());

                Map<String, Object> map = list.get(k);
                if ("发票代码".equals(map.get("map0")) || map.get("map0").toString().contains("资料区间")
                        || map.get("map0").toString().contains("发票类别")) {
                    continue;
                }
                /*
                 * param.put("invoiceNumber", list.get(k).get("map1") == null ?
                 * null : list.get(k).get("map1").toString()); List<InvoiceHead>
                 * temp = invoiceDao.querySame(param); if (null != temp &&
                 * temp.size() > 0) { return "已存在重复数据,不允许导入"; throw new
                 * BusinessException("已存在重复数据,不允许导入"); }
                 */
            }

            for (int i = 1; i < list.size(); i++) {

                if (i == 24) {
                    System.out.println();
                }

                String invoiceHID = UUIDUtils.getUUID();

                Map<String, Object> map = list.get(i);
                if ("发票代码".equals(map.get("map0")) || map.get("map0").toString().contains("资料区间")
                        || map.get("map0").toString().contains("发票类别")) {
                    continue;
                }
                // 发票代码
                String invoiceCode = map.get("map0") == null ? null : map.get("map0").toString();
                // 税收分类编码//发票状态
                String ssflbm = map.get("map17") == null ? null : map.get("map17").toString();

                if (invoiceCode.contains("份数")) {
                    break;
                }
                if (ssflbm != null && ssflbm.contains("作废")) {
                    continue;
                }
                // 发票号码
                String invoiceNumber = null;
                // 购方企业名称
                String buyCorp = null;
                // 购方税号
                String buyTaxno = null;
                // 银行账号
                String buyBankno = null;
                // 地址电话
                String addressPhone = null;
                // 开票日期
                String invoiceDate = null;
                // 商品编码版本号
                String productVersion = null;
                // 单据号
                String billNo = null;
                // 上一个不为空的对象
                Map<String, Object> tempMap = null;
                if (StringUtil.isEmpty(invoiceCode)) {

                    // 倒着追索到最近的上一个不为空的记录
                    for (int j = i - 1; j > 0; j--) {
                        tempMap = list.get(j);
                        Object obj1 = tempMap.get("map0");
                        Object obj2 = tempMap.get("map1");

                        if (StringUtil.objToStr(obj1) == null || StringUtil.objToStr(obj2) == null) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    //BUG 最后一行未检测到是空的状态
                    //改进 继续检查 【map9 商品名称，map12数量，map14 金额，map15 税率，map16 税额】
                    //map9 如果有值 不能为小计
                    int n = 0;
                    if (!StringUtil.objEmpty(map.get("map9"))) {
                        ++n;
                    }
                    if (!StringUtil.objEmpty(map.get("map12"))) {
                        ++n;
                    }
                    if (!StringUtil.objEmpty(map.get("map14"))) {
                        ++n;
                    }
                    if (!StringUtil.objEmpty(map.get("map15"))) {
                        ++n;
                    }
                    if (!StringUtil.objEmpty(map.get("map16"))) {
                        ++n;
                    }

                    //n 假设最少有两个项目有值就说明这一行数据不是空行
                    if (n <= 2) {
                        continue;
                    }

                    // 发票号码
                    invoiceCode = tempMap.get("map0") == null ? null : tempMap.get("map0").toString();
                    // 发票号码
                    invoiceNumber = tempMap.get("map1") == null ? null : tempMap.get("map1").toString();
                    // 购方企业名称
                    buyCorp = tempMap.get("map2") == null ? null : tempMap.get("map2").toString();
                    // 购方税号
                    buyTaxno = tempMap.get("map3") == null ? null : tempMap.get("map3").toString();
                    // 银行账号
                    buyBankno = tempMap.get("map4") == null ? null : tempMap.get("map4").toString();
                    // 地址电话
                    addressPhone = tempMap.get("map5") == null ? null : tempMap.get("map5").toString();
                    // 开票日期 发票日期
                    invoiceDate = tempMap.get("map6") == null ? null : tempMap.get("map6").toString();
                    // 商品编码版本号
                    productVersion = tempMap.get("map7") == null ? null : tempMap.get("map7").toString();
                    // 单据号
                    billNo = tempMap.get("map8") == null ? null : tempMap.get("map8").toString();

                    // 商品名称
                    String comName = StringUtil.getInvoiceStr2(map.get("map9"));
                    if ("小计".equals(comName)) {
                        continue;
                    }
                    // 规格
                    String spec = StringUtil.getInvoiceStr2(map.get("map10"));
                    // 单位
                    String measure = StringUtil.getInvoiceStr2(map.get("map11"));
                    // 税率
                    String taxRate = StringUtil.getInvoiceStr2(map.get("map15"));

                    // 数量
                    String nnumber = StringUtil.getInvoiceStr(map.get("map12"));

                    // 单价
                    String nprice = StringUtil.getInvoiceStr(map.get("map13"));
                    // 金额
                    String namount = StringUtil.getInvoiceStr(map.get("map14"));
                    // 税额
                    String taxAmount = StringUtil.getInvoiceStr(map.get("map16"));

                    if ((taxRate != null) && taxRate.contains("%")) {
                        taxRate = Double.parseDouble(taxRate.replaceAll("%", "")) / 100 + "";
                    }

                    // 税收分类编码
                    String taxTypeCode = StringUtil.getInvoiceStr(map.get("map17"));

                    // 构造发票字表
                    Object attribute = session.getAttribute("invoiceHID");
                    InvoiceBody invoiceBody = new InvoiceBody();
                    invoiceBody.setInvoiceBID(UUIDUtils.getUUID());

                    String obj = attribute == null ? null : attribute.toString();
                    if (StringUtil.isEmpty(obj)) {
                        invoiceBody.setInvoiceHID(invoiceHID);
                    } else {
                        invoiceBody.setInvoiceHID(attribute.toString());
                    }
                    invoiceBody.setComName(comName);
                    invoiceBody.setSpec(spec);

                    String comNameSpec = null; // 名称规格
                    if (!StringUtil.isEmpty(comName) && !StringUtil.isEmpty(invoiceBody.getSpec())) {
                        comNameSpec = comName + "-" + invoiceBody.getSpec(); // 可控硅调光电源-100W,24V
                    } else if (!StringUtil.isEmpty(comName)) {
                        comNameSpec = comName; // 名称规格
                    }
                    invoiceBody.setComNameSpec(comNameSpec);// 可控硅调光电源-100W,24V

                    invoiceBody.setMeasureID("");
                    invoiceBody.setMeasure(measure);
                    if (!StringUtil.isEmpty(nprice)) {
                        invoiceBody.setNprice(Double.parseDouble(nprice));
                    }
                    if (!StringUtil.isEmpty(nnumber)) {
                        invoiceBody.setNnumber(Double.parseDouble(nnumber));
                    }
                    if (!StringUtil.isEmpty(namount)) {
                        invoiceBody.setNamount(Double.parseDouble(namount));
                    }
                    if (!StringUtil.isEmpty(taxAmount)) {
                        invoiceBody.setTaxAmount(Double.parseDouble(taxAmount));
                    }

                    invoiceBody.setNtaxAmount(StringUtil.objToDoubleIsNuLL(namount) + StringUtil.objToDoubleIsNuLL(taxAmount));
                    if (namount == null && taxAmount == null) {
                        invoiceBody.setNtaxAmount(null);
                    }

                    invoiceBody.setTaxClass("");
                    invoiceBody.setCreatePsn(user.getUserName());
                    invoiceBody.setCreatePsnID(user.getUserID());
                    invoiceBody.setUserID(user.getUserID());
                    invoiceBody.setAccountID(account.getAccountID());
                    invoiceBody.setTaxTypeCode(taxTypeCode);
                    invoiceBody.setTaxRate(taxRate);
                    invoiceBody.setDes("销项发票导入");
                    invoiceBody.setInvoiceType("2");
                    invoiceBody.setPeriod(busDate);

                    if (comNameSpec != null) {
                        // statisticsGoods(invoBodyMap, namount, nnumber,
                        // comName, spec, comNameSpec);
                    }
                    invoiceDao.insertInvoiceB(invoiceBody);

                    InvoiceHead invoiceHead = new InvoiceHead();
                    invoiceHead.setBuyCorp(buyCorp);
                    invoiceHead.setBuyTaxno(buyTaxno);
                    invoiceHead.setInvoiceNumber(invoiceNumber);
                    invoiceHead.setInvoiceCode(invoiceCode);
                    invoiceHead.setInvoiceHID(invoiceBody.getInvoiceHID());
                    invoiceHead.setImportDate(System.currentTimeMillis());

                    getInvoiceMapping(invoiceHead, invoiceBody, mappingList);

                } else {
                    // 发票号码
                    invoiceNumber = map.get("map1") == null ? null : map.get("map1").toString();
                    // 购方企业名称
                    buyCorp = map.get("map2") == null ? null : map.get("map2").toString();
                    // 购方税号
                    buyTaxno = map.get("map3") == null ? null : map.get("map3").toString();
                    // 银行账号
                    buyBankno = map.get("map4") == null ? null : map.get("map4").toString();
                    // 地址电话
                    addressPhone = map.get("map5") == null ? null : map.get("map5").toString();
                    // 开票日期
                    invoiceDate = map.get("map6") == null ? null : map.get("map6").toString();
                    // 商品编码版本号
                    productVersion = map.get("map7") == null ? null : map.get("map7").toString();
                    // 单据号
                    billNo = map.get("map8") == null ? null : map.get("map8").toString();

                    // 商品名称
                    String comName = StringUtil.getInvoiceStr2(map.get("map9"));
                    if ("小计".equals(comName)) {
                        continue;
                    }
                    // 规格
                    String spec = StringUtil.getInvoiceStr(map.get("map10"));

                    // 单位
                    String measure = StringUtil.getInvoiceStr(map.get("map11"));
                    // 税率
                    String taxRate = StringUtil.getInvoiceStr2(map.get("map15"));

                    // 数量
                    String nnumber = StringUtil.getInvoiceStr2(map.get("map12"));
                    // 单价
                    String nprice = StringUtil.getInvoiceStr2(map.get("map13"));
                    // 金额
                    String namount = StringUtil.getInvoiceStr2(map.get("map14"));
                    // 税额
                    String taxAmount = StringUtil.getInvoiceStr2(map.get("map16"));

                    /*
                     * if(namount==null||nnumber==null){ throw new
                     * BusinessException("发票代码"+invoiceCode+"数量或金额不能为空,请仔细检查");
                     * }
                     */

                    if (taxRate != null && taxRate.contains("%")) {
                        taxRate = Double.parseDouble(taxRate.replaceAll("%", "")) / 100 + "";
                    } else {
                        taxRate = null;
                    }

                    // 税收分类编码
                    String taxTypeCode = map.get("map17") == null ? null : map.get("map17").toString();

                    // 构造数据做入库操作
                    InvoiceHead invoiceHead = new InvoiceHead();
                    invoiceHead.setInvoiceHID(invoiceHID);
                    if (null != invoiceDate && !"".equals(invoiceDate)) {
                        invoiceHead.setInvoiceDate(DateUtil.fomatDate(invoiceDate));
                    }
                    invoiceHead.setPeriod(sdf.parse(sessionMap.get("busDate").toString()));
                    invoiceHead.setInvoiceType("2");
                    invoiceHead.setInvoiceCode(invoiceCode);
                    invoiceHead.setInvoiceNumber(invoiceNumber);
                    invoiceHead.setInvoiceName("销项发票名称");
                    invoiceHead.setInvoiceState("正常");
                    invoiceHead.setInvoicePerson(user.getUserName());
                    invoiceHead.setBuyTaxno(buyTaxno);
                    invoiceHead.setBuyCorp(buyCorp);
                    invoiceHead.setBuyBankno(buyBankno);
                    invoiceHead.setAddress(addressPhone);
                    invoiceHead.setSaleCorp("");
                    invoiceHead.setSaleTaxno("");
                    invoiceHead.setSaleBankno("");
                    invoiceHead.setVouchID("");
                    invoiceHead.setAuditPsn("");
                    invoiceHead.setFj("");
                    invoiceHead.setInvoiceDes("");
                    invoiceHead.setCreatePsnID(user.getUserID());
                    invoiceHead.setCreatepsn(user.getUserName());
                    invoiceHead.setAccountID(account.getAccountID());
                    invoiceHead.setUserID(user.getUserID());
                    invoiceHead.setInvoice_confirmdate(null);
                    invoiceHead.setProductVersion(productVersion);
                    invoiceHead.setBillNo(billNo);
                    invoiceHead.setAddressPhone(addressPhone);
                    invoiceHead.setSureType("");
                    invoiceHead.setImportDate(System.currentTimeMillis());
                    // 构造发票字表
                    InvoiceBody invoiceBody = new InvoiceBody();
                    invoiceBody.setInvoiceBID(UUIDUtils.getUUID());
                    invoiceBody.setInvoiceHID(invoiceHID);
                    invoiceBody.setComName(comName);
                    invoiceBody.setSpec(spec);
                    String comNameSpec = null; // 名称规格
                    if (!StringUtil.isEmpty(comName) && !StringUtil.isEmpty(invoiceBody.getSpec())) {
                        comNameSpec = comName + "-" + invoiceBody.getSpec(); // 可控硅调光电源-100W,24V
                    } else if (!StringUtil.isEmpty(comName)) {
                        comNameSpec = comName; // 名称规格
                    }
                    invoiceBody.setComNameSpec(comNameSpec);// 名称规格
                    invoiceBody.setMeasureID("");
                    invoiceBody.setMeasure(measure);
                    if (null != nprice && !"".equals(nprice)) {
                        invoiceBody.setNprice(Double.parseDouble(nprice));
                    }
                    if (null != nnumber && !"".equals(nnumber)) {
                        invoiceBody.setNnumber(Double.parseDouble(nnumber));
                    }
                    if (null != namount && !"".equals(namount)) {
                        invoiceBody.setNamount(Double.parseDouble(namount));
                    }
                    if (null != taxAmount && !"".equals(taxAmount)) {
                        invoiceBody.setTaxAmount(Double.parseDouble(taxAmount));
                    }

                    if (namount == null && taxAmount == null) {
                        invoiceBody.setNtaxAmount(null);
                    } else {
                        double total_tax_mount = StringUtil.objToDoubleIsNuLL(namount)
                                + StringUtil.objToDoubleIsNuLL(taxAmount);
                        invoiceBody.setNtaxAmount(total_tax_mount);
                    }

                    invoiceBody.setTaxClass("");
                    invoiceBody.setCreatePsn(user.getUserName());
                    invoiceBody.setCreatePsnID(user.getUserID());
                    invoiceBody.setUserID(user.getUserID());
                    invoiceBody.setAccountID(account.getAccountID());
                    invoiceBody.setTaxTypeCode(taxTypeCode);
                    invoiceBody.setTaxRate(taxRate);
                    invoiceBody.setDes("销项发票导入");
                    invoiceBody.setInvoiceType("2");
                    invoiceBody.setPeriod(busDate);

                    if (comNameSpec != null) {
                        // statisticsGoods(invoBodyMap, namount, nnumber,
                        // comName, spec, comNameSpec);
                    }

                    invoiceDao.insertInvocieH(invoiceHead);
                    invoiceDao.insertInvoiceB(invoiceBody);

                    session.setAttribute("invoiceHID", invoiceHID);

                    getInvoiceMapping(invoiceHead, invoiceBody, mappingList);
                }
            }
            session.removeAttribute("invoiceHID");

            // 添加到库存表
            if (!invoBodyMap.isEmpty()) {
                // vatService.updateKcCommodity31(invoBodyMap);
            }
            if (!mappingList.isEmpty()) {
                mappingSubcode(account, busDate, mappingList);
            }
            return mappingList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    public List<InvoiceHead> queryInvoice(Map<String, Object> param, HttpSession session) throws BusinessException {
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user");
        Account account = (Account) sessionMap.get("account");
        param.put("period", sessionMap.get("busDate"));
        param.put("accountID", account.getAccountID());
        param.put("userID", user.getUserID());
        List<InvoiceHead> list = invoiceDao.queryInvoiceH(param);
        return list;
    }

    /**********************************
     * 生成进项 销项凭证 start
     **********************************************************/

    @SuppressWarnings({"unchecked", "unused"})
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    // 一键生成 进项销项凭证
    public void invoice2vouch(User user, Account account) throws BusinessException {
        String period = account.getUseLastPeriod();
        String busDate = period;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("busDate", period);
        param.put("period", period);
        param.put("accountID", account.getAccountID());
        vatService.subinit(account.getAccountID(), period, user.getUserID(), user.getUserName());
        Integer companyType = account.getCompanyType();// 1：生产型// 2：贸易型 // 3：服务型

        //stateTrackService.queryState(param);

        List<Voucher> jxVo = new ArrayList<>();
        List<Voucher> xxVo = new ArrayList<>();
        List<String> vhid = new ArrayList<>();
        Map<String, SubjectMessage> saleCorpCache = new HashMap<>();
        Map<String, String> commIDCache = new HashMap<>();
        param.put("vhid", vhid);


        /************ statr 通过映射获取进项与销项科目信息***************/

        //进项生成分录 借	应交税费_应交增值税_进项税
        //销项生成分录 贷	应交税费_应交增值税_销项税
        Map<String, String> mappingCode = vatService.getAllMappingSubCode(account.getAccountID());

        String[] arr = {"进项税", "销项税", "主营业务收入"};
        Map<String, SubjectMessage> subMap = vatService.getSubMessageByCode(account.getAccountID(), mappingCode, arr);

        SubjectMessage jx_subjectMessage = subMap.get("进项税");
        SubjectMessage xx_subjectMessage = subMap.get("销项税");
        if (jx_subjectMessage == null || xx_subjectMessage == null) {
            throw new BusinessException("一键生成 进项销项凭证异常 method invoice2vouch subMap=" + subMap);
        }

        String jx_subCode = jx_subjectMessage.getSub_code();
        String xx_subCode = xx_subjectMessage.getSub_code();

        //查询主营业收入 6001
        SubjectMessage sub6001 = subMap.get("主营业务收入");

        /*********** end  通过映射获取进项与销项科目信息 ****************/


        /******************* 进项生成凭证 start ************************/


        try {
            long s1 = System.currentTimeMillis();
            // 获取进项发票主表信息
            List<InvoiceHead> jxList = invoiceDao.queryJxInvoiceH2Voucher(param);

            ArrayList<String> ss = new ArrayList<>();

            for (InvoiceHead invoiceHead : jxList) {
                ss.add(invoiceHead.getInvoiceHID());
            }


            if (null != jxList && jxList.size() != 0) {
                Integer maxVoucher = voucherHeadDao.getMaxVoucherNo(param); // 凭证号
                for (int i = 0; i < jxList.size(); i++) {
                    InvoiceHead invoiceHead = jxList.get(i);
                    // 构造凭证头
                    VoucherHead voucherHead = new VoucherHead();
                    List<VoucherBody> voucherBodyList = new ArrayList<VoucherBody>();// 构造凭证分录列表
                    VoucherBody voucherBody1 = new VoucherBody();// 构造凭证体
                    VoucherBody voucherBody2 = new VoucherBody();// 构造凭证体
                    VoucherBody voucherBody3 = new VoucherBody();// 构造凭证体
                    String invoiceHID = invoiceHead.getInvoiceHID();
                    List<InvoiceBody> invBodyList = invoiceDao.queryInvByHid(invoiceHID);
                    // 设置分录主键
                    voucherBody1.setVouchAID(UUIDUtils.getUUID());
                    // 设置分录号
                    voucherBody1.setRowIndex("1");


                    //String style_vcsubject = "<span style='color:red;text-decoration: line-through'>库存商品_未知商品科目</span>"; //定义错误凭证分录科目名称

                    String vcsubject = invBodyList.get(0).getSub_full_name();  //凭证摘要取发票里面的科目全名

                    String subjectID = invBodyList.get(0).getSub_code();      //凭证商品分录科目  去发票里面的科目

                    if (StringUtil.isEmpty(vcsubject) || StringUtil.isEmpty(subjectID)) {
                        throw new BusinessException("invoiceService invoice2vouch StringUtil.isEmpty(vcsubject) || StringUtil.isEmpty(subjectID)");

                    }

                    //销方公司
                    String saleCorp = invoiceHead.getSaleCorp();

                    // 给分录 voucherBody1 设置摘要
                    if (subjectID.startsWith("1405") || subjectID.startsWith("1403")) {
                        voucherBody1.setVcabstact("应付" + saleCorp + "货款");
                    } else {
                        voucherBody1.setVcabstact("应付" + saleCorp + "费用");
                    }


                    String vb_subName = vcsubject;
                    /******** 分录1 ********/
                    Double namount = invBodyList.get(0).getNamount();
                    Double nnumber = invBodyList.get(0).getNnumber();

                    voucherBody1.setVouchAID(UUIDUtils.getUUID());
                    voucherBody1.setVcsubject(vb_subName);
                    voucherBody1.setVcunit(invBodyList.get(0).getMeasure());
                    // 商品金额 借
                    voucherBody1.setDebitAmount(namount);
                    voucherBody1.setAccountID(account.getAccountID());
                    voucherBody1.setUserID(user.getUserID());
                    voucherBody1.setDirection("1");
                    voucherBody1.setSubjectID(subjectID);

                    if (nnumber != null && namount != null && nnumber != 0) {
                        voucherBody1.setPrice(namount / nnumber);
                    }

                    voucherBody1.setUpdatePsnID(user.getUserID());
                    voucherBody1.setUpdatePsn(user.getUserName());
                    voucherBody1.setUpdatedate(new Date());

                    voucherBody1.setNumber(nnumber);// 库存商品数量会检查

                    /******** 分录2 ********/
                    // 设置分录主键
                    voucherBody2.setVouchAID(UUIDUtils.getUUID());
                    // 设置分录号
                    voucherBody2.setRowIndex("2");


                    Double taxAmount = invBodyList.get(0).getTaxAmount();

                    // 商品税额 借
                    voucherBody2.setVcsubject(jx_subjectMessage.getFull_name());
                    voucherBody2.setDebitAmount(taxAmount);
                    voucherBody2.setAccountID(account.getAccountID());
                    voucherBody2.setUserID(user.getUserID());
                    voucherBody2.setDirection("1");
                    voucherBody2.setSubjectID(jx_subCode);
                    voucherBody2.setUpdatePsnID(user.getUserID());
                    voucherBody2.setUpdatePsn(user.getUserName());
                    voucherBody2.setUpdatedate(new Date());
                    voucherBody2.setIsproblem("2");

                    /******** 分录3 ********/
                    // 设置分录主键
                    voucherBody3.setVouchAID(UUIDUtils.getUUID());
                    // 设置分录号
                    SubjectMessage cacheSub = saleCorpCache.get(saleCorp);
                    SubjectMessage subjectMessage = null;
                    String subCode = null;
                    if (cacheSub != null) {
                        subjectMessage = cacheSub;
                        subCode = subjectMessage.getSub_code();
                    } else {
                        // 贷 应付账款_销方公司名称
                        subjectMessage = vatService.querySub(saleCorp, "2202", "7");
                        if (null != subjectMessage) {
                            subCode = subjectMessage.getSub_code();
                        } else {
                            subCode = vatService.getNumber("2202", "7", "2202000");
                            subjectMessage = vatService.createSub(subCode, "2202", saleCorp, "应付账款_" + saleCorp, "进项生成凭证", null, null);
                        }
                        saleCorpCache.put(saleCorp, subjectMessage);
                    }
                    if (subCode == null || subjectMessage == null) {
                        throw new BusinessException("subCode==null || subjectMessage==null");
                    }

                    voucherBody3.setRowIndex("3");
                    voucherBody3.setSubjectID(subCode);
                    voucherBody3.setVcsubject(subjectMessage.getFull_name()); // 应付账套_某某公司
                    // 应付卖方总金额(价税合计) 金额+税额 贷
                    voucherBody3.setCreditAmount(invBodyList.get(0).getNtaxAmount());
                    voucherBody3.setAccountID(account.getAccountID());
                    voucherBody3.setUserID(user.getUserID());
                    voucherBody3.setDirection("2");
                    voucherBody3.setUpdatePsnID(user.getUserID());
                    voucherBody3.setUpdatePsn(user.getUserName());
                    voucherBody3.setUpdatedate(new Date());
                    voucherBody3.setIsproblem("2");

                    String vouchID = UUIDUtils.getUUID(); // 凭证主表id
                    voucherHead.setVouchID(vouchID);
                    voucherHead.setVcDate(new Date());
                    voucherHead.setCreatePsnID(user.getUserID());
                    voucherHead.setCreateDate(System.currentTimeMillis());
                    voucherHead.setCreatepsn(user.getUserName());
                    //设置凭证号
                    voucherHead.setVoucherNO(maxVoucher++);
                    voucherHead.setSource(0);
                    voucherHead.setAccountID(account.getAccountID());
                    voucherHead.setPeriod(busDate);
                    Double totalDbit = StringUtil.doubleIsNull(voucherBody1.getDebitAmount()) + StringUtil.doubleIsNull(voucherBody2.getDebitAmount());
                    voucherHead.setTotalDbit(totalDbit);
                    voucherHead.setTotalCredit(voucherBody3.getCreditAmount());

                    voucherBody1.setVouchID(vouchID);
                    voucherBody2.setVouchID(vouchID);
                    voucherBody3.setVouchID(vouchID);

                    voucherHeadDao.insertVouchHead(voucherHead);

                    invoiceDao.updateInvoiceVouID(invoiceHID, vouchID); // 反写凭证主键到发票表

                    if (voucherHead != null) {
                        VoucherBody[] arr_vb = {voucherBody1, voucherBody2, voucherBody3};
                        vatService.insertVouchBatch(arr_vb);
                    }

                    // 包装凭证对象
                    Voucher voucher = new Voucher();
                    voucherBodyList.add(voucherBody1);
                    voucherBodyList.add(voucherBody2);
                    voucherBodyList.add(voucherBody3);
                    voucher.setVoucherHead(voucherHead);
                    voucher.setVoucherBodyList(voucherBodyList);
                    // 新增凭证后 附带变更科目
                    param.put("jx_pz", "jx_pz"); // 定义进项凭证标识 jx_pz
                    boolean bool = vatService.checkVouch(param, voucher); // 检查凭证
                    if (bool) {
                        tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                    }
                    param.remove("jx_pz");
                }

                if (!vhid.isEmpty()) { // 更新凭证
                    vatDao.upVouchBody(param);
                    vatDao.upVouch(param);
                }
            }
            jxVo.clear();
            vhid.clear();

            /******************* 进项生成凭证 end ************************/

            /******************* 销项生成凭证 start ************************/
            // 获取主表 购方公司 分组

            List<String> buyCorpList = invoiceDao.queryHBuyCorp(param); // and
            // buyCorp 购方公司名称
            if (null != buyCorpList && buyCorpList.size() > 0) {

                Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 凭证号

                for (int i = 0; i < buyCorpList.size(); i++) { // 遍历销方公司
                    double zsTotalJe = 0.0;
                    double zsTotalSe = 0.0;
                    double fsTotalJe = 0.0;
                    double fsTotalSe = 0.0;
                    String buyCorp = buyCorpList.get(i);
                    List<String> invoiceHIDs = null;
                    if (!StringUtil.isEmpty(buyCorp)) {
                        param.put("buyCorp", buyCorp.trim());
                        // 根据购方公司名获取主表主键
                        // 根据公司名称得到主发票id集合  一个公司可能有多个主凭证
                        invoiceHIDs = invoiceDao.queryHIDByBuyCorp(param);
                        if (null != invoiceHIDs && invoiceHIDs.size() > 0) {
                            for (int j = 0; j < invoiceHIDs.size(); j++) {
                                String invoiceHID = invoiceHIDs.get(j);
                                // 根据主发票id获取 字表 金额 与 税额 总计
                                List<Map<String, Object>> list = invoiceDao.queryAmountByHID(invoiceHID);
                                if (list != null && list.size() > 0) {
                                    for (int k = 0; k < list.size(); k++) {
                                        Map<String, Object> map = list.get(k);
                                        Object jeObject = map.get("je"); // 金额namount
                                        Object seObject = map.get("se"); // 税额taxAmount
                                        double je = 0;
                                        double se = 0;
                                        if (jeObject != null) {
                                            je = Double.parseDouble(jeObject.toString());
                                        }
                                        if (seObject != null) {
                                            se = Double.parseDouble(seObject.toString());
                                        }
                                        if (je > 0 || se > 0) {
                                            zsTotalJe = zsTotalJe + je; // 购房公司总金额
                                            zsTotalSe = zsTotalSe + se; // 购房公司总税额
                                        } else if (je < 0 || se < 0) {
                                            fsTotalJe = fsTotalJe + je;
                                            fsTotalSe = fsTotalSe + se;
                                        }
                                    }
                                }
                            }
                            if (zsTotalJe > 0 || zsTotalSe > 0) {
                                List<VoucherBody> list = new ArrayList<VoucherBody>();
                                if (zsTotalJe != 0.0 || zsTotalJe != 0.0) {
                                    // 构造凭证头
                                    VoucherHead voucherHead = new VoucherHead();
                                    // 凭证头主键
                                    String vouchID = UUIDUtils.getUUID();
                                    voucherHead.setVouchID(vouchID);
                                    // 获取销项发票字表列表
                                    voucherHead.setVouchID(vouchID);
                                    voucherHead.setCreatePsnID(user.getUserID());
                                    voucherHead.setCreateDate(System.currentTimeMillis());
                                    voucherHead.setCreatepsn(user.getUserName());

                                    voucherHead.setVoucherNO(maxVoucherNo++);
                                    voucherHead.setAccountID(account.getAccountID());
                                    voucherHead.setSource(9);
                                    voucherHead.setVcDate(new Date());
                                    voucherHead.setPeriod(busDate);
                                    voucherHead.setAuditStatus(0);
                                    voucherHead.setVouchFlag(0);
                                    voucherHead.setTotalCredit(zsTotalJe + zsTotalSe);
                                    voucherHead.setTotalDbit(zsTotalJe + zsTotalSe);
                                    voucherHead.setIsproblem("2");
                                    // 写入凭证头
                                    voucherHeadDao.insertVouchHead(voucherHead);

                                    /******** 分录1 ********/
                                    VoucherBody vouBody1 = new VoucherBody();
                                    vouBody1.setVouchAID(UUIDUtils.getUUID());
                                    //vouBody1 设置摘要
                                    vouBody1.setVcabstact("应收" + buyCorp + "货款");
                                    vouBody1.setVouchID(vouchID);
                                    vouBody1.setRowIndex("1");


                                    String subCode = null;
                                    SubjectMessage subjectMessage = vatService.querySub(buyCorp, "1122", "7");
                                    if (null != subjectMessage) {
                                        subCode = subjectMessage.getSub_code();
                                    } else {
                                        subCode = vatService.getNumber("1122", "7", "1122000");
                                        subjectMessage = vatService.createSub(subCode, "1122", buyCorp, "应收账款_" + buyCorp, "销项生成凭证", null, null);
                                    }

                                    if (subCode == null || subjectMessage == null) {
                                        throw new BusinessException("subCode==null || subjectMessage==null");
                                    }

                                    vouBody1.setSubjectID(subCode);
                                    vouBody1.setVcsubject(subjectMessage.getFull_name());
                                    vouBody1.setDebitAmount(zsTotalJe + zsTotalSe); // 金额+税额
                                    vouBody1.setAccountID(account.getAccountID());
                                    vouBody1.setUserID(user.getUserID());
                                    vouBody1.setDirection("1");
                                    vouBody1.setUpdatePsnID(user.getUserID());
                                    vouBody1.setUpdatePsn(user.getUserName());
                                    vouBody1.setUpdatedate(new Date());
                                    vouBody1.setIsproblem("2");

                                    /******** 分录2 ********/
                                    //贷主营业务收入 不能直接使用固定科目作为凭证  需要查询获取
                                    VoucherBody vouBody2 = new VoucherBody();
                                    vouBody2.setCreditAmount(zsTotalJe);
                                    vouBody2.setVouchAID(UUIDUtils.getUUID());
                                    vouBody2.setVouchID(vouchID);
                                    vouBody2.setVcsubject(sub6001.getFull_name());
                                    vouBody2.setDirection("2");
                                    vouBody2.setAccountID(account.getAccountID());
                                    vouBody2.setUserID(user.getUserID());
                                    vouBody2.setUpdatePsnID(user.getUserID());
                                    vouBody2.setUpdatePsn(user.getUserName());
                                    vouBody2.setUpdatedate(new Date());
                                    vouBody2.setRowIndex("2");
                                    vouBody2.setSubjectID(sub6001.getSub_code());
                                    vouBody2.setIsproblem("2");

                                    /******** 分录3 ********/
                                    // 构造凭证分录3 税额等于0 销项税凭证不生成
                                    VoucherBody vouBody3 = new VoucherBody();
                                    if (zsTotalSe != 0.0) {
                                        vouBody3.setVouchAID(UUIDUtils.getUUID());
                                        vouBody3.setVouchID(vouchID);
                                        vouBody3.setVcsubject(xx_subjectMessage.getFull_name()); //科目名
                                        vouBody3.setDirection("2");
                                        vouBody3.setCreditAmount(zsTotalSe);
                                        vouBody3.setAccountID(account.getAccountID());
                                        vouBody3.setUserID(user.getUserID());
                                        vouBody3.setUpdatePsnID(user.getUserID());
                                        vouBody3.setUpdatePsn(user.getUserName());
                                        vouBody3.setUpdatedate(new Date());
                                        vouBody3.setRowIndex("3");
                                        vouBody3.setSubjectID(xx_subCode);
                                        vouBody3.setIsproblem("2");
                                        list.add(vouBody3);
                                    }
                                    if (voucherHead != null) {
                                        List<VoucherBody> addList = new ArrayList<>();
                                        addList.add(vouBody1);
                                        addList.add(vouBody2);
                                        if (vouBody3.getVouchID() != null) {
                                            addList.add(vouBody3);
                                        }
                                        vatService.insertVouchBatch(addList);
                                    }

                                    // 反写凭证主键到发票表
                                    for (int s = 0; s < invoiceHIDs.size(); s++) {
                                        invoiceDao.updateInvoiceVouID(invoiceHIDs.get(s), vouchID);
                                    }
                                    // 新增凭证后 附带变更科目
                                    Voucher voucher = new Voucher();

                                    list.add(vouBody1);
                                    list.add(vouBody2);

                                    voucher.setVoucherBodyList(list);
                                    voucher.setVoucherHead(voucherHead);

                                    boolean bool = vatService.checkVouch(param, voucher);
                                    if (bool) {
                                        param.put("kcpz", "kcpz");
                                        tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                                        param.remove("kcpz");
                                    }
                                }
                            }
                            if (fsTotalJe < 0 || fsTotalSe < 0) {
                                List<VoucherBody> list = new ArrayList<VoucherBody>();
                                if (fsTotalJe != 0.0 || fsTotalSe != 0.0) {
                                    // 构造凭证头
                                    VoucherHead voucherHead = new VoucherHead();
                                    // 凭证头主键
                                    String vouchID = UUIDUtils.getUUID();
                                    voucherHead.setVouchID(vouchID);
                                    // 获取销项发票字表列表
                                    voucherHead.setVouchID(vouchID);
                                    voucherHead.setCreatePsnID(user.getUserID());
                                    voucherHead.setCreateDate(System.currentTimeMillis());
                                    voucherHead.setCreatepsn(user.getUserName());

                                    voucherHead.setVoucherNO(maxVoucherNo++);

                                    voucherHead.setAccountID(account.getAccountID());
                                    voucherHead.setSource(9);
                                    voucherHead.setVcDate(new Date());
                                    voucherHead.setPeriod(busDate);
                                    voucherHead.setAuditStatus(0);
                                    voucherHead.setVouchFlag(0);
                                    voucherHead.setTotalCredit(fsTotalJe + fsTotalSe);
                                    voucherHead.setTotalDbit(fsTotalJe + fsTotalSe);
                                    voucherHead.setIsproblem("2");
                                    // 写入凭证头
                                    voucherHeadDao.insertVouchHead(voucherHead);
                                    // 构造凭证分录1
                                    VoucherBody vouBody1 = new VoucherBody();
                                    vouBody1.setVouchAID(UUIDUtils.getUUID());
                                    //vouBody1 设置摘要
                                    vouBody1.setVcabstact("应收" + buyCorp + "货款"); //如果是劳务费或者服务费 摘要不能设置货款结尾
                                    vouBody1.setVouchID(vouchID);
                                    vouBody1.setRowIndex("1");

                                    String subCode = null;
                                    SubjectMessage subjectMessage = vatService.querySub(buyCorp, "1122", "7");
                                    if (null != subjectMessage) {
                                        subCode = subjectMessage.getSub_code();
                                    } else {
                                        subCode = vatService.getNumber("1122", "7", "1122000");
                                        subjectMessage = vatService.createSub(subCode, "1122", buyCorp, "应收账款_" + buyCorp, "销项生成凭证", null, null);
                                    }

                                    if (subCode == null || subjectMessage == null) {
                                        throw new BusinessException("subCode==null || subjectMessage==null");
                                    }

                                    /******** 分录1 ********/
                                    vouBody1.setSubjectID(subCode);
                                    vouBody1.setVcsubject("应收账款_" + buyCorp);
                                    vouBody1.setDebitAmount(fsTotalJe + fsTotalSe); // 金额+税额
                                    vouBody1.setAccountID(account.getAccountID());
                                    vouBody1.setUserID(user.getUserID());
                                    vouBody1.setDirection("1");
                                    vouBody1.setUpdatePsnID(user.getUserID());
                                    vouBody1.setUpdatePsn(user.getUserName());
                                    vouBody1.setUpdatedate(new Date());
                                    vouBody1.setIsproblem("2");

                                    /******** 分录2 ********/
                                    VoucherBody vouBody2 = new VoucherBody();
                                    vouBody2.setCreditAmount(fsTotalJe);
                                    vouBody2.setVouchAID(UUIDUtils.getUUID());
                                    vouBody2.setVouchID(vouchID);

                                    //主营业务收入
                                    vouBody2.setVcsubject(sub6001.getSub_name());
                                    vouBody2.setDirection("2");
                                    // vouBody2.setCreditAmount(invoBody.getNamount());
                                    vouBody2.setAccountID(account.getAccountID());
                                    vouBody2.setUserID(user.getUserID());
                                    vouBody2.setUpdatePsnID(user.getUserID());
                                    vouBody2.setUpdatePsn(user.getUserName());
                                    vouBody2.setUpdatedate(new Date());
                                    vouBody2.setRowIndex("2");
                                    vouBody2.setSubjectID(sub6001.getSub_code());
                                    vouBody2.setIsproblem("2");

                                    /******** 分录3 ********/
                                    // 构造凭证分录3 税额等于0 销项税凭证不生成
                                    VoucherBody vouBody3 = new VoucherBody();
                                    if (fsTotalSe != 0.0) {
                                        vouBody3.setVouchAID(UUIDUtils.getUUID());
                                        vouBody3.setVouchID(vouchID);
                                        vouBody3.setVcabstact("应收" + buyCorp + "货款");

                                        vouBody3.setVcsubject(xx_subjectMessage.getFull_name());
                                        vouBody3.setDirection("2");
                                        vouBody3.setCreditAmount(fsTotalSe);
                                        vouBody3.setAccountID(account.getAccountID());
                                        vouBody3.setUserID(user.getUserID());
                                        vouBody3.setUpdatePsnID(user.getUserID());
                                        vouBody3.setUpdatePsn(user.getUserName());
                                        vouBody3.setUpdatedate(new Date());
                                        vouBody3.setRowIndex("3");
                                        vouBody3.setSubjectID(xx_subCode);
                                        vouBody3.setIsproblem("2");

                                        list.add(vouBody3);
                                    }

                                    if (voucherHead != null) {
                                        List<VoucherBody> addList = new ArrayList<>();
                                        addList.add(vouBody1);
                                        addList.add(vouBody2);
                                        if (vouBody3.getVouchID() != null) {
                                            addList.add(vouBody3);
                                        }
                                        vatService.insertVouchBatch(addList);
                                    }

                                    // 反写凭证主键到发票表
                                    for (int s = 0; s < invoiceHIDs.size(); s++) {
                                        invoiceDao.updateInvoiceVouID(invoiceHIDs.get(s), vouchID);
                                    }
                                    // 新增凭证后 附带变更科目
                                    Voucher voucher = new Voucher();

                                    list.add(vouBody1);
                                    list.add(vouBody2);

                                    voucher.setVoucherBodyList(list);
                                    voucher.setVoucherHead(voucherHead);

                                    boolean bool = vatService.checkVouch(param, voucher);
                                    if (bool) {
                                        param.put("kcpz", "kcpz");
                                        tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                                        param.remove("kcpz");
                                    }
                                }
                            }
                        }
                    }

                }
            }


            if (!vhid.isEmpty()) {
                vatDao.upVouchBody(param);
                vatDao.upVouch(param);
            }

            //销项发票入库
            //vatService.xxCommodityStorage(account.getAccountID(),busDate);


            /******************* 销项生成凭证 end ************************/
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    /**********************************
     * 小规模   销项生成凭证 start
     **********************************************************/

    @SuppressWarnings({"unchecked", "unused"})
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    // 一键生成 进项销项凭证
    public void invoiceOutOutGenerateVouch(User user, Account account) throws BusinessException {

        //小规模销项分录
        //借: 应收账款-xx公司  		100
        //贷: 主营业务收入           		97
        //贷: 应交税费-应交增值税	3
        //说明 小规模没有进项税和销项税 ，开票也只有销项发票。不存在进项导入

        String busDate = account.getUseLastPeriod();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("busDate", busDate);
        param.put("period", busDate);
        param.put("accountID", account.getAccountID());
        vatService.subinit(account.getAccountID(), busDate, user.getUserID(), user.getUserName());
        Integer companyType = account.getCompanyType();// 1：生产型// 2：贸易型 // 3：服务型


        List<String> vhid = new ArrayList<>();
        Map<String, SubjectMessage> saleCorpCache = new HashMap<>();
        Map<String, String> commIDCache = new HashMap<>();
        param.put("vhid", vhid);


        /************ statr 通过映射获取进项与销项科目信息***************/

        Map<String, String> mappingCode = vatService.getAllMappingSubCode(account.getAccountID());
        if (mappingCode.get("进项税") != null || mappingCode.get("销项税") != null) {
            //throw new BusinessException("一键生成 销项凭证异常  invoiceOutOutGenerateVouch mappingCode.get(\"进项税\")!=null || mappingCode.get(\"销项税\")!=null");
        }
        String[] arr = {"应交增值税", "主营业务收入"};
        Map<String, SubjectMessage> subMap = vatService.getSubMessageByCode(account.getAccountID(), mappingCode, arr);

        SubjectMessage yjzzs_subjectMessage = subMap.get("应交增值税");
        SubjectMessage sub6001 = subMap.get("主营业务收入");
        if (yjzzs_subjectMessage == null || sub6001 == null) {
            throw new BusinessException("一键生成 进项销项凭证异常 method invoice2vouch subMap=" + subMap);
        }
        String yjzzs_subCode = yjzzs_subjectMessage.getSub_code();

        /*********** end  通过映射获取进项与销项科目信息 ****************/


        /******************* 销项生成凭证 start ************************/
        // 获取主表 购方公司 分组

        List<String> buyCorpList = invoiceDao.queryHBuyCorp(param); // and
        // buyCorp 购方公司名称
        if (null != buyCorpList && buyCorpList.size() > 0) {
            Integer maxVoucherNo = voucherHeadDao.getMaxVoucherNo(param); // 开始最大凭证号


            for (int i = 0; i < buyCorpList.size(); i++) { // 遍历销方公司
                double zsTotalJe = 0.0;
                double zsTotalSe = 0.0;
                double fsTotalJe = 0.0;
                double fsTotalSe = 0.0;
                String buyCorp = buyCorpList.get(i);
                List<String> invoiceHIDs = null;
                if (!StringUtil.isEmpty(buyCorp)) {
                    param.put("buyCorp", buyCorp.trim());
                    // 根据购方公司名获取主表主键
                    // 根据公司名称得到主发票id集合  一个公司可能有多个主凭证
                    invoiceHIDs = invoiceDao.queryHIDByBuyCorp(param);
                    if (null != invoiceHIDs && invoiceHIDs.size() > 0) {
                        for (int j = 0; j < invoiceHIDs.size(); j++) {
                            String invoiceHID = invoiceHIDs.get(j);
                            // 根据主发票id获取 字表 金额 与 税额 总计
                            List<Map<String, Object>> list = invoiceDao.queryAmountByHID(invoiceHID);
                            if (list != null && list.size() > 0) {
                                for (int k = 0; k < list.size(); k++) {
                                    Map<String, Object> map = list.get(k);
                                    Object jeObject = map.get("je"); // 金额namount
                                    Object seObject = map.get("se"); // 税额taxAmount
                                    double je = 0;
                                    double se = 0;
                                    if (jeObject != null) {
                                        je = Double.parseDouble(jeObject.toString());
                                    }
                                    if (seObject != null) {
                                        se = Double.parseDouble(seObject.toString());
                                    }
                                    if (je > 0 || se > 0) {
                                        zsTotalJe = zsTotalJe + je; // 购房公司总金额
                                        zsTotalSe = zsTotalSe + se; // 购房公司总税额
                                    } else if (je < 0 || se < 0) {
                                        fsTotalJe = fsTotalJe + je;
                                        fsTotalSe = fsTotalSe + se;
                                    }
                                }
                            }
                        }
                        if (zsTotalJe > 0 || zsTotalSe > 0) {
                            List<VoucherBody> list = new ArrayList<VoucherBody>();
                            if (zsTotalJe != 0.0 || zsTotalJe != 0.0) {
                                // 构造凭证头
                                VoucherHead voucherHead = new VoucherHead();
                                // 凭证头主键
                                String vouchID = UUIDUtils.getUUID();
                                voucherHead.setVouchID(vouchID);
                                // 获取销项发票字表列表
                                voucherHead.setVouchID(vouchID);
                                voucherHead.setCreatePsnID(user.getUserID());
                                voucherHead.setCreateDate(System.currentTimeMillis());
                                voucherHead.setCreatepsn(user.getUserName());

                                voucherHead.setVoucherNO(maxVoucherNo++);
                                voucherHead.setAccountID(account.getAccountID());
                                voucherHead.setSource(9);
                                voucherHead.setVcDate(new Date());
                                voucherHead.setPeriod(busDate);
                                voucherHead.setAuditStatus(0);
                                voucherHead.setVouchFlag(0);
                                voucherHead.setTotalCredit(zsTotalJe + zsTotalSe);
                                voucherHead.setTotalDbit(zsTotalJe + zsTotalSe);
                                voucherHead.setIsproblem("2");
                                // 写入凭证头
                                voucherHeadDao.insertVouchHead(voucherHead);

                                /******** 分录1 ********/
                                VoucherBody vouBody1 = new VoucherBody();
                                vouBody1.setVouchAID(UUIDUtils.getUUID());
                                //vouBody1 设置摘要
                                vouBody1.setVcabstact("应收" + buyCorp + "货款");
                                vouBody1.setVouchID(vouchID);
                                vouBody1.setRowIndex("1");


                                String subCode = null;
                                SubjectMessage subjectMessage = vatService.querySub(buyCorp, "1122", "7");
                                if (null != subjectMessage) {
                                    subCode = subjectMessage.getSub_code();
                                } else {
                                    subCode = vatService.getNumber("1122", "7", "1122000");
                                    subjectMessage = vatService.createSub(subCode, "1122", buyCorp, "应收账款_" + buyCorp, "小规模销项生成凭证", null, null);
                                }

                                if (subCode == null || subjectMessage == null) {
                                    throw new BusinessException("subCode==null || subjectMessage==null");
                                }

                                vouBody1.setSubjectID(subCode);
                                vouBody1.setVcsubject(subjectMessage.getFull_name());
                                vouBody1.setDebitAmount(zsTotalJe + zsTotalSe); // 金额+税额
                                vouBody1.setAccountID(account.getAccountID());
                                vouBody1.setUserID(user.getUserID());
                                vouBody1.setDirection("1");
                                vouBody1.setUpdatePsnID(user.getUserID());
                                vouBody1.setUpdatePsn(user.getUserName());
                                vouBody1.setUpdatedate(new Date());
                                vouBody1.setIsproblem("2");

                                /******** 分录2 ********/
                                //贷主营业务收入 不能直接使用固定科目作为凭证  需要查询获取
                                VoucherBody vouBody2 = new VoucherBody();
                                vouBody2.setCreditAmount(zsTotalJe);
                                vouBody2.setVouchAID(UUIDUtils.getUUID());
                                vouBody2.setVouchID(vouchID);
                                vouBody2.setVcsubject(sub6001.getFull_name());
                                vouBody2.setDirection("2");
                                vouBody2.setAccountID(account.getAccountID());
                                vouBody2.setUserID(user.getUserID());
                                vouBody2.setUpdatePsnID(user.getUserID());
                                vouBody2.setUpdatePsn(user.getUserName());
                                vouBody2.setUpdatedate(new Date());
                                vouBody2.setRowIndex("2");
                                vouBody2.setSubjectID(sub6001.getSub_code());
                                vouBody2.setIsproblem("2");

                                /******** 分录3 ********/
                                // 构造凭证分录3 税额等于0 销项税凭证不生成
                                VoucherBody vouBody3 = new VoucherBody();
                                if (zsTotalSe != 0.0) {
                                    vouBody3.setVouchAID(UUIDUtils.getUUID());
                                    vouBody3.setVouchID(vouchID);
                                    vouBody3.setVcsubject(yjzzs_subjectMessage.getFull_name()); //应交税费_应交增值税科目全名
                                    vouBody3.setDirection("2");
                                    vouBody3.setCreditAmount(zsTotalSe);
                                    vouBody3.setAccountID(account.getAccountID());
                                    vouBody3.setUserID(user.getUserID());
                                    vouBody3.setUpdatePsnID(user.getUserID());
                                    vouBody3.setUpdatePsn(user.getUserName());
                                    vouBody3.setUpdatedate(new Date());
                                    vouBody3.setRowIndex("3");
                                    vouBody3.setSubjectID(yjzzs_subCode);//应交增值税科目编码
                                    vouBody3.setIsproblem("2");
                                    list.add(vouBody3);
                                }
                                if (voucherHead != null) {
                                    List<VoucherBody> addList = new ArrayList<>();
                                    addList.add(vouBody1);
                                    addList.add(vouBody2);
                                    if (vouBody3.getVouchID() != null) {
                                        addList.add(vouBody3);
                                    }
                                    vatService.insertVouchBatch(addList);
                                }

                                // 反写凭证主键到发票表
                                for (int s = 0; s < invoiceHIDs.size(); s++) {
                                    invoiceDao.updateInvoiceVouID(invoiceHIDs.get(s), vouchID);
                                }
                                // 新增凭证后 附带变更科目
                                Voucher voucher = new Voucher();

                                list.add(vouBody1);
                                list.add(vouBody2);

                                voucher.setVoucherBodyList(list);
                                voucher.setVoucherHead(voucherHead);

                                boolean bool = vatService.checkVouch(param, voucher);
                                if (bool) {
                                    param.put("kcpz", "kcpz");
                                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                                    param.remove("kcpz");
                                }
                            }
                        }
                        if (fsTotalJe < 0 || fsTotalSe < 0) {
                            List<VoucherBody> list = new ArrayList<VoucherBody>();
                            if (fsTotalJe != 0.0 || fsTotalSe != 0.0) {
                                // 构造凭证头
                                VoucherHead voucherHead = new VoucherHead();
                                // 凭证头主键
                                String vouchID = UUIDUtils.getUUID();
                                voucherHead.setVouchID(vouchID);
                                // 获取销项发票字表列表
                                voucherHead.setVouchID(vouchID);
                                voucherHead.setCreatePsnID(user.getUserID());
                                voucherHead.setCreateDate(System.currentTimeMillis());
                                voucherHead.setCreatepsn(user.getUserName());

                                voucherHead.setVoucherNO(maxVoucherNo++);

                                voucherHead.setAccountID(account.getAccountID());
                                voucherHead.setSource(9);
                                voucherHead.setVcDate(new Date());
                                voucherHead.setPeriod(busDate);
                                voucherHead.setAuditStatus(0);
                                voucherHead.setVouchFlag(0);
                                voucherHead.setTotalCredit(fsTotalJe + fsTotalSe);
                                voucherHead.setTotalDbit(fsTotalJe + fsTotalSe);
                                voucherHead.setIsproblem("2");
                                // 写入凭证头
                                voucherHeadDao.insertVouchHead(voucherHead);
                                // 构造凭证分录1
                                VoucherBody vouBody1 = new VoucherBody();
                                vouBody1.setVouchAID(UUIDUtils.getUUID());
                                //vouBody1 设置摘要
                                vouBody1.setVcabstact("应收" + buyCorp + "货款"); //如果是劳务费或者服务费 摘要不能设置货款结尾
                                vouBody1.setVouchID(vouchID);
                                vouBody1.setRowIndex("1");

                                String subCode = null;
                                SubjectMessage subjectMessage = vatService.querySub(buyCorp, "1122", "7");
                                if (null != subjectMessage) {
                                    subCode = subjectMessage.getSub_code();
                                } else {
                                    subCode = vatService.getNumber("1122", "7", "1122000");
                                    subjectMessage = vatService.createSub(subCode, "1122", buyCorp, "应收账款_" + buyCorp, "小规模销项生成凭证", null, null);
                                }

                                if (subCode == null || subjectMessage == null) {
                                    throw new BusinessException("subCode==null || subjectMessage==null");
                                }

                                /******** 分录1 ********/
                                vouBody1.setSubjectID(subCode);
                                vouBody1.setVcsubject("应收账款_" + buyCorp);
                                vouBody1.setDebitAmount(fsTotalJe + fsTotalSe); // 金额+税额
                                vouBody1.setAccountID(account.getAccountID());
                                vouBody1.setUserID(user.getUserID());
                                vouBody1.setDirection("1");
                                vouBody1.setUpdatePsnID(user.getUserID());
                                vouBody1.setUpdatePsn(user.getUserName());
                                vouBody1.setUpdatedate(new Date());
                                vouBody1.setIsproblem("2");

                                /******** 分录2 ********/
                                VoucherBody vouBody2 = new VoucherBody();
                                vouBody2.setCreditAmount(fsTotalJe);
                                vouBody2.setVouchAID(UUIDUtils.getUUID());
                                vouBody2.setVouchID(vouchID);

                                //主营业务收入
                                vouBody2.setVcsubject(sub6001.getSub_name());
                                vouBody2.setDirection("2");
                                vouBody2.setAccountID(account.getAccountID());
                                vouBody2.setUserID(user.getUserID());
                                vouBody2.setUpdatePsnID(user.getUserID());
                                vouBody2.setUpdatePsn(user.getUserName());
                                vouBody2.setUpdatedate(new Date());
                                vouBody2.setRowIndex("2");
                                vouBody2.setSubjectID(sub6001.getSub_code());
                                vouBody2.setIsproblem("2");

                                /******** 分录3 ********/
                                // 构造凭证分录3 税额等于0 销项税凭证不生成
                                VoucherBody vouBody3 = new VoucherBody();
                                if (fsTotalSe != 0.0) {
                                    vouBody3.setVouchAID(UUIDUtils.getUUID());
                                    vouBody3.setVouchID(vouchID);
                                    vouBody3.setVcabstact("应收" + buyCorp + "货款");

                                    vouBody3.setVcsubject(yjzzs_subjectMessage.getFull_name());  //应交税费_应交增值税 科目全名
                                    vouBody3.setDirection("2");
                                    vouBody3.setCreditAmount(fsTotalSe);
                                    vouBody3.setAccountID(account.getAccountID());
                                    vouBody3.setUserID(user.getUserID());
                                    vouBody3.setUpdatePsnID(user.getUserID());
                                    vouBody3.setUpdatePsn(user.getUserName());
                                    vouBody3.setUpdatedate(new Date());
                                    vouBody3.setRowIndex("3");
                                    vouBody3.setSubjectID(yjzzs_subCode);  //应交增值税科目编码
                                    vouBody3.setIsproblem("2");

                                    list.add(vouBody3);
                                }

                                if (voucherHead != null) {
                                    List<VoucherBody> addList = new ArrayList<>();
                                    addList.add(vouBody1);
                                    addList.add(vouBody2);
                                    if (vouBody3.getVouchID() != null) {
                                        addList.add(vouBody3);
                                    }
                                    vatService.insertVouchBatch(addList);
                                }

                                // 反写凭证主键到发票表
                                for (int s = 0; s < invoiceHIDs.size(); s++) {
                                    invoiceDao.updateInvoiceVouID(invoiceHIDs.get(s), vouchID);
                                }
                                // 新增凭证后 附带变更科目
                                Voucher voucher = new Voucher();

                                list.add(vouBody1);
                                list.add(vouBody2);

                                voucher.setVoucherBodyList(list);
                                voucher.setVoucherHead(voucherHead);

                                //销项生成凭证 没有商品在里面 所以无法检查1405的科目，对库商品也不能判断
                                boolean bool = vatService.checkVouch(param, voucher);
                                if (bool) {
                                    param.put("kcpz", "kcpz");
                                    tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
                                    param.remove("kcpz");
                                }
                            }
                        }
                    }
                }

            }
        }

        if (!vhid.isEmpty()) {
            vatDao.upVouchBody(param);
            vatDao.upVouch(param);
        }


    }


    @SuppressWarnings("unused")
    private void statisticsGoods(Map<String, InvoiceBody> map, String amount, String nnumber, String comName, String spec, String comNameSpec) {
        InvoiceBody invo = map.get(comNameSpec);
        Double num = 0.0;
        Double amot = 0.0;
        // 不包含税额
        if (StringUtil.isEmpty(amount) || StringUtil.isEmpty(nnumber)) {
            return;
        }
        if (invo != null) {
            num = invo.getNnumber();
            amot = invo.getNamount();
        } else {
            invo = new InvoiceBody();
        }
        invo.setComNameSpec(comNameSpec);
        invo.setComName(comName);
        invo.setSpec(spec);
        invo.setNnumber(StringUtil.strIsNull(nnumber) + num);
        invo.setNamount(StringUtil.strIsNull(amount) + amot);
        map.put(comNameSpec, invo);
    }

    // 删除发票
    // @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void delFaPiao(Map<String, Object> qerMap) throws BusinessException {
        try {
            int n2 = invoiceDao.delFaPiao2(qerMap); // 删除发票子表
            int n1 = invoiceDao.delFaPiao1(qerMap); // 删除发票主表
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    //科目映射发票保存操作
    @SuppressWarnings("unused")
    @Override

    @Transactional(rollbackFor = BusinessException.class)
    // 科目映射发票保存操作
    //editInvoiceMapping
    public void saveInvoiceMapping(Map<String, Object> param) throws BusinessException {
        try {
            //更新发票表的数据
            String str = param.get("contentDate").toString().trim();
            //分割符   值为&& 行为### 检查数量 价格 是否为整数或者小数
            //String regEx = "^[0-9]+([.]{1}[0-9]+){0,1}$";
            String regEx = "^(-?)(?=.*[^0.])(0|[1-9]\\d*)(\\.\\d+)?";
            Pattern p = Pattern.compile(regEx);
            String[] arr0 = str.split("###");
            for (int i = 0; i < arr0.length; i++) {
                String row0 = arr0[i];
                String[] text = row0.split("&&");
                for (int j = 0; j < text.length; j++) {
                    String field = text[j];
                    //j==3等于3为映射科目编码
                    if (j == 3) {
                        if (field == null || "null".equals(field) || field.trim().equals("")) {
                            throw new BusinessException(text[0] + " 科目编码不能为空");
                        }
                    }

                    //j==5为商品数量
                    if (j == 5) {
                        //注意数量只要科目编码为1405 与1405 开头的才有
                        String code = text[3];
                        if (code.startsWith("1405") || code.startsWith("1403")) {
                            if (field == null || "null".equals(field) || field.trim().equals("") || Double.valueOf(field) == 0.0) {
                                throw new BusinessException(text[3] + "数量不能为空");
                            }
                            field = field.trim();
                            boolean bool = p.matcher(field).matches(); //并且1405 1403数量只能为小数或者整数
                            if (bool == false) {
                                throw new BusinessException(text[3] + " 数量格式错误");
                            }
                        } else {
                            //非1405
                            if (field != null && !"null".equals(field) && !field.trim().equals("")) {
                                field = field.trim();
                                boolean bool = p.matcher(field).matches(); //并且1405 1403数量只能为小数或者整数
                                if (bool == false) {
                                    throw new BusinessException(text[3] + " 数量格式错误");
                                }
                            } else {
                                System.out.println();
                            }
                        }
                    }
                }
            }

            // 	字符串是否与正则表达式相匹配
            //str = "541a00d571e04ab1aa0768c4054bcf3d&&螺丝&&45#&&1405011&&库存商品_螺丝_45#&&10000&&8.9977";
            //str += "###a18068fde5414574bde249390c821c28&&创维彩电&&43Ee&&1405021&&库存商品_创维彩电_43Ee&&50&&18.88";
            //str += "###cc182491847d4652aab0d59e46ccc5f1&&钢板&&98#合金&&1403011009&&库存商品_钢板_98#合金&&100&&9.99";


            String[] arr = str.split("###");
            List<InvoiceBody> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                String row = arr[i];
                InvoiceBody vb = new InvoiceBody();
                String[] text = row.split("&&");
                for (int j = 0; j < text.length; j++) {
                    String field = text[j];
                    if (field == null || "null".equals(field) || field.trim().equals("")) {
                        field = null;
                    } else {
                        field = field.trim().replace(" ", "");
                    }
                    String index = String.valueOf(j);
                    switch (index) {
                        case "0":
                            vb.setInvoiceBID(field);
                            break;
                        case "1":
                            vb.setComName(field);
                            break;
                        case "2":
                            vb.setSpec(field);
                            break;
                        case "3":
                            //科目编码
                            vb.setSub_code(field);
                            break;
                        case "4":
                            //科目全名称
                            vb.setSub_full_name(field);
                            break;
                        case "5":
                            Double number = field == null ? null : Double.valueOf(field);
                            vb.setNnumber(number);
                            break;
                        case "6":
                            Double price = field == null ? null : Double.valueOf(field);
                            vb.setNprice(price);
                            break;
                        default:
                            break;
                    }
                }
                String comNameSpec = null;
                String comName = vb.getComName(); // 定义商品规格名称
                String spec = vb.getSpec();
                if (!StringUtil.isEmpty(comName) && !StringUtil.isEmpty(spec)) {
                    comNameSpec = comName + "-" + spec;
                } else if (!StringUtil.isEmpty(comName)) {
                    comNameSpec = comName;
                }
                vb.setComNameSpec(comNameSpec);
                //字段设置为之后 更新到数据库
                int num = invoiceDao.editInvoice(vb);
                System.out.println(num);
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    // type等于1为导入操作 type等于2为保持操作 type等于3为删除操作
    @Transactional(rollbackFor = BusinessException.class)
    @SuppressWarnings("unused")
    @Override
    public int upMappingrecord(String accountID, String period, String invoiceType, int type) throws BusinessException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("accountID", accountID);
        hashMap.put("period", period);
        hashMap.put("invoiceType", invoiceType);
        try {
            InvoiceMappingrecord record = invoiceMappingDao.queryMappingrecord(hashMap);
            if (record == null) {
                // type为1的话没有查询到导入记录就添加并且把保存状态置为0
                int insert = -666;
                if (type == 1) {
                    record = getRecordObject(accountID, period, invoiceType, 0, "0");//创建发票记录操作对象
                    insert = invoiceMappingDao.insertMappingrecord(record);
                } else if (type == 2) {
                    record = getRecordObject(accountID, period, invoiceType, 1, "1");
                    insert = invoiceMappingDao.insertMappingrecord(record);
                } else if (type == 3) {
                    System.out.println(1);
                }
                return insert;
            } else {
                // type为1的话有导入历史 先把本期的的映射保持状态设置为0
                String is_upload_save = record.getIs_upload_save();
                if (type == 1) {
                    hashMap.put("is_upload_save", "0");
                } else if (type == 2) {
                    hashMap.put("is_upload_save", "1");
                    Integer save_num = record.getSave_num();
                    save_num = save_num == null ? 0 : save_num; // 累计保存次数
                    hashMap.put("save_num", ++save_num);
                } else if (type == 3) {
                    hashMap.put("is_upload_save", "0");
                }
                // 更新用主键
                hashMap.put("mr_id", record.getMr_id());
                int update = invoiceMappingDao.updateMappingrecord(hashMap);
                return update;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("InvoiceServiceImpl upMappingrecord 更新异常");
        }

    }

    @SuppressWarnings("unused")
    @Override
    //页面刷新 重新查询发票映射状态 如果没有保持 查询所有发票数据以弹窗形式显示出来让用户确认
    public Map<String, Object> queryInvoiceMapping(Map<String, Object> param) throws BusinessException {

        try {
            Map<String, Object> retMap = new HashMap<>();
            String accountID = param.get("accountID").toString();
            String period = param.get("period").toString();

            Map<String, Object> statuMap = queryInvoiceMappingStatu(accountID, period);

            String codeStatu = (String) statuMap.get("code");
            String invoiceTypeStatu = (String) statuMap.get("invoiceType");
            String msg = (String) statuMap.get("msg"); //返回描述
            if (StringUtil.isEmpty(codeStatu) || StringUtil.isEmpty(invoiceTypeStatu)) {
                throw new BusinessException("InvoiceServiceImpl queryqueryInvoiceMapping [codeStatu null invoiceTypeStatu null]");
            }
            //codeStatu = 0 映射未完成,codeStatu= 1 检查通过
            if (codeStatu.equals("0")) {
                String prompt = ""; //提示变量  友好显示在页面中给用户提示
                //invoiceTypeStatu=1进项未完成映射 ,invoiceTypeStatu=2销项未完成映射
                if (invoiceTypeStatu.equals("1")) {
                    prompt = "检测到您导入的进项发票还没有对科目进行确认,请先确认";
                } else {
                    prompt = "检测到您导入的销项发票还没有对科目进行确认,请先确认";
                }
                List<Map<String, Object>> mappingDate = getMappingDate(accountID, period, invoiceTypeStatu);
                Collections.sort(mappingDate, new compartImportdate());
                retMap.put("code", "0");
                retMap.put("msg", prompt);
                retMap.put("invoiceType", invoiceTypeStatu);
                retMap.put("result", mappingDate);
                return retMap;
                //}else if(codeStatu.equals("1")){
            } else {
                retMap.put("code", "1");
                retMap.put("invoiceType", invoiceTypeStatu);
                retMap.put("result", msg);
                return retMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    @SuppressWarnings("unused")
    @Override
    //来自一键生成凭证必须传递参数invoiceType
    //页面刷新 重新查询发票映射状态 如果没有保持 查询所有发票数据以弹窗形式显示出来让用户确认
    public Map<String, Object> queryInvoiceMapping(String accountID, String period, String invoiceType) throws BusinessException {
        try {
            //invoiceType=1进项未完成映射 ,invoiceType=2销项未完成映射
            Map<String, Object> retMap = new HashMap<>();
            String prompt = Integer.valueOf(invoiceType) == 1 ? "检测到您导入的进项发票还没有对科目进行确认,请先确认" : "检测到您导入的销项发票还没有对科目进行确认,请先确认";
            List<Map<String, Object>> mappingDate = getMappingDate(accountID, period, invoiceType);
            Collections.sort(mappingDate, new compartImportdate());
            retMap.put("code", "0");
            retMap.put("msg", prompt);
            retMap.put("invoiceType", invoiceType);
            retMap.put("result", mappingDate);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @SuppressWarnings("unused")
    @Override
    public Map<String, Object> queryInvoiceMappingStatu(String accountID, String period) throws BusinessException {

        //先判断进项映射是否完成 再判断销项映射是否完成
        try {
            Map<String, Object> retMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("accountID", accountID);
            param.put("period", period);
            boolean aa = false;
            param.put("invoiceType", "1"); // 查询进项
            // select count(*) from t_fa_invoice_b where accountID=#{accountID} and period=#{period} and invoiceType = #{invoiceType}
            int jx_num = invoiceDao.queryInvobCount(param);
            //jx_num > 0 标识用户已经导入过进项发票
            if (jx_num > 0) {
                // select * from t_fa_invoice_mappingrecord where accountID =  #{accountID} and period = #{period} and invoiceType =  #{invoiceType}
                InvoiceMappingrecord record = invoiceMappingDao.queryMappingrecord(param);
                //record 为空标识用户导入过进项发票但是还没有保存映射后的科目数据
                if (record == null) {
                    record = getRecordObject(accountID, period, "1", 0, "0");
                    int num = invoiceMappingDao.insertMappingrecord(record);
                    aa = true;
                } else {
                    String is_upload_save = record.getIs_upload_save();
                    //is_upload_save == 1 标识已经记录到用户保存过进项发票映射数据
                    if (is_upload_save != null && is_upload_save.equals("1")) {
                        System.out.println("进项已经映射完成  下一步查询销项是否映射完成");
                    } else {
                        // is_upload_save==null 或者 不等于1 说明还没有保存数据
                        aa = true;
                    }
                }
            }

            //aa == true 标识 上面检查结果为进项还没有映射
            if (aa == true) {
                retMap.put("code", "0");
                retMap.put("invoiceType", "1");
                retMap.put("msg", "未映射完成");
                return retMap;
            } else {
                //进项映射检查完成没有发现问题
                //下面开始检查销项映射是否完成
                boolean bb = false;
                param.put("invoiceType", "2");
                int xx_num = invoiceDao.queryInvobCount(param); // 查询销项
                if (xx_num > 0) {
                    InvoiceMappingrecord record = invoiceMappingDao.queryMappingrecord(param);
                    // record == null 有导入销项发票但是还没有保存
                    if (record == null) {
                        record = getRecordObject(accountID, period, "2", 0, "0");
                        int num = invoiceMappingDao.insertMappingrecord(record);
                        retMap.put("code", "0");
                        retMap.put("msg", "销项映射未完成");
                        retMap.put("invoiceType", "2");
                        return retMap;

                    } else {
                        String is_upload_save = record.getIs_upload_save();
                        if (is_upload_save != null && is_upload_save.equals("1")) {
                            // 有导入销项发票并且有保存
                            retMap.put("code", "1");
                            retMap.put("msg", "销项映射完成");
                            retMap.put("invoiceType", "2");
                            return retMap;
                        } else {
                            // 有导入销项发票但是还没有保存
                            retMap.put("code", "0");
                            retMap.put("msg", "销项映射未完成");
                            retMap.put("invoiceType", "2");
                            return retMap;
                        }
                    }
                } else {
                    // 没有查询到销项发票导入数据
                    String ss = "进项映射检查通过：";

                    if (jx_num > 0) {
                        ss += "进项映射已经完成";
                    }
                    if (jx_num == 0) {
                        ss += "没有导入进项发票";
                    }
                    retMap.put("code", "1");
                    retMap.put("msg", ss + ",销项映射检查通过:没有导入销项发票");
                    retMap.put("invoiceType", "2");
                    return retMap;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @SuppressWarnings("unused")
    @Override
    //小规模查询销项发票导入是否已经映射完成
    //input  output
    public Map<String, Object> queryOutPutInvoiceMappingStatu(String accountID, String period) throws BusinessException {

        //先判断进项映射是否完成 再判断销项映射是否完成
        try {
            Map<String, Object> retMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("accountID", accountID);
            param.put("period", period);
            param.put("invoiceType", "2");
            int xx_num = invoiceDao.queryInvobCount(param); // 查询导入的销项发票数量
            if (xx_num > 0) {
                InvoiceMappingrecord record = invoiceMappingDao.queryMappingrecord(param);
                // record == null 有导入销项发票但是还没有保存
                if (record == null) {
                    record = getRecordObject(accountID, period, "2", 0, "0");
                    int num = invoiceMappingDao.insertMappingrecord(record);
                    retMap.put("code", "0");
                    retMap.put("msg", "销项映射未完成");
                    retMap.put("invoiceType", "2");
                    return retMap;

                } else {
                    String is_upload_save = record.getIs_upload_save();
                    if (is_upload_save != null && is_upload_save.equals("1")) {
                        // 有导入销项发票并且有保存
                        retMap.put("code", "1");
                        retMap.put("msg", "销项映射完成");
                        retMap.put("invoiceType", "2");
                        return retMap;
                    } else {
                        // 有导入销项发票但是还没有保存
                        retMap.put("code", "0");
                        retMap.put("msg", "销项映射未完成");
                        retMap.put("invoiceType", "2");
                        return retMap;
                    }
                }
            }
            retMap.put("code", "1");
            retMap.put("msg", "销项映射检查通过");
            retMap.put("invoiceType", "2");
            return retMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    private InvoiceMappingrecord getRecordObject(String accountID, String period, String invoiceType, int Save_num, String is_upload_save) {
        InvoiceMappingrecord record;
        record = new InvoiceMappingrecord();
        String uuid = UUIDUtils.getUUID();
        record.setMr_id(uuid);
        record.setAccountID(accountID);
        record.setPeriod(period);
        record.setSaveDate(new Date());
        record.setInvoiceType(invoiceType);
        record.setSave_num(Save_num);
        record.setIs_upload_save(is_upload_save);
        return record;
    }

    private List<Map<String, Object>> getMappingDate(String accountID, String period, String invoice_type) throws BusinessException {

        try {
            List<Map<String, Object>> mappingList = new ArrayList<>();
            // String invoice_type = "2";
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountID);
            hashMap.put("period", period);
            hashMap.put("invoiceType", invoice_type);
            // 查询发票头全部数据
            List<InvoiceHead> h_list = invoiceDao.queryInvoiceHAll(hashMap);
            if (h_list == null) {
                throw new BusinessException("invoice queryInvoiceMapping h_list null");
            }
            HashMap<String, InvoiceHead> hMap = new HashMap<>();
            for (InvoiceHead vh : h_list) {
                if (vh == null) {
                    throw new BusinessException("invoiceService queryInvoiceMapping  vh == null");
                }
                hMap.put(vh.getInvoiceHID(), vh);
            }

            // select * from t_fa_invoice_b where accountID = #{accountID} and period = #{period} and invoiceType = #{invoiceType}
            List<InvoiceBody> b_list = invoiceDao.queryInvoiceBAll(hashMap);
            if (b_list == null) {
                throw new BusinessException("invoiceService queryInvoiceMapping b_list null");
            }
            if (invoice_type.equals("1")) {
                // 进项 发票头与发票体是一对一
                if (b_list.size() != h_list.size()) {
                    throw new BusinessException("invoiceService queryInvoiceMapping  b_list.size()!=hMap.size()");
                }
            }
            for (InvoiceBody invoiceBody : b_list) {
                String invoiceHID = invoiceBody.getInvoiceHID();
                String invoiceType = invoiceBody.getInvoiceType();
                InvoiceHead invoiceHead = hMap.get(invoiceHID);
                if (!invoiceHead.getInvoiceType().equals(invoiceType)) {
                    throw new BusinessException("invoiceService queryInvoiceMapping  !invoiceHead.getInvoiceType().equals(invoiceType)");
                }
                getInvoiceMapping(invoiceHead, invoiceBody, mappingList);
            }
            Account account = new Account();
            account.setAccountID(accountID);
            mappingSubcode(account, period, mappingList);
            return mappingList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // 删除发票数据
    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void deleteInvoice(String invoiceBIDs, String type) throws BusinessException {
        try {

            Set<String> hashSet = new HashSet<>();
            Map<String, Object> param = new HashMap<>();
            String[] arr = invoiceBIDs.split(",");

            for (int i = 0; i < arr.length; i++) {
                String hbid = arr[i];
                String[] split = hbid.split("-");
                if (split == null || split.length != 2) {
                    throw new BusinessException(invoiceBIDs + "数据错误");
                }
                String bid = split[0];
                String hid = split[1];
                param.put("invoiceBID", bid);
                int num = invoiceDao.deleteInvoiceB(param);
                // hid总数统计
                hashSet.add(hid);
            }
            param.clear();
            List<String> list = new ArrayList<>();
            if ("1".equals(type)) {
                list.addAll(hashSet);
            } else {
                for (String hid : hashSet) {
                    param.put("invoiceHID", hid);
                    Integer num = invoiceDao.queryInvoiceBCountByHid(param);
                    if (num == null || num == 0) {
                        list.add(hid);
                    }
                }
            }
            if (!list.isEmpty()) {
                int num = delHidToInvoiceH(list);
                System.out.println(num);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private int delHidToInvoiceH(List<String> list) throws BusinessException {
        try {
            Map<String, Object> param = new HashMap<>();
            int num = -1;
            if (list.size() == 1) {
                param.put("invoiceHID", list.get(0));// 单条删除
                num = invoiceDao.deleteInvoiceH(param);
            } else {
                num = invoiceDao.deleteInvoiceHBath(list); // 批量删除
            }
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

}

class compartImportdate implements Comparator<Map<String, Object>> {
    @Override
    public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
        Long log0 = (long) arg0.get("importDate");
        Long log1 = (long) arg1.get("importDate");
        return log0.compareTo(log1);
    }
}
