<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>标准科目映射</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/subject-mapping.css">
</head>

<body class="must-import">
  <div class="crumbs must-item">
    <span class="crumbsTxt">标准科目映射</span>
  </div>
  <!-- 科目映射列表 -->
  <div class="layui-form table-box">
    <div class="layui-table-header">
      <table class="layui-table">
        <colgroup>
          <col width="20%">
          <col width="20%">
          <col width="25%">
          <col width="35%">
        </colgroup>
        <thead>
          <tr>
            <th>标准科目</th>
            <th>标准科目名称</th>
            <th>科目别称</th>
            <th>映射科目</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="layui-table-body layui-table-main">
      <table class="layui-table" id="mapping-table">
        <colgroup>
          <col width="20%">
          <col width="20%">
          <col width="25%">
          <col width="35%">
        </colgroup>
        <tbody id="mapping-body">
          <tr class="tc">
            <td colspan="4" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div class="must-item tc">
    <span class="layui-btn layui-btn-normal mr50" id="save-btn">保存</span>
    <span class="layui-btn layui-btn-primary" id="again-btn">自动映射</span>
  </div>

  <!-- 科目下拉弹窗 -->
  <div class="drop-down subList" id="subList" style="">
    <ul id="subLists"></ul>
    <p class="sub-addSub" id="sub-addSub">
      <span>新增科目</span>
    </p>
  </div>

  <!--  科目新增 -->
  <div id="addSub" class="dn layui-form">
    <div id="addSub_content">
      <div class="layui-form-item">
        <label class="layui-form-label">上级科目</label>
        <div class="layui-input-block">
          <select name="interest" lay-filter="subList" lay-search id="setSubData"></select>
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">科目编码</label>
        <div class="layui-input-block">
          <input type="text" name="title" required lay-verify="required" placeholder="请输入科目编码" autocomplete="off" class="layui-input"
            id="setSubCode">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">科目名称</label>
        <div class="layui-input-block">
          <input type="text" name="title" required lay-verify="required" placeholder="请输入科目名称  " autocomplete="off" class="layui-input"
            id="getSubName">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">余额方向</label>
        <div class="layui-input-block" id="setDirection">
          <input type="radio" name="sex" title="借" lay-filter="overMoney">
          <input type="radio" name="sex" title="贷" lay-filter="overMoney">
        </div>
      </div>
      <div class="layui-form-item addSub_num">
        <label class="layui-form-label">外币核算</label>
        <div class="layui-input-block">
          <input type="checkbox" name="switch" lay-skin="switch" lay-text="开启|关闭" lay-filter="foreignCurrency">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">数量核算</label>
        <div class="layui-input-block">
          <input type="checkbox" name="switch" lay-skin="switch" lay-text="开启|关闭" lay-filter="accounTing">
        </div>
      </div>
      <div class="layui-form-item setDisplay">
        <label class="layui-form-label">计量单位</label>
        <div class="layui-input-block">
          <select name="select" lay-filter="SurpluList" lay-search id="setSubSurplus"></select>
        </div>
      </div>
    </div>
  </div>

  <!-- ssType 标记小规模还是一般纳税人 0是一般纳税人, 1是小规模 -->
  <input type="hidden" id="ssType" value="${userDate.account.ssType }">


<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/addSub.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/init/subject-mapping.js?v=${timeStamp }"></script>
</body>

</html>
