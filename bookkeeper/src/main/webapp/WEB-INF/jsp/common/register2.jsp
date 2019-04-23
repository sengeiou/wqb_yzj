<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
	<title>页面过期</title>
	<style>
		body{
			background-image:url(../img/guoqi.png);
			background-repeat: no-repeat;
	  	background-size: 100% ;
	  	overflow: hidden;
		}
		.register{
			height: 200px;
			width: 500px;
			margin: 24% auto;
			background: #f3efef;
		}
		h2{
			text-align: center;
	   	color: #676565;
		}
		span{
			display: block;
	    margin: 45px auto;
	    height: 45px;
	    width: 120px;
	    text-align: center;
	    border: 1px solid #eeeef3;
	    line-height: 45px;
	    border-radius: 5px;
	    cursor: pointer;
	    background: #1E9FFF;
	    color: #fff;
		}
		#daoshu{
	    margin-left:20px;
	    height: 20px;
	    width: 40px;
	    text-align: center;
	    line-height: 20px;
	    color: #666666 ;
		}
	</style>
</head>

<body>
	<div class="register">
		<c:if test="${empty errors }">
				<h2>您的身份已过期，请重新登录</h2>
				<span><a class="ter">重新登录</a><a id="daoshu"></a></span>
				&nbsp;&nbsp;&nbsp;&nbsp;
		</c:if>

		<c:if test="${not empty  errors }">
			alert(${errors });
			<h6>${errors }</h6>

			<span><a class="ter">重新登录</a><a id="daoshu"></a></span>
			&nbsp;&nbsp;&nbsp;&nbsp;

		</c:if>






	</div>
	<script src="${pageContext.request.contextPath }/plugins/jquery-1.12.4.min.js"></script>
	<script>
		var hostname = location.hostname;
		var i = 3;
		var intervalid;
		intervalid = setInterval("fun()", 1000);
		function fun() {
			if (i == 0) {
				if(hostname =="localhost" || hostname.indexOf("192.168")!=-1){
					top.location.href = "${pageContext.request.contextPath }/system/toLogin";
				}else if(hostname =="acct.wqbol.net"){
					top.location.href = "//net.wqbol.net/loginAndRegister/login";
				}else if(hostname =="www.wqbol.com"){
					top.location.href = "//www.wqbol.com/loginAndRegister/login";
				}else if(hostname == "acct.wqbol.com"){
					top.location.href = "//www.wqbol.com/loginAndRegister/login";
				}
			clearInterval(intervalid);
			}
			document.getElementById("daoshu").innerHTML = i;
			i--;
		}



		$(".register span").on('click', function(){
			if(hostname =="localhost"){
				//$('.ter').attr('href',"${pageContext.request.contextPath }/system/toLogin");
				top.location.href = "${pageContext.request.contextPath }/system/toLogin";
			}else if(hostname =="acct.wqbol.net"){
				//$('.ter').attr('href','//net.wqbol.net/loginAndRegister/login');
				top.location.href = "//net.wqbol.net/loginAndRegister/login";
			}else if(hostname =="www.wqbol.com"){
				//$('.ter').attr('href','//www.wqbol.com/loginAndRegister/login');
				top.location.href = "//www.wqbol.com/loginAndRegister/login";
			}else if(hostname == "acct.wqbol.com"){
				$('.ter').attr('href','//www.wqbol.com/loginAndRegister/login');
				top.location.href = "//www.wqbol.com/loginAndRegister/login";
			}
		})
	</script>
</body>
</html>
