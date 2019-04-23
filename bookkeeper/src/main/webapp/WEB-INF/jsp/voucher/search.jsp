<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>凭证查询</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/voucherInput.css">
  <style>
    .layui-body{
      padding-top: 50px;
    }
    .fixed-search{display: block;}
  </style>
</head>

<body>
<!-- 内容主体区域 -->
<div class="layui-body">
  <!--  搜索 -->
  <div class="search fixed-search">
    <div class="search-item">
      <input type="text" class="layui-input" id="keyWord" name="keyWord" placeholder="请输入凭证号或摘要" maxlength="15">
    </div>
    <div class="search-item">
      <input type="text" class="layui-input wMonth" id="beginTime" name="beginTime" autocomplete="off" placeholder="请选择开始期间">
      <i class="layui-icon">&#xe637;</i>
    </div>
    <div class="search-item search-before">
      <input type="text" class="layui-input wMonth" id="endTime" name="endTime" autocomplete="off" placeholder="请选择结束期间">
      <i class="layui-icon">&#xe637;</i>
    </div>
    <!-- <div class="layui-btn layui-btn-normal id="search-btn"">查询</div> -->
    <div class="search-btn">
      <div class="layui-btn layui-btn-normal" id="add-voucher">新增</div>
      <div class="layui-btn layui-btn-normal" id="voucher-sort">整理断号</div>
      <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
      <!-- <div class="layui-btn layui-btn-primary" id="voucher-print">打印</div> -->
      <!-- <div class="layui-btn layui-btn-primary" id="voucher-export">导出</div> -->
    </div>
  </div>

  <div class="layui-form table-box">
    <div class="fixed-head">
      <table class="layui-table voucher-list">
        <colgroup>
          <col width="5%">
          <col width="10%">
          <col width="10%">
          <col width="20%">
          <col width="20%">
          <col width="10%">
          <col width="10%">
          <col width="8%">
          <col width="8%">
        </colgroup>
        <thead>
          <tr>
            <th>
              <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
            </th>
            <th>日期</th>
            <th>凭证字号</th>
            <th>摘要</th>
            <th>科目</th>
            <th>借方金额</th>
            <th>贷方金额</th>
            <th>制单人</th>
            <th>操作</th>
          </tr>
        </thead>
      </table>
    </div>
    <table class="layui-table voucher-list" id="voucher-table">
      <colgroup>
        <col width="5%">
        <col width="10%">
        <col width="10%">
        <col width="20%">
        <col width="20%">
        <col width="10%">
        <col width="10%">
        <col width="8%">
        <col width="8%">
      </colgroup>
      <thead>
        <tr>
          <th>
            <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
          </th>
          <th>日期</th>
          <th>凭证字号</th>
          <th>摘要</th>
          <th>科目</th>
          <th>借方金额</th>
          <th>贷方金额</th>
          <th>制单人</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody id="voucher-list">
        <tr class="tc"><td colspan="9" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td></tr>
      </tbody>
    </table>
    <div class="layui-table-page">
      <div id="layui-table-page1"></div>
    </div>
  </div>
</div>

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/voucher/voucher-import.js?v=${timeStamp }"></script>
</body>
</html>
