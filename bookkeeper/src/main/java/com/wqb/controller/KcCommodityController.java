package com.wqb.controller;

import com.wqb.common.*;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.model.KcCommodity;
import com.wqb.model.Page;
import com.wqb.service.KcCommodity.KcCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stock")
public class KcCommodityController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(KcCommodityController.class);

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    KcCommodityService kcCommodityService;
    @Autowired
    KcCommodityDao kcCommodityDao;

    // 库存商品导入 只导入一次
    @RequestMapping("/uploadStock")
    @ResponseBody
    Map<String, Object> uploadAssets(MultipartFile uploadStock) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> list = null;
        Map<String, Object> param = new HashMap<>();
        try {
            String userID = getUser().getUserID(); //用户id
            String userName = getUser().getUserName(); //用户姓名
            String accountID = getAccount().getAccountID(); //账套id
            Integer companyType = getAccount().getCompanyType(); //企业性质 企业性质(1：生产型2：贸易型3：服务型)
            param.put("accountID", accountID);
            param.put("userID", userID);
            param.put("userName", userName);
            param.put("period", getUserDate());
            param.put("companyType", companyType);

            if (uploadStock != null && !uploadStock.isEmpty()) {
                // 1获取文件上传路径
                String filePath = filePaths + "/" + userID + "/" + accountID + "/" + getUserDate() + "/";

//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE; // wtpwebapps/wqb/WEB-INF/classes/../../files/
                // 2获取文件名
                String fileName = FileUpload.fileUp(uploadStock, filePath,
                        "stock-" + userID + "-" + System.currentTimeMillis());
                // 3读取excel表格数据
                list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                if (list == null || list.size() == 0) {
                    result.put("message", "fail");
                    result.put("result", "导入失败，请核对表格内容的格式后再式。");
                    return result;
                }
                // 4调用库存商品业务层添加到数据库
                Map<String, Object> mes = kcCommodityService.insertCommodity(list, param);
                return mes;
            }
            result.put("result", "上传文件不存在");
            result.put("message", "fail");
            return result;
        } catch (BusinessException e) {
            logger.error("KcCommodityController【uploadStock】 上传异常!", "错误原因来自于： " + e);
            e.printStackTrace();
            result.put("message", "fail");

            result.put("result", e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("KcCommodityController【uploadStock】 上传异常!", "错误原因来自于： " + e);
            e.printStackTrace();
            result.put("message", "fail");
            result.put("result", e.getMessage());
            return result;
        }
    }


    // 库存商品模板下载
    @RequestMapping(value = "/downExcel")
    public void downExcel(HttpServletResponse response) throws Exception {
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "数量金额式总账.xls",
                "数量金额式总账-" + DateUtil.getDays() + ".xls");
    }


    /**
     * @param period      期间
     * @param comNameSpec
     * @param sub_code    科目编码
     * @param currentPage 当前页
     * @param size        每页显示大小
     * @return Map<String, Object>    返回类型
     * @Title: list
     * @Description: 库存商品列表页
     * @date 2018年4月12日  下午2:39:37
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    Map<String, Object> list(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String comNameSpec,
            @RequestParam(required = false) String sub_code,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer size) {

        Map<String, Object> res = new HashMap<String, Object>();

        Map<String, Object> param = new HashMap<>();

        String accountID = getAccount().getAccountID();
        if (StringUtil.isEmpty(period)) {
            period = getUserDate();
        }

        param.put("accountID", accountID);
        param.put("period", period);
        param.put("sub_code", sub_code);
        param.put("comNameSpec", comNameSpec);
        param.put("currentPage", currentPage);
        param.put("size", size);

        try {

            Page<KcCommodity> page = kcCommodityService.queryCommodityList(param);
            res.put("code", "0");
            res.put("msg", "success");
            res.put("count", page.getRecordTotal());
            res.put("data", page.getContent());
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "请求错误");
            res.put("count", "0");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "请求错误");
            res.put("count", "0");
            return res;
        }
    }

    //库存商品列表页
    @RequestMapping(value = "/Testlist")
    @ResponseBody
    Map<String, Object> Testlist() {

        Map<String, Object> res = new HashMap<String, Object>();

        Map<String, Object> param = new HashMap<>();

        // 获取用户信息
        String userId = "31fd7eee802f448193e8ed9e76729a51";// 用户id
        String accountId = "268504f7912e4c72a59ba0fe077fe5f0";// 账套id
        String period = "2018-02";

        param.put("accountID", accountId);
        param.put("period", period);
        param.put("sub_code", null);
        param.put("comNameSpec", null);
        param.put("currentPage", 1);
        param.put("size", 10);

        try {

            Page<KcCommodity> page = kcCommodityService.queryCommodityList(param);
            res.put("code", "0");
            res.put("msg", "success");
            res.put("count", page.getRecordTotal());
            res.put("data", page.getContent());
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "请求错误");
            res.put("count", "0");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "请求错误");
            res.put("count", "0");
            return res;
        }
    }

    // 查询是否已经导入过
    @RequestMapping("/stockImport")
    @ResponseBody
    Map<String, Object> stockImport() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        try {
            String accountID = getAccount().getAccountID(); //账套id
            param.put("accountID", accountID);
            // 检查是否有初始化导入
            Integer num = kcCommodityDao.queryCommodity(param);
            if (num != null && num > 0) {
                res.put("msg", "已经导入，再次导入将会覆盖之前的数据");
                res.put("code", "1");
            } else {
                res.put("code", "0");
            }
            return res;
        } catch (BusinessException e) {
            logger.error("KcCommodityController【stockImport】 异常!", "错误原因来自于： " + e);
            e.printStackTrace();
            res.put("msg", e.getMessage());
            res.put("code", "1");
            return res;
        } catch (Exception e) {
            logger.error("KcCommodityController【stockImport】 异常!", "错误原因来自于： " + e);
            e.printStackTrace();
            result.put("msg", e.getMessage());
            res.put("code", "1");
            return res;
        }
    }


}
