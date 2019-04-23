package com.wqb.service.assets;

import com.wqb.common.BusinessException;
import com.wqb.model.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface AssetsService {

    //固定资产excel上传添加到数据库
    Map<String, Object> insertAssert(List<Map<String, Object>> list, Map<String, String> map) throws BusinessException;

    //根据查询条件分页
    public Page listAssets(Map<String, Object> map) throws BusinessException;

    //删除固定资产
    void deleteByAsId(Map<String, Object> map) throws BusinessException;

    //批量删除
    public void delAll(Map<String, Object> map) throws BusinessException;

    // 固定资产生成凭证
    Voucher assets2vouch(User user, Account account) throws BusinessException;

    public Integer queryCount(Map<String, Object> map) throws BusinessException;

    // 固定资产添加
    Map<String, Object> addAssets(Assets assets, Map<String, String> map) throws BusinessException;

    // 固定资产添加
    Map<String, Object> addAssets(Map<String, Object> map) throws BusinessException;

    //检查添加固定资产编码与名字是否有重复
    Assets checkSub(Map<String, String> map) throws BusinessException;

    //查询所有科目
    Map<String, Object> queryAllSub(Map<String, Object> param) throws BusinessException;

    List<SubjectMessage> queryAllSubTree(Map<String, Object> param) throws BusinessException;

    //固定资产查看详情
    Assets queryAssById(Map<String, Object> param) throws BusinessException;

    //ceshi
    void deladd() throws BusinessException;

    void deladd(String id) throws BusinessException;

    int delAllAss(Map<String, Object> map) throws BusinessException;

    public Properties getToProperties();

    public List<String> getToSmallBaseSub();

    public List<String> getToBaseSub();


}
