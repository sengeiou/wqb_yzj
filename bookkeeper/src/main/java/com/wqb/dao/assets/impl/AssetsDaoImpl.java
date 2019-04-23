package com.wqb.dao.assets.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.model.Assets;
import com.wqb.model.SubjectMessage;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("assetsDao")
public class AssetsDaoImpl implements AssetsDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(AssetsDaoImpl.class);

    // excel表固定资产添加
    @Override
    public void insertAssert(Assets assets) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("assets.insertAsset", assets);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    // 手动添加固定资产
    public Integer addAssert(Assets assets) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer insert = 0;
        try {
            insert = sqlSession.insert("assets.insertAsset", assets);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return insert;
    }

    // 批量添加
    @Override
    public void insertBath(List<Assets> assets) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("assets.insertBath", assets);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 根据code防止重复数据导入
    @Override
    public Assets queryByCode(Assets assets) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Assets as = null;
        try {
            as = sqlSession.selectOne("assets.queryByCode", assets);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return as;
    }

    // 分页查询
    @Override
    public List<Assets> queryAssertPage(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Assets> list = sqlSession.selectList("assets.queryAssertPage", map);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        return null;
    }

    // 根据查询条件统计分页总数
    @Override
    public Integer queryCount(Map<String, Object> map) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = -1;
        try {
            count = sqlSession.selectOne("assets.queryCount", map);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    // 删除
    @Override
    public void deleteByAsId(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("assets.deleteByAsId", map);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 批量删除
    @Override
    public void delAll(Map<String, Object> map) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int delete = sqlSession.delete("assets.delAll", map);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<Assets> queryAssByAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Assets> list = null;
        try {
            list = sqlSession.selectList("assets.queryAssByAcc", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    @Override
    public void updAssets(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("assets.updAssets", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updAssets1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("assets.updAssets1", param);
        } catch (

                Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    // 查询所有科目
    public List<SubjectMessage> queryAllSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<SubjectMessage> list = sqlSession.selectList("assets.queryAllSub", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    // 校验固定资产名字 或 编码 是否重复
    public Assets checkSub(Map<String, String> param) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<Assets> list = sqlSession.selectList("assets.checkSub", param);

            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return null;
    }

    // 固定资产详情查看
    @Override
    public Assets queryAssById(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Assets as = null;
        try {
            as = sqlSession.selectOne("assets.queryAssById", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return as;
    }

    // ceshi
    @Override
    public void del1(String pk_sub_id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.delete("assets.del1", pk_sub_id);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // ceshi
    @Override
    public void add1(Map<String, String> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("assets.add1", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    // 更新凭证号到固定资产
    public void updAddAssets(Map<String, String> map) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("assets.updAddAssets", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updateUserTest(Map<String, Object> param) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("assets.updateUserTest", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 检查数据库和导入的数据是否有重复的
    @Override
    public List<String> queryCode(String accountID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> list = sqlSession.selectList("assets.queryCode", accountID);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public void updAssetsByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("assets.updAssetsByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }

    // 再次导入删除之前的数据
    @Override
    public int delAllAss(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.delete("assets.delAllAss", map);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        return num;
    }

    @Override
    public List<Assets> queryZjDetail(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Assets> list = sqlSession.selectList("assets.queryZjDetail", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryAssetsSum(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object obj = null;
        try {
            obj = sqlSession.selectOne("assets.queryAssetsSum", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return obj;
    }
}
