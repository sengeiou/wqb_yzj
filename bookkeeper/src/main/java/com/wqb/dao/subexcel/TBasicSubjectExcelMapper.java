package com.wqb.dao.subexcel;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectExcel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TBasicSubjectExcelMapper {

    void uploadSubExcel(TBasicSubjectExcel subExcel);

    List<TBasicSubjectExcel> querySubExcel(TBasicSubjectExcel tBasicSubjectExcel);

    List<TBasicSubjectExcel> querySubByisMatching(Map<String, Object> param);

    List<TBasicSubjectExcel> querySubBySubSode(Map<String, Object> param);

    List<TBasicSubjectExcel> updateSubExcel(Map<String, Object> param);

    int deleteSubExcelAll(TBasicSubjectExcel tBasicSubjectExcel);

    /**
     * @param tBasicSubjectExcel
     * @return List<TBasicSubjectExcel> 返回类型
     * @Title: querySubExcelMoney
     * @Description: 查询系统科目真实金额
     * @date 2018年1月25日 下午8:27:41
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectExcel> querySubExcelMoney(TBasicSubjectExcel tBasicSubjectExcel);

    /**
     * @param tBasicSubjectExcelList
     * @return int 返回类型
     * @Title: uploadSubExcelList
     * @Description: 上传的excel余额表以list方式导入数据库
     * @date 2018年2月5日 下午4:56:50
     * @author SiLiuDong 司氏旭东
     */
    int uploadSubExcelList(List<TBasicSubjectExcel> tBasicSubjectExcelList);

    TBasicSubjectExcel getExcelSubByPKID(String pkSubExcelID) throws BusinessException;
}
