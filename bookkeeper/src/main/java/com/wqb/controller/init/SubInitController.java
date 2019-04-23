package com.wqb.controller.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.UUIDUtils;
import com.wqb.controller.BaseController;
import com.wqb.dao.smart.Smart2newDao;
import com.wqb.model.*;
import com.wqb.service.UserService;
import com.wqb.service.account.AccountService;
import com.wqb.service.exchange.TBasicExchangeRateService;
import com.wqb.service.subexcel.TBasicSubjectExcelService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.subject.TBasicSubjectMappingMiddleService;
import com.wqb.service.subject.TBasicSubjectMappingService;
import com.wqb.service.subject.TBasicSubjectParentService;
import com.wqb.service.vat.VatService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/subinit")
public class SubInitController extends BaseController {
    @Autowired
    /** EXCEL导入的科目 */
            TBasicSubjectExcelService tBasicSubjectExcelService;

    @Autowired
    /** 新会计准则基础科目 */
            TBasicSubjectParentService tBasicSubjectParentService;

    @Autowired
    /** 系统科目 */
            TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    /** 汇率表 */
            TBasicExchangeRateService tBasicExchangeRateService;


    @Autowired
    /** 科目映射关系表 */
            TBasicSubjectMappingMiddleService tBasicSubjectMappingMiddleService;
    @Autowired
    /** 科目映射规则表 */
            TBasicSubjectMappingService tBasicSubjectMappingService;

    @Autowired
    /** 账套信息表 */
            AccountService accountService;

    /**
     * 增值税结转
     */
    @Autowired
    VatService vatService;

    @Autowired
    Smart2newDao smart2newDao;

    private static Log4jLogger logger = Log4jLogger.getLogger(SubInitController.class);

    @Autowired
    private UserService userService;

    public Map<String, Object> getSessions() {
        Map<String, Object> param = new HashMap<String, Object>();
        HttpSession session = getSession();
        @SuppressWarnings("unchecked")
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user");
        Account account = (Account) sessionMap.get("account");
        String userID = user.getUserID();
        param.put("userID", userID);
        String accountID = account.getAccountID();
        param.put("accountID", accountID);
        return param;
    }

    /**
     * @param modelAndView
     * @return ModelAndView 返回类型
     * @Title: init
     * @Description: 初始化页面跳转
     * @date 2017年12月29日 下午2:22:15
     * @author SiLiuDong 司氏旭东
     */
    // 首页点击记账 跳转到这来
    @RequestMapping("/initView")
    public ModelAndView init(ModelAndView modelAndView) {
        modelAndView.setViewName("init/index");
        return modelAndView;
    }

    /**
     * @param modelAndView
     * @return ModelAndView 返回类型
     * @Title: subjectContrast
     * @Description: 科目对照页面跳转
     * @date 2018年1月4日 上午10:13:38
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/subContrastView")
    public ModelAndView subjectContrast(ModelAndView modelAndView) {
        modelAndView.setViewName("init/subjectContrast");
        return modelAndView;
    }

    /**
     * 科目映射页面跳转
     */
    @RequestMapping("/subMappingView")
    public ModelAndView subjectMappingView(ModelAndView modelAndView) {
        modelAndView.setViewName("init/subjectMapping");
        return modelAndView;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: SubContrast
     * @Description: 科目自动对照添加 EXCEL导入的父类科目 （TBasicSubjectParent）与
     * 获取EXCEL导入的科目（TBasicSubjectExcel）
     * @date 2017年12月22日 下午3:21:04
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/subContrast")
    @ResponseBody
    public Map<String, Object> SubContrast() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        boolean isSmart = true;
        List<TBasicSubjectExcel> tBasicSubjectExcelList = new ArrayList<TBasicSubjectExcel>();
        List<TBasicSubjectExcel> tBasicSubjectExcelList2 = new ArrayList<TBasicSubjectExcel>();
        List<TBasicSubjectExcel> tBasicSubjectExcelList3 = new ArrayList<TBasicSubjectExcel>();

        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        try {

            /** 系统原始新会计准则科目 */
            List<TBasicSubjectParent> parentList = tBasicSubjectParentService.querySubParentList(session);

            Map<String, Object> sessions = new SubInitController().getSessions();
            System.out.println(sessions);

            /** 获取EXCEL导入的科目 */
            List<TBasicSubjectExcel> excelList = tBasicSubjectExcelService.querySubExcel(session);
            // Map<String, TBasicSubjectExcel> excelYsMap = new HashMap<String,
            // TBasicSubjectExcel>();
            if (null != excelList && excelList.size() > 0) {
                for (int t = 0; t < excelList.size(); t++) {
                    String subCode = excelList.get(t).getSubCode();
                    String subName = excelList.get(t).getSubName();
                    // excelYsMap.put(subCode, excelList.get(t));
                    if ("6901".equals(subCode) && "以前年度损益调整".equals(subName.replaceAll(" ", ""))) {// 匹配到表示是新会计准则
                        isSmart = false;
                        break;
                    }
                }
            }
            // 获取小企业会计准则和标准会计准则的映射关系
            /*
             * Map<String, String> ysMap = new HashMap<String, String>();
             * List<Smart2new> y2nList = null;
             *
             * if (isSmart) { y2nList = smart2newDao.queryAllSmart2New(); if
             * (y2nList != null && y2nList.size() > 0) { for (int s = 0; s <
             * y2nList.size(); s++) {
             * ysMap.put(y2nList.get(s).getSmartSubCode(),
             * y2nList.get(s).getNewSubCode()); } } }
             */
            Map<String, String> subCodeYsMap = new HashMap<String, String>();
            for (int i = 0; i < parentList.size(); i++) {
                TBasicSubjectParent subParent = parentList.get(i);
                String sysSuNumber = subParent.getSubCode();
                if (sysSuNumber.equals("1012")) {
                    System.out.println("1012尼玛瞎搞");
                }
                String sysSuName = subParent.getSubName();
                for (int j = 0; j < excelList.size(); j++) {
                    TBasicSubjectExcel subExcel = excelList.get(j);
                    String excelSuNumber = subExcel.getSubCode();
                    String subExcelName = subExcel.getSubName();

                    if ((sysSuName.trim().equals(subExcelName.trim()) && sysSuNumber.length() == 4
                            && excelSuNumber.length() == 4)
                            || (isMatch(sysSuName, subExcelName) && excelSuNumber.length() == 4)) {
                        subCodeYsMap.put(excelSuNumber, sysSuNumber);
                    }
                }
            }

            /** 获取系统科目 */
            // List<TBasicSubjectMessage> messageList =
            // tBasicSubjectMessageService.querySubMessageList(session);
            int pl = 0;
            int el = 0;

            // 1.判断 excel导入的和会计准则基础表中的 数据 如果 根据科目名称和科目编码
            // 一样加入tBasicSubjectExcelList集合中
            /** 匹配一级科目 开始 */
            for (int i = 0; i < parentList.size(); i++) {
                TBasicSubjectParent subParent = parentList.get(i);
                String sysSuNumber = subParent.getSubCode();
                String sysSuName = subParent.getSubName();
                // 小企业会计准则
                if (isSmart) {
                    // excelList =
                    // tBasicSubjectExcelService.querySubExcel(session);
                    for (int j = 0; j < excelList.size(); j++) {
                        TBasicSubjectExcel subExcel = excelList.get(j);
                        String excelSuNumber = subExcel.getSubCode();
                        String subName = subExcel.getSubName();
                        // 根据科目编码获取到小企业对应的新会计准则科目
                        String newSubCode = subCodeYsMap.get(excelSuNumber);
                        String debitCreditDirection = null;
                        if (StringUtils.isEmpty(newSubCode)) {
                            continue;
                        } else if (newSubCode.equals(sysSuNumber)) {
                            TBasicSubjectExcel subExcelTemp = new TBasicSubjectExcel();
                            try {
                                // new BigDecimalConverter(new BigDecimal(0));
                                ConvertUtils.register(new BigDecimalConverter(new BigDecimal(0)), BigDecimal.class);
                                BeanUtils.copyProperties(subExcelTemp, subExcel);

                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            // System.out.println("尼玛");
                            debitCreditDirection = subParent.getDebitCreditDirection();
                            // 设置一级excel导入的借贷方向（1.debit借方， 2.credit贷方）
                            subExcelTemp.setDebitCreditDirection(debitCreditDirection);
                            subExcelTemp.setSubCode(newSubCode);
                            subExcelTemp.setSubName(subParent.getSubName());
                            tBasicSubjectExcelList.add(subExcelTemp);
                        }
                    }
                } else {
                    for (int j = 0; j < excelList.size(); j++) {
                        TBasicSubjectExcel subExcel = excelList.get(j);
                        String excelSuNumber = subExcel.getSubCode();
                        String excelSuName = subExcel.getSubName();
                        if (sysSuNumber.equals(excelSuNumber) && sysSuName.equals(excelSuName)) {
                            // 获取系统原始科目借贷方向（1.debit借方， 2.credit贷方）
                            String debitCreditDirection = subParent.getDebitCreditDirection();
                            // 设置一级excel导入的借贷方向（1.debit借方， 2.credit贷方）
                            subExcel.setDebitCreditDirection(debitCreditDirection);
                            tBasicSubjectExcelList.add(subExcel);
                        }
                    }
                }
            }
            /** 匹配一级科目 结束 */
            excelList = tBasicSubjectExcelService.querySubExcel(session);
            /** 按匹配的一级科目编码去匹配所有有科目编码的子集科目 （外币除外） 开始 */
            // 2.判断 excel导入的和tBasicSubjectExcelList集合中的 数据 如果
            // 科目编码（excelSuNumber）以tBasicSubjectExcelList中的科目编码开头的
            // 加入tBasicSubjectExcelList2集合中
            for (int i = 0; i < tBasicSubjectExcelList.size(); i++) {
                TBasicSubjectExcel tbsubExcel = tBasicSubjectExcelList.get(i);
                String subCode = tbsubExcel.getSubCode();
                // 取出一级科目借贷方向（1.debit借方， 2.credit贷方）
                String debitCreditDirection = tbsubExcel.getDebitCreditDirection();
                if (isSmart) {
                    for (int j = 0; j < excelList.size(); j++) {
                        TBasicSubjectExcel subExcel = new TBasicSubjectExcel();
                        ConvertUtils.register(new BigDecimalConverter(new BigDecimal(0)), BigDecimal.class);
                        try {
                            BeanUtils.copyProperties(subExcel, excelList.get(j));
                        } catch (IllegalAccessException e) {

                            e.printStackTrace();
                        } catch (InvocationTargetException e) {

                            e.printStackTrace();
                        }
                        String excelSuNumber = subExcel.getSubCode();
                        excelSuNumber = subCodeYsMap.get(excelSuNumber.substring(0, 4)) + excelSuNumber.substring(4);
                        if (StringUtils.isNotBlank(excelSuNumber) && excelSuNumber.startsWith(subCode)) {
                            // 设置此一级科目子集的借贷方向（1.debit借方， 2.credit贷方）
                            subExcel.setDebitCreditDirection(debitCreditDirection);
                            subExcel.setSubCode(excelSuNumber);
                            if (excelSuNumber != null && excelSuNumber.length() == 4) {
                                subExcel.setSubName(tbsubExcel.getSubName());
                            }
                            tBasicSubjectExcelList2.add(subExcel);
                        } else {
                            continue;
                        }
                    }
                } else {
                    for (int j = 0; j < excelList.size(); j++) {
                        TBasicSubjectExcel subExcel = excelList.get(j);
                        String excelSuNumber = subExcel.getSubCode();
                        if (StringUtils.isNotBlank(excelSuNumber) && excelSuNumber.startsWith(subCode)) {
                            // 设置此一级科目子集的借贷方向（1.debit借方， 2.credit贷方）
                            subExcel.setDebitCreditDirection(debitCreditDirection);
                            subExcel.setSubCode(excelSuNumber);
                            tBasicSubjectExcelList2.add(subExcel);
                        } else {
                            continue;
                        }
                    }
                }
            }
            /*
             * List<TBasicSubjectExcel> tBasicSubjectExcelList2Temp = new
             * ArrayList<TBasicSubjectExcel>(); if (tBasicSubjectExcelList2 !=
             * null && tBasicSubjectExcelList2.size() > 0) { for (int s = 0; s <
             * tBasicSubjectExcelList2.size(); s++) { TBasicSubjectExcel excel =
             * tBasicSubjectExcelList2.get(s);
             * excel.setSubCode(ysMap.get(excel.getSubCode().substring(0, 4)) +
             * excel.getSubCode().substring(4));
             * tBasicSubjectExcelList2Temp.add(excel); } }
             */
            // tBasicSubjectExcelList2 = tBasicSubjectExcelList2Temp;
            /** 按匹配的一级科目编码去匹配所有有科目编码的子集科目 （外币除外） 结束 */

            /** 按匹配已经匹配的科目编码 匹配外币科目（只处理外币） 开始 */
            // 3.判断 excel导入的和tBasicSubjectExcelList2集合中的 数据 如果
            // 同级编码(一个银行多个外币时用到)（siblingsCoding）
            // 以tBasicSubjectExcelList中的科目编码开头的 加入tBasicSubjectExcelList3集合中
            for (int i = 0; i < tBasicSubjectExcelList2.size(); i++) {
                TBasicSubjectExcel tbsubExcel = tBasicSubjectExcelList2.get(i);
                String subCode = tbsubExcel.getSubCode();
                // 已经匹配的科目的借贷方向（1.debit借方， 2.credit贷方）
                String debitCreditDirection = tbsubExcel.getDebitCreditDirection();
                for (int j = 0; j < excelList.size(); j++) {
                    TBasicSubjectExcel subExcel = excelList.get(j);
                    String siblingsCoding = subExcel.getSiblingsCoding(); // 同级编码(一个银行多个外币时用到)
                    if (StringUtils.isNotBlank(siblingsCoding) && siblingsCoding.equals(subCode)) {
                        // 设置此外币科目的借贷方向（1.debit借方， 2.credit贷方）
                        subExcel.setDebitCreditDirection(debitCreditDirection);
                        tBasicSubjectExcelList3.add(subExcel);
                    } else {
                        continue;
                    }
                }
            }
            /** 按匹配已经匹配的科目编码 匹配外币科目（只处理外币） 开始 */

            tBasicSubjectExcelList2.addAll(tBasicSubjectExcelList3);// 把同级科目加入tBasicSubjectExcelList2中

            /** 把所有匹配到的科目（一级+子集+外币）全部封装list 传入service */
            // 执行插入数据库
            if (isSmart) {
                /*
                 * Map<String, TBasicSubjectParent> code2Info = new
                 * HashMap<String, TBasicSubjectParent>(); if (parentList !=
                 * null) { for (int k = 0; k < parentList.size(); k++) { String
                 * subCode = parentList.get(k).getSubCode();
                 * code2Info.put(subCode, parentList.get(k)); } }
                 */
                tBasicSubjectMessageService.addSubMessageExcelList(session, tBasicSubjectExcelList2, subCodeYsMap, null,
                        isSmart);
            } else {
                tBasicSubjectMessageService.addSubMessageExcelList(session, tBasicSubjectExcelList2, null, null, false);
            }
            Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
            List<TBasicSubjectMessage> TBasicSubjectMessageList = (List<TBasicSubjectMessage>) querySubMessage
                    .get("subMessage");

            /** ---------------------------新加 写入一级科目 ***********************/
            List<TBasicSubjectMessage> tBasicSubjectMessageArrayList = new ArrayList<TBasicSubjectMessage>();
            for (TBasicSubjectParent tBasicSubjectParent : parentList) {
                String parentSubCode = tBasicSubjectParent.getSubCode();
                TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
                int i = 0;
                for (TBasicSubjectMessage tBasicSubjectMessages : TBasicSubjectMessageList) {
                    String messageSubCode = tBasicSubjectMessages.getSubCode();
                    if (StringUtils.isNotBlank(messageSubCode)) {
                        if (parentSubCode.equals("1012") || parentSubCode == "1012") {
                            System.out.println(tBasicSubjectParent);
                        }
                        if (StringUtils.isNotBlank(parentSubCode) && messageSubCode.equals(parentSubCode)) {
                            i = 1;
                            break;
                        }
                    }
                }

                if (i == 0) {
                    i = 0;
                    TBasicSubjectMessage tBasicSubjectMessage2 = new TBasicSubjectMessage();
                    String userId = user.getUserID();// 用户id
                    String accountId = account.getAccountID();// 账套id
                    String busDate = account.getUseLastPeriod();
                    String pkSubId = UUIDUtils.getUUID();
                    tBasicSubjectMessage2.setPkSubId(pkSubId);
                    // 做帐的真实期间 年 - 月(帐套启用年-月份）
                    tBasicSubjectMessage2.setAccountPeriod(busDate);

                    tBasicSubjectMessage2.setAccountId(accountId);

                    // 用户ID
                    tBasicSubjectMessage2.setUserId(userId);
                    // 科目代码
                    tBasicSubjectMessage2.setSubCode(tBasicSubjectParent.getSubCode());
                    // 科目名称
                    String subName = tBasicSubjectParent.getSubName();
                    tBasicSubjectMessage2.setSubName(tBasicSubjectParent.getSubName());
                    // 是否多个同级(0无，1有)
                    tBasicSubjectMessage2.setIsMultipleSiblings("0");
                    tBasicSubjectMessage2.setTypeOfCurrency("");
                    tBasicSubjectMessage2.setSuperiorCoding("0");
                    tBasicSubjectMessage2.setFullName(subName);
                    // 更新时间
                    tBasicSubjectMessage2.setUpdateDate(new Date());
                    // 类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                    tBasicSubjectMessage2.setCategory(tBasicSubjectParent.getCategory());
                    // `update_timestamp` char(20) DEFAULT NULL COMMENT '时间戳',
                    // `sub_source` varchar(255) DEFAULT '' COMMENT
                    // '科目来源（导入,新增）',
                    tBasicSubjectMessage2.setSubSource("初始化没有的一级科目--系统再次添加");
                    // `state` varchar(255) DEFAULT '1' COMMENT '
                    // 启用状态(0禁用，1启用）',
                    tBasicSubjectMessage2.setState("1");
                    // `mender` varchar(255) DEFAULT NULL COMMENT '修改者',
                    tBasicSubjectMessage2.setMender(userId);
                    // `measure_state` int(2) DEFAULT '0' COMMENT
                    // '计量单位核算状态(0关闭，1开启）',
                    tBasicSubjectMessage2.setMeasureState(0);
                    // `exchange_rate__state` int(2) DEFAULT '0' COMMENT
                    // '外币设置状态(0关闭，1开启）',
                    tBasicSubjectMessage2.setExchangeRateState(0);
                    // `code_level` int(2) DEFAULT NULL COMMENT '编码级别',
                    tBasicSubjectMessage2.setCodeLevel(1);
                    // 借贷方向（1.debit借方， 2.credit贷方）
                    tBasicSubjectMessage2.setDebitCreditDirection(tBasicSubjectParent.getDebitCreditDirection());
                    tBasicSubjectMessageArrayList.add(tBasicSubjectMessage2);
                }
            }
            if (tBasicSubjectMessageArrayList != null && tBasicSubjectMessageArrayList.size() > 0) {
                Map<String, Object> addSubMessage1 = tBasicSubjectMessageService.addSubMessageList(session,
                        tBasicSubjectMessageArrayList);
                logger.info("初始化没有的一级科目--系统再次添加", addSubMessage1.size());
            }

            querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
            List<TBasicSubjectMessage> tBasicSubjectMessageList2 = (List<TBasicSubjectMessage>) querySubMessage
                    .get("subMessage");

            /** ---------------------------新加 写入一级科目 ***********************/
            if (tBasicSubjectMessageList2.size() < 1) {
                Map<String, Object> addSubMessage = tBasicSubjectMessageService.addSubMessage(session, parentList);
                int code = (int) addSubMessage.get("code");
                if (code == 1) {
                    result.put("msg", "科目都没有匹配到,已恢复原始科目。。。");
                }
                result.put("code", 1);
            }
            // result.putAll(querySubMessage);
            // 重置 缓存
            vatService.resetCache(session);
            // 获取系统科目
            List<TBasicSubjectMessage> sysList = tBasicSubjectMessageService.querySubMessageList(session);
            // 未匹配的信息 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
            excelList = tBasicSubjectExcelService.querySubExcel(session);
            List<TBasicSubjectExcel> matchListTwo = getMatchListThree(sysList, excelList);
            List<TBasicSubjectExcel> matchListTwoTemp = new ArrayList<TBasicSubjectExcel>();
            String hasBanlace = null;
            if (matchListTwo != null && matchListTwo.size() > 0) {
                for (int s = 0; s < matchListTwo.size(); s++) {
                    TBasicSubjectExcel excel = matchListTwo.get(s);
                    BigDecimal bd = excel.getEndingBalanceDebit();
                    BigDecimal bc = excel.getEndingBalanceCredit();
                    if (!((bd == null || bd.doubleValue() == 0) && ((bc == null || bc.doubleValue() == 0)))) {
                        excel.setHasBalance("2");// 有余额
                        hasBanlace = "2";
                    }
                    matchListTwoTemp.add(excel);
                }
            }
            matchListTwo = matchListTwoTemp;
            if (matchListTwo == null || matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() < excelList.size()) {
                result.put("code", "0");// 科目部分匹配
                result.put("msg",
                        "成功匹配上" + (excelList.size() - matchListTwo.size()) + "条,剩余" + matchListTwo.size() + "条未匹配!");
            }
            result.put("hasBanlace", hasBanlace);


        } catch (BusinessException e) {
            result.put("code", "-1");
            result.put("msg", "科目匹配异常!");
            return result;
        }
        return result;
    }

    @RequestMapping("/openInitPage")
    @ResponseBody
    public Map<String, Object> openInitPage() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("code", -1);
            HttpSession session = getSession();
            List<TBasicSubjectMessage> sysList = tBasicSubjectMessageService.querySubMessageList(session);
            // 未匹配的信息 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
            List<TBasicSubjectExcel> excelList = tBasicSubjectExcelService.querySubExcel(session);
            List<TBasicSubjectExcel> matchListTwo = getMatchListThree(sysList, excelList);
            List<TBasicSubjectExcel> matchListTwoTemp = new ArrayList<TBasicSubjectExcel>();
            String hasBanlace = null;
            if (matchListTwo != null && matchListTwo.size() > 0) {
                for (int s = 0; s < matchListTwo.size(); s++) {
                    TBasicSubjectExcel excel = matchListTwo.get(s);
                    BigDecimal bd = excel.getEndingBalanceDebit();
                    BigDecimal bc = excel.getEndingBalanceCredit();
                    if (!((bd == null || bd.doubleValue() == 0) && ((bc == null || bc.doubleValue() == 0)))) {
                        excel.setHasBalance("2");// 有余额
                        hasBanlace = "2";
                    }
                    matchListTwoTemp.add(excel);
                }
            }
            /** 获取系统一级科目代码名称不为空 */
            List<TBasicSubjectMessage> messageList = tBasicSubjectMessageService.querySubMessageLevel(session);

            // 调用 balance方法取试算平衡的值
            Map<String, Object> balance = balance();
            BigDecimal initCreditBalance = new BigDecimal("0.0");
            BigDecimal initDebitBalance = new BigDecimal("0.0");
            initDebitBalance = (BigDecimal) balance.get("initDebitBalance");// 系统
            // 期初借方合计
            initCreditBalance = (BigDecimal) balance.get("initCreditBalance");// 系统
            // 期初贷方合计
            result.put("inittotalDebit", initDebitBalance);// 期初借方金额
            result.put("inittotalCredit", initCreditBalance);// 期初贷方金额

            // 调用科目对照方法 返回 能匹配的 科目信息
            result.put("matchingSubNumber", excelList.size() - matchListTwo.size());// 匹配到的科目数
            result.put("initSubNumber", messageList.size());// 期初科目数
            matchListTwo = matchListTwoTemp;
            if (matchListTwo == null || matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() < excelList.size()) {
                result.put("code", "0");// 科目部分匹配
                result.put("msg",
                        "成功匹配上" + (excelList.size() - matchListTwo.size()) + "条,剩余" + matchListTwo.size() + "条未匹配!");
            }
            result.put("hasBanlace", hasBanlace);
            result.put("code", 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型 调用科目对照方法 返回 能匹配的 科目信息
     * @Title: excelInit
     * @Description: 用excel方式初始化
     * @date 2017年12月21日 下午9:21:11
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/excelInit")
    @ResponseBody
    Map<String, Object> excelInit() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();

            // 获取系统科目
            List<TBasicSubjectMessage> sysList = tBasicSubjectMessageService.querySubMessageList(session);

            // 获取EXCEL导入的科目 ？？？
            List<TBasicSubjectExcel> excelList = tBasicSubjectExcelService.querySubExcel(session);

            // 未匹配的信息 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
            List<TBasicSubjectExcel> matchListTwo = getMatchListThree(sysList, excelList);
            /** 获取系统一级科目代码名称不为空 */
            List<TBasicSubjectMessage> messageList = tBasicSubjectMessageService.querySubMessageLevel(session);

            // 调用 balance方法取试算平衡的值
            Map<String, Object> balance = balance();
            BigDecimal initCreditBalance = new BigDecimal("0.0");
            BigDecimal initDebitBalance = new BigDecimal("0.0");
            initDebitBalance = (BigDecimal) balance.get("initDebitBalance");// 系统
            // 期初借方合计
            initCreditBalance = (BigDecimal) balance.get("initCreditBalance");// 系统
            // 期初贷方合计
            result.put("inittotalDebit", initDebitBalance);// 期初借方金额
            result.put("inittotalCredit", initCreditBalance);// 期初贷方金额

            // 调用科目对照方法 返回 能匹配的 科目信息
            result.put("matchingSubNumber", excelList.size() - matchListTwo.size());// 匹配到的科目数
            result.put("initSubNumber", messageList.size());// 期初科目数

            /** 科目映射关系表 */
            // Map<String, Object> subAutoMapping =
            // tBasicSubjectMappingMiddleService.subAutoMapping(session);
            // Object code = subAutoMapping.get("code");
            result.put("code", 1);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param sysList
     * @param excelList
     * @return List<TBasicSubjectParent> 返回类型 新会计准则
     * @Title: getMatchList
     * @Description: 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
     * @date 2017年12月22日 上午11:09:02
     * @author SiLiuDong 司氏旭东
     */
    private List<TBasicSubjectParent> getMatchList(List<TBasicSubjectParent> sysList,
                                                   List<TBasicSubjectExcel> excelList) {
        List<TBasicSubjectParent> resultList = new ArrayList<TBasicSubjectParent>();
        for (int i = 0; i < sysList.size(); i++) {
            TBasicSubjectParent sysSub = sysList.get(i);
            String sysSuNumber = sysSub.getSubCode();
            String sysSuName = sysSub.getSubName();
            for (int j = 0; j < excelList.size(); j++) {

                TBasicSubjectExcel subExcel = excelList.get(j);
                String excelSuNumber = subExcel.getSubCode();
                String excelSuName = subExcel.getSubName();
                // 如果科目编码和科目名称都能匹配 把excel导入的添加到 系统科目表中
                if (sysSuNumber.equals(excelSuNumber) && sysSuName.equals(excelSuName)) {
                    resultList.add(sysSub);
                } else {
                    continue;
                }
            }
        }
        return resultList;
    }

    /**
     * @param sysMessageList
     * @param excelList
     * @return List<TBasicSubjectParent> 返回类型 新会计准则
     * @Title: getMatchList
     * @Description: 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
     * @date 2017年12月22日 上午11:09:02
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unused")
    private List<TBasicSubjectExcel> getMatchListTwo(List<TBasicSubjectMessage> sysMessageList,
                                                     List<TBasicSubjectExcel> excelList) {
        for (int i = 0; i < sysMessageList.size(); i++) {
            TBasicSubjectMessage sysSub = sysMessageList.get(i);
            // Excel导入的编码
            String sysSuNumber = sysSub.getExcelImportCode();
            String sysSuName = sysSub.getSubName();
            for (int j = 0; j < excelList.size(); j++) {
                TBasicSubjectExcel subExcel = excelList.get(j);
                String excelSuNumber = subExcel.getSubCode();
                String excelSuName = subExcel.getSubName();
                // 如果科目编码和科目名称都能匹配 把excel导入的List中数据删除
                if (StringUtils.isNotBlank(sysSuNumber) && sysSuNumber.equals(excelSuNumber)
                        && sysSuName.equals(excelSuName)) {
                    excelList.remove(subExcel);
                } else if (StringUtils.isBlank(excelSuNumber) || StringUtils.isBlank(excelSuName)) {
                    excelList.remove(subExcel);
                } else {
                    continue;
                }
            }
        }
        return excelList;
    }

    // 朱述渊添加 获取未匹配到的科目列表
    private List<TBasicSubjectExcel> getMatchListThree(List<TBasicSubjectMessage> sysMessageList,
                                                       List<TBasicSubjectExcel> excelList) {
        List<TBasicSubjectExcel> returnList = new ArrayList<TBasicSubjectExcel>();
        for (int i = 0; i < excelList.size(); i++) {
            String excelSubCode = excelList.get(i).getSubCode();
            boolean isMatch = false;
            for (int j = 0; j < sysMessageList.size(); j++) {
                String sysExcelImportCode = sysMessageList.get(j).getExcelImportCode();
                if (excelSubCode.equals(sysExcelImportCode)) {
                    isMatch = true;
                }
            }
            if (!isMatch) {
                returnList.add(excelList.get(i));
            }
        }
        return returnList;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: balance
     * @Description: 试算平衡
     * @date 2018年1月3日 下午4:37:11
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/balance")
    @ResponseBody
    public Map<String, Object> balance() {
        BigDecimal initCreditBalance = new BigDecimal("0.0");
        BigDecimal initDebitBalance = new BigDecimal("0.0");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        try {
            HttpSession session = getSession();

            /** 获取系统科目 */
            Map<String, Object> querySubMessageListMoney = tBasicSubjectMessageService
                    .querySubMessageListMoney(session);
            List<TBasicSubjectMessage> sysList = (List<TBasicSubjectMessage>) querySubMessageListMoney
                    .get("subMessage");
            // tBasicSubjectMessage.getInitCreditBalance(); // 期初余额(贷方)
            // tBasicSubjectMessage.getInitDebitBalance(); // 期初余额(借方)
            for (int i = 0; i < sysList.size(); i++) {
                TBasicSubjectMessage sysSubMes = sysList.get(i);
                BigDecimal initCreditBalance2 = null;
                if (sysSubMes.getInitCreditBalance() != null) {
                    initCreditBalance2 = sysSubMes.getInitCreditBalance().compareTo(new BigDecimal(0E-8)) == 0
                            ? new BigDecimal(0) : sysSubMes.getInitCreditBalance(); // 期初余额(借方)
                } else {
                    initCreditBalance2 = new BigDecimal(0);
                }

                initCreditBalance = initCreditBalance.add(initCreditBalance2); // 期初余额(贷方)
                BigDecimal initDebitBalance2 = null;
                if (sysSubMes.getInitDebitBalance() != null) {
                    initDebitBalance2 = sysSubMes.getInitDebitBalance().compareTo(new BigDecimal(0E-8)) == 0
                            ? new BigDecimal(0) : sysSubMes.getInitDebitBalance(); // 期初余额(借方)
                } else {
                    initDebitBalance2 = new BigDecimal(0);
                }
                initDebitBalance = initDebitBalance.add(initDebitBalance2); // 期初余额(借方)
            }
            BigDecimal bigDecimal = new BigDecimal(2);

            // // 除以二
            // initDebitBalance = initDebitBalance == new BigDecimal(0) ? new
            // BigDecimal(0) : initDebitBalance.divide(bigDecimal, 8,
            // BigDecimal.ROUND_HALF_UP);
            //
            // initCreditBalance = initCreditBalance == new BigDecimal(0) ? new
            // BigDecimal(0) : initCreditBalance.divide(bigDecimal, 8,
            // BigDecimal.ROUND_HALF_UP);

            result.put("initDebitBalance", initDebitBalance);// 系统 期初借方合计
            result.put("initCreditBalance", initCreditBalance);// 系统 期初贷方合计
            BigDecimal beginningCapital = initDebitBalance.subtract(initCreditBalance);// 系统
            // 期初差额
            result.put("beginningCapital", beginningCapital);// 系统 期初差额

            /** 获取EXCEL导入的科目 */
            List<TBasicSubjectExcel> excelList = tBasicSubjectExcelService.querySubExcelMoney(session);
            BigDecimal endingBalanceCredit = new BigDecimal("0.0"); // 期末余额(贷方)
            BigDecimal endingBalanceDebit = new BigDecimal("0.0"); // 期末余额(借方)
            for (int j = 0; j < excelList.size(); j++) {
                TBasicSubjectExcel SubExcel = excelList.get(j);
                BigDecimal endingBalanceCredit2 = SubExcel.getEndingBalanceCredit() == null ? new BigDecimal("0")
                        : SubExcel.getEndingBalanceCredit();
                endingBalanceCredit = endingBalanceCredit.add(endingBalanceCredit2); // 期末余额(贷方)
                BigDecimal endingBalanceDebit2 = SubExcel.getEndingBalanceDebit() == null ? new BigDecimal("0")
                        : SubExcel.getEndingBalanceDebit();
                endingBalanceDebit = endingBalanceDebit.add(endingBalanceDebit2); // 期末余额(借方)
            }
            // // 除以二
            // endingBalanceCredit = endingBalanceCredit == new BigDecimal(0) ?
            // new BigDecimal(0) : endingBalanceCredit.divide(bigDecimal, 8,
            // BigDecimal.ROUND_HALF_UP);
            //
            // endingBalanceDebit = endingBalanceDebit == new BigDecimal(0) ?
            // new BigDecimal(0) : endingBalanceDebit.divide(bigDecimal, 8,
            // BigDecimal.ROUND_HALF_UP);

            result.put("endingBalanceCredit", endingBalanceCredit);// Excel期末余额(贷方)
            result.put("endingBalanceDebit", endingBalanceDebit);// Excel期末余额(借方)
            BigDecimal finalBalance = endingBalanceDebit.subtract(endingBalanceCredit);// Excel期末借贷差额
            result.put("finalBalance", finalBalance);// 期末差额

            /** 比较差异 */
            BigDecimal debitVariance = initDebitBalance.subtract(endingBalanceDebit)
                    .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                    : initDebitBalance.subtract(endingBalanceDebit); // 借方差异
            BigDecimal CreditVariance = initCreditBalance.subtract(endingBalanceCredit)
                    .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                    : initCreditBalance.subtract(endingBalanceCredit);// 贷方差异
            result.put("debitVariance", debitVariance); // 借方差异
            result.put("CreditVariance", CreditVariance); // 贷方差异
            BigDecimal difference = beginningCapital.subtract(finalBalance).compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal(0) : beginningCapital.subtract(finalBalance); // 系统借
            // 减
            // 贷
            // 差异
            // 减去
            // excel导入的
            // 借
            // 减
            // 贷
            // 的差异
            result.put("difference", difference); // 差异之差

            result.put("code", 1); // 返回成功
        } catch (BusinessException e) {
            result.put("message", "SubInitController.balance");
            e.printStackTrace();
        } catch (Exception e) {
            result.put("message", "SubInitController.balance");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: toKmdz
     * @Description: 科目对照
     * @date 2018年1月3日 下午9:45:51
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/toKmdz")
    @ResponseBody
    public Map<String, Object> toKmdz() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();

            // 获取系统科目
            List<TBasicSubjectMessage> sysList = tBasicSubjectMessageService.querySubMessageList(session);

            // 获取EXCEL导入的科目 ？？？
            List<TBasicSubjectExcel> excelList = tBasicSubjectExcelService.querySubExcel(session);

            // 未匹配的信息 获取匹配的科目 根据科目名称和科目编码对照excel导入的和系统原始科目表中信息
            List<TBasicSubjectExcel> matchListTwo = getMatchListThree(sysList, excelList);
            // 未匹配的数量
            result.put("matchListSize", matchListTwo.size());
            // 未匹配的信息
            result.put("excelList", matchListTwo);

            List<TBasicSubjectExcel> matchListTwoTemp = new ArrayList<TBasicSubjectExcel>();
            String hasBanlace = null;
            if (matchListTwo != null && matchListTwo.size() > 0) {
                for (int s = 0; s < matchListTwo.size(); s++) {
                    TBasicSubjectExcel excel = matchListTwo.get(s);
                    BigDecimal bd = excel.getEndingBalanceDebit();
                    BigDecimal bc = excel.getEndingBalanceCredit();
                    if (!((bd == null || bd.doubleValue() == 0) && ((bc == null || bc.doubleValue() == 0)))) {
                        excel.setHasBalance("2");// 有余额
                        hasBanlace = "2";
                    }
                    matchListTwoTemp.add(excel);
                }
            }
            matchListTwo = matchListTwoTemp;
            if (matchListTwo == null || matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() == 0) {
                result.put("code", "1");// 科目全部匹配
                result.put("msg", "科目全部匹配");
            } else if (matchListTwo != null && matchListTwo.size() < excelList.size()) {
                result.put("code", "0");// 科目部分匹配
                result.put("msg",
                        "成功匹配上" + (excelList.size() - matchListTwo.size()) + "条,剩余" + matchListTwo.size() + "条未匹配!");
            }
            result.put("hasBanlace", hasBanlace);
            result.put("sysListSize", sysList.size());
            ArrayList<Object> arrayList = new ArrayList<>();
            for (TBasicSubjectMessage tBasicSubjectMessage : sysList) {
                if (StringUtils.isNotBlank(tBasicSubjectMessage.getExcelImportCode())) {
                    arrayList.add(tBasicSubjectMessage);
                }
            }
            // result.put("sysList", arrayList);
            result.put("sysList", sysList);
            result.put("code", 1);
        } catch (BusinessException e) {
            result.put("message", "fail");
        } catch (Exception e) {
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param subMessage
     * @param subExcels
     * @return Map<String, Object> 返回类型
     * @throws BusinessException
     * @Title: toKmdzAddList
     * @Description: 科目对照匹配不到手动添加
     * @date 2018年1月10日 上午10:59:06
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/toKmdzAddSubMessageList")
    @ResponseBody
    public Map<String, Object> toKmdzAddSubMessageList(String subMessage, String subExcels) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        result.put("code", -1);

        ArrayList<TBasicSubjectMessage> subMessageList = new ArrayList<TBasicSubjectMessage>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id

            String busDate = (String) sessionMap.get("busDate");

            // String parameter = getRequest().getParameter("subMessage");
            // //json string
            // String parameters = getRequest().getParameter("subExcels");

            // String jsonString = "{0=\"外观\",1=\"大厅\"}";
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            // {"pkSubId":"8c708f3baeb8496c9a7408fda86e52c3","subCode":"1001","userId":"31fd7eee802f448193e8ed9e76729a51","accountId":"31160fa7046546578d0a2f64afcd9622","accountPeriod":"","excelImportPeriod":null,"subName":"库存现金","typeOfCurrency":"RMB","initDebitBalance":175212.87,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":330000,"yearAmountCredit":172793.01,"endingBalanceDebit":175212.87,"excelImportCode":"1001","endingBalanceCredit":0,"isMultipleSiblings":"1","excelImportSiblingsCoding":null,"siblingsCoding":null,"excelImportSuperiorCoding":null,"superiorCoding":null,"fullName":"库存现金","updateDate":1515036455000,"updateTimestamp":"1515036455207","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":null,"mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":0,"codeLevel":1}
            Map<String, String> subMessageMap = new HashMap<String, String>();
            subMessageMap = gson.fromJson(subMessage, type);

            // String jsonString =
            // "[{id=\"45\",name=\"tr\"},{id=\"22\",name=\"bb\"}]";
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(subExcels, type2);

            List<Map<String, String>> subExcelList = new ArrayList<>();
            Map<String, String> subExcelsMap = new HashMap<String, String>();
            for (JsonObject jsonObject : jsonObjects) {
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                subExcelsMap = gson.fromJson(jsonObject, type3);
                subExcelList.add(subExcelsMap);
            }
            List<TBasicSubjectMessage> needChgList = new ArrayList<TBasicSubjectMessage>();
            for (int i = 0; i < subExcelList.size(); i++) {
                TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
                TBasicSubjectMessage tBasicSubjectMessage2 = new TBasicSubjectMessage();
                tBasicSubjectMessage2.setAccountId(accountId);
                tBasicSubjectMessage2.setUserId(userId);
                tBasicSubjectMessage2.setAccountPeriod(getUserDate());
                // 上级科目编码
                String superiorCoding = subMessageMap.get("subCode");
                // 科目借贷方向
                String debitCreditDirection = subMessageMap.get("debitCreditDirection");
                tBasicSubjectMessage2.setSuperiorCoding(superiorCoding);

                String codeLevelStr = subMessageMap.get("codeLevel");
                Integer codeLevel = Integer.valueOf(codeLevelStr);
                tBasicSubjectMessage2.setCodeLevel(codeLevel + 1);

                Map<String, String> subExcelMap = subExcelList.get(i);

                /** 主键 */
                String uuid = UUIDUtils.getUUID();
                tBasicSubjectMessage.setPkSubId(uuid);

                // 科目借贷方向
                tBasicSubjectMessage.setDebitCreditDirection(debitCreditDirection);

                /** 用户ID */
                tBasicSubjectMessage.setUserId(userId);

                /** 账套ID */
                tBasicSubjectMessage.setAccountId(accountId);

                /** Excel 期间 */
                String period = subExcelMap.get("subCode");
                tBasicSubjectMessage.setExcelImportPeriod(period);

                /** 帐套 期间 */
                // String accountPeriod = subExcelMap.get("period");
                tBasicSubjectMessage.setAccountPeriod(busDate);

                /** Excel导入 科目代码 */
                String subExcelCode = subExcelMap.get("subCode");
                tBasicSubjectMessage.setExcelImportCode(subExcelCode);

                /** 新 科目代码 */
                String subMessageCode = subMessageMap.get("subCode");

                Map<String, Object> querySubMessageMaxSubCode = tBasicSubjectMessageService
                        .querySubMessageMaxSubCode(session, tBasicSubjectMessage2);
                String subMessageCode1 = (String) querySubMessageMaxSubCode.get("subMessageCode");

                String valueOf = null;
                if (StringUtils.isNotBlank(subMessageCode1)) {
                    int parseInt = Integer.parseInt(subMessageCode1);
                    parseInt += i + 1;
                    valueOf = parseInt + "";
                } else {
                    String valueOf2 = String.valueOf(i + 1);
                    if (i <= 999) {
                        valueOf = subMessageCode1.valueOf(subMessageCode + "00".concat(valueOf2));
                    } else {
                        result.put("msg", "科目编码太大");
                        return result;
                    }
                }

                tBasicSubjectMessage.setSubCode(valueOf);

                /** 科目名称 */
                String subExcelName = subExcelMap.get("subName");
                tBasicSubjectMessage.setSubName(subExcelName);

                /** 科目名称 全名 */
                String subMessageName = subMessageMap.get("subName");
                tBasicSubjectMessage.setFullName(subMessageName + "_" + subExcelName);

                /** 币别 */
                String typeOfCurrency = subExcelMap.get("typeOfCurrency");
                tBasicSubjectMessage.setTypeOfCurrency(typeOfCurrency);

                /** 期初余额(借方) */
                BigDecimal initDebitBalance = new BigDecimal(subExcelMap.get("initDebitBalance"));
                tBasicSubjectMessage.setInitDebitBalance(initDebitBalance);

                /** 期初余额(贷方) */
                BigDecimal initCreditBalance = new BigDecimal(subExcelMap.get("initCreditBalance"));
                tBasicSubjectMessage.setInitCreditBalance(initCreditBalance);

                /** 本期发生额(借方) */
                // BigDecimal currentAmountDebit = new
                // BigDecimal(subExcelMap.get("currentAmountDebit"));
                // tBasicSubjectMessage.setCurrentAmountDebit(currentAmountDebit);

                /** 本期发生额(贷方) */
                // BigDecimal currentAmountCredit = new
                // BigDecimal(subExcelMap.get("currentAmountCredit"));
                // tBasicSubjectMessage.setCurrentAmountCredit(currentAmountCredit);

                /** 本年累计发生额(借方) */
                BigDecimal yearAmountDebit = new BigDecimal(subExcelMap.get("yearAmountDebit"));
                tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);

                /** 本年累计发生额(贷方) */
                BigDecimal yearAmountCredit = new BigDecimal(subExcelMap.get("yearAmountCredit"));
                tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);

                /** 期末余额(借方) */
                BigDecimal endingBalanceDebit = new BigDecimal(subExcelMap.get("endingBalanceDebit"));
                tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebit);

                /** 期末余额(贷方) */
                BigDecimal endingBalanceCredit = new BigDecimal(subExcelMap.get("endingBalanceCredit"));
                tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredit);

                /** 是否多个同级(0无，1有) */
                String isMultipleSiblings = subExcelMap.get("isMultipleSiblings");
                tBasicSubjectMessage.setIsMultipleSiblings(isMultipleSiblings);

                /** 同级编码(一个银行多个外币时用到) */
                // String siblingsCoding = subExcelMap.get("siblingsCoding");
                // tBasicSubjectMessage.setSiblingsCoding(siblingsCoding);

                /** 上级编码(1级为0，二级取前4位） */
                tBasicSubjectMessage.setSuperiorCoding(subMessageCode);

                /** 类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类) */
                String category = subMessageMap.get("category");
                tBasicSubjectMessage.setCategory(category);

                /** 更新时间 */
                tBasicSubjectMessage.setUpdateDate(new Date());

                /** 时间戳 */
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                String format = simpleDateFormat.format(new Date());
                tBasicSubjectMessage.setUpdateTimestamp(format);

                /** 科目来源（导入,新增） */
                tBasicSubjectMessage.setSubSource("Excel数据手动修改");

                /** 启用状态 */
                String state = subMessageMap.get("state");
                tBasicSubjectMessage.setState(state);

                /** 修改者 */
                tBasicSubjectMessage.setMender(userId);

                /** 计量单位主键 */
                String fkTBasicMeasureId = subMessageMap.get("fkTBasicMeasureId");
                tBasicSubjectMessage.setFkTBasicMeasureId(fkTBasicMeasureId);

                /** 计量单位核算状态(0关闭，1开启） */
                String measureStateStr = subMessageMap.get("measureState");
                if (StringUtils.isNotBlank(measureStateStr)) {
                    Integer measureState = Integer.valueOf(measureStateStr);
                    tBasicSubjectMessage.setMeasureState(measureState);
                } else {
                    tBasicSubjectMessage.setMeasureState(0);
                }

                /** 汇率设置主键 */
                String fkExchangeRateId = subMessageMap.get("fkExchangeRateId");
                tBasicSubjectMessage.setFkExchangeRateId(fkExchangeRateId);

                /** 编码级别 */
                if (StringUtils.isNotBlank(codeLevelStr)) {
                    tBasicSubjectMessage.setCodeLevel(codeLevel + 1);
                } else {
                    tBasicSubjectMessage.setCodeLevel(2);
                }

                /** 是否已经匹配到系统科目(0否，1是) */
                // String isMatching = subExcelMap.get("isMatching");
                // tBasicSubjectMessage.setIsMatching(isMatching);
                subMessageList.add(tBasicSubjectMessage);
                /*
                 * int v = 0; for (int s = 0; s < subExcelList.size(); s++) {
                 * String excelCode = subExcelList.get(s).get("subCode"); if
                 * (excelCode.startsWith(subExcelCode)) { v++; } } if (v == 1) {
                 * // 是末级科目 needChgList.add(tBasicSubjectMessage); }
                 */
            }
            result = tBasicSubjectMessageService.addSubMessageList(session, subMessageList);
            /*
             * for (int k = 0; k < subMessageList.size(); k++) {
             * TBasicSubjectMessage tm = subMessageList.get(k); // 正对末级科目变更金额
             * Map<String, Object> para = new HashMap<String, Object>();
             * para.put("direction", tm.getDebitCreditDirection() + "");
             * para.put("initDebitAmount", tm.getInitDebitBalance());
             * para.put("endDebitAmount", tm.getEndingBalanceDebit());
             *
             * para.put("initCreditAmount", tm.getInitCreditBalance());
             * para.put("endCreditAmount", tm.getEndingBalanceCredit());
             * para.put("busDate", getUserDate()); para.put("accountID",
             * getAccount().getAccountID()); String subCode = tm.getSubCode();
             * int length = subCode.length(); int jc = (length - 4) / 3; for
             * (int i = jc - 1; i >= 0; i--) { String tempSub =
             * subCode.substring(0, 4 + i * 3); para.put("subCode", tempSub);
             * tBasicSubjectMessageService.chgSubAmountByAddSub(para); }
             *
             * }
             */
            result.put("code", 1);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: subAutoMapping
     * @Description: 科目自动映射
     * @date 2018年10月30日 下午4:16:27
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/subAutoMapping")
    @ResponseBody
    public Map<String, Object> subAutoMapping() {

        HttpSession session = getSession();
        /** 科目映射规则表 */
        // Map<String, Object> querySubMappingList =
        // tBasicSubjectMappingService.querySubMappingList(session);

        /** 科目映射关系表 */
        Map<String, Object> subAutoMapping = tBasicSubjectMappingMiddleService.subAutoMapping(session);

        return subAutoMapping;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: querySubMappingList
     * @Description: 查询 映射列表
     * @date 2018年11月1日 下午9:36:07
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMappingList")
    @ResponseBody
    Map<String, Object> querySubMappingList() {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            // 查询映射信息表
            Map<String, Object> querySubMappingList = tBasicSubjectMappingService.querySubMappingList(session);
            List<TBasicSubjectMapping> subMappingList = querySubMappingList.get("tBasicSubjectMappings") == null
                    ? new ArrayList<>() : (List) querySubMappingList.get("tBasicSubjectMappings");
            // 查询映射信息中间表
            List<TBasicSubjectMappingMiddle> querySubMappingMiddleList = tBasicSubjectMappingMiddleService
                    .querySubMappingMiddleByAccId(accountId);

            if (subMappingList.size() > 0) {
                List<List<Object>> list = new ArrayList<List<Object>>();
                // 匹配到的
                List<TBasicSubjectMapping> matching = new ArrayList<TBasicSubjectMapping>();
                // 未匹配的
                List<TBasicSubjectMapping> unmatch = new ArrayList<TBasicSubjectMapping>();
                for (TBasicSubjectMapping tBasicSubjectMapping : subMappingList) {
                    String subMappingCode = tBasicSubjectMapping.getSubMappingCode();
                    String subMappingName = tBasicSubjectMapping.getSubMappingName();
                    for (TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle2 : querySubMappingMiddleList) {
                        String subMappingCode2 = tBasicSubjectMappingMiddle2.getSubMappingCode();
                        String subMappingName2 = tBasicSubjectMappingMiddle2.getSubMappingName();
                        // 已经匹配到的
                        if (subMappingCode.equals(subMappingCode2) && subMappingName.equals(subMappingName2)) {
                            List<Object> list2 = new ArrayList<Object>();
                            list2.add(tBasicSubjectMapping);
                            String subMessageCode = tBasicSubjectMappingMiddle2.getSubMessageCode();
                            // 通过帐套期间查询科目编码、
                            Map<String, Object> querySubMessageBySubCode = tBasicSubjectMessageService
                                    .querySubMessageBySubCode(session, subMessageCode);
                            Object object = querySubMessageBySubCode.get("subMessage");
                            List<TBasicSubjectMessage> subMessageList = new ArrayList<TBasicSubjectMessage>();
                            if (object != null && object != "" && !((List) object).isEmpty()) {
                                subMessageList = (List<TBasicSubjectMessage>) object;
                            } else {
                                TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
                                subMessageList.add(tBasicSubjectMessage);
                            }
                            list2.add(subMessageList.get(0));

                            matching.add(tBasicSubjectMapping);
                            list.add(list2);
                        }
                    }
                }
                // 映射科目总数
                result.put("subMappingList.size", subMappingList.size());
                // 映射科目已经匹配数
                result.put("matching.size", matching.size());

                // 未映射科目列表
                List<TBasicSubjectMapping> diffrent5 = getDiffrent5(matching, subMappingList);
                // 映射科目未匹配数
                result.put("unmatch.size", diffrent5.size());
                for (TBasicSubjectMapping tBasicSubjectMapping : diffrent5) {
                    System.out.println(tBasicSubjectMapping);
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(tBasicSubjectMapping);
                    // TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle2 =
                    // new TBasicSubjectMappingMiddle();
                    // list2.add(tBasicSubjectMappingMiddle2);

                    TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
                    list2.add(tBasicSubjectMessage);
                    matching.add(tBasicSubjectMapping);
                    // Object put = map.put(tBasicSubjectMapping,
                    // tBasicSubjectMappingMiddle2);
                    // list.add(put);
                    list.add(list2);
                }

                // 映射列表
                result.put("list", list);
                // addSubMapping(list);
                result.put("code", 1);

            }
        } catch (Exception e) {
            result.put("msg", "获取科目映射列表异常");
            e.printStackTrace();
        }
        return result;
    }

    private static List<TBasicSubjectMapping> getDiffrent5(List<TBasicSubjectMapping> list1,
                                                           List<TBasicSubjectMapping> list2) {
        List<TBasicSubjectMapping> diff = new ArrayList<TBasicSubjectMapping>();
        long start = System.currentTimeMillis();
        Map<TBasicSubjectMapping, Integer> map = new HashMap<TBasicSubjectMapping, Integer>(
                list1.size() + list2.size());
        List<TBasicSubjectMapping> maxList = list1;
        List<TBasicSubjectMapping> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (TBasicSubjectMapping object : maxList) {
            map.put(object, 1);
        }
        for (TBasicSubjectMapping object : minList) {
            Integer count = map.get(object);
            if (count != null) {
                map.put(object, ++count);
                continue;
            }
            map.put(object, 1);
        }
        for (Map.Entry<TBasicSubjectMapping, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        System.out.println("方法5 耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        return diff;

    }

    /**
     * @param subMessageAndSubMapping
     * @return Map<String, Object> 返回类型
     * @Title: addSubMapping
     * @Description: 手动增加科目映射
     * @date 2018年11月1日 下午8:37:54
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addSubMapping")
    @ResponseBody
    public Map<String, Object> addSubMapping(String subMessageAndSubMapping) {
        HttpSession session = getSession();

        // 获取用户信息
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user"); // 获取user信息
        String userId = user.getUserID();// 用户id
        String userName = user.getUserName();
        Account account = (Account) sessionMap.get("account"); // 获取帐套信息
        String accountId = account.getAccountID();// 账套id
        String busDate = (String) sessionMap.get("busDate");

        Map<String, Object> result = new HashMap<String, Object>();
        Gson gson = new Gson();
        result.put("code", -1);
        // [[com.wqb.model.TBasicSubjectMapping@67c5dba3,
        // com.wqb.model.TBasicSubjectMessage@72f9a3fd],
        // [com.wqb.model.TBasicSubjectMapping@67c5dba3,
        // com.wqb.model.TBasicSubjectMessage@54a8c7d],
        // [com.wqb.model.TBasicSubjectMapping@67c5dba3,
        // com.wqb.model.TBasicSubjectMessage@480d0f0e],
        // [com.wqb.model.TBasicSubjectMapping@d8a02cb6,
        // com.wqb.model.TBasicSubjectMessage@5cf1f98c],
        // [com.wqb.model.TBasicSubjectMapping@d8a02cb6,
        // com.wqb.model.TBasicSubjectMessage@63a84fa4],
        // [com.wqb.model.TBasicSubjectMapping@d8a02cb6,
        // com.wqb.model.TBasicSubjectMessage@32d00ec8],
        // [com.wqb.model.TBasicSubjectMapping@48bfeb48,
        // com.wqb.model.TBasicSubjectMessage@120502e3],
        // [com.wqb.model.TBasicSubjectMapping@61a42f79,
        // com.wqb.model.TBasicSubjectMessage@416bb4b6]]
        // [[{"pkSubMappingId":9,"subMappingCode":"2221","subMappingName":"进项税","similarName":"进项税额,进项本期税额","smallScaleTrading":1,"generalTaxpayerTrading":1,"smallScaleProduction":1,"generalTaxpayerProduction":1,"smallScaleImportAndExport":1,"generalTaxpayerImportAndExport":1,"smallScaleHighTech":1,"generalTaxpayerHighTech":1,"createPerson":"管理员","createDate":1540814967000,"updatePerson":null,"updateDate":null,"updateTimestamp":null},{"pkSubId":"c04e70d4cc4b4971b69ee8297b21f72d","subCode":"2221001002","userId":"9860d90ca3604596bafa5e6b8027a7d2","accountId":"0809818119804a4abee53eed0c5c03db","accountPeriod":"2018-06","excelImportPeriod":"1","subName":"进项税","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":68691.35,"yearAmountCredit":68691.35,"endingBalanceDebit":0,"excelImportCode":"222100102","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"2221001","superiorCoding":"2221001","fullName":"应交税费_应交增值税_进项税","updateDate":1540886262000,"updateTimestamp":"1540886262452","category":"2","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"1","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":3,"debitCreditDirection":"2","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null,"taxAmountType":null}],[{"pkSubMappingId":10,"subMappingCode":"2221","subMappingName":"销项税","similarName":"销项税额","smallScaleTrading":1,"generalTaxpayerTrading":1,"smallScaleProduction":1,"generalTaxpayerProduction":1,"smallScaleImportAndExport":1,"generalTaxpayerImportAndExport":1,"smallScaleHighTech":1,"generalTaxpayerHighTech":1,"createPerson":"管理员","createDate":1540814967000,"updatePerson":null,"updateDate":null,"updateTimestamp":null},{"pkSubId":"5a623dd1d5ba48d1a6a5fb03d32fe6e0","subCode":"2221001003","userId":"9860d90ca3604596bafa5e6b8027a7d2","accountId":"0809818119804a4abee53eed0c5c03db","accountPeriod":"2018-06","excelImportPeriod":"1","subName":"销项税","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":66019.7,"yearAmountCredit":66019.7,"endingBalanceDebit":0,"excelImportCode":"222100103","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"2221001","superiorCoding":"2221001","fullName":"应交税费_应交增值税_销项税","updateDate":1540886262000,"updateTimestamp":"1540886262452","category":"2","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"1","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":3,"debitCreditDirection":"2","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null,"taxAmountType":null}],[{"pkSubMappingId":16,"subMappingCode":"2221","subMappingName":"没有我","similarName":"没有我呀","smallScaleTrading":1,"generalTaxpayerTrading":1,"smallScaleProduction":1,"generalTaxpayerProduction":1,"smallScaleImportAndExport":1,"generalTaxpayerImportAndExport":1,"smallScaleHighTech":1,"generalTaxpayerHighTech":1,"createPerson":"管理员","createDate":1540814967000,"updatePerson":null,"updateDate":null,"updateTimestamp":null},{"pkSubId":"e3dc54901f2c407b9009b47c677d4616","subCode":"1002001","userId":"9860d90ca3604596bafa5e6b8027a7d2","accountId":"0809818119804a4abee53eed0c5c03db","accountPeriod":"2018-06","excelImportPeriod":"1","subName":"深圳农村商业银行万丰支行","typeOfCurrency":"","initDebitBalance":39760,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":0,"yearAmountCredit":0,"endingBalanceDebit":39760,"excelImportCode":"1002001","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":"1002","superiorCoding":"1002","fullName":"银行存款_深圳农村商业银行万丰支行","updateDate":1540886262000,"updateTimestamp":"1540886262451","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"1","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":2,"debitCreditDirection":"1","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null,"taxAmountType":null}],[{"pkSubMappingId":18,"subMappingCode":"2202","subMappingName":"没有我2202","similarName":"没有我2202","smallScaleTrading":1,"generalTaxpayerTrading":1,"smallScaleProduction":1,"generalTaxpayerProduction":1,"smallScaleImportAndExport":1,"generalTaxpayerImportAndExport":1,"smallScaleHighTech":1,"generalTaxpayerHighTech":1,"createPerson":"管理员","createDate":1540814967000,"updatePerson":null,"updateDate":null,"updateTimestamp":null},{"pkSubId":"dd8e6f326481469eb2e37c8c6550189e","subCode":"1012","userId":"9860d90ca3604596bafa5e6b8027a7d2","accountId":"0809818119804a4abee53eed0c5c03db","accountPeriod":"2018-06","excelImportPeriod":"1","subName":"其他货币资金","typeOfCurrency":"","initDebitBalance":0,"initCreditBalance":0,"currentAmountDebit":0,"currentAmountCredit":0,"yearAmountDebit":0,"yearAmountCredit":0,"endingBalanceDebit":0,"excelImportCode":"1012","endingBalanceCredit":0,"isMultipleSiblings":"0","excelImportSiblingsCoding":null,"siblingsCoding":null,"siblingsSubName":null,"excelImportSuperiorCoding":null,"superiorCoding":null,"fullName":"其他货币资金","updateDate":1540886262000,"updateTimestamp":"1540886262454","category":"1","subSource":"Excel自动导入","unit":null,"unitId":null,"price":null,"number":null,"amount":null,"state":"1","mender":null,"fkTBasicMeasureId":null,"measureState":null,"fkExchangeRateId":null,"exchangeRateState":1,"codeLevel":1,"debitCreditDirection":"1","amountDebit":null,"documentsType":null,"documentsUnit":null,"documentsUnitId":null,"documentsDecimal":null,"documentsNumber":null,"documentsAmount":null,"taxAmount":null,"taxAmountType":null}]]
        // String jsonString =
        // "[[{id=\"45\",name=\"tr\"},{id=\"22\",name=\"bb\"}],[{id=\"88\",name=\"ll\"},{id=\"99\",name=\"zz\"}]]";
        java.lang.reflect.Type type = new TypeToken<ArrayList<ArrayList<JsonObject>>>() {
        }.getType();
        ArrayList<ArrayList<JsonObject>> jsonObjects = new Gson().fromJson(subMessageAndSubMapping, type);

        // List<List<Object>> objectList = new ArrayList<List<Object>>();
        List<TBasicSubjectMappingMiddle> subMappingMiddleList = new ArrayList<TBasicSubjectMappingMiddle>();
        for (ArrayList<JsonObject> arrayList : jsonObjects) {
            // ArrayList<Object> arr = new ArrayList<>();
            TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = new TBasicSubjectMappingMiddle();
            for (int i = 0; i < arrayList.size(); i++) {
                // {"pkSubMappingId":9,"subMappingCode":"2221","subMappingName":"进项税","similarName":"进项税额,进项本期税额","smallScaleTrading":1,"generalTaxpayerTrading":1,"smallScaleProduction":1,"generalTaxpayerProduction":1,"smallScaleImportAndExport":1,"generalTaxpayerImportAndExport":1,"smallScaleHighTech":1,"generalTaxpayerHighTech":1,"createPerson":"管理员","createDate":1540814967000,"updatePerson":null,"updateDate":null,"updateTimestamp":null}
                // Object fromJson = new Gson().fromJson(jsonObject,
                // Object.class);
                if (i == 0) {
                    Map<String, String> retMap = gson.fromJson(arrayList.get(i), new TypeToken<Map<String, String>>() {
                    }.getType());
                    String subMappingCode = retMap.get("subMappingCode");
                    tBasicSubjectMappingMiddle.setSubMappingCode(subMappingCode);
                    String subMappingName = retMap.get("subMappingName");
                    tBasicSubjectMappingMiddle.setSubMappingName(subMappingName);
                    // arr.add(retMap);
                } else if (i == 1) {
                    Map<String, String> retMap = gson.fromJson(arrayList.get(i), new TypeToken<Map<String, String>>() {
                    }.getType());
                    String subMessageCode = retMap.get("subCode");
                    tBasicSubjectMappingMiddle.setSubMessageCode(subMessageCode);
                    String subMessageName = retMap.get("subName");
                    tBasicSubjectMappingMiddle.setSubMessageName(subMessageName);
                }
            }
            tBasicSubjectMappingMiddle.setCreatePerson(userId);
            tBasicSubjectMappingMiddle.setCreateDate(new Date());
            tBasicSubjectMappingMiddle.setAccountId(accountId);

            // List<TBasicSubjectMappingMiddle> validationSubMappingMiddle =
            // tBasicSubjectMappingMiddleService.validationSubMappingMiddle(tBasicSubjectMappingMiddle);
            /*
             * if(validationSubMappingMiddle.size() > 1) { // 有重复数据
             * result.put("msg", validationSubMappingMiddle.get(0) + "不能重复");
             * return result; }
             */
            tBasicSubjectMappingMiddleService.saveOrUpdate(tBasicSubjectMappingMiddle);
            // subMappingMiddleList.add(tBasicSubjectMappingMiddle);

            // tBasicSubjectMappingMiddleService.insertList(subMappingMiddleList);

            // objectList.add(arr);
        }

        // 校验数据是否有多出、映射的科目是否有重复

        result.put("subMessageAndSubMapping", subMessageAndSubMapping);
        // 更改 映射状态 为1 （已经映射成功）
        Map<String, Object> mappingStates = accountService.mappingStates(session);
        result.putAll(mappingStates);
        // result.put("code", 1);
        return result;
    }

    /**
     * @param subMappings
     * @return Map<String, Object> 返回类型
     * @Title: againAddSubMapping
     * @Description: 再次添加映射
     * @date 2018年11月8日 上午10:21:55
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/againAddSubMapping")
    @ResponseBody
    public Map<String, Object> againAddSubMapping(String subMappings) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            User user = userService.getCurrentUser();
            Account account = userService.getCurrentAccount(user);
            // 获取用户信息
            @SuppressWarnings("unchecked")
            // String userId = user.getUserID();//用户id
                    String userName = user.getUserName();
            String accountId = account.getAccountID();// 账套id
            String busDate = account.getUseLastPeriod();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();
            java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(subMappings, type2);
            // List<Map<String, String>> subMappingList = new ArrayList<>();

            int i = 0;

            List<TBasicSubjectMappingMiddle> subMappingMiddleList = new ArrayList<TBasicSubjectMappingMiddle>();
            for (JsonObject jsonObject : jsonObjects) {
                Map<String, String> subMappingMap = new HashMap<String, String>();
                java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
                }.getType();
                subMappingMap = gson.fromJson(jsonObject, type3);
                System.out.println(++i + ">>>>>>>" + subMappingMap);
                // 查询当前帐套是否存在 （科目名称和科目代码）
                String subMappingName = subMappingMap.get("subMappingName");
                String similarName = subMappingMap.get("similarName");
                String subMappingCode = subMappingMap.get("subMappingCode");
                subMappingMap.put("accountId", accountId);

                TBasicSubjectMappingMiddle querySubMappingMiddle = tBasicSubjectMappingMiddleService
                        .querySubMappingMiddle(subMappingMap);
                if (querySubMappingMiddle == null) {
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("subCode", subMappingCode);
                    param.put("subName", subMappingName);
                    param.put("accountId", accountId);
                    param.put("period", busDate);
                    // 映射关系表中不存在再查询科目余额表中
                    // 科目余额表中有的话就新增
                    Map<String, Object> sbuMessages = tBasicSubjectMessageService.querySbuMessageByMapping(param);
                    // 当科目余额表中 没有此数据时执行
                    if (sbuMessages.get("sbuMessages") == null || ((List) sbuMessages.get("sbuMessages")).isEmpty()) {
                        if (similarName.valueOf(",") != null) {
                            String[] split = similarName.split(",");
                            boolean isAdd = false;
                            // 重置 缓存
                            vatService.subinit(user, account);
                            for (int j = 0; j < split.length; j++) {
                                param.put("subName", split[j]);
                                Map<String, Object> sbuMessages2 = tBasicSubjectMessageService
                                        .querySbuMessageByMapping(param);
                                if (sbuMessages2.get("sbuMessages") != null
                                        || !((List) sbuMessages2.get("sbuMessages")).isEmpty()) {
                                    List<TBasicSubjectMessage> subjectMessages = (List<TBasicSubjectMessage>) sbuMessages2
                                            .get("sbuMessages");
                                    // if(subjectMessages.size() == 1)
                                    if (subjectMessages.size() > 0) {
                                        TBasicSubjectMessage tBasicSubjectMessage = subjectMessages.get(0);
                                        // 添加映射逻辑
                                        TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = new TBasicSubjectMappingMiddle();
                                        tBasicSubjectMappingMiddle.setAccountId(accountId);
                                        tBasicSubjectMappingMiddle.setCreateDate(new Date());
                                        tBasicSubjectMappingMiddle.setCreatePerson(userName);
                                        tBasicSubjectMappingMiddle.setSubMappingCode(subMappingCode);
                                        tBasicSubjectMappingMiddle.setSubMappingName(subMappingName);
                                        tBasicSubjectMappingMiddle.setSubMessageCode(tBasicSubjectMessage.getSubCode());
                                        tBasicSubjectMappingMiddle.setSubMessageName(tBasicSubjectMessage.getSubName());
                                        tBasicSubjectMappingMiddle.setSource("重新映射-相似名称匹配");

                                        // 查询科目是否是最下级
                                        Map<String, Object> parameters = new HashMap<String, Object>();
                                        parameters.put("subCode", tBasicSubjectMessage.getSubCode());
                                        parameters.put("accountId", accountId);
                                        parameters.put("period", busDate);

                                        boolean lastStage = tBasicSubjectMessageService.isLastStage(parameters);
                                        if (lastStage) {
                                            subMappingMiddleList.add(tBasicSubjectMappingMiddle);
                                            isAdd = true;
                                            break;
                                            // tBasicSubjectMappingMiddleService.insertSelective(tBasicSubjectMappingMiddle);
                                        }
                                    }
                                    // else if(subjectMessages.size() > 1)
                                    // {
                                    // result.put("msg", subMappingCode + "
                                    // 下面有多个科目名称为 " + subMappingName + "
                                    // 系统无法识别，请手动选择映射");
                                    // break;
                                    // }
                                }
                            }
                            if (!isAdd && account.getSsType() == 1) {
                                // 未交增值税 2221
                                if ("未交增值税".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "未交增值税", "应交税费_未交增值税");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                }
                                /*
                                 * else if ("销项税".equals(subMappingName) &&
                                 * "2221".equals(subMappingCode)) { // 销项税
                                 * //应交税费 应交增值税 //vatService.query String
                                 * subCode = null ; SubjectMessage
                                 * subjectMessage = vatService.querySub("应交增值税",
                                 * "2221", "7"); if (null != subjectMessage) {
                                 * subCode = subjectMessage.getSub_code(); }
                                 * else { subCode = vatService.getNumber("2221",
                                 * "7", "2221000"); subjectMessage =
                                 * vatService.createSub(subCode, "2221",
                                 * "应交增值税","应交税费_应交增值税"); } String three_subCode
                                 * = vatService.getNumber(subCode, "10",
                                 * subCode+"000");
                                 *
                                 * SubjectMessage three_subjectMessage =
                                 * vatService.createSub(three_subCode, subCode,
                                 * "销项税","应交税费_应交增值税_销项税"); // 添加映射逻辑
                                 * TBasicSubjectMappingMiddle tsm = new
                                 * TBasicSubjectMappingMiddle();
                                 * tsm.setAccountId(accountId);
                                 * tsm.setCreateDate(new Date());
                                 * tsm.setCreatePerson(userName);
                                 * tsm.setSubMappingCode(subMappingCode);
                                 * tsm.setSubMappingName(subMappingName);
                                 * tsm.setSubMessageCode(three_subjectMessage.
                                 * getSub_code());
                                 * tsm.setSubMessageName(three_subjectMessage.
                                 * getSub_name()); tsm.setSource("重新映射-相似名称匹配");
                                 * tBasicSubjectMappingMiddleService.
                                 * insertSelective(tsm);
                                 *
                                 *
                                 * }
                                 */
                                else if ("教育费附加".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    // 教育费附加 2221
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "教育费附加", "应交税费_教育费附加");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                } else if ("地方教育费附加".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    // 地方教育费附加
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "地方教育费附加",
                                            "应交税费_地方教育费附加");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                } else if ("应交城市维护建设税".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    // 应交城市维护建设税
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "应交城市维护建设税",
                                            "应交税费_应交城市维护建设税");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                } else if ("未分配利润".equals(subMappingName) && "4104".equals(subMappingCode)) {
                                    // 未分配利润 4104
                                    String subject = vatService.getNumber("4104", "7", "4104000");
                                    SubjectMessage sm = vatService.createSub(subject, "4104", "未分配利润", "利润分配_未分配利润");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                }
								/*else if ("工资".equals(subMappingName) && "2211".equals(subMappingCode)) {
									// 工资 2211
									String subject = vatService.getNumber("2211", "7", "2211000");
									SubjectMessage sm = vatService.createSub(subject, "2211", "工资", "应付职工薪酬_工资");
									// 添加映射逻辑
									TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
									tsm.setAccountId(accountId);
									tsm.setCreateDate(new Date());
									tsm.setCreatePerson(userName);
									tsm.setSubMappingCode(subMappingCode);
									tsm.setSubMappingName(subMappingName);
									tsm.setSubMessageCode(sm.getSub_code());
									tsm.setSubMessageName(sm.getSub_name());
									tsm.setSource("重新映射-相似名称匹配");
									tBasicSubjectMappingMiddleService.insertSelective(tsm);
								} */
                                else if ("主营业务收入".equals(subMappingName) && "6001".equals(subMappingCode)) {
                                    // 主营业务收入
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode("6001");
                                    tsm.setSubMessageName("主营业务收入");
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                } else if ("应交所得税".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    // 应交所得税
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "应交所得税", "应交税费_应交所得税");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                } else if ("应交增值税".equals(subMappingName) && "2221".equals(subMappingCode)) {
                                    // 应交增值税
                                    String subject = vatService.getNumber("2221", "7", "2221000");
                                    SubjectMessage sm = vatService.createSub(subject, "2221", "应交增值税", "应交税费_应交增值税");
                                    // 添加映射逻辑
                                    TBasicSubjectMappingMiddle tsm = new TBasicSubjectMappingMiddle();
                                    tsm.setAccountId(accountId);
                                    tsm.setCreateDate(new Date());
                                    tsm.setCreatePerson(userName);
                                    tsm.setSubMappingCode(subMappingCode);
                                    tsm.setSubMappingName(subMappingName);
                                    tsm.setSubMessageCode(sm.getSub_code());
                                    tsm.setSubMessageName(sm.getSub_name());
                                    tsm.setSource("重新映射-相似名称匹配");
                                    tBasicSubjectMappingMiddleService.insertSelective(tsm);
                                }

                            }
                        } else {
                            param.put("subName", similarName);
                            Map<String, Object> sbuMessages2 = tBasicSubjectMessageService
                                    .querySbuMessageByMapping(param);
                            List<TBasicSubjectMessage> subjectMessages = (List<TBasicSubjectMessage>) sbuMessages2
                                    .get("sbuMessages");
                            // if(subjectMessages.size() == 1)
                            if (subjectMessages.size() > 0) {
                                TBasicSubjectMessage tBasicSubjectMessage = subjectMessages.get(0);
                                // 添加映射逻辑
                                TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = new TBasicSubjectMappingMiddle();
                                tBasicSubjectMappingMiddle.setAccountId(accountId);
                                tBasicSubjectMappingMiddle.setCreateDate(new Date());
                                tBasicSubjectMappingMiddle.setCreatePerson(userName);
                                tBasicSubjectMappingMiddle.setSubMappingCode(subMappingCode);
                                tBasicSubjectMappingMiddle.setSubMappingName(subMappingName);
                                tBasicSubjectMappingMiddle.setSubMessageCode(tBasicSubjectMessage.getSubCode());
                                tBasicSubjectMappingMiddle.setSubMessageName(tBasicSubjectMessage.getSubName());
                                tBasicSubjectMappingMiddle.setSource("重新映射-相似名称匹配");
                                // 查询科目是否是最下级
                                Map<String, Object> parameters = new HashMap<String, Object>();
                                parameters.put("subCode", tBasicSubjectMessage.getSubCode());
                                parameters.put("accountId", accountId);
                                parameters.put("period", busDate);

                                boolean lastStage = tBasicSubjectMessageService.isLastStage(parameters);
                                if (lastStage) {
                                    subMappingMiddleList.add(tBasicSubjectMappingMiddle);
                                    // tBasicSubjectMappingMiddleService.insertSelective(tBasicSubjectMappingMiddle);
                                }
                            }
                            // else if(subjectMessages.size() > 1)
                            // {
                            // result.put("msg", subMappingCode + " 下面有多个科目名称为 "
                            // + subMappingName + " 系统无法识别，请手动选择映射");
                            // break;
                            // }
                        }
                    } else {
                        List<TBasicSubjectMessage> subjectMessages = (List<TBasicSubjectMessage>) sbuMessages
                                .get("sbuMessages");
                        // if(subjectMessages.size() == 1)
                        if (subjectMessages.size() > 0) {
                            TBasicSubjectMessage tBasicSubjectMessage = subjectMessages.get(0);
                            // 添加映射逻辑
                            TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = new TBasicSubjectMappingMiddle();
                            tBasicSubjectMappingMiddle.setAccountId(accountId);
                            tBasicSubjectMappingMiddle.setCreateDate(new Date());
                            tBasicSubjectMappingMiddle.setCreatePerson(userName);
                            tBasicSubjectMappingMiddle.setSubMappingCode(subMappingCode);
                            tBasicSubjectMappingMiddle.setSubMappingName(subMappingName);
                            tBasicSubjectMappingMiddle.setSubMessageCode(tBasicSubjectMessage.getSubCode());
                            tBasicSubjectMappingMiddle.setSubMessageName(tBasicSubjectMessage.getSubName());
                            tBasicSubjectMappingMiddle.setSource("重新映射-标准科目匹配");
                            // 查询科目是否是最下级
                            // 查询科目是否是最下级
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("subCode", tBasicSubjectMessage.getSubCode());
                            parameters.put("accountId", accountId);
                            parameters.put("period", busDate);

                            boolean lastStage = tBasicSubjectMessageService.isLastStage(parameters);
                            if (lastStage) {
                                subMappingMiddleList.add(tBasicSubjectMappingMiddle);
                                // tBasicSubjectMappingMiddleService.insertSelective(tBasicSubjectMappingMiddle);
                            }
                        }
                        // else if(subjectMessages.size() > 1)
                        // {
                        // result.put("msg", subMappingCode + " 下面有多个科目名称为 " +
                        // subMappingName + " 系统无法识别，请手动选择映射");
                        // break;
                        // }
                    }
                }
                // 存在
            }
            Map<String, List<TBasicSubjectMappingMiddle>> removeDuplicate1 = removeDuplicate(subMappingMiddleList);
            List<TBasicSubjectMappingMiddle> duplicatedData = removeDuplicate1.get("duplicatedData");
            List<TBasicSubjectMappingMiddle> resultSet = removeDuplicate1.get("resultSet");
            if (resultSet.size() > 0) {
                for (int j = 0; j < resultSet.size(); j++) {
                    tBasicSubjectMappingMiddleService.insertSelective(resultSet.get(j));
                }
            }
            if (duplicatedData.size() > 0) {
                for (int j = 0; j < duplicatedData.size(); j++) {
                    result.put("smg" + j, duplicatedData.get(j).getSubMappingCode() + "->>下面的科目名称-->> "
                            + duplicatedData.get(j).getSubMappingName() + " <<--存在多个，请自行选择！！！");
                }
            }
            result.put("subMappings", subMappings);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 去除 重复数据 返回反复数据 及去重后的数据
     *
     * @param list
     * @return
     */
    public static Map<String, List<TBasicSubjectMappingMiddle>> removeDuplicate(List<TBasicSubjectMappingMiddle> list) {
        Map<String, List<TBasicSubjectMappingMiddle>> returnValue = new HashMap<String, List<TBasicSubjectMappingMiddle>>();
        List<TBasicSubjectMappingMiddle> duplicatedData = new ArrayList<TBasicSubjectMappingMiddle>();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getSubMappingName().equals(list.get(i).getSubMappingName())
                        && list.get(j).getSubMappingCode().equals(list.get(i).getSubMappingCode())) {
                    list.remove(j);
                }
            }
        }
        returnValue.put("duplicatedData", duplicatedData);
        returnValue.put("resultSet", list);
        return returnValue;
    }

    @RequestMapping(value = "toKmdTest", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> toKmdTest(TBasicSubjectMessage tBasicSubjectMessage,
                                  List<TBasicSubjectExcel> tBasicSubjectExcels) {
        Map<String, Object> map = new HashMap<>();
        map.put("tBasicSubjectMessage", tBasicSubjectMessage);
        return map;
    }

    @RequestMapping("/toKmdTest2")
    @ResponseBody
    public Map<String, Object> toKmdTest2(TBasicSubjectExcel tBasicSubjectExcel) {
        Map<String, Object> map = new HashMap<>();
        map.put("tBasicSubjectExcel", tBasicSubjectExcel);
        return map;
    }

    @RequestMapping("/toKmdTest3")
    @ResponseBody
    public Map<String, Object> toKmdTest2(TBasicSubjectExcelForm tBasicSubjectExcelForm) {
        Map<String, Object> map = new HashMap<>();
        map.put("tBasicSubjectExcel", tBasicSubjectExcelForm.gettBasicSubjectExcelList());
        return map;
    }

    public boolean isMatch(String sysSuName, String subExcelName) {
        if ("应付职工薪酬".equals(sysSuName) && "应付工资".equals(subExcelName)) {
            return true;
        } else if ("应交税费".equals(sysSuName) && "应交税金".equals(subExcelName)) {
            return true;
        } else if ("库存现金".equals(sysSuName) && "现金".equals(subExcelName)) {
            return true;
        } else if ("所得税费用".equals(sysSuName) && "所得税".equals(subExcelName)) {
            return true;
        } else if ("营业税金及附加".equals(sysSuName) && "主营业务税金及附加".equals(subExcelName)) {
            return true;
        } else if ("其他业务成本".equals(sysSuName) && "营业外支出".equals(subExcelName)) {
            return true;
        } else if ("周转材料".equals(sysSuName) && "低值易耗品".equals(subExcelName)) {
            return true;
        } else if ("应付股利".equals(sysSuName) && "应付利润".equals(subExcelName)) {
            return true;
        } else if ("销售费用".equals(sysSuName) && "营业费用".equals(subExcelName)) {
            return true;
        }
        return false;
    }
}
