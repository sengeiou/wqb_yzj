package com.wqb.service.measure.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.measure.TBasicMeasureMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicMeasure;
import com.wqb.model.User;
import com.wqb.service.measure.TBasicMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TBasicMeasureServiceImpl
 * @Description: 数量单位业务层实现
 * @date 2017年12月20日 上午10:04:21
 */
@Component
@Service("tBasicMeasureService")
public class TBasicMeasureServiceImpl implements TBasicMeasureService {
    @Autowired
    TBasicMeasureMapper tBasicMeasureMapper;

    @Override
    public Map<String, Object> deleteByPrimaryKey(Map<String, Object> param, HttpSession session)
            throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();
        //获取用户信息
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user"); //获取user信息

        String userID = user.getUserID();//用户id
        param.put("userID", userID);

        Account account = (Account) sessionMap.get("account"); //获取帐套信息
        String accountID = account.getAccountID();//账套id
        param.put("accountID", accountID);

        String pkMeasureId = param.get("pkMeasureId") == null ? null : param.get("pkMeasureId").toString();
        try {

            if (StringUtil.isEmptyWithTrim(userID) || StringUtil.isEmptyWithTrim(accountID)) {
                result.put("message", "fail");
                return result;
            } else if (null != pkMeasureId) {
                int nub = tBasicMeasureMapper.deleteByPrimaryKey(pkMeasureId);
            }
            result.put("message", "数量单位上传成功");
            return result;
        } catch (Exception e) {
            result.put("message", "数量单位上传失败");
            return result;
        }
    }

    @Override
    public Map<String, Object> queryMeasure(HttpSession session) throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();
        TBasicMeasure tBasicMeasure = new TBasicMeasure();
        List<TBasicMeasure> queryExchangeRate = null;
        try {
            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userId = user.getUserID();//用户id
            tBasicMeasure.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountId = account.getAccountID();//账套id
            tBasicMeasure.setAccountId(accountId);
            queryExchangeRate = tBasicMeasureMapper.queryMeasure(tBasicMeasure);
            result.put("code", 1);
            result.put("queryExchangeRate", queryExchangeRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int insertMeasure(TBasicMeasure tBasicMeasure, HttpSession session) throws BusinessException {
        int result = 0;
        Date date = new Date();
        //获取用户信息
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        User user = (User) sessionMap.get("user"); //获取user信息
        String userID = user.getUserID();//用户id
        Account account = (Account) sessionMap.get("account"); //获取帐套信息
        String accountID = account.getAccountID();//账套id
        tBasicMeasure.setUserId(userID);//用户id
        tBasicMeasure.setAccountId(accountID);//帐套id
        String uuid = UUIDUtils.getUUID();
        String setPkMeasureId = uuid;
        tBasicMeasure.setPkMeasureId(setPkMeasureId);//数量单位主键
        tBasicMeasure.setCreatePsn(accountID);//创建人
        tBasicMeasure.setCreatePsnId(accountID);//创建人id
        tBasicMeasure.setCreateDate(date);//创建日期

        result = tBasicMeasureMapper.insertMeasure(tBasicMeasure);
        return result;
    }

    @Override
    public Map<String, Object> updateMessage(HttpSession session, TBasicMeasure tBasicMeasure) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息
            String busDate = (String) sessionMap.get("busDate");
            //用户id
            String userId = user.getUserID();
            tBasicMeasure.setUserId(userId);
            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountId = account.getAccountID();//账套id
            tBasicMeasure.setAccountId(accountId);
            tBasicMeasure.setUpdateDate(new Date());
            tBasicMeasure.setUpdatePsn(userId);
            tBasicMeasure.setUpdatePsnId(userId);
            int no = tBasicMeasureMapper.updateMeasure(tBasicMeasure);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> queryMeasureBySymbolOrName(HttpSession session, String symbolOrName) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicMeasure tBasicMeasure = new TBasicMeasure();
        List<TBasicMeasure> queryExchangeList = new ArrayList<TBasicMeasure>();
        try {
            //获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); //获取user信息

            String userId = user.getUserID();//用户id
            tBasicMeasure.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountId = account.getAccountID();//账套id
            tBasicMeasure.setAccountId(accountId);
            tBasicMeasure.setMeasUnitName(symbolOrName);
            tBasicMeasure.setMeasUnitSymbol(symbolOrName);
            queryExchangeList = tBasicMeasureMapper.queryMeasureBySymbolOrName(tBasicMeasure);
            result.put("code", 1);
            result.put("queryExchangeList", queryExchangeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
