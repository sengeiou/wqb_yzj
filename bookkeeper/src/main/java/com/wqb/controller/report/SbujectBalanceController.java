package com.wqb.controller.report;

import com.wqb.common.BusinessException;
import com.wqb.common.DataExcelExportUtil;
import com.wqb.common.DoubleUtils;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.controller.subject.SubjectController;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: SbujectBalance
 * @Description: 科目余额表
 * @date 2018年1月20日 下午5:53:27
 */
@Component
@Controller
@RequestMapping("/sbubalance")
public class SbujectBalanceController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(SubjectController.class);

    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    private UserService userService;

    /**
     * @return Map<String, Object> 返回类型
     * @Title: querySbujectBalance
     * @Description: 查询科目余额表
     * @date 2018年2月5日 下午1:00:02
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySbujectBalance")
    @ResponseBody
    public Map<String, Object> querySbujectBalance(String endTime, String startTime) {
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

            // 查询科目余额表
            Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySbujectBalance(user, account, parameters);
            result.putAll(querySubMessage);
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectBalance】,查询系统中该账套的全部科目出错", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectBalance】,查询系统中该账套的全部科目出错", e);
        }
        return result;
    }

    /**
     * 凭证页面，科目余额表页面导出功能；
     *
     * @return
     * @author tangsheng time:2018-08-02
     */
    @RequestMapping("/querySbujectExcleExport")
    @ResponseBody
    public Map<String, Object> querySbujectExcleExport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String period = "";
        try {
            List<Object[]> list = new ArrayList<Object[]>();
            HttpSession session = getSession();
            // 查询科目余额表
            Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySbujectExcleExport(session);
            List<TBasicSubjectMessage> subMessages = (List<TBasicSubjectMessage>) querySubMessage.get("subMessages");
            if (subMessages != null) {
                for (int i = 0; i < subMessages.size(); i++) {
                    Object[] subject = new Object[12];
                    TBasicSubjectMessage tBasicSubjectMessage = subMessages.get(i);
                    period = tBasicSubjectMessage.getAccountPeriod().split("\\-")[1];
                    subject[0] = period;
                    subject[1] = tBasicSubjectMessage.getSubCode();
                    subject[2] = tBasicSubjectMessage.getSubName();
                    subject[3] = tBasicSubjectMessage.getTypeOfCurrency();
                    BigDecimal initDebitBalance = tBasicSubjectMessage.getInitDebitBalance();
                    if (initDebitBalance == null) {
                        initDebitBalance = new BigDecimal(0);
                    }
                    if (initDebitBalance.compareTo(BigDecimal.ZERO) == 0) {
                        subject[4] = DoubleUtils.getNumber(initDebitBalance.doubleValue());
                    } else {
                        subject[4] = DoubleUtils.getNumber(initDebitBalance.doubleValue());
                    }
                    BigDecimal initCreditBalance = tBasicSubjectMessage.getInitCreditBalance();
                    if (null == initCreditBalance) {
                        initCreditBalance = new BigDecimal(0);
                    }
                    if (initCreditBalance.compareTo(BigDecimal.ZERO) == 0) {
                        subject[5] = DoubleUtils.getNumber(initCreditBalance.doubleValue());
                    } else {
                        subject[5] = DoubleUtils.getNumber(initCreditBalance.doubleValue());
                    }
                    BigDecimal currentAmountDebit = tBasicSubjectMessage.getCurrentAmountDebit();
                    if (null == currentAmountDebit) {
                        currentAmountDebit = new BigDecimal(0);
                    }
                    if (currentAmountDebit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[6] = DoubleUtils.getNumber(currentAmountDebit.doubleValue());
                    } else {
                        subject[6] = DoubleUtils.getNumber(currentAmountDebit.doubleValue());
                    }
                    BigDecimal currentAmountCredit = tBasicSubjectMessage.getCurrentAmountCredit();
                    if (null == currentAmountCredit) {
                        currentAmountCredit = new BigDecimal(0);
                    }
                    if (currentAmountCredit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[7] = DoubleUtils.getNumber(currentAmountCredit.doubleValue());
                    } else {
                        subject[7] = DoubleUtils.getNumber(currentAmountCredit.doubleValue());
                    }
                    BigDecimal yearAmountDebit = tBasicSubjectMessage.getYearAmountDebit();
                    if (null == yearAmountDebit) {
                        yearAmountDebit = new BigDecimal(0);
                    }
                    if (yearAmountDebit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[8] = DoubleUtils.getNumber(yearAmountDebit.doubleValue());
                    } else {
                        subject[8] = DoubleUtils.getNumber(yearAmountDebit.doubleValue());
                    }
                    BigDecimal yearAmountCredit = tBasicSubjectMessage.getYearAmountCredit();
                    if (null == yearAmountCredit) {
                        yearAmountCredit = new BigDecimal(0);
                    }
                    if (yearAmountCredit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[9] = DoubleUtils.getNumber(yearAmountCredit.doubleValue());
                    } else {
                        subject[9] = DoubleUtils.getNumber(yearAmountCredit.doubleValue());
                    }

                    BigDecimal endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit();
                    if (null == endingBalanceDebit) {
                        endingBalanceDebit = new BigDecimal(0);
                    }
                    if (endingBalanceDebit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[10] = DoubleUtils.getNumber(endingBalanceDebit.doubleValue());
                    } else {
                        subject[10] = DoubleUtils.getNumber(endingBalanceDebit.doubleValue());
                    }
                    BigDecimal endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit();
                    if (null == endingBalanceCredit) {
                        endingBalanceCredit = new BigDecimal(0);
                    }
                    if (endingBalanceCredit.compareTo(BigDecimal.ZERO) == 0) {
                        subject[11] = DoubleUtils.getNumber(endingBalanceCredit.doubleValue());
                    } else {
                        subject[11] = DoubleUtils.getNumber(endingBalanceCredit.doubleValue());
                    }

                    list.add(subject);
                }
                String[] colName = new String[]{"期间", "科目代码", "科目名称", "币别", "期初余额(借方)", "期初余额(贷方)", "本期发生额(借方)",
                        "本期发生额(贷方)", "本年累计发生额(借方)", "本年累计发生额(贷方)", "期末余额(借方)", "期末余额(贷方)"};
                DataExcelExportUtil dataExcelExportUtil = new DataExcelExportUtil();
                XSSFWorkbook wb = dataExcelExportUtil.exportExcelWorkbook("科目余额表数据导出", colName, list, true,
                        querySubMessage);
                String fileName = new String("科目余额表数据导出.xlsx".getBytes("GBK"), "ISO8859-1");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                wb.write(response.getOutputStream());
                response.getOutputStream().flush();
                result.put("success", "导出科目余额表excle数据成功");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectExcleExport】,科目余额表页面excle导出功能出错!", e);
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectExcleExport】,科目余额表页面excle导出功能出错!", e);
            result.put("success", "false");
            return result;
        }
        return result;
    }

    // 这是APP科目余额表查询接口
    @RequestMapping("/querySbujectBalanceAPP")
    @ResponseBody
    public Map<String, Object> querySbujectBalanceAPP(@RequestParam(value = "period", required = true) String period,
                                                      @RequestParam(value = "accountID", required = true) String accountID,
                                                      @RequestParam(value = "keyWord", required = false) String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();

        /*
         * accountID = "e9f46fb6f8b845239aebc34889d1eab8"; period = "2018-04";
         */

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("period", period);
            param.put("accountID", accountID);
            param.put("keyWord", keyWord);
            Map<String, Object> subMap = tBasicSubjectMessageService.querySbujectBalanceAPP(param);
            if (subMap != null && subMap.size() > 0) {
                result.put("code", 0);
                result.put("info", 0);
                result.put("type", "km");
                result.put("msg", subMap);
            } else {
                result.put("code", 0);
                result.put("info", -1);
                result.put("type", "km");
                result.put("msg", "未查询到数据");
            }

        } catch (BusinessException e) {
            result.put("msg", "获取数据失败");
            result.put("code", -1);
            result.put("type", "km");
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectBalance】,查询系统中该账套的全部科目出错", e);
        } catch (Exception e) {
            result.put("code", -1);
            result.put("type", "km");
            result.put("msg", "获取数据失败");
            e.printStackTrace();
            logger.error("SbujectBalanceController【querySbujectBalance】,查询系统中该账套的全部科目出错", e);
        }
        return result;
    }
}
