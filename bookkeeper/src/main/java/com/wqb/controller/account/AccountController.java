package com.wqb.controller.account;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.pay.DzPayDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.*;
import com.wqb.service.UserService;
import com.wqb.service.account.AccountService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.tempType.TempTypeService;
import com.wqb.service.vat.VatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountController.class);
    @Autowired
    AccountService accountService;
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    UserDao userDao;
    @Autowired
    DzPayDao dzPayDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    ProgressDao progressDao;
    @Autowired
    VatService vatService;
    @Autowired
    VatDao vatDao;
    @Autowired
    TempTypeService tempTypeService;
    @Autowired
    private UserService userService;


    private static Properties properties = null;

    // private HttpClientUtil httpClientUtil = new HttpClientUtil();
    // 读取配置文件
    static {
        InputStream inStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config/pay.properties");

        properties = new Properties();
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties pro1 = null;

    // 读取配置文件
    static {
        InputStream inStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config/address.properties");

        pro1 = new Properties();
        try {
            pro1.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1 用户切换账套 头部
    //// 2 1getAccList获取列表页-》2 点击记账 clickAccount
    @RequestMapping("/chgAccount")
    @ResponseBody
    Map<String, Object> chgAccount(String accountID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();

            Map<String, Object> upMap = new HashMap<>();

            // 根据确定的账套id获取用户账套
            Account account = accountService.queryAccByID(accountID);

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("userDate");

            User user = (User) map.get("user");
            if (user.getUserType() == 3 || user.getUserType() == 2) {
                upMap.put("userID", user.getUserID());
                // 如果是管理员 根据用户id记录最后账套切换时间
                UserAccount user_acc = accountDao.queryAcc(upMap); // userAcc.queryAcc
                if (user_acc == null) {
                    UserAccount userAccNew = new UserAccount();
                    userAccNew.setAccountID(accountID);
                    userAccNew.setLastTime(System.currentTimeMillis());
                    userAccNew.setUserID(user.getUserID());
                    userAccNew.setMg_id(UUIDUtils.getUUID());
                    userAccNew.setUser_phone(user.getLoginUser());
                    accountDao.insertUserAcc(userAccNew);
                } else {
                    upMap.put("accountID", accountID);
                    accountDao.chgAcc(upMap); // 如果是管理员登录切换账套 那么以用户id更新它的账套
                    // update t_user_acc accountID=#{accountID} where
                    // userID=#{userID}
                }

            }
            map.put("account", account);

            // 用户最后使用期间
            String use_last_period = account.getUseLastPeriod();
            // 1 用户第一次做账 时间设定为启动期间
            // 如果知道用户是第一进入了
            // 只要查询下面任何一项数据说明已经开始做账
            // 是否初始化科目。是否导入数量金额表，是否有凭证生成
            // 是否有记录最后切换期间
            String period = null;
            if (StringUtil.isEmpty(use_last_period)) {
                // 如果没有记录最后使用期间那么说明是第一次登陆，第一次登陆使用时间就是账号启动期间
                Date qidong_period = account.getPeriod();
                if (qidong_period == null) {
                    qidong_period = new Date();
                }
                String qidong_period_str = DateUtil.getMoth2(qidong_period);
                period = qidong_period_str;
            } else {
                // 2 用户第二次做账 进入账套期间为用户最后一次做账
                // 用户最后一次切换期间的时间设定为本次登录的期间
                period = use_last_period;
            }

            map.put("busDate", period);

            // 1 toSystem/index 进入主页
            // 2 toSystem/getAccList 获取账套列表
            // 3 account/chgAccount 选择账套进入。1 列表页 2弹窗 参数acc_id
            // 4common.js get请求 /wqb/account/getPeriodList 时间插件获取数据，确定时间最大 最小值。
            // (最大值最小值根据用户付费确定)
            /// 5 create.js multiSearch方法 向 wqb/voucher/queryAllVoucher 请求获取凭证数据

            // LoginController login/chgPeriod 选择期间 并记录选择期间写到数据库

            // 把新的账套数据更新到session里面去
            session.setAttribute("userDate", map);

            // loginAPP chgAccLastTime @RequestMapping("/choiceCompany") 285

            // 切换账套时,需要更改账套最后使用时间

            Integer count = accountService.chgAccLastTime(new Date(), accountID);

            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("accountID", account.getAccountID());
            pa.put("busDate", getUserDate());
            pa.put("period", getUserDate());
            List<StatusPeriod> list = periodStatusService.queryStatus(pa);
            if (list == null || list.size() == 0) {
                StatusPeriod statusPeriod = new StatusPeriod();
                statusPeriod.setPeriodID(UUIDUtils.getUUID());
                statusPeriod.setCreatePsn(getUser().getUserName());
                statusPeriod.setCreatePsnID(getUser().getUserID());
                statusPeriod.setIsCarryState(0);
                statusPeriod.setIsCreateVoucher(0);
                statusPeriod.setIsJz(0);
                statusPeriod.setIsJt(0);
                statusPeriod.setPeriod(getUserDate());
                statusPeriod.setAccountID(account.getAccountID());
                periodStatusService.insertPeriodStatu(statusPeriod);
            }
            List<Progress> proList = progressDao.queryProgress(pa);
            if (proList != null && proList.size() > 0 && proList.get(0) != null) {

            } else {
                Progress pro = new Progress();
                pro.setId(UUIDUtils.getUUID());
                pro.setAccountID(account.getAccountID());
                pro.setPeriod(getUserDate());
                pro.setCv(0);
                pro.setJt(0);
                pro.setUnJt(0);
                pro.setCarryState(0);
                pro.setUnCarryState(0);
                pro.setJz(0);
                progressDao.addProgress(pro);
            }

            if (count > 0) {
                result.put("message", "success");
                result.put("statu", account.getInitialStates());
            } else {
                result.put("message", "fail");
            }

            Map<String, Object> param = new HashMap<>();

            param.put("accountID", accountID);
            param.put("period", period);
            int num = vatDao.queryCountSubjectMessage(param);
            if (num > 10) {
                vatService.resetCache(accountID, period);
            }
            return result;
        } catch (BusinessException e) {
            logger.error("AccountController【chgAccount】 用户切换账套异常!", e);
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AccountController【chgAccount】 用户切换账套异常!", e);
            result.put("message", "fail");
            return result;

        }
    }

    // 输入框搜索账套
    @RequestMapping("/searchAccount")
    @ResponseBody
    Map<String, Object> searchAccount(String keyword) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            @SuppressWarnings("unchecked")
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            // Account account = (Account) userDate.get("account");
            // String period = (String) userDate.get("busDate");
            User user = (User) userDate.get("user");
            Integer userType = user.getUserType();
            Map<String, Object> map = new HashMap<>();
            map.put("statu", 1);
            map.put("keyWord", keyword);
            map.put("lastTime", "1");
            if (userType == 2 || userType == 3) {
                map.put("source", user.getUserID());
            } else {
                map.put("userID", user.getUserID());
            }
            List<Account> list = accountDao.queryAccByCondition(map);
            result.put("code", "0");
            result.put("msg", list);
            return result;
        } catch (Exception e) {
            logger.error("AccountController【searchAccount】 搜索账套异常!", e);
            result.put("code", "1");
            result.put("msg", e.getMessage());
            return result;

        }
    }

    @RequestMapping("/chgAccInitialStates")
    @ResponseBody
    public Map<String, Object> chgAccInitialStates() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            result = accountService.chgAccInitialStates(session);
            createTemp();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }


    // 选择账套 后台首页列表选择某个账套进去
    @RequestMapping("/queryByUserID")
    @ResponseBody
    Map<String, Object> queryByUserID() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            User user = userService.getCurrentUser();
            List<User> list = new ArrayList<>();
            String userID = user.getUserID();// 用户id
            int type = user.getUserType();
            List<Account> account = new ArrayList<Account>();
            if (type == 2 || type == 3) {
                list = userDao.queryUserByParentID(userID);
                boolean exists = list.contains(user);
                if (!exists) {
                    list.add(user);
                } else {

                }
                if (list != null && list.size() > 0) {
                    for (int s = 0; s < list.size(); s++) {
                        int statu = list.get(s).getState();
                        if (statu != 1) {
                            continue;
                        } else {
                            List<Account> accountList = accountService.queryByUserID(list.get(s).getUserID());
                            account.addAll(accountList);
                        }
                    }
                }
            } else if (type == 5 || type == 6) {
                account = accountService.queryByUserID(userID);
            }
            if (!account.isEmpty()) {
                result.put("account", account);
            }
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("AccountController【queryByUserID】,根据用户id查询此用户下面所有的帐套出错", e);
            return result;
        } catch (Exception e) {
            logger.error("AccountController【queryByUserID】,根据用户id查询此用户下面所有的帐套出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;

    }

    // 获取时间切换可选择元素和提示预警信息
    @RequestMapping("/getPeriodList")
    @ResponseBody
    Map<String, Object> getPeriodList() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取到平台用户ID
            User user = getUser();
            Integer userType = user.getUserType();
            String ptID = null;
            if (userType == 5 || userType == 6) {
                String userID = user.getParentUser();
                if (StringUtils.isBlank(userID)) {
                    result.put("success", "true");
                    result.put("msg", "测试用户没有父类用户，《司》出问题改这里");
                    return result;
                }
                User use = userDao.queryUserById(userID);
                ptID = use.getId();
            } else if (userType == 2 || userType == 3) {
                ptID = user.getId();
            }
            if (ptID == null) {
                logger.error("AccountController【chgAccount】 用户切换账套异常!");
                result.put("success", "false");
            } else {
                /*
                 * String url1 = pro1.getProperty("wqb_url") +
                 * "/Customer/GetChargebackInfo"; Map<String, String> createMap1
                 * = new HashMap<String, String>(); createMap1.put("customerId",
                 * ptID); createMap1.put("dataType", "json"); String
                 * httpOrgCreateTestRtn1 = httpClientUtil.doPost(url1,
                 * createMap1, pro1.getProperty("wqb_charset")); Map<?, ?> maps1
                 * = (Map<?, ?>) JSON.parse(httpOrgCreateTestRtn1); Map<?, ?>
                 * map1 = (Map<?, ?>) maps1.get("data"); boolean flag =
                 * Boolean.parseBoolean(map1.get("IsSucess").toString().trim());
                 * //flag= false;
                 *
                 */
                boolean flag = true;
                if (!flag) {
                    // 最近一次扣费失败
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("ptID", ptID);
                    List<DzPay> periodList = dzPayDao.querySeePeriod(param);
                    result.put("periodList", periodList);
                    result.put("flag", "false");
                } else {
                    // 调用商城接口获取当前管理员名下余额
                    // double ableAmount = 1;
                    /*
                     * String url = pro1.getProperty("wqb_url") +
                     * "/Customer/GetMyAssets"; Map<String, String> createMap =
                     * new HashMap<String, String>();
                     * createMap.put("customerId", ptID);
                     * createMap.put("dataType", "json"); String
                     * httpOrgCreateTestRtn = httpClientUtil.doPost(url,
                     * createMap, pro1.getProperty("wqb_charset")); Map<?, ?>
                     * maps = (Map<?, ?>) JSON.parse(httpOrgCreateTestRtn);
                     * Map<?, ?> map = (Map<?, ?>) maps.get("data"); String
                     * ableAmountStr = map.get("Balance").toString(); double
                     * ableAmount = Double.parseDouble(ableAmountStr);
                     */
                    double ableAmount = 100000;
                    double price = 0;
                    if (userType == 2 || userType == 5) {
                        price = Double.parseDouble(properties.get("dzPrice").toString().trim());

                    } else if (userType == 3 || userType == 6) {
                        price = Double.parseDouble(properties.get("jzPrice").toString().trim());
                    }
                    if (ableAmount < price) {
                        // 已经不够扣费,查出之前结账期间仅供查看
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("ptID", ptID);
                        List<DzPay> periodList = dzPayDao.querySeePeriod(param);
                        result.put("periodList", periodList);
                    }
                }
            }
        } catch (BusinessException e) {
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            return result;
        }
        result.put("success", "true");
        return result;
    }

    @SuppressWarnings("unused")
    public void createTemp() {
        try {
            String accountID = getAccount().getAccountID();
            tempTypeService.generateTemplate(getAccount().getAccountID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取时间切换可选择元素和提示预警信息
    @RequestMapping("/getWarnInfo")
    @ResponseBody
    Map<String, Object> getWarnInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取到平台用户ID
            User user = getUser();
            Integer userType = user.getUserType();
            String ptID = null;
            if (userType == 5 || userType == 6) {
                String userID = user.getParentUser();
                if (StringUtils.isBlank(userID)) {
                    result.put("success", "true");
                    result.put("msg", "测试用户没有父类用户，《司》出问题改这里");
                    return result;
                }
                User use = userDao.queryUserById(userID);
                ptID = use.getId();
            } else if (userType == 2 || userType == 3) {
                ptID = user.getId();
            }
            if (ptID == null) {
                logger.error("AccountController【chgAccount】 获取预警信息异常!");
                result.put("success", "false");
            } else {
                // 调用商城接口获取当前管理员名下余额
                /*
                 * String url = pro1.getProperty("wqb_url") +
                 * "/Customer/GetMyAssets"; Map<String, String> createMap = new
                 * HashMap<String, String>(); createMap.put("customerId", ptID);
                 * createMap.put("dataType", "json"); String
                 * httpOrgCreateTestRtn = httpClientUtil.doPost(url, createMap,
                 * pro1.getProperty("wqb_charset")); Map<?, ?> maps = (Map<?,
                 * ?>) JSON.parse(httpOrgCreateTestRtn); Map<?, ?> map = (Map<?,
                 * ?>) maps.get("data"); String ableAmountStr =
                 * map.get("Balance").toString(); double ableAmount =
                 * Double.parseDouble(ableAmountStr);
                 */
                double ableAmount = 1000000;
                double price = 0;
                int wareCount = 0;
                if (userType == 2 || userType == 5) {
                    wareCount = Integer.parseInt(properties.get("dzWarnCount").toString().trim());// 30
                    price = Double.parseDouble(properties.get("dzPrice").toString().trim());// 8
                } else if (userType == 3 || userType == 6) {
                    wareCount = Integer.parseInt(properties.get("jzWarnCount").toString().trim());// 5
                    price = Double.parseDouble(properties.get("jzPrice").toString().trim());// 39.9
                }
                if (price != 0 && ableAmount / price < wareCount) {
                    result.put("warnInfo", "您当前账户余额仅还能做" + ableAmount / price + "次账,请及时充值");
                }
            }
        } catch (BusinessException e) {
            result.put("success", "false");
            result.put("warnInfo", "获取预警信息异常");
            return result;
        } catch (Exception e) {
            result.put("success", "false");
            result.put("warnInfo", "获取预警信息异常");
            return result;
        }
        result.put("success", "true");
        return result;
    }

    // 1 用户切换账套 头部
    //// 2 1getAccList获取列表页-》2 点击记账 clickAccount
    @RequestMapping("/chgAccount1")
    @ResponseBody
    Map<String, Object> chgAccount1(String accountID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            Map<String, Object> upMap = new HashMap<>();
            // 根据确定的账套id获取用户账套
            Account account = accountService.queryAccByID(accountID);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) map.get("user");
            if (user.getUserType() == 3 || user.getUserType() == 2) {
                upMap.put("userID", user.getUserID());
                // 如果是管理员 根据用户id记录最后账套切换时间
                UserAccount user_acc = accountDao.queryAcc(upMap); // userAcc.queryAcc
                if (user_acc == null) {
                    UserAccount userAccNew = new UserAccount();
                    userAccNew.setAccountID(accountID);
                    userAccNew.setLastTime(System.currentTimeMillis());
                    userAccNew.setUserID(user.getUserID());
                    userAccNew.setMg_id(UUIDUtils.getUUID());
                    userAccNew.setUser_phone(user.getLoginUser());
                    accountDao.insertUserAcc(userAccNew);
                } else {
                    upMap.put("accountID", accountID);
                    accountDao.chgAcc(upMap); // 如果是管理员登录切换账套 那么以用户id更新它的账套
                    // update t_user_acc accountID=#{accountID} where
                    // userID=#{userID}
                }
            }
            map.put("account", account);

            Integer count = accountService.chgAccLastTime(new Date(), accountID);

            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("accountID", account.getAccountID());
            pa.put("busDate", getUserDate());
            pa.put("period", getUserDate());
            List<StatusPeriod> list = periodStatusService.queryStatus(pa);
            if (list == null || list.size() == 0) {
                StatusPeriod statusPeriod = new StatusPeriod();
                statusPeriod.setPeriodID(UUIDUtils.getUUID());
                statusPeriod.setCreatePsn(getUser().getUserName());
                statusPeriod.setCreatePsnID(getUser().getUserID());
                statusPeriod.setIsCarryState(0);
                statusPeriod.setIsCreateVoucher(0);
                statusPeriod.setIsJz(0);
                statusPeriod.setIsJt(0);
                statusPeriod.setPeriod(getUserDate());
                statusPeriod.setAccountID(account.getAccountID());
                periodStatusService.insertPeriodStatu(statusPeriod);
            }
            List<Progress> proList = progressDao.queryProgress(pa);
            if (proList != null && proList.size() > 0 && proList.get(0) != null) {

            } else {
                Progress pro = new Progress();
                pro.setId(UUIDUtils.getUUID());
                pro.setAccountID(account.getAccountID());
                pro.setPeriod(getUserDate());
                pro.setCv(0);
                pro.setJt(0);
                pro.setUnJt(0);
                pro.setCarryState(0);
                pro.setUnCarryState(0);
                pro.setJz(0);
                progressDao.addProgress(pro);
            }
            if (count > 0) {
                result.put("message", "success");
                result.put("statu", account.getInitialStates());
            } else {
                result.put("message", "fail");
            }
            return result;
        } catch (BusinessException e) {
            logger.error("AccountController【chgAccount】 用户切换账套异常!", e);
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AccountController【chgAccount】 用户切换账套异常!", e);
            result.put("message", "fail");
            return result;

        }
    }

    @RequestMapping("/mappingStates")
    @ResponseBody
    public Map<String, Object> mappingStates() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            result = accountService.mappingStates(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
