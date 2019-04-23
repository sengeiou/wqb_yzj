<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>账簿</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/accountBook.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/plugins/zTree/css/zTreeStyle/zTreeStyle.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/plugins/dataTables/DataTables.css">
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<!-- 右侧 内容包含区 -->
<div class="layui-body" id="content">
  <div class="layui-tab layui-tab-brief init_con" lay-filter="account">
    <ul class="crumbs layui-tab-title fixed-nav" id="navDOM1">
      <li class="layui-this" id="account_tab">总账</li>
      <li id="bills_tab">明细帐</li>
      <li id="amount-total-tab">数量金额总帐</li>
      <li id="amount-tab" style="display: none;">数量金额明细帐</li>
      <li id="home-balance">科目余额表</li>
      <li id="vchr-sum">凭证汇总表</li>
      <li id="depreciation_tab">固定资产折旧明细</li>
    </ul>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>

    <!-- tab内容展示 -->
    <div class="layui-tab-content">
      <!-- 总账 -->
      <!--总账1-->
      <div class="layui-tab-item layui-show" id="account">
        <div class="layui-form">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM2">
          	<div class="zongzhang">
          		<span style="float: left;width: 45px;text-align: center;">总账</span>
          		<span class="zz_Close"></span>
          	</div>
             <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <div class="layui-btn layui-btn-normal zongzhang_export">导出</div>
            </div>
          </div>
          <!-- 搜索 end -->
          <p class="allName"></p>
          <!--总账列表-->
          <div id="accountListHead"  class="fixed-head">
          	<table class="layui-table">
	            <colgroup>
	              <col width="10%">
	              <col width="30%">
	              <col width="10%">
	              <col width="11%">
	              <col width="11%">
	              <col width="11%">
	              <col width="6%">
	              <col width="11%">
	            </colgroup>
	            <thead>
	              <tr>
	                <th>科目编码</th>
	                <th>科目名称</th>
	                <th>期间</th>
	                <th>摘要</th>
	                <th>借方金额</th>
	                <th>贷方金额</th>
	                <th>方向</th>
	                <th>余额</th>
	              </tr>
	            </thead>
	          </table>
          </div>
          <div id="accountListS">
          	<table class="layui-table display" cellspacing="0" width="100%">
	            <colgroup>
	              <col width="10%">
	              <col width="30%">
	              <col width="10%">
	              <col width="11%">
	              <col width="11%">
	              <col width="11%">
	              <col width="6%">
	              <col width="11%">
	            </colgroup>
	            <tbody id="accountList_item1">
               <tr class="tc">
                  <td colspan="8" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
              </tbody>
	          </table>
          </div>
        </div>
      </div>

      <!-- 明细帐 -->
      <!-- 明细帐1 -->
      <div class="layui-tab-item" id="bills" style="min-height: 500px;">
        <div class="layui-form lay_form lay_form1">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM2">
            <div class="zongzhang">
          		<span style="float: left;width: 45px;text-align: center;">明细账</span>
          		<span class="zz_Close">2018年第1期 至 2018年第4期</span>
          	</div>
            <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <!--<div class="layui-btn layui-btn-normal search_export_btn">导出</div>-->
              <div class="layui-btn layui-btn-normal search_All">导出</div>
              <ul>
	          		<li class="search_export_btn">导出当前</li>
	          		<li class="search_all_btn">导出全部</li>
	          	</ul>
            </div>
          </div>
          <div class="fixed-head" id="fixedDOM2_1">
            <p class="allName maxLeng">科目名称：</p>
            <table class="layui-table detailed">
              <colgroup>
                <col width="10%">
                <col width="10%">
                <col width="11%">
                <col width="11%">
                <col width="11%">
                <col width="6%">
                <col width="11%">
              </colgroup>
              <thead>
                <tr>
                  <th>日期</th>
                  <th>凭证字号</th>
                  <th>摘要</th>
                  <th>借方</th>
                  <th>贷方</th>
                  <th>余额方向</th>
                  <th>余额</th>
                </tr>
              </thead>
            </table>
            <div class="nav_ringht dn">
              <div class="layui-tab layui-tab-card" lay-filter="bills_tab">
                <p class="nav_title">科目列表</p>
                <ul class="layui-tab-title nav_li">
                  <li class="layui-this">资产</li>
                  <li>负债</li>
                  <li>共同</li>
                  <li>权益</li>
                  <li>成本</li>
                  <li>损益</li>
                </ul>
              </div>
            </div>
          </div>
          <p class="allName maxLeng">科目名称：<span class="sub_code_name">1001 - 库存现金</span></p>
          <table class="layui-table detailed" id="">
            <colgroup>
              <col width="10%">
              <col width="10%">
              <col width="11%">
              <col width="11%">
              <col width="11%">
              <col width="6%">
              <col width="11%">
            </colgroup>
            <thead>
              <tr>
                <th>日期</th>
                <th>凭证字号</th>
                <th>摘要</th>
                <th>借方</th>
                <th>贷方</th>
                <th>余额方向</th>
                <th>余额</th>
              </tr>
            </thead>
            <tbody id="detailList1">
              <tr class="tc">
                <td colspan="7" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
              </tr>
            </tbody>
          </table>
          <div class="layui-table-page">
	          <div id="layui-table-page2"></div>
	        </div>
        </div>
        <!--快速查询-->
        <div class="fasttips">
        	<div class="fasttipsMian">
        		<div class="speediness">
	        		<span></span>快速切换
	        	</div>
	        	<div class="inquireBox">
	        		<input id="keyword" type="text" placeholder="请输入..." onKeyup="getContent(this);" onkeydown='if(event.keyCode==13) return false;'>
	        		<button class="inquireBot" onclick="searchTreeNode()"></button>
	        		<span class="layui-icon qingkong" title="清空查询内容">&#x1006;</span>
	        		<ul id="append"></ul>
	        	</div>
	        	<div class="subjectTree" id="menuContent">
	        		<ul id="subjectTree" class="ztree">
	        		</ul>
	        	</div>
        	</div>
        	<span class="unfold"></span>
        </div>
      </div>

      <!-- 数量金额总帐 -->
      <div class="layui-tab-item">
        <div class="layui-form lay_form">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM3">
            <div class="search-item">
              <input type="text" class="layui-input" placeholder="请输入科目编码" name="sub_code" maxlength="15" id="sub_code">
            </div>
            <div class="search-item">
              <input type="text" class="layui-input" placeholder="请输入科目名称" name="comNameSpec" maxlength="15" id="comNameSpec">
            </div>
            <div class="layui-btn layui-btn-normal" id="search-stock">查询</div>
             <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <div class="layui-btn layui-btn-normal sljezz_btn">导出</div>
            </div>
          </div>
          <div class="fixed-head" id="fixedDOM3">
            <table class="layui-table">
              <colgroup>
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	            </colgroup>
              <thead>
                <tr>
                  <th rowspan="2">科目编码</th>
                  <th rowspan="2">科目全名</th>
                  <th colspan="3">期初结存</th>
                  <th colspan="2">本期收入</th>
                  <th colspan="2">本期发出</th>
                  <th colspan="2">本年累计收入</th>
                  <th colspan="2">本年累计发出</th>
                  <th rowspan="2">余额借贷方向</th>
                  <th colspan="3">期末结存</th>
                </tr>
                <tr>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                </tr>
              </thead>
            </table>
          </div>
          <div id="amountID">
	          <table class="layui-table" id="amount-total">
	            <colgroup>
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	            </colgroup>
	            <tbody id="amount-account">
                <tr class="tc">
                  <td colspan="17" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
	            </tbody>
	          </table>
	        </div>
          <div class="layui-table-page">
            <div id="layui-table-page1"></div>
          </div>
        </div>
      </div>

      <!-- 数量金额明细帐 -->
      <div class="layui-tab-item">
        <div class="layui-form lay_form">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM4">
            <div class="search-item">
              <input type="text" class="layui-input startTime" placeholder="请选择开始日期">
              <i class="layui-icon">&#xe637;</i>
            </div>
            <div class="search-item search-before">
              <input type="text" class="layui-input endTime" placeholder="请选择结束日期">
              <i class="layui-icon">&#xe637;</i>
            </div>
            <div class="search-item ">
              <div class="lay_certificate layui-form lay_select">
                <select name="level" lay-search class="level" id="select_mount"></select>
              </div>
            </div>
            <div class="layui-btn layui-btn-normal search_btn">查询</div>
             <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <div class="layui-btn layui-btn-normal search_export">导出</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM4">
            <p class="allName maxLeng">科目名称：</p>
            <table class="layui-table detailed">
              <colgroup>
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
              </colgroup>
              <thead>
                <tr>
                  <th rowspan="2">日期</th>
                  <th rowspan="2">凭证号</th>
                  <th rowspan="2">摘要</th>
                  <th colspan="3">借方</th>
                  <th colspan="3">贷方</th>
                  <th rowspan="2">方向</th>
                  <th colspan="3">余额</th>
                </tr>
                <tr>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                </tr>
              </thead>
            </table>
          </div>
          <p class="allName maxLeng">科目名称：</p>
          <table class="layui-table detailed" id="num-amount">
            <colgroup>
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
              <col width="8%">
            </colgroup>
            <thead>
              <tr>
                <th rowspan="2">日期</th>
                <th rowspan="2">凭证号</th>
                <th rowspan="2">摘要</th>
                <th colspan="3">借方</th>
                <th colspan="3">贷方</th>
                <th rowspan="2">方向</th>
                <th colspan="3">余额</th>
              </tr>
              <tr>
                <th>数量</th>
                <th>单价</th>
                <th>金额</th>
                <th>数量</th>
                <th>单价</th>
                <th>金额</th>
                <th>数量</th>
                <th>单价</th>
                <th>金额</th>
              </tr>
            </thead>
            <tbody id="amount">
              <tr class="tc">
                <td colspan="13" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
              </tr>
            </tbody>
          </table>
          <!-- <div class="nav_ringht">
            <div class="layui-tab layui-tab-card" lay-filter="Amount_tab">
              <p class="nav_title">科目列表</p>
              <ul class="layui-tab-title nav_li" id="subJectLists">
                <li class="layui-this">资产</li>
                <li>负债</li>
                <li>共同</li>
                <li>权益</li>
                <li>成本</li>
                <li>损益</li>
              </ul>
              <div class="layui-tab-content" id="lay_content_tab">
                <div class="layui-tab-item layui-show">
                  <ul class="lay_nav_li ztree" id="zTreeTwo">

                  </ul>
                </div>
              </div>
            </div>
          </div> -->
        <!--   <div class="nav_icon">
            <i class="layui-icon">&#xe65c;</i>
          </div> -->
          <!--  <div class="layui-table-page">
            <div id="layui-table-page3"></div>
          </div> -->
        </div>
      </div>

      <!-- 科目余额表 -->
      <div class="balanceTable layui-tab-item">
        <div class="layui-form">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM5" style="width: 98%;">
            <div class="search-item">
              <input type="text" class="layui-input startTime" placeholder="请选择开始日期">
              <i class="layui-icon">&#xe637;</i>
            </div>
            <div class="search-item search-before">
              <input type="text" class="layui-input endTime" placeholder="请选择结束日期">
              <i class="layui-icon">&#xe637;</i>
            </div>
            <div class="layui-btn layui-btn-normal search_btn">查询</div>
             <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <div class="layui-btn layui-btn-normal search_export">导出</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM5" style="display: block;position:absolute;left:0;">
            <table class="layui-table  account-balances">
              <colgroup>
                <col width="8%">
                <col width="8%">
                <col width="20%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
              </colgroup>
              <thead>
                <tr>
                  <th rowspan="2">期间</th>
                  <th rowspan="2">科目编码</th>
                  <th rowspan="2">科目名称</th>
                  <th colspan="2">期初余额</th>
                  <th colspan="2">本期发生额</th>
                  <th colspan="2">本年累计发生额</th>
                  <th colspan="2">期末余额</th>
                </tr>
                <tr>
                  <th>借方</th>
                  <th>贷方</th>
                  <th>借方</th>
                  <th>贷方</th>
                  <th>借方</th>
                  <th>贷方</th>
                  <th>借方</th>
                  <th>贷方</th>
                </tr>
              </thead>
            </table>
          </div>
          <div id="balancesID">
          	<table class="layui-table  account-balances">
	            <colgroup>
	              <col width="8%">
	              <col width="8%">
	              <col width="20%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	              <col width="8%">
	            </colgroup>
	            <tbody id="balance">
                <tr class="tc">
                  <td colspan="11" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
              </tbody>
	            <tfoot class="dn">
	              <tr>
	                <td></td>
	                <td></td>
	                <td>
	                  <strong>合计：</strong>
	                </td>
	                <td></td>
	                <td></td>
	                <td></td>
	                <td></td>
	                <td id="DebitMoney" class="tr"></td>
	                <td id="CreditMoney" class="tr"></td>
	                <td></td>
	                <td></td>
	              </tr>
	            </tfoot>
	          </table>
          </div>
          <!--  <div class="layui-table-page">
            <div id="layui-table-page4"></div>
          </div> -->
        </div>
      </div>

      <!-- 凭证汇总表 -->
      <div class="layui-tab-item">
        <div class="layui-form">
          <!--  搜索 -->
          <div class="search fixed-search" id="searchDOM6">
            <div class="search-btn">
              <div class="layui-btn layui-btn-normal print_btn" id="print-vchr">打印</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM6">
            <!-- <p class="vchr-range">凭证范围：2018年2月1日至28日
              <span></span>
              <span>记：41张（1-41）</span>
              <span>共41张</span>
              <span>附件共0张</span>
            </p> -->
            <table class="layui-table">
              <colgroup>
                <col width="4%">
                <col width="16%">
                <col width="28%">
                <col width="26%">
                <col width="26%">
              </colgroup>
              <thead>
                <tr>
                  <th>序号</th>
                  <th>科目代码</th>
                  <th>科目名称</th>
                  <th>借方金额</th>
                  <th>贷方金额</th>
                </tr>
              </thead>
            </table>
          </div>
          <!-- <p class="vchr-range">凭证范围：2018年2月1日至28日
            <span></span>
            <span>记：41张（1-41）</span>
            <span>共41张</span>
            <span>附件共0张</span>
          </p> -->
          <div id="sumVoucher">
          	<table class="layui-table">
	            <colgroup>
	              <col width="4%">
	              <col width="16%">
	              <col width="28%">
	              <col width="26%">
	              <col width="26%">
	            </colgroup>
	            <tbody id="sum-voucher">
	              <tr class="tc">
	                <td colspan="5" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
	              </tr>
	            </tbody>
	          </table>
          </div>
        </div>
      </div>

    	<!--固定资产折旧明细-->
    	<div class="layui-tab-item" id="depreciation">
        <div class="layui-form">
          <!--  搜索 -->
          <div class="search fixed-search" id="depreciationDOM1">
         		<div style="width: 70px; padding-right: 0px; line-height: 30px;float: left;">会计期间：</div>
						<div class="search-item search-item3 depreciation" style="width: 128px;">
							<span class="accountingPeriod"></span>
		          <input type="text" style="display: none;" class="layui-input accounting" disabled="disabled" value="">
		          <ul id="startStopDate2"></ul>
	        	</div>
	        	<div class="search-item depreciation" style="margin-top:6px;">
		          <input type="checkbox" name="like1[read]" lay-skin="primary" title="显示已清理资产">
	        	</div>
            <div class="layui-btn layui-btn-normal search_btn1">查询</div>
             <div class="search-btn">
             	<!--<div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>-->
              <!--<div class="layui-btn layui-btn-normal">导出</div>-->
            </div>
          </div>
          <!-- 搜索 end -->
          <div class="fixed-head" id="gdzcmxbDOM1" style="width: 100%; position: absolute;left: 0;">
            <p class="allName">科目名称：</p>
            <table class="layui-table">
              <colgroup>
                <col width="6%">
	              <col width="8%">
	              <col width="12%">
	              <col width="8%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="6%">
	              <col width="10%">
	              <col width="10%">
              </colgroup>
              <thead>
                <tr>
                  <th>类别</th>
	                <th>编码</th>
	                <th>固定资产名称</th>
	                <th>部门</th>
	                <th>月折旧率%</th>
	                <th>原值</th>
	                <th class="dateText"></th>
	                <th>期末累计折旧</th>
	                <th>本年累计折旧</th>
	                <th>期末净值</th>
                </tr>
              </thead>
            </table>
          </div>

          <p class="allName">科目名称：</p>
          <!--总账列表-->
          <table class="layui-table" id="accountList">
            <colgroup>
              <col width="6%">
              <col width="8%">
              <col width="12%">
              <col width="8%">
              <col width="6%">
              <col width="6%">
              <col width="6%">
              <col width="6%">
              <col width="10%">
              <col width="10%">
            </colgroup>
            <thead>
              <tr>
                <th>类别</th>
                <th>编码</th>
                <th>固定资产名称</th>
                <th>部门</th>
                <th>月折旧率%</th>
                <th>原值</th>
                <th class="dateText"></th>
                <th>期末累计折旧</th>
                <th>本年累计折旧</th>
                <th>期末净值</th>
              </tr>
            </thead>
            <tbody id="depreciation_item">
              <tr class="tc">
                <td colspan="10" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <script type="text/html" id="table-tool">
        <span class="del" lay-event="del"></span>
    </script>
  </div>
</div>
<%@ include file="inquire.jsp" %>
<!-- End #content -->

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/plugins/zTree/js/jquery.ztree.core.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/zTree/js/jquery.ztree.exedit.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/zTree/js/jquery.ztree.exhide.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/dataTables/Datatable.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/zTreeData.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/accountBook/import.js?v=${timeStamp }"></script>
</body>

</html>
