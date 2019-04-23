package com.wqb.controller.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.customer.CustomerDao;
import com.wqb.dao.firstPage.FirstPageDao;
import com.wqb.dao.pay.DzPayDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.permission.dao.PermissionDao;
import com.wqb.dao.user.UserDao;
import com.wqb.httpClient.HttpClientUtil;
import com.wqb.model.*;
import com.wqb.model.vomodel.AccStatusPeriod;
import com.wqb.service.app.AccountAppService;
import com.wqb.service.log.service.LoginLogService;
import com.wqb.service.login.LoginService;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/toSystem")
public class IntoSystemController extends BaseController {

    private static Log4jLogger logger = Log4jLogger.getLogger(IntoSystemController.class);
    @Value("${wqb_url}")
    private String url;
    @Value("${wqb_charset}")
    private String charset;
    @Value("${wqb_trialTime}")
    private int trialTime;
    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setTrialTime(int trialTime) {
        this.trialTime = trialTime;
    }

    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    CustomerDao customerDao;

    @Autowired
    PermissionDao permissionDao;
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    FirstPageDao firstPageDao;
    @Autowired
    AccountAppService accountAppService;
    @Autowired
    DzPayDao dzPayDao;
    @Autowired
    LoginLogService loginLogService;
    @Autowired
    LoginService loginService;

    public ModelAndView tiaozhuan1() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/index");
        return mav;
    }

    // 商城平台登录入口

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView intoSystem(String token) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            String remoteAddr = getRequest().getRemoteAddr(); // 获取本地ip

            String httpOrgCreateTest = url + "Customer/GetCustomerInfo";
            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("Token", token);
            String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap, charset);

            JSONObject jsonObject = JSONObject.parseObject(httpOrgCreateTestRtn);
            Map<String, Object> maps = jsonObject.getInnerMap();
            Map<?, ?> map = (Map<?, ?>) maps.get("data");
            String Id = map.get("Id").toString();// 53880508-df33-4ec3-8d52-d95ecaba8f50
            String Mobile = map.get("Mobile").toString();// 13000000004
            boolean JZFW = Boolean.parseBoolean(map.get("JZFW").toString());
            boolean DZFW = Boolean.parseBoolean(map.get("DZFW").toString()); // true
            boolean IsStaff = Boolean.parseBoolean(map.get("IsStaff").toString());

            if (!JZFW && !DZFW && !IsStaff) {
                // 不是记账用户 不是代账用户 也不是员工，标记为测试用户type设置为1， 使用期间3天，用户类型为为C端员工只能开一个账套
                User user = userDao.queryUserByLineID(Id);
                if (user == null) {
                    user = new User();
                    String userID = UUIDUtils.getUUID();
                    user.setUserID(userID);
                    user.setLoginUser(Mobile);
                    user.setUserType(6);
                    user.setState(1);
                    user.setAbleDate(new Date());
                    user.setDisableDate(getdate(3));
                    user.setUpdatePsn("11");
                    user.setCreateDate(new Date());
                    user.setCreatePsn("11");
                    user.setParentUser(userID);
                    user.setId(Id);
                    user.setPasword("E10ADC3949BA59ABBE56E057F20F883E");
                    user.setUpdateDate(new Date());
                    user.setType(1);
                    userDao.addUser(user);
                }
                Map<String, Object> param = new HashMap<String, Object>();
                List<Permission> perList = permissionDao.queryPreByUserID(user.getUserID());
                List<String> actionList = new ArrayList<String>();
                if (null != perList) {
                    for (int s = 0; s < perList.size(); s++) {
                        Permission perms = perList.get(s);
                        String actionID = perms.getActionID();
                        actionList.add(actionID);
                    }
                }
                List<Account> accList = accountDao.queryByUserID(user.getUserID());
                if (accList != null && accList.size() > 0) {
                    param.put("account", accList.get(0));
                }
                Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
                String busDate = null;
                if (null == sessionMap) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    busDate = sdf.format(date);
                } else {
                    if (sessionMap.get("busDate") != null) {
                        busDate = sessionMap.get("busDate").toString();
                    } else {
                        busDate = DateUtil.getMoth2(new Date());
                    }
                }
                param.put("user", user);
                param.put("busDate", busDate);
                param.put("isAdmin", 0);
                param.put("perList", actionList);
                session.setAttribute("userDate", param);
                session.setMaxInactiveInterval(60 * 60 * 4);
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("userID", user.getUserID());
                p.put("sessionID", session.getId());
                userDao.updSessionIDbyID(p);
                return backView("system/index", param);
            }

            // 模拟数据 ， 绕过tonken登录 13000000004
            // String Id = "53880508-df33-4ec3-8d52-d95ecaba8f50"; //模拟数据 自己的
            // String Id = "589b4825-9c35-49a5-91fb-526990fbb8fb"; //模拟数据 高经理

            User user = userDao.queryUserByLineID(Id);// userID=0efd3c173cf94e3586265138cecbd77b

            Integer userType = null;

            String userID = null;

            if (JZFW || DZFW) {
                // 购买了代账 或者记账 type设置为0 正式用户
                if (user == null) {
                    user = new User();
                    userID = UUIDUtils.getUUID();
                    user.setUserID(userID);
                    user.setLoginUser(Mobile);
                    if (JZFW) {
                        user.setUserType(3);
                    } else if (DZFW) {
                        user.setUserType(2);
                    }
                    user.setType(0);
                    user.setState(1);
                    user.setAbleDate(new Date());
                    user.setDisableDate(getdate(365));
                    user.setUpdatePsn(userID);
                    user.setCreateDate(new Date());
                    user.setCreatePsn(userID);
                    user.setParentUser("0");
                    user.setId(Id);
                    user.setUserName("管理员");
                    user.setPasword("E10ADC3949BA59ABBE56E057F20F883E");
                    user.setUpdateDate(new Date());
                    userDao.addUser(user);
                } else {
                    // 用户不为空 1可能为正式用户 2可能为测试用户
                    // 正式用放行，如果是测试用户类型购买了记账或者代账需要变更用户信息
                    // 测试用户购买服务变为正式用户 需要把它底下的所有账套来源source更改为 购买服务的用户ID
                    if (user.getType() != null && user.getType() == 1) {
                        if (DZFW) {
                            userType = 2;
                        }
                        if (JZFW) {
                            userType = 3;
                        }
                        user.setType(0);
                        user.setUserType(userType);
                        user.setAbleDate(new Date());
                        user.setDisableDate(getdate(365));
                        user.setState(1);
                        user.setUserName("管理员");
                        user.setParentUser("0");
                        userDao.upUserByUid(user);
                    }
                }
            }

            if (null == user) {
                return null;
            } else {
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("userID", user.getUserID());
                p.put("sessionID", session.getId());
                userDao.updSessionIDbyID(p);
            }
            Map<String, Object> param = new HashMap<String, Object>();
            List<Permission> perList = permissionDao.queryPreByUserID(user.getUserID()); // 管理员不需要权限，员工才有权限控制
            List<String> actionList = new ArrayList<String>();
            if (null != perList) {
                for (int s = 0; s < perList.size(); s++) {
                    Permission perms = perList.get(s);
                    String actionID = perms.getActionID();
                    actionList.add(actionID);
                }
            }
            int type = user.getUserType();
            if (type == 2 || type == 3) {
                // 先分配管理员的账套
                Map<String, Object> map5 = new HashMap<>();

                map5.put("userID", user.getUserID());

                Account acc_one = null;
                UserAccount user_acc = accountDao.queryAcc(map5);
                if (user_acc != null) {
                    String accountID = user_acc.getAccountID();
                    if (!StringUtil.isEmpty(accountID)) {
                        Account acc_two = accountDao.queryAccByID(accountID);
                        if (acc_two != null) {
                            acc_one = acc_two;
                            param.put("account", acc_two);
                        }
                    }
                }

                if (acc_one == null) {
                    List<Account> mg_acc = accountDao.queryByUserID(user.getUserID());
                    if (mg_acc != null && mg_acc.size() > 0) {
                        Account account = getAccount(mg_acc);
                        param.put("account", account);
                    } else {
                        // 后分配员工账套
                        List<User> userList = userDao.queryUserByParentID(user.getUserID());
                        if (null != userList && userList.size() > 0) {
                            for (int i = 0; i < userList.size(); i++) {
                                List<Account> accList = accountDao.queryByUserID(userList.get(i).getUserID());
                                if (accList != null && accList.size() > 0) {
                                    Account account = getAccount(accList);
                                    param.put("account", account);
                                    break;
                                }
                            }
                        }
                    }
                }

            } else if (type == 5 || type == 6) {
                List<Account> accList = accountDao.queryByUserID(user.getUserID());
                if (accList != null && accList.size() > 0) {
                    Account account = getAccount(accList);
                    param.put("account", account);
                }
            }

            // 获取账套最后使用期间
            Account acc = (Account) param.get("account");
            String per = null;
            if (acc != null && acc.getUseLastPeriod() != null) {
                per = acc.getUseLastPeriod();
            } else {
                if (acc == null) {
                    per = DateUtil.getMonth();
                } else {
                    Date d1 = acc.getPeriod();
                    per = DateUtil.getMoth2(d1 != null ? d1 : (new Date()));
                }
            }

            // PC 登录后台查询是否有session,没有的话 把用户信息，期间，权限，账套给用户
            // Map<String, Object> sessionMap = (Map<String, Object>)
            // getSession().getAttribute("userDate");
            param.put("user", user);
            param.put("busDate", per);
            if (type == 2 || type == 3) {
                session.setAttribute("isAdmin", 1);
            } else {
                param.put("isAdmin", 0);
                param.put("perList", actionList);
            }
            session.setAttribute("userDate", param);

            session.setAttribute("login_phone", user.getLoginUser());
            session.setAttribute("loing_ip", remoteAddr); // 登录ip
            session.setAttribute("credate", System.currentTimeMillis());// 登录时间

            return backView("system/index", param);
        } catch (Exception e) {
            getSession().invalidate();
            e.printStackTrace();
            result.put("success", "false");
            result.put("message", "进入系统异常");
            result.put("errors", "sorry 您的账号出现异常啦,请联系管理员");
            return backView("common/register", result);
        }

    }

    public Account getAccount(List<Account> list) {
        Account account = null;
        int j = -1;
        Date time_satrt = new Date(0); // 1970
        for (int i = 0; i < list.size(); i++) {
            Account mg_account = list.get(i);
            Date lastTime = mg_account.getLastTime();
            if (lastTime != null) {
                boolean after = lastTime.after(time_satrt);
                if (after) {
                    time_satrt = lastTime;
                    j = i;
                }
            }
        }
        if (j > -1) {
            account = list.get(j);
        } else {
            account = list.get(0);
        }
        return account;
    }

    @RequestMapping("/getAccList")
    @ResponseBody
    Map<String, Object> getAccList(String keyWord, String curPage, String maxSize) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "fail");
        try {
            Map<String, Object> qurMap = new HashMap<>();
            User userTrue = getUser(); // 用户信息
            String busDate = getUserDate(); // 期间
            Account account = getAccount();
            String userID = userTrue.getUserID(); // 用户id
            int type = userTrue.getUserType();
            List<Account> accs = new ArrayList<>(); // 定义账套集合
            List<FirstPageEntity> fsList = new ArrayList<FirstPageEntity>();

            int curPageInt = (StringUtil.isEmpty(curPage) == true) ? 1 : Integer.valueOf(curPage);
            int maxSizeInt = (StringUtil.isEmpty(maxSize) == true) ? 10 : Integer.valueOf(maxSize);

            /****************** start *****************/
            // 判断 keyWor为汉字还是拼音
            // 如果是汉字查询企业名称
            // 如果是拼音查询 企业名称拼音

            if (!StringUtil.isEmpty(keyWord)) {
                // 第一种匹配
                /*
                 * Pattern p = Pattern.compile("[a-zA-Z]+"); Matcher matcher =
                 * p.matcher(keyWord); //// // 字符串是否与正则表达式相匹配 boolean bool =
                 * matcher.matches();
                 */

                // 第二种匹配
                boolean matches = keyWord.matches("[a-zA-Z]+");
                if (matches) {
                    String lowerCase = keyWord.toLowerCase(); // 转换成小写
                    qurMap.put("companyNamePinYin", StringUtil.replacekeyWord(lowerCase));
                } else {
                    qurMap.put("keyWord", StringUtil.replacekeyWord(keyWord));
                }
            }

            int begin = (curPageInt - 1) * maxSizeInt;
            qurMap.put("begin", begin);
            qurMap.put("pageSize", maxSizeInt);

            qurMap.put("statu", 1);
            qurMap.put("lastTime", "1");

            if (type == 2 || type == 3) {
                qurMap.put("source", userID);
            } else {
                qurMap.put("userID", userID);
            }

            // 获取起始页后十条数据
            accs = accountDao.queryAccByCondition(qurMap);

            // 如果是第一页 账套列表默认选择第一条
            if (curPageInt == 1 && account != null && !accs.isEmpty()) {
                int ind = accs.indexOf(account);
                if (ind > 0) {
                    Account acc_ind = accs.get(ind);
                    accs.remove(ind);
                    accs.add(0, acc_ind);
                }
                if (ind < 0) {
                    // 如果有查询,取查询条件数据，查询结果不包括当前账套。 除非当前账套包含查询关键词 比如 查询 深圳科迪有限公司
                    // 输入‘科迪’ 当前账套须包含 ‘科迪’字符串
                    if (!StringUtil.isEmpty(keyWord)) {
                        System.out.println();
                        /*
                         * qurMap.put("pageSize", 10); accs =
                         * accountDao.queryAccByCondition(qurMap);
                         */
                    } else {
                        // 没有查询 当前账套也不在十条数据之内，把当前账套设置为第一条显示
                        // 因为当前账套肯定属于这个用户，如果进入账套列表第一页没有当前账套，那我们就把当前账套置顶添加到第一页去并把索引设为0，显示就可以置顶
					/*	qurMap.put("pageSize", 9);
						qurMap.put("myself", "myself");
						qurMap.put("accountID", account.getAccountID());
						accs = accountDao.queryAccByCondition(qurMap);
						qurMap.remove("myself");
						qurMap.remove("accountID");*/
                        accs.add(0, account);
                    }
                }
            }

            // 获取总页码
            qurMap.remove("lastTime");
            qurMap.remove("begin");
            qurMap.remove("pageSize");
            int count_num = accountDao.queryAccByConditionCount(qurMap); // 分页总数
            // 包含搜索关键词的查询

            // 用户所有的账套
            // 不包含搜索关键词的查询
            qurMap.remove("keyWord");
            qurMap.remove("companyNamePinYin");
            int count = accountDao.queryAccByConditionCount(qurMap);

            // 查询进行中总数 isCreateVoucher = 1 and isJz!=1
            qurMap.put("period", busDate);
            int runing = accountDao.queryAccRuning(qurMap);
            // 查询已完成总数 and isJz=1
            int finish = accountDao.queryAccFinish(qurMap);

            int noFinish = count - runing - finish;

            getAccPage(busDate, accs, fsList);

            result.put("success", "true");
            result.put("returnList", fsList);
            result.put("count", count_num);

            result.put("noFinish", noFinish);
            result.put("finish", finish);
            result.put("runing", runing);

            return result;

            /****************** end *****************/
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            result.put("success", "fail");
            result.put("message", "暂无账套!");
            return result;
        }

    }

    @RequestMapping("/getAccListStatu")
    @ResponseBody
    Map<String, Object> getAccListStatu(@RequestParam(required = true) String curPage, String maxSize,
                                        @RequestParam(required = true) String statuType) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            Map<String, Object> qurMap = new HashMap<>();

            User userTrue = getUser();
            String busDate = getUserDate();
            String userID = userTrue.getUserID();

            int type = userTrue.getUserType();

            List<AccStatusPeriod> acc_per_10 = new ArrayList<>();

            List<FirstPageEntity> fsList = new ArrayList<FirstPageEntity>(); // 定义结果集合

            int count_num = 0;

            int curPageInt = (StringUtil.isEmpty(curPage) == true) ? 1 : Integer.valueOf(curPage);
            int maxSizeInt = (StringUtil.isEmpty(maxSize) == true) ? 10 : Integer.valueOf(maxSize);

            /****************** start *****************/

            // 查询完成
            List<String> list1 = new ArrayList<>(); // 定义全部账套IDS
            List<String> list2 = new ArrayList<>(); // 定义已经做账的账套IDS

            List<Account> acc10 = new ArrayList<>();

            int begin = (curPageInt - 1) * maxSizeInt;
            qurMap.put("begin", begin);
            qurMap.put("pageSize", maxSizeInt);

            if (statuType.equals("0")) {
                qurMap.put("statuType0", "statuType0");
            }
            if (statuType.equals("1")) {
                qurMap.put("statuType1", "statuType1");
            }
            if (statuType.equals("2")) {
                qurMap.put("statuType2", "statuType2");
            }

            if (type == 2 || type == 3) {
                qurMap.put("source", userID);
            }
            if (type == 5 || type == 6) {
                qurMap.put("userID", userID);
            }

            // 账套状态
            qurMap.put("statu", 1);
            qurMap.put("period", busDate);

            if (statuType.equals("2")) {

                Map<String, Object> map5 = new HashMap<>();
                map5.put("statu", 1);
                if (type == 2 || type == 3) {
                    map5.put("source", userID);
                } else {
                    map5.put("userID", userID);
                }

                list1 = accountDao.queryAidByCondition(map5); // 查询名下所有账套

                map5.put("period", busDate);
                // 查询已开始做账的账套
                List<String> list3 = accountDao.queryStartStaPer(map5);

                // 计算未开始的账套id
                if (list3 != null & list3.size() > 0) {
                    list1.removeAll(list3);
                }

                if (!list1.isEmpty()) {
                    count_num = list1.size(); // 页码总数
                    qurMap.put("ids", list1);
                    qurMap.remove("period");
                    // 得到未完成的账套十个账套
                    acc10 = accountDao.queryAccByCondition(qurMap);
                }
            }

            // 查询已完成 进行中
            if (statuType.equals("0") || statuType.equals("1")) {
                acc_per_10 = accountDao.queryAccJoinStatusPeriod(qurMap);
                qurMap.remove("begin");
                if (statuType.equals("0")) { // 查询已完成
                    count_num = accountDao.queryAccFinish(qurMap);
                }
                if (statuType.equals("1")) { // 查询进行中
                    count_num = accountDao.queryAccRuning(qurMap);
                }
            }
            if (!acc_per_10.isEmpty()) { // 0 , 1
                getAcc10(acc_per_10, acc10);
            }
            getAccPage(busDate, acc10, fsList); // 获取来自分页的十个账套id

            result.put("success", "true");
            result.put("returnList", fsList);
            result.put("count", count_num);
            return result;

            /****************** end *****************/
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            result.put("success", "fail");
            result.put("message", "暂无账套!");
            return result;
        }

    }

    public List<StatusPeriod> queryStaPer(String period, List<String> list) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", list);
        map.put("period", period);
        List<StatusPeriod> spList = accountDao.queryStaPeriod2(map);
        return spList;
    }

    private void getAccPage(String busDate, List<Account> accs, List<FirstPageEntity> fsList) throws BusinessException {

        if (accs != null && accs.size() > 0) {

            List<String> accList = new ArrayList<>();
            List<String> userList = new ArrayList<>();

            Map<String, User> uMap = new HashMap<>();

            Set<String> uset = new HashSet<>();
            for (Account acc : accs) {
                if (acc != null) {
                    uset.add(acc.getUserID());
                    accList.add(acc.getAccountID());
                }
            }
            userList.addAll(uset);
            List<User> ulist = userDao.queryUserBath(userList);
            for (User user : ulist) {
                uMap.put(user.getUserID(), user);
            }

            List<Customer> cusList = customerDao.queryCusBath(accList);
            // select * from t_customer where accountID in ( ? )
            // d2535085bc6949b69be482a0d75241f8
            Map<String, Customer> cusMap = new HashMap<>();
            if (null == cusList || cusList.size() == 0) {
                String sss = "Class: " + this.getClass().getName() + " method: "
                        + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:"
                        + Thread.currentThread().getStackTrace()[1].getLineNumber();

                throw new BusinessException(sss + "查询账套企业信息异常：accountID = " + accList);
            }
            for (Customer customer : cusList) {
                cusMap.put(customer.getAccountID(), customer);
            }

            List<StatusPeriod> perStatuList = queryStaPer(busDate, accList);

            Map<String, StatusPeriod> perStatuMap = new HashMap<>();
            if (perStatuList != null && !perStatuList.isEmpty()) {
                for (StatusPeriod stu : perStatuList) {
                    if (stu != null) {
                        perStatuMap.put(stu.getAccountID(), stu);
                    }
                }
            }

            for (int i = 0; i < accs.size(); i++) {
                Account account = accs.get(i);
                if (account != null) {
                    FirstPageEntity firstPageEntity = new FirstPageEntity();

                    String acc_id = account.getAccountID();

                    StatusPeriod sp = perStatuMap.get(acc_id);

                    if (sp == null) {
                        firstPageEntity.setCv(0);
                        firstPageEntity.setCs(0);
                        firstPageEntity.setJz(0);
                    } else {
                        firstPageEntity.setCv(sp.getIsCreateVoucher());
                        firstPageEntity.setCs(sp.getIsCarryState());
                        firstPageEntity.setJz(sp.getIsJz());
                    }

                    String user_id = account.getUserID();
                    User user = uMap.get(user_id);
                    if (user == null) {
                        user = userDao.queryUserById(user_id);
                        uMap.put(user_id, user);
                    }

                    String userName = user.getUserName();
                    String loginUser = user.getLoginUser();

                    Customer customer = cusMap.get(acc_id);

                    String telephone = "";
                    String cusName = "";
                    if (null != customer) {
                        cusName = customer.getCusName();
                        telephone = customer.getCusPhone();
                    }

                    telephone = StringUtil.isEmpty(telephone) == true ? loginUser : telephone;
                    cusName = StringUtil.isEmpty(cusName) == true ? account.getCompanyName() : cusName;

                    firstPageEntity.setCusName(cusName); // 客户名称
                    firstPageEntity.setTelephone(telephone); // 客户电话
                    String period = DateUtil.getMoth2(account.getPeriod());
                    firstPageEntity.setPeriod(period);
                    String statu = account.getStatu().toString();
                    firstPageEntity.setStatu(statu); // 设置状态
                    firstPageEntity.setOperation(userName == null ? loginUser : userName); // 操作员
                    firstPageEntity.setAccountID(acc_id);
                    firstPageEntity.setClName(userName == null ? loginUser : userName); // 用户名称
                    fsList.add(firstPageEntity);
                }
            }
        }
    }

    public void getAcc_ids(List<String> list, List<Account> acc) {
        if (acc != null & acc.size() > 0) {
            for (int i = 0; i < acc.size(); i++) {
                Account account = acc.get(i);
                if (account != null) {
                    list.add(account.getAccountID());
                }
            }
        }
    }

    public void getAcc10(List<AccStatusPeriod> list, List<Account> acc10) {
        if (list != null && list.size() > 0) {
            for (AccStatusPeriod ap : list) {
                if (ap != null) {
                    Account ac = new Account();
                    ac.setAccountID(ap.getAccountID());
                    ac.setUserID(ap.getUserID());
                    ac.setPeriod(ap.getPeriod());
                    ac.setCompanyName(ap.getCompanyName());
                    ac.setStatu(ap.getStatu());
                    acc10.add(ac);
                }
            }
        }
    }

    public static Date getdate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
    {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, i);
        dat = cd.getTime();
        return dat;
    }

    @RequestMapping("/queryUserByPtID")
    @ResponseBody
    Map<String, Object> queryUserByPtID(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // id = "b8243ba9-7f0e-4894-ac60-3779c179caf2";

            User user = userDao.queryUserByLineID(id);
            result.put("success", "true");
            if (user == null) {
                result.put("empty", "true");
            } else {
                result.put("empty", "false");

                Integer type = user.getType();
                if (type != null && type == 1) {
                    result.put("type", "true");
                } else {
                    result.put("type", "false");
                }

                Date abDate = user.getDisableDate();

                if (abDate != null && (new Date().before(abDate))) {
                    result.put("timeout", "false");
                } else {
                    result.put("timeout", "true");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            result.put("message", "查询财税用户异常!");
            return result;
        }
    }

    @RequestMapping("/queryUserStateById")
    @ResponseBody
    Map<String, Object> queryUserStateById(String id) {

        // id = "589b4825-9c35-49a5-91fb-526990fbb8fb"; 13600000001 陈建中管理员

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = userDao.queryUserByLineID(id); // 平台id
            result.put("success", "true");
            if (user == null) {
                result.put("empty", "true");
            } else {
                Integer userType = user.getUserType();

                result.put("empty", "false");

                Integer type = user.getType();

                if (userType == 2 || userType == 3) {
                    int state = user.getState();
                    result.put("manager", "true");

                    if (state != 1) {
                        result.put("state", "false");
                    } else {
                        result.put("state", "true");

                        result.put("timeout", "false");
                        if (user.getDisableDate().before(new Date())) {
                            result.put("timeout", "false");
                        }
                    }
                } else if (userType == 5 || userType == 6) {
                    result.put("manager", "false");
                    String parentUser2 = user.getParentUser();
                    User parentUser = userDao.queryUserById(parentUser2); // 用户id
                    if (parentUser.getState() == 2) {
                        result.put("state", "false");
                    } else {
                        result.put("state", "true");

                        result.put("timeout", "false");
                        Date disableDate = parentUser.getDisableDate();
                        // System.out.println(disableDate.toLocaleString());
                        if (disableDate.before(new Date())) {
                            result.put("timeout", "false");
                        }

                        if (user.getState() == 2) {
                            result.put("sonstate", "false");
                        } else {
                            result.put("sonstate", "true");
                        }
                    }
                } else {
                    result.put("success", "fail");
                    result.put("msg", "查询异常");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            result.put("msg", "查询异常!");
            return result;
        }
    }

    @RequestMapping("/queryUserInfo")
    @ResponseBody
    Map<String, Object> queryUserInfo(String ptID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = userDao.queryUserByLineID(ptID);
            if (user == null) {
                result.put("success", "false");
                result.put("msg", "用户不存在");
                return result;
            }
            Integer userType = user.getUserType();
            if (userType == 2 || userType == 5) {
                result.put("success", "true");
                result.put("type", 1);
                return result;
            } else if (userType == 3 || userType == 6) {
                result.put("success", "true");
                result.put("type", 2);
                if (userType == 3) {
                    List<Account> accountList1 = accountDao.queryByUserID(user.getUserID());
                    if (null != accountList1 && !accountList1.isEmpty()) {
                        result.put("ssType", accountList1.get(0).getSsType());
                    } else {
                        List<User> userList = userDao.queryUserByParentID(user.getUserID());
                        if (userList != null && !userList.isEmpty()) {
                            for (User use : userList) {
                                String userID = use.getUserID();
                                List<Account> accountList = accountDao.queryByUserID(userID);
                                if (accountList != null && !accountList.isEmpty()) {
                                    result.put("ssType", accountList.get(0).getSsType());
                                    break;
                                }
                            }
                        }
                    }
                } else if (userType == 6) {
                    List<Account> accountList2 = accountDao.queryByUserID(user.getUserID());
                    if (null != accountList2 && !accountList2.isEmpty()) {
                        result.put("ssType", accountList2.get(0).getSsType());
                    }
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "查询失败");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "查询失败");
            return result;
        }
        return result;
    }

    @RequestMapping("/getFirstZl")
    @ResponseBody
    Map<String, Object> getFirstZl(String time) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取本月新增
            User user = getUser();
            String userID = user.getUserID();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userID", userID);
            param.put("time", time);
            Object byxzqys = accountDao.getByxzqy(param);
            Map<String, Object> byxzMap = (Map<String, Object>) byxzqys;
            int byxz = 0;
            if (!StringUtil.objEmpty(byxzMap.get("sl"))) {
                byxz = Integer.parseInt(byxzMap.get("sl").toString());
            }
            result.put("byxz", byxz);
            Object syzqyObj = accountDao.getSyzqy(param);
            Map<String, Object> syzqyMap = (Map<String, Object>) syzqyObj;
            int syzqy = Integer.parseInt(syzqyMap.get("sl").toString());
            if (syzqy != 0) {
                String zz = (double) byxz * 100 / syzqy + "%";
                result.put("zz", zz);
            }

            Object tyzqysObj = accountDao.getZtyqys(param);
            Map<String, Object> tyzqysMap = (Map<String, Object>) tyzqysObj;
            int tyzqys = Integer.parseInt(tyzqysMap.get("sl").toString());
            result.put("tyzqys", tyzqys);

            Object zqysObj = accountDao.getZqys(param);
            Map<String, Object> zqysMap = (Map<String, Object>) zqysObj;
            int zqys = Integer.parseInt(zqysMap.get("sl").toString());
            if (zqys != 0) {
                String zb = (double) tyzqys * 100 / zqys + "%";
                result.put("zb", zb);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "获取总览数据异常");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "获取总览数据异常");
            return result;
        }
        result.put("success", "true");
        return result;
    }
}
