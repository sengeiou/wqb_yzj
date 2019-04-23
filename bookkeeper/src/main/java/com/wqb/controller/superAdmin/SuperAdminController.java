package com.wqb.controller.superAdmin;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.user.UserDao;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.service.SuperAdminService.SuperAdminService;
import com.wqb.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 超级管理员页面扇形图做账统计数量 开发时间：2018-07-21
 *
 * @author tangsheng
 */

@Controller
@RequestMapping("/superAdminController")
public class SuperAdminController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(SuperAdminController.class);
    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @RequestMapping("/superAdmin")
    @ResponseBody
    Map<String, Object> queryCountQuantity() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            String period = (String) sessionMap.get("busDate");
            param.put("period", period);
            param.put("isCV", 0);
            // 通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
            int countData1 = superAdminService.getQueryCountQuantity1(param);
            param.put("isJZ", 1);
            // 通过账套ID和做账期间以及条件： 已完成结账--1 （查询做账统计数量）
            int countData2 = superAdminService.getQueryCountQuantity2(param);
            param.put("isCreateVoucher", 1);
            param.put("isJz", 0);
            // 通过账套ID和做账期间以及条件：生成凭证-1但是没有结账--0 （查询做账统计数量）
            int countData3 = superAdminService.getQueryCountQuantity3(param);

            int notfinish = countData1 - countData2 - countData3;
            returnMap.put("success", "true");
            returnMap.put("countData1", countData2);
            returnMap.put("countData2", countData3);
            returnMap.put("countData3", notfinish);
            return returnMap;
        } catch (BusinessException e) {
            logger.error("SuperAdminController【queryCountQuantity】 获取做账统计数量失败！", e);
            returnMap.put("success", "fail");
            returnMap.put("message", " 获取做账统计数量失败！");
            return returnMap;
        } catch (Exception e) {
            logger.error("SuperAdminController【queryCountQuantity】 获取做账统计数量失败！", e);
            returnMap.put("success", "fail");
            returnMap.put("message", " 获取做账统计数量失败！");
            return returnMap;
        }
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @SuppressWarnings("unlikely-arg-type")
    @RequestMapping("/statusCustomer")
    @ResponseBody
    Map<String, Object> queryStatusCustomer(@RequestParam(value = "cusType", required = true) String cusType,
                                            String curPage, String keyWord) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> pam = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            String period = (String) sessionMap.get("busDate");
            String schedule = "--";
            int countnumber = 0;
            int pageSize = 10;

            param.put("period", period);
            param.put("keyword", keyWord);
            List<StatusPeriod> list = null;
            List<Account> acclist = null;
            List<Account> accountlist = new ArrayList<Account>();
            if (!StringUtil.isEmpty(curPage)) {
                param.put("begin", (Integer.parseInt(curPage) - 1) * pageSize);
                param.put("end", pageSize);
            }
            // cusType为传入的查询参数类型 0代表查询全部 1和2以及3的意义见如下注释
            if (!"".equals(cusType) && "0".equals(cusType)) {
                System.out.println("传值:" + cusType + "  ?>" + curPage + "  ?> " + keyWord);
                acclist = superAdminService.queryStatusCustomer0(param);
                countnumber = superAdminService.getQueryCountQuantity0(param);
                if (acclist.size() > 0) {
                    for (int i = 0; i < acclist.size(); i++) {
                        Account acc = acclist.get(i);
                        String userID = acc.getUserID();
                        User use = userDao.queryUserById(userID);

                        pam.put("accountId", acc.getAccountID());
                        pam.put("period", period);
                        StatusPeriod statusPeriod = superAdminService.queryStatusCustomer4(pam);
                        if (acc != null) {
                            //acc.setPhoneNumber(user.getLoginUser());
                            acc.setAgencyCompany("");
                            acc.setAgent(use.getUserName());
                            acc.setStatuperiod(period);
                            acc.setPhoneNumber(use.getLoginUser());
                            int statu = acc.getStatu();
                            if (statu == 1) {
                                acc.setStatutype("正常");
                            } else if (statu == 2) {
                                acc.setStatutype("已禁用");
                            } else {
                                acc.setStatutype("未知状态");
                            }
                            if (statusPeriod == null) {
                                schedule = "0%";
                            } else {
                                int isCreateVoucher = statusPeriod.getIsCreateVoucher();
                                int isJt = statusPeriod.getIsJt();
                                int isCarryState = statusPeriod.getIsCarryState();
                                int isDetection = statusPeriod.getIsDetection();
                                int isJz = statusPeriod.getIsJz();

                                if (isCreateVoucher == 0 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "0%";

                                }
                                if (isCreateVoucher == 1 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "20%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "40%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "60%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                        && isJz == 0) {
                                    schedule = "80%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                        && isJz == 1) {
                                    schedule = "100%";

                                }
                            }

                            acc.setSchedule(schedule);
                            accountlist.add(acc);
                        }
                    }
                }
            } else if (!"".equals(cusType) && "1".equals(cusType)) {
                param.put("isJZ", 1);
                // 通过账套ID和做账期间以及条件： 已完成结账--1 （查询做账统计数量）
                list = superAdminService.queryStatusCustomer1(param);
                countnumber = superAdminService.getQueryCountQuantity2(param);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        StatusPeriod statusPeriod = list.get(i);
                        pam.put("accountId", statusPeriod.getAccountID());

                        Account account = accountService.queryPeriodId(pam);

                        if (account == null) {
                            list.remove(i);
                            countnumber--;
                        } else {
                            String userID = account.getUserID();
                            User use = userDao.queryUserById(userID);
                            account.setPhoneNumber(use.getLoginUser());
                            account.setAgencyCompany("");
                            account.setAgent(use.getUserName());
                            account.setStatuperiod(statusPeriod.getPeriod());
                            int statu = account.getStatu();
                            if (statu == 1) {
                                account.setStatutype("正常");
                            } else if (statu == 2) {
                                account.setStatutype("已禁用");
                            } else {
                                account.setStatutype("未知状态");
                            }
                            int isCreateVoucher = statusPeriod.getIsCreateVoucher();
                            int isJt = statusPeriod.getIsJt();
                            int isCarryState = statusPeriod.getIsCarryState();
                            int isDetection = statusPeriod.getIsDetection();
                            int isJz = statusPeriod.getIsJz();

                            if (isCreateVoucher == 0 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "0%";

                            }
                            if (isCreateVoucher == 1 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "20%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "40%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "60%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                    && isJz == 0) {
                                schedule = "80%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                    && isJz == 1) {
                                schedule = "100%";

                            }
                            account.setSchedule(schedule);
                            accountlist.add(account);
                        }

                    }
                }
            } else if ("2".equals(cusType)) {
                param.put("isCreateVoucher", 1);
                param.put("isJz", 0);
                // 通过账套ID和做账期间以及条件：生成凭证-1但是没有结账--0 （查询做账统计数量）
                list = superAdminService.queryStatusCustomer2(param);
                countnumber = superAdminService.getQueryCountQuantity3(param);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        StatusPeriod statusPeriod = list.get(i);
                        pam.put("accountId", statusPeriod.getAccountID());

                        Account account = accountService.queryPeriodId(pam);
                        if (account == null) {
                            list.remove(i);
                            countnumber--;
                        } else {
                            String userID = account.getUserID();
                            User use = userDao.queryUserById(userID);
                            account.setPhoneNumber(use.getLoginUser());
                            //account.setPhoneNumber(user.getLoginUser());
                            account.setAgencyCompany("");
                            account.setAgent(use.getUserName());
                            account.setStatuperiod(statusPeriod.getPeriod());
                            int statu = account.getStatu();
                            if (statu == 1) {
                                account.setStatutype("正常");
                            } else if (statu == 2) {
                                account.setStatutype("已禁用");
                            } else {
                                account.setStatutype("未知状态");
                            }
                            int isCreateVoucher = statusPeriod.getIsCreateVoucher();
                            int isJt = statusPeriod.getIsJt();
                            int isCarryState = statusPeriod.getIsCarryState();
                            int isDetection = statusPeriod.getIsDetection();
                            int isJz = statusPeriod.getIsJz();

                            if (isCreateVoucher == 0 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "0%";

                            }
                            if (isCreateVoucher == 1 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "20%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 0 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "40%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 0
                                    && isJz == 0) {
                                schedule = "60%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                    && isJz == 0) {
                                schedule = "80%";

                            }
                            if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                    && isJz == 1) {
                                schedule = "100%";

                            }
                            account.setSchedule(schedule);
                            accountlist.add(account);
                        }

                    }
                }
            } else if ("3".equals(cusType)) {
                param.put("isCV", 0);
                param.put("isCreateVoucher", 1);
                param.put("isJz", 0);
                param.put("isJZ", 1);
                // 通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
                acclist = superAdminService.queryStatusCustomer3(param);
                countnumber = superAdminService.queryStatusCustomer5(param);
                if (acclist.size() > 0) {
                    for (int i = 0; i < acclist.size(); i++) {
                        Account accountL = acclist.get(i);
                        if (accountL != null) {
                            String userID = accountL.getUserID();
                            User use = userDao.queryUserById(userID);
                            accountL.setPhoneNumber(use.getLoginUser());

                            //accountL.setPhoneNumber(user.getLoginUser());
                            accountL.setAgencyCompany("");
                            accountL.setAgent(use.getUserName());
                            accountL.setStatuperiod(period);
                            int statu = accountL.getStatu();
                            if (statu == 1) {
                                accountL.setStatutype("正常");
                            } else if (statu == 2) {
                                accountL.setStatutype("已禁用");
                            } else {
                                accountL.setStatutype("未知状态");
                            }
                            pam.put("accountId", accountL.getAccountID());
                            pam.put("period", period);
                            StatusPeriod statusPeriod = superAdminService.queryStatusCustomer4(pam);
                            if (statusPeriod == null) {
                                schedule = "0%";
                            } else {
                                int isCreateVoucher = statusPeriod.getIsCreateVoucher();
                                int isJt = statusPeriod.getIsJt();
                                int isCarryState = statusPeriod.getIsCarryState();
                                int isDetection = statusPeriod.getIsDetection();
                                int isJz = statusPeriod.getIsJz();

                                if (isCreateVoucher == 0 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "0%";

                                }
                                if (isCreateVoucher == 1 && isJt == 0 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "20%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 0 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "40%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 0
                                        && isJz == 0) {
                                    schedule = "60%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                        && isJz == 0) {
                                    schedule = "80%";

                                }
                                if (isCreateVoucher == 1 && isJt == 1 && isCarryState == 1 && isDetection == 1
                                        && isJz == 1) {
                                    schedule = "100%";

                                }
                            }

                            accountL.setSchedule(schedule);
                            accountlist.add(accountL);
                        }

                    }
                }
            }
            param.put("begin", null);
            param.put("end", null);
            returnMap.put("listsize", countnumber);
            returnMap.put("pageSize", pageSize);
            returnMap.put("success", "true");
            returnMap.put("list", accountlist);
            return returnMap;
        } catch (BusinessException e) {
            logger.error("SuperAdminController【queryStatusCustomer】 根据超级管理员指定的凭证节点获取账套客户信息失败！", e);
            returnMap.put("success", "fail");
            returnMap.put("message", " 获取做账统计数量失败！");
            return returnMap;
        } catch (Exception e) {
            logger.error("SuperAdminController【queryStatusCustomer】 根据超级管理员指定的凭证节点获取账套客户信息失败！", e);
            returnMap.put("success", "fail");
            returnMap.put("message", " 获取做账统计数量失败！");
            return returnMap;
        }
    }

    /**
     * 各代理公司负责企业数量比例图
     *
     * @return
     */

    @RequestMapping("/getDzqyAccCount")
    @ResponseBody
    Map<String, Object> getDzqyAccCount() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> list = accountDao.getDzqyAccCount();
            result.put("list", list);
            result.put("success", "true");
        } catch (BusinessException e) {
            result.put("success", "false");
            logger.error("获取各代理公司负责企业数量比例图数据异常", e);
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("获取各代理公司负责企业数量比例图数据异常", e);
            return result;
        }
        return result;
    }

    /**
     * 平台有效企业数量趋势图
     *
     * @return
     */
    @RequestMapping("/getPtActiveCount")
    @ResponseBody
    Map<String, Object> getPtActiveCount() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 12; i++) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                String time = DateUtil.getMonth();
                Date date = DateUtil.getMonth(time);
                Date d = DateUtil.getNextNMonth2(date, i - 12);
                String month = DateUtil.getMoth2(d);
                param.put("Month", month);
                Object obj = superAdminService.getPtActiveCount(param);
                Map<String, Object> map = (Map<String, Object>) obj;
                String sl = map.get("sl").toString();
                tempMap.put("Month", month);
                tempMap.put("sl", sl);
                list.add(tempMap);
            }
            result.put("success", "true");
            result.put("list", list);
        } catch (BusinessException e) {
            result.put("success", "false");
            logger.error("获取平台有效企业数量趋势图异常", e);
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("获取平台有效企业数量趋势图异常", e);
            return result;
        }
        return result;
    }

    /**
     * 代理和自助增长趋势图
     *
     * @return
     */
    @RequestMapping("/getPtAccZz")
    @ResponseBody
    Map<String, Object> getPtAccZz() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            List<Map<String, Object>> dzList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> jzList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 12; i++) {
                Map<String, Object> tempMapDz = new HashMap<String, Object>();
                Map<String, Object> tempMapJz = new HashMap<String, Object>();
                String time = DateUtil.getMonth();
                Date date = DateUtil.getMonth(time);
                Date d = DateUtil.getNextNMonth2(date, i - 12);
                String month = DateUtil.getMoth2(d);
                param.put("Month", month);
                Object objDz = superAdminService.getPtDzAccZz(param);
                Object objJz = superAdminService.getPtJzAccZz(param);
                Map<String, Object> mapDz = (Map<String, Object>) objDz;
                Map<String, Object> mapJz = (Map<String, Object>) objJz;
                String dzSl = mapDz.get("sl").toString();
                String jzSl = mapJz.get("sl").toString();
                tempMapDz.put("Month", month);
                tempMapDz.put("sl", dzSl);
                tempMapJz.put("Month", month);
                tempMapJz.put("sl", jzSl);
                dzList.add(tempMapDz);
                jzList.add(tempMapJz);
            }
            result.put("success", "true");
            result.put("dzList", dzList);
            result.put("jzList", jzList);
        } catch (BusinessException e) {
            result.put("success", "false");
            logger.error("获取代理和自助增长趋势图异常", e);
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("获取代理和自助增长趋势图异常", e);
            return result;
        }
        return result;
    }

    @RequestMapping("/getZlInfo")
    @ResponseBody
    Map<String, Object> getZlInfo(String time) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 一.本月平台新增企业
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("time", time);
            Object objByxz = superAdminService.getByXzQy(param);
            Map<String, Object> mapByxz = (Map<String, Object>) objByxz;
            int byxz = Integer.parseInt(mapByxz.get("sl").toString());
            result.put("byptxzqy", byxz);

            Object objTotalQyByTime = superAdminService.getTotalQyByTime(param);
            Map<String, Object> mapTotalQyByTime = (Map<String, Object>) objTotalQyByTime;
            int totalQyByTime = Integer.parseInt(mapTotalQyByTime.get("sl").toString());
            if (totalQyByTime != 0) {
                result.put("byptxzqyzz", (double) byxz * 100 / totalQyByTime + "%");
            }
            // 三.本月平台停用企业
            Object objByTy = superAdminService.getByTyQy(param);
            Map<String, Object> mapByTy = (Map<String, Object>) objByTy;
            int byTy = Integer.parseInt(mapByTy.get("sl").toString());
            result.put("bypttyqy", byTy);
            if (totalQyByTime != 0) {
                result.put("bypttyqyzb", (double) byTy * 100 / (totalQyByTime + byTy) + "%");
            }

            // 四.获取平台总企业数
            Object objPtZqy = superAdminService.getPtTotalAcc(param);
            Map<String, Object> mapPtZqy = (Map<String, Object>) objPtZqy;
            int ptZqy = Integer.parseInt(mapPtZqy.get("sl").toString());
            result.put("ptzqys", ptZqy);
            // 五.平台累计停用企业
            Object objPtTyQy = superAdminService.getPtTotalTyAcc(param);
            Map<String, Object> mapPtTyQy = (Map<String, Object>) objPtTyQy;
            int ptTyQy = Integer.parseInt(mapPtTyQy.get("sl").toString());
            result.put("ptljtyqy", ptTyQy);
            // 本月新增代理记账公司
            Object objByXzDlCom = superAdminService.getByXzDlCom(param);
            Map<String, Object> mapByXzDlCom = (Map<String, Object>) objByXzDlCom;
            int byXzDlCom = Integer.parseInt(mapByXzDlCom.get("sl").toString());
            result.put("byXzDlCom", byXzDlCom);
            // 平台累计代理公司
            Object objDlCom = superAdminService.getDlCom(param);
            Map<String, Object> mapDlCom = (Map<String, Object>) objDlCom;
            int dlCom = Integer.parseInt(mapDlCom.get("sl").toString());
            result.put("dlCom", dlCom);
            result.put("success", "true");
            // 增长率
            if (dlCom - byXzDlCom != 0) {
                result.put("zzl", (double) byXzDlCom * 100 / (dlCom - byXzDlCom) + "%");
            }
        } catch (BusinessException e) {
            result.put("success", "false");
            result.put("msg", "获取超级管理员工作台总览模块数据异常");
            logger.error("获取超级管理员工作台总览模块数据异常", e);
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            result.put("msg", "获取超级管理员工作台总览模块数据异常");
            logger.error("获取超级管理员工作台总览模块数据异常", e);
            return result;
        }
        return result;
    }
}
