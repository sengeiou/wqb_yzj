<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>生成凭证</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/voucher-cr.css">
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <div class="layui-tab layui-tab-brief" lay-filter="tab">
    <ul class="layui-tab-title crumbs" id="navDOM1">
      <li class="layui-this" lay-id="0">生成凭证</li>
      <li lay-id="1">修正凭证</li>
    </ul>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>

    <!-- tab内容展示 -->
    <div class="layui-tab-content">
      <!-- 生成凭证 -->
      <div class="layui-tab-item layui-show">
        <!-- 流程图 -->
        <div class="search tc" id="search1">
          <div class="search-oneKey dib">
            <div class="layui-btn layui-btn-normal layui-btn-disabled" id="voucher-btn">一键生成凭证</div>
            <img src="../img/arrow.png" alt="箭头">
            <div class="layui-btn layui-btn-normal layui-btn-disabled" id="carry-btn">一键计提</div>
            <img src="../img/arrow.png" alt="箭头">
            <div class="layui-btn layui-btn-normal layui-btn-disabled" id="review-btn">一键结转</div>
            <img src="../img/arrow.png" alt="箭头">
            <div class="layui-btn layui-btn-normal layui-btn-disabled" id="voucher-audit">一键检查</div>
            <img src="../img/arrow.png" alt="箭头">
            <div class="layui-btn layui-btn-normal layui-btn-disabled" id="settle-accounts">一键结账</div>
          </div>
          <div class="search-btn">
             <%-- <div class="layui-btn layui-btn-normal export_btn">导出</div> --%>
            <div class="layui-btn layui-btn-normal print_btn" id="print-btn">打印</div>
          </div>
          <%-- <div class="layui-btn layui-btn-normal search_btn fr" id="search-btn">查询</div>
          <div class="search-item fr">
            <label for="keyWord">
              <input type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord" maxlength="15" id="keyWord">
            </label>
          </div> --%>
        </div>

        <!-- 凭证列表 -->
        <div class="layui-form table-box">
          <div class="layui-table-header">
            <table class="layui-table">
              <colgroup>
                <col width="80">
                <col width="8%">
                <col width="10%">
                <col width="20%">
                <col width="100">
                <col width="100">
                <col width="80">
                <col width="100">
              </colgroup>
              <thead>
                <tr>
                  <th>日期</th>
                  <th>凭证字号</th>
                  <th>摘要</th>
                  <th>科目</th>
                  <th>借方金额</th>
                  <th>贷方金额</th>
                  <th>结转状态</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <div class="layui-table-body layui-table-main">
            <table class="layui-table voucher-list list1" id="create-table">
              <colgroup>
                <col width="80">
                <col width="8%">
                <col width="10%">
                <col width="20%">
                <col width="100">
                <col width="100">
                <col width="80">
                <col width="100">
              </colgroup>
              <tbody id="voucher-list" data-length="0">
                <tr class="tc">
                  <td colspan="8" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
              </tbody>
            </table>
          </div>
          <span class="layui-btn layui-btn-danger p-abs isShow" style="background: #C9C9C9;cursor: auto;z-index: 2;">一键删除</span>
          <span class="layui-btn layui-btn-danger p-abs" id="remove-all" style="display: none;">一键删除</span>
          <div class="layui-table-page">
            <div id="layui-table-page1"></div>
          </div>
        </div>
        <div class="layui-collapse dn" id="voucher" lay-accordion="">
          <div class="layui-colla-item">
            <h2 class="layui-colla-title">凭证检查
              <p>共有4项类目，其中<label class="itemMag1">0</label>项类目有风险<span></span>
                <img class="salaryImg1" src="../img/risk-warn1.png">
                <img class="salaryImg2" src="../img/risk-warn2.png">
                <img class="salaryImg3" src="../img/risk-warn3.png">
              </p>
            </h2>
            <div class="layui-colla-content layui-show">
              <ul>
                <li class="salary">工资表、固定资产折旧有无计提
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li>成本有无结转
                  <img src="../img/risk-warn3.png">
                  <span>成本有无结转 检查通过</span>
                </li>
                <li>凭证是否断号
                  <img src="../img/risk-warn3.png">
                  <span>凭证是否断号 检查通过</span>
                </li>
                <%--<li>成本结转是否合理
                  <img src="../img/risk-warn3.png">
                  <span>成本结转是否合理 检查通过</span>
                </li>--%>
              </ul>
            </div>
          </div>
          <div class="layui-colla-item">
            <h2 class="layui-colla-title">余额检查
              <p>共有5项类目，其中<label class="itemMag2">0</label>项类目有风险<span></span>
                <img class="inventoryImg1" src="../img/risk-warn1.png">
                <img class="inventoryImg2" src="../img/risk-warn2.png">
                <img class="inventoryImg3" src="../img/risk-warn3.png">
              </p>
            </h2>
            <div class="layui-colla-content">
              <ul>
                <li class="inventory">资金有无负数
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li class="bank">银行存款有无负数
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li class="kcCommodity">存货有无负数
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <%--<li>往来科目有无长期挂账，超过一年没有收或付的要提醒
                  <img src="../img/risk-warn3.png">
                  <span>往来科目有无长期挂账，超过一年没有收或付的要提醒 检查通过</span>
                </li>
                <li>往来科目合并与对冲，应收、预收、应付、预付二级科目有没有合并与对冲的情况。
                  <img src="../img/risk-warn3.png">
                  <span>往来科目合并与对冲，应收、预收、应付、预付二级科目有没有合并与对冲的情况  检查通过</span>
                </li>--%>
              </ul>
            </div>
          </div>
          <div class="layui-colla-item">
            <h2 class="layui-colla-title">报表检查
              <p>共有3项类目，其中<label class="itemMag3">0</label>项类目有风险<span></span>
                <img class="warnImg1" src="../img/risk-warn1.png">
                <img class="warnImg2" src="../img/risk-warn2.png">
                <img class="warnImg3" src="../img/risk-warn3.png">
              </p>
            </h2>
            <div class="layui-colla-content">
              <ul>
                <li class="balanceSheet">资产负债表是否平衡
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li class="incomeStatement">成本倒挂
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li class="insolvency">资不抵债
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
              </ul>
            </div>
          </div>
          <div class="layui-colla-item">
            <h2 class="layui-colla-title">其它指标
              <p>共有8项类目，其中<label class="itemMag4">0</label>项类目有风险<span></span>
                <img class="itemImg1" src="../img/risk-warn1.png">
                <img class="itemImg2" src="../img/risk-warn2.png">
                <img class="itemImg3" src="../img/risk-warn3.png">
              </p>
            </h2>
            <div class="layui-colla-content">
              <ul>
                <li class="weal">福利费扣除标准
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li>业务招待费
                  <img src="../img/risk-warn3.png">
                  <span>业务招待费 检查通过！</span>
                </li>
                <li class="zeroDeclaration">连续零申报
                  <img class="warn3" src="../img/risk-warn3.png">
                  <img class="warn2" src="../img/risk-warn2.png">
                  <img class="warn1" src="../img/risk-warn1.png">
                  <span></span>
                </li>
                <li>资产负债率
                  <img src="../img/risk-warn3.png">
                  <span>资产负债率 检查通过！</span>
                </li>
                <li>所得税税负率
                  <img src="../img/risk-warn3.png">
                  <span>所得税税负率 检查通过！</span>
                </li>
                <li>增值税税负率
                  <img src="../img/risk-warn3.png">
                  <span>增值税税负率 检查通过！</span>
                </li>
                <li>毛利率
                  <img src="../img/risk-warn3.png">
                  <span>毛利率 检查通过！</span>
                </li>
                <li>税负波动
                  <img src="../img/risk-warn3.png">
                  <span>税负波动 检查通过！</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <!-- 修正凭证 -->
      <div class="layui-tab-item">
        <%--  搜索 -->
        <!-- <div class="search">
          <div class="search-btn">
             <div class="layui-btn layui-btn-normal export_btn">导出</div>
          </div>
          <div class="search-item">
            <label for="keyWord2">
              <input type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord2" maxlength="15" id="keyWord2">
            </label>
          </div>
          <div class="layui-btn layui-btn-normal search_btn" id="search-btn2">查询</div>
        </div> --%>

        <!-- 凭证列表 -->
        <div class="layui-form table-box">
          <div class="layui-table-header">
            <table class="layui-table">
              <colgroup>
                <col width="80">
                <col width="8%">
                <col width="10%">
                <col width="20%">
                <col width="100">
                <col width="100">
                <col width="80">
                <col width="100">
              </colgroup>
              <thead>
                <tr>
                  <!-- <th><input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose" id="all-choose"></th> -->
                  <th>日期</th>
                  <th>凭证字号</th>
                  <th>摘要</th>
                  <th>科目</th>
                  <th>借方金额</th>
                  <th>贷方金额</th>
                  <th>结转标识</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <div class="layui-table-body layui-table-main noSearch">
            <table class="layui-table voucher-list" id="revise-table">
              <colgroup>
                <col width="80">
                <col width="8%">
                <col width="10%">
                <col width="20%">
                <col width="100">
                <col width="100">
                <col width="80">
                <col width="100">
              </colgroup>
              <tbody id="voucher-list2" data-length="0">
                <tr class="tc">
                  <td colspan="8" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="layui-table-page">
            <div id="layui-table-page2"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
  <%--上传附件-->
  <!--<div class="uploadBody">
    <div class="uploadA">
      <div class="uploadA_top">上传附件
        <span class="uploadA_close layui-icon">&#x1006</span>
      </div>
      <div class="uploadA_C">
        <form id="uploading1" style="margin: 32px 0px 0 35px;" class="uploading" method="post" enctype="multipart/form-data">
          <label style="float: left;display: block;width: 220px;">
            <a href="javascript:void(0);">
              <input type="hidden" name="vouchID" value=""/>
              <span style="width:59%" class="span">请选择png/jpg图片格式</span>
              <input class="voucherFile" id="file" type="file" name="attachFile" placeholder="请选择png/jpgtu图片格式"/>
            </a>
          </label>
          <span class="fujian layui-btn layui-btn-normal mr50">上传附件</span>
       </form>
      </div>
      <div class="uploadA_B">

      </div>
    </div>
  </div>--%>
<!-- End #content -->

<!--凭证字号弹出框-->
<div class="dn cusSel" id="getHistoryBill-list">
  <div class="content">
    <!-- 固定头部 -->
    <div class="head ">
      <table class="layui-table ">
        <thead>
          <tr id="isInTheOutput"></tr>
        </thead>
      </table>
    </div>
    <div class="tablebody2 cusTable">
      <table class="layui-table">
        <tbody id="getHistoryBillList"></tbody>
      </table>
    </div>
  </div>
</div>

<!-- 选择凭证打印范围弹窗 -->
<div class="popup layui-form print-popup" id="print-popup">
  <div class="popup-item">
    <span class="popup-text">打印选项</span>
    <label class="dib"><input type="radio" name="printCountType" lay-filter="printCountType" value="0" title="打印全部凭证" checked></label>
    <label class="dib"><input type="radio" name="printCountType" lay-filter="printCountType" value="1" title="选择打印范围"></label>
  </div>
  <div class="popup-item cl">
    <label class="popup-text">凭证字号范围</label>
    <input type="text" class="layui-input dib layui-disabled" id="print-begin" placeholder="请输入开始凭证字号" maxlength="16" autocomplete="off" disabled>
    <span class="lianjie">一</span>
    <input type="text" class="layui-input dib fr layui-disabled" id="print-end" placeholder="请输入结束凭证字号" maxlength="16" autocomplete="off" disabled>
  </div>
</div>

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/voucher/create.js?v=${timeStamp }"></script>
</body>

</html>
