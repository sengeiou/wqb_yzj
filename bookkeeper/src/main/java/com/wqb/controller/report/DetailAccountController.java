package com.wqb.controller.report;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: DetailAccount
 * @Description: 明细账
 * @date 2018年1月20日 下午3:43:59
 */
@Component
@Controller
@RequestMapping("/detailAccount")
public class DetailAccountController extends BaseController {
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    private UserService userService;

    /**
     * @param endTime
     * @param startTime
     * @param subMessage
     * @return Map<String, Object> 返回类型
     * @Title: queryDetailAccount
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @date 2018年1月20日 下午4:24:36
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryDetailAccount")
    @ResponseBody
    public Map<String, Object> queryDetailAccount(String endTime, String startTime, String subMessage) {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> querySubMessage = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(endTime)) {
                parameters.put("endTime", endTime);
            }
            if (StringUtils.isNotBlank(startTime)) {
                parameters.put("startTime", startTime);
            }
            if (StringUtils.isNotBlank(subMessage)) {
                parameters.put("subMessage", subMessage);
            }

            HttpSession session = getSession();
            querySubMessage = tBasicSubjectMessageService.queryLedgerByParameters(user, account, parameters);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return querySubMessage;
    }

    // 这是明细账APP接口
    @RequestMapping("/queryDetailAccountAPP")
    @ResponseBody
    public Map<String, Object> queryDetailAccountAPP(
            @RequestParam(value = "period", required = true) String period,
            @RequestParam(value = "accountID", required = true) String accountID,
            @RequestParam(value = "keyWord", required = false) String keyWord) {

	/*	accountID = "e9f46fb6f8b845239aebc34889d1eab8";
		period = "2018-04";*/
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("period", period);
            param.put("accountID", accountID);
            param.put("keyWord", keyWord);

            Map<String, Object> sheetMap = tBasicSubjectMessageService.queryDetailAccountAPP(param);
            if (sheetMap != null && sheetMap.size() > 0) {
                result.put("code", 0);
                result.put("info", 0);
                result.put("type", "mx");
                result.put("msg", sheetMap.get("sub"));
                return result;
            } else {
                result.put("code", 0);
                result.put("info", -1);
                result.put("type", "mx");
                result.put("msg", "未查询到数据");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取数据异常");
            return result;
        }

    }
}
