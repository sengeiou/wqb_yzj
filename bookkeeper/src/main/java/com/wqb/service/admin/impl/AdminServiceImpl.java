package com.wqb.service.admin.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.DoubleUtils;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.admin.AdminDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.user.UserDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: AdminServiceImpl
 * @Description: 管理员首页
 * @date 2018年7月25日 上午9:36:23
 */
@Component
@Service("adminService")
public class AdminServiceImpl implements AdminService {
    private static Log4jLogger logger = Log4jLogger.getLogger(AdminServiceImpl.class);
    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PeriodStatusDao periodStatusDao;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: accountCount
     * @Description: 做帐统计
     * @date 2018年7月25日 上午9:30:27
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> accountCount(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        //		result = adminDao.accountCount(result);
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: corpOverview
     * @Description: 企业总览
     * @date 2018年7月25日 上午9:30:52
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> corpOverview(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            // 获取user信息
            User user = (User) sessionMap.get("user");
            // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
            if (user.getUserType() != 2 & user.getUserType() != 3) {
                result.put("msg", "这个不是管理员账号，没有权限进入此系统");
                return result;
            }
            // 用户id
            String userId = user.getUserID();
            // 帐套期间
            String busDate = (String) sessionMap.get("busDate");
            //一、总企业数（统计全部）
            //1.查询管理员下面的员工
            List<User> queryUserByParentID = userDao.queryUserByParentID(userId);
            //2.通过员工查询 所有帐套信息
            List<Account> queryAllAccByUserIdStr = new ArrayList<Account>();
            for (User user2 : queryUserByParentID) {
                String userID2 = user2.getUserID();
                queryAllAccByUserIdStr.addAll(accountDao.queryAllAccByUserIdStr(userID2));

            }
            //3.查询管理员下面的帐套
            List<Account> queryAllAccByUserIdStrParent = accountDao.queryAllAccByUserIdStr(userId);
            queryAllAccByUserIdStr.addAll(queryAllAccByUserIdStrParent);

            // 全部帐套
            int allAcc = 1;
            if (queryAllAccByUserIdStr.size() > 0) {
                allAcc = queryAllAccByUserIdStr.size();
            }
            result.put("allAcc", allAcc);
            logger.info("全部帐套 : " + allAcc);
            //4.
            //二、累计停用的企业数（判断是否禁用）
            int countStatu2 = 0;
            //三、本月新增的企业
            int countAdded = 0;
            //五、本月停用的企业
            double countStatuPeriod2 = 0;
            //四、本月记账完成的企业
            double queryJz = 0;

            for (Account account2 : queryAllAccByUserIdStr) {
                //   `statu` int(11) DEFAULT NULL COMMENT '账套状态（0:新生成1:启用2:禁用）',
                //   `ssType` int(11) DEFAULT NULL COMMENT '一般纳税人（传0）   小规模（传1）',
                //   `initial_states` int(2) DEFAULT '0' COMMENT '初始化状态(0没有初始化，1已经初始化)',
                if (account2.getStatu() == 2) {
                    //二、累计停用的企业数（判断是否禁用）
                    countStatu2++;
                }
                Date period = account2.getPeriod();
                String moth2 = DateUtil.getMoth2(period);
                if (moth2 != null && moth2.equals(busDate)) {
                    //三、本月新增的企业（） 占比
                    countAdded++;

                    if (account2.getStatu() == 2) {
                        //五、本月停用的企业（）占比
                        countStatuPeriod2++;
                    }

                    // 账套状态（0:新生成1:启用2:禁用）
                    if (account2.getStatu() != 0) {
                        //四、本月记账完成的企业（）占比
                        String accountID2 = account2.getAccountID();
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("accountID", accountID2);
                        param.put("period", busDate);
                        queryJz += periodStatusDao.queryJz(param);
                    }
                }
            }

            //二、累计停用的企业数（判断是否禁用）
            //			int countStatu2 = 0;
            logger.info("累计停用的企业数（判断是否禁用） : " + countStatu2);
            result.put("countStatu2", countStatu2);
            //三、本月新增的企业（） 占比
            //			int countAdded = 0;
            logger.info("三、本月新增的企业: " + countAdded);
            result.put("countAdded", countAdded);
            double addedProportion = DoubleUtils.formatDouble((double) countAdded / allAcc);
            double countAddedRate = DoubleUtils.formatDouble(addedProportion * 100);
            result.put("countAddedRate", DoubleUtils.getTwoString(countAddedRate));
            logger.info("三、本月新增的企业--占比 : " + DoubleUtils.getTwoString(countAddedRate));
            //五、本月停用的企业（）占比
            //			int countStatuPeriod2 = 0;
            logger.info("五、本月停用的企业: " + countStatuPeriod2);
            result.put("countStatuPeriod2", countStatuPeriod2);
            if (countStatu2 == 0) {
                countStatu2 = 1;
            }
//			double statuPeriodProportion = (double) countStatuPeriod2 / countStatu2;
            double statuPeriodProportion = (double) countStatuPeriod2 / allAcc;
            double countStatuPeriod2Rate = DoubleUtils.formatDouble(statuPeriodProportion * 100);
            result.put("countStatuPeriod2Rate", DoubleUtils.getTwoString(countStatuPeriod2Rate));
            logger.info("五、本月停用的企业--占比: " + DoubleUtils.getTwoString(countStatuPeriod2Rate));
            //四、本月记账完成的企业（）占比
            //			int queryJz = 0;
            logger.info("四、本月记账完成的企业: " + queryJz);
            result.put("queryJz", queryJz);

            double countStatuProportion = (double) queryJz / countStatu2;
            double queryJzRate = DoubleUtils.formatDouble(countStatuProportion * 100);
            logger.info("四、本月记账完成的企业--占比 : " + DoubleUtils.getTwoString(queryJzRate));
            result.put("queryJzRate", DoubleUtils.getTwoString(queryJzRate));
            result.put("code", 1);
            //三、本月新增的企业（） 占比

            //四、本月记账完成的企业（）占比

            //五、本月停用的企业（）占比
            //			result = adminDao.corpOverview(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: acctList
     * @Description: 做帐列表
     * @date 2018年7月25日 上午9:30:46
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> acctList(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        //		result = adminDao.acctList(result);
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: staffCorpScale
     * @Description: 各财务人员负责企业比例图
     * @date 2018年7月25日 上午9:30:38
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> staffCorpScale(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            // 获取user信息
            User user = (User) sessionMap.get("user");
            // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
            if (user.getUserType() != 2 & user.getUserType() != 3) {
                result.put("msg", "这个不是管理员账号，没有权限进入此系统");
                return result;
            }
            // 用户id
            String userId = user.getUserID();
            String loginUser = user.getLoginUser();
            //一、总企业数（统计全部）
            //1.查询管理员下面的员工
            List<User> queryUserByParentID = userDao.queryUserByParentID(userId);
            //2.通过员工查询 所有帐套信息
            List<Account> queryAllAccByUserIdStr = new ArrayList<Account>();
            for (User user2 : queryUserByParentID) {
                String userID2 = user2.getUserID();
                queryAllAccByUserIdStr.addAll(accountDao.queryAllAccByUserIdStr(userID2));

            }
            //3.查询管理员下面的帐套
            List<Account> queryAllAccByUserIdStrParent = accountDao.queryAllAccByUserIdStr(userId);
            queryAllAccByUserIdStr.addAll(queryAllAccByUserIdStrParent);

            // 全部帐套
            int allAcc = 1;
            if (queryAllAccByUserIdStr.size() > 0) {
                allAcc = queryAllAccByUserIdStr.size();
            }
            result.put("allAcc", allAcc);
            logger.info("全部帐套 : " + allAcc);

            List<Map<Object, Object>> staffCorpList = new ArrayList<Map<Object, Object>>();
            if (queryUserByParentID != null && queryUserByParentID.size() <= 10) {
                Map<Object, Object> map1 = new HashMap<Object, Object>();
                for (User user2 : queryUserByParentID) {
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    String userID2 = user2.getUserID();
                    String loginUser2 = user2.getLoginUser();
                    List<Account> queryAllAccByUserIdStr2 = accountDao.queryAllAccByUserIdStr(userID2);
                    map.put("user", loginUser2);
                    map.put("rate",
                            queryAllAccByUserIdStr2.size());
//					DoubleUtils.formatDouble((double) queryAllAccByUserIdStr2.size()));
                    staffCorpList.add(map);
                    logger.info("员工：" + map);
                }
                map1.put("user", loginUser);
                map1.put("rate",
                        queryAllAccByUserIdStrParent.size());
                staffCorpList.add(map1);
                logger.info("管理员：" + map1);
            } else {
                Map<Object, Object> map1 = new LinkedHashMap<Object, Object>();
                for (User user2 : queryUserByParentID) {
                    Map<Object, Object> map = new LinkedHashMap<Object, Object>();
                    String userID2 = user2.getUserID();
                    String loginUser2 = user2.getLoginUser();
                    List<Account> queryAllAccByUserIdStr2 = accountDao.queryAllAccByUserIdStr(userID2);
                    map.put("user", loginUser2);
                    map.put("rate",
                            queryAllAccByUserIdStr2.size());
                    staffCorpList.add(map);
                    logger.info("员工：" + map);
                }
                map1.put("user", loginUser);
                map1.put("rate",
                        queryAllAccByUserIdStrParent.size());
                staffCorpList.add(map1);
                logger.info("管理员：" + map1);
            }
            //[{rate=0.0, user=18111110000}, {rate=11.0, user=15155762666}, {rate=20.0, user=15155760666}]

            List<Map<Object, Object>> comparatorList = comparatorList(staffCorpList);
            Collections.reverse(comparatorList);
            result.put("staffCorpList", comparatorList);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        //		result = adminDao.staffCorpScale(result);
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: staffCorpScaleNew
     * @Description: 各财务人员负责企业比例图(SQL查询)
     * @date 2018年8月1日  下午4:27:45
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> staffCorpScaleNew(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            // 获取user信息
            User user = (User) sessionMap.get("user");
            // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
            if (user.getUserType() != 2 & user.getUserType() != 3) {
                result.put("msg", "这个不是管理员账号，没有权限进入此系统");
                return result;
            }
            // 用户id
            String userId = user.getUserID();
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            Map<String, Object> staffCorpScale = adminDao.staffCorpScale(param);
            result.putAll(staffCorpScale);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return result;
    }

    public static List<Map<Object, Object>> comparatorList(List<Map<Object, Object>> list) {
        Collections.sort(list, new Comparator<Map<Object, Object>>() {
            public int compare(Map<Object, Object> o1, Map<Object, Object> o2) {
                int map1value = (Integer) o1.get("rate");
                int map2value = (Integer) o2.get("rate");
                /**
                 * 只有在value的值是int是用下面写法
                 * 如果value是其他类型的用compareTo比较
                 */
                return map1value - map2value;
                //return String.valueOf(map1value).compareTo(String.valueOf(map2value));
            }
        });
        System.out.println("排序后" + list);
        return list;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: effectiveCorpTrend
     * @Description: 有效企业趋势图
     * @date 2018年7月25日 上午9:30:33
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> effectiveCorpTrend(HttpSession session) throws BusinessException {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        Map<String, Map<String, Object>> reMap = new LinkedHashMap<>();
        List<Object> arrayList = new ArrayList<>();
        result.put("code", -1);
        try {

            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            // 获取user信息
            User user = (User) sessionMap.get("user");
            // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
            if (user.getUserType() != 2 & user.getUserType() != 3) {
                result.put("msg", "这个不是管理员账号，没有权限进入此系统");
                return result;
            }
            // 用户id
            String userId = user.getUserID();
            // 帐套期间
            String busDate = (String) sessionMap.get("busDate");
            //一、总企业数（统计全部）
            //1.查询管理员下面的员工
            List<User> queryUserByParentID = userDao.queryUserByParentID(userId);
            //2.通过员工查询 所有帐套信息
            Map<String, String> hashMap = new HashMap<String, String>();

            Map<String, String> getmonth2 = getmonth2(busDate);

            for (String periodStr : getmonth2.keySet()) {
                Map<String, Object> map = reMap.get(periodStr);
                if (map == null) {
                    Map<String, Object> map2 = new LinkedHashMap<>();
                    int collect = 0;
                    hashMap.put("busDate", periodStr);
                    for (User user2 : queryUserByParentID) {
                        String userID2 = user2.getUserID();
                        hashMap.put("userID", userID2);

                        collect += accountDao.queryAllAccByUserIdYear(hashMap).size();

                    }
                    //3.查询管理员下面的帐套
                    hashMap.put("userID", userId);
                    List<Account> queryAllAccByUserIdStrParent = accountDao.queryAllAccByUserIdYear(hashMap);
                    collect += queryAllAccByUserIdStrParent.size();

                    map2.put("collect", collect);
                    map2.put("period", periodStr);
                    arrayList.add(map2);
                }
            }
            Collections.reverse(arrayList);
            logger.info("全部帐套 : " + arrayList);
            result.put("code", 1);
            result.put("arrayList", arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return result;
    }


    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: effectiveCorpTrendNew
     * @Description: 有效企业趋势图(SQL查询)
     * @date 2018年8月1日  下午4:26:59
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> effectiveCorpTrendNew(HttpSession session) throws BusinessException {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        List<Object> arrayList = new ArrayList<>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            // 获取user信息
            User user = (User) sessionMap.get("user");
            // 用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）
            if (user.getUserType() != 2 & user.getUserType() != 3) {
                result.put("msg", "这个不是管理员账号，没有权限进入此系统");
                return result;
            }
            // 用户id
            String userId = user.getUserID();
            // 帐套期间
            String busDate = (String) sessionMap.get("busDate");
            //一、总企业数（统计全部）
            Map<String, String> getmonth2 = getmonth2(busDate);
            for (String periodStr : getmonth2.keySet()) {
                Map<String, Object> param = new HashMap<String, Object>();
                Map<String, Object> map2 = new LinkedHashMap<>();
                param.put("userId", userId);
                param.put("busDate", periodStr);
                logger.info("有效企业趋势图参数：", param);
                Map<String, Object> effectiveCorpTrend = adminDao.effectiveCorpTrend(param);
                map2.putAll(effectiveCorpTrend);
                map2.put("period", periodStr);
                arrayList.add(map2);
            }
            // 反转list
            Collections.reverse(arrayList);
            result.put("code", 1);
            result.put("arrayList", arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return result;
    }

    // 获取上个月
    public Map<String, String> getmonth2(String date) {  //选择期间
        try {
            Map<String, String> hashMap = new LinkedHashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 1; i > -11; i--) {
                Date time = sdf.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.MONTH, i);// 月份减一
                Date dateTemp = cal.getTime();
                String month = sdf.format(dateTemp);
                hashMap.put(month, month);
            }
            return hashMap;
        } catch (Exception e) {
            return null;
        }
    }

}
