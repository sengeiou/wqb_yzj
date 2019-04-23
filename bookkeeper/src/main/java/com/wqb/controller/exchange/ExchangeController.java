package com.wqb.controller.exchange;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.model.TBasicExchangeRate;
import com.wqb.service.exchange.TBasicExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: ExchangeController
 * @Description: 汇率信息
 * @date 2018年1月4日 下午4:55:47
 */
@Component
@Controller
@RequestMapping("/exchange")
public class ExchangeController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(ExchangeController.class);

    @Autowired
    TBasicExchangeRateService tBasicExchangeRateService;

    /**
     * @param tBasicExchangeRate
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: addExchange
     * @Description: 添加汇率
     * @date 2017年12月19日 下午5:41:00
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addExchange")
    @ResponseBody
    public Map<String, Object> addExchange(TBasicExchangeRate tBasicExchangeRate) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            Map<String, Object> insertExchangeRate = tBasicExchangeRateService.insertExchangeRate(tBasicExchangeRate, session);
            result.put("insertExchangeRate", insertExchangeRate);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @param exchangeRateRemarks    备注
     * @param currencyAbbreviateName 币别简写
     * @param currencyFullName       币别全名
     * @param endingCurrencyRate     '月末 外币汇率'
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: addExchangeByStr
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2018年1月22日 上午11:15:41
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addExchangeByStr")
    @ResponseBody
    public Map<String, Object> addExchangeByStr(String exchangeRateRemarks, String currencyAbbreviateName,
                                                String currencyFullName, String endingCurrencyRate) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
            tBasicExchangeRate.setCurrencyAbbreviateName(currencyAbbreviateName);
            BigDecimal bigDecimal = new BigDecimal(endingCurrencyRate);
            tBasicExchangeRate.setEndingCurrencyRate(bigDecimal);
            tBasicExchangeRate.setCurrencyFullName(currencyFullName);
            tBasicExchangeRate.setExchangeRateRemarks(exchangeRateRemarks);
            HttpSession session = getSession();
            Map<String, Object> insertExchangeRate =
                    tBasicExchangeRateService.insertExchangeRate(tBasicExchangeRate, session);
            result.putAll(insertExchangeRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: deleteExchangeRateAll
     * @Description: 删除此用户汇率信息
     * @date 2018年1月4日 下午5:17:38
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteExchangeRateAll")
    @ResponseBody
    public Map<String, Object> deleteExchangeRateAll() throws BusinessException {
        HttpSession session = getSession();
        Map<String, Object> deleteExchangeRateAll = tBasicExchangeRateService.deleteExchangeRateAll(session);
        return deleteExchangeRateAll;
    }

    /**
     * @param pkExchangeRateId
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: delExchangeByExchangeId
     * @Description: 根据 汇率主键 删除汇率
     * @date 2018年1月22日 下午4:12:30
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/delExchangeByExchangeId")
    @ResponseBody
    public Map<String, Object> delExchangeByExchangeId(String pkExchangeRateId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            Map<String, Object> deleteExchangeRateAll =
                    tBasicExchangeRateService.delExchangeByExchangeId(session, pkExchangeRateId);
            result.putAll(deleteExchangeRateAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param queryExchangeRate
     * @return Map<String, Object> 返回类型
     * @Title: queryExchange
     * @Description: 查询汇率
     * @date 2017年12月19日 上午11:59:32
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryExchangeRate")
    @ResponseBody
    Map<String, Object> queryExchangeRate() {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<TBasicExchangeRate> Exchange = tBasicExchangeRateService.queryExchangeRate(session);
            result.put("Exchange", Exchange);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TExchangeService【deleteExchange】 计量单位删除出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TExchangeService【deleteExchange】,计量单位删除出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;

    }

    /**
     * @param pkExchangeId
     * @return Map<String, Object> 返回类型
     * @Title: deleteExchange
     * @Description: 删除汇率
     * @date 2017年12月18日 下午3:51:17
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteByPrimaryKey")
    @ResponseBody
    Map<String, Object> deleteByPrimayKey(String pkExchangeId) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pkExchangeId", pkExchangeId);
        try {
            tBasicExchangeRateService.deleteByPrimaryKey(param, session);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TExchangeService【deleteExchange】 计量单位删除出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TExchangeService【deleteExchange】,计量单位删除出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @param tBasicExchangeRate
     * @return Map<String, Object> 返回类型
     * @Title: updateExchange
     * @Description: 更新汇率
     * @date 2017年12月19日 上午11:59:51
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateExchange")
    @ResponseBody
    Map<String, Object> updateExchange(TBasicExchangeRate tBasicExchangeRate) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            Map<String, Object> updateExchangeRate = tBasicExchangeRateService.updateExchangeRate(session, tBasicExchangeRate);
            result.putAll(updateExchangeRate);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param pkExchangeRateId    汇率主键
     * @param exchangeRateRemarks 备注
     * @param endingCurrencyRate  月末 外币汇率
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: updateExchangeByStr
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2018年1月22日 下午6:23:11
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateExchangeByStr")
    @ResponseBody
    public Map<String, Object> updateExchangeByStr(String pkExchangeRateId, String exchangeRateRemarks,
                                                   String endingCurrencyRate) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            TBasicExchangeRate tBasicExchangeRate = new TBasicExchangeRate();
            tBasicExchangeRate.setPkExchangeRateId(pkExchangeRateId);
            BigDecimal bigDecimal = new BigDecimal(endingCurrencyRate);
            tBasicExchangeRate.setEndingCurrencyRate(bigDecimal);
            tBasicExchangeRate.setExchangeRateRemarks(exchangeRateRemarks);
            HttpSession session = getSession();
            Map<String, Object> updateExchangeByStr =
                    tBasicExchangeRateService.updateExchangeByStr(tBasicExchangeRate, session);
            result.putAll(updateExchangeByStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
