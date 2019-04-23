<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>凭证编辑/查看</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/voucherInput.css">
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body">
  <%-- <div class="layui-tab layui-tab-brief" lay-filter="tab"> --%>
    <span class="btn-toolbar fixed-icon">
    	<i class="layui-icon cu" id="i-calculator" title="计算器">计算器</i>
      <!-- <i class="layui-icon cu" id="i-refresh">刷新</i> -->
      <i class="layui-icon cu" id="i-hints">快捷键</i>
      <i class="layui-icon cu" id="i-Speed">平衡</i>
    </span>
    <%-- tab内容展示 -->
    <!-- <div class="layui-tab-content"> -->
      <!-- 凭证录入 -->
      <!-- <div class="layui-tab-item layui-show"> -->
        <!-- 中间start --%>
        <div class="head-title">
        	<strong>记账凭证</strong>
        	<div id="paging">
        		<span class="paging_But" id="prevVc">上一张</span>
        		<span class="paging_But" id="nextVc">下一张</span>
        	</div>
        </div>
        <div class="book-keeping cl" id="book-keeping">
          <div class="book-state hide cl" id="book-state"></div>
          <div class="book-select cl">
            <div class="select-date">
              <input type="text" class="layui-input" id="period" name="period" placeholder="请选择日期" disabled>
              <i class="layui-icon book-icon">&#xe637;</i>
            </div>
            <div class="select-voucher">
              <div class="layui-form">
                <select name="jzlx" >
                  <option value="1">记</option>
                </select>
              </div>
              <div id="" class="book-number">
              	<input type="number" id="currVoucherNo" min="1">
              </div>
            </div>
            <div class="select-btn">
            	<%-- <form id="uploading" class="uploading" method="post" enctype="multipart/form-data">
									<label style="float: left;display: block;width: 220px;">
										<a href="javascript:void(0);">
											<span class="span">请选择97格式.xls</span>
											<input class="voucherFile in-file" id="file1" type="file" name="voucherFile" placeholder="格式：.xls"/>
										</a>
									</label>
									<span class="shangchuan layui-btn layui-btn-normal mr50">上传凭证</span>
							</form>
              <span id="addSave" class="hide">保存并新增</span> --%>
              <span id="copy">复制</span>
              <span id="save" class="hide">保存</span>
              <span id="cancel">返回</span>
            </div>
          </div>
          <ul class="book-title cl">
            <li class="w1">摘要</li>
            <li class="w2" >会计科目</li>
            <li class="w3">数量</li>
            <li class="w3">单价</li>
            <li class="book-title-debit">
              <p class="boox-amount">借方金额</p>
              <div class="book-unit">
                <span>亿</span>
                <span>千</span>
                <span>百</span>
                <span>十</span>
                <span>万</span>
                <span>千</span>
                <span>百</span>
                <span>十</span>
                <span>元</span>
                <span>角</span>
                <span>分</span>
              </div>
            </li>
            <li class="book-title-credits">
              <p class="boox-amount">贷方金额</p>
              <div class="book-unit">
                <span>亿</span>
                <span>千</span>
                <span>百</span>
                <span>十</span>
                <span>万</span>
                <span>千</span>
                <span>百</span>
                <span>十</span>
                <span>元</span>
                <span>角</span>
                <span>分</span>
              </div>
            </li>
            <li class="w5">操作</li>
          </ul>
          <!--  内容 -->
          <div id="content" class="cl">
            <%-- 不可操作的表格 --%>
            <ul class="book-content">
              <li class="w1"></li>
              <li class="w2"></li>
              <li class="w3"></li>
              <li class="w3"></li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w5"><span class="layui-icon" title="向下增行">&#xe654;</span><span class="layui-icon" title="删除本行">&#x1006;</span></li>
            </ul>
            <ul class="book-content">
              <li class="w1"></li>
              <li class="w2"></li>
              <li class="w3"></li>
              <li class="w3"></li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w5"><span class="layui-icon" title="向下增行">&#xe654;</span><span class="layui-icon" title="删除本行">&#x1006;</span></li>
            </ul>
            <ul class="book-content">
              <li class="w1"></li>
              <li class="w2"></li>
              <li class="w3"></li>
              <li class="w3"></li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w5"><span class="layui-icon" title="向下增行">&#xe654;</span><span class="layui-icon" title="删除本行">&#x1006;</span></li>
            </ul>
            <ul class="book-content">
              <li class="w1"></li>
              <li class="w2"></li>
              <li class="w3"></li>
              <li class="w3"></li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w4">
                <p class="w4 book-img"><span></span></p>
              </li>
              <li class="w5"><span class="layui-icon" title="向下增行">&#xe654;</span><span class="layui-icon" title="删除本行">&#x1006;</span></li>
            </ul>
          </div>

          <!-- 合计/备注 -->
          <div class="book-floor">
            <ul class="book-content">
              <li class="w1" style="font-size: 16px;line-height:60px;">合计：</li>
              <li class="w2" style="text-align: left;padding: 10px 5px;line-height:1.5;" id="formatAmount"></li>
              <li class="w3"></li>
              <li class="w3"></li>
              <li class="w4">
                <p class="w4 book-img">
                  <span id="debit"></span>
                </p>
              </li>
              <li class="w4">
                <p class="w4 book-img">
                  <span id="credits"></span>
                </p>
              </li>
              <li class="w5"></li>
            </ul>
            <div class="book-remark">
              <p>备注：</p>
              <div class="remark" >
                <input type="text" style="border:0" id="book-remark" maxlength="100">
              </div>
            </div>
          </div>

          <!-- 底部表单 -->
          <div class="book-input">
            <label for="form">制单人：
              <input type="text" name="form" id="form" readonly>
            </label>
            <label for="audit">审核：
                <input type="text" name="audit" id="audit" readonly>
            </label>
            <label for="accounting">记账：
                <input type="text" name="accounting" id="accounting" readonly>
            </label>
          </div>


          <!-- 科目列表 -->
          <div class="subList" id="subList">
              <ul id="subLists"></ul>
              <p class="sub-addSub" id="sub-addSub">
                <span>新增科目</span>
              </p>
            </div>
        </div>



        <%-- 中间end -->
      <!-- </div> -->
    <!-- </div> -->
  <!-- </div> --%>
</div>
<!-- End #content -->


<%@ include file="import-popup.jsp" %>
<%@ include file="../calculator.jsp" %>
<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/voucher/voucherInput.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/addSub.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/voucher/voucher-import.js?v=${timeStamp }"></script>
</body>
</html>
