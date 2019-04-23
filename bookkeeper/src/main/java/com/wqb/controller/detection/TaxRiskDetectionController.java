package com.wqb.controller.detection;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.detection.TaxRiskDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TaxRiskDetectionConttroller
 * @Description: 税务风险检测
 * @date 2018年5月10日 上午10:23:49
 */
@Component
@Controller
@RequestMapping("/detection")
public class TaxRiskDetectionController extends BaseController {
    @Autowired
    TaxRiskDetectionService taxRiskDetectionService;

    @Autowired
    private UserService userService;

    /**
     * @return Map<String, Object>    返回类型
     * @Title: queryDetection
     * @Description: 税务风险检测
     * @date 2018年5月10日  上午10:44:40
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryDetection")
    @ResponseBody
    public Map<String, Object> queryDetection() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);

        try {
            Map<String, Object> queryDetection = taxRiskDetectionService.queryDetection(user, account);
            result.putAll(queryDetection);
        } catch (BusinessException e) {
            result.put("msg", e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: updateDetectionZero
     * @Description: 更新检查状态  '风险检测 是否检测通过 0:未通过 1:通过',
     * @date 2018年5月10日  下午4:17:24
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateDetectionZero")
    @ResponseBody
    public Map<String, Object> updateDetectionZero() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            Map<String, Object> updateDetection = taxRiskDetectionService.updateDetection(user, account, 0);
            result.putAll(updateDetection);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: updateDetectionOne
     * @Description: 更新检查状态  '风险检测 是否检测通过 0:未通过 1:通过',
     * @date 2018年5月10日  下午4:17:24
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateDetectionOne")
    @ResponseBody
    public Map<String, Object> updateDetectionOne() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            Map<String, Object> updateDetection = taxRiskDetectionService.updateDetection(user, account, 1);
            result.putAll(updateDetection);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
