package com.wqb.dao.assets;

import com.wqb.common.BusinessException;
import com.wqb.model.Assets;
import com.wqb.model.SubjectMessage;

import java.util.List;
import java.util.Map;

public interface AssetsDao {

    // 固定资产excel上传添加到数据库
    void insertAssert(Assets assets) throws BusinessException;

    // 固定资产excel上传添加到数据库
    Integer addAssert(Assets assets) throws BusinessException;

    // 批量添加

    void insertBath(List<Assets> assets) throws BusinessException;

    // 防止重复数据导入
    Assets queryByCode(Assets assets) throws BusinessException;

    // 根据月份 和资产名称 查询固定资产信息
    List<Assets> queryAssertPage(Map<String, Object> map) throws BusinessException;

    // 查询总数
    Integer queryCount(Map<String, Object> map) throws BusinessException;

    // 删除固定资产
    void deleteByAsId(Map<String, Object> map) throws BusinessException;

    // 删除全部
    void delAll(Map<String, Object> map) throws BusinessException;

    // 获取账套名下固定资产
    List<Assets> queryAssByAcc(Map<String, Object> param) throws BusinessException;

    // 折旧修改固定资产信息
    void updAssets(Map<String, Object> param) throws BusinessException;

    // 固定资产折旧特殊修改
    void updAssets1(Map<String, Object> param) throws BusinessException;

    // 查询所有科目
    List<SubjectMessage> queryAllSub(Map<String, Object> param) throws BusinessException;

    // 校验固定资产名字 或 编码 是否重复
    Assets checkSub(Map<String, String> param) throws BusinessException;

    // 固定资产查看详情
    Assets queryAssById(Map<String, Object> param) throws BusinessException;

    // 测试
    void del1(String pk_sub_id) throws BusinessException;

    void add1(Map<String, String> map) throws BusinessException;

    // 更新凭证号到固定资产
    void updAddAssets(Map<String, String> map);

    void updateUserTest(Map<String, Object> param);

    List<String> queryCode(String accountID) throws BusinessException;

    // 反结转回退固定资产明细
    void updAssetsByID(Map<String, Object> param) throws BusinessException;

    int delAllAss(Map<String, Object> map) throws BusinessException;

    List<Assets> queryZjDetail(Map<String, Object> param) throws BusinessException;

    Object queryAssetsSum(Map<String, Object> param) throws BusinessException;
}
