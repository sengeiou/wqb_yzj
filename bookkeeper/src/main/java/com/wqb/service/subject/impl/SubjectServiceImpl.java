package com.wqb.service.subject.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.subject.SubjectDao;
import com.wqb.model.SubExcel;
import com.wqb.model.Subject;
import com.wqb.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("subjectService")
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectDao subjectDao;

    @Override
    public List<Subject> queryVouSubject(Map<String, Object> param) throws BusinessException {

        return subjectDao.queryVouSubject(param);
    }

    @Override
    public List<Subject> querySysSub(String userID, String accountID) throws BusinessException {
        // TODO Auto-generated method stub
        return subjectDao.querySysSub(userID, accountID);
    }

    @Override
    public List<SubExcel> queryExcelSub(String userID, String accountID) throws BusinessException {
        // TODO Auto-generated method stub
        return subjectDao.queryExcelSub(userID, accountID);
    }

}
