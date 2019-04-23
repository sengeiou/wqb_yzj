package com.wqb.controller.subject;

import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.model.TBasicSubjectMappingMiddle;
import com.wqb.service.subject.TBasicSubjectMappingMiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMappingMiddleController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月24日 上午10:14:16
 */
@Controller
@RequestMapping("/subjectMappingMiddle")
public class TBasicSubjectMappingMiddleController extends BaseController {

    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingMiddleController.class);

    @Autowired
    TBasicSubjectMappingMiddleService tBasicSubjectMappingMiddleService;

    @RequestMapping("/deleteByPrimaryKey")
    @ResponseBody
    public int deleteByPrimaryKey(Integer pkSubMappingMiddleId) {
        logger.info("TBasicSubjectMappingMiddleController.deleteByPrimaryKey", pkSubMappingMiddleId);
        return tBasicSubjectMappingMiddleService.deleteByPrimaryKey(pkSubMappingMiddleId);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public int insert(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleController.insert", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleService.insert(tBasicSubjectMappingMiddle);
    }

    @RequestMapping("/insertSelective")
    @ResponseBody
    public int insertSelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleController.insertSelective", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleService.insertSelective(tBasicSubjectMappingMiddle);
    }

    @RequestMapping("/selectByPrimaryKey")
    @ResponseBody
    public TBasicSubjectMappingMiddle selectByPrimaryKey(Integer pkSubMappingMiddleId) {
        logger.info("TBasicSubjectMappingMiddleController.selectByPrimaryKey", pkSubMappingMiddleId);
        return tBasicSubjectMappingMiddleService.selectByPrimaryKey(pkSubMappingMiddleId);
    }

    @RequestMapping("/updateByPrimaryKeySelective")
    @ResponseBody
    public int updateByPrimaryKeySelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleController.updateByPrimaryKeySelective", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleService.updateByPrimaryKeySelective(tBasicSubjectMappingMiddle);
    }

    @RequestMapping("/updateByPrimaryKey")
    @ResponseBody
    public int updateByPrimaryKey(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleController.updateByPrimaryKey", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleService.updateByPrimaryKey(tBasicSubjectMappingMiddle);
    }

}
