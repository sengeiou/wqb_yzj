package com.wqb.controller.report;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectMessage;
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
 * @ClassName: Ledger
 * @Description: 总帐
 * @date 2018年1月18日 上午10:28:30
 */
@Component
@Controller
@RequestMapping("/ledger")
public class LedgerController extends BaseController {
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    private UserService userService;

    /**
     * @return Map<String, Object> 返回类型
     * @Title: queryLedger
     * @Description: 查询全部总账
     * @date 2018年1月18日 上午10:31:07
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryLedger")
    public Map<String, Object> queryLedger() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> querySubMessage = new HashMap<String, Object>();
        ;
        try {
            HttpSession session = getSession();
            querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return querySubMessage;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: queryLedgerByParameters
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @date 2018年1月18日 上午11:17:13
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryLedgerByParameters")
    @ResponseBody
    public Map<String, Object> queryLedgerByParameters(String endTime, String startTime, String subMessage) {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, String> parameters = new HashMap<String, String>();
        try {
            HttpSession session = getSession();
            if (StringUtils.isNotBlank(endTime)) {
                parameters.put("endTime", endTime);
            }
            if (StringUtils.isNotBlank(startTime)) {
                parameters.put("startTime", startTime);
            }
            if (StringUtils.isNotBlank(subMessage)) {
                parameters.put("subMessage", subMessage);
            }

            Map<String, Object> querySubMessage = tBasicSubjectMessageService.queryLedgerByParameters(user, account, parameters);
            result.putAll(querySubMessage);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/queryLedgerByParameters2")
    @ResponseBody
    public Map<String, Object> queryLedgerByParameters2(TBasicSubjectMessage params) {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, String> parameters = new HashMap<String, String>();
        try {
            HttpSession session = getSession();
            Map<String, Object> querySubMessage =
                    tBasicSubjectMessageService.queryLedgerByParameters(user, account, parameters);
            result.putAll(querySubMessage);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }


    // 这是总账APP接口
    @RequestMapping("/queryLedgerAccountAPP")
    @ResponseBody
    public Map<String, Object> queryDetailAccountAPP(
            @RequestParam(value = "period", required = true) String period,
            @RequestParam(value = "accountID", required = true) String accountID,
            @RequestParam(value = "keyWord", required = false) String keyWord) {

		/*accountID = "e9f46fb6f8b845239aebc34889d1eab8";
		period = "2018-04";*/


        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("period", period);
            param.put("accountID", accountID);
            param.put("keyWord", keyWord);

            Map<String, Object> sheetMap = tBasicSubjectMessageService.queryDetailAccountAPP(param);
            if (sheetMap != null && sheetMap.size() > 0) {
                result.put("code", 0);
                result.put("info", 0);
                result.put("type", "zz");
                result.put("msg", sheetMap.get("sub"));
            } else {
                result.put("code", 0);
                result.put("type", "zz");
                result.put("info", -1);
                result.put("msg", "未查询到数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("type", "zz");
            result.put("msg", "获取数据异常");
        }
        return result;
    }


}
