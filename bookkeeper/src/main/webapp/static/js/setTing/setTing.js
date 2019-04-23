/*
 * @Author: Rong
 * @Date: 2018-04-19 10:21:03
 * @Last Modified by: Rong
 * @Last Modified time: 2018-04-19 10:36:39
 */
// 显示权限按钮
; (function () {
  var url = location.hash;
  if(url.substr(url.length-1,1) == 1){
	$("#navDOM1 li:eq(0)").click();
  }else if(url.substr(url.length-1,1) == 2){
	$("#navDOM1 li:eq(1)").click();
	_queryMeasure();
  }else if(url.substr(url.length-1,1) == 3){
	$("#navDOM1 li:eq(2)").click();
	_addExchange();
  }
  var show = getPurviewData(1000);
  if (show === 'block') {
    $('.search').css('display', show);
    $('#content').css('padding-top', '25px');
  }
  var Iheight = $("#content").height();
  $("#subMessage").css({"max-height":Iheight-152});
  $("#accountDataT").css({"max-height":Iheight-102});
  // 定义公用对象，存放数据
  var state = {
    'category': 1
  };

  $(function () {
    var $set_tbost = $('.set_tbost'),
      $set_add_alert = $('#set_add-alert'),
      $set_unit = $('.set_unit'),
      $set_alert_addForm = $('#set_alert-addForm'),
      title = '',
      $set_unit_num = $('#set_unit_num'),
      $set_unit_sign = $('#set_unit_sign'),
      $set_unit_nane = $('#set_unit_nane'),
      $set_unit_remark = $('#set_unit_remark');

    // 监听tab切换
    layui.element.on('tab(setTingTab)', function (data) {
      state.tabIndex = data.index;
      var $con = $('#content');
      switch (state.tabIndex) {
        case 0:
          fixedHead($('#fixedDOM1'), $('#subMessage'), $('#searchDOM1'), $('#navDOM1'));
          $con.css('padding-top', '46px');
          break;
        case 1:
          if (state.symbolNum > 0) {
            fixedHead($('#fixedDOM2'), $('#ser_account').parent(), $('#searchDOM2'), $('#navDOM1'));
            $con.css('padding-top', '46px');
            return false;
          }
          _queryMeasure(); // 计量单位设置
          break;
        case 2:
          if (state.exchange > 0) {
            fixedHead($('#fixedDOM3'), $('#set_unit').parent(), $('#searchDOM3'), $('#navDOM1'));
            $con.css('padding-top', '46px');
            return false;
          }
          _addExchange(); // 获取人名币页面
          $('#unit_name').css('letter-spacing', '14px');
          break;
        case 3:
          fixedHead(null, null, null, $('#navDOM1'));
          $con.css('padding-top', '46px');
          break;
      }

      // 每次切换，都情况公用组件
      $set_unit_num.val('');
      $set_unit_sign.val('');
      $set_unit_nane.val('');
      $set_unit_remark.val('');
    })

    // 科目设置，列表切换
    layui.element.on('tab(subMessage)', function (data) {
      state.category = data.index + 1;
      _subMessage(state.category, '');// 科目列表
    });


    // 科目设置
    // 点击删除本行
    $set_tbost.on('click', '.set_icon-remove', function () {
      var $this = $(this);
      layer.confirm('确定删除该内容？', {
        title: ['提示', 'font-size:18px;'],
        btn: ['确定', '取消'],
        skin: 'demo-class',
        btn1: function () {
          var subId = $this.parent().attr("data-subid"),
            url = window.baseURL + '/subject/deleteMessageByPrimaryKey',
            params = {
              'pkSubId': subId
            };
          console.log(subId);
          postRequest(url, params, function (res) {
            if (res.code === 1) {
              layer.msg('操作成功');
              $this.parent().parent('tr').remove();
            }
          })
        }
      });

      // 新增科目
    }).on('click', '.set_icon-add', function () {
      state.exchType = 1;
      var subCode = $(this).attr('data-code'),
        title = '科目新增';
      var subName = $(this).parent().siblings('.set_tbost_name').text();
      $('#set_alert-hide').text(subCode);
      $('.set_alert-hide, .set_alert-grade').css('display', 'block');
      $('#set_code').prop('disabled', false).css({
        'padding-left': '34px',
        'margin-top': '0',
        'width': '156px'
      });
      $('#set_subjectName').prop('disabled', false).val('');

      // 自动计算宽度
      state.timer = setTimeout(function () {
        var width = $('#set_alert-hide').width() + 3;
        $('#set_code').css({
          'padding-left': width + 'px',
          'width': (190 - width) + 'px'
        }).val('001').focus();
      }, 10);
      // 科目新增
      new Subject({
        'elem': $('#addSub'), // 唤醒弹窗的id
        'addUnit': $('#addUnit'),// 唤醒弹窗的id
        'SubElem': $('#setSubData'), // 科目列表id
        'codeElem': $('#setSubCode'),// 科目编码id
        'SurpluElem': $('#setSubSurplus'),// 数量核算id
        'SubValElem': $('#getSubName'), // 科目名称
        'UnitSymbol': $('#UnitSymbol'),// 计量单位符号
        'UnitName': $('#UnitName'),// 计量单位名称
        'UnitRemarks': $('#UnitRemarks'), // 备注
        'setDirection': $('#setDirection'), // 设置单选按钮
        'refresh': _refresh // 新增科目刷新数据
      });
      //$(".layui-select-title .layui-input").val(subCode+'-'+subName);
    });

    //   汇率设置
    // 点击删除本行
    $set_unit.on('click', '.set_icon-remove', function () {
      var $this = $(this);
      layer.confirm('是否删除该条数据？', {
        title: ['提示', 'font-size:18px;'],
        btn: ['确定', '取消'],
        skin: 'demo-class',
        btn1: function () {
          if (state.tabIndex === 2) {
            // 汇率
            var ExId = $this.parent().attr('data-exid'),
              url = window.baseURL + '/exchange/delExchangeByExchangeId',
              params = {
                'pkExchangeRateId': ExId
              };
            _removeMesure($this, url, params);
          }
        }
      });

      // 修改汇率
    }).on('click', '.set_icon-edit', function () {
      state.exchType = 2;
      var code,
        name;
      if (state.tabIndex === 2) {
        $('#set_none').css('display', 'block');
        state.EkId = { 'pkExchangeRateId': $(this).parent().attr('data-exid') }; //汇率id
        code = $(this).parent().siblings('.set_tbost_code').text();
        name = $(this).parent().siblings('.set_tbost_name').text();
        $set_unit_num.prop('disabled', true).val(code);
        $set_unit_sign.prop('disabled', true).val(name);
        title = '币种修改';
        _RMBAlert($set_alert_addForm, title);
      }

      // 新增汇率
    }).on('click', '.set_icon-add', function () {
      $set_alert_addForm.find('input[type="text"]').prop('value', '');
      state.exchType = 1;
//    if (state.tabIndex === 2) {
        $('#set_none').css('display', 'block');
        $('#set_unit_sign, #set_unit_num').prop('disabled', false).val('');
        title = '币种新增';
        _RMBAlert($set_alert_addForm, title);
//    }
    })

    // 新增汇率 / 计量单位
    $('.set_item_add').on('click', function () {
      $set_alert_addForm.find('input[type="text"]').prop('value', '');
      state.exchType = 1;
//    if (state.tabIndex === 2) {
        $('#set_none').css('display', 'block');
        $('#set_unit_sign, #set_unit_num').prop('disabled', false).val('');
        title = '币种新增';
        _RMBAlert($set_alert_addForm, title);
//    }
    })


    // 科目编码输入，限制3位，只能输入数子
    var $this,
      LIMIT = 1000000000;
    $set_add_alert.on('input', '#set_code', function () {
      $this = $(this);
      var v = $this.val().replace(/\D/g, "");
      $this.val(v);

      // 输入初期余额限制10亿内
    }).on('input', '#set_balance', function () {
      $this = $(this);
      limitInputNum($this, LIMIT);
      var a = toFixedNum($this);
      $this.val(a);
    });


    //汇率小数点后面位数 普通4位，先进5位
    $set_unit_nane.on('input', function () {
      if (state.tabIndex === 2) {
        limitInputNum($(this), LIMIT);
        var v = toFixed($(this));
        $(this).val(v);
      }
    });


    // 点击禁用
    $('#set-fixedHead').on('click', 'li', function () {
      state.$this = $(this);
      state.isTab = $(this).attr('data-tab');
    });


    // 科目设置关键字查询
    $('#set_data_btn').on('click', function () {
      var val = $('#set_val').val();
      var oldVal = $(this).attr('data-val');
      if (!val) {
        layer.msg('请输入关键字');

        if (oldVal) {
          $(this).removeAttr('data-val');
          _subMessage(state.category, '');// 科目列表
        }

        return false;
      }

      if (oldVal === val) {
        return false;
      }

      $(this).attr('data-val', val);
      _subMessage(state.category, val);// 科目列表
    });

    // 计量单位搜索
    $('#searchName').on('click', function () {
      var name = $('#symbolOrName').val();
      var oldVal = $(this).attr('data-val');
      if (!name) {
        layer.msg('请输入关键字');

        if (oldVal) {
          $(this).removeAttr('data-val');
          _queryMeasure();
        }
        return false;
      }

      if (oldVal === name) {
        return false;
      }

      $(this).attr('data-val', name);
      _queryMeasure(name);
    })

    // 获取焦点， 输入框内容为空
    removeInputVal($('#set_val'));
    // 获取焦点， 输入框内容为空
    removeInputVal($('#symbolOrName'));


  });


  // 新增计量单位弹窗
  function _RMBAlert($el, title) {
    var page = layer.open({
      type: 1,
      skin: 'demo-class',
      title: [title, 'font-size:18px;'],
      area: ['800px', 'auto'],
      shade: 0.3,
      anim: 5,
      content: $el,
      btn: ['保存并新增', '关闭'],
      cancel: function () {
        $el.hide();
      },
      btn1: function () {
        state.unitNum = $('#set_unit_num').val();
        state.unitSign = $('#set_unit_sign').val();
        state.unitNane = $('#set_unit_nane').val();
        state.unitRemark = $('#set_unit_remark').val();
        var isErr = getError(2, '币种简称', '币种名称', '汇率');
        if (isErr) {
          layer.msg(isErr);
          return false;
        }
        var params = {
          'exchangeRateRemarks': state.unitRemark, // 备注
          'currencyAbbreviateName': state.unitNum,  // 币种简称
          'currencyFullName': state.unitSign, // 币种名称
          'endingCurrencyRate': state.unitNane // 汇率
        }
        _allExchData(params);

        $el.hide();
        layer.close(page);
      },
      btn2: function () {
        $el.hide();
      }
    });
  }

  // 判断错误
  function getError(type, num, sign, name) {
    var isErr;
    if (type === 2) {
      if (!isErr && !state.unitNum) {
        isErr = num;
      }
    }
    if (!isErr && !state.unitSign) {
      isErr = sign;
    }
    if (!isErr && !state.unitNane) {
      isErr = name;
    }
    return isErr;
  }



  // 小数后面5位
  function toFixed(elem) {
    //先把非数字的都替换掉，除了数字和.
    elem.val(elem.val().replace(/[^\d.]/g, "").//只允许一个小数点
      replace(/^\./g, "").replace(/\.{2,}/g, ".").//只能输入小数点后5位
      replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d\d\d\d).*$/, '$1$2.$3'));
    return elem.val();
  }

  // 科目
  // 科目设置
  function _subMessage(type, code) {
    if (state.isTab) {
      return false;
    }
    layer.load();
    $.ajax({
      type: "GET",
      url: window.baseURL + '/subject/querySubMessageByCategory?category=' + type + '&subject=' + code,
      data: {},
      dataType: 'json',
      success: function (res) {
        if (res.code === 1) {
          if (state.$this) {
            state.$this.attr('data-tab', 'isTab').siblings().removeAttr('data-tab');
          }
          var str = '',
            i = 0,
            subList = res.subMessageList,
            leng = subList.length;
          state.subject = leng;
          if (leng > 0) {
            for (i; i < leng; i++) {
              subList[i].direction = !subList[i].debitCreditDirection ? '' : subList[i].debitCreditDirection === '1' ? '借' : '贷';
              if (subList[i].state === '1') {
                subList[i].state = '启用';
              } else if (subList[i].state === '0' || subList[i].state === '2') { // 不确定数据库用的是0还是2
                subList[i].state = '禁用';
              }
              if (subList[i].subName) {
                str += '<tr>'
                  + '<td class="set_tbost_code">' + subList[i].subCode + '</td>'
                  + '<td class="set_tbost_name tl">' + subList[i].subName + '</td>'
                  + '<td class="tl">' + subList[i].fullName + '</td>'
                  + '<td>' + subList[i].direction + '</td>'
                  + '<td>' + subList[i].state + '</td>'
                  + '<td class="set_icon" data-subId="' + subList[i].pkSubId + '">'
                  + '<i data-code="' + subList[i].subCode + '" class="layui-icon set_icon-add">&#xe654;</i>'
                  // + '<i class="layui-icon set_icon-edit">&#xe642;</i>'
                  // + '<i class="layui-icon set_icon-remove">&#x1006;</i>'
                  + '</td>'
                  + '</tr>';
              }
            }
          } else {
            str = '<tr class="tc"><td colspan="6" style="height: 80px;">暂无数据</td></tr>';
          }
          $('#set_tbost').html(str);
          fixedHead($('#fixedDOM1'), $('#subMessage'), $('#searchDOM1'), $('#navDOM1'));
          layer.closeAll('loading');
          if($("#set_tbost").height() > $("#content").height()-152){
          	$("#fixedDOM1").width($("#fixedDOM1").width()-17);
          }
        }
      },
      error: function () {
        layer.closeAll('loading');
      }
    })
  };


  // 计量单位设置
  function _queryMeasure(name) {
    var symbolOrName = name || ''
    layer.load();
    $.ajax({
      type: "GET",
      url: window.baseURL + '/measure/queryMeasureBySymbolOrName?symbolOrName=' + symbolOrName,
      data: {},
      dataType: 'json',
      success: function (res) {
        if (res.code === 1) {
          var str = '',
            radio = '';
          list = res.queryExchangeList,
            leng = list.length,
            i = 0;
          state.symbolNum = leng;
          if (leng > 0) {
            for (i; i < leng; i++) {
              radio += '<input type="radio" name="sex" value="' + list[i].measUnitSymbol + '" title="' + list[i].measUnitName + '">';
              list[i].upDate = formatDate(list[i].updateDate, 'yyyy-MM-dd');
              list[i].ctDate = formatDate(list[i].createDate, 'yyyy-MM-dd');
              str += '<tr>'
                + '<td class="set_tbost_code">' + (i + 1) + '</td>'
                + '<td>' + list[i].measUnitSymbol + '</td>'
                + '<td>' + list[i].measUnitName + '</td>'
                + '<td>' + list[i].ctDate + '</td>'
                + '<td>' + list[i].upDate + '</td>'
                + '<td>' + list[i].measUnitRemarks + '</td>'
                + '</tr>';
            }
          } else {
            str = '<tr class="tc"><td colspan="6" style="height: 80px;">暂无数据</td></tr>';
          }
          $('#ser_account').html(str);
          $('#set_radio_input').html(radio);
          fixedHead($('#fixedDOM2'), $('#ser_account').parent(), $('#searchDOM2'), $('#navDOM1'));
          layer.closeAll('loading');
          layui.form.render('radio');
        }
      },
      error: function () {
        layer.closeAll('loading');
      }
    })
  }


  // 删除计量单位
  function _removeMesure(el, url, params) {
    postRequest(url, params, function (res) {
      layer.msg('操作成功');
      el.parent().parent('tr').remove();
    })
  }

  // 汇率
  // 获取汇率页面
  function _addExchange() {
    var url = window.baseURL + '/exchange/queryExchangeRate',
      ExchangeList,
      ExStr = '',
      leng,
      i = 0;
    postRequest(url, {}, function (res) {
      // if (res.code === 1) {
      ExchangeList = res.Exchange || [];
      leng = ExchangeList.length;
      state.exchange = leng;
      if (leng > 0) {
        for (i; i < leng; i++) {
          ExStr += '<tr>'
            + '<td class="set_tbost_code">' + ExchangeList[i].currencyAbbreviateName + '</td>'
            + '<td class="set_tbost_name">' + ExchangeList[i].currencyFullName + '</td>'
            + '<td>人民币</td>'
            + '<td>' + ExchangeList[i].endingCurrencyRate + '</td>'
            + '<td></td>'
            + '<td>' + ExchangeList[i].exchangeRateRemarks + '</td>'
            + '<td class="set_icon"  data-exId="' + ExchangeList[i].pkExchangeRateId + '">'
            + '<i class="layui-icon set_icon-add">&#xe654;</i>'
            + '<i class="layui-icon set_icon-edit">&#xe642;</i>'
            + '<i class="layui-icon set_icon-remove">&#x1006;</i>'
            + '</td>'
            + '</tr>';
        }
      } else {
        ExStr = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无数据</td></tr>';
      }
      $('#set_unit').html(ExStr);
      fixedHead($('#fixedDOM3'), $('#set_unit').parent(), $('#searchDOM3'), $('#navDOM1'));
    })
  };

  // 新增 / 修改 1新增，2修改
  function _allExchData(params) {
    if (state.exchType === 1) {
      var url = window.baseURL + '/exchange/addExchangeByStr';
      postRequest(url, params, function (res) {
        _addExchange();
        layer.msg('新增成功');
      });
    } else {
      if (state.EkId) {
        $.extend(params, state.EkId);
        var url = window.baseURL + '/exchange/updateExchange';
        postRequest(url, params, function (res) {
          _addExchange();
          layer.msg('修改成功');
        });
      }

    }
  }

  // 新增科目刷新数据
  function _refresh() {
    _subMessage(state.category, ''); // 科目设置
  }

  _subMessage(state.category, ''); // 科目设置

}());


