var $, tab, dataStr, layer;
window.sessionStorage.setItem("codeList", "");
layui.config({
  base: "../js/"
}).extend({
  "bodyTab": "bodyTab"
})
layui.use(['bodyTab', 'form', 'element', 'layer', 'jquery'], function () {
  var form = layui.form,
    element = layui.element;
  $ = layui.$;
  layer = parent.layer === undefined ? layui.layer : top.layer;
  tab = layui.bodyTab({
    openTabNum: "50", //最大可打开窗口数量
  });
  var $accountPopup = $('#account-list-popup'),
    $accountList = $('#account-list'), //账套列表父级
    $accountSearch = $('#account-search'),
    search_input = $("#mainQuery");

  //糊弄查询账套
  $(search_input).on("keyup", function () {
    if (search_input.val() == '') {
      $($accountList).show();
    }
    $("#account-list tr:contains(" + search_input.val().trim() + ")").show();
    $("#account-list tr:not(:contains(" + search_input.val().trim() + "))").hide();
  });

  //通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
  function getData(json) {
    $.getJSON(tab.tabConfig.url, function (data) {
      if (json == "contentManagement") {
        dataStr = data.contentManagement;
        //重新渲染左侧菜单
        tab.render();
      } else if (json == "memberCenter") {
        dataStr = data.memberCenter;
        //重新渲染左侧菜单
        tab.render();
      } else if (json == "systemeSttings") {
        dataStr = data.systemeSttings;
        //重新渲染左侧菜单
        tab.render();
      } else if (json == "seraphApi") {
        dataStr = data.seraphApi;
        //重新渲染左侧菜单
        tab.render();
      }
    })
  }
  //距离顶部的高度     $(this).find(".YJlipiao").offset().top
  //当前DIV的高度  $(this).find(".YJlipiao").height()
  //浏览器高度  $(window).height();
  $(".layui-nav-item").mouseover(function () {
    $(this).find(".YJlipiao").show();
    //判断是否有子级菜单
    if ($(this).children("div").hasClass("YJlipiao")) {
      //获取到底部距离
      var bottomS = $(window).height() - $(this).find(".YJlipiao").height() - $(this).find(".YJlipiao").offset().top;
      if (bottomS < 20) {
        //负数转正数
        var heightS = Math.abs(bottomS);
        $(this).find(".YJlipiao").css({ "bottom": "17px" });
        $(this).find(".YJlipiao .triangle_border_left").css({ "top": heightS + 28 });
      }
    }
  });
  $(".layui-nav-item").mouseout(function () {
    $(this).find(".YJlipiao").hide();
    //鼠标移除还原样式
    $(this).find(".YJlipiao").css({ "bottom": "" });
    $(this).find(".YJlipiao .triangle_border_left").css({ "top": "14px" });
  });
  $(".layui-nav-itemS").mouseover(function () {
    $(this).find(".YJlipiaoS").show();
  });
  $(".layui-nav-itemS").mouseout(function () {
    $(this).find(".YJlipiaoS").hide();
  });

  // 选择账套弹窗
  $('#choose-account').on('click', function () {
    getAccountList();
  });

  // 获取账套列表
  function getAccountList() {
    getRequest(window.baseURL + '/account/queryByUserID', function (res) {
      if (res.message == 'success') {
        var list = res.account || [];
        if (list.length) {
          showAccountList(list);
        } else {
          $('#choose-account > .selectCus').html('请选择账套').attr({
            'data-init': '',
            'data-mapped': ''
          });
          layer.msg('您的可用账套为空，请先设置账套', {
            icon: 0,
            shade: 0.5,
            time: 2000
          }, function() {
            $('#KHGL').click();
          });
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
        str += '<tr data-id="' + data.accountID + '" data-state="' + data.initialStates + '" data-mapped="' + data.mappingStates + '">' +
          '<td>' + data.companyName + '</td>' +
          '<td>' + formatDate(data.period, 'yyyy-MM') + '</td>' +
          '<td>' + str2 + '</td>' +
          '</tr>';
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

  // 时间戳转换日期格式 默认yyyy-MM-dd HH:mm:ss
  function formatDate(b, d) {
    if (!b) return "";
    var a = b ? new Date(b) : new Date;
    b = a.getFullYear();
    var c = 10 > a.getMonth() + 1 ? "0" + (a.getMonth() + 1) : a.getMonth() + 1,
      e = 10 > a.getDate() ? "0" + a.getDate() : a.getDate(),
      f = 10 > a.getHours() ? "0" + a.getHours() : a.getHours(),
      g = 10 > a.getMinutes() ? "0" + a.getMinutes() : a.getMinutes(),
      a = 10 > a.getSeconds() ? "0" + a.getSeconds() : a.getSeconds();
    return "yyyy-MM" == d ? b + "-" + c : "yyyy-MM-dd" == d ? b + "-" + c + "-" + e : b + "-" + c + "-" + e + " " + f + ":" + g + ":" + a
  };



  // AJAX GET请求封装
  function getRequest(url, cb, cb2, asyncType) {
    layer.load();
    $.ajax({
      async: asyncType || true,
      url: url,
      // timeout: 60000,
      success: function (res) {
        layer.closeAll('loading');
        cb && cb(res);
      },
      error: function (res) {
        layer.closeAll('loading');
        if (cb2) {
          cb2(res);
        } else {
          if (res.responseText) {
            layer.alert(res.responseText);
          } else {
            layer.msg('请求错误');
          }
        }
      }
    });
  }
  //  $(".layui-nav-item").hover(function (){
  //    $(this).find(".YJlipiao").show();
  //  });
  //页面加载时判断左侧菜单是否显示
  //通过顶部菜单获取左侧菜单
  $(".topLevelMenus li,.mobileTopLevelMenus dd").click(function () {
    if ($(this).parents(".mobileTopLevelMenus").length != "0") {
      $(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
    } else {
      $(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
    }
    $(".layui-layout-admin").removeClass("showMenu");
    $("body").addClass("site-mobile");
    getData($(this).data("menu"));
    //渲染顶部窗口
    tab.tabMove();
  })

  //隐藏左侧导航
  $(".hideMenu").click(function () {
    if ($(".topLevelMenus li.layui-this a").data("url")) {
      layer.msg("此栏目状态下左侧菜单不可展开"); //主要为了避免左侧显示的内容与顶部菜单不匹配
      return false;
    }
    $(".layui-layout-admin").toggleClass("showMenu");
    //渲染顶部窗口
    tab.tabMove();
  })

  //通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
  getData("contentManagement");

  // 添加新窗口
  $("body").on("click", ".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')", function () {
    //如果不存在子级
    if ($(this).siblings().length == 0) {
      addTab($(this));
      $('body').removeClass('site-mobile'); //移动端点击菜单关闭菜单层
    }
    $(this).parent("li").siblings().removeClass("layui-nav-itemed");
    window.sessionStorage.setItem("iSrefresh", "false");
  })

  //清除缓存
  //  $(".clearCache").click(function(){
  //    window.sessionStorage.clear();
  //      window.localStorage.clear();
  //      var index = layer.msg('清除缓存中，请稍候',{icon: 16,time:false,shade:0.8});
  //      setTimeout(function(){
  //          layer.close(index);
  //          layer.msg("缓存清除成功！");
  //      },1000);
  //  })

  //刷新后还原打开的窗口
  if (cacheStr == "true") {
    if (window.sessionStorage.getItem("menu") != null) {
      menu = JSON.parse(window.sessionStorage.getItem("menu"));
      curmenu = window.sessionStorage.getItem("curmenu");
      var openTitle = '';
      for (var i = 0; i < menu.length; i++) {
        openTitle = '';
        if (menu[i].icon) {
          if (menu[i].icon.split("-")[0] == 'icon') {
            openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
          } else {
            openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
          }
        }
        openTitle += '<cite>' + menu[i].title + '</cite>';
        openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
        element.tabAdd("bodyTab", {
          title: openTitle,
          content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
          id: menu[i].layId
        })
        //定位到刷新前的窗口
        if (curmenu != "undefined") {
          if (curmenu == '' || curmenu == "null") { //定位到后台首页
            element.tabChange("bodyTab", '');
          } else if (JSON.parse(curmenu).title == menu[i].title) { //定位到刷新前的页面
            element.tabChange("bodyTab", menu[i].layId);
          }
        } else {
          element.tabChange("bodyTab", menu[menu.length - 1].layId);
        }
      }
      //渲染顶部窗口
      tab.tabMove();
    }
  } else {
    window.sessionStorage.removeItem("menu");
    window.sessionStorage.removeItem("curmenu");
  }
})

//打开新窗口
function addTab(_this) {
  tab.tabAdd(_this);
}
