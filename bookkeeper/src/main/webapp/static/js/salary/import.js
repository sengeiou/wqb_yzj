/**
** 工资导入
**/
;(function () {
  // 显示权限按钮
  var show = getPurviewData(300),
    isCarryState = _queryStatus()[1];
  if (show === 'block') {
    $('.search').css('display', show);
    $('#content').css('padding-top', '15px');
  }
//show = null;
  delete session.userType;
  delete session.isTestUser;
  if (isCarryState === 1) {
    $('#popup-btn').hide();
    $('#batch-del').hide();
    layer.alert('您已经一键结转，不能修改数据。<br>如需修改，请前往生成凭证页面反结转。', {
      icon: 0
    });
  }

  $(function() {
    var layer = layui.layer,
      currPage = 1,
      $keyWord = $('#keyWord'),
      $selectDate = $('#select-date'),
      $uploadPopup = $('#upload-popup'),
      $uploadInput = $('#upload-input'),
      $uploadBtn = $('#upload-btn'),
      $table = $('table'),
      $tableList = $('#salary-list'),
      $searchBtn = $('#search-btn'),
      $popupBtn = $('#popup-btn'),
      $batchBtn = $('#batch-del');

    pageInit();

    // laydate选择日期
    layui.laydate.render({
      elem: '#acperiod'
      ,btns: ['now', 'confirm']
      ,type: 'month'
      ,format: 'yyyy-MM'
      // ,value: session.busDate
      ,max: 0
      ,theme: '#1E9FFF'
    });

    // 点击搜索
    $searchBtn.on('click', function() {
      multiSearch();
    });

    // 打开导入Excel弹窗
    $popupBtn.on('click', showUploadPopup);

    $uploadInput.on( 'change', function() {
      var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
      $(this).prev().text(v);
      $uploadBtn.removeClass( 'loading loaded' );
    });

    // 上传Excel
    $uploadBtn.on('click', function() {
      var that = this;

      uploadInit( that, window.baseURL + '/arch/uploadArch', function(res) {
        if ( res.message == 'success' ) {
          $uploadBtn.addClass('loaded');
          layer.msg( res.result, {
            icon: 1,
            shade: 0.1,
            time: 1000
          }, function() {
            layer.closeAll('page');
            $uploadPopup.hide();
            pageInit();
          });
        } else {
          layer.msg( res.result, {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000000
          });
        }
      });
    });

    $table
      // 点击复选框
      .on('click', '.layui-form-checkbox', function() {
        checkBoxState($(this));
      })
      // 单条数据删除
      .on('click', 'span.remove', function() {
        var id = $(this).parents('tr').attr('id'),
          arr = id.split(','),
          params = {
            archID: id
          };

        if(!id){
          layer.msg('本条数据ID不存在');
          return false;
        }
        deleteData(window.baseURL + '/arch/del', params, arr);
      });

    // 批量数据删除
    $batchBtn.on('click', batchDel);

    // 获取焦点， 输入框内容为空
    removeInputVal($('#keyWord'));

    // 页面初始化
    function pageInit() {
      // 加载进项发票首页
      getDateList();
      $keyWord.val('');
      $selectDate.val('-1');
      layui.form.render('select');
      multiSearch();
    }


    // 查询工资发放月份列表
    function getDateList() {
      getRequest(window.baseURL + '/arch/queryArchDate', function(res) {
        if (res.message == 'success') {
            var list = res.result || '',
                arr = list.split('&')
                str = '<option value="-1">工资发放月份</option>';

          if ( list ) {
            $.each(arr, function (idx, el) {
              str += '<option value="' + el + '">' + el + '</option>';
            });
          }
          $selectDate.html(str);
          layui.form.render('select');
        } else {
          layer.msg('查询工资发放月份列表失败');
        }
      });
    }

    /**
     * 查询工资
     */
    function multiSearch( page ) {
      var params = {
        currPage: page || 1,
        acName: $keyWord.val().trim(),
        archDate: $selectDate.val() == '-1' ? '' : $selectDate.val() //工资发放月份
      };

      currPage = params.currPage;
      postRequest(window.baseURL + '/arch/list', params, function(res) {
        var data = res.result;
        if (res.message == 'success') {
          var ele = 'layui-table-page1',
              list = data.content || [],
              total = data.recordTotal,
              limit = data.pageSize;

          _listHTML(list, 'salary-list');
          flushPage(ele, currPage, total, limit, multiSearch);
          if (show === 'block') {
				    $('span.remove').show();
				  }else{
				    $('span.remove').hide();
				  }
        } else {
          layer.msg('分页查询工资异常');
        }
      });

      function _listHTML(list, ele) {
        var str = '',
            arr,
            oSum,
            $listTable = $('#' + ele);

        if (list.length) {
          var permission = isCarryState === 1 ? '' : '<span class="layui-icon cu red remove">&#x1006;</span>';

          $.each(list, function(i, el) {
            oSum = {
              0: el.basePay,
              1: el.subsidy,
              2: el.overtimeFree,
              3: el.overtimeWeekend,
              4: el.otherFree,
              5: el.payAble,
              6: el.socialfree,
              7: el.provident,
              8: el.deduction + el.utilities,
              9: el.totalCharged,
              10: el.taxFree,
              11: el.realwages
            };
            arr = initNumber(oSum);
            str += '<tr id="' + el.archID + '">'
                + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
                + '<td>' + (++i) + '</td>'
                + '<td>' + el.acDepartment + '</td>'
                + '<td>' + el.acCode + '</td>'
                + '<td>' + el.acName + '</td>'
                + '<td>' + el.archDate + '</td>'
                + '<td>' + el.attendanceDays + '</td>'
                + '<td>' + el.attendanceActual + '</td>'
                + '<td>' + arr[0] + '</td>'
                + '<td>' + arr[1] + '</td>'
                + '<td>' + arr[2] + '</td>'
                + '<td>' + arr[3] + '</td>'
                + '<td>' + arr[4] + '</td>'
                + '<td>' + arr[5] + '</td>'
                + '<td>' + arr[6] + '</td>'
                + '<td>' + arr[7] + '</td>'
                + '<td>' + arr[8] + '</td>'
                + '<td>' + arr[9] + '</td>'
                + '<td>' + arr[10] + '</td>'
                + '<td>' + arr[11] + '</td>'
                + '<td class="opt">' + permission + '</td>'
                + '</tr>';
          });
        } else {
          str = '<tr class="tc"><td colspan="21" style="height: 80px;">暂无数据</td></tr>'
        }
        $listTable.html( str );
        fixedHead($('.fixed-head'), $('#table'), $('.fixed-search'), $('#navDOM1'));
        refreshCheckbox();
      }
    }

    // 格式化数字
    function initNumber(list) {
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


    /**
     * 导入Excel
     */
    // 打开导入Excel弹窗
    function showUploadPopup() {
      $uploadInput.val('').prev().text('请选择97格式.xls...');
      layer.open({
        type: 1
        ,title: '导入工资97格式Excel'
        // ,area: [width, '80%']
        ,content: $uploadPopup
        ,cancel: function() {
          $uploadPopup.hide();
        }
      });
    }

    /**
     * 批量选择
     */
    // 批量选择数据
    function batchSelect(obj) {
      var arr = [],
          checkbox = $(obj).find(':checked');
      $.each(checkbox, function () {
        var id = $(this).parents('tr').attr('id');
        if (id) {
          arr.push(id);
        }
      });
      return arr.toString();
    }

    /**
     * 数据删除
     */
    // 批量删除
    function batchDel() {
      var id = batchSelect($table),
          arr = id.split(','),
          params = {
            ids: id
          },
          url = window.baseURL + '/arch/delAll';

      console.log(id);
      if(!id){
        layer.msg('请选择删除数据');
        return false;
      }
      deleteData(url, params, arr);
    }

    // 数据删除
    function deleteData(url, params, arr) {
      postRequest(url, params, function(res) {
        var message = res.result;
        if (res.message == 'success') {
          layer.msg( message, {
            icon: 1,
            shade: 0.1,
            time: 600
          }, function(idx) {
            var $tableBox = $tableList.parents('.table-box');
            $.each(arr, function(idx, val) {
              $('#' + val).remove();
            });
            $tableBox.find('input[name="allChoose"]').prop('checked', false);
            refreshCheckbox();
            if (!$tableList.find('tr').length && $tableBox.find('.layui-laypage-next.layui-disabled').length) {
              var $skipInput = $tableBox.find('.layui-laypage-skip input'),
                page = $skipInput.val() - 1;
              page && $skipInput.val(page);
            }
            $tableBox.find('button.layui-laypage-btn').click();
          });
        } else {
          layer.msg( message );
        }
      });
    }
  });

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
          	shadeClose: true,
            time: 2000000
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
