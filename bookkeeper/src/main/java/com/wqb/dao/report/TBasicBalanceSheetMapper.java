package com.wqb.dao.report;

import com.wqb.model.TBasicBalanceSheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TBasicBalanceSheetMapper {
//    int deleteByPrimaryKey(String pkBalanceSheetId);
//
//    int insert(TBasicBalanceSheet record);

    int addBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet);

    List<TBasicBalanceSheet> queryBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet);

    List<TBasicBalanceSheet> queryBalanceSheet2(TBasicBalanceSheet tBasicBalanceSheet);

    Map<String, String> queryBalanceSheetMap(TBasicBalanceSheet tBasicBalanceSheet);

    int deleteBalanceSheet(TBasicBalanceSheet tBasicBalanceSheet);

//    TBasicBalanceSheet selectByPrimaryKey(String pkBalanceSheetId);
//
//    int updateByPrimaryKeySelective(TBasicBalanceSheet record);
//
//    int updateByPrimaryKey(TBasicBalanceSheet record);
}
