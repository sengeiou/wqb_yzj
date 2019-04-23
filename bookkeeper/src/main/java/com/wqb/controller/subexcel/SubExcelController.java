package com.wqb.controller.subexcel;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectExcel;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.service.exchange.TBasicExchangeRateService;
import com.wqb.service.subexcel.TBasicSubjectExcelService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.subject.TBasicSubjectMappingMiddleService;
import com.wqb.service.subject.TBasicSubjectParentService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: SubExcelController
 * @Description: 科目余额表excel导入Controller
 * @date 2017年12月25日 下午5:32:25
 */
@Component
@Controller
@RequestMapping("/subexcel")
public class SubExcelController extends BaseController {
    @Value("${filePaths}")
    private String filePaths;

    private static Log4jLogger logger = Log4jLogger.getLogger(SubExcelController.class);

    @Autowired
    /** EXCEL导入的科目 */
            TBasicSubjectExcelService subExcelService;

    @Autowired
    /** 新会计准则基础科目 */
            TBasicSubjectParentService tBasicSubjectParentService;

    @Autowired
    /** 系统科目 */
            TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    /** 汇率表 */
            TBasicExchangeRateService tBasicExchangeRateService;

    @Autowired
    /** 凭证头 */
            VoucherHeadService voucherHeadService;

    @Autowired
    /** 科目映射表 */
            TBasicSubjectMappingMiddleService tBasicSubjectMappingMiddleService;

    /**
     * @param uploadSubExcel
     * @return Map<String, Object> 返回类型
     * @Title: uploadSubExcel
     * @Description: 科目初始化excel表上传
     * @date 2017年12月7日 上午9:49:29
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/uploadSubExcel")
    @ResponseBody
    Map<String, Object> uploadSubExcel(MultipartFile uploadSubExcel) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        // 获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) getRequest().getSession().getAttribute("userDate");
        User user = (User) userDate.get("user");
        Account account = (Account) userDate.get("account");
        String busDate = (String) userDate.get("busDate");
        // 用户id
        String userID = user.getUserID();
        // 账套id
        String accountID = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userID) || StringUtil.isEmptyWithTrim(accountID)) {
            result.put("msg", "账套id为空，请检查是否登录");
            return result;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userID", userID);
        map.put("accountID", accountID);
        map.put("userName", user.getUserName());

        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);

        try {
            int queryCountVouch2 = voucherHeadService.queryCountVouch2(param);
            if (queryCountVouch2 > 0) {
                result.put("msg", "此帐套已有凭证生成，不允许再次初始化科目");
                return result;
            }

            if (null != uploadSubExcel && !uploadSubExcel.isEmpty()) {
                // 1获取文件上传路径
                // String filePath = PathUtil.getClasspath() +
                // Constrants.FILEPATHFILE;
                // //wtpwebapps/wqb/WEB-INF/classes/../../files/
                String filePath = filePaths + "/" + user.getUserID() + "/" + accountID + "/" + busDate + "/"; // wtpwebapps/wqb/WEB-INF/classes/../../files/
                // 2获取文件名
                String fileName = FileUpload.fileUp(uploadSubExcel, filePath,
                        "SubExcelInit-" + user.getUserID() + "-" + System.currentTimeMillis());
                // 3读取excel表格数据
                List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                File file = new File(filePath, fileName);
                // 期间
                if (list != null && list.size() > 0 && list.get(0).get("map1").equals("1001")) {
                    // 删除最后一行合计 防止导入到数据中
                    Map<String, Object> map2 = list.get(list.size() - 1);
                    String stringhj = (String) map2.get("map2");
                    if (stringhj.replace(" ", "").equals("合计")) {
                        list.remove(list.size() - 1);
                    }

                    // 调用删除清空此用户导入的科目信息
                    // tBasicSubjectMessageService.deleteMessageAll(session); //
                    // 删除 系统科目中的数据
                    tBasicSubjectMessageService.deleteMessageByAcctperiod(session); // 删除
                    // 系统科目中当前期间的数据
                    subExcelService.deleteSubExcelAll(session); // 删除 Excel
                    // 导入的科目
                    tBasicExchangeRateService.deleteExchangeRateAll(session); // 删除
                    // 汇率表数据
                    tBasicSubjectMappingMiddleService.deleteByAccountId(session); // 根据
                    // 账套id
                    // 删除映射信息表
                    // 4调用固定资产业务层添加到数据库
                    Map<String, Object> uploadSubExcel2 = subExcelService.uploadSubExcel(list, map, file);
                    result.putAll(uploadSubExcel2);
                } else {
                    result.put("msg", "请检查科目表 《B2》数据是否为 1001");
                }
            }

            // 获取系统科目
            List<TBasicSubjectMessage> sysList = tBasicSubjectMessageService.querySubMessageList(session);
            // 小规模账套初始化 小规模不允许在2221科目下出现包含进项,销项的三级科目!
            if (getAccount().getSsType() == 1) {
                if (null != sysList && !sysList.isEmpty()) {
                    for (int i = 0; i < sysList.size(); i++) {
                        TBasicSubjectMessage tsm = sysList.get(i);
                        if (tsm != null) {
                            String sysSubCode = tsm.getSubCode();
                            String sysSubName = tsm.getSubName();
                            if (sysSubCode != null && sysSubCode.startsWith("2221") && sysSubCode.length() > 7
                                    && (sysSubName.contains("进项") || sysSubName.contains("销项"))) {
                                result.put("code", "-1");
                                result.put("msg", "科目初始化异常,小规模不允许在2221科目下出现包含进项,销项的三级科目,请重新导入!");

                                return result;
                            }
                        }
                    }
                }
            }
        } catch (BusinessException e) {
            result.put("msg", "上传失败请检查excel格式是否是97" + e);
            e.printStackTrace();
        } catch (Exception e) {
            result.put("msg", "上传失败请检查excel格式是否是97" + e);
        }
        return result;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubExcel
     * @Description: 查询已导入的初始化excel数据
     * @date 2017年12月7日 下午3:12:45
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubExcel")
    @ResponseBody
    Map<String, Object> querySubExcel() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            List<TBasicSubjectExcel> list = subExcelService.querySubExcel(session);
            result.put("code", 1);
            result.put("list", list);
            result.put("message", 1);
        } catch (BusinessException e) {
            result.put("message", e);
            logger.error("SubExcelController【querySubExcel】,科目查询出错", e);
        } catch (Exception e) {
            result.put("message", e);
            logger.error("SubExcelController【querySubExcel】,科目查询出错", e);
        }
        return result;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubByisMatching
     * @Description: isMatching根据查询所有为匹配到的科目
     * @date 2017年12月15日 下午4:48:43
     * @author SiLiuDong 司氏旭东
     */

    @RequestMapping("/querySubByisMatching")
    @ResponseBody
    Map<String, Object> querySubByisMatching(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            HttpSession session = getSession();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            param.put("sunumber", keyWord);
            List<TBasicSubjectExcel> list = subExcelService.querySubByisMatching(param);
            result.put("list.size()", list.size());
            result.put("list", list);
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            logger.error("SubExcelController【querySubExcel】,科目查询出错", e);
            result.put("message", "fail");
            return result;
        }
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubBySubSode
     * @Description: 根据SubSode查询包含这个编码所有未添加的子集
     * @date 2017年12月15日 下午4:49:11
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubBySubSode")
    @ResponseBody
    Map<String, Object> querySubBySubSode(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            HttpSession session = getSession();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            param.put("sunumber", keyWord);
            List<TBasicSubjectExcel> list = subExcelService.querySubBySubSode(param);
            result.put("list", list);
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            logger.error("SubExcelController【querySubBySubSode】,科目查询出错", e);
            result.put("message", "fail");
            return result;
        }
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: deleteSubExcel
     * @Description: 刪除已导入的初始化excel数据
     * @date 2017年12月7日 下午3:12:45
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteSubExcel")
    @ResponseBody
    Map<String, Object> deleteSubExcel() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            subExcelService.deleteSubExcelAll(session);
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            logger.error("SubExcelController【deleteSubExcel】,科目删除出错", e);
            result.put("message", "fail");
            return result;
        }
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: updateSubExcel
     * @Description: 更新导入的数据
     * @date 2017年12月15日 下午6:05:40
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateSubExcel")
    @ResponseBody
    Map<String, Object> updateSubExcel(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            HttpSession session = getSession();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            param.put("sunumber", keyWord);

            List<TBasicSubjectExcel> list = subExcelService.updateSubExcel(param);
            result.put("list", list);
            result.put("message", "success");
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("SubExcelController【updateSubExcel】,科目更新出错", e);
            result.put("message", "fail");
        }
        return result;
    }

    /**
     * @param response
     * @throws Exception void 返回类型
     * @Title: downSubExcel
     * @Description: 下载科目余额初始excel模板
     * @date 2018年1月4日 下午4:03:13
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping(value = "/downSubExcel")
    public void downSubExcel(HttpServletResponse response) throws Exception {
        Account account = getAccount();
        if (account.getSsType() == 1) {
            FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "小规模_科目余额初始化.xls",
                    "科目余额初始化-" + DateUtil.getDays() + ".xls");
        } else {
            // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
            FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "一般纳税人_科目余额初始化.xls",
                    "科目余额初始化-" + DateUtil.getDays() + ".xls");
        }
    }
}
