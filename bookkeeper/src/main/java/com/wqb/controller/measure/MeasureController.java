package com.wqb.controller.measure;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.TBasicMeasure;
import com.wqb.service.measure.TBasicMeasureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: MeasureController
 * @Description: 计量单位控制层
 * @date 2017年12月20日 上午10:01:32
 */
@Component
@Controller
@RequestMapping("/measure")
public class MeasureController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(MeasureController.class);

    @Autowired
    TBasicMeasureService tMeasureService;

    /**
     * @param tBasicMeasure
     * @return Map<String, Object> 返回类型
     * @Title: addMeasure
     * @Description: 增加计量单位
     * @date 2017年12月18日 上午11:27:53
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addMeasure")
    @ResponseBody
    public Map<String, Object> addMeasure(TBasicMeasure tBasicMeasure) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        int results = 0;
        try {
            results = tMeasureService.insertMeasure(tBasicMeasure, session);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        result.put("results", results);
        result.put("message", "success");
        return result;
    }

    /**
     * @param tBasicMeasure
     * @return Map<String, Object>    返回类型
     * @Title: queryMeasure
     * @Description: 查询计量单位
     * @date 2017年12月19日  上午11:59:32
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryMeasure")
    @ResponseBody
    Map<String, Object> queryMeasure(TBasicMeasure tBasicMeasure) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            result = tMeasureService.queryMeasure(session);
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("TMeasureService【deleteMeasure】 计量单位删除出错", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TMeasureService【deleteMeasure】,计量单位删除出错", e);
        }
        result.put("code", 1);
        return result;

    }

    /**
     * @param tBasicMeasure
     * @return Map<String, Object>    返回类型
     * @Title: queryMeasureBySymbolOrName
     * @Description: 根据计量单位符号（英文名称）或者 中文名称查询
     * @date 2018年2月28日  上午11:08:25
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryMeasureBySymbolOrName")
    @ResponseBody
    Map<String, Object> queryMeasureBySymbolOrName(String symbolOrName) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            if (StringUtils.isNotBlank(symbolOrName)) {
//				symbolOrName = new String(symbolOrName.getBytes("ISO-8859-1"),"UTF-8");
            } else {
                symbolOrName = null;
            }
            Map<String, Object> tMeasureServiceList = tMeasureService.queryMeasureBySymbolOrName(session, symbolOrName);
            result.putAll(tMeasureServiceList);
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("TMeasureService【queryMeasureBySymbolOrName】  根据计量单位符号（英文名称）或者 中文名称查询出错", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TMeasureService【queryMeasureBySymbolOrName】, 根据计量单位符号（英文名称）或者 中文名称查询出错", e);
        }
        return result;

    }

    /**
     * @param pkMeasureId
     * @return Map<String, Object>    返回类型
     * @Title: deleteMeasure
     * @Description: 删除计量单位
     * @date 2017年12月18日  下午3:51:17
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteBypkMeasureId")
    @ResponseBody
    Map<String, Object> deleteBypkMeasureId(String pkMeasureId) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pkMeasureId", pkMeasureId);
        try {
            tMeasureService.deleteByPrimaryKey(param, session);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TMeasureService【deleteMeasure】 计量单位删除出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TMeasureService【deleteMeasure】,计量单位删除出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @param tBasicMeasure
     * @return Map<String, Object>    返回类型
     * @Title: updateMessage
     * @Description: 更新计量单位
     * @date 2017年12月19日  上午11:59:51
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateMessage")
    @ResponseBody
    Map<String, Object> updateMessage(TBasicMeasure tBasicMeasure) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Map<String, Object> updateMessage = tMeasureService.updateMessage(session, tBasicMeasure);
            result.putAll(updateMessage);
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param response
     * @throws Exception void    返回类型
     * @Title: downMeasureExcel
     * @Description: 下载计量单位初始excel模板
     * @date 2018年3月1日  下午2:34:47
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping(value = "/downMeasureExcel")
    public void downMeasureExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "计量单位初始模版.xls",
                "计量单位初始模版-" + DateUtil.getDays() + ".xls");
    }
}
