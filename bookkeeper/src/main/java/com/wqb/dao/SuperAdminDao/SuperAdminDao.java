package com.wqb.dao.SuperAdminDao;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;

import java.util.List;
import java.util.Map;

/**
 * 超级管理员页面扇形图做账统计数量 开发时间：2018-07-21
 *
 * @author tangsheng
 */
public interface SuperAdminDao {

    // 查询分页总数--通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
    int getQueryCountQuantity0(Map<String, Object> param) throws BusinessException;

    // 通过账套ID和做账期间以及条件：未生成凭证--0 （查询做账统计数量）
    int getQueryCountQuantity1(Map<String, Object> param) throws BusinessException;

    // 通过账套ID和做账期间以及条件： 已完成结账--1 （查询做账统计数量）
    int getQueryCountQuantity2(Map<String, Object> param) throws BusinessException;

    // 通过账套ID和做账期间以及条件：生成凭证-1但是没有结账--0 （查询做账统计数量）
    int getQueryCountQuantity3(Map<String, Object> param) throws BusinessException;

    // 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
    List<Account> queryStatusCustomer0(Map<String, Object> param) throws BusinessException;

    //根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
    List<StatusPeriod> queryStatusCustomer1(Map<String, Object> param) throws BusinessException;

    // 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
    List<StatusPeriod> queryStatusCustomer2(Map<String, Object> param) throws BusinessException;

    StatusPeriod queryStatusCustomer4(Map<String, Object> param) throws BusinessException;

    int queryStatusCustomer5(Map<String, Object> param) throws BusinessException;

    // 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
    List<Account> queryStatusCustomer3(Map<String, Object> param) throws BusinessException;

    // 获取平台有效企业用户
    Object getPtActiveCount(Map<String, Object> param) throws BusinessException;

    // 获取平台代账有效企业用户
    Object getPtDzAccZz(Map<String, Object> param) throws BusinessException;

    // 获取平台记账有效企业用户
    Object getPtJzAccZz(Map<String, Object> param) throws BusinessException;

    // 获取本月新增企业数量
    Object getByXzQy(Map<String, Object> param) throws BusinessException;

    // 获取本月平台停用企业
    Object getByTyQy(Map<String, Object> param) throws BusinessException;

    // 平台总企业数
    Object getPtTotalAcc(Map<String, Object> param) throws BusinessException;

    // 平台累计停用企业
    Object getPtTotalTyAcc(Map<String, Object> param) throws BusinessException;

    // 本月新增代理记账公司
    Object getByXzDlCom(Map<String, Object> param) throws BusinessException;

    // 平台累计代理记账公司
    Object getDlCom(Map<String, Object> param) throws BusinessException;


    //获取截止到上月，平台总的企业数(开启的账套)
    Object getTotalQyByTime(Map<String, Object> param) throws BusinessException;
}
