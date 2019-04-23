package com.wqb.common;

/**
 * 系统常量
 *
 * @author Administrator
 */
public class Constrants {
    public final static String MESSAGE_TIP = "message";// 系统提示信息KEY
    public final static String MESSAGE_TIP_FLAGS = "errorFlags";// 系统错误提示信息标志KEY
    public final static String FLAGS = "flags";// 系统提示信息标示KEY

    public final static int PREV_PARENT_DOM = 0;// 权限一级菜单父节点

    public final static String FILEPATHIMG = "QTSimages/";//图片/附件上传路径
    public final static String FILEPATHFILE = "files/";
    /* 前台常量 */


    /* 后台常量 */
    public final static int SUPER_USER = 0;// 超级管理员权限
    public final static int SIMPLE_USER = 1;// 普通用户权限
    public final static String USER_PRIVILEGE_KEY = "Privilege_List";// 系统用户权限存储KEY

    public final static String USER_KEY = "system_user";// 系统后台用户信息session存储KEY
    public final static String CUS_KEY = "sysCustomer";//系统后台客户信息session存储KEY
    public final static String BUSI_DATE = "busidate";//系统登录业务日期 --add by lisc 2017年3月27日11:03:56

    public final static String SYS_DEFAULT_PWD = "12345678"; // 系统初始化重置默认密码

    public final static int USABLE_USER = 0;// 用户启用状态
    public final static int FREEZE_USER = 1;// 用户冻结状态

    public final static int DATA_NOT_DEL = 0;// 数据未删除
    public final static int DATA_DEL = 1;// 数据删除

    public final static int DATA_NOT_PUBLISH = 0;// 数据未发布
    public final static int DATA_PUBLISH = 1;// 数据已发布

    public final static int DATA_NOT_VERIFY = 0;// 数据未审核
    public final static int DATA_VERIFY = 1;// 数据已审核通过
    public final static int DATA_VERFY_NOT_PASS = 2;// 数据审核未通过

    public final static int SYS_ROLE = 0;// 系统角色
    public final static int DP_ROLE = 1;// 部门角色

    public final static int FREEZE_DATA = 0;// 数据未启用状态
    public final static int USABLE_DATA = 1;// 数据启用状态


    public final static String TIME_TYPE_FIRST = "1";  //时间类型
    public final static String TIME_TYPE_TWO = "2";
    public final static String TIME_TYPE_THREE = "3";


    public final static String BANK_TYPE_QB = "0";  //全部银行
    public final static String BANK_TYPE_GS = "1";  //工商银行
    public final static String BANK_TYPE_JS = "2";  //建设银行
    public final static String BANK_TYPE_JT = "3";  //交通银行
    public final static String BANK_TYPE_SR = "4";  //深圳农村商业银行
    public final static String BANK_TYPE_PA = "5";  //平安银行
    public final static String BANK_TYPE_ZS = "6";  //招商银行
    public final static String BANK_TYPE_ZG = "7";  //中国银行
    public final static String BANK_TYPE_ZX = "8";  //中信银行

    public final static String BANK_NAME_QB = "全部银行";
    public final static String BANK_NAME_GS = "工商银行";
    public final static String BANK_NAME_JS = "建设银行";
    public final static String BANK_NAME_JT = "交通银行";
    public final static String BANK_NAME_SR = "深圳农村商业银行";
    public final static String BANK_NAME_PA = "平安银行";
    public final static String BANK_NAME_ZS = "招商银行";
    public final static String BANK_NAME_ZG = "中国银行";
    public final static String BANK_NAME_ZX = "中信银行";
    public final static String BANK_NAME_NY = "中国农业银行";

    public final static Integer RANG_PERIOD = 5;

    public final static int TEMP_COUNT_NUM = 30;


}
