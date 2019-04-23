<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

	/* 添加属性样式 */
	/* table tr:nth-child(odd){
		background: #ccc;
	} */

	/* 当需要偶数行背景时使用样式： */
	/* table tr:nth-child(even){
		background: #ccc;
	} */

	/* 隔行变色 */

	table tr:nth-child(odd){background:#F4F4F4;}
	table td:nth-child(even){color:#C00;}
	/* table tr:nth-child(5){background:#73B1E0;color:#FFF;} */


 </style>

</head>

<body>


		<div>
			<p>ceshi--
				<c:out value="${ceshi }"></c:out>

				<c:out value="${-1>0 or 7>0 }">111</c:out>
			</p>
			<p>
				<span>${subName }</span>
				<span>${pageCount }</span>
				<span>${subCode }</span>
				<span>${code }</span>
			</p>
		</div>


	<div  style="margin: 50px">

		<table class="hovertable" border="1" bordercolor="#000000" width="50%" height="100%" cellpadding="0" cellspacing="0">
			<caption align="top" id="zz">明细账</caption>

			<thead>
				 	<th style="text-align: left;" colspan="8">科目${subCode} &nbsp;&nbsp;  ${subName}</th>

			</thead>

			<thead>
				 	<th>日期</th>
	      			<th>凭证字号</th>
	      			<th>期间</th>
	      			<th>摘要</th>
	      			<th>借方</th>
	      			<th>贷方</th>
	      			<th>方向</th>
	      			<th>余额</th>
			</thead>



			<c:forEach items="${msg}" var="arr">

					<c:if test="${! empty arr.list}">

							<c:forEach items="${arr.list}" var="aa"  varStatus="status">

								<tr class="tdh">
									<td >
										<fmt:formatDate value="${aa.updateDate }" pattern="yyyy-MM-dd"/>
									</td>
									<td>
										<c:if test="${!empty aa.vouchNum }">
											<a href="{pageContext.request.contextPath }/aaa/bbb?vouchID="+${aa.vouchID }>记-${aa.vouchNum }</a>
										</c:if>

									</td>

									<td>${aa.period }</td>
									<td>
										<c:if test="${!empty arr.vouchNum }">
											${aa.vcabstact }
										</c:if>
										<c:if test="${empty arr.vouchNum }">
											&nbsp;&nbsp;&nbsp;&nbsp;${aa.vcabstact }
										</c:if>

									</td>
									<td>
										<c:if test="${aa.debitAmount gt 0 or aa.debitAmount lt 0}">
											${aa.debitAmount}
										</c:if>
									</td>
									<td>
										<c:if test="${aa.creditAmount gt 0 or aa.creditAmount lt 0}">
											${aa.creditAmount }
										</c:if>
									</td>
									<td>${aa.direction }</td>
									<td>
										<c:if test="${aa.blanceAmount gt 0 or aa.blanceAmount lt 0}">
											${aa.blanceAmount }
										</c:if>

									</td>

								</tr>

							</c:forEach>
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
