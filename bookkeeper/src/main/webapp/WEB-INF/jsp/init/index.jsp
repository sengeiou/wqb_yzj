<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>初始化</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/init-index.css">
  <style>
    .home-right1{
      text-align: right;
    }
    .home-left1{
      text-align: left;
    }
  </style>
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <!-- 首页 -->
  <div style="display: none" id="setHome">
    <div class="home">
      <div class="home-left cl">
        <div class="home-tab">
          <div class="home-tab-nav ">
            <a href="${pageContext.request.contextPath }/voucher/importView#5" class="home-tab-text home-voucher">新增凭证</a>
          </div>
          <div class="home-tab-nav ">
            <a href="${pageContext.request.contextPath }/accountBook/queryView?home=5#9" class="home-tab-text home-balance">余额表</a>
          </div>
          <div class="home-tab-nav ">
            <a href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView?home=1#10" class="home-tab-text home-liabilities">资产负债表</a>
          </div>
          <div class="home-tab-nav ">
            <a href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView?home=2#10" class="home-tab-text home-profits">利润表</a>
          </div>
          <div class="home-tab-nav ">
            <a href="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView?home=3#10" class="home-tab-text home-traffic">现金流量表</a>
          </div>
        </div>
        <div class="home-chart">
          <h3 class="hone-chart-title">利润变化趋势</h3>
          <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
          <div class="home-chart-table" id="homeChart-left">
            <div id="home-isNull">
              <img src="${pageContext.request.contextPath }/img/noData.png" class="home-nodata" alt="暂无数据">
              <span class="home-nodata-text">暂无数据</span>
            </div>
          </div>
        </div>
      </div>
      <div class="home-right cl">
        <div class="home-notice">
          <div class="home-title cl">
            <h3 class="title">系统公告</h3>
            <!--  <a href="javascript:;" class="more">更多</a> -->
          </div>
          <ul class="home-list">
            <li>
              <span>1.新手指引，《用户操作指南》</span>
              <a href="${pageContext.request.contextPath }/system/downGuide" style="color: #4381e6;">下载</a>
              <i class="date">2018-11-22</i>
            </li>
            <li>
              <span>1.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
            <li>
              <span>1.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
            <li>
              <span>1.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
            <li>
              <span>1.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
          </ul>
        </div>
        <div class="home-chart">
          <h3 class="hone-chart-title" id="chartTitle"></h3>
          <div id="homeChart-right" class="home-chart-round">
            <div id="home-isData">
              <img src="${pageContext.request.contextPath }/img/noData.png" class="home-nodata" alt="暂无数据">
              <span class="home-nodata-text">暂无数据</span>
            </div>
          </div>

          <div class="home-money" id="home-money"></div>
        </div>
      </div>
    </div>
  </div>

  <!-- 账套 -->
  <div style="display: none" id="setSubject">
    <div class="crumbs fixed-nav" id="navDOM1">
      <span class="crumbsTxt">账套初始化</span>
    </div>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>
    <div class="title_init">账套信息</div>
    <div class="account_info cl">
      <ul class="info_list">
        <li>
          <span class="name">公司名称 :</span>
          <span class="detail">${userDate.account.companyName }</span>
        </li>
        <li>
          <span class="name">所属类型 :</span>
          <span class="detail">
            <c:choose>
              <c:when test="${userDate.account.ssType eq '1'}">小规模记账</c:when>
              <c:otherwise>一般纳税人</c:otherwise>
            </c:choose>
          </span>
        </li>
        <li>
          <span class="name ">账套状态 :</span>
          <span class="detail accStatus" data-state="${userDate.account.statu }">
            <c:choose>
              <c:when test="${userDate.account.statu eq '0'}">新生成</c:when>
              <c:when test="${userDate.account.statu eq '1'}">启用</c:when>
              <c:otherwise>禁用</c:otherwise>
            </c:choose>
          </span>
        </li>
      </ul>
      <ul class="info_list">
        <li>
          <span class="name">会计准则 :</span>
          <span class="detail">${userDate.account.calculate }</span>
        </li>
        <li>
          <span class="name">启用时间 :</span>
          <span class="detail">
            <fmt:formatDate value="${userDate.account.period }" pattern="yyyy-MM-dd HH:mm:ss" />
          </span>
        </li>
      </ul>
    </div>
    <div class="title_init">科目初始化</div>
    <div class="file">
      <form method="post" enctype="multipart/form-data">
        <label>
          <span>请选择97格式.xls</span>
          <input type="file" class="in-file" name="uploadSubExcel" id="init-excel1" placeholder="格式：.xls">
        </label>
        <span class="layui-btn layui-btn-normal mr50" id="init-upload1">上传</span>
        <a class="layui-btn layui-btn-primary mr10" href="${pageContext.request.contextPath }/subexcel/downSubExcel">下载模板</a>
        <span class="layui-btn layui-btn-primary mr10 dn" id="contrast-btn">科目对照</span>
        <span class="layui-btn layui-btn-primary mr10 dn" id="mapping-btn">科目映射</span>
        <span class="layui-btn layui-btn-primary trial dn" id="trial-btn">试算平衡</span>
        <div class="progress">
          <span class="percentage"></span>
          <span class="text p-rel">0%</span>
        </div>
      </form>
    </div>
    <!-- 科目对照结果 -->
    <div class="matchRes dn" id="matchRes">
      <p class="mb15">期初科目数：
        <span></span>
      </p>
      <p class="mb15">匹配科目数：
        <span></span>
      </p>
      <p class="mb15">期初借方金额：
        <span></span>
      </p>
      <p class="mb15">期初贷方金额：
        <span></span>
      </p>
    </div>
    <div class="title_init">固定资产初始化</div>
    <div class="file">
      <form method="post" enctype="multipart/form-data">
        <label>
          <span>请选择97格式.xls</span>
          <input type="file" class="in-file" name="uploadAssets" id="init-excel2" placeholder="格式：.xls">
        </label>
        <span class="layui-btn layui-btn-normal mr50" id="init-upload2">上传</span>
        <a class="layui-btn layui-btn-primary" href="${pageContext.request.contextPath }/assets/downExcel">下载模板</a>
        <div class="progress">
          <span class="percentage"></span>
          <span class="text p-rel">0%</span>
        </div>
      </form>
    </div>
    <div class="title_init">数量金额初始化（总账）</div>
    <div class="file">
      <form method="post" enctype="multipart/form-data">
        <label>
          <span>请选择97格式.xls</span>
          <input type="file" class="in-file" name="uploadStock" id="init-excel3" placeholder="格式：.xls">
        </label>
        <span class="layui-btn layui-btn-normal mr50" id="init-upload3">上传</span>
        <a class=" layui-btn layui-btn-primary" href="${pageContext.request.contextPath }/stock/downExcel">下载模板</a>
        <div class="progress">
          <span class="percentage"></span>
          <span class="text p-rel">0%</span>
        </div>
      </form>
    </div>
    <div class="bottomBtn">
      <span class="layui-btn layui-btn-normal mr50" id="init-btn">初始化</span>
      <span class="layui-btn layui-btn-primary" id="skip-btn">跳过初始化</span>
      <!-- <span class="layui-btn layui-btn-normal" id="reset-btn">重新初始化</span> -->
    </div>
  </div>
</div>
<!-- End #content -->

<%@ include file="popup.jsp" %>

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/plugins/echarts.common.min.js"></script>
<script src="${pageContext.request.contextPath }/js/init/index.js?v=${timeStamp }"></script>
</body>

</html>
