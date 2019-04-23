package com.wqb.dao.report;

import com.wqb.model.TBasicIncomeStatement;

import java.util.List;

public interface TBasicIncomeStatementMapper {
    int deleteByPrimaryKey(String pkIncomeStatementId);

    int insert(TBasicIncomeStatement record);

    int insertSelective(TBasicIncomeStatement record);

    TBasicIncomeStatement selectByPrimaryKey(String pkIncomeStatementId);

    int updateByPrimaryKeySelective(TBasicIncomeStatement record);

    int updateByPrimaryKey(TBasicIncomeStatement record);

    int addIncomeStatement(TBasicIncomeStatement tBasicIncomeStatement);

    List<TBasicIncomeStatement> queryIncomeStatrment(TBasicIncomeStatement tBasicIncomeStatement);

    //APP获取利润
    List<TBasicIncomeStatement> queryIncomeStatrment2(TBasicIncomeStatement tBasicIncomeStatement);

    int deleteIncomeStatrment(TBasicIncomeStatement tBasicIncomeStatement);

}
