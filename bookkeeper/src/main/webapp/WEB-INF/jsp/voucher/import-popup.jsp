<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 科目下拉弹窗 -->
<div class="dn p-abs layui-form select-info" id="select-subject">
  <ul class="select-sub-list">
    <li class="select-item tips">没有匹配的科目</li>
  </ul>
  <div class="tc add-sub-cont">
    <span class="layui-btn layui-btn-sm edit" id="add-sub-btn">新增科目</span>
  </div>
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

<%-- <!-- 新增计量单位 -->
<!-- <div id="addUnit" class="dn layui-form">
  <div class="layui-form-item">
    <label class="layui-form-label">计量单位符号</label>
    <div class="layui-input-block">
      <input type="text" name="title" required lay-verify="required" placeholder="请输入计量单位符号，如kg" autocomplete="off" class="layui-input" id="UnitSymbol">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">计量单位名称</label>
    <div class="layui-input-block">
      <input type="text" name="title" required lay-verify="required" placeholder="请输入计量单位名称，如千克" autocomplete="off" class="layui-input" id="UnitName">
    </div>
  </div>
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">备注</label>
    <div class="layui-input-block">
      <textarea name="desc" placeholder="请输入内容" class="layui-textarea" id="UnitRemarks"></textarea>
    </div>
  </div>
</div> --> --%>

<!-- 快捷键提示 -->
<div class="dn shortcuts-hints" id="shortcuts-hints">
  <p class="">键盘常用操作</p>
  <table class="layui-table">
    <colgroup>
      <col width="35%">
      <col width="65%">
    </colgroup>
    <thead>
      <tr>
        <td>快捷键</td>
        <td>凭证</td>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>F8</td>
        <td>保存</td>
      </tr>
      <tr>
        <td>Tab</td>
        <td>跳转下一栏</td>
      </tr>
      <tr>
        <td>Shift+Tab</td>
        <td>跳转上一栏</td>
      </tr>
      <tr>
        <td>Enter</td>
        <td>确定并跳转下一栏</td>
      </tr>
      <tr>
        <td>空格键</td>
        <td>金额在借贷方之间转换</td>
      </tr>
       <tr>
        <td>Alt+F10</td>
        <td>调出计算器</td>
      </tr>
      <tr>
        <td>Alt+F11</td>
        <td>关闭计算器</td>
      </tr>
      <tr>
        <td>Ctrl+Alt+F11</td>
        <td>计算结果回填当前输入框</td>
      </tr>
      <tr>
        <td>Ctrl+F6</td>
        <td>快速打开凭证录入</td>
      </tr>
      <tr>
        <td>Ctrl+Delect</td>
        <td>清空计算器</td>
      </tr>
      <tr>
        <td>=</td>
        <td>自动平衡当前科目金额</td>
      </tr>
      <tr>
        <td>alt+=</td>
        <td>自动平衡未输入金额的第一个科目</td>
      </tr>
    </tbody>
  </table>
</div>

<!--成本结转(出库)-->
<div class="dn warehouse layui-form table-box" id="warehouse">
	<div class="triangle_border_top">
	    <span></span>
	</div>
	<p class="">说明：保存凭证后请重新刷新此列表</p> 
	<i class="layui-icon layui-icon-refresh" id="refresh-warehouse"></i>
	<table class="layui-table">
		<colgroup>
			<col width="5%">
      <col width="13%">
      <col width="25%">
      <col width="10%">
      <col width="12%">
    </colgroup>
    <thead>
      <tr>
      	<td><input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose"></td>
        <td>科目编码</td>
        <td>商品名称</td>
        <td>期末数量</td>
        <td>期末余额</td>
      </tr>
    </thead>
	</table>
	<div class="tableBody">
		<table class="layui-table">
			<colgroup>
				<col width="5%">
	      <col width="13%">
	      <col width="25%">
	      <col width="10%">
	      <col width="12%">
	    </colgroup>
	    <tbody id="warehouseBody">
	    </tbody>
		</table>
	</div>
	<div class="SCimport">生成凭证</div>
</div>

<!--新增模板-->
<div class="newTemplate" id="newTemplate">
	<ul>
		<li>
			<div class="templateL">模板类别：</div>
			<div class="templateR">
				<span>请选择...</span>
				<i class="layui-icon">&#xe61a;</i>
				<div class="templateDiv">
					<ul id="templateList">
					</ul>
					<span id="addType"><i class="layui-icon">&#xe654;</i>增加类别</span>
				</div>
			</div>
		</li>
		<li>
			<div class="templateL">模板名称：<label style="color: red;">*</label></div>
			<div class="templateR">
				<input class="assistName" type="text">
			</div>
		</li>
		<li>
			<div class="templateL">保存金额：</div>
			<div class="templateR layui-form table-box" style="border: none;padding-top: 4px;">
				<input type="checkbox" name="ckbox" lay-skin="primary">
			</div>
		</li>
	</ul>
</div>

<!--模板类别-->
<div class="templateType" id="templateType">
	<div class="templateTop">
		<input type="tetx" id="templateVal" placeholder="请输入要增加的类别名称">
		<span class="preservaType">保存</span>
		<span class="cancel">取消</span>
	</div>
	<div class="templateT">
		<table class="layui-table">
			<colgroup>
	      <col width="20%">
	      <col width="80%">
	    </colgroup>
	    <thead>
	      <tr>
	        <td>操作</td>
	        <td>类别名称</td>
	      </tr>
	    </thead>
		</table>
		<div class="templateBody">
			<table class="layui-table">
				<colgroup>
					<col width="20%">
	      	<col width="80%">
		    </colgroup>
		    <tbody id="templateBody">
		    </tbody>
			</table>
		</div>
	</div>
</div>

<!--选择模板-->
<div class="choiceTemplate" id="choiceTemplate">
	<div class="choiceTempT">
		<table class="layui-table">
			<colgroup>
				<col width="10%" />
				<col width="25%" />
				<col width="65%" />
			</colgroup>
			<thead>
	      <tr>
	        <td>操作</td>
	        <td>类别</td>
	        <td>名称</td>
	      </tr>
	    </thead>
		</table>
		<div class="choiceTempBody">
			<table class="layui-table">
				<colgroup>
					<col width="10%" />
					<col width="25%" />
					<col width="65%" />
				</colgroup>
				<tbody id="choiceTempBody">
				</tbody>
			</table>
		</div>
	</div>
</div>

<!--复制粘贴-->
<div class="copy" id="copyPaste" style="display: none;">
	<div class="copyDate">
		<span>选择复制期间：</span>
		<input id="copyDate" type="text" class="layui-input layui-inline wMonth cu" name="copyDate" placeholder="选择日期" maxlength="7" value="">
		<i class="layui-icon">&#xe637;</i>
	</div>
	<div class="copyWordSize">
		<span>凭证字号：</span>
		<input type="text" class="nextVoucherNo" readonly="readonly">
	</div>
</div>


<!--跨月复制查看-->
<div class="book-keeping cl" id="cmccCopy" style="display: none;">
	<span class="copyHint"></span>
  <div class="book-state hide cl" id="book-state"></div>
  <div class="book-select cl">
    <div class="select-date">
      <input type="text" class="layui-input" id="period" name="period" placeholder="请选择日期" value="${userDate.busDate }" disabled>
      <i class="layui-icon book-icon">&#xe637;</i>
    </div>
    <div class="select-voucher">
      <div class="layui-form">
        <select name="jzlx" >
          <option value="1">记</option>
        </select>
      </div>
      <div id="" class="book-number">
      	<input type="number" id="currVoucherNo" min="1" readonly>
      </div>
    </div>
    <div class="select-btn">
    	<form id="uploading" class="uploading dn" method="post" enctype="multipart/form-data">
							<label style="float: left;display: block;width: 220px;">
								<a href="javascript:void(0);">
									<span class="span">请选择97格式.xls</span>
									<input class="voucherFile in-file" id="file1" type="file" name="voucherFile" placeholder="格式：.xls"/>
								</a>
							</label>
							<span class="shangchuan layui-btn layui-btn-normal mr50">上传凭证</span>
					</form>
      <span id="addSave" class="hide">保存并新增</span>
      <!-- <span id="save"  class="hide">保存</span>
      <span id="cancel" class="hide">返回</span> -->
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
  <div id="contentCopy" class="cl">
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
        <input type="text" style="border:0" id="book-remark" maxlength="100" readonly>
      </div>
    </div>
  </div>
  <!-- 底部表单 -->
  <div class="book-input">
    <label for="form">制单人：
      <input type="text" name="form" id="form" value="${userDate.user.userName }" readonly>
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
      <ul id="subLists">
        <li class="sub-title ellipsis">正在加载数据~</li>
      </ul>
      <p class="sub-addSub" id="sub-addSub">
        <span>新增科目</span>
      </p>
  </div>
  <div class="zhezhao"></div>
</div>
