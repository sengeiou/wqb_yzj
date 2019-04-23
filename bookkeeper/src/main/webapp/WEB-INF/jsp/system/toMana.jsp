<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <link rel="shortcut icon" type="image/x-icon" href="/wqbmana/img/favicon.ico" media="screen">
  <title>微企宝-无账套</title>
  <style>
    * {
        margin: 0;
        padding: 0;
    }

    a {
        text-decoration: none;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    }

    h1 {
        margin: 100px 0;
        font-size: 20px;
        text-align: center;
    }

    .layui-btn {
        display: block;
        position: relative;
        margin: 0 auto;
        width: 304px;
        height: 38px;
        font-size: 18px;
        line-height: 38px;
        text-align: center;
        vertical-align: middle;
        letter-spacing: 2px;
        border: 1px solid #0f9abd;
        -webkit-border-radius: 4px;
        border-radius: 4px;
        color: #fff;
        background: #25a8e0;
        cursor: pointer;
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
    }

    .layui-btn:hover {
        opacity: 0.8;
        filter: alpha(opacity=80);
        color: #fff;
    }

    .layui-btn:active {
        opacity: 1;
        filter: alpha(opacity=100);
    }
  </style>
</head>

<body>
  <h1>您的可用账套为空，请先设置账套</h1>
  <a class="layui-btn" href="https://acctmana.wqbol.net/wqbmana/system/toLogin">去设置</a>
</body>

</html>
