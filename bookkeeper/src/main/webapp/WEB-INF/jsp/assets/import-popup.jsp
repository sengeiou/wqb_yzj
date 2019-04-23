<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 导入文件弹窗 -->
<div class="popup file" id="upload-popup">
	<form method="post" enctype="multipart/form-data">
		<label class="ovh fileLabel"> <span
			class="layui-elip db fileName">请选择97格式.xls...</span> <input
			type="file" class="in-file" name="uploadAssets" id="upload-input"
			placeholder="格式：.xls">
		</label>
		<div class="progress">
			<span class="percentage"></span> <span class="text p-rel">0%</span>
		</div>
		<span class="layui-btn layui-btn-normal mr50" id="upload-btn">上传</span>
		<a class="layui-btn layui-btn-primary mr10"
			href="${pageContext.request.contextPath }/assets/downExcel">下载模板</a>
	</form>
</div>

<!-- 新增固定资产弹窗 -->
<div class="popup site-text site-block" id="add-popup">
	<form class="layui-form">
		<h2>资产信息</h2>
		<ul class="cl">
			<li class="layui-form-item"><label class="layui-form-label"
				for="asCode"><span class="asterisk">*</span>资产编码</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input naturalNum" name="asCode"
						id="asCode" lay-verify="required" maxlength="30"
						placeholder="请输入资产编码">
				</div> <span class="layui-word-aux"></span></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asName"><span class="asterisk">*</span>资产名称</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input nospace" name="asName"
						id="asName" lay-verify="required" maxlength="20"
						placeholder="请输入资产名称">
				</div> <span class="layui-word-aux"></span></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>资产类别</label>
				<div class="layui-input-inline">
					<select name="asCategory" id="asCategory" lay-verify="select">
						<option value=""></option>
						<option value="车辆">车辆</option>
						<option value="机器设备">机器设备</option>
						<option value="房屋建筑">房屋建筑</option>
						<option value="办公用品">办公用品</option>
					</select>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>增加资产的方式</label>
				<div class="layui-input-inline">
					<select name="sourceway" id="sourceway" lay-verify="select">
						<option value="购入" selected>购入</option>
						<option value="赠送">赠送</option>
					</select>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>使用部门</label>
				<div class="layui-input-inline">
					<select name="department" id="department" lay-verify="select">
						<option value=""></option>
						<option value="管理部门">管理部门</option>

						<option value="生产部门">生产部门</option>

						<option value="销售部门">销售部门</option>


					</select>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>使用情况(资产状态)</label>
				<div class="layui-input-inline">
					<input type="radio" name="asState" value="使用中" title="使用中" checked>
					<input type="radio" name="asState" value="未使用" title="未使用">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asAccountDatea"><span class="asterisk">*</span>入账日期</label>
				<div class="layui-input-inline">
					<input type="text" name="asAccountDatea" id="asAccountDatea"
						lay-verify="required" class="layui-input wDate" autocomplete="off"
						placeholder="请选择日期"> <i class="layui-icon p-abs icon">&#xe637;</i>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label">入帐前已开始使用</label>
				<div class="layui-input-inline">
					<input type="radio" name="isBeforeUse" value="1" title="是">
					<input type="radio" name="isBeforeUse" value="2" title="否" checked>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asBeforeUseDate">入帐前开始使用日期</label>
				<div class="layui-input-inline">
					<input type="text" name="asBeforeUseDate" id="asBeforeUseDate" autocomplete="off"
						class="layui-input wDate" placeholder="请选择日期"> <i
						class="layui-icon p-abs icon">&#xe637;</i>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asPosition">存放地点</label>
				<div class="layui-input-inline">
					<input type="text" name="asPosition" id="asPosition"
						class="layui-input nospace" maxlength="100">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asManufactor"><span class="asterisk">*</span>供货方</label>
				<div class="layui-input-inline">
					<input type="text" name="asManufactor" id="asManufactor"
						class="layui-input nospace" maxlength="100" lay-verify="required">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asManufactorDate">生产日期</label>
				<div class="layui-input-inline ">
					<input type="text" name="asManufactorDate" id="asManufactorDate" autocomplete="off"
						class="layui-input wDate" placeholder="请选择日期"> <i
						class="layui-icon p-abs icon">&#xe637;</i>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asModel">型号</label>
				<div class="layui-input-inline">
					<input type="text" name="asModel" id="asModel"
						class="layui-input nospace" maxlength="100">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asEconomicUse">经济用途</label>
				<div class="layui-input-inline">
					<input type="text" name="asEconomicUse" id="asEconomicUse"
						class="layui-input nospace" maxlength="50">
				</div></li>
		</ul>

		<h2>折旧方式</h2>
		<ul class="cl">
			<li class="layui-form-item"><label class="layui-form-label">
				<span class="asterisk">*</span>折旧方法</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input" name="dmethod" id="dmethod"
						lay-verify="required" value="平均年限法" readonly>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asEstimatePeriod"><span class="asterisk">*</span>预计使用期间</label>
				<div class="layui-input-inline">
					<input type="text" name="asEstimatePeriod" id="asEstimatePeriod"
						class="layui-input double" lay-verify="required" placeholder=""
						maxlength="14"> <span class="layui-icon p-abs icon">月</span>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asExpectedPeriod">
				<span class="asterisk">*</span>预计剩余折旧期间数(工作总量)</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input double" name="asExpectedPeriod" id="asExpectedPeriod" lay-verify="required" placeholder="" maxlength="14">
					<span class="layui-icon p-abs icon">期</span>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label" for="asDepreciaPeriod">
				<span class="asterisk">*</span>折旧计算的预计使用期间(工作总量)</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input double"
						name="asDepreciaPeriod" id="asDepreciaPeriod"  lay-verify="required" placeholder=""
						maxlength="14"> <span class="layui-icon p-abs icon">期</span>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>固定资产科目</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input cu" name="gdsubject"
						id="gdsubject" lay-verify="subject" placeholder="请选择科目" readonly>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>累计折旧科目</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input cu"
						name="asCumulativeSubject" id="asCumulativeSubject"
						lay-verify="subject" placeholder="请选择科目" readonly>
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"><span
					class="asterisk">*</span>折旧费用科目</label>
				<div class="layui-input-inline">
					<input type="text" class="layui-input cu" name="asDepreciaSubject"
						id="asDepreciaSubject" placeholder="请选择科目" lay-verify="subject"
						readonly>
				</div></li>
		</ul>

		<h2>原值、净值、累计折旧</h2>
		<ul class="cl">
			<li class="layui-form-item"><label class="layui-form-label"
				for="asvalue"><span class="asterisk">*</span>原值</label>
				<div class="layui-input-inline">
					<input type="text" name="asvalue" id="asvalue"
						lay-verify="required" class="layui-input double tr pr10"
						maxlength="14" placeholder="0.00">
				</div></li>

			<li class="layui-form-item"><label class="layui-form-label" for="asUseDepreciaValue">
				<span class="asterisk">*</span>用于折旧计算的原值</label>
				<div class="layui-input-inline">
					<input type="text" name="asUseDepreciaValue" id="asUseDepreciaValue" lay-verify="required" class="layui-input double tr pr10"
						maxlength="14" placeholder="0.00">
				</div></li>

			<li class="layui-form-item"><label class="layui-form-label "for="taxRate">
				<span class="asterisk">*</span>应交税费</label>
				<div class="layui-input-inline">
					<input type="text" name="taxRate" id="taxRate" lay-verify="required" class="layui-input double tr pr10"
						maxlength="14" placeholder="0.00">
				</div></li>

			<li class="layui-form-item"><label class="layui-form-label" for="asWorth">
				<span class="asterisk">*</span>净值</label>
				<div class="layui-input-inline">
					<input type="text" name="asWorth" id="asWorth"  lay-verify="required" class="layui-input double tr pr10" maxlength="14"
						placeholder="0.00">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asCumulativeImpairment">累计减值准备</label>
				<div class="layui-input-inline">
					<input type="text" name="asCumulativeImpairment"
						id="asCumulativeImpairment" class="layui-input double tr pr10"
						maxlength="14" placeholder="0.00">
				</div></li>
			<li class="layui-form-item"><label class="layui-form-label"
				for="asAddDeprecia"><span class="asterisk">*</span>累计折旧</label>
				<div class="layui-input-inline">
					<input type="text" name="asAddDeprecia" id="asAddDeprecia"
						class="layui-input double tr pr10" maxlength="14"
						placeholder="0.00">
				</div></li>

			<li class="layui-form-item"><label class="layui-form-label"
				for="asNetSalvage">预计净残值</label>
				<div class="layui-input-inline">
					<input type="text" name="asNetSalvage" id="asNetSalvage"
						class="layui-input double tr pr10" maxlength="14"
						placeholder="0.00">
				</div></li>

			<!--每期折旧额 = （原值 - 净值)/预计使用期间     这个值需要自动计算-->
			<li class="layui-form-item"><label class="layui-form-label"
				for="depreciaAmount">每期折旧额</label>
				<div class="layui-input-inline">
					<input type="text" id="depreciaAmount" class="layui-input tr pr10"
						placeholder="0.00" readonly>
				</div></li>

			<li class="layui-form-item"><label class="layui-form-label"
				for="des">备注</label>
				<div class="layui-input-inline remarks">
					<input type="text" name="des" id="des" class="layui-input"
						maxlength="100" placeholder="请输入内容">
				</div></li>
		</ul>

		<div class="mt10 tc">
			<button class="layui-btn layui-btn-normal mr50" lay-submit>保存</button>
			<span class="layui-btn layui-btn-primary layui-layer-close">取消</span>
		</div>
	</form>
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
	</div>
</div>
