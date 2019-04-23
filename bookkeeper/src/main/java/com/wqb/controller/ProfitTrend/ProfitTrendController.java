package com.wqb.controller.ProfitTrend;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.service.ProfitTrend.ProfitTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//查询利润趋势
@Controller
@RequestMapping("/profit")
public class ProfitTrendController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(ProfitTrendController.class);

    @Autowired
    ProfitTrendService profitTrendService;

    // 利润趋势
    @RequestMapping("/profitTrend")
    @ResponseBody
    Map<String, Object> queryProfitTrend() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("account_id", getAccount().getAccountID());
        map.put("account_period", getUserDate());
        //模拟数据
        try {
            Map<String, Map<String, Object>> res = profitTrendService.queryProfitTrend(map);
            result.put("code", "0");
            result.put("msg", res);
            return result;
        } catch (BusinessException e) {
            logger.error("ProfitTrendController【queryProfitTrend】 查询利润趋势失败!", e);
            result.put("code", "-1");
            result.put("msg", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ProfitTrendController【queryProfitTrend】 查询利润趋势失败!", e);
            result.put("code", "-1");
            result.put("msg", e.getMessage());
            return result;
        }
    }

    // 利润分析
    @RequestMapping("/profitAnalyze")
    @ResponseBody
    Map<String, Object> queryprofitAnalyze() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("account_id", getAccount().getAccountID());//账套id
        map.put("account_period", getUserDate());
        //模拟数据
        try {
            Map<String, Object> res = profitTrendService.queryprofitAnalyze(map);
            result.put("code", "0");
            result.put("msg", res);
            return result;
        } catch (BusinessException e) {
            logger.error("ProfitTrendController【queryProfitTrend】 查询利润趋势失败!", e);
            result.put("code", "-1");
            result.put("msg", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ProfitTrendController【queryProfitTrend】 查询利润趋势失败!", e);
            result.put("code", "-1");
            result.put("msg", e.getMessage());
            return result;
        }
    }


}
