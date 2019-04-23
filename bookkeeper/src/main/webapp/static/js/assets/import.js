/**
** 固定资产
**/
var layer = layui.layer,
  voucherType = 0,
  table = layui.table,
  currPage = 1,
  $keyWord = $('#keyWord'),
  $acperiod = $('#acperiod'),
  $uploadPopup = $('#upload-popup'),
  $uploadInput = $('#upload-input'),
  $uploadBtn = $('#upload-btn'),
  $table = $('table'),
  $assetsList = $('#assets-list'),
  $addPopup = $('#add-popup'),
  $asCode = $('#asCode'),
  $asName = $('#asName'),
  $subjectPopup = $('#subject-popup'),
  state = {},
  subjectData,
  subjectTree;

// zTree 默认设置
var setting = {
  'view': {
    'showLine': false,
    'selectedMulti': false,
  },
  'data': {
    'simpleData': {
      'enable': true,
    }
  },
  'edit': {
    'enable': false
  }
};

var Iheight = $("#content").height();
$("#assetsDataT").height(Iheight-152);

// 显示权限按钮
!function () {
  var show = getPurviewData(500);
  if (show === 'block') {
    $('.search').css('display', show);
    $('#content').css('padding-top', '40px');
    $('#content .layui-tab-item:not(:first)').css('margin-top', '-70px');
  }
}();

$(function () {
  var table = layui.table,
      $searchBtn = $('#search-btn'),
      $addBtn = $('#add-btn'),
      $popupBtn = $('#popup-btn'),
      $batchBtn = $('#batch-del');

  init();
  pageInit();
	_queryStatus();
	
  if(voucherType == 1){
  	$("#assets-list .remove").hide();
  }
	
  // 点击搜索
  $searchBtn.on('click', function () {

    // 条件判断
    var word = $keyWord.val(),
        Time = $acperiod.val();
    if (!word && !Time) {
      layer.msg('请输入任意查询条件');
      return false;
    }

    multiSearch(1);
  });

  // 点击新增
  $addBtn.on('click', function () {
    openPopup();
  });

  // 打开导入Excel弹窗
  $popupBtn.on('click', showUploadPopup);

  $uploadInput.on('change', function () {
    var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
    $(this).prev().text(v);
    $uploadBtn.removeClass('loading loaded');
  });

  // 上传Excel
  $uploadBtn.on('click', function () {
    var that = this,
    url = window.baseURL + '/assets/uploadAssets';
    postRequest(window.baseURL + '/assets/queryAss', {}, function(res) {
    	if(res.code == '2'){
	    		layer.confirm('提示<br/><span class="red">'+res.msg+'<span>'||'提示<br/><span class="red">是否覆盖，重新上传？<span>', {
	        icon: 3,
	        title: '提示'
     		}, function () {
					uploadInit(that, url, uploadFileCB);
	    	})
	    	layer.closeAll('loading');
    	}else if(res.code == '1'){
    		layer.msg(res.msg, {
		      icon: 2,
		      shadeClose: true,
		      time: 2000000
		    });
    	}else if(res.code == '0'){
    		uploadInit(that, url, uploadFileCB);
    	}
    })
  });

	//固定资产导出
	$('#search_export').on('click', function(){
		var balanceS = $('#assets-list tr').length;
		if(balanceS < 1){
			layer.msg('没有可导出数据！');
		}else{
			window.location.href = window.baseURL + "/export/exportAssets";
		}
	});

  $table
    // 点击复选框
    .on('click', '.layui-form-checkbox', function () {
      checkBoxState($(this));
    })
    // 查看详情
    .on('click', 'span.detail', function () {
      seeDetails(this);
    })
    // 单条数据删除
    .on('click', 'span.remove', function () {
    	if(voucherType == 1){
    		layer.msg('已生成凭证，不可删除！');
    		return;
    	}else{
    		singleDel(this);
    	}
    });

  // 批量数据删除
  $batchBtn.on('click', batchDel);

  // 选择固定资产科目
  $addPopup.on('click', '#gdsubject', function () {
    openSubPopup(1);
  })
    .on('click', '#asCumulativeSubject', function () {
      openSubPopup(2);
    })
    .on('click', '#asDepreciaSubject', function () {
      openSubPopup(3);
    });

  $asCode.on('blur', function () {
    checkAsName();
  });

  $asName.on('blur', function () {
    removeAllSpace($(this));
    checkAsName(2);
  });

  var state = {
    categoryId: 1
  };
  $subjectPopup.on('click', 'a', function () {
    $subjectPopup.attr('data-title', $(this).attr('title'))
      .find('a').removeClass('curSelectedNode');
    $(this).addClass('curSelectedNode');
  })

  //限制输入空格
  $('.nospace').on('blur', function () {
    removeAllSpace($(this));
  });

  //限制输入正整数
  $('.integer').on('input', function () {
    positiveInteger($(this));
  });

  //限制输入自然数
  $('.naturalNum').on('input', function () {
    naturalNum($(this));
  });

  //限制输入两位小数
  $('.double').on('input', function () {
    positiveNumber($(this));
  });

  // 计算每期折旧额
  $('#asEstimatePeriod').on('input', assetsDepreciation);
  $('#asvalue').on('input', assetsDepreciation);
  $('#asNetSalvage').on('input', assetsDepreciation);

  layui.element.on('tab(tab)', function(data) {
    state.tabIdx = data.index;
    _getTabData(state.tabIdx); //点击请求数据
  });

  // 选择科目弹窗监听切换
  layui.element.on('tab(sub_tab)', function (data) {
    state.categoryId = data.index + 1;
    var isTab = state.$this.attr('data-tab');
    new TreeData({
      '$JQ': $.fn.zTree,
      'initData': setting,
      'category': state.categoryId,
      'ulElem': $('#tree1'),
      'Intercept': state.$this,
      'flag': isTab
    })
  });
  // 点击截留
  $('#subject-popup').on('click', 'li', function () {
    state.$this = $(this);
    state.isTab = $(this).attr('data-tab');
  });

  // 获取焦点， 输入框内容为空
  removeInputVal($('#acperiod'));
  removeInputVal($('#keyWord'));

  table.render({
    elem: '#table2'
    ,url: window.baseURL + '/stock/list'
    // ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
    ,cols: [[
      {field:'period', title: '期间'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
      ,{field:'sub_code', title: '科目编码'}
      ,{field:'sub_comName', title: '科目名称'}
      ,{field:'qc_balanceNum', title: '期初结存数量'}
      ,{field:'qc_balancePrice', title: '期初结存单价'}
      ,{field:'qc_balanceAmount', title: '期初结存金额'}
      ,{field:'bq_incomeNum', title: '本期收入数量'}
      ,{field:'bq_incomeAmount', title: '本期收入金额'}
      ,{field:'bq_issueNum', title: '本期发出数量'}
      ,{field:'bq_issueAmount', title: '本期发出金额'}
      ,{field:'total_incomeNum', title: '本年累计收入数量'}
      ,{field:'total_incomeAmount', title: '本年累计收入金额'}
      ,{field:'total_issueNum', title: '本年累计发出数量'}
      ,{field:'total_issueAmount', title: '本年累计发出金额'}
      ,{field:'qm_balanceNum', title: '期末结存数量'}
      ,{field:'qm_balancePrice', title: '期末结单价'}
      ,{field:'qm_balanceAmount', title: '期末结存金额'}
      ,{field:'importDate', title: '库存商品导入时间', templet: function(d){
        return formatDate(d.importDate,'yyyy-MM-dd')
      }}
      ,{field:'des', title: '备注'}
    ]]
    ,text: '暂无数据'
    // ,width: 1200
    // ,height: 'full - 400'
    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
      layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
      //,curr: 1 //设定初始在第 1 页
      // ,groups: 5 //只显示 5 个连续页码
      ,limit: 10
      // ,first: false //不显示首页
      // ,last: false //不显示尾页
      ,prev: '&#x4E0A;&#x4E00;&#x9875;'
      ,next: '&#x4E0B;&#x4E00;&#x9875;'

    }
    ,limit: 10
    // ,limits: [10,20,50]
  });
/*
  // 数量金额初始化导入的总账
  table.render({
    elem: '#table2'
    ,url: window.baseURL + '/stock/queryZong'
    // ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
    ,cols: [[
      {field:'period', title: '期间'} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
      ,{field:'comDate', title: '日期', templet: function(d){
        return formatDate(d.comDate, 'yyyy-MM-dd')
      }}
      ,{field:'comabstract', title: '摘要'}
      ,{field:'comName', title: '商品名称'}
      ,{field:'qcTotal', title: '期初数量'}
      ,{field:'qcAmount', title: '期初金额'}
      ,{field:'balanceNum', title: '结存数量'}
      ,{field:'balanceAmount', title: '结存金额'}
      ,{field:'updateDate', title: '导入时间', templet: function(d){
        return formatDate(d.updateDate)
      }}
    ]]
    ,text: '暂无数据'
    // ,width: 1200
    // ,height: 'full - 400'
    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
      layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
      //,curr: 1 //设定初始在第 1 页
      // ,groups: 5 //只显示 5 个连续页码
      ,limit: 10
      // ,first: false //不显示首页
      // ,last: false //不显示尾页
      ,prev: '&#x4E0A;&#x4E00;&#x9875;'
      ,next: '&#x4E0B;&#x4E00;&#x9875;'

    }
    ,limit: 10
    // ,limits: [10,20,50]
  });

  // 数量金额初始化导入的明细账
  table.render({
    elem: '#table3'
    ,url: window.baseURL + '/stock/queryMx'
    // ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
    ,cols: [[
      {field:'comDate', title: '日期', templet: function(d){
        return formatDate(d.comDate, 'yyyy-MM-dd')
      }} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
      ,{field:'voucherName', title: '凭证字号'}
      ,{field:'period', title: '期间'}
      ,{field:'comName', title: '商品名称-科目'}
      ,{field:'comabstract', title: '摘要'}
      ,{field:'businessNum', title: '业务编号'}
      ,{field:'currency', title: '币别'}
      ,{field:'rate', title: '汇率'}
      ,{field:'incomeNum', title: '收入数量'}
      ,{field:'incomePriceOriginal', title: '收入单价(原币)'}
      ,{field:'incomePrice', title: '收入单价'}
      ,{field:'incomeAmountOriginal', title: '收入金额(原币)'}
      ,{field:'incomeAmount', title: '收入金额'}
      ,{field:'issueNum', title: '发出数量'}
      ,{field:'issuePriceOriginal', title: '发出单价(原币)'}
      ,{field:'issuePrice', title: '发出单价'}
      ,{field:'issueAmountOriginal', title: '发出金额(原币)'}
      ,{field:'issueAmount', title: '发出金额'}
      ,{field:'direction', title: '借贷方向'}
      ,{field:'balanceNum', title: '结存数量'}
      ,{field:'balancePrice', title: '结存单价'}
      ,{field:'balanceAmount', title: '结存金额'}
      ,{field:'qcAmount', title: '期初金额'}
      ,{field:'qcTotal', title: '期初数量'}
      ,{field:'importDate', title: '导入时间', templet: function(d){
        return formatDate(d.importDate)
      }}
    ]]
    ,text: '暂无数据'
    // ,width: 1200
    // ,height: 'full - 400'
    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
      layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
      //,curr: 1 //设定初始在第 1 页
      // ,groups: 5 //只显示 5 个连续页码
      ,limit: 10
      // ,first: false //不显示首页
      // ,last: false //不显示尾页
      ,prev: '&#x4E0A;&#x4E00;&#x9875;'
      ,next: '&#x4E0B;&#x4E00;&#x9875;'

    }
    ,limit: 10
    // ,limits: [10,20,50]
  });*/
});


// 页面初始化
function init() {
  var laydate = layui.laydate,
    form = layui.form;

  // laydate选择日期
  laydate.render({
    elem: '#acperiod'
    , btns: ['clear', 'now', 'confirm']
    , type: 'month'
    , format: 'yyyy-MM'
    , max: 0
    , theme: '#1E9FFF'
  });

  lay('.wDate').each(function () {
    laydate.render({
      elem: this
      , max: 0
      , theme: '#1E9FFF'
    });
  });

  //自定义验证规则
  form.verify({
    select: function (val, item) {
      if ($(item).val() == '') {
        $(item).parents('.layui-layer-content').scrollTop(0);
        return '请选择下拉框';
      }
    }
    , subject: function (val, item) {
      if ($(item).val() == '') {
        return '请选择科目';
      }
    }
  });

  //监听提交
  form.on('submit', function (data) {
    savePopup();
    return false;
  });
}

// 加载list首页
function pageInit() {
  $keyWord.val('');
  $acperiod.val('');
  multiSearch(1);
}

/**
 * 查询固定资产
 */
// 不定项多条件查询固定资产(分页查询)
// 点击查询按钮/点击分页请求数据
function multiSearch(page) {
  var url = window.baseURL + '/assets/list',
    params = {
      'currentPage': page || 1,
      'asName': $keyWord.val().trim(),
      'acperiod': $acperiod.val().trim()
    };

  currPage = params.currentPage;
  // searchPort(params, url, searchPortCB);
  postRequest(url, params, searchPortCB);
}

// 获取凭证操作状态
  function _queryStatus() {
    var arr = [0, 0, 0, 0];
    $.ajax({
      async: false,
      url: window.baseURL + '/status/queryStatus',
      success: function (res) {
        if (res.success === 'true') {
          var data = res.status;
          voucherType = data.isCreateVoucher
          arr = [data.isCreateVoucher, data.isCarryState, data.isCheck, data.isJz];
        } else {
          layer.msg('获取操作状态异常', {
            time: 1000
          });
        }
      },
      error: function (res) {
        console.log(res);
        if (res.responseText) {
          layer.alert(res.responseText);
        } else {
          layer.msg('请求错误');
        }
      }
    });
    return arr;
  }

// 查询接口回调
function searchPortCB(res) {
  var data = res.result;
  if (res.message == 'success') {
    var ele = 'layui-table-page0',
      total = data.recordTotal,
      limit = 30;

    tableListHTML(data, 'assets-list');
    flushPage(ele, data.currentPage, total, limit, multiSearch);
  } else {
    layer.msg('不定项多条件分页查询异常');
  }
}

// 列表innerHTML
function tableListHTML(obj, ele) {
  var str = '',
      num = 0,
      list = obj.content || [],
      pages = obj.pageTotal,
      $listTable = $('#' + ele);

  if (list.length) {
    $.each(list, function (idx, data) {
      num = data.asvalue.toFixed(2);
      num = formatNum(num);
      str += '<tr id="' + data.assetsID + '">'
          + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
          + '<td>' + data.asCode + '</td>'
          + '<td>' + data.asName + '</td>'
          + '<td>' + data.asCategory + '</td>'
          + '<td>' + data.department + '</td>'
          + '<td>' + data.asState + '</td>'
          + '<td>' + formatDate(data.asAccountDatea, 'yyyy-MM-dd') + '</td>'
          + '<td>' + data.sourceway + '</td>'
          + '<td>' + data.dmethod + '</td>'
          + '<td>' + data.asEstimatePeriod + '</td>'
          + '<td>' + data.gdsubject + '</td>'
          + '<td>' + data.asCumulativeSubject + '</td>'
          + '<td>' + num + '</td>'
          + '<td class="opt">'
          + '<span class="layui-icon cu red detail">&#xe642;</span>'
          + '<span class="layui-icon cu red remove">&#x1006;</span>'
          + '<p class="dn">' + JSON.stringify(data) + '</p>'
          + '</td>'
          + '</tr>';
    });
  } else {
    str = '<tr class="tc"><td colspan="99" style="height: 80px;">暂无数据</td></tr>';
    // str = '<div class="layui-none">暂无数据</div>';
  }
  $listTable.html(str);
	
	if(voucherType == 1){
  	$("#assets-list .remove").hide();
  }
	
  fixedHead($('.fixed-head'), $('#table'), $('.fixed-search'), $('.fixed-nav'));
  refreshCheckbox();
}


/**
 * 导入Excel
 */
// 打开导入Excel弹窗
function showUploadPopup() {
  $uploadInput.val('').prev().text('请选择97格式.xls...');
  layer.open({
    type: 1
    , title: '导入固定资产Excel'
    // ,area: [width, '80%']
    , content: $uploadPopup
    , cancel: function () {
      $uploadPopup.hide();
      layer.closeAll('tips');
    }
  });
  layer.tips('请下载统一的模版，并按相应的格式在Excel软件中填写您的业务数据，然后再导入到系统中。', '#upload-popup a', { time: -1, tips: [2, '#1eadf3'] });
}

// 上传Excel回调
function uploadFileCB(result) {
  if (result.code == '0') {
    $uploadBtn.addClass('loaded');
    layer.msg(result.msg, {
      icon: 1,
      time: 1500
    }, function () {
      layer.closeAll('page');
      $uploadPopup.hide();
      layer.closeAll('tips');
    });
    pageInit();
  } else if(result.code == '1'){
    layer.msg(result.msg, {
      icon: 2,
      shadeClose: true,
      time: 2000000
    });
  }
}

/**
 * 批量选择
 */
// 批量选择数据
function batchSelect(obj) {
  var arr = [],
    checkbox = $(obj).find(':checked');
  $.each(checkbox, function () {
    var id = $(this).parents('tr').attr('id');
    if (id) {
      arr.push(id);
    }
  });
  return arr.toString();
}

/*// 检测复选框状态
function checkBoxState(obj) {
  var attrName = $(obj).prev().attr('name'),
    currTable = $assetsList,
    $selectAll = currTable.parents('.layui-table-box').find('input[name="allChoose"]');

  if (attrName == 'ckbox') { // 单选
    if (currTable.find('.layui-form-checked').length == currTable.find('tr').length) {
      $selectAll.prop('checked', true);
    } else {
      $selectAll.prop('checked', false);
    }
  } else if (attrName == 'allChoose') { // 全选
    if ($(obj).hasClass('layui-form-checked')) {
      $selectAll.prop('checked', true);
      currTable.find('input[name="ckbox"]').prop('checked', true);
    } else {
      $selectAll.prop('checked', false);
      currTable.find('input[name="ckbox"]').prop('checked', false);
    }
  }
  refreshCheckbox();
}*/

/**
 * 数据删除
 */
// 点击删除按钮
function singleDel(item) {
  var id = $(item).parents('tr').attr('id'),
    arr = id.split(','),
    params = {
      assetsID: id
    },
    url = window.baseURL + '/assets/del';

  deleteData(url, params, arr);
}

// 批量删除
function batchDel() {
	if(voucherType == 1){
    layer.msg('已生成凭证，不可删除！');
    return;
  }else{
  	var id = batchSelect($table),
    	arr = id.split(','),
    	params = {
     	  ids: id
    	},
    url = window.baseURL + '/assets/delAll';
  	deleteData(url, params, arr);
  }
}

// 数据删除
function deleteData(url, params, arr) {
  // if (id == '') {
  //   layer.msg('请选择');
  //   return;
  // }
  // var arr = params.invoiceHIDs.split(',');

  layer.load(2);
  $.ajax({
    type: 'POST',
    url: url,
    data: params,
    dataType: 'json',
//    timeout: 10000,
    success: function (res) {
      layer.closeAll('loading');
      console.log(res);
      var message = res.result;
      if (res.message == 'success') {
        layer.msg(message, {
          icon: 1,
          time: 1500
        }, function (idx) {
          // layer.close(idx);
          $.each(arr, function (idx, val) {
            $('#' + val).remove();
          });
          $table.find('input[name="allChoose"]').prop('checked', false);
          refreshCheckbox();
          multiSearch(currPage);
        });
      } else {
        layer.msg(message);
      }
    },
    error: function (res) {
      layer.closeAll('loading');
      console.log(res);
      layer.msg('网络异常');
    }
  });
}

// 查看详情
function seeDetails(item) {
  var width = ($(window).width() > 900) ? '80%' : '90%',
      data = $(item).siblings('p').text().trim();

  data = JSON.parse(data);
  $addPopup.find('input').prop('disabled', 'disabled');
  $addPopup.find('select').prop('disabled', 'disabled');
  $addPopup.find('div.tc').addClass('dn');

  $('#asCode').val( data.asCode || '' );
  $('#asName').val( data.asName || '' );
  $('#asCategory').val( data.asCategory || '' );
  $('#sourceway').val( data.sourceway || '' );
  $('#department').val( data.department || '' );
  if (data.asState == '使用中') {
    $addPopup.find('input[name="asState"]').eq(0).prop('checked', true);
  } else {
    $addPopup.find('input[name="asState"]').eq(1).prop('checked', true);
  }
  $('#asAccountDatea').val( formatDate(data.asAccountDatea, 'yyyy-MM-dd') );
  if (data.isBeforeUse == '1') {
    $addPopup.find('input[name="isBeforeUse"]').eq(0).prop('checked', true);
  } else {
    $addPopup.find('input[name="isBeforeUse"]').eq(1).prop('checked', true);
  }
  $('#asBeforeUseDate').val( (data.asBeforeUseDate ? formatDate(data.asBeforeUseDate, 'yyyy-MM-dd') : '') );
  $('#asPosition').val( data.asPosition || '' );
  $('#asManufactor').val( data.asManufactor || '' );
  $('#asManufactorDate').val( (data.asManufactorDate ? formatDate(data.asManufactorDate, 'yyyy-MM-dd') : '') );
  $('#asModel').val( data.asModel || '' );
  $('#asEconomicUse').val( data.asEconomicUse || '' );
  $('#dmethod').val( data.dmethod || '' );
  $('#asEstimatePeriod').val( data.asEstimatePeriod || '' );
  $('#asExpectedPeriod').val( data.asExpectedPeriod || '' );
  $('#asDepreciaPeriod').val( data.asDepreciaPeriod || '' );
  $('#gdsubject').val( data.gdsubject || '' );
  $('#asCumulativeSubject').val( data.asCumulativeSubject || '' );
  $('#asDepreciaSubject').val( data.asDepreciaSubject || '' );
  $('#asvalue').val( data.asvalue || '' );
  $('#asUseDepreciaValue').val( data.asUseDepreciaValue || '' );
  $('#taxRate').val( data.taxRate || '' );
  $('#asWorth').val( data.asWorth || '' );
  $('#asCumulativeImpairment').val( data.asCumulativeImpairment || '' );
  $('#asAddDeprecia').val( data.asAddDeprecia || '' );
  $('#asNetSalvage').val( data.asNetSalvage || '' );
  assetsDepreciation();
  $('#des').val( data.des || '' );
  layui.form.render();

  layer.open({
    type: 1
    , title: '固定资产详情'
    , area: [width, '80%']
    , anim: 2
    // , maxmin: true
    , content: $addPopup
    ,btn: ['确定']
    ,btnAlign: 'c'
    ,yes: function(idx) {
      layer.close(idx);
      $addPopup.hide();
      $addPopup.find('input').prop('disabled', '');
      $addPopup.find('select').prop('disabled', '');
      $addPopup.find('div.tc').removeClass('dn');
    }
    , cancel: function () {
      $addPopup.hide();
      $addPopup.find('input').prop('disabled', '');
      $addPopup.find('select').prop('disabled', '');
      $addPopup.find('div.tc').removeClass('dn');
    }
  });
}

// 计算每期折旧额
// 每期折旧额 = （原值 - 预计净残值)/预计使用期间
function assetsDepreciation() {
  if ( $('#asEstimatePeriod').val() > 0 && $('#asvalue').val() > 0 && $('#asNetSalvage').val() > 0 ) {
    var num = ($('#asvalue').val() - $('#asNetSalvage').val()) / $('#asEstimatePeriod').val();
    $('#depreciaAmount').val( num.toFixed(2) );
  } else {
    $('#depreciaAmount').val( '' );
  }
}

/**
 * 新增固定资产
 */
// 打开弹窗
function openPopup() {
  var width = ($(window).width() > 900) ? '80%' : '90%';
  clearPopup();
  layer.open({
    type: 1
    , title: '新增固定资产'
    , area: [width, '80%']
    , anim: 2
    , maxmin: true
    , content: $addPopup
    // ,btn: ['保存', '重置', '取消']
    // ,btnAlign: 'c'
    // ,yes: function() {
    //   savePopup();
    // }
    // ,btn2: function() {
    //   alert('重置')
    //   return false;
    // }
    // ,btn3: function() {
    //   $addPopup.hide();
    // }
    , cancel: function () {
      $addPopup.hide();
    }
  });
  // $addPopup.parent().siblings('.layui-layer-btn').children('a').eq(0).attr('lay-submit')
}

// 清空弹窗数据
function clearPopup() {
  $asCode.parent().next().removeClass().addClass('layui-word-aux').text('');
  $asName.parent().next().removeClass().addClass('layui-word-aux').text('');
  $addPopup.find('input[type="text"]').prop('value', '');
  $addPopup.find('textarea').prop('value', '');
  $('#dmethod').val('平均年限法');
  $addPopup.find('input[name="asState"]').eq(1).next().click();
  $addPopup.find('input[name="isBeforeUse"]').eq(1).next().click();
  layui.form.render();
}

// 检测弹窗数据
function checkPopup() {
  if (!checkAsName()) return;
  /*if (!$password.val()) {
    $userPopup.parent().scrollTop(0);
    layer.tips('请输入密码', '#password', {tips:[2,'#1eadf3']});
    $password.focus();
    return false;
  } else if ($password.val().length < 6) {
    $userPopup.parent().scrollTop(0);
    layer.tips('请输入6-16位密码', '#password', {tips:[2,'#1eadf3']});
    $password.focus();
    return false;
  }
  if (!$ableDate.val()) {
    $userPopup.parent().scrollTop(160);
    layer.tips('请输入生效日期', '#ableDate', {tips:[2,'#1eadf3']});
    return false;
  }
  if (!$disableDate.val()) {
    $userPopup.parent().scrollTop(160);
    layer.tips('请输入失效日期', '#disableDate', {tips:[2,'#1eadf3']});
    return false;
  }*/
  return true;
}

// 固定资产添加 资产名称 编码检验
function checkAsName(type) {
  var flag = false,
    url = window.baseURL + '/assets/check',
    params = {},
    item = $asCode,
    v = '',
    $span;

  if (type == 2) {
    item = $asName;
    v = item.val();
    params.asName = v;
  } else {
    v = item.val();
    params.asCode = v;
  }
  $span = item.parent().next();
  if (v.length == 0) {
    $span.html('');
    return;
  }
  $.ajax({
    async: false,
    type: 'POST',
    url: url,
    data: params,
    dataType: 'json',
//    timeout: 30000,
    beforeSend: function () {
      $span.removeClass().addClass('layui-icon layui-anim layui-anim-rotate layui-anim-loop').html('&#xe63d;');
    },
    success: function (res) {
      layer.closeAll('loading');
      console.log(res);
      var message = res.result;
      cb(res);
    },
    error: function (res) {
      layer.closeAll('loading');
      console.log(res);
      layer.msg('网络异常');
    }
  });
  return flag;

  function cb(res) {
    var message = res.result;
    if (res.message == 'success') {
      $span.removeClass().addClass('p-abs layui-word-aux').html(message);
      flag = true;
    } else {
      $span.removeClass().addClass('p-abs red').html(message);
      item.addClass('layui-form-danger').focus()
        .parents('.layui-layer-content').scrollTop(0);
      flag = false;
    }
  }
}

// 新增固定资产
function savePopup() {
  if (!checkPopup()) return;
  var url = window.baseURL + '/assets/addAss',
    params = {};
  // params = {
  //   // 资产信息
  //   // assetsID: ''   //主键
  //   // ,picture: ''   //图片(存储图片内容)
  //   asCode: ''    //*资产编码
  //   ,asName: ''   //*资产名称
  //   ,asCategory: ''   //*资产类别
  //   ,asState: ''  //*使用情况(资产状态)
  //   ,department: '' //*使用部门
  //   ,asPosition: ''   //存放地点
  //   ,asManufactor: '' //生产厂家
  //   ,asModel: ''    //型号
  //   ,asEconomicUse: ''    //经济用途
  //   ,isBeforeUse: ''    //入账前已开始使用( 1是 2 否)
  //   ,asBeforeUseDate: ''    //入账前开始使用日期
  //   ,asAccountDatea: '' //*入账日期
  //   ,asManufactorDate: '' //*生产日期
  //   ,sourceway: ''    //*增加资产方式
  //   ,asEstimatePeriod: ''   //*预计使用期间(工作总量：月为单位)
  //   ,dmethod: ''    //*折旧方法
  //   ,asAddDeprecia: ''    //累计折旧
  //   ,asExpectedPeriod: ''   //预计剩余折旧期间数(工作总量)
  //   ,asDepreciaPeriod: ''   //折旧计算的预计使用期间(工作总量)
  //   ,asCumulativeSubject: ''    //*累计折旧科目
  //   ,asDepreciaSubject: ''    //折旧费用科目
  //   ,gdsubject: ''    //*固定资产科目
  //   // 原值、净值
  //   ,asvalue: ''  //*原值
  //   ,asWorth: ''    //净值
  //   ,asNetSalvage: ''   //预计净残值
  //   ,asCumulativeImpairment: ''   //累计减值准备
  //   ,asUseDepreciaValue: ''   //用于折旧计算的原值
  //   ,des: ''      //说明备注
  //   // ,dsubject: ''   //折旧摊销科目
  //   // ,costsubject: ''  //成本费用科目
  //   // ,initdepreciation: '' //期初已折旧金额decimal(10,0)
  //   // ,asyears: '' // 年限
  //   // ,residualrate: ''   //折旧率decimal(10,0)
  //   // ,netvalue: ''   //剩余折旧金额
  //   // ,usedyears: ''   //已使用年限int 11
  //   // ,useddate: ''   //使用日期 datetime
  //   // ,gdStatus: ''   //是否计提varchar(4)
  // };

  arr = $addPopup.find('form').serializeArray();
  $.each(arr, function (i, el) {
    params[arr[i].name] = arr[i].value;
  });
  // params.asAddDeprecia = params.asAddDeprecia.split(' ')[0]; // 累计折旧
  postRequest(url, params, savePopupCB);
}

// 新增固定资产回调
function savePopupCB(res) {
  var message = res.result;
  if (res.message == 'success') {
    layer.msg(message, {
      icon: 1
    }, function () {
      layer.closeAll('page');
      $addPopup.hide();
    });
    subjectData = null;
    pageInit();
  } else {
    layer.msg(message);
  }
}

// 打开所有固定资产科目弹窗
function openSubPopup(state) {
  var url = window.baseURL + '/assets/queryAllSub';
  $subjectPopup.attr('data-state', state);
  popup();

  // 打开弹窗
  function popup() {
    layer.open({
      type: 1
      , title: '选择固定资产科目'
      , area: ['420px', '60%']
      , anim: 2
      , content: $subjectPopup
      , btn: ['确定', '取消']
      , btnAlign: 'c'
      , yes: function (index) {
        selectSubject(index, state);
      }
      , btn2: function () {
        $subjectPopup.hide().attr('data-title', '');
      }
      , cancel: function () {
        $subjectPopup.hide().attr('data-title', '');
      }
    });
    new TreeData({
      '$JQ': $.fn.zTree,
      'initData': setting,
      'ulElem': $('#tree1'),
    })
  }
}

// 建立tree结构
function subTree(obj) {
  var obj = obj,
    data1 = obj.code1,
    data2 = obj.code2,
    data3 = obj.code3,
    data4 = obj.code4,
    data5 = obj.code5,
    data6 = obj.code6,
    setting = {
      view: {
        showLine: false,
        dblClickExpand: false
      },
      data: {
        simpleData: {
          enable: true,
          idKey: 'superior_coding',
          pIdKey: 'parentid'
        },
        key: {
          name: function (node) {
            return (node.sub_code + ' ' + node.sub_name);
          },
          children: 'children'
        }
      },
      callback: {
        beforeClick: beforeClick,
        onDblClick: returnData
      }
    };

  var subjectData, subjectTree;

  for (var i = 0; i < obj.length; i++) {

  }
  var subject_tree = $.fn.zTree.init($("#tree1"), setting, obj);
  subject_tree.expandAll(true);
  subjectTree = $.fn.zTree.getZTreeObj('tree1');
  function beforeClick(treeId, treeNode) {
    if (!detail) {
      return;
    };
    var check = (treeNode && !treeNode.isParent);
    if (!check) {
      layer.msg('科目不能选择非明细科目！', {
        icon: 2,
        shadeClose: true,
        time: 2000000
      });
    }
    return check;
  }

  function returnData(event, treeId, data) {
    if (!data || data.isParent) {
      return;
    }
    if (typeof (onDataSelect) == 'function') {
      onDataSelect(data, target);
    }
    api.close();
  }
}

// 选择固定资产科目
function selectSubject(index, state) {
  var title = $subjectPopup.attr('data-title');
  if (!check()) return;

  if (state == 1) {
    $('#gdsubject').val(title);
  } else if (state == 2) {
    $('#asCumulativeSubject').val(title);
  } else if (state == 3) {
    $('#asDepreciaSubject').val(title);
  }
  layer.close(index);
  $subjectPopup.hide().attr('data-title', '');


  function check() {
    if (!title) {
      layer.msg('请选择科目', {
        icon: 2,
        shadeClose: true,
        time: 2000000
      });
      return false;
    }
    return true;
  }
}

//数据接口调用请求
function _getTabData(idx) {
  switch (idx) {
    case 0:
      fixedHead($('.fixed-head'), $('#table'), $('.fixed-search'), $('.fixed-nav'));
      break;
    case 1:
      fixedHead($('.fixed-head'), $('#table2'), '', $('#navDOM1'));
      break;
  }
}
