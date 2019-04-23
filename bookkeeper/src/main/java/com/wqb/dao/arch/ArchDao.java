package com.wqb.dao.arch;

import com.wqb.common.BusinessException;
import com.wqb.model.Arch;

import java.util.List;
import java.util.Map;

public interface ArchDao {

    // 工资添加
    void insertArch(Arch arch) throws BusinessException;

    // 批量添加
    int insertBath(List<Arch> list) throws BusinessException;

    // 查询是否存在重复数据
    List<Arch> queryByCodeAndAcperiod(Map<String, Object> map) throws BusinessException;

    // 综合查询 分页(区间，姓名)
    List<Arch> queryListPage(Map<String, Object> map) throws BusinessException;

    // 删除
    void delById(Map<String, String> map) throws BusinessException;

    // 批量删除
    void delBathById(Map<String, Object> map) throws BusinessException;

    // 查询需要计提的工资列表
    List<Arch> query2vouch(Map<String, Object> map) throws BusinessException;

    // 获取薪资数据所包含的部门信息
    List<Map<String, Object>> queryDepart(Map<String, Object> param) throws BusinessException;

    // 查询期间工资总数量
    int queryCount(Map<String, Object> param) throws BusinessException;

    // 查询做账期间所有工资月份
    String queryArchDate(Map<String, Object> param) throws BusinessException;

    List<String> querycode(Map<String, Object> param) throws BusinessException;

    // 查询发放薪资数据
    Object queryFfArchData(Map<String, Object> param) throws BusinessException;

    // 查询薪资数据
    List<Arch> queryArchData(Map<String, Object> param) throws BusinessException;

}
