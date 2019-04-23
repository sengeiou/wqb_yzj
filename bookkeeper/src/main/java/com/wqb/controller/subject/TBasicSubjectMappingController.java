package com.wqb.controller.subject;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.TBasicSubjectMapping;
import com.wqb.model.User;
import com.wqb.service.subject.TBasicSubjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMappingController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月24日 上午10:14:09
 */
@Controller
@RequestMapping("/subjectMapping")
public class TBasicSubjectMappingController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingController.class);

    @Autowired
    TBasicSubjectMappingService tBasicSubjectMappingService;

    @Value("${filePaths}")
    private String filePaths;

    /**
     * @param subMapping
     * @return Map<String, Object>    返回类型
     * @Title: uploadSubMapping
     * @Description: 上传科目映射配置文件
     * @date 2018年10月29日  下午5:25:22
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/uploadSubMapping")
    @ResponseBody
    Map<String, Object> uploadSubMapping(MultipartFile subMapping) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getRequest().getSession();
        try {
            if (null != subMapping && !subMapping.isEmpty()) {
                User user = (User) session.getAttribute("user");
                // 创建人
                String createPsnName = user.getUserName();
                String createPsnId = user.getUserID();

                //1获取文件上传路径
//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE; //wtpwebapps/wqb/WEB-INF/classes/../../files/
//				String filePaths = "/usr/local/servers/tomcat/wqbFiles/wqbmanaUpload";
                String filePath = filePaths + "/" + createPsnId + "/"; //wtpwebapps/wqb/WEB-INF/classes/../../files/
                //2获取文件名
                String fileName = FileUpload.fileUp(subMapping, filePath,
                        "SubMappingInit-" + "-" + System.currentTimeMillis());
                //3读取excel表格数据
                List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                File file = new File(filePath, fileName);
                result.put("SubMappingList.size()", list.size());
                result.put("SubMappingList", list);
                //4调用科目映射业务层添加到数据库
                Map<String, Object> uploadSubMappings = tBasicSubjectMappingService.uploadSubMapping(list, session, file);
                result.putAll(uploadSubMappings);
            }
        } catch (BusinessException e) {
            result.put("msg", "上传失败请检查excel格式是否是97");
            e.printStackTrace();
        } catch (Exception e) {
            result.put("msg", "上传失败请检查excel格式是否是97");
        }
        return result;
    }

    @RequestMapping("/deleteSubMappingList")
    @ResponseBody
    Map<String, Object> deleteSubMappingList(List<TBasicSubjectMapping> tBasicSubMappingList) {
        HttpSession session = getRequest().getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Map<String, Object> deleteSubMappings = tBasicSubjectMappingService.deleteSubMappingList(tBasicSubMappingList, session);
            result.putAll(deleteSubMappings);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMappingController【deleteSubMappingList】 科目映射删除出错", e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("TBasicSubjectMappingController【deleteSubMappingList】,科目映射删除出错", e);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/deleteByPrimaryKey")
    @ResponseBody
    /**
     *  根据映射id删除映射表
     */
    public int deleteByPrimaryKey(Integer pkSubMappingId) {
        logger.info("TBasicSubjectMappingController.deleteByPrimaryKey", pkSubMappingId);
        return tBasicSubjectMappingService.deleteByPrimaryKey(pkSubMappingId);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public Map<String, Object> insert(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingController.insert", tBasicSubjectMapping);
        return tBasicSubjectMappingService.insert(tBasicSubjectMapping);
    }

    @RequestMapping("/insertSelective")
    @ResponseBody
    public int insertSelective(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingController.insertSelective", tBasicSubjectMapping);
        return tBasicSubjectMappingService.insertSelective(tBasicSubjectMapping);
    }

    @RequestMapping("/selectByPrimaryKey")
    @ResponseBody
    public TBasicSubjectMapping selectByPrimaryKey(Integer pkSubMappingId) {
        logger.info("TBasicSubjectMappingController.selectByPrimaryKey", pkSubMappingId);
        return tBasicSubjectMappingService.selectByPrimaryKey(pkSubMappingId);
    }

    /**
     * @return Map<String, Object>    返回类型
     * @Title: querySubMappingList
     * @Description: 查询科目映射表
     * @date 2018年10月29日  下午9:04:57
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMappingList")
    @ResponseBody
    public Map<String, Object> querySubMappingList() {
        HttpSession session = getRequest().getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            result = tBasicSubjectMappingService.querySubMappingList(session);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TBasicSubjectMappingController【querySubMappingList】,查询科目映射列表出错", e);
        }
        result.put("code", 1);
        return result;

    }

    @RequestMapping("/updateByPrimaryKeySelective")
    @ResponseBody
    public int updateByPrimaryKeySelective(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingController.updateByPrimaryKeySelective", tBasicSubjectMapping);
        return tBasicSubjectMappingService.updateByPrimaryKeySelective(tBasicSubjectMapping);
    }

    @RequestMapping("/updateByPrimaryKey")
    @ResponseBody
    public int updateByPrimaryKey(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingController.updateByPrimaryKey", tBasicSubjectMapping);
        return tBasicSubjectMappingService.updateByPrimaryKey(tBasicSubjectMapping);
    }

    @RequestMapping("/subMappingView")
    public ModelAndView subjectContrast(ModelAndView modelAndView) {
        modelAndView.setViewName("system/subMapping");
        return modelAndView;
    }

    @RequestMapping("/uploadSubMappingView")
    public ModelAndView uploadSubMappingView(ModelAndView modelAndView) {
        modelAndView.setViewName("uploadSubMappingView");
        return modelAndView;
    }

    /**
     * @param response
     * @throws Exception void    返回类型
     * @Title: downSubMappingExcel
     * @Description: 下载科目映射初始excel模板
     * @date 2018年10月29日  下午6:19:05
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping(value = "/downSubMappingExcel")
    public void downSubMappingExcel(HttpServletResponse response) throws Exception {
        // 下载修改成的对应的excle名字 ,文件存放位置在/webRoot/files下面
        FileDownload.filedownload(response, PathUtil.getClasspath() + Constrants.FILEPATHFILE + "科目映射初始表.xls",
                "科目映射初始表-" + DateUtil.getDays() + ".xls");
    }

}
