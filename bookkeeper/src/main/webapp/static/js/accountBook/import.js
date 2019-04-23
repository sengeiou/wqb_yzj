/*
 * @Author: qiang
 * @Date: 2018-04-19 10:37:52
 * @Last Modified by: qiang
 * @Last Modified time: 2018-04-19 11:33:56
 */
var treeText = [],
  treeNodeName = '',
  subCodeVal = '1001',
  $subjectPopup = $('#subject-popup'),
  subCodeDateB = session.busDate, //起始时间
  subCodeDateE = session.busDate, //结束时间
  codeK = '', //起始科目
  codeJ = '', //结束科目
  kmjcK = '', //科目节次开始
  kmjcJ = '', //科目节次结束
  yeIs = '', //余额为0不显示
  isEndSubCode = '', //只显示最明细科目
  ye2Is = '', //无发生额且余额为0不显示
  type = '1', //1是总账或者明细账菜单 2是筛选框
  isClean = 0; //显示已清理资产0不显示1显示

// zTree 默认设置
var setting = {
  'view': {
    'showLine': true,
    'selectedMulti': true,
  },
  'data': {
    'simpleData': {
      'enable': true,
    }
  },
  'edit': {
    'enable': false,
  },
  'callback': { //添加点击事件
    'onClick': zTreeOnClick,
  }
};
// 显示权限按钮
;
(function() {
  var content = $("#content").width();
  $("#navDOM1, #search1").css({ 'width': content, 'left': '270px' });
  $("#searchDOM1, #depreciationDOM1, #searchDOM2, #fixedDOM1").css({ 'width': content });
  $("#navDOM1 li").click(function() {
    var content = $("#content").width();
    $("#searchDOM1, #depreciationDOM1, #searchDOM2, #fixedDOM1").css({ 'width': content });
    $("#navDOM1, #search1").css({ 'width': content, 'left': '270px' });
  });
  var Iheight = $(window).height();
  $("#menuContent").height(Iheight - 152);
  //数量金额总账列表设置最大高度balancesID
  if (Iheight > 0) {
    $("#accountListS").css({ "max-height": Iheight - 152 });
    $("#profitDataT").css({ "max-height": Iheight - 192 });
    $("#amountID").css({ "max-height": Iheight - 192 });
    $("#balancesID").css({ "max-height": Iheight - 192 });
    $("#sumVoucher").css({ "max-height": Iheight - 192 });
  }

  var url = location.hash;
  if (url.length > 20) {
    subCodeVal = url.match(/&Val=(\S*)&Name/)[1];
    var subCodeName = url.split("Name=")[1];
    $("#navDOM1 li:eq(1)").click();
    //重置
    reset_btn();
    // 明细账1
    _getBills();
    //起止时间
    startStopDate();
    //调用快速切换接口
    onLoadZTree();
    $('.sub_code_name').text(subCodeVal + ' - ' + decodeURI(subCodeName));
  } else {
    if (url.substr(url.length - 1, 1) == 1) {
      $("#navDOM1 li:eq(0)").click();
    } else if (url.substr(url.length - 1, 1) == 2) {
      $("#navDOM1 li:eq(1)").click();
      //重置
      reset_btn();
      // 明细账1
      _getBills();
      //起止时间
      startStopDate();
      //调用快速切换接口
      onLoadZTree();
    } else if (url.substr(url.length - 1, 1) == 3) {
      $("#navDOM1 li:eq(2)").click();
      // 数量金额总帐
      var params = {
        'sub_code': $('#sub_code').val(),
        'comNameSpec': $('#comNameSpec').val()
      };
      _getAmountTotal(params);
    } else if (url.substr(url.length - 1, 1) == 4) {
      $("#navDOM1 li:eq(4)").click();
      // 科目余额表
      _getBalance();
    } else if (url.substr(url.length - 1, 1) == 5) {
      $("#navDOM1 li:eq(5)").click();
      //凭证汇总表
      _getVchrSum()
    } else if (url.substr(url.length - 1, 1) == 6) {
      $("#navDOM1 li:eq(6)").click();
      //起止时间
      startStopDate();
      //固定资产折旧明细
      depreciation();
    }
  }
  //总账
  //var table = $('#accountListS').DataTable({
  //    "scrollY": Iheight-122, //设置Tbody高度
  //    "paging": false,        //是否允许分页true OR false
  //    "searching": false,       //是否允许搜索true OR false
  //    "bLengthChange": false,   //去掉每页显示多少条数据方法
  //    "info": false,      //控制总数信息(标准界面右下角显示总数和过滤条数的控件)的显隐
  //    "ordering": false,    //全局控制列表的所有排序功能
  //});
  //明细账
  //$('#accountList1S').dataTable({
  //    "scrollY": Iheight-202, //设置Tbody高度
  //    "paging": false,      //是否允许分页true OR false
  //    "searching": false,     //是否允许搜索true OR false
  //    "bLengthChange": false,   //去掉每页显示多少条数据方法
  //    "info": false,      //控制总数信息(标准界面右下角显示总数和过滤条数的控件)的显隐
  //    "ordering": false,    //全局控制列表的所有排序功能
  //    "bScrollCollapse" : true,
  //});
  $(".search_All").hover(function() {
	$('.search-btn ul').css({ "display": "block" });
  });
  
  $('html').on('click', function () {
  	$('.search-btn ul').css({ "display": "none" });
  });
  // 定义公用对象，存放数据
  var state = {};
  state.category = 1;
  state.categoryId = 1;
  state.code = '';
  $('.timeBegin').val(subCodeDateB);
  $('.timeEnd').val(subCodeDateB);
  var nian = subCodeDateB.substr(0, 4);
  var yue2 = subCodeDateB.substr(subCodeDateB.length - 2);
  var yue1 = subCodeDateB.substr(subCodeDateB.length - 1);
  if (yue2 < 10) {
    $('.timeBeginS').text(nian + '年第' + yue1 + '期');
    $('.timeEndS, .accountingPeriod').text(nian + '年第' + yue1 + '期');
    $('.zz_Close').text(nian + '年第' + yue1 + '期  至  ' + nian + '年第' + yue1 + '期');
    $(".dateText").text(nian + '年' + yue1 + '月折旧');
  } else {
    $('.timeBeginS').text(nian + '年第' + yue2 + '期');
    $('.timeEndS, .accountingPeriod').text(nian + '年第' + yue2 + '期');
    $('.zz_Close').text(subCodeDateB + '  至  ' + subCodeDateB);
    $('.zz_Close').text(nian + '年第' + yue2 + '期  至  ' + nian + '年第' + yue2 + '期');
    $(".dateText").text(nian + '年' + yue2 + '月折旧');
  }

  var show = getPurviewData(700);
  if (show === 'block') {
    $('.search').css('display', show);
    $('.zz_pop_up').hide();
    $('#content').css('padding-top', '44px');
    $('.fixed-head').css({ 'padding-top': '20px' });
    $('#searchDOM6').css({ 'width': content });
    $('#fixedDOM5').css({ 'padding-top': '20px' });
  }
  $(function() {
    var loca = window.location.search.substr(1),
      num = loca.split('=')[1],
      $printVchr = $('#print-vchr');
    // 判断首页跳转切换页面
    num === '5' ? showTable(num) : _allAccount(); //总账列表
    // 监听tab切换
    layui.element.on('tab(account)', function(data) {
      state.tabIndex = data.index || 0;
      _setTabData(state.tabIndex); //点击请求数据
    });

    // laydate 选择开始日期
    lay('.startTime').each(function() {
      layui.laydate.render({
        elem: this,
        type: 'month',
        max: 0,
        theme: '#1E9FFF',
        done: function(val) {
          state.startTime = val;
        }
      });
    });

    // 选择结束日期
    lay('.endTime').each(function() {
      layui.laydate.render({
        elem: this,
        type: 'month',
        max: 0,
        theme: '#1E9FFF',
        done: function(val) {
          state.endTime = val;
        }
      });
    });

    //搜索条件鼠标悬浮效果
    $(".zz_Close").hover(function() {
      $('.zz_pop_up').show();
      $('.zz_Close').css({ "border-bottom": "none" });
      if (window.sessionStorage.curmenu.search("总账") != -1) {
        $(".wfsYeIs").show();
        $(".xszmx").hide();
      } else if (window.sessionStorage.curmenu.search("明细账") != -1) {
        $(".xszmx, .wfsYeIs").hide();
      }
    });
    $(".zz_pop_up").hover(function() {
      $('.zz_pop_up').show();
      $('.zz_Close').css({ "border-bottom": "none" });
    });
    //会计期间开始悬浮效果
    $(".search-item1").hover(function() {
      $('#startStopDate').css({ "display": "block" });
    }, function() {
      $('#startStopDate').css({ "display": "none" });
    });
    //会计期间结束悬浮效果
    $(".search-item2").hover(function() {
      $('#startStopDate1').css({ "display": "block" });
    }, function() {
      $('#startStopDate1').css({ "display": "none" });
    });
    //会计期间结束悬浮效果
    $(".search-item3").hover(function() {
      $('#startStopDate2').css({ "display": "block" });
    }, function() {
      $('#startStopDate2').css({ "display": "none" });
    });
    //会计期间开始列表选择
    $("#startStopDate").delegate('li', 'click', function() {
      $('.timeBeginS').text($(this).text());
      $('.timeBegin').val($(this).children('input').val());
    });
    //会计期间结束列表选择
    $("#startStopDate1").delegate('li', 'click', function() {
      $('.timeEndS').text($(this).text());
      $('.timeEnd').val($(this).children('input').val());
    });
    //会计期间结束列表选择
    $("#startStopDate2").delegate('li', 'click', function() {
      $('.accountingPeriod').text($(this).text());
      $('.accounting').val($(this).children('input').val());
    });
    //关闭查询条件框
    $('#navDOM1, .init_con, #header, .nav-floor').click(function() {
      $('.zz_pop_up').hide();
      $('.zz_Close').css({ "border-bottom": "1px solid #e4e4e4" });
    });
    //清空明细账快速切换的查询条件
    $('.qingkong').on('click', function() {
      $("#keyword").val('');
      $("#keyword").css({ "border": "1px solid #b9b8b8" });
    })
    //科目节次点击加
    $('.KMendJIA').on('click', function() {
      var inputVal = $(this).prevAll(".KMbeginI").val();
      var KMbeginIi = inputVal || 0;
      KMbeginIi++;
      if (KMbeginIi > 3) {
        return false;
      } else {
        $(this).prevAll('.KMbeginI').val(KMbeginIi);
      }
    });
    //科目节次点击减
    $('.KMendJIAN').on('click', function() {
      var inputVal = $(this).prevAll(".KMbeginI").val();
      var KMendIi = inputVal || 3;
      KMendIi--;
      if (KMendIi < 1) {
        return false;
      } else {
        $(this).prevAll('.KMbeginI').val(KMendIi);
      }
    });
    //点击重置
    $(".reset_btn").on('click', function() {
      reset_btn();
    });
    // 点击搜索
    var $search_val = '',
      $search_valE = '',
      $this;
    $('.search').on('click', '.search_btn', function() {
      $search_val = $(this).prev().children('.lay_select').children('.level').val();
      if (window.sessionStorage.curmenu.search("科目余额表") != -1) {
        if (!state.startTime && !state.endTime) {
          layer.msg('请选择者开始时间结束时间');
          return false;
        }
      } else if (state.tabIndex !== 3 && state.tabIndex !== 4) {
        state.code = $search_val.split('-')[0];
        if (!$search_val && !state.startTime && !state.endTime) {
          layer.msg('请选择科目，或者开始时间结束时间');
          return false;
        }
      }
      if (state.startTime) {
        if (!state.endTime) {
          layer.msg('请选择结束时间');
          return false;
        }
      }
      if (state.endTime) {
        if (!state.startTime) {
          layer.msg('请选择开始时间');
          return false;
        }
      }
      if (state.startTime > state.endTime) {
        layer.msg('开始时间不能大于结束时间');
        return false;
      }
      $(this).parent().next('.fixed-head').children('p').text('科目名称 ：' + $search_val);
      // 点击查询数据
      _getAllTableData();
    });
    //新总账、明细账查询
    $('.search_btn1').on('click', function() {
      if (window.sessionStorage.curmenu.search("总账") != -1 || window.sessionStorage.curmenu.search("明细账") != -1) {
        //获取起始科目
        $search_val = $(this).siblings('ul').children('.qishi').find('#qsSubject').val();
        //获取结束科目
        $search_valE = $(this).siblings('ul').children('.jieshu').find('#jsSubject').val();
        if ($search_val != undefined) {
          //截取科目编码
          codeK = $search_val.split(' ')[0];
        }
        if ($search_valE != undefined) {
          //截取科目编码
          codeJ = $search_valE.split(' ')[0];
        }
        //获取起始时间
        subCodeDateB = $(this).siblings('ul').find('.timeBegin').val();
        //获取结束时间
        subCodeDateE = $(this).siblings('ul').find('.timeEnd').val();
        //余额为0不显示
        var yue = $(this).siblings('ul').find('.yeIs').children("input[type='checkbox']");
        if (yue.is(":checked") == true) {
          yeIs = '1';
        }
        //无发生额且余额为0不显示
        var yueS = $(this).siblings('ul').find('.wfsYeIs').children("input[type='checkbox']");
        if (yueS.is(":checked") == true) {
          ye2Is = '1';
        }
        //只显示最明细科目
        var EndSubCode = $(this).siblings('ul').find('.xszmx').children("input[type='checkbox']");
        if (EndSubCode.is(":checked") == true) {
          isEndSubCode = '1';
        }
        //判断起始时间是否大于结束时间
        if (subCodeDateB > subCodeDateE) {
          layer.msg('开始时间不能大于结束时间');
          return false;
        }
        var kmjcKS = $(this).siblings('ul').find('.KMbegin').children('input').val();
        var kmjcJS = $(this).siblings('ul').find('.KMend').children('input').val();
        kmjcK = kmjcKS;
        kmjcJ = kmjcJS;
        type = '2';
        $('.zz_Close').text(subCodeDateB + '  至  ' + subCodeDateB);
        //关闭搜索框并重置搜索条件
        $('.zz_pop_up').hide();
        $('.zz_Close').css({ "border-bottom": "1px solid #e4e4e4" });
        //获取会计期间赋值到总账显示
        var qijianK = $(this).siblings('ul').find('.timeBeginS').text();
        var qijianJ = $(this).siblings('ul').find('.timeEndS').text();
        $('.zz_Close').text(qijianK + '  至  ' + qijianJ);
        if (window.sessionStorage.curmenu.search("总账") != -1) {
          //总账
          _allAccount();
        } else if (window.sessionStorage.curmenu.search("明细账") != -1) {
          //明细账
          _getBills();
          //调用快速切换接口
          onLoadZTree();
        }
      } else if (window.sessionStorage.curmenu.search("固定资产折旧明细") != -1) {
        //获取期间日期
        subCodeDateB = $(".search-item3 .accounting").val() || subCodeDateB;
        //显示清理资产
        var yue = $(".depreciation input[type='checkbox']");
        if (yue.is(":checked") == true) {
          isClean = 1;
        } else {
          isClean = 0;
        }
        var nian = subCodeDateB.substr(0, 4);
        var yue2 = subCodeDateB.substr(subCodeDateB.length - 2);
        var yue1 = subCodeDateB.substr(subCodeDateB.length - 1);
        if (yue2 < 10) {
          $(".dateText").text(nian + '年' + yue1 + '月折旧');
        } else {
          $(".dateText").text(nian + '年' + yue2 + '月折旧');
        }
        // 点击查询数据
        _getAllTableData();
      }
    });

    //快速切换收进
    $('.speediness span').on('click', function() {
      $('.fasttipsMian').hide();
      $('.fasttips .unfold').show();
      $('.fasttips').css({ "width": "1.5%", "background": "rgb(239, 239, 239)" });
      $('.lay_form1').css({ "width": "97%" });
    });
    //快速切换展开
    $('.fasttips .unfold').on('click', function() {
      $('.fasttipsMian').show();
      $('.fasttips .unfold').hide();
      $('.fasttips').css({ "width": "19.2%", "background": "#fff" });
      $('.lay_form1').css({ "width": "79%" });
    });

    // 查询数量金额总帐
    $('#search-stock').on('click', function() {
      var a = $('#sub_code').val(),
        b = $('#comNameSpec').val(),
        params = {
          'sub_code': a,
          'comNameSpec': b
        };

      if (!a && !b) {
        layer.msg('请输入任意查询条件', {
          time: 1000
        });
      }
      _getAmountTotal(params);
    });

    //科目余额表导出
    $('.search_export').on('click', function() {
      var balanceS = $('#accountList_item .tc').length;
      if (balanceS === 1) {
        layer.msg('没有可导出数据！');
      } else {
        window.location.href = window.baseURL + "/sbubalance/querySbujectExcleExport";
      }
    });
    //总账表导出
    $('.zongzhang_export').on('click', function() {
      var balanceS = $('#accountList_item1 .tc').length;
      if (balanceS === 1) {
        layer.msg('没有可导出数据！');
      } else {
        window.location.href = window.baseURL + "/export/exportGeneralLedger?beginTime=" + subCodeDateB + '&endTime=' + subCodeDateE + '&subCode=' + subCodeVal + '&ye=' + yeIs + '&type=' + type + '&beginSubCode=' + codeK + '&endSubCode=' + codeJ + '&startLevel=' + kmjcK + '&endLevel=' + kmjcJ + '&isEndSubCode=' + isEndSubCode;
      }
    });
    //数量金额总账导出
    $('.sljezz_btn').on('click', function() {
      var balanceS = $('#amount-account .tc').length;
      if (balanceS === 1) {
        layer.msg('没有可导出数据！');
      } else {
        window.location.href = window.baseURL + "/subBook/exportStockExcel";
      }
    });
    //明细账导出(当前)
    $(".search_export_btn").on('click', function() {
      var balanceS = $('#detailList1 .tc').length;
      if (balanceS === 1) {
        layer.msg('没有可导出数据！');
      } else {
        window.location.href = window.baseURL + "/subBook/exportDetailAccount?beginTime=" + subCodeDateB + '&endTime=' + subCodeDateE + '&subCode=' + subCodeVal + '&ye=' + yeIs + '&ye2=' + ye2Is + '&type=' + type + '&beginSubCode=' + codeK + '&endSubCode=' + codeJ + '&startLevel=' + kmjcK + '&endLevel=' + kmjcJ + '&isEndSubCode=' + isEndSubCode;
      }
    });
    //明细账导出（全部）
	$(".search_all_btn").on('click', function() {
        window.location.href = window.baseURL + "/subBook/exportAllDetailAccount?beginTime=" + subCodeDateB + '&endTime=' + subCodeDateE + '&subCode=' + subCodeVal + '&ye=' + yeIs + '&ye2=' + ye2Is + '&type=' + type + '&beginSubCode=' + codeK + '&endSubCode=' + codeJ + '&startLevel=' + kmjcK + '&endLevel=' + kmjcJ + '&isEndSubCode=' + isEndSubCode;
    });
    $("#qsSubject").on('click', function() {
      openSubPopup(1);
    });
    $("#jsSubject").on('click', function() {
      openSubPopup(2);
    });

    // 清空科目列表
    $('.level').parent().off('click').on('click', function() {
      $(this).children('div').children('.layui-select-title').children('.layui-input').val('');
    });

    // 获取焦点， 输入框内容为空
    removeInputVal($('.endTime'));
    removeInputVal($('.startTime'));
    removeInputVal($('#sub_code'));
    removeInputVal($('#comNameSpec'));

    $('#sub_code').on('input', function() {
      // 限制输入正整数
      positiveInteger($(this));
    });

    $('#comNameSpec').on('input', function() {
      // 清除空格（所有）
      removeAllSpace($(this));
    });

    $printVchr.on('click', function() {
      // 凭证打印前检测是否能打印
      if (!state.voucher || !state.voucher.length) {
        layer.msg('凭证汇总表请求错误', {
          icon: 0,
          shade: 0.1,
          time: 1000
        });
        return;
      }
      var date = window.session.busDate.split('-'),
        day = new Date(date[0], date[1], 0).getDate(),
        params = {
          'sumList': state.voucher,
          'sumListS': state.voucherListTotal,
          'dfAmount': state.dfAmount,
          'jfAmount': state.jfAmount,
          'compName': top.$('#choose-account .selectCus').text().trim(),
          'range': date[0] + '年' + date[1] + '月1日至' + day + '日',
          'operator': $('#nav .hrefPageFn').text().trim().replace('您好，', '')
        };
      layer.confirm('是否要打印凭证汇总表？', {
        icon: 0,
        title: '提示'
      }, function(index) {
        var iframeWin;
        layer.open({
          type: 2,
          title: '凭证汇总表打印',
          area: ['800px', '80%'],
          maxmin: true,
          content: window.baseURL + '/voucher/printPreview',
          btn: ['打印', '取消'],
          success: function(layero) {
            layer.closeAll('dialog');
            iframeWin = window.frames[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象
            iframeWin._sumHTML(params);
          },
          yes: function() {
            iframeWin._printPreview('sum');
          }
        });
        layer.close(index);
      });
    });

  });
  // 获取科目列表
  postRequest(window.baseURL + '/subject/querySubByIDAndName', {}, function(res) {
    var list,
      leng,
      str = '<option value="">请输入上级科目</option>',
      str1 = '<option value="">请输入下级科目</option>',
      i = 0;
    if (res.success === 'true') {
      list = res.list;
      leng = list.length;
      for (i; i < leng; i++) {
        if (list[i].subCode) {
          list[i].codenName = list[i].subCode + ' - ' + list[i].subName;
          str += '<option value="' + list[i].codenName + '">' + list[i].codenName + '</option>';
          str1 += '<option value="' + list[i].codenName + '">' + list[i].codenName + '</option>';
        }
      }
      $('#select_levelBegin').html(str);
      $('#select_levelEnd').html(str1);
      $('#select_levelBegin1').html(str);
      $('#select_levelEnd1').html(str1);
      $('#select_bills').html(str);
      $('#select_mount').html(str);
      renderSelect();
    }
  });

  state.initName = '期初余额';
  state.currentName = '本期合计';
  state.yearName = '本年累计';
  state.interestName = '利息收入';


  layui.element.on('tab(tab)', function(data) {
    state.tabIdx = data.index;
    _getTabData(state.tabIdx); //点击请求数据
  });
  // 选择科目弹窗监听切换
  //layui.element.on('tab(sub_tab)', function (data) {
  //  state.categoryId = data.index + 1;
  //  var isTab = undefined; //state.$this.attr('data-tab');
  //  new TreeData({
  //    '$JQ': $.fn.zTree,
  //    'initData': setting,
  //    'category': state.categoryId,
  //    'ulElem': $('#tree1'),
  //    'Intercept': state.$this,
  //    'flag': isTab
  //  })
  //});

  // 打开所有固定资产科目弹窗
  function openSubPopup(state) {
    var url = window.baseURL + '/assets/queryAllSub';
    $subjectPopup.attr('data-state', state);
    popup();
    // 打开弹窗
    function popup() {
      layer.open({
        type: 1,
        title: '选择固定资产科目',
        area: ['420px', '60%'],
        anim: 2,
        content: $subjectPopup,
        btn: ['确定', '取消'],
        btnAlign: 'c',
        yes: function(index) {
          selectSubject(index, state);
        },
        btn2: function() {
          $subjectPopup.hide().attr('data-title', '');
        },
        cancel: function() {
          $subjectPopup.hide().attr('data-title', '');
        }
      });
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 1,
        'ulElem': $('#tree1'),
      });
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 2,
        'ulElem': $('#tree2'),
      })
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 3,
        'ulElem': $('#tree3'),
      })
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 4,
        'ulElem': $('#tree4'),
      })
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 5,
        'ulElem': $('#tree5'),
      })
      new TreeData({
        '$JQ': $.fn.zTree,
        'initData': setting,
        'category': 6,
        'ulElem': $('#tree6'),
      })
    }
  }

  // 选择固定资产科目
  function selectSubject(index, state) {
    var title = treeNodeName;
    if (title == null) {
      return;
    }
    if (state == 1) {
      $('#qsSubject').val(title);
    } else if (state == 2) {
      $('#jsSubject').val(title);
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



  //重置
  function reset_btn() {
    $('.timeBegin').val(subCodeDateB);
    $('.timeEnd').val(subCodeDateB);
    if (yue2 < 10) {
      $('.timeBeginS').text(nian + '年第' + yue1 + '期');
      $('.timeEndS').text(nian + '年第' + yue1 + '期');
    } else {
      $('.timeBeginS').text(nian + '年第' + yue2 + '期');
      $('.timeEndS').text(nian + '年第' + yue2 + '期');
    }
    $('#qsSubject').val('');
    $('#jsSubject').val('');
    $('.KMbegin input, .KMend input').val('');
    $('.zz_pop_up ul li div').removeClass('layui-form-checked');
  }

  // 根据切换列表index的不同，点击查询获取不同的数据
  function _getAllTableData() {
    if (window.sessionStorage.curmenu.search("总账") != -1) {
      $(".xszmx, .wfsYeIs").show();
      //重置
      reset_btn();
      // 总账1
      _allAccount();
    } else if (window.sessionStorage.curmenu.search("明细账") != -1) {
      // 明细账列表
      $(".xszmx, .wfsYeIs").hide();
      //重置
      reset_btn();
      // 明细账1
      _getBills();
      //起止时间
      startStopDate();
      //调用快速切换接口
      onLoadZTree();
    } else if (window.sessionStorage.curmenu.search("数量金额总账") != -1) {
      // 数量金额总帐
      var params = {
        'sub_code': $('#sub_code').val(),
        'comNameSpec': $('#comNameSpec').val()
      };
      _getAmountTotal(params);
    } else if (window.sessionStorage.curmenu.search("数量金额明细帐") != -1) {
      // 数量金额明细帐
      var params = {
        'startTime': state.startTime || '',
        'endTime': state.endTime || '',
        'subMessage': state.code || '',
      };
      _getAmount(params);
    } else if (window.sessionStorage.curmenu.search("科目余额表") != -1) {
      // 科目余额表
      var params = {
        'startTime': state.startTime || '',
        'endTime': state.endTime || ''
      };
      _getBalance();
    } else if (window.sessionStorage.curmenu.search("固定资产折旧明细") != -1) {
      //起止时间
      startStopDate();
      //固定资产折旧明细
      depreciation();
    }
    //  switch (state.tabIndex) {
    //    case 0:
    //      $(".xszmx, .wfsYeIs").show();
    //      //重置
    //    reset_btn();
    //      // 总账1
    //      _allAccount();
    //      break;
    //    case 1:
    //      // 明细账列表
    //      $(".xszmx, .wfsYeIs").hide();
    //      //重置
    //    reset_btn();
    //      // 明细账1
    //      _getBills();
    //      //起止时间
    //    startStopDate();
    //      //调用快速切换接口
    //      onLoadZTree();
    //      break;
    //    case 2:
    //      // 数量金额总帐
    //      var params = {
    //        'sub_code': $('#sub_code').val(),
    //        'comNameSpec': $('#comNameSpec').val()
    //      };
    //      _getAmountTotal(params);
    //      break;
    //    case 3:
    //      // 数量金额明细帐
    //      var params = {
    //        'startTime': state.startTime || '',
    //        'endTime': state.endTime || '',
    //        'subMessage': state.code || '',
    //      };
    //      _getAmount(params);
    //      break;
    //    case 4:
    //      // 科目余额表
    //      var params = {
    //        'startTime': state.startTime || '',
    //        'endTime': state.endTime || ''
    //      };
    //      _getBalance();
    //      break;
    //    case 5:
    //      // 凭证汇总表
    //      break;
    //    case 6:
    //      //起止时间
    //    startStopDate();
    //      //固定资产折旧明细
    //      depreciation();
    //    break;
    //    default:
    //      // 总账列表
    //      var params = {
    //        'startTime': state.startTime || '',
    //        'endTime': state.endTime || '',
    //        'subMessage': state.code || '',
    //      };
    //      _allAccount(params);
    //  }
  }

  // 数据接口调用请求
  function _setTabData(index) {
    switch (index) {
      case 0:
        //还原查询条件
        subCodeDateB = session.busDate; //起始时间
        subCodeDateE = session.busDate; //结束时间
        codeK = ''; //起始科目
        codeJ = ''; //结束科目
        kmjcK = ''; //科目节次开始
        kmjcJ = ''; //科目节次结束
        yeIs = ''; //余额为0不显示
        isEndSubCode = ''; //只显示最明细科目
        ye2Is = ''; //无发生额且余额为0不显示
        type = '1'; //1是总账或者明细账菜单 2是筛选框
        isClean = 0; //显示已清理资产0不显示1显示
        $(".wfsYeIs").show();
        $(".xszmx").hide();
        //重置
        reset_btn();
        // 总账1
        _allAccount();
        break;
      case 1:
        //还原查询条件
        subCodeDateB = session.busDate; //起始时间
        subCodeDateE = session.busDate; //结束时间
        codeK = ''; //起始科目
        codeJ = ''; //结束科目
        kmjcK = ''; //科目节次开始
        kmjcJ = ''; //科目节次结束
        yeIs = ''; //余额为0不显示
        isEndSubCode = ''; //只显示最明细科目
        ye2Is = ''; //无发生额且余额为0不显示
        type = '1'; //1是总账或者明细账菜单 2是筛选框
        isClean = 0; //显示已清理资产0不显示1显示
        $(".xszmx, .wfsYeIs").hide();
        //重置
        reset_btn();
        // 明细账1
        _getBills();
        //起止时间
        startStopDate();
        //调用快速切换接口
        onLoadZTree();
        break;
      case 2:
        if (state.amountTotal > 0) {
          fixedHead($('#fixedDOM3'), $('#amount-total'), $('#searchDOM3'));
          return false;
        }
        _getAmountTotal(); // 数量金额总帐
        break;
      case 3:
        if (state.amiunt > 0) {
          fixedHead($('#fixedDOM4'), $('#num-amount'), $('#searchDOM4'));
          return false;
        }
        _getAmount(); // 数量金额明细帐
        break;
      case 4:
        if (state.balance > 0) {
          fixedHead($('#balance').parent(), $('#searchDOM5'));
          return false;
        }
        _getBalance(); // 科目余额表
        break;
      case 5:
        // 凭证汇总表
        if (state.voucher && state.voucher.length) {
          fixedHead($('#searchDOM6'));
          return false;
        }
        _getVchrSum();
        break;
      case 6:
        //固定资产折旧明细
        depreciation();
        //起止时间
        startStopDate();
        break;
      default:
        // 默认总账
        fixedHead($('#fixedDOM1'), $('#accountList'));
    }
  }

  // 数量金额总账
  function _getAmountTotal(param) {
    var url = window.baseURL + '/stock/list',
      params = {
        'page': 1,
        'limit': 50
      },
      amountList,
      listStr = "",
      i = 0,
      leng,
      direction;

    $.extend(params, param);
    postRequest(url, params, function(res) {
      if (res.code === '0') {
        amountList = res.data || [];
        leng = amountList.length;
        state.amountTotal = leng;
        if (leng > 0) {
          for (i; i < leng; i++) {
            var list = {
              'qc_balancePrice': amountList[i].qc_balancePrice,
              'qc_balanceAmount': amountList[i].qc_balanceAmount,
              'bq_incomeAmount': amountList[i].bq_incomeAmount,
              'bq_issueAmount': amountList[i].bq_issueAmount,
              'total_incomeAmount': amountList[i].total_incomeAmount,
              'total_issueAmount': amountList[i].total_issueAmount,
              'qm_balancePrice': amountList[i].qm_balancePrice,
              'qm_balanceAmount': amountList[i].qm_balanceAmount
            }
            var initArray = initNumber2(list);

            listStr += '<tr>' +
              '<td class="tl" >' + (amountList[i].sub_code || '') + '</td>' +
              '<td >' + amountList[i].sub_comName + '</td>' +
              '<td class="tc">' + (amountList[i].qc_balanceNum || "") + '</td>' +
              '<td class="tr">' + (initArray[0] || "") + '</td>' +
              '<td class="tr">' + (initArray[1] || "") + '</td>' +
              '<td>' + (amountList[i].bq_incomeNum || "") + '</td>' +
              '<td class="tr">' + (initArray[2] || "") + '</td>' +
              '<td>' + (amountList[i].bq_issueNum || "") + '</td>' +
              '<td class="tr">' + (initArray[3] || "") + '</td>' +
              '<td>' + (amountList[i].total_incomeNum || "") + '</td>' +
              '<td class="tr">' + (initArray[4] || "") + '</td>' +
              '<td>' + (amountList[i].total_issueNum || "") + '</td>' +
              '<td class="tr">' + (initArray[5] || "") + '</td>' +
              '<td class="tc">' + (amountList[i].balance_direction || '') + '</td>' +
              '<td class="tc">' + (amountList[i].qm_balanceNum || "") + '</td>' +
              '<td class="tr">' + (initArray[6] || "") + '</td>' +
              '<td class="tr">' + (initArray[7] || "") + '</td>' +
              '</tr>';
          }
        } else {
          listStr = '<tr class="tc"><td colspan="17" style="height: 80px;">暂无数据</td></tr>';
        }
        $('#amount-account').html(listStr);
        flushPage('layui-table-page1', params.page, res.count, params.limit, function(leng) {
          var params = {
            'page': leng,
            'sub_code': $('#sub_code').val(),
            'comNameSpec': $('#comNameSpec').val()
          };
          _getAmountTotal(params);
        });
        fixedHead($('#fixedDOM3'), $('#amount-total'), $('#searchDOM3'));
      } else {
        layer.msg(res.msg || '数量金额总账请求错误');
      }
    });
  }

  // 数量金额明细帐
  function _getAmount(params) {
    var url = window.baseURL + '/ledger/queryLedgerByParameters',
      params = params || {},
      amountList,
      listStr = "",
      i = 0,
      leng,
      direction;
    postRequest(url, params, function(res) {
      if (res.code === 1) {
        amountList = res.subMessages;
        leng = amountList.length;
        state.amiunt = leng;
        if (leng > 0) {
          for (i; i < leng; i++) {
            var list = {
              'initDebitBalance': amountList[i].initDebitBalance,
              'currentAmountDebit': amountList[i].currentAmountDebit,
              'yearAmountDebit': amountList[i].yearAmountDebit,
              'initCreditBalance': amountList[i].initCreditBalance,
              'currentAmountCredit': amountList[i].currentAmountCredit,
              'yearAmountCredit': amountList[i].yearAmountCredit
            }
            var initArray = initNumber(list);

            // 判断方向
            direction = _isMaxNum(amountList[i]);
            listStr += '<tr>' +
              '<td>' + amountList[i].accountPeriod + '</td>' +
              '<td></td>' +
              '<td>' + state.initName + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[0] + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[3] + '</td>' +
              '<td>' + direction.text + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + direction.num + '</td>' +
              '</tr>' +
              '<tr>' +
              '<td>' + amountList[i].accountPeriod + '</td>' +
              '<td></td>' +
              '<td>' + state.currentName + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[1] + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[4] + '</td>' +
              '<td>' + direction.val + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + direction.num1 + '</td>' +
              '</tr>' +
              '<tr>' +
              '<td>' + amountList[i].accountPeriod + '</td>' +
              '<td></td>' +
              '<td>' + state.yearName + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[2] + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + initArray[5] + '</td>' +
              '<td>' + direction.val + '</td>' +
              '<td></td>' +
              '<td></td>' +
              '<td class="tr">' + direction.num1 + '</td>' +
              '</tr>';
          }
        } else {
          listStr = '<tr class="tc"><td colspan="13" style="height: 80px;">暂无数据</td></tr>';
        }
        $('#amount').html(listStr);
        fixedHead($('#fixedDOM4'), $('#num-amount'), $('#searchDOM4'));
      } else {
        layer.msg('数量金额明细帐请求错误');
      }
    });
  }

  // 科目余额表
  function _getBalance(params) {
    var url = window.baseURL + '/sbubalance/querySbujectBalance',
      params = params || {},
      $tbody = $('#balance'),
      $tfoot = $('#balance').next();
    $tfoot.hide();
    postRequest(url, params, function(res) {
      var balanceList,
        listStr = "",
        tfootStr = '',
        i = 0,
        leng,
        currentDebitMoney = 0,
        currentCreditMoney = 0;

      if (res.code === 1) {
        balanceList = res.subMessages;
        leng = balanceList.length;
        state.balance = leng;
        if (leng > 0) {
          for (i; i < leng; i++) {
            if (balanceList[i].subCode) {

              var list = {
                'initDebitBalance': balanceList[i].initDebitBalance,
                'currentAmountDebit': balanceList[i].currentAmountDebit,
                'yearAmountDebit': balanceList[i].yearAmountDebit,
                'initCreditBalance': balanceList[i].initCreditBalance,
                'currentAmountCredit': balanceList[i].currentAmountCredit,
                'yearAmountCredit': balanceList[i].yearAmountCredit,
                'endingBalanceDebit': balanceList[i].endingBalanceDebit,
                'endingBalanceCredit': balanceList[i].endingBalanceCredit
              }
              var initArray = initNumber(list, 1);

              currentDebitMoney += (balanceList[i].currentAmountDebit);
              currentCreditMoney += (balanceList[i].currentAmountCredit);
              listStr += '<tr>' +
                '<td>' + balanceList[i].accountPeriod + '</td>' +
                '<td>' + balanceList[i].subCode + '</td>' +
                '<td class="tl">' + balanceList[i].subName + '</td>' +
                '<td  class="tr">' + initArray[0] + '</td>' +
                '<td  class="tr">' + initArray[3] + '</td>' +
                '<td  class="tr" >' + initArray[1] + '</td>' +
                '<td  class="tr">' + initArray[4] + '</td>' +
                '<td  class="tr">' + initArray[2] + '</td>' +
                '<td  class="tr">' + initArray[5] + '</td>' +
                '<td  class="tr">' + initArray[6] + '</td>' +
                '<td  class="tr">' + initArray[7] + '</td>' +
                '</tr>';
            }
          }

          // 合计
          var totalArr = initNumber({
            initDebitBalanceTotal: res.initDebitBalanceTotal,
            initCreditBalanceTotal: res.initCreditBalanceTotal,
            currentAmountDebitTotal: res.currentAmountDebitTotal,
            currentAmountCreditTotal: res.currentAmountCreditTotal,
            yearAmountDebitTotal: res.yearAmountDebitTotal,
            yearAmountCreditTotal: res.yearAmountCreditTotal,
            endingBalanceDebitTotal: res.endingBalanceDebitTotal,
            endingBalanceCreditTotal: res.endingBalanceCreditTotal
          });
          tfootStr = '<tr><td></td><td></td><td><strong>合计：</strong></td>' +
            '<td class="tr">' + totalArr[0] + '</td>' +
            '<td class="tr">' + totalArr[1] + '</td>' +
            '<td class="tr">' + totalArr[2] + '</td>' +
            '<td class="tr">' + totalArr[3] + '</td>' +
            '<td class="tr">' + totalArr[4] + '</td>' +
            '<td class="tr">' + totalArr[5] + '</td>' +
            '<td class="tr">' + totalArr[6] + '</td>' +
            '<td class="tr">' + totalArr[7] + '</td></tr>';
          $tbody.html(listStr);
          $tfoot.html(tfootStr).show();
        } else {
          listStr = '<tr class="tc"><td colspan="11" style="height: 80px;text-align:center;">暂无数据</td></tr>';
        }
      } else {
        layer.msg('科目余额表请求错误');
        listStr = '<tr class="tc"><td colspan="11" style="height: 80px;text-align:center;">科目余额表请求错误</td></tr>';
      }
      $tbody.html(listStr);
      fixedHead($('#balance').parent(), $('#searchDOM5'));
      if ($("#balancesID table").height() > $("#balancesID").height()) {
        if(window.screen.width < 1680){
			$("#fixedDOM5").css({ "width": "calc(100% - 22px)" })
		}else{
			$("#fixedDOM5").css({ "width": "calc(100% - 17px)" })
		}
      }
    });
  }

  // 凭证汇总表
  function _getVchrSum() {
    getRequest(window.baseURL + '/voucher/voucherSummary', function(res) {
      if (res.code == 1) {
        var list = res.info || [],
          len = list.length,
          amount,
          direction,
          i = 0,
          str = '';

        state.voucher = list;
        state.voucherListTotal = res.num;
        state.dfAmount = res.dfAmount;
        state.jfAmount = res.jfAmount;
        state.voucherListTotal
        if (len) {
          $.each(list, function(i, el) {
            amount = initNumber({
              Debit: el.currentAmountDebit,
              Credit: el.currentAmountCredit
            });
            direction = el.debitCreditDirection;
            str += '<tr>' +
              '<td>' + (++i) + '</td>' +
              '<td class="tl">' + el.subCode + '</td>' +
              '<td class="tl">' + el.subName + '</td>' +
              '<td class="tr">' + amount[0] + '</td>' +
              '<td class="tr">' + amount[1] + '</td>' +
              '</tr>';
          });
        } else {
          str = '<tr class="tc"><td colspan="5" style="height: 80px;text-align:center;">暂无数据</td></tr>';
        }
        $('#sum-voucher').html(str);
        fixedHead($('#searchDOM6'));
      } else {
        layer.msg(res.info || '凭证汇总表获取失败', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }
    });
  }

  // 明细账字符串拼接
  function _billsSrt(detailList) {
    var str = "",
      i = 0,
      leng = detailList.length,
      direction;
    state.bills = leng;
    if (leng > 0) {
      for (i; i < leng; i++) {
        var list = {
          'initDebitBalance': detailList[i].initDebitBalance,
          'currentAmountDebit': detailList[i].currentAmountDebit,
          'yearAmountDebit': detailList[i].yearAmountDebit,
          'initCreditBalance': detailList[i].initCreditBalance,
          'currentAmountCredit': detailList[i].currentAmountCredit,
          'yearAmountCredit': detailList[i].yearAmountCredit,

        }
        var initArray = initNumber(list);

        // 判断方向
        direction = _isMaxNum(detailList[i]);
        if (detailList[i].subCode) {
          str += '<tr>' +
            '<td>' + detailList[i].accountPeriod + '</td>' +
            '<td></td>' +
            '<td class="tl">' + detailList[i].subCode + '-' + detailList[i].subName + '</td>' +
            '<td>' + state.initName + '</td>' +
            '<td  class="tr">' + initArray[0] + '</td>' +
            '<td  class="tr">' + initArray[3] + '</td>' +
            '<td>' + direction.text + '</td>' +
            '<td  class="tr">' + direction.num + '</td>' +
            '</tr>'

          str += '<tr>' +
            '<td>' + detailList[i].accountPeriod + '</td>' +
            '<td></td>' +
            '<td class="tl">' + detailList[i].subCode + '-' + detailList[i].subName + '</td>' +
            '<td>' + state.currentName + '</td>' +
            '<td  class="tr">' + initArray[1] + '</td>' +
            '<td  class="tr">' + initArray[4] + '</td>' +
            '<td>' + direction.val + '</td>' +
            '<td  class="tr">' + direction.num1 + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td>' + detailList[i].accountPeriod + '</td>' +
            '<td></td>' +
            '<td class="tl">' + detailList[i].subCode + '-' + detailList[i].subName + '</td>' +
            '<td>' + state.yearName + '</td>' +
            '<td  class="tr">' + initArray[2] + '</td>' +
            '<td  class="tr">' + initArray[5] + '</td>' +
            '<td>' + direction.val + '</td>' +
            '<td  class="tr">' + direction.num1 + '</td>' +
            '</tr>';
        }
      }
    } else {
      str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无数据</td></tr>';
    }
    $('#detailList').html(str);
    fixedHead($('#fixedDOM2'), $('#detailed'));
  }

  //总账1
  function _allAccount() {
    startStopDate();
    var url = window.baseURL + '/subBook/getLedgerAccount',
      accountList,
      rez = '',
      str = "";
    var params = {
      'beginTime': subCodeDateB, //开始期间
      'endTime': subCodeDateE, //结束期间
      'startLevel': kmjcK, //开始科目节次
      'endLevel': kmjcJ, //结束科目节次
      'ye': yeIs, //余额为0不显示
      'ye2': ye2Is, //无发生额且余额为0不显示
      'beginSubCode': codeK, //开始科目
      'endSubCode': codeJ //结束科目
    }
    layer.load();
    postRequest(url, params, function(res) {
      if (res.code == 0) {
    	  var load = layer.load();
        accountList = res.msg;
        if (accountList != null) {
          leng = accountList.length;
          if (leng > 0) {
            for (var i = 0; i < leng; i++) {
              var qj = '', //期间
                zy = '', //摘要
                jfje = '', //借方金额
                dfje = '', //贷方金额
                fx = '', //方向
                ye = ''; //余额
              for (var j = 0; j < accountList[i].list.length; j++) {
                if (accountList[i].list[j].jf_amount == 0) {
                  accountList[i].list[j].jf_amount = '';
                }
                if (accountList[i].list[j].df_amount == 0) {
                  accountList[i].list[j].df_amount = '';
                }
                if (accountList[i].list[j].amount == 0) {
                  accountList[i].list[j].amount = '';
                }
                qj += '<p>' + accountList[i].list[j].account_period + '</p>';
                zy += '<p>' + accountList[i].list[j].zhaiYao + '</p>';
                jfje += '<p class="isZero">' + accountList[i].list[j].jf_amount + '</p>';
                dfje += '<p class="isZero">' + accountList[i].list[j].df_amount + '</p>';
                fx += '<p>' + accountList[i].list[j].fx_jd + '</p>';
                ye += '<p class="isZero">' + accountList[i].list[j].amount + '</p>';
              }
              str += '<tr>' +
                '<td><a class="subCodeS" style="text-decoration:underline;cursor: pointer;">' + accountList[i].subCode + '</a></td>' +
                '<td>' + accountList[i].subName + '</td>' +
                '<td>' + qj + '</td>' +
                '<td>' + zy + '</td>' +
                '<td>' + jfje + '</td>' +
                '<td>' + dfje + '</td>' +
                '<td>' + fx + '</td>' +
                '<td>' + ye + '</td>' +
                '</tr>';
            }
          } else {
            str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无数据</td></tr>';
          }
        } else {
          str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无数据</td></tr>';
        }
        $('#accountList_item1').html(str);
        if ($("#accountListS table").height() > $("#accountListS").height()) {
        	if(window.screen.width < 1680){
				$("#accountListHead").css({ "width": "calc(100% - 22px)" })
			}else{
				$("#accountListHead").css({ "width": "calc(100% - 17px)" })
			}

        }
        layer.close(load);
      } else {
        layer.msg(res.msg || '总账列表请求错误');
      }
    });
    var content = $("#content").width();
    if(window.screen.width < 1680){
		$(".dataTables_scrollHeadInner").css({ 'width': content - 22 });
	}else{
		$(".dataTables_scrollHeadInner").css({ 'width': content - 17 });
	}
  };

  //明细账1
  function _getBills(page) {
    var url = window.baseURL + '/subBook/getDetailAccount',
      accountList,
      rez = '',
      str = "";
    var params = {
      'beginTime': subCodeDateB, //开始期间
      'endTime': subCodeDateE, //结束期间
      'currPage': page || 1, //第几页
      'subCode': subCodeVal, //科目编码
      'ye': yeIs, //余额为0不显示
      'type': type, //入口 1是总账或者明细账菜单 2是筛选框 3是快速查询
      'ye2': ye2Is, //余额为0且发生额为0不显示
      'beginSubCode': codeK, //起始科目
      'endSubCode': codeJ, //结束科目
      'startLevel': kmjcK, //起始等级
      'endLevel': kmjcJ, //结束等级
      'isEndSubCode': isEndSubCode //只显示最明细科目
    }
    layer.load();
    postRequest(url, params, function(res) {
      if (res.code == 0) {
        accountList = res.msg;
        if (accountList != null) {
          for (var i = 0; i < accountList.length; i++) {
            var listLength = accountList[i].list.length,
              pzzh = '',
              zy = '',
              jf = '',
              rq = '',
              df = '';
            for (var j = 0; j < listLength; j++) {
              if (accountList[i].list[j].vouchNum == null) {
                accountList[i].list[j].vouchNum = '';
                pzzh = accountList[i].list[j].vouchNum;
                zy = '<span style="padding-left:12px">' + accountList[i].list[j].vcabstact + '</span>';
                //时间戳转换
                var date = new Date(accountList[i].list[j].updateDate);
                Y = date.getFullYear() + '-';
                M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                D = date.getDate() + ' ';
                h = date.getHours() + ':';
                m = date.getMinutes() + ':';
                s = date.getSeconds();
                rq = Y + M + D + h + m + s;
              } else {
                pzzh = '<a class="vouchNum" id="' + accountList[i].list[j].vouchID + '" style="text-decoration:underline;cursor:pointer;">记-' + accountList[i].list[j].vouchNum + '</a>';
                zy = accountList[i].list[j].vcabstact;
                //时间戳转换
                var date = new Date(accountList[i].list[j].up_date);
                Y = date.getFullYear() + '-';
                M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                D = date.getDate() + ' ';
                h = date.getHours() + ':';
                m = date.getMinutes() + ':';
                s = date.getSeconds();
                rq = Y + M + D + h + m + s;
              }
              /*  if(accountList[i].list[j].debitAmount == null){
                  accountList[i].list[j].debitAmount = '';
                }
                if(accountList[i].list[j].creditAmount == null){
                  accountList[i].list[j].creditAmount = '';
                }*/
              jf = accountList[i].list[j].debitAmount;
              df = accountList[i].list[j].creditAmount;
              var ye = accountList[i].list[j].blanceAmount;
              if (jf) {
                jf = jf.toFixed(2);
              } else {
                jf = '';
              }
              if (df) {
                df = df.toFixed(2);
              } else {
                df = '';
              }
              if (ye) {
                ye = ye.toFixed(2);
              } else {
                ye = '';
              }
              str += '<tr>' +
                '<td>' + rq + '</td>' +
                '<td style="text-align: left;">' + pzzh + '</td>' +
                '<td style="text-align: left;">' + zy + '</td>' +
                '<td style="text-align: right;">' + jf + '</td>' +
                '<td style="text-align: right;">' + df + '</td>' +
                '<td>' + accountList[i].list[j].direction + '</td>' +
                '<td style="text-align: right;">' + ye + '</td>' +
                '</tr>'
            }
          }
        } else {
          str = '<tr class="tc"><td colspan="7" style="height: 80px;">暂无数据</td></tr>';
        }
        $('#detailList1').html(str);
        //window.parent.document.getElementsByClassName("layui-show").getElementById("#detailList1").innerHTML = str;
      } else {
        layer.msg(res.msg || '明细表请求错误');
      }
      //分页
      flushPage('layui-table-page2', params.currPage, res.cout, res.size, _getBills);
    })
  }

  //固定资产折旧明细
  function depreciation() {
    var url = window.baseURL + '/assets/queryZjDetail';
    var params = {
      'time': subCodeDateB, //期间
      'isClean': isClean //是否清理资产
    }
    postRequest(url, params, function(res) {
      if (res.success == "true") {
        var list = res.list;
        if (list != null) {
          var listLength = list.length,
            str = "";
          if (listLength > 0) {
            for (var i = 0; i < listLength; i++) {
              if(list[i].yjzl == null || list[i].yjzl =="null"){
                list[i].yjzl = "";
              }
              str += '<tr>' +
                '<td>' + list[i].asCategory + '</td>' +
                '<td style="text-align: left;">' + list[i].asCode + '</td>' +
                '<td style="text-align: left;">' + list[i].asName + '</td>' +
                '<td style="text-align: right;">' + list[i].department + '</td>' +
                '<td style="text-align: right;">' + list[i].yjzl + '</td>' +
                '<td>' + list[i].asvalue + '</td>' +
                '<td>' + list[i].ssyzje + '</td>' +
                '<td>' + list[i].qmSum + '</td>' +
                '<td>' + list[i].bqSum + '</td>' +
                '<td style="text-align: right;">' + list[i].asWorth + '</td>' +
                '</tr>'
            }
          } else {
            str = '<tr class="tc"><td colspan="10" style="height: 80px;">暂无数据</td></tr>'
          }
        } else {
          str = '<tr class="tc"><td colspan="10" style="height: 80px;">暂无数据</td></tr>'
        }
        $('#depreciation_item').html(str);
      } else {
        layer.msg(res.mag || '固定资产折旧明细请求错误');
      }
      $('#gdzcmxbDOM1').show();
      //      var content = $("#depreciation").width();
      //      $("#gdzcmxbDOM1").css({'width':content});
    })
  }

  //获取会计期间
  function startStopDate() {
    var str = '';
    postRequest(window.baseURL + '/subBook/getRangePeriod', {}, function(res) {
      if (res.code == 0) {
        if (res.msg == null) {
          str = '<li>暂无数据</li>';
        } else {
          if (res.msg.length != 0) {
            for (var i = 0; i < res.msg.length; i++) {
              str += '<li>' + res.msg[i].day2 + '<input type="text" style="display: none;" class="layui-input" disabled="disabled" value="' + res.msg[i].day1 + '"></li>'
            }
          } else {
            str = '<li>暂无数据</li>';
          }
          $('#startStopDate').html(str);
          $('#startStopDate1').html(str);
          $('#startStopDate2').html(str);
        }
      } else {
        layer.msg('起止时间请求错误');
      }
    })
  }
  
  $('#accountList_item1').delegate('.subCodeS', 'click', function() {
    subCodeVal = $(this).text(); //科目编号
    var subCodeName = $(this).parent('td').next('td').text();
    var detail = window.parent.document.getElementById("detail");
    $(detail).attr({ "data-url": window.baseURL + "/accountBook/queryView#8?typeS=2&Val=" + subCodeVal + "&Name=" + subCodeName + "" }); //第一次有效
    detail.click();
    top.$('.clildFrame > .layui-show iframe')[0].src = window.baseURL + "/accountBook/queryView#8?typeS=2&Val=" + subCodeVal + "&Name=" + subCodeName + "";
    top.$('.clildFrame > .layui-show iframe')[0].contentWindow.location.reload();
    debugger;
    subCodeVal = $(this).text();
    _getBills();
    $('.sub_code_name').text(subCodeVal + ' - ' + subCodeName);
  });

  $('#accountList_item1').delegate('tr', 'click', function() {
    $(this).css({ "background": "#dec9c93d" });
    $(this).siblings('tr').css({ "background": "#fff" });
  })

  // 打开编辑凭证
  $('#detailList1').delegate('.vouchNum', 'click', function() {
    var vouchNum = $(this).attr('id');
    window.sessionStorage.setItem('editVouch', JSON.stringify({
      'id': vouchNum,
      'isHasData': true,
      'isEditor': '已审核'
    }));
    // // 点击切换标签页，重新加载iframe的URL
    var layId = window._currTabTimeStamp();
    top.$('#compilePZ').click();
    var _iframe = top.$('#top_tabs_box > .clildFrame > .layui-show iframe')[0];
    _iframe.src = window.baseURL + "/voucher/editView?source=1&from=accounts&layId=" + layId;
    // top.$('.clildFrame > .layui-show iframe')[0].contentWindow.location.reload();
  });

  // 格式话余额数据
  function _isMaxNum(item) {
    if (!item) {
      return false;
    }
    var direction = item.debitCreditDirection; // 余额方向： 1：借 2：贷
    var balanceMoney = 0;
    if (direction === '1') {
      balanceMoney = item.initDebitBalance - item.initCreditBalance; //期初余额 借-贷
    } else if (direction === '2') {
      balanceMoney = item.initCreditBalance - item.initDebitBalance; //期初余额 贷-借
    }
    var amountMoney = balanceMoney + item.currentAmountDebit - item.currentAmountCredit; // 本期合计 和 本年累计的余额是同样的
    var text = _ifTrue(direction, balanceMoney); //期初余额方向
    var val = _ifTrue(direction, amountMoney); // 本期合计 和 本年累计 的余额方向
    var num = 0;
    var num1 = 0;
    // 获取两位小数 和千分号
    num = formatNum(balanceMoney.toFixed(2)); //期初余额
    num1 = formatNum(amountMoney.toFixed(2)); //本期合计
    return {
      'text': text,
      'val': val,
      'num': num,
      'num1': num1
    }
  }

  // 判断借贷方向
  function _ifTrue(direction, balanceMoney) {
    var text = '';
    if (direction === '1') { // 借方
      if (balanceMoney == 0) {
        text = '平';
      } else {
        text = '借';
      }
    } else if (direction === '2') {
      if (balanceMoney == 0) {
        text = '平';
      } else {
        text = '贷';
      }
    }
    return text;
  }

  // 格式化数字
  function initNumber(list, type) {
    var arr = [];
    for (var i in list) {
      if (list[i] === null) {
        list[i] = 0;
      }
      var srt = list[i].toFixed(2);
      arr.push(formatNum(srt));
    }
    return arr;
  }

  // 格式化数字
  function initNumber2(list, type) {
    var arr = [];
    for (var i in list) {
      if (list[i] === null) {
        arr.push(null);
      } else {
        var srt = list[i].toFixed(2);
        arr.push(formatNum(srt));
      }
    }
    return arr;
  }

  // 科目余额表 首页跳转过来
  function showTable(num) {
    $('#home-balance').click();
    _getBalance();
    return;
    $('#home-balance').addClass('layui-this').siblings().removeClass('layui-this');
    $('.balanceTable').addClass('layui-show').siblings().removeClass('layui-show');
    var width = $('.balanceTable').width();
    $('#home-balance').parent().width(width);
    fixedHead($('#fixedDOM4'), $('#balance').parent(), $('#searchDOM4'));
  }

  //明细快速切换tree
  function onLoadZTree() {
    //切换选项清空数组
    treeText = [];
    //切换选项隐藏切换提示并清空快速切换查询条件
    $("#append").hide();
    $("#keyword").val('');
    var treeNodes;
    var url = window.baseURL + '/subBook/fastQuery',
      accountList,
      rez = '',
      str = "";
    var params = {
      'beginTime': subCodeDateB, //开始期间
      'endTime': subCodeDateE, //结束期间
      'ye': yeIs, //余额为0不显示
      'type': type, //入口 1是总账或者明细账菜单 2是筛选框
      'ye2': ye2Is, //余额为0且发生额为0不显示
      'beginSubCode': codeK, //起始科目
      'endSubCode': codeJ, //结束科目
      'startLevel': kmjcK, //起始等级
      'endLevel': kmjcJ, //结束等级
      'isEndSubCode': isEndSubCode //只显示最明细科目
    }
    postRequest(url, params, function(res) {
      if (res.code == 0) {
        //请求成功后处理函数
        var newArr = [];
        var list = res.msg.list;
        if (list != null || list.length > 0) {
          var listTree = list.length;
          for (var i = 0; i < listTree; i++) {
            newArr.push({
              'id': list[i].sub_code,
              'pId': list[i].superior_coding,
              'name': list[i].sub_code + ' - ' + list[i].sub_name,
              'subCode': list[i].sub_code,
              'superiorCoding': list[i].sub_name
            });
            treeText.push(list[i].sub_code + ' - ' + list[i].sub_name);
          }
        } else {
          treeText += '<li style="text-align:center">暂无数据</li>'
        }
        treeNodes = newArr; //把后台封装好的简单Json格式赋给treeNodes
        $("#append").html(treeText);
      } else {
        layer.msg(res.msg || '快速入口请求错误');
      }
      var t = $("#subjectTree");
      t = $.fn.zTree.init(t, setting, treeNodes);
    })
  }
}());

function zTreeOnClick(event, treeId, treeNode, page) {
  if (treeNode == undefined) {
    subCodeVal = subCodeVal;
  } else {
    subCodeVal = treeNode.id;
    $('.sub_code_name').text(treeNode.name);
  }
  if ($('#subject-popup').css('display') === 'none') {
    var url = window.baseURL + '/subBook/getDetailAccount',
      accountList,
      rez = '',
      str = "";
      type = '3';
    var params = {
      'beginTime': subCodeDateB, //开始期间
      'endTime': subCodeDateE, //结束期间
      'currPage': page || 1, //第几页
      'ye': yeIs, //余额为0不显示
      'subCode': subCodeVal, //科目编码
      'type': type, //入口 1是总账或者明细账菜单 2是筛选框
    }
    postRequest(url, params, function(res) {
      if (res.code == 0) {
        accountList = res.msg;
        if (accountList != null) {
          for (var i = 0; i < accountList.length; i++) {
            var listLength = accountList[i].list.length,
              pzzh = '',
              zy = '',
              jf = '',
              rq = '',
              df = '';
            for (var j = 0; j < listLength; j++) {
              if (accountList[i].list[j].vouchNum == null) {
                accountList[i].list[j].vouchNum = '';
                pzzh = accountList[i].list[j].vouchNum;
                zy = '<span style="padding-left:12px">' + accountList[i].list[j].vcabstact + '</span>';
                //时间戳转换
                var date = new Date(accountList[i].list[j].updateDate);
                Y = date.getFullYear() + '-';
                M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                D = date.getDate() + ' ';
                h = date.getHours() + ':';
                m = date.getMinutes() + ':';
                s = date.getSeconds();
                rq = Y + M + D + h + m + s;
              } else {
                pzzh = '<a class="vouchNum" id="' + accountList[i].list[j].vouchID + '" style="text-decoration:underline;cursor:pointer;">记-' + accountList[i].list[j].vouchNum + '</a>';
                zy = accountList[i].list[j].vcabstact;
                //时间戳转换
                var date = new Date(accountList[i].list[j].up_date);
                Y = date.getFullYear() + '-';
                M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                D = date.getDate() + ' ';
                h = date.getHours() + ':';
                m = date.getMinutes() + ':';
                s = date.getSeconds();
                rq = Y + M + D + h + m + s;
              }
              /*  if(accountList[i].list[j].debitAmount == null || accountList[i].list[j].debitAmount == 0){
                    accountList[i].list[j].debitAmount = '';
                  }
                if(accountList[i].list[j].creditAmount == null || accountList[i].list[j].creditAmount == 0){
                    accountList[i].list[j].creditAmount = '';
                }*/
              jf = accountList[i].list[j].debitAmount;
              df = accountList[i].list[j].creditAmount;
              var ye = accountList[i].list[j].blanceAmount;
              if (jf) {
                jf = jf.toFixed(2);
              } else {
                jf = '';
              }
              if (df) {
                df = df.toFixed(2);
              } else {
                df = '';
              }
              if (ye) {
                ye = ye.toFixed(2);
              } else {
                ye = '';
              }
              str += '<tr class="cgColor">' +
                '<td>' + rq + '</td>' +
                '<td style="text-align: left;">' + pzzh + '</td>' +
                '<td style="text-align: left;">' + zy + '</td>' +
                '<td style="text-align: right;">' + jf + '</td>' +
                '<td style="text-align: right;">' + df + '</td>' +
                '<td>' + accountList[i].list[j].direction + '</td>' +
                '<td style="text-align: right;">' + ye + '</td>' +
                '</tr>'
            }
          }
        } else {
          str = '<tr class="tc"><td colspan="7" style="height: 80px;">暂无数据</td></tr>';
        }
        $('#detailList1').html(str);
      } else {
        layer.msg(res.msg || '明细表请求错误');
      }
      //分页
      flushPage('layui-table-page2', params.currPage, res.cout, res.size, zTreeOnClick);
    })
  } else {
    treeNodeName = treeNode.name;
  }
};
//根据文本框的关键词输入情况自动匹配树内节点 进行模糊查找
//加载树节点的方法
var departName = null; //查询条件
function searchTreeNode() {
  departName = $("#keyword").val();
  if (departName == null || departName == '') {
    return;
  }
  var zTree_Menu = $.fn.zTree.getZTreeObj('subjectTree');
  var selectedNodes = zTree_Menu.getSelectedNodes();
  var node = zTree_Menu.getNodesByFilter(filter, true); //第二个参数表示仅查找一个节点
  if (node == null) {
    layer.msg('没有查询到相关信息');
    return;
  }
  zTree_Menu.selectNode(node, false, false); //指定选中ID的节点 ；参数：节点对象，表示是否单独选中（false只会选中一个，之前的会被取消），是否会让节点自动滚到到可视区域内
  zTree_Menu.expandNode(node, true, false); //指定选中ID节点展开
  zTreeOnClick(zTree_Menu, selectedNodes, node); //点击查询调用树形节点点击事件
}
//树节点，查询过滤器
function filter(node) {
  return (node.name.indexOf(departName) > -1);
}

$(document).ready(function() {
  $(document).keydown(function(e) {
    e = e || window.event;
    var keycode = e.which ? e.which : e.keyCode;
    if (keycode == 38) {
      if (jQuery.trim($("#append").html()) == "") {
        return;
      }
      movePrev();
    } else if (keycode == 40) {
      if (jQuery.trim($("#append").html()) == "") {
        return;
      }
      $("#keyword").blur();
      if ($(".item").hasClass("addbg")) {
        moveNext();
      } else {
        $(".item").removeClass('addbg').eq(0).addClass('addbg');
      }
    } else if (keycode == 13) {
      dojob();
    }
  });
  var movePrev = function() {
    $("#keyword").blur();
    var index = $(".addbg").prevAll().length;
    if (index == 0) {
      $(".item").removeClass('addbg').eq($(".item").length - 1).addClass('addbg');
    } else {
      $(".item").removeClass('addbg').eq(index - 1).addClass('addbg');
    }
  }
  var moveNext = function() {
    var index = $(".addbg").prevAll().length;
    if (index == $(".item").length - 1) {
      $(".item").removeClass('addbg').eq(0).addClass('addbg');
    } else {
      $(".item").removeClass('addbg').eq(index + 1).addClass('addbg');
    }
  }
  var dojob = function() {
    $("#keyword").blur();
    var value = $(".addbg").text();
    $("#keyword").val(value);
    $("#append").hide().html("");
  }
});

function getContent(obj) {
  $(obj).css({ "border": "1px solid #a6a2a2" });
  var keyword = jQuery.trim($(obj).val());
  if (keyword == "") {
    $("#append").hide().html("");
    return false;
  }
  var num1 = parseInt(keyword);
  var bool1 = false;
  if (!isNaN(num1)) {
    bool1 = true;
  }
  var myReg = /^[\u4e00-\u9fa5]+$/;
  var bool2 = myReg.test(keyword);
  var maths;
  //输入数字进行从头开始匹配
  if (bool1) {
    maths = "^" + keyword;
  }
  //输入中文进行模糊匹配
  if (bool2) {
    maths = keyword;
  }
  if (bool1 == false && bool2 == false) {
    //没有匹配到的数据
    $("#append").show().html('<li style="text-align:center">暂无数据</li>');
    return false;
  }
  var html = "";
  //循环遍历存储匹配成功的数据
  for (var i = 0; i < treeText.length; i++) {
    var text = treeText[i].match(maths);
    if (text) {
      html += "<li class='item' onmouseenter='getFocus(this)' onClick='getCon(this);'>" + treeText[i] + "</li>"
    }
  }
  //将匹配到的数据添加到HTML中
  if (html != "") {
    $("#append").show().html(html);
  } else {
    //没有匹配到的数据
    $("#append").show().html('<li style="text-align:center">暂无数据</li>');
  }
}

function getFocus(obj) {
  $(".item").removeClass("addbg");
  $(obj).addClass("addbg");
}
//点击匹配到的数据填充到input框
function getCon(obj) {
  var value = $(obj).text();
  $("#keyword").val(value);
  $("#append").hide().html("");
  $("#keyword").css({ "border": "1px solid #a6a2a2" });
}
