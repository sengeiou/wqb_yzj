<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!-- Start #header -->
  <!-- 头部区域 -->
  <header class="layui-header" id="header">
    <nav id="nav">
      <div class="nav_logo">
        <a href="${pageContext.request.contextPath }/subinit/initView?init=${userDate.account.initialStates}"  onclick="initialStates(this)">
          <img src="../img/logo.png" alt="LOGO">
        </a>
      </div>
      <div class="right flex">
        <ul class="left_set">
        	<li class="crumbs returnS">
        		首页&nbsp;&nbsp;>&nbsp;&nbsp;工作台
        	</li>
        	<li class="backHomepage">
        		<a></a>
        	</li>
        	<li class="cus cu fl" id="choose-account">
        		<span class="selectCus layui-elip">${userDate.account.companyName}</span>
          	<i class="iconfont icon-arrow"></i>
        	</li>
        	<li class="cus cu fl" id="textName">
        		<span></span>
          	<i class="iconfont icon-arrow"></i>
        	</li>
					<li class="date-select date-selectA">
	            <input id="busDate" type="text" class="layui-input dib wMonth cu" name="busDate" placeholder="会计期间" maxlength="7" autocomplete="off" lay-key="1001">
	            <i class="layui-icon">&#xe637;</i>
	          </li>
	          <li class="date-select date-selectB">
	            <input id="busDate1" type="text" class="layui-input dib wMonth cu" name="busDate" placeholder="会计期间" maxlength="7" autocomplete="off" lay-key="1012">
	            <i class="layui-icon">&#xe637;</i>
	          </li>
				</ul>
        <span id="balanceHint"></span>
        <ul class="right_set">
        	<li class="user layui-elip mark setUpThe">
          	<!-- 后台跳转路径 -->
            <a href="${pageContext.request.contextPath}/setTing/list#12" id="nav_set">设置</a>
          </li>
        	<li class="user layui-elip mark">
            <a href="" id="getAdmin">管理员</a>
          </li>
          <li class="user layui-elip mark">
            <a href="" id="getAdmin">超级管理员</a>
          </li>
          <li class="user layui-elip mark">
            <!-- 后台跳转路径 -->
            <form action="localhost:18080/wqbmana/system/intoSys" target="_blank">
              <input style="display: none;" type="text" name="userID" value="${userDate.user.userID}">
              <input type="submit" value="后台管理" id="getAdmin">
            </form>
          </li>
          <li class="user layui-elip">
            <a href="javascript:;" class="hrefPageFn">
              <!-- <img src="http://t.cn/RCzsdCq" class="layui-nav-img"> href="javascript:;"-->
              <span>您好，${userDate.user.loginUser }</span>
              <span class="administrator"></span>
            </a>
          </li>
          <!-- <li class="cu" onclick="logout();">
            <i class="icon-logout"></i>注销</li> -->
        </ul>
      </div>
    </nav>
  </header>

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
  <!-- End #sidebar -->
