package com.wqb.service.subexcel;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectExcel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface TBasicSubjectExcelService {

    /**
     * @param list
     * @param map2
     * @return Map<String, Object>    返回类型
     * @Title: uploadSubExcel
     * @Description: 科目初始化excel表上传
     * @date 2017年12月25日  下午5:34:29
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> uploadSubExcel(List<Map<String, Object>> list, Map<String, String> map2, File file);

    List<TBasicSubjectExcel> querySubExcel(HttpSession session) throws BusinessException;

    List<TBasicSubjectExcel> querySubByisMatching(Map<String, Object> param);

    List<TBasicSubjectExcel> querySubBySubSode(Map<String, Object> param);

    List<TBasicSubjectExcel> updateSubExcel(Map<String, Object> param) throws BusinessException;

    Map<String, Object> deleteSubExcelAll(HttpSession session);

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectExcel>    返回类型
     * @Title: querySubExcelMoney
     * @Description: 查询系统科目真实金额
     * @date 2018年1月25日  下午8:28:08
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectExcel> querySubExcelMoney(HttpSession session) throws BusinessException;

    TBasicSubjectExcel getExcelSubByPKID(String pkSubExcelID) throws BusinessException;

}
