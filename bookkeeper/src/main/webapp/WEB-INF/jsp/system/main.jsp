<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>主页</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css">
  <style>
    /* vue显示数据时，页面闪现原始代码解决方案 */
    [v-cloak] {display: none;}
  </style>
</head>

<body>
  <!-- 内容 -->
  <div class="main" id="mainApp" v-cloak>
    <template>
      <div id="businessPie" style="width: 0px;height: 0px;display: none;"></div>
      <div id="effectiveLine" style="width: 0px;height: 0px;display: none;"></div>
      <%-- session --%>
      <span style="display: none;" id="session" class="main-session">${userDate.perList}</span>
      <%-- 代账 --%>
      <div id="DZview" v-if="userType==='2' || userType==='5'">
      	<!-- l1 视图 / 公告 -->
	      <div class="main-data cl">
	        <!-- 客户统计 -->
	        <div class="main-left">
	          <div class="main-title">
	            <h2>客户数据</h2>
	          </div>
	          <div class="tableTitle">
	              <table class="layui-table">
	                  <colgroup>
	                    <col width="22%">
	                    <col width="26%">
	                    <col width="26%">
	                    <col width="26%">
	                  </colgroup>
	                  <thead>
	                    <tr>
	                      <th>会计人员</th>
	                      <th>客户总数</th>
	                      <th>{{curMonth}}月新增</th>
	                      <th>{{curMonth}}月停用</th>
	                    </tr>
	                  </thead>
	              </table>
	          </div>
	          <div class="tbodyWarp">
	            <table class="layui-table">
	              <colgroup>
	                <col width="22%">
	                <col width="26%">
	                <col width="26%">
	                <col width="26%">
	              </colgroup>
	              <tbody class="">
	                <tr v-if="isShowAdmin">
	                  <td>未分配</td>
                    <!-- <td>${userDate.user.loginUser }</td> -->
	                  <td>{{userCountData.wfp}}</td>
	                  <td>{{userCountData.adminXzhj}}</td>
	                  <td>{{userCountData.adminTy}}</td>
	                </tr>
	                <tr v-if="isShowAdmin" v-for="(item, index) in userCountData.list" :key="index">
	                  <td>{{item.userName}}</td>
	                  <td>{{item.sl}}</td>
	                  <td>{{item.xz}}</td>
	                  <td>{{item.userTy}}</td>
	                </tr>

	                <tr v-if="!isShowAdmin">
	                  <td>{{curuserName}}</td>
	                  <td>{{userCountData.sl}}</td>
	                  <td>{{userCountData.xz}}</td>
	                  <td>{{userCountData.hjty}}</td>
	                </tr>
	              </tbody>
	              <tfoot v-if="isShowAdmin">
	                <tr>
	                  <td>总计</td>
	                  <td>{{userCountData.hj}}</td>
	                  <td>{{userCountData.xzhj}}</td>
	                  <td>{{userCountData.hjty}}</td>
	                </tr>
	              </tfoot>
	            </table>
	          </div>
	        </div>
	        <!--  系统公告 -->
	        <div class="main-right">
	          <div class="main-title">
	            <h2>系统公告</h2>
	            <!--   <a href="javascript:;" class="more">更多</a> -->
	          </div>
	          <ul class="main-list main-centen">
	            <li>
	              <span>1.新手指引，《用户操作指南》</span>
                <a href="${pageContext.request.contextPath }/system/downGuide" style="color: #4381e6;">下载</a>
                <i class="date">2018-11-22</i>
	            </li>
	            <li>
	              <span>2.新手指引，操作视频更新</span>
	              <i class="date">2018-03-22</i>
	            </li>
	            <li>
	              <span>3.新手指引，操作视频更新</span>
	              <i class="date">2018-03-22</i>
	            </li>
	          </ul>
	        </div>
	      </div>
	      <!-- l2.做账列表 -->
	      <div class="main-table cl">
	        <div class="main-search">
	          <input id="main-query" type="text" placeholder="请输入企业名称/首字母" v-model="keyWrold" @keyup.enter="searchUserList()">
	          <button id="main-search" @click="searchUserList()">查询</button>
	          <button id="main-allot" v-if="isShowAdmin" @click="distribution">分配</button>
	        </div>
	        <table class="layui-table layui-form" lay-filter="output">
	          <colgroup>
	            <col width="5%">
	            <col width="5%">
	            <col width="22%">
	            <col width="10%">
	            <col width="8%">
	            <col width="8%">
	            <col width="5%">
	            <col width="5%">
	            <col width="6%">
	          </colgroup>
	          <thead>
	            <tr>
	              <th>
	                <div class="layui-unselect layui-form-checkbox" lay-skin="primary" @click="checkPageAll" :class="isCheckAll? 'layui-form-checked':''">
	                	<i class="layui-icon layui-icon-ok"></i>
	                </div>
	              </th>
	              <th>序号</th>
	              <th>企业名称</th>
	              <th>手机号码</th>
	              <th>启用日期</th>
	              <th>处理人</th>
	              <th>状态</th>
	              <th>进度</th>
	              <th>操作</th>
	            </tr>
	          </thead>
	          <tbody class="main-tbody " id="mianTbody">
              <tr v-if="userListData.length > 0" v-for="(item,index) in userListData"
                :data-id="item.accountID"
                :class="choseTrIndex === index && isNoOnSearch? 'thisClass':''">
	              <td class="first-td">
	                <div class="layui-unselect layui-form-checkbox" :class="item.isCheck? 'layui-form-checked':''" lay-skin="primary" @click="checkTemplateItem(item)"><i
	                    class="layui-icon layui-icon-ok"></i></div>
	              </td>
	              <td class="dataName">{{ (curPage - 1) * 10 + index + 1}}</td>
	              <td class="dataName tl">{{item.cusName}}</td>
	              <td class="dataName">{{item.telephone}}</td>
	              <td class="dataName">{{item.period}}</td>
	              <td class="dataName">{{item.clName}}</td>
	              <td class="dataName">
	                {{item.statu === '1'? '正常' : item.statu === '2'? '已禁用' : '未开启'}}
	              </td>
	              <td class="dataName">
	                <div class="layui-progress" lay-showpercent="true" lay-filter="val1">
	                  <div class="layui-progress-bar layui-bg-blue" lay-percent="0%" style="width: 0%;">
	                    <span class="layui-progress-text">
	                      {{item.jz === 1 ? '100%' : item.cs === 1 ? '75%' : item.cv === 1 ? '50%' : '0%' }}
	                    </span>
	                  </div>
	                </div>
	              </td>
	              <td class="main-button">
                  <div
                    :class="[{'cu':item.accountID != currAccountID }, 'getHref main-button-css']"
                    @click="choseTrItem(index,item.accountID)"
                    :data-subid="item.accountID"
                  >{{item.accountID === currAccountID ? '当前账套' : '记账'}}</div>
	              </td>
	            </tr>
	            <tr v-if="userListData.length == 0" class="tc">
	              <td colspan="9" style="height: 80px;font-size:18px">暂无数据</td>
	            </tr>
	          </tbody>
	        </table>
	        <div class="layui-table-page">
	          <div id="layui-table-page2"></div>
	        </div>
	      </div>
      </div>
      <%-- 记账 --%>
      <div id="JZview" v-else-if="userType==='3' || userType==='6'">
      	<div class="JZview_left">
      		<div class="JZviewName">公司业务趋势</div>
      		<div id="businessLine"></div>
      	</div>
      	<div class="JZview_right">
      		<div class="main-title">
	          <h2>系统公告</h2>
	        </div>
          <ul class="main-list main-centen">
            <li style="overflow: hidden;height: 40px;">
              <span>1.新手指引，《用户操作指南》</span>
              <a href="${pageContext.request.contextPath }/system/downGuide" style="color: #4381e6;">下载</a>
              <i class="date">2018-11-22</i>
            </li>
            <li>
              <span>2.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
            <li>
              <span>3.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
            <li>
              <span>4.新手指引，操作视频更新</span>
              <i class="date">2018-03-22</i>
            </li>
          </ul>
      	</div>
      	<a href="https://www.wqbol.com/home/finance" target="_blank" class="advertising"></a>
      	<div class="Copyright">
      		<p>Copyright © 2017-2018 深圳微企宝计算机系统有限公司</p>
      		<p>粤ICP备16123131号-1</p>
      	</div>
      </div>
      <!-- 分配弹窗 -->
      <div class="layui-form" action="" id="distribPopup">
        <div class="layui-inline">
          <label class="layui-form-label">分配给：</label>
          <div class="layui-input-inline">
            <select name="modules" lay-verify="required" lay-filter="chcek_id" lay-search="">
              <option value="">直接选择或搜索选择</option>
              <option v-for="(item,index) in distrUserListData" :value="item.userID" :key="index">{{item.userName}}（{{item.loginUser}}）</option>
            </select>
          </div>
        </div>
      </div>
    </template>
  </div>
  <%@ include file="../voucher/import-popup.jsp" %>

  <script src="${pageContext.request.contextPath }/plugins/vue-2.5.13.min.js"></script>
  <script src="${pageContext.request.contextPath }/plugins/echarts.common.min.js"></script>
  <%@ include file="../js.jspf" %>
  <script src="${pageContext.request.contextPath }/js/setTing/main.js?v=${timeStamp }"></script>
</body>
</html>
