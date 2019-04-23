package com.wqb.service.arch;

import com.wqb.common.BusinessException;
import com.wqb.model.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ArchService {

    // 工资添加
    Map<String, Object> insertArch(List<Map<String, Object>> list, Map<String, String> map) throws BusinessException;

    // 批量添加
    Map<String, Object> insertBath(List<Map<String, Object>> list, Map<String, String> map) throws BusinessException;

    // 综合查询 分页(区间，姓名)
    Page<Arch> queryListPage(Map<String, Object> map) throws BusinessException;

    // 删除
    void delById(Map<String, String> map) throws BusinessException;

    // 批量删除
    void delBathById(Map<String, Object> map) throws BusinessException;

    // 获取薪资数据所涉及的部门
    List<Map<String, Object>> queryDepart(HttpSession session) throws BusinessException;

    // 薪资计提
    void arch2vouch1(HttpSession session) throws BusinessException;

    // 发放工资
    Voucher arch2vouch2(HttpSession session) throws BusinessException;

    // 查询做账期间所有工资月份
    String queryArchDate(Map<String, Object> param) throws BusinessException;

    void arch2vouch3(User user, Account account) throws BusinessException;

    void arch2vouch4(HttpSession session) throws BusinessException;

}
