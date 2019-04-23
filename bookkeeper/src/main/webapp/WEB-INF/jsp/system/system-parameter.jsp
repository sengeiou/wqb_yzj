<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>系统参数</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/system-parameter.css?v=${timeStamp }">
</head>

<body>
<!-- Start #content -->
<div class="container layui-form" id="content">
  <div class="param-item init-param">
    <h4>基础参数</h4>
    <ul>
      <li class="form-item">
        <label class="form-text" for="companyName"><span class="asterisk">*</span>公司名称</label>
        <input type="text" class="layui-input" id="companyName" value="${userDate.account.companyName }" placeholder="请输入公司全称" maxlength="100">
      </li>
      <li class="form-item">
        <label class="form-text"><span class="asterisk">*</span>本位币</label>
        <select lay-verify="required">
          <option value="RMB">CNY 人民币</option>
        </select>
      </li>
      <li class="form-item">
        <label class="form-text" for="period"><span class="asterisk">*</span>启用期间</label>
        <input type="text" class="layui-input layui-disabled" id="period" value='<fmt:formatDate value="${userDate.account.period }" pattern="yyyy-MM-dd" />' autocomplete="off" readonly disabled>
      </li>
      <li class="form-item">
        <label class="form-text"><span class="asterisk">*</span>会计准则</label>
        <input type="text" class="layui-input layui-disabled" value="${userDate.account.calculate }" readonly disabled>
        <%-- <select id="accstandards" lay-verify="required">
          <option value="">新会计准则</option>
          <option value="">2013小企业会计准则</option>
          <option value="">2007企业会计准则</option>
        </select> --%>
      </li>
    </ul>
  </div>
  <div class="param-item subject-param">
    <h4>科目参数</h4>
    <ul>
      <li class="form-item">
        <label class="form-text"><span class="asterisk">*</span>科目级次</label>
        <select id="subject-level" lay-verify="required" lay-filter="subject-level">
          <option value="3">3</option>
          <option value="4" selected>4</option>
          <option value="5">5</option>
        </select>
        <span class="tips"><i class="layui-layer-ico layui-layer-ico0"></i>科目级次和长度调大后，不能再调小（即：不能再恢复到调整前的级次和长度），请谨慎操作！</span>
      </li>
      <li class="form-item code-length" id="code-length">
        <label class="form-text"><span class="asterisk">*</span>编码长度</label>
        <span>
          <input type="number" class="layui-input sub-text" value="4" autocomplete="off" min="3" max="5">
        </span>
        <span>&nbsp;-&nbsp;
          <input type="number" class="layui-input sub-text" value="2" autocomplete="off" min="2" max="5">
        </span>
        <span>&nbsp;-&nbsp;
          <input type="number" class="layui-input sub-text" value="2" autocomplete="off" min="2" max="5">
        </span>
        <span>&nbsp;-&nbsp;
          <input type="number" class="layui-input sub-text" value="2" autocomplete="off" min="2" max="5">
        </span>
      </li>
    </ul>
  </div>
  <div class="bottom-btn">
    <span class="layui-btn layui-btn-normal" id="save-btn">保存</span>
  </div>
</div>
<!-- End #content -->

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/system/system-parameter.js?v=${timeStamp }"></script>
</body>

</html>
