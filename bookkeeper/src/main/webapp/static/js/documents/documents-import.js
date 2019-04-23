/**
 ** 单据导入
 **/
// 显示权限按钮
! function() {
  var show = getPurviewData(400);
  if (show === 'block') {
    $('.search').css('display', show);
    $('#content').css('padding-top', '0px');
  }
}();

$(function() {
  var state = {},
    $content = $('#content'),
    $fixedNav = $('#navDOM1'),
    $tabContent = $('#tab-content'),
    $tabItem = $tabContent.children('.layui-tab-item'),
    $addBtn = $tabContent.find('.add_btn'),
    $saveBtn = $tabContent.find('.save_btn'),
    $resetBtn = $tabContent.find('.reset_btn'),
    $ticketsList = $tabContent.find('.tickets-list'),
    $unitPopup = $('#unit-popup'),
    $unitInput,
    $searchDoc = $('#search-doc'),
    $batchBtn = $('#batch-del'),
    $docTableBox = $('#doc-table-box'), // 单据录入列表盒子
    $docList = $('#doc-list'); // 单据录入表单

  // init
  state.procCode = {};
  state.tabIdx = $fixedNav.children('.layui-this').index();
  _getTabData(state.tabIdx);
  _getUnit(); // 获取计量单位

  // nav切换
  layui.element.on('tab(tab)', function(data) {
    state.tabIdx = data.index;
    if ($tabItem.eq(state.tabIdx).hasClass('edit_tab')) {
      $tabItem.eq(state.tabIdx).removeClass('edit_tab')
        .find('li').removeClass('dn');
    }
    _getTabData(state.tabIdx); //点击请求数据
    $content.scrollTop(0);
  })

  // 采购票一级科目切换
  layui.element.on('tab(subCode)', function(data) {
    var idx = data.index,
      subCode = $(this).attr('data-code');

    _getProcSubCode(idx, subCode);
    $content.scrollTop(0);
  });

  $tabContent
    // 全选内容
    .on('focus', 'input', function() {
      $(this).select();
    })
    // 票据列表模糊查询
    .on('input', 'input.tickets-search', function() {
      var v = $(this).val().trim(),
        $div = $(this).parents('.proc-title').next();
      $div.find('li.fullName:contains(' + v + ')').parent().show();
      $div.find('li.fullName:not( :contains(' + v + ') )').parent().hide();
    })

  // 点击隐藏计量单位列表
  $('html').on('click', function(event) {
    $unitPopup.stop().slideUp(200);
  });

  // 计量单位输入框
  $ticketsList
    // 计量单位定位显示
    .off('click').on('click', 'input.unit-text', function(event) {
      var e = event || window.event;
      // 阻止冒泡
      e.stopPropagation();
    })
    .off('focus').on('focus', 'input.unit-text', function() {
      $unitInput = $(this);
      $(this).addClass('focus');
      $unitInput.oldValue = $unitInput.val();
      $unitPopup.find('li').show();
      _unitListPosition();
    })
    // 计量单位模糊查询
    .off('input').on('input', 'input.unit-text', function() {
      var v = $(this).val().trim();

      $unitPopup.find('li:contains(' + v + ')').show();
      $unitPopup.find('li:not( :contains(' + v + ') )').hide();
    })
    .off('blur').on('blur', 'input.unit-text', function() {
      $(this).removeClass('focus');
      if (!$(this).attr('data-unitid')) {
        $(this).val('');
      } else {
        $unitInput.oldValue && $(this).val($unitInput.oldValue);
      }
    });

  // 写入计量单位
  $unitPopup.off('click').on('click', 'li', function() {
    var v = $(this).text(),
      unit = $(this).attr('data-unit'),
      id = $(this).attr('data-id');

    if ($(this).hasClass('disabled')) {
      return;
    }
    $unitInput.val(v).attr({
      'data-unit': unit,
      'data-unitId': id
    });
    $unitInput = null;
    $unitPopup.stop().slideUp(200);
  });

  $('#form3').on('blur', 'input[type="text"]', function() {
    var $this = $(this),
      attrName = $this.attr('name') === 'tax' ? 'data-tax' : 'data-amount',
      v = $this.val();

    $this.parents('li').attr(attrName, v);
  });

  $addBtn.on('click', _addData);

  $saveBtn.on('click', _saveData);

  $resetBtn.on('click', _resetForm);

  // 采购票科目
  $('#subCode-content1, #subCode-content2')
    // 选择li
    .on('click', 'li', function() {
      $(this).addClass('on').siblings().removeClass('on');
    })
    // 模糊查询
    .on('input', 'input', function() {
      var v = $(this).val(),
        $div = $(this).parent();
      $div.find('li:contains(' + v + ')').show();
      $div.find('li:not( :contains(' + v + ') )').hide();
    })

  // 搜索栏
  $searchDoc.on('click', function() {
    var params = {},
      word = $('#keyWord').val().trim();
    if (word) {
      // params
      _getAccDoc(params);
    } else {
      layer.msg('请输入任意查询条件');
    }
  });

  // 限制输入数字
  $content.on('input', '.digit-input', function(e) {
    _toFixedNum($(this));
  });

  // 批量删除数据
  $batchBtn.on('click', function() {
    _batchDel($docList);
  });

  $docTableBox
    // 点击复选框
    .on('click', '.layui-form-checkbox', function() {
      checkBoxState($(this));
    })
    // 单条数据修改
    .on('click', 'span.edit', function() {
      _toEdit($(this));
    })
    // 单条数据删除
    .on('click', 'span.remove', function() {
      var $tr = $(this).parents('tr'),
        idx = $tr.index();

      layer.confirm('是否删除当前数据?', { icon: 0, title: '提示' }, function() {
        _deleteData([idx]);
      });
    });

  // 计量单位列表滚动适应
  $('#proc-raw-list, #proc-stock-list').off('scroll').on('scroll', function() {
    _unitListPosition();
  })

  // 数据接口调用请求
  function _getTabData(idx) {
    switch (idx) {
      case 0:
        $unitInput = null;
        if (state.procRaw) {
          fixedHead($('#fixedDOM1'), $('#form1'), '', $fixedNav);
          _unitListResize();
          return false;
        }
        _getProcurementData(0); // procurement ticket 采购票(原材料) raw
        _getProcSubCode(0, 2202);
        break;
      case 1:
        $unitInput = null;
        if (state.procStock) {
          fixedHead($('#fixedDOM2'), $('#form2'), $('#searchDOM2'), $fixedNav);
          _unitListResize();
          return false;
        }
        _getProcurementData(1); // procurement ticket 采购票(库存) inventory
        _getProcSubCode(0, 2202);
        break;
      case 2:
        if (state.sales) {
          fixedHead($('#fixedDOM3'), $('#form3'), $('#searchDOM3'), $fixedNav);
          return false;
        }
        _getTickets(); // sales revenue ticket 销售收入票
        break;
      case 3:
        if (state.costRow) {
          fixedHead($('#fixedDOM4'), $('#form4'), $('#searchDOM4'), $fixedNav);
          return false;
        }
        _getTickets(); // cost ticket 费用票(现金支付) cash
        break;
      case 4:
        if (state.queryDocumentsList) {
          fixedHead($('#fixedDOM5'), $('#acc-doc'), $('#searchDOM5'), $fixedNav);
          return false;
        }
        _getAccDoc();
        break;
    }
  }


  /**
   * 动态获取采购票
   * type 0:库存商品 1:原材料
   * 单据类别 documentsType(1. 采购单据 2.销售单据)
   */
  function _getProcurementData(type) {
    var url = [window.baseURL + '/documents/procurementInventoryRow', window.baseURL + '/documents/procurementRawMaterialsRow'],
      ele = ['#proc-raw-list', '#proc-stock-list'];

    getRequest(url[type], function(res) {
      if (res.code === 1) {
        layer.closeAll('dialog');
      } else {
        res.msg && layer.msg(res.msg, {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }
      _listHTML(res.debitList || []);
    });

    function _listHTML(list) {
      var str = '',
        len = list.length,
        $ele = $(ele[type]);


      if (len) {
        $.each(list, function(i, el) {
          str += '<ul class="proc-list">' +
            '<li class="list-item w1 fullName" title="' + el.subCode + ' ' + el.fullName + '">' + el.fullName + '</li>' +
            '<li class="list-item w2">' +
            '<input type="text" name="unit" class="layui-input unit-text" placeholder="选择计量单位">' +
            '</li>' +
            '<li class="list-item w3">' +
            '<input type="text" name="number" class="layui-input num-input" maxlength="12" placeholder="数量">' +
            '</li>' +
            '<li class="list-item w3">' +
            '<input type="text" name="price" class="layui-input digit-input" maxlength="12" placeholder="单价">' +
            '</li>' +
            '<li class="list-item w3">' +
            '<input type="text" name="amount" class="layui-input digit-input" maxlength="12" placeholder="金额">' +
            '</li>' +
            '</ul>';
        });
      } else {
        str = '<ul class="proc-list"><li class="list-item tc" style="line-height: 80px;">暂无数据</li></ul>';
      }
      if (len * 37 > ($(window).height() - 214)) {
        $ele.prev('dl').addClass('side');
      } else {
        $ele.prev('dl').removeClass('side');
      }
      $ele.html(str);
      $unitInput = null;
      switch (type) {
        case 0:
          state.procRaw = list;
          fixedHead($('#fixedDOM1'), $('#form1'), '', $fixedNav);
          len && _unitListResize();
          /*// 计量单位列表宽度
          if (len) {
            var $item = $ele.find('.w2').eq(0),
              _width = $item.width();

            $unitPopup.css('width', _width);
            $(window).off('resize').on('resize', function () {
              var _left = $item.offset().left + 5 - 20;
              _width = $item.width();

              $unitPopup.css({
                'left': _left,
                'width': _width
              });
            });
          }*/
          break;
        case 1:
          state.procStock = list;
          fixedHead($('#fixedDOM2'), $('#form2'), $('#searchDOM2'), $fixedNav);
          len && _unitListResize();
          break;
      }
    }
  }

  // 根据一级科目获取采购票科目
  function _getProcSubCode(tabIdx, subCode) {
    if (state.procCode[subCode]) {
      return false;
    }
    postRequest(window.baseURL + '/documents/procurementCreditRow', {
      creditSubCode: subCode
    }, function(res) {
      if (res.code === 1) {
        _listHTML(res.creditList || []);
      } else {
        res.msg && layer.msg(res.msg, {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        _listHTML([]);
      }
    });

    function _listHTML(list) {
      var len = list.length,
        str = '',
        title;

      if (len) {
        $.each(list, function(i, el) {
          title = el.subCode + ' ' + el.fullName;
          str += '<li class="layui-elip subcode-item ' + (i === 0 ? 'on' : '') + '" title="' + title + '">' + title + '</li>';
        });
      } else {
        str = '<p class="subcode-empty">暂无数据</p>';
      }
      state.procCode[subCode] = list;
      $('#subCode-content1').find('ul').eq(tabIdx).html(str);
      $('#subCode-content2').find('ul').eq(tabIdx).html(str);
    }
  }

  /**
   * 动态获取销售收入票、费用票
   * fullName 部分为null(有外币)
   * 单据录入金额 amountDebit
   * 单据类别 documentsType(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
   */
  function _getTickets() {
    var urlArr = [window.baseURL + '/documents/salesCreditRow', window.baseURL + '/documents/ticketsCostRow'],
      url;

    switch (state.tabIdx) {
      case 2:
        url = urlArr[0];
        break;
      case 3:
        url = urlArr[1];
        break;
    }

    getRequest(url, function(res) {
      if (res.code === 1) {
        _listHTML(res.ticketsCostRow || []);
      } else {
        res.msg && layer.msg(res.msg, {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }
    });

    function _listHTML(list) {
      var arr = [],
        str1 = '',
        str2 = '',
        str3 = '',
        superiorCoding,
        $ul;

      switch (state.tabIdx) {
        case 2:
          $.each(list, function(i, el) {
            superiorCoding = el.subCode.substring(0, 4);
            if (superiorCoding === '1001') {
              str1 += _listInnerHTML(i, el);
            } else if (superiorCoding === '1122') {
              str2 += _listInnerHTML(i, el, 'tax');
            } else if (superiorCoding === '2203') {
              str3 += _listInnerHTML(i, el, 'tax');
            } else if (superiorCoding === '2221') {
              // subName.equals("未交增值税"), subName.contains("免税")
              arr.push(el);
            } else {
              // 新增票据时需要传参
              arr.push(el);
            }
          });
          $ul = $ticketsList.eq(2).children('ul');
          $ul.eq(0).html(str1);
          $ul.eq(1).html(str2);
          $ul.eq(2).html(str3);
          layui.form.render('radio');
          state.sales = list;
          state.salesReturn = arr;
          fixedHead($('#fixedDOM3'), $('#form3'), $('#searchDOM3'), $fixedNav);
          break;
        case 3:
          $.each(list, function(i, el) {
            superiorCoding = el.subCode.substring(0, 4);
            if (superiorCoding === '6601') {
              str1 += _listInnerHTML(i, el);
            } else if (superiorCoding === '6602') {
              str2 += _listInnerHTML(i, el);
            } else {
              // 新增票据时需要传参
              arr.push(el);
            }
          });
          $ul = $ticketsList.eq(3).children('ul');
          $ul.eq(0).html(str1);
          $ul.eq(1).html(str2);
          state.costRow = list;
          state.costReturn = arr;
          fixedHead($('#fixedDOM4'), $('#form4'), $('#searchDOM4'), $fixedNav);
          break;
      }
    }

    function _listInnerHTML(index, el, hasTax) {
      var dict = el.subCode,
        title = el.fullName || el.subName,
        str,
        str1;

      str1 = '<span class="tax-select">' +
        '<input type="radio" name="tax-select' + index + '" title="未交增值税" value="1" checked>' +
        '<input type="radio" name="tax-select' + index + '" title="免税" value="2">' +
        '</span>' +
        '<div class="layui-input-inline">' +
        '<input type="text" class="layui-input digit-input" name="tax" data-index="' + index + '" maxlength="12" placeholder="税金（选填）">' +
        '</div>';
      str = '<li class="form-item">' +
        '<label class="layui-form-label" for="' + dict + '" title="' + dict + ' ' + title + '">' + title + '</label>' +
        '<div class="layui-input-inline">' +
        '<input type="text" class="layui-input digit-input" name="' + dict + '" id="' + dict + '" data-index="' + index + '" maxlength="12" placeholder="请输入金额">' +
        '</div>' +
        (hasTax === 'tax' ? str1 : '') +
        '</li>';
      return str;
    }
  }

  // 清空当前票据列表输入
  function _resetForm(type) {
    var $inputs = $ticketsList.eq(state.tabIdx).parent().find('input[type="text"]');
    if (type === true) {
      $inputs.prop('value', '').parents('li').removeClass('dn');
      return;
    }
    layer.confirm('是否清空当前表单数据?', { icon: 0, title: '提示' }, function(index) {
      $inputs.prop('value', '');
      layer.close(index);
    });
  }

  /*
   * 新增当前票据列表
   * (费用票)
   * 单据类别 documentsType(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
   */
  function _addData() {
    var tabIdx = state.tabIdx,
      $lis,
      $inputs = $ticketsList.eq(tabIdx).find('input[type="text"]'),
      urlArr = [window.baseURL + '/documents/addProcurementInventoryList', window.baseURL + '/documents/addProcurementRawMaterialsList', window.baseURL + '/documents/addsalesCreditList', window.baseURL + '/documents/addTicketsCostList'],
      documentsType,
      url,
      params = {},
      list,
      procCode,
      procCodeIdx,
      listArr = [],
      count = 0;

    if ($inputs.length === 0) {
      layer.msg('当前票据列表为空，请先设置票据', {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
      return;
    }
    switch (tabIdx) {
      case 0:
        url = urlArr[0];
        list = state.procRaw.concat();
        procCode = $('#subCode-content1').prev().children('.layui-this').attr('data-code');
        procCodeIdx = $('#subCode-content1').children('.layui-show').find('.on').index();
        break;
      case 1:
        url = urlArr[1];
        list = state.procStock.concat();
        procCode = $('#subCode-content2').prev().children('.layui-this').attr('data-code');
        procCodeIdx = $('#subCode-content2').children('.layui-show').find('.on').index();
        break;
      case 2:
        url = urlArr[2];
        documentsType = 3;
        list = state.sales.concat();
        $lis = $ticketsList.eq(tabIdx).find('li');
        var listReturn = state.salesReturn;
        var liLen = $lis.length,
          listLen = list.length;

        $.each($lis, function(index, el) {
          var $input = $(this).find('input'),
            amount = $(this).attr('data-amount') || '',
            tax = $(this).attr('data-tax') || '',
            taxType = $(this).find('input[type="radio"]:checked').val();
          i = $input.attr('data-index');

          // 判断是否免税
          if (true) {
            // tax = 0;
          }
          // 除了有金额的数据，未显示的数据也要添加进去
          if (index === 0) {
            for (var j = liLen; j < listLen; j++) {
              listArr.push(list[j]);
            }
            // for(var j = 0, length = listReturn.length; j < length; j++){
            //   listArr.push(listReturn[j]);
            // }
          }
          if (amount) {
            count++;
            list[i].documentsAmount = amount;
            // list[i].amountDebit = amount;
            list[i].documentsType = documentsType;
            if (tax) {
              list[i].taxAmountType = taxType;
              list[i].taxAmount = tax;
            }
            listArr.push(list[i]);
          }
        });
        break;
      case 3:
        url = urlArr[3];
        documentsType = 4;
        list = state.costRow.concat();
        var listReturn = state.costReturn;

        $.each($inputs, function(index, el) {
          var v = $(this).val(),
            i = Number($(this).attr('data-index'));

          // 除了有金额的数据，未显示的数据也要添加进去
          if (index === 0) {
            for (var j = 0, length = listReturn.length; j < length; j++) {
              listArr.push(listReturn[j]);
            }
          }
          if (v) {
            count++;
            list[i].amountDebit = v;
            list[i].documentsType = documentsType;
            listArr.push(list[i]);
          }
        });
        break;
      default:
        return false;
    }
    if (tabIdx === 0 || tabIdx === 1) {
      documentsType = 1;
      params.tBasicSubjectMessage = state.procCode[procCode][procCodeIdx];
      // params.tBasicSubjectMessage.taxAmount = $tabContent.find('input[name="taxAmount"]').eq(tabIdx).val();
      // if (!params.tBasicSubjectMessage.taxAmount) {
      //   layer.msg('请输入税金', {
      //     icon: 2,
      //     shade: 0.1,
      //     time: 1000
      //   });
      //   return;
      // }
      $.each($inputs, function(index, el) {
        var attrName = $(this).attr('name'),
          v = $(this).val(),
          i = $(this).parents('ul').index();

        if (v === '') {
          return true;
        }
        switch (attrName) {
          case 'unit':
            list[i].documentsUnit = $(this).attr('data-unit');
            list[i].documentsUnitId = $(this).attr('data-unitId');
            break;
          case 'number':
            list[i].documentsNumber = v;
            break;
          case 'price':
            list[i].documentsDecimal = v;
            break;
          case 'amount':
            count++;
            list[i].documentsAmount = v;
            list[i].documentsType = documentsType;
            listArr.push(list[i]);
            break;
        }
      });
      params.tBasicSubjectMessage = JSON.stringify(params.tBasicSubjectMessage);
    }
    params.tBasicSubjectMessageList = JSON.stringify(listArr);
    // 检测当前票据列表有效输入
    if (count === 0) {
      layer.msg('请输入至少一个金额', {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
      return;
    }

    postRequest(url, params, function(res) {
      if (res.code === 1) {
        var msg = '新增' + res.no + '条单据成功';
        layer.msg(res.no ? msg : '新增成功', {
          icon: 1,
          shade: 0.1,
          time: 1000
        });
        if (tabIdx === 2) {
          $('#form3 .form-item').removeAttr('data-amount')
            .removeAttr('data-tax');
        }
        _resetForm(true);
        delete state.queryDocumentsList;
      } else {
        layer.msg(res.msg || '新增失败', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }
    });
  }

  // 保存修改
  function _saveData() {
    var url = window.baseURL + '/documents/addTicketsCostList',
      v = $ticketsList.eq(state.tabIdx).find('li').not('.dn').find('input[type="text"]').val();
    console.log(v);
  }

  // 生成凭证
  function _createVoucher() {

  }

  // 查询单据录入
  function _getAccDoc(params) {
    var url = window.baseURL + '/documents/queryDocumentsList',
      params = params || {};
    postRequest(url, params, function(res) {
      state.queryDocumentsList = res.queryDocumentsList || [];
      if (res.code === 1) {
        _listHTML(res.queryDocumentsList || []);
        res.msg && layer.msg(res.msg, {
          icon: 1,
          shade: 0.1,
          time: 1000
        });
      } else {
        res.msg && layer.msg(res.msg, {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        _listHTML([]);
      }
    });

    function _listHTML(list) {
      var amount,
        str = '',
        type,
        len = list.length;

      // state.queryDocumentsList = list;
      if (len) {
        $.each(list, function(idx, el) {
          if (el.documentsType == '1') {
            type = '采购票';
          } else if (el.documentsType == '2') {
            type = '销售收入票';
          } else if (el.documentsType == '3') {
            type = '银行流水';
          } else if (el.documentsType == '4') {
            type = '费用票（现金支付）';
          } else {
            type = '';
            // return true;
          }
          amount = _formattingNumbers([el.amountCredit]);
          str += '<tr id="' + el.pkDocumentsId + '">' +
            '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>' +
            '<td>' + (++idx) + '</td>' +
            '<td>' + type + '</td>' +
            '<td class="tl">' + el.subCodeCredit + ' ' + el.subNameCredit + '</td>' +
            '<td class="tl">' + el.subCodeDebit + ' ' + el.subNameDebit + '</td>' +
            '<td class="tr">' + amount[0] + '</td>' +
            '<td>' + (el.voucherNumber || '') + '</td>' +
            '<td>' + formatDate(el.updateDate || el.createDate) + '</td>' +
            '<td class="opt">'+
            '<span class="layui-icon cu edit ' + (type ? '' : 'disabled') + '">&#xe642;</span>'+ 
//          '<span class="layui-icon cu compile">&#xe642;</span>' +
            '<span class="layui-icon cu remove">&#x1006;</span>' +
            '</td>' +
            '</tr>';
        });
      } else {
        str = '<tr class="tc"><td colspan="99" style="height: 80px;">暂无数据</td></tr>';
      }
      $docList.html(str);


      fixedHead($('#fixedDOM5'), $('#acc-doc'), $('#searchDOM5'), $fixedNav);
      refreshCheckbox();
    }
  }

  // 批量删除数据
  function _batchDel(obj) {
    var arr = [],
      checkbox = $(obj).find(':checked');
    if (checkbox.length === 0) {
      layer.msg('请选择删除数据');
      return;
    }
    layer.confirm('是否批量删除选中数据?', { icon: 0, title: '提示' }, function() {
      $.each(checkbox, function() {
        var idx = $(this).parents('tr').index();
        arr.push(idx);
      });
      _deleteData(arr);
    });
  }

  // 删除数据
  function _deleteData(arr) {
    var url = window.baseURL + '/documents/deleteDocumentsList',
      data,
      params = {},
      i = 0,
      len = arr.length;

    params.tBasicDocumentss = [];
    for (i; i < len; i++) {
      data = state.queryDocumentsList[arr[i]];
      params.tBasicDocumentss.push(data)
    }
    params.tBasicDocumentss = JSON.stringify(params.tBasicDocumentss);
    postRequest(url, params, function(res) {
      layer.closeAll('dialog'); //关闭信息框
      if (res.code === 1) {
        layer.msg(res.msg || '删除' + res.no + '条单据成功', {
          icon: 1,
          shade: 0.1,
          time: 600
        }, function() {
          var $lis = $docList.find('tr');
          for (i = 0; i < len; i++) {
            $lis.eq(arr[i]).remove();
          }
          if ($docList.find('input[name="ckbox"]').length === 0) {
            $docList.parents('.table-box').find('input[name="allChoose"]').prop('checked', false);
            refreshCheckbox();
          }
          _getAccDoc();
        });
      } else {
        layer.msg(res.msg || '删除单据失败', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }
    });
  }

  // 去修改数据页面
  function _toEdit(obj) {
    var $this = obj;
    if ($this.hasClass('disabled')) {
      return false;
    }
    var idx = $this.parents('tr').index(),
      data = state.queryDocumentsList[idx],
      tabIdx = data.documentsType - 1;
    // console.log(data);
    state.tabIdx = tabIdx;
    $tabItem.removeClass('layui-show').eq(tabIdx).addClass('edit_tab layui-show')
      .find('li').addClass('dn');
    $('#' + data.subCodeDebit).val(data.amountCredit)
      .parents('li').removeClass('dn');
  }

  // 只能输入数字并且只能是两位小数
  function _toFixedNum(elem) {
    //先把非数字的都替换掉，除了数字小数点
    elem.val(elem.val().replace(/[^\d.]/g, "")
      .replace(/^\./g, "").replace(".", "$#$").replace(/\./g, "").replace("$#$", ".") //只允许一个小数点
      // .replace(/^\-/g, "$*$").replace(/\-/g, "").replace("$*$", "-")//只允许一个负号
      .replace(/\.{2,}/g, ".") //只能输入小数点后两位
      .replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'));
    return elem.val();
  }

  // 格式化数字
  function _formattingNumbers(list) {
    var arr = [];
    for (var i in list) {
      if (list[i] === null) {
        list[i] = 0;
      }
      var srt = (Math.abs(list[i])).toFixed(2);
      arr.push(formatNum(srt));
    }
    return arr;
  }

  /*
   * 计量单位
   */
  // 获取计量单位
  function _getUnit() {
    getRequest(window.baseURL + '/measure/queryMeasureBySymbolOrName', function(res) {
      var list = res.queryExchangeList || [],
        str = '',
        title,
        i = 0,
        leng = list.length;

      if (leng > 0) {
        for (i; i < leng; i++) {
          title = list[i].measUnitSymbol + ' / ' + list[i].measUnitName;
          str += '<li class="unit-item layui-elip" data-id="' + list[i].pkMeasureId + '" data-unit="' + list[i].measUnitSymbol + '" title="' + title + '">' + list[i].measUnitSymbol + '</li>';
        }
      } else {
        str = '<li class="unit-item disabled">无数据，请前往设置添加</li>';
      }
      $('#unit-popup > ul').html(str);
    });
  }

  // 计算计量单位列表的位置
  function _unitListPosition() {
    if (!$unitInput) {
      return false;
    }
    var _winH = $(window).height(),
      _left = $unitInput.offset().left - 20,
      _top = $unitInput.offset().top + $unitInput.height() + 1;

    if (_top > _winH || _top + 5 < $unitInput.parents('div').offset().top) { // 超过显示界面则隐藏
      // $unitInput.blur();
      $unitPopup.hide();
      return;
    } else if (_top + 180 > _winH) { // 在上方显示
      _top = $unitInput.offset().top + 1 - 180;
      $unitPopup.removeClass('below').addClass('above');
    } else { // 在下方显示
      $unitPopup.removeClass('above').addClass('below');
    }
    $unitPopup.css({
      width: $unitInput.outerWidth(),
      top: _top,
      left: _left
    });
    $unitPopup.stop().slideDown(100); //显示科目列表
  }

  // 计量单位列表resize
  function _unitListResize() {
    // 和固定头部resize兼容
    $(window).on('resize', function() {
      _unitListPosition();
    });
  }

});
