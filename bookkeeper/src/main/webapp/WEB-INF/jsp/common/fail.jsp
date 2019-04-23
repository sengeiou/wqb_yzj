<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="${pageContext.request.contextPath}/css/err.css" rel="stylesheet" type="text/css" />
</head>


<body>
   <div class="place">
        <span>操作失败</span>
   </div>
    
    <div class="rightinfo">
        <div class="tools">     
            <ul class="seachform">
                <li>
                    <input id="cancel" type="button" class="scbtn" value="返回" />
                </li>
            </ul>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/plugins/jquery-1.12.4.min.js"></script>
    <script>
	    $(function() {
	    	//取消按钮初始化
	        $("#cancel").click(function(){
	            history.back(-1);
	        });
	    });
    </script>
</body>
</html>