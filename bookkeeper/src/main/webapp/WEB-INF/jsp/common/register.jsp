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
		.btn{
			display: block;
	    margin: 45px auto;
	    height: 45px;
	    width: 120px;
	    text-align: center;
	    border: 1px solid #eeeef3;
	    line-height: 45px;
	    border-radius: 5px;
	    cursor: pointer;
	    text-decoration: none;
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
		.guoqi{
			text-align: center;
			color: red;
			font-style: italic;
			z-index: 100;

		}
	</style>
</head>

<body>



	<div class="register">
		<c:if test="${empty errors }">
				<h5 id="prompts" class="guopqi">您的身份已过期，请重新登录</h5>&nbsp;&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${not empty  errors }">
			<h6  id="prompts">${errors }</h6>&nbsp;&nbsp;&nbsp;&nbsp;
			<!-- <span><a class="ter">重新登录</a><a id="daoshu"></a></span> -->

		</c:if>


		<a class="btn">重新登录<span id="daoshu">2</span></a>

	</div>

	<script>
		var hostname = location.hostname;
		var i = 1;
		var intervalid;
		intervalid = setInterval("fun()", 1000);
		function fun() {
			if (i == 0) {
				document.querySelector('a.btn').click();
				// if(hostname =="localhost" || hostname.indexOf("192.168")!=-1){
				// 	top.location.href = "${pageContext.request.contextPath }/system/toLogin";
				// }else if(hostname =="acct.wqbol.net"){
				// 	top.location.href = "//net.wqbol.net/loginAndRegister/login";
				// }else if(hostname =="www.wqbol.com"){
				// 	top.location.href = "//www.wqbol.com/loginAndRegister/login";
				// }else if(hostname == "acct.wqbol.com"){
				// 	top.location.href = "//www.wqbol.com/loginAndRegister/login";
				// }
				clearInterval(intervalid);
			}
			document.getElementById("daoshu").innerHTML = i;
			i--;
		}

		document.querySelector("a.btn").addEventListener('click', function(){
			top.location.href = "${pageContext.request.contextPath }/system/toLogin";
		});
	</script>
</body>

</html>
