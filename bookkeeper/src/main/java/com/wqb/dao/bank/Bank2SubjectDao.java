package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.Bank2Subject;

import java.util.List;
import java.util.Map;

public interface Bank2SubjectDao {
    List<Bank2Subject> queryBank2Subject(Map<String, Object> param) throws BusinessException;

    int addBank2Subject(Bank2Subject b2s) throws BusinessException;

    int updBank2Subject(Bank2Subject b2s) throws BusinessException;

    List<Bank2Subject> queryBank2SubjectByID(Map<String, Object> param) throws BusinessException;

    List<Bank2Subject> queryByBankAccount(Map<String, Object> param) throws BusinessException;

}
