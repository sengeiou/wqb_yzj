/**
 ** 生成凭证、结转、审核、凭证检查、结账
 **/
;
(function () {
  // 显示权限按钮
  var show = getPurviewData(600),
    $createBtns = $('#search1 .search-oneKey > div'),
    btnStatus,
    //vchrStatusArr = [0, 0, 0, 0, 0],
    vchrStatus = _queryStatus(),
    $getHistoryBill = $('#getHistoryBill-list'); // 查看凭证字号

  if (show === 'block') {
    $('.search').css('display', show);
    $('#remove-all').css('display', show);
    $('#create-table').parent().removeClass('noSearch');
  } else {
    $('#create-table').parent().addClass('noSearch');
  }
  // 流程图权限
  $.each($createBtns, function (index) {
    btnStatus = getPurviewData(601 + index) === 'block' ? 'inline-block' : 'none';
    $(this).css('display', btnStatus)
      .prev('img').css('display', btnStatus);
  });
  btnStatus = null;
  show = null;

  $(function () {
    var currPage = 1,
      currPage2 = 1,
      state = {},
      $voucherBtn = $('#voucher-btn'), //一键生成凭证
      $carryBtn = $('#carry-btn'), //一键结转
      $reviewBtn = $('#review-btn'), //一键审核
      $voucherAudit = $('#voucher-audit'), //凭证检查
      $settleAccoBtn = $('#settle-accounts'), //一键结账
      $exportBtns = $('.export_btn'),
      $printBtn = $('#print-btn'),
      $searchBtn = $('#search-btn'),
      $removeAll = $('#remove-all'),
      $searchBtn2 = $('#search-btn2'),
      $createVoucher = $('#create-table'),
      $reviseVoucher = $('#revise-table'),
      $printPopup = $('#print-popup'),
      $printBegin = $('#print-begin'),
      $printEnd = $('#print-end'),
      editVouchTab = window.sessionStorage.getItem('editVouchTab'),
      _splitDate = window.session.busDate.split('-'),
      _vcDay = window.session.busDate + '-' + new Date(_splitDate[0], _splitDate[1], 0).getDate(); // 凭证日期

    // 页面初始化
    showProgress();

    state.tabIndex = $('#navDOM1').children('.layui-this').index();
    _setTabData(state.tabIndex);
    if (state.tabIndex === 0) {
      multiSearch2();
    }

    // 监听选项卡切换
    layui.element.on('tab(tab)', function (data) {
      state.tabIndex = data.index;
      _setTabData(state.tabIndex); //点击请求数据
    });

    // 监听凭证打印弹窗
    layui.form.on('radio(printCountType)', function (data) {
      var type = data.value;
      state.printAll = type;
      switch (type) {
      case '0':
        $printPopup.find('.layui-input').addClass('layui-disabled').prop('disabled', true);
        break;
      case '1':
        $printPopup.find('.layui-input').removeClass('layui-disabled').prop('disabled', false);
        break;
      }
    });

    // 操作流程按钮 start
    $voucherBtn.on('click', createVoucher);

    $carryBtn.on('click', carryForward);

    $reviewBtn.on('click', function () {
      oneKeyAudit();
    });

    $voucherAudit.on('click', voucherAuditS);

    $settleAccoBtn.on('click', settleAccounts);
    // 操作流程按钮 end

    // 点击一键删除
    $removeAll.on('click', _removeAll);

    // 点击打印按钮
    $printBtn.on('click', function () {
      // var index = $(this).index();
      // 凭证打印前检测是否能打印
      if (state.revise > 0) {
        layer.msg('打印全部凭证前，请先修正凭证', {
          icon: 0,
          shade: 0.5,
          time: 1000
        }, function () {
          layui.element.tabChange('tab', 1);
        });
        return;
      } else if (state.create == 0) {
        layer.msg('当前没有凭证可打印', {
          icon: 0,
          shade: 0.1,
          time: 1000
        });
        return;
      }

      layer.open({
        type: 1,
        title: '凭证打印',
        area: ['500px', '250px'],
        content: $printPopup,
        btn: ['确定', '取消'],
        resize: false,
        success: function () {
          $printPopup.find('input[name="printCountType"]').eq(0).next().click();
          $printPopup.find('input[type="text"]').val('');
          _getPrintRange();
        },
        yes: function (index) {
          switch (state.printAll) {
          case '0':
            _printAllVoucher(state.printAll);
            layer.close(index);
            $printPopup.hide();
            break;
          case '1':
            var begin = Number($printBegin.val()),
              end = Number($printEnd.val());
            if (begin && end && begin <= end && begin <= state.vcRange[1] && end <= state.vcRange[1]) {
              _printAllVoucher(state.printAll, begin, end);
              // layer.close(index);
              // $printPopup.hide();
            } else {
              layer.msg('请选择有效的打印范围: (' + state.vcRange[0] + ',' + state.vcRange[1] + ')', {
                icon: 0,
                shade: 0.3,
                time: 2000
              });
            }
            break;
          }
        },
        btn2: function () {
          $printPopup.hide();
        },
        cancel: function () {
          $printPopup.hide();
        }
      });
    });

    // 限制输入正整数
    $printPopup.on('input', '.layui-input', function () {
      var val = $(this).val().replace(/\D/g, '');
      // if (val.length) {
      //   val = parseInt(val);
      // }
      // if (0 === val) {
      //   val = '';
      // }
      if (0 < parseInt(val)) {
        // statement
      } else {
        val = '';
      }
      $(this).val(val);
    });
    /*$exportBtns.on('click', function () {
      var index = $(this).index();
      layer.confirm('是否要导出全部凭证？')
    });*/

    // 生成凭证列表
    $createVoucher
      //复制粘贴
      .on('click', 'span.copy', function () {
        var id = $(this).parents('tr').attr('id'),
          params = {
            vouchID: id
          };
        postRequest(window.baseURL + '/voucher/copyVoucher', params, function (res) {
          if (res.code === '0') {
            top._subListRefresh(res.data); // 修改科目列表期末余额
            multiSearch();
            layer.msg('复制粘贴凭证成功!', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
          } else {
            layer.msg(res.msg || '复制粘贴凭证异常!', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 20000000
            });
          }
        })
      })
      // 编辑凭证
      .on('click', 'span.edit', function () {
        var id = $(this).parents('tr').attr('id'),
          auditStatus = $(this).parent().prev().text();
        window.sessionStorage.setItem('editVouchTab', state.tabIndex);
        window.sessionStorage.setItem('editVouch', JSON.stringify({
          'id': id,
          'isHasData': true,
          'isEditor': auditStatus === '已审核' ? false : true
        }));
        // 点击切换标签页，重新加载iframe的URL
        var layId = window._currTabTimeStamp();
        top.$('#compilePZ').click();
        var _iframe = top.$('#top_tabs_box .clildFrame > .layui-show iframe')[0];
        _iframe.src = window.baseURL + "/voucher/editView?source=1&from=createList&layId=" + layId;
        // _iframe.contentWindow.location.href = window.baseURL + "/voucher/editView?source=1";
      })
      // 单条数据删除
      .on('click', 'span.remove', function () {
        //        if (vchrStatus.isAudited === 1) {
        //          layer.msg('已审核，不能删除凭证', {
        //            icon: 2,
        //            shade: 0.1,
        //            shadeClose: true,
        //            time: 2000000
        //          });
        //          return false;
        //        }
        // var id = $(this).parents('tr').attr('id');
        deleteData($(this).parents('tr'));
      })
      // 打印
      .on('click', 'span.print', function () {

        var that = $(this),
          $tr = that.parents('tr'),
          error = $tr.attr('data-error');
        if (error === '1') {
          layer.msg('请先修正凭证', {
            icon: 2,
            shade: 0.5,
            shadeClose: true,
            time: 2000000
          }, function () {
            that.siblings('.edit').click();
          });
        } else {
          that.vouchID = $tr.attr('id');
          postRequest(window.baseURL + '/voucher/toEditVoucher', { 'vouchID': that.vouchID }, function (res) {
            if (res.code === '0') {
              var compName = top.$('#choose-account .selectCus').text().trim(),
                list = [res.voucher],
                iframeWin;
              layer.open({
                type: 2,
                title: '凭证打印预览',
                area: ['800px', '80%'],
                maxmin: true,
                content: window.baseURL + '/voucher/printPreview',
                btn: ['打印', '取消'],
                success: function (layero) {
                  layer.closeAll('dialog');
                  iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象
                  iframeWin._innerHtml(list, compName);
                },
                yes: function () {
                  iframeWin._printPreview();
                }
              });
            } else {
              layer.msg(res.msg || '获取凭证信息异常', {
                icon: 2,
                shadeClose: true,
                time: 2000000
              });
            }
          });
        }
      })
      //上传附件弹出框
      .on('click', 'span.uploadAccessory', function () {
        var that = this,
          $tr = $(this).parents('tr'),
          id = $tr.attr('id');
        layer.open({
          type: 1,
          title: '上传附件' //不显示标题栏
            ,
          area: ['380px', '220px'],
          shade: 0.4,
          id: 'uploadAccessory' //设定一个id，防止重复弹出
            ,
          btn: ['查看附件', '确定'],
          moveType: 1 //拖拽模式，0或者1
            ,
          content: '<form id="uploading1" style="margin: 28px 0px 0 42px;" class="uploading" method="post" enctype="multipart/form-data">' +
            '<label style="float: left;display: block;width: 220px;">' +
            '<a href="javascript:void(0);">' +
            '<input type="hidden" name="vouchID" value=""/>' +
            '<span style="width:54%" class="span">请选择png/jpg图片格式</span>' +
            '<input class="voucherFile" id="file" type="file" name="attachFile" placeholder="请选择png/jpgtu图片格式"/>' +
            '</a>' +
            '</label>' +
            '<span  id="' + id + '" class="fujian layui-btn layui-btn-normal mr50">上传附件</span>' +
            '</form>',
          yes: function () {
            $("#" + id).children("td").children(".accessory").click();
          },
          btn2: function () {
            $("#i-refresh").click();
          },
          cancel: function () {
            $("#i-refresh").click();
          }
        });
      })
      //查看附件
      .on('click', 'span.accessory', function () {
        var that = $(this),
          $tr = $(this).parents('tr'),
          error = $tr.attr('data-error');
        //      if (error === '1') {
        //        layer.msg('请先修正凭证', {
        //          icon: 2,
        //          shade: 0.5,
        //          shadeClose: true,
        //          time: 2000000
        //        }, function() {
        //          that.siblings('.edit').click();
        //        });
        //      } else {
        that.vouchID = $tr.attr('id');
        postRequest(window.baseURL + '/voucher/queryAttach', { 'vouchID': that.vouchID }, function (res) {
          var compName = top.$('#choose-account .selectCus').text().trim();
          var str = '';
          if (res.success === 'true') {
            for (var i = 0; i < res.list.length; i++) {
              str += '<tr id="' + res.list[i].id + '">' +
                '<td style="text-align: center;height: 135px"><img style="width:1120px;" src="' + res.url + res.list[i].attachUrl + res.list[i].attachName + '"></td>' +
                '<td style="text-align:center;cursor:pointer;"><a id="delAttach" class="layui-icon" style="font-size:26px;" data-code="' + res.list[i].id + '" title="删除">&#x1006</a></td>' +
                '</tr>';
            }
            //显示附件
            layer.open({
              type: 1,
              title: '查看附件' //不显示标题栏
                ,
              area: ['1280px', '720px'],
              shade: 0.4,
              maxmin: true,
              id: 'LAY_layuipro' //设定一个id，防止重复弹出
                ,
              btn: ['确定', '取消'],
              moveType: 1 //拖拽模式，0或者1
                ,
              content: '<div class="accessory">' +
                '<table><tr><td style="width: 90%;height:40px;padding-left: 12px;">附件&nbsp;&nbsp;(共：<span id="listLength">' + res.list.length + '</span>个附件)</td><td style="text-align: center">操作</td></tr>' +
                str +
                '</table>' +
                '</div>',
              yes: function () {
                $("#i-refresh").click();
              },
              btn2: function () {
                $("#i-refresh").click();
              },
              cancel: function () {
                $("#i-refresh").click();
              }
            });
          } else {
            layer.msg(res.message, {
              icon: 2,
              shadeClose: true,
              time: 2000000
            });
          }
        });
        //        }
      });

    // 修正凭证列表
    $reviseVoucher
      //复制粘贴
      .on('click', 'span.copy', function () {
        var id = $(this).parents('tr').attr('id'),
          params = {
            vouchID: id
          };
        postRequest(window.baseURL + '/voucher/copyVoucher', params, function (res) {
          if (res.code === '0') {
            top._subListRefresh(res.data); // 修改科目列表期末余额
            multiSearch();
            layer.msg('复制粘贴凭证成功!', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
          } else {
            layer.msg(res.msg || '复制粘贴凭证异常!', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 20000000
            });
          }
        })
      })
      // 编辑凭证
      .on('click', 'span.edit', function () {
        var id = $(this).parents('tr').attr('id');
        window.sessionStorage.setItem('editVouchTab', state.tabIndex);
        window.sessionStorage.setItem('editVouch', JSON.stringify({
          'id': id,
          'isHasData': true,
          'isEditor': true
        }));
        // 点击切换标签页，重新加载iframe的URL
        var layId = window._currTabTimeStamp();
        top.$('#compilePZ').click();
        var _iframe = top.$('#top_tabs_box .clildFrame > .layui-show iframe')[0];
        _iframe.src = window.baseURL + "/voucher/editView?source=2&from=createError&layId=" + layId;
        // _iframe.contentWindow.location.href = window.baseURL + "/voucher/editView?source=2";
      })
      // 单条数据删除
      .on('click', 'span.remove', function () {
        //        if (vchrStatus.isAudited === 1) {
        //          layer.msg('已审核，不能删除凭证', {
        //            icon: 2,
        //            shade: 0.1,
        //            shadeClose: true,
        //            time: 2000000
        //          });
        //          return false;
        //        }
        // var id = $(this).parents('tr').attr('id');
        deleteData($(this).parents('tr'));
      });

    // 获取焦点， 输入框内容为空
    removeInputVal($('#keyWord'));

    $("body").delegate("#delAttach", "click", function () {
      var attachID = $(this).attr('data-code');
      postRequest(window.baseURL + '/voucher/delAttach', { 'attachID': attachID }, function (res) {
        if (res.success === 'true') {
          layer.msg('删除成功', {
            icon: 1,
            shade: 0.1,
            time: 1000
          });
          $('#' + attachID).remove();
          var shuliang = $(".accessory tr").length - 1;
          $("#listLength").text(shuliang);
        } else {
          layer.msg('删除失败', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000000
          });
        }
      });
    });




    // 数据接口调用请求
    function _setTabData(index) {
      switch (index) {
      case 0:
        if (state.awaitUpdate === 0) {
          multiSearch();
        } else if (state.create >= 0) {
          _tableScroll('voucher-list');
          // return false;
        } else {
          multiSearch();
        }
        break;
      case 1:
        if (state.awaitUpdate === 1) {
          multiSearch2();
        } else if (state.revise >= 0) {
          _tableScroll('voucher-list2');
          // return false;
        } else {
          multiSearch2();
        }
        break;
      default:
        layui.element.tabChange('tab', 0);
      }
    }

    /*
     *凭证查询
     */
    // 不定项多条件查询生成凭证(分页查询)
    // 点击查询按钮/点击分页请求数据
    function multiSearch(page) {
      var url = window.baseURL + '/voucher/queryAllVoucher',
        params = {
          curPage: page || 1,
          source: 10
          // ,keyWord: $keyWord.val().trim()
          // ,beginTime: $beginTime.val().trim()
          // ,endTime: $endTime.val().trim()
        };

      currPage = params.curPage;
      postRequest(url, params, function (res) {
        var list = res.list || [];
        if (state.awaitUpdate === 0) {
          delete state.awaitUpdate;
        }
        if (res.success == 'true') {
          var ele = 'layui-table-page1',
            total = res.totalCount,
            limit = res.maxPage;

          $('#voucher-list').attr('data-length', total);
          // vchrStatus.isAudited = 0;
          tableListHTML(list, 'voucher-list');
          flushPage(ele, currPage, total, limit, multiSearch);
        } else {
          layer.msg(res.message || '凭证查询异常');
        }
      });

      // 列表innerHTML
      function tableListHTML(list, ele) {
        var errorCount = 0,
          str = '',
          str2 = vchrStatus.isCarryState == 1 ? '已结转' : '未结转',
          str3 = '',
          str4 = '',
          rows = 1,
          len = list.length,
          headList = '',
          bodyList = '',
          $listTable = $('#' + ele),
          $tableBody = $listTable.parents('.layui-table-body'),
          $tableHeader = $tableBody.prev('.layui-table-header');
        state.create = len;
        if (len) {
          var permissionStr = '<span class="layui-icon cu edit" title="查看/修改">&#xe642;</span><span class="layui-icon cu copy" title="复制粘贴">&#xe630;</span><span class="layui-icon cu remove" title="删除">&#x1006;</span><span class="layui-icon cu print" title="打印">&#xe63c;</span><span class="layui-icon cu uploadAccessory" title="上传附件">&#xe681;</span>',
            permissionNo = getPurviewData(600) ? permissionStr : '',
            permission = getPurviewData(600) ? permissionStr + '<span class="layui-icon cu accessory" title="查看附件">&#xe64a;</span>' : '';

          $.each(list, function (idx, el) {
            headList = el.voucherHead;
            bodyList = el.voucherBodyList;
            rows = bodyList.length;
            if (headList.source == 0 || headList.source == 9) {
              str4 = '<a class="CKvoucherNO" data-source="' + headList.source + '">记-' + headList.voucherNO + '</a>'
            } else {
              str4 = '<span>记-' + headList.voucherNO + '</span>'
            }
            if (rows == 0) {
              str += '<tr id="' + headList.vouchID + '" data-error="' + headList.isproblem + '">' +
                '<td>' + _vcDay + '</td>' +
                // '<td>' + formatDate(headList.vcDate, 'yyyy-MM-dd') + '</td>' +
                '<td>' + str4 + '</td>' +
                '<td colspan="6">数据为空</td>' +
                '<td class="opt"><span class="layui-icon cu remove">&#x1006;</span>' +
                '</tr>';
              return true;
            }
            if (headList.isproblem === '1') {
              errorCount++;
            }
            //str3 = headList.auditStatus == 1 ? '已审核' : '未审核';
            if (headList.attachID == "" || headList.attachID == null) {
              str += '<tr id="' + headList.vouchID + '" data-error="' + headList.isproblem + '">' +
                '<td>' + _vcDay + '</td>' +
                // '<td>' + formatDate(headList.vcDate, 'yyyy-MM-dd') + '</td>' +
                '<td>' + str4 + '</td>' +
                _vchrBodyList(bodyList) +
                '<td>' + str2 + '</td>' +
                //'<td>' + str3 + '</td>' +
                '<td class="opt">' + permissionNo + '</td>' +
                '</tr>';
            } else {
              str += '<tr id="' + headList.vouchID + '" data-error="' + headList.isproblem + '">' +
                '<td>' + _vcDay + '</td>' +
                // '<td>' + formatDate(headList.vcDate, 'yyyy-MM-dd') + '</td>' +
                '<td>' + str4 + '</td>' +
                _vchrBodyList(bodyList) +
                '<td>' + str2 + '</td>' +
                //'<td>' + str3 + '</td>' +
                '<td class="opt">' + permission + '</td>' +
                '</tr>';
            }

          });
        } else {
          str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无凭证数据</td></tr>';
        }
        $listTable.html(str).attr({
          'data-length': list.length,
          'data-error': errorCount
        });
        _tableScroll(ele);
        //结转、检查、结账后不可删除和修改
        if (vchrStatus.isCarryState === 1 || vchrStatus.isPzjc === 1 || vchrStatus.isJz === 1) {
          $('.remove,.copy').hide();
        }
      }
    }

    // 不定项多条件查询异常的生成凭证(分页查询)
    function multiSearch2(page) {
      var params = {
        curPage: page || 1
        // ,source: 10
        // ,keyWord: $keyWord.val().trim()
        // ,beginTime: $beginTime.val().trim()
        // ,endTime: $endTime.val().trim()
      };

      currPage2 = params.curPage;
      postRequest(window.baseURL + '/voucher/queryRevisedVoucher', params, function (res) {
        var list = res.list || [];
        // 跳转修正凭证
        if (state.awaitUpdate !== 1 && list.length >= 1) {
          // $('#navDOM1 li:eq(1)').click();
          layui.element.tabChange('tab', 1);
        }
        if (state.awaitUpdate === 1) {
          delete state.awaitUpdate;
        }
        if (res.success == 'true') {
          var ele = 'layui-table-page2',
            total = res.totalCount || 0,
            limit = res.maxPage,
            len = $('#voucher-list2').attr('data-length');

          // res.message && layer.msg(res.message);
          if (len > 0 && len > total) {
            delete state.create;
          }
          $('#voucher-list2').attr('data-length', total);
          tableListHTML(list, 'voucher-list2');
          flushPage(ele, currPage2, total, limit, multiSearch2);
        } else {
          layer.msg('分页查询待修正凭证异常');
        }
      }, function (res) {
        if (!res.list) {
          var ele = 'layui-table-page2',
            total = 0,
            limit = 10;

          $('#voucher-list2').attr('data-length', total);
          tableListHTML([], 'voucher-list2');
          flushPage(ele, currPage2, total, limit, multiSearch2);
        }
      }, false);

      // 列表innerHTML
      function tableListHTML(list, ele) {
        var str = '',
          str2 = vchrStatus.isCarryState == 1 ? '已结转' : '未结转',
          str3 = '',
          rows = 1,
          len = list.length,
          headList = '',
          bodyList = '',
          $listTable = $('#' + ele),
          $tableBody = $listTable.parents('.layui-table-body'),
          $tableHeader = $tableBody.prev('.layui-table-header');

        state.revise = len;
        if (len) {
          var permission = getPurviewData(600) ? '<span class="layui-icon cu edit">&#xe642;</span><span class="layui-icon cu remove">&#x1006;</span>' : '';

          $.each(list, function (idx, el) {
            headList = el.voucherHead;
            bodyList = el.voucherBodyList;
            rows = bodyList.length;
            if (rows == 0) {
              str += '';
              return true;
            }
            str += '<tr id="' + headList.vouchID + '">' +
              '<td>' + _vcDay + '</td>' +
              // '<td>' + formatDate(headList.vcDate, 'yyyy-MM-dd') + '</td>' +
              '<td><a class="CKvoucherNO">记-' + headList.voucherNO + '</a>' +
              (headList.des ? ('<div style="position: absolute;width: auto;margin: -39px 0 0 0;color: #fff;background: #c71818;font-size: 12px;padding: 0px 3px;">' + headList.des + '</div></td>') : '') +
              _vchrBodyList(bodyList) +
              '<td>' + str2 + '</td>' +
              '<td class="opt">' + permission + '</td>' +
              '</tr>';
          });
        } else {
          str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无凭证数据</td></tr>';
        }
        $listTable.html(str);
        if (state.tabIndex == 1) {
          _tableScroll(ele);
        }
      }
    }

    // 遍历凭证数据
    function _vchrBodyList(bodyList) {
      var str = '<td class="tl">' + _pList('vcabstact') + '</td>' +
        '<td class="tl">' + _pList('subjectID,vcsubject') + '</td>' +
        '<td class="tr">' + _pList('debitAmount', true) + '</td>' +
        '<td class="tr">' + _pList('creditAmount', true) + '</td>';
      return str;

      function _pList(value, format) {
        var arr = value.split(','),
          len = bodyList.length,
          list,
          str = '',
          str1;

        for (var i = 0; i < len; i++) {
          list = bodyList[i];
          // 修改凭证：修改的科目金额为0时，不需要显示在修改凭证中
          //          if (!list.debitAmount && !list.creditAmount) {
          //            console.warn('修改的科目金额为0');
          //            continue;
          //          }
          switch (arr.length) {
          case 1:
            str1 = list[value] || '';
            if (format && str1) {
              str1 = str1.toFixed(2);
              str1 = formatNum(str1);
            }
            break;
          case 2:
            str1 = list[arr[0]] + ' ' + list[arr[1]];
            if (list.isproblem === '1') {
              str += '<div class="error-tips">' + list.des + '</div>';
            }
            break;
          }
          str += '<p' + (i + 1 === len ? ' class="last"' : '') + '>' + str1 + '</p>';
        }
        return str;
      }
    }

    // 一键生成凭证
    function createVoucher() {
        layer.alert('该功能不可用');
        return;
      if (vchrStatus.isCarryState === 1) {
        layer.msg('已经结转，不可操作', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      } else if (vchrStatus.isCreateVoucher === 1) {
        layer.msg('已经生成凭证，可以一键删除后重新生成', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }

      layer.confirm('是否一键生成凭证？', {
        icon: 0,
        title: '生成凭证'
      }, function () {
        // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
        layer.msg('检查是否正在生成凭证，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        //      postRequest('', {}, function(res) {
        //              if(res.code == ""){
        //                layer.confirm('请先完善银行流水与科目映射', {
        //            btn: ['确定','取消'] //按钮
        //          }, function(index){
        //            bankMapping()
        //            layer.close(index)
        //            return false;
        //          }, function(){
        //            return false;
        //          });
        //              }
        //            })
        postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
          if (res.success === 'true') {
            if (res.progress.cv == 0) {
              layer.msg('正在生成凭证中，请稍候~', {
                icon: 16,
                shade: 0.3,
                time: -1
              });
              postRequest(window.baseURL + '/voucher/createVoucher', {}, function (res) {
                vchrStatus = _queryStatus();
                showProgress();
                multiSearch2();
                if (res.code == '0') {
                  top.subListRefresh = true; // 末级科目列表subListData刷新
                  window._topTabsRefresh(['进项发票导入', '销项发票导入', '银行对账单导入', '工资表导入', '费用票录入', '固定资产导入', '新增凭证', '编辑凭证', '总账', '明细帐', '数量金额总帐', '科目余额表', '凭证汇总表']);
                  // _awaitUpdateVcNo(); // 新增凭证页面重新获取凭证号
                  layer.msg(res.info || '一键生成凭证成功', {
                    icon: 1,
                    shade: 0.1,
                    time: 1000
                  }, function () {
                    if (state.revise > 0) {
                      delete state.create;
                      layer.msg('请先修正凭证', {
                        icon: 0,
                        shade: 0.5,
                        time: 600
                      }, function () {
                        layui.element.tabChange('tab', 1);
                      });
                    } else {
                      multiSearch();
                    }
                  });
                } else if (res.code == "2") {
                  layer.confirm(res.info, {
                    btn: ['确定', '取消'] //按钮
                  }, function (index) {
                    InvoiceMapping(res.invoiceType)
                    layer.close(index)
                  }, function () {

                  });
                } else if (res.code == "9999") {
                  layer.confirm('请先完善银行流水与科目映射', {
                    btn: ['确定', '取消'] //按钮
                  }, function (index) {
                    bankMapping()
                    layer.close(index)
                  }, function () {

                  });
                } else if (res.code == "-9999") {
                  layer.confirm('请前往银行设置,添加银行账号与科目的映射!', {
                    btn: ['确定', '取消'] //按钮
                  }, function (index) {
                    bankCodeMapping()
                    layer.close(index)
                  }, function () {

                  });
                } else {
                  if (res.info.indexOf('未检查到科目初始化映射,请确认是否已经对科目进行映射处理 voucherServoce checkMappingSub') > -1) {
                    top.layer.open({
                      type: 0,
                      icon: 0,
                      title: '提示',
                      content: '请先完成标准科目映射',
                      btn: ['确定'],
                      resize: false,
                      yes: function (index) {
                        top.$("#subMap").click();
                        top.layer.close(index);
                      },
                      cancel: function () {
                        top.$("#subMap").click();
                      }
                    });
                  } else {
                    layer.msg(res.info || '生成凭证错误', {
                      icon: 2,
                      shade: 0.3,
                      shadeClose: true,
                      time: 2000000
                    }, function () {
                      multiSearch();
                    });
                  }
                }
              });
              layer.closeAll('loading');
            } else {
              layer.msg('正在生成凭证中，操作中止', {
                icon: 0,
                shade: 0.1
              });
            }
          } else {
            layer.msg(res.message || '检查是否正在生成凭证异常，操作中止', {
              icon: 2,
              shade: 0.1
            });
          }
        });
      });
    }

    // 一键计提
    function carryForward() {
      if (vchrStatus.isJt === 1) {
        reverseAccounting(); // 已经计提
        return;
      } else if (vchrStatus.isCreateVoucher === 0) {
        layer.msg('请先一键生成凭证', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      if (state.revise > 0) {
        layer.msg('请先修正凭证', {
          icon: 0,
          shade: 0.5,
          time: 1000
        }, function () {
          layui.element.tabChange('tab', 1);
        });
        return;
      }

      layer.confirm('计提后不可导入数据，只允许手动录入凭证<br>是否一键计提？', {
        icon: 0,
        title: '提示'
      }, function (index) {
        // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
        layer.msg('检查是否正在计提，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
          if (res.success === 'true') {
            if (res.progress.jt == 0) {
              delete state.revise;
              layer.msg('正在计提中，请稍候~', {
                icon: 16,
                shade: 0.3,
                time: -1
              });
              //      postRequest(window.baseURL + '/qmjz/doQmjz', {}, function(res) {
              postRequest(window.baseURL + '/jt/oneKeyJt', {}, function (res) {
                vchrStatus = _queryStatus();
                showProgress();
                // _awaitUpdateVcNo(); // 新增凭证页面重新获取凭证号
                if (res.success == 'true') {
                  top.subListRefresh = true; // 末级科目列表subListData刷新
                  window._topTabsRefresh(['新增凭证', '编辑凭证', '总账', '明细帐', '数量金额总帐', '科目余额表', '凭证汇总表']);
                  multiSearch2();
                  layer.msg(res.info || '一键计提成功', {
                    icon: 1,
                    shade: 0.1,
                    time: 1000
                  }, function () {
                    if (state.revise > 0) {
                      delete state.create;
                      layer.msg('请先修正凭证', {
                        icon: 0,
                        shade: 0.5,
                        time: 1000
                      }, function () {
                        layui.element.tabChange('tab', 1);
                      });
                    } else {
                      multiSearch();
                    }
                  });
                } else {
                  res.info && layer.msg(res.info, {
                    icon: 2,
                    shade: 0.1,
                    shadeClose: true,
                    time: 2000000
                  });
                }
              });
              layer.closeAll('loading');
            } else {
              layer.msg('正在计提中，操作中止', {
                icon: 0,
                shade: 0.1
              });
            }
          } else {
            layer.msg('检查是否正在计提异常，操作中止', {
              icon: 2,
              shade: 0.1
            });
          }
        });
      });
    }

    // 反计提
    // 删除修正的凭证
    function reverseAccounting() {
      if (vchrStatus.isJz === 1) {
        layer.msg('已结账，不能反计提，请先反结账', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      } else if (vchrStatus.isCarryState === 1) {
        layer.msg('已结转，不能反计提，请先反结转', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      layer.confirm('是否反计提？', {
        icon: 0,
        title: '提示'
      }, function (index) {
        // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
        layer.msg('检查是否正在反计提，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
          if (res.success === 'true') {
            if (res.progress.unJt == 0) {
              layer.msg('正在反计提，请稍候~', {
                icon: 16,
                shade: 0.3,
                time: -1
              });
              postRequest(window.baseURL + '/unJt/doUnJt', {}, function (res) {
                vchrStatus = _queryStatus();
                showProgress();
                // _awaitUpdateVcNo(); // 新增凭证页面重新获取凭证号
                top.subListRefresh = true; // 末级科目列表subListData刷新
                window._topTabsRefresh(['新增凭证', '编辑凭证', '总账', '明细帐', '数量金额总帐', '科目余额表', '凭证汇总表']);
                if (res.success === 'true') {
                  top.subListRefresh = true; // 末级科目列表subListData刷新
                  layer.msg('反计提成功', {
                    icon: 1,
                    shade: 0.1,
                    time: 1000
                  }, function () {
                    state.revise = 0;
                    currPage2 = 1;
                    $('#voucher-list2').attr('data-length', 0).html('<tr class="tc"><td colspan="8" style="height: 80px;">暂无凭证数据</td></tr>');
                    flushPage('layui-table-page2', currPage2, 0, 10, multiSearch2);
                    multiSearch();
                  });
                } else {
                  layer.msg(res.msg || '反计提失败', {
                    icon: 2,
                    shade: 0.1,
                    shadeClose: true,
                    time: 2000000
                  });
                }
              });
              layer.closeAll('loading');
            } else {
              layer.msg('正在反计提中，操作中止', {
                icon: 0,
                shade: 0.1
              });
            }
          } else {
            layer.msg('检查是否正在反计提异常，操作中止', {
              icon: 2,
              shade: 0.1
            });
          }
        });

      });
    }

    // 凭证一键结转
    function oneKeyAudit() {
      if (vchrStatus.isCarryState === 1) {
        fanjiezhuan(); //已经结转
        return;
      }
      if (state.revise > 0) {
        layer.msg('请先修正凭证', {
          icon: 0,
          shade: 0.5,
          time: 1000
        }, function () {
          layui.element.tabChange('tab', 1);
        });
        return;
      }
      if (vchrStatus.isJz === 1) {
        layer.msg('已结账，不可操作', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      } else if (vchrStatus.isJt === 0) {
        layer.msg('请先一键计提', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }

      var params = {
        auditStatus: vchrStatus.isPzjc === 1 ? 0 : 1
      };

      layer.confirm('一键结转后凭证不可修改<br>反结转后可修改凭证', {
        icon: 0,
        title: '提示'
      }, function () {
        if (vchrStatus.isPzjc === 1) {
          layer.msg('已检查，不能反结转', {
            icon: 16,
            shade: 0.3
          });
        } else {
          // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
          layer.msg('检查是否正在结转，请稍候~', {
            icon: 16,
            shade: 0.1,
            time: -1
          });
          postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
            if (res.success === 'true') {
              if (res.progress.carryState == 0) {
                layer.msg('正在结转中，请稍候~', {
                  icon: 16,
                  shade: 0.1,
                  time: -1
                });
                //      postRequest(window.baseURL + '/voucher/oneKeyCheckVoucher', params, function(res) {
                postRequest(window.baseURL + '/qmjz/doQmjz', params, function (res) {
                  vchrStatus = _queryStatus();
                  showProgress();
                  if (res.success == 'true') {
                    top.subListRefresh = true; // 末级科目列表subListData刷新
                    window._topTabsRefresh(['新增凭证', '编辑凭证', '总账', '明细帐', '数量金额总帐', '科目余额表', '凭证汇总表']);
                    layer.msg(res.message || '一键结转成功', {
                      icon: 1,
                      shade: 0.1,
                      time: 1000
                    }, function () {
                      // 重新请求第一页数据
                      multiSearch();
                      // $('#layui-table-page1').find('input').val('1').next().click();
                    });
                  } else {
                    layer.msg(res.message || '结转失败', {
                      icon: 2,
                      shade: 0.1,
                      shadeClose: true,
                      time: 2000000
                    }, function () {

                    });
                    //$('#i-refresh').click();
                  }
                });
                layer.closeAll('loading');
              } else {
                layer.msg('正在结转中，操作中止', {
                  icon: 0,
                  shade: 0.1
                });
              }
            } else {
              layer.msg('检查是否正在结转异常，操作中止', {
                icon: 2,
                shade: 0.1
              });
            }
          });
        }
      });
    }

    // 反结转
    // 删除修正的凭证
    function fanjiezhuan() {
      if (vchrStatus.isJz === 1) {
        layer.msg('已结账，不能反结转，请先反结账', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      } else if (vchrStatus.isPzjc === 1) {
        layer.msg('已检查，不能反结转，请先反检查', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      layer.confirm('反结转后凭证可新增、编辑、删除<br>是否反结转？', {
        icon: 0,
        title: '提示'
      }, function (index) {
        // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
        layer.msg('检查是否正在反结转，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
          if (res.success === 'true') {
            if (res.progress.unCarryState == 0) {
              layer.msg('正在反结转，请稍候~', {
                icon: 16,
                shade: 0.3,
                time: -1
              });
              postRequest(window.baseURL + '/unQmjz/doUnQmjz', {}, function (res) {
                vchrStatus = _queryStatus();
                showProgress();
                if (res.success === 'true') {
                  top.subListRefresh = true; // 末级科目列表subListData刷新
                  window._topTabsRefresh(['新增凭证', '编辑凭证', '总账', '明细帐', '数量金额总帐', '科目余额表', '凭证汇总表']);
                  layer.msg('反结转成功', {
                    icon: 1,
                    shade: 0.1,
                    time: 1000
                  }, function () {
                    state.revise = 0;
                    currPage2 = 1;
                    $('#voucher-list2').attr('data-length', 0).html('<tr class="tc"><td colspan="8" style="height: 80px;">暂无凭证数据</td></tr>');
                    flushPage('layui-table-page2', currPage2, 0, 10, multiSearch2);
                    multiSearch();
                    // $('#i-refresh').click();
                  });
                } else {
                  layer.msg(res.msg || '反结转失败', {
                    icon: 2,
                    shade: 0.1,
                    shadeClose: true,
                    time: 2000000
                  });
                }
              });
              layer.closeAll('loading');
            } else {
              layer.msg('正在反结转中，操作中止', {
                icon: 0,
                shade: 0.1
              });
            }
          } else {
            layer.msg('检查是否正在反结转异常，操作中止', {
              icon: 2,
              shade: 0.1
            });
          }
        });

      });
    }

    //凭证检查
    function voucherAuditS() {
      // var wod = $("#content").width();
      // $("#search1,#navDOM1").css({ "left": "20px", "width": wod });
      if (state.revise > 0) {
        layer.msg('请先修正凭证', {
          icon: 0,
          shade: 0.5,
          time: 1000
        }, function () {
          layui.element.tabChange('tab', 1);
        });
        return;
      }
      if (vchrStatus.isJz === 1) {
        layer.msg('已结账，不可操作', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      } else if (vchrStatus.isCarryState === 0) {
        layer.msg('请先一键结转', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      var params = {
        auditStatus: vchrStatus.isPzjc === 1 ? 0 : 1
      };

      layer.confirm('确定要凭证检查吗？', {
        icon: 0,
        title: '提示'
      }, function () {
        if (vchrStatus.isPzjc === 1) {
          layer.msg('重新检测中，请稍候~', {
            icon: 16,
            shade: 0.1,
            time: -1
          });
        } else {
          layer.msg('凭证检查中，请稍候~', {
            icon: 16,
            shade: 0.1,
            time: -1
          });
        }
        postRequest(window.baseURL + '/detection/queryDetection', params, function (res) {
          if (res.code === 1) {
            var $parent = $('#voucher');

            // 重置状态图标
            $parent.find('h2.layui-colla-title > p > img').hide(); // 隐藏标题所有状态图标
            $parent.find(".layui-colla-content li img").show(); // 显示li所有状态图标
            $parent.find(".layui-colla-content li span").show(); // 显示li所有检查结果

            // 检查标题风险数量
            for (var i = 1; i < 5; i++) {
              $parent.find('.itemMag' + i).html(res['msg' + i]);
            }
            // $(".layui-colla-title .itemMag1").html(res.msg1);
            // $(".layui-colla-title .itemMag2").html(res.msg2);
            // $(".layui-colla-title .itemMag3").html(res.msg3);
            // $(".layui-colla-title .itemMag4").html(res.msg4);

            // 真实检测的项目
            $parent.find(".balanceSheet span").html(res.balanceSheet.msg);
            $parent.find(".insolvency span").html(res.insolvency.msg);
            $parent.find(".salary span").html(res.salary.msg);
            $parent.find(".inventory span").html(res.inventory.msg);
            $parent.find(".kcCommodity span").html(res.kcCommodity.msg);
            $parent.find(".incomeStatement span").html(res.incomeStatement.msg);
            $parent.find(".weal span").html(res.weal.msg);
            $parent.find(".zeroDeclaration span").html(res.zeroDeclaration.msg);
            $parent.find(".bank span").html(res.bank.msg);

            // 凭证检查 start
            //工资表、固定资产折旧有无计提
            if (res.salary.state === 1) {
              $parent.find(".salary .warn2").hide();
              $parent.find(".salary .warn1").hide();
              // $parent.find(".salaryImg3").show();
            } else if (res.salary.state === 0) {
              $parent.find(".salary .warn2").hide();
              $parent.find(".salary .warn3").hide();
              // $parent.find(".salaryImg1").show();
            } else if (res.salary.state === 2) {
              $parent.find(".salary .warn1").hide();
              $parent.find(".salary .warn3").hide();
              // $parent.find(".salaryImg2").show();
            }
            var state1 = res.salary.state;
            // 标题状态图标
            // var state1;
            // if (res.salary.state == 0) {
            //   state1 = 0;
            // } else if (res.salary.state == 2) {
            //   state1 = 2;
            // } else {
            //   state1 = 1;
            // }
            $parent.find('.salaryImg' + _checkIcon(state1)).show();

            // 凭证检查 end

            // 余额检查 start
            //银行存款有无负数
            if (res.bank.state === 1) {
              $parent.find(".bank .warn2").hide();
              $parent.find(".bank .warn1").hide();
            } else if (res.bank.state === 0) {
              $parent.find(".bank .warn2").hide();
              $parent.find(".bank .warn3").hide();
              // $parent.find(".inventoryImg1").show();
            } else if (res.bank.state === 2) {
              $parent.find(".bank .warn1").hide();
              $parent.find(".bank .warn3").hide();
              // $parent.find(".inventoryImg2").show();
            }
            //资金有无负数
            if (res.inventory.state === 1) {
              $parent.find(".inventory .warn2").hide();
              $parent.find(".inventory .warn1").hide();
            } else if (res.inventory.state === 0) {
              $parent.find(".inventory .warn2").hide();
              $parent.find(".inventory .warn3").hide();
              // $parent.find(".inventoryImg1").show();
            } else if (res.inventory.state === 2) {
              $parent.find(".inventory .warn1").hide();
              $parent.find(".inventory .warn3").hide();
              // $parent.find(".inventoryImg2").show();
            }
            //存货有无负数
            if (res.kcCommodity.state === 1) {
              $parent.find(".kcCommodity .warn2").hide();
              $parent.find(".kcCommodity .warn1").hide();
            } else if (res.kcCommodity.state === 0) {
              $parent.find(".kcCommodity .warn2").hide();
              $parent.find(".kcCommodity .warn3").hide();
              // $parent.find(".inventoryImg1").show();
            } else if (res.kcCommodity.state === 2) {
              $parent.find(".kcCommodity .warn1").hide();
              $parent.find(".kcCommodity .warn3").hide();
              // $parent.find(".inventoryImg2").show();
            }
            // 标题状态图标
            var state2;
            if (res.bank.state == 0 || res.inventory.state == 0 || res.kcCommodity.state == 0) {
              state2 = 0;
            } else if (res.bank.state == 2 || res.inventory.state == 2 || res.kcCommodity.state == 2) {
              state2 = 2;
            } else {
              state2 = 1;
            }
            $parent.find('.inventoryImg' + _checkIcon(state2)).show();
            // 余额检查 end

            // 报表检查 start
            //资产负债表是否平衡
            if (res.balanceSheet.state === 1) {
              $parent.find(".balanceSheet .warn2").hide();
              $parent.find(".balanceSheet .warn1").hide();
            } else if (res.balanceSheet.state === 0) {
              $parent.find(".balanceSheet .warn2").hide();
              $parent.find(".balanceSheet .warn3").hide();
              // $parent.find(".warnImg1").show();
            } else if (res.balanceSheet.state === 2) {
              $parent.find(".balanceSheet .warn1").hide();
              $parent.find(".balanceSheet .warn3").hide();
              // $parent.find(".warnImg2").show();
            }
            //成本倒挂
            if (res.incomeStatement.state === 1) {
              $parent.find(".incomeStatement .warn2").hide();
              $parent.find(".incomeStatement .warn1").hide();
            } else if (res.incomeStatement.state === 0) {
              $parent.find(".incomeStatement .warn2").hide();
              $parent.find(".incomeStatement .warn3").hide();
              // $parent.find(".warnImg1").show();
            } else if (res.incomeStatement.state === 2) {
              $parent.find(".incomeStatement .warn1").hide();
              $parent.find(".incomeStatement .warn3").hide();
              // $parent.find(".warnImg2").show();
            }
            //资不抵债
            if (res.insolvency.state === 1) {
              $parent.find(".insolvency .warn2").hide();
              $parent.find(".insolvency .warn1").hide();
            } else if (res.insolvency.state === 0) {
              $parent.find(".insolvency .warn2").hide();
              $parent.find(".insolvency .warn3").hide();
              // $parent.find(".warnImg1").show();
            } else if (res.insolvency.state === 2) {
              $parent.find(".insolvency .warn1").hide();
              $parent.find(".insolvency .warn3").hide();
              // $parent.find(".warnImg2").show();
            }
            // 标题状态图标
            var state3;
            if (res.balanceSheet.state == 0 || res.incomeStatement.state == 0 || res.insolvency.state == 0) {
              state3 = 0;
            } else if (res.balanceSheet.state == 2 || res.incomeStatement.state == 2 || res.insolvency.state == 2) {
              state3 = 2;
            } else {
              state3 = 1;
            }
            $parent.find('.warnImg' + _checkIcon(state3)).show();
            // 报表检查 end

            // 其他指标 start
            //福利费扣除标准
            if (res.weal.state === 1) {
              $parent.find(".weal .warn2").hide();
              $parent.find(".weal .warn1").hide();
            } else if (res.weal.state === 0) {
              $parent.find(".weal .warn2").hide();
              $parent.find(".weal .warn3").hide();
              // $parent.find(".inventoryImg1").show();
            } else if (res.weal.state === 2) {
              $parent.find(".weal .warn1").hide();
              $parent.find(".weal .warn3").hide();
              // $parent.find(".itemImg2").show();
            }
            //连续零申报
            if (res.zeroDeclaration.state === 1) {
              $parent.find(".zeroDeclaration .warn2").hide();
              $parent.find(".zeroDeclaration .warn1").hide();
            } else if (res.zeroDeclaration.state === 0) {
              $parent.find(".zeroDeclaration .warn2").hide();
              $parent.find(".zeroDeclaration .warn3").hide();
              // $parent.find(".inventoryImg1").show();
            } else if (res.zeroDeclaration.state === 2) {
              $parent.find(".zeroDeclaration .warn1").hide();
              $parent.find(".zeroDeclaration .warn3").hide();
              // $parent.find(".itemImg2").show();
            }
            // 标题状态图标
            var state4;
            if (res.weal.state == 0 || res.zeroDeclaration.state == 0) {
              state4 = 0;
            } else if (res.weal.state == 2 || res.zeroDeclaration.state == 2) {
              state4 = 2;
            } else {
              state4 = 1;
            }
            $parent.find('.itemImg' + _checkIcon(state4)).show();
            // 其他指标 end
          }
          vchrStatus = _queryStatus();
          showProgress();
          if (res.code == 1) {
            layer.msg(res.msg, {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
            // 切换凭证检查/重新检查
            var str = vchrStatus.isPzjc === 1 ? '已检查' : '未检查';
            $('#voucher-list tr').each(function () {
              $(this).children('td:eq(6)').html(str);
            });
          } else if (res.code == 2) {
            layer.msg(res.msg, {
              icon: 1,
              shade: 0.1,
              shadeClose: true,
              time: 1000
            });
            // 切换凭证检查/重新检查
            var str = vchrStatus.isPzjc === 1 ? '已检查' : '未检查';
            $('#voucher-list tr').each(function () {
              $(this).children('td:eq(6)').html(str);
            });
          } else {
            layer.msg(res.msg || '检查未通过', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 2000000
            });
          }
          //$("#i-refresh").click();
          // fixedHead('', $('#create-table'), $('#search1'), $('#navDOM1'));
        });
        layer.closeAll('loading');
      });

      // 映射状态
      function _checkIcon(type) {
        var state;
        switch (type) {
        case 0:
          state = 1;
          break;
        case 1:
          state = 3;
          break;
        case 2:
          state = 2;
          break;
        }
        return state;
      }
    }
    // 重新检测
    function updateDetectionOne() {
      if (vchrStatus.isJz === 0) {
        layer.msg('请先一键结账', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      layer.confirm('是否反结账？', {
        icon: 0,
        title: '提示'
      }, function () {
        layer.msg('正在反结账中，请稍候~', {
          icon: 16,
          shade: 0.3,
          time: -1
        });
        //      postRequest(window.baseURL + '/detection/updateDetectionOne', {}, function(res) {
        postRequest(window.baseURL + '/detection/updateDetectionZero', {}, function (res) {
          vchrStatus = _queryStatus();
          showProgress();
          if (res.success === 'true') {
            layer.msg('反结账成功', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
            multiSearch();
            delete state.revise;
          } else {
            layer.msg(res.msg || '反结账失败', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 2000000
            });
          }
        });
        layer.closeAll('loading');
      });
    }

    // 一键结账
    // 结账检查
    function checkoutExamine() {
      var flag = false,
        url = window.baseURL + '/checkout/checkoutExamine';
      if (vchrStatus.isJz === 1) {
        antiSettlement();
        return false;
      } else if (vchrStatus.isPzjc === 0) {
        layer.msg('请先凭证检查', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return false;
      }

      layer.msg('结账检查中，请稍候~', {
        icon: 16,
        shade: 0.1,
        time: -1
      });
      return true; //接口未完成
    }

    // 结账
    function settleAccounts() {
      if (state.revise > 0) {
        layer.msg('请先修正凭证', {
          icon: 0,
          shade: 0.5,
          time: 1000
        }, function () {
          layui.element.tabChange('tab', 1);
        });
        return;
      }
      if (!checkoutExamine()) {
        return;
      }
      layer.confirm('是否一键结账？', {
        icon: 0,
        title: '提示'
      }, function () {
        // 检测服务器是否正在执行中（防止用户刷新页面后再次提交生成请求）
        layer.msg('检查是否正在结转，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        postRequest(window.baseURL + '/progress/getProgress', {}, function (res) {
          if (res.success === 'true') {
            if (res.progress.jz == 0) {
              layer.msg('正在结账中，请稍候~', {
                icon: 16,
                shade: 0.3,
                time: -1
              });
              postRequest(window.baseURL + '/checkout/settleAccounts', {}, function (res) {
                var nowdays = new Date();
                var year = nowdays.getFullYear();
                var month = nowdays.getMonth();
                if (month == 0) {
                  month = 12;
                  year = year - 1;
                } else if (month < 10) {
                  month = "0" + month;
                }
                var beforeMonth = year + "-" + month; //上个月
                var _busDate = top.$('#busDate').val();
                vchrStatus = _queryStatus();
                showProgress();
                if (res.code === 1) {
                  if (beforeMonth <= _busDate) { // 2018-09 > 2018-01
                    layer.msg('结账成功!', {
                      icon: 1,
                      shade: 0.1,
                      time: 1000
                    });
                    multiSearch();
                    delete state.revise;
                  } else if (beforeMonth > _busDate) {
                    layer.confirm('结账成功，是否前往下一期做账？', {
                      icon: 0,
                      title: '提示'
                    }, function () {
                      var nowdays = new Date(_busDate);
                      var year = nowdays.getFullYear();
                      var month = nowdays.getMonth() + 2; // 下个月
                      if (month == 13) {
                        month = '01';
                        year = year + 1;
                      } else if (month < 10) {
                        month = "0" + month;
                      }
                      var _period = year + '-' + month;
                      postRequest(window.baseURL + '/system/chgPeriod', { period: _period }, function (res) {
                        layer.closeAll('loading');
                        if (res == 'success') {
                          layer.msg('切换会计期间成功');
                          // window.location.href = window.baseURL + "/subinit/initView?init=1";
                          top.location.reload();
                        } else {
                          layer.msg('切换会计期间异常');
                        }
                      });
                    }, function () {
                      multiSearch();
                      delete state.revise;
                    });
                  } else {
                    layer.msg('结账失败!', {
                      icon: 2,
                      shade: 0.1,
                      shadeClose: true,
                      time: 2000000
                    });
                  }
                } else {
                  layer.msg(res.msg || '结账失败!', {
                    icon: 2,
                    shade: 0.1,
                    shadeClose: true,
                    time: 2000000
                  });
                }
              });
              layer.closeAll('loading');
            } else {
              layer.msg('正在结账中，操作中止', {
                icon: 0,
                shade: 0.1
              });
            }
          } else {
            layer.msg('检查是否正在结账异常，操作中止', {
              icon: 2,
              shade: 0.1
            });
          }
        });
      });
    }

    // 反结账
    function antiSettlement() {
      if (vchrStatus.isJz === 0) {
        layer.msg('请先一键结账', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }
      layer.confirm('是否反结账？', {
        icon: 0,
        title: '提示'
      }, function () {
        layer.msg('正在反结账中，请稍候~', {
          icon: 16,
          shade: 0.3,
          time: -1
        });
        postRequest(window.baseURL + '/jzcl/unJzcl', {}, function (res) {
          vchrStatus = _queryStatus();
          showProgress();
          if (res.success === 'true') {
            layer.msg(res.msg || '反结账成功', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
            multiSearch();
            delete state.revise;
          } else {
            layer.msg(res.msg || '反结账失败', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 2000000
            });
          }
        });
        layer.closeAll('loading');
      });
    }

    //生成凭证检查进销项映射没通过跳转进销项重新映射
    function InvoiceMapping(item) {
      window.sessionStorage.setItem("isItemType", item);
      if (item == "1") {
        top.$("#navBar .YJlipiao #YJlipiao0").click();
      } else if (item == "2") {
        top.$("#navBar .YJlipiao #YJlipiao1").click();
      }
    }

    //生成凭证检查银行科目映射，没通过跳转到银行映射页面
    function bankMapping() {
      top.$("#navBar .YJlipiao #YJlipiao2").click();
    }

    function bankCodeMapping() {
      top.$("#navBar .YJlipiao #bankCode").click();
    }
    // 全部删除
    function _removeAll() {
      //      if (vchrStatus.isAudited === 1) {
      //          layer.msg('已审核，不能删除凭证', {
      //        icon: 2,
      //            shade: 0.1,
      //            shadeClose: true,
      //            time: 2000000
      //          });
      //          return;
      //        }
      //凭证导入 删除判断
      if (vchrStatus.isCreateVoucher === 0) {
        var len = $(".opt").length;
        if (len == 0) {
          layer.msg('没有可以删除的凭证', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000000
          });
          return;
        }
      }
      var type = "";
      if (vchrStatus.isCreateVoucher === 0) {
        type = 0;
      } else if (vchrStatus.isCreateVoucher === 1 && vchrStatus.isCarryState != 1) {
        type = 1;
      } else if (vchrStatus.isCarryState === 1) {
        type = 2;
      }
      if (type !== 0 && type !== 1 && type !== 2) {
        layer.msg('您没有权限删除', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
        return;
      }

      layer.confirm('提示<br/><span class="red">是否删除全部凭证？删除之后将会清除本期所有科目发生额与数量金额表 , 请谨慎操作。<span>', {
        icon: 3,
        title: '提示'
      }, function () {

        layer.msg('正在删除中，请稍候~', {
          icon: 16,
          shade: 0.1,
          time: -1
        });
        postRequest(window.baseURL + '/voucher/oneKeyDelVouch', {
          type: type
        }, function (res) {
          if (res.success === 'true') {
            top.subListRefresh = true; // 末级科目列表subListData刷新
            // tab栏更新
            window._topTabsRefresh(['进项发票导入', '销项发票导入', '银行对账单导入', '工资表导入', '费用票录入', '固定资产导入', '新增凭证', '凭证汇总表']);
            top.$('#top_tabs > li > cite').each(function (idx) {
              var title = $(this).text();
              if ('编辑凭证' === title) {
                $(this).siblings('i.layui-tab-close').click();
              }
            });
            layer.msg(res.info || '删除全部凭证成功', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
            var str = '<tr class="tc"><td colspan="8" style="height: 80px;">暂无凭证数据</td></tr>';
            $('#voucher-list').html(str).parents('.table-box').find('.layui-table-page .layui-laypage').html('');
            $('#voucher-list2').html(str).parents('.table-box').find('.layui-table-page .layui-laypage').html('');
            _setTabData(0);
            state.create = 0;
            state.revise = 0;
            vchrStatus = _queryStatus();
            showProgress();

          } else {
            res.info && layer.msg(res.info, {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 2000000
            });
          }
        });
        layer.closeAll('loading');
      });

    }

    // 删除凭证
    function deleteData(item) {
      var id = item.attr('id');
      if (id == '') {
        layer.msg('请选择凭证');
        return;
      }

      layer.confirm('是否删除凭证?', {
        icon: 3,
        title: '提示'
      }, function () {
        postRequest(window.baseURL + '/voucher/delVoucher', {
          vouchIDs: id || '',
        }, function (res) {
          if (res.code == 0) {
            layer.msg('删除凭证成功', {
              icon: 1,
              shade: 0.1,
              time: 1000
            });
            top._subListRefresh(res.data); // 修改科目列表期末余额
            window._topTabsRefresh(['新增凭证', '编辑凭证', '凭证汇总表']);
            // _awaitUpdateVcNo(); // 新增凭证页面重新获取凭证号
            // 删除生成列表中的凭证
            // 重新请求修正列表数据
            // 判断是在哪个列表点击删除按钮
            if (state.tabIndex === 0) {
              // $createVoucher.parents('.table-box').find('button.layui-laypage-btn').click();
              state.awaitUpdate = 1; // 等待更新: 0生成列表 1修正列表
              if (item.attr('data-error') === '1') { // 修正凭证
                state.revise = state.revise > 0 ? state.revise - 1 : 0;
                $reviseVoucher.attr('data-length', state.revise);
                $('#layui-table-page2 .layui-laypage-count').text('共 ' + state.revise + ' 条');
              }
              item.remove();
              $('#layui-table-page1 .layui-laypage-btn').click();
            } else if (state.tabIndex === 1) {
              state.awaitUpdate = 0; // 等待更新: 0生成列表 1修正列表
              state.create = state.create > 0 ? state.create - 1 : 0;
              $createVoucher.attr('data-length', state.create);
              $('#layui-table-page1 .layui-laypage-count').text('共 ' + state.create + ' 条');
              item.remove();
              $('#layui-table-page2 .layui-laypage-btn').click();
            }
            // 判断编辑凭证页面是否该凭证
            if (window.sessionStorage.editVouch === undefined) {} else {
              var editVouch = JSON.parse(window.sessionStorage.editVouch);
              if (id === editVouch.id) {
                top.$('#top_tabs > li > cite').each(function (idx) {
                  if ($(this).text() === '编辑凭证') {
                    $(this).siblings('i.layui-tab-close').click();
                  }
                });
              }
            }
          } else {
            layer.msg(res.message || '删除凭证异常', {
              icon: 2,
              shade: 0.1,
              shadeClose: true,
              time: 2000000
            });
          }
        });
      });
    }
    window.state = state;
  });

  // 获取凭证操作状态
  function _queryStatus() {
    var obj = {
      isCreateVoucher: 0, //生成凭证
      isCarryState: 0, //结转
      isAudited: 0, //审核 现在改为使用检查了
      isPzjc: 0, //检查
      isJz: 0, //结账
      isJt: 0 //计提
    };

    $.ajax({
      async: false,
      url: window.baseURL + '/status/queryStatus',
      success: function (res) {
        if (res.success === 'true') {
          var data = res.status;
          obj = {
            isCreateVoucher: data.isCreateVoucher,
            isCarryState: data.isCarryState,
            isAudited: data.isCheck,
            isPzjc: data.isDetection,
            isJz: data.isJz,
            isJt: data.isJt
          };

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
    return obj;
  }

  // 显示结账流程进度
  function showProgress() {
    // 已结账
    if (vchrStatus.isJz === 1) {
      $(".table-box").eq(0).show();
      $("#voucher").hide();
      $('.isShow').show();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(2).html('一键结转');
      $createBtns.eq(3).removeClass().addClass('layui-btn layui-btn-disabled').html('检查通过');
      $createBtns.eq(4).removeClass().addClass('layui-btn layui-btn-primary').html('反结账');
    }
    // 凭证检查
    else if (vchrStatus.isPzjc === 1) {
      $(".table-box").eq(0).hide();
      $("#voucher").show();
      $('.isShow').show();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(3).removeClass().addClass('layui-btn layui-btn-primary').html('重新检查');
      $createBtns.eq(4).removeClass().addClass('layui-btn layui-btn-normal').html('一键结账');
    } else if (vchrStatus.isPzjc === 2 && vchrStatus.isCarryState === 1) {
      $(".table-box").eq(0).hide();
      $("#voucher").show();
      $('.isShow').show();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(2).removeClass().addClass('layui-btn layui-btn-primary').html('反结转');
      $createBtns.eq(3).removeClass().addClass('layui-btn layui-btn-normal').html('一键检查');
    }
    // 已结转
    else if (vchrStatus.isCarryState === 1) {
      $(".table-box").eq(0).show();
      $("#voucher").hide();
      $('.isShow').show();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(2).removeClass().addClass('layui-btn layui-btn-primary').html('反结转');
      $createBtns.eq(3).removeClass().addClass('layui-btn layui-btn-normal').html('一键检查');
    }

    // 已计提
    else if (vchrStatus.isJt === 1) {
      $(".table-box").eq(0).show();
      $("#voucher").hide();
      $('.isShow').show();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(1).removeClass().addClass('layui-btn layui-btn-primary').html('反计提');
      $createBtns.eq(2).removeClass().addClass('layui-btn layui-btn-normal').html('一键结转');
    }
    // 已生成凭证
    else if (vchrStatus.isCreateVoucher === 1) {
      $(".table-box").eq(0).show();
      $("#voucher").hide();
      $('.isShow').hide();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(1).removeClass().addClass('layui-btn layui-btn-normal').html('一键计提');
    }
    // 未生成凭证
    else if (vchrStatus.isCreateVoucher === 0) {
      $(".table-box").eq(0).show();
      $("#voucher").hide();
      $createBtns.addClass('layui-btn-disabled');
      $createBtns.eq(0).removeClass('layui-btn-disabled');
    } else {
      console.log(vchrStatus);
    }
  }

  // 查询凭证号范围
  function _getPrintRange() {
    getRequest(window.baseURL + '/voucher/getPrintVoucherRange', function (res) {
      if ('true' === res.success) {
        var min = res.minNo,
          max = res.maxNo;
        state.vcRange = [min, max];
        if (min && max) {
          $('#print-begin').val(min);
          $('#print-end').val(max);
        } else {
          alert('[getPrintVoucherRange]获取凭证号范围异常');
        }
      } else {
        layer.msg('获取凭证号范围异常');
        state.vcRange = null;
      }
    });
  }

  // 全部凭证打印预览
  function _printAllVoucher(type, begin, end) {
    var msg = 0 === type ? '正在查询所有凭证，请稍候~' : '正在查询所选凭证范围，请稍候~';
    layer.msg(msg, {
      icon: 16,
      shade: 0.1,
      time: -1
    });
    var params = {
      isAll: type,
      begin: begin || '',
      end: end || ''
    };
    postRequest(window.baseURL + '/voucher/printVoucher', params, function (res) {
      if (res.success === 'true') {
        var compName = top.$('#choose-account .selectCus').text().trim(),
          list = res.list,
          len = list.length;

        if (len === 0) {
          layer.msg('当前没有凭证可打印', {
            icon: 0,
            shade: 0.1,
            time: 1000
          });
          return;
        }
        1 === type && $('#print-popup').parents('.layui-layer-page').find('.layui-layer-close')[0].click(); // 打印全部凭证则关闭凭证打印选择弹窗
        var iframeWin;
        layer.open({
          type: 2,
          title: '全部凭证打印预览',
          area: ['800px', '80%'],
          maxmin: true,
          content: window.baseURL + '/voucher/printPreview',
          btn: ['打印', '取消'],
          success: function (layero) {
            layer.closeAll('dialog');
            iframeWin = window.frames[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象
            iframeWin._innerHtml(list, compName);
          },
          yes: function () {
            iframeWin._printPreview();
          },
          btn2: function () {
            layer.closeAll('dialog');
          },
          cancel: function () {
            layer.closeAll('dialog');
          }
        });
      } else {
        layer.msg('获取凭证列表异常', {
          icon: 2,
          shade: 0.1
        });
      }
    });
    layer.closeAll('loading');
  }

  //上传附件
  $("body").delegate(".fujian", "click", function () {
    var id = $(".fujian").attr('id');
    url = window.baseURL + '/voucher/uploadVoucherAttach';
    $('input[name="vouchID"]').val(id);
    uploadImg(this, url, uploadVoucherAttach);
  })

  function uploadVoucherAttach(result) {
    var id = $(".fujian").attr('id');
    if (result.success == 'true') {
      layer.msg('上传成功', {
        icon: 1,
        shade: 0.1,
        time: 2000
      });
      $(".fujian").text("继续上传");
      $("#" + id).children(".opt").html('<span class="layui-icon cu edit" title="修改">&#xe642;</span><span class="layui-icon cu remove" title="删除">&#x1006;</span><span class="layui-icon cu print" title="打印">&#xe63c;</span><span class="layui-icon cu uploadAccessory" title="上传附件">&#xe681;</span><span class="layui-icon cu accessory" title="查看附件">&#xe64a;</span>');
    } else {
      layer.msg(result.message, {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
    }
  }
  $("body").delegate("#file", "change", function () { // 当 id 为 file 的对象发生变化时
    var fileSize = this.files[0].size;
    var size = fileSize / 1024 / 1024;
    if (size > 50) {
      alert("附件不能大于50M,请将文件压缩后重新上传！");
      this.value = "";
      return false;
    } else {
      $("#uploading1 .span").text($("#file").val()); //将 #file 的值赋给 #file_name
    }
  });
  //双击凭证列表查看或编辑凭证
  $("#voucher-list").on('dblclick', 'tr', function () {
    $(this).children('.opt').children('.edit').click();
  })
  //点击凭证字号查看详情
  // $("#voucher-list").delegate('.CKvoucherNO', 'click', function() {
  $("#voucher-list").on('click', '.CKvoucherNO', function () {
    var source = $(this).attr('data-source'),
      vouchID = $(this).parent('td').parent('tr').attr('id');
    var params = {
      'source': source, //进销项
      'vouchID': vouchID //凭证头ID
    };
    postRequest(window.baseURL + '/voucher/getHistoryBill', params, function (res) {
      if (res.success == "true") {
        var list = res.list;
        var str = "";
        var str1 = "";
        var rq = "";
        //判断进项OR销项显示不同列表
        if (source == "0") { //0是进项
          str1 += '<th width="80">发票代码</th>' +
            '<th width="80">发票号码</th>' +
            '<th width="70">开票日期</th>' +
            '<th width="120">销方税号</th>' +
            '<th width="210">销方名称</th>' +
            '<th width="80">金额</th>' +
            '<th width="80">税额</th>' +
            '<th width="80">认证日期</th>' +
            '<th width="80">发票类型</th>'
          if (list.length > 0) {
            for (var i = 0; i < list.length; i++) {
              //发票类型
              var type = list[i].invoiceHead.invoiceType;
              if (type == 1) {
                type = "进项发票";
              } else if (type == 2) {
                type = "销项发票";
              } else {
                type = "";
              }
              //获取凭证体长度
              var bodyList = list[i].bodyList.length;
              //金额
              var namount = "";
              //税额
              var taxAmount = "";
              for (var j = 0; j < bodyList; j++) {
                namount += '<p>' + list[i].bodyList[0].namount + '</p>'
                taxAmount += '<p>' + list[i].bodyList[0].taxAmount + '</p>'
              }
              // //时间戳转换
              // var date = new Date(list[i].invoiceHead.invoiceDate);
              // Y = date.getFullYear() + '-';
              // M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
              // D = date.getDate() + ' ';
              // h = date.getHours() + ':';
              // m = date.getMinutes() + ':';
              // s = date.getSeconds();
              // rq = Y + M + D;
              str += '<tr>' +
                '<td width="80">' + list[i].invoiceHead.invoiceCode + '</td>' +
                '<td width="80">' + list[i].invoiceHead.invoiceNumber + '</td>' +
                '<td width="70">' + formatDate(list[i].invoiceHead.invoiceDate, 'yyyy-MM-dd') + '</td>' +
                '<td width="120">' + list[i].invoiceHead.saleTaxno + '</td>' +
                '<td width="210">' + list[i].invoiceHead.saleCorp + '</td>' +
                '<td width="80">' + namount + '</td>' +
                '<td width="80">' + taxAmount + '</td>' +
                '<td width="80">' + formatDate(list[i].invoiceHead.invoice_confirmdate, 'yyyy-MM-dd') + '</td>' +
                '<td width="80">' + type + '</td>' +
                '</tr>'
            }
          } else {
            str = '<tr class="tc"><td colspan="7" style="height: 80px;text-align: center;">暂无数据</td></tr>'
          }
        } else if (source == "9") { //9是销项
          str1 += '<th width="80">发票代码</th>' +
            '<th width="80">发票号码</th>' +
            '<th width="210">购方企业名称</th>' +
            '<th width="130">购方税号</th>' +
            '<th width="70">开票日期</th>' +
            '<th width="180">商品名称</th>' +
            '<th width="90">规格</th>' +
            '<th width="50">单位</th>' +
            '<th width="50">数量</th>' +
            '<th width="50">单价</th>' +
            '<th width="50">金额</th>' +
            '<th width="50">税率</th>' +
            '<th width="50">税额</th>'
          if (list.length > 0) {
            for (var i = 0; i < list.length; i++) {
              var bodyList = list[i].bodyList.length; //获取凭证体长度
              comName = "", //商品名称
                spec = "", //规格
                measure = "", //单位
                nnumber = "", //数量
                nprice = "", //单价
                namount = "", //金额
                taxRate = "", //税率
                taxAmount = ""; //税额
              for (var j = 0; j < bodyList; j++) {
                var specS = list[i].bodyList[j].spec,
                  taxRate = list[i].bodyList[j].taxRate;
                if (specS == null) {
                  specS = "";
                }
                if (taxRate == null) {
                  taxRate = "";
                }
                comName += '<p>' + list[i].bodyList[j].comName + '</p>'
                spec += '<p>' + specS + '</p>'
                measure += '<p>' + list[i].bodyList[j].measure + '</p>'
                nnumber += '<p>' + list[i].bodyList[j].nnumber + '</p>'
                nprice += '<p>' + list[i].bodyList[j].nprice + '</p>'
                namount += '<p>' + list[i].bodyList[j].namount + '</p>'
                taxRate += '<p>' + taxRate + '</p>'
                taxAmount += '<p>' + list[i].bodyList[j].taxAmount + '</p>'
              }
              //时间戳转换
              // var date = new Date(list[i].invoiceHead.invoiceDate);
              // Y = date.getFullYear() + '-';
              // M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
              // D = date.getDate() + ' ';
              // h = date.getHours() + ':';
              // m = date.getMinutes() + ':';
              // s = date.getSeconds();
              // rq = Y + M + D;
              str += '<tr>' +
                '<td width="80" >' + list[i].invoiceHead.invoiceCode + '</td>' +
                '<td width="80">' + list[i].invoiceHead.invoiceNumber + '</td>' +
                '<td width="210">' + list[i].invoiceHead.buyCorp + '</td>' +
                '<td width="130">' + list[i].invoiceHead.buyTaxno + '</td>' +
                '<td width="70">' + formatDate(list[i].invoiceHead.invoiceDate, 'yyyy-MM-dd') + '</td>' +
                '<td width="180">' + comName + '</td>' +
                '<td width="90">' + spec + '</td>' +
                '<td width="50">' + measure + '</td>' +
                '<td width="50">' + nnumber + '</td>' +
                '<td width="50">' + nprice + '</td>' +
                '<td width="50">' + namount + '</td>' +
                '<td width="50">' + taxRate + '</td>' +
                '<td width="50">' + taxAmount + '</td>' +
                '</tr>'
            }
          } else {
            str = '<tr class="tc"><td colspan="7" style="height:80px;text-align:center;">暂无数据</td></tr>'
          }
        }
        $("#isInTheOutput").html(str1);
        $("#getHistoryBillList").html(str);
        layer.open({
          type: 1,
          title: '查看凭证字号',
          area: ['90%', '600px'],
          btn: ['确定'],
          content: $getHistoryBill,
          yes: function (index) {
            $getHistoryBill.hide();
            layer.close(index);
          },
          cancel: function () {
            $getHistoryBill.hide();
          }
        });
      } else {
        layer.msg(res.msg || '获取凭证字号异常', {
          icon: 2,
          shade: 0.1
        });
      }
    })
  });

  // table判断是否有滚动条
  function _tableScroll(ele) {
    var $listTable = $('#' + ele),
      $tableBody = $listTable.parents('.layui-table-body'),
      $tableHeader = $tableBody.prev('.layui-table-header');

    if ($listTable.height() > $tableBody.height()) {
      $tableHeader.addClass('side');
    } else {
      $tableHeader.removeClass('side');
    }
  }

  // 新增凭证页面重新获取凭证号
  function _awaitUpdateVcNo() {
    top.$('#top_tabs > li > cite').each(function (idx) {
      if ($(this).text() === '新增凭证') {
        // top.$('#top_tabs_box > div.clildFrame > div.layui-tab-item > iframe').eq(idx).contents().find('#currVoucherNo').val(1);
        top.$('#top_tabs_box > div.clildFrame > div.layui-tab-item > iframe')[idx].contentWindow.InitCertificate.prototype._getVchrNo();
      }
    });
  }
}());
