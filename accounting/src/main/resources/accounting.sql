/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : accounting

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 04/04/2019 18:04:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_basic_account
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_account`;
CREATE TABLE `t_basic_account`  (
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `userID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID(外键)',
  `customID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户ID',
  `companyName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `period` date NOT NULL COMMENT '启用期间',
  `accstandards` tinyint(3) NULL DEFAULT NULL COMMENT '会计准则(3:新会计准则,1小企业会计准则2政府民间非营利组织4村集体会计准则)',
  `calculate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目方案(会计准则)',
  `updatepsnID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人ID',
  `updatepsn` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updatedate` datetime(0) NOT NULL COMMENT '修改时间',
  `createpsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人ID',
  `createpsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `createDate` datetime(0) NOT NULL COMMENT '创建时间',
  `des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `lastTime` datetime(0) NULL DEFAULT NULL COMMENT '最后一次使用时间[当前登陆用户默认使用的账套设置为最后一次使用的账套，如若是第一次登陆，随机指定一个账套]',
  `useLastPeriod` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录账套最后使用期间',
  `companyType` int(2) NOT NULL COMMENT '企业性质(1：生产型2：贸易型3：服务型)',
  `gsmm` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '国税密码',
  `tyxydm` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一信用代码',
  `jzbwb` int(2) NULL DEFAULT 1 COMMENT '记账本位币(1:人民币)',
  `statu` int(11) NULL DEFAULT NULL COMMENT '账套状态（0:新生成1:启用2:禁用）',
  `ssType` int(11) NULL DEFAULT NULL COMMENT '公司纳税人类别 ：一般纳税人（传0）   小规模（传1）',
  `initial_states` int(2) NULL DEFAULT 0 COMMENT '初始化状态(0没有初始化，1已经初始化)',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套类型  暂时未启用',
  `source` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源  来自那一个管理员',
  `chgStatuTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `companyNamePinYin` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称首字母拼接',
  `mapping_states` int(2) NULL DEFAULT 0 COMMENT '映射状态（0.未映射 1.已映射）',
  `level` tinyint(3) NULL DEFAULT NULL COMMENT '科目级别 默认4级',
  `rule` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目规则(4-3-3-3 默认)',
  PRIMARY KEY (`accountID`) USING BTREE,
  INDEX `FK_ID_account`(`customID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '账套信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_basic_account
-- ----------------------------
INSERT INTO `t_basic_account` VALUES ('2ae1099b794346dd9399d34e00f8b51f', '223d3c4b909c4df2aa45c8a6af397563', '71e80e0716944e78acc147323110096c', '深圳市佳欣成机电设备有限公司', '2019-01-13', 3, '新会计准则', '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-03-25 09:45:58', '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-03-13 17:51:03', '', '2019-03-13 17:52:51', '2019-01', 2, '123456', '914403001922038216', 1, 1, 0, 1, '2', '223d3c4b909c4df2aa45c8a6af397563', '2019-03-25 09:45:57', 'szsjxcjdsbyxgs', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_basic_bankaccount2subject
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_bankaccount2subject`;
CREATE TABLE `t_basic_bankaccount2subject`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账套ID',
  `bankAccount` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行账户',
  `bankName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bankType` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行类型',
  `currency` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `subID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行科目主键',
  `subCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行科目编码',
  `subName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `subFullName` varchar(244) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目全名',
  `createID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `createName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `createTime` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间戳',
  `createTel` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者手机号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '银行账户和科目映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_basic_bankaccount2subject
-- ----------------------------
INSERT INTO `t_basic_bankaccount2subject` VALUES ('77fd4ddac4e345109af46808346ce1d5', '8b292128ea13438d87ff5c1c4502b2ce', '743257944046', '中国银行', '中国银行', 'CNY', '44c5ebb7c52d41babdcf8e36f88c4864', '1002002', '中国银行-743257944046', '银行存款_中国银行-743257944046', '9860d90ca3604596bafa5e6b8027a7d2', '管理员', '2018-11-30 11:59:30', '18110719680');
INSERT INTO `t_basic_bankaccount2subject` VALUES ('7a740895c9304f7ba638eac97e0745c2', '7c3a09a7be2a444393a3f89a421506b1', '743257944046', '中国银行', '中国银行', '人民币', 'e0e38df44faf4b1a80048574a7ed32d7', '1002002', '中国银行743257944046', '银行存款_中国银行743257944046', 'd2e4ee6d2d22476f83f54d0f80b1a1fe', '吴志强~逻辑强', '2019-03-29 08:55:48', '13500000005');
INSERT INTO `t_basic_bankaccount2subject` VALUES ('a3f839c198104bfd803c5b45340a823a', '1d36e00c0ea242dfb2b65fbe2e100daf', '743257944046', '中国银行', '中国银行', '人民币', '6fcde8e0261f4bfc936c000ac80d57b1', '1002002', '中国银行743257944046', '银行存款_中国银行743257944046', 'd2e4ee6d2d22476f83f54d0f80b1a1fe', '吴志强~逻辑强', '2019-03-29 10:29:05', '13500000005');
INSERT INTO `t_basic_bankaccount2subject` VALUES ('b19eb3fec50946d1af5701bce6e909d3', '2ae1099b794346dd9399d34e00f8b51f', '743257944046', '中国银行', '中国银行', '人民币', '7af0e8a9f3bb444f9f0981a0b720cd79', '1002002', '中国银行743257944046', '银行存款_中国银行743257944046', '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-03-13 19:54:37', '18684844593');
INSERT INTO `t_basic_bankaccount2subject` VALUES ('c189ac6ffbef42fc81eb2f4606d5e51e', '239a1e6ff1104c0db8d96e6c36c75e64', '743257944046', '中国银行', '中国银行', '人民币', 'c5230bd9387e4405a4762f16f8dc2a65', '1002002', '中国银行743257944046', '银行存款_中国银行743257944046', 'e4b88a898a5c4c94bfe30995176ab638', NULL, '2019-03-14 17:56:04', '15989744575');

-- ----------------------------
-- Table structure for t_basic_subject_book
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_subject_book`;
CREATE TABLE `t_basic_subject_book`  (
  `sub_bk_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `accountID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套',
  `period` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `vouchID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '凭证主键',
  `vouchAID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '凭证体主键',
  `vouchNum` int(8) NULL DEFAULT NULL COMMENT '凭证号',
  `vcabstact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `sub_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目编码',
  `sub_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `debitAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '借方金额',
  `creditAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '贷方金额',
  `blanceAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '余额',
  `direction` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目方向',
  `updateDate` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `up_date` bigint(20) NULL DEFAULT NULL COMMENT '时间戳',
  `isEndSubCode` tinyint(2) NULL DEFAULT NULL COMMENT '是否为更新末级科目 0 否1是',
  PRIMARY KEY (`sub_bk_id`) USING BTREE,
  INDEX `sub_bk_accountID`(`accountID`) USING BTREE,
  INDEX `sub_bk_period`(`period`) USING BTREE,
  INDEX `sub_bk_sub_code`(`sub_code`) USING BTREE,
  INDEX `sub_bk_vouchID`(`vouchID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_basic_subject_book
-- ----------------------------
INSERT INTO `t_basic_subject_book` VALUES (164, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'f37761efd6434cb4a6944c6d5b951ad0', '82414e3f59b82bb396912deabc494786', 1, '现金收入', '1001', '库存现金', 1000.00000000, 0.00000000, 0.00000000, '1', '2019-04-04 16:05:36', 1554365136083, 1);
INSERT INTO `t_basic_subject_book` VALUES (165, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'f37761efd6434cb4a6944c6d5b951ad0', 'e49c65abba847cf41d283c662340bfc9', 1, '现金收入', '6001', '主营业务收入', 0.00000000, 983.00000000, 0.00000000, '2', '2019-04-04 16:05:36', 1554365136090, 1);
INSERT INTO `t_basic_subject_book` VALUES (166, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'f37761efd6434cb4a6944c6d5b951ad0', 'd57ff5b62fb12ac8de338795380f0c7f', 1, '现金收入', '2221002', '未交增值税', 0.00000000, 17.00000000, 0.00000000, '2', '2019-04-04 16:05:36', 1554365136098, 1);
INSERT INTO `t_basic_subject_book` VALUES (167, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'f37761efd6434cb4a6944c6d5b951ad0', 'd57ff5b62fb12ac8de338795380f0c7f', 1, '现金收入', '2221', '应交税费', 0.00000000, 17.00000000, 0.00000000, '2', '2019-04-04 16:05:36', 1554365136106, 0);
INSERT INTO `t_basic_subject_book` VALUES (168, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a44e4024e4c34432a585d61deb507047', '8dbbf934a9a9d51742c169ef1e11c604', 1, '现金收入', '1001', '库存现金', 1000.00000000, 0.00000000, 0.00000000, '1', '2019-04-04 16:09:36', 1554365375575, 1);
INSERT INTO `t_basic_subject_book` VALUES (169, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a44e4024e4c34432a585d61deb507047', 'cca6664f8800dde10f3bfe575030f53d', 1, '现金收入', '6001', '主营业务收入', 0.00000000, 983.00000000, 0.00000000, '2', '2019-04-04 16:09:36', 1554365375587, 1);
INSERT INTO `t_basic_subject_book` VALUES (170, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a44e4024e4c34432a585d61deb507047', '8f36cae3786bbbb3802c1b7ac2722d58', 1, '现金收入', '2221002', '未交增值税', 0.00000000, 17.00000000, 0.00000000, '2', '2019-04-04 16:09:36', 1554365375596, 1);
INSERT INTO `t_basic_subject_book` VALUES (171, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a44e4024e4c34432a585d61deb507047', '8f36cae3786bbbb3802c1b7ac2722d58', 1, '现金收入', '2221', '应交税费', 0.00000000, 17.00000000, 0.00000000, '2', '2019-04-04 16:09:36', 1554365375616, 0);
INSERT INTO `t_basic_subject_book` VALUES (172, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a8d7c96f8add4ed0b104d9c80dc2d9e4', '15ec19050dd39ddc171b43a478bf8e1b', 2, '现金购商品', '1405001', '打印机', 1000.00000000, 0.00000000, 0.00000000, '1', '2019-04-04 16:56:44', 1554368203923, 1);
INSERT INTO `t_basic_subject_book` VALUES (173, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a8d7c96f8add4ed0b104d9c80dc2d9e4', '15ec19050dd39ddc171b43a478bf8e1b', 2, '现金购商品', '1405', '库存商品', 1000.00000000, 0.00000000, 0.00000000, '1', '2019-04-04 16:56:44', 1554368203930, 0);
INSERT INTO `t_basic_subject_book` VALUES (174, '2ae1099b794346dd9399d34e00f8b51f', '2019-01', 'a8d7c96f8add4ed0b104d9c80dc2d9e4', '4317bcd22e79ed49af66e5a26dbee63d', 2, '现金购商品', '1001', '库存现金', 0.00000000, 0.00000000, 0.00000000, '1', '2019-04-04 16:56:44', 1554368203937, 1);

-- ----------------------------
-- Table structure for t_basic_subject_message
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_subject_message`;
CREATE TABLE `t_basic_subject_message`  (
  `pk_sub_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `sub_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编码',
  `superior_coding` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '  父级编码  (1级为0，二级取前4位）',
  `account_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账套ID',
  `account_period` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '做帐的真实期间 年 - 月(帐套启用年-月份）',
  `sub_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `init_debit_balance` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '期初余额(借方)',
  `init_credit_balance` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '期初余额(贷方)',
  `current_amount_debit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '本期发生额(借方)',
  `current_amount_credit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '本期发生额(贷方)',
  `ending_balance_debit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '期末余额(借方)',
  `ending_balance_credit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '期末余额(贷方)',
  `year_amount_debit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '本年累计发生额(借方)',
  `year_amount_credit` decimal(20, 8) NULL DEFAULT 0.00000000 COMMENT '本年累计发生额(贷方)',
  `is_multiple_siblings` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否多个同级(0无，1有)',
  `excel_import_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'excel导入的编码',
  `excel_import_siblings_coding` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'excel导入的同级编码(一个银行多个外币时用到)',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `excel_import_period` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导入的期间只有月份',
  `type_of_currency` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币别',
  `siblings_sub_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `excel_import_superior_coding` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'excel导入的上级编码(1级为0，二级取前4位）',
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目完整名称',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_timestamp` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间戳',
  `category` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)',
  `sub_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '科目来源（导入,新增）',
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `unit_id` decimal(20, 8) NULL DEFAULT NULL COMMENT '计量单位ID',
  `price` decimal(20, 8) NULL DEFAULT NULL COMMENT '单价(国际单位)',
  `number` decimal(20, 8) NULL DEFAULT NULL COMMENT '数量',
  `amount` decimal(20, 8) NULL DEFAULT NULL COMMENT '金额=数量*金额',
  `siblings_coding` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 同级编码(一个银行多个外币时用到)',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT ' 启用状态(0禁用，1启用）',
  `mender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `fk_t_basic_measure_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位主键',
  `measure_state` int(2) NULL DEFAULT 0 COMMENT '计量单位核算状态(0关闭，1开启）',
  `fk_exchange_rate_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇率设置主键',
  `exchange_rate__state` int(2) NULL DEFAULT 0 COMMENT '外币设置状态(0关闭，1开启）',
  `code_level` int(2) NULL DEFAULT NULL COMMENT '编码级别',
  `debit_credit_direction` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '借贷方向（1.debit借方， 2.credit贷方）',
  PRIMARY KEY (`pk_sub_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'EXCEL科目档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_basic_subject_message
-- ----------------------------
INSERT INTO `t_basic_subject_message` VALUES ('09583828f53e2b186bab2eb283e65a94', '6602', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '管理费用', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('29804ccfae8db3b2482a5064646367c6', '6602002', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '房租', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_房租', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('2e8bbf432fa67dc5315956c71b5fbdd6', '2202', '2202', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '应付账款', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应付账款', NULL, NULL, '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('365135d78eb0d586b3226f0e91ae2fef', '2221', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '应交税费', 0.00000000, 0.00000000, 0.00000000, 17.00000000, 0.00000000, 0.00000000, 0.00000000, 17.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应交税费', '2019-04-04 16:09:36', '1554365375616', '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '2');
INSERT INTO `t_basic_subject_message` VALUES ('491d8d5a28f3a86b8a3b0bddde3f9203', '1122', '1122', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '应收账款', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应收账款', NULL, NULL, '1', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('4a03d8372dd8126d60b826582060713e', '6602001', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '办公用品', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_办公用品', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('5547061d08c3dbdd4d76259b7ddce32e', '6602006', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '市内交通费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_市内交通费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('55bae6e01782cd4abbc360737abd80ed', '1405001', '1405', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '打印机', 0.00000000, 0.00000000, 1000.00000000, 0.00000000, 0.00000000, 0.00000000, 1000.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '库存商品_打印机', '2019-04-04 16:56:44', '1554368203923', '1', '手动新增2019-04-04 16:56:43', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('683f2ab0b304a3a4eaff08b6be8de629', '1001', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '库存现金', 0.00000000, 0.00000000, 1000.00000000, 1000.00000000, 0.00000000, 0.00000000, 1000.00000000, 1000.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '库存现金', '2019-04-04 16:56:44', '1554368203937', '1', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('72692c1e4a1272705d191233c8846cec', '2221002', '2221', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '未交增值税', 0.00000000, 0.00000000, 0.00000000, 17.00000000, 0.00000000, 0.00000000, 0.00000000, 17.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应交税费_未交增值税', '2019-04-04 16:09:36', '1554365375596', '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '2');
INSERT INTO `t_basic_subject_message` VALUES ('779185ec2f49e5ee5cfa8fbadef1017b', '2211', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '应付职工薪酬', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应付职工薪酬', NULL, NULL, '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '2');
INSERT INTO `t_basic_subject_message` VALUES ('810a1e199046bb09ee136230a1de92f7', '6602003', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '物业管理费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_物业管理费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('99fc31e3fecbac1a6b866797953c2e95', '2241', '2241', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '其他应付款', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '其他应付款', NULL, NULL, '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('9a500a51f7d713ac675ca762f133f606', '6602017', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '咨询费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_咨询费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('a12afdb8a4d9115a286a670f760195c4', '6602021', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '通讯费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_通讯费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('a3b9091e98daa74992cd19dd5aaf2c9d', '6602005', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '交际应酬费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_交际应酬费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('a5da39ce4623fed96a6a701fe3bcdf34', '6602004', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '水电费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_水电费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('af541b3f755ae9a1fcefbd7265f6b7f3', '1002', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '银行存款', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '银行存款', NULL, NULL, '1', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('b21e0a1a8ba98df09fc014dca9236c18', '6602016', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '福利费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_福利费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('cbe7831d562dc7ce44b220087a93e33c', '1405', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '库存商品', 0.00000000, 0.00000000, 1000.00000000, 0.00000000, 0.00000000, 0.00000000, 1000.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '库存商品', '2019-04-04 16:56:44', '1554368203930', '1', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('cd6bc4c2e55209eb1852ddb9238350a7', '2211001', '2211', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '工资', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '应付职工薪酬_工资', NULL, NULL, '2', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '2');
INSERT INTO `t_basic_subject_message` VALUES ('cf138ef4287e182622bc7d835d8de8fd', '6602012', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '工资', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_工资', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('d808b7401d647e645ef5cf5d1150bbea', '6001', '0', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '主营业务收入', 0.00000000, 0.00000000, 0.00000000, 983.00000000, 0.00000000, 0.00000000, 0.00000000, 983.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '主营业务收入', '2019-04-04 16:09:36', '1554365375587', '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '2');
INSERT INTO `t_basic_subject_message` VALUES ('e3bd94c11c3e050706bc13fac6a3def8', '1221', '1221', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '其他应收款', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '其他应收款', NULL, NULL, '1', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 1, '1');
INSERT INTO `t_basic_subject_message` VALUES ('eeb25012ca8de857462e47f9734f1c2d', '6602018', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '运输费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_运输费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');
INSERT INTO `t_basic_subject_message` VALUES ('fc1d4178bdc87def388577fd21325a52', '6602008', '6602', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '差旅费', 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.00000000, '0', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', NULL, NULL, NULL, NULL, '管理费用_差旅费', NULL, NULL, '6', '自动初始化', NULL, NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, 0, NULL, 1, 2, '1');

-- ----------------------------
-- Table structure for t_basic_subject_parent
-- ----------------------------
DROP TABLE IF EXISTS `t_basic_subject_parent`;
CREATE TABLE `t_basic_subject_parent`  (
  `p_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `sub_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目代码',
  `sub_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目名称',
  `sub_fullName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目全名',
  `dir` smallint(3) NULL DEFAULT NULL COMMENT '借贷方向（1.debit借方， 2.credit贷方）',
  `accstandards` smallint(3) NULL DEFAULT NULL COMMENT '会计准则(3:新会计准则,1小企业会计准则2政府民间非营利组织4村集体会计准则)',
  `level` smallint(3) NULL DEFAULT NULL COMMENT '等级',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类别(1、资产2、负债　3、共同4、权益5、成本6、损益  7净资产 8收入费用)',
  `categoryName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目分组名称',
  `creatorDate` date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`p_id`) USING BTREE,
  UNIQUE INDEX `sub_code`(`sub_code`) USING BTREE,
  UNIQUE INDEX `sub_name`(`sub_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'EXCEL科目档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_basic_subject_parent
-- ----------------------------
INSERT INTO `t_basic_subject_parent` VALUES ('014b9c0a11fd412493a2391ff48f76cf', '6301', '营业外收入', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('031b027e72f341a6b4c59dc3d7215400', '2901', '递延所得税负债', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('03dec9733483498bba2ee352d1da6fdc', '1002', '银行存款', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('04e55d60a3ff49d996ae9c8d07353634', '1404', '材料成本差异', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('0ad4941e3d19402194cf81e3418c1b7f', '1601', '固定资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('11d071b9dbbf41a39eda3a80896c68e7', '4001', '实收资本', NULL, 2, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('16cc9947a29449d28d8c2728daa71e79', '1606', '固定资产清理', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('184ed691a4d5450eb26d45ae15c71cd1', '2401', '递延收益', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('1a4d4c2335e64b039d045bad557ffd36', '2701', '长期应付款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('1cbe2cb9f57943acaf08a8e5cb6bb898', '4002', '资本公积', NULL, 2, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('1ed856bf7d1b4305bbc43ab6b34c17db', '6051', '其他业务收入', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('1f1fe19944a74e8dbb391460f3bb994f', '1801', '长期待摊费用', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('1f7dcc058dd64c878433deee90458045', '3201', '套期工具', NULL, 1, NULL, NULL, '3', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('24faf7d8219e47718c06abb47fc6807d', '1471', '存货跌价准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('250e833d89a74ab0ba6e1e87c5a10f1e', '6402', '其他业务成本', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('29fd6d768ce9410e93387cca9d191ab6', '1701', '无形资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('2bf03c8a3de945919c77b04a07eed44f', '1411', '周转材料', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('2d0c98d4c0544c0bb117a98c77a44e71', '1501', '持有至到期投资', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('31407d0d723b45cd989536267a683362', '2001', '短期借款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('3e42bb8ca90e41408644a23787d1fda1', '1503', '可供出售金融资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('3f3fcddd66924c0f8239538ba697008e', '6801', '所得税费用', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('3fa9f9257bef4b599a7c4082e8ebb2e6', '6603', '财务费用', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('42791c65c183496da6605c499b973e5b', '1605', '工程物资', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('42aad9b4bb3e45f0a9c7224eff01e4e8', '1901', '待处理财产损溢', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('438ea29d11484a1b9b628e9b141793eb', '2232', '应付股利', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('486d06946aea4eb68aa8e5cf591b3ec5', '2221', '应交税费', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('4a1ebf380e91402a9c0abe5737aa9d42', '1531', '长期应收款', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('4dedabd3487942bf8ae9cbc7a8f6a4ee', '1512', '长期股权投资减值准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('4f55f418a5f24e2ab14327ddb8cc20c6', '1406', '发出商品', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('50adfb02e31f4e1fa6681fe62e1b8c5a', '4201', '库存股', NULL, 1, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('58a920e01b194f4b95aaded7c065040c', '2501', '长期借款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('590917c2c6b6443593d0d6b64a4bec98', '1321', '代理业务资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('595ca88adb31442692e930f451a5c5c6', '2201', '应付票据', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('5ae7e7e05d934b35a45da9889ef96de5', '1402', '在途物资', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('5bfab8d34a79460f817084d2713ac714', '2711', '专项应付款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('5f296e82737f4c07ba56e0b39a3db4cf', '2211', '应付职工薪酬', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('61b53175ab6c4d22b6e802c958e08382', '1405', '库存商品', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('672f64417aca4562b3a4a81009b14ba3', '2801', '预计负债', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6892ea8721204bdcb93e140ac30d5d42', '1122', '应收账款', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('68f6b373ffc3486fb94915a4c723bd7b', '4104', '利润分配', NULL, 2, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6a77de38d2284e21b1f1a6300d248df0', '1408', '委托加工物资', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6aac98064c17484c860de1fa4649f262', '1521', '投资性房地产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6afc8255c8164555ab16686d25fd4138', '2203', '预收账款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6b6494b7c3ae43868bf396afab9b961d', '1012', '其他货币资金', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6b6e3119c5cc48e7a965a8f81ec91f1a', '5001', '生产成本', NULL, 1, NULL, NULL, '5', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('6dc5f5fee6cc488ab0b4c763edd56824', '2314', '代理业务负债', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('71a3b24c1ee24e69ac048fe4c4202e91', '1603', '固定资产减值准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('739221ae086f4bebab6c5b27bf582c35', '1604', '在建工程', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('7f48ab45f908405486b872ac8978f9bc', '4101', '盈余公积', NULL, 2, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('899d2033165f44f58c3880be57699a4e', '6601', '销售费用', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('8b0a7d33cea34f4db59eaeda2aa3adc1', '1532', '未实现融资收益', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('8ba08ba31f1c442a9101208413e0f7fa', '6602', '管理费用', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('8d7b2d024dd348b8836d2441a0ba7fa4', '1702', '累计摊销', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('9226d9ad5c77436f8a87bd19fc727085', '1403', '原材料', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('97a9ac8a0c954912807653fcf2b09517', '1703', '无形资产减值准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('9ce7841d1df3428987ea3dc3b64cac4b', '1001', '库存现金', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('9d426fe2bf9f481ebd378eefb1b68114', '6701', '资产减值损失', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('a8b97b115cbd416686bea6cdb87a1443', '5301', '研发支出', NULL, 1, NULL, NULL, '5', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('ab94774a657c4cae994798628beaf544', '1231', '坏账准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('aed7d4a26b9e46e1bef40ced4e9ca5dd', '1131', '应收股利', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('af5e00e2c0724de592f80eb06a5d14aa', '1502', '持有至到期投资减值准备', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('b3494e58c3384be986258facc953b4e1', '5101', '制造费用', NULL, 1, NULL, NULL, '5', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('b5a104ff4e444378877c360e31ce31b1', '1407', '商品进销差价', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('b866568f3f044c12a8b44a39d4b212aa', '1811', '递延所得税资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('bb49beb48b3943abb8e9705076ce3f42', '6901', '以前年度损益调整', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('bcaa9a0867054d6cb67adc124bc7af59', '1121', '应收票据', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('c9753dd09e754cf8b456560bca30141e', '6401', '主营业务成本', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('cd4456fa1d80412e97e75a43c13fe137', '6403', '营业税金及附加', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('ceb1ad45d7d1429196a384bd9af8ade9', '1602', '累计折旧', NULL, 2, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d17e95c234c0440facef18521e126603', '2702', '未确认融资费用', NULL, 1, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d3440486cd6749029636041bcf98f422', '1401', '材料采购', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d414d6c2594c457dba132c6f60d61ef2', '1101', '交易性金融资产', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d41d3a0a06b5431fbb714f7dc3c2a7bb', '6001', '主营业务收入', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d7751450fd214f2cb546042b25e58aff', '4103', '本年利润', NULL, 2, NULL, NULL, '4', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d7cb48a212ca4e32934a507c2c458808', '3101', '衍生工具', NULL, 1, NULL, NULL, '3', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d8bebfccadce49c4a982e702a3f0a724', '1132', '应收利息', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d96a4c9e1e514e91be9a329b4840ef3a', '2502', '应付债券', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('d98c894a2a984c358c1f9da25848d30e', '1221', '其他应收款', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('e03d615ea65144c0aa0a5abc87b1e49b', '2231', '应付利息', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('e1272b112e824f85b591e7447399aea3', '6101', '公允价值变动损益', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('e3d7e1140cf145ae86a00e3a3bc7bf1f', '6111', '投资收益', NULL, 2, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('eb39db4dc9894e64aa95cf3dc18e106b', '2101', '交易性金融负债', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('ed8b5e14f5f74eedb4d53a62209894db', '2241', '其他应付款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f1c5a62bb9114600ba100c5f1ff1a216', '5201', '劳务成本', NULL, 1, NULL, NULL, '5', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f1f716c8781648b4a433cd22f4c70b6a', '1511', '长期股权投资', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f281e30e82f6463484246881bd3e1e2d', '6711', '营业外支出', NULL, 1, NULL, NULL, '6', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f5881a1b18ba493e8f16b743d3048bdc', '1711', '商誉', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f702e731acac4e81bc143fa38fcc756f', '2202', '应付账款', NULL, 2, NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('f8e698b63af14f6e9ba2192f875fec0f', '1123', '预付账款', NULL, 1, NULL, NULL, '1', NULL, NULL);
INSERT INTO `t_basic_subject_parent` VALUES ('fc628c45eb474f5ea551df308ceff7d1', '3202', '被套期项目', NULL, 1, NULL, NULL, '3', NULL, NULL);

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `customID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户ID',
  `cusName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `busNature` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业性质',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套ID',
  `cusAddress` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户详细地址',
  `cusPhone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户电话号码',
  `belongPersonID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属于谁ID',
  `belongPerName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属人昵称',
  `createPersionID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `createPerName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人昵称',
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司ID（天眼查的公司id）',
  `createDate` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tax_num` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `binding_phone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定手机号',
  `state_tax_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国税密码',
  `land_tax_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地税密码',
  `real_account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名账号',
  `real_account_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名账号密码',
  `period` date NULL DEFAULT NULL COMMENT '期间',
  `com_tyxydm` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一信用代码',
  `three_and_one` int(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '三证合一 0：否 1：是',
  `com_kjxzxqy` int(1) NULL DEFAULT NULL COMMENT '科技型中小企业0：否 1：是',
  `com_gxjsqy` int(1) NULL DEFAULT NULL COMMENT '高新技术企业0：否 1：是',
  `com_jsrgynssx` int(1) NULL DEFAULT NULL COMMENT '技术入股递延纳税事项0：否 1：是',
  `companyType` int(2) NULL DEFAULT NULL COMMENT '企业性质(1：生产型2：贸易型3：服务型4：混合型5：物业)',
  `ssType` int(11) NULL DEFAULT NULL COMMENT '一般纳税人（传0）   小规模（传1）',
  `accstandards` int(11) NULL DEFAULT NULL COMMENT '会计准则(1:2007企业会计准则2:2013小企业会计准则3:新会计准则)',
  `jzbwb` int(2) NULL DEFAULT NULL COMMENT '记账本位币(1:人民币)',
  `com_scale` int(10) NULL DEFAULT NULL COMMENT '企业规模',
  `com_city_area` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市区域',
  `com_comcode` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '总分机构',
  `pay_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缴费方式0：微信缴税 1：银行缴税',
  `increment_tax` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '增值税0：否 1：是',
  `income_tax` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业所得税0：否 1：是',
  `attachment_tax` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附征税0：否 1：是',
  `stamp_duty_tax` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '印花税0：否 1：是',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户银行名称',
  `bank_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `data_synchro` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '与税务局数据同步  0：未同步  1：已同步',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `provinceName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cityName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`customID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES ('71e80e0716944e78acc147323110096c', '深圳市佳欣成机电设备有限公司', NULL, '2ae1099b794346dd9399d34e00f8b51f', '深圳市龙华区某某', '13666666666', '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '', '2019-03-13 00:00:00', NULL, '13666666666', '123456', NULL, '213213', '21321', '2019-01-13', '914403001922038216', 0, 1, 0, 0, 2, 0, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '20', '广东省', '214', '深圳市');

-- ----------------------------
-- Table structure for t_fa_invoice_b
-- ----------------------------
DROP TABLE IF EXISTS `t_fa_invoice_b`;
CREATE TABLE `t_fa_invoice_b`  (
  `invoiceBID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `invoiceHID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票主表主键(外键)',
  `period` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `taxRate` decimal(20, 2) NULL DEFAULT NULL COMMENT '税率',
  `ntaxAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '价税合计',
  `taxAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '税额',
  `namount` decimal(20, 8) NULL DEFAULT NULL COMMENT '金额',
  `nnumber` decimal(20, 8) NULL DEFAULT NULL COMMENT '数量',
  `comName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `comNameSpec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称规格',
  `invoiceType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1进项2销项',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属账套',
  `measureID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位ID(外键)',
  `measure` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `nprice` decimal(20, 8) NULL DEFAULT NULL COMMENT '单价',
  `taxClass` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '税种类别',
  `updatePsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `updatePsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updatedate` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `createPsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `CREATEpsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `userID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
  `taxTypeCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '税收分类编码',
  `des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sub_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '映射科目',
  `sub_full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '映射科目全名',
  PRIMARY KEY (`invoiceBID`) USING BTREE,
  INDEX `FK_ID_invoice_b_accountID`(`accountID`) USING BTREE,
  INDEX `FK_ID_invoice_b_invoiceHID`(`invoiceHID`) USING BTREE,
  INDEX `fa_invoice_b_index_period`(`period`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fa_invoice_b
-- ----------------------------
INSERT INTO `t_fa_invoice_b` VALUES ('022d7b75452546ccb1528467799528a6', '11b8bd00180a4ab68ffa89ef0b63721c', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金飞刀', NULL, '铝合金飞刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405022', '库存商品_铝合金飞刀');
INSERT INTO `t_fa_invoice_b` VALUES ('022dfb0b95f04253b52c311cf0af0fe4', '5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金热风刀（不用）', NULL, '铝合金热风刀（不用）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405030', '库存商品_铝合金热风刀（不用）');
INSERT INTO `t_fa_invoice_b` VALUES ('095dfc21dcd04b1a9ad69499ed92c58b', '1cba3c9abd45430f9c01d7ec962a0628', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀（680L）', NULL, '铝合金风刀（680L）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405005', '库存商品_铝合金风刀（680L）');
INSERT INTO `t_fa_invoice_b` VALUES ('0aa23373891f4da2a77c2f2c824fb76b', '7b07abb40f0c4853b02702f74ab2c5a2', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '套模治具', NULL, '套模治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405036', '库存商品_套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('0ac0b3c256984c8ab4a7409fbef85ea2', 'ca6264a29d234ec9b1ace2cf5b72ddff', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金强风刀', NULL, '铝合金强风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405019', '库存商品_铝合金强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('0c10303e55734b1fb8997fa80e566b73', 'ea578b2e5c614a9db0d72520e1f09b5a', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金气刀', NULL, '铝合金气刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405053', '库存商品_铝合金气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('0c52e154014b4112a84047c3e40d3da1', '1c8c5d1516cb48c4aca5580232af85a3', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金热风刀（把）', NULL, '铝合金热风刀（把）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405018', '库存商品_铝合金热风刀（把）');
INSERT INTO `t_fa_invoice_b` VALUES ('0cd5d196dfc94939a44bbef9ecc7ff07', '5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '340风刀', NULL, '340风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405028', '库存商品_340风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('0d191292400c42968eceb7ce793d18fc', '94135131559d4c4e83a9c2deec50169e', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('0fee280f3209497cbf915f4740db38f0', 'd50b1e744822476a8a0409fe0f246225', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '升降器', NULL, '升降器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405009', '库存商品_升降器');
INSERT INTO `t_fa_invoice_b` VALUES ('1a6547f851034212ac38cede4052a54b', 'ae3b218aecfb4118b3e2c1e650c83b8b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '套模治具', NULL, '套模治具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405036', '库存商品_套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('1e189d6ba1884be28e6c55ab2ae80392', 'f9294d8f39b34502927bf154928eabbb', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀热', NULL, '铝合金风刀热', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405024', '库存商品_铝合金风刀热');
INSERT INTO `t_fa_invoice_b` VALUES ('1edd9b37eab94f76b82372e8b025c2b7', 'a277587c062043f580a79c62df3085f8', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝型材', NULL, '铝型材', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405004', '库存商品_铝型材');
INSERT INTO `t_fa_invoice_b` VALUES ('229395382cdc4cabb76b8c207d105389', '635afc6a8805486fb2c22de81aa4b604', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, 'AA风刀', NULL, 'AA风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405020', '库存商品_AA风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('2f2f0d7822ed4640b27fcd4202704d6c', '94135131559d4c4e83a9c2deec50169e', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('31af74c2d3bf4c05890905ed9ce1153b', 'def3aac716af45e283eb0545859233e3', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '风刀（批）', NULL, '风刀（批）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405027', '库存商品_风刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('32168cd42ca64277a22e668bb52e0450', 'ee6ab694fe5b40d1b05ef83215e99d76', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢锁紧套', NULL, '不锈钢锁紧套', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405046', '库存商品_不锈钢锁紧套');
INSERT INTO `t_fa_invoice_b` VALUES ('32261739592f467ea3d909fe4819f242', 'd50b1e744822476a8a0409fe0f246225', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '风刀', NULL, '风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405013', '库存商品_风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('328c642bd0e8444d9e02ccae2e11aca5', '37f7427335974f7dbd2fe1a86144751f', '2019-01', 0.16, 10549.44000000, 2557.44000000, 7992.00000000, 4.00000000, '吸刀', NULL, '吸刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405003', '库存商品_吸刀');
INSERT INTO `t_fa_invoice_b` VALUES ('32a3668db5c140028c59ea88b09b7722', 'bd6211742dc84106acfa3eb3e646928b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '风刀', NULL, '风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405013', '库存商品_风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('34cdcb2247224e5fb0de5c4140de2573', '2966746895f242ee8f30157d2ab043b6', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '弯切脚治具', NULL, '弯切脚治具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405012', '库存商品_弯切脚治具');
INSERT INTO `t_fa_invoice_b` VALUES ('363fa88ff26d46538a7ae9376de51047', '758dfb5f6e72456ab25a91cb74df35a1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('3671394c5b6240718933a7291dcc64e6', '1d23e9362ec84f8f98a4edd3a9d9a820', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '电脑', NULL, '电脑', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405055', '库存商品_电脑');
INSERT INTO `t_fa_invoice_b` VALUES ('369ecc27ae6c44d7aa939f88b71191a0', 'c882bc06299e42e4af0762cb18798684', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀（580L）', NULL, '铝合金风刀（580L）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405037', '库存商品_铝合金风刀（580L）');
INSERT INTO `t_fa_invoice_b` VALUES ('38e4680b24e24a4cb826fc75ea3ee7ad', '5a6666b1b44d4937b234b0c249982944', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('39f984ef9b6b4453aabf0e3381b847b9', '690160bf12c24023b87cc79b65b64d3c', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '强风刀', NULL, '强风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405014', '库存商品_强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('3b47e63f56f24306a398febc1f8bf9d2', '6f4f450aad624b4886bb61679c630c5b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '小孔风刀', NULL, '小孔风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405026', '库存商品_小孔风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('3c079bbe7cd24e8f9bfa2a7494cbfe7b', '4fbe21a4821847828db48f2ee4ad8446', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '测试架', NULL, '测试架', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405006', '库存商品_测试架');
INSERT INTO `t_fa_invoice_b` VALUES ('3db934c6550f410c8e292016310c3008', '621529a350af4cdb9245397c58ed2628', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '气刀', NULL, '气刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405038', '库存商品_气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('3fde4a4dcb8e4f2b8d00203874e9f6e4', '3992fb697f934879946691701d0f85db', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '三极管无丝印面刷胶治具', NULL, '三极管无丝印面刷胶治具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405007', '库存商品_三极管无丝印面刷胶治具');
INSERT INTO `t_fa_invoice_b` VALUES ('40db98db80aa4ac2b265b117793c7e50', 'f66348a698d14925b7f83b043246874c', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '组立治具', NULL, '组立治具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405011', '库存商品_组立治具');
INSERT INTO `t_fa_invoice_b` VALUES ('44c7e27d127042b9a30cf445e498c69c', '5aabee98a87f4669b09a393543e048a9', '2019-01', NULL, 8551.44000000, 2557.44000000, 5994.00000000, 3.00000000, '三极管无丝印面刷胶治具', NULL, '三极管无丝印面刷胶治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405007', '库存商品_三极管无丝印面刷胶治具');
INSERT INTO `t_fa_invoice_b` VALUES ('474d5f50ac37426babe7c21ed2e270af', '07d1e2b5e0b743ecbd901741bfa7300a', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀热', NULL, '铝合金风刀热', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405024', '库存商品_铝合金风刀热');
INSERT INTO `t_fa_invoice_b` VALUES ('47d8f71162e24e0b8653140363c25805', '14d329949b184f37a2a48c9d65d6011c', '2019-01', NULL, 10549.44000000, 2557.44000000, 7992.00000000, 4.00000000, '快速接头', NULL, '快速接头', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405008', '库存商品_快速接头');
INSERT INTO `t_fa_invoice_b` VALUES ('49a082055e584ca38fe4a7b7a9c372aa', '995468660c67402d9cf952d12dbabe7e', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀（580L）', NULL, '铝合金风刀（580L）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405037', '库存商品_铝合金风刀（580L）');
INSERT INTO `t_fa_invoice_b` VALUES ('4a7d9288a13c4bdc8178af707e4c33e0', '61796ae5ef104d0397f9f54cdb4a8f1e', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '套模', NULL, '套模', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405051', '库存商品_套模');
INSERT INTO `t_fa_invoice_b` VALUES ('4aea26dc85e743b4a42b5f6df0d6b9ab', '8b6290320ed144cfb04f6af4c925bfcf', '2018-09', 0.17, 1000.00000000, 17.00000000, 983.00000000, 1.00000000, '打印机', '', '打印机', '2', '8b292128ea13438d87ff5c1c4502b2ce', '', '台', 983.00000000, '', NULL, NULL, NULL, '9860d90ca3604596bafa5e6b8027a7d2', '管理员', NULL, NULL, '销项发票导入', '1405001', '库存商品_打印机');
INSERT INTO `t_fa_invoice_b` VALUES ('4b8b9ce17c11424db65ec5648266861a', '0855f41c399a4999a49a3547b55ddc60', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '热风刀', NULL, '热风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405015', '库存商品_热风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('4d54dfa05487401d9d675266a676871f', '37f7427335974f7dbd2fe1a86144751f', '2019-01', 0.16, 8551.44000000, 2557.44000000, 5994.00000000, 3.00000000, '铝合金风刀', NULL, '铝合金风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405002', '库存商品_铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('4e8981653a874dd0a7740dad6d05ee1e', 'b65ff1d595f5473e93abe9b287612285', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '强铝合金风刀', NULL, '强铝合金风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405032', '库存商品_强铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('55c5b94736024420a1d0761f4a1454ce', '72901a2f123147f8b76a8dc946b0b9d9', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '喷管接头', NULL, '喷管接头', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405021', '库存商品_喷管接头');
INSERT INTO `t_fa_invoice_b` VALUES ('5dd65b4d2ebf4aa0a96fb6349bfaec25', '621529a350af4cdb9245397c58ed2628', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '套模治具', NULL, '套模治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405036', '库存商品_套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('610fe3814e3242ddb2d84bd3ebec3790', 'fd9bc1f2644e4fefbcf462a1296996e6', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '电脑', NULL, '电脑', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405055', '库存商品_电脑');
INSERT INTO `t_fa_invoice_b` VALUES ('630ba690e5674ae48418d12597ec4105', '0855f41c399a4999a49a3547b55ddc60', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '强风刀', NULL, '强风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405014', '库存商品_强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('63504f440eed4cfb8c6adab9c06a0efa', 'cb4b42f568aa4868965c683a8cd44d95', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, 'PWA-CVS-IO-R套模治具', NULL, 'PWA-CVS-IO-R套模治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405052', '库存商品_PWA-CVS-IO-R套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('63adacd2cd154900aa69b34bb831d6fd', '82c9b1f5462244abae51bedb7ae51d10', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '超级气刀', NULL, '超级气刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405034', '库存商品_超级气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('6435cc66222547cba08f02c924837321', 'ca68d947722a4287898bd073e473a396', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, 'AA强风刀', NULL, 'AA强风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405023', '库存商品_AA强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('67ec8f42e9884c05ad5e1b09fcab7452', '5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '风刀（批）', NULL, '风刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405027', '库存商品_风刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('6897e6b480704d9e94608237e77ce045', 'afef89dc2cb648c5832be3d44dc71816', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金配件', NULL, '铝合金配件', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405017', '库存商品_铝合金配件');
INSERT INTO `t_fa_invoice_b` VALUES ('6e9f0795747644c8bc9e8bcfc82197d6', 'ba9e5b25458b49e5b9809ba858e2de05', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金热风刀（把）', NULL, '铝合金热风刀（把）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405018', '库存商品_铝合金热风刀（把）');
INSERT INTO `t_fa_invoice_b` VALUES ('70d73ded70514d94a07c440b74f1e41c', '94135131559d4c4e83a9c2deec50169e', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '帅克E8精雕机', NULL, '帅克E8精雕机', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405040', '库存商品_帅克E8精雕机');
INSERT INTO `t_fa_invoice_b` VALUES ('727f994cc85f4140b14c18857c7c559a', '678949bdf75f487288ff3239284b8eb1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '超级气刀', NULL, '超级气刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405034', '库存商品_超级气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('73de1d94aaeb405ba174769b622d7f75', '93dc0e4e69b44c019e28d32a6c86ef75', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '热风刀', NULL, '热风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405015', '库存商品_热风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('74303b0a81444c83882f35f3f1499ae3', '0673069104034c06a4eda788a64faebb', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '鸭咀风刀', NULL, '鸭咀风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405016', '库存商品_鸭咀风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('759cdf93a8a0484b9e5d9b8a177a2661', '86696d50d0db419c88dbe4eb0e570dfe', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '下风刀', NULL, '下风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405042', '库存商品_下风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('788517c2cdae42fcb974f3ec15450e75', 'd50b1e744822476a8a0409fe0f246225', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '弯切脚治具', NULL, '弯切脚治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405012', '库存商品_弯切脚治具');
INSERT INTO `t_fa_invoice_b` VALUES ('78d4abc2b27043b5a96b265da103e26a', 'f92796e685c44f91a4d486ce61b19ff2', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('7b26454e99594f3b830ed50ff7380ab0', '678949bdf75f487288ff3239284b8eb1', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '热铝合金风刀', NULL, '热铝合金风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405033', '库存商品_热铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('7ce9ac18ef664c40baf44bf1372516b9', '8cf1e8649e8c4a42922b31ea28d5bff8', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀强', NULL, '铝合金风刀强', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405025', '库存商品_铝合金风刀强');
INSERT INTO `t_fa_invoice_b` VALUES ('7e0e09fed67343d7a259c74c707b5d93', '8fcaf9f489c84966a03494cd6c62d5f7', '2019-01', NULL, 8551.44000000, 2557.44000000, 5994.00000000, 3.00000000, '测试架', NULL, '测试架', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405006', '库存商品_测试架');
INSERT INTO `t_fa_invoice_b` VALUES ('7fe8253290e54672bbff2fb96ecfc224', '37f7427335974f7dbd2fe1a86144751f', '2019-01', NULL, 6553.44000000, 2557.44000000, 3996.00000000, 2.00000000, '配件', NULL, '配件', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405001', '库存商品_配件');
INSERT INTO `t_fa_invoice_b` VALUES ('85c3690ca88b4d5faa42eb31ce418614', '635afc6a8805486fb2c22de81aa4b604', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金强风刀', NULL, '铝合金强风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405019', '库存商品_铝合金强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('866a151b2a0944e2a065aa57257c0307', '1c338e95a71f4ec6a08c25232dd351e6', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢连接器', NULL, '不锈钢连接器', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405043', '库存商品_不锈钢连接器');
INSERT INTO `t_fa_invoice_b` VALUES ('8670e9b60c2d4229b5b5546034aa8f1e', 'a68efae26bfc4ebba5d492e9aceebd76', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '热铝合金风刀', NULL, '热铝合金风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405033', '库存商品_热铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('89459a2ccdad4c2fb421549680da2e26', '4d68fb1b218a4201858d08645a8906ca', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('8b0dd9473b954795a240f5d66db00f8b', '5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '强铝合金风刀', NULL, '强铝合金风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405032', '库存商品_强铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('8b5227be078642cd8a927621519c058b', '23eda9013070450cb7496384f57ca956', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '下风刀', NULL, '下风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405042', '库存商品_下风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('8dfc8f14bcd24ca3a51a0e0cfbe95bfa', 'f0b5fe6038234392aab532dc9158178e', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金型材', NULL, '铝合金型材', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405045', '库存商品_铝合金型材');
INSERT INTO `t_fa_invoice_b` VALUES ('8e1a98bc7c4b40ed8b9a0e2847d18330', 'fd9bc1f2644e4fefbcf462a1296996e6', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢喷管接头', NULL, '不锈钢喷管接头', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405047', '库存商品_不锈钢喷管接头');
INSERT INTO `t_fa_invoice_b` VALUES ('8f7503ac73f849a7aa851c50adac2c74', '6430424ddabb42a4b65b2563c3ac52ee', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀(小)', NULL, '铝合金风刀(小)', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405035', '库存商品_铝合金风刀(小)');
INSERT INTO `t_fa_invoice_b` VALUES ('9129216a54ab40cd83801ed6178f7f9a', '4fde7b5273294826904b8f550001e294', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('91c669e5fd7740b99c159c6fda3f296b', 'd50b1e744822476a8a0409fe0f246225', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '组立治具', NULL, '组立治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405011', '库存商品_组立治具');
INSERT INTO `t_fa_invoice_b` VALUES ('91f6e0a29dfd4848b019edf15e038d58', '408bda5f31c14a389a8adca70243c9d2', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '电脑', NULL, '电脑', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405055', '库存商品_电脑');
INSERT INTO `t_fa_invoice_b` VALUES ('92a583bc6596405eaf2aa5395a3acdf7', 'd3d7b9779ea84b92996eb0e6084c4b6d', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '鸭咀风刀', NULL, '鸭咀风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405016', '库存商品_鸭咀风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('98f017b400204049b4155299c43a68fe', 'fbb8f25a91a04256aa6504af0c12472e', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('99a994106a3243f79c2ba84300c11703', '37f7427335974f7dbd2fe1a86144751f', '2019-01', 0.16, 12547.44000000, 2557.44000000, 9990.00000000, 5.00000000, '铝型材', NULL, '铝型材', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405004', '库存商品_铝型材');
INSERT INTO `t_fa_invoice_b` VALUES ('9bed08261a154c10b0d44e9a03221906', '617a30576e5f4a698976c3b8b93e5443', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '套模治具', NULL, '套模治具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405036', '库存商品_套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('9d7a6f180eb045479d326aa3e5d5a871', 'e3d5871ec0184b86bd8782f011397539', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀', NULL, '铝合金风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405002', '库存商品_铝合金风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('9ddf3ca8584c43d080b01aa9e5be3214', '94135131559d4c4e83a9c2deec50169e', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '风机', NULL, '风机', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405049', '库存商品_风机');
INSERT INTO `t_fa_invoice_b` VALUES ('a25fcdd85c66471381f745e384d8b6c2', 'fd9bc1f2644e4fefbcf462a1296996e6', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('a335ef005438489d81568bf1d5269582', 'f7c61838538d4cbfaaa037041cda6a9e', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '340风刀', NULL, '340风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405028', '库存商品_340风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('a484f423be2f41b691148e8f04be4d6f', 'cb4b42f568aa4868965c683a8cd44d95', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '套模', NULL, '套模', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405051', '库存商品_套模');
INSERT INTO `t_fa_invoice_b` VALUES ('a5e5674be94e4149920e788b7c812ad4', 'bf9d295694724fe4bc362b79329d360b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金冷风刀', NULL, '铝合金冷风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405029', '库存商品_铝合金冷风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('a5fc3ed25406460289c0d66937dc19f8', '2cf54a144370459998d35575e95bb792', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金型材', NULL, '铝合金型材', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405045', '库存商品_铝合金型材');
INSERT INTO `t_fa_invoice_b` VALUES ('a94e7be7599444e7819bc5847d91e5ba', '5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金冷风刀', NULL, '铝合金冷风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405029', '库存商品_铝合金冷风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('ac0561dee9d94cd1b793e38cfbc6fe96', '8484722d65a046fda1decb9eb87e13de', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金可调风刀', NULL, '铝合金可调风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405031', '库存商品_铝合金可调风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('ac06e9f8d8ec4ad39ed3c4a7dfe76eb0', '31924dc9d2ae4a328b5830b0c49f463d', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, 'AA强风刀', NULL, 'AA强风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405023', '库存商品_AA强风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('ac4fb58981f64cc0a738fcf9901f751f', '98455d3fa1304190ad0795df0f650869', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金热风刀（不用）', NULL, '铝合金热风刀（不用）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405030', '库存商品_铝合金热风刀（不用）');
INSERT INTO `t_fa_invoice_b` VALUES ('b10dfec4fe2b497c961899920b1aae20', '4e36cdf7af5c43159c258712faecbc62', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, 'AA风刀', NULL, 'AA风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405020', '库存商品_AA风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('b1d1d007100a486bbb8df129b30c69c1', '621529a350af4cdb9245397c58ed2628', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀（580L）', NULL, '铝合金风刀（580L）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405037', '库存商品_铝合金风刀（580L）');
INSERT INTO `t_fa_invoice_b` VALUES ('b3adfd1660604c79b6c5a3b14e0e98a6', '021572e0426a4a1d888aec35c795f7ed', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀强', NULL, '铝合金风刀强', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405025', '库存商品_铝合金风刀强');
INSERT INTO `t_fa_invoice_b` VALUES ('b8e234c8977c4002ba23c94b65d1d75e', '4fde7b5273294826904b8f550001e294', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('b8ec73f0ca824937b41f3709f1c7880d', 'fd9bc1f2644e4fefbcf462a1296996e6', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢分风器', NULL, '不锈钢分风器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405048', '库存商品_不锈钢分风器');
INSERT INTO `t_fa_invoice_b` VALUES ('babe2a2313f84b0d8cb3af7b965736f2', '899a0eca49854b4eb2074e1c3197a18d', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '配件', NULL, '配件', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405001', '库存商品_配件');
INSERT INTO `t_fa_invoice_b` VALUES ('bce32029a1ee408eafd910664795a650', '621529a350af4cdb9245397c58ed2628', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀(小)', NULL, '铝合金风刀(小)', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405035', '库存商品_铝合金风刀(小)');
INSERT INTO `t_fa_invoice_b` VALUES ('bdc2276c01504427991206a4709b9b4f', '758dfb5f6e72456ab25a91cb74df35a1', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '气刀', NULL, '气刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405038', '库存商品_气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('be035dcc8be445e1bb245f541732e696', '1290c7ef44e6449a8205bec9d114c094', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '打印机', NULL, '打印机', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405054', '库存商品_打印机');
INSERT INTO `t_fa_invoice_b` VALUES ('bf9d8733120f4227995b07c08b5b2536', '94135131559d4c4e83a9c2deec50169e', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '风机', NULL, '风机', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405049', '库存商品_风机');
INSERT INTO `t_fa_invoice_b` VALUES ('c0a1fcf98ead4a1b90e58c6db092770a', 'e39084ce18924519abaf70d3e1c9ea36', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '吸刀', NULL, '吸刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405003', '库存商品_吸刀');
INSERT INTO `t_fa_invoice_b` VALUES ('c0ff5fdee8224b26825aa61838a7b15d', '7369c7814d6e42fab4ac2a566082b826', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢风刀', NULL, '不锈钢风刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405041', '库存商品_不锈钢风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('c5241f0355c143f29aa7353c4f114667', '23d61059d1294775b8bffd3374cab194', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '气刀', NULL, '气刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405038', '库存商品_气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('c538e1c0823f4414984fa6777e3295f6', 'f0f975ab6c344adc8c1b56e37ed2fdf8', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '打印机', NULL, '打印机', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405054', '库存商品_打印机');
INSERT INTO `t_fa_invoice_b` VALUES ('c5e4f686fc5b4dba8e4486e9baf1454f', '79304c33837e4836b08e200660a3fd5f', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '小孔风刀', NULL, '小孔风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405026', '库存商品_小孔风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('c717a077ab1e4b568c4258d00dadfe01', 'eefdc6d95c4d49008546407e9a9db93c', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '气刀', NULL, '气刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405038', '库存商品_气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('cdca1df7dffc453986748404c7ae3155', 'a8b20c973c8b4279b97c092f14469dd6', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢锁紧套', NULL, '不锈钢锁紧套', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405046', '库存商品_不锈钢锁紧套');
INSERT INTO `t_fa_invoice_b` VALUES ('ce60fd43b0704d559a118824cd00dd23', '450ff1c29e334feeaa29f7e471d4e096', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '升降器', NULL, '升降器', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405009', '库存商品_升降器');
INSERT INTO `t_fa_invoice_b` VALUES ('cf8e4dfcd30c4882bf386fed398f5ecc', 'f55e2266c9db4647a81789f490addd3b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '不锈钢喷管接头', NULL, '不锈钢喷管接头', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405047', '库存商品_不锈钢喷管接头');
INSERT INTO `t_fa_invoice_b` VALUES ('d0a4d8b12ae849e3a8d1dcc470d24736', '09ef3ac0f02045599cf5b94580d4d080', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '帅克E8精雕机', NULL, '帅克E8精雕机', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405040', '库存商品_帅克E8精雕机');
INSERT INTO `t_fa_invoice_b` VALUES ('d0f3d7c07e064b08a5eeff2234ca12cb', 'e76f34fcba2b476a87516858d6fea478', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金气刀', NULL, '铝合金气刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405053', '库存商品_铝合金气刀');
INSERT INTO `t_fa_invoice_b` VALUES ('d50bef82e64a47dca969b60f163e3dac', '78944129831d43f7925cf359cd08d296', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金风刀(小)', NULL, '铝合金风刀(小)', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405035', '库存商品_铝合金风刀(小)');
INSERT INTO `t_fa_invoice_b` VALUES ('d548e8938e7b4f0eab7f1a88f43136d7', '635afc6a8805486fb2c22de81aa4b604', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '喷管接头', NULL, '喷管接头', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405021', '库存商品_喷管接头');
INSERT INTO `t_fa_invoice_b` VALUES ('dc826fbcbdbf48c5bc6b0f00707e9a64', '4657a69e4f594228af12ae51073a7080', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('df77b39b05a8425d9f9c23d53a0e5afb', 'd4d574fd8cbe40a294337cc543bba3a5', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '电脑', NULL, '电脑', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405055', '库存商品_电脑');
INSERT INTO `t_fa_invoice_b` VALUES ('e69a92099f3f4d86856796059b01c997', 'd50b1e744822476a8a0409fe0f246225', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '电容成型模具', NULL, '电容成型模具', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405010', '库存商品_电容成型模具');
INSERT INTO `t_fa_invoice_b` VALUES ('e73bd2648ecf4601b741066fe509acf5', '758dfb5f6e72456ab25a91cb74df35a1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '帅克E8精雕机', NULL, '帅克E8精雕机', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405040', '库存商品_帅克E8精雕机');
INSERT INTO `t_fa_invoice_b` VALUES ('e85bbd81a6484086966c63c45f976e4f', '95103389e055430d9e3214919fa84bc6', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '风机', NULL, '风机', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405049', '库存商品_风机');
INSERT INTO `t_fa_invoice_b` VALUES ('e909a11cb1784128b5b891296f4881da', '635afc6a8805486fb2c22de81aa4b604', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀', NULL, '铝合金飞刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405022', '库存商品_铝合金飞刀');
INSERT INTO `t_fa_invoice_b` VALUES ('e989ebdd083e43988572740fcace1c79', '5aa1402cdec74679aadcedd8fe027964', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金风刀（580L）', NULL, '铝合金风刀（580L）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405037', '库存商品_铝合金风刀（580L）');
INSERT INTO `t_fa_invoice_b` VALUES ('ec7db673468f4e7c9f660d2bc9682068', '23de0313f922415b8158dbb779ac16c8', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, 'PWA-CVS-IO-R套模治具', NULL, 'PWA-CVS-IO-R套模治具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405052', '库存商品_PWA-CVS-IO-R套模治具');
INSERT INTO `t_fa_invoice_b` VALUES ('ed9947d5cfb74d89a0f18e2919e9040e', 'ae88623666484b1f9c03933bdf2d0f9c', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '铝合金配件', NULL, '铝合金配件', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405017', '库存商品_铝合金配件');
INSERT INTO `t_fa_invoice_b` VALUES ('f056c96d5cc24489a017a994ae0a8c46', '758dfb5f6e72456ab25a91cb74df35a1', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢风刀', NULL, '不锈钢风刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405041', '库存商品_不锈钢风刀');
INSERT INTO `t_fa_invoice_b` VALUES ('f1bc48ab495e490ab0ae4748ccd8bd29', '03c45577319b450b90d2052542f24692', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '不锈钢连接器', NULL, '不锈钢连接器', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405043', '库存商品_不锈钢连接器');
INSERT INTO `t_fa_invoice_b` VALUES ('f2fcd976f52044a48a478dbd0c6b3522', 'af135a9638604ff3a5227c468c029f9b', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, 'PVC水刀', NULL, 'PVC水刀', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405044', '库存商品_PVC水刀');
INSERT INTO `t_fa_invoice_b` VALUES ('f4dad815bb194cd9b11291108f2d3353', 'c57a4445d62f414c9bf1c8f17f64b4d0', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '快速接头', NULL, '快速接头', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405008', '库存商品_快速接头');
INSERT INTO `t_fa_invoice_b` VALUES ('f58de458aa3f42fcad5336e23aea6019', '300dfe5ec3fb4d6c919e40cfa0fef9f4', '2019-01', NULL, 115977.19000000, 15996.85000000, 99980.34000000, 100.00000000, '电容成型模具', NULL, '电容成型模具', '1', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 999.80000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, NULL, '进项导入', '1405010', '库存商品_电容成型模具');
INSERT INTO `t_fa_invoice_b` VALUES ('f7972e9e0752458baf4661011dee8be0', 'ea46ac814f944aefad038c2a07079949', '2019-01', NULL, 6553.44000000, 2557.44000000, 3996.00000000, 2.00000000, '铝合金风刀（680L）', NULL, '铝合金风刀（680L）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080310990000000000', '销项发票导入', '1405005', '库存商品_铝合金风刀（680L）');
INSERT INTO `t_fa_invoice_b` VALUES ('fa9ae6e2658444dcb7ea5a6e72ad44c2', '621529a350af4cdb9245397c58ed2628', '2019-01', 0.16, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, '铝合金飞刀（批）', NULL, '铝合金飞刀（批）', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405039', '库存商品_铝合金飞刀（批）');
INSERT INTO `t_fa_invoice_b` VALUES ('ff9a9c768efd4dc7b9cd7558f5b47e93', '9b81ce5325b8467b984e1bf613eb88b8', '2019-01', NULL, 18541.44000000, 2557.44000000, 15984.00000000, 8.00000000, 'PVC水刀', NULL, 'PVC水刀', '2', '2ae1099b794346dd9399d34e00f8b51f', '', '台', 1998.00000000, '', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', NULL, '1080314010500000000', '销项发票导入', '1405044', '库存商品_PVC水刀');

-- ----------------------------
-- Table structure for t_fa_invoice_h
-- ----------------------------
DROP TABLE IF EXISTS `t_fa_invoice_h`;
CREATE TABLE `t_fa_invoice_h`  (
  `invoiceHID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `period` datetime(0) NOT NULL COMMENT '会计期间',
  `invoiceType` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票类型(1:进项发票2:销项发票)',
  `invoiceCode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票编码',
  `invoiceNumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  `invoiceName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票名称',
  `invoiceState` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '正常' COMMENT '发票状态',
  `invoiceDate` datetime(0) NULL DEFAULT NULL COMMENT '发票日期',
  `invoicePerson` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '制票人',
  `buyTaxno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购方税号',
  `buyCorp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购方公司名称',
  `buyBankno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购方账户',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `saleCorp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销方公司名称',
  `saleTaxno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销方税号',
  `saleBankno` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销方账户',
  `vouchID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成凭证主键',
  `auditPsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `fj` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `invoiceDes` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票说明',
  `createPsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `createDate` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `createpsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属账套(外键)',
  `invoice_confirmdate` date NULL DEFAULT NULL COMMENT '确认日期',
  `productVersion` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码版本号',
  `billNo` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `addressPhone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址电话',
  `importDate` bigint(20) NULL DEFAULT NULL,
  `sureType` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '认证方式',
  PRIMARY KEY (`invoiceHID`) USING BTREE,
  INDEX `FK_ID_invoice_h_accountID`(`accountID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fa_invoice_h
-- ----------------------------
INSERT INTO `t_fa_invoice_h` VALUES ('021572e0426a4a1d888aec35c795f7ed', '2019-01-01 00:00:00', '2', '4403172130', '64490968', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471370929, '');
INSERT INTO `t_fa_invoice_h` VALUES ('03c45577319b450b90d2052542f24692', '2019-01-01 00:00:00', '2', '4403172130', '64490967', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471371836, '');
INSERT INTO `t_fa_invoice_h` VALUES ('0673069104034c06a4eda788a64faebb', '2019-01-01 00:00:00', '2', '4403172130', '64490980', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471370411, '');
INSERT INTO `t_fa_invoice_h` VALUES ('07d1e2b5e0b743ecbd901741bfa7300a', '2019-01-01 00:00:00', '1', '3100164143', '3723892496', '增值税专票', '正常', '2017-11-06 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357491, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('0855f41c399a4999a49a3547b55ddc60', '2019-01-01 00:00:00', '2', '4403172130', '64490979', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471370311, '');
INSERT INTO `t_fa_invoice_h` VALUES ('09ef3ac0f02045599cf5b94580d4d080', '2019-01-01 00:00:00', '1', '3100164159', '3723892512', '增值税专票', '正常', '2017-10-26 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357587, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('11b8bd00180a4ab68ffa89ef0b63721c', '2019-01-01 00:00:00', '1', '3100164141', '3723892494', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357472, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('1290c7ef44e6449a8205bec9d114c094', '2019-01-01 00:00:00', '2', '4403172130', '64490981', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471372439, '');
INSERT INTO `t_fa_invoice_h` VALUES ('14d329949b184f37a2a48c9d65d6011c', '2019-01-01 00:00:00', '2', '4403172130', '34280877', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471369978, '');
INSERT INTO `t_fa_invoice_h` VALUES ('1c338e95a71f4ec6a08c25232dd351e6', '2019-01-01 00:00:00', '1', '3100164162', '3723892515', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357605, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('1c8c5d1516cb48c4aca5580232af85a3', '2019-01-01 00:00:00', '1', '3100164137', '3723892490', '增值税专票', '正常', '2017-10-29 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357447, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('1cba3c9abd45430f9c01d7ec962a0628', '2019-01-01 00:00:00', '1', '4403172130', '25996865', '增值税专票', '正常', '2017-11-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357377, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('1d23e9362ec84f8f98a4edd3a9d9a820', '2019-01-01 00:00:00', '2', '4403172130', '34280888', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471372743, '');
INSERT INTO `t_fa_invoice_h` VALUES ('23d61059d1294775b8bffd3374cab194', '2019-01-01 00:00:00', '1', '3100164157', '3723892510', '增值税专票', '正常', '2017-11-23 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357572, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('23de0313f922415b8158dbb779ac16c8', '2019-01-01 00:00:00', '1', '3100164171', '3723892524', '增值税专票', '正常', '2017-10-26 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357646, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('23eda9013070450cb7496384f57ca956', '2019-01-01 00:00:00', '2', '4403172130', '34280888', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471371772, '');
INSERT INTO `t_fa_invoice_h` VALUES ('2966746895f242ee8f30157d2ab043b6', '2019-01-01 00:00:00', '1', '3100164131', '3723892484', '增值税专票', '正常', '2017-10-23 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357410, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('2cf54a144370459998d35575e95bb792', '2019-01-01 00:00:00', '2', '4403172130', '34280877', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471371978, '');
INSERT INTO `t_fa_invoice_h` VALUES ('300dfe5ec3fb4d6c919e40cfa0fef9f4', '2019-01-01 00:00:00', '1', '3100164130', '3723892482', '增值税专票', '正常', '2017-10-21 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357402, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('31924dc9d2ae4a328b5830b0c49f463d', '2019-01-01 00:00:00', '1', '3100164142', '3723892495', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357477, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('37f7427335974f7dbd2fe1a86144751f', '2019-01-01 00:00:00', '2', '4403172130', '34280887', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471369584, '');
INSERT INTO `t_fa_invoice_h` VALUES ('3992fb697f934879946691701d0f85db', '2019-01-01 00:00:00', '1', '3100172130', '3723892145', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357386, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('408bda5f31c14a389a8adca70243c9d2', '2019-01-01 00:00:00', '1', '3100164174', '3723892527', '增值税专票', '正常', '2017-10-29 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357659, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('450ff1c29e334feeaa29f7e471d4e096', '2019-01-01 00:00:00', '1', '4403171130', '3723892480', '增值税专票', '正常', '2017-10-20 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357397, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('4657a69e4f594228af12ae51073a7080', '2019-01-01 00:00:00', '1', '3100164158', '3723892511', '增值税专票', '正常', '2017-10-25 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357577, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('4d68fb1b218a4201858d08645a8906ca', '2019-01-01 00:00:00', '1', '3100164167', '3723892520', '增值税专票', '正常', '2017-10-22 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357629, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('4e36cdf7af5c43159c258712faecbc62', '2019-01-01 00:00:00', '1', '3100164139', '3723892492', '增值税专票', '正常', '2017-12-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357460, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('4fbe21a4821847828db48f2ee4ad8446', '2019-01-01 00:00:00', '1', '3100172130', '372389251', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357382, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('4fde7b5273294826904b8f550001e294', '2019-01-01 00:00:00', '2', '4403172130', '64490979', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471373210, '');
INSERT INTO `t_fa_invoice_h` VALUES ('5a0f8387a79c4d7ba67f4d5278b611d1', '2019-01-01 00:00:00', '2', '4403172130', '34280878', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471371069, '');
INSERT INTO `t_fa_invoice_h` VALUES ('5a6666b1b44d4937b234b0c249982944', '2019-01-01 00:00:00', '1', '3100164169', '3723892522', '增值税专票', '正常', '2017-10-24 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357637, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('5aa1402cdec74679aadcedd8fe027964', '2019-01-01 00:00:00', '2', '4403172130', '64490982', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471371536, '');
INSERT INTO `t_fa_invoice_h` VALUES ('5aabee98a87f4669b09a393543e048a9', '2019-01-01 00:00:00', '2', '4403172130', '64490968', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471369911, '');
INSERT INTO `t_fa_invoice_h` VALUES ('61796ae5ef104d0397f9f54cdb4a8f1e', '2019-01-01 00:00:00', '1', '3100164170', '3723892523', '增值税专票', '正常', '2017-10-25 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357641, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('617a30576e5f4a698976c3b8b93e5443', '2019-01-01 00:00:00', '2', '4403172130', '64490980', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471373304, '');
INSERT INTO `t_fa_invoice_h` VALUES ('621529a350af4cdb9245397c58ed2628', '2019-01-01 00:00:00', '2', '4403172130', '34280878', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471373013, '');
INSERT INTO `t_fa_invoice_h` VALUES ('635afc6a8805486fb2c22de81aa4b604', '2019-01-01 00:00:00', '2', '4403172130', '34280887', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471370628, '');
INSERT INTO `t_fa_invoice_h` VALUES ('6430424ddabb42a4b65b2563c3ac52ee', '2019-01-01 00:00:00', '2', '4403172130', '64490980', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471371395, '');
INSERT INTO `t_fa_invoice_h` VALUES ('678949bdf75f487288ff3239284b8eb1', '2019-01-01 00:00:00', '2', '4403172130', '64490979', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471371278, '');
INSERT INTO `t_fa_invoice_h` VALUES ('690160bf12c24023b87cc79b65b64d3c', '2019-01-01 00:00:00', '1', '3100164133', '3723892486', '增值税专票', '正常', '2017-10-25 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357418, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('6f4f450aad624b4886bb61679c630c5b', '2019-01-01 00:00:00', '1', '3100164145', '3723892498', '增值税专票', '正常', '2017-10-21 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357502, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('72901a2f123147f8b76a8dc946b0b9d9', '2019-01-01 00:00:00', '1', '3100164140', '3723892493', '增值税专票', '正常', '2017-11-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357468, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('7369c7814d6e42fab4ac2a566082b826', '2019-01-01 00:00:00', '1', '3100164160', '3723892513', '增值税专票', '正常', '2017-12-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357591, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('758dfb5f6e72456ab25a91cb74df35a1', '2019-01-01 00:00:00', '2', '4403172130', '34280887', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471371603, '');
INSERT INTO `t_fa_invoice_h` VALUES ('78944129831d43f7925cf359cd08d296', '2019-01-01 00:00:00', '1', '3100164154', '3723892507', '增值税专票', '正常', '2017-10-30 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357554, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('79304c33837e4836b08e200660a3fd5f', '2019-01-01 00:00:00', '2', '4403172130', '34280877', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471370994, '');
INSERT INTO `t_fa_invoice_h` VALUES ('7b07abb40f0c4853b02702f74ab2c5a2', '2019-01-01 00:00:00', '2', '4403172130', '64490981', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471371470, '');
INSERT INTO `t_fa_invoice_h` VALUES ('82c9b1f5462244abae51bedb7ae51d10', '2019-01-01 00:00:00', '1', '3100164153', '3723892506', '增值税专票', '正常', '2017-10-29 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357545, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('8484722d65a046fda1decb9eb87e13de', '2019-01-01 00:00:00', '1', '3100164150', '3723892503', '增值税专票', '正常', '2017-10-26 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357529, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('86696d50d0db419c88dbe4eb0e570dfe', '2019-01-01 00:00:00', '1', '3100164161', '3723892514', '增值税专票', '正常', '2017-11-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357599, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('899a0eca49854b4eb2074e1c3197a18d', '2019-01-01 00:00:00', '1', '4403172130', '25996866', '增值税专票', '正常', '2017-11-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357098, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('8b6290320ed144cfb04f6af4c925bfcf', '2018-09-01 00:00:00', '2', '1', '1122151', '销项发票名称', '正常', '2019-04-04 14:08:19', '管理员', '', '  东莞市凯利达超声波设备有限公司', '0', '0', '', '', '', 'a44e4024e4c34432a585d61deb507047', '', '', '', '9860d90ca3604596bafa5e6b8027a7d2', NULL, '管理员', '8b292128ea13438d87ff5c1c4502b2ce', NULL, '1000', '0', '0', 1553774982565, '');
INSERT INTO `t_fa_invoice_h` VALUES ('8cf1e8649e8c4a42922b31ea28d5bff8', '2019-01-01 00:00:00', '1', '3100164144', '3723892497', '增值税专票', '正常', '2017-10-20 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357496, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('8fcaf9f489c84966a03494cd6c62d5f7', '2019-01-01 00:00:00', '2', '4403172130', '64490967', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471369836, '');
INSERT INTO `t_fa_invoice_h` VALUES ('93dc0e4e69b44c019e28d32a6c86ef75', '2019-01-01 00:00:00', '1', '3100164134', '3723892487', '增值税专票', '正常', '2017-10-26 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357422, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('94135131559d4c4e83a9c2deec50169e', '2019-01-01 00:00:00', '2', '4403172130', '34280878', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471372044, '');
INSERT INTO `t_fa_invoice_h` VALUES ('95103389e055430d9e3214919fa84bc6', '2019-01-01 00:00:00', '1', '3100164168', '3723892521', '增值税专票', '正常', '2017-10-23 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357633, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('98455d3fa1304190ad0795df0f650869', '2019-01-01 00:00:00', '1', '3100164149', '3723892502', '增值税专票', '正常', '2017-10-25 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357523, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('995468660c67402d9cf952d12dbabe7e', '2019-01-01 00:00:00', '1', '3100164156', '3723892509', '增值税专票', '正常', '2017-11-23 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357568, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('9b81ce5325b8467b984e1bf613eb88b8', '2019-01-01 00:00:00', '2', '4403172130', '64490968', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471371903, '');
INSERT INTO `t_fa_invoice_h` VALUES ('a277587c062043f580a79c62df3085f8', '2019-01-01 00:00:00', '1', '4403172130', '25996895', '增值税专票', '正常', '2017-12-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357364, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('a68efae26bfc4ebba5d492e9aceebd76', '2019-01-01 00:00:00', '1', '3100164152', '3723892505', '增值税专票', '正常', '2017-10-28 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357539, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('a8b20c973c8b4279b97c092f14469dd6', '2019-01-01 00:00:00', '2', '4403172130', '64490982', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471372503, '');
INSERT INTO `t_fa_invoice_h` VALUES ('ae3b218aecfb4118b3e2c1e650c83b8b', '2019-01-01 00:00:00', '1', '3100164155', '3723892508', '增值税专票', '正常', '2017-10-31 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357561, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('ae88623666484b1f9c03933bdf2d0f9c', '2019-01-01 00:00:00', '1', '3100164136', '3723892489', '增值税专票', '正常', '2017-10-28 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357441, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('af135a9638604ff3a5227c468c029f9b', '2019-01-01 00:00:00', '1', '3100164163', '3723892516', '增值税专票', '正常', '2017-11-14 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357610, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('afef89dc2cb648c5832be3d44dc71816', '2019-01-01 00:00:00', '2', '4403172130', '64490981', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471370478, '');
INSERT INTO `t_fa_invoice_h` VALUES ('b65ff1d595f5473e93abe9b287612285', '2019-01-01 00:00:00', '1', '3100164151', '3723892504', '增值税专票', '正常', '2017-10-27 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357535, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('ba9e5b25458b49e5b9809ba858e2de05', '2019-01-01 00:00:00', '2', '4403172130', '64490982', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471370553, '');
INSERT INTO `t_fa_invoice_h` VALUES ('bd6211742dc84106acfa3eb3e646928b', '2019-01-01 00:00:00', '1', '3100164132', '3723892485', '增值税专票', '正常', '2017-10-24 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357414, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('bf9d295694724fe4bc362b79329d360b', '2019-01-01 00:00:00', '1', '3100164148', '3723892501', '增值税专票', '正常', '2017-10-24 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357518, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('c57a4445d62f414c9bf1c8f17f64b4d0', '2019-01-01 00:00:00', '1', '4403171130', '3723892479', '增值税专票', '正常', '2017-11-06 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357390, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('c882bc06299e42e4af0762cb18798684', '2019-01-01 00:00:00', '2', '4403172130', '64490981', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471373371, '');
INSERT INTO `t_fa_invoice_h` VALUES ('ca6264a29d234ec9b1ace2cf5b72ddff', '2019-01-01 00:00:00', '1', '3100164138', '3723892491', '增值税专票', '正常', '2017-10-30 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357455, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('ca68d947722a4287898bd073e473a396', '2019-01-01 00:00:00', '2', '4403172130', '34280888', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471370794, '');
INSERT INTO `t_fa_invoice_h` VALUES ('cb4b42f568aa4868965c683a8cd44d95', '2019-01-01 00:00:00', '2', '4403172130', '64490979', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471372264, '');
INSERT INTO `t_fa_invoice_h` VALUES ('d3d7b9779ea84b92996eb0e6084c4b6d', '2019-01-01 00:00:00', '1', '3100164135', '3723892488', '增值税专票', '正常', '2017-10-27 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357428, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('d4d574fd8cbe40a294337cc543bba3a5', '2019-01-01 00:00:00', '2', '4403172130', '64490968', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471372877, '');
INSERT INTO `t_fa_invoice_h` VALUES ('d50b1e744822476a8a0409fe0f246225', '2019-01-01 00:00:00', '2', '4403172130', '34280878', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471370084, '');
INSERT INTO `t_fa_invoice_h` VALUES ('def3aac716af45e283eb0545859233e3', '2019-01-01 00:00:00', '1', '3100164146', '3723892499', '增值税专票', '正常', '2017-10-22 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357507, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('e39084ce18924519abaf70d3e1c9ea36', '2019-01-01 00:00:00', '1', '4403172130', '25996894', '增值税专票', '正常', '2017-12-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357360, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('e3d5871ec0184b86bd8782f011397539', '2019-01-01 00:00:00', '1', '4403172130', '25996896', '增值税专票', '正常', '2017-12-08 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357152, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('e76f34fcba2b476a87516858d6fea478', '2019-01-01 00:00:00', '2', '4403172130', '64490980', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471372369, '');
INSERT INTO `t_fa_invoice_h` VALUES ('ea46ac814f944aefad038c2a07079949', '2019-01-01 00:00:00', '2', '4403172130', '34280888', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471369769, '');
INSERT INTO `t_fa_invoice_h` VALUES ('ea578b2e5c614a9db0d72520e1f09b5a', '2019-01-01 00:00:00', '1', '3100164172', '3723892525', '增值税专票', '正常', '2017-10-27 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357651, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('ee6ab694fe5b40d1b05ef83215e99d76', '2019-01-01 00:00:00', '1', '3100164165', '3723892518', '增值税专票', '正常', '2017-10-20 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357619, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('eefdc6d95c4d49008546407e9a9db93c', '2019-01-01 00:00:00', '2', '4403172130', '64490982', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91120110668830354P', '天津市未来申星自动化设备有限公司', '中国工商银行天津市先锋路支行 0302042109300199534', '东丽区新立街道窖上村 022-84390380', '', '', '', 'a9a1a2f96b7647b8bdbb5971c1788f7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东丽区新立街道窖上村 022-84390380', 1552471373451, '');
INSERT INTO `t_fa_invoice_h` VALUES ('f0b5fe6038234392aab532dc9158178e', '2019-01-01 00:00:00', '1', '3100164164', '3723892517', '增值税专票', '正常', '2017-11-06 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市勤创光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357615, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('f0f975ab6c344adc8c1b56e37ed2fdf8', '2019-01-01 00:00:00', '1', '3100164173', '3723892526', '增值税专票', '正常', '2017-10-28 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357654, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('f55e2266c9db4647a81789f490addd3b', '2019-01-01 00:00:00', '1', '3100164166', '3723892519', '增值税专票', '正常', '2017-10-21 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海欧切斯实业有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357623, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('f66348a698d14925b7f83b043246874c', '2019-01-01 00:00:00', '1', '3100164130', '3723892483', '增值税专票', '正常', '2017-10-22 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '上海加布光电科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357406, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('f7c61838538d4cbfaaa037041cda6a9e', '2019-01-01 00:00:00', '1', '3100164147', '3723892500', '增值税专票', '正常', '2017-10-23 00:00:00', '', '91440300565729982N', NULL, NULL, NULL, '深圳市山普智能科技有限公司', '', '', '', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, 1552471357512, NULL);
INSERT INTO `t_fa_invoice_h` VALUES ('f92796e685c44f91a4d486ce61b19ff2', '2019-01-01 00:00:00', '2', '4403172130', '64490967', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471372810, '');
INSERT INTO `t_fa_invoice_h` VALUES ('f9294d8f39b34502927bf154928eabbb', '2019-01-01 00:00:00', '2', '4403172130', '64490967', '销项发票名称', '正常', '2018-02-07 00:00:00', '不一样的烟火', '91330402570577602T', '嘉兴航林机电设备有限公司', '中国工商银行股份有限公司嘉兴兴禾支行  1204062009888010334', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', '', '', '', 'dcaf2c117cd343b58536f0f6f1a0921c', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '嘉兴市南湖区新丰镇工业园区乍王公路北侧1号楼1楼  057383024808', 1552471370861, '');
INSERT INTO `t_fa_invoice_h` VALUES ('fbb8f25a91a04256aa6504af0c12472e', '2019-01-01 00:00:00', '2', '4403172130', '34280877', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471372952, '');
INSERT INTO `t_fa_invoice_h` VALUES ('fd9bc1f2644e4fefbcf462a1296996e6', '2019-01-01 00:00:00', '2', '4403172130', '34280887', '销项发票名称', '正常', '2018-02-02 00:00:00', '不一样的烟火', '92441900L60224113P', '东莞市石碣捷瑞特机械设备厂', '东莞农村商业银行股份有限公司四甲分理处 290030190010004717', '东莞市石碣镇四甲西河工业区 0769-86367000', '', '', '', '12bd219b3ad6408da243560d145ecc7e', '', '', '', '223d3c4b909c4df2aa45c8a6af397563', NULL, '不一样的烟火', '2ae1099b794346dd9399d34e00f8b51f', NULL, '19.0', '', '东莞市石碣镇四甲西河工业区 0769-86367000', 1552471372578, '');

-- ----------------------------
-- Table structure for t_fa_invoice_mappingrecord
-- ----------------------------
DROP TABLE IF EXISTS `t_fa_invoice_mappingrecord`;
CREATE TABLE `t_fa_invoice_mappingrecord`  (
  `mr_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套ID',
  `period` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `invoiceType` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1进项2销项',
  `is_upload_save` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导入是否保存1保存0未保存',
  `save_num` smallint(5) NULL DEFAULT NULL COMMENT '保持累次次数',
  `saveDate` datetime(0) NULL DEFAULT NULL COMMENT '保存时间',
  PRIMARY KEY (`mr_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票映射科目记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fa_invoice_mappingrecord
-- ----------------------------
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('0b7c4244445f413383eefafb3406892c', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '1', '1', 1, '2019-03-13 18:02:38');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('1b15d84001d34013a633eb3351fdc142', '239a1e6ff1104c0db8d96e6c36c75e64', '2019-03', '1', '0', 0, '2019-03-14 14:10:48');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('44d6db8c6d544a2aa635e4c99c190802', '8b292128ea13438d87ff5c1c4502b2ce', '2018-08', '1', '1', 2, '2018-11-30 11:51:50');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('65921b604f21428782affc63c704029b', '239a1e6ff1104c0db8d96e6c36c75e64', '2019-01', '2', '1', 1, '2019-03-14 17:55:53');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('ce9d5e9df4fb471fafb83825bc908f67', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '2', '1', 1, '2019-03-13 18:02:54');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('d74b035dbe51400794b4c14aaf206c3d', '239a1e6ff1104c0db8d96e6c36c75e64', '2019-01', '1', '1', 1, '2019-03-14 16:51:26');
INSERT INTO `t_fa_invoice_mappingrecord` VALUES ('ff6c9498bb5d4541b87c26807bc391a7', '8b292128ea13438d87ff5c1c4502b2ce', '2018-08', '2', '1', 1, '2018-11-30 11:54:27');

-- ----------------------------
-- Table structure for t_kc_commodity
-- ----------------------------
DROP TABLE IF EXISTS `t_kc_commodity`;
CREATE TABLE `t_kc_commodity`  (
  `comID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `sub_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编码',
  `importSubcode` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导入的科目编码',
  `sub_comName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `comNameSpec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称规格',
  `comName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `balance_direction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额借贷方向',
  `period` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `qc_balanceNum` double(20, 8) NULL DEFAULT NULL COMMENT '期初结存数量',
  `qc_balancePrice` decimal(20, 8) NULL DEFAULT NULL COMMENT '期初结存单价',
  `qc_balanceAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '期初结存金额',
  `bq_incomeNum` double(20, 8) NULL DEFAULT NULL COMMENT '本期收入数量',
  `bq_incomeAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '本期收入金额',
  `bq_incomePrice` decimal(20, 8) NULL DEFAULT NULL COMMENT '本期收入价格',
  `bq_issueNum` double(20, 8) NULL DEFAULT NULL COMMENT '本期发出数量',
  `bq_issueAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '本期发出金额',
  `bq_issuePrice` decimal(20, 8) NULL DEFAULT NULL COMMENT '本期发出价格',
  `total_incomeNum` double(20, 8) NULL DEFAULT NULL COMMENT '本年累计收入数量',
  `total_incomeAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '本年累计收入金额',
  `total_issueNum` double(20, 8) NULL DEFAULT NULL COMMENT '本年累计发出数量',
  `total_issueAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '本年累计发出金额',
  `qm_balanceNum` double(20, 8) NULL DEFAULT NULL COMMENT '期末结存数量',
  `qm_balancePrice` decimal(20, 8) NULL DEFAULT NULL COMMENT '期末结存单价',
  `qm_balanceAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '期末结存金额',
  `direction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账套ID',
  `startPeriod` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '起始期间(第一次导入期间)',
  `endPeriod` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '截止期间(第一次导入期间)',
  `updatePsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `updatePsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updatedate` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `createPsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `createDate` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `createPsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明备注',
  `importDate` datetime(0) NULL DEFAULT NULL COMMENT '库存商品导入时间',
  `balanceDate` datetime(0) NULL DEFAULT NULL COMMENT '结存时间',
  `vcunit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  PRIMARY KEY (`comID`) USING BTREE,
  INDEX `kc_comm_index_accountID`(`accountID`) USING BTREE,
  INDEX `kc_comm_index_period`(`period`) USING BTREE,
  INDEX `kc_comm_index_sub_code`(`sub_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_kc_commodity
-- ----------------------------
INSERT INTO `t_kc_commodity` VALUES ('d217b9c6d741f6cbdf0927ed8eb6e086', '1405001', NULL, '库存商品_打印机', '打印机', '打印机', NULL, NULL, '2019-01', NULL, NULL, NULL, 1.00000000, 1000.00000000, 0.00000000, NULL, NULL, NULL, 1.00000000, 1000.00000000, NULL, NULL, 1.00000000, 1000.00000000, 1000.00000000, NULL, '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, NULL, NULL, '2019-04-04 16:56:44', NULL, '2019-04-04 16:56:44', NULL, '打印机凭证补录新增', '2019-04-04 16:56:44', NULL, NULL);

-- ----------------------------
-- Table structure for t_simple_provision_item
-- ----------------------------
DROP TABLE IF EXISTS `t_simple_provision_item`;
CREATE TABLE `t_simple_provision_item`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parent_id` int(10) NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '启用状态(0禁用，1启用）',
  `has_amount_type` tinyint(4) NULL DEFAULT 0 COMMENT '涉及费用是否有现金或银行分类 0没有 1有',
  `has_children` tinyint(4) NULL DEFAULT 0 COMMENT '是否有子节点 0没有 1有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50005 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计提凭证项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_simple_provision_item
-- ----------------------------
INSERT INTO `t_simple_provision_item` VALUES (50000, 0, '计提工资', 1, 0, 0);
INSERT INTO `t_simple_provision_item` VALUES (50002, 0, '发放工资', 1, 1, 0);
INSERT INTO `t_simple_provision_item` VALUES (50003, 50002, '现金', 1, 0, 0);
INSERT INTO `t_simple_provision_item` VALUES (50004, 50002, '银行(公账)', 1, 0, 0);

-- ----------------------------
-- Table structure for t_simple_subject
-- ----------------------------
DROP TABLE IF EXISTS `t_simple_subject`;
CREATE TABLE `t_simple_subject`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编码',
  `parent_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '父级编码  (1级为0，二级取前4位）',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目全民',
  `category` tinyint(4) NULL DEFAULT NULL COMMENT '类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)',
  `direction` tinyint(4) NULL DEFAULT 1 COMMENT '借贷方向（1.debit借方， 2.credit贷方）',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT ' 启用状态(0禁用，1启用）',
  `extend` tinyint(4) NULL DEFAULT 0 COMMENT ' 可扩展(0不能，1能）银行存款、其他应收款等需要带对方信息',
  `extend_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展标示：bank 银行号码类，name 名称类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记账科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_simple_subject
-- ----------------------------
INSERT INTO `t_simple_subject` VALUES (1, '1001', '0', '库存现金', '库存现金', 1, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (2, '1002', '0', '银行存款', '银行存款', 1, 1, 1, 1, 'bank');
INSERT INTO `t_simple_subject` VALUES (3, '1405', '0', '库存商品', '库存商品', 1, 1, 1, 1, 'name');
INSERT INTO `t_simple_subject` VALUES (4, '2211', '0', '应付职工薪酬', '应付职工薪酬', 2, 2, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (5, '2211001', '2211', '工资', '应付职工薪酬_工资', 2, 2, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (6, '2221', '0', '应交税费', '应交税费', 2, 2, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (7, '2221002', '2221', '未交增值税', '应交税费_未交增值税', 2, 2, 1, 0, '');
INSERT INTO `t_simple_subject` VALUES (8, '6001', '0', '主营业务收入', '主营业务收入', 6, 2, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (9, '6602', '0', '管理费用', '管理费用', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (10, '6602001', '6602', '办公用品', '管理费用_办公用品', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (11, '6602002', '6602', '房租', '管理费用_房租', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (12, '6602003', '6602', '物业管理费', '管理费用_物业管理费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (13, '6602004', '6602', '水电费', '管理费用_水电费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (14, '6602005', '6602', '交际应酬费', '管理费用_交际应酬费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (15, '6602006', '6602', '市内交通费', '管理费用_市内交通费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (16, '6602008', '6602', '差旅费', '管理费用_差旅费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (17, '6602012', '6602', '工资', '管理费用_工资', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (18, '6602016', '6602', '福利费', '管理费用_福利费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (19, '6602017', '6602', '咨询费', '管理费用_咨询费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (20, '6602018', '6602', '运输费', '管理费用_运输费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (21, '6602021', '6602', '通讯费', '管理费用_通讯费', 6, 1, 1, 0, NULL);
INSERT INTO `t_simple_subject` VALUES (26, '1122', '1122', '应收账款', '应收账款', 1, 1, 1, 1, 'name');
INSERT INTO `t_simple_subject` VALUES (27, '2202', '2202', '应付账款', '应付账款', 2, 1, 1, 1, 'name');
INSERT INTO `t_simple_subject` VALUES (28, '1221', '1221', '其他应收款', '其他应收款', 1, 1, 1, 1, 'name');
INSERT INTO `t_simple_subject` VALUES (29, '2241', '2241', '其他应付款', '其他应付款', 2, 1, 1, 1, 'name');

-- ----------------------------
-- Table structure for t_simple_voucher_item
-- ----------------------------
DROP TABLE IF EXISTS `t_simple_voucher_item`;
CREATE TABLE `t_simple_voucher_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '启用状态(0禁用，1启用）',
  `has_amount_type` tinyint(4) NULL DEFAULT 0 COMMENT '涉及费用是否有现金或银行分类 0没有 1有',
  `has_children` tinyint(4) NULL DEFAULT 0 COMMENT '是否有子节点 0没有 1有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1501 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生成凭证项目表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_simple_voucher_item
-- ----------------------------
INSERT INTO `t_simple_voucher_item` VALUES (1, '新增收入', 0, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (4, '新增支出', 0, 1, 0, 1);
INSERT INTO `t_simple_voucher_item` VALUES (5, '采购', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (7, '办公用品', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (8, '房租', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (9, '物业管理费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (10, '水电费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (11, '交际应酬费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (12, '市内交通费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (13, '差旅费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (14, '工资', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (15, '福利费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (16, '咨询费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (17, '运输费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (18, '通讯费', 4, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (19, '新增工资', 0, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (20, '新增往来', 0, 1, 0, 1);
INSERT INTO `t_simple_voucher_item` VALUES (21, '应收账款', 20, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (22, '应付账款', 20, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (23, '借出款项', 20, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (25, '借款收回', 20, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (27, '股东借款', 20, 1, 1, 0);
INSERT INTO `t_simple_voucher_item` VALUES (28, '取银行备用金', 20, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1000, '现金', 1, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1010, '银行(公账)', 1, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1020, '现金', 5, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1030, '银行(公账)', 5, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1040, '现金', 7, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1050, '银行(公账)', 7, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1060, '现金', 8, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1070, '银行(公账)', 8, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1080, '现金', 9, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1090, '银行(公账)', 9, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1100, '现金', 10, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1110, '银行(公账)', 10, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1120, '现金', 11, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1130, '银行(公账)', 11, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1140, '现金', 12, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1150, '银行(公账)', 12, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1160, '现金', 13, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1170, '银行(公账)', 13, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1200, '现金', 15, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1210, '银行(公账)', 15, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1220, '现金', 16, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1230, '银行(公账)', 16, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1240, '现金', 17, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1250, '银行(公账)', 17, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1260, '现金', 18, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1270, '银行(公账)', 18, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1310, '现金', 25, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1320, '银行(公账)', 25, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1330, '现金', 23, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1340, '银行(公账)', 23, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1440, '现金', 27, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1450, '银行(公账)', 27, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1490, '现金', 14, 1, 0, 0);
INSERT INTO `t_simple_voucher_item` VALUES (1500, '银行(公账)', 14, 1, 0, 0);

-- ----------------------------
-- Table structure for t_simple_voucher_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_simple_voucher_rule`;
CREATE TABLE `t_simple_voucher_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '科目id',
  `subject_deriction` tinyint(4) NULL DEFAULT NULL COMMENT '科目借贷方向 1借 2贷',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '0 禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目与科目关系规则表  生成凭证用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_simple_voucher_rule
-- ----------------------------
INSERT INTO `t_simple_voucher_rule` VALUES (1, 1000, 1, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (2, 1000, 8, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (3, 1000, 7, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (4, 1010, 2, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (5, 1010, 8, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (6, 1010, 7, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (7, 1020, 3, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (8, 1020, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (9, 1030, 3, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (10, 1030, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (11, 1040, 10, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (12, 1040, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (13, 1050, 10, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (14, 1050, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (15, 1060, 11, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (16, 1060, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (17, 1070, 11, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (18, 1070, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (19, 1080, 12, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (20, 1080, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (21, 1090, 12, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (22, 1090, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (23, 1100, 13, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (24, 1100, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (25, 1110, 13, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (26, 1110, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (27, 1120, 14, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (28, 1120, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (29, 1130, 14, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (30, 1130, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (31, 1140, 15, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (32, 1140, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (33, 1150, 15, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (34, 1150, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (35, 1160, 16, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (36, 1160, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (37, 1170, 16, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (38, 1170, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (43, 1200, 18, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (44, 1200, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (45, 1210, 18, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (46, 1210, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (47, 1220, 19, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (48, 1220, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (49, 1230, 19, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (50, 1230, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (51, 1240, 20, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (52, 1240, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (53, 1250, 20, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (54, 1250, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (55, 1260, 21, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (56, 1260, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (57, 1270, 21, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (58, 1270, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (60, 21, 26, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (61, 21, 8, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (62, 21, 7, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (63, 22, 3, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (64, 22, 27, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (65, 1310, 1, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (66, 1310, 28, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (67, 1320, 2, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (68, 1320, 28, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (69, 1330, 28, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (70, 1330, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (71, 1340, 28, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (72, 1340, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (73, 137, 17, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (74, 137, 5, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (75, 1440, 1, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (76, 1440, 29, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (77, 1450, 2, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (78, 1450, 29, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (79, 50000, 17, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (80, 50000, 5, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (81, 50003, 5, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (82, 50003, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (83, 50004, 5, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (84, 50004, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (85, 1490, 17, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (86, 1490, 1, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (87, 1500, 17, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (88, 1500, 2, 2, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (89, 28, 1, 1, 1);
INSERT INTO `t_simple_voucher_rule` VALUES (90, 28, 2, 2, 1);

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `userID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键（同租户ID）',
  `loginUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户名(手机号)',
  `userName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `userType` int(11) NOT NULL COMMENT '用户类型标识（1:后台超级管理员2:后台普通管理员(B端代账)3:C端管理员记账4:内部用户5:B端员工用户6:C端员工用户）',
  `type` int(2) NULL DEFAULT 0 COMMENT '0:正式用户 1：试用用户',
  `parentUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级ID（由谁添加的信息（最近一层父级））',
  `pasword` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码(des加密存储)',
  `state` int(11) NOT NULL COMMENT '账号状态（0:新建1:启用、2:禁用）',
  `accountNum` int(10) NULL DEFAULT NULL COMMENT '账套数',
  `companyType` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司类型 1小规模 2一般纳税人',
  `ableDate` datetime(0) NOT NULL COMMENT '生效日期',
  `disableDate` datetime(0) NOT NULL COMMENT '失效日期',
  `des` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `updateDate` datetime(0) NOT NULL COMMENT '修改日期',
  `updatePsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `createDate` datetime(0) NOT NULL COMMENT '创建日期',
  `createPsn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `jod` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `gender` int(11) NULL DEFAULT NULL COMMENT '性别(1:男2:女)',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `realname` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `address` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `telphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `postCode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `corp` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属公司名称',
  `id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '平台用户ID',
  `IDcard` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `power` int(4) NULL DEFAULT 0 COMMENT '权限分数    99  :平台管理员',
  `sessionID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sessionID ',
  `initPassword` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '初始密码 随机生成',
  PRIMARY KEY (`userID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES ('223d3c4b909c4df2aa45c8a6af397563', '18684844593', '不一样的烟火', 2, 0, '0', 'E10ADC3949BA59ABBE56E057F20F883E', 1, NULL, NULL, '2019-03-12 14:09:21', '2019-03-12 14:09:23', NULL, '2019-03-12 14:09:28', '223d3c4b909c4df2aa45c8a6af397563', '2019-03-12 14:09:34', '223d3c4b909c4df2aa45c8a6af397563', NULL, '2019-03-12', NULL, '18684844593', NULL, NULL, NULL, NULL, '441653192@qq.com', NULL, '223d3c4b-909c-4df2-aa45-c8a6af397563', NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for t_vouch_b
-- ----------------------------
DROP TABLE IF EXISTS `t_vouch_b`;
CREATE TABLE `t_vouch_b`  (
  `vouchAID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `rowIndex` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分录号',
  `subjectID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编码',
  `debitAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '借方金额',
  `creditAmount` decimal(20, 8) NULL DEFAULT NULL COMMENT '贷方金额',
  `vouchID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '凭证主表主键',
  `vcsubject` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `number` decimal(20, 8) NULL DEFAULT NULL COMMENT '数量',
  `isproblem` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1有问题2没问题',
  `des` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `direction` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方向(1:借2:贷)',
  `vcabstact` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `period` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账套ID',
  `vcunit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `vcunitID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计量单位ID',
  `updatePsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `updatePsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updatedate` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `price` decimal(20, 8) NULL DEFAULT NULL COMMENT '单价(国际单位)',
  PRIMARY KEY (`vouchAID`) USING BTREE,
  INDEX `FK_ID_t_vouch_b_accountID`(`accountID`) USING BTREE,
  INDEX `vouch_b_index_period`(`period`) USING BTREE,
  INDEX `vouch_b_index_vouchID`(`vouchID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '凭证子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_vouch_b
-- ----------------------------
INSERT INTO `t_vouch_b` VALUES ('15ec19050dd39ddc171b43a478bf8e1b', '1', '1405001', 1000.00000000, 0.00000000, 'a8d7c96f8add4ed0b104d9c80dc2d9e4', '库存商品_打印机', 1.00000000, '2', NULL, '1', '现金购商品', '2019-01', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-04-04 16:56:44', NULL);
INSERT INTO `t_vouch_b` VALUES ('4317bcd22e79ed49af66e5a26dbee63d', '2', '1001', 0.00000000, 1000.00000000, 'a8d7c96f8add4ed0b104d9c80dc2d9e4', '库存现金', 0.00000000, '2', NULL, '2', NULL, '2019-01', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-04-04 16:56:44', NULL);
INSERT INTO `t_vouch_b` VALUES ('8dbbf934a9a9d51742c169ef1e11c604', '1', '1001', 1000.00000000, 0.00000000, 'a44e4024e4c34432a585d61deb507047', '库存现金', 0.00000000, '2', NULL, '1', '现金收入', '2019-01', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-04-04 16:09:35', NULL);
INSERT INTO `t_vouch_b` VALUES ('8f36cae3786bbbb3802c1b7ac2722d58', '3', '2221002', 0.00000000, 17.00000000, 'a44e4024e4c34432a585d61deb507047', '应交税费_未交增值税', 0.00000000, '2', NULL, '2', NULL, '2019-01', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-04-04 16:09:35', NULL);
INSERT INTO `t_vouch_b` VALUES ('cca6664f8800dde10f3bfe575030f53d', '2', '6001', 0.00000000, 983.00000000, 'a44e4024e4c34432a585d61deb507047', '主营业务收入', 0.00000000, '2', NULL, '2', NULL, '2019-01', '2ae1099b794346dd9399d34e00f8b51f', NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', '2019-04-04 16:09:35', NULL);

-- ----------------------------
-- Table structure for t_vouch_h
-- ----------------------------
DROP TABLE IF EXISTS `t_vouch_h`;
CREATE TABLE `t_vouch_h`  (
  `vouchID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `voucherNo` int(11) NOT NULL COMMENT '凭证号',
  `totalCredit` decimal(20, 8) NULL DEFAULT NULL COMMENT '凭证贷方金额合计',
  `totalDbit` decimal(20, 8) NULL DEFAULT NULL COMMENT '凭证借方金额合计',
  `isproblem` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1有问题2没问题',
  `source` int(11) NULL DEFAULT NULL COMMENT '来源0:进项凭证1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据 7.结转成本 9销项凭证 10结转全年净利润 11导入序时薄凭证 13结转增值税 131结转留底税 14 结转附赠税 15结转企业所得税',
  `des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套ID',
  `period` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期间',
  `vcDate` datetime(0) NULL DEFAULT NULL COMMENT '生成日期',
  `updatePsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `updatePsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updatedate` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `createPsnID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `createpsn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `createDate` bigint(20) NULL DEFAULT NULL COMMENT '创建日期',
  `checkedDate` datetime(0) NULL DEFAULT NULL COMMENT '审核日期',
  `checker` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `auditStatus` int(11) NULL DEFAULT 0 COMMENT '审核状态(0:未审核1:审核)',
  `currency` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币别',
  `currencyID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币别ID',
  `vouchFlag` int(11) NULL DEFAULT NULL COMMENT '0:非模凭证1:模板凭证',
  `attachID` varchar(329) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件ID',
  `voucherType` smallint(4) NULL DEFAULT NULL COMMENT '凭证类型2 标记手工添加的结转成本',
  PRIMARY KEY (`vouchID`) USING BTREE,
  INDEX `FK_ID_t_vouch_h_accountID`(`accountID`) USING BTREE,
  INDEX `vouch_h_index_period`(`period`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '凭证主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_vouch_h
-- ----------------------------
INSERT INTO `t_vouch_h` VALUES ('a44e4024e4c34432a585d61deb507047', 1, 1000.00000000, 1000.00000000, '2', 5, '采购打印机', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '2019-04-04 16:09:35', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', 1554365375461, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
INSERT INTO `t_vouch_h` VALUES ('a8d7c96f8add4ed0b104d9c80dc2d9e4', 2, 1000.00000000, 1000.00000000, '2', 5, '采购打印机', '2ae1099b794346dd9399d34e00f8b51f', '2019-01', '2019-04-04 16:56:44', NULL, NULL, NULL, '223d3c4b909c4df2aa45c8a6af397563', '不一样的烟火', 1554368203616, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_wa_arch
-- ----------------------------
DROP TABLE IF EXISTS `t_wa_arch`;
CREATE TABLE `t_wa_arch`  (
  `archID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `acperiod` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '期间（做账日期）',
  `archDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工资发放月份',
  `accountID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账套ID',
  `acDepartment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `acCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员编码',
  `acName` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员姓名',
  `basePay` double(10, 2) NULL DEFAULT NULL COMMENT '基本工资',
  `subsidy` double(10, 2) NULL DEFAULT NULL COMMENT '岗位津贴',
  `overtimeFree` double(10, 2) NULL DEFAULT NULL COMMENT '平时加班',
  `overtimeWeekend` double(10, 2) NULL DEFAULT NULL COMMENT '周末加班',
  `otherFree` double(10, 2) NULL DEFAULT NULL COMMENT '其他补贴',
  `payAble` double(10, 2) NULL DEFAULT NULL COMMENT '应发工资',
  `socialfree` double(10, 2) NULL DEFAULT NULL COMMENT '扣社保',
  `taxFree` double(10, 2) NULL DEFAULT NULL COMMENT '个税',
  `totalCharged` double(10, 2) NULL DEFAULT NULL COMMENT '扣款合计',
  `provident` double(10, 2) NULL DEFAULT NULL COMMENT '扣公积金',
  `utilities` double(10, 2) NULL DEFAULT NULL COMMENT '扣水电费',
  `deduction` double(10, 2) NULL DEFAULT NULL COMMENT '其它扣款',
  `attendanceDays` double(10, 1) NULL DEFAULT NULL COMMENT '考勤天数',
  `attendanceActual` double(10, 1) NULL DEFAULT NULL COMMENT '实际出勤',
  `realwages` double(10, 2) NULL DEFAULT NULL COMMENT '实发工资',
  `importDate` datetime(0) NULL DEFAULT NULL COMMENT '导入时间',
  `createpsnID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人ID',
  PRIMARY KEY (`archID`) USING BTREE,
  INDEX `FK_ID_t_arch_accountID`(`accountID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人员薪资档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_wa_arch
-- ----------------------------
INSERT INTO `t_wa_arch` VALUES ('02a53d82498c497183a4624333aed0d1', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1013', '黄梦云', 3470.00, 0.00, 0.00, 433.33, 0.00, 3903.33, 98.00, 193.00, 386.00, 70.00, 20.00, 5.00, 22.0, 22.0, 3517.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('14102cd072d64438bfcac0446ed02577', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1002', '刘春花', 3000.00, 0.00, 0.00, 500.00, 0.00, 3500.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3500.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('1f5b5ea2392840e4a2e033cf2fdfde34', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1011', '刘月', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3933.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('4ad02788f63f4d90a36fee3c98446b61', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1015', '陈亮', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 101.00, 198.00, 396.00, 72.00, 20.00, 5.00, 22.0, 22.0, 3537.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('4bb197864f5046bc98c2f589c1220598', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1008', '李丽', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 100.00, 191.00, 382.00, 66.00, 20.00, 5.00, 22.0, 22.0, 3551.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('506f56cab85d408f9aa868478b913e1b', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1006', '严贡延', 3000.00, 0.00, 0.00, 300.00, 0.00, 3300.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3300.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('5318d77fe1284509bb96553b2eba5b23', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1001', '郭斌根', 3200.00, 0.00, 0.00, 300.00, 0.00, 3500.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3500.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('5605c628b4eb4d83a94c86e89ce94124', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '销售部门', '1018', '刘宝宝', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 104.00, 204.00, 408.00, 75.00, 20.00, 5.00, 22.0, 22.0, 3525.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('7f66021e04454836b5c382663076177b', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1010', '林华', 3200.00, 0.00, 0.00, 433.33, 0.00, 3633.33, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3633.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('a34b1568c693484a8a9db92026b85c9a', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1007', '王丽', 3400.00, 0.00, 0.00, 433.33, 0.00, 3833.33, 98.00, 188.00, 376.00, 65.00, 20.00, 5.00, 22.0, 21.0, 3457.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('adf436fa13244cfcbe7030e546309695', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1012', '郭红', 3220.00, 0.00, 0.00, 433.33, 0.00, 3653.33, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3653.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('b2bf62287b4e4d4aaaefd51de071248e', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '销售部门', '1016', '陈建军', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 102.00, 200.00, 400.00, 73.00, 20.00, 5.00, 22.0, 22.0, 3533.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('bc21f8bdc3004c0fa163fc061404aa27', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1009', '李菲菲', 3000.00, 0.00, 0.00, 433.33, 0.00, 3433.33, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 20.0, 3433.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('cff78495e87944479484314ff58173e8', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1003', '郭朝生', 3000.00, 0.00, 0.00, 500.00, 0.00, 3500.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3500.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('d0836ae6942d492287853ec8b7db208b', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1005', '黄冬冬', 3000.00, 0.00, 0.00, 500.00, 0.00, 3500.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3500.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('dd304bc10f754e078b7f0f1fe7e1ae68', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1014', '李明', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 100.00, 196.00, 392.00, 71.00, 20.00, 5.00, 22.0, 22.0, 3541.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('e694dfd53bc04f32818de3904c2cc6ed', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '销售部门', '1017', '黄琳', 3500.00, 0.00, 0.00, 433.33, 0.00, 3933.33, 103.00, 202.00, 404.00, 74.00, 20.00, 5.00, 22.0, 22.0, 3529.33, '2019-03-13 19:17:13', '223d3c4b909c4df2aa45c8a6af397563');
INSERT INTO `t_wa_arch` VALUES ('fc1a5b852fc44199b6bd3a152fa03f3f', '2019-01', '2018-01', '2ae1099b794346dd9399d34e00f8b51f', '管理部门', '1004', '刘大清', 3000.00, 0.00, 0.00, 500.00, 0.00, 3500.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 22.0, 22.0, 3500.00, '2019-03-13 19:17:12', '223d3c4b909c4df2aa45c8a6af397563');

SET FOREIGN_KEY_CHECKS = 1;
