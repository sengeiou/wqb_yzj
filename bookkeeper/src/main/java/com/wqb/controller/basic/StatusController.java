package com.wqb.controller.basic;

import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.model.StatusPeriod;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/status")
public class StatusController extends BaseController {
    @Autowired
    PeriodStatusService periodStatusService;
    private static Log4jLogger logger = Log4jLogger.getLogger(LoginController.class);

    @RequestMapping("/queryStatus")
    @ResponseBody
    Map<String, Object> queryStatus() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("busDate", getUserDate());
            List<StatusPeriod> list = periodStatusService.queryStatus(param);
            if (null != list && list.size() > 0) {
                result.put("success", "true");
                result.put("status", list.get(0));
            } else {
                result.put("success", "fail");
            }
            return result;
        } catch (Exception e) {
            logger.error("获取操作状态异常", e);
            result.put("success", "fail");
            return result;
        }
    }
}
