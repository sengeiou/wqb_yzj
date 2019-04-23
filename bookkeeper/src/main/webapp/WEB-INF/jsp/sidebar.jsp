<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!-- Start #sidebar -->
  <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
  <div class="nav-floor">
    <div id="sidebar" class="layui-side">
      <div class="navigation" id="navList">
        <a href="${pageContext.request.contextPath }/invoice/importView#1" id="nav_tickets">一键理票</a>
        <%--<a class="tickets" href="${pageContext.request.contextPath }/invoice/importView#1">1.1 发票导入</a>
        <a class="tickets" href="${pageContext.request.contextPath }/bank/importView#2">1.2 银行导入</a>
        <a class="tickets" href="${pageContext.request.contextPath }/arch/importView#3">1.3 工资导入</a>
        <a class="tickets" href="${pageContext.request.contextPath }/documents/documentsView#4">1.4 单据录入</a>
        <a class="tickets" href="${pageContext.request.contextPath }/voucher/importView#5">1.5 凭证录入</a>
        <a class="tickets" href="${pageContext.request.contextPath }/assets/importView#6">1.6 固定资产</a>--%>
        <a href="${pageContext.request.contextPath }/voucher/createView#7" id="nav_certificate">一键凭证</a>
        <%--<a href="${pageContext.request.contextPath }/voucher/createView#8">2.1 生成凭证</a>
        <a href="${pageContext.request.contextPath }/accountBook/queryView#9">2.2 账簿查询</a>
        <a href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#10" id="nav_report">报表</a>
        <a href="https://ms.yun9.com/user/login" id="nav_taxReturn">报税</a>
        <a href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#10"target="_blank" id="nav_report">一键报税</a>--%>
        <a href="https://ms.yun9.com/user/login" target="_blank" id="nav_report">一键报税</a>
        <a id="nav_set">完成</a>
        <!--<a href="${pageContext.request.contextPath }/invoice/importView#12" id="nav_set">完成</a>-->
      </div>
      <div class="ticketsList">
        <ul>
          <li><a class="tickets" href="${pageContext.request.contextPath }/invoice/importView#1">进销项导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/bank/importView#2">银行导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/arch/importView#3">工资导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/documents/documentsView#4">费用导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/assets/importView#5">固定资产导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/voucher/importView#6">其他导入</a></li>
          <li><a class="tickets" href="${pageContext.request.contextPath }/voucher/createView#7">生成凭证</a></li>
          <li><a class="tickets zpcx" href="${pageContext.request.contextPath }/accountBook/queryView#8">账簿查询</a></li>
          <li><a class="tickets zpcx" href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#9">报表</a></li>
        </ul>
      </div>
      <%--<div id="nav_set">
       <a href="javascript:;" class="nav_set">设置</a>
      </div>--%>
    </div>
  </div>
  <!-- End #sidebar -->
