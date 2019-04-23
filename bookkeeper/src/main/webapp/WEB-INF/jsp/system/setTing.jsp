<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html>

    <head>
      <%@ include file="../head.jspf" %>
        <title>设置</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/setTing.css">
    </head>

    <body>
      <!-- Start #content -->
      <!-- 内容主体区域 -->
      <!-- 右侧 内容包含区 -->
      <div class="layui-body" id="content" style="padding-top: 20px !important;">
        <div class="layui-tab layui-tab-brief" lay-filter="setTingTab">
          <ul class="layui-tab-title fixed-nav" id="navDOM1">
            <li class="layui-this">科目设置</li>
            <li>计量单位设置</li>
            <li>汇率设置</li>
            <!-- <li>单据设置</li> -->
          </ul>
          <span class="btn-toolbar fixed-icon">
            <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
          </span>

          <div class="layui-tab-content">
            <!-- 科目设置 -->
            <div class="layui-tab-item layui-show">
              <!--  搜索 -->
              <div class="search fixed-search" id="searchDOM1">
                <div class="search-item">
                  <label for="keyWord">
                    <input type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord" id="set_val">
                  </label>
                </div>
                <div class="layui-btn layui-btn-normal" id="set_data_btn">查询</div>
              </div>
              <div class="fixed-head" id="fixedDOM1">
                <div class="layui-tab layui-tab-card" lay-filter="subMessage">
                  <ul class="layui-tab-title" id="set-fixedHead">
                    <li class="layui-this">资产</li>
                    <li>负债</li>
                    <li>共同</li>
                    <li>权益</li>
                    <li>成本</li>
                    <li>损益</li>
                  </ul>
                  <table class="layui-table">
                    <colgroup>
                      <col width="12%">
                      <col width="30%">
                      <col width="30%">
                      <col width="6%">
                      <col width="10%">
                      <col width="12%">
                    </colgroup>
                    <thead>
                      <tr>
                        <th>科目编码</th>
                        <th>科目名称</th>
                        <th>科目全称</th>
                        <th>方向</th>
                        <th>状态</th>
                        <th>操作</th>
                      </tr>
                    </thead>
                  </table>
                </div>
              </div>
              <div class="layui-tab layui-tab-card mt20" lay-filter="subMessage" id="subMessage">
                <div class="layui-tab-content" id="setDataT">
                  <table class="layui-table" style="margin: 10px 0 0;">
                    <colgroup>
                      <col width="12%">
                      <col width="30%">
                      <col width="30%">
                      <col width="6%">
                      <col width="10%">
                      <col width="12%">
                    </colgroup>
                    <tbody class="set_tbost" id="set_tbost"></tbody>
                  </table>
                </div>
              </div>
            </div>

            <!-- 计量单位设置 -->
            <div class="layui-tab-item">
              <!--  搜索 -->
              <div class="search fixed-search" id="searchDOM2">
                <div class="search-item">
                  <label for="keyWord">
                    <input type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord" id="symbolOrName">
                  </label>
                </div>
                <div class="layui-btn layui-btn-normal" id="searchName">查询</div>
              </div>

              <div class="fixed-head" id="fixedDOM2">
                <table class="layui-table">
                  <colgroup>
                    <col width="5%">
                    <col width="20%">
                    <col width="20%">
                    <col width="15%">
                    <col width="15%">
                    <col width="30%">
                  </colgroup>
                  <thead>
                    <tr>
                      <th>编号</th>
                      <th>计量单位名称</th>
                      <th>计量单位符号</th>
                      <th>创建日期</th>
                      <th>修改日期</th>
                      <th>备注</th>
                      <!-- <th>操作</th> -->
                    </tr>
                  </thead>
                </table>
              </div>
              <div id="accountDataT">
                  <table class="layui-table">
                    <colgroup>
                      <col width="5%">
                      <col width="20%">
                      <col width="20%">
                      <col width="15%">
                      <col width="15%">
                      <col width="30%">
                    </colgroup>
                    <tbody class="set_unit" id="ser_account">

                    </tbody>
                  </table>
              </div>
            </div>


            <!-- 汇率设置 -->
            <div class="layui-tab-item">
              <!--  搜索 -->
              <div class="search fixed-search" id="searchDOM3">
                <div class="search-btn">
                  <div class="layui-btn layui-btn-normal set_item_add">新增</div>
                </div>
              </div>

              <div class="fixed-head" id="fixedDOM3">
                <table class="layui-table">
                  <colgroup>
                    <col width="14%">
                    <col width="14%">
                    <col width="14%">
                    <col width="14%">
                    <col width="14%">
                    <col width="14%">
                    <col width="14%">
                  </colgroup>
                  <thead>
                    <tr>
                      <th>币种符号</th>
                      <th>币种名称</th>
                      <th>默认币种</th>
                      <th>汇率</th>
                      <th>修改日期</th>
                      <th>备注</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                </table>
              </div>
              <table class="layui-table">
                <colgroup>
                  <col width="14%">
                  <col width="14%">
                  <col width="14%">
                  <col width="14%">
                  <col width="14%">
                  <col width="14%">
                  <col width="14%">
                </colgroup>
                <thead>
                  <tr>
                    <th>币种符号</th>
                    <th>币种名称</th>
                    <th>默认币种</th>
                    <th>汇率</th>
                    <th>修改日期</th>
                    <th>备注</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody class="set_unit" id="set_unit"></tbody>
              </table>
            </div>

            <!-- 单据设置 -->
            <div class="layui-tab-item">
              <!--  搜索 -->
              <div class="search fixed-search" id="searchDOM4" style="min-width:0;">
                <div class="search-btn">
                  <div class="layui-btn layui-btn-normal" id="add-doc">新增</div>
                  <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
                </div>
              </div>

              <div class="layui-form table-box">
                <div class="fixed-head" id="fixedDOM4">
                  <table class="layui-table">
                    <colgroup>
                      <col width="5%">
                      <col width="15%">
                      <col width="15%">
                      <col width="18%">
                      <col width="16%">
                      <col width="16%">
                      <col width="15%">
                    </colgroup>
                    <thead>
                      <tr>
                        <th>
                          <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                        </th>
                        <th>项目编码</th>
                        <th>项目名称</th>
                        <th>单据摘要</th>
                        <th>借方科目</th>
                        <th>贷方科目</th>
                        <th>操作</th>
                      </tr>
                    </thead>
                  </table>
                </div>
                <table class="layui-table" id="acc-doc">
                  <colgroup>
                    <col width="5%">
                    <col width="15%">
                    <col width="15%">
                    <col width="18%">
                    <col width="16%">
                    <col width="16%">
                    <col width="15%">
                  </colgroup>
                  <thead>
                    <tr>
                      <th>
                        <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                      </th>
                      <th>项目编码</th>
                      <th>项目名称</th>
                      <th>单据摘要</th>
                      <th>借方科目</th>
                      <th>贷方科目</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody class="" id="doc-list"></tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- End #content -->

      <!-- 新增计量单位 -->
      <div id="set_alert-addForm">
        <ul class="set_alert-unit">
          <li style="margin-top: 20px;" id="set_none">
            <lable id="unit_num">币种简称</lable>
            <input type="text" id="set_unit_num" placeholder="币种简称">
          </li>
          <li>
            <lable id="unit_sign">币种名称</lable>
            <input type="text" id="set_unit_sign" placeholder="币种名称">
          </li>
          <li>
            <lable id="unit_name">汇率</lable>
            <input type="text" id="set_unit_nane" placeholder="汇率">
          </li>
          <li>
            <lable>备注 ：</lable>
            <textarea name="desc" placeholder="请输入备注" class="layui-textarea" id="set_unit_remark"></textarea>
          </li>
        </ul>
      </div>


      <div id="modify" class="modify" style="display:noen">
        <div class="search">
          <div class="search-item layui-form">
            <select lay-search lay-filter="subList" id="modify-sub">
              <option value="">请选择科目</option>
            </select>
          </div>
          <div class="search-item layui-form search-symnol">
            <select lay-search lay-filter="symbol" id="modify-symbol">
              <option value="">请选择运算符</option>
            </select>
          </div>
          <div class="search-item layui-form">
            <select lay-search lay-filter="rule" id="modify-rule">
              <option value="">请选择取现规则</option>
            </select>
          </div>
          <div class="search-btn">
            <div class="layui-btn layui-btn-normal " id="modify-add">添加</div>
          </div>
        </div>
        <div>
          <div class="layui-tab-item layui-show">
            <table class="layui-table ">
              <colgroup>
                <col width="40%">
                <col width="10%">
                <col width="30%">
                <col width="20%">
              </colgroup>
              <thead>
                <tr>
                  <th>科目</th>
                  <th>运算符</th>
                  <th>取现规则</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="modify-item">
                <tr>
                  <td>1001</td>
                  <td>+</td>
                  <td>本期发生额</td>
                  <td>
                    <i class="layui-icon removeIcon">&#x1006;</i>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 单据项目 -->
      <div class="popup" id="acc-doc-popup">
        <ul class="layui-form">
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>项目类别</label>
            <select name="" id="" lay-search="" lay-filter="component" lay-verify="required">
              <option value="FYPXJZF">费用票（现金支付）</option>
              <option value="CGP">采购票</option>
              <option value="XSSRP">销售收入票</option>
              <option value="YHLS">银行流水</option>
            </select>
          </li>
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>项目名称</label>
            <input type="text" class="layui-input nospace" name="" id="" placeholder="请输入1-20个字符" maxlength="20">
          </li>
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>项目编码</label>
            <input type="text" class="layui-input " name="" id="" placeholder="请输入1-20个字符" maxlength="20">
          </li>
          <li class="popup-item">
            <label class="popup-text">单据摘要</label>
            <input type="text" class="layui-input " name="" id="" placeholder="请输入1-20个字符" maxlength="20">
          </li>
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>上级项目编码</label>
            <select name="" id="" lay-search="" lay-filter="component" lay-verify="required">
              <option value="FYPXJZF">费用票（现金支付）</option>
              <option value="CGP">采购票</option>
              <option value="XSSRP">销售收入票</option>
              <option value="YHLS">银行流水</option>
            </select>
          </li>
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>借方科目</label>
            <input type="text" class="layui-input " name="" id="" placeholder="请选择科目" readonly>
          </li>
          <li class="popup-item">
            <label class="popup-text">
              <span class="asterisk">*</span>贷方科目</label>
            <input type="text" class="layui-input " name="" id="" placeholder="请选择科目" readonly>
          </li>
        </ul>
      </div>


      <%@ include file="../voucher/import-popup.jsp" %>

        <%@ include file="../js.jspf" %>
          <script src="${pageContext.request.contextPath }/js/setTing/setTing.js?v=${timeStamp }"></script>
          <script src="${pageContext.request.contextPath }/js/addSub.js?v=${timeStamp }"></script>
          <script src="${pageContext.request.contextPath }/plugins/echarts.common.min.js"></script>
    </body>


    </html>
