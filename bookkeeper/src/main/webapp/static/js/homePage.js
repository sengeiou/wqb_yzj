/**
 * 登陆后首页：主页
 */
var $accountList = $('#account-list'),
  laydate = layui.laydate,
  test = window.location.pathname,
  layer = layui.layer;
window.sessionStorage.setItem("iSrefresh", "true");
// document.getElementById("iframeID").contentWindow.子级页面方法()

if (session.user === '') {
    window.location.href = "/system/toLogin";
}

// 修改导航路劲，后台管理页面不受影响
// 没有账套，强制进入账套管理页面
if (session.accountID.length === 0) {
  layer.msg('您的可用账套为空，请先设置账套', {
    icon: 0,
    shade: 0.5,
    time: 2000
  });
  $(".YJlipiao ul li a").not(".mana").off('click').on('click', function () {
  	if($(this).text() != "退出" && $(this).text() != "锁屏" && $(this).text() != "功能设定" && $(this).text() != "更换皮肤"){
  		$("#ZTGL").click();
    	return false;
  	}
  });
}
//获取当前账套是否初始化0为未初始化、1为已初始化
else if ($("#choose-account div").attr("data-init") === '0') {
  //未初始化情况改变所有路径为初始化，过滤后台管理wqbmana的
  $("#navBar .displayLi").hide();
  $('#navBar .YJlipiao li a').not(".ignore").off('click').on('click', function () {
    $("#seraphCSH").click();
    return false;
  });
  $("#subMap").off('click').on('click', function () {
    $("#seraphCSH").click();
    return false;
  });
} else {
  //已初始化则隐藏初始化不可进
  $("#navBar .displayLi").show();
  // 兼容旧数据，未执行科目映射的
  if ($("#choose-account div").attr('data-mapped') !== '1') {
    // $(".YJlipiao ul li a").not(".ignore").off('click').on('click', function(){
    //   $("#subMap").click();
    //   return false;
    // });
    $("#subMap").attr('data-url', $("#subMap").attr('data-url') + '?reloadPage=true');
    layer.open({
      type: 0,
      icon: 0,
      title: '提示',
      content: '请先完成标准科目映射',
      btn: ['确定'],
      resize: false,
      yes: function (index) {
        $("#subMap").click();
        layer.close(index);
      },
      cancel: function () {
        $("#subMap").click();
      }
    });
  }
}
$('#userInfo li a').removeAttr('data-url'); // 系统 导航删除跳转页面事件

//首页代账代账用户趋势图
$("#top_tabs").delegate("li", "click", function () {
  if ($("#top_tabs li").length == 2) {
    if (session.userType == 3 || session.userType == 6) {
      document.getElementById("pageContext").contentWindow.chartLeft();
    }
  }
});
//首页代账代账用户趋势图
$("#top_tabs li:eq(0)").click(function () {
  if (session.userType == 3 || session.userType == 6) {
    document.getElementById("pageContext").contentWindow.chartLeft();
  }
});
$("#top_tabs").delegate("li", "click", function () {
  if (window.sessionStorage.iSrefresh == "true") {
    //如果没有开启Tab缓存则执行
    if (window.sessionStorage.changeRefresh == "false" || window.sessionStorage.changeRefresh == undefined) {
      if ($(this).children("cite").text().search("固定资产导入") != -1 || $(this).children("cite").text().search("科目余额表") != -1 || $(this).children("cite").text().search("总账") != -1 || $(this).children("cite").text().search("利润表") != -1 || $(this).children("cite").text().search("固定资产折旧明细") != -1 || $(this).children("cite").text().search("资产负债表") != -1 || $(this).children("cite").text().search("银行对账单导入") != -1) {
        setTimeout(function () {
          $(".refreshThis").click();
        }, 100);
      }
    }
  }
});

var curmenul;
if(((curmenul = window.sessionStorage.curmenu) !== '') && curmenu.search("报税") !== -1){
	$(".bszz").show();
}

(function () {
  $(function () {
    // var $reInitPop = $('#re-init-popup'); // 重新初始化弹窗

    //键盘快捷键
    $(document).on('keydown', function (event) {
      var e = event || window.event,
        keyCode = e.keyCode ? e.keyCode : e.which;
      //alt+F6跳到凭证页面
      if (e.ctrlKey && keyCode == 117) {
        top.addTab($('#add-voucher'));
      }
    })

    // 监听tab点击事件，还没有切换tab选项卡
    top.$('#top_tabs')
      .on('click', 'li', function () {
        var $this = $(this),
          title = $this.children('cite').text(), //选项卡标题
          $iframe = $('#top_tabs_box > div.clildFrame > div.layui-tab-item:eq(' + $this.index() + ') > iframe'), // 当前iframe
          iframeURL = $iframe[0].src;
        if ($this.hasClass('close')) {
          // 阻止事件冒泡，点击关闭选项卡不执行
          return;
        } else if ($this.hasClass('layui-this')) {
          // 获取焦点的tab
          // console.log(title);
          $this.removeClass('reload update');
        } else if ($this.hasClass('reload')) {
          // 其他tab需要重载页面
          $this.removeClass('reload update');
          // 重载页面
          if (iframeURL.indexOf(location.origin) > -1) { // 未跨域
            $iframe[0].contentWindow.location.reload();
          } else {
            $iframe[0].src = iframeURL;
          }
        } else if ($this.hasClass('update')) {
          // 局部刷新
          $this.removeClass('update');
          // 局部刷新
          if (iframeURL.indexOf(location.origin) > -1) { // 未跨域
            $iframe.contents().find('#updatePage').click();
          }
        }
      })
      .on('click', 'i', function () {
        var $li = $(this).parent(), // 当前选项卡
          $content = $('#top_tabs_box > div.clildFrame > div.layui-show'), // 当前iframe的父元素
          $nextLi,
          $nextIframe;
        // 阻止事件冒泡，触发选项卡li的事件
        $li.addClass('close');
        if ($li.hasClass('layui-this')) {
          // 判断即将获取焦点选项卡位置
          if ($li.index() === $('#top_tabs li').length - 1) {
            $nextLi = $li.prev();
            $nextIframe = $content.prev().children('iframe');
          } else {
            $nextLi = $li.next();
            $nextIframe = $content.next().children('iframe');
          }
          var iframeURL = $nextIframe[0].src;
          if ($nextLi.hasClass('reload')) {
            // 其他tab需要重载页面
            $nextLi.removeClass('reload update');
            // 重载页面
            if (iframeURL.indexOf(location.origin) > -1) { // 未跨域
              $nextIframe[0].contentWindow.location.reload();
            } else {
              $nextIframe[0].src = iframeURL;
            }
          } else if ($nextLi.hasClass('update')) {
            $nextLi.removeClass('update');
            // 局部刷新
            if (iframeURL.indexOf(location.origin) > -1) { // 未跨域
              $nextIframe.contents().find('#updatePage').click();
            }
          }
        }
      })

    // 重新初始化
    top.$('#ReInitial').off().on('click', function () {
      layer.open({
        type: 0,
        // icon: 0,
        title: '提示',
        area: ['530px', '320px'],
        content: '<div class="dn" id="re-init-popup"><i class="layui-layer-ico layui-layer-ico0"></i><div class="layui-form re-initialize"><h3>重新初始化系统将会清空你录入的所有数据，请慎重！</h3><ul><li>系统将删除您新增的所有科目</li><li>系统将删除您录入的所有凭证</li><li>系统将删除您录入的所有初始化数据</li></ul><p><span class="check-box-wrap"><input type="checkbox" name="understand" lay-skin="primary" id="re-init-understand"><label for="re-init-understand">&nbsp;&nbsp;我已清楚了解将产生的后果</label></span></p><p class="check-confirm">（请先确认并勾选“我已清楚了解将产生的后果”）</p></div></div>',
        // content: $reInitPop,
        btn: ['重新初始化', '取消'],
        resize: false,
        success: function () {
          layui.form.render('checkbox');
          // label绑定给选择框
          $('#re-init-understand').off('change').on('change',function(){
            var $this = $(this);
            if (true === $this.prop('checked')) {
              $this.siblings('.layui-form-checkbox').addClass('layui-form-checked');
            } else {
              $this.siblings('.layui-form-checkbox').removeClass('layui-form-checked');
            }
          });
        },
        yes: function (index) {
          if (true === $('#re-init-understand').prop('checked')) {
            postRequest(window.baseURL + '/accInit/accountReinitialization', {}, function (res) {
              if (0 == res.code) {
                layer.msg(res.msg, {
                  icon: 1,
                  shade: 0.3,
                  time: 2000
                }, function () {
                  layer.close(index);
                  // $reInitPop.hide();
                  $('#seraphCSH').click();
                });
                top.$('#top_tabs > li').length > 1 && top.$('#top_tabs_box .closePageAll').click(); //关闭全部页签
              } else {
                res.msg && layer.msg(res.msg, {
                  shade: 0.3
                });
              }
            });
          } else {
            $('#re-init-understand').parents('p').next().css('visibility', 'visible');
          }
        },
        // btn2: function () {
        //   $reInitPop.hide();
        // },
        // cancel: function () {
        //   $reInitPop.hide();
        // }
      });
      // // label绑定给选择框
      // $('#re-init-understand').off('change').on('change', function () {
      //   var $this = $(this);
      //   if (true === $this.prop('checked')) {
      //     $this.siblings('.layui-form-checkbox').addClass('layui-form-checked');
      //   } else {
      //     $this.siblings('.layui-form-checkbox').removeClass('layui-form-checked');
      //   }
      // });
    });

    //旧商城使用
    getRequest(window.baseURL + '/account/getPeriodList', function (res) {
      if (res.flag === "false") { // 表示上次扣费失败 只能查看以前做过的账
        var minLength = res.periodList[0].period;
        var minA = minLength.slice(0, 4);
        var minB = minLength.slice(5, 7);
        var minPeriod = minA + "-" + minB + "-01";
        if (res.periodList.length >= 1) {
          var maxLength = res.periodList.pop().period;
          var MaxA = maxLength.slice(0, 4);
          var MaxB = maxLength.slice(5, 7);
          var a = new Date,
            b = MaxA,
            a = MaxB;
          b = b + "-" + a + "-31";
          laydate.render({
            elem: "#busDate",
            btns: ["confirm"],
            type: "month",
            format: "yyyy-MM",
            value: session.busDate,
            min: minPeriod,
            max: b,
            theme: "#1E9FFF",
            done: function (a) {
              changeCurrPeriod(a);
              window.sessionStorage.removeItem("editVouch");
            }
          });
          delete minPeriod;
        }
      } else if (res.success == "true") {
        if (res.periodList != null) {
          if (res.periodList.length >= 1) {
            var minLength = res.periodList[0].period;
            var minA = minLength.slice(0, 4);
            var minB = minLength.slice(5, 7);
            var minPeriod = minA + "-" + minB + "-01";
            // 上次扣费成功 但是余额不够再次做账
            var maxLength = res.periodList.pop().period;
            var MaxA = maxLength.slice(0, 4);
            var MaxB = maxLength.slice(5, 7);
            var a = new Date,
              b = MaxA,
              a = MaxB;
            b = b + "-" + a + "-31";
            laydate.render({
              elem: "#busDate",
              btns: ["confirm"],
              type: "month",
              format: "yyyy-MM",
              value: session.busDate,
              min: minPeriod,
              max: b,
              theme: "#1E9FFF",
              done: function (a) {
                changeCurrPeriod(a);
                window.sessionStorage.removeItem("editVouch");
              }
            });
            delete minPeriod;
          } else {
            var a = new Date,
              b = a.getFullYear(),
              a = a.getMonth();
            0 == a ? (a = 12, b--) : 10 > a && (a = "0" + a);
            b = b + "-" + a + "-31";
            var MinS = session.minPeriod.slice(0, 7) + "-01";
            laydate.render({
              elem: "#busDate",
              btns: ["confirm"],
              type: "month",
              format: "yyyy-MM",
              value: session.busDate,
              min: MinS,
              max: b,
              theme: "#1E9FFF",
              done: function (a) {
                changeCurrPeriod(a);
                window.sessionStorage.removeItem("editVouch");
              }
            });
            delete session.minPeriod;
          }
        } else {
          var a = new Date,
            b = a.getFullYear(),
            a = a.getMonth();
          0 == a ? (a = 12, b--) : 10 > a && (a = "0" + a);
          b = b + "-" + a + "-31";
          var MinS = session.minPeriod.slice(0, 7) + "-01";
          laydate.render({
            elem: "#busDate",
            btns: ["confirm"],
            type: "month",
            format: "yyyy-MM",
            value: session.busDate,
            min: MinS,
            max: b,
            theme: "#1E9FFF",
            done: function (a) {
              changeCurrPeriod(a);
              window.sessionStorage.removeItem("editVouch");
            }
          });
          delete session.minPeriod;
        }
      }
    })
  });
})();

// 退出登录
function logout() {
  layer.load();
  $.ajax({
    type: "GET",
    url: window.baseURL + "/system/loginOut",
    success: function (a) {
      "success" == a && layer.msg("\u9000\u51fa\u767b\u5f55")
    },
    error: function (a) {
      console.log("\u9000\u51fa\u767b\u5f55error:" + a)
    }
  });
  // 判断用户是否开启Tab缓存
  if (window.sessionStorage.cache == 'true') {
    window.sessionStorage.clear();
    window.sessionStorage.setItem('cache', 'true');
  } else {
    window.sessionStorage.clear();
  }
  top.location.href = window.baseURL + "/system/toLogin"
};

// 页面权限配置
function purview(arr) {
  var obj = {};
  for (var i = 0, len = arr.length; i < len; i++) {
    obj[arr[i]] = true;
  }
  obj = JSON.stringify(obj);
  window.sessionStorage.setItem('purview', obj);
}

// 切换账套
$accountList.on('click', 'span', function () {
  var $tr = $(this).parents('tr'),
    id = $tr.attr('data-id'),
    state = $tr.attr('data-state');
  postRequest(window.baseURL + '/account/chgAccount', { accountID: id }, function (res) {
    if (res.message == 'success') {
      layer.msg('切换账套成功');
      window.sessionStorage.removeItem('editVouch');
      window.sessionStorage.removeItem('editVouchTab');
      top.$('#top_tabs > li').length > 1 && top.$('#top_tabs_box .closePageAll').click(); //关闭全部页签
      checkInitialState(state);
      top.location.reload();
    } else {
      layer.msg('切换账套出错');
    }
  });
})
// 判断当前账套是否已经初始化 1去首页 0去初始化
function checkInitialState(state) {
  //主页切换账户刷新当前页面
  if (state == 0) {
    $("#seraphCSH").attr('data-url', window.baseURL + '/subinit/initView?init=0').click();
  }
  //  if(test == window.baseURL + '/toSystem/index'){
  //    window.location.reload();51755
  //  }else{
  //    var initUrl = window.baseURL + '/invoice/importView';
  //    window.location.href = state == 1 ? initUrl + '#1' : initUrl;
  //  }
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
      window.location.reload();
    } else {
      layer.msg('切换会计期间异常');
    }
  });
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
    error: function (res) {
      layer.closeAll('loading');
      if (cb2) {
        cb2(res);
      } else {
        if (res.responseText) {
          if (res.responseText.search('重新登录') != -1) {
            layer.open({
              type: 1,
              title: '异地登录',
              area: ['100%', '100%'],
              content: res.responseText,
              cancel: function () {
                layer.closeAll('tips');
              }
            });
          } else {
            layer.alert(res.responseText);
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
    error: function (res) {
      layer.closeAll('loading');
      if (cb2) {
        cb2(res);
      } else {
        if (res.responseText) {
          if (res.responseText.search('重新登录') != -1) {
            layer.open({
              type: 1,
              title: '异地登录',
              area: ['100%', '100%'],
              content: res.responseText,
              cancel: function () {
                layer.closeAll('tips');
              }
            });
          } else {
            layer.alert(res.responseText);
          }
        } else {
          layer.msg('请求错误');
        }
      }
    }
  });
}

/* //鼠标点击特效
var a_idx = 0;
jQuery(document).ready(function($) {
 $("body").click(function(e) {
     var a = new Array("（*@ο@*）", "傻子，好看嘛？", "（⊙o⊙）", "★~★", "(*^‧^*)", "傻子，好看嘛？", "（ˇ＾ˇ〉", "~>_<~+", "(≥◇≤)" ,"（*>.<*）", ">_<|||", "→_→", "└(^o^)┘", " (*^__^*)");
     var $i = $("<span></tagObj>").text(a[a_idx]);
     a_idx = (a_idx + 1) % a.length;
     var x = e.pageX,
     y = e.pageY;
     $i.css({
         "z-index": '999999999999999999999999999999999999999999999999999999999999999999999',
         "top": y - 20,
         "left": x,
         "position": "absolute",
         "font-weight": "bold",
         "color": "#ff6651"
     });
     $("body").append($i);
     $i.animate({
         "top": y - 180,
         "opacity": 0
     },
     1500,
   function() {
         $i.remove();
     });
 });
});*/
//点击报税
$(".layui-nav-item .dDutiableGoods").on('click', function () {
    var text = $(this).children('cite').text();
    if(text == "报税"){
    	$(".bszz").show();
    }else{
    	$(".bszz").hide();
    }
})
$("#top_tabs").delegate('li', 'click', function (){
	var text = $(this).children('cite').text();
    if(text == "报税"){
    	$(".bszz").show();
    }else{
    	$(".bszz").hide();
    }
})

//点击进项
$(".YJlipiao #YJlipiao0").on('click', function () {
  if (window.sessionStorage.menu && window.sessionStorage.menu.indexOf("进项发票导入") != -1) {
    setTimeout(function () {
      top.$('.clildFrame > .layui-show iframe')[0].contentWindow.location.reload();
    }, 100); //延迟0.1秒
  }
});
//点击销项
$(".YJlipiao #YJlipiao1").on('click', function () {
  if (window.sessionStorage.menu && window.sessionStorage.menu.indexOf("销项发票导入") != -1) {
    setTimeout(function () {
      top.$('.clildFrame > .layui-show iframe')[0].contentWindow.location.reload();
    }, 100); //延迟0.1秒
  }
});

// 修改科目列表期末余额
function _subListRefresh(data) {
  // console.time('_subListRefresh');
  if (undefined === data || null === data || '' === data) {
    top.subListRefresh = true;
    console.log('无返回值');
    return;
  }
  if (top.subListData.length === 0 || top.subListRefresh === true) {
    // 请求科目列表
  } else {
    // 修改科目列表期末余额
    var len = data.length,
      cacheData = top.subListData,
      leng = cacheData.length,
      cacheSub,
      returnSubCode;
    for (var i = 0; i < len; i++) {
      returnSubCode = data[i].subCode;
      for (var j = 0; j < leng; j++) {
        cacheSub = cacheData[j];
        if (cacheSub["subCode"] == returnSubCode) {
          cacheData[j] = data[i];
          break;
        }
        if (j == leng - 1) {
          // console.error('subListData未找到科目:' + returnSubCode);
          alert('subListData未找到科目:' + returnSubCode);
        }
      }
    }
    top.subListData = cacheData;
  }
  // console.timeEnd('_subListRefresh');
}

// 新增科目,更新科目列表
function _subListUpdate(data, oldCodeArr) {
  // console.time('_subListRefresh');
  if (undefined === data || null === data || '' === data) {
    top.subListRefresh = true;
    console.log('无返回值');
    return;
  }
  if (top.subListData.length === 0 || top.subListRefresh === true) {
    // 请求科目列表
  } else {
    // 修改科目列表期末余额
    var len = data.length,
      cacheData = top.subListData,
      leng = cacheData.length,
      cacheSub,
      oldSubCode;
    for (var i = 0; i < len; i++) {
      oldSubCode = oldCodeArr[i];
      for (var j = 0; j < leng; j++) {
        cacheSub = cacheData[j];
        if (cacheSub["subCode"] == oldSubCode) {
          cacheData[j] = data[i];
          break;
        }
        if (j == leng - 1) {
          // console.error('subListData未找到科目:' + oldSubCode);
          alert('subListData未找到科目:' + oldSubCode);
        }
      }
    }
    top.subListData = cacheData;
  }
  // console.timeEnd('_subListRefresh');
}
