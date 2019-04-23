package com.wqb.service.KcCommodity.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.model.KcCommodity;
import com.wqb.model.Page;
import com.wqb.model.SubjectMessage;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.vomodel.RedisSub;
import com.wqb.service.KcCommodity.KcCommodityService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("kcCommodityService")
public class KcCommodityServiceImpl implements KcCommodityService {

    @Autowired
    KcCommodityDao kcCommodityDao;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadService voucherHeadService;
    @Autowired
    VoucherBodyDao voucherBodyDao;
    @Autowired
    InvoiceDao invoiceDao;

    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> insertCommodity(List<Map<String, Object>> list, Map<String, Object> map)
            throws BusinessException {

        Integer companyType = (Integer) map.get("companyType");

        try {


            kcCommodityDao.delInitCommodity(map);

            Map<String, Object> rev = new HashMap<>();
            rev.put("message", "fail");
            List<KcCommodity> listCommodity = new ArrayList();  //定义库存商品集合

            vatService.subinit(map);

            Map<String, Object> createMap = new HashMap<>();

            boolean c = false;

            String period = map.get("period").toString();
            String accountID = map.get("accountID").toString();
            String userName = (String) map.get("userName");
            String month = period.split("-")[1];
            if (month.equals("01"))
                c = true;

            List<String> ids = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {

                Map<String, Object> mm = list.get(i);
                if (mm == null || mm.isEmpty())
                    continue;


                String SubcomName = StringUtil.objToStr1(mm.get("map3"));
                String importSubcode = StringUtil.objToStr(mm.get("map2"));

                String str = null;

                if (SubcomName == null)
                    str = "第" + (i + 1) + "商品名称不能为空,请填写!";
                else if (StringUtil.isEmpty(importSubcode))
                    str = "第" + (i + 1) + "行," + SubcomName + "没有发现对应的科目编码,请确认";
                else if (importSubcode != null && importSubcode.length() > 10)
                    str = "第" + (i + 1) + "行," + SubcomName + "科目编码不能超过十位数";
                if (str != null) {
                    rev.put("result", str);
                    return rev;
                }

                if (!importSubcode.startsWith("1405") && !importSubcode.startsWith("1403")) {
                    rev.put("result", importSubcode + "异常，数量金额表不支持1405 1403开头的科目");
                    return rev;
                }

                String[] arr5 = SubcomName.split(" - ");
                if (arr5.length > 3) {
                    rev.put("result", "科目级别不能大于三位");
                    return rev;
                }

                if (importSubcode.length() > 4 && arr5.length == 1) {
                    rev.put("result", importSubcode + " 科目名称与模本格式不符合,请修改为全名,请仔细检查");
                    return rev;
                }


                Double qm_num = StringUtil.objToDoubleIsNuLL(mm.get("map17"));

                Double qm_amount = StringUtil.objToDoubleIsNuLL(mm.get("map19"));
                if ((qm_amount == 0.0 && qm_num != 0.0) || (qm_amount != 0.0 && qm_num == 0.0)) {
                    String s1 = (qm_amount == 0.0) ? "请修改期末结余金额" : "请修改期末结余数量";
                    str = "第" + (i + 1) + "行，[" + SubcomName + "]异常，期末结余金额为:" + qm_amount + ",期末结余数量为:" + qm_num + ";" + s1;
                    rev.put("result", str);
                    return rev;
                }
                ids.add(importSubcode);
            }


            Map<String, String> allSubMap = new HashMap<>();
            List<SubjectMessage> allSub = null;
            if (!ids.isEmpty()) {
                map.put("ids", ids);
                allSub = kcCommodityDao.queryAllSubByImpoerCode(map);
                map.remove("ids");
                if (allSub != null && allSub.size() > 0)
                    for (SubjectMessage sub : allSub)
                        if (sub != null)
                            allSubMap.put(sub.getExcel_import_code(), sub.getSub_code());
            }


            String codeStr = kcCommodityDao.queryAllSub14(map);
            if (StringUtil.isEmpty(codeStr)) {
                rev.put("result", "科目余额表未查询到科目");
                return rev;
            }

            String[] split = codeStr.split(",");
            HashSet<String> hashSet = new HashSet<>();
            for (int j = 0; j < split.length; j++) {
                String code1 = split[j];
                if (code1 != null) {
                    if (code1.startsWith("1405") || code1.startsWith("1403")) {
                        if (!code1.equals("1405") && !code1.equals("1403")) {
                            hashSet.add(code1);
                        }
                    }
                }
            }

            List<String> arr2 = new ArrayList<>();
            for (String code2 : hashSet) {
                List<String> arr1 = new ArrayList<>();
                for (String cdeo3 : hashSet)

                    if (cdeo3.startsWith(code2)) {
                        arr1.add(code2);
                        if (arr1.size() > 1) {
                            break;
                        }

                    }
                if (arr1.size() == 1) {
                    arr2.add(code2);
                }

            }
            if (arr2.size() == 0) {
                rev.put("result", "KcCommodityServiceImpl arr2.size()==0");
                return rev;
            }


            List<String> arr3 = new ArrayList<>();
            for (int i = 0; i < arr2.size(); i++) {
                String code3 = arr2.get(i);
                boolean mm = false;
                for (int j = 0; j < ids.size(); j++) {
                    String code4 = ids.get(j);
                    if (code3.equals(code4)) {
                        mm = true;
                        break;
                    }
                }
                if (mm == false)
                    arr3.add(code3);
            }

            if (arr3.size() > 0) {
                StringBuffer bf = new StringBuffer();
                for (int i = 0; i < arr3.size(); i++) {
                    String code5 = arr3.get(i);
                    if (i > 10) {
                        bf.append("\n");
                    }
                    if (i == (arr3.size() - 1)) {
                        bf.append(code5);
                    } else {
                        bf.append(code5).append(",");
                    }
                }
                String string = bf.toString();
                rev.put("result", string + "缺失,请到科目余额表检查，并添加到要导入的数量金额表里面去");
                return rev;
            }


            List<SubjectMessage> excelCode1405 = getSubMess(map, 1405);

            List<SubjectMessage> excelCode1403 = getSubMess(map, 1403);

            if (excelCode1405 == null && excelCode1403 == null) {
                throw new BusinessException("确认是否导入初始化科目？如未导入请导入");
            }


            Set<String> subCode1405 = getCodeSet(excelCode1405);
            Set<String> subCode1403 = getCodeSet(excelCode1403);

            int len2 = kcCommodityDao.querySubLevel(accountID);

            len2 = len2 > 0 ? len2 : 7;

            int count = 0;

            for (int i = 0; i < list.size(); i++) {

                KcCommodity comm = new KcCommodity();

                Map<String, Object> commMap = list.get(i);
                // 主键
                comm.setComID(UUIDUtils.getUUID());
                // 账套ID
                comm.setAccountID(accountID);
                // 做账期间
                comm.setPeriod(period);
                // 起始期间
                comm.setStartPeriod(StringUtil.objToStr(commMap.get("map0")));
                // 截止期间
                comm.setEndPeriod(StringUtil.objToStr(commMap.get("map1")));
                // 科目代码
                comm.setImportSubcode(StringUtil.objToStr(commMap.get("map2")));
                // 科目名称
                String SubcomName = StringUtil.objToStr1(commMap.get("map3"));

                // 借贷方向
                comm.setDirection(StringUtil.objToStr(commMap.get("map4")));

                //导入的数据是上个月的，做账期间会相差一个月，所以要做期间转移。

                // 本期收入数量
                comm.setBq_incomeNum(0.0);
                // 本期收入金额
                comm.setBq_incomeAmount(new BigDecimal(0));
                // 本期发出数量
                comm.setBq_issueNum(0.0);
                // 本期发出金额
                comm.setBq_issueAmount(new BigDecimal(0));


                if (c == true) {
                    //如果导入的期间是第一个月的话  本年累计需要清零，从新开始计算
                    comm.setTotal_incomeNum(0.0);
                    comm.setTotal_incomeAmount(new BigDecimal(0));
                    comm.setTotal_issueNum(0.0);
                    comm.setTotal_issueAmount(new BigDecimal(0));
                } else {
                    // 本年累计收入数量
                    comm.setTotal_incomeNum(StringUtil.objToDouble(commMap.get("map12")));
                    // 本年累计收入金额
                    comm.setTotal_incomeAmount(StringUtil.objToBigDecimal(commMap.get("map13")));
                    // 本年累计发出数量
                    comm.setTotal_issueNum(StringUtil.objToDouble(commMap.get("map14")));
                    // 本年累计发出金额
                    comm.setTotal_issueAmount(StringUtil.objToBigDecimal(commMap.get("map15")));
                }

                // 余额借贷方向
                String yedir = StringUtil.objToStr(commMap.get("map16"));

                comm.setBalance_direction(yedir);

                // 期末结存数量
                Double qm_num = StringUtil.objToDouble(commMap.get("map17"));
                // 期末结存单价
                BigDecimal qm_price = StringUtil.objToBigDecimal(commMap.get("map18"));
                // 期末结存金额
                BigDecimal qm_amount = StringUtil.objToBigDecimal(commMap.get("map19"));


                //期末和期初是相同的
                /**********************************************/
                comm.setQm_balanceNum(qm_num);
                comm.setQm_balancePrice(qm_price);
                comm.setQm_balanceAmount(qm_amount);


                // 期初结存数量
                comm.setQc_balanceNum(qm_num);
                // 期初结存单价
                comm.setQc_balancePrice(qm_price);
                // 期初结存金额
                comm.setQc_balanceAmount(qm_amount);
                /**********************************************/

                // 创建人
                comm.setCreatePsnID(map.get("userID").toString());
                // 创建时间
                comm.setCreateDate(new Date());
                // 创建人
                comm.setCreatePsn(userName);
                // 修改人ID
                comm.setUpdatePsnID(map.get("userID").toString());
                // 修改时间
                comm.setUpdatedate(new Date());
                // 修改人
                comm.setUpdatePsn(userName);
                // 说明备注
                comm.setDes("金额初始化导入");
                // 导入时间
                comm.setImportDate(new Date());
                //结转时间
                comm.setBalanceDate(null);

                //商品名称
                String comNameSpec = "";
                //导入的科目编码
                String importSubcode = comm.getImportSubcode();

				/*String aaa = "库存商品 - 可控硅调光电源 - 100W,24V";
				获取真正的商品名称   可控硅调光电源 - 100W,24V
				 */

                String sub_comName = "";
                //  库存商品 - 创维彩电 - 49E( 36 )6W  , 用  - 分组
                String[] arr = SubcomName.split(" - ");

                int len = arr.length;
                //去空
                for (int j = 0; j < len; j++)
                    arr[j] = arr[j].replace(" ", "");

                //科目级别判断  最多不能大于五级
                if (len > 3) {
                    rev.put("result", "第" + (i + 1) + "行,科目级别不能大于三级");
                    return rev;
                }

                if (len == 2) {
                    comNameSpec = arr[1].replace(" ", "");
                    comm.setComName(comNameSpec);
                    comm.setSpec(null);
                    sub_comName = comNameSpec;
                } else if (len == 3) {
                    comNameSpec = arr[1] + "-" + arr[2];
                    sub_comName = arr[1] + "_" + arr[2];
                    comm.setComName(arr[1]);
                    comm.setSpec(arr[2]);
                } else if (len == 4) {

                    //库存商品-空调-变频空调-aaa
                    //System.out.println( arr[0]+"-"+arr[1]+"-"+arr[2]+"-"+arr[3] );

                } else if (len == 5) {
                    //库存商品-洗衣机-变频洗衣机-xxx-ccc
                    //System.out.println(arr[0]+"-"+arr[1]+"-"+arr[2]+"-"+arr[3]+"-"+arr[4]);
                }

                String parentName = "库存商品_";  //定义一级科目名称

                boolean flg1 = importSubcode.startsWith("1405");
                boolean flg2 = importSubcode.startsWith("1403");
                if (flg1 == true)
                    parentName = "库存商品_";
                if (flg2 == true)
                    parentName = "原材料_";

                comm.setComNameSpec(comNameSpec);                //设置商品名称到数据库  —> 可控硅调光电源-100W,24V
                comm.setSub_comName(parentName.concat(sub_comName));    //库存商品_可控硅调光电源_100W,24V
                comm.setVcunit(null);
                comm.setBq_incomePrice(null);
                comm.setBq_issuePrice(null);
                /*********************** 创建科目 *********************************/

                SubjectMessage sub = null;

                if (importSubcode != null) {
                    String sub_code = allSubMap.get(importSubcode);  //得到数据库转换好的编码
                    if (!StringUtil.isEmpty(sub_code)) {
                        sub = new SubjectMessage();
                        sub.setSub_code(sub_code);
                    } else {
                        map.put("importSubcode", importSubcode);
                        sub = kcCommodityDao.querySubByImpoerSubCode(map);  //根据导入的科目编码 去科目余额表查找是否有导入的科目编码，然后得到我们的编码
                    }
                }

                if (sub != null) {
                    String sub_code = sub.getSub_code();
                    comm.setSub_code(sub.getSub_code());
                } else
                    System.out.println();
                //throw new BusinessException("第" + (i + 1) + "行，["+SubcomName +"]异常，找不到对应的科目编码");

                String importSubcode_new = null;


                /*************************  satrt  科目编码转换成 4 3 3格式 *****************************/
                if (sub == null && importSubcode != null && len <= 3) {

                    if (len == 2) {
                        //  4  2
                        if (importSubcode.length() == 6)
                            importSubcode_new = importSubcode.substring(0, 4).concat("0").concat(importSubcode.substring(4));
                        //	4  3
                        if (importSubcode.length() == 7)
                            importSubcode_new = importSubcode;
                    }

                    if (len == 3) {
                        // 4 3 1
                        // 4 2 2
                        if (importSubcode.length() == 8)
                            if (len2 == 6)
                                importSubcode_new = modifyCode(importSubcode, 3, 5);
                        if (importSubcode.length() == 9) {
                            // 4 4 1
                            //4 2 3
                            if (len2 == 6)
                                importSubcode_new = modifyCode(importSubcode, 3, null);
                            //  4 3 2
                            if (len2 == 7)
                                importSubcode_new = modifyCode(importSubcode, 6, null);
                        }
                    }

                    if (len == 2 || len == 3) {
                        importSubcode_new = importSubcode_new == null ? null : importSubcode_new;
                        comm.setSub_code(importSubcode_new);
                    }
                }

                /*************************  end  科目编码转换成4 3 3格式*****************************/

                // 大于三级科目处理
                if (sub == null && importSubcode != null && len > 3)
                    if (flg1 || flg2) {
                        List<String> list140 = new ArrayList<>();
                        Set<String> set140 = new HashSet<>();
                        if (flg1 && !subCode1405.isEmpty())
                            getBeforeCode(subCode1405, set140, importSubcode, len);  //获取这个编码的所有上级编码
                        else
                            getBeforeCode(subCode1403, set140, importSubcode, len);
                        if (!set140.isEmpty()) {
                            list140.addAll(set140);
                            Collections.sort(list140);
                            String ss = list140.get(0);  //得到一级编码
                            //14050106
                            for (int j = 1; j < len; j++) {   //编码导入的某个编码的所有上级编码
                                String str = list140.get(j).substring(list140.get(j - 1).length());
                                //截取j级编码与上级编码相差的 部分。得到的这个部分就是要补0的部分
                                //分别得到 01和06
                                //重新连接转换好的部分
                                ss = ss.concat(addCode(str));
                            }
                            comm.setSub_code(ss);
                        }
                    }

                // 添加到数据库
                kcCommodityDao.insertCommodity(comm);
                count++;
            }

            String str = vatService.resetCache(map);

            rev.put("message", "success");
            rev.put("result", "导入完毕，共导入" + count + "条数据。");
            return rev;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    private String modifyCode(String importSubcode, Integer a, Integer b) {
        char[] charArray = importSubcode.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < charArray.length; j++)
            if (j == a)
                sb.append(charArray[j]).append('0');
            else if (b != null && b == j)
                sb.append(charArray[j]).append('0');
            else
                sb.append(charArray[j]);
        return sb.toString();
    }


    public List<SubjectMessage> getSubMess(Map<String, Object> map, int code) throws BusinessException {
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", map.get("accountID"));
        param.put("one_code", code);
        return kcCommodityDao.queryExcelCode(param);
    }

    public void getBeforeCode(Set<String> codeSet1, Set<String> codeSet2, String imporeCode, int len) {
        Iterator<String> iterator = codeSet1.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            int num = imporeCode.indexOf(str);  //判断某个导入的科目编码包含的所有上级编码
            if (num != -1)
                codeSet2.add(str);
            if (len == codeSet2.size())
                break;
        }
    }


    public static Set<String> getCodeSet(List<SubjectMessage> list) {
        Set<String> setCode = new HashSet<String>();
        if (list != null && list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                SubjectMessage sub = list.get(i);
                if (sub != null && sub.getExcel_import_code() != null)
                    setCode.add(sub.getExcel_import_code());
            }
        return setCode;
    }


    public static String addCode(String code) throws BusinessException {
        char[] arr = code.toCharArray();
        StringBuilder sb = new StringBuilder();

        if (arr.length > 3)
            throw new BusinessException(code + "不能大于三位");
        if (arr.length == 2)
            sb.append("0").append(code);
        if (arr.length == 1)
            sb.append("00").append(code);
        if (arr.length == 3)
            sb.append(code);
        return sb.toString();
    }

    @Override
    public Page<KcCommodity> queryCommodityList(Map<String, Object> param) throws BusinessException {

        try {

            Page<KcCommodity> page = new Page<>();
            Integer cout = kcCommodityDao.queryCommodityCount(param);
            Integer size = (Integer) param.get("size");
            //int pageSize = page.getPageSize(); // 每页显示条数
            int pageSize = 50; // 每页显示条数
            // 计算总页数
            int pageTotal = cout % pageSize > 0 ? cout / pageSize + 1 : cout / pageSize;
            // 当前页
            Integer currentPage = (Integer) param.get("currentPage");
            page.setCurrentPage(currentPage);
            // 总页数
            page.setPageTotal(pageTotal);
            // 总条数
            page.setRecordTotal(cout);
            // 起始页
            Integer start = (currentPage - 1) * pageSize;
            param.put("start", start); // 起始页
            param.put("size", pageSize);
            // 根据查询条件查询库存商品数据
            List<KcCommodity> list = kcCommodityDao.queryCommodityList(param);
            page.setContent(list);
            return page;

        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    //更新数量金额表
    @SuppressWarnings("unused")
    @Override
    public void updateKccommddityByAddSubMessage(TBasicSubjectMessage subject, String type) throws BusinessException {
        try {
            if (subject == null || subject.getSubCode() == null) {
                return;
            }

            RedisSub redisSub = StringUtil.tBasicSubjectMessageToRedisSub(subject);
            String sss = vatService.addSubToCache(subject.getAccountId(), subject.getAccountPeriod(), redisSub);

            //查询是否存在子级
            //上级科目编码
            String subCode = subject.getSubCode();
            if (!subCode.startsWith("14")) {

                if (type.equals("1")) {
                    Map<String, Object> upMap = new HashMap<>();
                    //查询下面是否有凭证生成 如果有凭证的话修改分录名称
                    upMap.put("accountID", subject.getAccountId());
                    upMap.put("period", subject.getAccountPeriod());
                    upMap.put("subCode", subCode);
                    upMap.put("fullName", subject.getFullName());
                    upMap.put("parentCode", subCode.substring(0, subCode.length() - 3));

                    int total = voucherHeadService.queryCountVouch(upMap);
                    if (total > 0) {
                        int num2 = voucherBodyDao.updeVbByAddSubMessage(upMap);
                        System.out.println(num2);
                    }
                }

                return;
            }

            //1404 材料成本差异  1407商品进销差价  1471存货跌价准备
            if (subCode.startsWith("1404") || subCode.startsWith("1407") || subCode.startsWith("1471")) {
                if (type.equals("1")) {
                    Map<String, Object> upMap = new HashMap<>();
                    //查询下面是否有凭证生成 如果有凭证的话修改分录名称
                    upMap.put("accountID", subject.getAccountId());
                    upMap.put("period", subject.getAccountPeriod());
                    upMap.put("subCode", subCode);
                    upMap.put("fullName", subject.getFullName());
                    upMap.put("parentCode", subCode.substring(0, subCode.length() - 3));

                    int total = voucherHeadService.queryCountVouch(upMap);
                    if (total > 0) {
                        int num2 = voucherBodyDao.updeVbByAddSubMessage(upMap);
                        System.out.println(num2);
                    }
                }
                return;
            }

            String accountPeriod = subject.getAccountPeriod();
            String accountId = subject.getAccountId();
            String subName = subject.getSubName();
            String fullName = subject.getFullName();
            String unit = subject.getUnit(); //计量单位
            int code_level = (subCode.length() - 4) == 0 ? 1 : ((subCode.length() - 4) / 3 + 1);
            String parentCode = subCode.substring(0, subCode.length() - 3);
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountId);
            hashMap.put("period", accountPeriod);
            Date date = new Date();
            KcCommodity comm = null;
            String comID = "";
            if (type.equals("1")) {

                //末级科目下面用户再新增子级科目，原来的末级科目变为父级。那么需要把新增的子级科目进行初始化，期初，本期，本年累计和期末借贷方金额从它的父级转移到新增科目里面去。
                //这样才能体现科目层次关系，对科目进行汇总
                //比如 2221001是一个子级科目， 下面新增子级 2221001001，那么2221001的金额汇总应该来自于这个新建的子级。2221001001的金额新建的时候就应该存在发生额并且等于它的上级

                hashMap.put("sub_code", parentCode);
                List<KcCommodity> list = kcCommodityDao.queryCommByCondition(hashMap);
                //父级科目是一级科目处理
                if (parentCode.equals("1405") || parentCode.equals("1403")) {
                    if (list == null) {
                        comm = new KcCommodity();
                        comm.setDes(parentCode + "下面增加新科目:" + subCode + ":" + DateUtil.getDays());
                    } else {
                        comm = list.get(0);
                        comID = comm.getComID();
                        comm.setDes(parentCode + "下面增加新科目:" + subCode + ":" + DateUtil.getDays());
                    }

                } else {

                    if (list == null || list.isEmpty())
                        throw new BusinessException("parentCode " + parentCode + " 在数量金额表查询结果为null ");
                    comm = list.get(0);
                    comID = comm.getComID();
                    comm.setDes(parentCode + "下面增加新科目:" + subCode + ":" + DateUtil.getDays());
                }

            } else if (type.equals("2")) {
                //不是末级科目下面增加子科目，可以直接新增，无需汇总金额
                comm = new KcCommodity();
                comm.setDes(parentCode + "下面增加新科目:" + subCode + ":" + DateUtil.getDays());
            } else
                System.out.println("nothing");

            String comName = "";
            String[] arr = fullName.split("_");
            // 设置商品名字与规格字段
            if (code_level == 2) {
                comm.setComName(subName);
                comm.setSpec(null);
                comm.setComNameSpec(subName);
            }
            if (code_level > 2) {
                for (int i = 0; i < arr.length; i++)
                    if (i >= 1 && i < arr.length - 1)
                        comName += arr[i];
                comm.setComName(comName);
                comm.setSpec(subName);
                comm.setComNameSpec(comName + "-" + comm.getSpec());
            }


            comm.setComID(UUIDUtils.getUUID());
            comm.setAccountID(accountId);
            comm.setPeriod(accountPeriod);
            comm.setSub_code(subCode);
            comm.setSub_comName(fullName);
            comm.setCreateDate(new Date());
            comm.setUpdatedate(new Date());
            comm.setImportSubcode(null);
            comm.setImportDate(null);
            comm.setBalanceDate(null);
            comm.setVcunit(unit);

            int num = kcCommodityDao.insertCommodity(comm);

            //末级以下再添加子级   添加成功之后要把原来的上级删除掉
            if (type.equals("1") && !StringUtil.isEmpty(comID)) {
                Map<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("comID", comID);
                int delete = kcCommodityDao.delCommodityById(hashMap2);
                System.out.println(delete);
            }
            if (type.equals("1")) {
                //查询下面是否有凭证生成 如果有凭证的话修改分录名称
                hashMap.put("subCode", subCode);
                hashMap.put("fullName", fullName);
                hashMap.put("parentCode", parentCode);

                int total = voucherHeadService.queryCountVouch(hashMap);
                if (total > 0) {
                    int num2 = voucherBodyDao.updeVbByAddSubMessage(hashMap);
                    System.out.println(num2);
                }
            }

        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }


}
