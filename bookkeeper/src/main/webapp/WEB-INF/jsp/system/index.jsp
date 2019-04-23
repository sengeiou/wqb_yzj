<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
	<title>主页</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/plugins/layui/css/layui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/Kindex.css">
	<script>
		var session = {
		    user: '${userDate}',
			busDate: '${userDate.busDate }',
			userType: '${userDate.user.userType }',
			accountID: '${userDate.account.accountID }',
			minPeriod: '<fmt:formatDate value="${userDate.account.period }" pattern="yyyy-MM-dd" />',
		};
		var baseURL = '${pageContext.request.contextPath }'; // api请求路径(项目名称)
	</script>
	<%@ include file="../option.jsp" %>
</head>

<body class="main_body">
<div class="layui-layout layui-layout-admin">
	<!-- 顶部 -->
	<div class="layui-header header">
		<div class="layui-main mag0">
			<div class="nav_logo logo">
		    <%--<a href="${pageContext.request.contextPath }/subinit/initView?init=${userDate.account.initialStates}"  onclick="initialStates(this)">--%>
		    <a>
		      <img src="../img/logo.png" alt="LOGO">
		    </a>
		  </div>
			<!-- 显示/隐藏菜单 -->
			<!--<a href="javascript:;" class="seraph hideMenu icon-caidan"></a>-->
			<ul class="layui-layout-left">
			  	<li class="nav-item cu" id="choose-account">
	        		<div class="selectCus layui-elip" data-init="${userDate.account.initialStates}" data-mapped="${userDate.account.mappingStates}">${userDate.account.companyName}<c:if test="${empty userDate.account.accountID }">请选择账套</c:if></div>
	          		<i class="layui-icon">&#xe625;</i>
	            </li>
		        <li class="nav-item">
		            <input id="busDate" type="text" class="layui-input layui-inline wMonth cu" name="busDate" autocomplete="off" placeholder="会计期间" maxlength="7" value="${userDate.busDate }">
		            <i class="layui-icon">&#xe637;</i>
		        </li>
			</ul>
			<a href="#" class="advertising">
				<span>查看详情</span>
			</a>
		</div>
	</div>
	<!-- 左侧导航 -->
	<div class="layui-side layui-bg-black">
		<div class="navBar layui-side-scroll" id="navBar">
			<ul class="layui-nav layui-nav-tree" style="height: 764px;">
				<li class="layui-nav-item layui-this" style="display: none;">
					<a data-url="${pageContext.request.contextPath }/system/index">
						<i class="layui-icon layui-icon-home" data-icon=""></i>
						<cite>首页</cite>
					</a>
				</li>
				<li class="layui-nav-item">
					<a>
						<i class="seraph icon-text" data-icon="icon-text"></i>
						<cite>理票</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="">
								<a id="YJlipiao0" data-url="${pageContext.request.contextPath }/invoice/importView#1?typeS=2">
									<i class="seraph"></i>
									<cite>进项发票导入</cite>
								</a>
							</li>
							<li class="">
								<a id="YJlipiao1" data-url="${pageContext.request.contextPath }/invoice/importView#1?typeS=1">
									<i class="seraph"></i>
									<cite>销项发票导入</cite>
								</a>
							</li>
							<li class="">
								<a id="YJlipiao2" data-url="${pageContext.request.contextPath }/bank/importView#2">
									<i class="seraph"></i>
									<cite>银行对账单导入</cite>
								</a>
							</li>
							<li class="">
								<a id="YJlipiao3" data-url="${pageContext.request.contextPath }/arch/importView#3">
									<i class="seraph"></i>
									<cite>工资表导入</cite>
								</a>
							</li>
							<li class="">
								<a data-url="${pageContext.request.contextPath }/documents/documentsView#4">
									<i class="seraph"></i>
									<cite>费用票录入</cite>
								</a>
							</li>
							<li class="">
								<a data-url="${pageContext.request.contextPath }/assets/importView#5">
									<i class="seraph"></i>
									<cite>固定资产导入</cite>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="layui-nav-item">
					<a>
						<i class="layui-icon">&#xe630;</i>
						<cite>凭证</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/voucher/createView#7">
									<i class="seraph"></i>
									<cite>生成凭证</cite>
								</a>
							</li>
							<li class="layui-nav-item layui-hide">
								<a id="compilePZ" data-url="${pageContext.request.contextPath }/voucher/editView">
									<i class="seraph"></i>
									<cite>编辑凭证</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a id="add-voucher" data-url="${pageContext.request.contextPath }/voucher/importView">
									<i class="seraph"></i>
									<cite>新增凭证</cite>
								</a>
							</li>
							<li class="layui-nav-item" style="display: none;">
								<a data-url="${pageContext.request.contextPath }/voucher/searchView">
									<i class="seraph"></i>
									<cite>查询凭证</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=5">
									<i class="seraph"></i>
									<cite>凭证汇总表</cite>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="layui-nav-item">
					<a class="dDutiableGoods" data-url="https://ms.yun9.com/user/login">
						<i class="layui-icon">&#xe659;</i>
						<cite>报税</cite>
					</a>
				</li>
				<li class="layui-nav-item">
					<a><i class="layui-icon">&#xe63c;</i>
						<cite>账簿</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=1">
									<i class="seraph"></i>
									<cite>总账</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a id="detail" data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=2">
									<i class="seraph"></i>
									<cite>明细账</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=3">
									<i class="seraph"></i>
									<cite>数量金额总账</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=4">
									<i class="seraph"></i>
									<cite>科目余额表</cite>
								</a>
							</li>

							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/accountBook/queryView#8?typeS=6">
									<i class="seraph"></i>
									<cite>固定资产折旧明细</cite>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="layui-nav-item">
					<a><i class="layui-icon">&#xe629;</i>
						<cite>报表</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#9?typeS=1">
									<i class="seraph"></i>
									<cite>资产负债表</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#9?typeS=2">
									<i class="seraph"></i>
									<cite>利润表</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/BalanceSheet/BalanceSheetView#9?typeS=3">
									<i class="seraph"></i>
									<cite>现金流量表</cite>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="layui-nav-item">
					<a><i class="layui-icon">&#xe716;</i>
						<cite>设置</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item displayLi">
							</li>
							<li class="layui-nav-item">
								<a class="ignore" id="seraphCSH" data-url="${pageContext.request.contextPath }/subinit/initView?init=0">
									<i class="seraph"></i>
									<cite>账套初始化</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a class="ignore" id="subMap" data-url="${pageContext.request.contextPath }/subinit/subMappingView">
									<i class="seraph"></i>
									<cite>标准科目映射</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a id="bankCode" data-url="${pageContext.request.contextPath }/tempType/systemTemplate">
									<i class="seraph"></i>
									<cite>凭证模板</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a id="bankCode" data-url="${pageContext.request.contextPath }/bank/setBank">
									<i class="seraph"></i>
									<cite>银行设置</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/setTing/list#12?typeS=1">
									<i class="seraph"></i>
									<cite>科目设置</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/setTing/list#12?typeS=2">
									<i class="seraph"></i>
									<cite>计量单位设置</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a data-url="${pageContext.request.contextPath }/setTing/list#12?typeS=3">
									<i class="seraph"></i>
									<cite>汇率设置</cite>
								</a>
							</li>
							<c:if test="${userDate.user.power gt '98'}">
							<!-- 超级管理员 -->
							<li class="layui-nav-item">
								<a class="ignore mana" data-url="${manaBaseURL }/subjectMapping/subMappingView?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>标准科目别称配置</cite>
								</a>
							</li>
							</c:if>
							<c:if test="${userDate.user.userType lt '4'}">
							<!-- 管理员可配置员工 -->
							<li class="layui-nav-item">
								<a class="ignore mana" id="YGPZ" data-url="${manaBaseURL }/system/intoSys?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>员工配置</cite>
								</a>
							</li>

							<li class="layui-nav-item">
								<a class="ignore mana" id="ZTGL" data-url="${manaBaseURL }/system/toManagerAccount?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>账套管理</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a class="ignore mana" id="KHGL" data-url="${manaBaseURL }/system/toManagerCustom?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>客户管理</cite>
								</a>
							</li>
							</c:if>
							<c:if test="${userDate.user.userType gt '4'}">
							<li class="layui-nav-item">
								<a class="ignore mana" id="ZTGL" data-url="${manaBaseURL }/system/toUserIndex?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>账套管理</cite>
								</a>
							</li>
							<li class="layui-nav-item">
								<a class="ignore mana" id="KHGL" data-url="${manaBaseURL }/system/toUserCustom?userID=${userDate.user.userID}">
									<i class="seraph"></i>
									<cite>客户管理</cite>
								</a>
							</li>
							</c:if>
							<li class="layui-nav-item">
								<a class="ignore mana" id="changePassword" data-url="${pageContext.request.contextPath }/system/changePassword">
									<i class="seraph"></i>
									<cite>修改密码</cite>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="layui-nav-item" id="userInfo">
					<a href="javascript:;" title="${userDate.user.loginUser }">
						<i class="layui-icon">&#xe770;</i>
						<!-- <cite class="adminName">${userDate.user.loginUser }</cite> -->
						<cite class="adminName">系统</cite>
					</a>
					<div class="YJlipiao">
						<div class="triangle_border_left">
						    <span></span>
						</div>
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item lockcms" pc><a href="javascript:;" class="ignore"><i class="seraph icon-lock"></i><cite>锁屏</cite></a></li>
							<li class="layui-nav-item"><a href="javascript:;" class="ignore functionSetting"><i class="layui-icon">&#xe620;</i><cite>功能设定</cite><span class="layui-badge-dot"></span></a></li>
							<li class="layui-nav-item"><a href="javascript:;" class="ignore changeSkin"><i class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a></li>
							<li class="layui-nav-item"><a href="javascript:logout();" id="signOut" class="ignore signOut"><i class="seraph icon-tuichu"></i><cite>退出</cite></a></li>
						</ul>
					</div>
				</li>
				<span class="layui-nav-bar" style="height: 0px; top: 112.5px; opacity: 0;"></span>
				<!-- 显示/隐藏菜单   收缩 -->
			  <!--<a href="javascript:;" class="seraph hideMenu icon-caidan"></a>-->
			</ul>
		</div>
		<%--<div class="userName">   勿删
			顶部右侧菜单
		    <ul class="layui-nav top_menu">
				<li class="layui-nav-item" pc>
					<a href="javascript:;" class="clearCache"><i class="layui-icon" data-icon="&#xe640;">&#xe640;</i><cite>清除缓存</cite><span class="layui-badge-dot"></span></a>
				</li>
				<li class="layui-nav-item lockcms" pc>
					<a href="javascript:;"><i class="seraph icon-lock"></i><cite>锁屏</cite></a>
				</li>
				<li class="layui-nav-item" id="userInfo">
					<a href="javascript:;">
						<i class="layui-icon">&#xe770;</i>
						<cite class="adminName">${userDate.user.loginUser }</cite>
					</a>
					<div class="YJlipiao">
						<ul class="layui-nav layui-nav-tree">
							<li class="layui-nav-item lockcms" pc><a href="javascript:;"><i class="seraph icon-lock"></i><cite>锁屏</cite></a></li>
							<li class="layui-nav-item"><a href="javascript:;" class="functionSetting"><i class="layui-icon">&#xe620;</i><cite>功能设定</cite><span class="layui-badge-dot"></span></a></li>
							<li class="layui-nav-item"><a href="javascript:;" class="changeSkin"><i class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a></li>
							<li class="layui-nav-item"><a href="javascript:logout();" class="signOut"><i class="seraph icon-tuichu"></i><cite>退出</cite></a></li>
						</ul>
					</div>
					<dl class="layui-nav-child">
						<dd pc><a href="javascript:;"><i class="seraph icon-lock"></i><cite>锁屏</cite></a></dd>
						<dd pc><a href="javascript:;" class="functionSetting"><i class="layui-icon">&#xe620;</i><cite>功能设定</cite><span class="layui-badge-dot"></span></a></dd>
						<dd pc><a href="javascript:;" class="changeSkin"><i class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a></dd>
						<dd><a href="javascript:logout();" class="signOut"><i class="seraph icon-tuichu"></i><cite>退出</cite></a></dd>
					</dl>
				</li>
			</ul>
		</div>--%>
	</div>
	<!-- 右侧内容 -->
	<div class="layui-body layui-form">
		<!--报税遮罩-->
		<div class="bszz"></div>
		<div class="layui-tab mag0" lay-filter="bodyTab" id="top_tabs_box">
			<ul class="layui-tab-title top_tab" id="top_tabs">
				<li class="layui-this" lay-id="">
					<c:choose>
						<c:when test="${empty userDate.account.accountID }">
							<cite>客户管理</cite>
						</c:when>
						<c:otherwise>
							<i class="layui-icon layui-icon-home"></i><cite>首页</cite>
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
			<ul class="layui-nav closeBox Wbox">
			  <li class="layui-nav-item closeBoxLi">
			    <a href="javascript:;" class="refresh refreshThis" title="刷新当前"><i class="layui-icon layui-icon-refresh"></i></a>
			    <a href="javascript:;" class="closePageOther" title="关闭其他"><i class="seraph icon-prohibit"></i></a>
			    <a href="javascript:;" class="closePageAll" title="关闭全部"><i class="seraph icon-guanbi"></i></a>
			  </li>
			</ul>
			<div class="layui-tab-content clildFrame">
				<div class="layui-tab-item layui-show">
					<c:choose>
						<c:when test="${empty userDate.account.accountID }">
							<!-- 没有账套，强制进入客户管理页面 -->
							<c:choose>
								<c:when test="${userDate.user.userType lt '4'}">
									<iframe id="pageContext" src="${manaBaseURL }/system/toManagerCustom?userID=${userDate.user.userID}"></iframe>
								</c:when>
								<c:otherwise>
									<iframe id="pageContext" src="${manaBaseURL }/system/toUserCustom?userID=${userDate.user.userID}"></iframe>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<iframe id="pageContext" src="${pageContext.request.contextPath }/system/main"></iframe>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 移动导航 -->
<div class="site-mobile-shade"></div>

<!-- 账套列表弹窗 -->
<div class="dn cusSel" id="account-list-popup">
  <span class="upward"></span>
  <!--搜索账套-->
  <div class="selectSel">
  	<input placeholder="请输入查询内容" id="mainQuery" type="text">
  	<!--<button id="selectSel">查询</button>-->
  </div>
  <div class="content">
    <!-- 固定头部 -->
    <div class="head ">
      <table class="layui-table ">
        <colgroup>
          <col>
          <col width="120">
          <col width="120">
        </colgroup>
        <thead>
          <tr>
            <th>客户名称</th>
            <th>期间</th>
            <th>操作</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="tablebody2 cusTable">
      <table class="layui-table">
        <colgroup>
          <col>
          <col width="120">
          <col width="120">
        </colgroup>
        <tbody id="account-list"></tbody>
      </table>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath }/plugins/jquery-1.12.4.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/layui/layui.js"></script>
<script src="${pageContext.request.contextPath }/plugins/layui/layui.all.js"></script>
<script src="${pageContext.request.contextPath }/js/index.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/cache.js?v=${timeStamp }"></script>
<script src="${pageContext.request.contextPath }/js/homePage.js?v=${timeStamp }"></script>
<c:if test="${userDate.user.userType gt '4'}">
<script>
	if (session.userType == 5 || session.userType == 6) {
		purview(${userDate.perList }); // 页面权限配置
	}
</script>
</c:if>
</body>

</html>
