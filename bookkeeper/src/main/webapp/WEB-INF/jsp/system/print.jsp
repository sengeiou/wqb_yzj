<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <meta charset="UTF-8">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
  <title>打印预览</title>
</head>

<body>
  <div id="print-content"></div>

  <script>
    var baseURL = '${pageContext.request.contextPath }'; // api请求路径(项目名称)
  </script>
  <script src="${pageContext.request.contextPath }/js/print/print-report.js?v=${timeStamp }"></script>
</body>

</html>
