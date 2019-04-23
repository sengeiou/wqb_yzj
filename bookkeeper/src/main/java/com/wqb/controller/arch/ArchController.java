package com.wqb.controller.arch;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.Arch;
import com.wqb.model.Page;
import com.wqb.service.arch.ArchService;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/arch")
public class ArchController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(ArchController.class);

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    ArchService archService;

    @Autowired
    PeriodStatusService periodStatusService;

    // 薪资导入
    @RequestMapping("/uploadArch")
    @ResponseBody
    Map<String, Object> uploadAssets(MultipartFile uploadArch) {
        Map<String, Object> result = new HashMap<String, Object>();
        ModelAndView mdv = new ModelAndView();
        List<Map<String, Object>> list = null;

        Map<String, String> map2 = new HashMap<>();
        String userID = getUser().getUserID(); //用户id
        String userName = getUser().getUserName(); //用户姓名
        String accountID = getAccount().getAccountID(); //账套id
        String account_period = getUserDate(); //账套期间
        //account_period = "2017-12";
        map2.put("accountID", accountID);//账套id
        map2.put("account_period", account_period);
        map2.put("userID", userID);
        try {
            //已经结账本期不允许再导入
            String isjz = periodStatusService.queryAccStatus(accountID, account_period);
            if (isjz != null && isjz.equals("1")) {
                result.put("message", "fail");
                result.put("result", "本期已经结账，禁止导入");
                return result;
            }

            if (uploadArch != null && !uploadArch.isEmpty()) {
                // 1获取文件上传路径
//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE; // wtpwebapps/wqb/WEB-INF/classes/../../files/
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + account_period + "/";

                // 2获取文件名
                String fileName = FileUpload.fileUp(uploadArch, filePath,
                        "arch-" + userID + "-" + System.currentTimeMillis());
                // 3读取excel表格数据
                list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                if (list == null || list.size() == 0) {
                    result.put("message", "fail");
                    result.put("result", "导入失败，请核对表格内容的格式后再式。");
                    return result;
                }
                // 4调用薪资业务层添加到数据库
                Map<String, Object> mes = archService.insertBath(list, map2);
                if (!StringUtil.isEmpty(((String) mes.get("fail")))) {
                    result.put("result", mes.get("fail").toString());
                    result.put("message", "fail");
                    return result;
                }
                result.put("message", "success"); // "导入完毕，共导入"+count+"条数据。"
                result.put("result", mes.get("success").toString());
                return result;
            }
            result.put("result", "上传文件不存在");
            result.put("message", "fail");
            return result;
        } catch (BusinessException e) {
            logger.error("ArchController【uploadArch】 上传异常!", e);
            e.printStackTrace();
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ArchController【uploadArch】 上传异常!", e);
            e.printStackTrace();
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        }

    }

    //列表页
    @RequestMapping("/list")
    @ResponseBody
    Map<String, Object> list(@RequestParam(value = "currPage", required = false, defaultValue = "1") Integer currentPage,
                             @RequestParam(value = "acName", required = false) String acName, @RequestParam(value = "archDate", required = false) String archDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        Integer pageSzie = 30;
        try {
            param.put("accountID", getAccount().getAccountID()); // 账套ID
            param.put("acName", acName); // 查询姓名
            param.put("currentPage", currentPage); //当前页dl
            param.put("archDate", archDate);
            param.put("acperiod", getUserDate());
            Page<Arch> page = archService.queryListPage(param);
            result.put("result", page);
            result.put("message", "success");
            return result;
        } catch (BusinessException e) {
            logger.error("ArchController【list】 操作异常!", e);
            e.printStackTrace();
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ArchController【list】 操作异常!!", e);
            e.printStackTrace();
            result.put("message", "fail");
            result.put("result", e.getMessage());

            return result;
        }

    }

    // 薪资删除
    @RequestMapping("/del")
    @ResponseBody
    // 参数当前页 查询月份 名称
    Map<String, Object> del(String archID) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<>();
        try {
            param.put("archID", archID); // 主键
            param.put("accountID", getAccount().getAccountID());// 账套ID
            archService.delById(param);
            result.put("message", "success");
            result.put("result", "操作成功!");
            return result;
        } catch (BusinessException e) {
            logger.error("ArchController【del】 删除失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ArchController【del】 删除失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
        }
        return result;
    }

    // 批量删除
    @RequestMapping("/delAll")
    @ResponseBody
    Map<String, Object> delAll(String ids) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        try {
            if (ids != null && !"".equals(ids)) {
                String[] arr = ids.split(",");
                map.put("ids", arr); // 主键
                map.put("accountID", getAccount().getAccountID());// 账套ID
                archService.delBathById(map);
                result.put("message", "success");
                result.put("result", "操作成功!");
                return result;
            }
            result.put("message", "fail");
            result.put("result", "操作失败!");
            return result;
        } catch (BusinessException e) {
            logger.error("ArchController【delAll】 批量删除失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("ArchController【delAll】 批量删除失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        }
    }

    // 薪资模板excel下载
    @RequestMapping(value = "/downExcel")
    public void downExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "工资表.xls",
                "工资表-" + DateUtil.getDays() + ".xls");
    }


    /*
     * 获取薪资表中所涉猎部门
     */
    @RequestMapping("/queryArchDate")
    @ResponseBody
    Map<String, Object> queryArchDate() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("acperiod", getUserDate());
            map.put("accountID", getAccount().getAccountID());
            String archDate = archService.queryArchDate(map);
            result.put("message", "success");
            result.put("result", archDate);
        } catch (BusinessException e) {
            logger.error("ArchController【queryArchDate】 获取做账期间工资月份异常!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
        } catch (Exception e) {
            logger.error("ArchController【queryArchDate】 获取做账期间工资月份异常!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
        }
        return result;
    }


    /*
     * 获取薪资表中所涉猎部门
     */
    @RequestMapping("/queryDepart")
    @ResponseBody
    Map<String, Object> queryDepart() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> list = archService.queryDepart(getSession());
            result.put("success", "true");
            result.put("list", list);
        } catch (BusinessException e) {
            logger.error("ArchController【queryDepart】 获取薪资部门列表异常!", e);
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            logger.error("ArchController【queryDepart】 获取薪资部门列表异常!", e);
            result.put("success", "false");
            return result;
        }
        return result;
    }


    public Map<String, String> getUserInfo() {
        Map<String, String> map = new HashMap<>();
        String userID = getUser().getUserID(); //用户id
        String userName = getUser().getUserName(); //用户姓名
        String accountID = getAccount().getAccountID(); //账套id
        String account_period = getUserDate(); //账套id
        /*account_period = "2017-12";*/
        map.put("accountID", accountID);//账套id
        map.put("userID", userID);
        map.put("userName", userName);
        map.put("period", account_period);
        return map;
    }


    @RequestMapping("/importView")
    public ModelAndView init(ModelAndView modelAndView) {
        modelAndView.setViewName("salary/import");
        return modelAndView;
    }


}
