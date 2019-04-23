<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 导入文件弹窗 -->
<div class="zz_pop_up layui-form search">
	<ul>
		<li>
			<div style="width: 80px; padding-right: 0px; line-height: 30px;float: left;">会计期间：</div>
			<div class="search-item search-item1">
				<span class="timeBeginS"></span>
	          <input type="text" style="display: none;" class="layui-input timeBegin" disabled="disabled" value="">
	          <ul id="startStopDate"></ul>
	        </div>
	        <div class="search-item search-item2 search-before">
	        	<span class="timeEndS"></span>
	          <input type="text" style="display: none;" class="layui-input timeEnd" disabled="disabled" value="">
	          <ul id="startStopDate1"></ul>
	        </div>
		</li>
		<li class="qishi">
			<div style="width: 80px; padding-right: 0px; line-height: 30px;float: left;">起始科目：</div>
			<div class="search-item searchEnd">
			  <input type="text" class="layui-input cu" name="qsSubject" id="qsSubject" lay-verify="subject" placeholder="请选择科目" readonly="">
	          <!--<div class="lay_certificate layui-form lay_select">
	            <select name="level" lay-search class="level" id="select_levelBegin"></select>
	          </div>-->
	        </div>
		</li>
		<li class="jieshu">
			<div style="width: 80px; padding-right: 0px; line-height: 30px;float: left;">结束科目：</div>
			<div class="search-item searchEnd">
			  <input type="text" class="layui-input cu" name="qsSubject" id="jsSubject" lay-verify="subject" placeholder="请选择科目" readonly="">
	          <!--<div class="lay_certificate layui-form lay_select">
	            <select name="level" lay-search class="level" id="select_levelEnd"></select>
	          </div>-->
	        </div>
		</li>
		<li>
			<div style="width: 80px; padding-right: 0px; line-height: 30px;float: left;">科目节次：</div>
			<div class="KMbegin">
				<input  type="text" class="layui-input KMbeginI" disabled="disabled" value=""/>
				<span class="layui-icon KMendJIA">&#xe625;</span>
				<span class="layui-icon KMendJIAN">&#xe625;</span>
			</div>
			至
			<div class="KMend">
				<input  type="text" class="layui-input KMbeginI" disabled="disabled" value=""/>
				<span class="layui-icon KMendJIA">&#xe625;</span>
				<span class="layui-icon KMendJIAN">&#xe625;</span>
			</div>
		</li>
		<li class="xszmx"><input type="checkbox" name="like1[read]" lay-skin="primary" title="只显示最明细科目"></li>
		<li class="yeIs"><input type="checkbox" name="like1[read]" lay-skin="primary" title="余额为0不显示"></li>
		<li class="wfsYeIs"><input type="checkbox" name="like1[read]" lay-skin="primary" title="无发生额且余额为0不显示"></li>
	</ul>
	<div class="layui-btn layui-btn-normal reset_btn">重置</div>
	<div class="layui-btn layui-btn-normal search_btn1">查询</div>
</div>


<!-- 所有固定资产科目弹窗 -->
<div class="popup layui-tab layui-tab-card" id="subject-popup" lay-filter="sub_tab">
	<ul class="layui-tab-title">
		<li class="layui-this">资产</li>
		<li>负债</li>
		<li>共同</li>
		<li>权益</li>
		<li>成本</li>
		<li>损益</li>
	</ul>
	<div class="layui-tab-content">
		<div class="layui-tab-item layui-show">
			<ul class="ztree" id="tree1">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
		<div class="layui-tab-item">
			<ul class="ztree" id="tree2">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
		<div class="layui-tab-item">
			<ul class="ztree" id="tree3">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
		<div class="layui-tab-item">
			<ul class="ztree" id="tree4">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
		<div class="layui-tab-item">
			<ul class="ztree" id="tree5">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
		<div class="layui-tab-item">
			<ul class="ztree" id="tree6">
				<p class="tc" style="line-height: 80px;">暂无数据</p>
			</ul>
		</div>
	</div>
</div>