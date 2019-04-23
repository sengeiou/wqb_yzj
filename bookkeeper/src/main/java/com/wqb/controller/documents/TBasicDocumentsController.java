package com.wqb.controller.documents;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.TBasicDocumentsForm;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicDocumentsController
 * @Description: 单据Controller
 * @date 2018年6月5日 上午8:57:46
 */
@Controller
@RequestMapping("/documents")
public class TBasicDocumentsController extends BaseController {
    @Autowired
    TBasicDocumentsService tBasicDocumentsService;

    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    // 凭证 创建
    @Autowired
    VatService vatService;

    @Autowired
    private UserService userService;

    @RequestMapping("/documentsView")
    public ModelAndView toDocumentsView(ModelAndView modelAndView) {
        modelAndView.setViewName("documents/import");
        return modelAndView;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: addTicketsCost
     * @Description: 添加费用票现金支付
     * @date 2018年3月9日 下午3:27:25
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addTicketsCost")
    @ResponseBody
    public Map<String, Object> addTicketsCost() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> addTicketsCost = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            addTicketsCost = tBasicDocumentsService.addTicketsCost(user, account, tBasicSubjectMessageService);
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        result.putAll(addTicketsCost);
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: ticketsCostRow
     * @Description: 动态获取单据-费用票列（6601，6602）
     * @date 2018年3月12日 上午8:43:50
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/ticketsCostRow")
    @ResponseBody
    public Map<String, Object> ticketsCostRow() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {
            HttpSession session = getSession();
            Map<String, String> parameters = new HashMap<String, String>();
            // 查询科目余额表
            Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account,
                    parameters);

            Map<String, Object> ticketsCostRowMap = tBasicDocumentsService.ticketsCostRow(session, querySbujectBalance);
            result.putAll(ticketsCostRowMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessageList
     * @return Map<String, Object> 返回类型
     * @Title: addTicketsCostList
     * @Description: 添加費用票
     * @date 2018年3月20日 上午9:07:43
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addTicketsCostList")
    @ResponseBody
    public Map<String, Object> addTicketsCostList(String tBasicSubjectMessageList) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> addTicketsCost = new HashMap<String, Object>();
        Gson gson = new Gson();

        // String jsonString =
        // "[{id=\"45\",name=\"tr\"},{id=\"22\",name=\"bb\"}]";
        java.lang.reflect.Type type2 = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(tBasicSubjectMessageList, type2);

        List<Map<String, Object>> subjectMessages = new ArrayList<>();
        Map<String, Object> subExcelsMap = new HashMap<String, Object>();
        for (JsonObject jsonObject : jsonObjects) {
            java.lang.reflect.Type type3 = new TypeToken<HashMap<String, String>>() {
            }.getType();
            subExcelsMap = gson.fromJson(jsonObject, type3);
            subjectMessages.add(subExcelsMap);
        }
        try {
            HttpSession session = getSession();
            addTicketsCost = tBasicDocumentsService.addTicketsCostList(session, subjectMessages);
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        result.putAll(addTicketsCost);
        return result;
    }

    /**
     * @param tBasicSubjectMessageForm
     * @return Map<String, Object> 返回类型
     * @Title: addProcurementInventoryList
     * @Description: 添加 采购库存商品 集合
     * @date 2018年3月26日 下午6:58:29
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addProcurementInventoryList")
    @ResponseBody
    public Map<String, Object> addProcurementInventoryList(String tBasicSubjectMessage,
                                                           String tBasicSubjectMessageList) {
        // TODO Auto-generated catch block
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> addProcurementInventory = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            addProcurementInventory = tBasicDocumentsService.addProcurementInventoryList(session, tBasicSubjectMessage,
                    tBasicSubjectMessageList);
            result.put("tBasicSubjectMessage", tBasicSubjectMessage);
            result.put("tBasicSubjectMessageList", tBasicSubjectMessageList);
            result.putAll(addProcurementInventory);
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessageForm
     * @return Map<String, Object> 返回类型
     * @Title: addProcurementRawMaterialsList
     * @Description: 添加 采购原材料科目 集合
     * @date 2018年3月26日 下午7:00:17
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addProcurementRawMaterialsList")
    @ResponseBody
    public Map<String, Object> addProcurementRawMaterialsList(String tBasicSubjectMessage,
                                                              String tBasicSubjectMessageList) {
        // TODO Auto-generated catch block
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> addProcurementRawMaterials = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            HttpSession session = getSession();
            addProcurementRawMaterials = tBasicDocumentsService.addProcurementRawMaterialsList(session,
                    tBasicSubjectMessage, tBasicSubjectMessageList);
            result.put("tBasicSubjectMessage", tBasicSubjectMessage);
            result.put("tBasicSubjectMessageList", tBasicSubjectMessageList);
            result.putAll(addProcurementRawMaterials);
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param debitSubCode
     * @param creditSubCode
     * @return Map<String, Object> 返回类型
     * @Title: procurementInventoryRow
     * @Description: 获取采购库存商品科目列表
     * @date 2018年3月21日 上午10:52:08
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/procurementInventoryRow")
    @ResponseBody
    public Map<String, Object> procurementInventoryRow() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {
            HttpSession session = getSession();
            Map<String, String> parameters = new HashMap<String, String>();
            // 查询科目余额表
            Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account,
                    parameters);

            Map<String, Object> ticketsCostRowMap = tBasicDocumentsService.procurementInventoryRow(session,
                    querySbujectBalance);
            result.putAll(ticketsCostRowMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: procurementRawMaterialsRow
     * @Description: 获取采购原材料科目列表
     * @date 2018年3月21日 上午10:58:33
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/procurementRawMaterialsRow")
    @ResponseBody
    public Map<String, Object> procurementRawMaterialsRow() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {
            HttpSession session = getSession();
            Map<String, String> parameters = new HashMap<String, String>();
            // 查询科目余额表
            Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account,
                    parameters);

            Map<String, Object> ticketsCostRowMap = tBasicDocumentsService.procurementRawMaterialsRow(session,
                    querySbujectBalance);
            result.putAll(ticketsCostRowMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: procurementCreditRow
     * @Description: 获取采购贷方科目列表 （应付，预付，现金，银行）
     * @date 2018年3月20日 上午9:10:05
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/procurementCreditRow")
    @ResponseBody
    public Map<String, Object> procurementCreditRow(String creditSubCode) {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {
            HttpSession session = getSession();
            Map<String, String> parameters = new HashMap<String, String>();
            // 查询科目余额表
            Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account,
                    parameters);

            Map<String, Object> ticketsCostRowMap = tBasicDocumentsService.procurementCreditRow(session,
                    querySbujectBalance, creditSubCode);
            result.putAll(ticketsCostRowMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: salesCreditRow
     * @Description: 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款）
     * 1122下级科目（匹配未交增值税科目）有税金要添加
     * @date 2018年3月20日 上午9:10:05
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/salesCreditRow")
    @ResponseBody
    public Map<String, Object> salesCreditRow() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {
            HttpSession session = getSession();
            Map<String, String> parameters = new HashMap<String, String>();
            // 查询科目余额表
            Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account,
                    parameters);

            Map<String, Object> ticketsCostRowMap = tBasicDocumentsService.salesCreditRow(session, querySbujectBalance);
            result.putAll(ticketsCostRowMap);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessageList
     * @return Map<String, Object> 返回类型
     * @Title: addsalesCreditList
     * @Description: 添加 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款） 集合
     * @date 2018年3月26日 下午7:03:21
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addsalesCreditList")
    @ResponseBody
    public Map<String, Object> addsalesCreditList(String tBasicSubjectMessageList) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> addsalesCredit = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            addsalesCredit = tBasicDocumentsService.addsalesCreditList(session, tBasicSubjectMessageList);
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        result.putAll(addsalesCredit);
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: queryDocumentsList
     * @Description: 查询单据集合
     * @date 2018年3月20日 上午9:06:56
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryDocumentsList")
    @ResponseBody
    public Map<String, Object> queryDocumentsList() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> queryDocumentsList = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            queryDocumentsList = tBasicDocumentsService.queryDocumentsList(session);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result.putAll(queryDocumentsList);
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: documentsToVoucher
     * @Description: 单据转成凭证
     * @date 2018年3月20日 上午9:06:32
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/documentsToVoucher")
    @ResponseBody
    public Map<String, Object> documentsToVoucher() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> queryDocumentsList = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            queryDocumentsList = tBasicDocumentsService.documentsToVoucher(user, account);
            result.putAll(queryDocumentsList);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/documentsToVouchers")
    @ResponseBody
    public Map<String, Object> documentsToVouchers() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> queryDocumentsList = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            queryDocumentsList = tBasicDocumentsService.documentsToVouchers(session);
            result.putAll(queryDocumentsList);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param tBasicDocumentss
     * @return Map<String, Object> 返回类型
     * @Title: deleteDocumentsList
     * @Description: 删除单据集合
     * @date 2018年3月29日 下午7:27:53
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteDocumentsList")
    @ResponseBody
    public Map<String, Object> deleteDocumentsList(String tBasicDocumentss) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> deleteDocumentsList = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();

            deleteDocumentsList = tBasicDocumentsService.deleteDocumentsList(session, tBasicDocumentss);
            result.putAll(deleteDocumentsList);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result.putAll(deleteDocumentsList);
        return result;

    }

    @RequestMapping("/deleteDocumentss")
    @ResponseBody
    public Map<String, Object> deleteDocumentss(TBasicDocumentsForm tBasicDocumentss) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> deleteDocumentsList = new HashMap<String, Object>();
        HttpSession session = getSession();
        result.put("tBasicDocumentss", tBasicDocumentss);
        return result;

    }

    // 导入费用票(现金支付)
    @RequestMapping(value = "/uploadFeeStamp")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(required = false) MultipartFile feeStampFile,
                                      HttpServletRequest request, ModelMap model) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {

                return result;
            } catch (Exception e) {
                result.put("success", "false");
                result.put("message", "导入失败");
                return result;
            }
        } catch (Exception e) {
            // logger.error("导入失败", e);
            result.put("success", "false");
            result.put("message", "导入失败");
            return result;
        }
    }

}
