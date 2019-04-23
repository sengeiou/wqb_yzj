package com.wqb.controller.bank;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.service.bank.TCmBkbillBocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TCmBkbillBocController
 * @Description: 中国银行控制层
 * @date 2017年12月29日 下午2:32:22
 */
@Component
@Controller
@RequestMapping("/bkbillBoc")
public class TCmBkbillBocController extends BaseController {
    @Autowired
    TCmBkbillBocService tCmBkbillBocService;

    /**
     * @param updateBkillBocFile
     * @return Map<String, Object>    返回类型
     * @Title: uploadTCmBkbillBoc
     * @Description: 上传中国银行excel
     * @date 2018年1月2日  下午5:00:20
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/uploadTCmBkbillBoc")
    @ResponseBody
    public Map<String, Object> uploadTCmBkbillBoc(MultipartFile updateBkillBocFile) {
        HttpSession session = getSession();
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("code", -1);
        try {
            Map<String, Object> param = tCmBkbillBocService.uploadTCmBkbillBoc(updateBkillBocFile, session);
            results.put("param", param);
        } catch (Exception e) {
            results.put("message", "TCmBkbillBocController.updateTCmBkbillBoc上传出错");
        }
        return results;
    }

    @RequestMapping("/queryTCmBkbillBocList")
    @ResponseBody
    public Map<String, Object> queryTCmBkbillBocList() {
        HttpSession session = getSession();
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("code", -1);
        try {
            results = tCmBkbillBocService.queryTCmBkbillBocList(session);
            results.put("code", 1);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            results.put("message", "queryTCmBkbillBoc查询出错");
        }
        return results;
    }

    @RequestMapping("/deleteTCmBkbillBocAll")
    @ResponseBody
    public Map<String, Object> deleteTCmBkbillBocAll() {
        HttpSession session = getSession();
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("code", -1);
        try {
            results = tCmBkbillBocService.deleteTCmbkbillBocAll(session);
            results.put("code", 1);

        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @RequestMapping("/deleteTCmBkbillBocByBkbillBoc")
    @ResponseBody
    public Map<String, Object> deleteTCmBkbillBocByPkBkbillBoc(String pkBkbillBoc) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("code", -1);
        HttpSession session = getSession();
        try {
            results = tCmBkbillBocService.deleteTCmBkbillBocByBkbillBoc(session, pkBkbillBoc);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return results;
    }
}
