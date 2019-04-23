<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <meta charset="UTF-8">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
  <title>凭证打印</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath }/plugins/layui/css/layui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/css/print-voucher.css?v=20190227">
  <style>
    .print-content{
    	margin: 20px auto;
    	padding-left: 20px;
    }
    .print-content .head-title h2{
    	margin: 20px 0;
    }
    .print-content table tr{
    	height: 25px;
    }
  </style>
</head>

<body>
  <div class="layui-form option">
    <div class="layui-form-item">
      <label class="layui-form-label">打印设置：</label>
      <div class="layui-input-block">
        <input type="radio" name="type" value="发票版针式" title="发票版 针式打印" checked>
        <input type="radio" name="type" value="发票版纵向" title="发票版 纵向打印">
        <%-- <input type="radio" name="type" value="A4" title="A4">
        <input type="radio" name="type" value="A5" title="A5">
        <input type="radio" name="type" value="B5" title="B5"> --%>
        <input type="radio" name="type" value="自定义" title="自定义纸张、方向">
      </div>
    </div>
  </div>
  <div id="print-content"></div>

<script>
  var baseURL = '${pageContext.request.contextPath }'; // api请求路径(项目名称)
</script>
<script src="${pageContext.request.contextPath }/plugins/layui/layui.js"></script>
<script src="${pageContext.request.contextPath }/plugins/LodopFuncs.js?v=20190227"></script>
<script src="${pageContext.request.contextPath }/js/print/print-voucher.js?v=20190227"></script>
</body>

</html>
