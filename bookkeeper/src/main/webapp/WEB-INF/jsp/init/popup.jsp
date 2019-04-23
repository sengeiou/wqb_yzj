<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Start 内容主体弹窗 -->

<!-- 试算平衡弹窗 -->
<div class="popup" id="trial-balance">
  <table class="layui-table">
    <colgroup>
      <col width="20%">
      <col width="30%">
      <col width="30%">
      <col width="20%">
    </colgroup>
    <thead>
    <tr>
      <th>项目</th>
      <th>期初借方合计</th>
      <th>期初贷方合计</th>
      <th>期初差额</th>
    </tr>
    </thead>
    <tbody></tbody>
  </table>
</div>

<!-- 试算平衡差异（未匹配科目）弹窗 -->
<div class="popup" id="TrDetail">
  <table class="layui-table">
    <colgroup>
      <col width="5%">
      <col width="20%">
      <col width="25%">
      <col width="20%">
      <col width="20%">
    </colgroup>
    <thead>
    <tr>
      <th>序号</th>
      <th>未匹配科目编码</th>
      <th>未匹配科目名称</th>
      <th>期末借方金额</th>
      <th>期末贷方金额</th>
    </tr>
    </thead>
    <tbody></tbody>
  </table>
</div>
<!-- End 内容主体弹窗 -->
