/**
 ** 科目对照
 **/
$(function () {
  var layer = layui.layer,
    $contrast = $('.init_contrast'),
    $sysList = $('#sys-list'),
    $excelList = $('#excel-list'),
    $listSize = $('#list-size'),
    $searchBtn = $('#search-btn'),
    $allChoose = $('#allChoose').parent();

  pageInit();

  $searchBtn.on('click', searchSub);

  $sysList.on('click', 'span', function () {
    var $this = $(this),
      id = $this.parents('tr').attr('id');
    // 添加系统科目
    // if ($(this).hasClass('add')) {
    //   addSubPopup($(this));
    // }
    // 未匹配科目添加到系统科目
    if ($this.hasClass('lead')) {
      sysAddSubList($this);
    }
    // 删除
    else if ($this.hasClass('remove')) {
      deleteData(id);
    }

  });

  // 点击复选框
  $excelList.on('click', '.layui-form-checkbox', function () {
    checkBoxState($(this));
  });

  // 点击全选框
  $allChoose.on('click', '.layui-form-checkbox', function () {
    checkBoxState($(this));
  });


  // 页面初始化
  function pageInit() {
    getSubCont();

  }

  // 获取科目对照数据
  function getSubCont() {
    getRequest(window.baseURL + '/subinit/toKmdz', function (res) {
      if (res.code == 1) {
        showSubCont(res);
      } else {
        layer.msg('科目对照异常');
      }
    });
  }

  // 科目对照展示
  function showSubCont(res) {
    var str1 = '',
      str2 = '',
      codeLen,
      removeBtn = '',
      subTipDisplay = 'none',
      sysList = res.sysList || [],
      excelList = res.excelList || [],
      matchSize = res.matchListSize || 0;

    if (sysList.length == 0) {
      console.log('微企宝科目为空');
    }
    // 微企宝科目
    $.each(sysList, function (idx, data) {
      codeLen = data.subCode.trim().length;
      if (codeLen) {
        // if (data.codeLevel == 1) {
        if (codeLen === 4) {
          removeBtn = '<span class="dib" style="margin: 0 4px;width:20px;"></span>';
        } else {
          removeBtn = '<span class="layui-icon layui-icon-delete cu remove"></span>';
          // removeBtn = '<span class="layui-icon cu remove"><svg width="20" height="20"><image xlink:href="../img/svg/icon-del.svg" src="../img/icon-del.png" width="20" height="20"></image></svg>';
        }
        data.updateDate = formatDate(data.updateDate);
        str1 += '<tr id="' + data.pkSubId + '">' +
          '<td>' + data.subCode + '</td>' +
          '<td>' + data.subName + '</td>' +
          '<td>' +
          // '<span class="add"></span>'+
          '<span class="lead"></span>' +
          removeBtn +
          '<p class="dn">' + JSON.stringify(data) + '</p>' +
          '</td>' +
          '</tr>';
      } else {
        console.log('sysList error:', idx);
      }
    });
    // 未匹配科目
    $.each(excelList, function (idx, data) {
      if (data.subCode) {
        data.updateDate = formatDate(data.updateDate);
        if (data.hasBalance == 2) {
          subTipDisplay = 'block';
        }
        str2 += '<tr' + (data.hasBalance == 2 ? ' class="red"' : '') + ' id="' + data.pkSubExcelId + '">' +
          '<td>' +
          '<input type="checkbox" name="ckbox" lay-skin="primary">' +
          '<p class="dn">' + JSON.stringify(data) + '</p>' +
          '</td>' +
          '<td>' + data.subCode + '</td>' +
          '<td>' + data.subName + '</td>' +
          '</tr>';
      }
    });
    if (!str1) {
      str1 = '<tr class="empty"><td colspan="3">暂无数据</td></tr>';
    }
    if (!str2) {
      str2 = '<tr class="empty"><td colspan="3">科目已匹配</td></tr>';
    }
    $sysList.html(str1);
    $excelList.html(str2);
    $listSize.html(matchSize);
    $('.sub-tips').css('display', subTipDisplay);
    refreshCheckbox();
  }

  /**
   * 批量选择
   */
  // 批量选择数据
  function batchSelectData(item) {
    var arr = [],
      obj = {},
      checkbox = $(item).find('tr:visible :checked');
    $.each(checkbox, function () {
      var id = $(this).parents('tr').attr('id');
      if (id) {
        obj = JSON.parse($('#' + id).find('p').text().trim());
        arr.push(obj);
      }
    });
    return arr;
  }

  // 检测复选框状态
  function checkBoxState(item) {
    var attrName = $(item).prev().attr('name'),
      currTable = $excelList,
      $selectAll = $('#allChoose');

    if (attrName == 'ckbox') { // 单选
      if (currTable.find('.layui-form-checked').length == currTable.find('tr:visible').length) {
        $selectAll.prop('checked', true);
      } else {
        $selectAll.prop('checked', false);
      }
    } else if (attrName == 'allChoose') { // 全选
      if ($(item).hasClass('layui-form-checked')) {
        currTable.find('tr:visible input[name="ckbox"]').prop('checked', true);
      } else {
        currTable.find('tr:visible input[name="ckbox"]').prop('checked', false);
      }
    }
    refreshCheckbox();
  }

  /**
   * 数据删除
   */
  // 点击删除按钮
  /*function singleDel(item) {
    var id = $(item).parents('tr').attr('id'),
      arr = id.split(','),
      params = {
        pkSubId: id
      },
      url = window.baseURL + '/subject/deleteMessageByPrimaryKey';

    deleteData(url, params, arr);
  }*/

  // 删除系统科目
  function deleteData(id) {
    postRequest(window.baseURL + '/subject/kmdzPageDelSubByPKID', {
      pkSubId: id
    }, function (res) {
      if (res.code == '1') {
        layer.msg(res.msg || '删除科目成功', {
          icon: 1,
          time: 1000
        }, function () {
          $('#sys-list #' + id).remove();
        });
        getSubCont();
      } else {
        layer.msg(res.msg || '删除系统中的科目出错', {
          icon: 2,
          shade: 0.1
        });
      }
    });
  }

  /**
   * 添加系统科目
   */
  // 点击添加系统科目按钮
  /*function addSubPopup(item) {
    var id = $(item).parents('tr').attr('id'),
      url = window.baseURL + '/subject/addSubMessageList',
      params = {
        List: id
      };

    alert(id);
    // postRequest( url, params, addSubMessCB );
    // addSubMessage( url, params );
  }

  // 添加系统科目回调
  function addSubMessCB(res) {
    var message = '删除系统中的科目出错';
    if (res.message == 'success') {
      layer.msg('删除系统中的科目成功', {
        icon: 1,
        time: 1000
      }, function () {
        // layer.close(idx);
        $.each(arr, function (i, val) {
          $('#' + val).remove();
        });
        getSubCont();
      });
    } else {
      layer.msg(message);
    }
  }*/

  /**
   * 未匹配科目添加到系统科目
   */
  // 获取选中系统科目data，被选中未匹配科目data
  function sysAddSubList(item) {
    var arr = batchSelectData($excelList);
    // 检测被选中未匹配科目是否为空
    if (arr.length === 0) {
      layer.msg('请选择未匹配科目');
      return;
    }
    var params = {
        subMessage: $(item).parent().find('p').text().trim(),
        subExcels: JSON.stringify(arr)
      },
      url = window.baseURL + '/subinit/toKmdzAddSubMessageList';

    postRequest(url, params, function (res) {
      if (res.code == '1') {
        layer.msg(res.msg || '添加系统科目集合成功', {
          icon: 1,
          time: 1000
        }, function () {
          $.each(arr, function (i, data) {
            $('#excel-list #' + data.pkSubExcelId).remove();
          });
        });
        getSubCont();
      } else {
        layer.msg(res.msg || '添加系统科目集合出错');
      }
    });
  }

  /**
   * 本地搜索列表
   */
  function searchSub() {
    var val = $('#keyWord').val();
    var $trs = $sysList.find('tr'),
      len = $trs.length,
      $td, subCode, subName, flag1, flag2;
    $trs.removeClass('on');
    if (!val) {
      layer.msg('请输入编码或名称', {
        time: 1000,
        shade: 0.03
      });
      // 未匹配科目全部显示
      $excelList.find('tr').show();
      return;
    }
    // 模糊查询
    if ($listSize.text() > 0) {
      $excelList.find('td:not( :contains(' + val + ') )').parent().hide();
      $excelList.find('td:contains(' + val + ')').parent().show();
    }
    // 定位微企宝科目tr
    for (var i = 0; i < len; i++) {
      $td = $trs.eq(i).find('td');
      subCode = $td.eq(0).text();
      subName = $td.eq(1).text();
      flag1 = subCode.indexOf(val);
      flag2 = subName.indexOf(val);
      if (flag1 == 0 || flag2 == 0) {
        $trs.eq(i).addClass('on');
        $sysList.parent().parent().animate({
          scrollTop: $trs[i].offsetTop
        }, 400);
        break;
      }
    }
  }
});
