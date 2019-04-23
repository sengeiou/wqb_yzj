package com.wqb.controller.report;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.report.TBasicIncomeStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: IncomeStatementController
 * @Description: 利润表控制层
 * @date 2018年2月26日 上午11:15:15
 */
@Controller
@RequestMapping("/IncomeStatement")
public class TBasicIncomeStatementController extends BaseController {
    @Autowired
    TBasicIncomeStatementService tBasicIncomeStatementService;

    @Autowired
    private UserService userService;

    @RequestMapping("/addIncomeStatement")
    @ResponseBody
    //2 利润表查询 计算利润表数据并添加大我科目余额表
    public Map<String, Object> addIncomeStatement() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Map<String, Object> addIncomeStatement = tBasicIncomeStatementService.addIncomeStatement(user, account);
            result.putAll(addIncomeStatement);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    //账簿查询 利润表
    @SuppressWarnings("unused")
    @RequestMapping("queryIncomeStatrment")
    @ResponseBody
    public Map<String, Object> queryIncomeStatrment() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();

        try {
            Map<String, Object> deleteIncomeStatrment = deleteIncomeStatrment(); //1 先删除旧的利润表数据
            Map<String, Object> addIncomeStatement = addIncomeStatement();  //2 根据科目余额表重新计算利润表 并 添加到数据库
            Map<String, Object> queryIncomeStatrmentMap = tBasicIncomeStatementService.queryIncomeStatrment(user, account);
            result.putAll(queryIncomeStatrmentMap);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping("/deleteIncomeStatrment")
    @ResponseBody
    public Map<String, Object> deleteIncomeStatrment() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();

        try {
            Map<String, Object> deleteIncomeStatrment = tBasicIncomeStatementService.deleteIncomeStatrment(user, account);
            result.putAll(deleteIncomeStatrment);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        return result;

    }

    //这是利润表的APP接口
    @RequestMapping("/incomeStatrmentAPP")
    @ResponseBody
    public Map<String, Object> queryIncomeStatrmentAPP(
            @RequestParam(value = "period", required = true) String period,
            @RequestParam(value = "accountID", required = true) String accountID) {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        param.put("period", period);
        param.put("accountID", accountID);
        try {
            Map<String, Object> sheetMap = tBasicIncomeStatementService.queryIncomeStatrmentAPP(param);
            if (sheetMap != null && sheetMap.size() > 0) {
                result.put("code", 0);
                result.put("info", 0);
                result.put("msg", sheetMap);
            } else {
                result.put("code", 0);
                result.put("info", -1);
                result.put("msg", "未查询到数据");
            }
        } catch (BusinessException e) {
            result.put("code", -1);
            result.put("msg", "获取数据异常");
        }

        return result;

    }


}
