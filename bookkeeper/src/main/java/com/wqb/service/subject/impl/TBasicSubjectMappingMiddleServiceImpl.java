package com.wqb.service.subject.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.subject.TBasicSubjectMappingMapper;
import com.wqb.dao.subject.TBasicSubjectMappingMiddleMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.model.*;
import com.wqb.service.subject.TBasicSubjectMappingMiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service("TBasicSubjectMappingMiddleService")
public class TBasicSubjectMappingMiddleServiceImpl implements TBasicSubjectMappingMiddleService {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMappingMiddleServiceImpl.class);

    @Autowired
    TBasicSubjectMappingMiddleMapper tBasicSubjectMappingMiddleMapper;

    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    TBasicSubjectMappingMapper tBasicSubjectMappingMapper;

    @Override
    public int deleteByPrimaryKey(Integer pkSubMappingMiddleId) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.deleteByPrimaryKey", pkSubMappingMiddleId);
        return tBasicSubjectMappingMiddleMapper.deleteByPrimaryKey(pkSubMappingMiddleId);
    }

    @Override
    public int insert(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.insert", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.insert(tBasicSubjectMappingMiddle);
    }

    @Override
    public int insertSelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.insertSelective", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.insertSelective(tBasicSubjectMappingMiddle);
    }

    @Override
    public TBasicSubjectMappingMiddle selectByPrimaryKey(Integer pkSubMappingMiddleId) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.selectByPrimaryKey", pkSubMappingMiddleId);
        return tBasicSubjectMappingMiddleMapper.selectByPrimaryKey(pkSubMappingMiddleId);
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.updateByPrimaryKeySelective", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.updateByPrimaryKeySelective(tBasicSubjectMappingMiddle);
    }

    @Override
    public int updateByPrimaryKey(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.updateByPrimaryKey", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.updateByPrimaryKey(tBasicSubjectMappingMiddle);
    }

    @Override
    public Map<String, Object> subAutoMapping(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            String busDate = (String) sessionMap.get("busDate");
            Map<String, Object> param = new HashMap<String, Object>();
            String accountID = account.getAccountID();
            param.put("accountID", accountID);
            param.put("busDate", busDate);
            // 查询次账号本期全部科目
            List<TBasicSubjectMessage> queryAllSubject = tBasicSubjectMessageMapper.queryAllSubject(param);

            TBasicSubjectMapping tBasicSubjectMapping2 = new TBasicSubjectMapping();
            //`ssType` int(11) DEFAULT NULL COMMENT '一般纳税人（传0）   小规模（传1）'
            Integer ssType = account.getSsType();
            //`companyType` int(2) NOT NULL COMMENT '企业性质(1：生产型2：贸易型3：服务型)',
            Integer companyType = account.getCompanyType();

//		  `small_scale_trading` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—贸易型',
//		  `general_taxpayer_trading` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—贸易型',
//		  `small_scale_production` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—生产型',
//		  `general_taxpayer_production` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—生产型',
//		  `small_scale_import_and_export` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—进出口',
//		  `general_taxpayer_import_and_export` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—进出口',
//		  `small_scale_high_tech` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '小规模—高新',
//		  `general_taxpayer_high_tech` smallint(2) unsigned zerofill DEFAULT '00' COMMENT '一般纳税人—高新',

            short short1 = 1;
            // 一般纳税人（传0）
            if (ssType != null && ssType == 0) {
                if (companyType != null) {
                    // 企业性质(1：生产型2：贸易型3：服务型)
                    switch (companyType) {
                        case 1:
                            tBasicSubjectMapping2.setGeneralTaxpayerProduction(short1);
                            break;
                        case 2:
                            tBasicSubjectMapping2.setGeneralTaxpayerTrading(short1);
                            break;
                        case 3:
                            System.out.println("2");
                            break;
                    }
                }
            }
            // 小规模（传1）
            else if (ssType != null && ssType == 1) {
                if (companyType != null) {
                    // 企业性质(1：生产型2：贸易型3：服务型)
                    switch (companyType) {
                        case 1:
                            tBasicSubjectMapping2.setSmallScaleProduction(short1);
                            break;
                        case 2:
                            tBasicSubjectMapping2.setSmallScaleTrading(short1);
                            break;
                        case 3:
                            System.out.println("2");
                            break;
                    }
                }
            }

//			List<TBasicSubjectMapping> querySubMappingListByssType = tBasicSubjectMappingMapper.querySubMappingListByssType(tBasicSubjectMapping2);
            // 查询映射配置表全部信息--要增加 公司类型判断
            List<TBasicSubjectMapping> querySubMappingList = tBasicSubjectMappingMapper.querySubMappingList(account.getSsType());

            List<TBasicSubjectMappingMiddle> tBasicSubjectMappingMiddleList = new ArrayList<TBasicSubjectMappingMiddle>();

            for (TBasicSubjectMapping tBasicSubjectMapping : querySubMappingList) {
                List<TBasicSubjectMessage> list = new ArrayList<TBasicSubjectMessage>();

                // 标准科目代码
                String subMappingCode = tBasicSubjectMapping.getSubMappingCode();
                // 标准名称
                String subMappingName = tBasicSubjectMapping.getSubMappingName();
                // 如果标准名称没有匹配到  再查找相似名称--多个值 用，逗号隔开
                String similarName = tBasicSubjectMapping.getSimilarName();
                String[] similarNameSplit = new String[]{};
                if (similarName.indexOf(",") > 0) {
                    similarNameSplit = similarName.split(",");
                } else {
                    String[] similarNameArr = {similarName};
                    similarNameSplit = similarNameArr;
                }

                for (TBasicSubjectMessage tBasicSubjectMessage : queryAllSubject) {
                    // 需要增加查询科目是否有下级如果有下级也不能自动匹配  --  用数据库做查询 速度会更快

                    String subCode = tBasicSubjectMessage.getSubCode();
                    if (subCode.startsWith(subCode)) {
                        String subName = tBasicSubjectMessage.getSubName();
                        if (subMappingName != null && subMappingName.equals(subName)) {
                            list.add(tBasicSubjectMessage);
                        } else if (similarNameSplit.length > 0) {
                            for (int i = 0; similarNameSplit.length > i; i++) {
                                String string = similarNameSplit[i];
                                if (string != null && string.equals(subName)) {
                                    list.add(tBasicSubjectMessage);
                                }
                            }
                        }
                    }
                }
                if (list.size() == 1) {
                    TBasicSubjectMessage tBasicSubjectMessage = list.get(0);
                    String subCode = tBasicSubjectMessage.getSubCode();
                    String subName = tBasicSubjectMessage.getSubName();

                    TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle = new TBasicSubjectMappingMiddle();
                    tBasicSubjectMappingMiddle.setAccountId(accountID);
                    tBasicSubjectMappingMiddle.setCreateDate(new Date());
                    tBasicSubjectMappingMiddle.setCreatePerson(user.getUserID());
                    tBasicSubjectMappingMiddle.setSubMappingCode(subMappingCode);
                    tBasicSubjectMappingMiddle.setSubMappingName(subMappingName);
                    tBasicSubjectMappingMiddle.setSubMessageCode(subCode);
                    tBasicSubjectMappingMiddle.setSubMessageName(subName);
                    // 改为 list 插入
                    tBasicSubjectMappingMiddleMapper.insert(tBasicSubjectMappingMiddle);
                    tBasicSubjectMappingMiddleList.add(tBasicSubjectMappingMiddle);
                }
            }
            result.put("tBasicSubjectMappingMiddle", tBasicSubjectMappingMiddleList);
            result.put("code", 1);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMappingMiddleServiceImpl.subAutoMapping", e);
            e.printStackTrace();
            result.put("msg", "TBasicSubjectMappingMiddleServiceImpl.subAutoMapping{科目自动映射出错}");
        }
        return result;
    }

    @Override
    public List<TBasicSubjectMappingMiddle> querySubMappingMiddleByAccId(String accountId) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.querySubMappingMiddleByAccId", accountId);
        return tBasicSubjectMappingMiddleMapper.querySubMappingMiddleByAccId(accountId);
    }

    @Override
    public int insertList(List<TBasicSubjectMappingMiddle> subMappingMiddleList) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.insertList", subMappingMiddleList);
        return tBasicSubjectMappingMiddleMapper.insertList(subMappingMiddleList);
    }

    @Override
    public int saveOrUpdate(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.saveOrUpdate", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.saveOrUpdate(tBasicSubjectMappingMiddle);
    }

    @Override
    public TBasicSubjectMappingMiddle querySubMappingMiddle(Map<String, String> subMappingMap) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.querySubMappingMiddle", subMappingMap);
        return tBasicSubjectMappingMiddleMapper.querySubMappingMiddle(subMappingMap);
    }

    @Override
    public List<TBasicSubjectMappingMiddle> validationSubMappingMiddle(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle) {
        logger.info("TBasicSubjectMappingMiddleServiceImpl.validationSubMappingMiddle", tBasicSubjectMappingMiddle);
        return tBasicSubjectMappingMiddleMapper.validationSubMappingMiddle(tBasicSubjectMappingMiddle);
    }

    /**
     * 根据 账套id 删除映射信息表
     *
     * @param session
     * @return
     */
    public int deleteByAccountId(HttpSession session) {

        int no = 0;
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account"); //获取帐套信息
            String accountId = account.getAccountID();//账套id
            logger.info("TBasicSubjectMappingMiddleMapper.deleteByAccountId", accountId);
            no = tBasicSubjectMappingMiddleMapper.deleteByAccountId(accountId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TBasicSubjectMappingMiddleServiceImpl.deleteByAccountId");
        }
        return no;
    }

}
