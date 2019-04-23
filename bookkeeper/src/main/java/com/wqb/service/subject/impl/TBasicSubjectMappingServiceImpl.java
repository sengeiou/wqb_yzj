package com.wqb.service.subject.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.subject.TBasicSubjectMappingMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicMeasure;
import com.wqb.model.TBasicSubjectMapping;
import com.wqb.model.User;
import com.wqb.service.subject.TBasicSubjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Service("TBasicSubjectMappingService")
public class TBasicSubjectMappingServiceImpl implements TBasicSubjectMappingService {

    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingServiceImpl.class);

    @Autowired
    TBasicSubjectMappingMapper tBasicSubjectMappingMapper;

    @Autowired
    AccountDao accountDao;

    /**
     * @param list
     * @param session
     * @param file
     * @return
     * @throws BusinessException
     * @Title: uploadSubMapping
     * @Description: (非 JavaDoc)
     * @see com.wqb.service.subject.TBasicSubjectMappingService#uploadSubMapping(java.util.List, javax.servlet.http.HttpSession, java.io.File)
     */
    @Override
    public Map<String, Object> uploadSubMapping(List<Map<String, Object>> list, HttpSession session, File file)
            throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        //获取用户信息
        try {
            User user = (User) session.getAttribute("user");
            // 创建人
            String createPsnName = user.getUserName();
//			String createPsnId = user.getUserID();
            //循环遍历每一行excel表数据
            String path = file.getPath();
            System.out.println(path);
            List<TBasicSubjectMapping> tBasicSubjectMappingList = new ArrayList<TBasicSubjectMapping>();
            for (int i = 0; i < list.size(); i++)//从第一行开始读
            {
                TBasicSubjectMapping tBasicSubjectMapping = new TBasicSubjectMapping();
                Map<String, Object> subMappingMap = list.get(i);
                // `pk_sub_mapping_id` int(32) NOT NULL AUTO_INCREMENT COMMENT '科目关系映射主键',
//				String pkSubMappingId = subMappingMap.get("map0") == null ? null : subMappingMap.get("map0").toString();
//				tBasicSubjectMapping.setPkSubMappingId(pkSubMappingId);

                //  `sub_mapping_code` varchar(100) DEFAULT NULL COMMENT '映射科目编码',
                if (subMappingMap.get("map1") == null) {
                    result.put("msg", "第 " + i + "行科目编码不能为空");
                    return result;
                }
                String subMappingCode = subMappingMap.get("map1").toString(); //映射科目编码
                tBasicSubjectMapping.setSubMappingCode(subMappingCode);

                if (subMappingMap.get("map2") == null) {
                    result.put("msg", "第 " + i + "行映射科目名称不能为空");
                    return result;
                }
                //   `sub_mapping_name` varchar(30) DEFAULT NULL COMMENT '映射科目名称',
                String subMappingName = subMappingMap.get("map2").toString(); //映射科目名称
                tBasicSubjectMapping.setSubMappingName(subMappingName);

                //  `similar_name` varchar(100) DEFAULT NULL COMMENT '相似名称',
                String similarName = subMappingMap.get("map3") == null ? null : subMappingMap.get("map3").toString(); //相似名称
                tBasicSubjectMapping.setSimilarName(similarName);

                //  `small_scale_trading` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—贸易型',
                if (subMappingMap.get("map4").toString() == "1.0") {
                    result.put("msg", "必须改为文本型才能上传");
                    return result;
                }
                Short smallScaleTrading = subMappingMap.get("map4") == null ? 0 : Short.parseShort(subMappingMap.get("map4").toString()); //小规模—贸易型
                tBasicSubjectMapping.setSmallScaleTrading(smallScaleTrading);

//				  `general_taxpayer_trading` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—贸易型',
                Short generalTaxpayerTrading = subMappingMap.get("map5") == null ? 0 : Short.parseShort(subMappingMap.get("map5").toString());
                tBasicSubjectMapping.setGeneralTaxpayerTrading(generalTaxpayerTrading);

//				  `small_scale_production` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—生产型',
                Short smallScaleProduction = subMappingMap.get("map6") == null ? 0 : Short.parseShort(subMappingMap.get("map6").toString());
                tBasicSubjectMapping.setSmallScaleProduction(smallScaleProduction);

//				  `general_taxpayer_production` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—生产型',
                Short generalTaxpayerProduction = subMappingMap.get("map7") == null ? 0 : Short.parseShort(subMappingMap.get("map7").toString());
                tBasicSubjectMapping.setGeneralTaxpayerProduction(generalTaxpayerProduction);

//				  `small_scale_import_and_export` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—进出口',
                Short smallScaleImportAndExport = subMappingMap.get("map8") == null ? 0 : Short.parseShort(subMappingMap.get("map8").toString());
                tBasicSubjectMapping.setSmallScaleImportAndExport(smallScaleImportAndExport);

//				  `general_taxpayer_import_and_export` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—进出口',

                Short generalTaxpayerImportAndExport = subMappingMap.get("map9") == null ? 0 : Short.parseShort(subMappingMap.get("map9").toString());
                tBasicSubjectMapping.setGeneralTaxpayerImportAndExport(generalTaxpayerImportAndExport);

//				  `small_scale_high_tech` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—高新',
                Short smallScaleHighTech = subMappingMap.get("map10") == null ? 0 : Short.parseShort(subMappingMap.get("map10").toString());
                tBasicSubjectMapping.setSmallScaleHighTech(smallScaleHighTech);

//				  `general_taxpayer_high_tech` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—高新',
                Short generalTaxpayerHighTech = subMappingMap.get("map11") == null ? 0 : Short.parseShort(subMappingMap.get("map11").toString());
                tBasicSubjectMapping.setGeneralTaxpayerHighTech(generalTaxpayerHighTech);

                // `user_id`   '用户id',
//				tBasicSubjectMapping.setUserId(createPsnId);
                // `account_id`   '帐套id',
                // 作为通用条件 所有人查询都带上此条件
//				tBasicSubjectMapping.setAccountId("admin");
                // `meas_unit_type`   '计量单位类型',
                // `meas_unit_remarks`   '备注说明',
//				tBasicSubjectMapping.setMeasUnitRemarks("后台管理员上传");
                // `create_person`   '创建人',
                tBasicSubjectMapping.setCreatePerson(createPsnName);
                // `create_date`   '创建日期',
                Date date = new Date();
                tBasicSubjectMapping.setCreateDate(date);
                tBasicSubjectMappingList.add(tBasicSubjectMapping);
            }
//			deleteByAccountId(session);
            deleteAll();
            int no = tBasicSubjectMappingMapper.uploadSubMappingList(tBasicSubjectMappingList);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteByAccountId(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
//		User user = (User) session.getAttribute("user");
        // 创建人
//		String userID = user.getUserID();
        TBasicMeasure tBasicMeasure = new TBasicMeasure();
        try {
            tBasicMeasure.setAccountId("admin");
            int no = tBasicSubjectMappingMapper.deleteByAccountId(tBasicMeasure);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "计量单位删除失败");
        }
        return result;
    }

    @Override
    public int deleteAll() {
        logger.info("TBasicSubjectMappingServiceImpl.deleteAll");
        return tBasicSubjectMappingMapper.deleteAll();
    }

    @Override
    public int deleteByPrimaryKey(Integer pkSubMappingId) {
        logger.info("TBasicSubjectMappingServiceImpl.deleteByPrimaryKey", pkSubMappingId);
        return tBasicSubjectMappingMapper.deleteByPrimaryKey(pkSubMappingId);
    }

    @Override
    public Map<String, Object> insert(TBasicSubjectMapping tBasicSubjectMapping) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            logger.info("TBasicSubjectMappingServiceImpl.insert", tBasicSubjectMapping);
            int insertMapping = tBasicSubjectMappingMapper.insert(tBasicSubjectMapping);
            if (insertMapping == 1) {
                result.put("code", 1);
                // 更新全部 映射状态
                accountDao.updateMappingStates();
            }

        } catch (Exception e) {
            logger.error("TBasicSubjectMappingServiceImpl.【insert】" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int insertSelective(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingServiceImpl.insertSelective", tBasicSubjectMapping);
        return tBasicSubjectMappingMapper.insert(tBasicSubjectMapping);
    }

    @Override
    public TBasicSubjectMapping selectByPrimaryKey(Integer pkSubMappingId) {
        logger.info("TBasicSubjectMappingServiceImpl.selectByPrimaryKey", pkSubMappingId);
        return tBasicSubjectMappingMapper.selectByPrimaryKey(pkSubMappingId);
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingServiceImpl.updateByPrimaryKeySelective", tBasicSubjectMapping);
        return tBasicSubjectMappingMapper.updateByPrimaryKeySelective(tBasicSubjectMapping);
    }

    @Override
    public int updateByPrimaryKey(TBasicSubjectMapping tBasicSubjectMapping) {
        logger.info("TBasicSubjectMappingServiceImpl.updateByPrimaryKey", tBasicSubjectMapping);
        return tBasicSubjectMappingMapper.updateByPrimaryKey(tBasicSubjectMapping);
    }

    @Override
    public Map<String, Object> querySubMappingList(HttpSession session) {
        // 判断是否有权限
        Map<String, Object> result = new HashMap<String, Object>();
        logger.info("TBasicSubjectMappingServiceImpl.querySubMappingList", session);
        session.getAttribute("");

        List<TBasicSubjectMapping> subMappings = new ArrayList<TBasicSubjectMapping>();
        Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
        Account account = (Account) sessionMap.get("account");
        subMappings = tBasicSubjectMappingMapper.querySubMappingList(account.getSsType());
        result.put("code", 1);
        result.put("tBasicSubjectMappings", subMappings);
        return result;
    }

    @Override
    public Map<String, Object> deleteSubMappingList(List<TBasicSubjectMapping> tBasicSubMappingList,
                                                    HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = (User) session.getAttribute("user");
        Integer userType = user.getUserType();
        if (1 == userType) {
            int no = tBasicSubjectMappingMapper.deleteMeasureList(tBasicSubMappingList);
            result.put("no", no);
            result.put("code", 1);
        } else {
            result.put("msg", "只有超级管理员才有权限删除");
        }
        return result;
    }

}
