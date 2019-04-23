<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>理票——进销项发票导入</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/invoice-im.css">
  <style>
    .layui-body{
      min-width: 1200px;
    }
    .fixed-head{
    	display: block;
    }
    #outputList{
    	height: auto;
	    overflow: auto;
	    margin-top: 60px;
    }
    #outputList .layui-table{
    	margin-top: 0px !important;
    	table-layout: fixed;
    }
    #head2 {
		  margin-top: -37px;
		}
		.subjectsF{
			display: none;
			padding: 0 20px;
		}
		.subjectsF table{
			margin-top: 15px;
		}
		.subjectsF table tr .caozuo span{
			font-size: 30px;
			cursor: pointer;
			display: block;
			float: left;
			margin-left: 15px;
		}
		#subjectsF .reminder{
			display: none;
			color: #ee3c3c;
			font-size: 12px;
			width: 95%;
			text-align: center;
			position: absolute;
		  top: -2px;
		}
		.zhezhao{
			display: none;
			width: 100%;
			height: 100%;
			background-color: rgb(0, 0, 0);
			opacity: 0.3;
			position: absolute;
			top: 0;
			z-index: 999;
		}
		#subjectsBody table{
			margin-top: 0px;
		}
		.#subjectsBody div{
			text-align: left;
		}
		#subjectsBody .fapiao{
			text-align: left;
    	padding-left: 12px;
    	background: #eaeaea;
		}
		#subjectsBody tr td{
			padding: 0;
		}
		#subjectsBody .subCodeDiv{
			max-width: 500px;
			width: 100%;
			padding-left: 0;
			line-height: 40px;
			text-align: left;
			cursor: pointer;
			overflow: hidden;
	    white-space: nowrap;
	    text-overflow: ellipsis;
		}
		#subjectsBody .subjectTable .subCodeInput{
			width: calc(100% - 1px);
		}
		#subjectsBody .moneyTable .subCodeInput{
			border: none;
			text-align: center;
		}
		#subBody tr{
			height: 40px;
		}
		#subBody tr:nth-last-child td(1){
			border-bottom: none;
		}
		#subBody tr td:nth-last-child(1){
			border-right: none;
		}
		#subBody tr td:nth-child(1){
			border-left: none;
		}
		.outputListB{
			margin-top: 0px !important;
			height: 404px !important;
		}
		.layui-table, .layui-table-view{
			margin: 0;
		}
		.layui-layer-page .layui-layer-content{
			overflow: hidden;
		}
		.layui-layer-iframe .layui-layer-btn, .layui-layer-page .layui-layer-btn{
			/*margin-top: -40px;*/
		}
		.delDate{
			font-size: 30px;
			cursor: pointer;
		}
		#subjectsBody .layui-table .subCodeInput{
			width: calc(100% - 1px);
		}
		#subjectsBody .layui-table .subCodeInput,
		#subjectsBody .layui-table .keycodeComName,
		#subjectsBody .layui-table .keycodeSpec,
		#subjectsBody .layui-table .keycodeNnumber{
			display: none;
	    position: absolute;
	    height: 99%;
	    width: 100%;
	    left: 0px;
	    top: 0;
	    border: 1px solid #afcaff;
	    z-index: 10;
		}
		.subCodeDiv{
			height: 38px;
			margin-left: -1px;
		}
		.subList{
			display: none;
			position: absolute;
			width: 310px;
			height: 212px;
			overflow-y: auto;
			overflow-x: hidden;
			background-color: #fff;
			z-index: 9999999999;
		}
		.subList>ul {
		    height: 180px;
		    overflow-y: scroll;
		    border: 1px solid #afcaff;
		    border-top: 0;
		}
		.subList>ul>li{
			cursor: pointer;
		}
		.subList>ul>li:hover {
			background: #e6eefc;
		}
		.sub-addSub>span {
		    display: inline-block;
		    background-color: #4d91fe;
		    padding: 0 15px;
		}
		.sub-addSub {
		    margin-top: -1px;
		    height: 30px;
		    line-height: 30px;
		    border: 1px solid #4d91fe;
		    color: #fff;
		    text-align: center;
		    font-size: 14px;
		    cursor: pointer;
		}
		.subList .sub-title {
		    padding: 6px 0;
		    margin: 0 4px;
		    border-bottom: 1px solid #a8a8a8;
		    font-size: 12px;
		    color: #333;
		}
		td .compile{
			display: none;
			position: absolute;
	    right: 1px;
	    color: #5274ba;
	    font-size: 12px;
	    line-height: 22px;
	    cursor: pointer;
	    top: 1px;
		}
		.codeComName,
		.codeSpec,
		.subCodeDiv,
		.codeNnumber{
			min-height: 37px;
			line-height: 38px;
			position: absolute;
	    top: 0px;
	    left: 0px;
	    width: 100%;
	    z-index: 9;
	    overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
		}
		.codeTr .delDate{
			font-size: 30px;
    	color: #828282;
		}
		.layui-table .opt p{
			margin-right: 0px;
		}
		.layui-table .opt p span{
			padding: 5px;
		}
  </style>
</head>

<body>

<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content">
  <div class="layui-tab layui-tab-brief" lay-filter="tab">
    <ul class="layui-tab-title crumbs fixed-nav" id="navDOM1">
      <li class="layui-this">进项发票</li>
      <li>销项发票</li>
    </ul>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>

    <!--  搜索 -->
    <div class="search fixed-search" style="padding-top: 13px;">
      <div class="search-item">
        <label for="keyWord">
          <input type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord" maxlength="15" id="keyWord">
        </label>
      </div>
      <div class="search-item">
        <input type="text" class="layui-input wDate" id="beginTime" name="beginTime" autocomplete="off" placeholder="认证日期开始日期">
        <i class="layui-icon">&#xe637;</i>
      </div>
      <div class="search-item search-before">
        <input type="text" class="layui-input wDate" id="endTime" name="endTime" autocomplete="off" placeholder="认证日期结束日期">
        <i class="layui-icon">&#xe637;</i>
      </div>
      <div class="layui-btn layui-btn-normal" id="search-btn">查询</div>
      <div class="search-btn">
        <div class="layui-btn layui-btn-normal" id="popup-btn">导入</div>
        <!-- 取消批量删除功能 -->
         <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
      </div>
    </div>

    <!-- tab内容展示 -->
    <div class="layui-tab-content">
      <!-- 进项发票 -->
      <div class="layui-tab-item layui-show">
        <div class="layui-form table-box">
          <div class="fixed-head" id="head1">
            <table class="layui-table table1" lay-filter="income">
              <colgroup>
                <col width="3%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="16%">
                <col width="14%">
                <col width="16%">
                <col width="8%">
                <col width="6%">
                <col width="6%">
                <col width="6%">
                <col width="5%">
              </colgroup>
              <thead>
                <tr>
                  <th>
                    <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                  </th>
                  <th>发票代码</th>
                  <th>发票号码</th>
                  <th>开票日期</th>
                  <th>科目名称</th>
                  <th>销方税号</th>
                  <th>销方名称</th>
                  <th>金额</th>
                  <th>税额</th>
                  <th>认证日期</th>
                  <th>发票类型</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <table class="layui-table table1" id="income-table" lay-filter="income">
            <colgroup>
              <col width="3%">
	            <col width="6%">
	            <col width="6%">
	            <col width="6%">
	            <col width="16%">
	            <col width="14%">
	            <col width="16%">
	            <col width="8%">
	            <col width="6%">
	            <col width="6%">
	            <col width="6%">
	            <col width="5%">
            </colgroup>
            <thead>
              <tr>
                <th>
                  <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                </th>
                <th>发票代码</th>
	              <th>发票号码</th>
	              <th>开票日期</th>
	              <th>科目名称</th>
	              <th>销方税号</th>
	              <th>销方名称</th>
	              <th>金额</th>
	              <th>税额</th>
	              <th>认证日期</th>
	              <th>发票类型</th>
	              <th>操作</th>
              </tr>
            </thead>
            <tbody id="income-list">
              <tr class="tc">
                <td colspan="11" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
              </tr>
            </tbody>
          </table>
          <div class="layui-table-page">
            <div id="layui-table-page1"></div>
          </div>
        </div>
      </div>

      <!-- 销项发票 -->
      <div class="layui-tab-item">
        <div class="layui-form table-box">
          <div class="fixed-head" id="head2">
            <table class="layui-table table2" lay-filter="output">
              <colgroup>
                <col width="5%">
	              <col width="6%">
	              <col width="6%">
	              <col width="14%">
	              <col width="20%">
	              <col width="8%">
	              <col width="6%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
              </colgroup>
              <thead>
                <tr>
                  <th>
                    <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                  </th>
                  <th>发票代码</th>
                  <th>发票号码</th>
                  <th>购方企业名称</th>
                  <th>科目名称</th>
                  <th>商品名称</th>
                  <th>规格</th>
                  <th>单位</th>
                  <th>数量</th>
                  <th>单价</th>
                  <th>金额</th>
                  <th>税率</th>
                  <th>税额</th>
                  <th>操作</th>
                </tr>
              </thead>
            </table>
          </div>
          <div id="outputList">
          	<table class="layui-table table2" id="output-table" lay-filter="output">
	            <colgroup>
	              <col width="5%">
	              <col width="6%">
	              <col width="6%">
	              <col width="14%">
	              <col width="20%">
	              <col width="8%">
	              <col width="6%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	              <col width="5%">
	            </colgroup>
	            <tbody id="output-list">
	              <tr class="tc">
	                <td colspan="11" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
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
    <script type="text/html" id="table-tool">
      <span class="del" lay-event="del"></span>
      <!-- <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a> -->
    </script>
  </div>

  <div id="subjectsJX" class="subjectsF">
		<ul  style="background:#FCFCFC; color:#bebebe">
			    <li>1 请确认发票里面的每一条数据是否都映射好了科目,如果为空或者不正确请手动编辑 </li>
			    <li>2 科目映射完成之后请点击确认进行保存</li>
		</ul>

  	<div class="outputListH">
  		<!--<div style="height: 14px"></div>
  		<div>
	  		<div style="float: left;">
					<span id="tishi">提示映射数量${word }</span>
				</div>
				<div style="float: right;">
					<button type="button" onclick="dianji()">自动映射</button>
				</div>
		  </div>-->
  		<table class="layui-table">
				<colgroup>
					<col width="15%">
			    	<col width="9%">
				  	<col width="10%">
				  	<col width="22%">
				  	<col width="7%">
				  	<col width="7%">
				  	<col width="7%">
				  	<col width="4%">
				  	<col width="6%">
				  	<col width="7%">
				  	<col width="5%">
			  </colgroup>
				<thead>
					<tr>
						<th class="companyName">公司名称</th>
						<th>货物或应税劳务、服务名称</th>
						<th>规格型号</th>
						<th>映射科目</th>
						<th>价格</th>
						<th>数量</th>
						<th>金额</th>
						<th>单位</th>
						<th>税率</th>
						<th>税额</th>
						<th>操作</th>
					</tr>
				</thead>
			</table>
  	</div>
  	<div class="outputListB" id="outputList">
  		<table class="layui-table">
				<colgroup>
					<col width="18%">
			    <col width="13%">
				  <col width="7%">
				  <col width="20%">
				  <col width="7%">
				  <col width="7%">
				  <col width="7%">
				  <col width="6%">
				  <col width="3%">
				  <col width="7%">
				  <col width="5%">
			  </colgroup>
				<thead>
				<tbody id="subjectsBody">

				</tbody>
			</table>
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
    <span class="layui-btn layui-btn-normal upload-btn" id="upload-btn">上传</span>
    <div class="progress">
      <span class="percentage"></span>
      <span class="text p-rel">0%</span>
    </div>
  </form>
  <!-- <p class="upload-tips"><span class="red">提示：</span>1.请使用提供的模版格式，认证月份须和会计期间一致。<br/>2.请先导入进项发票,然后导入销项发票。如果没有可以跳过</p> -->
	 <p class="upload-tips">
		 <span class="red">提示：</span><br/>
		 <span style="color:#1e9fff"> 1.请使用提供的模版格式，认证月份须和会计期间一致。</span><br/>
		 <span style="color:#1e9fff">2.请先导入进项发票,然后导入销项发票。如果没有可以跳过。</span>
	 </p>

  <div>
    <a class="layui-btn layui-btn-primary mr50" href="${pageContext.request.contextPath }/invoice/downJxExcel">下载进项模板</a>
    <a class="layui-btn layui-btn-primary" href="${pageContext.request.contextPath }/invoice/downXxExcel">下载销项模板</a>
  </div>
</div>

<%@ include file="../voucher/import-popup.jsp" %>
<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/invoice/import.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/addSub.js?v=${timeStamp }"></script>

</body>

</html>
