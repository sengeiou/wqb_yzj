<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>科目对照</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/init-contrast.css">
</head>

<body class="p-abs">
<!-- 科目对照弹窗 -->
<div class=" init_contrast" id="">
  <div class="search">
    <div class="search-btn">
      <span class="layui-btn layui-btn-normal" id="search-btn">查询</span>
    </div>
    <div class="search-item fr">
      <input type="text" class="layui-input" placeholder="请输入编码或名称" name="keyWord" maxlength="15" id="keyWord">
    </div>
  </div>

  <div class="layui-form kmTable cl">
    <!-- 微企宝科目 -->
    <div class="fl space wqbKm">
      <div class="tabletit">微企宝科目</div>
      <div class="head">
        <table class="layui-table ">
          <colgroup>
            <col width="120">
            <col>
            <col width="126">
          </colgroup>
          <thead>
          <tr>
            <th>编码</th>
            <th>名称</th>
            <th>操作</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="tablebody">
        <table class="layui-table delTable">
          <colgroup>
            <col width="120">
            <col>
            <col width="126">
          </colgroup>
          <tbody id="sys-list"></tbody>
        </table>
      </div>
    </div>

    <!-- 未匹配科目 -->
    <div class="fl space leadKm">
      <div class="tabletit">未匹配科目<span class="count">（<span id="list-size">0</span>）</span></div>
      <div class="head">
        <table class="layui-table ">
          <colgroup>
            <col width="50">
            <col width="120">
            <col>
          </colgroup>
          <thead>
          <tr>
            <th>
              <input type="checkbox" name="allChoose" id="allChoose" lay-skin="primary" lay-filter="allChoose">
            </th>
            <th>编码</th>
            <th>名称</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="tablebody">
        <table class="layui-table  KmNotTable ">
          <colgroup>
            <col width="50">
            <col width="120">
            <col>
          </colgroup>
          <tbody id="excel-list"></tbody>
        </table>
      </div>
      <div class="sub-tips dn">标红:科目有余额,请慎重！！！</div>
    </div>
  </div>
</div>

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/init/subject-contrast.js?v=${timeStamp }"></script>
</body>

</html>


