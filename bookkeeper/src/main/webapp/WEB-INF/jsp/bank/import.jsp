<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>理票——银行对账单导入</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bank.css" />
</head>

<body>

<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <!--<div class="crumbs fixed-nav" id="navDOM1">
    <span class="crumbsTxt">银行对账单导入</span>
  </div>-->
  <span class="btn-toolbar fixed-icon">
    <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
  </span>

  <!-- 搜索 -->
  <div class="search fixed-search" style="padding-top: 13px;">
    <div class="search-item layui-form">
      <select name="select-bank" id="select-bank" lay-search lay-filter="bank" lay-verify="required">
        <!-- <option value="">请选择银行</option> -->
        <option value="0">全部银行</option>
        <option value="1">工商银行</option>
        <option value="2">建设银行</option>
        <option value="3">交通银行</option>
        <option value="4">深圳农村商业银行</option>
        <option value="5">平安银行</option>
        <option value="6">招商银行</option>
        <option value="7">中国银行</option>
        <option value="8">中信银行</option>
        <option value="9">农业银行</option>
      </select>
    </div>
    <div class="search-item">
      <label for="keyWord">
        <input type="text" class="layui-input" placeholder="请输入对方账户查询..." name="keyWord" maxlength="15" id="keyWord">
      </label>
    </div>
    <div class="search-item">
      <input type="text" class="layui-input wDate" id="beginTime" name="beginTime" autocomplete="off" placeholder="请选择开始日期">
      <i class="layui-icon">&#xe637;</i>
    </div>
    <div class="search-item search-before">
      <input type="text" class="layui-input wDate" id="endTime" name="endTime" autocomplete="off" placeholder="请选择结束日期">
      <i class="layui-icon">&#xe637;</i>
    </div>
    <div class="layui-btn layui-btn-normal" id="search-btn">查询</div>
    <div class="search-btn">
    	<div class="layui-btn layui-btn-normal" id="save-btn" style="display: none;">保存</div>
      <div class="layui-btn layui-btn-normal" id="popup-btn">导入</div>
      <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
    </div>
  </div>

  <!-- 内容展示 -->
  <div class="layui-form table-box">
    <div class="fixed-head" id="Dywe">
      <table class="layui-table" lay-filter="table">
        <colgroup>
          <col width="5%">
          <col width="8%">
          <col width="8%">
          <col width="10%">
          <col width="18%">
          <col width="7%">
          <col width="7%">
          <col width="8%">
          <col width="16%">
          <col width="8%">
          <col width="5%">
        </colgroup>
        <thead>
          <tr>
            <th>
              <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
            </th>
            <th>银行类型</th>
            <th>交易日期</th>
            <th>摘要</th>
            <th>科目</th>
            <th>收入金额</th>
            <th>支出金额</th>
            <th>账户余额</th>
            <th>对方户名</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
      </table>
    </div>
    <div id="dataList">
    	<table class="layui-table" id="table" lay-filter="table">
	      <colgroup>
	        <col width="5%">
          <col width="8%">
          <col width="8%">
          <col width="10%">
          <col width="18%">
          <col width="7%">
          <col width="7%">
          <col width="8%">
          <col width="16%">
          <col width="8%">
          <col width="5%">
	      </colgroup>
	      <tbody id="data-list">
	        <tr class="tc">
	          <td colspan="11" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
	        </tr>
	      </tbody>
	    </table>
    </div>
    <div class="layui-table-page">
      <div id="layui-table-page1"></div>
    </div>
  </div>
  <!-- 科目列表 -->
	<div class="subList" id="subList">
	  <ul id="subLists">
	    <li class="sub-title ellipsis">正在加载数据~</li>
	  </ul>
	  <p class="sub-addSub" id="sub-addSub">
	    <span>新增科目</span>
	  </p>
	</div>
	
	<!--导入完成弹框-->
	<div id="bankPopUpBox" class="bankPopUpBox">
		<div class="fixed-head" id="Dywe">
      <table class="layui-table" lay-filter="table">
        <colgroup>
          <col width="8%">
          <col width="8%">
          <col width="10%">
          <col width="18%">
          <col width="7%">
          <col width="7%">
          <col width="8%">
          <col width="16%">
          <col width="8%">
        </colgroup>
        <thead>
          <tr>
            <th>银行类型</th>
            <th>交易日期</th>
            <th>摘要</th>
            <th>科目</th>
            <th>收入金额</th>
            <th>支出金额</th>
            <th>账户余额</th>
            <th>对方户名</th>
            <th>备注</th>
          </tr>
        </thead>
      </table>
    </div>
    <div id="dataListT">
    	<table class="layui-table" id="table" lay-filter="table">
	      <colgroup>
          <col width="8%">
          <col width="8%">
          <col width="10%">
          <col width="18%">
          <col width="7%">
          <col width="7%">
          <col width="8%">
          <col width="16%">
          <col width="8%">
	      </colgroup>
	      <tbody id="bankDataList">
	        <tr class="tc">
	          <td colspan="10" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
	        </tr>
	      </tbody>
	    </table>
    </div>
	</div>
	<!--科目选择弹出框-->
	<div id="subjectsF" class="subjectsF">
		<div class="reminder">金额不平衡，请检查！</div>
		<table class="layui-table">
			<colgroup>
		    <col width="65%">
			  <col width="20%">
			  <col width="15%">
		  </colgroup>
			<thead>
				<tr>
					<th>对方科目</th>
					<th>金额</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="subjectsBody">

			</tbody>
		</table>
	</div>
<div class="zhezhao"></div>
</div>

<!-- End #content -->

<!-- 导入文件弹窗 -->
<div class="popup file" id="upload-popup">
  <form method="post" enctype="multipart/form-data">
    <label class="ovh fileLabel">
      <span class="layui-elip db fileName">请选择97格式.xls...</span>
      <input type="file" class="in-file" name="file" id="upload-input" placeholder="格式：.xls">
    </label>
    <div class="progress">
      <span class="percentage"></span>
      <span class="text p-rel">0%</span>
    </div>
    <span class="layui-btn layui-btn-normal mr50" id="upload-btn">上传</span>
    <span class="layui-btn layui-btn-primary" id="to-down">选择下载模板</span>
  </form>
</div>

<!-- 银行模板下载弹窗 -->
<div class="popup" id="down-popup">
  <ul class="layui-form ml100">
    <li data-id="1">
      <input type="radio" name="bank-select" title="工商银行" value="1">
    </li>
    <li data-id="2">
      <input type="radio" name="bank-select" title="建设银行" value="2">
    </li>
    <li data-id="3">
      <input type="radio" name="bank-select" title="交通银行" value="3">
    </li>
    <li data-id="4">
      <input type="radio" name="bank-select" title="深圳农村商业银行" value="4">
    </li>
    <li data-id="5">
      <input type="radio" name="bank-select" title="平安银行" value="5">
    </li>
    <li data-id="6">
      <input type="radio" name="bank-select" title="招商银行" value="6">
    </li>
    <li data-id="7">
      <input type="radio" name="bank-select" title="中国银行" value="7">
    </li>
    <li data-id="8">
      <input type="radio" name="bank-select" title="中信银行" value="8">
    </li>
    <li data-id="9">
      <input type="radio" name="bank-select" title="农业银行" value="9">
    </li>
  </ul>
</div>


<%@ include file="../voucher/import-popup.jsp" %>
<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/bank/import.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/addSub.js?v=${timeStamp }"></script>
</body>

</html>
