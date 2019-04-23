<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>固定资产管理</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/assets.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/plugins/zTree/css/zTreeStyle/zTreeStyle.css">
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <div class="layui-tab layui-tab-brief" lay-filter="tab">
    <ul class="layui-tab-title crumbs fixed-nav" id="navDOM1">
      <li class="layui-this">固定资产管理</li>
    </ul>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>

    <div class="layui-tab-content">
      <!-- 固定资产 -->
      <div class="layui-tab-item layui-show">
        <!--  搜索 -->
        <div class="search fixed-search" style="padding-top: 13px;">
          <div class="search-item">
            <label for="keyWord">
              <input type="text" class="layui-input" placeholder="请输入资产名称..." name="keyWord" maxlength="15" id="keyWord">
            </label>
          </div>
          <div class="search-item">
            <input type="text" class="layui-input wMonth" id="acperiod" name="acperiod" autocomplete="off" placeholder="查询入账时间">
            <i class="layui-icon">&#xe637;</i>
          </div>
          <div class="layui-btn layui-btn-normal" id="search-btn">查询</div>
          <div class="layui-btn layui-btn-normal" id="add-btn">新增</div>
          <div class="search-btn">
            <div class="layui-btn layui-btn-normal" id="popup-btn">导入</div>
            <div class="layui-btn layui-btn-normal" id="search_export">导出</div>
            <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
          </div>
        </div>

        <div class="layui-form table-box">
          <div class="fixed-head">
            <table class="layui-table" lay-filter="table">
              <colgroup>
                <col width="5%">
                <col width="5%">
                <col width="14%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="14%">
                <col width="6%">
                <col width="8%">
                <col width="6%">
                </colgroup>
              <thead>
                <tr>
                  <th>
                    <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                  </th>
                  <th>代码</th>
                  <th>名称</th>
                  <th>类别</th>
                  <th>使用部门</th>
                  <th>使用情况</th>
                  <th>入账日期</th>
                  <th>增加方式</th>
                  <th>折旧方法</th>
                  <th>预计使用期间</th>
                  <th>固定资产科目</th>
                  <th>累计折旧科目</th>
                  <th>原值</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <div id="assetsDataT">
	          <table class="layui-table" id="table" lay-filter="table">
		          <colgroup>
		            <col width="5%">
		            <col width="5%">
		            <col width="14%">
		            <col width="6%">
		            <col width="6%">
		            <col width="6%">
		            <col width="6%">
		            <col width="6%">
		            <col width="6%">
		            <col width="6%">
		            <col width="14%">
		            <col width="6%">
		            <col width="8%">
		            <col width="6%">
		          </colgroup>
	            <tbody id="assets-list">
	              <tr class="tc">
	                <td colspan="14" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
	              </tr>
	            </tbody>
	          </table>
					</div>
          <div class="layui-table-page">
            <div id="layui-table-page0"></div>
          </div>
        </div>
      </div>
    </div>

  </div>
</div>
<!-- End #content -->

<%@ include file="import-popup.jsp" %>
<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/plugins/zTree/js/jquery.ztree.core.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/zTree/js/jquery.ztree.exedit.min.js"></script>
<script src="${pageContext.request.contextPath }/js/zTreeData.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/assets/import.js?v=${timeStamp }"></script>
<!--[if lte IE 8]>
<script>
  if ($("#content").css('width') <= 1117) {
    $('.layui-table .opt').css('padding', 0);
    $('.opt>.layui-icon').css({
      'margin': '0 2px',
      'padding': '3px'
    });
  }
</script>
<![endif]-->
</body>

</html>
