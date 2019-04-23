package com.wqb.service.tempType.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.BusinessException;
import com.wqb.common.Constrants;
import com.wqb.common.StringUtil;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.tempType.TempTypeDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.Account;
import com.wqb.model.SubjectMessage;
import com.wqb.model.TempType;
import com.wqb.model.vomodel.TempVo;
import com.wqb.service.tempType.TempTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Service("tempTypeService")
public class TempTypeServiceImpl implements TempTypeService {

    @Autowired
    VatDao vatDao;
    @Autowired
    TempTypeDao tempTypeDao;
    @Autowired
    AccountDao accountDao;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Integer insertTempType(String aid, String tempName) throws BusinessException {
        try {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("parentIDA", "1");
            paramMap.put("accountID", aid);
            int count = tempTypeDao.queryTempCount(paramMap);
            if (count > Constrants.TEMP_COUNT_NUM) {
                throw new BusinessException("新增模板类别不能大于30个");
            }

            TempType tmp = new TempType();
            tmp.setAccountID(aid);

            tmp.setTempSoure(1); // 自加
            tmp.setTempName(tempName);
            tmp.setParentID(0);
            tmp.setAssistName(null);

            tmp.setVbContent(null);
            tmp.setSaveAmount(null);

            tmp.setUpdateB(System.currentTimeMillis());
            tmp.setUpdateA(new Date());
            tmp.setDes("新增");
            Integer num = tempTypeDao.insertTempType(tmp);
            System.out.println("isnert num=" + num);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)

    public Map<String, Object> saveTempVoucher(String aid, String tempName, String assistName, String pid,
                                               String saveAmount, String content) throws BusinessException {
        try {
            Map<String, Object> res = new HashMap<>();
            if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(assistName) || StringUtil.isEmpty(tempName)
                    || StringUtil.isEmpty(content) || StringUtil.isEmpty(saveAmount)) {
                Map<String, String> prit = new HashMap<>();
                prit.put("tempName", tempName);
                prit.put("assistName", assistName);
                prit.put("pid", pid);
                prit.put("content", content);
                prit.put("saveAmount", saveAmount);
                throw new BusinessException("saveTempVoucher param  error," + prit.toString());
            }
            checkContent(saveAmount, content);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("parentIDB", "1");
            paramMap.put("accountID", aid);
            int count = tempTypeDao.queryTempCount(paramMap);
            if (count > Constrants.TEMP_COUNT_NUM) {
                res.put("code", "2");
                res.put("msg", "新增凭证模板不能大于30个");
                return res;
            }

            Map<String, Object> paramMap2 = new HashMap<>();
            paramMap2.put("parentID", pid);
            paramMap2.put("assistName", assistName);
            int count2 = tempTypeDao.queryTempCount(paramMap2);
            if (count2 > 0) {
                res.put("code", "3");
                res.put("msg", "模板分类下面存在重复名称,不能添加");
                return res;
            }


            TempType tmp = new TempType();
            tmp.setAccountID(aid);
            tmp.setTempSoure(1);
            tmp.setAssistName(assistName);
            tmp.setTempName(tempName);
            tmp.setParentID(Integer.valueOf(pid));
            Integer sa = Integer.valueOf(saveAmount) == 0 ? 0 : 1;
            tmp.setSaveAmount(sa);
            tmp.setVbContent(content);
            tmp.setDes("新增");
            tmp.setUpdateB(System.currentTimeMillis());
            tmp.setUpdateA(new Date());
            Integer num = tempTypeDao.insertTempType(tmp);
            if (num == 0) {
                throw new BusinessException("添加失败");
            }
            res.put("code", "0");
            res.put("msg", "保存模板成功");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    //检查模板内容
    private void checkContent(String saveAmount, String content) throws BusinessException {
        Gson gson = new Gson();
        TypeToken<List<Map<String, String>>> token = new TypeToken<List<Map<String, String>>>() {
        };
        List<Map<String, String>> fromJson = gson.fromJson(content, token.getType());
        String[] arr = {"subCode", "subName", "dir"};
        for (Map<String, String> maps : fromJson) {
            for (int i = 0; i < arr.length; i++) {
                if (StringUtil.objEmpty(maps.get(arr[i]))) {
                    if (arr[i].equals("dir") && saveAmount.equals("0")) {

                    } else {
                        throw new BusinessException("content is null ,occurrence reason " + arr[i] + "is null,contentJson = " + content);
                    }

                }
            }
        }
    }

    @Override
    public List<TempType> queryTemp(Map<String, Object> map) throws BusinessException {
        List<TempType> list = tempTypeDao.queryTemp(map);
        return list;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> delTem(String tmid) throws BusinessException {
        Map<String, Object> res = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("tempID", tmid);

            List<TempType> queryTemp = tempTypeDao.queryTemp(map);
            if (queryTemp == null) {
                throw new BusinessException("tempTypeService delTem queryTemp==null");
            }
            Integer parentID = queryTemp.get(0).getParentID();

            if (parentID != 0) {
                int delTem = tempTypeDao.delTem(map);
                res.put("code", "0");
                res.put("msg", delTem);
                return res;
            } else {

                Map<String, Object> qrmap = new HashMap<>();
                qrmap.put("parentID", tmid);
                int num = tempTypeDao.queryTempCount(qrmap);
                if (num == 0) {
                    int delTem = tempTypeDao.delTem(map);
                    res.put("code", "0");
                    res.put("msg", delTem);
                    return res;
                } else {
                    res.put("code", "1");
                    res.put("msg", "该模板已经被使用,不能被删除,请先删除下面分类模板");
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> upTem(Integer tempID, String tempName) throws BusinessException {

        try {
            Map<String, Object> res = new HashMap<>();

            Map<String, Object> qrmap = new HashMap<>();
            qrmap.put("tempID", tempID);
            qrmap.put("tempName", tempName);

            int upTem = tempTypeDao.upTem(qrmap);
            if (upTem == 0) {
                throw new BusinessException("upTem==0 更新失败");
            }
            if (upTem == 1) {
                qrmap.remove("tempID");
                qrmap.put("parentID", tempID);
                int num = tempTypeDao.upTem(qrmap);
            }

            res.put("code", "0");
            res.put("msg", "success");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void generateTemplate(String accountID) throws BusinessException {

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("accountID", accountID);

            int tempCount = tempTypeDao.queryTempCount(map);
            if (tempCount > 0) {
                return;
            }
            int querySubCount = tempTypeDao.querySubCount(map);
            if (querySubCount < 90) {
                return;
            }
            Map<String, String> queryGroupSub = tempTypeDao.queryGroupSub(map);
            if (queryGroupSub == null || queryGroupSub.isEmpty() || StringUtil.isEmpty(queryGroupSub.get("account_period"))) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",queryGroupSub==null,queryGroupSub=" + queryGroupSub + ",map=" + map);
            }

            Map<String, Object> map1 = new HashMap<>();
            map1.put("accountID", accountID);
            map1.put("period", queryGroupSub.get("account_period"));

            List<SubjectMessage> allSub = tempTypeDao.queryAllSub(map1);
            if (allSub == null || allSub.isEmpty()) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",allSub==null  allSub.isEmpty()" + ",map1=" + map1);
            }

            Map<String, SubjectMessage> subMap = new HashMap<>();
            for (int j = 0; j < allSub.size(); j++) {
                SubjectMessage sm = allSub.get(j);
                subMap.put(sm.getSub_code(), sm);
            }

            if (subMap.size() != allSub.size()) {
                List<SubjectMessage> arrayList = new ArrayList<>();
                arrayList.addAll(subMap.values());
                allSub = arrayList;
            }

            SubjectMessage bankSub1002 = null;
            int len = 0;
            for (SubjectMessage sub : allSub) {
                if (sub.getSub_code().startsWith("1002")) {
                    int length = sub.getSub_code().length();
                    if (length > len) {
                        bankSub1002 = sub;
                        len = length;
                    }
                }
            }
            if (bankSub1002 == null) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",not find 1002银行存款" + ",map1=" + map1);
            }

            len = 0;
            SubjectMessage pay2211 = null;
            for (SubjectMessage sub : allSub) {
                if (sub.getSub_code().startsWith("2211")) {
                    int length = sub.getSub_code().length();
                    if (length > len) {
                        pay2211 = sub;
                        len = length;
                    }
                }
            }
            if (pay2211 == null) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",not find 职工薪酬" + pay2211 + ",map1=" + map1);
            }

            len = 0;
            SubjectMessage commodity1405 = null;
            for (SubjectMessage sub : allSub) {
                if (sub.getSub_code().startsWith("1405")) {
                    int length = sub.getSub_code().length();
                    if (length > len) {
                        commodity1405 = sub;
                        len = length;
                    }
                }
            }
            if (commodity1405 == null) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",not find 1405, is null" + ",map1=" + map1);
            }

            SubjectMessage sub660123 = null;
            List<SubjectMessage> arr = new ArrayList<>();
            for (SubjectMessage sub : allSub) {
                String sub_code = sub.getSub_code();
                if (sub_code.startsWith("6601") || sub_code.startsWith("6602") || sub_code.startsWith("6603")) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (SubjectMessage sub2 : allSub) {
                        if (sub2.getSub_code().startsWith(sub_code)) {
                            arrayList.add(sub_code);
                            if (arrayList.size() > 1) {
                                break;
                            }
                        }
                    }
                    if (arrayList.size() == 1) {
                        arr.add(sub);
                    }
                }
            }
            if (arr.isEmpty()) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",6601 6602 6603 arr.isEmpty()" + ",map1=" + map1);
            }

            outterLoop:
            for (int j = 0; j < arr.size(); j++) {
                SubjectMessage sub66 = arr.get(j);
                String sub_name = sub66.getSub_name();
                String[] rrr = {"水电", "租", "物业", "利息", "电话", "差旅", "住宿", "招待", "交通", "车", "办公", "服务", "手续", "维护", "通讯", "车辆", "交通", "福利", "社保", "其它", "快递"};
                for (int k = 0; k < rrr.length; k++) {
                    if (sub_name.contains(rrr[k])) {
                        sub660123 = sub66;
                        break outterLoop;
                    }
                }
            }

            if (sub660123 == null) {
                len = 0;
                for (int j = 0; j < arr.size(); j++) {
                    SubjectMessage sub66 = arr.get(j);
                    int length = sub66.getSub_code().length();
                    if (length > len) {
                        sub660123 = sub66;
                        len = length;
                    }
                }
            }
            if (sub660123 == null) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",660123 is null" + ",map1=" + map1);
            }

            // 1生成工资 大类

            TempType tempType0 = createA("工资", accountID);
            Integer num0 = tempTypeDao.insertTempType(tempType0);
            if (num0 == null || num0 == 0) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",生成工资A error num0=" + num0 + ",accountID=" + accountID + ",map=" + map);
            }

            TempType tmp0 = createB("工资", "发放工资", accountID, tempType0.getTempID());

            TempVo tempVo01 = createTempVo("发放工资", "2", bankSub1002);
            TempVo tempVo02 = createTempVo("发放工资", "1", pay2211);
            String content0 = generateTempContent(tempVo01, tempVo02);

            tmp0.setVbContent(content0);
            Integer num02 = tempTypeDao.insertTempType(tmp0);
            if (num02 == 0) {
                String errs = "line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",发放工资B 添加失败 accountID=" + accountID + ",content0=" + content0 + ",map=" + map);
            }

            TempType tempType11 = createA("采购", accountID);
            Integer num11 = tempTypeDao.insertTempType(tempType11);
            if (num11 == null || num11 == 0) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",生成采购模板A  insertTempType error num11=" + num11 + ",accountID=" + accountID + ",map=" + map);
            }

            TempType tmp11 = createB("采购", "采购货物", accountID, tempType11.getTempID());

            TempVo tempVo11 = createTempVo("采购货物", "2", bankSub1002);
            TempVo tempVo21 = createTempVo("采购货物", "1", commodity1405);
            String content11 = generateTempContent(tempVo11, tempVo21);

            tmp11.setVbContent(content11);
            Integer num12 = tempTypeDao.insertTempType(tmp11);
            if (num12 == 0) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",采购货物 B添加失败 accountID=" + accountID + ",content11=" + content11 + ",map=" + map);
            }

            TempType tempType33 = createA("日常开支", accountID);
            Integer num31 = tempTypeDao.insertTempType(tempType33);
            if (num31 == null || num31 == 0) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",生成日常开支模板A  insertTempType error num31=" + num31 + ",accountID=" + accountID + ",map=" + map);
            }

            String sub_name = sub660123.getSub_name();
            TempType tmp33 = createB("日常开支", sub_name, accountID, tempType33.getTempID());
            TempVo tempVo31 = createTempVo(sub_name, "2", bankSub1002);
            TempVo tempVo32 = createTempVo(sub_name, "1", sub660123);
            String content33 = generateTempContent(tempVo31, tempVo32);
            tmp33.setVbContent(content33);
            Integer num32 = tempTypeDao.insertTempType(tmp33);
            if (num32 == 0) {
                String errs = " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException("errs=" + errs + ",日常开支 B添加失败 accountID=" + accountID + ",content33=" + content33 + ",map=" + map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    //批量生成模板
    @Override
    //@Transactional(rollbackFor = BusinessException.class)
    public void bathGenerateTemplate() throws BusinessException {
        // 1查询所有账套数据

        // 2查询该账套 的期间查询所有科目

        // 给每个账套生成三个模板
        // 6001销售费用 6602管理费用 6603财务费用
        // a 工资 发放工资 1002 银行存款 有子级取子级 没有子级取一级 ，2211 有子级取子级 没有子级取一级 应付职工薪酬
        // b 日常支出 1002 银行存款 有子级取子级 没有子级取一级 查询66开头的有没有 【水电 房租
        // 物业，利息。电话，差旅，住宿，招待,交通,车辆】包含的科目名称
        // c 采购货物 1002 银行存款 有子级取子级 没有子级取一级 ，1405 下面选一个子级
        try {

            //测试发现mybits map字段为空的话，没有结果返回
            //Map<String, Object> queryUser = tempTypeDao.queryUser();


            Map<String, Object> map = new HashMap<>();
            map.put("kkk", "kkk");
            List<Account> list = accountDao.queryAccByCondition(map);
            //list.size() == 2598
            map.clear();
            Map<String, Object> map1 = new HashMap<>();

            // 根据账套循环生产模板
            for (int i = 0; i < list.size(); i++) {

                Account account = list.get(i);
                String accountID = account.getAccountID();


                if (StringUtil.isEmpty(accountID)) {
                    throw new BusinessException("StringUtil.isEmpty(accountID)");
                }

                map.put("accountID", accountID);
                int querySubCount = tempTypeDao.querySubCount(map);
                if (querySubCount < 90) {
                    continue;
                }


                Map<String, String> queryGroupSub = tempTypeDao.queryGroupSub(map);
                if (queryGroupSub == null || queryGroupSub.isEmpty() || StringUtil.isEmpty(queryGroupSub.get("account_period"))) {
                    System.out.println("map=" + map + ",account" + account);
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",queryGroupSub==null,queryGroupSub=" + queryGroupSub + ",map=" + map);
                }

                String period = queryGroupSub.get("account_period");
                map1.put("accountID", accountID);
                map1.put("period", period);

                List<SubjectMessage> allSub = tempTypeDao.queryAllSub(map1);
                if (allSub == null || allSub.isEmpty()) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",allSub==null  allSub.isEmpty()" + ",map1=" + map1);
                }

                //allSub 数据里面有重复的科目 需要去重处理
                Map<String, SubjectMessage> subMap = new HashMap<>();
                for (int j = 0; j < allSub.size(); j++) {
                    SubjectMessage sm = allSub.get(j);
                    SubjectMessage subjectMessage = subMap.get(sm.getSub_code());
                    if (subjectMessage == null) {
                        subMap.put(sm.getSub_code(), sm);
                    }
                }

                if (subMap.size() != allSub.size()) {
                    List<SubjectMessage> arrayList = new ArrayList<>();
                    arrayList.addAll(subMap.values());
                    allSub = arrayList;
                    Collections.sort(allSub, new Comparator<SubjectMessage>() {
                        @Override
                        public int compare(SubjectMessage arg0, SubjectMessage arg1) {
                            return arg0.getSub_code().compareTo(arg1.getSub_code());
                        }
                    });
                }

                // 根据科目自动生成模板
                // 6001销售费用 6602管理费用 6603财务费用
                // a 工资 发放工资 1002 银行存款 有子级取子级 没有子级取一级 ，2211 有子级取子级 没有子级取一级
                // 应付职工薪酬
                // b 日常支出 1002 银行存款 有子级取子级 没有子级取一级 查询66开头的有没有 【水电 房租
                // 物业，利息。电话，差旅，住宿，招待,交通,车辆】包含的科目名称
                // c 采购货物 1002 银行存款 有子级取子级 没有子级取一级 ，1405 下面选一个子级

                // 1确定1002 银行存款科目
                SubjectMessage bankSub1002 = null;
                int len = 0;
                for (SubjectMessage sub : allSub) {
                    if (sub.getSub_code().startsWith("1002")) {
                        int length = sub.getSub_code().length();
                        if (length > len) {
                            bankSub1002 = sub;
                            len = length;
                        }
                    }
                }
                if (bankSub1002 == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",not find 1002银行存款" + ",map1=" + map1);
                }

                // 1确定职工薪酬2211科目
                len = 0;
                SubjectMessage pay2211 = null;
                for (SubjectMessage sub : allSub) {
                    if (sub.getSub_code().startsWith("2211")) {
                        int length = sub.getSub_code().length();
                        if (length > len) {
                            pay2211 = sub;
                            len = length;
                        }
                    }
                }
                if (pay2211 == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",not find 职工薪酬" + pay2211 + ",map1=" + map1);
                }

                // 3确定1405 商品科目
                len = 0;
                SubjectMessage commodity1405 = null;
                for (SubjectMessage sub : allSub) {
                    if (sub.getSub_code().startsWith("1405")) {
                        int length = sub.getSub_code().length();
                        if (length > len) {
                            commodity1405 = sub;
                            len = length;
                        }
                    }
                }
                if (commodity1405 == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",not find 1405, is null" + ",map1=" + map1);
                }

                // 4确定日常开支 6601 6602 6603 这三个科目
                SubjectMessage sub660123 = null;
                List<SubjectMessage> arr = new ArrayList<>();// 定义末级科目集合对象
                for (SubjectMessage sub : allSub) {
                    String sub_code = sub.getSub_code();
                    if (sub_code.startsWith("6601") || sub_code.startsWith("6602") || sub_code.startsWith("6603")) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (SubjectMessage sub2 : allSub) {
                            String sub_code2 = sub2.getSub_code();
                            if (sub_code2.startsWith(sub_code)) {
                                arrayList.add(sub_code);
                                if (arrayList.size() > 1) {
                                    break;
                                }
                            }
                        }
                        if (arrayList.size() == 1) {
                            arr.add(sub); // 确定为末级科目
                        }
                    }
                }
                if (arr.isEmpty()) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",6601 6602 6603 arr.isEmpty()" + ",map1=" + map1);
                }

                outterLoop:
                for (int j = 0; j < arr.size(); j++) {
                    SubjectMessage sub66 = arr.get(j);
                    String sub_name = sub66.getSub_name();
                    // 水电 房租 物业，利息。电话，差旅，住宿，招待,交通,车辆
                    String[] rrr = {"水电", "租", "物业", "利息", "电话", "差旅", "住宿", "招待", "交通", "车", "办公", "服务", "手续", "维护", "通讯", "车辆", "交通", "福利", "社保", "其它", "快递"};
                    for (int k = 0; k < rrr.length; k++) {
                        String zy = rrr[k];
                        if (sub_name.contains(zy)) {
                            sub660123 = sub66;
                            break outterLoop;
                        }
                    }
                }

                if (sub660123 == null) {
                    len = 0;
                    for (int j = 0; j < arr.size(); j++) {
                        SubjectMessage sub66 = arr.get(j);
                        int length = sub66.getSub_code().length();
                        if (length > len) {
                            sub660123 = sub66;
                            len = length;
                        }
                    }
                }
                if (sub660123 == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",660123 is null" + ",map1=" + map1);
                }

                // 4 找到1002 银行存款科目 bankSub1002 ， 1405库存商品科目 commodity1405 ，2211
                // 职工薪酬科目 pay2211，sub660123 660123 管理费用科目 开始生产模板啦

                //查询账套是否拥有凭证数据

                List<TempVo> salaryList = null;
                List<TempVo> goodsList = null;
                int countVB = tempTypeDao.queryCountVB(map);
                if (countVB > 2) {
                    List<TempVo> salaryVB = tempTypeDao.querySalaryVB(map);
                    List<TempVo> goodsVB = tempTypeDao.queryGoodsVB(map);
                    if (salaryVB != null && salaryVB.size() > 1) {
                        salaryList = salaryVB;
                    }
                    if (goodsVB != null && goodsVB.size() > 1) {
                        goodsList = goodsVB;
                    }
                }


                // 1生成工资 大类模板
                TempType tempType0 = createA("工资", accountID);
                Integer num0 = tempTypeDao.insertTempType(tempType0);
                if (num0 == null || num0 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",生成工资A error num0=" + num0 + ",accountID=" + accountID + ",map=" + map);
                }

                TempType tmp0 = createB("工资", "发放工资", accountID, tempType0.getTempID());

                String content0 = null;
                if (salaryList == null || salaryList.size() < 2) {
                    TempVo tempVo01 = createTempVo("发放工资", "2", bankSub1002);
                    TempVo tempVo02 = createTempVo("发放工资", "1", pay2211);
                    content0 = generateTempContent(tempVo01, tempVo02);
                } else {
                    boolean flg = false;
                    Map<String, String> res = generateTempContent(1, salaryList);
                    if (res.get("code").toString().equals("0")) {
                        int length = res.get("content").length();
                        if (length < 4800) {
                            flg = true;
                        }
                    }
                    if (flg == true) {
                        content0 = res.get("content");
                    } else {
                        TempVo tempVo01 = createTempVo("发放工资", "2", bankSub1002);
                        TempVo tempVo02 = createTempVo("发放工资", "1", pay2211);
                        content0 = generateTempContent(tempVo01, tempVo02);
                    }
                }

                tmp0.setVbContent(content0);
                Integer num02 = tempTypeDao.insertTempType(tmp0);
                if (num02 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",发放工资B 添加失败 accountID=" + accountID + ",content0=" + content0 + ",map=" + map);
                }


                // 2生成采购模板
                TempType tempType11 = createA("采购", accountID);
                Integer num11 = tempTypeDao.insertTempType(tempType11);
                if (num11 == null || num11 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",生成采购模板A  insertTempType error num11=" + num11 + ",accountID=" + accountID + ",map=" + map);
                }

                TempType tmp11 = createB("采购", "采购货物", accountID, tempType11.getTempID());

                String content11 = null;
                if (goodsList == null || goodsList.size() < 2) {
                    TempVo tempVo11 = createTempVo("采购货物", "2", bankSub1002);
                    TempVo tempVo21 = createTempVo("采购货物", "1", commodity1405);
                    content11 = generateTempContent(tempVo11, tempVo21);
                } else {

                    boolean flg = false;
                    Map<String, String> res = generateTempContent(1, goodsList);
                    if (res.get("code").toString().equals("0")) {
                        int length = res.get("content").length();
                        if (length < 4800) {
                            flg = true;
                        }
                    }
                    if (flg == true) {
                        content11 = res.get("content");
                    } else {
                        TempVo tempVo11 = createTempVo("采购货物", "2", bankSub1002);
                        TempVo tempVo21 = createTempVo("采购货物", "1", commodity1405);
                        content11 = generateTempContent(tempVo11, tempVo21);
                    }
                }

                tmp11.setVbContent(content11);
                Integer num12 = tempTypeDao.insertTempType(tmp11);
                if (num12 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",采购货物 B添加失败 accountID=" + accountID + ",content11=" + content11 + ",map=" + map);
                }


                // 3生成日常开支模板
                TempType tempType33 = createA("日常开支", accountID);
                Integer num31 = tempTypeDao.insertTempType(tempType33);
                if (num31 == null || num31 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",生成日常开支模板A  insertTempType error num31=" + num31 + ",accountID=" + accountID + ",map=" + map);
                }

                String sub_name = sub660123.getSub_name();
                TempType tmp33 = createB("日常开支", sub_name, accountID, tempType33.getTempID());
                TempVo tempVo31 = createTempVo(sub_name, "2", bankSub1002);
                TempVo tempVo32 = createTempVo(sub_name, "1", sub660123);
                String content33 = generateTempContent(tempVo31, tempVo32);
                tmp33.setVbContent(content33);
                Integer num32 = tempTypeDao.insertTempType(tmp33);
                if (num32 == 0) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException("errs=" + errs + ",日常开支 B添加失败 accountID=" + accountID + ",content33=" + content33 + ",map=" + map);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    private Map<String, String> generateTempContent(int type, List<TempVo> arr) {
        Map<String, String> hashMap = new HashMap<>();

        try {

            String zy = "";
            for (int i = 0; i < arr.size(); i++) {
                TempVo tempVo = arr.get(i);
                if (tempVo == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",tempVo==null");
                }
                if (StringUtil.isEmpty(tempVo.getSubCode())) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",tempVo.getSubCode()==null");
                }
                if (StringUtil.isEmpty(tempVo.getSubName())) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",tempVo.getSubName()==null");
                }
                if (StringUtil.isEmpty(tempVo.getDir())) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",tempVo.getDir()==null");
                }

                if (!StringUtil.isEmpty(tempVo.getZy())) {
                    zy = tempVo.getZy();
                }
            }

            if (StringUtil.isEmpty(zy)) {
                if (type == 1) {
                    zy = "发放工资";
                } else if (type == 2) {
                    zy = "采购货物";
                }
                //String errs = "Class: " + this.getClass().getName() + "method: "+ Thread.currentThread().getStackTrace()[1].getMethodName() + " line:"+ Thread.currentThread().getStackTrace()[1].getLineNumber();
                //throw new BusinessException(errs+",StringUtil.isEmpty(zy)==null");
            }

            for (TempVo tempVo : arr) {
                tempVo.setZy(zy);
                tempVo.setPrice("");
                tempVo.setNumber("");
            }

            String json = new Gson().toJson(arr);
            hashMap.put("code", "0");
            hashMap.put("content", json);
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.put("code", "1");
            return hashMap;
        }
    }

    private String generateTempContent(TempVo tempVo01, TempVo tempVo02) {
        List<TempVo> arr = new ArrayList<>();
        arr.add(tempVo01);
        arr.add(tempVo02);
        String content0 = new Gson().toJson(arr);
        return content0;
    }

    private TempType createB(String TempName, String assistName, String accountID, Integer num) {
        TempType tmp2 = new TempType();
        tmp2.setAccountID(accountID);
        tmp2.setAssistName(assistName);
        tmp2.setTempName(TempName);
        tmp2.setParentID(num); // 外键id
        tmp2.setSaveAmount(0);
        tmp2.setDes("built-in B");
        tmp2.setTempSoure(0);
        tmp2.setUpdateB(System.currentTimeMillis());
        tmp2.setUpdateA(new Date());
        return tmp2;
    }

    private TempType createA(String tempName, String accountID) {
        TempType tempType1 = new TempType();

        tempType1.setAccountID(accountID);
        tempType1.setTempName(tempName);

        tempType1.setTempSoure(0); // 内置

        tempType1.setParentID(0);
        tempType1.setAssistName(null);

        tempType1.setVbContent(null);
        tempType1.setSaveAmount(null);

        tempType1.setUpdateB(System.currentTimeMillis());
        tempType1.setUpdateA(new Date());
        tempType1.setDes("built-in A");
        return tempType1;
    }

    private TempVo createTempVo(String zy, String dir, SubjectMessage sub) {
        TempVo tempVo1 = new TempVo();
        tempVo1.setZy(zy);
        tempVo1.setSubCode(sub.getSub_code());
        tempVo1.setSubName(sub.getFull_name()); //科目全名
        tempVo1.setDir(dir);
        tempVo1.setNumber("");
        tempVo1.setPrice("");
        tempVo1.setAmount("");
        return tempVo1;
    }

}
