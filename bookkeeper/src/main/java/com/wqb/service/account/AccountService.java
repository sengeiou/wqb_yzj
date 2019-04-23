package com.wqb.service.account;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccountService {
    //切换账套时变更账套最后使用时间
    Integer chgAccLastTime(Date lastTime, String accountID) throws BusinessException;

    // 根据账套ID获取账套信息
    Account queryAccByID(String accountID) throws BusinessException;

    /**
     * @param userID
     * @return
     * @throws BusinessException List<Account> 返回类型
     * @Title: queryByUserID
     * @Description: 根据用户id查询此用户下面所有的帐套
     * @date 2017年12月29日 下午4:06:24
     * @author SiLiuDong 司氏旭东
     */
    List<Account> queryByUserID(String userID) throws BusinessException;


    //tangsheng--根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示begin：
    Account queryPeriodId(Map<String, Object> pam) throws BusinessException;
    //tangsheng--根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示end：

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: chgAccInitialStates
     * @Description: 更改初始化状态
     * @date 2018年1月4日  上午11:30:16
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> chgAccInitialStates(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return Map<String, Object>    返回类型
     * @Title: mappingStates
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2018年11月9日  下午5:11:55
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> mappingStates(HttpSession session);
}
