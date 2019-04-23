<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>理票——工资导入</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <style>
    /* 文件上传  */
    .file {
    padding-left: 10px;
  }
  .file label {
    position: relative;
    display: inline-block;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
    margin: 10px 0;
    padding: 5px 0 0 6px;
    width: 280px;
    height: 32px;
    border: 1px solid #ccc;
    background: url(../img/icon-up.png) no-repeat 250px;
  }
  .file .sub-btn {
    vertical-align: top;
  }
  .file .in-file {
    color: transparent;
    position: absolute;
    top: 0;
    left: 0;
    opacity: 0;
    filter:alpha(opacity=0);
  }
  .table-box td{
    text-align: right;
  }
  .table-box td:nth-of-type(1),
  .table-box td:nth-of-type(2),
  .table-box td:nth-of-type(4),
  .table-box td:nth-of-type(6),
  .table-box td:nth-of-type(7),
  .table-box td:nth-of-type(8),
  .table-box td:last-of-type {
    text-align: center;
  }
  .table-box td:nth-of-type(3),
  .table-box td:nth-of-type(5){
    text-align: left;
  }
  .table-box .fixed-head {
    padding-top: 0px;
    margin-top: -22px;
  }
  @media screen and (max-width: 1500px) {
    .layui-table th{padding: 8px 0;}
  }
  </style>
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <!--<div class="crumbs fixed-nav" id="navDOM1" style="display: none;">
    <span class="crumbsTxt">工资导入</span>
  </div>-->
  <span class="btn-toolbar fixed-icon">
    <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
  </span>

  <!--  搜索 -->
  <div class="search fixed-search" style="padding-top: 13px;">
    <div class="search-item">
      <label for="keyWord">
        <input type="text" class="layui-input" placeholder="请输入姓名..." name="keyWord" maxlength="15" id="keyWord">
      </label>
    </div>
    <div class="search-item layui-form">
      <select name="archDate" id="select-date" lay-search lay-verify="required">
        <option value="-1">工资发放月份</option>
      </select>
    </div>
    <div class="layui-btn layui-btn-normal" id="search-btn">查询</div>
    <div class="search-btn">
      <div class="layui-btn layui-btn-normal" id="popup-btn">导入</div>
      <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
    </div>
  </div>

  <!-- 内容展示 -->
  <div class="layui-form table-box">
    <div class="fixed-head">
      <table class="layui-table" lay-filter="table">
        <colgroup>
          <col width="3%">
          <col width="3%">
          <col width="5%">
          <col width="4%">
          <col width="5%">
          <col width="5%">
          <col width="3%">
          <col width="3%">
          <col width="6%">
          <col width="5%">
          <col width="6%">
          <col width="5%">
          <col width="5%">
          <col width="6%">
          <col width="6%">
          <col width="6%">
          <col width="5%">
          <col width="5%">
          <col width="5%">
          <col width="6%">
          <col width="3%">
        </colgroup>
        <thead>
          <tr>
            <th>
              <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
            </th>
            <th>序号</th>
            <th>所属部门</th>
            <th>人员编码</th>
            <th>人员姓名</th>
            <th>工资发放月份</th>
            <th>考勤天数</th>
            <th>实际出勤</th>
            <th>基本工资</th>
            <th>岗位津贴</th>
            <th>平时加班</th>
            <th>周末加班</th>
            <th>其他补贴</th>
            <th>应发工资</th>
            <th>扣社保</th>
            <th>扣公积金</th>
            <th>其他扣款</th>
            <th>扣款合计</th>
            <th>个税</th>
            <th>实发工资</th>
            <th>操作</th>
          </tr>
        </thead>
      </table>
    </div>
    <div id="salaryList">
    	<table class="layui-table " id="table" lay-filter="table">
	      <colgroup>
	        <col width="3%">
	        <col width="3%">
	        <col width="5%">
	        <col width="4%">
	        <col width="5%">
	        <col width="5%">
	        <col width="3%">
	        <col width="3%">
	        <col width="6%">
	        <col width="5%">
	        <col width="6%">
	        <col width="5%">
	        <col width="5%">
	        <col width="6%">
	        <col width="6%">
	        <col width="6%">
	        <col width="5%">
	        <col width="5%">
	        <col width="5%">
	        <col width="6%">
	        <col width="3%">
	      </colgroup>
	      <tbody id="salary-list">
	        <tr class="tc">
	          <td colspan="21" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
	        </tr>
	      </tbody>
	    </table>
    </div>
    <div class="layui-table-page">
      <div id="layui-table-page1"></div>
    </div>
  </div>
</div>
<!-- End #content -->

<!-- 导入文件弹窗 -->
<div class="popup file" id="upload-popup">
  <form method="post" enctype="multipart/form-data">
    <label class="ovh fileLabel">
      <span class="layui-elip db fileName">请选择97格式.xls...</span>
      <input type="file" class="in-file" name="uploadArch" id="upload-input" placeholder="格式：.xls">
    </label>
    <div class="progress">
      <span class="percentage"></span>
      <span class="text p-rel">0%</span>
    </div>
    <span class="layui-btn layui-btn-normal mr50" id="upload-btn">上传</span>
    <a class="layui-btn layui-btn-primary mr10" href="${pageContext.request.contextPath }/arch/downExcel">下载模板</a>
  </form>
</div>

<%@ include file="../js.jspf" %>
  <script src="${pageContext.request.contextPath }/js/salary/import.js?v=${timeStamp }"></script>
</body>

</html>
