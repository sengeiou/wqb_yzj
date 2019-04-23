package com.wqb.service.subBook;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.vomodel.SubMessageVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface SubBookService {

    Map<String, Object> queryDetailAccount(Map<String, Object> param) throws BusinessException;

    List<Map<String, Object>> queryLedger(Map<String, Object> param) throws BusinessException;

    int delSubBook(Map<String, Object> param) throws BusinessException;

    int delSubBookBath(Map<String, Object> param) throws BusinessException;

    int delSubBookBath2(Map<String, Object> param) throws BusinessException;

    public List<Map<String, String>> getRangePeriod(Map<String, Object> param) throws BusinessException;

    List<SubMessageVo> querySubAll(Map<String, Object> param) throws BusinessException;

    Map<String, Object> fastQuery(Map<String, Object> map) throws BusinessException;

    String exportStockExcel(Map<String, Object> param, HttpServletResponse response) throws BusinessException;

    HSSFWorkbook exportDetailAccount(Map<String, Object> param) throws BusinessException;

    Map<String, Object> queryAllSubBook(Map<String, Object> map) throws Exception;


    Map<String, Object> exportProfitStatement(User user, Account account, String period) throws BusinessException;


}
