package com.wqb.controller.admin;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.service.admin.AdminService;
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
 * @ClassName: AdminController
 * @Description: 管理员首页
 * @date 2018年7月24日 上午10:46:13
 */
@Component
@Controller
@RequestMapping("/adminController")
public class AdminController extends BaseController {
    public static Log4jLogger logger = Log4jLogger.getLogger(AdminController.class);

    @Autowired
    AdminService adminService;

    /**
     * @return Map<String, Object>    返回类型
     * @Title: accountCount
     * @Description: 做帐统计
     * @date 2018年7月24日  上午10:50:16
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/accountCount")
    @ResponseBody
    public Map<String, Object> accountCount() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            result = adminService.accountCount(session);
        } catch (BusinessException e) {
            logger.error("AdminController【accountCount】 获取做帐统计失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        } catch (Exception e) {
            logger.error("AdminController【accountCount】 获取做帐统计失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        }
        logger.info("/accountCount : 返回参数是" + result);
        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: corpOverview
     * @Description: 企业总览
     * @date 2018年7月24日  上午10:56:48
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/corpOverview")
    @ResponseBody
    public Map<String, Object> corpOverview() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            result = adminService.corpOverview(session);
        } catch (BusinessException e) {
            logger.error("AdminController【corpOverview】 获取企业总览 失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        } catch (Exception e) {
            logger.error("AdminController【corpOverview】 获取企业总览 失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        }
        logger.info("/corpOverview : 返回参数是" + result);
        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: acctList
     * @Description: 做帐列表
     * @date 2018年7月24日  上午10:59:37
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/acctList")
    @ResponseBody
    public Map<String, Object> acctList() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            result = adminService.acctList(session);
        } catch (BusinessException e) {
            logger.error("AdminController【acctList】 获取做帐列表失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        } catch (Exception e) {
            logger.error("AdminController【acctList】 获取做帐列表失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        }
        logger.info("/acctList : 返回参数是" + result);
        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: StaffCorpScale
     * @Description: 各财务人员负责企业比例图
     * @date 2018年7月24日  上午11:02:57
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/staffCorpScale")
    @ResponseBody
    public Map<String, Object> staffCorpScale() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
//			result = adminService.staffCorpScale(session);
            result = adminService.staffCorpScaleNew(session);
        } catch (BusinessException e) {
            logger.error("AdminController【staffCorpScale】 获取各财务人员负责企业比例图失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        } catch (Exception e) {
            logger.error("AdminController【staffCorpScale】 获取各财务人员负责企业比例图失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        }
        logger.info("/staffCorpScale : 返回参数是" + result);
        return result;
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: effectiveCorpTrend
     * @Description: 有效企业趋势图
     * @date 2018年7月24日  上午11:21:25
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/effectiveCorpTrend")
    @ResponseBody
    public Map<String, Object> effectiveCorpTrend() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
//			result = adminService.effectiveCorpTrend(session);
            result = adminService.effectiveCorpTrendNew(session);
        } catch (BusinessException e) {
            logger.error("AdminController【effectiveCorpTrend】 获取有效企业趋势图失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        } catch (Exception e) {
            logger.error("AdminController【effectiveCorpTrend】 获取有效企业趋势图失败！", e);
            result.put("code", -1);
            result.put("msg", " 获取做账统计数量失败！");
            return result;
        }
        logger.info("/effectiveCorpTrend : 返回参数是" + result);
        return result;
    }
}
