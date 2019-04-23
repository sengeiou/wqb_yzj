/*
 * @Author: Rong
 * @Date: 2018-04-19 13:54:08
 * @Last Modified by:   Rong (报表)
 * @Last Modified time: 2018-04-19 13:54:08
 */


;
(function () {
  var url = location.hash;
  if (url.substr(url.length - 1, 1) == 1) {
    $("#navDOM1 li:eq(0)").click();
  } else if (url.substr(url.length - 1, 1) == 2) {
    $("#navDOM1 li:eq(1)").click();

  } else if (url.substr(url.length - 1, 1) == 3) {
    $("#navDOM1 li:eq(2)").click();
  }
  var content = $("#report").width();
  $("#navDOM1").css({ 'width': content, 'left': '270px' });
  $("#search1").css({ 'width': content, 'left': '270px' });
  $("#navDOM1 li").click(function () {
    var content = $("#content").width();
    $("#navDOM1").css({ 'width': content, 'left': '270px' });
    $("#search1").css({ 'width': content, 'left': '270px' });
  });
  var contentH = $("#report").height();
  $("#assetsDataT").css({ "max-height": contentH - 147 });
  $("#profitDataT").css({ "max-height": contentH - 147 });
  var state = {};
  isSettleAccounts = _queryStatus()[3]; // 是否结账
  // 显示权限按钮
  ! function () {
    var show = getPurviewData(800);
    if (show === 'block') {
      $('.fixed-search').css('display', show);
      $('#report').css('padding-top', '38px');
    }
  }();

  $(function () {
    var loca = window.location.search.substr(1),
      num = loca.split('=')[1],
      $printBtns = $('.print_btn'),
      $exportBtns = $('.export-btn');

    // 判断首页跳转切换页面
    showTable(num);
    // num > 1 ? showTable(num) : _setAssestList();// 资产负债表数据


    // 监听tab切换
    layui.element.on('tab(docDemoTabBrief)', function (data) {
      var index = data.index;
      _initHead(index);
    });

    // 打印
    $printBtns.on('click', function () {
      if (1 !== isSettleAccounts) {
        layer.msg('当前数据不完整，请在结账后操作', {
          icon: 0,
          shade: 0.3
        });
        return;
      }
      var that = this,
        iframeWin,
        $iframeDoc;

      that.index = $(this).attr('data-index');
      switch (that.index) {
      case '0':
        that.title = '资产负债表';
        that.params = state.balancePrint;
        break;
      case '1':
        that.title = '利润表';
        that.params = state.profitPrint;
        break;
      case '2':
        that.title = '现金流量表';
        layert.alert('未完成');
        return false;
        break;
      }
      layer.confirm('是否要打印' + that.title + '？', {
        icon: 0,
        title: '提示'
      }, function (index) {
        layer.open({
          type: 2,
          title: that.title + '打印',
          area: ['820px', '80%'],
          maxmin: true,
          content: window.baseURL + '/print/print',
          btn: ['打印', '取消'],
          success: function (layero) {
            // layer.closeAll('dialog');
            var cssURL = window.baseURL + '/css/print-report.css',
              // jsURL = window.baseURL + '/js/report/print-report.js',
              linkTag = $('<link href="' + cssURL + '" type="text/css" rel="stylesheet">'),
              compName = top.$('#choose-account .selectCus').text().trim(),
              date = window.session.busDate.split('-'),
              day = new Date(date[0], date[1], 0).getDate(),
              option = {
                'type': 'report',
                'title': that.title,
                'compName': compName,
                'date': date[0] + '年' + date[1] + '月' + day + '日',
                'params': that.params
              };

            iframeWin = window.frames[layero.find('iframe')[0]['name']];
            $iframeDoc = $('#' + layero.find('iframe')[0]['id']).contents();

            $iframeDoc[0].title = that.title + $iframeDoc[0].title;
            $iframeDoc.find('head').eq(0).append(linkTag);
            iframeWin._reportHTML(option);
          },
          yes: function () {
            iframeWin.print();
          }
        });
        layer.close(index);
      });
    });

    // 导出
    $exportBtns.on('click', function () {
      if (1 !== isSettleAccounts) {
        layer.msg('当前数据不完整，请在结账后操作', {
          icon: 0,
          shade: 0.3
        });
        return;
      }
      var that = this,
        url,
        $iframeDoc;

      that.index = $(this).attr('data-index');
      switch (that.index) {
      case '0':
        that.title = '资产负债表';
        url = window.baseURL + '/BalanceSheet/downLoadBalanceSheet';
        break;
      case '1':
        if (1 >= $('#profit tr').length) {
          layer.msg('没有可导出数据！');
          return;
        }
        that.title = '利润表';
        url = window.baseURL + '/subBook/exportProfitStatement';
        break;
      case '2':
        that.title = '现金流量表';
        layert.alert('未完成');
        return false;
        url = window.baseURL + '现金流量表';
        break;
      }
      layer.confirm('是否要导出' + that.title + '？', {
        icon: 0,
        title: '提示'
      }, function (index) {
        location.href = url;
        layer.close(index);
      });
    });

    // 判断首页跳转切换页面
    function showTable(num) {
      switch (num) {
      case '1':
        _setCut($('.liabilities'), $('.liabilitiesTable'));
        _setAssestList();
        break;
      case '2':
        _setCut($('.profits'), $('.profitsTable'));
        _setProfitList();
        break;
      case '3':
        _setCut($('.traffic'), $('.trafficTable'));
        break;
      default:
        if (url.substr(url.length - 1, 1) == 1) {
          _setCut($('.liabilities'), $('.liabilitiesTable'));
          _setAssestList();
        } else if (url.substr(url.length - 1, 1) == 2) {
          _setCut($('.profits'), $('.profitsTable'));
          _setProfitList();
        } else if (url.substr(url.length - 1, 1) == 3) {
          _setCut($('.traffic'), $('.trafficTable'));
        }

      }
    }

    // 监听tab切换
    function _initHead(index) {
      switch (index) {
      case 0:
        if (state.balance > 0) {
          fixedHead($('#head1'), $('#assets').parent(), $('#search1'), $('#navDOM1'));
          return false;
        }
        _setAssestList();
        break;
      case 1:
        if (state.profit > 0) {
          fixedHead($('#head2'), $('#profit').parent(), $('#search2'), $('#navDOM1'));
          return false;
        }
        _setProfitList();
        break;
      case 2:
        // if (state.output > 0) {
        fixedHead($('#head3'), $('#cash').parent(), $('#search3'), $('#navDOM1'));
        //     return false;
        // }
        break;
      }
    }

    // 展示列表
    function _setCut(parent, child) {
      parent.addClass('layui-this').siblings().removeClass('layui-this');
      child.addClass('layui-show').siblings().removeClass('layui-show');
      var width = child.width();
      parent.parent().width(width);
    }

    /*    // 点击编辑利润表
     $('#profit').off('click').on('click', '.editIcon', function () {
         console.log('利润表')
     });

     // 点击编辑资产负债表
     $('#assets').off('click').on('click', '.editIcon', function () {
         console.log('资产负债表')
     }); */

    // 报表弹窗
    /*   modifyAlert('修改规则');
      _modifyData(); */

  });



  // 获取资产负债表数据
  function _setAssestList() {
    postRequest(window.baseURL + '/BalanceSheet/queryBalanceSheet', {}, function (res) {
      // console.time('资产负债表渲染');
      if (res.code === 1 && res.queryBalanceSheet.length > 0) {
        var list = res.queryBalanceSheet[0];

        // 流动资产
        var assets = _setAssets();
        var assetsList = _process(list, assets);

        // 流动负债
        var liabilities = _setLiabilities();
        var liabiliList = _process(list, liabilities);

        state.balancePrint = {
          'assetsList': assetsList,
          'liabiliList': liabiliList
        };

        // 列表渲染
        _setBalanceData(assetsList, liabiliList);
      } else {
        layer.closeAll('dialog'); //关闭信息框
        $('#assets').html('<tr><td colspan="8" style="height: 80px;text-align:center">暂无数据</td></tr>');
        fixedHead($('#head1'), $('#assets').parent(), $('#search1'), $('#navDOM1'));
      }
      // console.timeEnd('资产负债表渲染');
    });
    layer.closeAll('loading');
    layer.msg('正在获取资产负债表数据，请稍候~', {
      icon: 16,
      shade: 0.3,
      time: -1
    });

    // 资产负债表数据整合
    // 流动资产
    function _setAssets() {
      var setBalance = [
        { "key": '', "name": "流动资产：", "init": "", "end": "" },
        { "key": 1, "name": "货币资金", "init": "initCash", "end": "endCash" },
        { "key": 2, "name": "交易性金融资产", "init": "initTransactionMonetaryAssets", "end": "endTransactionMonetaryAssets" },
        { "key": 3, "name": "应收票据", "init": "initNotesReceivable", "end": "endNotesReceivable" },
        { "key": 4, "name": "应收账款", "init": "initAccountsReceivable", "end": "endAccountsReceivable" },
        { "key": 5, "name": "预付款项", "init": "initAccountsPrepaid", "end": "endAccountsPrepaid" },
        { "key": 6, "name": "应收利息", "init": "initInterestReceivable", "end": "endInterestReceivable" },
        { "key": 7, "name": "应收股利", "init": "initDividendReceivable", "end": "endDividendReceivable" },
        { "key": 8, "name": "其他应收款", "init": "initOtherReceivables", "end": "endOtherReceivables" },
        { "key": 9, "name": "存货", "init": "initInventories", "end": "endInventories" },
        { "key": 10, "name": "一年内到期的非流动资产", "init": "initNonCurrentAssetsDueInOneYear", "end": "endNonCurrentAssetsDueInOneYear" },
        { "key": 11, "name": "其他流动资产", "init": "initOtherCurrentAssets", "end": "endOtherCurrentAssets" },
        { "key": '', "name": "流动资产合计：", "init": "initTotalCurrentAssets", "end": "endTotalCurrentAssets" },
        { "key": '', "name": "非流动资产：", "init": "", "end": "" },
        { "key": 12, "name": " 可供出售金融资产", "init": "initAvailableForSaleFinancialAssets", "end": "endAvailableForSaleFinancialAssets" },
        { "key": 13, "name": "持有至到期投资", "init": "initHeldToMaturityInvestmen", "end": "endHeldToMaturityInvestmen" },
        { "key": 14, "name": "长期应收款", "init": "initLongTermReceivables", "end": "endLongTermReceivables" },
        { "key": 15, "name": "长期股权投资", "init": "initLongTermEquityInvestment", "end": "endLongTermEquityInvestment" },
        { "key": 16, "name": "投资性房地产", "init": "initInvestmentRealEstates", "end": "endInvestmentRealEstates" },
        { "key": 17, "name": "固定资产", "init": "initFixedAssets", "end": "endFixedAssets" },
        { "key": 18, "name": "在建工程", "init": "initConstructionInProgress", "end": "endConstructionInProgress" },
        { "key": 19, "name": "工程物资", "init": "initConstructionSupplies", "end": "endConstructionSupplies" },
        { "key": 20, "name": "固定资产清理", "init": "initFixedAssetsPendingDisposal", "end": "endFixedAssetsPendingDisposal" },
        { "key": 21, "name": "生产性生物资产", "init": "initBearerBiologicalAssets", "end": "endBearerBiologicalAssets" },
        { "key": 22, "name": "油气资产", "init": "initOilAndNaturalGasAssets", "end": "endOilAndNaturalGasAssets" },
        { "key": 23, "name": "无形资产", "init": "initIntangibelAssets", "end": "endIntangibelAssets" },
        { "key": 24, "name": "开发支出", "init": "initResearchAndDevelopmentCosts", "end": "endResearchAndDevelopmentCosts" },
        { "key": 25, "name": "商誉", "init": "initGoodwill", "end": "endGoodwill" },
        { "key": 26, "name": "长期待摊费用", "init": "initLongTermDeferredExpenses", "end": "endLongTermDeferredExpenses" },
        { "key": 27, "name": "递延所得税资产", "init": "initDeferredTaxAssets", "end": "endDeferredTaxAssets" },
        { "key": 28, "name": "其他非流动资产", "init": "initTotalOfNonCurrentAsses", "end": "endTotalOfNonCurrentAsses" },
        { "key": '', "name": "非流动资产合计：", "init": "initOtherNonCurrentAssets", "end": "endOtherNonCurrentAssets" },
        { "key": '', "name": " 资产总计：", "init": "initTotalOfAssets", "end": "endTotalOfAssets" }
      ];
      return setBalance;
    }

    // 流动负债
    function _setLiabilities() {
      var setLiabilties = [
        { "key": '', "name": "流动负债：", "init": "", "end": "" },
        { "key": 29, "name": "短期借款", "init": "initShortTermLoan", "end": "endShortTermLoan" },
        { "key": 30, "name": "交易性金融负债", "init": "initTradableFinancialLiabilities", "end": "endTradableFinancialLiabilities" },
        { "key": 31, "name": "应付票据", "init": "initNotesPayable", "end": "endNotesPayable" },
        { "key": 32, "name": "应付账款", "init": "initAccountsPayable", "end": "endAccountsPayable" },
        { "key": 33, "name": "预收款项", "init": "initAdvanceReceipts", "end": "endAdvanceReceipts" },
        { "key": 34, "name": "应付职工薪酬", "init": "initAccruedPayroll", "end": "endAccruedPayroll" },
        { "key": 35, "name": "应交税费", "init": "initAccruedTax", "end": "endAccruedTax" },
        { "key": 36, "name": "应付利息", "init": "initAccruedInterestPayable", "end": "endAccruedInterestPayable" },
        { "key": 37, "name": "应付股利", "init": "initDividendPayable", "end": "endDividendPayable" },
        { "key": 38, "name": "其他应付款", "init": "initOtherPayables", "end": "endOtherPayables" },
        { "key": 39, "name": "一年内到期的非流动负债", "init": "initCurrentLiailitiesFallingDueWithinOneYear", "end": "endCurrentLiailitiesFallingDueWithinOneYear" },
        { "key": 40, "name": "其他流动负债", "init": "initOtherCurrentLiabilities", "end": "endOtherCurrentLiabilities" },
        { "key": '', "name": "流动负债合计：", "init": "initTotalOfCurrentLiabilities", "end": "endTotalOfCurrentLiabilities" },
        { "key": '', "name": "非流动负债：", "init": "", "end": "" },
        { "key": 41, "name": " 长期借款", "init": "initLongTermLoan", "end": "endLongTermLoan" },
        { "key": 42, "name": "应付债券", "init": "initBondsPayable", "end": "endBondsPayable" },
        { "key": 43, "name": "长期应付款", "init": "initLongTermAccountsPayable", "end": "endLongTermAccountsPayable" },
        { "key": 44, "name": "专项应付款", "init": "initAccountsPayableForSpecialisedTerms", "end": "endAccountsPayableForSpecialisedTerms" },
        { "key": 45, "name": "预计负债", "init": "initProvisionForLiabilities", "end": "endProvisionForLiabilities" },
        { "key": 46, "name": " 递延所得税负债", "init": "initDeferredIncomeTaxLiabilities", "end": "endDeferredIncomeTaxLiabilities" },
        { "key": 47, "name": "其他非流动负债", "init": "initOtherNonCurrentLiabilities", "end": "endOtherNonCurrentLiabilities" },
        { "key": 48, "name": "非流动负债合计：", "init": "initTotalOfNonCurrentLiabilities", "end": "endTotalOfNonCurrentLiabilities" },
        { "key": 49, "name": "负债合计：", "init": "initTotalOfLiabilities", "end": "endTotalOfLiabilities" },
        { "key": '', "name": "所有者权益(或股东权益)：", "init": "", "end": "" },
        { "key": 50, "name": " 实收资本(或股本)", "init": "initCapital", "end": "endCapital" },
        { "key": 51, "name": "资本公积", "init": "initCapitalReserves", "end": "entCapitalReserves" },
        { "key": 52, "name": "减:库存股", "init": "initLessTreasuryStock", "end": "endLessTreasuryStock" },
        { "key": 53, "name": "盈余公积", "init": "initEarningsReserve", "end": "endEarningsReserve" },
        { "key": 54, "name": "未分配利润", "init": "initRetainedEarnings", "end": "endRetainedEarnings" },
        { "key": 55, "name": "所有者权益(或股东权益)合计：", "init": "initTotalOfOwnersEquity", "end": "endTotalOfOwnersEquity" },
        { "key": '', "name": "", "init": "", "end": "" },
        { "key": '', "name": "负债和所有者权益(或股东权益)总计：", "init": "initTotalOfLiabilitiesAndOwnersEquity", "end": "endTotalOfLiabilitiesAndOwnersEquity" }
      ];
      return setLiabilties;
    }

    // 资产负债表
    function _setBalanceData(assetsList, liabiliList) {
      var str = '';
      state.balance = assetsList.length;
      // 是否结账
      if (isSettleAccounts === 1) {
        for (var i = 0, leng = assetsList.length; i < leng; i++) {
          str += '<tr>' +
            '<td class="tc" style="color:#000;">' + assetsList[i].key + '</td>' +
            '<td class="tl">' +
            '<span>' + assetsList[i].name + '</span>' +
            '</td>' +
            '<td>' + assetsList[i].end + '</td>' +
            '<td>' + assetsList[i].init + '</td>' +
            '<td class="tc"  style="color:#000;">' + liabiliList[i].key + '</td>' +
            '<td class="tl">' +
            '<span>' + liabiliList[i].name + '</span>' +
            '</td>' +
            '<td>' + liabiliList[i].end + '</td>' +
            '<td>' + liabiliList[i].init + '</td>' +
            '</tr>';
        }
      } else {
        for (var i = 0, leng = assetsList.length; i < leng; i++) {
          if (assetsList[i].end == "" || liabiliList[i].end == "") {
            assetsList[i].end = "";
            liabiliList[i].end = "";
          } else {
            assetsList[i].end = "0.00";
            liabiliList[i].end = "0.00";
          }
          str += '<tr>' +
            '<td class="tc" style="color:#000;">' + assetsList[i].key + '</td>' +
            '<td class="tl">' +
            '<span>' + assetsList[i].name + '</span>' +
            '</td>' +
            '<td>' + assetsList[i].end + '</td>' +
            '<td>' + assetsList[i].init + '</td>' +
            '<td class="tc"  style="color:#000;">' + liabiliList[i].key + '</td>' +
            '<td class="tl">' +
            '<span>' + liabiliList[i].name + '</span>' +
            '</td>' +
            '<td>' + liabiliList[i].end + '</td>' +
            '<td>' + liabiliList[i].init + '</td>' +
            '</tr>';
        }
      }

      layer.closeAll('dialog'); //关闭信息框
      $('#assets').html(str);
      fixedHead($('#head1'), $('#assets').parent(), $('#search1'), $('#navDOM1'));
    }
  }


  // 获取利润表数据
  function _setProfitList() {
    /*postRequest(window.baseURL + '/IncomeStatement/queryIncomeStatrment', {}, function (res) {
      console.time('利润表渲染');
      if (res.code === 1) {
        if (res.queryIncomeStatrment.length > 0) {
          var list = res.queryIncomeStatrment[0];
          // 流动资产
          var profit = _profitList();
          var profitList = _process(list, profit);

          state.profitPrint = {
            'profitList': profitList
          };

          // 列表渲染
          _profitTable(profitList);
        } else {
          layer.closeAll('dialog'); //关闭信息框
          $('#profit').html('<tr><td colspan="8" style="height: 80px;text-align:center">暂无数据</td></tr>');
          fixedHead($('#head2'), $('#profit').parent(), $('#search2'), $('#navDOM1'));
        }
      }
      console.timeEnd('利润表渲染');
    });*/
    postRequest(window.baseURL + '/vat/queryProfit', {}, function (res) {
      console.time('利润表渲染');
      if (res.code === '0') {
        var list = res.msg;
        var profit = _profitList();
        var profitList = _process(list, profit);

        state.profitPrint = {
          'profitList': profitList
        };

        // 列表渲染
        _profitTable(profitList);
      } else {
        layer.closeAll('dialog'); //关闭信息框
        res.msg && layer.msg(res.msg);
        $('#profit').html('<tr><td colspan="8" style="height: 80px;text-align:center">暂无数据</td></tr>');
        fixedHead($('#head2'), $('#profit').parent(), $('#search2'), $('#navDOM1'));
      }
      console.timeEnd('利润表渲染');
    });

    layer.closeAll('loading');
    layer.msg('正在获取利润表数据，请稍候~', {
      icon: 16,
      shade: 0.3,
      time: -1
    });

    // 利润表
    function _profitTable(list) {
      var level, str = '';
      state.profit = list.length;
      for (var i = 0, leng = list.length; i < leng; i++) {
        // var flag = (list[i].key === 1 || list[i].key === 11 || list[i].key === 15 || list[i].key === 17 || list[i].key === 18);
        if (list[i].level === 2) {
          level = ' ti1';
        } else if (list[i].level === 3) {
          level = ' ti4';
        } else if (list[i].level === 4) {
          level = ' ti8';
        } else {
          level = '';
        }
        str += '<tr>' +
          '<td class="tc">' + list[i].key + '</td>' +
          '<td class="tl' + level + '">' + list[i].name + '</td>' +
          // if (flag) {
          //   str += '<td class="tl">' + list[i].name + '</td>'
          // } else {
          //   str += '<td class="tl ti">' + list[i].name + '</td>'
          // }
          // str +=
          '<td>' + list[i].year + '</td>' +
          '<td>' + list[i].quarter + '</td>' +
          '<td>' + list[i].period + '</td>' +
          '</tr>';
      }
      $('#profit').html(str);
      fixedHead($('#head2'), $('#profit').parent(), $('#search2'), $('#navDOM1'));
      setTimeout(function () {
        layer.closeAll('dialog'); //关闭信息框
      }, 600);
    }

    // 利润表数据整合
    function _profitList() {
      var profitList = [
        { "key": 1, "name": "一、营业收入", "year": "yearSalesFromOperati", "quarter": "jdSalesFromOperati", "period": "currentSalesFromOperati", "level": 1 },
        { "key": 2, "name": " 减：营业成本", "year": "yearLessOperatingCosts", "quarter": "jdLessOperatingCosts", "period": "currentLessOperatingCosts", "level": 2 },
        { "key": 3, "name": " 营业税金及附加", "year": "yearOperatingTaxAndAdditions", "quarter": "jdOperatingTaxAndAdditions", "period": "currentOperatingTaxAndAdditions", "level": 3 },
        { "key": 4, "name": " 销售费用", "year": "yearSellingExpenses", "quarter": "jdSellingExpenses", "period": "currentSellingExpenses", "level": 4 },
        { "key": 5, "name": " 管理费用", "year": "yearGeneralAndAdministrativeExpense", "quarter": "jdGeneralAndAdministrativeExpense", "period": "currentGeneralAndAdministrativeExpense", "level": 4 },
        { "key": 6, "name": " 财务费用", "year": "yearFinaneiaExpense", "quarter": "jdFinaneiaExpense", "period": "currentFinaneiaExpense", "level": 4 },
        { "key": 7, "name": " 资产减值损失", "year": "yearLossesOnTheAssetImpairment", "quarter": "jdLossesOnTheAssetImpairment", "period": "currentLossesOnTheAssetImpairment", "level": 4 },
        { "key": 8, "name": " 加：公允价值变动收益（损失以“-”号填列）", "year": "yearAddProfitsOrLossesOntheChangesInFairValue", "quarter": "jdAddProfitsOrLossesOntheChangesInFairValue", "period": "currentAddProfitsOrLossesOntheChangesInFairValue", "level": 2 },
        { "key": 9, "name": " 投资收益（损失以“-”号填列）", "year": "yearInvestmentIncome", "quarter": "jdInvestmentIncome", "period": "currentInvestmentIncome", "level": 3 },
        { "key": 10, "name": "  其中：对联营企业和合营企业的投资收益", "year": "yearAmongInvestmentIncomeFromAffiliatedBusiness", "quarter": "jdAmongInvestmentIncomeFromAffiliatedBusiness", "period": "currentAmongInvestmentIncomeFromAffiliatedBusiness", "level": 3 },
        { "key": 11, "name": "二、营业利润（亏损以“-”号填列）", "year": "yearOperatingIncome", "quarter": "jdOperatingIncome", "period": "currentOperatingIncome", "level": 1 },
        { "key": 12, "name": " 加：营业外收入", "year": "yearAddNonOperatingIncome", "quarter": "jdAddNonOperatingIncome", "period": "currentAddNonOperatingIncome", "level": 2 },
        { "key": 13, "name": " 减：营业外支出", "year": "yearLessNonOperatingExpense", "quarter": "jdLessNonOperatingExpense", "period": "currentLessNonOperatingExpense", "level": 2 },
        { "key": 14, "name": " 其中：非流动资产处置损失", "year": "yearIncludingLossesFromDisposalOfNonCurrentAssets", "quarter": "jdIncludingLossesFromDisposalOfNonCurrentAssets", "period": "currentIncludingLossesFromDisposalOfNonCurrentAssets", "level": 3 },
        { "key": 15, "name": "三、利润总额（亏损总额以“-”号填列）", "year": "yearIncomeBeforeTax", "quarter": "jdIncomeBeforeTax", "period": "currentIncomeBeforeTax", "level": 1 },
        { "key": 16, "name": " 减：所得税费用", "year": "yearLessIncomeTax", "quarter": "jdLessIncomeTax", "period": "currentLessIncomeTax", "level": 2 },
        { "key": 17, "name": "四、净利润（净亏损以“-”号填列）", "year": "yearEntIncome", "quarter": "jdtEntIncome", "period": "currentEntIncome", "level": 1 },
        { "key": 18, "name": "五、每股收益：", "year": "yearEarningsPerShare", "quarter": "jdEarningsPerShare", "period": "currentEarningsPerShare", "level": 3 },
        { "key": 19, "name": "（一）基本每股收益", "year": "yearBasicEps", "quarter": "jdBasicEps", "period": "currentBasicEps", "level": 3 },
        { "key": 20, "name": "（二）稀释每股收益", "year": "yearDilutedEps", "quarter": "jdDilutedEps", "period": "currentDilutedEps", "level": 3 }
      ];
      return profitList;
    }
  }



  // 数据整理
  function _process(list, balance) {
    // console.time('_process');
    for (var key in list) {
      if (list.hasOwnProperty(key)) {
        for (var i = 0, leng = balance.length; i < leng; i++) {
          if (balance[i].init === key) {
            var num = (list[key] ? list[key].toFixed(2) : '0.00');
            balance[i].init = formatNum(num);
            break;
          }
          if (balance[i].end === key) {
            var num = (list[key] ? list[key].toFixed(2) : '0.00');
            balance[i].end = formatNum(num);
            break;
          }
          if (balance[i].year === key) {
            var num = (list[key] ? list[key].toFixed(2) : '0.00');
            balance[i].year = formatNum(num);
            break;
          }
          if (balance[i].quarter === key) {
            var num = (list[key] ? list[key].toFixed(2) : '0.00');
            balance[i].quarter = formatNum(num);
            break;
          }
          if (balance[i].period === key) {
            var num = (list[key] ? list[key].toFixed(2) : '0.00');
            balance[i].period = formatNum(num);
            break;
          }
        }
      }
    }
    // console.timeEnd('_process');
    return balance;
  }




  // 报表弹窗
  function modifyAlert(title) {
    var $modify = $('#modify'); // 弹窗Id
    var modify = layer.open({
      'type': 1,
      'skin': 'demo-class',
      'title': [title, 'font-size:18px;'],
      'area': ['600px', 'auto'],
      'shade': 0.3,
      'anim': 5,
      'content': $modify,
      'btn': ['取消', '确定'],
      cancel: function () {
        $modify.hide();
      },
      btn0: function () {
        $modify.hide();
      },
      btn1: function () {
        $modify.hide();
        layer.close(modify);
      }
    });
  }

  // 弹窗操作
  function _modifyData() {
    // 添加
    $('#modify-add').off('click').on('click', function () {
      var subData = $('#modify-sub').init();
      var symbolData = $('#modify-symbol').init();
      var ruleData = $('#modify-rule').init();
      var isTrue = null;
      if (!isTrue && !subData) {
        isTrue = '请选择科目类型';
      }
      if (!isTrue && !symbolData) {
        isTrue = '请选择运算符';
      }
      if (!isTrue && !ruleData) {
        isTrue = '请选择取现规则';
      }
      if (isTrue) {
        layer.msg(isTrue);
        return false;
      }

      // 渲染页面
      var str = ' <tr> ' +
        ' <td>' + subData + '</td> ' +
        ' <td>' + symbolData + '</td> ' +
        ' <td>' + ruleData + '</td> ' +
        ' <td> ' +
        ' <i class="layui-icon removeIcon">&#x1006;</i> ' +
        ' </td> ' +
        ' </tr> ';
      $('#modify-item').append(str);
    });

    // 删除
    $('#modify-item').off('click').on('click', '.removeIcon', function () {

    })
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
}());
