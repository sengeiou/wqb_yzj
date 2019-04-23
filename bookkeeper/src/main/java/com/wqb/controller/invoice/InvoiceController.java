package com.wqb.controller.invoice;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.controller.assets.AssetsController;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.invoice.InvoiceMappingDao;
import com.wqb.model.*;
import com.wqb.service.invoice.InvoiceService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.stateTrack.StateTrackService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseController {

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    InvoiceService invoiceService;
    private static Log4jLogger logger = Log4jLogger.getLogger(AssetsController.class);

    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    VoucherHeadService voucherHeadService;
    @Autowired
    StateTrackService stateTrackService;

    @Autowired
    InvoiceMappingDao invoiceMappingDao;

    @Autowired
    PeriodStatusService periodStatusService;

    // 导入发票
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/uploadFile")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {
        String message = null;
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> res = null;
        try {
            // 获取用户信息
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userID = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountID = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");
            List<Map<String, Object>> list = null;
            Map<String, Object> qerMap = new HashMap<>();
            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);
            qerMap.put("busDate", busDate);

            //para.put("accountID", account.getAccountID());
            //para.put("busDate", busDate);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(qerMap);
            if (!statuList.isEmpty()) {
                StatusPeriod sp = statuList.get(0);
                if (sp.getIsCreateVoucher() == 1) {
                    result.put("code", "2");
                    result.put("msg", "已经一键生成凭证，禁止再导入数据");
                    return result;
                }
            }


            //int num = voucherHeadService.queryCountVouch(qerMap);
			/*if (num > 0) {
				result.put("code", "2");
				result.put("msg", "系统已生成凭证，禁止再导入数据");
				return result;
			}*/

            if (file != null && !file.isEmpty()) {
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath, "invoice-" + System.currentTimeMillis());
                list = ReadExcal.readExcel(filePath, fileName, 0, 0);

                if (null == list || list.size() == 0) {
                    result.put("code", "3");
                    result.put("msg", "没有可以导入的相关数据");
                    return result;
                }
            }

            Map<String, Object> oneMap = list.get(0);
            String invoiceFlag = oneMap.get("map0") == null ? null : oneMap.get("map0").toString();

            // 已经导入再导入提示
            qerMap.put("invoiceType", "1"); // 查询进项
            int num2 = invoiceDao.queryInvobCount(qerMap);

            qerMap.put("invoiceType", "2");
            int num3 = invoiceDao.queryInvobCount(qerMap); // 查询销项


            if ("发票清单".equals(invoiceFlag)) {
                if (num2 > 0) {
                    result.put("code", "4");
                    result.put("msg", "本期已经导入过进项发票");
                    return result;
                }
            } else {
                if (num3 > 0) { // 销项发票
                    result.put("code", "5");
                    result.put("msg", "本期已经导入过销项发票");
                    return result;
                }
            }

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("accountID", accountID);
            hashMap.put("period", busDate);
            String invoice_type = null;
            String invoice_crop = null;
            if ("发票清单".equals(invoiceFlag)) {
                // 进项发票 写入数据库
                res = invoiceService.jxInvoice2Data(list, getSession());
                invoice_type = "1";
                invoice_crop = "销方";
            } else {
                // 销项发票
                res = invoiceService.xxInvoice2Data(list, getSession());
                invoice_type = "2";
                invoice_crop = "购方";
            }
            result.put("invoice_type", invoice_type);
            result.put("invoice_crop", invoice_crop);
            invoiceService.upMappingrecord(accountID, busDate, invoice_type, 1);
        } catch (Exception e) {
            logger.error("导入失败", e);
            result.put("code", "1");
            result.put("msg", e.getMessage());
            return result;
        }
        result.put("code", "0");
        result.put("msg", res);
        return result;
    }


    // 测试 导入发票 有页面
    @RequestMapping(value = "/testUploadInvoice")
    @ResponseBody
    ModelAndView testuploadInvoice(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> res = null; //定义弹框页面返回数据
        ModelAndView modelView = new ModelAndView();

        modelView.addObject("ceshi", "555");
        try {
            // 获取用户信息
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息
            String userID = user.getUserID();// 用户id
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountID = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");
            List<Map<String, Object>> list = null;
            Map<String, Object> qerMap = new HashMap<>();
            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);
            qerMap.put("busDate", busDate);

		/*	int num = voucherHeadService.queryCountVouch(qerMap);
			if (num > 0) {
				result.put("code", "1");
				result.put("msg", "系统已生成凭证，禁止再导入数据");
				modelView.addAllObjects(result);
				return modelView;
			}*/

            if (file != null && !file.isEmpty()) {
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + busDate + "/";
                String fileName = FileUpload.fileUp(file, filePath, "invoice-" + System.currentTimeMillis());
                list = ReadExcal.readExcel(filePath, fileName, 0, 0);

                if (null == list || list.size() == 0) {
                    result.put("code", "1");
                    result.put("msg", "没有可以导入的相关数据");
                    modelView.setViewName("error");
                    modelView.addAllObjects(result);
                    return modelView;
                }
            }

            Map<String, Object> oneMap = list.get(0);
            String invoiceFlag = oneMap.get("map0") == null ? null : oneMap.get("map0").toString();


            // 已经导入再导入提示
            qerMap.put("invoiceType", "1"); // 查询进项
            int num2 = invoiceDao.queryInvobCount(qerMap);

            qerMap.put("invoiceType", "2");
            int num3 = invoiceDao.queryInvobCount(qerMap); // 查询销项


            if ("发票清单".equals(invoiceFlag)) {
                if (num2 > 0) {
                    result.put("code", "2");
                    modelView.addAllObjects(result);
                    result.put("msg", "已经导入进项发票");
                    modelView.setViewName("error");
                    return modelView;
                }
            } else {
                if (num3 > 0) { // 销项发票
                    result.put("code", "2");
                    modelView.addAllObjects(result);
                    result.put("msg", "已经导入销项发票");
                    modelView.setViewName("error");
                    return modelView;
                }
            }
            if ("发票清单".equals(invoiceFlag)) {
                // 进项发票 写入数据库
                res = invoiceService.jxInvoice2Data(list, getSession());
                result.put("invoice_type", "1");
                result.put("invoice_crop", "销方");
                invoiceService.upMappingrecord(accountID, busDate, "1", 1);
            } else {
                // 销项发票 写入数据库
                res = invoiceService.xxInvoice2Data(list, getSession());
                result.put("invoice_type", "2");
                result.put("invoice_crop", "购方");
                invoiceService.upMappingrecord(accountID, busDate, "2", 1);
            }

        } catch (Exception e) {
            result.put("code", "1");
            result.put("msg", e.getMessage());
            modelView.setViewName("error");
            modelView.addAllObjects(result);
            return modelView;
        }
        result.put("code", "0");
        result.put("msg", res);
        result.put("laiyuan", "testUploadInvoice");
        modelView.setViewName("report/invoice2Mapping");
        modelView.addAllObjects(result);
        return modelView;
    }


    // 不定项多条件查询发票
    @RequestMapping(value = "/queryInvoice")
    @ResponseBody
    Map<String, Object> queryInvoice(String invoiceType, String beginTime, String endTime, String keyWords,
                                     String curPage) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            int pageSize = 10;
            // 第一次进属于查询全部0:全部1:进项2:销项
            param.put("invoiceType", invoiceType);
            param.put("pageSize", pageSize);
            if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                param.put("beginTime", DateUtil.fomatDate(beginTime));
                param.put("endTime", DateUtil.fomatDate(endTime));
            }
            // keyWords = "深圳";
            if (!StringUtil.isEmpty(keyWords)) {
                param.put("keyWords", keyWords);
            }
            if (!StringUtil.isEmpty(curPage)) {
                param.put("begin", (Integer.parseInt(curPage) - 1) * pageSize);
                param.put("end", pageSize);
            }
            List<Object> li = new ArrayList<Object>();
            List<InvoiceHead> list = invoiceService.queryInvoice(param, getSession());
            if (list != null && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data.put("invoiceHead", list.get(j));
                    String invoiceHID = list.get(j).getInvoiceHID();
                    List<InvoiceBody> bodyList = invoiceDao.queryInvByHid(invoiceHID);
                    data.put("bodyList", bodyList);
                    li.add(data);
                }
            }
            param.put("begin", null);
            param.put("end", null);
            List<InvoiceHead> totalList = invoiceService.queryInvoice(param, getSession());
            result.put("message", "success");
            result.put("list", li);
            result.put("totalCount", totalList.size());
            result.put("pageSize", pageSize);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("不定项多条件分页查询发票信息异常", e);
            return result;
        } catch (Exception e) {
            result.put("message", "fail");
            logger.error("不定项多条件分页查询发票信息异常", e);
            return result;
        }
        return result;
    }

    // 批量删除发票
    @RequestMapping(value = "/deleteInvoice")
    @ResponseBody
    //invoiceBIDs hid与bid主键用中划线拼接的字符串   aca800abfac4479e91ad1d77c71339f5-4b9bc8291ad54bd3929ace77a3f337fb
    //invoiceType 1进 2销
    Map<String, Object> deleteInvoice(@RequestParam(required = true) String invoiceBIDs, @RequestParam(required = true) String invoiceType) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            System.out.println(invoiceBIDs);
            System.out.println(invoiceType);

            invoiceService.deleteInvoice(invoiceBIDs, invoiceType);

        } catch (BusinessException e) {
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;
        }
        result.put("msg", "success");
        result.put("code", "0");
        return result;
    }

    // delFaPiaoAll
    // 删除所有发票  如果重新导入将会先把之前导入的进项和销项发票全部删除 然后再可以导入新的数据
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/delFaPiaoAll")
    @ResponseBody
    Map<String, Object> delFaPiaoAll() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String accountID = account.getAccountID();
            String busDate = (String) sessionMap.get("busDate");

            Map<String, Object> qerMap = new HashMap<>();

            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);

            invoiceService.delFaPiao(qerMap);
            //删除之后要把映射表记录进项更改
            invoiceService.upMappingrecord(accountID, busDate, "1", 3);
            invoiceService.upMappingrecord(accountID, busDate, "2", 3);

            //stateTrackService.upkuchu(qerMap);  //发票导入与数量金额表分离 不需要再去删除数量金额表了
            //invoiceDao.delFaPiao2(qerMap);
            result.put("code", "0");
            return result;
        } catch (Exception e) {
            result.put("code", "1");
            result.put("msg", e.getMessage());
            logger.error("批量删除发票信息异常", e);
            return result;
        }

    }


    //正式接口  没有页面 发票映射弹框刷新请求接口
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/queryInvoiceMapping")
    @ResponseBody
    Map<String, Object> queryInvoiceMapping(String yjInvoicetype) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String accountID = account.getAccountID();
            String busDate = (String) sessionMap.get("busDate");

            Map<String, Object> qerMap = new HashMap<>();

            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);
            qerMap.put("busDate", busDate);

            Map<String, Object> map = null;
            if (StringUtil.isEmpty(yjInvoicetype)) {
                map = invoiceService.queryInvoiceMapping(qerMap);
            } else {
                map = invoiceService.queryInvoiceMapping(accountID, busDate, yjInvoicetype);
            }

            String code = map.get("code").toString();
            if (code.equals("1")) { //已经保存过
                res.put("code", "1");
                return res;
            } else if (code.equals("0")) { //未保存过
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("result");
                String invoiceType = (String) map.get("invoiceType");

                res.put("code", "0");
                res.put("msg", list);

                res.put("invoice_type", invoiceType);
                if (invoiceType.equals("1")) {
                    res.put("invoice_crop", "销方");
                } else {
                    res.put("invoice_crop", "购方");
                }
                res.put("laiyuan", "queryInvoiceMapping");
                return res;
            } else {
                res.put("code", "2");
                res.put("msg", "未知错误");
                return res;
            }
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "2");
            return res;
        }

    }


    //测试接口 有页面 发票映射弹框刷新请求接口
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/testqueryInvoiceMapping")
    @ResponseBody
    //发票映射刷新重新请求   或者 从一键生成凭证跳转过来
    //yjInvoicetype 一键生成凭证传递过来
    ModelAndView testqueryInvoiceMapping(String yjInvoicetype) {
        Map<String, Object> res = new HashMap<String, Object>();
        ModelAndView modelView = new ModelAndView();

        try {
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String accountID = account.getAccountID();
            String busDate = (String) sessionMap.get("busDate");

            Map<String, Object> qerMap = new HashMap<>();

            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);
            qerMap.put("busDate", busDate);
            Map<String, Object> map = null;
            if (!StringUtil.isEmpty(yjInvoicetype)) {
                map = invoiceService.queryInvoiceMapping(qerMap);
            } else {
                map = invoiceService.queryInvoiceMapping(accountID, busDate, yjInvoicetype);
            }
            String code = map.get("code").toString();
            if (code.equals("1")) { //已经保存过
                res.put("code", "1");
                res.put("msg", "已经保存过");
                res.put("laiyuan", "testqueryInvoiceMapping");
                modelView.setViewName("error");
                modelView.addAllObjects(res);
                return modelView;

            } else if (code.equals("0")) { //未保存过
                //发票映射数据
                List<Map<String, Object>> shuju = (List<Map<String, Object>>) map.get("result");
                String invoiceType = (String) map.get("invoiceType");

                res.put("code", "0");
                res.put("msg", shuju); //给弹框使用

                res.put("invoice_type", invoiceType);
                if (invoiceType.equals("1")) {
                    res.put("invoice_crop", "销方");
                } else {
                    res.put("invoice_crop", "购方");
                }
                modelView.setViewName("report/invoice2Mapping");
                modelView.addAllObjects(res);
                return modelView;
            } else {
                res.put("code", "2");
                res.put("msg", "未知错误");
                modelView.setViewName("error");
                modelView.addAllObjects(res);
                return modelView;
            }
        } catch (Exception e) {
            res.put("code", "1");
            res.put("msg", e.getMessage());
            modelView.setViewName("error");
            modelView.addAllObjects(res);
            return modelView;
        }

    }


    // 判断发票弹框数据是否已经保存
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/queryInvoiceMappingStatu")
    @ResponseBody
    Map<String, Object> queryInvoiceMappingStatu() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String accountID = account.getAccountID();
            String busDate = (String) sessionMap.get("busDate");
            Map<String, Object> map = invoiceService.queryInvoiceMappingStatu(accountID, busDate);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "2");
            return res;
        }
    }


    // 保存发票弹框数据 并记录保存状态
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveInvoiceMapping")
    @ResponseBody
    Map<String, Object> saveInvoiceMapping(@RequestParam(required = true) String invoiceType, @RequestParam(required = true) String contentDate) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(invoiceType)) {
                throw new BusinessException("saveInvoiceMapping invoiceType null");
            }
            if (!invoiceType.equals("1") && !invoiceType.equals("2")) {
                throw new BusinessException("saveInvoiceMapping invoiceType error val is " + invoiceType);
            }
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            String accountID = account.getAccountID();
            String busDate = (String) sessionMap.get("busDate");

            Map<String, Object> qerMap = new HashMap<>();

            qerMap.put("accountID", accountID);
            qerMap.put("period", busDate);
            qerMap.put("busDate", busDate);

            qerMap.put("contentDate", contentDate);

            //保存发票映射数据
            invoiceService.saveInvoiceMapping(qerMap);

            //把保存记录添加到数据库中去
            int num = invoiceService.upMappingrecord(accountID, busDate, invoiceType, 2);

            System.out.println(num);

            res.put("msg", "success");
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "1");
            return res;
        }

    }


    // 发票编辑第一步
    @RequestMapping(value = "/invoiceEditOne")
    @ResponseBody
    Map<String, Object> invoiceEditOne(@RequestParam(required = true) String invoiceBID) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("invoiceBID", invoiceBID);
            InvoiceBody body = invoiceDao.queryInvoiceByBid(map);
            if (body == null) {
                res.put("msg", invoiceBID + "invoiceBID没有查询到相关数据");
                res.put("code", "1");
                return res;
            } else {

                String invoiceHID = body.getInvoiceHID();
                map.put("invoiceHID", invoiceHID);
                InvoiceHead head = invoiceDao.queryInvoiceByHid(map);
                map.clear();

                map.put("body", body);
                map.put("head", head);

                res.put("msg", map);
                res.put("code", "0");
                return res;
            }
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "1");
            return res;
        }
    }

    // 发票编辑第二步
    @RequestMapping(value = "/invoiceEditTwo")
    @ResponseBody
    Map<String, Object> invoiceEditTwo(
            @RequestParam(required = true) String invoiceBID,
            @RequestParam(required = true) String subCode,
            @RequestParam(required = true) String subName) {
        Map<String, Object> res = new HashMap<String, Object>();

        try {
            Map<String, Object> map = new HashMap<>();

            InvoiceBody vb = new InvoiceBody();
            vb.setInvoiceBID(invoiceBID);
            vb.setSub_code(subCode);
            vb.setSub_full_name(subName);

            invoiceDao.editInvoice(vb);
            res.put("msg", "success");
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            res.put("msg", e.getMessage());
            res.put("code", "1");
            return res;
        }
    }


    // 下载模板excel
    @RequestMapping(value = "/downJxExcel")
    public void downJxExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "进项发票.xls",
                "进项发票-" + DateUtil.getDays() + ".xls");
    }

    // 下载模板excel
    @RequestMapping(value = "/downXxExcel")
    public void downXxExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "销项发票.xls",
                "销项发票-" + DateUtil.getDays() + ".xls");
    }

    @RequestMapping("/importView")
    public ModelAndView tiaozhuan2() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("invoice/import");
        return mav;
    }


}
