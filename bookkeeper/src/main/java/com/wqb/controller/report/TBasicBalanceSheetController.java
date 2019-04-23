package com.wqb.controller.report;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.DoubleUtils;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.TBasicBalanceSheet;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.report.TBasicBalanceSheetService;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: BalanceSheet
 * @Description: 资产负债表
 * @date 2018年1月20日 下午5:50:49
 */
@Component
@Controller
@RequestMapping("/BalanceSheet")
public class TBasicBalanceSheetController extends BaseController {
    @Autowired
    TBasicBalanceSheetService tBasicBalanceSheetService;

    @Autowired
    private UserService userService;

    /**
     * @param modelAndvide
     * @return ModelAndView 返回类型
     * @Title: BalanceSheetView
     * @Description: 跳转到资产负债表
     * @date 2018年2月8日 下午6:57:00
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/BalanceSheetView")
    @ResponseBody
    public ModelAndView BalanceSheetView(ModelAndView modelAndvide) {
        modelAndvide.setViewName("report/report");
        return modelAndvide;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: queryBalanceSheet
     * @Description: 查询资产负债表
     * @date 2018年2月8日 下午6:57:44
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryBalanceSheet")
    @ResponseBody
    public Map<String, Object> queryBalanceSheet() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> queryBalanceSheet = null;
        try {
            HttpSession session = getSession();
            Map<String, Object> deleteBalanceSheet = deleteBalanceSheet();
            if (!deleteBalanceSheet.isEmpty() && (int) deleteBalanceSheet.get("code") == -1) {
                result.put("msg", "删除资产负债表数据异常！");
                return result;
            }
            Map<String, Object> addBalanceSheet = addBalanceSheet();
            if (!addBalanceSheet.isEmpty() && (int) addBalanceSheet.get("code") == -1) {
                result.put("msg", "添加资产负债表数据异常,请检查科目余额表是否有数据！");
                return result;
            }

            queryBalanceSheet = tBasicBalanceSheetService.queryBalanceSheet(user, account);
            result.putAll(queryBalanceSheet);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: queryBalanceSheetMap
     * @Description: 查询资产负债表 返回map
     * @date 2018年2月23日 下午3:34:45
     * @author SiLiuDong 司氏旭东
     */
    // PC调用接口
    @RequestMapping("/queryBalanceSheetMap")
    @ResponseBody
    public Map<String, Object> queryBalanceSheetMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> queryBalanceSheet = null;
        try {
            HttpSession session = getSession();
            queryBalanceSheet = tBasicBalanceSheetService.queryBalanceSheetMap(session);
            result.putAll(queryBalanceSheet);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/deleteBalanceSheet")
    @ResponseBody
    public Map<String, Object> deleteBalanceSheet() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> deleteBalanceSheet = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            deleteBalanceSheet = tBasicBalanceSheetService.deleteBalanceSheet(user, account);
            result.putAll(deleteBalanceSheet);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: addBalanceSheet
     * @Description: 增加资产负债表
     * @date 2018年2月8日 下午6:58:14
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addBalanceSheet")
    @ResponseBody
    public Map<String, Object> addBalanceSheet() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        Map<String, Object> addBalanceSheet = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            addBalanceSheet = tBasicBalanceSheetService.addBalanceSheet(user, account);
            result.putAll(addBalanceSheet);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 这是资产负载表APP接口
    @RequestMapping("/balanceSheetApp")
    @ResponseBody
    public Map<String, Object> queryBalanceSheetApp(@RequestParam(value = "period", required = true) String period,
                                                    @RequestParam(value = "accountID", required = true) String accountID) {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> param = new HashMap<>();
        param.put("period", period);
        param.put("accountID", accountID);

        try {
            Map<String, Object> sheetMap = tBasicBalanceSheetService.queryBalanceSheetApp(param);
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
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取数据异常");
        }
        return result;
    }

    // 导出资产负债表
    @RequestMapping("/downLoadBalanceSheet")
    @ResponseBody
    Map<String, Object> queryCusExcleExport(HttpServletResponse response) {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            /*
             * Map<String, Object> deleteBalanceSheet = deleteBalanceSheet(); if
             * (!deleteBalanceSheet.isEmpty() && (int)
             * deleteBalanceSheet.get("code") == -1) { resultMap.put("msg",
             * "删除资产负债表数据异常！"); return resultMap; }
             *
             * Map<String, Object> addBalanceSheet = addBalanceSheet(); if
             * (!addBalanceSheet.isEmpty() && (int) addBalanceSheet.get("code")
             * == -1) { resultMap.put("msg", "添加资产负债表数据异常,请检查科目余额表是否有数据！");
             * return resultMap; }
             */
            Map<String, Object> paramMap = tBasicBalanceSheetService.queryBalanceSheet(user, account);
            List<TBasicBalanceSheet> list = (List<TBasicBalanceSheet>) paramMap.get("queryBalanceSheet");
            if (list == null || list.size() == 0) {
                resultMap.put("msg", "导出资产负债表异常,资产负债表数据不存在!");
                return resultMap;
            }
            TBasicBalanceSheet tbs = list.get(0);

            XSSFWorkbook wk = new XSSFWorkbook();
            // sheet对象
            XSSFSheet sheet = wk.createSheet("资产负债表导出");
            sheet.setColumnWidth(0, 20 * 350);
            sheet.setColumnWidth(1, 20 * 200);
            sheet.setColumnWidth(2, 20 * 200);
            sheet.setColumnWidth(3, 20 * 420);
            sheet.setColumnWidth(4, 20 * 200);
            sheet.setColumnWidth(5, 20 * 200);
            // 左对齐
            XSSFCellStyle leftStyle = wk.createCellStyle();
            leftStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            // 居中
            XSSFCellStyle middeleStyle = wk.createCellStyle();
            middeleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            // 右对齐
            XSSFCellStyle rightStyle = wk.createCellStyle();
            rightStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            // 去边框
            XSSFCellStyle noBorderStyle = wk.createCellStyle();
            // noBorderStyle.BORDER_NONE
            noBorderStyle.setBorderLeft(XSSFCellStyle.BORDER_NONE);
            noBorderStyle.setBorderBottom(XSSFCellStyle.BORDER_NONE);
            noBorderStyle.setBorderRight(XSSFCellStyle.BORDER_NONE);
            noBorderStyle.setBorderTop(XSSFCellStyle.BORDER_NONE);

            // 标题栏字体 1
            XSSFFont font1 = wk.createFont(); // 创建字体
            font1.setFontName("仿宋_GB2312"); // 设置字体名称
            font1.setFontHeightInPoints((short) 20);// 设置字体大小
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
            // font1.setColor(HSSFColor.BLUE.index);
            // 合并单元格（第一行、标题）
            CellRangeAddress cAddress = new CellRangeAddress(0, 1, 0, 5);
            sheet.addMergedRegion(cAddress);
            // 创建第一行
            XSSFRow row1 = sheet.createRow(0);
            // row1.setHeight((short)100);
            XSSFFont font = wk.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 16);// 设置字体大小

            // 创建第一行第一列
            XSSFCell row1Cell1 = row1.createCell(0);
            XSSFCellStyle style = wk.createCellStyle();
            style.setFont(font1);
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 水平居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            row1Cell1.setCellStyle(style);
            row1Cell1.setCellValue("资产负债表");
            XSSFCell row1Cell2 = row1.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row1Cell2.setCellValue("");
            XSSFCell row1Cell3 = row1.createCell(2);
            row1Cell3.setCellValue("");
            XSSFCell row1Cell4 = row1.createCell(3);
            row1Cell4.setCellValue("");
            // 创建第二行
            CellRangeAddress cAddress1 = new CellRangeAddress(2, 2, 0, 1);
            sheet.addMergedRegion(cAddress1);
            XSSFRow row2 = sheet.createRow(2);
            XSSFCell row2Cell1 = row2.createCell(0);
            row2Cell1.setCellValue("单位名称:" + getAccount().getCompanyName());

            CellRangeAddress cAddress2 = new CellRangeAddress(2, 2, 2, 3);
            sheet.addMergedRegion(cAddress2);
            XSSFCell row2Cell2 = row2.createCell(2);
            row2Cell2.setCellStyle(middeleStyle);
            row2Cell2.setCellValue(getUserDate().substring(0, 4) + "年" + getUserDate().substring(5, 7) + "月"
                    + DateUtil.getDayofMonth(getUserDate()) + "日");

            CellRangeAddress cAddress3 = new CellRangeAddress(2, 2, 4, 5);
            sheet.addMergedRegion(cAddress3);
            XSSFCell row2Cell3 = row2.createCell(4);
            row2Cell3.setCellStyle(rightStyle);
            row2Cell3.setCellValue("单位:元");

            // 创建第三行
            XSSFRow row3 = sheet.createRow(3);
            XSSFCell row3Cell1 = row3.createCell(0);
            row3Cell1.setCellStyle(middeleStyle);
            row3Cell1.setCellValue("资产");
            XSSFCell row3Cell2 = row3.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row3Cell2.setCellValue("期末余额");
            row3Cell2.setCellStyle(middeleStyle);
            XSSFCell row3Cell3 = row3.createCell(2);
            row3Cell3.setCellValue("年初余额");
            row3Cell3.setCellStyle(middeleStyle);
            XSSFCell row3Cell4 = row3.createCell(3);
            row3Cell4.setCellValue("负债和所有者权益(或股东权益)");
            row3Cell4.setCellStyle(middeleStyle);
            XSSFCell row3Cell5 = row3.createCell(4);
            row3Cell5.setCellValue("期末余额");
            row3Cell5.setCellStyle(middeleStyle);
            XSSFCell row3Cell6 = row3.createCell(5);
            row3Cell6.setCellValue("年初余额");
            row3Cell6.setCellStyle(middeleStyle);

            // 创建第四行
            XSSFRow row4 = sheet.createRow(4);
            XSSFCell row4Cell1 = row4.createCell(0);
            row4Cell1.setCellValue("流动资产:");
            row4Cell1.setCellStyle(leftStyle);
            XSSFCell row4Cell2 = row4.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row4Cell2.setCellValue("");
            XSSFCell row4Cell3 = row4.createCell(2);
            row4Cell3.setCellValue("");
            XSSFCell row4Cell4 = row4.createCell(3);
            row4Cell4.setCellValue("流动负债:");
            row4Cell4.setCellStyle(leftStyle);
            XSSFCell row4Cell5 = row4.createCell(4);
            row4Cell5.setCellValue("");
            XSSFCell row4Cell6 = row4.createCell(5);
            row4Cell6.setCellValue("");
            // 创建第五行
            XSSFRow row5 = sheet.createRow(5);
            XSSFCell row5Cell1 = row5.createCell(0);
            row5Cell1.setCellValue("    货币资金");
            XSSFCell row5Cell2 = row5.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row5Cell2.setCellStyle(rightStyle);
            row5Cell2.setCellValue((tbs.getEndCash() == null || tbs.getEndCash().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndCash().doubleValue()));
            XSSFCell row5Cell3 = row5.createCell(2);
            row5Cell3.setCellStyle(rightStyle);
            row5Cell3.setCellValue((tbs.getInitCash() == null || tbs.getInitCash().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitCash().doubleValue()));
            XSSFCell row5Cell4 = row5.createCell(3);
            row5Cell4.setCellValue("    短期借款");
            XSSFCell row5Cell5 = row5.createCell(4);
            row5Cell5.setCellStyle(rightStyle);
            row5Cell5.setCellValue((tbs.getEndShortTermLoan() == null || tbs.getEndShortTermLoan().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndShortTermLoan().doubleValue()));
            XSSFCell row5Cell6 = row5.createCell(5);
            row5Cell6.setCellStyle(rightStyle);
            row5Cell6.setCellValue((tbs.getInitShortTermLoan() == null || tbs.getInitShortTermLoan().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitShortTermLoan().doubleValue()));

            // 创建第六行
            XSSFRow row6 = sheet.createRow(6);
            XSSFCell row6Cell1 = row6.createCell(0);
            row6Cell1.setCellValue("    交易性金融资产");
            XSSFCell row6Cell2 = row6.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row6Cell2.setCellStyle(rightStyle);
            row6Cell2.setCellValue((tbs.getEndTransactionMonetaryAssets() == null
                    || tbs.getEndTransactionMonetaryAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTransactionMonetaryAssets().doubleValue()));
            XSSFCell row6Cell3 = row6.createCell(2);
            row6Cell3.setCellStyle(rightStyle);
            row6Cell3.setCellValue((tbs.getInitTransactionMonetaryAssets() == null
                    || tbs.getInitTransactionMonetaryAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTransactionMonetaryAssets().doubleValue()));
            XSSFCell row6Cell4 = row6.createCell(3);
            row6Cell4.setCellValue("    交易性金融负债");
            XSSFCell row6Cell5 = row6.createCell(4);
            row6Cell5.setCellStyle(rightStyle);
            row6Cell5.setCellValue((tbs.getEndTradableFinancialLiabilities() == null
                    || tbs.getEndTradableFinancialLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTradableFinancialLiabilities().doubleValue()));
            XSSFCell row6Cell6 = row6.createCell(5);
            row6Cell6.setCellStyle(rightStyle);
            row6Cell6.setCellValue((tbs.getInitTradableFinancialLiabilities() == null
                    || tbs.getInitTradableFinancialLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTradableFinancialLiabilities().doubleValue()));

            // 创建第七行
            XSSFRow row7 = sheet.createRow(7);
            XSSFCell row7Cell1 = row7.createCell(0);
            row7Cell1.setCellValue("    应收票据");
            XSSFCell row7Cell2 = row7.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row7Cell2.setCellStyle(rightStyle);
            row7Cell2.setCellValue(
                    (tbs.getEndNotesReceivable() == null || tbs.getEndNotesReceivable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndNotesReceivable().doubleValue()));
            XSSFCell row7Cell3 = row7.createCell(2);
            row7Cell3.setCellStyle(rightStyle);
            row7Cell3.setCellValue(
                    (tbs.getInitNotesReceivable() == null || tbs.getInitNotesReceivable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitNotesReceivable().doubleValue()));
            XSSFCell row7Cell4 = row7.createCell(3);
            row7Cell4.setCellValue("    应付票据");
            XSSFCell row7Cell5 = row7.createCell(4);
            row7Cell5.setCellStyle(rightStyle);
            row7Cell5.setCellValue((tbs.getEndNotesPayable() == null || tbs.getEndNotesPayable().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndNotesPayable().doubleValue()));
            XSSFCell row7Cell6 = row7.createCell(5);
            row7Cell6.setCellStyle(rightStyle);
            row7Cell6.setCellValue((tbs.getInitNotesPayable() == null || tbs.getInitNotesPayable().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitNotesPayable().doubleValue()));

            // 创建第八行
            XSSFRow row8 = sheet.createRow(8);
            XSSFCell row8Cell1 = row8.createCell(0);
            row8Cell1.setCellValue("    应收账款");
            XSSFCell row8Cell2 = row8.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row8Cell2.setCellStyle(rightStyle);
            row8Cell2.setCellValue(
                    (tbs.getEndAccountsReceivable() == null || tbs.getEndAccountsReceivable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndAccountsReceivable().doubleValue()));
            XSSFCell row8Cell3 = row8.createCell(2);
            row8Cell3.setCellStyle(rightStyle);
            row8Cell3.setCellValue(
                    (tbs.getInitAccountsReceivable() == null || tbs.getInitAccountsReceivable().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitAccountsReceivable().doubleValue()));
            XSSFCell row8Cell4 = row8.createCell(3);
            row8Cell4.setCellValue("    应付账款");
            XSSFCell row8Cell5 = row8.createCell(4);
            row8Cell5.setCellStyle(rightStyle);
            row8Cell5.setCellValue(
                    (tbs.getEndAccountsPayable() == null || tbs.getEndAccountsPayable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndAccountsPayable().doubleValue()));
            XSSFCell row8Cell6 = row8.createCell(5);
            row8Cell6.setCellStyle(rightStyle);
            row8Cell6.setCellValue(
                    (tbs.getInitAccountsPayable() == null || tbs.getInitAccountsPayable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitAccountsPayable().doubleValue()));

            // 创建第九行
            XSSFRow row9 = sheet.createRow(9);
            XSSFCell row9Cell1 = row9.createCell(0);
            row9Cell1.setCellValue("    预付款项");
            XSSFCell row9Cell2 = row9.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row9Cell2.setCellStyle(rightStyle);
            row9Cell2.setCellValue(
                    (tbs.getEndAccountsPrepaid() == null || tbs.getEndAccountsPrepaid().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndAccountsPrepaid().doubleValue()));
            XSSFCell row9Cell3 = row9.createCell(2);
            row9Cell3.setCellStyle(rightStyle);
            row9Cell3.setCellValue(
                    (tbs.getInitAccountsPrepaid() == null || tbs.getInitAccountsPrepaid().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitAccountsPrepaid().doubleValue()));
            XSSFCell row9Cell4 = row9.createCell(3);
            row9Cell4.setCellValue("    预收款项");
            XSSFCell row9Cell5 = row9.createCell(4);
            row9Cell5.setCellStyle(rightStyle);
            row9Cell5.setCellValue(
                    (tbs.getEndAdvanceReceipts() == null || tbs.getEndAdvanceReceipts().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndAdvanceReceipts().doubleValue()));
            XSSFCell row9Cell6 = row9.createCell(5);
            row9Cell6.setCellStyle(rightStyle);
            row9Cell6.setCellValue(
                    (tbs.getInitAdvanceReceipts() == null || tbs.getInitAdvanceReceipts().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitAdvanceReceipts().doubleValue()));

            // 创建第十行
            XSSFRow row10 = sheet.createRow(10);
            XSSFCell row10Cell1 = row10.createCell(0);
            row10Cell1.setCellValue("    应收利息");
            XSSFCell row10Cell2 = row10.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row10Cell2.setCellStyle(rightStyle);
            row10Cell2.setCellValue(
                    (tbs.getEndInterestReceivable() == null || tbs.getEndInterestReceivable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndInterestReceivable().doubleValue()));
            XSSFCell row10Cell3 = row10.createCell(2);
            row10Cell3.setCellStyle(rightStyle);
            row10Cell3.setCellValue(
                    (tbs.getInitInterestReceivable() == null || tbs.getInitInterestReceivable().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitInterestReceivable().doubleValue()));
            XSSFCell row10Cell4 = row10.createCell(3);
            row10Cell4.setCellValue("    应付职工薪酬");
            XSSFCell row10Cell5 = row10.createCell(4);
            row10Cell5.setCellStyle(rightStyle);
            row10Cell5
                    .setCellValue((tbs.getEndAccruedPayroll() == null || tbs.getEndAccruedPayroll().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndAccruedPayroll().doubleValue()));
            XSSFCell row10Cell6 = row10.createCell(5);
            row10Cell6.setCellStyle(rightStyle);
            row10Cell6.setCellValue(
                    (tbs.getInitAccruedPayroll() == null || tbs.getInitAccruedPayroll().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitAccruedPayroll().doubleValue()));

            // 创建第十一行
            XSSFRow row11 = sheet.createRow(11);
            XSSFCell row11Cell1 = row11.createCell(0);
            row11Cell1.setCellValue("    应收股利");
            XSSFCell row11Cell2 = row11.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row11Cell2.setCellStyle(rightStyle);
            row11Cell2.setCellValue(
                    (tbs.getEndDividendReceivable() == null || tbs.getEndDividendReceivable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndDividendReceivable().doubleValue()));
            XSSFCell row11Cell3 = row11.createCell(2);
            row11Cell3.setCellStyle(rightStyle);
            row11Cell3.setCellValue(
                    (tbs.getInitDividendReceivable() == null || tbs.getInitDividendReceivable().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitDividendReceivable().doubleValue()));
            XSSFCell row11Cell4 = row11.createCell(3);
            row11Cell4.setCellValue("    应交税费");
            XSSFCell row11Cell5 = row11.createCell(4);
            row11Cell5.setCellStyle(rightStyle);
            row11Cell5.setCellValue((tbs.getEndAccruedTax() == null || tbs.getEndAccruedTax().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndAccruedTax().doubleValue()));
            XSSFCell row11Cell6 = row11.createCell(5);
            row11Cell6.setCellStyle(rightStyle);
            row11Cell6.setCellValue((tbs.getInitAccruedTax() == null || tbs.getInitAccruedTax().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitAccruedTax().doubleValue()));

            // 创建第十二行
            XSSFRow row12 = sheet.createRow(12);
            XSSFCell row12Cell1 = row12.createCell(0);
            row12Cell1.setCellValue("    其他应收款");
            XSSFCell row12Cell2 = row12.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row12Cell2.setCellStyle(rightStyle);
            row12Cell2.setCellValue(
                    (tbs.getEndOtherReceivables() == null || tbs.getEndOtherReceivables().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndOtherReceivables().doubleValue()));
            XSSFCell row12Cell3 = row12.createCell(2);
            row12Cell3.setCellStyle(rightStyle);
            row12Cell3.setCellValue(
                    (tbs.getInitOtherReceivables() == null || tbs.getInitOtherReceivables().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitOtherReceivables().doubleValue()));
            XSSFCell row12Cell4 = row12.createCell(3);
            row12Cell4.setCellValue("    应付利息");
            XSSFCell row12Cell5 = row12.createCell(4);
            row12Cell5.setCellStyle(rightStyle);
            row12Cell5.setCellValue((tbs.getEndAccruedInterestPayable() == null
                    || tbs.getEndAccruedInterestPayable().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndAccruedInterestPayable().doubleValue()));
            XSSFCell row12Cell6 = row12.createCell(5);
            row12Cell6.setCellStyle(rightStyle);
            row12Cell6.setCellValue((tbs.getInitAccruedInterestPayable() == null
                    || tbs.getInitAccruedInterestPayable().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitAccruedInterestPayable().doubleValue()));

            // 创建第十三行
            XSSFRow row13 = sheet.createRow(13);
            XSSFCell row13Cell1 = row13.createCell(0);
            row13Cell1.setCellValue("    存货");
            XSSFCell row13Cell2 = row13.createCell(1);
            row13Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row13Cell2.setCellValue((tbs.getEndInventories() == null || tbs.getEndInventories().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndInventories().doubleValue()));
            XSSFCell row13Cell3 = row13.createCell(2);
            row13Cell3.setCellStyle(rightStyle);
            row13Cell3.setCellValue((tbs.getInitInventories() == null || tbs.getInitInventories().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitInventories().doubleValue()));
            XSSFCell row13Cell4 = row13.createCell(3);
            row13Cell4.setCellValue("    应付股利");
            XSSFCell row13Cell5 = row13.createCell(4);
            row13Cell5.setCellStyle(rightStyle);
            row13Cell5.setCellValue(
                    (tbs.getEndDividendPayable() == null || tbs.getEndDividendPayable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndDividendPayable().doubleValue()));
            XSSFCell row13Cell6 = row13.createCell(5);
            row13Cell6.setCellStyle(rightStyle);
            row13Cell6.setCellValue(
                    (tbs.getInitDividendPayable() == null || tbs.getInitDividendPayable().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitDividendPayable().doubleValue()));

            // 创建第十四行
            XSSFRow row14 = sheet.createRow(14);
            XSSFCell row14Cell1 = row14.createCell(0);
            row14Cell1.setCellValue("    一年内到期的非流动资产");
            XSSFCell row14Cell2 = row14.createCell(1);
            row14Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row14Cell2.setCellValue((tbs.getEndNonCurrentAssetsDueInOneYear() == null
                    || tbs.getEndNonCurrentAssetsDueInOneYear().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndNonCurrentAssetsDueInOneYear().doubleValue()));
            XSSFCell row14Cell3 = row14.createCell(2);
            row14Cell3.setCellStyle(rightStyle);
            row14Cell3.setCellValue((tbs.getInitNonCurrentAssetsDueInOneYear() == null
                    || tbs.getInitNonCurrentAssetsDueInOneYear().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitNonCurrentAssetsDueInOneYear().doubleValue()));
            XSSFCell row14Cell4 = row14.createCell(3);
            row14Cell4.setCellValue("    其他应付款");
            XSSFCell row14Cell5 = row14.createCell(4);
            row14Cell5.setCellStyle(rightStyle);
            row14Cell5.setCellValue((tbs.getEndOtherPayables() == null || tbs.getEndOtherPayables().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndOtherPayables().doubleValue()));
            XSSFCell row14Cell6 = row14.createCell(5);
            row14Cell6.setCellStyle(rightStyle);
            row14Cell6
                    .setCellValue((tbs.getInitOtherPayables() == null || tbs.getInitOtherPayables().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitOtherPayables().doubleValue()));

            // 创建第十五行
            XSSFRow row15 = sheet.createRow(15);
            XSSFCell row15Cell1 = row15.createCell(0);
            row15Cell1.setCellValue("    其他流动资产");
            XSSFCell row15Cell2 = row15.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row15Cell2.setCellStyle(rightStyle);
            row15Cell2.setCellValue(
                    (tbs.getEndOtherCurrentAssets() == null || tbs.getEndOtherCurrentAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndOtherCurrentAssets().doubleValue()));
            XSSFCell row15Cell3 = row15.createCell(2);
            row15Cell3.setCellStyle(rightStyle);
            row15Cell3.setCellValue(
                    (tbs.getInitOtherCurrentAssets() == null || tbs.getInitOtherCurrentAssets().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitOtherCurrentAssets().doubleValue()));
            XSSFCell row15Cell4 = row15.createCell(3);
            row15Cell4.setCellValue("    一年内到期的非流动负债");
            XSSFCell row15Cell5 = row15.createCell(4);
            row15Cell5.setCellStyle(rightStyle);
            row15Cell5.setCellValue((tbs.getEndCurrentLiailitiesFallingDueWithinOneYear() == null
                    || tbs.getEndCurrentLiailitiesFallingDueWithinOneYear().doubleValue() == 0) ? "0"
                    : DoubleUtils
                    .getNumber(tbs.getEndCurrentLiailitiesFallingDueWithinOneYear().doubleValue()));
            XSSFCell row15Cell6 = row15.createCell(5);
            row15Cell6.setCellStyle(rightStyle);
            row15Cell6.setCellValue((tbs.getInitCurrentLiailitiesFallingDueWithinOneYear() == null
                    || tbs.getInitCurrentLiailitiesFallingDueWithinOneYear().doubleValue() == 0) ? "0"
                    : DoubleUtils
                    .getNumber(tbs.getInitCurrentLiailitiesFallingDueWithinOneYear().doubleValue()));

            // 创建第十六行
            XSSFRow row16 = sheet.createRow(16);
            XSSFCell row16Cell1 = row16.createCell(0);
            row16Cell1.setCellValue("流动资产合计：");
            XSSFCell row16Cell2 = row16.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row16Cell2.setCellStyle(rightStyle);
            row16Cell2.setCellValue(
                    (tbs.getEndTotalCurrentAssets() == null || tbs.getEndTotalCurrentAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndTotalCurrentAssets().doubleValue()));
            XSSFCell row16Cell3 = row16.createCell(2);
            row16Cell3.setCellStyle(rightStyle);
            row16Cell3.setCellValue(
                    (tbs.getInitTotalCurrentAssets() == null || tbs.getInitTotalCurrentAssets().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitTotalCurrentAssets().doubleValue()));
            XSSFCell row16Cell4 = row16.createCell(3);
            row16Cell4.setCellValue("    其他流动负债");
            XSSFCell row16Cell5 = row16.createCell(4);
            row16Cell5.setCellStyle(rightStyle);
            row16Cell5.setCellValue((tbs.getEndOtherCurrentLiabilities() == null
                    || tbs.getEndOtherCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndOtherCurrentLiabilities().doubleValue()));
            XSSFCell row16Cell6 = row16.createCell(5);
            row16Cell6.setCellStyle(rightStyle);
            row16Cell6.setCellValue((tbs.getInitOtherCurrentLiabilities() == null
                    || tbs.getInitOtherCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitOtherCurrentLiabilities().doubleValue()));

            // 创建第十七行
            XSSFRow row17 = sheet.createRow(17);
            XSSFCell row17Cell1 = row17.createCell(0);
            row17Cell1.setCellValue("非流动资产：");
            XSSFCell row17Cell2 = row17.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row17Cell2.setCellValue("");
            XSSFCell row17Cell3 = row17.createCell(2);
            row17Cell3.setCellValue("");
            XSSFCell row17Cell4 = row17.createCell(3);
            row17Cell4.setCellValue("流动负债合计：");
            XSSFCell row17Cell5 = row17.createCell(4);
            row17Cell5.setCellStyle(rightStyle);
            row17Cell5.setCellValue((tbs.getEndTotalOfCurrentLiabilities() == null
                    || tbs.getEndTotalOfCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTotalOfCurrentLiabilities().doubleValue()) + "");
            XSSFCell row17Cell6 = row17.createCell(5);
            row17Cell6.setCellStyle(rightStyle);
            row17Cell6.setCellValue((tbs.getInitTotalOfCurrentLiabilities() == null
                    || tbs.getInitTotalOfCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTotalOfCurrentLiabilities().doubleValue()) + "");

            // 创建第十八行
            XSSFRow row18 = sheet.createRow(18);
            XSSFCell row18Cell1 = row18.createCell(0);
            row18Cell1.setCellValue("    可供出售金融资产");
            XSSFCell row18Cell2 = row18.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row18Cell2.setCellStyle(rightStyle);
            row18Cell2.setCellValue((tbs.getEndAvailableForSaleFinancialAssets() == null
                    || tbs.getEndAvailableForSaleFinancialAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndAvailableForSaleFinancialAssets().doubleValue()) + "");
            XSSFCell row18Cell3 = row18.createCell(2);
            row18Cell3.setCellStyle(rightStyle);
            row18Cell3.setCellValue((tbs.getInitAvailableForSaleFinancialAssets() == null
                    || tbs.getInitAvailableForSaleFinancialAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitAvailableForSaleFinancialAssets().doubleValue()) + "");
            XSSFCell row18Cell4 = row18.createCell(3);
            row18Cell4.setCellValue("非流动负债：");
            XSSFCell row18Cell5 = row18.createCell(4);
            row18Cell5.setCellValue("");
            XSSFCell row18Cell6 = row18.createCell(5);
            row18Cell6.setCellValue("");

            // 创建第十九行
            XSSFRow row19 = sheet.createRow(19);
            XSSFCell row19Cell1 = row19.createCell(0);
            row19Cell1.setCellValue("    持有至到期投资");
            XSSFCell row19Cell2 = row19.createCell(1);
            row19Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row19Cell2.setCellValue((tbs.getEndHeldToMaturityInvestmen() == null
                    || tbs.getEndHeldToMaturityInvestmen().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndHeldToMaturityInvestmen().doubleValue()));
            XSSFCell row19Cell3 = row19.createCell(2);
            row19Cell3.setCellStyle(rightStyle);
            row19Cell3.setCellValue((tbs.getInitHeldToMaturityInvestmen() == null
                    || tbs.getInitHeldToMaturityInvestmen().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitHeldToMaturityInvestmen().doubleValue()));
            XSSFCell row19Cell4 = row19.createCell(3);
            row19Cell4.setCellValue("    长期借款");
            XSSFCell row19Cell5 = row19.createCell(4);
            row19Cell5.setCellStyle(rightStyle);
            row19Cell5.setCellValue((tbs.getEndLongTermLoan() == null || tbs.getEndLongTermLoan().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndLongTermLoan().doubleValue()));
            XSSFCell row19Cell6 = row19.createCell(5);
            row19Cell6.setCellStyle(rightStyle);
            row19Cell6.setCellValue((tbs.getInitLongTermLoan() == null || tbs.getInitLongTermLoan().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitLongTermLoan().doubleValue()));
            // 创建第二十行
            XSSFRow row20 = sheet.createRow(20);
            XSSFCell row20Cell1 = row20.createCell(0);
            row20Cell1.setCellValue("    长期应收款");
            XSSFCell row20Cell2 = row20.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row20Cell2.setCellStyle(rightStyle);
            row20Cell2.setCellValue(
                    (tbs.getEndLongTermReceivables() == null || tbs.getEndLongTermReceivables().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndLongTermReceivables().doubleValue()));
            XSSFCell row20Cell3 = row20.createCell(2);
            row20Cell3.setCellStyle(rightStyle);
            row20Cell3.setCellValue(
                    (tbs.getInitLongTermReceivables() == null || tbs.getInitLongTermReceivables().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitLongTermReceivables().doubleValue()));
            XSSFCell row20Cell4 = row20.createCell(3);
            row20Cell4.setCellValue("    应付债券");
            XSSFCell row20Cell5 = row20.createCell(4);
            row20Cell5.setCellStyle(rightStyle);
            row20Cell5.setCellValue((tbs.getEndBondsPayable() == null || tbs.getEndBondsPayable().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndBondsPayable().doubleValue()));
            XSSFCell row20Cell6 = row20.createCell(5);
            row20Cell6.setCellStyle(rightStyle);
            row20Cell6.setCellValue((tbs.getInitBondsPayable() == null || tbs.getInitBondsPayable().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitBondsPayable().doubleValue()));

            // 创建第二十一行
            XSSFRow row21 = sheet.createRow(21);
            XSSFCell row21Cell1 = row21.createCell(0);
            row21Cell1.setCellValue("    长期股权投资");
            XSSFCell row21Cell2 = row21.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row21Cell2.setCellStyle(rightStyle);
            row21Cell2.setCellValue((tbs.getEndLongTermEquityInvestment() == null
                    || tbs.getEndLongTermEquityInvestment().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndLongTermEquityInvestment().doubleValue()));
            XSSFCell row21Cell3 = row21.createCell(2);
            row21Cell3.setCellStyle(rightStyle);
            row21Cell3.setCellValue((tbs.getInitLongTermEquityInvestment() == null
                    || tbs.getInitLongTermEquityInvestment().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitLongTermEquityInvestment().doubleValue()));
            XSSFCell row21Cell4 = row21.createCell(3);
            row21Cell4.setCellValue("    长期应付款");
            XSSFCell row21Cell5 = row21.createCell(4);
            row21Cell5.setCellStyle(rightStyle);
            row21Cell5.setCellValue((tbs.getEndLongTermAccountsPayable() == null
                    || tbs.getEndLongTermAccountsPayable().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndLongTermAccountsPayable().doubleValue()));
            XSSFCell row21Cell6 = row21.createCell(5);
            row21Cell6.setCellStyle(rightStyle);
            row21Cell6.setCellValue((tbs.getInitLongTermAccountsPayable() == null
                    || tbs.getInitLongTermAccountsPayable().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitLongTermAccountsPayable().doubleValue()));

            // 创建第二十二行
            XSSFRow row22 = sheet.createRow(22);
            XSSFCell row22Cell1 = row22.createCell(0);
            row22Cell1.setCellValue("    投资性房地产");
            XSSFCell row22Cell2 = row22.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row22Cell2.setCellStyle(rightStyle);
            row22Cell2.setCellValue(
                    (tbs.getEndInvestmentRealEstates() == null || tbs.getEndInvestmentRealEstates().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndInvestmentRealEstates().doubleValue()));
            XSSFCell row22Cell3 = row22.createCell(2);
            row22Cell3.setCellStyle(rightStyle);
            row22Cell3.setCellValue((tbs.getInitInvestmentRealEstates() == null
                    || tbs.getInitInvestmentRealEstates().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitInvestmentRealEstates().doubleValue()));
            XSSFCell row22Cell4 = row22.createCell(3);
            row22Cell4.setCellValue("    专项应付款");
            XSSFCell row22Cell5 = row22.createCell(4);
            row22Cell5.setCellStyle(rightStyle);
            row22Cell5.setCellValue((tbs.getEndAccountsPayableForSpecialisedTerms() == null
                    || tbs.getEndAccountsPayableForSpecialisedTerms().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndAccountsPayableForSpecialisedTerms().doubleValue()));
            XSSFCell row22Cell6 = row22.createCell(5);
            row22Cell6.setCellStyle(rightStyle);
            row22Cell6.setCellValue((tbs.getInitAccountsPayableForSpecialisedTerms() == null
                    || tbs.getInitAccountsPayableForSpecialisedTerms().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitAccountsPayableForSpecialisedTerms().doubleValue()));

            // 创建第二十三行
            XSSFRow row23 = sheet.createRow(23);
            XSSFCell row23Cell1 = row23.createCell(0);
            row23Cell1.setCellValue("    固定资产");
            XSSFCell row23Cell2 = row23.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row23Cell2.setCellStyle(rightStyle);
            row23Cell2.setCellValue((tbs.getEndFixedAssets() == null || tbs.getEndFixedAssets().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndFixedAssets().doubleValue()));
            XSSFCell row23Cell3 = row23.createCell(2);
            row23Cell3.setCellStyle(rightStyle);
            row23Cell3.setCellValue((tbs.getInitFixedAssets() == null || tbs.getInitFixedAssets().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getInitFixedAssets().doubleValue()));
            XSSFCell row23Cell4 = row23.createCell(3);
            row23Cell4.setCellValue("    预计负债");
            XSSFCell row23Cell5 = row23.createCell(4);
            row23Cell5.setCellStyle(rightStyle);
            row23Cell5.setCellValue((tbs.getEndProvisionForLiabilities() == null
                    || tbs.getEndProvisionForLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndProvisionForLiabilities().doubleValue()));
            XSSFCell row23Cell6 = row23.createCell(5);
            row23Cell6.setCellStyle(rightStyle);
            row23Cell6.setCellValue((tbs.getInitProvisionForLiabilities() == null
                    || tbs.getInitProvisionForLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitProvisionForLiabilities().doubleValue()));

            // 创建第二十四行
            XSSFRow row24 = sheet.createRow(24);
            XSSFCell row24Cell1 = row24.createCell(0);
            row24Cell1.setCellValue("    在建工程");
            XSSFCell row24Cell2 = row24.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row24Cell2.setCellStyle(rightStyle);
            row24Cell2.setCellValue((tbs.getEndConstructionInProgress() == null
                    || tbs.getEndConstructionInProgress().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndConstructionInProgress().doubleValue()));
            XSSFCell row24Cell3 = row24.createCell(2);
            row24Cell3.setCellStyle(rightStyle);
            row24Cell3.setCellValue((tbs.getInitConstructionInProgress() == null
                    || tbs.getInitConstructionInProgress().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitConstructionInProgress().doubleValue()));
            XSSFCell row24Cell4 = row24.createCell(3);
            row24Cell4.setCellValue("    递延所得税负债");
            XSSFCell row24Cell5 = row24.createCell(4);
            row24Cell5.setCellStyle(rightStyle);
            row24Cell5.setCellValue((tbs.getEndDeferredIncomeTaxLiabilities() == null
                    || tbs.getEndDeferredIncomeTaxLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndDeferredIncomeTaxLiabilities().doubleValue()));
            XSSFCell row24Cell6 = row24.createCell(5);
            row24Cell6.setCellStyle(rightStyle);
            row24Cell6.setCellValue((tbs.getInitDeferredIncomeTaxLiabilities() == null
                    || tbs.getInitDeferredIncomeTaxLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitDeferredIncomeTaxLiabilities().doubleValue()));

            // 创建第二十五行
            XSSFRow row25 = sheet.createRow(25);
            XSSFCell row25Cell1 = row25.createCell(0);
            row25Cell1.setCellValue("    工程物资");
            XSSFCell row25Cell2 = row25.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row25Cell2.setCellStyle(rightStyle);
            row25Cell2.setCellValue(
                    (tbs.getEndConstructionSupplies() == null || tbs.getEndConstructionSupplies().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndConstructionSupplies().doubleValue()) + "");
            XSSFCell row25Cell3 = row25.createCell(2);
            row25Cell3.setCellStyle(rightStyle);
            row25Cell3.setCellValue(
                    (tbs.getInitConstructionSupplies() == null || tbs.getInitConstructionSupplies().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitConstructionSupplies().doubleValue()) + "");
            XSSFCell row25Cell4 = row25.createCell(3);
            row25Cell4.setCellValue("    其他非流动负债");
            XSSFCell row25Cell5 = row25.createCell(4);
            row25Cell5.setCellStyle(rightStyle);
            row25Cell5.setCellValue((tbs.getEndOtherNonCurrentLiabilities() == null
                    || tbs.getEndOtherNonCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndOtherNonCurrentLiabilities().doubleValue()) + "");
            XSSFCell row25Cell6 = row25.createCell(5);
            row25Cell6.setCellStyle(rightStyle);
            row25Cell6.setCellValue((tbs.getInitOtherNonCurrentLiabilities() == null
                    || tbs.getInitOtherNonCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitOtherNonCurrentLiabilities().doubleValue()) + "");

            // 创建第二十六行
            XSSFRow row26 = sheet.createRow(26);
            XSSFCell row26Cell1 = row26.createCell(0);
            row26Cell1.setCellValue("    固定资产清理");
            XSSFCell row26Cell2 = row26.createCell(1);
            row26Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row26Cell2.setCellValue((tbs.getEndFixedAssetsPendingDisposal() == null
                    || tbs.getEndFixedAssetsPendingDisposal().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndFixedAssetsPendingDisposal().doubleValue()));
            XSSFCell row26Cell3 = row26.createCell(2);
            row26Cell3.setCellStyle(rightStyle);
            row26Cell3.setCellValue((tbs.getInitFixedAssetsPendingDisposal() == null
                    || tbs.getInitFixedAssetsPendingDisposal().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitFixedAssetsPendingDisposal().doubleValue()));
            XSSFCell row26Cell4 = row26.createCell(3);
            row26Cell4.setCellValue("    非流动负债合计：");
            XSSFCell row26Cell5 = row26.createCell(4);
            row26Cell5.setCellStyle(rightStyle);
            row26Cell5.setCellValue((tbs.getEndTotalOfNonCurrentLiabilities() == null
                    || tbs.getEndTotalOfNonCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTotalOfNonCurrentLiabilities().doubleValue()));
            XSSFCell row26Cell6 = row26.createCell(5);
            row26Cell6.setCellStyle(rightStyle);
            row26Cell6.setCellValue((tbs.getInitTotalOfNonCurrentLiabilities() == null
                    || tbs.getInitTotalOfNonCurrentLiabilities().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTotalOfNonCurrentLiabilities().doubleValue()));

            // 创建第二十七行
            XSSFRow row27 = sheet.createRow(27);
            XSSFCell row27Cell1 = row27.createCell(0);
            row27Cell1.setCellValue("    生产性生物资产");
            XSSFCell row27Cell2 = row27.createCell(1);
            row27Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row27Cell2.setCellValue((tbs.getEndBearerBiologicalAssets() == null
                    || tbs.getEndBearerBiologicalAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndBearerBiologicalAssets().doubleValue()));
            XSSFCell row27Cell3 = row27.createCell(2);
            row27Cell3.setCellStyle(rightStyle);
            row27Cell3.setCellValue((tbs.getInitBearerBiologicalAssets() == null
                    || tbs.getInitBearerBiologicalAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitBearerBiologicalAssets().doubleValue()));
            XSSFCell row27Cell4 = row27.createCell(3);
            row27Cell4.setCellValue("负债合计：");
            XSSFCell row27Cell5 = row27.createCell(4);
            row27Cell5.setCellStyle(rightStyle);
            row27Cell5.setCellValue(
                    (tbs.getEndTotalOfLiabilities() == null || tbs.getEndTotalOfLiabilities().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndTotalOfLiabilities().doubleValue()));
            XSSFCell row27Cell6 = row27.createCell(5);
            row27Cell6.setCellStyle(rightStyle);
            row27Cell6.setCellValue(
                    (tbs.getInitTotalOfLiabilities() == null || tbs.getInitTotalOfLiabilities().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitTotalOfLiabilities().doubleValue()));

            // 创建第二十八行
            XSSFRow row28 = sheet.createRow(28);
            XSSFCell row28Cell1 = row28.createCell(0);
            row28Cell1.setCellValue("    油气资产");
            XSSFCell row28Cell2 = row28.createCell(1);
            row28Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row28Cell2.setCellValue((tbs.getEndOilAndNaturalGasAssets() == null
                    || tbs.getEndOilAndNaturalGasAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndOilAndNaturalGasAssets().doubleValue()));
            XSSFCell row28Cell3 = row28.createCell(2);
            row28Cell3.setCellStyle(rightStyle);
            row28Cell3.setCellValue((tbs.getInitOilAndNaturalGasAssets() == null
                    || tbs.getInitOilAndNaturalGasAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitOilAndNaturalGasAssets().doubleValue()));
            XSSFCell row28Cell4 = row28.createCell(3);
            row28Cell4.setCellValue("所有者权益(或股东权益)：");
            XSSFCell row28Cell5 = row28.createCell(4);
            row28Cell5.setCellValue("");
            XSSFCell row28Cell6 = row28.createCell(5);
            row28Cell6.setCellValue("");

            // 创建第二十九行
            XSSFRow row29 = sheet.createRow(29);
            XSSFCell row29Cell1 = row29.createCell(0);
            row29Cell1.setCellValue("    无形资产");
            XSSFCell row29Cell2 = row29.createCell(1);
            row29Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row29Cell2.setCellValue(
                    (tbs.getEndIntangibelAssets() == null || tbs.getEndIntangibelAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndIntangibelAssets().doubleValue()));
            XSSFCell row29Cell3 = row29.createCell(2);
            row29Cell3.setCellStyle(rightStyle);
            row29Cell3.setCellValue(
                    (tbs.getInitIntangibelAssets() == null || tbs.getInitIntangibelAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitIntangibelAssets().doubleValue()));
            XSSFCell row29Cell4 = row29.createCell(3);
            row29Cell4.setCellValue("    实收资本(或股本)");
            XSSFCell row29Cell5 = row29.createCell(4);
            row29Cell5.setCellStyle(rightStyle);
            row29Cell5.setCellValue((tbs.getEndCapital() == null || tbs.getEndCapital().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndCapital().doubleValue()));
            XSSFCell row29Cell6 = row29.createCell(5);
            row29Cell6.setCellStyle(rightStyle);
            row29Cell6.setCellValue((tbs.getInitCapital() == null || tbs.getInitCapital().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitCapital().doubleValue()));

            // 创建第三十行
            XSSFRow row30 = sheet.createRow(30);
            XSSFCell row30Cell1 = row30.createCell(0);
            row30Cell1.setCellValue("    开发支出");
            XSSFCell row30Cell2 = row30.createCell(1);
            row30Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row30Cell2.setCellValue((tbs.getEndResearchAndDevelopmentCosts() == null
                    || tbs.getEndResearchAndDevelopmentCosts().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndResearchAndDevelopmentCosts().doubleValue()));
            XSSFCell row30Cell3 = row30.createCell(2);
            row30Cell3.setCellStyle(rightStyle);
            row30Cell3.setCellValue((tbs.getInitResearchAndDevelopmentCosts() == null
                    || tbs.getInitResearchAndDevelopmentCosts().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitResearchAndDevelopmentCosts().doubleValue()));
            XSSFCell row30Cell4 = row30.createCell(3);
            row30Cell4.setCellValue("    资本公积");
            XSSFCell row30Cell5 = row30.createCell(4);
            row30Cell5.setCellStyle(rightStyle);
            row30Cell5.setCellValue(
                    (tbs.getEntCapitalReserves() == null || tbs.getEntCapitalReserves().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEntCapitalReserves().doubleValue()) + "");
            XSSFCell row30Cell6 = row30.createCell(5);
            row30Cell6.setCellStyle(rightStyle);
            row30Cell6.setCellValue(
                    (tbs.getInitCapitalReserves() == null || tbs.getInitCapitalReserves().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitCapitalReserves().doubleValue()) + "");

            // 创建第三十一行
            XSSFRow row31 = sheet.createRow(31);
            XSSFCell row31Cell1 = row31.createCell(0);
            row31Cell1.setCellValue("    商誉");
            XSSFCell row31Cell2 = row31.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row31Cell2.setCellStyle(rightStyle);
            row31Cell2.setCellValue((tbs.getEndGoodwill() == null || tbs.getEndGoodwill().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndGoodwill().doubleValue()));
            XSSFCell row31Cell3 = row31.createCell(2);
            row31Cell3.setCellStyle(rightStyle);
            row31Cell3.setCellValue((tbs.getInitGoodwill() == null || tbs.getInitGoodwill().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitGoodwill().doubleValue()));
            XSSFCell row31Cell4 = row31.createCell(3);
            row31Cell4.setCellValue("    减:库存股");
            XSSFCell row31Cell5 = row31.createCell(4);
            row31Cell5.setCellStyle(rightStyle);
            row31Cell5.setCellValue(
                    (tbs.getEndLessTreasuryStock() == null || tbs.getEndLessTreasuryStock().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndLessTreasuryStock().doubleValue()));
            XSSFCell row31Cell6 = row31.createCell(5);
            row31Cell6.setCellStyle(rightStyle);
            row31Cell6.setCellValue(
                    (tbs.getInitLessTreasuryStock() == null || tbs.getInitLessTreasuryStock().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitLessTreasuryStock().doubleValue()));

            // 创建第三十二行
            XSSFRow row32 = sheet.createRow(32);
            XSSFCell row32Cell1 = row32.createCell(0);
            row32Cell1.setCellValue("    长期待摊费用");
            XSSFCell row32Cell2 = row32.createCell(1);
            row32Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row32Cell2.setCellValue((tbs.getEndLongTermDeferredExpenses() == null
                    || tbs.getEndLongTermDeferredExpenses().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndLongTermDeferredExpenses().doubleValue()));
            XSSFCell row32Cell3 = row32.createCell(2);
            row32Cell3.setCellStyle(rightStyle);
            row32Cell3.setCellValue((tbs.getInitLongTermDeferredExpenses() == null
                    || tbs.getInitLongTermDeferredExpenses().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitLongTermDeferredExpenses().doubleValue()));
            XSSFCell row32Cell4 = row32.createCell(3);
            row32Cell4.setCellValue("    盈余公积");
            XSSFCell row32Cell5 = row32.createCell(4);
            row32Cell5.setCellStyle(rightStyle);
            row32Cell5.setCellValue(
                    (tbs.getEndEarningsReserve() == null || tbs.getEndEarningsReserve().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndEarningsReserve().doubleValue()));
            XSSFCell row32Cell6 = row32.createCell(5);
            row32Cell6.setCellStyle(rightStyle);
            row32Cell6.setCellValue(
                    (tbs.getInitEarningsReserve() == null || tbs.getInitEarningsReserve().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitEarningsReserve().doubleValue()));

            // 创建第三十三行
            XSSFRow row33 = sheet.createRow(33);
            XSSFCell row33Cell1 = row33.createCell(0);
            row33Cell1.setCellValue("    递延所得税资产");
            XSSFCell row33Cell2 = row33.createCell(1);
            row33Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row33Cell2.setCellValue(
                    (tbs.getEndDeferredTaxAssets() == null || tbs.getEndDeferredTaxAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndDeferredTaxAssets().doubleValue()));
            XSSFCell row33Cell3 = row33.createCell(2);
            row33Cell3.setCellStyle(rightStyle);
            row33Cell3.setCellValue(
                    (tbs.getInitDeferredTaxAssets() == null || tbs.getInitDeferredTaxAssets().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitDeferredTaxAssets().doubleValue()));
            XSSFCell row33Cell4 = row33.createCell(3);
            row33Cell4.setCellValue("    未分配利润");
            XSSFCell row33Cell5 = row33.createCell(4);
            row33Cell5.setCellStyle(rightStyle);
            row33Cell5.setCellValue(
                    (tbs.getEndRetainedEarnings() == null || tbs.getEndRetainedEarnings().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getEndRetainedEarnings().doubleValue()));
            XSSFCell row33Cell6 = row33.createCell(5);
            row33Cell6.setCellStyle(rightStyle);
            row33Cell6.setCellValue(
                    (tbs.getInitRetainedEarnings() == null || tbs.getInitRetainedEarnings().doubleValue() == 0) ? "0"
                            : DoubleUtils.getNumber(tbs.getInitRetainedEarnings().doubleValue()));

            // 创建第三十四行
            XSSFRow row34 = sheet.createRow(34);
            XSSFCell row34Cell1 = row34.createCell(0);
            row34Cell1.setCellValue("    其他非流动资产");
            XSSFCell row34Cell2 = row34.createCell(1);
            row34Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row34Cell2.setCellValue((tbs.getEndTotalOfNonCurrentAsses() == null
                    || tbs.getEndTotalOfNonCurrentAsses().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTotalOfNonCurrentAsses().doubleValue()));
            XSSFCell row34Cell3 = row34.createCell(2);
            row34Cell3.setCellStyle(rightStyle);
            row34Cell3.setCellValue((tbs.getInitTotalOfNonCurrentAsses() == null
                    || tbs.getInitTotalOfNonCurrentAsses().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTotalOfNonCurrentAsses().doubleValue()));
            XSSFCell row34Cell4 = row34.createCell(3);
            row34Cell4.setCellValue("所有者权益(或股东权益)合计：");
            XSSFCell row34Cell5 = row34.createCell(4);
            row34Cell5.setCellStyle(rightStyle);
            row34Cell5.setCellValue(
                    (tbs.getEndTotalOfOwnersEquity() == null || tbs.getEndTotalOfOwnersEquity().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndTotalOfOwnersEquity().doubleValue()));
            XSSFCell row34Cell6 = row34.createCell(5);
            row34Cell6.setCellStyle(rightStyle);
            row34Cell6.setCellValue(
                    (tbs.getInitTotalOfOwnersEquity() == null || tbs.getInitTotalOfOwnersEquity().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitTotalOfOwnersEquity().doubleValue()));

            // 创建第三十五行
            XSSFRow row35 = sheet.createRow(35);
            XSSFCell row35Cell1 = row35.createCell(0);
            row35Cell1.setCellValue("非流动资产合计：");
            XSSFCell row35Cell2 = row35.createCell(1);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row35Cell2.setCellStyle(rightStyle);
            row35Cell2.setCellValue(
                    (tbs.getEndOtherNonCurrentAssets() == null || tbs.getEndOtherNonCurrentAssets().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getEndOtherNonCurrentAssets().doubleValue()));
            XSSFCell row35Cell3 = row35.createCell(2);
            row35Cell3.setCellStyle(rightStyle);
            row35Cell3.setCellValue((tbs.getInitOtherNonCurrentAssets() == null
                    || tbs.getInitOtherNonCurrentAssets().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitOtherNonCurrentAssets().doubleValue()));
            XSSFCell row35Cell4 = row35.createCell(3);
            row35Cell4.setCellValue("");
            XSSFCell row35Cell5 = row35.createCell(4);
            row35Cell5.setCellValue("");
            XSSFCell row35Cell6 = row35.createCell(5);
            row35Cell6.setCellValue("");

            // 创建第三十六行
            XSSFRow row36 = sheet.createRow(36);
            XSSFCell row36Cell1 = row36.createCell(0);
            row36Cell1.setCellValue("资产总计：");
            XSSFCell row36Cell2 = row36.createCell(1);
            row36Cell2.setCellStyle(rightStyle);
            // 为了保证合并的单元格能有效追加外框、被合并的单元格、内容要设置为空
            row36Cell2.setCellValue((tbs.getEndTotalOfAssets() == null || tbs.getEndTotalOfAssets().doubleValue() == 0)
                    ? "0" : DoubleUtils.getNumber(tbs.getEndTotalOfAssets().doubleValue()));
            XSSFCell row36Cell3 = row36.createCell(2);
            row36Cell3.setCellStyle(rightStyle);
            row36Cell3
                    .setCellValue((tbs.getInitTotalOfAssets() == null || tbs.getInitTotalOfAssets().doubleValue() == 0)
                            ? "0" : DoubleUtils.getNumber(tbs.getInitTotalOfAssets().doubleValue()));
            XSSFCell row36Cell4 = row36.createCell(3);
            row36Cell4.setCellValue("负债和所有者权益(或股东权益)总计：");
            XSSFCell row36Cell5 = row36.createCell(4);
            row36Cell5.setCellStyle(rightStyle);
            row36Cell5.setCellValue((tbs.getEndTotalOfLiabilitiesAndOwnersEquity() == null
                    || tbs.getEndTotalOfLiabilitiesAndOwnersEquity().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getEndTotalOfLiabilitiesAndOwnersEquity().doubleValue()));
            XSSFCell row36Cell6 = row36.createCell(5);
            row36Cell6.setCellStyle(rightStyle);
            row36Cell6.setCellValue((tbs.getInitTotalOfLiabilitiesAndOwnersEquity() == null
                    || tbs.getInitTotalOfLiabilitiesAndOwnersEquity().doubleValue() == 0) ? "0"
                    : DoubleUtils.getNumber(tbs.getInitTotalOfLiabilitiesAndOwnersEquity().doubleValue()));

            String fileName = new String("资产负债表导出.xlsx".getBytes("GBK"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            wk.write(response.getOutputStream());
            response.getOutputStream().flush();
            resultMap.put("success", "资产负债表数据导出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
