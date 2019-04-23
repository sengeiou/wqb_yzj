<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath }/plugins/jquery-1.12.4.min.js"></script>
<title>Insert title here</title>
 <style type="text/css">

 	  table.hovertable {
	      font-family: verdana,arial,sans-serif;
	      font-size:11px;
	      color:#333333;
	      border-width: 1px;
	      border-color: #999999;
	      border-collapse: collapse;
	 }

	 table.hovertable th {
	    background-color:#c3dde0;
	     border-width: 1px;
	     padding: 8px;
	     border-style: solid;
	     border-color: #a9c6c9;
	 }
	 table.hovertable tr {
	     background-color:#d4e3e5;
	 }
	 table.hovertable td {
	     border-width: 1px;
	     padding: 8px;
	     border-style: solid;
	     border-color: #a9c6c9;
	 }
	#zz{
		margin: 20px;
		font-size: 30px;
	}

 </style>

</head>
<body>
		<div>
			<p>ceshi--
				<c:out value="${ceshi }"></c:out>
				<c:out value="${-1>0 or 7>0 }">111</c:out>
			</p>
			<p>
				<c:forEach items="${msg}" var="arr">
					<hr></hr> --%>
				</c:forEach>
			</p>
		</div>
	<div  style="margin: 50px">
		<table class="hovertable" border="1" bordercolor="#000000" width="50%" height="100%" cellpadding="0" cellspacing="0">
			<caption align="top" id="zz">总账</caption>
			<thead>
				 	<th>科目编码</th>
	      			<th>科目名称</th>
	      			<th>期间</th>
	      			<th>摘要</th>
	      			<th>借方</th>
	      			<th>贷方</th>
	      			<th>方向</th>
	      			<th>余额</th>
			</thead>
			<c:forEach items="${msg}" var="arr">
				<c:if test="${! empty arr.list}">
				<tbody   class="tdh">
				<c:forEach items="${arr.list}" var="aa"  varStatus="status">
					<c:if test="${status.index eq 0 }">
						<tr>
							<td  rowspan="${fn:length(arr.sub_list)}">${arr.sub_code }</td>
							<td  rowspan="${fn:length(arr.sub_list)}">${arr.sub_name }</td>
							<td>${aa.account_period }</td>
							<td>${aa.zhaiYao }</td>
							<td>
								<c:if test="${aa.jf_amount gt 0 or aa.jf_amount lt 0}">
									${aa.jf_amount}
								</c:if>
							</td>
							<td>
								<c:if test="${aa.df_amount gt 0 or aa.df_amount lt 0}">
									${aa.df_amount }
								</c:if>
							</td>
							<td>${aa.fx_jd }</td>
							<td>
								<c:if test="${aa.amount gt 0 or aa.amount lt 0}">
									${aa.amount }
								</c:if>
							</td>
						</tr>
					</c:if>
					<c:if test="${status.index gt 0 }">
						<tr>
							<td>${aa.account_period }</td>
							<td>${aa.zhaiYao }</td>
							<td>
								<c:if test="${aa.jf_amount gt 0 or aa.jf_amount lt 0}">
									${aa.jf_amount}
								</c:if>
							</td>
							<td>
								<c:if test="${aa.df_amount gt 0 or aa.df_amount lt 0}">
									${aa.df_amount }
								</c:if>
							</td>
							<td>${aa.fx_jd }</td>
							<td>
								<c:if test="${aa.amount gt 0 or aa.amount lt 0}">
									${aa.amount }
								</c:if>
							</td>
						</tr>

					</c:if>
					</c:forEach>
				</tbody>
				</c:if>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
	  $(".tdh").on({
		    mouseover:function(){
		    	$(this).find("tr").css("background-color","lightgray");
		    	},
		    mouseout:function(){
		    	$(this).find("tr").css("background-color","#d4e3e5");
		    }
		 });
	</script>
</body>
</html>
