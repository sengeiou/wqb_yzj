package com.wqb.service.exchange.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.exchange.TBasicExchangeRateMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicExchangeRate;
import com.wqb.model.User;
import com.wqb.service.exchange.TBasicExchangeRateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicExchangeRateServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017年12月28日 上午10:34:03
 */
//@Transactional
@Service("tBasicExchangeRateService")
public class TBasicExchangeRateServiceImpl implements TBasicExchangeRateService {
    @Autowired
    TBasicExchangeRateMapper tBasicExchangeRateMapper;

    @Override
    public Map<String, Object> deleteByPrimaryKey(Map<String, Object> param, HttpSession session)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 1);

        return null;
    }

    @Override
    public Map<String, Object> insertExchangeRate(TBasicExchangeRate tBasicExchangeRate, HttpSession session)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            Date date = new Date();

            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userID = user.getUserID();//用户id

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountID = account.getAccountID();//账套id

            String pkExchangeRateId = UUIDUtils.getUUID();

            tBasicExchangeRate.setPkExchangeRateId(pkExchangeRateId);//主键id
            tBasicExchangeRate.setUserId(userID);//用户id
            tBasicExchangeRate.setAccountId(accountID);//帐套id
            tBasicExchangeRate.setCreatePsn(accountID);//创建人
            tBasicExchangeRate.setCreatePsnId(accountID);//创建人id
            tBasicExchangeRate.setCreateDate(date);//创建日期
            //		tBasicExchangeRate.setExchangeRateName(exchangeRateName);
            //		tBasicExchangeRate.setInitExchangeRate(initExchangeRate);
            //		tBasicExchangeRate.setExchangeRateRemarks(exchangeRateRemarks);
            //		tBasicExchangeRate.setEndingExchangeRate(endingExchangeRate);
            tBasicExchangeRate.setUpdatePsn(accountID);//修改人
            //		tBasicExchangeRate.setUpdatePsnId(accountID);//修改人id
            //		tBasicExchangeRate.setUpdateDate(date); //修改日期

            tBasicExchangeRate.setCurrencyRateNo(pkExchangeRateId);//币别编号(外键)
            int no = tBasicExchangeRateMapper.insertExchangeRate(tBasicExchangeRate);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * @param tBasicExchangeRate
     * @param session
     * @return
     * @throws BusinessException List<TBasicExchangeRate> 返回类型
     * @Title: queryExeRateByCuyAbbe
     * @Description: 初始化自动添加币别
     * @date 2017年12月27日 下午5:23:22
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicExchangeRate> queryExeRateByCuyAbbe(String typeOfCurrency, HttpSession session)
            throws BusinessException {
        TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
        if (StringUtils.isNotBlank(typeOfCurrency)) {
            Map<String, Object> param = new HashMap<String, Object>();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());

            tBasicExchangeRate.setPkExchangeRateId(UUIDUtils.getUUID()); // 汇率主键

            tBasicExchangeRate.setUserId(user.getUserID()); // 用户ID
            tBasicExchangeRate.setAccountId(account.getAccountID()); //帐套id

            tBasicExchangeRate.setCurrencyAbbreviateName(typeOfCurrency);// 币别缩写

            String currencyRateNo = null;
            currencyRateNo = UUIDUtils.getUUID();
            tBasicExchangeRate.setCurrencyRateNo(currencyRateNo); //币别编号(外键)
            switch (typeOfCurrency) {
                case "USD":
                    tBasicExchangeRate.setCurrencyFullName("美元"); //币别全名
                    break;

                case "EUR":
                    tBasicExchangeRate.setCurrencyFullName("欧元"); //币别全名
                    break;

                case "JPY":
                    tBasicExchangeRate.setCurrencyFullName("日元"); //币别全名
                    break;

                case "HKD":
                    tBasicExchangeRate.setCurrencyFullName("港币"); //币别全名 简写为HK＄ 标准货币符号为HKD
                    break;

                case "GBP":
                    tBasicExchangeRate.setCurrencyFullName("英镑"); //币别全名
                    break;

                case "MYR":
                    tBasicExchangeRate.setCurrencyFullName("马来西亚林吉特"); //币别全名马来西亚林吉特 MYR
                    break;

                case "SUR":
                    tBasicExchangeRate.setCurrencyFullName("俄罗斯卢布"); //币别全名
                    break;

                case "KRW":
                    tBasicExchangeRate.setCurrencyFullName("韩国元"); //币别全名
                    break;

                case "MOP":
                    tBasicExchangeRate.setCurrencyFullName("澳门元"); //币别全名
                    break;

                case "TWD":
                    tBasicExchangeRate.setCurrencyFullName("新台币"); //新台币(New TaiWan Dollar)简写为(NT＄)，标准货币TWD
                    break;

                default:
                    tBasicExchangeRate.setCurrencyFullName("人民币"); //简写为RMB￥ 币别全名标 准货币符号为CNY
                    break;
            }

            //					tBasicExchangeRate.setInitExchangeRate(initExchangeRate); // 月初 外币汇率
            //					tBasicExchangeRate.setEndingExchangeRate(endingExchangeRate); // 月末 外币汇率
            tBasicExchangeRate.setExchangeRateRemarks("初始匹配导入");//备注
            tBasicExchangeRate.setCreatePsnId(account.getAccountID()); // 创建人ID
            //					tBasicExchangeRate.setCreatePsn(createPsn);//创建人名称
            tBasicExchangeRate.setCreateDate(new Date());//创建时间
            //					tBasicExchangeRate.setUpdatePsnId(updatePsnId); // 修改人ID
            //					tBasicExchangeRate.setUpdatePsn(updatePsn); // 修改人名称
            //					tBasicExchangeRate.setUpdateDate(updateDate); // 修改时间
            tBasicExchangeRate.setAccountDate(new Date());//做帐日期

            try {
                int result = tBasicExchangeRateMapper.queryExeRateByCuyAbbe(tBasicExchangeRate);
                if (result < 1) {
                    int results = tBasicExchangeRateMapper.insertExchangeRate(tBasicExchangeRate); //添加汇率信息
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> deleteExchangeRateAll(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息
            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String userId = user.getUserID();//用户id
            String accountId = account.getAccountID();//账套id
            tBasicExchangeRate.setAccountId(accountId);
            tBasicExchangeRate.setUserId(userId);
            int no = tBasicExchangeRateMapper.deleteExchangeRateAll(tBasicExchangeRate);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("maeeger", "TBasicExchangeRateServiceImpl.deleteExchangeRateAll删除失败");
        }
        return result;
    }

    @Override
    public List<TBasicExchangeRate> queryExchangeRate(HttpSession session) throws BusinessException {
        TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
        List<TBasicExchangeRate> queryExchangeRate = null;
        try {
            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userId = user.getUserID();//用户id
            tBasicExchangeRate.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountId = account.getAccountID();//账套id
            tBasicExchangeRate.setAccountId(accountId);
            queryExchangeRate = tBasicExchangeRateMapper.queryExchangeRate(tBasicExchangeRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryExchangeRate;
    }

    /**
     * @param session
     * @param pkExchangeRateId
     * @return Map<String, Object> 返回类型
     * @Title: delExchangeByExchangeId
     * @Description: 根据 汇率主键 删除汇率
     * @date 2018年1月22日 下午4:13:03
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Map<String, Object> delExchangeByExchangeId(HttpSession session, String pkExchangeRateId)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息
            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String userId = user.getUserID();//用户id
            String accountId = account.getAccountID();//账套id
            tBasicExchangeRate.setAccountId(accountId);
            tBasicExchangeRate.setUserId(userId);
            tBasicExchangeRate.setPkExchangeRateId(pkExchangeRateId);
            int no = tBasicExchangeRateMapper.delExchangeByExchangeId(tBasicExchangeRate);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("maeeger", "TBasicExchangeRateServiceImpl.deleteExchangeRateAll删除失败");
        }
        return result;
    }

    /**
     * @param tBasicExchangeRate
     * @param session
     * @return
     * @throws BusinessException
     * @Title: updateExchangeByStr
     * @Description: (非 JavaDoc)
     * @see com.wqb.service.exchange.TBasicExchangeRateService#updateExchangeByStr(com.wqb.model.TBasicExchangeRate,
     * javax.servlet.http.HttpSession)
     */
    @Override
    public Map<String, Object> updateExchangeByStr(TBasicExchangeRate tBasicExchangeRate, HttpSession session)
            throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();
        try {

            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());
            System.out.println("--------------" + timestamp);

            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userID = user.getUserID();//用户id

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountID = account.getAccountID();//账套id

            String pkExchangeRateId = UUIDUtils.getUUID();

            tBasicExchangeRate.setPkExchangeRateId(pkExchangeRateId);//主键id
            tBasicExchangeRate.setUserId(userID);//用户id
            tBasicExchangeRate.setAccountId(accountID);//帐套id
            tBasicExchangeRate.setCreatePsn(accountID);//创建人
            tBasicExchangeRate.setCreatePsnId(accountID);//创建人id
            tBasicExchangeRate.setCreateDate(date);//创建日期
            //		tBasicExchangeRate.setExchangeRateName(exchangeRateName);
            //		tBasicExchangeRate.setInitExchangeRate(initExchangeRate);
            //		tBasicExchangeRate.setExchangeRateRemarks(exchangeRateRemarks);
            //		tBasicExchangeRate.setEndingExchangeRate(endingExchangeRate);
            tBasicExchangeRate.setUpdatePsn(accountID);//修改人
            //		tBasicExchangeRate.setUpdatePsnId(accountID);//修改人id
            //		tBasicExchangeRate.setUpdateDate(date); //修改日期

            tBasicExchangeRate.setCurrencyRateNo(pkExchangeRateId);//币别编号(外键)
            int no = tBasicExchangeRateMapper.insertExchangeRate(tBasicExchangeRate);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> updateExchangeRate(HttpSession session, TBasicExchangeRate tBasicExchangeRate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            Date date = new Date();

            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userID = user.getUserID();//用户id

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountID = account.getAccountID();//账套id
            tBasicExchangeRate.setUserId(userID);//用户id
            tBasicExchangeRate.setAccountId(accountID);//帐套id
            tBasicExchangeRate.setUpdatePsn(accountID);//修改人
            tBasicExchangeRate.setUpdatePsnId(accountID);//修改人id
            tBasicExchangeRate.setUpdateDate(date); //修改日期
            tBasicExchangeRate.setAccountDate(new Date());

            int no = tBasicExchangeRateMapper.updateExchangeRate(tBasicExchangeRate);
            result.put("code", 1);
            result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * @param currencyAbbreviateName 币别简写
     * @return String    返回类型
     * @Title: newCurrencyFullName
     * @Description: 根据币别简写  币别全名
     * @date 2018年1月23日  下午5:27:18
     * @author SiLiuDong 司氏旭东
     */
    public static String newCurrencyFullName(String typeOfCurrency) {
        String currencyFullName = null;
        switch (typeOfCurrency) {
            case "USD":
                currencyFullName = "美元"; //币别全名
                break;

            case "EUR":
                currencyFullName = "欧元"; //币别全名
                break;

            case "JPY":
                currencyFullName = "日元"; //币别全名
                break;

            case "HKD":
                currencyFullName = "港币"; //币别全名 简写为HK＄ 标准货币符号为HKD
                break;

            case "GBP":
                currencyFullName = "英镑"; //币别全名
                break;

            case "MYR":
                currencyFullName = "马来西亚林吉特"; //币别全名马来西亚林吉特 MYR
                break;

            case "SUR":
                currencyFullName = "俄罗斯卢布"; //币别全名
                break;

            case "KRW":
                currencyFullName = "韩国元"; //币别全名
                break;

            case "MOP":
                currencyFullName = "澳门元"; //币别全名
                break;

            case "TWD":
                currencyFullName = "新台币"; //新台币(New TaiWan Dollar)简写为(NT＄)，标准货币TWD
                break;

            case "CNY":
                currencyFullName = "人民币"; //简写为RMB￥ 币别全名标 准货币符号为CNY
                break;

            case "RMB":
                currencyFullName = "人民币"; //简写为RMB￥ 币别全名标 准货币符号为CNY
                break;

            default:
                currencyFullName = "其它币别";
                break;
        }
        return currencyFullName;
    }

}
