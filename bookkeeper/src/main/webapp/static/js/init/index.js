/*
 * @Author: Rong
 * @Date: 2018-04-19 13:41:35
 * @Last Modified by: Rong
 * @Last Modified time: 2018-04-19 13:45:10
 */

;
(function () {
  var layer = layui.layer,
    noLeadNum = $('.noLead'),
    $trialPopup = $('#trial-balance'),
    $trialDetail = $('#TrDetail'),
    $initPopup = $('#init-popup');

  $(function () {
    var $upload1 = $('#init-upload1'),
      $upload2 = $('#init-upload2'),
      $upload3 = $('#init-upload3'),
      $contrastBtn = $('#contrast-btn'),
      $mappingBtn = $('#mapping-btn'),
      $trialBtn = $('#trial-btn'),
      $initBtn = $('#init-btn'),
      $skipBtn = $('#skip-btn'),
      $resetBtn = $('#reset-btn'),

      loca = window.location.search.substr(1),
      code = loca.split('=')[1];
    // 如果状态 为 1， 说明有账套，显示主页，没有则跳进初始化
    if (code === '1') {
      document.title = '账套首页';
      $('#setHome').show();
      $('#setSubject').hide();
      chartLeft();
      chartRight();
      // 自适应
      window.addEventListener('resize', _debounce(300, function () {
        chartLeft();
        chartRight();
      }));
    } else {
      $('#setSubject').show();
      $('#setHome').hide();
      onload();
    }


    // 文件输入框
    $(':file').on('change', function () {
      var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
      $(this).prev().text(v);
      $(this).parent().next().removeClass('loading loaded');
    });

    // 科目Excel
    $upload1.on('click', function () {
      var that = this,
        url = window.baseURL + '/subexcel/uploadSubExcel';
	  window.sessionStorage.setItem("codeList", '');//清除科目缓存
      uploadInit(that, url, subCallBack);
    });

    // 固定资产Excel
    $upload2.on('click', function () {
      var that = this,
        url = window.baseURL + '/assets/uploadAssets';
      postRequest(window.baseURL + '/assets/queryAss', {}, function (res) {
        if (res.code == '2') {
          layer.confirm('提示<br/><span class="red">' + res.msg + '<span>' || '提示<br/><span class="red">是否覆盖，重新上传？<span>', {
            icon: 3,
            title: '提示'
          }, function () {
            uploadInit(that, url, assetsCallBack);
          })
          layer.closeAll('loading');
        } else if (res.code == '1') {
          layer.msg(res.msg, {
            icon: 2,
            shadeClose: true,
            time: 2000000
          });
        } else if (res.code == '0') {
          uploadInit(that, url, assetsCallBack);
        }
      })
    });

    // 数量金额Excel
    $upload3.on('click', function () {
      var that = this,
        url = window.baseURL + '/stock/uploadStock';
      getRequest(window.baseURL + '/stock/stockImport', function (res) {
        if (res.code === "1") {
          layer.confirm(res.msg, {
            icon: 0,
            title: '提示'
          }, function (index) {
            uploadInit(that, url, amountCallBack);
          });
        } else {
          uploadInit(that, url, amountCallBack);
        }
      });
    });

    // 打开科目对照弹窗
    $contrastBtn.on('click', function () {
      var width = ($(window).width() > 900) ? '80%' : '90%';
      layer.open({
        type: 2,
        title: '科目对照',
        area: [width, '80%'],
        maxmin: true,
        resize: false,
        content: window.baseURL + '/subinit/subContrastView',
        cancel: function () {
          getTrialResult(); // 查询已经匹配到的科目数量
        }
      });
    });

    // 打开科目映射弹窗
    $mappingBtn.on('click', function () {
      var width = ($(window).width() > 900) ? '80%' : '90%';
      var iframeWin;
      layer.open({
        type: 2,
        title: '标准科目映射',
        area: [width, '80%'],
        maxmin: true,
        content: window.baseURL + '/subinit/subMappingView',
        btn: ['保存', '自动映射', '取消'],
        success: function (layero) {
          layer.msg('正在请求数据……', {
            icon: 0,
            shade: 0.1,
            time: -1
          });
          iframeWin = window.frames[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象
        },
        yes: function () {
          iframeWin._submit();
        },
        btn2: function () {
          iframeWin._mapping();
          return false;
        },
        btn3: function () {
          // 解绑点击事件
          top != window && top.$('html').off('click');
          $('html').off('click');
        },
        cancel: function () {
          // 解绑点击事件
          top != window && top.$('html').off('click');
          $('html').off('click');
        }
      });
    });

    // 试算平衡弹窗
    $trialBtn.on('click', getTrialBalance);

    // 初始化
    $initBtn.on('click', openInitPopup);

    // 跳过初始化
    $skipBtn.on('click', skipInit);
  });

  /**
   * 账套初始化
   */
  // 页面初始化执行函数
  function onload() {
    layui.use('layer', function () {
      layui.layer.ready(function () {
        layer.msg('新公司不需要导入数据，<br>请点击跳过初始化', {
          time: 2000
        });
      });
    });
    // fixedHead($('.fixed-head'), $('.account_info'), '', $('#navDOM1'));
    // $("#sidebar a").attr('href', 'javascript:;'); // 测试用
    // getTrialResult(); // 查询已经匹配到的科目数量
    getRequest(window.baseURL + '/subinit/openInitPage', function (res) {
      if (res.code == 1) {
        var $span = $('#matchRes').find('span');
        $span.eq(0).text(res.initSubNumber);
        $span.eq(1).text(res.matchingSubNumber);
        $span.eq(2).text(res.inittotalDebit);
        $span.eq(3).text(res.inittotalCredit);
        $('#contrast-btn,#mapping-btn,#trial-btn').removeClass('dn');
        $('#matchRes').show();
        // 判断未匹配科目是否有余额
        if (res.hasBalance == '2') {
          // 打开科目对照弹窗
          $('#contrast-btn').click();
        }
      } else {
        $('#matchRes').hide();
      }
    });
  }

  // 查询已经匹配到的科目数量
  function getTrialResult() {
    getRequest(window.baseURL + '/subinit/excelInit', function (res) {
      if (res.code === 1) {
        var $span = $('#matchRes').find('span');

        $span.eq(0).text(res.initSubNumber);
        $span.eq(1).text(res.matchingSubNumber);
        $span.eq(2).text(res.inittotalDebit);
        $span.eq(3).text(res.inittotalCredit);
        $('#contrast-btn').removeClass('dn');
        $('#mapping-btn').removeClass('dn');
        $('#trial-btn').removeClass('dn');
        $('#matchRes').show();
      } else {
        $('#matchRes').hide();
      }
    });
  }

  /**
   * 导入Excel文件
   */
  // 科目回调
  function subCallBack(result) {
    var msg = result.msg || '';
    if (result.code == 1) {
      layer.msg('科目上传成功，' + msg + ',<br>正在科目匹配', {
        icon: 16,
        shade: 0.03,
        time: -1
      });
      // 科目匹配
      postRequest(window.baseURL + '/subinit/subContrast', {}, function (res) {
        // code:1(全部匹配)    0(部分匹配)
        getTrialResult(); // 查询已经匹配到的科目数量
        if (res.code == 1) {
          layer.msg(res.msg || '科目全部匹配', {
            icon: 1,
            shade: 0.1,
            time: 2000
          });
        } else if (res.code == 0) {
          layer.msg(res.msg || '科目部分匹配', {
            icon: 1,
            shade: 0.1,
            time: 2000
          }, function () {
            // 判断未匹配科目是否有余额
            if (res.hasBalance == '2') {
              // 打开科目对照弹窗
              $('#contrast-btn').click();
            }
          });
        } else {
          layer.msg(res.msg || '科目匹配异常', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000000
          });
        }
      });
      layer.closeAll('loading');
    } else {
      layer.msg(msg, {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
    }
  }

  // 固定资产回调
  function assetsCallBack(result) {
    if (result.code == '0') {
      layer.msg(result.msg, {
        icon: 1,
        shade: 0.1,
        time: 2000
      });
    } else if (result.code == '1') {
      layer.msg(result.msg, {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
    }
  }

  // 数量金额初始化（总账）回调
  function amountCallBack(result) {
    if (result.message == 'success') {
      layer.msg(result.result, {
        icon: 1,
        shade: 0.1,
        time: 2000
      });
    } else {
      layer.msg(result.result, {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
    }
  }

  // 试算平衡
  function getTrialBalance() {
    getRequest(window.baseURL + '/subinit/balance', function (res) {
      layer.closeAll('loading');
      if (res.code == 1) {
        showTrialBalance(res);
      } else {
        layer.msg(res.message);
      }
    });

    // 试算平衡弹窗显示
    function showTrialBalance(res) {
      // 数据填充
      var str = '';
      str = '<tr>' +
        '<td>系统科目</td>' +
        '<td>' + res.initDebitBalance + '</td>' +
        '<td>' + res.initCreditBalance + '</td>' +
        '<td>' + res.beginningCapital + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>导入科目</td>' +
        '<td>' + res.endingBalanceDebit + '</td>' +
        '<td>' + res.endingBalanceCredit + '</td>' +
        '<td>' + res.finalBalance + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>差异</td>' +
        '<td>' + res.debitVariance + '</td>' +
        '<td>' + res.CreditVariance + '</td>' +
        '<td>' + res.difference + '</td>' +
        '</tr>';
      $trialPopup.find('tbody').html(str);
      layer.open({
        type: 1,
        title: '试算平衡',
        area: ['600px', 'auto'],
        content: $trialPopup,
        btn: ['查看差异', '确定'],
        yes: function () {
          // 获取科目对照数据
          getRequest(window.baseURL + '/subinit/toKmdz', function (res) {
            if (res.code == 1) {
              showTrialDetail(res);
            } else {
              layer.msg('科目对照异常');
            }
          });
        },
        btn2: function () {
          $trialPopup.hide();
        },
        cancel: function () {
          $trialPopup.hide();
        }
      });
    }

    // 试算平衡差异（未匹配科目）弹窗显示
    function showTrialDetail(res) {
      var str = '',
        sSpan = '',
        sysList = res.sysList || [],
        excelList = res.excelList || [],
        matchSize = res.matchListSize || 0;

      if (sysList.length === 0) {
        console.error('微企宝科目为空');
      } else if (excelList.length === 0) {
        layer.alert('科目已匹配');
        return;
      }
      $.each(excelList, function (idx, data) {
        if (data.subCode && data.isMatching == 0) {
          data.updateDate = formatDate(data.updateDate);
          str += '<tr data-id="' + data.pkSubExcelId + '">' +
            '<td>' + (++idx) + '</td>' +
            '<td>' + data.subCode + '</td>' +
            '<td>' + data.subName + '</td>' +
            '<td>' + data.endingBalanceDebit + '</td>' //期末余额(借方)',
            +
            '<td>' + data.endingBalanceCredit + '</td>' //期末余额(贷方)',
            +
            '</tr>';
        }
      });
      $trialDetail.find('tbody').html(str);
      layer.open({
        type: 1,
        title: '查看详情',
        maxmin: true,
        area: ['80%', '90%'],
        content: $trialDetail,
        btn: ['确定'],
        yes: function (i) {
          layer.close(i);
          $trialDetail.hide();
        },
        cancel: function () {
          $trialDetail.hide();
        }
      });
    }
  }


  /*
   * 初始化操作
   */
  // 更改初始化状态
  function completionStatus() {
    postRequest(window.baseURL + '/account/chgAccInitialStates', { initialStates: 1 }, function (res) {
      if (res.code == 1) {
        layer.msg(res.msg || '更改初始化状态成功', {
          icon: 1,
          shade: 0.1,
          time: 1000
        }, function () {
          window.sessionStorage.cache = "false";
          top.location.reload();
        });
      } else {
        layer.msg(res.msg || '更改初始化状态异常');
      }
    });
  }

  // 打开初始化弹窗
  function openInitPopup() {
    var message = '您正在执行账套初始化，请确保已经导入所有数据，否则账套可能作废。<br>提示：新公司不需要导入数据。<br>是否继续操作？';
    layer.confirm(message, {
      icon: 0,
      title: '账套初始化'
    }, function () {
      checkInit();
      layer.msg('初始化检查中', {
        icon: 16,
        shade: 0.3,
        time: -1
      });
    });

    // 初始化检查
    function checkInit() {
      getRequest(window.baseURL + '/subinit/balance', function (res) {
        if (res.code == 1) {
          if (res.beginningCapital == 0 && res.finalBalance == 0 && res.debitVariance == 0 && res.CreditVariance == 0 && res.difference == 0) {
            // console.log('试算平衡');
            completionStatus();
          } else {
            layer.msg('试算不平衡');
          }
        } else {
          layer.msg(res.message);
        }
      });
    }
  }

  // 跳过账套初始化
  function skipInit() {
    var message = '您正在跳过账套初始化，只有新公司不需要导入数据，否则账套可能作废。<br>是否继续操作？';
    layer.confirm(message, {
      icon: 0,
      title: '提示'
    }, completionStatus);
  }

  /**
   * 首页
   */
  // 左边的数据表图
  function chartLeft() {
    getRequest(window.baseURL + '/profit/profitTrend', function (res) {
      var obj = {
        date: [],
        income: [],
        costOf: [],
        cost: [],
        profits: []
      }
      if (res.code === '0') {
        var data = res.msg;

        var isNull = JSON.stringify(data);
        if (isNull !== "{}") {
          for (var item in data) {
            if (data.hasOwnProperty(item)) {
              obj.date.push(data[item].period); // 日期
              obj.income.push(data[item].lr.toFixed(2)); // 利润
              obj.costOf.push(data[item].cb.toFixed(2)); // 成本
              obj.cost.push(data[item].fy.toFixed(2)); // 费用
              obj.profits.push(data[item].sr.toFixed(2)); // 收入
            }
          }
          homeChart(obj);
        } else {
          $('#home-isNull').show();
        }
      }
    })
  }

  function homeChart(obj) {
    var chart = document.getElementById('homeChart-left'); // 基于准备好的dom，初始化echarts实例
    chart.style.width = $('.home-left').width() + 'px'; // 获取宽度
    var myChart = echarts.init(chart); // 初始化
    myChart.showLoading(); // 动画开始
    myChart.resize(); // 自适应

    // 指定图表的配置项和数据
    var option = {
      'title': {
        'text': '单位：元',
        'left': '8%',
        'textStyle': {
          'color': '#333',
          'fontSize': '14'
        }
      },
      'tooltip': {
        'show': true,
        'trigger': 'axis',
        'axisPointer': {
          'type': 'cross',
          'label': {
            'backgroundColor': '#6a7985'
          }
        }
      },
      'legend': {
        'data': ['收入', '成本', '费用', '利润'],
        'right': '6%'
      },
      'grid': {
        'left': '3%',
        'right': '4%',
        'bottom': '3%',
        'containLabel': true
      },
      'xAxis': [
        {
          'type': 'category',
          'boundaryGap': false,
          'data': obj.date
        }
      ],
      'yAxis': [
        {
          'type': 'value'
        }
      ],
      'series': [

        {
          'name': '利润',
          'type': 'line',
          'stack': '总量',
          'areaStyle': {
            'color': '#58cb03', // 颜色
            'opacity': .15
          },
          'lineStyle': {
            'normal': {
              'color': "#90c44f" //连线颜色
            }
          },
          'itemStyle': {
            'normal': {
              'color': '#90c44f', // 原点的颜色
            }
          },
          'label': { // 在折线图上显示数字
            'normal': {
              'show': true,
              'position': 'top'
            }
          },
          'data': obj.income
        },
        {
          'name': '费用',
          'type': 'line',
          'stack': '总量',
          'areaStyle': {
            'color': '#ee9700',
            'opacity': .15
          },
          'lineStyle': {
            'normal': {
              'color': "#ecc167" // 连线颜色
            }
          },
          'itemStyle': {
            'normal': {
              'color': '#ecc167', // 原点的颜色
            }
          },
          'data': obj.cost
        },
        {
          'name': '成本',
          'type': 'line',
          'stack': '总量',
          'areaStyle': {
            'color': '#e82b2b',
            'opacity': .15
          },
          'lineStyle': {
            'normal': {
              'color': "#ea5a56" //连线颜色
            }
          },
          'itemStyle': {
            'normal': {
              'color': '#ea5a56', // 原点的颜色
            }
          },
          'data': obj.costOf
        },
        {
          'name': '收入',
          'type': 'line',
          'stack': '总量',
          'areaStyle': {
            'color': '#2a9af0',
            'opacity': .15
          },
          'lineStyle': {
            'normal': {
              'color': "#b8ddff" // 连线颜色
            }
          },
          'itemStyle': {
            'normal': {
              'color': '#b8ddff', // 圆点的颜色
            }
          },
          'data': obj.profits
        }
      ]
    };
    myChart.setOption(option); // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading(); // 动画结束
  }

  // 右边的数据原图
  function chartRight() {
    getRequest(window.baseURL + '/profit/profitAnalyze', function (res) {
      if (res.code === '0') {
        var data = res.msg;
        $('#chartTitle').text(session.busDate + '利润表结构分析');

        // 判断是否有数据
        if (JSON.stringify(data) === "{}") {
          $('#home-isData').show();
        } else {
          data.profits = data.lr.toFixed(2) || 0.00; // 利润
          data.cost = data.fy.toFixed(2) || 0.00; // 费用
          data.costOf = data.cb.toFixed(2) || 0.00; // 成本
          data.amount = (((+data.profits) + (+data.cost) + (+data.costOf)).toFixed(2)); // 总额
          homeChartRight(data);
          var str = '<table>' +
            '<tr>' +
            '<th class="home-money-all">总额</th>' +
            '<th class="home-money-num">' + data.amount + '</th>' +
            '</tr>' +
            '<tr>' +
            '<th class="home-money-into">成本</th>' +
            '<th class="home-money-num">' + data.costOf + '</th>' +
            '</tr>' +
            '<tr>' +
            '<th class="home-money-cost">费用</th>' +
            '<th class="home-money-num">' + data.cost + '</th>' +
            '</tr>' +
            '<tr>' +
            '<th class="home-money-profits">利润</th>' +
            '<th class="home-money-num">' + data.profits + '</th>' +
            '</tr>' +
            '</table>'
          $("#home-money").html(str);
        }
      }
    });
  }

  function homeChartRight(data) {
    var chart = document.getElementById('homeChart-right'); // 基于准备好的dom，初始化echarts实例
    chart.style.width = $('.home-right').width() - 20 + 'px';
    var myChart = echarts.init(chart);
    myChart.showLoading(); // 动画开始
    myChart.resize();

    var option = {
      tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
      },
      series: [
        {
          name: data.period + '利润表结构分析',
          type: 'pie',
          radius: ['80%', '40%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          hoverAnimation: false,
          label: {
            normal: {
              show: true,
              position: 'outside'
            },
          },
          labelLine: {
            normal: {
              show: true,
              length: 90,
              length2: 30
            }
          },
          data: [
            { value: data.cost, name: '费用', itemStyle: { color: '#ffb432', fontSize: 18 } },
            { value: data.costOf, name: '成本', itemStyle: { color: '#81ca4b', fontSize: 18 } },
            { value: data.profits, name: '利润', itemStyle: { color: '#ee6f6a', fontSize: 18 } },
          ]
        },
        // + data.cost+ data.costOf+ data.profits
        {
          name: '透明度圆圈',
          type: 'pie',
          radius: ['50%', '0%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          hoverAnimation: false, // 关闭 hover 在扇区上的放大动画效果。
          cursor: 'default', // 鼠标悬浮时在图形元素上时鼠标的样式是什么。同 CSS 的 cursor。
          silent: true, // 图形是否不响应和触发鼠标事件，默认为 false，即响应和触发鼠标事件。
          data: [
            {
              value: 0,
              itemStyle: {
                color: "#000",
                opacity: .1
              }
            }
          ]
        },
        {
          name: '总额',
          type: 'pie',
          radius: ['40%', '0%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          hoverAnimation: false, // 关闭 hover 在扇区上的放大动画效果。
          cursor: 'default', // 鼠标悬浮时在图形元素上时鼠标的样式是什么。同 CSS 的 cursor。
          silent: true, // 图形是否不响应和触发鼠标事件，默认为 false，即响应和触发鼠标事件。
          label: {
            normal: {
              show: true,
              color: '#54a4ff',
              fontSize: 16,
              position: 'center',
              formatter: function () {
                return ' 总额  \n' + (data.amount / 10000).toFixed(2) + '万';
              }
            }
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: [
            {
              value: 335,
              itemStyle: {
                color: "#fff"
              }
            }
          ]
        }
      ]
    };
    myChart.setOption(option); // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading(); // 动画结束
  }

  // 函数节流
  function _debounce(del, fun) {
    var timer;
    _this = this;
    return function () {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(function () {
        fun.apply(_this, arguments);
        timer = null;
      }, del);
    }
  }
}());
