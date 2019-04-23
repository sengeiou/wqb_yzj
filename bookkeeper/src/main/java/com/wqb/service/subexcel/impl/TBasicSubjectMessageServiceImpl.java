package com.wqb.service.subexcel.impl;

import com.wqb.common.*;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.model.*;
import com.wqb.service.KcCommodity.KcCommodityService;
import com.wqb.service.exchange.TBasicExchangeRateService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMessageServiceImpl
 * @Description: 系统科目业务接口实现类
 * @date 2017年12月20日 下午10:55:34
 */
// @Transactional
@Service("tBasicSubjectMessageService")
public class TBasicSubjectMessageServiceImpl implements TBasicSubjectMessageService {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMessageServiceImpl.class);

    @Autowired
    /** 汇率表 */
            TBasicExchangeRateService tBasicExchangeRateService;

    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    /** 状态表 */
            PeriodStatusDao periodStatusDao;

    @Autowired
    /** 状态表 */
            KcCommodityService commodityService;
    @Autowired
    JedisClient jedisClient;

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: addSubMessage
     * @Description: 添加系统科目
     * @date 2017年12月21日 上午9:27:36
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> addSubMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            logger.info("新增科目：", tBasicSubjectMessage);
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            // 做帐期间
            tBasicSubjectMessage.setAccountPeriod(busDate);

            String subCode = tBasicSubjectMessage.getSubCode();

            // 获取 科目代码首个数字
            String category = (String) subCode.subSequence(0, 1);
            // 设置科目类别
            tBasicSubjectMessage.setCategory(category);

            String pkSubId = UUIDUtils.getUUID();
            tBasicSubjectMessage.setPkSubId(pkSubId);

            tBasicSubjectMessage.setState("1");
            tBasicSubjectMessage.setSubSource("手动新增");
            tBasicSubjectMessage.getSubCode();

            List<TBasicSubjectMessage> querySubMessageBySubcode3 = querySubMessageBySubcode2(tBasicSubjectMessage);
            if (!querySubMessageBySubcode3.isEmpty()) {
                result.put("code", 1);
                result.put("num", 0);
                result.put("msg", "科目可能已经存在，请刷新页面后再新增！");
                return result;
            }

            if (subCode != null && "001".equals(subCode.subSequence(subCode.length() - 3, subCode.length()))) {
                TBasicSubjectMessage tBasicSubjectMessage2 = new TBasicSubjectMessage();
                String superiorCoding = tBasicSubjectMessage.getSuperiorCoding();
                tBasicSubjectMessage2.setAccountId(accountId);
                tBasicSubjectMessage2.setAccountPeriod(busDate);
                tBasicSubjectMessage2.setSubCode(superiorCoding);
                TBasicSubjectMessage querySubMessageBySubcode = new TBasicSubjectMessage();
                List<TBasicSubjectMessage> querySubMessageBySubcode2 = querySubMessageBySubcode2(tBasicSubjectMessage2);
                querySubMessageBySubcode = querySubMessageBySubcode2.get(0);

                tBasicSubjectMessage.setInitCreditBalance(querySubMessageBySubcode.getInitCreditBalance());
                tBasicSubjectMessage.setInitDebitBalance(querySubMessageBySubcode.getInitDebitBalance());
                tBasicSubjectMessage.setCurrentAmountCredit(querySubMessageBySubcode.getCurrentAmountCredit());
                tBasicSubjectMessage.setCurrentAmountDebit(querySubMessageBySubcode.getCurrentAmountDebit());
                tBasicSubjectMessage.setEndingBalanceCredit(querySubMessageBySubcode.getEndingBalanceCredit());
                tBasicSubjectMessage.setEndingBalanceDebit(querySubMessageBySubcode.getEndingBalanceDebit());
                tBasicSubjectMessage.setYearAmountCredit(querySubMessageBySubcode.getYearAmountCredit());
                tBasicSubjectMessage.setYearAmountDebit(querySubMessageBySubcode.getYearAmountDebit());
                int num = tBasicSubjectMessageMapper.addSubMessage(tBasicSubjectMessage);
                commodityService.updateKccommddityByAddSubMessage(tBasicSubjectMessage, "1");
                result.put("code", 1);
                result.put("num", num);
                result.put("msg", "新增 " + num + " 条科目成功！");

            } else {
                // 查询科目表中全部科目
                Map<String, Object> querySubMessage = querySubMessage(user, account);
                List<TBasicSubjectMessage> querySubMessageList = (List<TBasicSubjectMessage>) querySubMessage
                        .get("subMessage");
                int no = 1;
                for (TBasicSubjectMessage tBasicSubjectMessage2 : querySubMessageList) {
                    // 需要添加的科目父类代码
                    String superiorCoding = tBasicSubjectMessage.getSuperiorCoding();
                    // 数据库中的科目父类代码
                    String superiorCoding2 = tBasicSubjectMessage2.getSuperiorCoding();
                    // 数据库中的科目名称
                    String subName2 = tBasicSubjectMessage2.getSubName();
                    // 需要添加的科目名称
                    String subName = tBasicSubjectMessage.getSubName();

                    // 判断这个科目的下级是否已经存在了相同科目名称的科目
                    if (superiorCoding2 != null && superiorCoding2.equals(superiorCoding) && subName2 != null
                            && subName2.equals(subName)) {
                        no = ++no;
                    }
                }
                if (no > 1) {
                    result.put("code", -1);
                    result.put("msg", "科目名称已经存在,直接搜索科目名称即可！");
                    logger.info("msg", "科目名称已经存在,直接搜索科目名称即可！");
                } else {
                    int num = tBasicSubjectMessageMapper.addSubMessage(tBasicSubjectMessage);
                    commodityService.updateKccommddityByAddSubMessage(tBasicSubjectMessage, "2");
                    result.put("code", 1);
                    result.put("num", num);
                    result.put("msg", "新增 " + num + " 条科目成功！");
                    logger.info("msg", "新增 " + num + " 条科目成功！");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            logger.error(e.getMessage());
            throw new BusinessException(e);
        }

    }

    /**
     * @param tBasicSubjectMessage
     * @return
     * @throws BusinessException TBasicSubjectMessage 返回类型
     * @Title: querySubMessageBySubcode2
     * @Description: 根据科目编码、期间、帐套id查询科目信息
     * @date 2018年7月20日 下午3:49:53
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageBySubcode2(TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException {
        List<TBasicSubjectMessage> subMessageList = new ArrayList<TBasicSubjectMessage>();
        subMessageList = tBasicSubjectMessageMapper.querySubMessageBySubcode2(tBasicSubjectMessage);
        return subMessageList;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: addSubMessageList
     * @Description: 添加系统科目集合
     * @date 2017年12月21日 上午10:34:04
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> addSubMessageList(HttpSession session,
                                                 List<TBasicSubjectMessage> tBasicSubjectMessagelist) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");

            int num = tBasicSubjectMessageMapper.addSubMessageList(tBasicSubjectMessagelist);
            result.put("code", 1);
            result.put("num", num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubParent
     * @Description: 查询系统中该账套的全部科目
     * @date 2017年12月20日 下午10:43:36
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessage(User user, Account account) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = account.getUseLastPeriod();
            tBasicSubjectMessage.setAccountPeriod(busDate);
            // 查询一级科目真实金额，没有外币时查询币别为""空值的，有外币时查询本位币金额
            tBasicSubjectMessage.setCodeLevel(1);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessage(tBasicSubjectMessage);
            for (TBasicSubjectMessage tBasicSubjectMessages : querySubMessage) {
                // 去除 科目代码和科目名称为空的情况
                // querySubMessagetBasicSubjectMessages.get
            }
            result.put("subMessage", querySubMessage);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "fail");
        }
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageLevel
     * @Description: 指定查询科目级别科目编码名称不为空
     * @date 2018年1月31日 上午10:00:58
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageLevel(HttpSession session) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            tBasicSubjectMessage.setAccountPeriod(busDate);
            // tBasicSubjectMessage.setCodeLevel(1);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessageLevel(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "fail");
        }
        return querySubMessage;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageNullLevel
     * @Description: 指定查询科目级别科目编码名称不为空
     * @date 2018年1月31日 上午10:00:58
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectMessage> querySubMessageNullLevel(HttpSession session) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            tBasicSubjectMessage.setAccountPeriod(busDate);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessageLevel(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "fail");
        }
        return querySubMessage;
    }

    @Override
    public List<TBasicSubjectMessage> querySubMessageList(HttpSession session) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");

            // 帐套期间
            tBasicSubjectMessage.setAccountPeriod(busDate);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessage(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            result.put("message", "fail");
        }
        return querySubMessage;
    }

    /**
     * @param session
     * @param subCode
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageBySubCode
     * @Description: 根据科目编码查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessageBySubCode(HttpSession session, String subCode) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            tBasicSubjectMessage.setSubCode(subCode);
            String busDate = (String) sessionMap.get("busDate");
            // 帐套期间
            tBasicSubjectMessage.setAccountPeriod(busDate);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessageBySubCode(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySubMessageMaxSubCode
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月12日 下午1:58:07
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessageMaxSubCode(HttpSession session,
                                                         TBasicSubjectMessage tBasicSubjectMessage) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            String subMessageCode = tBasicSubjectMessageMapper.querySubMessageMaxSubCode(tBasicSubjectMessage);
            result.put("subMessageCode", subMessageCode);
        } catch (Exception e) {
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param session
     * @param subName
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageBySubName
     * @Description: 根据科目名称查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessageBySubName(HttpSession session, String subName) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessageBySubName(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @throws BusinessException
     * @Title: updateMessage
     * @Description: 更新系统中的科目
     * @date 2017年12月21日 上午10:28:30
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> updateMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException {
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            querySubMessage = tBasicSubjectMessageMapper.updateMessage(tBasicSubjectMessage);
            result.put("code", 1);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param pkSubId
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessageByPrimaryKey
     * @Description: 根据主键删除系统中的科目
     * @date 2017年12月21日 上午10:29:43
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> deleteMessageByPrimaryKey(HttpSession session, String pkSubId) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            tBasicSubjectMessage.setPkSubId(pkSubId);

            String busDate = (String) sessionMap.get("busDate");
            // 帐套期间
            tBasicSubjectMessage.setAccountPeriod(busDate);
            int no = tBasicSubjectMessageMapper.deleteMessageByPrimaryKey(tBasicSubjectMessage);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessage
     * @Description: 单条科目删除
     * @date 2018年1月31日 上午11:06:16
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> deleteMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            int no = tBasicSubjectMessageMapper.deleteMessage(tBasicSubjectMessage);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessageAll
     * @Description: 删除账套在系统中全部科目
     * @date 2017年12月21日 上午10:31:13
     * @author SiLiuDong 司氏旭东
     */
    @Override
    // @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> deleteMessageAll(HttpSession session) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);

            querySubMessage = tBasicSubjectMessageMapper.deleteMessageAll(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: deleteMessageByAcctperiod
     * @Description: 根据帐套id删除系统本期间中的科目
     * @date 2018年7月7日 下午6:07:05
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> deleteMessageByAcctperiod(HttpSession session) throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            // 帐套期间
            tBasicSubjectMessage.setAccountPeriod(busDate);
            querySubMessage = tBasicSubjectMessageMapper.deleteMessageByAcctperiod(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> addSubMessage(HttpSession session, List<TBasicSubjectParent> tBasicSubjectParent) {

        List<TBasicSubjectMessage> tBasicSubjectMessageList = new ArrayList<TBasicSubjectMessage>();
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicSubjectMessage> querySubMessage = null;
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");
            String userId = user.getUserID();// 用户id

            for (TBasicSubjectParent tBasicSubjectParent2 : tBasicSubjectParent) {
                TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();

                tBasicSubjectMessage.setAccountId(accountId);

                tBasicSubjectMessage.setAccountPeriod(busDate);

                tBasicSubjectMessage.setUserId(userId);

                String pkSubId = UUIDUtils.getUUID();
                tBasicSubjectMessage.setPkSubId(pkSubId);

                String accountingStandard = tBasicSubjectParent2.getAccountingStandard();
                tBasicSubjectMessage.setAccountId(accountId);

                String category = tBasicSubjectParent2.getCategory();
                tBasicSubjectMessage.setCategory(category);

                String debitCreditDirection = tBasicSubjectParent2.getDebitCreditDirection();
                tBasicSubjectMessage.setDebitCreditDirection(debitCreditDirection);

                String mender = tBasicSubjectParent2.getMender();
                tBasicSubjectMessage.setMeasureState(0);
                tBasicSubjectMessage.setExchangeRateState(0);
                tBasicSubjectMessage.setCodeLevel(1);
                tBasicSubjectMessage.setSubSource("导入匹配不成功,系统再次分配初始科目");
                tBasicSubjectMessage.setState("1");

                tBasicSubjectMessage.setUpdateDate(new Date());

                String subCode = tBasicSubjectParent2.getSubCode();
                tBasicSubjectMessage.setSubCode(subCode);

                String subName = tBasicSubjectParent2.getSubName();
                tBasicSubjectMessage.setSubName(subName);

                tBasicSubjectMessageList.add(tBasicSubjectMessage);
            }
            int num = tBasicSubjectMessageMapper.addSubMessageList(tBasicSubjectMessageList);
            result.put("num", num);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static StringBuffer NewCode(String a, String b, int t) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(a.substring(0, t));
        stringBuffer.append(b);
        stringBuffer.append(a.substring(t, a.length()));
        return stringBuffer;
    }

    /**
     * @param session
     * @param tBasicSubjectExcelList2
     * @throws BusinessException void 返回类型
     * @Title: addSubMessageExcel
     * @Description: 添加系统科目集合
     * @date 2017年12月27日 下午4:57:18
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public void addSubMessageExcel(HttpSession session, List<TBasicSubjectExcel> tBasicSubjectExcelList)
            throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            int no = 0;
            int subCodeLength = 0;
            ArrayList<Object> arrayList = new ArrayList<>();
            Set<Integer> set = new HashSet<Integer>();

            // 循环遍历 tBasicSubjectExcelList 集合 取出科目编码长度
            for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                String subCode = tBasicSubjectExcel.getSubCode();
                if (StringUtils.isNotBlank(subCode)) {
                    // 获取科目编码长度
                    subCodeLength = subCode.length();
                }

                // 把获取到的长度放到set中 去重
                set.add(subCodeLength);
            }

            // 把set转换成list
            arrayList.addAll(set);

            if (arrayList.size() > 1) {
                if ("4".equals(arrayList.get(0)) && "2".equals(arrayList.get(1))) {
                    for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                        String subCode = tBasicSubjectExcel.getSubCode();
                        if (StringUtils.isNotBlank(subCode)) {
                            // 1为原字符串，2为要插入的字符串，3为插入位置
                            int s = (int) arrayList.get(0);
                            NewCode(subCode, "0", s);
                            // 获取科目编码长度
                            subCodeLength = subCode.length();

                            subCode.substring(0, (int) arrayList.get(0));

                            // subCode.substring(0,4)+0+subCode(t+1,a.length());

                        }

                        // 把获取到的长度放到set中 去重
                        set.add(subCodeLength);
                    }

                }
            }

            // 循环遍历 list tBasicSubjectExcel 转换成 tBasicSubjectMessage
            for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                String pkSubId = UUIDUtils.getUUID();
                tBasicSubjectMessage.setPkSubId(pkSubId);
                tBasicSubjectExcel.getAccountId(); // 账套ID

                BigDecimal currentAmountCredit = tBasicSubjectExcel.getCurrentAmountCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getCurrentAmountCredit(); // 本期发生额(贷方)
                tBasicSubjectMessage.setCurrentAmountCredit(currentAmountCredit);

                BigDecimal currentAmountDebit = tBasicSubjectExcel.getCurrentAmountDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getCurrentAmountDebit(); // 本期发生额(借方)
                tBasicSubjectMessage.setCurrentAmountDebit(currentAmountDebit);

                BigDecimal endingBalanceCredit = tBasicSubjectExcel.getEndingBalanceCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getEndingBalanceCredit(); // 期末余额(贷方)
                tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredit);

                BigDecimal endingBalanceDebit = tBasicSubjectExcel.getEndingBalanceDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getEndingBalanceDebit(); // 期末余额(借方)
                tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebit);

                BigDecimal initCreditBalance = tBasicSubjectExcel.getInitCreditBalance()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getInitCreditBalance(); // 期初余额(贷方)
                tBasicSubjectMessage.setInitCreditBalance(initCreditBalance);

                BigDecimal initDebitBalance = tBasicSubjectExcel.getInitDebitBalance()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getInitDebitBalance(); // 期初余额(借方)
                tBasicSubjectMessage.setInitDebitBalance(initDebitBalance);

                String isMatching = tBasicSubjectExcel.getIsMatching(); // 是否已经匹配到系统科目(0否，1是)

                String isMultipleSiblings = tBasicSubjectExcel.getIsMultipleSiblings(); // 是否多个同级(0无，1有)
                tBasicSubjectMessage.setIsMultipleSiblings(isMultipleSiblings);

                String period = tBasicSubjectExcel.getPeriod(); // 期间
                tBasicSubjectMessage.setAccountPeriod("2017" + period);

                tBasicSubjectExcel.getPkSubExcelId(); // 主键

                String subCode = tBasicSubjectExcel.getSubCode(); // 科目代码
                String category = null;

                if (StringUtils.isNotBlank(subCode)) {
                    // 获取科目编码长度
                    int length = subCode.length();
                    // 判断这个科目代码 为几级
                    int indexOf = arrayList.indexOf(length);
                    indexOf = indexOf + 1;
                    System.out.println("这个科目为 " + indexOf + " 级科目");
                    if (length > 4) {
                        // 取出上级科目编码
                        String superiorCoding = subCode.substring(0,
                                (int) arrayList.get(arrayList.indexOf(length) - 1));
                        tBasicSubjectMessage.setSuperiorCoding(superiorCoding);
                    }

                    tBasicSubjectMessage.setSubCode(subCode);
                    // 获取 科目代码首个数字
                    category = (String) subCode.subSequence(0, 1);
                } else {
                    tBasicSubjectMessage.setSuperiorCoding("");
                    tBasicSubjectMessage.setSubCode("");
                }

                // String siblingsCoding =
                // tBasicSubjectExcel.getSiblingsCoding() == null ? "" :
                // tBasicSubjectExcel.getSiblingsCoding(); //同级编码
                // tBasicSubjectMessage.setSiblingsCoding(siblingsCoding);

                String siblingsCoding = tBasicSubjectExcel.getSiblingsCoding(); // 同级编码
                if (StringUtils.isNotBlank(siblingsCoding)) {
                    // 设置同级科目编码
                    tBasicSubjectMessage.setSiblingsCoding(siblingsCoding);

                    // 获取 科目代码首个数字
                    category = (String) siblingsCoding.subSequence(0, 1);
                }

                // '类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                tBasicSubjectMessage.setCategory(category);

                String subName = tBasicSubjectExcel.getSubName(); // 科目名称
                if (StringUtils.isNotBlank(subName)) {
                    tBasicSubjectMessage.setSubName(subName);
                } else {
                    tBasicSubjectMessage.setSubName("");
                }

                String fullName = "--科目完整名称--" + subName;
                tBasicSubjectMessage.setFullName(fullName);
                System.out.println(tBasicSubjectMessage);

                // 科目来源
                tBasicSubjectMessage.setSubSource("Excel自动导入");

                String typeOfCurrency = tBasicSubjectExcel.getTypeOfCurrency(); // 币别

                tBasicSubjectMessage.setTypeOfCurrency(typeOfCurrency);

                String typeOfCurrencyMessage = tBasicSubjectMessage.getTypeOfCurrency();
                if (typeOfCurrencyMessage.contains("RMB") || typeOfCurrency.contains("本位币")) {
                    tBasicSubjectMessage.setExchangeRateState(0);// 外币设置状态(0关闭，1开启)
                } else {
                    tBasicSubjectMessage.setExchangeRateState(1);// 外币设置状态
                    // 开启(0关闭，1开启)
                    // 调用
                    // 查询此用户有无此币别
                    // 没有则添加
                    tBasicExchangeRateService.queryExeRateByCuyAbbe(typeOfCurrency, session);
                }

                BigDecimal yearAmountCredit = tBasicSubjectExcel.getYearAmountCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getYearAmountCredit(); // 本年累计发生额(借方)
                tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);

                BigDecimal yearAmountDebit = tBasicSubjectExcel.getYearAmountDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getYearAmountDebit(); // 本年累计发生额(贷方)
                tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);

                no += tBasicSubjectMessageMapper.addSubMessage(tBasicSubjectMessage);
            }
            result.put("no", no);
        } catch (Exception e) {
            result.put("message", "");
            e.printStackTrace();
        }
    }

    /**
     * @param session
     * @param tBasicSubjectExcelList2
     * @throws BusinessException void 返回类型
     * @Title: addSubMessageExcelList
     * @Description: 添加系统科目List集合 把TBasicSubjectExcel转换成TBasicSubjectMessage
     * @date 2018年1月5日 下午8:36:33
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public void addSubMessageExcelList(HttpSession session, List<TBasicSubjectExcel> tBasicSubjectExcelList,
                                       Map<String, String> ysMap, Map<String, TBasicSubjectParent> code2Info, boolean isSmart)
            throws BusinessException {

        /*
         * if(ysMap!=null && code2Info!=null){ if(null!=tBasicSubjectExcelList
         * && tBasicSubjectExcelList.size()>0){ List<TBasicSubjectExcel>
         * tBasicSubjectExcelListTemp = new ArrayList<TBasicSubjectExcel>();
         * for(int s =0;s<tBasicSubjectExcelList.size();s++){ TBasicSubjectExcel
         * tExcel = tBasicSubjectExcelList.get(s); String excelSubCode =
         * tExcel.getSubCode(); if(excelSubCode.length()==4){
         * tExcel.setSubName(code2Info.get(ysMap.get(excelSubCode)).getSubName()
         * ); } tBasicSubjectExcelListTemp.add(tExcel); } tBasicSubjectExcelList
         * = tBasicSubjectExcelListTemp; } }
         */
        if (isSmart) {
            Map<String, String> ysMapTemp = new HashMap<String, String>();
            Iterator<Map.Entry<String, String>> entries = ysMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                ysMapTemp.put(entry.getValue(), entry.getKey());
            }
            ysMap = ysMapTemp;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicSubjectMessage> addSubMessage = new ArrayList<TBasicSubjectMessage>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            // 做帐日期
            String accountPeriod = (String) sessionMap.get("busDate");

            int no = 0;
            int subCodeLength = 0;
            ArrayList<Object> arrayList = new ArrayList<>();
            Set<Integer> set = new HashSet<Integer>();

            /** 取出所有科目的长度 开始 */
            // 循环遍历 tBasicSubjectExcelList 集合 取出科目编码长度
            for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                String subCode = tBasicSubjectExcel.getSubCode();
                if (StringUtils.isNotBlank(subCode)) {
                    // 获取科目编码长度
                    subCodeLength = subCode.trim().replaceAll(" ", "").length();
                }

                // 把获取到的长度放到set中 去重
                set.add(subCodeLength);
            }

            // 把set转换成list
            arrayList.addAll(set);
            /** 取出所有科目的长度 结束 */
            logger.info("EXCEL导入的科目级别", arrayList);

            // 一月份初始化科目时本年累计 应该为0
            String substring = accountPeriod.substring(accountPeriod.lastIndexOf("-") + 1);
            if (substring.equals("01")) {
                // 循环遍历 list tBasicSubjectExcel 转换成 tBasicSubjectMessage
                for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                    TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();

                    // 设置做帐时间
                    tBasicSubjectMessage.setAccountPeriod(accountPeriod);
                    // 设置用户ID
                    tBasicSubjectMessage.setUserId(userId);
                    // 设置帐套id
                    tBasicSubjectMessage.setAccountId(accountId);
                    // 设置科目主键
                    String pkSubId = UUIDUtils.getUUID();
                    tBasicSubjectMessage.setPkSubId(pkSubId);
                    // excel 导入的期间
                    String excelImportPeriod = tBasicSubjectExcel.getPeriod();
                    tBasicSubjectMessage.setExcelImportPeriod(excelImportPeriod);
                    // 设置启用状态
                    tBasicSubjectMessage.setState("1");

                    // 设置借贷方向（1.debit借方， 2.credit贷方）
                    String debitCreditDirection = tBasicSubjectExcel.getDebitCreditDirection();
                    tBasicSubjectMessage.setDebitCreditDirection(debitCreditDirection);

                    // 本期发生额(贷方)
                    // BigDecimal currentAmountCredit =
                    // tBasicSubjectExcel.getCurrentAmountCredit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getCurrentAmountCredit();
                    tBasicSubjectMessage.setCurrentAmountCredit(new BigDecimal("0.0"));

                    // 本期发生额(借方)
                    // BigDecimal currentAmountDebit =
                    // tBasicSubjectExcel.getCurrentAmountDebit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getCurrentAmountDebit();
                    tBasicSubjectMessage.setCurrentAmountDebit(new BigDecimal("0.0"));

                    // 期末余额(贷方)
                    BigDecimal endingBalanceCredit = tBasicSubjectExcel.getEndingBalanceCredit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getEndingBalanceCredit();
                    tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredit);

                    // 期末余额(借方)
                    BigDecimal endingBalanceDebit = tBasicSubjectExcel.getEndingBalanceDebit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getEndingBalanceDebit();
                    tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebit);

                    // 期初余额(贷方)
                    // BigDecimal initCreditBalance =
                    // tBasicSubjectExcel.getInitCreditBalance().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getInitCreditBalance();
                    tBasicSubjectMessage.setInitCreditBalance(endingBalanceCredit);

                    // 期初余额(借方)
                    // BigDecimal initDebitBalance =
                    // tBasicSubjectExcel.getInitDebitBalance().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getInitDebitBalance();
                    tBasicSubjectMessage.setInitDebitBalance(endingBalanceDebit);

                    // 是否已经匹配到系统科目(0否，1是)
                    String isMatching = tBasicSubjectExcel.getIsMatching();

                    // 是否多个同级(0无，1有)
                    String isMultipleSiblings = tBasicSubjectExcel.getIsMultipleSiblings();
                    tBasicSubjectMessage.setIsMultipleSiblings(isMultipleSiblings);

                    // 科目代码
                    String subCode = tBasicSubjectExcel.getSubCode();
                    // 类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                    String category = null;

                    if (StringUtils.isNotBlank(subCode)) {
                        // Excel 导入的科目代码
                        tBasicSubjectMessage.setExcelImportCode(subCode);

                        // 科目代码
                        tBasicSubjectMessage.setSubCode(subCode);

                        // 获取科目编码长度
                        int length = subCode.length();
                        // 判断这个科目代码 为几级
                        int indexOf = arrayList.indexOf(length);
                        indexOf = indexOf + 1;
                        tBasicSubjectMessage.setCodeLevel(indexOf);
                        System.out.println("这个科目为 " + indexOf + " 级科目");
                        if (length > 4) {
                            // 取出上级科目编码
                            String excelImportSuperiorCoding = subCode.substring(0,
                                    (int) arrayList.get(arrayList.indexOf(length) - 1));
                            // 添加到导入的上级科目编码
                            tBasicSubjectMessage.setExcelImportSuperiorCoding(excelImportSuperiorCoding);
                            // 添加上级科目编码
                            tBasicSubjectMessage.setSuperiorCoding(excelImportSuperiorCoding);
                        }

                        // tBasicSubjectMessage.setSubCode(subCode);
                        // 获取 科目代码首个数字
                        category = (String) subCode.subSequence(0, 1);
                    } else {
                        // 添加到导入的上级科目编码
                        tBasicSubjectMessage.setExcelImportSuperiorCoding("");

                        // 添加上级科目编码
                        tBasicSubjectMessage.setSuperiorCoding("");

                        // Excel 导入的科目代码
                        tBasicSubjectMessage.setExcelImportCode("");

                        // 科目代码
                        tBasicSubjectMessage.setSubCode("");
                    }

                    // String siblingsCoding =
                    // tBasicSubjectExcel.getSiblingsCoding() == null ? "" :
                    // tBasicSubjectExcel.getSiblingsCoding(); //同级编码
                    // tBasicSubjectMessage.setSiblingsCoding(siblingsCoding);

                    // 同级编码
                    String excelImportSiblingsCoding = tBasicSubjectExcel.getSiblingsCoding();

                    // 同级科目名称
                    String excelImportSiblingsSubName = tBasicSubjectExcel.getSiblingsSubName();
                    if (StringUtils.isNotBlank(excelImportSiblingsSubName)) {
                        tBasicSubjectMessage.setSiblingsSubName(excelImportSiblingsSubName);
                    }

                    if (StringUtils.isNotBlank(excelImportSiblingsCoding)) {
                        // 设置Excel导入的同级科目编码
                        tBasicSubjectMessage.setExcelImportSiblingsCoding(excelImportSiblingsCoding);
                        // 设置同级科目编码
                        tBasicSubjectMessage.setSiblingsCoding(excelImportSiblingsCoding);

                        // 获取科目编码长度
                        int length = excelImportSiblingsCoding.length();
                        // 判断这个科目代码 为几级
                        int indexOf = arrayList.indexOf(length);
                        indexOf = indexOf + 1;
                        tBasicSubjectMessage.setCodeLevel(indexOf);

                        // 获取 科目代码首个数字
                        category = (String) excelImportSiblingsCoding.subSequence(0, 1);
                    }

                    // '类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                    tBasicSubjectMessage.setCategory(category);

                    String subName = tBasicSubjectExcel.getSubName(); // 科目名称
                    if (StringUtils.isNotBlank(subName)) {
                        tBasicSubjectMessage.setSubName(subName);
                    } else {
                        tBasicSubjectMessage.setSubName("");
                    }

                    // String fullName ="--科目完整名称--" + subName;
                    // tBasicSubjectMessage.setFullName(fullName);
                    // System.out.println(tBasicSubjectMessage);

                    // 科目来源
                    tBasicSubjectMessage.setSubSource("Excel自动导入");

                    // 币别
                    String typeOfCurrency = tBasicSubjectExcel.getTypeOfCurrency();
                    tBasicSubjectMessage.setTypeOfCurrency(typeOfCurrency);

                    String typeOfCurrencyMessage = tBasicSubjectMessage.getTypeOfCurrency();
                    if (typeOfCurrencyMessage.contains("RMB") || typeOfCurrency.contains("本位币")) {
                        tBasicSubjectMessage.setExchangeRateState(0);// 外币设置状态(0关闭，1开启)
                    } else {
                        tBasicSubjectMessage.setExchangeRateState(1);// 外币设置状态
                        // 开启(0关闭，1开启)
                        // 调用
                        // 查询此用户有无此币别
                        // 没有则添加
                        tBasicExchangeRateService.queryExeRateByCuyAbbe(typeOfCurrency, session);
                    }

                    // 本年累计发生额(借方)
                    // BigDecimal yearAmountCredit =
                    // tBasicSubjectExcel.getYearAmountCredit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getYearAmountCredit();
                    tBasicSubjectMessage.setYearAmountCredit(new BigDecimal("0.0"));

                    // 更新时间
                    tBasicSubjectMessage.setUpdateDate(new Date());

                    // 更新时间戳

                    Date date = new Date();// 获得系统时间.
                    String timestamp = String.valueOf(date.getTime());
                    tBasicSubjectMessage.setUpdateTimestamp(timestamp);

                    // 本年累计发生额(贷方)
                    // BigDecimal yearAmountDebit =
                    // tBasicSubjectExcel.getYearAmountDebit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getYearAmountDebit();
                    tBasicSubjectMessage.setYearAmountDebit(new BigDecimal("0.0"));
                    addSubMessage.add(tBasicSubjectMessage);
                }
            } else {
                // 循环遍历 list tBasicSubjectExcel 转换成 tBasicSubjectMessage
                for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                    TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();

                    // 设置做帐时间
                    tBasicSubjectMessage.setAccountPeriod(accountPeriod);
                    // 设置用户ID
                    tBasicSubjectMessage.setUserId(userId);
                    // 设置帐套id
                    tBasicSubjectMessage.setAccountId(accountId);
                    // 设置科目主键
                    String pkSubId = UUIDUtils.getUUID();
                    tBasicSubjectMessage.setPkSubId(pkSubId);
                    // excel 导入的期间
                    String excelImportPeriod = tBasicSubjectExcel.getPeriod();
                    tBasicSubjectMessage.setExcelImportPeriod(excelImportPeriod);

                    // 设置启用状态
                    tBasicSubjectMessage.setState("1");

                    // 设置借贷方向（1.debit借方， 2.credit贷方）
                    String debitCreditDirection = tBasicSubjectExcel.getDebitCreditDirection();
                    tBasicSubjectMessage.setDebitCreditDirection(debitCreditDirection);

                    // 本期发生额(贷方)
                    // BigDecimal currentAmountCredit =
                    // tBasicSubjectExcel.getCurrentAmountCredit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getCurrentAmountCredit();
                    tBasicSubjectMessage.setCurrentAmountCredit(new BigDecimal("0.0"));

                    // 本期发生额(借方)
                    // BigDecimal currentAmountDebit =
                    // tBasicSubjectExcel.getCurrentAmountDebit().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getCurrentAmountDebit();
                    tBasicSubjectMessage.setCurrentAmountDebit(new BigDecimal("0.0"));

                    // 期末余额(贷方)
                    BigDecimal endingBalanceCredit = tBasicSubjectExcel.getEndingBalanceCredit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getEndingBalanceCredit();
                    tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredit);

                    // 期末余额(借方)
                    BigDecimal endingBalanceDebit = tBasicSubjectExcel.getEndingBalanceDebit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getEndingBalanceDebit();
                    tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebit);

                    // 期初余额(贷方)
                    // BigDecimal initCreditBalance =
                    // tBasicSubjectExcel.getInitCreditBalance().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getInitCreditBalance();
                    tBasicSubjectMessage.setInitCreditBalance(endingBalanceCredit);

                    // 期初余额(借方)
                    // BigDecimal initDebitBalance =
                    // tBasicSubjectExcel.getInitDebitBalance().compareTo(new
                    // BigDecimal(0E-8)) == 0
                    // ? new BigDecimal(0) :
                    // tBasicSubjectExcel.getInitDebitBalance();
                    tBasicSubjectMessage.setInitDebitBalance(endingBalanceDebit);

                    // 是否已经匹配到系统科目(0否，1是)
                    String isMatching = tBasicSubjectExcel.getIsMatching();

                    // 是否多个同级(0无，1有)
                    String isMultipleSiblings = tBasicSubjectExcel.getIsMultipleSiblings();
                    tBasicSubjectMessage.setIsMultipleSiblings(isMultipleSiblings);

                    // 科目代码
                    String subCode = tBasicSubjectExcel.getSubCode();
                    // 类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                    String category = null;

                    if (StringUtils.isNotBlank(subCode)) {
                        // Excel 导入的科目代码
                        if (isSmart) {
                            String subCodeTemp = ysMap.get(subCode.substring(0, 4)) + subCode.substring(4);
                            tBasicSubjectMessage.setExcelImportCode(subCodeTemp);
                        } else {
                            tBasicSubjectMessage.setExcelImportCode(subCode);
                        }

                        // 科目代码
                        tBasicSubjectMessage.setSubCode(subCode);

                        // 获取科目编码长度
                        int length = subCode.length();
                        // 判断这个科目代码 为几级
                        int indexOf = arrayList.indexOf(length);
                        indexOf = indexOf + 1;
                        tBasicSubjectMessage.setCodeLevel(indexOf);
                        System.out.println("这个科目为 " + indexOf + " 级科目");
                        if (length > 4) {
                            // 取出上级科目编码
                            String excelImportSuperiorCoding = subCode.substring(0,
                                    (int) arrayList.get(arrayList.indexOf(length) - 1));
                            // 添加到导入的上级科目编码
                            tBasicSubjectMessage.setExcelImportSuperiorCoding(excelImportSuperiorCoding);
                            // 添加上级科目编码
                            tBasicSubjectMessage.setSuperiorCoding(excelImportSuperiorCoding);
                        }

                        // tBasicSubjectMessage.setSubCode(subCode);
                        // 获取 科目代码首个数字
                        category = (String) subCode.subSequence(0, 1);
                    } else {
                        // 添加到导入的上级科目编码
                        tBasicSubjectMessage.setExcelImportSuperiorCoding("");

                        // 添加上级科目编码
                        tBasicSubjectMessage.setSuperiorCoding("");

                        // Excel 导入的科目代码
                        tBasicSubjectMessage.setExcelImportCode("");

                        // 科目代码
                        tBasicSubjectMessage.setSubCode("");
                    }

                    // String siblingsCoding =
                    // tBasicSubjectExcel.getSiblingsCoding() == null ? "" :
                    // tBasicSubjectExcel.getSiblingsCoding(); //同级编码
                    // tBasicSubjectMessage.setSiblingsCoding(siblingsCoding);

                    // 同级编码
                    String excelImportSiblingsCoding = tBasicSubjectExcel.getSiblingsCoding();

                    // 同级科目名称
                    String excelImportSiblingsSubName = tBasicSubjectExcel.getSiblingsSubName();
                    if (StringUtils.isNotBlank(excelImportSiblingsSubName)) {
                        tBasicSubjectMessage.setSiblingsSubName(excelImportSiblingsSubName);
                    }

                    if (StringUtils.isNotBlank(excelImportSiblingsCoding)) {
                        // 设置Excel导入的同级科目编码
                        tBasicSubjectMessage.setExcelImportSiblingsCoding(excelImportSiblingsCoding);
                        // 设置同级科目编码
                        tBasicSubjectMessage.setSiblingsCoding(excelImportSiblingsCoding);

                        // 获取科目编码长度
                        int length = excelImportSiblingsCoding.length();
                        // 判断这个科目代码 为几级
                        int indexOf = arrayList.indexOf(length);
                        indexOf = indexOf + 1;
                        tBasicSubjectMessage.setCodeLevel(indexOf);

                        // 获取 科目代码首个数字
                        category = (String) excelImportSiblingsCoding.subSequence(0, 1);
                    }

                    // '类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                    tBasicSubjectMessage.setCategory(category);

                    String subName = tBasicSubjectExcel.getSubName(); // 科目名称
                    if (StringUtils.isNotBlank(subName)) {
                        tBasicSubjectMessage.setSubName(subName);
                    } else {
                        tBasicSubjectMessage.setSubName("");
                    }

                    // String fullName ="--科目完整名称--" + subName;
                    // tBasicSubjectMessage.setFullName(fullName);
                    // System.out.println(tBasicSubjectMessage);

                    // 科目来源
                    tBasicSubjectMessage.setSubSource("Excel自动导入");

                    // 币别
                    String typeOfCurrency = tBasicSubjectExcel.getTypeOfCurrency();
                    tBasicSubjectMessage.setTypeOfCurrency(typeOfCurrency);

                    String typeOfCurrencyMessage = tBasicSubjectMessage.getTypeOfCurrency();
                    if (typeOfCurrencyMessage.contains("RMB") || typeOfCurrency.contains("本位币")) {
                        tBasicSubjectMessage.setExchangeRateState(0);// 外币设置状态(0关闭，1开启)
                    } else {
                        tBasicSubjectMessage.setExchangeRateState(1);// 外币设置状态
                        // 开启(0关闭，1开启)
                        // 调用
                        // 查询此用户有无此币别
                        // 没有则添加
                        tBasicExchangeRateService.queryExeRateByCuyAbbe(typeOfCurrency, session);
                    }

                    // 本年累计发生额(借方)
                    BigDecimal yearAmountCredit = tBasicSubjectExcel.getYearAmountCredit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getYearAmountCredit();
                    tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);

                    // 更新时间
                    tBasicSubjectMessage.setUpdateDate(new Date());

                    // 更新时间戳

                    Date date = new Date();// 获得系统时间.
                    String timestamp = String.valueOf(date.getTime());
                    tBasicSubjectMessage.setUpdateTimestamp(timestamp);

                    // 本年累计发生额(贷方)
                    BigDecimal yearAmountDebit = tBasicSubjectExcel.getYearAmountDebit()
                            .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                            : tBasicSubjectExcel.getYearAmountDebit();
                    tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);
                    addSubMessage.add(tBasicSubjectMessage);
                }
            }

            // 把科目编码更改为4-3-3
            NewSubMessageCode newSubMessageCode = new NewSubMessageCode();
            List<TBasicSubjectMessage> newSubMessageCodeList = newSubMessageCode.newSubMessageCodeList(addSubMessage);

            List<TBasicSubjectMessage> newSuperiorCoding = new ArrayList<TBasicSubjectMessage>();
            List<Object> arrayList2 = new ArrayList<>();
            Set<Integer> set2 = new HashSet<Integer>();
            for (TBasicSubjectMessage tBasicSubjectMessage2 : newSubMessageCodeList) {
                /** 取出所有科目的长度 开始 */
                // 循环遍历 tBasicSubjectExcelList 集合 取出科目编码长度
                String subCode = tBasicSubjectMessage2.getSubCode();
                if (StringUtils.isNotBlank(subCode)) {
                    // 获取科目编码长度
                    subCodeLength = subCode.length();
                }
                // 把获取到的长度放到set中 去重
                set2.add(subCodeLength);
            }
            arrayList2.add(set2);

            // 获取上级科目代码
            for (TBasicSubjectMessage tBasicSubjectMessage2 : newSubMessageCodeList) {
                String subCode = tBasicSubjectMessage2.getSubCode();
                if (StringUtils.isNotBlank(subCode)) {

                    // 获取科目编码长度
                    int length = subCode.length();
                    // 判断这个科目代码 为几级
                    int indexOf = arrayList2.indexOf(length);
                    indexOf = indexOf + 1;
                    System.out.println("这个科目为 " + indexOf + " 级科目");
                    if (length > 4) {
                        // 取出上级科目编码
                        String superiorCoding = subCode.substring(0, length - 3);
                        // 添加上级科目编码
                        tBasicSubjectMessage2.setSuperiorCoding(superiorCoding);
                    }
                    // tBasicSubjectMessage.setSubCode(subCode);
                    // 获取 科目代码首个数字
                    newSuperiorCoding.add(tBasicSubjectMessage2);
                }
            }

            // 添加科目全名
            SubMessageFullName subMessageFullName = new SubMessageFullName();
            List<TBasicSubjectMessage> newSubMessageFullName = subMessageFullName
                    .newSubMessageFullName(newSuperiorCoding);

            tBasicSubjectMessageMapper.addSubMessageList(newSubMessageFullName);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", e);
        }
    }

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @param tBasicSubjectExcelList
     * @return Map<String, Object> 返回类型
     * @Title: toKmdzAddSubMessageList
     * @Description: 科目对照匹配不到手动添加
     * @date 2018年1月10日 上午10:59:43
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> toKmdzAddSubMessageList(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage,
                                                       List<TBasicSubjectExcel> tBasicSubjectExcelList) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id

            List<TBasicSubjectMessage> tBasicSubjectMessageList = new ArrayList<TBasicSubjectMessage>();

            for (TBasicSubjectExcel tBasicSubjectExcel : tBasicSubjectExcelList) {
                TBasicSubjectMessage tBasicSubjectMessageNew = new TBasicSubjectMessage();
                tBasicSubjectMessageNew.setUserId(userId);
                // 账套ID
                tBasicSubjectMessageNew.setAccountId(accountId);

                String pkSubId = UUIDUtils.getUUID();
                tBasicSubjectMessageNew.setPkSubId(pkSubId);

                // 本期发生额(贷方)
                BigDecimal currentAmountCredit = tBasicSubjectExcel.getCurrentAmountCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getCurrentAmountCredit();
                tBasicSubjectMessageNew.setCurrentAmountCredit(currentAmountCredit);

                // 本期发生额(借方)
                BigDecimal currentAmountDebit = tBasicSubjectExcel.getCurrentAmountDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getCurrentAmountDebit();
                tBasicSubjectMessageNew.setCurrentAmountDebit(currentAmountDebit);

                // 期末余额(贷方)
                BigDecimal endingBalanceCredit = tBasicSubjectExcel.getEndingBalanceCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getEndingBalanceCredit();
                tBasicSubjectMessageNew.setEndingBalanceCredit(endingBalanceCredit);

                // 期末余额(借方)
                BigDecimal endingBalanceDebit = tBasicSubjectExcel.getEndingBalanceDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getEndingBalanceDebit();
                tBasicSubjectMessageNew.setEndingBalanceDebit(endingBalanceDebit);

                // 期初余额(贷方)
                BigDecimal initCreditBalance = tBasicSubjectExcel.getInitCreditBalance()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getInitCreditBalance();
                tBasicSubjectMessageNew.setInitCreditBalance(initCreditBalance);

                // 期初余额(借方)
                BigDecimal initDebitBalance = tBasicSubjectExcel.getInitDebitBalance()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getInitDebitBalance();
                tBasicSubjectMessageNew.setInitDebitBalance(initDebitBalance);

                // 是否已经匹配到系统科目(0否，1是)
                String isMatching = tBasicSubjectExcel.getIsMatching();

                // 是否多个同级(0无，1有)
                String isMultipleSiblings = tBasicSubjectExcel.getIsMultipleSiblings();
                tBasicSubjectMessageNew.setIsMultipleSiblings(isMultipleSiblings);

                // 期间
                String period = tBasicSubjectExcel.getPeriod();
                tBasicSubjectMessageNew.setAccountPeriod("2017" + period);

                tBasicSubjectMessageNew.setAccountPeriod("");

                // 科目代码
                String subCode = tBasicSubjectExcel.getSubCode();
                String category = null;

                if (StringUtils.isNotBlank(subCode)) {
                    // Excel 导入的科目代码
                    tBasicSubjectMessageNew.setExcelImportCode(subCode);

                    // 取得父级科目编码为xx的最大的科目编码
                    // tBasicSubjectMessageMapper.querySubMessageMaxSubCode(tBasicSubjectMessageNew);
                    // 科目代码
                    tBasicSubjectMessageNew.setSubCode(subCode);

                    // 获取科目编码长度
                    int length = subCode.length();

                    Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                    // 科目级别
                    tBasicSubjectMessageNew.setCodeLevel(codeLevel + 1);
                    // tBasicSubjectMessage.get
                    // 添加到导入的上级科目编码
                    // tBasicSubjectMessageNew.setExcelImportSuperiorCoding(excelImportSuperiorCoding);
                    // 添加上级科目编码
                    tBasicSubjectMessageNew.setSuperiorCoding(subCode);

                    // tBasicSubjectMessage.setSubCode(subCode);
                    // 获取 科目代码首个数字
                    category = (String) subCode.subSequence(0, 1);
                } else {
                    // 添加到导入的上级科目编码
                    tBasicSubjectMessageNew.setExcelImportSuperiorCoding("");

                    // 添加上级科目编码
                    tBasicSubjectMessageNew.setSuperiorCoding("");

                    // Excel 导入的科目代码
                    tBasicSubjectMessageNew.setExcelImportCode("");

                    // 科目代码
                    tBasicSubjectMessageNew.setSubCode("");
                }

                // //同级编码
                // String excelImportSiblingsCoding =
                // tBasicSubjectExcel.getSiblingsCoding();
                // if (StringUtils.isNotBlank(excelImportSiblingsCoding))
                // {
                // // 设置Excel导入的同级科目编码
                // tBasicSubjectMessageNew.setExcelImportSiblingsCoding(excelImportSiblingsCoding);
                // // 设置同级科目编码
                // tBasicSubjectMessageNew.setSiblingsCoding(excelImportSiblingsCoding);
                //
                // // 获取 科目代码首个数字
                // category = (String) excelImportSiblingsCoding.subSequence(0,
                // 1);
                // }

                // '类别(1、资产类2、负债类 3、共同类4、所有者权益类5、成本类6、损益类)
                tBasicSubjectMessageNew.setCategory(category);

                String subName = tBasicSubjectExcel.getSubName(); // 科目名称
                if (StringUtils.isNotBlank(subName)) {
                    // 科目名称
                    tBasicSubjectMessageNew.setSubName(subName);
                    // 科目全名
                    String fullName = tBasicSubjectMessage.getFullName();
                    tBasicSubjectMessageNew.setFullName(fullName + subName);
                } else {
                    tBasicSubjectMessageNew.setSubName("");
                }

                // 科目来源
                tBasicSubjectMessage.setSubSource("Excel导入-手动匹配");

                // 币别
                String typeOfCurrency = tBasicSubjectExcel.getTypeOfCurrency();
                tBasicSubjectMessage.setTypeOfCurrency(typeOfCurrency);

                String typeOfCurrencyMessage = tBasicSubjectMessage.getTypeOfCurrency();
                if (typeOfCurrencyMessage.contains("RMB") || typeOfCurrency.contains("本位币")) {
                    tBasicSubjectMessage.setExchangeRateState(0);// 外币设置状态(0关闭，1开启)
                } else {
                    tBasicSubjectMessage.setExchangeRateState(1);// 外币设置状态
                    // 开启(0关闭，1开启)
                    // 调用
                    // 查询此用户有无此币别
                    // 没有则添加

                    tBasicExchangeRateService.queryExeRateByCuyAbbe(typeOfCurrency, session);

                }

                // 本年累计发生额(借方)
                BigDecimal yearAmountCredit = tBasicSubjectExcel.getYearAmountCredit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getYearAmountCredit();
                tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);

                // 本年累计发生额(贷方)
                BigDecimal yearAmountDebit = tBasicSubjectExcel.getYearAmountDebit()
                        .compareTo(new BigDecimal(0E-8)) == 0 ? new BigDecimal(0)
                        : tBasicSubjectExcel.getYearAmountDebit();
                tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);

                tBasicSubjectMessageList.add(tBasicSubjectMessage);
            }
            int no = tBasicSubjectMessageMapper.addSubMessageList(tBasicSubjectMessageList);
            result.put("code", 1);
            result.put("no", no);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 科目金额写入账套下一会计期间
     */
    @Override
    public void insertSubjectNextPerion(HttpSession session) throws BusinessException {
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user");
        Account account = (Account) sessionMap.get("account");
        String busDate = (String) sessionMap.get("busDate");
        Map<String, Object> param = new HashMap<String, Object>();
        String accountID = account.getAccountID();
        param.put("accountID", accountID);
        param.put("busDate", busDate);
        String[] yearMonth = busDate.split("-");
        Integer year = Integer.parseInt(yearMonth[0]);
        Integer month = Integer.parseInt(yearMonth[1]);
        String nexPeriod = null;
        boolean isYearEnd = false;
        if (month < 10) {
            nexPeriod = year + "-0" + (month + 1);
        } else if (month == 10 || month == 11) {
            nexPeriod = year + "-" + (month + 1);
        } else if (month == 12) {
            isYearEnd = true;
            nexPeriod = (year + 1) + "-01";
        }
        List<TBasicSubjectMessage> list = tBasicSubjectMessageMapper.queryAllSubject(param);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TBasicSubjectMessage oldSubject = list.get(i);
                oldSubject.setPkSubId(UUIDUtils.getUUID());
                // TBasicSubjectMessage newSubject = new TBasicSubjectMessage();
                // 设置下个会计期间
                oldSubject.setAccountPeriod(nexPeriod);
                // 设置下个期间期初借贷方金额
                oldSubject.setInitDebitBalance(oldSubject.getEndingBalanceDebit());
                oldSubject.setInitCreditBalance(oldSubject.getEndingBalanceCredit());
                oldSubject.setCurrentAmountCredit(new BigDecimal(0.0));
                oldSubject.setCurrentAmountDebit(new BigDecimal(0.0));
                if (isYearEnd) {
                    oldSubject.setYearAmountDebit(new BigDecimal(0.0));
                    oldSubject.setYearAmountCredit(new BigDecimal(0.0));
                }
                List<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
                tBasicSubjectMessageMapper.addSubMessageList(tBasicSubjectMessagelist);
            }
        }
    }

    @Override
    public List<TBasicSubjectMessage> querySubByIDAndName(Map<String, Object> param) throws BusinessException {
        return tBasicSubjectMessageMapper.querySubByIDAndName(param);
    }

    /**
     * @param session
     * @param subCode
     * @return
     * @throws BusinessException
     * @Title: querySubMessageMaxSubCodeStr
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @see com.wqb.service.subexcel.TBasicSubjectMessageService#querySubMessageMaxSubCodeStr(javax.servlet.http.HttpSession,
     * java.lang.String)
     */
    @Override
    public Map<String, Object> querySubMessageMaxSubCodeStr(HttpSession session, String subCode)
            throws BusinessException {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            // 获取帐套期间
            String busDate = (String) sessionMap.get("busDate");

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            tBasicSubjectMessage.setAccountPeriod(busDate);

            if (StringUtils.isNotBlank(subCode)) {
                // 设置上级科目编码
                tBasicSubjectMessage.setSuperiorCoding(subCode);

                // 如果科目长度为4 表示为一级科目
                if (subCode.length() == 4) {
                    // 一级科目添加时查询二级科目最大值
                    tBasicSubjectMessage.setCodeLevel(2);
                } else {
                    // 只针对于4-3-3格式科目代码有效
                    int level = (subCode.length() - 4) / 3;
                    tBasicSubjectMessage.setCodeLevel(level + 2);
                }

            }
            String subMessageCode = tBasicSubjectMessageMapper.querySubMessageMaxSubCode(tBasicSubjectMessage);
            // int parseInt = 0;
            // long parseLong = 0;
            if (StringUtils.isNotBlank(subMessageCode)) {
                // long数据类型是64位、有符号的以二进制补码表示的整数；
                // 最小值是-9,223,372,036,854,775,808（-2^63）；
                // 最大值是9,223,372,036,854,775,807（2^63 -1）；
                // parseLong = Long.parseLong(subMessageCode.trim());
                // subMessageCode = parseLong + 1 + "";

                // int数据类型是32位、有符号的以二进制补码表示的整数；
                // 最小值是-2,147,483,648（-2^31）；
                // 最大值是2,147,483,647（2^31 - 1）；
                // parseInt = Integer.parseInt(subMessageCode.trim());
                // subMessageCode = parseInt + 1 + "";

                // 获取下级科目代码最大值
                StringBuffer stringBuffer = new StringBuffer(subMessageCode.trim());
                String substring = stringBuffer.substring(subMessageCode.length() - 7, subMessageCode.length());// 1403220003--0003
                String substring2 = stringBuffer.substring(0, subMessageCode.length() - 7);
                int parseInt = Integer.parseInt(substring);
                // Integer parseInteger = parseInt;
                // String parseString = parseInteger.toString();
                // for(int i = 4; parseString.length() < i;)
                // {
                // parseString = "0" + parseString;
                // }
                // parseInt = Integer.parseInt(parseString);
                if (substring2.equals("0")) {
                    subMessageCode = (parseInt + 1) + "";
                } else {
                    subMessageCode = substring2 + (parseInt + 1);
                }
            } else {
                subMessageCode = subCode + "001";
            }
            result.put("subMessageCode", subMessageCode);
        }
        // catch (NumberFormatException e)
        // {
        // e.printStackTrace();
        // result.put("message", "最大只支持六级");
        // }
        catch (Exception e) {
            e.printStackTrace();
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param session
     * @param parameters
     * @return
     * @throws BusinessException
     * @Title: queryLedgerByParameters
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @see com.wqb.service.subexcel.TBasicSubjectMessageService#queryLedgerByParameters(javax.servlet.http.HttpSession,
     * java.util.Map)
     */
    @Override
    public Map<String, Object> queryLedgerByParameters(User user, Account account, Map<String, String> parameters)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String accountId = account.getAccountID();// 账套id
            String busDate = account.getUseLastPeriod();
            String userId = user.getUserID();// 用户id
            if (parameters.isEmpty()) {
                parameters.put("accountPeriod", busDate);
            }
            parameters.put("userId", userId);
            parameters.put("accountId", accountId);
            // parameters.put("codeLevel", String.valueOf(1));
            // parameters.put("typeOfCurrency", "综合本位币");

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.queryLedgerByParameters(parameters);

            List<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
            BigDecimal initCreditBalances = new BigDecimal("0");
            BigDecimal initDebitBalances = new BigDecimal("0");
            BigDecimal endingBalanceCredits = new BigDecimal("0");
            BigDecimal endingBalanceDebits = new BigDecimal("0");
            BigDecimal yearAmountDebits = new BigDecimal("0");
            BigDecimal yearAmountCredits = new BigDecimal("0");
            for (TBasicSubjectMessage tBasicSubjectMessage : subMessages) {
                String typeOfCurrency = tBasicSubjectMessage.getTypeOfCurrency();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (1 == codeLevel) {
                    if ("综合本位币".equals(typeOfCurrency)) {
                        String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                        if (StringUtils.isNotBlank(siblingsCoding)) {
                            tBasicSubjectMessage.setSubCode(siblingsCoding);
                        }
                        String siblingsSubName = tBasicSubjectMessage.getSiblingsSubName();
                        if (StringUtils.isNotBlank(siblingsSubName)) {
                            tBasicSubjectMessage.setSubName(siblingsSubName);
                        }
                        // BigDecimal initCreditBalance =
                        // tBasicSubjectMessage.getInitCreditBalance();
                        // initCreditBalances.add(initCreditBalance);
                        // tBasicSubjectMessage.setInitCreditBalance(initCreditBalances);
                        //
                        // BigDecimal initDebitBalance =
                        // tBasicSubjectMessage.getInitDebitBalance();
                        // initDebitBalances.add(initDebitBalance);
                        // tBasicSubjectMessage.setInitDebitBalance(initDebitBalances);
                        //
                        // BigDecimal endingBalanceCredit =
                        // tBasicSubjectMessage.getEndingBalanceCredit();
                        // endingBalanceCredits.add(endingBalanceCredit);
                        // tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredits);
                        //
                        // BigDecimal endingBalanceDebit =
                        // tBasicSubjectMessage.getEndingBalanceDebit();
                        // endingBalanceDebits.add(endingBalanceDebit);
                        // tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebits);
                        //
                        // BigDecimal yearAmountDebit =
                        // tBasicSubjectMessage.getYearAmountDebit();
                        // yearAmountDebits.add(yearAmountDebit);
                        // tBasicSubjectMessage.setYearAmountDebit(yearAmountDebits);
                        //
                        // BigDecimal yearAmountCredit =
                        // tBasicSubjectMessage.getYearAmountCredit();
                        // yearAmountCredits.add(yearAmountCredit);
                        // tBasicSubjectMessage.setYearAmountCredit(yearAmountCredits);

                        tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isBlank(typeOfCurrency)) {
                        // BigDecimal initCreditBalance =
                        // tBasicSubjectMessage.getInitCreditBalance();
                        // initCreditBalances.add(initCreditBalance);
                        // tBasicSubjectMessage.setInitCreditBalance(initCreditBalances);
                        //
                        // BigDecimal initDebitBalance =
                        // tBasicSubjectMessage.getInitDebitBalance();
                        // initDebitBalances.add(initDebitBalance);
                        // tBasicSubjectMessage.setInitDebitBalance(initDebitBalances);
                        //
                        // BigDecimal endingBalanceCredit =
                        // tBasicSubjectMessage.getEndingBalanceCredit();
                        // endingBalanceCredits.add(endingBalanceCredit);
                        // tBasicSubjectMessage.setEndingBalanceCredit(endingBalanceCredits);
                        //
                        // BigDecimal endingBalanceDebit =
                        // tBasicSubjectMessage.getEndingBalanceDebit();
                        // endingBalanceDebits.add(endingBalanceDebit);
                        // tBasicSubjectMessage.setEndingBalanceDebit(endingBalanceDebits);
                        //
                        // BigDecimal yearAmountDebit =
                        // tBasicSubjectMessage.getYearAmountDebit();
                        // yearAmountDebits.add(yearAmountDebit);
                        // tBasicSubjectMessage.setYearAmountDebit(yearAmountDebits);
                        //
                        // BigDecimal yearAmountCredit =
                        // tBasicSubjectMessage.getYearAmountCredit();
                        // yearAmountCredits.add(yearAmountCredit);
                        // tBasicSubjectMessage.setYearAmountCredit(yearAmountCredits);

                        tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                    }

                }
            }

            Collections.sort(tBasicSubjectMessagelist, new Comparator<TBasicSubjectMessage>() {
                @Override
                public int compare(TBasicSubjectMessage o1, TBasicSubjectMessage o2) {
                    return o1.getSubCode().compareTo(o2.getSubCode());
                }
            });
            result.put("subMessages", tBasicSubjectMessagelist);

            result.put("subMessages", subMessages);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> querySubjectByName(HttpSession session, String subName) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id

            map.put("account", account);
            map.put("subName", subName);

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.querySubjectByName(map);
            result.put("subMessages", subMessages);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param subject  科目名称或代
     * @param category 科目类别
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySubMessageByCategory
     * @Description: 根据科目名称或代码 和 科目类别查询科目
     * @date 2018年1月23日 上午9:55:04
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessageByCategory(HttpSession session, String subject, String category)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userId = user.getUserID();// 用户id

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate"); // 获取帐套期间

            map.put("accountId", accountId);
            map.put("subject", subject);
            map.put("category", category);
            map.put("userId", userId);
            map.put("busDate", busDate);
            List<TBasicSubjectMessage> subMessageList = tBasicSubjectMessageMapper.querySubMessageByCategory(map);
            result.put("subMessageList", subMessageList);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: addSubMessageMessageList
     * @Description: 结账时科目 本期结账到下期
     * @date 2018年1月24日 下午7:01:31
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> addSubMessageMessageList(Account account, User user,
                                                        List<TBasicSubjectMessage> tBasicSubjectMessageList) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();

        List<TBasicSubjectMessage> tBasicSubjectMessages = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        BigDecimal bigDecimal = new BigDecimal("0.0");

        // 本年累计发生额(贷方)
        BigDecimal yearAmountCredit = new BigDecimal("0.0");
        // 本年累计发生额(借方)
        BigDecimal yearAmountDebit = new BigDecimal("0.0");

        String busDate = account.getUseLastPeriod();
        String accountID = account.getAccountID();
        String userID = user.getUserID();

        if (account != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            List<StatusPeriod> list = periodStatusDao.queryStatus(param);
            if (null != list && list.size() > 0) {
                if (list.get(0).getIsJz() == 0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                    String substring = busDate.substring(busDate.lastIndexOf("-") + 1);
                    if (substring.equals("12")) {
                        for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                            Date date = new Date();
                            // String accountPeriod =
                            // tBasicSubjectMessage.getAccountPeriod();
                            String newAccountPeriod = newAccountPeriod(busDate);
                            tBasicSubjectMessage.setAccountPeriod(newAccountPeriod);

                            String uuid = UUIDUtils.getUUID();
                            tBasicSubjectMessage.setPkSubId(uuid);
                            tBasicSubjectMessage.setAccountId(accountID);
                            BigDecimal endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit();
                            BigDecimal endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit();

                            // 期初余额(贷方)
                            tBasicSubjectMessage.setInitCreditBalance(endingBalanceCredit);
                            // 期初余额(借方)
                            tBasicSubjectMessage.setInitDebitBalance(endingBalanceDebit);

                            // 本期发生额(贷方)',
                            tBasicSubjectMessage.setCurrentAmountCredit(bigDecimal);
                            // 本期发生额(借方)'
                            tBasicSubjectMessage.setCurrentAmountDebit(bigDecimal);

                            tBasicSubjectMessage.setUpdateDate(date);

                            String format = simpleDateFormat.format(date);
                            tBasicSubjectMessage.setUpdateTimestamp(format);

                            tBasicSubjectMessage.setUserId(userID);

                            // 本年累计发生额(贷方)
                            tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);
                            // 本年累计发生额(借方)
                            tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);

                            tBasicSubjectMessages.add(tBasicSubjectMessage);
                        }
                    } else {
                        for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                            Date date = new Date();
                            // String accountPeriod =
                            // tBasicSubjectMessage.getAccountPeriod();
                            String newAccountPeriod = newAccountPeriod(busDate);
                            tBasicSubjectMessage.setAccountPeriod(newAccountPeriod);

                            String uuid = UUIDUtils.getUUID();
                            tBasicSubjectMessage.setPkSubId(uuid);
                            tBasicSubjectMessage.setAccountId(accountID);
                            BigDecimal endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit();
                            BigDecimal endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit();

                            // 期初余额(贷方)
                            tBasicSubjectMessage.setInitCreditBalance(endingBalanceCredit);
                            // 期初余额(借方)
                            tBasicSubjectMessage.setInitDebitBalance(endingBalanceDebit);

                            // 本期发生额(贷方)',
                            tBasicSubjectMessage.setCurrentAmountCredit(bigDecimal);
                            // 本期发生额(借方)'
                            tBasicSubjectMessage.setCurrentAmountDebit(bigDecimal);

                            tBasicSubjectMessage.setUpdateDate(date);

                            String format = simpleDateFormat.format(date);
                            tBasicSubjectMessage.setUpdateTimestamp(format);

                            tBasicSubjectMessage.setUserId(userID);

                            // 本年累计发生额(贷方)
                            // tBasicSubjectMessage.setYearAmountCredit(yearAmountCredit);
                            // 本年累计发生额(借方)
                            // tBasicSubjectMessage.setYearAmountDebit(yearAmountDebit);

                            tBasicSubjectMessages.add(tBasicSubjectMessage);
                        }
                    }
                    //先删除下一期的科目余额表
                    TBasicSubjectMessage nextTsm = new TBasicSubjectMessage();
                    nextTsm.setAccountId(accountID);
                    String nextPeriod = newAccountPeriod(busDate);
                    nextTsm.setAccountPeriod(nextPeriod);
                    tBasicSubjectMessageMapper.deleteMessageByAcctperiod(nextTsm);
                    int no = tBasicSubjectMessageMapper.addSubMessageList(tBasicSubjectMessages);
                    if (no > 0) {
                        periodStatusDao.updStatuJz(param);
                    }
                    result.put("no", no);
                    result.put("code", 1);
                } else if (list.get(0).getIsJz() == 1) {
                    result.put("msg", "本期已结帐,不允许再次结帐");
                } else {
                    result.put("msg", "结帐失败,请检查后再试");
                }
            } else {
                result.put("msg", "没有查到当期帐套");
            }
        }

        return result;
    }

    public static String newAccountPeriod(String accountPeriod) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date parse = null;
        try {
            parse = dateFormat.parse(accountPeriod);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        instance.add(instance.MONTH, 1);
        Date time = instance.getTime();
        String format = dateFormat.format(time);
        return format;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageListMoney
     * @Description: 查询真实金额
     * @date 2018年1月25日 下午7:14:28
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySubMessageListMoney(HttpSession session) {
        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        List<TBasicSubjectMessage> querySubMessage = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicSubjectMessage.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicSubjectMessage.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            tBasicSubjectMessage.setAccountPeriod(busDate);
            tBasicSubjectMessage.setCodeLevel(1);

            querySubMessage = tBasicSubjectMessageMapper.querySubMessageListMoney(tBasicSubjectMessage);
            result.put("subMessage", querySubMessage);
            result.put("cdoe", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySbujectBalance
     * @Description: 查询科目余额表(外币情况只取综合本位币)
     * @date 2018年2月5日 下午3:07:05
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> querySbujectBalance(User user, Account account, Map<String, String> parameters)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String accountId = account.getAccountID();// 账套id
            String busDate = account.getUseLastPeriod();
            String userId = user.getUserID();// 用户id
            if (parameters.isEmpty()) {
                parameters.put("accountPeriod", busDate);
            } else if (!parameters.equals(busDate)) {
                // 用于查询资产负债表年初余额数据
                parameters.put("accountPeriod", busDate);
            }
            parameters.put("userId", userId);
            parameters.put("accountId", accountId);

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.querySbujectBalance(parameters);

            ArrayList<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
            for (TBasicSubjectMessage tBasicSubjectMessage : subMessages) {
                String typeOfCurrency = tBasicSubjectMessage.getTypeOfCurrency();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                // if(1 == codeLevel)
                // {
                if ("综合本位币".equals(typeOfCurrency)) {
                    String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                    if (StringUtils.isNotBlank(siblingsCoding)) {
                        tBasicSubjectMessage.setSubCode(siblingsCoding);
                    }
                    String siblingsSubName = tBasicSubjectMessage.getSiblingsSubName();
                    if (StringUtils.isNotBlank(siblingsSubName)) {
                        tBasicSubjectMessage.setSubName(siblingsSubName);
                    }
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
                if (StringUtils.isBlank(typeOfCurrency)) {
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
                // }
            }

            Collections.sort(tBasicSubjectMessagelist, new Comparator<TBasicSubjectMessage>() {
                @Override
                public int compare(TBasicSubjectMessage o1, TBasicSubjectMessage o2) {
                    return o1.getSubCode().compareTo(o2.getSubCode());
                }
            });

            // 期初余额(借方)合计
            BigDecimal initDebitBalanceTotal = new BigDecimal("0");
            // 期初余额(贷方)合计
            BigDecimal initCreditBalanceTotal = new BigDecimal("0");
            // 本期发生额(借方)合计
            BigDecimal currentAmountDebitTotal = new BigDecimal("0");
            // 本期发生额(贷方)合计
            BigDecimal currentAmountCreditTotal = new BigDecimal("0");
            // 期末余额(借方)合计
            BigDecimal endingBalanceDebitTotal = new BigDecimal("0");
            // 期末余额(贷方)合计
            BigDecimal endingBalanceCreditTotal = new BigDecimal("0");
            // 本年累计发生额(借方)合计
            BigDecimal yearAmountDebitTotal = new BigDecimal("0");
            // 本年累计发生额(贷方)合计
            BigDecimal yearAmountCreditTotal = new BigDecimal("0");
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessagelist) {
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (codeLevel == 1) {
                    // 期初余额(借方)
                    BigDecimal initDebitBalance = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getInitDebitBalance());
                    initDebitBalanceTotal = initDebitBalanceTotal.add(initDebitBalance);
                    // 期初余额(贷方)
                    BigDecimal initCreditBalance = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getInitCreditBalance());
                    initCreditBalanceTotal = initCreditBalanceTotal.add(initCreditBalance);
                    // 本期发生额(借方)
                    BigDecimal currentAmountDebit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getCurrentAmountDebit());
                    currentAmountDebitTotal = currentAmountDebitTotal.add(currentAmountDebit);
                    // 本期发生额(贷方)',
                    BigDecimal currentAmountCredit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getCurrentAmountCredit());
                    currentAmountCreditTotal = currentAmountCreditTotal.add(currentAmountCredit);
                    // 期末余额(借方)',
                    BigDecimal endingBalanceDebit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getEndingBalanceDebit());
                    endingBalanceDebitTotal = endingBalanceDebitTotal.add(endingBalanceDebit);
                    // 期末余额(贷方)',
                    BigDecimal endingBalanceCredit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getEndingBalanceCredit());
                    endingBalanceCreditTotal = endingBalanceCreditTotal.add(endingBalanceCredit);
                    // 本年累计发生额(借方)',
                    BigDecimal yearAmountDebit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getYearAmountDebit());
                    yearAmountDebitTotal = yearAmountDebitTotal.add(yearAmountDebit);
                    // 本年累计发生额(贷方)',
                    BigDecimal yearAmountCredit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getYearAmountCredit());
                    yearAmountCreditTotal = yearAmountCreditTotal.add(yearAmountCredit);
                }
            }
            result.put("subMessages", tBasicSubjectMessagelist);
            result.put("initDebitBalanceTotal", initDebitBalanceTotal);
            result.put("initCreditBalanceTotal", initCreditBalanceTotal);
            result.put("currentAmountDebitTotal", currentAmountDebitTotal);
            result.put("currentAmountCreditTotal", currentAmountCreditTotal);
            result.put("endingBalanceDebitTotal", endingBalanceDebitTotal);
            result.put("endingBalanceCreditTotal", endingBalanceCreditTotal);
            result.put("yearAmountDebitTotal", yearAmountDebitTotal);
            result.put("yearAmountCreditTotal", yearAmountCreditTotal);
            // result.put("subMessages", subMessages);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 凭证页面，科目余额表页面导出功能；
     *
     * @return
     * @author tangsheng time:2018-08-02
     */
    @Override
    public Map<String, Object> querySbujectExcleExport(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");
            if (parameters.isEmpty()) {
                parameters.put("accountPeriod", busDate);
            }
            parameters.put("accountId", accountId);

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.querySbujectBalance(parameters);
            ArrayList<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
            for (TBasicSubjectMessage tBasicSubjectMessage : subMessages) {
                String typeOfCurrency = tBasicSubjectMessage.getTypeOfCurrency();
                if ("综合本位币".equals(typeOfCurrency)) {
                    String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                    if (StringUtils.isNotBlank(siblingsCoding)) {
                        tBasicSubjectMessage.setSubCode(siblingsCoding);
                    }
                    String siblingsSubName = tBasicSubjectMessage.getSiblingsSubName();
                    if (StringUtils.isNotBlank(siblingsSubName)) {
                        tBasicSubjectMessage.setSubName(siblingsSubName);
                    }
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
                if (StringUtils.isBlank(typeOfCurrency)) {
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
            }

            Collections.sort(tBasicSubjectMessagelist, new Comparator<TBasicSubjectMessage>() {
                @Override
                public int compare(TBasicSubjectMessage o1, TBasicSubjectMessage o2) {
                    return o1.getSubCode().compareTo(o2.getSubCode());
                }
            });

            // 期初余额(借方)合计
            BigDecimal initDebitBalanceTotal = new BigDecimal("0");
            // 期初余额(贷方)合计
            BigDecimal initCreditBalanceTotal = new BigDecimal("0");
            // 本期发生额(借方)合计
            BigDecimal currentAmountDebitTotal = new BigDecimal("0");
            // 本期发生额(贷方)合计
            BigDecimal currentAmountCreditTotal = new BigDecimal("0");
            // 期末余额(借方)合计
            BigDecimal endingBalanceDebitTotal = new BigDecimal("0");
            // 期末余额(贷方)合计
            BigDecimal endingBalanceCreditTotal = new BigDecimal("0");
            // 本年累计发生额(借方)合计
            BigDecimal yearAmountDebitTotal = new BigDecimal("0");
            // 本年累计发生额(贷方)合计
            BigDecimal yearAmountCreditTotal = new BigDecimal("0");
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessagelist) {
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (codeLevel == 1) {
                    // 期初余额(借方)
                    BigDecimal initDebitBalance = tBasicSubjectMessage.getInitDebitBalance() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getInitDebitBalance();
                    initDebitBalanceTotal = initDebitBalanceTotal.add(initDebitBalance);
                    // 期初余额(贷方)
                    BigDecimal initCreditBalance = tBasicSubjectMessage.getInitCreditBalance() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getInitCreditBalance();
                    initCreditBalanceTotal = initCreditBalanceTotal.add(initCreditBalance);
                    // 本期发生额(借方)
                    BigDecimal currentAmountDebit = tBasicSubjectMessage.getCurrentAmountDebit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getCurrentAmountDebit();
                    currentAmountDebitTotal = currentAmountDebitTotal.add(currentAmountDebit);
                    // 本期发生额(贷方)',
                    BigDecimal currentAmountCredit = tBasicSubjectMessage.getCurrentAmountCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getCurrentAmountCredit();
                    currentAmountCreditTotal = currentAmountCreditTotal.add(currentAmountCredit);
                    // 期末余额(借方)',
                    BigDecimal endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getEndingBalanceDebit();
                    endingBalanceDebitTotal = endingBalanceDebitTotal.add(endingBalanceDebit);
                    // 期末余额(贷方)',
                    BigDecimal endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getEndingBalanceCredit();
                    endingBalanceCreditTotal = endingBalanceCreditTotal.add(endingBalanceCredit);
                    // 本年累计发生额(借方)',
                    BigDecimal yearAmountDebit = tBasicSubjectMessage.getYearAmountDebit() == null ? new BigDecimal("0")
                            : tBasicSubjectMessage.getYearAmountDebit();
                    yearAmountDebitTotal = yearAmountDebitTotal.add(yearAmountDebit);
                    // 本年累计发生额(贷方)',
                    BigDecimal yearAmountCredit = tBasicSubjectMessage.getYearAmountCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getYearAmountCredit();
                    yearAmountCreditTotal = yearAmountCreditTotal.add(yearAmountCredit);
                }
            }
            result.put("subMessages", tBasicSubjectMessagelist);
            result.put("initDebitBalanceTotal", initDebitBalanceTotal);
            result.put("initCreditBalanceTotal", initCreditBalanceTotal);
            result.put("currentAmountDebitTotal", currentAmountDebitTotal);
            result.put("currentAmountCreditTotal", currentAmountCreditTotal);
            result.put("endingBalanceDebitTotal", endingBalanceDebitTotal);
            result.put("endingBalanceCreditTotal", endingBalanceCreditTotal);
            result.put("yearAmountDebitTotal", yearAmountDebitTotal);
            result.put("yearAmountCreditTotal", yearAmountCreditTotal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 这是APP科目余额表查询接口
    @Override
    public Map<String, Object> querySbujectBalanceAPP(Map<String, Object> param) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();
        try {

            // 用于查询资产负债表年初余额数据
            parameters.put("accountPeriod", param.get("period").toString());
            parameters.put("accountId", param.get("accountID").toString());

            if (!StringUtil.objEmpty(param.get("keyWord"))) {
                String keyWord = param.get("keyWord").toString();
                // linux 正是环境是正常编码 使用下面代码会导致 乱码
                // keyWord = new String(keyWord.getBytes("ISO-8859-1"),
                // "UTF-8");
                if (keyWord.matches("^\\d+$")) {
                    parameters.put("sub_code_app", keyWord); // 左
                } else {
                    parameters.put("sub_name_app", keyWord); // 左右
                }
            }

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.querySbujectBalance(parameters);
            ArrayList<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
            for (TBasicSubjectMessage tBasicSubjectMessage : subMessages) {
                String typeOfCurrency = tBasicSubjectMessage.getTypeOfCurrency();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                // if(1 == codeLevel)
                // {
                if ("综合本位币".equals(typeOfCurrency)) {
                    String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                    if (StringUtils.isNotBlank(siblingsCoding)) {
                        tBasicSubjectMessage.setSubCode(siblingsCoding);
                    }
                    String siblingsSubName = tBasicSubjectMessage.getSiblingsSubName();
                    if (StringUtils.isNotBlank(siblingsSubName)) {
                        tBasicSubjectMessage.setSubName(siblingsSubName);
                    }
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
                if (StringUtils.isBlank(typeOfCurrency)) {
                    tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                }
                // }
            }

            Collections.sort(tBasicSubjectMessagelist, new Comparator<TBasicSubjectMessage>() {
                @Override
                public int compare(TBasicSubjectMessage o1, TBasicSubjectMessage o2) {
                    return o1.getSubCode().compareTo(o2.getSubCode());
                }
            });

            // 期初余额(借方)合计
            BigDecimal initDebitBalanceTotal = new BigDecimal("0");
            // 期初余额(贷方)合计
            BigDecimal initCreditBalanceTotal = new BigDecimal("0");
            // 本期发生额(借方)合计
            BigDecimal currentAmountDebitTotal = new BigDecimal("0");
            // 本期发生额(贷方)合计
            BigDecimal currentAmountCreditTotal = new BigDecimal("0");
            // 期末余额(借方)合计
            BigDecimal endingBalanceDebitTotal = new BigDecimal("0");
            // 期末余额(贷方)合计
            BigDecimal endingBalanceCreditTotal = new BigDecimal("0");
            // 本年累计发生额(借方)合计
            BigDecimal yearAmountDebitTotal = new BigDecimal("0");
            // 本年累计发生额(贷方)合计
            BigDecimal yearAmountCreditTotal = new BigDecimal("0");
            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessagelist) {
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (codeLevel == 1) {
                    // 期初余额(借方)
                    BigDecimal initDebitBalance = tBasicSubjectMessage.getInitDebitBalance() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getInitDebitBalance();
                    initDebitBalanceTotal = initDebitBalanceTotal.add(initDebitBalance);
                    // 期初余额(贷方)
                    BigDecimal initCreditBalance = tBasicSubjectMessage.getInitCreditBalance() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getInitCreditBalance();
                    initCreditBalanceTotal = initCreditBalanceTotal.add(initCreditBalance);
                    // 本期发生额(借方)
                    BigDecimal currentAmountDebit = tBasicSubjectMessage.getCurrentAmountDebit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getCurrentAmountDebit();
                    currentAmountDebitTotal = currentAmountDebitTotal.add(currentAmountDebit);
                    // 本期发生额(贷方)',
                    BigDecimal currentAmountCredit = tBasicSubjectMessage.getCurrentAmountCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getCurrentAmountCredit();
                    currentAmountCreditTotal = currentAmountCreditTotal.add(currentAmountCredit);
                    // 期末余额(借方)',
                    BigDecimal endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getEndingBalanceDebit();
                    endingBalanceDebitTotal = endingBalanceDebitTotal.add(endingBalanceDebit);
                    // 期末余额(贷方)',
                    BigDecimal endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getEndingBalanceCredit();
                    endingBalanceCreditTotal = endingBalanceCreditTotal.add(endingBalanceCredit);
                    // 本年累计发生额(借方)',
                    BigDecimal yearAmountDebit = tBasicSubjectMessage.getYearAmountDebit() == null ? new BigDecimal("0")
                            : tBasicSubjectMessage.getYearAmountDebit();
                    yearAmountDebitTotal = yearAmountDebitTotal.add(yearAmountDebit);
                    // 本年累计发生额(贷方)',
                    BigDecimal yearAmountCredit = tBasicSubjectMessage.getYearAmountCredit() == null
                            ? new BigDecimal("0") : tBasicSubjectMessage.getYearAmountCredit();
                    yearAmountCreditTotal = yearAmountCreditTotal.add(yearAmountCredit);
                }
            }
            result.put("subMessages", tBasicSubjectMessagelist);
            result.put("initDebitBalanceTotal", initDebitBalanceTotal);
            result.put("initCreditBalanceTotal", initCreditBalanceTotal);
            result.put("currentAmountDebitTotal", currentAmountDebitTotal);
            result.put("currentAmountCreditTotal", currentAmountCreditTotal);
            result.put("endingBalanceDebitTotal", endingBalanceDebitTotal);
            result.put("endingBalanceCreditTotal", endingBalanceCreditTotal);
            result.put("yearAmountDebitTotal", yearAmountDebitTotal);
            result.put("yearAmountCreditTotal", yearAmountCreditTotal);
            if (tBasicSubjectMessagelist != null && tBasicSubjectMessagelist.size() > 0) {
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取数据异常");
        }
    }

    // 这是总账与明细账APPP接口
    @Override
    public Map<String, Object> queryDetailAccountAPP(Map<String, Object> param) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("accountPeriod", param.get("period").toString());
            parameters.put("accountId", param.get("accountID").toString());
            // accountId
            if (!StringUtil.objEmpty(param.get("keyWord"))) {
                String keyWord = param.get("keyWord").toString();
                // linux 正是环境是正常编码 使用下面代码会导致 乱码
                // keyWord = new String(keyWord.getBytes("ISO-8859-1"),
                // "UTF-8");
                if (keyWord.matches("^\\d+$")) {
                    parameters.put("sub_code", keyWord); // 右查询
                } else {
                    parameters.put("sub_name", keyWord); // 左右
                }
            }

            List<TBasicSubjectMessage> subMessages = tBasicSubjectMessageMapper.queryLedgerByParameters(parameters);

            List<TBasicSubjectMessage> tBasicSubjectMessagelist = new ArrayList<TBasicSubjectMessage>();
            BigDecimal initCreditBalances = new BigDecimal("0");
            BigDecimal initDebitBalances = new BigDecimal("0");
            BigDecimal endingBalanceCredits = new BigDecimal("0");
            BigDecimal endingBalanceDebits = new BigDecimal("0");
            BigDecimal yearAmountDebits = new BigDecimal("0");
            BigDecimal yearAmountCredits = new BigDecimal("0");
            for (TBasicSubjectMessage tBasicSubjectMessage : subMessages) {
                String typeOfCurrency = tBasicSubjectMessage.getTypeOfCurrency();
                Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
                if (1 == codeLevel) {
                    if ("综合本位币".equals(typeOfCurrency)) {
                        String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                        if (StringUtils.isNotBlank(siblingsCoding)) {
                            tBasicSubjectMessage.setSubCode(siblingsCoding);
                        }
                        String siblingsSubName = tBasicSubjectMessage.getSiblingsSubName();
                        if (StringUtils.isNotBlank(siblingsSubName)) {
                            tBasicSubjectMessage.setSubName(siblingsSubName);
                        }
                        tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                    }
                    if (StringUtils.isBlank(typeOfCurrency)) {
                        tBasicSubjectMessagelist.add(tBasicSubjectMessage);
                    }
                }
            }

            Collections.sort(tBasicSubjectMessagelist, new Comparator<TBasicSubjectMessage>() {
                @Override
                public int compare(TBasicSubjectMessage o1, TBasicSubjectMessage o2) {
                    return o1.getSubCode().compareTo(o2.getSubCode());
                }
            });
            // result.put("subMessages", tBasicSubjectMessagelist);
            if (tBasicSubjectMessagelist != null && tBasicSubjectMessagelist.size() > 0) {
                result.put("sub", tBasicSubjectMessagelist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<TBasicSubjectMessage> queryAllSubject(Map<String, Object> param) throws BusinessException {

        return tBasicSubjectMessageMapper.queryAllSubject(param);
    }

    @Override
    public List<TBasicSubjectMessage> querySysBankSubject(Map<String, Object> param) throws BusinessException {
        return tBasicSubjectMessageMapper.querySysBankSubject(param);
    }

    /**
     * @param param
     * @return
     * @Title: querySbuMessageByMapping
     * @Description: 根据科目名称、科目代码、期间查询科目信息
     * @see com.wqb.service.subexcel.TBasicSubjectMessageService#querySbuMessageByMapping(java.util.Map)
     */
    @Override
    public Map<String, Object> querySbuMessageByMapping(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        List<TBasicSubjectMessage> sbuMessages = new ArrayList<TBasicSubjectMessage>();
        sbuMessages = tBasicSubjectMessageMapper.querySbuMessageByMapping(param);
        result.put("sbuMessages", sbuMessages);
        return result;
    }

    @Override
    public boolean isLastStage(Map<String, Object> parameters) {
        boolean lastStage = tBasicSubjectMessageMapper.isLastStage(parameters);
        return lastStage;
    }

    @Override
    public TBasicSubjectMessage querySubMessageByPkSubId(String pkSubId) throws BusinessException {

        return tBasicSubjectMessageMapper.querySubMessageByPkSubId(pkSubId);
    }

    @Override
    public void delSubMessageByPkSubId(String pkSubId) throws BusinessException {
        tBasicSubjectMessageMapper.delSubMessageByPkSubId(pkSubId);

    }

    @Override
    public void chgSubAmountByDeleteSub(Map<String, Object> param) throws BusinessException {
        tBasicSubjectMessageMapper.chgSubAmountByDeleteSub(param);

    }

    @Override
    public void chgSubAmountByAddSub(Map<String, Object> param) throws BusinessException {
        tBasicSubjectMessageMapper.chgSubAmountByAddSub(param);

    }

}
