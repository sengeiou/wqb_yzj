<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>银行设置</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bank.css" />
</head>

<body>
<!-- 内容主体区域 -->
<div class="layui-body setBank_body" id="content">
	<div class="bank_data">
		<span>银行资料</span>
		<table class="layui-table">
			<thead>
				<tr>
					<td>银行名称</td>
					<td>类型</td>
					<td>币种</td>
					<td>账号</td>
					<td>当前账套的科目</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody id="bankData">

			</tbody>
			<tfoot>
				<tr>
					<td colspan="6"><span class="AddbankData">新增</span></td>
				</tr>
			</tfoot>
		</table>
	</div>
	<div class="bank_abstract" style="display: none;">
		<span>银行对账单摘要确认</span>
		<table class="layui-table">
			<thead>
				<tr>
					<td>摘要/备注/用途包含内容</td>
					<td>匹配</td>
					<td>方向</td>
					<td>银行</td>
					<td>当前账套的科目</td>
				</tr>
			</thead>
			<tbody id="bankAbstract">
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6"><span>新增</span></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>

<div id="addAndEditor">
	<ul>
		<li class="left">银行名称：</li>
		<li class="right"><input class="layui-input bankName" type="text"/></li>
		<li class="left">类型：</li>
		<li class="right">
			<div class="search-item layui-form">
		        <select name="select-bank" class="bankType" id="select-bank" lay-search lay-filter="bank" lay-verify="required">
			        <option value="0">工商银行</option>
			        <option value="1">建设银行</option>
			        <option value="2">交通银行</option>
			        <option value="3">深圳农村商业银行</option>
			        <option value="4">平安银行</option>
			        <option value="5">招商银行</option>
			        <option value="6">中国银行</option>
			        <option value="7">中信银行</option>
			        <option value="8">农业银行</option>
		        </select>
		    </div>
		</li>
		<li class="left">币种：</li>
		<li class="right"><input class="layui-input currency" type="text" value="CNY" readonly="readonly"/></li>
		<li class="left">账号：<i style="color: red;">*</i></li>
		<li class="right"><input class="layui-input bankAccount" type="text" /></li>
		<li class="left">当前账套科目：<i style="color: red;">*</i></li>
		<li class="right">
			<input class="layui-input rightKm" type="text"/>
			<div class="search-itemS layui-form">
		        <ul id="select-bankKM"></ul>
		    </div>
		</li>
	</ul>
</div>
<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/bank/setBank.js?v=${timeStamp }"></script>
</body>
</html>
