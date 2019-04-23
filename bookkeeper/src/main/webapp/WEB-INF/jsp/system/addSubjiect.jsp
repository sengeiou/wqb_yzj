<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- 科目新增 -->
<div id="addSub" class="layui-form dn">
  <div class="addSub_content" id="addSub_content">
    <div class="layui-form-item">
      <label class="layui-form-label">上级科目</label>
      <div class="layui-input-block">
        <select name="interest" lay-filter="subList">
          <option value="0">写作</option>
          <option value="1">阅读</option>
        </select>
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">科目编码</label>
      <div class="layui-input-block">
        <input type="text" name="title" required lay-verify="required" placeholder="请输入上级科目" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">科目名称</label>
      <div class="layui-input-block">
        <input type="text" name="title" required lay-verify="required" placeholder="请输入科目名称  " autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">余额方向</label>
      <div class="layui-input-block">
        <input type="radio" name="sex" value="借" title="借">
        <input type="radio" name="sex" value="贷" title="贷">
      </div>
    </div>
    <div class="layui-form-item addSub_num">
      <label class="layui-form-label">数量核算</label>
      <div class="layui-input-block">
        <input type="checkbox" name="switch" lay-skin="switch" lay-text="开启|关闭">
      </div>
      <div class="addSub_unit">
        <lable>计算单位</lable>
        <input type="text" placeholder="请输入计算单位">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">外币核算</label>
      <div class="layui-input-block">
        <input type="checkbox" name="switch" lay-skin="switch" lay-text="开启|关闭">
      </div>
    </div>
    <div class="layui-form-item">
      <label class="layui-form-label">余额方向</label>
      <div class="layui-input-block">
        <input type="radio" name="sex" value="借" title="借">
        <input type="radio" name="sex" value="贷" title="贷">
      </div>
    </div>
  </div>
</div>
