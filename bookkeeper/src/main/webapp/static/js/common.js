/*公用js*/
var laydate = layui.laydate,
  test = window.location.pathname,
  layer = layui.layer;

$('.left_set').delegate('.returnS', 'click', function () {
  window.history.go(-1);
});

function initialStates(obj) {
  if (obj.href.charAt(obj.href.length - 1) == 0) {
    $(this).href = window.baseURL + "/subinit/initView?init=0";
  } else {
    $(this).href = window.baseURL + "/invoice/importView#1";
  }
}

// 权限 0全局禁用 1全局启用
function getPurviewData(val) {
  var purview = JSON.parse(window.sessionStorage.getItem('purview')) || {};
  if (session.userType === '2' || session.userType === '3' || session.isTestUser === '1') {
    return 'block';
  } else if (purview[0]) {
    return '';
  } else if (purview[1]) {
    return 'block';
  } else if (purview[val]) {
    return 'block';
  }
}

$(function () {
  var $accountPopup = $('#account-list-popup'),
      $accountList = $('#account-list'),        //账套列表父级
      $accountSearch = $('#account-search'),
      search_input = $("#mainQuery");           //输入框
      //search_content = $("#account-list");    //账套列表父级

  //键盘快捷键
  $(document).on('keydown',function(event){
    var e = event || window.event,
        keyCode = e.keyCode ? e.keyCode : e.which;
    //alt+F6跳到凭证页面
    if (e.ctrlKey && keyCode == 117) {
      top.$('#add-voucher').click();
    }
  })

  //糊弄查询账套
  $(search_input).on("keyup", function () {
    if (search_input.val() == '') {
      $($accountList).show();
    }
    $("#account-list tr:contains(" + search_input.val().trim() + ")").show();
    $("#account-list tr:not(:contains(" + search_input.val().trim() + "))").hide();
  });

  // 解决TOCMAT重启之后 ，意外的在其他小窗口中登录的情况
  if (window != top && location.pathname == window.baseURL + '/system/toLogin') {
    top.location.href = location.href
  }

  (function () {
    if (window.session === undefined) return;
    laydate.render({
      elem: '#busDate1', //指定元素
      btns: ["confirm"],
      type: "month",
      format: "yyyy-MM",
      value: session.busDate,
      theme: "#1E9FFF",
      done: function (a) {
        changeCurrPeriod(a);
        window.sessionStorage.removeItem("editVouch");
      }
    });
  })();

  //balanceHint();

  // 刷新
  $('#i-refresh').on('mouseenter', function () { this.index = layer.tips('刷新', this, { time: -1, tips: [2, '#1eadf3'] }) }).on('mouseleave', function () { layer.close(this.index) }).on('click', pageRefresh);

  // 返回上一页
  $('#i-back').on('mouseenter', function () { this.index = layer.tips('返回上一页', this, { time: -1, tips: [2, '#1eadf3'] }) }).on('mouseleave', function () { layer.close(this.index) }).on('click', pageBack);

  // 帮助
  $('#i-help').on('mouseenter', function () { this.index = layer.tips('帮助', this, { time: -1, tips: [2, '#1eadf3'] }) }).on('mouseleave', function () { layer.close(this.index) }).on('click', pageHelp);

  // 选择账套弹窗
  $('#choose-account').on('click', function () {
    getAccountList();
  });

  // 切换账套
  $accountList.on('click', 'span', function () {
    var $tr = $(this).parents('tr'),
      id = $tr.attr('data-id'),
      state = $tr.attr('data-state');

    postRequest(window.baseURL + '/account/chgAccount', { accountID: id }, function(res) {
      if (res.message == 'success') {
        layer.msg('切换账套成功');
        window.sessionStorage.removeItem('editVouch');
        window.sessionStorage.removeItem('editVouchTab');
        checkInitialState(state);
        $('#i-refresh').click();
      } else {
        layer.msg('切换账套出错');
      }
    });
  })

  $accountPopup.on('focus', 'input', function() {
      // 全选内容
      $(this).select();
    })
    // 票据列表模糊查询
    .on('input', 'input', function() {
      var v = $(this).val().trim();

      // $accountList.find('td:contains(' + v + ')').parent().show();
      // $accountList.find('td:not( :contains(' + v + ') )').parent().hide();
    })

  /**
   * * 切换账套
   */
  // 获取账套列表
  function getAccountList() {
    getRequest(window.baseURL + '/account/queryByUserID', function (res) {
      if (res.message == 'success') {
        var list = res.account;
        if (list.length) {
          showAccountList(list);
        } else {
          layer.msg('查询帐套出错');
        }
      } else {
        layer.msg('查询帐套出错');
      }
    });

    // 显示账套列表
    function showAccountList(list) {
      var str = '',
          str2 = '';

      $.each(list, function (idx, data) {
        if (session.accountID == data.accountID) {
          str2 = '当前账套';
        } else {
          str2 = '<span class="cu iconfont icon-qiehuan2 blue">切换</span>';
        }
        str += '<tr data-id="' + data.accountID + '" data-state="' + data.initialStates + '">'
          + '<td>' + data.companyName + '</td>'
          + '<td>' + formatDate(data.period, 'yyyy-MM') + '</td>'
          + '<td>' + str2 + '</td>'
          + '</tr>';
      });
      $accountList.html(str);
      layer.open({
        type: 1,
        title: '选择账套',
        area: ['600px', '500px'],
        content: $accountPopup
      });
    }
  }
  $("body").delegate(".layui-layer-setwin", "click", function () {
    $('#account-list-popup').hide();
  })

  // 判断当前账套是否已经初始化 1去首页 0去初始化
  function checkInitialState(state) {
    //主页切换账户刷新当前页面
    if (test == window.baseURL + '/toSystem/index') {
      window.location.reload();
    } else {
      var initUrl = window.baseURL + '/invoice/importView';
      window.location.href = state == 1 ? initUrl + '#1' : initUrl;
    }
    /*
     * if (state == 1) { window.location.href = initUrl + '?init=1'; } else {
     * window.location.href = initUrl; }
     */
  }

  // 切换会计期间 传递参数 period: yyyy-MM
  function changeCurrPeriod(period) {
    postRequest(window.baseURL + '/system/chgPeriod', { period: period }, function (res) {
      layer.closeAll('loading');
      if (res == 'success') {
        layer.msg('切换会计期间成功');
        top.location.reload();
      } else {
        layer.msg('切换会计期间异常');
      }
    });
  }

});

// 余额次数
//function balanceHint(){
//getRequest(window.baseURL + '/account/getWarnInfo', function(res) {
//  if (res.success == 'true') {
//    $("#balanceHint").html(res.warnInfo);
//  }
//});
//}

// 退出登录
function logout() { layer.load(); $.ajax({ type: "GET", url: window.baseURL + "/system/loginOut", success: function (a) { "success" == a && layer.msg("\u9000\u51fa\u767b\u5f55") }, error: function (a) { console.log("\u9000\u51fa\u767b\u5f55error:" + a) } }); if(window.sessionStorage.cache=='true'){window.sessionStorage.clear();window.sessionStorage.setItem('cache','true')}else{window.sessionStorage.clear()} top.location.href = window.baseURL + "/system/toLogin" };

// 网页强制刷新
function pageRefresh() {
  window.location.reload(true);
}

// 网页刷新
function pageReload() {
  window.location.reload(true);
}

// 返回上一页
function pageBack() {
  window.history.back();
}

// 帮助
function pageHelp() {
}

// 限制输入自然数
function naturalNum(a) { var b = a.val().replace(/\D/g, ""); a.val(b) }

// 正整数（parseInt精度16位）
function positiveInteger(b) { function c(a) { a = parseInt(a) ? Math.abs(parseInt(a)) : ""; return a.toString() } var a = b.val().toString().replace(/\D/, ""); if (16 < a.length) var d = a.substring(0, 16), a = a.substring(16), a = c(d) + a; else a = c(a); b.val(a) };

// 限制输入limit位小数，默认两位
function positiveNumber(c, d) { var a = c.val().toString().trim(), b = d ? d : 2; d = a.length; var e = a.indexOf("."), b = e + 1 + b; Number(a) ? (0 < e && d > b && (a = Number(a).toString().substring(0, b)), 0 > a && (a = Math.abs(a)), c.val(a)) : parseFloat(a) ? (a = parseFloat(a), 0 > a && (a = Math.abs(a)), c.val(a)) : 0 == parseFloat(a) ? (0 < e && d >= b && (Number(a) || (a = parseFloat(a)), a = Number(a).toString().substring(0, b)), c.val(a)) : c.val("") };

// 限制输入数字大小
function limitInputNum(b, c) { var a = parseFloat(b.val()).toString().trim(); a && a > c && (a = a.substring(0, a.length - 1), b.val(a)) };

// 清除空格（所有）
function removeAllSpace(b) { var a = b.val().replace(/\s+/g, ""); b.val(a) }

// 时间戳转换日期格式 默认yyyy-MM-dd HH:mm:ss
function formatDate(b,d){if(!b)return"";var a=b?new Date(b):new Date;b=a.getFullYear();var c=10>a.getMonth()+1?"0"+(a.getMonth()+1):a.getMonth()+1,e=10>a.getDate()?"0"+a.getDate():a.getDate(),f=10>a.getHours()?"0"+a.getHours():a.getHours(),g=10>a.getMinutes()?"0"+a.getMinutes():a.getMinutes(),a=10>a.getSeconds()?"0"+a.getSeconds():a.getSeconds();return"yyyy-MM"==d?b+"-"+c:"yyyy-MM-dd"==d?b+"-"+c+"-"+e:b+"-"+c+"-"+e+" "+f+":"+g+":"+a}

// 检测复选框状态
function checkBoxState(obj) {
  var attrName = $(obj).prev().attr('name'),
    currTable = $(obj).parents('.table-box'),
    currTbody = currTable.find('tbody'),
    $selectBatch = currTable.find('input[name="allChoose"]'),
    $selectAll = currTable.find('input[type="checkbox"]');

  if (attrName == 'ckbox') { // 单选
    if (currTbody.find('.layui-form-checked').length == currTbody.find('tr').length) {
      $selectBatch.prop('checked', true);
    } else {
      $selectBatch.prop('checked', false);
    }
  } else if (attrName == 'allChoose') { // 全选
    if ($(obj).hasClass('layui-form-checked')) {
      $selectAll.prop('checked', true);
    } else {
      $selectAll.prop('checked', false);
    }
  }
  refreshCheckbox();
}

// layui复选框刷新
function refreshCheckbox() {
  layui.form.render('checkbox');
}

// 拼接字符串，显示复选框
function renderSelect() {
  layui.form.render('select');
}

// AJAX GET请求封装
function getRequest(url, cb, cb2, asyncType) {
  layer.load();
  $.ajax({
    async: asyncType || true,
    url: url,
    // timeout: 60000,
    success: function (res) {
      //567 是ajax请求服务器，检查到身份过期返回给前端特别的号码
      if (res.code == '567') {
        layer.alert(res.msg);
        var param = encodeURI(res.msg);
        var url = decodeURI(window.baseURL + "/system/ajaxAuthentication?msg=" + res.msg);
        top.location.href = url;
      } else {
        layer.closeAll('loading');
        cb && cb(res);
      }
    },
    error: function (XMLHttpRequest) {
      layer.closeAll('loading');
      if (cb2) {
        cb2(XMLHttpRequest);
      } else {
        if (XMLHttpRequest.responseText) {
          if (XMLHttpRequest.responseText.search('重新登录') != -1) {
            layer.open({
              type: 1,
              title: '异地登录',
              area: ['100%', '100%'],
              content: XMLHttpRequest.responseText,
              cancel: function () {
                layer.closeAll('tips');
              }
            });
          } else {
            layer.alert(XMLHttpRequest.responseText);
          }
        } else {
          layer.msg('请求错误');
        }
      }
    }
  });
}

// AJAX POST请求封装
function postRequest(url, params, cb, cb2, asyncType) {
  layer.load();
  $.ajax({
    async: asyncType || true,
    type: 'POST',
    url: url,
    data: params,
    dataType: 'json',
    // timeout: 30000,
    success: function (res) {
      //567 是ajax请求服务器，检查到身份过期返回给前端特别的号码
      if (res.code == '567') {
        layer.alert(res.msg);
        var param = encodeURI(res.msg);
        var url = decodeURI(window.baseURL + "/system/ajaxAuthentication?msg=" + res.msg);
        top.location.href = url;
      } else {
        layer.closeAll('loading');
        cb && cb(res);
      }
    },
    //error: function (res) {
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      layer.closeAll('loading');
      if (cb2) {
        cb2(XMLHttpRequest);
      } else {
        if (XMLHttpRequest.responseText) {
          if (XMLHttpRequest.responseText.search('重新登录') != -1) {
            layer.open({
              type: 1,
              title: '异地登录',
              area: ['100%', '100%'],
              content: XMLHttpRequest.responseText,
              cancel: function () {
                layer.closeAll('tips');
              }
            });
          } else {
            layer.alert(XMLHttpRequest.responseText);
          }
        } else {
          layer.msg('请求错误');
        }
      }
    }
  });
}

// 刷新分页
function flushPage(ele, page, total, limit, fn) {
  layui.laypage.render({
    elem: ele
    , count: total // 数据总数，从服务端得到
    , limit: limit
    , curr: page
    // ,curr: location.hash.replace('#!page=', '') //获取起始页
    // ,hash: 'page' //自定义hash值
    , layout: ['prev', 'page', 'next', 'skip', 'count']
    , jump: function (obj, first) {
      // obj包含了当前分页的所有参数，比如：
      // 首次不执行
      if (!first) {
        fn(obj.curr);
      } else if (page > obj.curr && obj.curr > 1) {
        fn(obj.curr);
      }
    }
  });
}

// 只能输入数字并且只能是两位小数
function toFixedNum(elem) {
  // 先把非数字的都替换掉，除了数字和.
  elem.val(elem.val().replace(/[^\d.]/g, "").// 只允许一个小数点
    replace(/^\./g, "").replace(/\.{2,}/g, ".").// 只能输入小数点后两位
    replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'));
  return elem.val();
}

// 格式化数字 // 123654789 = 》 "123,654,789"
function formatNum(str) {
  if (typeof str === 'number') {
    str = str + '';
  }
  return str.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// 获取焦点， 输入框内容为空
function removeInputVal(el) {
  el.off('focus').on('focus', function () {
    $(this).val('');
  });
}

// 固定 h1 search head
function fixedHead(fixedDOM, tableDOM, searchDOM, navDOM) {
  var width, left;
  tableDOM && _width();
  fixedDOM && fixedDOM.css('display', 'block');

  $(window).off('resize').on('resize', function () {
    tableDOM && _width();
  });

  $(window).off('scroll').on('scroll', function () {
    if (tableDOM) {
      left = tableDOM.offset().left - $(document).scrollLeft() + 'px';
    }
    fixedDOM && fixedDOM.css('left', left);
    searchDOM && searchDOM.css('left', left);
    navDOM && navDOM.css('left', left);
  });

  $('.layui-body').on('scroll', function () {
    var top = $(this).scrollTop();
    // navDOM.next('.fixed-icon').css('margin-top', top);
  });

  function _style(el) {
    if (el) {
      el.css({
        'width': width,
        'left': left
      });
    }
  }

  function _width() {
    width = tableDOM.width() + 'px';
    left = tableDOM.offset().left - $(document).scrollLeft() + 'px';
    fixedDOM && _style(fixedDOM);
    searchDOM && _style(searchDOM);
    navDOM && _style(navDOM);
  }
}

/**
 * Form表单上传文件 <form method="post" enctype="multipart/form-data">
 */
function uploadInit(a,b,c){var d=$(a),e=d.parent();if(d.hasClass("loading")){return false}else if(checkExcel(e)){e.attr("action",b);a=new FormData(e[0]);d.addClass("loading");layer.msg('正在上传中，请稍候~',{icon:16,shade:0.3,time:-1});$.ajax({url:b,type:"POST",data:a,processData:!1,contentType:!1,xhr:function(){myXhr=$.ajaxSettings.xhr();myXhr.upload&&myXhr.upload.addEventListener("progress",function(a){uploadProgress(a,e)},!1);return myXhr},success:function(a){d.removeClass("loading");c&&"function"==typeof c&&c(a)},error:function(a){layer.msg("\u8bf7\u6c42\u9519\u8bef",{time:1E3})}})}}
function checkExcel(a){a=$(a).find('.in-file').val();return ""==a?(layer.msg("\u8bf7\u9009\u62e9\u6587\u4ef6",{time:1E3}),!1):".xls"!=a.substr(a.lastIndexOf(".")).toLowerCase()?(layer.msg("\u8bf7\u4e0a\u4f20xls\u683c\u5f0f\u7684\u6587\u4ef6",{time:1E3}),!1):!0}
function uploadProgress(a,b){b=b.find(".progress");if(a.lengthComputable){var c=Math.floor(a.loaded/a.total*100)+"%";b.show().attr({value:a.loaded,max:a.total}).find(".percentage").css({width:c});b.find(".text").html(c);a.loaded==a.total&&b.fadeOut(1E3)}};

function uploadImg(a,b,c){var d=$(a),e=d.parent();if(d.hasClass("loading")){return false}else if(checkImg(e)){e.attr("action",b);a=new FormData(e[0]);d.addClass("loading");layer.msg('正在上传中，请稍候~',{icon:16,shade:0.3,time:-1});$.ajax({url:b,type:"POST",data:a,processData:!1,contentType:!1,xhr:function(){myXhr=$.ajaxSettings.xhr();myXhr.upload&&myXhr.upload.addEventListener("progress",function(a){uploadProgress(a,e)},!1);return myXhr},success:function(a){d.removeClass("loading");c&&"function"==typeof c&&c(a)},error:function(a){layer.msg("\u8bf7\u6c42\u9519\u8bef",{time:1E3})}})}}
function checkImg(a){a=$(a).find('input[name="attachFile"]').val();return ""==a?(layer.msg("\u8bf7\u9009\u62e9\u6587\u4ef6",{time:1E3}),!1):a.substr(a.lastIndexOf(".")).toLowerCase()!=".png"&&a.substr(a.lastIndexOf(".")).toLowerCase()!=".jpg"&&a.substr(a.lastIndexOf(".")).toLowerCase()!=".BMP"&&a.substr(a.lastIndexOf(".")).toLowerCase()!=".jpeg"&&a.substr(a.lastIndexOf(".")).toLowerCase()!=".tiff"?(layer.msg("请上传过.png格式文件",{time:1E3}),!1):!0}

/**
 * 兼容IE8
 */
if(String.prototype.trim){}else{String.prototype.trim=function(){return this.replace(/^\s*|\s*$/g,"")}}

// 重写toFixed兼容方法
Number.prototype.toFixed = function (b) { if (20 < b || 0 > b) throw new RangeError("toFixed() digits argument must be between 0 and 20"); if (isNaN(this) || this >= Math.pow(10, 21)) return this.toString(); if ("undefined" == typeof b || 0 == b) return Math.round(this).toString(); var a = this.toString(), d = a.split("."); if (2 > d.length) { for (var a = a + ".", c = 0; c < b; c += 1)a += "0"; return a } c = d[0]; d = d[1]; if (d.length == b) return a; if (d.length < b) { for (c = 0; c < b - d.length; c += 1)a += "0"; return a } a = c + "." + d.substr(0, b); c = d.substr(b, 1); 5 <= parseInt(c, 10) && (c = Math.pow(10, b), a = (Math.round(parseFloat(a) * c) + 1) / c, a = a.toFixed(b)); return a };

//herf 参数值获取
function _queryStringHash(name) {
  var reg = new RegExp("(^|&|/?)" + name + "=([^&]*)(&|$)", "i");
  var _index = window.location.href.indexOf("?");
  //console.log(_index)
  var r = window.location.href.substr(_index + 1);
  var strs = r.split("&");
  for (var i = 0; i < strs.length; i++) {
    var strs1 = strs[i].split("=");
    if (strs1[0] == name) {
      //console.log("??======", strs1[1])
      return strs1[1]
    }
  }
  return "";
};

// 返回当前头部选项卡的lay-id（时间戳）
function _currTabTimeStamp() {
  return top.$('#top_tabs').children('li.layui-this').attr('lay-id');
}

// 给已打开的特定选项卡titleArr添加刷新标识className,默认刷新页面
function _topTabsRefresh(titleArr, className) {
  // 开启Tab切换刷新不需要执行
  if (sessionStorage.changeRefresh === 'true') return;
  var className = className || 'reload';
  top.$('#top_tabs > li').each(function(index, el) {
    var title = $(this).children('cite').text();
    for (var i = 0, len = titleArr.length; i < len; i++) {
      if (title === titleArr[i]) {
        $(this).addClass(className);
      }
    }
  });
}
