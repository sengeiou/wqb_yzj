<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
  <%@ include file="../head.jspf" %>
  <title>理票——单据录入</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/nav.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/documents-import.css">
</head>

<body>
<!-- Start #content -->
<!-- 内容主体区域 -->
<div class="layui-body" id="content" style="top: 162px">
  <div class="layui-tab layui-tab-brief" lay-filter="tab">
    <ul class="layui-tab-title crumbs fixed-nav" id="navDOM1">
      <li>采购票(库存)</li>
      <li>采购票(原材料)</li>
      <li>销售收入票</li>
      <li class="layui-this">费用票（现金支付）</li>
      <li>单据录入列表</li>
    </ul>
    <span class="btn-toolbar fixed-icon">
      <i class="layui-icon cu" id="i-refresh">&#x1002;</i>
    </span>

    <!-- tab内容展示 -->
    <div class="layui-tab-content" id="tab-content">
      <!-- 采购票(原材料) -->
      <div class="layui-tab-item">
        <div class="layui-form">
          <!-- 搜索 -->
          <div class="search fixed-search" id="searchDOM1" style="padding: 6px 0 0px;">
            <div class="search-btn">
              <div class="layui-btn layui-btn-normal save_btn">保存</div>
              <div class="layui-btn layui-btn-normal add_btn">保存并新增</div>
              <div class="layui-btn layui-btn-normal reset_btn">重置表单</div>
            </div>
          </div>
          <div class="fixed-head" id="fixedDOM1">
          </div>
          <div class="p-rel mt20" id="form1">
            <h2>请选择付款方式科目代码</h2>
            <div class="layui-tab layui-tab-card proc-raw-l" lay-filter="subCode">
              <ul class="layui-tab-title">
                <li class="layui-this" data-code="2202" title="2202 应付账款">应付</li>
                <li data-code="1123" title="1123 预付账款">预付</li>
                <li data-code="1001" title="1001库存现金">现金</li>
                <li data-code="1002" title="1002 银行存款">银行</li>
              </ul>
              <div class="layui-tab-content" id="subCode-content1">
                <div class="layui-tab-item layui-show">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
              </div>
            </div>
            <div class="p-abs proc-raw-r">
              <div class="proc-title">
                <dt class="title-item tl">
                  <div class="layui-input-inline">
                    <input type="text" class="layui-input tickets-search" placeholder="请输入会计科目名称">
                  </div>
                </dt>
              </div>
              <div class="tickets-list">
                <dl class="proc-title">
                  <dt class="title-item w1">会计科目</dt>
                  <dt class="title-item w2">计量单位</dt>
                  <dt class="title-item w3">数量</dt>
                  <dt class="title-item w3">单价</dt>
                  <dt class="title-item w3">金额</dt>
                </dl>
                <div id="proc-raw-list">
                  <ul class="proc-list">
                    <li class="list-item tc"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 采购票(库存) -->
      <div class="layui-tab-item">
        <div class="layui-form">
          <!-- 搜索 -->
          <div class="search fixed-search" id="searchDOM2">
            <!-- <div class="search-item">
              <input type="text" class="layui-input digit-input" placeholder="请输入税金" name="taxAmount" maxlength="15" id="">
            </div> -->
            <div class="search-btn">
              <div class="layui-btn layui-btn-normal save_btn">保存</div>
              <div class="layui-btn layui-btn-normal add_btn">保存并新增</div>
              <div class="layui-btn layui-btn-normal reset_btn">重置表单</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM2">

          </div>
          <div class="p-rel mt20" id="form2">
            <h2>请选择付款方式科目代码</h2>
            <div class="layui-tab layui-tab-card proc-raw-l" lay-filter="subCode">
              <ul class="layui-tab-title">
                <li class="layui-this" data-code="2202" title="2202 应付账款">应付</li>
                <li data-code="1123" title="1123 预付账款">预付</li>
                <li data-code="1001" title="1001 库存现金">现金</li>
                <li data-code="1002" title="1002 银行存款">银行</li>
              </ul>
              <div class="layui-tab-content" id="subCode-content2">
                <div class="layui-tab-item layui-show">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
                <div class="layui-tab-item">
                  <input type="text" class="layui-input subcode-search" placeholder="请输入科目名称或代码">
                  <ul class="subcode-list">
                    <p class="subcode-empty"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</p>
                  </ul>
                </div>
              </div>
            </div>

            <div class="p-abs proc-raw-r">
              <div class="proc-title">
                <!-- <label class="layui-form-label"><span class="asterisk"></span>税金</label>
                <div class="layui-input-inline">
                  <input type="text" class="layui-input digit-input" placeholder="请输入税金" name="taxAmount" maxlength="15" id="taxAmount0">
                </div> -->
                <dt class="title-item tl">
                  <!-- <label class="layui-form-label">模糊查询</label> -->
                  <div class="layui-input-inline">
                    <input type="text" class="layui-input tickets-search" placeholder="请输入会计科目名称">
                  </div>
                </dt>
              </div>
              <div class="tickets-list">
                <dl class="proc-title">
                  <dt class="title-item w1">会计科目</dt>
                  <dt class="title-item w2">计量单位</dt>
                  <dt class="title-item w3">数量</dt>
                  <dt class="title-item w3">单价</dt>
                  <dt class="title-item w3">金额</dt>
                </dl>
                <div id="proc-stock-list">
                  <ul class="proc-list">
                    <li class="list-item tc"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 销售收入票 -->
      <div class="layui-tab-item">
        <div class="layui-form">
          <!-- 搜索 -->
          <div class="search fixed-search" id="searchDOM3">
            <div class="search-btn">
              <div class="layui-btn layui-btn-normal save_btn">保存</div>
              <div class="layui-btn layui-btn-normal add_btn">保存并新增</div>
              <div class="layui-btn layui-btn-normal reset_btn">重置表单</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM3"></div>
          <div class="tickets-list mt20" id="form3">
            <h4>1001 库存现金</h4>
            <ul></ul>

            <h4>1122 应收账款</h4>
            <ul></ul>

            <h4>2203 预收账款</h4>
            <ul></ul>
          </div>
        </div>
      </div>

      <!-- 费用票（现金支付） -->
      <div class="layui-tab-item layui-show">
        <div class="layui-form">
          <!-- 搜索 -->
          <div class="search fixed-search" id="searchDOM4">
            <div class="search-btn">
              <div class="layui-btn layui-btn-normal save_btn">保存</div>
              <div class="layui-btn layui-btn-normal add_btn">保存并新增</div>
              <div class="layui-btn layui-btn-normal reset_btn">重置表单</div>
            </div>
          </div>

          <div class="fixed-head" id="fixedDOM4"></div>
          <div class="tickets-list mt20" id="form4">
            <h4>6601 销售费用</h4>
            <ul></ul>

            <h4>6602 管理费用</h4>
            <ul></ul>
          </div>
        </div>
      </div>

      <!-- 单据录入列表 -->
      <div class="layui-tab-item">
        <div class="layui-form">
          <!-- 搜索 -->
          <div class="search fixed-search" id="searchDOM5">
            <div class="search-item">
              <label for="keyWord">
                <input style="width: 120px;" type="text" class="layui-input" placeholder="请输入关键字..." name="keyWord" maxlength="15" id="keyWord">
              </label>
            </div>
            <div class="layui-btn layui-btn-normal search_btn" id="search-doc">查询</div>
            <div class="search-btn">
              <div class="layui-btn layui-btn-danger" id="batch-del">批量删除</div>
               <div class="layui-btn layui-btn-normal export_btn">导出</div>
            </div>
          </div>

          <div class="layui-form table-box" id="doc-table-box">
            <div class="fixed-head" id="fixedDOM5">
              <table class="layui-table" lay-filter="accDoc">
                <colgroup>
                  <col width="4%">
                  <col width="5%">
                  <col width="15%">
                  <col width="15%">
                  <col width="15%">
                  <col width="13%">
                  <col width="10%">
                  <col width="15%">
                  <col width="8%">
                </colgroup>
                <thead>
                  <tr>
                    <th>
                      <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                    </th>
                    <th>序号</th>
                    <th>单据类型</th>
                    <th>借方科目</th>
                    <th>贷方科目</th>
                    <th>金额</th>
                    <th>凭证号</th>
                    <th>修改时间</th>
                    <th>操作</th>
                  </tr>
                </thead>
              </table>
            </div>
            <table class="layui-table" id="acc-doc" lay-filter="accDoc">
              <colgroup>
                <col width="4%">
                <col width="5%">
                <col width="15%">
                <col width="15%">
                <col width="15%">
                <col width="13%">
                <col width="10%">
                <col width="15%">
                <col width="8%">
              </colgroup>
              <thead>
                <tr>
                  <th>
                    <input type="checkbox" name="allChoose" lay-skin="primary" lay-filter="allChoose">
                  </th>
                  <th>序号</th>
                  <th>单据类型</th>
                  <th>借方科目</th>
                  <th>贷方科目</th>
                  <th>金额</th>
                  <th>凭证号</th>
                  <th>修改时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="doc-list">
                <tr class="tc">
                  <td colspan="11" style="height: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i> 正在加载中</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

  </div>

  <!-- 计量单位弹窗 -->
  <div class="unit-popup" id="unit-popup">
    <ul>
      <li class="unit-item">无数据，请前往设置添加</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">千克 / kg</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
      <li class="unit-item layui-elip" data-unit="" data-id="">克 / g</li>
    </ul>
  </div>
</div>
<!-- End #content -->

<%@ include file="../js.jspf" %>
<script src="${pageContext.request.contextPath }/js/documents/documents-import.js?v=${timeStamp }"></script>
</body>

</html>
