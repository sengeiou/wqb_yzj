package com.wqb.controller.basic;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.permission.dao.PermissionDao;
import com.wqb.dao.user.UserDao;
import com.wqb.httpClient.HttpClientUtil;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.account.AccountService;
import com.wqb.service.app.AccountAppService;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/systemAPP")
public class LoginAPPController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(IntoSystemController.class);
    // 测试环境 http://api.wqbol.net/  正式环境  http://api.wqbol.com/
    @Value("${wqb_url}")
    private String url;
    @Value("${wqb_charset}")
    private String charset;

    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUrl() {
        return url;
    }

    public String getCharset() {
        return charset;
    }

    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    AccountService accountService;

    @Autowired
    PermissionDao permissionDao;
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    AccountAppService accountAppService;

    @RequestMapping("/loginAPP")
    @ResponseBody
    public Map<String, Object> loginAPP(@RequestParam(value = "token", required = true) String token) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (StringUtil.isEmpty(token)) {
            result.put("code", "-2");
            result.put("msg", "非法登录");
            return result;
        }

        System.out.println(url);

        String httpOrgCreateTest = url + "Customer/GetCustomerInfo";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("Token", token);
        try {
            String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap, charset);
            Map<?, ?> maps = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn);
            Map<?, ?> map = (Map<?, ?>) maps.get("data");
            String Id = map.get("Id").toString(); // 平台用户ID  52431500-8854-478c-bcb8-421b0f6bdda7
            String Mobile = map.get("Mobile").toString(); // 手机号 登陆帐号  15520000000  13000000004

            boolean JZFW = Boolean.parseBoolean(map.get("JZFW").toString()); // 是否有记账服务 C端 管理员 3   true
            boolean DZFW = Boolean.parseBoolean(map.get("DZFW").toString()); // 是否有代账服务  B端  管理员 2

            User user = userDao.queryUserByLineID(Id);  // 这个id是平台id
            if (JZFW || DZFW) {
                if (user == null) {
                    user = new User();
                    user.setUserID(UUIDUtils.getUUID()); // 这个用户表主键id   63d57f81f82c4cecbfa810e95b05d222  C端管理员账号
                    user.setLoginUser(Mobile); // 做账系统 与后台登录的用户名 登录用来检查
                    if (JZFW) {
                        user.setUserType(3);
                    } else if (DZFW) {
                        user.setUserType(2);
                    }
                    user.setState(1);
                    user.setAbleDate(new Date());
                    user.setDisableDate(new Date());
                    user.setDisableDate(new Date());
                    user.setUpdatePsn("11");
                    user.setCreateDate(new Date());
                    user.setCreatePsn("11");
                    user.setId(Id);
                    user.setPasword("E10ADC3949BA59ABBE56E057F20F883E"); // 设置默认密码  后台登录开通员工用  //E10ADC3949BA59ABBE56E057F20F883E  123456
                    user.setUpdateDate(new Date());
                    userDao.addUser(user);
                }
            }

            if (user == null) {
                result.put("code", "-1");
                result.put("msg", "用户不存在");
                return result;
            }
            /******* 用户信息保存 ********/

            // 获取用户类型
            // 2:后台普通管理员(B端)3:C端管理员 5:B端员工用户6:C端员工用户
            Integer userType = user.getUserType();
            String userID = user.getUserID();

            Map<String, Object> accMap = new HashMap<>();

            List<String> listIds = new ArrayList<>();
            List<Account> list = null;

            accMap.put("limit", 1);
            if (userType == 2 || userType == 3) {
                // 查询管理员下面的所有员工
                accMap.put("userID", user.getUserID());
                listIds = accountAppService.queryUserByUserID2(accMap);
                listIds.add(userID);  //添加管理员
            }

            // 这是员工类型
            if (userType == 5 || userType == 6) {
                listIds.add(user.getUserID());
            }
            if (listIds != null && listIds.size() > 0) {
                accMap.put("listID", listIds);
                list = accountAppService.queryAllByID2(accMap);
            }

            result.put("type", userType);
            result.put("phone", user.getLoginUser());
            result.put("uid", user.getUserID());
            result.put("code", "0");
            result.put("msg", "登录成功");

            if (list != null && list.size() > 0) {
                result.put("acc", "0");
                result.put("info", list.get(0)); // 返回给平台
            } else {
                result.put("acc", "-1");
                result.put("info", "没有可启用的账套");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "-1");
            result.put("msg", e.getMessage());
            return result;
        }
    }

    // 查询公司
    @RequestMapping("/queryComName")
    @ResponseBody
    public Map<String, Object> queryComName(
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "userID", required = true) String userID) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> accMap = new HashMap<>();
        try {

            accMap.put("userID", userID);
            // 获取用户类型
            User loginUser = accountAppService.queryAcc(accMap);
            if (loginUser == null) {
                result.put("code", "-1");
                result.put("msg", "用户不存在");
                return result;
            }
            Integer userType = loginUser.getUserType();

            List<Account> list = null;   //定义账套集合
            Map<String, Object> queryMap = new HashMap<>();
            List<String> ids = new ArrayList<>();


            int serverPort = getRequest().getServerPort();  //8090
            String serverName = getRequest().getServerName();
            if (!StringUtil.isEmpty(companyName)) {
                //本地需要开启编码乱码
                if (serverPort == 8090 || "localhost".equals(serverName)) {
                    companyName = new String(companyName.getBytes("ISO-8859-1"), "UTF-8");
                }
            }
            if (!StringUtil.isEmpty(companyName)) {
                queryMap.put("companyName", companyName);
            }


            if (userType == 3 || userType == 2) {
                ids = accountAppService.queryUserByUserID2(accMap);  //查询管理员下面的员工
                ids.add(userID);  //添加管理员
            }

            //员工
            if (userType == 5 || userType == 6) {
                ids.add(loginUser.getUserID());
            }

            if (ids != null && ids.size() > 0) {
                queryMap.put("listID", ids);
                list = accountAppService.queryAllByID(queryMap);
            }

            result.put("type", userType);
            result.put("phone", loginUser.getPhone());
            result.put("uid", loginUser.getUserID());

            if (list != null && list.size() > 0) {
                result.put("code", "0");
                result.put("msg", list);
                result.put("info", 0);
                return result;
            } else {
                result.put("code", "0");
                result.put("msg", null);
                result.put("info", -1);
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "-1");
            result.put("msg", "查询异常");
            return result;
        }

    }

    // 选择公司
    @RequestMapping("/choiceCompany")
    @ResponseBody
    public Map<String, Object> choiceCompany(@RequestParam(value = "accountID", required = true) String accountID) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> accMap = new HashMap<>();
            accMap.put("accountID", accountID);
            Account account = accountAppService.queryAccByID(accMap); // 查询用户账套信息
            if (account != null) {
                result.put("code", "0");
                result.put("info", "0");
                result.put("msg", account);

                String accID = account.getAccountID();
                Map<String, Object> upMap = new HashMap<>();
                upMap.put("accountID", accID);
                upMap.put("lastTime", new Date());
                //更新最近使用的账套登录时间
                accountService.chgAccLastTime(new Date(), accID);
                return result;
            } else {
                result.put("code", "0");
                result.put("info", "-1");
                result.put("msg", "未查询到账套");
                return result;
            }

        } catch (Exception e) {

            e.printStackTrace();
            result.put("code", "-1");
            result.put("msg", "查询公司异常");
            return result;
        }
    }


}
