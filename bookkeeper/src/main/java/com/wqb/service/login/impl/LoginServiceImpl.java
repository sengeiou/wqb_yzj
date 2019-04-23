package com.wqb.service.login.impl;


import com.alibaba.fastjson.JSONObject;
import com.wqb.common.*;
import com.wqb.common.sendSms.WqbSmsSendRequest;
import com.wqb.common.sendSms.WqbSmsSendResponse;
import com.wqb.common.sendSms.WqbSmsUtil;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.permission.dao.PermissionDao;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.userOrder.UserOrderDao;
import com.wqb.httpClient.HttpClientUtil;
import com.wqb.model.*;
import com.wqb.model.vomodel.UserLoingVo;
import com.wqb.service.common.loingThread;
import com.wqb.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Component
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Value("${wqb_url}")
    private String url;
    @Value("${wqb_charset}")
    private String charset;
    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    UserOrderDao userOrderDao;
    private HttpClientUtil httpClientUtil = new HttpClientUtil();
    private static Log4jLogger logger = Log4jLogger.getLogger(LoginServiceImpl.class);


    @Override
    public Map<String, Object> login(String loginUser, String password) throws BusinessException {
        try {

            Map<String, Object> result = new HashMap<String, Object>();
            String md5_password = Md5Utils.MD5(password);
            Map<String, Object> param = new HashMap<>();
            param.put("loginUser", loginUser);
            param.put("password", md5_password);
            // 查询该用户是否存在
            List<User> list_user = userDao.queryUserByCondition(param);
            if (null == list_user || list_user.isEmpty()) {
                result.put("msg", "用户名或者密码错误");
                result.put("code", "1");
                return result;
            }
            User user = list_user.get(0);
            Integer state = user.getState();
            // 判断是否被禁用
            if (state == null || state != 1) {
                result.put("msg", "您的账号已被禁用,请联系管理员");
                result.put("code", "2");
                return result;
            }

            int userType = user.getUserType();
            String userID = user.getUserID();

            Map<String, Object> map = new HashMap<>();
            map.put("lastTime", 1); // 排序
            map.put("statu", 1); // 启用
            map.put("limitLogin", "1"); // 取一条
            if (userType == 2 || userType == 3) {
                map.put("source", userID);
            } else {
                map.put("userID", userID);
            }

            Map<String, Object> ret = new HashMap<>(); // 返回参数

            // 根据时间排序查询用户最后使用的账套
            List<Account> accList = accountDao.queryAccByCondition(map);
            if (null != accList && accList.size() > 0) {
                ret.put("account", accList.get(0));
            } else {
                ret.put("account", null);
            }
            user.setPasword("");
            user.setInitPassword("");
            ret.put("user", user);
            ret.put("code", "0");
            return ret;
        } catch (Exception e) {
            logger.error("LoginServiceImpl【login】用户登录异常!", e);
            e.printStackTrace();
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("code", "0");
            hashMap.put("msg", "登录失败");
            return hashMap;
        }
    }

    @Override
    public Map<String, Object> login1(String userName, String password, HttpSession session) throws BusinessException {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            String httpOrgCreateTest = url + "Customer/Login";
            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("LoginName", userName);
            createMap.put("Password", password);
            String httpOrgCreateTestRtn = null;
            String Id = null;
            String Token = null;
            try {
                httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap, charset);
                Map<?, ?> maps = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn);
                Map<?, ?> map1 = (Map<?, ?>) maps.get("data");
                Id = map1.get("Id").toString();
                Token = map1.get("Token").toString();
            } catch (Exception e) {
                throw new BusinessException("调用商城登录接口异常!");
            }

            User user = userDao.queryUserByLineID(Id);
            if (user != null) {
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("userID", user.getUserID());
                p.put("sessionID", session.getId());
                userDao.updSessionIDbyID(p);
            }
            if (null == user) {
                result.put("message", "用户名或者密码错误或者没有开启");
                return result;
            }
            int statu = user.getState();
            if (statu == 2) {
                result.put("message", "该用户已被禁用,登录失败!");
                return result;
            }
            int userType = user.getUserType();
            if (userType == 5) {
                User user1 = userDao.queryUserById(user.getParentUser());
                Id = user1.getId();
            }

            // 调用商城接口获取当前管理员名下余额
            String url1 = url + "/Customer/GetMyAssets";
            Map<String, String> createMap1 = new HashMap<String, String>();
            createMap1.put("customerId", Id);
            createMap1.put("dataType", "json");
            double ableAmount = 0;
            try {
                String httpOrgCreateTestRtn1 = httpClientUtil.doPost(url1, createMap1, charset);
                Map<?, ?> maps1 = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn1);
                Map<?, ?> map2 = (Map<?, ?>) maps1.get("data");
                String ableAmountStr = map2.get("Balance").toString();
                ableAmount = Double.parseDouble(ableAmountStr);
            } catch (Exception e) {
                throw new BusinessException("调用商城获取账户余额接口异常!");
            }
            /*
             * if (ableAmount == 0 || ableAmount < 0) { result.put("message",
             * "您账户余额不足,请联系管理员充值!"); return result; }
             */
            // int userType = user.getUserType();
            String userID = user.getUserID();

            List<Permission> perList = permissionDao.queryPreByUserID(userID);
            Map<String, Object> map = new HashMap<>();
            map.put("lastTime", 1); // 排序
            map.put("statu", 1); // 启用
            if (userType == 2 || userType == 3) {
                map.put("source", userID);
            } else {
                map.put("userID", userID);
            }
            // limitLogin
            map.put("limitLogin", 1);
            List<Account> accList = accountDao.queryAccByCondition(map);
            if (null != accList && accList.size() > 0) {
                result.put("account", accList.get(0));
            } else {
                result.put("account", null);
            }
            result.put("perList", perList);
            result.put("user", user);
            result.put("Token", Token);
            return result;

        } catch (Exception e) {
            logger.error("LoginServiceImpl【login】用户登录异常!", e);
            return null;
        }
    }

    // 商城同步用户数据接口
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> synchronizeUserToAccoun(AccountOrder accountOrder) throws BusinessException {
        try {
            // 接收参数
            // 1 获取数据后 第一步检查数据是否存在
            String sourceCode = accountOrder.getSourceCode(); // 平台id
            if (StringUtil.isEmpty(sourceCode)) {
                return retMap("0101", "缺少平台id");
            }
            String order_userid = accountOrder.getUserid(); // 用户id
            if (StringUtil.isEmpty(order_userid)) {
                return retMap("0102", "缺少用户id");
            }
            String mobile = accountOrder.getMobile(); // 手机号
            if (StringUtil.isEmpty(mobile)) {
                return retMap("0103", "缺少手机号");
            }
            String password = accountOrder.getPassword(); // 密码 明文
            if (StringUtil.isEmpty(password)) {
                return retMap("0104", "缺少密码");
            }
            String email = accountOrder.getEmail(); // 邮箱
            String orderNumber = accountOrder.getOrderNumber();// 订单编号
            if (StringUtil.isEmpty(orderNumber)) {
                return retMap("0105", "缺少订单编号");
            }
            String productJ = accountOrder.getProductJ(); // 用户类型 云记账1、云代账2
            if (StringUtil.isEmpty(productJ)) {
                return retMap("0106", "缺少用户类型");
            }
            String productType = accountOrder.getProductType(); // 购买类型 购买1、续费2
            if (StringUtil.isEmpty(productType)) {
                return retMap("0107", "缺少购买类型");
            }
            String ssType = accountOrder.getSsType(); // 产品类型 小规模1、一般纳税人2 智能云代账3
            // 注 云记账（1）分小规模和一般纳税人
            if (productJ.equals("1") && StringUtil.isEmpty(ssType)) {
                return retMap("010", "云记账缺少产品类型 ");
            }
            String successTime = accountOrder.getSuccessTime(); // 下单时间
            if (StringUtil.isEmpty(successTime)) {
                return retMap("0108", "缺少下单时间");
            }
            String month = accountOrder.getMonth(); // 时间周期 月份
            if (StringUtil.isEmpty(month)) {
                return retMap("0109", "缺少时间周期");
            }
            Integer account = accountOrder.getAccount();// 账套数  云记账=1,云代账以实际商品为准（例如500家）
            // 云代账购买要有账套数 比如100 500 ， 云代账续费可以不需要账套数但是要知道购买时间
            if (productJ.equals("2") && productType.equals("1") && account == null) {
                return retMap("0111", "云代账缺少账套数");
            }
            Double sprice = accountOrder.getSprice(); // 金额 以订单金额为准

            String productsn = accountOrder.getProductsn(); // 产品编号
            if (StringUtil.isEmpty(productsn)) {
                return retMap("0112", "缺少产品编号");
            }
            // 2 根据购买类型判断 是购买 还是续费
            Map<String, Object> map1 = new HashMap<>();
            map1.put("loginUser", mobile);
            List<User> listUser = userDao.queryUserByCondition(map1);

            String account_type = Integer.valueOf(productJ) == 1 ? "云记账" : "云代账";
            String user_id = null; // 定义做账系统用户id
            String local_time = DateUtil.getTime();
            // productType 1购买 2续费
            if (productType.equals("1")) {
                boolean aa = false;
                if (listUser != null && listUser.size() == 1) {
                    User user = listUser.get(0);
                    Integer type = user.getType();
                    if (type == 1) {
                        aa = true;
                    }
                }
                // aa == true 为试用户购买
                if (aa == true) {
                    // 如果是试用户类型购买了记账或者代账需要变更用户信息
                    // 试用户购买服务变为正式用户 需要把它底下的所有账套来源source更改为 购买服务的用户ID
                    User user1 = new User();
                    user1.setType(0); // 更改为正式用户

                    // 设置 生效日期
                    Date ableDate = DateUtil.fomatToDate1(successTime);
                    //Long val = Long.valueOf(successTime); // 订单支付时间
                    //Date ableDate = new Date(val); // ableDate 生效日期
                    user1.setAbleDate(ableDate);

                    // 设置失效日期
                    Date disableDate = getDisableDate(month, ableDate);
                    user1.setDisableDate(disableDate);

                    user1.setState(1); // 设置开启
                    user1.setUserName(account_type + "管理员");
                    user1.setParentUser("0"); // 设置上级

                    if (productJ.equals("2")) {
                        user1.setUserType(2); // 试用户购买云代账 更改为代账
                        user1.setAccountNum(account); // 设置云代账购买的账套数
                        user1.setCompanyType("");
                        user1.setDes("试用户转正代账成功" + local_time);
                        if (account == null) {
                            throw new BusinessException("0113试用户同步失败 account is null");
                        }
                    } else if (productJ.equals("1")) {
                        // 试用户购买云记账小规模 或者 一般纳税人
                        user1.setUserType(3); // 更改为记账
                        user1.setAccountNum(0);
                        user1.setCompanyType(ssType); // 小规模1、一般纳税人2
                        user1.setDes("试用户转正记账成功" + local_time);
                        if (ssType == null) {
                            throw new BusinessException("0114试用户同步失败 ssType is null");
                        }
                    }
                    String userID2 = listUser.get(0).getUserID();
                    if (StringUtil.isEmpty(userID2)) {
                        throw new BusinessException("0201 试用户uid为空");
                    }

                    String userID = listUser.get(0).getUserID();
                    user_id = userID;
                    user1.setUserID(userID);
                    int numUp = userDao.upUserByUid(user1);
                    if (numUp == 0) {
                        throw new BusinessException("0115试用户购买同步到数据库失败");
                    }

                } else {
                    // 正式用户第一次购买
                    if (listUser == null || listUser.isEmpty()) {
                        User user = new User();
                        String userID = UUIDUtils.getUUID();
                        user_id = userID;
                        user.setUserID(userID);
                        user.setLoginUser(mobile);
                        // productJ  云记账1、云代账2
                        if (productJ.equals("2")) {
                            user.setUserType(2); //设置为代账
                            user.setAccountNum(account); // 账套数
                            user.setCompanyType("");
                            user.setDes("购买代账成功" + local_time);
                            if (account == null) {
                                throw new BusinessException("0116 productJ 2 account is null");
                            }
                        } else if (productJ.equals("1")) {
                            user.setUserType(3);//设置为记账
                            user.setAccountNum(0); // 账套数  云记账=1,云代账以实际商品为准（例如500家）
                            user.setCompanyType(ssType);
                            user.setDes("购买记账成功" + local_time);
                            if (ssType == null) {
                                throw new BusinessException("0117 productJ 1 ssType is null");
                            }
                        }
                        user.setType(0); // 0:正式用户 1：试用用户
                        user.setState(1); // 账号状态（0:新建1:启用、2:禁用）
                        user.setParentUser("0");
                        String initPassword = StringUtil.generatePassword(8);

                        // 设置密码
                        user.setInitPassword(initPassword);// 设置初始密码
                        String md5 = Md5Utils.MD5(initPassword);
                        user.setPasword(md5); // 设置新的密码 对初始化密码进行加密

                        // 设置 生效日期
                        Date ableDate = DateUtil.fomatToDate1(successTime);
                        user.setAbleDate(ableDate);

                        // 设置失效日期
                        Date disableDate = getDisableDate(month, ableDate);
                        user.setDisableDate(disableDate);

                        //String ableDate_formt = DateUtil.getTime(ableDate);
                        //String disableDate_formt = DateUtil.getTime(disableDate);

                        user.setUpdatePsn(userID);
                        user.setCreatePsn(userID);
                        user.setCreateDate(new Date());

                        user.setId(sourceCode); // 平台用户ID
                        user.setUserName(account_type + "管理员");
                        user.setEmail(email);
                        user.setUpdateDate(new Date());

                        int insert = userDao.addUser(user);
                        if (insert == 0) {
                            throw new BusinessException("0118 购买同步用户到数据库失败");
                        }

                        String msg = "尊敬的微企宝用户，您的" + account_type + "初始密码为" + initPassword
                                + ",请勿泄漏给他人并及时修改,如需帮助请拨打0755-29666320";
                        WqbSmsSendRequest smsSingleRequest = new WqbSmsSendRequest(msg, mobile);
                        String requestJson = com.alibaba.fastjson.JSON.toJSONString(smsSingleRequest);
                        // 发送短信
                        String smsSingleRequestServerUrl = "http://vsms.253.com/msg/send/json";
                        String response = WqbSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
                        @SuppressWarnings("unused")
                        WqbSmsSendResponse smsSingleResponse = com.alibaba.fastjson.JSON.parseObject(response,
                                WqbSmsSendResponse.class);

                    } else {
                        // 或者已经够买过其它的云服务的
                        if (productJ.equals("2")) {
                            for (User user2 : listUser) {
                                if (user2.getUserType() == 2) {
                                    return retMap("014", "0119 已购买过云代账,一个账号不能同时购买云代账");
                                }
                            }
                        } else if (productJ.equals("1")) {
                            for (User user2 : listUser) {
                                if (user2.getUserType() == 3) {
                                    return retMap("018", "0120 已购买过云记账,一个账号不能同时购买云记账");
                                }
                            }
                        }
                        // 再次购买新的云服务 创建新的用户
                        User user = new User();
                        String userID = UUIDUtils.getUUID();
                        user_id = userID;
                        user.setUserID(userID);
                        user.setLoginUser(mobile);
                        user.setType(0); // 0:正式用户 1：试用用户
                        user.setState(1); // 账号状态（0:新建1:启用、2:禁用）
                        user.setParentUser("0");

                        // productJ 用户类型 云记账1、云代账2
                        if (productJ.equals("2")) {
                            user.setUserType(2);
                            user.setAccountNum(account); // 账套数
                            user.setCompanyType("");
                            user.setDes("购买代账成功" + local_time);
                        } else if (productJ.equals("1")) {
                            user.setUserType(3);
                            user.setAccountNum(0); // 账套数
                            user.setCompanyType(ssType);
                            user.setDes("购买记账成功" + local_time);
                        }

                        // 设置密码
                        String pasword = listUser.get(0).getPasword();
                        user.setInitPassword(null);
                        user.setPasword(pasword); // 当前用户已经存在使用存在的密码

                        // 设置 生效日期
                        Date ableDate = DateUtil.fomatToDate1(successTime);
                        user.setAbleDate(ableDate);

                        // 设置失效日期
                        Date disableDate = getDisableDate(month, ableDate);
                        user.setDisableDate(disableDate);

                        user.setUpdatePsn(userID);
                        user.setCreatePsn(userID);
                        user.setCreateDate(new Date());

                        user.setId(sourceCode); // 平台ID
                        user.setUserName(account_type + "管理员");
                        user.setEmail(email);
                        user.setUpdateDate(new Date());
                        int insert = userDao.addUser(user);
                        if (insert == 0) {
                            throw new BusinessException("0121 购买同步用户到数据库失败");
                        }
                    }
                }

            } else if (productType.equals("2")) {
                // 2续费
                if (listUser == null || listUser.isEmpty()) {
                    return retMap("013", "续费用户不存在");
                }

                Date new_disableDate = null;
                // productJ==2代账用户续费
                if (productJ.equals("2")) {
                    for (User user4 : listUser) {
                        Integer userType = user4.getUserType();
                        if (userType == 2) {
                            Date disableDate = user4.getDisableDate(); // 原来的失效时间
                            // disableDate.before(new Date()) 判断续费之前是否已经过期
                            // 如果过期的话计算失效时间从下单开始算起，否则在原来的失效时间上累计
                            if (disableDate.before(new Date())) {
                                disableDate = DateUtil.fomatToDate1(successTime);
                                //Long val = Long.valueOf(successTime); // 订单支付时间
                                //disableDate = new Date(val);
                            }
                            new_disableDate = getDisableDate(month, disableDate); // 设置新的失效时间
                            user_id = user4.getUserID();
                            break;
                        }
                    }
                } else if (productJ.equals("1")) {
                    // productJ==1记账用户续费
                    for (User user5 : listUser) {
                        Integer userType = user5.getUserType();
                        String companyType = user5.getCompanyType();
                        if (userType == 3 && companyType != null && companyType.equals(ssType)) {
                            Date disableDate = user5.getDisableDate(); // 原来的失效时间
                            // 判断续费之前是否已经过期 如果过期的话计算失效时间从下单开始算起，否则在原来的失效时间上累计
                            if (disableDate.before(new Date())) {
                                disableDate = DateUtil.fomatToDate1(successTime);
                                //Long val = Long.valueOf(successTime); // 订单支付时间
                                //disableDate = new Date(val);
                            }
                            new_disableDate = getDisableDate(month, disableDate); // 设置新的失效时间
                            user_id = user5.getUserID();
                            break;
                        }
                    }
                }

                User up_user = new User();
                up_user.setUserID(user_id);
                up_user.setState(1);
                up_user.setDisableDate(new_disableDate);
                String time = DateUtil.getTime();
                up_user.setDes("续费成功" + local_time);
                if (new_disableDate == null || user_id == null) {
                    return retMap("015", "0123 续费异常");
                }
                int upUser = userDao.upUserByUid(up_user);
                if (upUser == 0) {
                    throw new BusinessException("0124 续费更新失败");
                }
            } else {
                // 其它错误
                return retMap("011", "购买类型错误");
            }
            // 3把订单数据插入到数据库

            String des1 = Integer.valueOf(productType) == 1 ? "购买" : "续费";
            String des2 = Integer.valueOf(productJ) == 1 ? (des1 + "记账") : (des1 + "代账");

            UserOrder userOrder = new UserOrder();

            String uuid = UUIDUtils.getUUID();
            userOrder.setId(uuid); // 主键
            userOrder.setUser_id(user_id); // 外键
            userOrder.setPlatformId(sourceCode); // 平台id
            userOrder.setUserid(order_userid);// 来自平台用户id
            userOrder.setOrderNumber(orderNumber);// 订单编号
            userOrder.setProduceNumber(productsn);// 产品编号
            userOrder.setUserType(productJ);// 用户类型 1云记账 2云代账

            userOrder.setBuyType(productType);// 购买类型 1购买 2续费
            userOrder.setCompanyType(ssType); // 产品类型 =公司类型 1小规模、2一般纳税人 3智能云代账
            userOrder.setMobile(mobile);// 手机号
            userOrder.setPassword(password); // 密码
            userOrder.setSuccessTime(successTime); // 下单时间
            userOrder.setAccountNum(account);// 账套数
            userOrder.setMonth(month); // 时间周期 统一用月份
            userOrder.setPrice(sprice); // 金额 以订单金额为准
            userOrder.setEmail(email);
            userOrder.setSynchronizeTime(new Date()); // 同步时间

            userOrder.setDes(des2 + "成功");// 描述
            int insert = userOrderDao.insertUserOrder(userOrder);
            if (insert == 0) {
                throw new BusinessException("0125 同步订单到数据库失败");
            }

            // 4返回结果
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("resultCode", "001");
            res.put("reason", des2 + "同步成功");
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private Date getDisableDate(String month, Date ableDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ableDate);
        calendar.add(Calendar.MONTH, Integer.valueOf(month));
        Date disableDate = calendar.getTime(); // 失效日期
        return disableDate;
    }

    Map<String, Object> retMap(String code, String str) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("resultCode", code);
        res.put("reason", str);
        return res;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> savePassword(String loginUser, String oldPassword, String newPassword)
            throws BusinessException {
        Map<String, Object> res = new HashMap<>();
        try {
            String md5 = Md5Utils.MD5(oldPassword);
            User user = userDao.login(loginUser, md5);
            if (user == null) {
                res.put("code", "1");
                res.put("msg", "原密码错误");
                return res;
            }
            String userID = user.getUserID();
            Map<String, Object> param = new HashMap<>();
            String newPass = Md5Utils.MD5(newPassword);
            param.put("pasword", newPass);
            param.put("userID", userID);
            param.put("loginUser", loginUser);
            int num = userDao.updUserByLoginUser(param);
            if (num < 1) {
                throw new BusinessException("修改失败");
            }
            res.put("code", "0");
            res.put("msg", "修改成功");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    // 试用户才有检查失效期
    public Map<String, Object> testUserCheckPermission(String loginUser, String userId) throws BusinessException {

        Map<String, Object> res = new HashMap<>();

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("loginUser", loginUser);
        List<User> list = userDao.queryUserByCondition(hashMap);

        //1  试用户 第一次进来为空  做账系统自动注册  放行
        //2  试用户 第二次进来  已经存在 判断有没有失效
        if (list != null && list.size() > 0) {

            if (list.size() > 1) {
                res.put("resultCode", "3");
                res.put("reason", ("非法进入"));
                res.put("success", false);
                return res;
            }

            User user = list.get(0);
            if (user != null) {
                // type 0:正式用户 1：试用用户
                int type = user.getType();
                if (type == 0) {
                    res.put("resultCode", "1");
                    res.put("reason", ("正式用户请到我的服务登录"));
                    res.put("success", false);
                    return res;
                }

                // state 账号状态 0:新建1:启用、2:禁用
                int state = user.getState();
                if (state != 1) {
                    res.put("resultCode", "2");
                    res.put("reason", "您的身份已被禁用,请联系管理员");
                    res.put("success", false);
                    return res;
                }

                // 不是正式身份登录
                // 计算失效时间是否过期
                Date disableDate = user.getDisableDate();
                Date date = new Date();
                if (disableDate.before(date)) {
                    res.put("resultCode", "3");
                    res.put("reason", "已过失效期,请续费");
                    res.put("success", false);
                    return res;
                }
            }
        }

        res.clear();
        res.put("resultCode", "0");
        res.put("reason", "验证通过");
        res.put("success", true);
        return res;
    }

    @Override
    // mallUserId 平台用户id
    // loginUser 请求手机号
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> testUserLoing(String loginUser, String mallUserId, HttpSession session,
                                             String ip_address) throws BusinessException {
        // 不是记账用户 不是代账用户 也不是员工，标记为测试用户type设置为1， 使用期间3天，用户类型为为C端员工只能开一个账套
        Map<String, Object> res = new HashMap<>();
        try {

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("loginUser", loginUser);
            List<User> list = userDao.queryUserByCondition(hashMap);
            User user = null;
            if (list == null || list.isEmpty()) {
                user = new User();
                String userID = UUIDUtils.getUUID();
                user.setUserID(userID);
                user.setLoginUser(loginUser); // 手机号
                user.setUserType(6); // 记账员工
                user.setCompanyType("2"); //默认一般纳税人
                user.setState(1); // 1:启用
                user.setType(1); // 测试身份
                user.setParentUser(userID); // 父id默认自己
                user.setDes(loginUser + "试用户第一次登录财税系统自动注册账号");

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
                cd.add(Calendar.MONTH, 3); // 默认三个月
                Date disableDate = cd.getTime();
                user.setDisableDate(disableDate);

                user.setUpdatePsn(userID);
                user.setCreateDate(new Date());
                user.setCreatePsn(userID);

                user.setId(null);

                user.setUpdateDate(new Date());

                // 第一次登录添加试用户身份
                int addUser = userDao.addUser(user);
                System.out.println(addUser);

                String msg = "尊敬的微企宝用户，您的云记账初始密码为" + initPassword + ",请勿泄漏给他人并及时修改,如需帮助请拨打0755-29666320";
                WqbSmsSendRequest smsSingleRequest = new WqbSmsSendRequest(msg, loginUser);
                String requestJson = com.alibaba.fastjson.JSON.toJSONString(smsSingleRequest);
                // 开始发送短信啦
                String smsSingleRequestServerUrl = "http://vsms.253.com/msg/send/json";
                String response = WqbSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
                @SuppressWarnings("unused")
                WqbSmsSendResponse smsSingleResponse = com.alibaba.fastjson.JSON.parseObject(response, WqbSmsSendResponse.class);
                System.out.println(1);
            } else {
                // 已经注册过使用户身份
                user = list.get(0);
            }
            if (user == null) {
                throw new BusinessException("user null");
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

            res.clear();
            res.put("resultCode", "0");
            res.put("reason", "登录成功");
            res.put("success", true);
            return res;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }


    @Override
    public Map<String, Object> userToCheckPermission(String loginUser, String userId) throws BusinessException {
        Map<String, Object> res = new HashMap<>();

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("loginUser", loginUser);
        List<User> list = userDao.queryUserByCondition(hashMap);
        if (list != null && list.size() > 0) {
            User user = list.get(0);
            // type 0:正式用户 1：试用用户
            int type = user.getType();
            if (type != 0) {
                res.put("resultCode", "1");
                res.put("reason", ("身份错误" + type));
                res.put("success", false);
                return res;
            }

            // state 账号状态 0:新建1:启用、2:禁用
            int state = user.getState();
            if (state != 1) {
                res.put("resultCode", "2");
                res.put("reason", "您的身份已被禁用,请联系管理员");
                res.put("success", false);
                return res;
            }
        } else {
            res.put("resultCode", "3");
            res.put("reason", "用户不存在");
            res.put("success", false);
            return res;
        }

        res.clear();
        res.put("resultCode", "0");
        res.put("reason", "验证通过");
        res.put("success", true);
        return res;
    }

}
