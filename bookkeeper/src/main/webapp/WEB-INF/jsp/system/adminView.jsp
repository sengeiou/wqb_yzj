<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
    	<%@ include file="../head.jspf" %>
        <title>管理员</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css">
        <style>
        	.layui-layer-msg{
        		width: auto !important;
    			top: 45% !important;
    			left: 45% !important;
        	}
        	.layui-layer-dialog .layui-layer-padding{
        		height: auto !important;
        	}
        </style>
    </head>
    <!--onkeydown="keySelect();" //回车键查询-->
    <body onkeydown="keySelect();">
    	<%@ include file="../header.jsp" %>
    	<div class="main administratorS">
          <span style="display: none;" id="session" class="main-session">${userDate.perList}</span>
          <!-- 数据 / 公告 -->
          <div class="main-data cl">
            <div class="main-left">
            	<div class="main-dataS-top">做账统计</div>
                <div class="main-quantity" id="main-quantity">
                </div>
                <div class="main-dataS">
	              	<table>
		              	<tr>
		              		<td><div class="main-color1"></div></td>
		              		<td>已完成<label class="main-label1"></label></td>
		              	</tr>
		              	<tr>
		              		<td><div class="main-color2"></div></td>
		              		<td>进行中<label class="main-label2"></label></td>
		              	</tr>
		              	<tr>
		              		<td><div class="main-color3"></div></td>
		              		<td>未开始<label class="main-label3"></label></td>
		              	</tr>
		            </table>
                </div>
            </div>
            <!--数据总览-->
            <div class="main-right main-right-top">
              <div class="main-title ">
                <h3 class="title">总览</h3>
              </div>
              <ul class="main-list">
                <li>
                  <span class="newEnterprise">本月新负责企业 </span>
                  <span class="newNumber byxzz"></span>
                  <span class="percentage byptxzqyzz"></span>
                </li>
                <li>
                  <span class="newEnterprise">本月记账完成企业 </span>
                  <span class="newNumber dlCom"></span>
                  <span class="percentage byXzDlCom"></span>
                </li>
                <li>
                  <span class="newEnterprise">停用企业 </span>
                  <span class="newNumber byTy"></span>
                  <span class="percentage zzl"></span>
                </li>
                <li>
                  <span class="newEnterprise">总企业数 </span>
                  <span class="newNumber ptZqy"></span>
                </li>
                <li style="border-right: 1px solid #e9e9e9;">
                  <span class="newEnterprise">累计停用企业数</span>
                  <span class="newNumber ptTyQy"></span>
                </li>
              </ul>
            </div>
          </div>
          <!-- 做账列表 -->
          <div class="main-table cl">
            <div style="padding: 11px 0; float: left;">
              <h3 class="main-title">做账列表</h3>
            </div>
            <div class="main-search">
              <input id="main-query" type="text" placeholder="请输入搜索关键字">
              <button id="main-search">查询</button>
            </div>
            <table class="layui-table">
              <colgroup>
                <col width="5%">
                <col width="22%">
                <col width="10%">
                <col width="8%">
                <col width="12%">
                <col width="5%">
                <col width="5%">
                <col width="6%">
              </colgroup>
              <thead>
                <tr>
                  <th>序号</th>
                  <th>企业名称</th>
                  <th>手机号码</th>
                  <th>启用日期</th>
                  <th>处理人</th>
                  <th>状态</th>
                  <th>进度</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody class="mian-tbody" id="mianTbody"></tbody>
            </table>
            <div class="layui-table-page">
              <div id="layui-table-page2"></div>
            </div>
          </div>

          <!--各财务人员负责企业数量比例图-->
          <div class="main-table pie">
            <div class="main-title">
              <h3 class="title">各财务人员负责企业数量比例图</h3>
            </div>
            <div id="businessPie2">
            </div>
            <div class="mainDataS">
              	<table>
	              	<tbody id="business"></tbody>
	            </table>
            </div>
          </div>

           <!--平台有效企业数量趋势图-->
          <div class="main-table line">
            <div class="main-title">
              <h3 class="title">平台有效企业数量趋势图</h3>
            </div>
            <div id="effectiveLine">

            </div>
          </div>
        </div>

    	<%@ include file="../voucher/import-popup.jsp" %>
        <%@ include file="../js.jspf" %>
        <script src="${pageContext.request.contextPath }/plugins/echarts.common.min.js"></script>
        <script src="${pageContext.request.contextPath }/js/setTing/main.js?v=${timeStamp }"></script>
        <script src="${pageContext.request.contextPath }/js/setTing/adminView.js?v=${timeStamp }"></script>
 	</body>
</html>
