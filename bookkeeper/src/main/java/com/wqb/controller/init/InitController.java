package com.wqb.controller.init;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.SubExcel;
import com.wqb.model.Subject;
import com.wqb.model.User;
import com.wqb.service.init.SljeInitService;
import com.wqb.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/init")
public class InitController extends BaseController {

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    SubjectService subjectService;
    @Autowired
    SljeInitService sljeInitService;
    private static Log4jLogger logger = Log4jLogger.getLogger(InitController.class);

    @RequestMapping("/toInit")
    @ResponseBody
    Map<String, Object> init() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = (User) getSession().getAttribute("user");
            Account account = (Account) getSession().getAttribute("account");
            // 获取系统科目
            List<Subject> sysList = subjectService.querySysSub(user.getUserID(), account.getAccountID());
            // 获取EXCEL导入的科目
            List<SubExcel> excelList = subjectService.queryExcelSub(user.getUserID(), account.getAccountID());
            double totalDebit = 0.0;
            double totalCredit = 0.0;
            for (int i = 0; i < sysList.size(); i++) {
                Subject sysSub = sysList.get(i);
                totalDebit += sysSub.getTotalDebit();
                totalCredit += sysSub.getTotalCredit();
            }
            List<Subject> matchList = getMatchList(sysList, excelList);
            result.put("message", "success");//
            result.put("qcSubCount", sysList.size());// 期初科目数
            result.put("matchList", matchList);// 匹配到的科目
            result.put("size", matchList.size());// 匹配到的科目数
            result.put("totalDebit", totalDebit);// 期初借方金额
            result.put("totalCredit", totalCredit);// 期初贷方金额
        } catch (BusinessException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * 获取匹配的科目
     *
     * @param subList
     * @param subdzList
     * @return
     */
    private List<Subject> getMatchList(List<Subject> sysList, List<SubExcel> excelList) {
        List<Subject> resultList = new ArrayList<Subject>();
        for (int i = 0; i < sysList.size(); i++) {
            Subject sysSub = sysList.get(i);
            String sysSuNumber = sysSub.getSunumber();
            String sysSuName = sysSub.getSuname();
            for (int j = 0; j < excelList.size(); j++) {
                SubExcel subExcel = excelList.get(j);
                String excelSuNumber = subExcel.getSunumber();
                String excelSuName = subExcel.getSuname();
                if (sysSuNumber.equals(excelSuNumber) && sysSuName.equals(excelSuName)) {
                    resultList.add(sysSub);
                } else {
                    continue;
                }
            }
        }
        return resultList;
    }

    // 试算平衡
    @RequestMapping("/balance")
    @ResponseBody
    Map<String, Object> balance() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = (User) getSession().getAttribute("user");
            Account account = (Account) getSession().getAttribute("account");
            // 获取系统科目
            List<Subject> sysList = subjectService.querySysSub(user.getUserID(), account.getAccountID());
            // 获取EXCEL导入的科目
            List<SubExcel> excelList = subjectService.queryExcelSub(user.getUserID(), account.getAccountID());
            double sysTotalInitJf = 0.0;
            double sysTotalInitDf = 0.0;
            for (int i = 0; i < sysList.size(); i++) {
                Subject sysSub = sysList.get(i);
                double sysInitJfBalance = sysSub.getInitJFBalance();
                double sysInitDfBalace = sysSub.getInitDFBalance();
                sysTotalInitJf += sysInitJfBalance;
                sysTotalInitDf += sysInitDfBalace;
            }

            double excelTotalInitJf = 0.0;
            double excelTotalInitDf = 0.0;
            for (int j = 0; j < excelList.size(); j++) {
                SubExcel excelSub = excelList.get(j);
                double excelInitJfBalance = excelSub.getInitJFBalance();
                double excelInitDfBalace = excelSub.getInitDFBalance();
                excelTotalInitJf += excelInitJfBalance;
                excelTotalInitDf += excelInitDfBalace;
            }
            result.put("sysTotalInitJf", sysTotalInitJf);// 期初借方合计
            result.put("sysTotalInitDf", sysTotalInitDf);// 期初贷方合计
            result.put("sysDiffer", sysTotalInitJf - sysTotalInitDf);// 期初差额
            // 倒入科目
            result.put("excelTotalInitJf", excelTotalInitJf);// 期初借方合计
            result.put("excelTotalInitDf", excelTotalInitDf);// 期初贷方合计
            result.put("excelDiffer", excelTotalInitJf - excelTotalInitDf);// 期初差额
            // 差异
            result.put("sysExcelJfDiff", sysTotalInitJf - excelTotalInitJf);
            result.put("sysExcelDfDiff", sysTotalInitDf - excelTotalInitDf);
            result.put("initDiff", sysTotalInitJf - sysTotalInitDf - (excelTotalInitJf - excelTotalInitDf));
        } catch (BusinessException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    // 科目对照
    @RequestMapping("/toKmdz")
    @ResponseBody
    Map<String, Object> toKmdz() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = (User) getSession().getAttribute("user");
            Account account = (Account) getSession().getAttribute("account");
            // 获取系统科目
            List<Subject> sysList = subjectService.querySysSub(user.getUserID(), account.getAccountID());
            // 获取EXCEL导入的科目
            List<SubExcel> excelList = subjectService.queryExcelSub(user.getUserID(), account.getAccountID());
            result.put("message", "success");
            result.put("sysList", sysList);
            result.put("excelList", excelList);
        } catch (BusinessException e) {
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    // 数量金额初始化
    @RequestMapping(value = "/sljeInit")
    @ResponseBody
    public String upload(@RequestParam(required = false) MultipartFile file, HttpServletRequest request,
                         ModelMap model) {
        List<Map<String, Object>> list = null;
        try {
            if (file != null && !file.isEmpty()) {

                //获取用户信息
                HttpSession session = getSession();
                Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
                User user = (User) sessionMap.get("user"); //获取user信息
                String userID = user.getUserID();//用户id
                Account account = (Account) sessionMap.get("account"); //获取帐套信息
                String busDate = (String) sessionMap.get("busDate");
                String accountID = account.getAccountID();//账套id

//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE;
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath,
                        "sljeInit-" + getSession().getAttribute("user") + "-" + System.currentTimeMillis());
                list = ReadExcal.readExcel(filePath, fileName, 1, 0);
            }
            sljeInitService.slje2Date(list, getSession());
        } catch (BusinessException e) {
            logger.error("数量金额初始化错误", e);
            return "fail";
        } catch (Exception e) {
            logger.error("数量金额初始化错误", e);
            return "fail";
        }
        if (null == list || list.size() == 0) {
            return "fail";
        }
        return "success";
    }

    // 下载模板excel
    @RequestMapping(value = "/downSljeExcel")
    public void downJxExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "数量金额式总账(初始化).xls",
                "数量金额式-" + DateUtil.getDays() + ".xls");
    }
}
