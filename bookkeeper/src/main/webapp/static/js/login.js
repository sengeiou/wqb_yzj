/**
 ** 登录
 **/
var DZuserId = "",
    JZuserId = "",
    YGuserId = ""
;
(function () {
  var laydate = layui.laydate,
    layer = layui.layer,
    $name = $('#login-name'),
    $pwd = $('#login-pwd'),
    $busDate = $('#busDate'),
    $submit = $('#loginsubmit');

  $(function () {
    if ('pushState' in history) {
      pushHistory();
      //阻止返回键
      window.addEventListener("popstate", function (e) {
        pushHistory();
      }, false);
    }

    /*// 选择会计期间
    (function() {
      var nowdays = new Date();
      var year = nowdays.getFullYear();
      var month = nowdays.getMonth();
      if (month == 0) {
        month = 12;
        year = year--;
      } else if (month < 10) {
        month = '0' + month;
      }
      var date = new Date(year, month, 0);
      var lastDay = year + '-' + month + '-' + date.getDate(); //上个月的最后一天
      laydate.render({
        elem: '#busDate',
        btns: ['confirm'],
        type: 'month',
        format: 'yyyy-MM',
        value: date,
        max: lastDay,
        theme: '#1E9FFF'
      });
    }());*/

    // 清除当前输入框
    $('.clear-btn').on('click', function () {
      $(this).hide().prev().val('').focus();
    });
	function login(){
		var params = {
        loginUser: $name.val()
      };
      if ($name.val().length == 11) {
        $.ajax({
          url: window.baseURL + '/system/checkUserType',
          type: 'post',
          data: params,
          dataType: 'json',
          success: function (res) {
            if (res.code == '0') {
              $("#loginsubmit").hide();
              if(res.user.length > 2){
              	$(".login-item #JZloginsubmit,.login-item #DZloginsubmit,.login-item #YGloginsubmit").css({"width":"100px"})
              }else{
              	$(".login-item #JZloginsubmit,.login-item #DZloginsubmit,.login-item #YGloginsubmit").css({"width":"150px"})
              }
              for(var i=0; i<res.user.length; i++){
              	if(res.user[i].userType == 2){			//云代账管理员
              		DZuserId = res.user[i].userID;
              		$("#DZloginsubmit").show();
              	}else if(res.user[i].userType == 3){	//云记账管理员
              		JZuserId = res.user[i].userID;
              		$("#JZloginsubmit").show();
              	}else if(res.user[i].userType == 5){  	//云代账用户
              		YGuserId = res.user[i].userID;
              		$("#YGloginsubmit").show();
              	}else if(res.user[i].userType == 6){	//云记账用户
              		YGuserId = res.user[i].userID;
              		$("#YGloginsubmit").show();
              	}
              }
            } else {
              $("#loginsubmit").show();
              $("#JZloginsubmit,#DZloginsubmit,#YGloginsubmit").hide();
            }
          },
          error: function (res) {
            layer.closeAll('loading');
            layer.msg('网络异常');
          }
        });
      }
	}

    $('#login-name').blur(function () {
      login()
    });

    // 用户名
    $name.on('input', function () {
        var v = $(this).val().replace(/\s+/g, '');
        $(this).val(v);
      })
      .on('keyup', function () {
        if ($(this).val()) {
          $(this).next().css({ 'display': 'inline-block' });
        } else {
          $(this).next().hide();
        }
      })
      .on('keydown', function (event) {
        event = event || window.event;
        if (event.keyCode == 13) {
          // $pwd.focus();
          _login();
          return false;
        }
      })
      .on('focus', function () {
        $(this).parent().addClass('item-focus');
      })
      .on('blur', function () {
        var $this = $(this);
        $this.parent().removeClass('item-focus');
        if ($this.val()) {
          $this.siblings('p').text('')
            .parent().removeClass('item-error');
        }
      });

    // 密码
    $pwd.on('input', function () {
        var v = $(this).val().replace(/\s+/g, '');
        $(this).val(v);
      })
      .on('keyup', function () {
        if ($(this).val()) {
          $(this).next().css({ 'display': 'inline-block' });
        } else {
          $(this).next().hide();
        }
      })
      .on('keydown', function (event) {
        event = event || window.event;
        if (event.keyCode == 13) {
          _login();
          return false;
        }
      })
      .on('focus', function () {
        $(this).parent().addClass('item-focus');
      })
      .on('blur', function () {
        $(this).parent().removeClass('item-focus');
        if ($pwd.val()) {
          $pwd.parent().removeClass('item-error');
        }
      });

    $submit.on('click', _login);

    //忘记密码
    $('.Wjpassword').on('click', function () {
      var _host = window.location.host,
        _hostname = location.hostname;
      if (_hostname === 'localhost' || _hostname === '127.0.0.1' || _hostname.indexOf('192.168.') === 0 || _host.match(RegExp(/.net/))) {
        $(".Wjpassword").attr("href", "https://net.wqbol.net/loginAndRegister/forgetPassword");
      } else if (_host.match(RegExp(/.com/))) {
        $(".Wjpassword").attr("href", "https://www.wqbol.com/loginAndRegister/forgetPassword");
      }
    });
    //免费注册
    $('.MFregister').on('click', function () {
      var _host = window.location.host,
        _hostname = location.hostname;
      if (_hostname === 'localhost' || _host.match(RegExp(/.net/))) {
        $(".MFregister").attr("href", "https://net.wqbol.net/loginAndRegister/register");
      } else if (_host.match(RegExp(/.com/))) {
        $(".MFregister").attr("href", "https://www.wqbol.com/loginAndRegister/register");
      }
    });
  });

  //登录验证
  function _checkLogin() {
    $('.login-item').removeClass('item-error');
    $('p.remark').text('');
    if ($name.val() == '') {
      // layer.tips('请输入用户名', $name, { tips: [2, '#1eadf3'] });
      $name.focus()
        .siblings('p').text('请输入用户名')
        .parent().addClass('item-error');
      return false;
    }
    if ($pwd.val() == '') {
      // layer.tips('请输入密码', $pwd, { tips: [2, '#1eadf3'] });
      $pwd.focus()
        .siblings('p').text('请输入密码')
        .parent().addClass('item-error');
      return false;
    }
    // if ($busDate.val() == '') {
    //   layer.tips('请输入会计期间', $busDate, { tips: [2, '#1eadf3'] });
    //   return false;
    // }
    return true;
  }

  //多身份登录
  $("#JZloginsubmit,#DZloginsubmit,#YGloginsubmit").on('click', function () {
    if ($(this).text() == "记账登录") {
      var userID = JZuserId;
    } else if ($(this).text() == "代账登录") {
      var userID = DZuserId;
    }else if ($(this).text() == "员工登录"){
      var userID = YGuserId;
    }
    document.getElementById("accountLogin").action = window.baseURL + "/system/accountLogin?userID=" + userID;
    _login();
  });

  // 登录
  function _login() {
    if (!_checkLogin()) return;
    $('#submit').click();
    return
    var params = {
      userName: $name.val(),
      password: $pwd.val(),
      // busDate: $busDate.val()
    };

    layer.load();
    $.ajax({
      url: window.baseURL + '/system/login',
      type: 'post',
      data: params,
      dataType: 'json',
      success: function (res) {
        if (res.success == 'true') {
          if (res.account == null || JSON.stringify(res.account) == '{}') {
            layer.msg('您的可用账套为空，请先设置账套', {
              icon: 0,
              time: -1
            });
          } else {
            layer.msg('登录成功', {
              icon: 1
            });
          }
          // var arr = [window.baseURL + '/system/toMana', window.baseURL + '/subinit/initView', window.baseURL + '/subinit/initView?init=1'];
          purview(res.perList); // 页面权限配置
          window.location.href = window.baseURL + '/toSystem/index?token=' + res.Token;
          /*if (res.account == null || JSON.stringify(res.account) == '{}') {
            layer.closeAll('loading');
            layer.msg('您的可用账套为空，请先设置账套', function() {
              window.location.href = arr[0];
            });
          } else {
            // layer.msg('登录成功', {
            //   icon: 1
            // });
            window.location.href = window.baseURL + '/toSystem/index?token=' + res.Token;
            // if (res.account.initialStates == 1) {
            //   window.location.href = arr[2];
            // } else {
            //   window.location.href = arr[1];
            // }
          }*/
        } else {
          layer.closeAll('loading');
          layer.msg(res.message);
        }
      },
      error: function (res) {
        layer.closeAll('loading');
        layer.msg('网络异常');
      }
    });

    // 页面权限配置
    function purview(data) {
      var obj = {};
      for (var i = 0, leng = data.length; i < leng; i++) {
        obj[data[i].actionID] = true;
      }
      obj = JSON.stringify(obj);
      window.sessionStorage.setItem('purview', obj);
    }
  }

  // 监听浏览器返回、后退、上一页按钮的事件
  function pushHistory() {
    var state = {
      title: "title",
      url: "#"
    };
    window.history.pushState(state, "title", "#");
  }
}());
function getContent(obj) {
	if(obj.value.length == 11){
	  var params = {
        loginUser: $('#login-name').val()
      };
      if ($('#login-name').val().length == 11) {
        $.ajax({
          url: window.baseURL + '/system/checkUserType',
          type: 'post',
          data: params,
          dataType: 'json',
          success: function (res) {
            if (res.code == '0') {
              $("#loginsubmit").hide();
              if(res.user.length > 2){
              	$(".login-item #JZloginsubmit,.login-item #DZloginsubmit,.login-item #YGloginsubmit").css({"width":"100px"})
              }else{
              	$(".login-item #JZloginsubmit,.login-item #DZloginsubmit,.login-item #YGloginsubmit").css({"width":"150px"})
              }
              for(var i=0; i<res.user.length; i++){
              	if(res.user[i].userType == 2){			//云代账管理员
              		DZuserId = res.user[i].userID;
              		$("#DZloginsubmit").show();
              	}else if(res.user[i].userType == 3){	//云记账管理员
              		JZuserId = res.user[i].userID;
              		$("#JZloginsubmit").show();
              	}else if(res.user[i].userType == 5){  	//云代账用户
              		YGuserId = res.user[i].userID;
              		$("#YGloginsubmit").show();
              	}else if(res.user[i].userType == 6){	//云记账用户
              		YGuserId = res.user[i].userID;
              		$("#YGloginsubmit").show();
              	}
              }
            } else {
              $("#loginsubmit").show();
              $("#JZloginsubmit,#DZloginsubmit,#YGloginsubmit").hide();
            }
          },
          error: function (res) {
            layer.closeAll('loading');
            layer.msg('网络异常');
          }
        });
      }
	}
}
