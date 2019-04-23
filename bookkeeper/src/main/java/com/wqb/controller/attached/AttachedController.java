package com.wqb.controller.attached;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.service.attached.AttachedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//附加税结转
@Controller
@RequestMapping("/atta")
public class AttachedController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AttachedController.class);
    @Autowired
    AttachedService attachedService;

    @RequestMapping("/attaSettle")
    @ResponseBody
    Map<String, Object> attSettle() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> userInfo = getUserInfo();
        try {
            result = attachedService.fjsCarryover(userInfo);
            if (result == null) {
                result.put("result", "附加税结转结转异常");
                result.put("message", "fail");
            }
            return result;
        } catch (BusinessException e) {
            logger.error("AttachedController【settlement】附加税结转结转异常!", e);
            result.put("result", "附加税结转结转异常");
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AttachedController【settlement】 附加税结转结转异常!", e);
            result.put("result", "附加税结转结转异常");
            result.put("message", "fail");
            return result;
        }
    }

    public Map<String, Object> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        String userID = getUser().getUserID(); //用户id
        String userName = getUser().getUserName(); //用户姓名
        String accountID = getAccount().getAccountID(); //账套id
        String account_period = getUserDate(); //账套id
        map.put("accountID", accountID);//账套id
        map.put("userID", userID);
        map.put("userName", userName);
        map.put("period", account_period);
        return map;

    }


}
