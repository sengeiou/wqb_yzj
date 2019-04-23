package com.wqb.controller.basic;


import com.wqb.common.*;
import com.wqb.common.sendSms.WqbSmsSendRequest;
import com.wqb.common.sendSms.WqbSmsSendResponse;
import com.wqb.common.sendSms.WqbSmsUtil;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.permission.dao.PermissionDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.userOrder.UserOrderDao;
import com.wqb.model.*;
import com.wqb.model.vomodel.UserLoingVo;
import com.wqb.model.vomodel.UserVo;
import com.wqb.service.common.loingThread;
import com.wqb.service.log.service.LoginLogService;
import com.wqb.service.login.LoginService;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/system")
public class LoginController extends BaseController {
    @SuppressWarnings("unused")
    private static Log4jLogger logger = Log4jLogger.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;
    @Autowired
    LoginLogService loginLogService;
    @Autowired
    AccountDao accountDao;
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    ProgressDao progressDao;
    @Autowired
    UserDao userDao;
    @Autowired
    UserOrderDao userOrderDao;
    @Autowired
    PermissionDao permissionDao;

    // 登录
    @SuppressWarnings("unused")
    @RequestMapping("/login")
    @ResponseBody
    public void login(@RequestParam(required = true) String loginUser, @RequestParam(required = true) String password,
                      HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> returnMap = null;
        HttpSession session = null;
        try {
            returnMap = loginService.login(loginUser, password);
            Map<String, Object> param = new HashMap<String, Object>();
            String url = request.getContextPath() + "/system/toLogin?code=";
            if (returnMap == null || returnMap.get("code") == null || !returnMap.get("code").equals("0")) {
                response.sendRedirect(url + "3&errors=" + StringUtil.errEncode((returnMap == null || returnMap.get("msg") == null) ? "未知异常" : returnMap.get("msg").toString()));
                return;
            }
            User user = (User) returnMap.get("user");
            if (user == null) {
                response.sendRedirect(url + "5&errors=" + StringUtil.errEncode("登录失败"));
                return;
            }

            session = getRequest().getSession();

            // 第一次注册的用户 还没有建账套 account肯定为空 也没有期间
            String busDate = null;
            Object accountObj = returnMap.get("account");
            if (null != accountObj) {
                Account account = (Account) accountObj;
                if (account.getUseLastPeriod() != null) {
                    busDate = account.getUseLastPeriod();
                } else if (account.getLastTime() != null) {
                    busDate = DateUtil.getMoth2(account.getLastTime());
                } else {
                    busDate = DateUtil.getMoth2(account.getPeriod());
                }
            }
            if (busDate == null) {
                busDate = DateUtil.getMonth();
            }

            returnMap.put("busDate", busDate);
            Account account = (Account) returnMap.get("account");
            String ip = getIp2(request);
            UserLoingVo userVo = new UserLoingVo(user, account, busDate, ip);
            new Thread(new loingThread(userVo)).start();
            Integer userType = user.getUserType();
            if (userType == 2 || userType == 3) {
                session.setAttribute("isAdmin", 1);
            } else {
                // 查询权限表 获取该员工所有权限的操作ids
                List<Permission> perList = permissionDao.queryPreByUserID(user.getUserID()); // 管理员不需要权限，员工才有权限控制
                List<String> actionList = new ArrayList<String>();
                if (actionList != null) {
                    for (Permission per : perList) {
                        actionList.add(per.getActionID());
                    }
                }
                returnMap.put("perList", actionList);
                returnMap.put("isAdmin", 0);
            }

            session.setAttribute("login_phone", user.getLoginUser());
            session.setAttribute("loing_ip", ip);
            session.setAttribute("credate", System.currentTimeMillis());// 登录时间

            session.setAttribute("userDate", returnMap);
            session.setMaxInactiveInterval(60 * 60 * 4);

            response.sendRedirect(request.getContextPath() + "/system/index");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.invalidate();
            }
            try {
                //String message = Base64.getBase64("登录失败");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=999");
            } catch (Exception e3) {
            }
            return;
        }
    }


    /********************************************************************************************************/


    // 登录2
    @SuppressWarnings("unused")
    @RequestMapping("/accountLogin")
    @ResponseBody
    public void accountLogin(
            @RequestParam(required = true) String loginUser,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String userID,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            if (StringUtil.isEmpty(loginUser) || StringUtil.isEmpty(password)) {
                String message = StringUtil.errEncode("用户名或者密码不能为空");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=2&errors=" + message);
                return;
            }
            if (StringUtil.isEmpty(userID)) {
                String message = StringUtil.errEncode("请选择你需要登录的系统");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=21&errors=" + message);
                if (session != null) {
                    session.invalidate();
                }
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("loginUser", loginUser);
            map.put("password", Md5Utils.MD5(password));
            map.put("userID", userID);

            List<User> list = userDao.queryUserByCondition(map);
            if (list == null || list.isEmpty() || list.get(0) == null) {
                String message = StringUtil.errEncode("用户名或者密码错误");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=21&errors=" + message);
                if (session != null) {
                    session.invalidate();
                }
                return;
            }
            User relUser = list.get(0);

            Integer state = relUser.getState();
            if (state == null || state != 1) {
                String message = StringUtil.errEncode("您的账号已被禁用,请联系管理员");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=21&errors=" + message);
                return;
            }

            int userType = relUser.getUserType();

            Map<String, Object> param = new HashMap<>();
            param.put("lastTime", 1); // 排序
            param.put("statu", 1); // 启用
            param.put("limitLogin", "1"); // 取一条
            if (userType == 2 || userType == 3) {
                param.put("source", userID);
            } else {
                param.put("userID", userID);
            }

            //定义用户信息 账套 保存到session域中
            Map<String, Object> hashMap = new HashMap<>();

            // 根据时间排序查询用户最后使用的账套
            Account last_login_account = null;
            List<Account> accList = accountDao.queryAccByCondition(param);
            if (null != accList && accList.size() > 0 && accList.get(0) != null) {
                last_login_account = accList.get(0);
            }

            // 第一次注册的用户 还没有建账套 account肯定为空 也没有期间
            String busDate = null;
            if (null != last_login_account) {

                hashMap.put("account", last_login_account);

                if (last_login_account.getUseLastPeriod() != null) {
                    busDate = last_login_account.getUseLastPeriod();
                } else if (last_login_account.getLastTime() != null) {

                    busDate = DateUtil.getMoth2(last_login_account.getLastTime());
                } else {

                    busDate = DateUtil.getMoth2(last_login_account.getPeriod());
                }
            } else {

                hashMap.put("account", null);
            }

            if (busDate == null) {
                busDate = DateUtil.getMonth();
            }


            hashMap.put("busDate", busDate);

            hashMap.put("user", relUser);

            String ip = getIp2(request);
            UserLoingVo userVo = new UserLoingVo(relUser, last_login_account, busDate, ip);
            new Thread(new loingThread(userVo)).start();
            if (userType == 2 || userType == 3) {
                session.setAttribute("isAdmin", 1);
            } else {

                List<Permission> perList = permissionDao.queryPreByUserID(userID); // 管理员不需要权限，员工才有权限控制
                List<String> actionList = new ArrayList<String>();
                if (actionList != null) {
                    for (Permission per : perList) {
                        actionList.add(per.getActionID());
                    }
                }
                hashMap.put("perList", actionList);
                hashMap.put("isAdmin", 0);
            }

            session.setAttribute("login_phone", loginUser);
            session.setAttribute("loing_ip", ip);
            session.setAttribute("credate", System.currentTimeMillis());// 登录时间

            session.setAttribute("userDate", hashMap);
            session.setMaxInactiveInterval(60 * 60 * 4);

            response.sendRedirect(request.getContextPath() + "/system/index");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.invalidate();
            }
            try {
                //String message = Base64.getBase64("登陆失败");
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=6&errors=" + StringUtil.errEncode("登陆失败"));
            } catch (Exception e2) {
                try {
                    response.sendRedirect(request.getContextPath() + "/system/toLogin");
                } catch (Exception e3) {
                }
            }
            return;
        }
    }


    /********************************************************/

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (null != ip && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (null != ip && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    @RequestMapping("/loginOut")
    @ResponseBody
    String loginOut() {
        try {
            getSession().invalidate();
            return "success";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 路由配置
     *
     * @return
     */
    @RequestMapping("/toLogin")
    public ModelAndView toLogin() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/login");
        return mav;
    }

    @SuppressWarnings("unused")
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/index");
        return mav;
    }

    // sessiong 过期调用页面
    @RequestMapping("/sessionOver")
    public ModelAndView sessionOver() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/register");
        return mav;
    }

    // sessiong 过期调用页面
    @RequestMapping("/ajaxAuthentication")
    public ModelAndView ajaxAuthentication(String msg) {
        String res;
        try {
            res = new String(msg.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            res = "您的身份已过期请重新登录";
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("errors", res);
        mav.setViewName("common/register");
        return mav;
    }

    // 财税首页
    @RequestMapping("/main")
    public ModelAndView toMana() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/main");
        return mav;
    }

    // 选择期间
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/chgPeriod")
    @ResponseBody
    String chgPeriod(String period) {
        try {
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            sessionMap.put("busDate", period);
            session.setAttribute("userDate", sessionMap);

            Map<String, Object> map = new HashMap<>();
            map.put("useLastPeriod", period);
            map.put("accountID", account.getAccountID());
            // 记录每一次切换的期间写到t_basic_account 表
            int num = accountDao.chgAccByCondintion(map);

            Map<String, Object> pa = new HashMap<String, Object>();
            if (getAccount() != null && getAccount().getAccountID() != null) {
                pa.put("accountID", getAccount().getAccountID());
                pa.put("busDate", period);
                pa.put("period", period);
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
                    statusPeriod.setPeriod(period);
                    statusPeriod.setAccountID(getAccount().getAccountID());
                    periodStatusService.insertPeriodStatu(statusPeriod);
                }
                List<Progress> proList = progressDao.queryProgress(pa);
                if (proList == null || proList.isEmpty()) {
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
            }
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    // 超级管理员路由
    @RequestMapping("/superAdminView")
    public ModelAndView superAdminView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/superAdminView");
        return mav;
    }

    // 管理员路由
    @RequestMapping("/adminView")
    public ModelAndView adminView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/adminView");
        return mav;
    }

    // 修改密码页面
    @RequestMapping("/changePassword")
    public ModelAndView changePassword() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/changePassword");
        return mav;
    }

    @RequestMapping(value = "/downGuide")
    public void downJxExcel(HttpServletResponse response, String bankType) throws Exception {

        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "/" + "用户操作指南.pdf",
                "操作指南-" + DateUtil.getDays() + ".pdf");
    }


    // 描述
    // 此接口用于从商城购买云记账或者云代账的用户下单支付后同步到我们的做账系统
    @RequestMapping(value = "/synchronizeUserToAccoun", method = RequestMethod.POST)
    @ResponseBody
    // @RequestBody AccountOrder accountOrder
    Map<String, Object> synchronizeUserToAccoun(AccountOrder accountOrder) {

        try {
            Map<String, Object> map = loginService.synchronizeUserToAccoun(accountOrder);
            return map;
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("resultCode", "010");
            res.put("reason", e.getMessage().substring(0, 100));
            return res;
        }
    }

    // 用户修改密码
    @RequestMapping(value = "/savePassword")
    @ResponseBody
    Map<String, Object> savePassword(@RequestParam(required = true) String oldPassword,
                                     @RequestParam(required = true) String newPassword) {
        try {

            String loginUser = getUser().getLoginUser();
            Map<String, Object> map = loginService.savePassword(loginUser, oldPassword, newPassword);
            return map;
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "1");
            res.put("msg", "修改异常");
            return res;
        }
    }

    // 登录 检查用户身份
    @RequestMapping("/checkUserType")
    @ResponseBody
    Map<String, Object> checkUserType(@RequestParam(required = true) String loginUser) {
        try {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("loginUser", loginUser);
            List<User> list = userDao.queryUserByCondition(hashMap);
            if (list != null && list.size() > 1) {
                List<UserVo> arr = new ArrayList<>();
                for (User user : list) {
                    UserVo vo = new UserVo();
                    BeanUtils.copyProperties(vo, user);
                    arr.add(vo);
                }
                hashMap.put("user", arr);
                hashMap.put("code", "0");
                return hashMap;
            } else if (list != null && list.size() == 0) {
                List<UserVo> arr = new ArrayList<>();
                UserVo vo = new UserVo();
                BeanUtils.copyProperties(vo, list.get(0));
                arr.add(vo);
                hashMap.put("user", arr);
                hashMap.put("code", "1");
                return hashMap;
            } else {
                hashMap.put("user", null);
                hashMap.put("code", "2");
                return hashMap;
            }
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "3");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    /*****************************************************************************************************/
    /*****************************************************************************************************/

    // 描述
    // 此接口用于从商城以试用户的身份登录做账系统验证身份使用
    // 试用户 验证
    @RequestMapping("/testUserToCheckPermission")
    @ResponseBody
    Map<String, Object> testUserToCheckPermission(@RequestParam(required = true) String phone,
                                                  @RequestParam(required = true) String userId) {
        try {

            Map<String, Object> map = loginService.testUserCheckPermission(phone, userId);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> res = new HashMap<>();
            res.put("resultCode", "4");
            res.put("reason", e.getMessage());
            res.put("success", false);
            return res;
        }
    }

    // 描述
    // 此接口用于从商城以试用户的身份登录做账系统
    // 试用户 登录
/*	@RequestMapping("/testToUserLoing3")
	@ResponseBody
	Map<String, Object> testToUserLoing3(@RequestParam(required = true) String phone,
			@RequestParam(required = true) String userId, HttpServletRequest request) {
		HttpSession session = null;
		try {
			session = request.getSession();
			String ip_address = getIp2(request);
			Map<String, Object> map = loginService.testUserLoing(phone, userId, session, ip_address);

			session.setAttribute("login_phone", phone);
			session.setAttribute("loing_ip", ip_address);
			session.setAttribute("credate", System.currentTimeMillis());// 登录时间
			session.setMaxInactiveInterval(60 * 60 * 4);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.invalidate();
			}
			Map<String, Object> res = new HashMap<>();
			res.put("resultCode", "4");
			String message2 = e.getMessage();
			res.put("reason", message2.substring(0, 200));
			res.put("success", false);
			return res;
		}
	}*/


    @RequestMapping("/testToUserLoing")
    public void testToUserLoing(@RequestParam(required = true, value = "phone") String loginUser, @RequestParam(required = true) String userId,
                                HttpServletResponse response, HttpServletRequest request) {

        HttpSession session = null;
        try {

            session = request.getSession();
            String ip_address = getIp2(request);
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("loginUser", loginUser);
            List<User> list = userDao.queryUserByCondition(hashMap);
            User user = null;
            if (list == null || list.isEmpty()) {
                //只有第一次免费使用自动注册
                user = new User();
                String userID = UUIDUtils.getUUID();
                user.setUserID(userID);
                user.setLoginUser(loginUser); // 手机号
                user.setUserType(6); // 记账员工
                user.setCompanyType("2"); //默认一般纳税人
                user.setState(1); // 1:启用
                user.setType(1); // 测试身份
                user.setParentUser(userID); // 父id默认自己
                user.setDes(loginUser + "试用户第一次登录财税系统注册账号");

                //设置初始密码
                String initPassword = StringUtil.generatePassword(8);
                user.setInitPassword(initPassword);
                //设置登录密码
                String md5 = Md5Utils.MD5(initPassword);
                user.setPasword(md5);

                // 设置生效日期
                Date ableDate = new Date();
                user.setAbleDate(ableDate);


                // 设置失效日期
                Calendar cd = Calendar.getInstance();
                cd.setTime(ableDate);
                cd.add(Calendar.DATE, 3); // 默认三天
                Date disableDate = cd.getTime();
                user.setDisableDate(disableDate);

                user.setUpdatePsn(userID);
                user.setCreateDate(new Date());
                user.setCreatePsn(userID);

                user.setId(null);

                user.setUpdateDate(new Date());
                user.setUserName("试用户" + loginUser);

                // 第一次登录添加试用户身份
                int addUser = userDao.addUser(user);
                System.out.println(addUser);

                String msg = "尊敬的微企宝试用户，您已经获得我们的在线云记账免费使用3天,登录初始密码为" + initPassword + ",请勿泄漏给他人并及时修改,如需帮助请拨打0755-29666320";
                WqbSmsSendRequest smsSingleRequest = new WqbSmsSendRequest(msg, loginUser);
                String requestJson = com.alibaba.fastjson.JSON.toJSONString(smsSingleRequest);
                // 开始发送短信啦
                String smsSingleRequestServerUrl = "http://vsms.253.com/msg/send/json";
                String wqb_response = WqbSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
                @SuppressWarnings("unused")
                WqbSmsSendResponse smsSingleResponse = com.alibaba.fastjson.JSON.parseObject(wqb_response, WqbSmsSendResponse.class);
            } else {
                // 已经注册过使用户身份
                user = list.get(0);
            }
            if (user == null) {
                throw new BusinessException("001 user is null");
            }

            Map<String, Object> param = new HashMap<String, Object>();

            String busDate = null;
            // 查询该用户的账套
            List<Account> accList = accountDao.queryByUserID(user.getUserID());
            if (accList != null && accList.size() > 0) {
                Account account = accList.get(0);
                param.put("account", account);
                // 最后使用期间
                if (account.getUseLastPeriod() != null) {
                    busDate = account.getUseLastPeriod();
                } else if (account.getLastTime() != null) {
                    // 最后使用时间
                    busDate = DateUtil.getMoth2(account.getLastTime());
                } else {
                    // 启用期间
                    busDate = DateUtil.getMoth2(account.getPeriod());
                }
            } else {
                param.put("account", null);

            }

            // 使用当前期间
            if (busDate == null) {
                busDate = DateUtil.getMonth();
            }

            param.put("user", user);
            param.put("busDate", busDate);
            param.put("perList", null); // 试用户没有权限
            param.put("isAdmin", 0);
            session.setAttribute("userDate", param);

            Account acc = (Account) param.get("account");
            UserLoingVo userVo = new UserLoingVo(user, acc, busDate, ip_address);
            new Thread(new loingThread(userVo)).start();


            session.setAttribute("login_phone", loginUser);
            session.setAttribute("loing_ip", ip_address);
            session.setAttribute("credate", System.currentTimeMillis());// 登录时间
            session.setMaxInactiveInterval(60 * 60 * 4);

            response.sendRedirect(request.getContextPath() + "/system/index");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.invalidate();
            }
            try {
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("用户不存在"));
                return;
            } catch (Exception e1) {
                try {
                    response.sendRedirect(request.getContextPath() + "/system/toLogin");
                } catch (IOException e2) {
                }
            }
            return;
        }
    }


    Map<String, Object> retMap(String code, String str) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("resultCode", code);
        res.put("reason", str);
        return res;
    }


    /*****************************************************************************************************/
    /*****************************************************************************************************/

    // 描述
    // 此接口用于从商城以试用户的身份登录做账系统验证身份使用
    // 正式用户 验证
    @RequestMapping("/userToCheckPermission")
    @ResponseBody
    Map<String, Object> userCheckPermission(@RequestParam(required = true) String phone,
                                            @RequestParam(required = true) String userId) {
        try {

            Map<String, Object> map = loginService.userToCheckPermission(phone, userId);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> res = new HashMap<>();
            res.put("resultCode", "4");
            res.put("reason", e.getMessage());
            res.put("success", false);
            return res;
        }
    }


    @SuppressWarnings("unused")
    @RequestMapping("/userToLogin")
    // 正式用户 登录
    // phone 手机号 , userId 用户id, userType 2 记账 3代账, companyType 公司类型 1小规模 2一般纳税人
    public void userToLogin(@RequestParam(required = true) String phone,
                            @RequestParam(required = true) String userId, @RequestParam(required = true) String userType,
                            String companyType, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> returnMap = null;
        HttpSession session = null;
        ModelAndView mv = new ModelAndView();
        User loing_user = null;
        try {
            session = request.getSession();
            Map<String, Object> param = new HashMap<>();
            param.put("loginUser", phone);
            List<User> list = userDao.queryUserByCondition(param);
            if (list == null || list.isEmpty()) {
                //mv.addObject("errors", "用户不存在");
                //mv.setViewName("system/loing");
                //String message = URLEncoder.encode("用户不存在", "GBK");
                //return "redirect:/system/toLogin?code=001&errors="+message;
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("用户不存在"));
                return;
            }

            // 一个用户每一种产品可以买一种类型
            // 此入口只能是管理员登录
            if (userType.equals("2")) {
                User user_type_2 = null;
                for (User user2 : list) {
                    if (user2 != null && user2.getUserType() != null) {
                        Integer userType2 = user2.getUserType();
                        if (userType2 == 2) {
                            user_type_2 = user2;
                            break;
                        }
                    }
                }
                if (user_type_2 == null) {
                    response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("检测到云代账不存在"));
                    return;
                }
                loing_user = user_type_2;

            } else if (userType.equals("3")) {
                if (StringUtil.isEmpty(companyType)) {
                    response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("companyType error"));
                    return;
                }

                User user_type_3 = null;
                for (User user3 : list) {
                    if (user3 != null && user3.getUserType() != null) {
                        Integer userType3 = user3.getUserType();
                        if (userType3 == 3 && user3.getCompanyType() != null) {
                            String companyType3 = user3.getCompanyType();
                            if (user3.getCompanyType().equals(companyType)) {
                                user_type_3 = user3;
                                break;
                            }
                        }
                    }
                }
                if (user_type_3 == null) {
                    //String message = Base64.getBase64("检测到云记账不存在");
                    response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("检测到云记账不存在"));
                    return;
                }
                loing_user = user_type_3;
            } else {
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("用户类型不存在"));
                return;
            }

            if (loing_user == null) {
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=006&errors=" + StringUtil.errEncode("用户错误"));
                return;
            }

            Map<String, Object> dataMap = new HashMap<String, Object>();

            String busDate = null;
            // 查询该用户的账套
            List<Account> accList = accountDao.queryByUserID(loing_user.getUserID());
            if (accList != null && accList.size() > 0) {
                Account account = accList.get(0);
                dataMap.put("account", account);
                // 最后使用期间
                if (account.getUseLastPeriod() != null) {
                    busDate = account.getUseLastPeriod();
                } else if (account.getLastTime() != null) {
                    // 最后使用时间
                    busDate = DateUtil.getMoth2(account.getLastTime());
                } else {
                    // 启用期间
                    busDate = DateUtil.getMoth2(account.getPeriod());
                }
            } else {
                dataMap.put("account", null);
            }

            // 使用当前期间
            if (busDate == null) {
                busDate = DateUtil.getMonth();
            }

            dataMap.put("user", loing_user);
            dataMap.put("busDate", busDate);
            // dataMap.put("perList", null); //管理员没有权限
            dataMap.put("isAdmin", 1);
            session.setAttribute("userDate", dataMap);

            String ip = getIp2(request);

            session.setAttribute("login_phone", loing_user.getLoginUser());
            session.setAttribute("loing_ip", ip);
            session.setAttribute("credate", System.currentTimeMillis());// 登录时间

            session.setMaxInactiveInterval(60 * 60 * 4);

            Account acc = (Account) dataMap.get("account");
            UserLoingVo userVo = new UserLoingVo(loing_user, acc, busDate, ip);
            new Thread(new loingThread(userVo)).start();
            response.sendRedirect(request.getContextPath() + "/system/index");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.invalidate();
            }
            String message2 = e.getMessage().substring(0, 100);
            try {
                response.sendRedirect(request.getContextPath() + "/system/toLogin?code=2&errors=" + message2);
            } catch (IOException e1) {
                try {
                    response.sendRedirect(request.getContextPath() + "/system/toLogin");
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return;
        }
    }


}
