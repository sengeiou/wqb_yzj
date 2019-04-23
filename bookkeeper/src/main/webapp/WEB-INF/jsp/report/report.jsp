<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
    <%@ include file="../head.jspf" %>
    <title>报表</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
    <style>
    	#navDOM1{
    		display: none !important;
    	}
        .fixed-search {
            padding: 20px 0;
        }
        #assetsDataT .layui-table,
        #profitDataT .layui-table {
            margin-bottom: 0;
        }
        #report .layui-table th {
            text-align: center;
            color: #333;
            height: 40px;
        }

        #report td {
            padding: 8px 5px;
            text-align: right;
        }

        #report .tr {
            text-align: right !important;
        }

        #report .tl {
            text-align: left !important;
        }

        #report .tc {
            text-align: center !important;
        }

        .modify {
            display: none;
        }

        .editIcon {
            padding: 5px;
            cursor: pointer;
        }
        #profit .tl {
            padding-left: 5em;
        }
        .ti{
            text-indent: 2em;
        }
        .ti1 {
            text-indent: 1em;
        }
        .ti4 {
            text-indent: 4em;
        }
        .ti8 {
            text-indent: 8em;
        }
    </style>
</head>

<body>
<!-- Start #content -->
<!-- 右侧 内容包含区 -->
<div class="layui-body" id="report">
    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
        <ul class="layui-tab-title fixed-nav" id="navDOM1">
            <li class="liabilities layui-this">资产负债表</li>
            <li class="profits">利润表</li>
            <li class="traffic">现金流量表</li>
        </ul>
        <span class="btn-toolbar fixed-icon">
          <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
        </span>
        <div class="layui-tab-content">
            <!--资产负债表-->
            <div class="liabilitiesTable layui-tab-item layui-show">
                <div class="search fixed-search" id="search1">
                    <div class="layui-btn layui-btn-normal print_btn" data-index="0">打印本页</div>
                    <div class="layui-btn layui-btn-normal export-btn" data-index="0">导出</div>
                </div>
                <div class="fixed-head" id="head1">
                    <table class="layui-table">
                        <colgroup>
                            <col width="4%">
                            <col width="20%">
                            <col width="13%">
                            <col width="13%">
                            <col width="4%">
                            <col width="20%">
                            <col width="13%">
                            <col width="13%">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>行次</th>
                                <th>资产</th>
                                <th>期末余额</th>
                                <th>年初余额</th>
                                <th>行次</th>
                                <th>负债和所有者权益（或股东权益）</th>
                                <th>期末余额</th>
                                <th>年初余额</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div id="assetsDataT">
	                <table class="layui-table">
	                    <colgroup>
	                        <col width="4%">
	                        <col width="20%">
	                        <col width="13%">
	                        <col width="13%">
	                        <col width="4%">
	                        <col width="20%">
	                        <col width="13%">
	                        <col width="13%">
	                    </colgroup>
	                    <tbody id="assets"></tbody>
	                </table>
	            </div>
            </div>

            <!--利润表-->
            <div class="profitsTable layui-tab-item">
                <div class="search fixed-search" id="search2">
                    <div class="layui-btn layui-btn-normal print_btn" data-index="1">打印本页</div>
                    <div class="layui-btn layui-btn-normal export-btn" data-index="1">导出</div>
                </div>
                <div class="fixed-head" id="head2">
                    <table class="layui-table">
                        <colgroup>
                            <col width="6%">
                            <col width="49%">
                            <col width="15%">
                            <col width="15%">
                            <col width="15%">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>行次</th>
                                <th>项目</th>
                                <th>本年金额</th>
                                <th>本季金额</th>
                                <th>本期金额</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div id="profitDataT">
	                <table class="layui-table">
	                    <colgroup>
	                        <col width="6%">
                            <col width="49%">
                            <col width="15%">
                            <col width="15%">
                            <col width="15%">
	                    </colgroup>
	                    <tbody id="profit"></tbody>
	                </table>
	            </div>
            </div>

             <!-- 现金流量表-->
             <div class="trafficTable layui-tab-item ">
                <div class="search fixed-search" id="search3">
                    <!-- <div class="layui-btn layui-btn-normal print_btn" data-index="2">打印本页</div> -->
                    <!-- <div class="layui-btn layui-btn-normal export-btn" data-index="2">导出</div> -->
                </div>
                <div class="fixed-head" id="head3">
                    <table class="layui-table">
                        <colgroup>
                            <col width="50%">
                            <col width="10%">
                            <col width="20%">
                            <col width="20%">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>项目</th>
                                <th>行次</th>
                                <th>本月金额</th>
                                <th>本年累计金额</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <table class="layui-table">
                    <colgroup>
                        <col width="50%">
                        <col width="10%">
                        <col width="20%">
                        <col width="20%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>项目</th>
                            <th>行次</th>
                            <th>本月金额</th>
                            <th>本年累计金额</th>
                        </tr>
                    </thead>
                    <tbody id="cash">
                        <tr>
                            <td>一、经营活动产生的现金流量：</td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>销售产成品、商品、提供劳务收到的现金</td>
                            <td class="tc">1</td>
                            <td class="tr">5485125.25</td>
                            <td class="tr">2.2525</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- 右侧 内容包含区 -->
<!-- End #content -->

<!-- 打印 -->
<div id="print-popup"></div>

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/report/report.js?v=${timeStamp }"></script>
</body>

</html>
