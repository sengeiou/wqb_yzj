var userType = session.userType, //获取用户身份标识
  chcek_id = ''; //选中要分配的员工 Id

//使用vue 数据绑定
var vm = new Vue({
  el: '#mainApp',
  data: function () {
    return {
      isShowAdmin: false,
      userType: userType,
      curPage: 1, // 默认当前页
      maxSize: 10, // 默认当前一页条数
      keyWrold: '', //查询关键字
      userCountData: {}, // 头部客户数统计
      userListData: [], // 做账客户列表
      distrUserListData: [], // 分配人员下拉列表
      dataLength: 0, // 处理防止 分页重新渲染
      choseTrIndex: 0, // 默认列表下标
      isNoOnSearch: true, // 未点击查询时 默认选中 列表第一条样式
      isCheckAll: false, // 是否全选
      checkListDataParmaStr: '', // 选中参数
      curuserName: '', // 当前姓名
      curMonth: '', // 当前月份
      manaBaseURL: '', // 后台管理系统请求路径
      currAccountID: '', // 当前账套ID
    }
  },

  mounted: function () {
    this.currAccountID = session.accountID;
    //初始化 部分数据
    this.curMonth = new Date().getMonth() + 1;
    this.curuserName = session.userName;

    if (window.manaBaseURL) {
      this.manaBaseURL = window.manaBaseURL;
    } else {
      var hostname = location.hostname;
      if (hostname.indexOf('acct.wqbol.') === 0) {
        this.manaBaseURL = location.protocol + '//' + hostname.replace('acct', 'acctmana') + '/';
      } else {
        this.manaBaseURL = location.protocol + '//' + hostname + ':18080'; // manaPort本地后台系统端口
      }
    }
    //权限控制  页面显示
    if (this.userType == '2') {
      this.isShowAdmin = true;
      this._getDistriUser(); //获取分配的 人员列表
    }
    //函数调用
    this._getUserListData(this.curPage); //为了与公共一样
    this._getUserCount();
    this._queryAllSubToPage();
    //select分配 id事件绑定
    layui.form.on('select(chcek_id)', function (data) {
      chcek_id = data.value;
    });
  },
  methods: {
    // 查询账套列表
    _getUserListData: function (page, isSearch) {
      var that = this;
      var parma = {
        curPage: page || 1, // 当前页
        maxSize: that.maxSize, // 每页几条
        keyWord: that.keyWrold
      };
      postRequest(window.baseURL + '/toSystem/getAccList', parma, function (res) {
        if ('true' === res.success) {
          that.checkListDataParmaStr = [];
        }
        var arr = res.returnList || [];
        arr.forEach(function (element) {
          element.isCheck = false;
        });
        that.userListData = arr;
        that.curPage = page;
        // if (that.dataLength * 1 <= 0 || isSearch === true) {
        // 分页
        flushPage($('#layui-table-page2'), that.curPage, res.count, that.maxSize, that._getUserListData);
        // }
        //处理搜索结果
        // if (res.message == '暂无账套!') {
        //   that.dataLength = 0;
        //   return false
        // } else {
        //   that.dataLength = res.returnList.length || 0;
        // }
      })
    },
    // 客户统计数据
    _getUserCount: function () {
      var that = this;
      getRequest(window.baseURL + '/userFirstPage/getFirstPageData', function (res) {
        that.userCountData = res;
        if (that.userType == '2' && that.userCountData.list.length >= 2) {
          $('.main-left .tableTitle').css('padding-right', '17px');
        }
      })
    },
    // 查询员工列表
    _getDistriUser: function () {
      var that = this;
      postRequest(that.manaBaseURL + '/user/queryUserByParentID', {
        userID: session.userID
      }, function (res) {
        if ('true' === res.success) {
          that.distrUserListData = res.list; // 分配人员下拉列表
        } else {
          layer.msg(res.msg || '查询员工列表异常');
        }
      });
    },
    // 点击查询账套列表
    searchUserList: function () {
      var that = this;
      that.isNoOnSearch = false;
      var val = that.keyWrold.trim();
      that.curPage = 1;
      if (!val) {
        // layer.msg('请输入关键字');
        // return false;
        that._getUserListData(that.curPage);
      } else {
        that._getUserListData(that.curPage, true);
      }
    },
    // 点击记账 选中切换账套
    choseTrItem: function (index, id) {
      // 当前账套ID == 列表账套ID
      if (this.currAccountID == id) {
        return false;
      }
      this.choseTrIndex = index;
      this.isNoOnSearch = true;
      // 切换账套
      postRequest(window.baseURL + '/account/chgAccount', {
        accountID: id
      }, function (res) {
        if (res.message == 'success') {
          layer.msg('切换账套成功');
          top.$('#top_tabs > li').length > 1 && top.$('#top_tabs_box .closePageAll').click(); // 关闭全部页签
          top.location.reload();
        } else {
          layer.msg('切换账套出错');
        }
      });
    },
    //单选 参数形成
    checkTemplateItem: function (arrItem) {
      var that = this;
      arrItem.isCheck = !arrItem.isCheck;
      // var isTrueFn = value => value.isCheck == true;
      var isTrueFn = function (value) {
        return value.isCheck == true;
      };
      var checkArr = [];
      checkArr = that.userListData.filter(isTrueFn);
      if (checkArr.length > 0) {
        var checkStrArr = [];
        checkArr.forEach(function (element) {
          checkStrArr.push(element.accountID);
        });
        that.checkListDataParmaStr = checkStrArr.join(",");
        //console.log('选择参数字符串',that.checkListDataParmaStr);
        //是否全选  改变全选状态
        for (var i = 0; i < that.userListData.length; i++) {
          //自要有一个不全选
          if (!that.userListData[i].isCheck) {
            that.isCheckAll = false;
            // //console.log('没全选！！',that.isCheckAll)
            return false;
          } else {
            that.isCheckAll = true;
          }
        }
      } else {
        that.checkListDataParmaStr = '';
      }
    },
    //当前页全选
    checkPageAll: function () {
      var that = this;
      that.isCheckAll = !that.isCheckAll;
      if (that.isCheckAll) {
        var checkAllArr = [];
        that.userListData.forEach(
          function (ele) {
            ele.isCheck = true;
            checkAllArr.push(ele.accountID)
          });
        that.checkListDataParmaStr = checkAllArr.join(",");
      } else {
        that.checkListDataParmaStr = '',
          that.userListData.forEach(function (ele) {
            ele.isCheck = false;
          })
      }
    },
    // 点击分配
    distribution: function () {
      var that = this;
      layui.form.render(); //渲染 下拉
      if (that.checkListDataParmaStr.length > 0) {
        if (that.distrUserListData.length) {
          layer.open({
            type: 1,
            title: '分配',
            anim: 1,
            area: ['420px', '480px'], //宽高
            content: $('#distribPopup'),
            btn: ['确定', '取消'],
            yes: function (index) {
              if (!chcek_id) {
                layer.msg('请选中要分配的人员', {
                  icon: 0,
                  time: 2000
                });
                return;
              }
              var parma = {
                userID: chcek_id,
                accountID: that.checkListDataParmaStr
              };
              postRequest(that.manaBaseURL + '/custom/chgAccBlEmp', parma, function (res) {
                if (res.success == "true") {
                  layer.msg('分配成功！', {
                    icon: 1,
                    time: 1500
                  }, function () {
                    layer.close(index);
                    $('#distribPopup').hide();
                  });
                  //刷新列表 及 关闭弹窗    重置全选状态
                  that.isCheckAll = false;
                  // 如果是当前账套分配给别人,需要先执行切换账套同步当前账套信息
                  if (that.checkListDataParmaStr.indexOf(that.currAccountID) > -1) {
                    postRequest(window.baseURL + '/account/chgAccount', {
                      accountID: that.currAccountID
                    }, function (res) {
                      that._getUserListData(that.curPage);
                      if (res.message != 'success') {
                        console.log('切换账套出错');
                      }
                    });
                  } else {
                    that._getUserListData(that.curPage);
                  }
                } else {
                  layer.msg(res.message || '分配异常！');
                }
              });
            },
            btn2: function () {
              $('#distribPopup').hide();
            },
            cancel: function () {
              $('#distribPopup').hide();
            }
          });
        } else {
          layer.confirm('您当前无可用员工，是否前往添加员工？', function () {
            top.$('#YGPZ').click();
          });
        }
      } else {
        top.layer.msg('请先选择客户后，再操作分配');
      }
    },
    _queryAllSubToPage: function () {
      postRequest(window.baseURL + '/vat/queryAllSubToPage', { keyWord: '' }, function (res) {
        top.subListData = res.data || []; // 末级科目列表
        top.subListRefresh = false; // 末级科目列表是否需要刷新
      });
    }
  }
});

//记账图表
if (userType == 3 || userType == 6) {
  chartLeft();
}
//公司业务趋势数据获取
function chartLeft() {
  getRequest(window.baseURL + '/profit/profitTrend', function (res) {
    var obj = {
      date: [],
      income: [],
      costOf: [],
      cost: [],
      profits: []
    };
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
        _lineChartData = obj;
        lineChart(_lineChartData);
      } else {
        $('#home-isNull').show();
      }
    } else {
      var str = '';
      str += '<div class="TemporarilyNoData">' +
        '<span>暂无数据</span>' +
        '</div>';
      $('#businessLine').html(str);
    }
  })
}
//公司业务趋势图
function lineChart(obj) {
  var chart = document.getElementById('businessLine'); // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(chart); // 初始化
  myChart.showLoading(); // 动画开始
  myChart.resize();
  // 指定图表的配置项和数据
  var option = {
    'title': {
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
      'right': '6%',
      'top': '4%'
    },
    'grid': {
      'left': '3%',
      'right': '4%',
      'bottom': '3%',
      'containLabel': true
    },
    'xAxis': [{
      'type': 'category',
      'boundaryGap': false,
      'data': obj.date
    }],
    'yAxis': [{
      'type': 'value'
    }],
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
