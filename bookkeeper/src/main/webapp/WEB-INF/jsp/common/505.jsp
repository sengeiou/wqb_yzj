<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>505错误</title>
<link href="${pageContext.request.contextPath}/css/err.css" rel="stylesheet" type="text/css" />
</head>

<body style="background: #edf6fa;">
	<div class="place">
	</div>

	<div class="error">
		<h2>非常遗憾，您访问的页面不存在！</h2>
	<%-- 	<div class="reindex">
			<a href="${pageContext.request.contextPath}/" target="_parent">返回首页</a>
		</div> --%>
	</div>
	
	<script src="${pageContext.request.contextPath}/plugins/jquery-1.12.4.min.js"></script>
	<script language="javascript">
		$(function() {
			$('.error').css({
				'position' : 'absolute',
				//'left' : ($(window).width())/ 2
				 'margin':'auto'
			});
			$(window).resize(function() {
				$('.error').css({
					'position' : 'absolute',
					/* 'left' : ($(window).width())/ 2 */
					 'margin':'auto'
				});
			})
		});
	</script>
</body>
</html>