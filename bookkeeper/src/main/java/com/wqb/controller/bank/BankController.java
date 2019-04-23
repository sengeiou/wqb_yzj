package com.wqb.controller.bank;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.dao.bank.Bank2SubjectDao;
import com.wqb.dao.bank.TccbBankDao;
import com.wqb.model.Account;
import com.wqb.model.Bank2Subject;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.model.bank.BankBill;
import com.wqb.service.bank.BankService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Component
@Controller
@RequestMapping("/bank")
public class BankController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(BankController.class);

    @RequestMapping("/importView")
    public ModelAndView init(ModelAndView modelAndView) {
        modelAndView.setViewName("bank/import");
        return modelAndView;
    }

    @RequestMapping("/setBank")
    public ModelAndView setBank(ModelAndView modelAndView) {
        modelAndView.setViewName("bank/setBank");
        return modelAndView;
    }

    @Autowired
    BankService bankService;

    @Autowired
    Bank2SubjectDao bank2SubjectDao;

    @Autowired
    TccbBankDao tccbBankDao;
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    // 导入银行对账单
    @RequestMapping(value = "/uploadFile")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(required = false) MultipartFile file, HttpServletRequest request,
                                      ModelMap model) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            try {
                Map<String, Object> map = bankService.uploadBankBill(file, request);
                return map;
            } catch (Exception e) {
                result.put("success", "fail");
                result.put("message", e.getMessage());
                return result;
            }
        } catch (Exception e) {
            logger.error("导入失败", e);
            result.put("success", "fail");
            result.put("message", e.getMessage());
            return result;
        }
    }

    // 分页查询银行对账单

    /**
     * @param dfAccountName 对方户名
     * @param curPage       当前页码
     * @param beginTime     启始日期(交易日期)
     * @param endTime       截止日期(交易日期)
     * @return
     */
    @RequestMapping(value = "/queryBankBill")
    @ResponseBody
    public Map<String, Object> queryBankBill(String keyWords, String curPage, String beginTime, String endTime,
                                             String bankType) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            String busDate = (String) sessionMap.get("busDate");
            Account account = (Account) sessionMap.get("account");
            // 每页显示十行
            Integer maxPage = 1000;
            Map<String, Object> param = new HashMap<String, Object>();
            if ("0".equals(bankType)) {
                param.put("bankType", "全部");
            } else if ("1".equals(bankType)) {
                param.put("bankType", "工商银行");
            } else if ("2".equals(bankType)) {
                param.put("bankType", "建设银行");
            } else if ("3".equals(bankType)) {
                param.put("bankType", "交通银行");
            } else if ("4".equals(bankType)) {
                param.put("bankType", "深圳农商行");
            } else if ("5".equals(bankType)) {
                param.put("bankType", "平安银行");
            } else if ("6".equals(bankType)) {
                param.put("bankType", "招商银行");
            } else if ("7".equals(bankType)) {
                param.put("bankType", "中国银行");
            } else if ("8".equals(bankType)) {
                param.put("bankType", "中信银行");
            } else if ("9".equals(bankType)) {
                param.put("bankType", "中国农业银行");
            }
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            if (null != keyWords && !"".equals(keyWords)) {
                param.put("keyWords", keyWords);
            }
            if (null != beginTime && !"".equals(beginTime)) {
                param.put("beginTime", beginTime);
            }
            if (null != endTime && !"".equals(endTime)) {
                param.put("endTime", endTime);
            }
            param.put("maxPage", maxPage);
            List<BankBill> totalList = bankService.queryBankBill(param);
            param.put("curPage", curPage);
            List<BankBill> list = bankService.queryBankBill(param);

            if ("0".equals(bankType)) {
                List<BankBill> returnList = new ArrayList<BankBill>();
                Integer curPageInt = Integer.parseInt(curPage);
                if (totalList.size() / maxPage + 1 == curPageInt) {
                    Integer begin = (curPageInt - 1) * (Integer.parseInt(maxPage.toString()));
                    for (int i = begin; i < totalList.size(); i++) {
                        returnList.add(totalList.get(i));
                    }
                } else {
                    Integer begin = (curPageInt - 1) * (Integer.parseInt(maxPage.toString()));
                    for (int i = begin; i < begin + maxPage; i++) {
                        returnList.add(totalList.get(i));
                    }
                }

                result.put("list", returnList);
            } else {
                if (list != null && list.size() > 0) {
                    if (list.size() <= maxPage) {
                        result.put("list", list);
                    } else {
                        List<BankBill> returnList = new ArrayList<BankBill>();
                        for (int i = 0; i < maxPage; i++) {
                            returnList.add(list.get(i));
                        }
                        result.put("list", returnList);
                    }
                } else {
                    result.put("list", null);
                }
            }
            result.put("success", "true");
            result.put("totalCount", totalList.size());
            result.put("maxPage", maxPage);
            // result.put("list", list);
        } catch (BusinessException e) {
            result.put("success", "fail");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            return result;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(result);
            System.out.print(jsonString);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 批量删除银行对账单
    @RequestMapping(value = "/deleteBankBill")
    @ResponseBody
    public Map<String, Object> deleteBankBill(String billIDs) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            bankService.deleteByID(billIDs);
            result.put("success", "true");
            result.put("message", "删除银行对账单success!");
        } catch (BusinessException e) {
            result.put("success", "fail");
            result.put("message", "删除银行对账单fail!");
            return result;
        } catch (Exception e) {
            result.put("success", "fail");
            result.put("message", "删除银行对账单fail!");
            return result;
        }
        return result;
    }

    // 查询银行清单列表
    @RequestMapping(value = "/queryBankList")
    @ResponseBody
    public Map<String, Object> queryBankList(String billIDs) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<String> list = new ArrayList<String>();
            list.add("全部银行");
            list.add("工商银行");
            list.add("建设银行");
            list.add("交通银行");
            list.add("深圳农村商业银行");
            list.add("平安银行");
            list.add("招商银行");
            list.add("中国银行");
            list.add("中信银行");
            list.add("中国农业银行");
            result.put("success", "true");
            result.put("list", list);
        } catch (Exception e) {
            result.put("success", "fail");
            result.put("message", "查询银行下拉列表异常!");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/downBankExcel")
    public void downJxExcel(HttpServletResponse response, String bankType) throws Exception {

        if ("1".equals(bankType)) {
            // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "工商银行.xls",
                    "工商银行-" + DateUtil.getDays() + ".xls");
        } else if ("2".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "建设银行.xls",
                    "建设银行-" + DateUtil.getDays() + ".xls");
        } else if ("3".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "交通银行.xls",
                    "交通银行-" + DateUtil.getDays() + ".xls");
        } else if ("4".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "深圳农村商业银行.xls",
                    "深圳农村商业银行-" + DateUtil.getDays() + ".xls");
        } else if ("5".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "平安银行.xls",
                    "平安银行-" + DateUtil.getDays() + ".xls");
        } else if ("6".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "招商银行.xls",
                    "招商银行-" + DateUtil.getDays() + ".xls");
        } else if ("7".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "中国银行.xls",
                    "中国银行-" + DateUtil.getDays() + ".xls");
        } else if ("8".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "中信银行.xls",
                    "中信银行-" + DateUtil.getDays() + ".xls");
        } else if ("9".equals(bankType)) {
            FileDownload.filedownload(response,
                    PathUtil.getClasspath() + Constrants.FILEPATHFILE + "bank/" + "中国农业银行.xls",
                    "中国农业银行-" + DateUtil.getDays() + ".xls");
        }
    }

    @RequestMapping(value = "/queryPage")
    public void queryPage(String keyWords, String curPage, Date beginTime, Date endTime) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            String busDate = (String) sessionMap.get("busDate");
            Account account = (Account) sessionMap.get("account");
            // 每页显示十行
            Integer maxPage = 1000;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            if (null != keyWords && !"".equals(keyWords)) {
                param.put("keyWords", keyWords);
            }
            if (null != beginTime && !"".equals(beginTime)) {
                param.put("beginTime", beginTime);
            }
            if (null != endTime && !"".equals(endTime)) {
                param.put("endTime", endTime);
            }
            param.put("maxPage", maxPage);
            bankService.queryPage(param);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/bankVouch")
    public void bankVouch(HttpSession session) {
        try {
            bankService.bankVouch(session);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/queryBank2Subject")
    @ResponseBody
    Map<String, Object> queryBank2Subject() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            Account account = getAccount();
            param.put("accountID", account.getAccountID());
            List<Bank2Subject> list = bank2SubjectDao.queryBank2Subject(param);
            if (list != null && list.size() > 0) {
                result.put("success", "true");
                result.put("list", list);
            } else {
                result.put("success", "false");
            }
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("获取用户是否配置了银行账户和科目映射引发异常！");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/addBank2Subject")
    @ResponseBody
    Map<String, Object> addBank2Subject() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Bank2Subject b2s = new Bank2Subject();
            // 银行账户
            b2s.setBankAccount(getRequest().getParameter("bankAccount").trim());
            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("bankAccount", getRequest().getParameter("bankAccount").trim());
            List<Bank2Subject> list = bank2SubjectDao.queryByBankAccount(pa);
            boolean isExists = false;
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (getRequest().getParameter("bankAccount").trim().equals(list.get(i).getBankAccount())) {
                        isExists = true;
                        break;
                    }
                }
            }
            if (isExists) {
                result.put("success", "false");
                result.put("msg", "账号已经存在,不允许重复添加");
                logger.error("账号已经存在,不允许重复添加");
                return result;
            }

            b2s.setId(UUIDUtils.getUUID());
            // 账套ID
            b2s.setAccountID(getAccount().getAccountID());
            // 获取银行名称
            b2s.setBankName(getRequest().getParameter("bankName"));
            // 银行类型
            b2s.setBankType(getRequest().getParameter("bankType"));
            // 币别
            b2s.setCurrency(getRequest().getParameter("currency"));

            // 科目主键
            b2s.setSubID(getRequest().getParameter("subID"));
            // 科目编码
            b2s.setSubCode(getRequest().getParameter("subCode"));
            // 科目名称
            b2s.setSubName(getRequest().getParameter("subName"));
            // 科目全名
            b2s.setSubFullName(getRequest().getParameter("subFullName"));
            b2s.setCreateID(getUser().getUserID());
            b2s.setCreateName(getUser().getUserName());
            b2s.setCreateTel(getUser().getLoginUser());
            b2s.setCreateTime(new Date());

            int count = bank2SubjectDao.addBank2Subject(b2s);
            if (count == 0) {
                result.put("success", "false");
                logger.error("添加银行账户与科目映射失败！");
                return result;
            } else if (count == 1) {
                result.put("success", "true");
                logger.error("添加银行账户与科目映射成功！");
                return result;
            }
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("添加银行账户与科目映射异常！");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/updBank2Subject")
    @ResponseBody
    Map<String, Object> updBank2Subject() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            Bank2Subject b2s = new Bank2Subject();
            b2s.setId(getRequest().getParameter("id"));
            // 获取银行名称
            b2s.setBankName(getRequest().getParameter("bankName"));
            // 银行类型
            b2s.setBankType(getRequest().getParameter("bankType"));
            // 币别
            b2s.setCurrency(getRequest().getParameter("currency"));
            // 银行账户
            // b2s.setBankAccount(getRequest().getParameter("bankAccount"));
            // 科目主键
            b2s.setSubID(getRequest().getParameter("subID"));
            // 科目编码
            b2s.setSubCode(getRequest().getParameter("subCode"));
            // 科目名称
            b2s.setSubName(getRequest().getParameter("subName"));
            // 科目全名
            b2s.setSubFullName(getRequest().getParameter("subFullName"));
            int count = bank2SubjectDao.updBank2Subject(b2s);
            if (count == 0) {
                result.put("success", "false");
                logger.error("修改银行账户与科目映射失败！");
                return result;
            } else if (count == 1) {
                result.put("success", "true");
                logger.error("修改银行账户与科目映射成功！");
                return result;
            }
        } catch (Exception e) {
            result.put("success", "false");
            logger.error("修改银行账户与科目映射异常！");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/updBank")
    @ResponseBody
    Map<String, Object> updBank() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String infos = getRequest().getParameter("infos");
            result = bankService.updBank(infos);
        } catch (Exception e) {
            return result;
        }
        return result;

    }

    @RequestMapping(value = "/queryMjBankSubject")
    @ResponseBody
    Map<String, Object> queryMjBankSubject() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicSubjectMessage> mjSub = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            Account account = getAccount();
            String period = getUserDate();
            param.put("accountID", account.getAccountID());
            // param.put("period", getUserDate());
            param.put("subCode", "1002");
            param.put("period", period);
            List<TBasicSubjectMessage> list = tBasicSubjectMessageService.querySysBankSubject(param);
            if (list != null && list.size() != 0) {
                mjSub = SubjectUtils.getMjSub(list);
            }
        } catch (Exception e) {
            result.put("success", "false");
            result.put("msg", "获取银行科目下拉列表异常");
            return result;
        }
        result.put("success", "true");
        result.put("list", mjSub);
        return result;
    }

    @RequestMapping(value = "/queryBank2SubjectByID")
    @ResponseBody
    Map<String, Object> queryBank2SubjectByID() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Bank2Subject> list = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", getRequest().getParameter("id"));
            list = bank2SubjectDao.queryBank2SubjectByID(param);

        } catch (Exception e) {
            result.put("success", "false");
            result.put("msg", "获取银行科目下拉列表异常");
            return result;
        }
        result.put("success", "true");
        result.put("bank2Subject", list.get(0));
        return result;
    }
}
