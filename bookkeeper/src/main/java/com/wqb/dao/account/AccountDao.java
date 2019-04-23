package com.wqb.dao.account;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.model.UserAccount;
import com.wqb.model.vomodel.AccStatusPeriod;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccountDao {
    List<Account> queryByUserID(String userID) throws BusinessException;

    // 切换账套时变更账套最后使用时间
    Integer chgAccLastTime(Date lastTime, String accountID) throws BusinessException;

    /**
     * @param account
     * @return
     * @throws BusinessException Integer 返回类型
     * @Title: chgAccInitialStates
     * @Description: 更改初始化状态
     * @date 2018年1月4日 上午11:18:27
     * @author SiLiuDong 司氏旭东
     */
    Integer chgAccInitialStates(Account account) throws BusinessException;

    // 根据账套ID获取账套信息
    Account queryAccByID(String accountID) throws BusinessException;

    // 查询所有账套
    List<Account> queryAccs(Map<String, Object> param) throws BusinessException;

    // tangsheng--根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示begin：
    Account queryPeriodId(Map<String, Object> param) throws BusinessException;
    // tangsheng--根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示end：

    // 查询员工所有账套
    List<Account> queryAccs2(Map<String, Object> param) throws BusinessException;

    List<User> queryUserById(Map<String, Object> param) throws BusinessException;

    int queryAccsNum(Map<String, Object> param) throws BusinessException;

    List<Account> queryAccsNum2(Map<String, Object> param) throws BusinessException;

    List<Account> queryAccByCondition(Map<String, Object> param) throws BusinessException;

    // 根据条件查询账套总数
    Integer queryCountAccByCondition(Map<String, Object> param) throws BusinessException;

    List<StatusPeriod> queryStatusPeriod(Map<String, Object> param) throws BusinessException;

    int chgAccByCondintion(Map<String, Object> param) throws BusinessException;

    int insertUserAcc(UserAccount userAcc) throws BusinessException;

    int chgAcc(Map<String, Object> param) throws BusinessException;

    List<AccStatusPeriod> queryAccJoinStatusPeriod(Map<String, Object> param) throws BusinessException;

    Integer queryAccJoinStatusPeriodCount(Map<String, Object> param) throws BusinessException;

    UserAccount queryAcc(Map<String, Object> param) throws BusinessException;

    Object getMonthAddAcc(Map<String, Object> param) throws BusinessException;

    Object getMonthTyAcc(Map<String, Object> param) throws BusinessException;

    List<Map<String, Object>> getDzqyAccCount() throws BusinessException;

    // 根据账套id查询期间状态数据
    List<StatusPeriod> queryStaPeriod2(Map<String, Object> param) throws BusinessException;

    // 获取代账员工本月新增企业数
    Object getByxzqy(Map<String, Object> param) throws BusinessException;

    // 获取截止到上月的总企业数
    Object getSyzqy(Map<String, Object> param) throws BusinessException;

    // 查看员工总停用企业数
    Object getZtyqys(Map<String, Object> param) throws BusinessException;

    // 查看员工总企业数
    Object getZqys(Map<String, Object> param) throws BusinessException;

    int queryAccByConditionCount(Map<String, Object> param) throws BusinessException;

    // 查询进行中
    int queryAccRuning(Map<String, Object> param) throws BusinessException;

    // 查询完成
    int queryAccFinish(Map<String, Object> param) throws BusinessException;

    /**
     * @param userId
     * @return
     * @throws BusinessException List<Account> 返回类型
     * @Title: queryAllAccByUserIdStr
     * @Description: 查询用户下面所有的帐套
     * @date 2018年7月25日 下午12:54:37
     * @author SiLiuDong 司氏旭东
     */
    List<Account> queryAllAccByUserIdStr(String userId) throws BusinessException;

    /**
     * @param hashMap
     * @return
     * @throws BusinessException List<Account> 返回类型
     * @Title: queryAllAccByUserIdYear
     * @Description: 按年份查询用户下面所有的帐套
     * @date 2018年7月27日 上午11:00:47
     * @author SiLiuDong 司氏旭东
     */
    List<Account> queryAllAccByUserIdYear(Map<String, String> hashMap) throws BusinessException;

    List<String> queryStartStaPer(Map<String, Object> hashMap) throws BusinessException;

    List<String> queryAidByCondition(Map<String, Object> hashMap) throws BusinessException;

    List<Account> queryAccByUserID(String userID) throws BusinessException;

    List<Map<String, Object>> queryAdminFirstData(Map<String, Object> param) throws BusinessException;

    Object queryXzCount(Map<String, Object> param) throws BusinessException;

    Object queryHj(Map<String, Object> param) throws BusinessException;

    Object queryXzHj(Map<String, Object> param) throws BusinessException;

    Object queryAdminXzHj(Map<String, Object> param) throws BusinessException;

    Object queryTy(Map<String, Object> param) throws BusinessException;

    Object queryHjTy(Map<String, Object> param) throws BusinessException;

    /**
     * @param account
     * @return
     * @throws BusinessException Integer    返回类型
     * @Title: mappingStates
     * @Description: 更改映射状态
     * @date 2018年11月9日  下午5:18:27
     * @author SiLiuDong 司氏旭东
     */
    Integer mappingStates(Account account) throws BusinessException;

    /**
     * 更新 全部 映射状态
     *
     * @return
     */
    int updateMappingStates();
}
