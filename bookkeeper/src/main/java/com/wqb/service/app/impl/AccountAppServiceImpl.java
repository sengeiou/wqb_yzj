package com.wqb.service.app.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.app.AccountAppDao;
import com.wqb.model.Account;
import com.wqb.model.Test01;
import com.wqb.model.User;
import com.wqb.service.app.AccountAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("accountAppService")
public class AccountAppServiceImpl implements AccountAppService {
    @Autowired
    AccountAppDao accountAppDao;

    @Override
    //根据用户id查询账套
    public Account queryAccByUserID(Map<String, Object> param) throws BusinessException {
        try {
            Account account = accountAppDao.queryAccByUserID(param);
            //select * from  t_basic_account where userID=#{userID} and statu=1 limit 1;
            return account;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }

    //根据公司名称查询用户下面的所有账套
    @Override
    public List<Account> queryAllAccByUserIDAndComName(Map<String, Object> param) throws BusinessException {
        try {
            List<Account> list = accountAppDao.queryAllAccByUserIDAndComName(param);
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }

    //选择公司查询进入账套
    @Override
    public Account queryAccByID(Map<String, Object> param) throws BusinessException {
        try {
            Account account = accountAppDao.queryAccByID(param);
            return account;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }


    @Override
    ////根据管理员查询员工
    public List<User> queryUserByUserID(Map<String, Object> param) throws BusinessException {
        try {
            List<User> list = accountAppDao.queryUserByUserID(param);
            //select * from  t_sys_user where parentUser=#{userID}
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }

    @Override
    //根据管理员查询员工
    public List<String> queryUserByUserID2(Map<String, Object> param) throws BusinessException {
        try {
            List<String> list = accountAppDao.queryUserByUserID2(param);
            //select userID from  t_sys_user where parentUser=#{userID} and state = 1
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }


    //根据员工查询账套
    @Override
    public List<Account> queryAllByID(Map<String, Object> param) throws BusinessException {
        try {
            List<Account> list = accountAppDao.queryAllByID(param);
	/*		select * from t_basic_account  where userID in
			<foreach collection="listID" item="id" open="(" close=")" separator=",">
					#{id}
			</foreach>
			and companyName  LIKE CONCAT('%','${companyName}','%' )  and statu=1*/
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }

    @Override
    public List<Account> queryAllByID2(Map<String, Object> param) throws BusinessException {
        try {
            List<Account> list = accountAppDao.queryAllByID2(param);
			/*select * from t_basic_account  where statu=1   and  userID in
					<foreach collection="listID" item="id" open="(" close=")" separator=",">
							#{id}
					</foreach>
					<if test="companyName!=null and companyName!='' ">
						and	companyName  LIKE CONCAT('%','${companyName}','%' )
					</if>
					order by lastTime desc
					<if test="limit!=null">
						limit 1
					</if>*/
            return list;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }

    //根据用户查询权限
    @Override
    public User queryAcc(Map<String, Object> param) throws BusinessException {
        try {
            User user = accountAppDao.queryAcc(param);
            //select * from  t_sys_user where userID=#{userID};
            return user;
        } catch (Exception e) {
            throw new BusinessException("查询异常");
        }
    }


    @Override
    public void insertUser(Test01 test01) throws BusinessException {
        accountAppDao.insertUser(test01);
    }


}
