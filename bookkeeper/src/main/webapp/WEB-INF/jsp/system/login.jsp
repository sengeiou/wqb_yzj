<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="java.lang.String" %>
<%@ page language="java" import="java.net.URLDecoder" %>
<%@ page language="java" import="com.wqb.common.Base64" %>
<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="keywords" content="微企宝">
  <meta http-equiv="description" content="This is my page">
  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/plugins/layui/css/layui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/css/common.css">
  <title>微企宝-登录</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath }/css/login.css">
</head>

<body>
<%-- <div class="login_mian">
  <div class="login_name">微企宝智能云代账系统</div>
  <div class="login-formY"></div>
  <div class="login-form">
    <h1 class="tc"></h1>
    <div class="login-box">
      <div class="login-item item-input">
        <label for="login-name" class="login-label name-label"></label>
        <input id="login-name" type="text" class="itxt" name="userName" autocomplete="off" placeholder="用户名" maxlength="11">
        <span class="clear-btn"></span>
      </div>
      <div class="login-item item-input">
        <label for="login-pwd" class="login-label pwd-label"></label>
        <input type="password" id="login-pwd" name="password" class="itxt" autocomplete="off" placeholder="密码" maxlength="16">
        <span class="clear-btn"></span>
        <span class="capslock dn"><b></b>${mes }</span>
      </div>
      <!-- 会计期间 -->
      <div class="login-item item-input" style="display: none;">
        <label for="busDate" class="login-label flex date-label"><i class="layui-icon">&#xe637;</i></label>
        <input id="busDate" type="text" class="itxt" name="busDate" autocomplete="off" placeholder="会计期间" maxlength="7">
        <span class="clear-btn"></span>
      </div>
      <div class="login-item mb5">
        <span class="login-btn layui-btn layui-btn-normal layui-unselect" id="loginsubmit">登&nbsp;&nbsp;&nbsp;&nbsp;录</span>
      </div>
    </div>
    <div class="login-item login-safe">
      <div class="safe safeL">
        <span class="forget-pw-safe">
          <a href="" class="Wjpassword" id="Wjpassword">忘记密码</a>
        </span>
      </div>
      <div class="safeR">
        <span class="forget-pw-safe">
          <a href="" class="MFregister" id="MFregister">免费注册</a>
          <!--<a href="" class="" target="" clstag="pageclick|keycount|201607144|3">免费注册</a>-->
        </span>
      </div>
    </div>
  </div>
  <p class="dibu">Copyright © 2017-2018 深圳微企宝计算机系统有限公司  粤ICP备16123131号-1</p>
</div> --%>
<div class="login-layout">

  <div class="login-form">
    <h1>微企宝智能云代账系统</h1>
    <h2>欢迎使用微企宝智能云代账系统平台，三分钟一套账，智能批量报税，让您的工作更轻松更高效！</h2>
		<!--/wqb/system/accountLogin?userID=43999ab6a9a0459999efe63de1e805f5-->
    <div class="login-box">
      <form action="${pageContext.request.contextPath }/system/login" method="post" id="accountLogin">
        <%
          	String value=request.getParameter("errors");
       		if(value!=null){
        		out.print("<h5 style='color: red;margin-bottom: 10px'>"+ URLDecoder.decode(value,"utf-8") +"</h5>");
        	}

     /*     if(value!=null){
        	value=value.replaceAll(" ", "+");
        	value=value.replaceAll("\n", "").trim();
        	value = Base64.getFromBase64(value);
        	);
          } */
        %>
        <div class="login-item item-input">
          <label for="login-name" class="login-label name-label"></label>
          <input id="login-name" type="text" class="itxt" name="loginUser" autocomplete="off" onKeyup="getContent(this);" placeholder="用户名" maxlength="11">
          <span class="clear-btn"></span>
          <p class="remark"></p>
        </div>
        <div class="login-item item-input">
          <label for="login-pwd" class="login-label pwd-label"></label>
          <input type="password" id="login-pwd" name="password" class="itxt" autocomplete="off" placeholder="密码" maxlength="16">
          <span class="clear-btn"></span>
          <span class="capslock dn"><b></b></span>
          <p class="remark"></p>
        </div>
        <%-- 会计期间 -->
        <div class="login-item item-input" style="display: none;">
          <label for="busDate" class="login-label flex date-label"><i class="layui-icon">&#xe637;</i></label>
          <input id="busDate" type="text" class="itxt" name="busDate" autocomplete="off" placeholder="会计期间" maxlength="7">
          <span class="clear-btn"></span>
        </div> --%>
        <div class="login-item submit">
          <span class="login-btn layui-btn layui-btn-normal layui-unselect" id="loginsubmit">登&nbsp;&nbsp;&nbsp;&nbsp;录</span>
          <span class="login-btn layui-btn layui-btn-normal layui-unselect" id="JZloginsubmit">记账登录</span>
          <span class="login-btn layui-btn layui-btn-normal layui-unselect" id="DZloginsubmit">代账登录</span>
          <span class="login-btn layui-btn layui-btn-normal layui-unselect" id="YGloginsubmit">员工登录</span>
        </div>
        <input type="submit" class="dn" id="submit" value="提交" />
      </form>
    </div>

    <!--<div class="login-safe cl">
      <span class="safe-item">
        <a class="Wjpassword cu" id="Wjpassword">忘记密码</a>
      </span>
      <span class="safe-item">
        <a class="MFregister cu" id="MFregister">免费注册</a>
      </span>
    </div>-->
  </div>
  <p class="foot">Copyright&nbsp;&copy;&nbsp;2017-2018&nbsp;深圳微企宝计算机系统有限公司&nbsp;&nbsp;粤ICP备16123131号-1</p>
</div>

<script>
  var baseURL = '${pageContext.request.contextPath }'; // api请求路径(项目名称)
</script>
<script src="${pageContext.request.contextPath }/plugins/jquery-1.12.4.min.js"></script>
<script src="${pageContext.request.contextPath }/plugins/layui/layui.all.js"></script>
<script src="${pageContext.request.contextPath }/js/login.js?v=${timeStamp }"></script>
</body>

</html>
