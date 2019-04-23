package com.wqb.controller.assets;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.model.Account;
import com.wqb.model.Assets;
import com.wqb.model.Page;
import com.wqb.model.SubjectMessage;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/assets")
public class AssetsController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AssetsController.class);

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    AssetsService assetsService;
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    AssetsDao assetsDao;

    // 固定资产excel表上传
    @RequestMapping("/uploadAssets")
    @ResponseBody
    Map<String, Object> uploadAssets(MultipartFile uploadAssets) {
        Map<String, Object> result = new HashMap<String, Object>();
        ModelAndView mdv = new ModelAndView();
        List<Map<String, Object>> list = null;
        try {
            String userID = getUser().getUserID(); // 用户id
            String accountID = getAccount().getAccountID(); // 账套id
            String userName = getUser().getUserName(); // 用户姓名
            String account_period = getUserDate(); // 账套id

            Map<String, String> map2 = new HashMap<>();
            map2.put("userID", userID);
            map2.put("period", account_period);
            map2.put("accountID", accountID);
            map2.put("userName", getUser().getUserName());

            // 已经结账本期不允许再导入
            String isjz = periodStatusService.queryAccStatus(accountID, account_period);
            if (isjz != null && isjz.equals("1")) {
                result.put("code", "1");
                result.put("msg", "本期已经结账，禁止导入");
                return result;
            }

            // 查询之前是否已经导入过
            Map<String, Object> map3 = new HashMap<>();
            map3.put("accountID", accountID);
            /*
             * Integer num = assetsService.queryCount(map3);
             *
             * if(num>0){ assetsService.delAllAss(map3); }
             */

            assetsService.delAllAss(map3);

            if (uploadAssets != null && !uploadAssets.isEmpty()) {
                // 1获取文件上传路径
                // String filePath = PathUtil.getClasspath() +
                // Constrants.FILEPATHFILE;
                // //wtpwebapps/wqb/WEB-INF/classes/../../files/
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + account_period + "/";

                // 2获取文件名
                String fileName = FileUpload.fileUp(uploadAssets, filePath,
                        "assets-" + userID + "-" + System.currentTimeMillis());
                // 3读取excel表格数据
                list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                if (list == null || (list != null && list.size() == 0)) {
                    result.put("code", "1");
                    result.put("msg", "导入失败，没有可以导入的数据。");
                    return result;
                }
                // 4调用固定资产业务层添加到数据库
                Map<String, Object> mes = assetsService.insertAssert(list, map2);
                if (!StringUtil.isEmpty(((String) mes.get("fail")))) {
                    result.put("msg", mes.get("fail").toString()); // 第" + (i +
                    // 1) +
                    // "行数据与第" +
                    // (j + 1) +
                    // "行数据重复,请仔细核查
                    result.put("code", "1");
                    return result;
                }
                result.put("code", "0"); // "导入完毕，共导入"+count+"条数据。"
                result.put("msg", "导入成功");
                return result;
            }
            result.put("msg", "上传文件不存在");
            result.put("code", "1");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【uploadAssets】 上传异常!", e);
            e.printStackTrace();
            result.put("code", "1");
            result.put("msg", "导入失败: " + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【uploadAssets】 上传异常!", e);
            e.printStackTrace();
            result.put("code", "1");
            result.put("msg", "导入失败: " + e.getMessage());
            return result;
        }

    }

    // 固定资产列表页
    @RequestMapping("/queryAss")
    @ResponseBody
    // 参数当前页 查询月份 名称
    Map<String, Object> queryAss() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String userID = getUser().getUserID();
            String accountID = getAccount().getAccountID();
            // 查询之前是否已经导入过
            Map<String, Object> map = new HashMap<>();
            map.put("accountID", accountID);
            Integer num = assetsService.queryCount(map);
            if (num > 0) {
                result.put("code", "2");
                result.put("msg", "已经导入过固定资产,再次导入将会覆盖之前的数据,确认需要导入吗?");
                return result;
            } else {
                result.put("code", "0");
                result.put("msg", "success");
            }

            return result;
        } catch (BusinessException e) {
            result.put("code", "1");
            result.put("msg", "查询失败: " + e.getMessage());
            return result;
        } catch (Exception e) {
            result.put("code", "1");
            result.put("msg", "查询失败: " + e.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request, Model model) throws Exception {
        // 下载文件路径
        // filename = new String(filename.getBytes("iso-8859-1"),"utf-8");
        String filename = "固定资产清单2.xls";
        String path = request.getServletContext().getRealPath("/file");
        File file = new File(path + File.separator + filename);
        HttpHeaders headers = new HttpHeaders();
        // 下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
        // 通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        // application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);// valueOf("application/octet-stream")
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    // 下载模板excel2
    @RequestMapping(value = "/downExcel")
    public void downExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, getClasspath2() + Constrants.FILEPATHFILE + "固定清单.xls",
                "固定清单-" + DateUtil.getDays() + ".xls");
    }

    public static String getClasspath2() {
        String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../")
                .replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    // 固定资产列表页
    @RequestMapping("/list")
    @ResponseBody
    // 参数当前页 查询月份 名称
    Map<String, Object> listAssets(
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            String asName, @RequestParam(value = "acperiod", required = false) String inputPeriod) {
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<>(); // 查询参数集合
        // asAccountDatea
        try {
            String userID = getUser().getUserID();
            String accountID = getAccount().getAccountID();
            map.put("beginTime", null); // 查询开始时间
            map.put("endTime", null); // 查询结束时间
            // 时间为空取当前月
            if (!StringUtil.isEmpty(inputPeriod)) {
                int dayofMonth = DateUtil.getDayofMonth(inputPeriod); // 计算查询月的总天数作为最后一天
                String beginDay = inputPeriod + "-01"; // 拼接开始时间
                String endDay = inputPeriod + "-" + dayofMonth; // 拼接最后时间
                map.put("beginTime", DateUtil.fomatDate(beginDay)); // 查询开始时间
                map.put("endTime", DateUtil.fomatDate(endDay)); // 查询结束时间
            }
            map.put("userID", userID); // 用户ID
            map.put("accountID", accountID); // 账套ID
            map.put("currentPage", currentPage); // 当前页
            map.put("asName", asName); // 查询名称

            // 调用业务层获取分页数据
            Page page = assetsService.listAssets(map);
            result.put("result", page);
            result.put("message", "success");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【listAssets】 失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【listAssets】 失败!", e);
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        }
    }

    // 删除
    @RequestMapping("/del")
    @ResponseBody
    // 参数当前页 查询月份 名称
    Map<String, Object> deleteAs(String assetsID) {
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<>();
        try {
            /* assetsService.deladd(); */
            String accountID = getAccount().getAccountID();
            map.put("assetsID", assetsID); // 主键
            map.put("accountID", accountID);// 账套ID
            assetsService.deleteByAsId(map);
            result.put("result", "删除成功");
            result.put("message", "success");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【del】 删除失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【del】 删除失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }
    }

    // 批量删除
    @RequestMapping("/delAll")
    @ResponseBody
    Map<String, Object> delAll(String ids) {
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<>();
        try {
            String accountID = getAccount().getAccountID();
            if (ids != null && !"".equals(ids)) {
                StringBuffer sb = new StringBuffer();
                String[] arr = ids.split(",");
                map.put("ids", arr); // 主键
                map.put("accountID", accountID);// 账套ID
                assetsService.delAll(map);
                result.put("result", "删除成功");
                result.put("message", "success");
                return result;
            }
            result.put("result", "删除失败");
            result.put("message", "fail");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【delAll】 批量删除失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【delAll】 批量删除失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }
    }

    // 添加固定资产
    @RequestMapping("/addAss")
    @ResponseBody
    Map<String, Object> addAss(Assets assets) {
        Map<String, Object> result = new HashMap<String, Object>();
        /* Map<String, String> map = new HashMap<>(); */
        try {
            Map<String, String> userInfo = getUserInfo();
            result = assetsService.addAssets(assets, userInfo);
            // 添加成功
            if (result.get("message").equals("success")) {
                return result;
            }
            result.put("result", "添加失败");
            result.put("message", "fail");
            return result;

        } catch (BusinessException e) {
            logger.error("AssetsController【delAll】添加失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【delAll】 添加失败!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }
    }

    // 固定资产添加 资产名称 编码检验
    @RequestMapping("/check")
    @ResponseBody
    Map<String, Object> checkSub(@RequestParam(required = false) String asCode,
                                 @RequestParam(required = false) String asName) {
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, String> map = new HashMap<>();
        String name = null;
        try {
            String accountID = getAccount().getAccountID();
            map.put("account_id", accountID);
            if (!StringUtil.isEmptyWithTrim(asCode)) {
                name = "资产编码";
                map.put("asCode", asCode);
            }
            if (!StringUtil.isEmptyWithTrim(asName)) {
                name = "资产名字";
                map.put("asName", asName);
            }
            Assets assets = assetsService.checkSub(map);
            if (assets != null) {
                result.put("result", name + "已经存在不能重复");
                result.put("message", "fail");
                return result;
            }
            result.put("result", name + "可以使用");
            result.put("message", "success");
            return result;

        } catch (BusinessException e) {
            logger.error("AssetsController【checkSub】 未知错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【checkSub】 未知错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }

    }

    // 查询所有固定资产科目
    @RequestMapping("/queryAllSub")
    @ResponseBody
    Map<String, Object> queryAllSub(String category) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        try {
            String account_period = getUserDate();
            String account_id = getAccount().getAccountID(); // 账套id

            map.put("category", category);
            map.put("account_id", account_id);
            map.put("account_period", account_period);

            // Map<String, Object> list = assetsService.queryAllSub(map);
            List<SubjectMessage> list = assetsService.queryAllSubTree(map);
            if (list != null) {
                result.put("subMessageList", list);
                result.put("message", "success");
                return result;
            }
            result.put("result", "没有数据");
            result.put("message", "fail");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【queryAllSub】数据错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【queryAllSub】 数据错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }
    }

    // 查询固定资产科目
    @RequestMapping("/detail")
    @ResponseBody
    Map<String, Object> detail(String assetsID) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        try {
            String accountID = getAccount().getAccountID();
            map.put("accountID", accountID);
            map.put("assetsID", assetsID);
            Assets ass = assetsService.queryAssById(map);

            if (ass != null) {
                result.put("result", ass);
                result.put("message", "success");
                return result;
            }
            result.put("result", "数据错误");
            result.put("message", "fail");
            return result;
        } catch (BusinessException e) {
            logger.error("AssetsController【detail】数据错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("AssetsController【detail】 数据错误!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        }
    }

    // 查询固定资产科目
    @RequestMapping("/ceshi")
    @ResponseBody
    Map<String, Object> ceshi(String uid) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        try {
            String accountID = getAccount().getAccountID();
            map.put("accountID", accountID);

            assetsService.deladd(uid);

            result.put("message", "success");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "fail");
            return result;
        }
    }

    public Map<String, String> getUserInfo() {
        Map<String, String> map = new HashMap<>();
        String userID = getUser().getUserID(); // 用户id
        String userName = getUser().getUserName(); // 用户姓名
        String accountID = getAccount().getAccountID(); // 账套id
        String account_period = getUserDate(); // 账套id
        map.put("accountID", accountID);// 账套id
        map.put("userID", userID);
        map.put("userName", userName);
        map.put("period", account_period);
        return map;

    }

    @RequestMapping("/importView")
    public ModelAndView init(ModelAndView modelAndView) {
        modelAndView.setViewName("assets/import");
        return modelAndView;
    }

    // 查询固定资产折旧明细
    @RequestMapping("/queryZjDetail")
    @ResponseBody
    Map<String, Object> queryZjDetail(String time, String isClean) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            String busDate = (String) sessionMap.get("busDate");
            Account account = (Account) sessionMap.get("account");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("time", time);
            param.put("isClean", isClean);
            List<Assets> list = assetsDao.queryZjDetail(param);
            List<Assets> returnList = new ArrayList<Assets>();
            if (null != list && list.size() > 0) {
                for (Assets assets : list) {
                    String assetsID = assets.getAssetsID();
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("isQm", "1");
                    para.put("time", time);
                    para.put("assetsID", assetsID);
                    Object obj1 = assetsDao.queryAssetsSum(para);
                    Map<String, Object> m1 = (Map<String, Object>) obj1;
                    if (m1 == null) {
                        continue;
                    }
                    Double qmSum = Double.parseDouble(m1.get("sum1").toString());
                    assets.setQmSum(qmSum);
                    para.remove("isQm");
                    para.put("isBq", "1");
                    Object obj2 = assetsDao.queryAssetsSum(para);
                    Map<String, Object> m2 = (Map<String, Object>) obj2;
                    Double bqSum = Double.parseDouble(m2.get("sum1").toString());
                    assets.setBqSum(bqSum);
                    returnList.add(assets);
                }
            }
            result.put("list", returnList);
            result.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            logger.error("查看固定资产明细异常!");
            result.put("message", "查看固定资产折旧明细异常!");
            return result;
        }
        return result;
    }
}
