<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script>
  var baseURL = '${pageContext.request.contextPath }'; // api请求路径(项目名称)
  var manaBaseURL = 'http://localhost:18080/wqbmana'; // 后台管理系统请求路径
</script>
<%-- <c:set var="manaBaseURL" scope="session" value="https://acctmana.wqbol.com/wqbmana"/> --%>
<c:set var="manaBaseURL" scope="session" value="http://localhost:18080/wqbmana"/>
<%--<c:set var="timeStamp" scope="session" value="<%=System.currentTimeMillis() / 1000%>"/>--%>
<c:set var="timeStamp" scope="session" value="1"/>