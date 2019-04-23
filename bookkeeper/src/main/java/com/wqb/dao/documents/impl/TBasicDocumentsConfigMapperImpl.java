package com.wqb.dao.documents.impl;

import com.wqb.common.Log4jLogger;
import com.wqb.dao.documents.TBasicDocumentsConfigMapper;
import com.wqb.model.TBasicDocumentsConfig;
import com.wqb.model.TBasicSubjectParent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("tBasicDocumentsConfigMapper")
public class TBasicDocumentsConfigMapperImpl implements TBasicDocumentsConfigMapper {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicDocumentsMapperImpl.class);

    @Override
    public int deleteByPrimaryKey(String pkDocumentsConfigId) {
        logger.info(pkDocumentsConfigId);// TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(TBasicDocumentsConfig record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(TBasicDocumentsConfig record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TBasicDocumentsConfig selectByPrimaryKey(String pkDocumentsConfigId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(TBasicDocumentsConfig record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(TBasicDocumentsConfig record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void insertDocumentsConfig(TBasicSubjectParent subParent) {
        // TODO Auto-generated method stub

    }

}
