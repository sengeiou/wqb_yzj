package com.wqb.service.SuperAdminService.SuperAdminServiceImpl;

import com.wqb.common.BusinessException;
import com.wqb.dao.SuperAdminDao.SuperAdminDao;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;
import com.wqb.service.SuperAdminService.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 超级管理员页面扇形图做账统计数量 开发时间：2018-07-21
 *
 * @author tangsheng
 */
@Component
@Service("superAdminService")
public class SuperAdminServiceImpl implements SuperAdminService {
    @Autowired
    SuperAdminDao superAdminDao;

    @Override
    public int getQueryCountQuantity0(Map<String, Object> param) throws BusinessException {

        return superAdminDao.getQueryCountQuantity0(param);
    }

    // 通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity1(Map<String, Object> param) throws BusinessException {

        return superAdminDao.getQueryCountQuantity1(param);
    }

    // 通过账套ID和做账期间以及条件：已完成结账--1 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity2(Map<String, Object> param) throws BusinessException {

        return superAdminDao.getQueryCountQuantity2(param);
    }

    // 通过账套ID和做账期间以及条件： 生成凭证-1但是没有结账--0 （查询做账统计数量）
    @Override
    public int getQueryCountQuantity3(Map<String, Object> param) throws BusinessException {

        return superAdminDao.getQueryCountQuantity3(param);
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
     * 开发时间：2018-07-23
     * 开发人：tangsheng
     */
    @Override
    public List<Account> queryStatusCustomer0(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer0(param);
    }


    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
     * 开发时间：2018-07-23
     * 开发人：tangsheng
     */
    @Override
    public List<StatusPeriod> queryStatusCustomer1(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer1(param);
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
     * 开发时间：2018-07-23
     * 开发人：tangsheng
     */
    @Override
    public List<StatusPeriod> queryStatusCustomer2(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer2(param);
    }

    @Override
    public StatusPeriod queryStatusCustomer4(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer4(param);
    }

    @Override
    public int queryStatusCustomer5(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer5(param);
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public List<Account> queryStatusCustomer3(Map<String, Object> param) throws BusinessException {
        return superAdminDao.queryStatusCustomer3(param);
    }

    // 平台有效企业数量趋势图
    @Override
    public Object getPtActiveCount(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getPtActiveCount(param);
    }

    // 平台代理企业数量
    @Override
    public Object getPtDzAccZz(Map<String, Object> param) throws BusinessException {
        // TODO Auto-generated method stub
        return superAdminDao.getPtDzAccZz(param);
    }

    // 平台自助记账数量
    @Override
    public Object getPtJzAccZz(Map<String, Object> param) throws BusinessException {
        // TODO Auto-generated method stub
        return superAdminDao.getPtJzAccZz(param);
    }

    @Override
    public Object getByXzQy(Map<String, Object> param) throws BusinessException {
        // TODO Auto-generated method stub
        return superAdminDao.getByXzQy(param);
    }

    @Override
    public Object getByTyQy(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getByTyQy(param);
    }

    @Override
    public Object getPtTotalAcc(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getPtTotalAcc(param);
    }

    @Override
    public Object getPtTotalTyAcc(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getPtTotalTyAcc(param);
    }

    @Override
    public Object getByXzDlCom(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getByXzDlCom(param);
    }

    @Override
    public Object getDlCom(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getDlCom(param);
    }

    @Override
    public Object getTotalQyByTime(Map<String, Object> param) throws BusinessException {
        return superAdminDao.getTotalQyByTime(param);
    }
}
