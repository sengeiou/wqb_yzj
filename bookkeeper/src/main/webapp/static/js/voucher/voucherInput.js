/*
 * @Author: rong
 * @Date: 2018-03-22 16:55:32
 * @Last Modified by: weiqi
 * @Last Modified time: 2018-12-27 17:41:20
 */
var valText = '',
  textKey = '', // 计算器识别ul坐标
  valKey = ''; // 计算器识别li坐标
// subListData = []; // 本地存储会计科目列表

;
(function (w) {
  warehouse();
  var saveFlag = 0; // 保存flag
  function InitCertificate(opt) {
    this.isCheck = opt.isCheck; // isCarryState: 0未结转,1已经结转  getPurviewData(400)!=='block': true无权限
    this.content = opt.content; // 渲染元素的ID
    this.subList = opt.subList; // 科目列表
    this.Type = opt.Type; // {isHasData：是否有值}
    this.vouchID = opt.vouchID || ''; // 编辑凭证：isHasData为true时，传凭证ID，
    this.isProblem = 2; // 凭证是否有问题 1:有问题 2:没问题
    this.listType = 1; // 1来自全部凭证列表 2来自修正凭证列表
    this.voucherNo = null; // 凭证号码
    // this.clickIndex = null;
    // this.index = null;
    this.init();
    this.binding();
  }
  InitCertificate.prototype = {
    init: function () {
      var that = this;
      // 科目列表为空或者重新请求科目状态为true
      if (undefined === top.subListData || 0 === top.subListData.length || true === top.subListRefresh) {
        this._getSubListData(); // 请求科目列表
      }
      if (this.Type.isHasData) {
        this._getCredentials(); // 编辑凭证
      } else {
        this._newCredentials(); // 新增凭证
      }
    },

    // 新增凭证初始化
    _newCredentials: function () {
      $('#book-remark').removeClass('red');
      if (this.isCheck) { // 1已经结转，true无权限
        // 不能新增
        // $('#currVoucherNo, #book-remark').prop('readonly', true);
        this.initiDom();
        // this.isCheck === 1 && layer.alert('您已经一键结转，不能新增凭证', {
        //   icon: 0
        // });
      } else {
        // 可新增
        // $('#currVoucherNo, #book-remark').prop('readonly', false);
        this.initiDom(this.switching);
        // $("#paging").hide();
        // $('#addSave').show();
        $('#content li:first').click(); // 新增凭证，光标定位第一行摘要
      }
    },

    // 新增时 进来初始化数据
    initiDom: function (fn) {
      var that = this;
      var str = '';
      for (var i = 0; i < 4; i++) {
        (function (i) {
          str += that._string()
        }(i));
      }
      this.content.html(str);
      fn && fn.call(this);
    },

    // 获取编辑凭证
    _getCredentials: function (fn) {
      var that = this;
      that.listType = that._getVouchTab();
      if (true === that.isCheck) { // true无权限
        // layer.alert('您没有权限录入修改凭证!', {
        //   icon: 0,
        // });
        $('#currVoucherNo, #book-remark').prop('readonly', true);
        $('#copy, #save').hide();
        $('#content').off();
        that.content.find('ul.book-content > li.w5 > span').removeClass('book-add book-remove');
      }
      $('#cancel').show(); // 显示返回按钮
      postRequest(window.baseURL + '/voucher/toEditVoucher', { 'vouchID': that.vouchID }, function (res) {
        if (res.code === '0') {
          var isCarryState = res.periodStatus.isCarryState,
            voucherHead = res.voucher.voucherHead,
            list = that._hasData(res.voucher.voucherBodyList);
          that._setCredentials(list, voucherHead); // 格式化数据
          that._cancel(); // 返回键事件
          if (1 === isCarryState) {
            $('#book-state').show(); // 显示结转图片
          } else {
            $('#book-state').hide(); // 隐藏结转图片
          }
          if (that.isCheck !== true) {
            // 根据结转状态判断
            that.isCheck = isCarryState;
            if (that.isCheck === 1) { // 1已经结转，只是展示
              $('#currVoucherNo, #book-remark').prop('readonly', true);
              $('#save').hide(); //结账不显示按钮
              $('#cancel').parent().show();
              that.content.find('ul.book-content > li.w5 > span').removeClass('book-add book-remove');
              $('#content').off();
            } else { // 0未结转
              $('#copy, #save').show(); // 显示按钮
              $('#currVoucherNo, #book-remark').prop('readonly', false);
              that.switching();
            }
          }
        } else {
          layer.msg(res.msg || '获取凭证异常', {
            icon: 2,
            shadeClose: true,
            time: 2000000
          });
        }
      });
    },

    // 有值数据展示 list凭证体 head凭证头
    _setCredentials: function (list, head) {
      var data,
        flag = false, // 科目编码和名称不存在
        len = list.length,
        str = '',
        debitAmount,
        creditAmount,
        credit,
        $des = $('#book-remark'),
        date = head.period.split('-'),
        day = new Date(date[0], date[1], 0).getDate();
      $('#period').val(head.period + '-' + day);
      InitCertificate.source = head.source; // 凭证类型
      InitCertificate.isProblem = head.isproblem; // 凭证是否有问题 1:有问题 2:没问题
      this.voucherNo = head.voucherNO;
      document.getElementById('currVoucherNo').value = head.voucherNO; // 显示凭证号

      if (len > 0) {
        for (var i = 0; i < len; i++) {
          data = list[i];
          if ('' === data.subjectID + data.vcsubject) {
            flag = true;
          } else {
            flag = false;
          }
          debitAmount = data.debitAmount.replace(/\./g, '').replace(/\-/g, "");
          creditAmount = data.creditAmount.replace(/\./g, '').replace(/\-/g, "");
          str += '<ul class="book-content">' +
            '<li class="w1" data-abstract="' + data.vcabstact + '">' +
            '<div class="book-text w1 hide">' +
            '<textarea class="book-val">' + data.vcabstact + '</textarea>' +
            '</div>' +
            '<p class="w1 book-focus">' + data.vcabstact + '</p>' +
            '</li>' +
            '<li class="w2 book-subList" data-subtext="' + data.subjectID + '/' + data.vcsubject + '/' + data.direction + '">' +
            '<div class="book-text w2 hide">' +
            (flag === true ? '<textarea class="book-subText"></textarea>' :
              '<textarea class="book-subText">' + data.subjectID + '-' + data.vcsubject + '</textarea>') +
            '</div>' +
            '<div class="w2 book-subList-text book-focus">'

          if (data.isproblem === '1') { // 异常凭证
            str += (flag === true ? '<div class="book-subName ellipsis" style="color:red;"></div>' :
                '<div class="book-subName ellipsis" style="color:red;">' + data.subjectID + '-' + data.vcsubject + '</div>') +
              '<span class="book-msg">' + data.des + '</span>'
          } else {
            str += '<div class="book-subName ellipsis" title="' + data.subjectID + '-' + data.vcsubject + '">' + data.subjectID + '-' + data.vcsubject + '</div>'
          }
          //查看凭证时不显示余额
          //str += '<div class="book-subMoney ellipsis">金额：' + data.directionMoney + '</div>'
          str += '<div class="book-subMoney ellipsis"></div>' +
            '</div>' +
            '</li>' +
            '<li class="w3 book-self-num" data-num="' + data.number + '">' +
            '<div class="book-text w3 hide">' +
            '<input class="book-num ime-disabled" value="' + data.number + '"/>' +
            '</div>' +
            '<p class="w3 tr book-focus">' + data.number + '</p>' +
            '</li>' +
            '<li class="w3  book-self-amount" data-amount="' + data.price + '">' +
            '<div class="book-text w3 hide">' +
            '<input class="book-amount ime-disabled" value="' + data.price + '" />' +
            '</div>' +
            '<p class="w3 tr book-focus">' + data.price + '</p>' +
            '</li>' +
            '<li class="w4 book-left-debit" data-debit="' + (data.debitAmount || '0.00') + '">' +
            '<div class="w4 hide">' +
            '<input type="text" class="book-debit w4 ime-disabled" maxlength="12" value="' + data.debitAmount + '" >' +
            '</div>' +
            '<p class="w4 book-img book-focus">' +
            '<span' + (data.debitAmount < 0 ? ' style="color:red"' : '') + '>' + debitAmount + '</span>'
          // if (data.debitAmount < 0) {
          //   str += '<span style="color:red">' + debitAmount + '</span>'
          // } else {
          //   str += '<span>' + debitAmount + '</span>'
          // }

          str += '</p>' +
            '</li>' +
            '<li class="w4 book-right-credits" data-credits="' + (data.creditAmount || '0.00') + '">' +
            '<div class="w4 hide">' +
            '<input type="text" class="book-credits w4 ime-disabled" maxlength="12" value="' + data.creditAmount + '">' +
            '</div>' +
            '<p class="w4 book-img book-focus">'

          if (data.creditAmount < 0) {
            str += '<span style="color:red">' + creditAmount + '</span>'
          } else {
            str += '<span>' + creditAmount + '</span>'
          }

          str += '</p>' +
            '</li>' +
            '<li class="w5">' +
            '<span class="book-add layui-icon" title="向下增行">&#xe654;</span>' +
            '<span class="book-remove layui-icon" title="删除本行">&#x1006;</span>' +
            '</li>' +
            '</ul>';
        }
      } else {
        // 没有分录数据
      }
      this.content.html(str);
      this._total(); // 获取合计总数
      $des.val(head.des); // 备注
      if (InitCertificate.isProblem == '1') {
        $des.addClass('red');
      } else if (InitCertificate.isProblem == '2') {
        $des.removeClass('red');
        // 没问题的凭证，处理附征税的凭证4位小数转2位小数显示造成的借贷方金额合计可能不平衡的问题（影响：编辑凭证后会改变贷方最后一条凭证分录金额）
        // 借贷不平衡
        if (InitCertificate.debitVal != InitCertificate.creditsVal) {
          var $li = this.content.find('ul:last > li.book-right-credits');
          credit = parseFloat($li.attr('data-credits')) + (InitCertificate.debitVal - InitCertificate.creditsVal); // 借-贷
          credit = credit.toFixed(2);
          creditAmount = credit.replace('.', '');
          $li.attr('data-credits', credit)
            .find('input').val(credit)
            .end()
            .find('.book-img span').text(creditAmount);
          $('#credits').text($('#debit').text()); // 贷方合计 = 借方合计
          InitCertificate.creditsVal = InitCertificate.debitVal;
        }
      }
      $('#form').val(head.createpsn); // 制单人
    },

    //DOM 操作方法（不区分权限）
    binding: function () {
      var that = this;
      // 上一张凭证查看
      $('#prevVc').off('click').on('click', function () {
        that._getSibling('prev');
      });
      // 下一张凭证查看
      $('#nextVc').off('click').on('click', function () {
        that._getSibling('next');
      });
    },

    // 获取编辑凭证页面是从生成凭证页面哪个列表打开的
    _getVouchTab: function () {
      var type = window._queryStringHash('source'); // 主页刷新之后会清除location.search
      if ('' === type) {
        // 生成凭证页面tab
        if (window.sessionStorage.editVouchTab === '1') {
          type = 2;
        } else {
          type = 1;
        }
      }
      return type;
    },

    // 获取上下凭证
    _getSibling: function (place) {
      var that = this;
      if (window.sessionStorage.editVouch) {
        var editVouch = JSON.parse(window.sessionStorage.editVouch);
      } else {
        console.log('编辑凭证页面未找到 editVouch');
        return;
      }
      switch (place) {
      case 'prev':
        var url = window.baseURL + '/vat/previousVb';
        break;
      case 'next':
        var url = window.baseURL + '/vat/nextVb';
        break;
      }
      var params = {
        type: that.listType, // 1查询全部凭证 2查询修正凭证
        vid: that.vouchID, // 凭证主键
        voucherNo: $('#currVoucherNo').val() // 凭证号
      };
      postRequest(url, params, function (res) {
        if ('0' === res.code) {
          layer.msg('切换凭证成功', {
            icon: 1,
            time: 1000
          });
          var voucherHead = res.data.voucherHead,
            list = that._hasData(res.data.voucherBodyList);
          that.vouchID = voucherHead.vouchID;
          window.sessionStorage.editVouch = window.sessionStorage.editVouch.replace(that.vouchID, voucherHead.vouchID); // 修改sessionStorage.editVouch
          that._setCredentials(list, voucherHead); // 格式化数据
          // var voucherHead = res.data.voucherHead;
          // sessionStorage.editVouch = sessionStorage.editVouch.replace(that.vouchID, voucherHead.vouchID); // 修改sessionStorage.editVouch
          // new InitCertificate({
          //   'isCheck': that.isCheck,
          //   'content': $('#content'),
          //   'subList': $('#subList'),
          //   'Type': {
          //     'isHasData': true
          //   },
          //   'vouchID': voucherHead.vouchID
          // });
        } else if ('5' === res.code) {
          // 当前凭证时第一张/最后一张
          layer.msg(res.msg || '获取凭证上下页信息异常', {
            icon: 0,
            shade: 0.1,
            shadeClose: true,
            time: 10000
          });
        } else {
          layer.msg(res.msg || '获取凭证上下页信息异常', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000
          });
        }
      });
    },

    // DOM 操作方法（需要权限）
    switching: function () {
      var that = this,
        $str = this._string(),
        $text,
        $this;
      // 隐藏 P 标签， 显示输入框
      this.content.off('click').on('click', 'li', function () {
          InitCertificate.clickIndex = $(this).parents('ul.book-content').index();
          $(this).children('p').hide().end().children('div').show().children().focus();
          // 切换时候也要隐藏
          $('#subLists').scrollTop(0); // 当一个元素的display属性为'none'时，对该元素设置scrollTop属性是无效的
          that.subList.stop().slideUp(0);
          // that._subJectList();
          // .find('li').eq(0).addClass('on')
          //   .siblings().removeClass('on');
        })

        // 科目列表
        .on('click', '.book-subList', function (event) {
          var e = window.event || event;
          // 阻止冒泡
          e.cancelBubble = true || e.stopPropagation();

          InitCertificate.index = $(this).parents('ul.book-content').index(); // 获取当前索引

          // 点击获取光标
          $(this).children('.book-subList-text').hide().end().children('.book-text').show().children().focus();
          // 显示科目列表
          that._subListPosition(that.subList);

          // 显示选择的科目
          var val = $(this).attr('data-subtext') || '';
          val = val.length ? val.split('/')[0] + '-' + val.split('/')[1] : '';
          $(this).children('.book-text').children().val(val).select();
        })

        // 点击添加下一行
        .on('click', '.book-add', function () {
          $(this).parents('ul.book-content').after($str);
        })

        // 点击删除当前行数
        .on('click', '.book-remove', function () {

          // 页面至少保留两行
          var $bookContent = $('.book-content').length;
          if ($bookContent <= 3) { // 因为还有合计一行
            layer.msg('最少剩下两行');
            return false;
          }
          $(this).parents('ul.book-content').remove();
          that._total();
        })

      // 隐藏输入框 ， 显示P 标签
      this.content.off('blur').on('blur', '.book-val', function () { // 摘要
          $text = $(this).val() || '';
          $(this).parent('div').hide().next().show().text($text);
          $(this).parent().parent().attr('data-abstract', $text);
        })

        //点击数量
        .on('click', '.book-self-num', function () {
          var $this = $(this),
            $parent = $this.parent();
          // 获取选中的科目编码，可填写单价和数量编号：1403,1405,1408
          var val = $this.siblings('.book-subList').find('textarea').val().substr(0, 4);
          valText = val;
          if (val == '1403' || val == '1405' || val == '1408') { // 可输入
            //获取ul下标
            textKey = $this.parents('ul.book-content').index();
            //获取li下标
            valKey = $this.index();
            $parent.find(".book-num").attr("readonly", false);
            $parent.find(".book-amount").attr("readonly", false);
          } else {
            // 不可输入，清空值
            $this.find('input').blur();
            $parent.children(".book-self-num").attr("data-num", '0')
              .find(".book-num").val('').attr("readonly", true);
            $parent.children(".book-self-amount").attr("data-amount", '0')
              .find(".book-amount").val('').attr("readonly", true);
            $parent.find('.book-focus.w3').text('');
            if (val === '' || val === '-') { // 未选择科目
              layer.msg('请先选择科目');
            } else { // 禁止输入的科目
              layer.msg('您选择的科目不可输入单价和金额');
            }
          }

          /*if ($parent.find('.book-subName').text() == '') { // 不可输入，清空值
            $parent.children(".book-self-num").attr("data-num", '0')
              .find(".book-num").val('').attr("readonly", true);
            $parent.children(".book-self-amount").attr("data-amount", '0')
              .find(".book-amount").val('').attr("readonly", true);
            $parent.find('.book-focus.w3').text('');
            layer.msg('请先选择科目');
          } else {
            //获取选中的科目编码
            var val = $(this).siblings('.book-subList').children('.book-focus').children('.book-subName').text().substring(0, 4);
            valText = val;
            //编号为1403or1405方可填写单价和数量
            if (val == '1403' || val == '1405' || val == '1408') {
              //获取ul下标
              textKey = $(this).parents('ul.book-content').index();
              //获取li下标
              valKey = $(this).index();
              $(".book-num").attr("readonly", false);
              $(".book-amount").attr("readonly", false);
            } else {
              $(".book-num").attr("readonly", true);
              $(".book-amount").attr("readonly", true);
              layer.msg('您选择的科目不可输入单价和金额');
            }
          }*/
        })

        //点击单价
        .on('click', '.book-self-amount', function () {
          var $this = $(this),
            $parent = $this.parent();
          // 获取选中的科目编码，可填写单价和数量编号：1403,1405,1408
          var val = $this.siblings('.book-subList').find('textarea').val().substr(0, 4);
          valText = val;
          if (val == '1403' || val == '1405' || val == '1408') { // 可输入
            //获取ul下标
            textKey = $this.parents('ul.book-content').index();
            //获取li下标
            valKey = $this.index();
            $parent.find(".book-num").attr("readonly", false);
            $parent.find(".book-amount").attr("readonly", false);
          } else {
            // 不可输入，清空值
            $this.find('input').blur();
            $parent.children(".book-self-num").attr("data-num", '0')
              .find(".book-num").val('').attr("readonly", true);
            $parent.children(".book-self-amount").attr("data-amount", '0')
              .find(".book-amount").val('').attr("readonly", true);
            $parent.find('.book-focus.w3').text('');
            if (val === '' || val === '-') { // 未选择科目
              layer.msg('请先选择科目');
            } else { // 禁止输入的科目
              layer.msg('您选择的科目不可输入单价和金额');
            }
          }
          /*if ($('.book-subName').text() == '') {
            $(".book-num").attr("readonly", true);
            $(".book-amount").attr("readonly", true);
            layer.msg('请先选择科目');
          } else {
            var val = $(this).siblings('.book-subList').children('.book-focus').children('.book-subName').text().substring(0, 4);
            valText = val;
            if (val == '1403' || val == '1405' || val == '1408') {
              //获取ul下标
              textKey = $(this).parents('ul.book-content').index();
              //获取li下标
              valKey = $(this).index();
              $(".book-num").attr("readonly", false);
              $(".book-amount").attr("readonly", false);
            } else {
              $(".book-num").attr("readonly", true);
              $(".book-amount").attr("readonly", true);
              layer.msg('您选择的科目不可输入单价和金额');
            }
          }*/
        })

        //点击借方金额
        .on('click', '.book-left-debit', function () {
          //获取ul下标
          textKey = $(this).parents('ul.book-content').index();
          //获取li下标
          valKey = $(this).index();
        })

        //点击贷方金额
        .on('click', '.book-right-credits', function () {
          //获取ul下标
          textKey = $(this).parents('ul.book-content').index();
          //获取li下标
          valKey = $(this).index();
        })

        // 科目列表
        .on('blur', '.book-subText', function () {
          $(this).parent('div').hide().next().show();
        })

        // 数量 必填数字
        .on('blur', '.book-num', function () {
          $this = $(this);
          var val = $this.val().split(',').join('');
          $text = val || '0';
          that._hide($this, $text, null, null, function () {
            $this.parent().parent().attr('data-num', $text);
          });
          that._toCalculate(); // 自动计算价格
          that._toTheUnitPrice($this.parents('ul').index());
        })

        // 单价 必填数字
        .on('blur', '.book-amount', function () {
          $this = $(this);
          var val = $this.val().split(',').join('');
          $text = val === '' ? '0' : parseFloat(val).toFixed(2);
          that._hide($this, $text, 1, null, function () {
            $this.parent().parent().attr('data-amount', $text);
          });
          that._toCalculate(); // 自动计算价格
        })

        // 借方金额输入必填数字
        .on('blur', '.book-debit', function () {
          $this = $(this);
          $text = $this.val() || '0'; // 用户输入的值
          var val = (parseFloat($text)).toFixed(2); // 存借方金额
          var next = $this.parent().parent().next(); // 下一个兄弟元素
          if ($this.val() === '') {
            // debugger
            $this.parent('div').hide().next().show().children().text('');
            $this.parent().parent().attr('data-debit', '');
            that._total();
          } else {
            that._hide($this, $text, 1, 1, setNextData); // 判断单价 和 金额输入，数量 ：输入的是否是数字
          }

          function setNextData() {
            if (next.attr('space')) {
              next.removeAttr('space').attr('data-credits', val).find('input').val(val);
              $this.val('').parent().parent().attr('data-debit', '0.00').children('p').children().text('');
            } else {
              next.attr('data-credits', '0.00').find('input').val('');
              next.children('p').children().text('');
              $this.parent().parent().attr('data-debit', val);
            }

            that._total();
          }

          // that._hide($this, $text, 1, 1, setNextData); // 判断单价 和 金额输入，数量 ：输入的是否是数字
          // // 判断是借方还是贷方输入金额
          // function setNextData() {
          //   if (next.attr('data-credits') > '0.00') {
          //     val = next.attr('data-credits');
          //     text = next.attr('data-credits', '0.00').children('p').children().text();
          //     next.attr('data-credits', '0.00').children('p').children().text('');
          //     $this.parent().parent().attr('data-debit', val).children('p').children().text(text);
          //   } else {
          //     next.attr('data-credits', '0.00').children('p').children().text('');
          //     $this.parent().parent().attr('data-debit', val);
          //   }
          //   that._total();
          // };
          that._toTheUnitPrice($this.parents('ul').index());
        })

        // 贷方金额输入必填数字
        .on('blur', '.book-credits', function () {
          $this = $(this);
          $text = $this.val() || '0';
          var val = (parseFloat($text)).toFixed(2); // 存贷方金额
          var next = $this.parent().parent().prev(); // 上一个兄弟元素
          if ($this.val() === '') {
            // debugger
            $this.parent('div').hide().next().show().children().text('');
            $this.parent().parent().attr('data-credits', '');
            that._total();
          } else {
            that._hide($this, $text, 1, 1, setPrevData); // 判断单价 和 金额输入，数量 ：输入的是否是数字
          }

          // 判断是借方还是贷方输入金额
          function setPrevData() {
            if (next.attr('space')) {
              next.removeAttr('space').attr('data-debit', val).find('input').val(val);
              $this.parent().parent().attr('data-credits', '0.00').children('p').children().text('');
            } else {
              next.attr('data-debit', '0.00').find('input').val('');
              next.children('p').children().text('');
              $this.parent().parent().attr('data-credits', val);
            }

            that._total();
          };
          // that._hide($this, $text, 1, 1, setPrevData); // 判断单价 和 金额输入，数量 ：输入的是否是数字
          // // 判断是借方还是贷方输入金额
          // function setPrevData() {
          //   if (prev.attr('data-debit') > '0.00') {
          //     val = prev.attr('data-debit');
          //     text = prev.attr('data-debit', '0.00').children('p').children().text();
          //     prev.attr('data-debit', '0.00').children('p').children().text('');
          //     $this.parent().parent().attr('data-credits', val).children('p').children().text(text);
          //   } else {
          //     prev.attr('data-debit', '0.00').children('p').children().text('');
          //     $this.parent().parent().attr('data-credits', val);
          //   }
          //   that._total();
          // };
          that._toTheUnitPrice($this.parents('ul').index());
        });


      // 获取焦点，选中内容 .book-val, .book-num, .book-amount, .book-subText, .book-debit,.book-credits
      this.content.off('focus').on('focus', 'input, textarea', function () {
          $(this).select();
        })

        // 获取科目的指向this
        .on('focus', '.book-subText', function () {
          $this = $(this);
          // 搜索当前选择科目
          var val = $this.val();
          if ($this.val === '-') {
            val = '';
            // $this.val(val);
          }
          that._subJectList(val);
        })

      // 如果是负数，修改字体的颜色
      this.content.off('input').on('input', '.book-debit, .book-credits', function () {
          var val = $(this).val();
          $(this).val(val.replace(/[^\d.-]/g, '')); // 值允许输入数字和-减号，.小数点
          // 如果的负数，改变颜色
          var reg = /\-/g;
          reg.test(val) ? $(this).css('color', 'red') : $(this).css('color', '#333');
        })

        // 模糊搜索
        .on('input', '.book-subText', that._debounce(30, function () {
          var val = $this.val();
          that._subJectList(val);
        }));


      // 科目选择条目
      this.subList.off('click').on('click', '.sub-title', function () {
        // 科目列表无数据
        if (top.subListData.length === 0) {
          return;
        }
        // 搜索条件无数据
        if ('暂无数据' === $(this).text()) {
          return;
        }
        var code = $(this).attr('data-code'),
          codeName = code.split('/'),
          subCodeL = codeName[0].substring(0, 4),
          subCode = codeName[0] + '-' + codeName[1], // 科目编码科目名字
          direction = codeName[2], // 借贷方向
          debit = codeName[3] === 'null' ? "0.00" : +codeName[3], // 借方金额
          credit = codeName[4] === 'null' ? "0.00" : +codeName[4], // 贷方金额
          // 写入科目
          item = direction === '1' ? (debit - credit) : (credit - debit), // 借贷金额
          dire = direction === '1' ? '(借)' : '(贷)', // 借贷方向
          moneyCode = (+item).toFixed(2) + ' ' + dire, //  默认小数两位
          parent = that.content.children('ul').eq(InitCertificate.index).children().eq(1); // li.book-subList
        valText = subCodeL;
        //如果选择非1403或1405科目，则清除单价和数量的值
        if (subCodeL != '1403' && subCodeL != '1405' && subCodeL != '1408') {
          var list = that.content.children(),
            index = InitCertificate.clickIndex,
            parents = list.eq(index).children();
          parents.eq(2).attr('data-amount', '');
          parents.eq(2).children().eq(1).text('');
          parents.eq(2).children().eq(0).children().val(''); //单价
          parents.eq(3).attr('data-amount', '');
          parents.eq(3).children().eq(1).text('');
          parents.eq(3).children().eq(0).children().val(''); //数量
        }
        var dataSubtext = '期末余额：' + moneyCode;
        if (subCodeL == "1405" || subCodeL == "1403" || subCodeL == "1408") {
          var param = { 'subCode': codeName[0] };
          var urls = window.baseURL + '/vat/queryQmNum';
          postRequest(urls, param, function (res) {
            if (res.code == "0") {
              if (res.msg) {
                if (res.msg.qm_balanceNum) {
                  dataSubtext += '      期末数量：' + res.msg.qm_balanceNum;
                } else {
                  dataSubtext += '      期末数量：' + "0";
                }
              } else {
                dataSubtext += '      期末数量：' + "未查询到此科目数据";
              }
            } else {
              dataSubtext += '      期末数量：' + "查询异常";
            }
            parent.find('.book-subList-text').children('.book-subName').text(subCode).attr('title', subCode).css('color', '#333').next().text(dataSubtext);
          })
        } else {
          parent.find('.book-subList-text').children('.book-subName').text(subCode).attr('title', subCode).css('color', '#333').next().text(dataSubtext);
        }
        // 回写科目
        parent.attr('data-subtext', code);
        parent.find('textarea.book-subText').val($(this).text());
        /*parent.find('.book-subList-text').children('.book-subName').text(subCode).attr('title', subCode).css('color', '#333')
          .next().text('金额：' + moneyCode);
        parent.attr('data-subtext', code);*/

      });

      // 凭证号
      $('#currVoucherNo')
        .off('input').on('input', function () {
          positiveInteger($(this));
        })
        .off('blur').on('blur', function () {
          if (!$(this).val()) {
            $(this).val(1)
          }
        });

      // 复制粘贴
      /*$('#copy').on('click', function () {
        //that._copy();
      });*/

      // 保存并新增
      $('#addSave').off('click').on('click', function () {
        that._submint();
      });

      //上传凭证
      $(':file').off('change').on('change', function () {
        var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
        $(this).prev().text(v);
        $(this).parent().next().removeClass('loading loaded');
      });

      // 保存编辑
      $('#save').off('click').on('click', function () {
        that._edit();
      });

      // 点击新增科目
      $('#sub-addSub').off('click').on('click', function (event) {
        var e = window.event || event,
          index = InitCertificate.clickIndex;
        e.cancelBubble = true || e.stopPropagation(); //阻止冒泡
        new Subject({
          'elem': $('#addSub'), // 唤醒弹窗的id
          'addUnit': $('#addUnit'), // 唤醒弹窗的id
          'SubElem': $('#setSubData'), // 科目列表id
          'codeElem': $('#setSubCode'), // 科目编码id
          'SurpluElem': $('#setSubSurplus'), // 数量核算id
          'SubValElem': $('#getSubName'), // 科目名称
          'UnitSymbol': $('#UnitSymbol'), // 计量单位符号
          'UnitName': $('#UnitName'), // 计量单位名称
          'UnitRemarks': $('#UnitRemarks'), // 备注
          'setDirection': $('#setDirection'), // 设置单选按钮
          'indexLength': index,
          'refresh': refresh // 新增科目回调函数
        });

        function refresh(params) {
          // 回写
          var $ul = that.content.children().eq(index),
            str = params.subCode + '-' + params.fullName,
            str2 = params.dir == '1' ? '金额：0.00(借方)' : '金额：0.00(贷方)';
          $ul
            .find('.book-subList').attr('data-subtext', params.subCode + '/' + params.fullName + '/' + params.dir)
            .end()
            .find('.book-subText').val(str)
            .end()
            .find('.book-subName').text(str)
            .end()
            .find('.book-subMoney').text(str2);
          that._getSubListData(); // 请求科目列表
        }
      });

      // 点击隐藏科目列表，未输入内容时默认第一个选中
      $('html').off('click').on('click', function (event) {
        // 如果是键盘事件就不执行隐藏
        var e = window.event || event,
          keyCode = e.keyCode ? e.keyCode : e.which;
        // Tab Enter Spacebar
        if (keyCode === 9 || keyCode === 13 || keyCode === 32) {
          return false;
        }
        // 解决火狐会隐藏科目列表
        if (!that.content.find('textarea.book-subText').eq(InitCertificate.index).is(":focus")) {
          $('#subLists').scrollTop(0);
          that.subList.stop().slideUp(0);
          that._subJectList();
        }
        // .find('li').eq(0).addClass('on')
        //   .siblings().removeClass('on');
      });

      // 键盘事件
      this.content.off('keydown').on('keydown', 'li', function (event) {
          var e = window.event || event,
            keyCode = e.keyCode ? e.keyCode : e.which,
            $this = $(this),
            $parent = $this.parent();

          // 监听 Tab键 点击切换
          if (keyCode === 9) {
            var $parent = $this.parent();
            // 会计科目
            if ($this.hasClass('book-subList')) { // 会计科目
              // 监听 Tab键 点击切换
              // 判断subCode
              if (!e.shiftKey) {
                var val = $this.find('textarea').val().substr(0, 4);
                valText = val;
                if (val == '1403' || val == '1405' || val == '1408') { // 可输入
                  //获取ul下标
                  textKey = $this.parents('ul.book-content').index();
                  //获取li下标
                  valKey = $this.index();
                  $parent.find(".book-num").attr("readonly", false);
                  $parent.find(".book-amount").attr("readonly", false);
                  $this.next().find('.book-focus').off('click').click();
                  return false;
                } else {
                  // 不可输入，清空值
                  $parent.children(".book-self-num").attr("data-num", '0')
                    .find(".book-num").val('').attr("readonly", true);
                  $parent.children(".book-self-amount").attr("data-amount", '0')
                    .find(".book-amount").val('').attr("readonly", true);
                  $parent.find('.book-focus.w3').text('');

                  $this.siblings('.book-left-debit').find('.book-focus').off('click').click();
                  return false;
                }
              }
              // Shift+Tab  跳转上一栏
              else if (e.shiftKey) {
                $this.prev().find('.book-focus').off('click').click();
                return false;
              }

            }
            // 借方金额
            else if ($this.hasClass('book-left-debit')) { // 借方金额
              // 监听 Tab键 点击切换
              if (!e.shiftKey) {
                $this.next().find('.book-focus').off('click').click();
                return false;
              }
              // Shift+Tab  跳转上一栏
              // 判断subCode
              else if (e.shiftKey) {
                var val = $this.siblings('.book-subList').find('textarea').val().substr(0, 4);
                valText = val;
                if (val == '1403' || val == '1405' || val == '1408') { // 可输入
                  //获取ul下标
                  textKey = $this.parents('ul.book-content').index();
                  //获取li下标
                  valKey = $this.index();
                  $parent.find(".book-num").attr("readonly", false);
                  $parent.find(".book-amount").attr("readonly", false);
                  $this.prev().find('.book-focus').off('click').click();
                  return false;
                } else {
                  // 不可输入，清空值
                  $parent.children(".book-self-num").attr("data-num", '0')
                    .find(".book-num").val('').attr("readonly", true);
                  $parent.children(".book-self-amount").attr("data-amount", '0')
                    .find(".book-amount").val('').attr("readonly", true);
                  $parent.find('.book-focus.w3').text('');

                  $this.siblings('.book-subList').find('.book-focus').off('click').click();
                  return false;
                }
              }
            }
            // 监听 Tab键 点击切换
            if (!e.shiftKey) {
              $this.next().find('.book-focus').off('click').click();
              return false;
            }
            // Shift+Tab  跳转上一栏
            else if (e.shiftKey) {
              $this.prev().find('.book-focus').off('click').click();
              return false;
            }
          }

          // 监听 enter键 点击切换
          if (keyCode === 13) {
            if ($this.hasClass('book-subList')) { // 会计科目
              var $on = that.subList.find('.on');
              if ($on.length) {
                // 默认选择第一条
                $on.click();
                // 获取选中的科目编码，可填写单价和数量编号：1403,1405,1408
                var val = $on.text().substr(0, 4);
                valText = val;
                if (val == '1403' || val == '1405' || val == '1408') { // 可输入
                  //获取ul下标
                  textKey = $this.parents('ul.book-content').index();
                  //获取li下标
                  valKey = $this.index();
                  $parent.find(".book-num").attr("readonly", false);
                  $parent.find(".book-amount").attr("readonly", false);
                } else { // 不可输入，清空值，跳到借方金额
                  $parent.children(".book-self-num").attr("data-num", '0')
                    .find(".book-num").val('').attr("readonly", true);
                  $parent.children(".book-self-amount").attr("data-amount", '0')
                    .find(".book-amount").val('').attr("readonly", true);
                  $parent.find('.book-focus.w3').text('');
                  // layer.msg('您选择的科目不可输入单价和金额');
                  $this.siblings('.book-left-debit').find('.book-focus').off('click').click();
                  return false;
                }
              } else {
                layer.msg('请输入科目', {
                  time: 2000
                });
                return false;
              }
            } else if ($this.hasClass('w4') && $this.find('input').val() != 0) { // 金额
              // 追加最后一行
              if (!$parent.next().length) {
                $parent.after(that._string());
              }
              // 跳转下一行
              $parent.next().find('li:first .book-focus').off('click').click();
              return false;
            } else if ($this.hasClass('book-right-credits')) { // 贷方金额
              layer.msg('请输入金额', {
                time: 2000
              });
              return false;
            }
            // 点击下一个li
            $this.next().find('.book-focus').off('click').click();
            return false;
          }

          // 监听 空格键 点击切换借贷方金额
          if (keyCode === 32 && $this.hasClass('w4')) {
            return false;
          }

          // 监听 F8键 输入框失去焦点
          if (keyCode === 119) {
            $this.find('textarea,input').blur();
            // 隐藏科目列表
            if ($this.hasClass('book-subList')) {
              $('html').click();
            }
          }

          // 上下箭头选择会计科目
          if (that.subList.is(":visible")) {
            if (keyCode == 38) { // up
              var _top = $('#subLists').scrollTop(), // 会计科目列表高度
                _index = that.subList.find('li.on').index(); // 选中的会计科目下标
              if (_index) {
                _index--;
                that.subList.find('li').eq(_index).addClass('on')
                  .siblings().removeClass('on');
                _index > 3 && $('#subLists').scrollTop(_top - 29);
              }
            } else if (keyCode == 40) { // down
              var _top = $('#subLists').scrollTop(), // 会计科目列表高度
                len = that.subList.find('li.sub-title').length, // 可选择的会计科目总数
                _index = that.subList.find('li.on').index(); // 选中的会计科目下标
              if (len !== 1 && _index < len) {
                _index++;
                that.subList.find('li').eq(_index).addClass('on')
                  .siblings().removeClass('on');
                _index > 4 && $('#subLists').scrollTop(_top + 29);
              }
            }
          }
        })
        .on('keyup', 'li', function (event) {
          var e = window.event || event,
            keyCode = e.keyCode ? e.keyCode : e.which;

          // 借贷金额输入框
          if ($(this).hasClass('w4')) {
            // 监听 空格键 点击切换借贷方金额
            if (keyCode === 32) {
              $(this).siblings('.w4').attr('space', 1).find('.book-focus').off('click').click();
            }
            // 借贷金额平衡 =
            else if (!e.altKey && keyCode == 187) {
              that._balance($(this));
            }
          }
        })

      // 窗口绑定事件
      $(window).on('keydown', function (event) {
        var e = window.event || event,
          keyCode = e.keyCode ? e.keyCode : e.which;

        // 监听 F8键 点击保存
        // 先让停留在表格的光标失去焦点，完成保存校验
        if (keyCode === 119) {
          //that.content.find('textarea,input').blur();
          if ($('#addSave').is(':visible')) {
            that._submint();
          } else {
            that._edit();
          }
          return false;
        }
      }).on('blur', function () {
        that.subList.stop().slideUp(200);
      });

      // 键盘快捷键
      $(document).on('keyup', function (event) {
        var e = event || window.event,
          keyCode = e.keyCode ? e.keyCode : e.which;

        // Ctrl+Delect  清空计算器
        if (e.ctrlKey && keyCode == 46) {
          $("#simpleClearAllBtn").click();
        }
        // 借贷平衡
        if (e.altKey && keyCode == 187) {
          PHmoney();
        }
        //alt+F10打开计算器
        if (e.altKey && keyCode == 121) {
          calculator.clearAll(); // 清空计算器
          $("#calculator_main").show();
        }
        //alt+F11关闭计算器
        if (!e.ctrlKey && e.altKey && keyCode == 122) {
          $("#calculator_main").hide();
        }
        //Ctrl+Alt+F11关闭计算器并取值
        else if (e.ctrlKey && e.altKey && keyCode == 122) {
          var resultIpt = Math.floor($("#resultIpt").val() * 1000) / 1000;
          var resultIptS = resultIpt.toFixed(2);
          var liVal = $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ")");
          $("#calculator_main").hide();
          if (valKey == 4 || valKey == 5) {
            liVal.find("input").focus().val(resultIpt).blur();
            /*$(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") p span").text(resultIptS.replace(/\./g, ""));
            $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") div input").val(resultIpt);
            if (valKey == 4) {
              $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ")").attr("data-debit", resultIptS);
              var ulLength = $("#content ul").length,
                heji = 0;
              for (var i = 0; i < ulLength; i++) {
                heji += parseFloat($("#content ul:eq(" + i + ") .book-left-debit").attr("data-debit")) || 0;
              }
              heji = parseFloat($("#debit").text()) + parseFloat(resultIptS.replace(/\-/g, "").replace(/\./g, ""));
              $("#debit").text(heji);
              var hejiS = heji + "";
              var xiaoshu = hejiS.substr(hejiS.length - 2);
              var zhengshu = hejiS.substr(0, hejiS.length - 2);
              var zongji = zhengshu + "." + xiaoshu;
              var momey = _formatA(zongji);
              $('#formatAmount').text(momey);
            } else if (valKey == 5) {
              $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ")").attr("data-credits", resultIptS);
              var heji = parseFloat($("#credits").text()) + parseFloat(resultIptS.replace(/\-/g, "").replace(/\./g, ""));
              $("#credits").text(heji);
            }*/
          } else if (valKey == 2) {
            liVal.find("input").focus().val(resultIpt).blur();
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ")").attr("data-debit", resultIpt);
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") p span").text(resultIpt);
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") div input").val(resultIpt);
          } else if (valKey == 3) {
            liVal.find("input").focus().val(resultIpt).blur();
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ")").attr("data-debit", resultIptS);
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") p span").text(resultIptS);
            // $(".book-content:eq(" + textKey + ") li:eq(" + valKey + ") div input").val(resultIpt);
          }
        }
      });

      $('#navDOM1').parent().before($('#shortcuts-hints'));
      // 键盘快捷键提示
      $('#i-hints,#shortcuts-hints').on('mouseenter', function () {
        $('#shortcuts-hints').stop().slideDown(0);
      }).on('mouseleave', function () {
        $('#shortcuts-hints').stop().slideUp(0);
      });
    },

    // 金额输入框计算借贷平衡
    _balance: function (item) {
      //凭证ul行数、平衡差、借方金额和、贷方金额和
      var that = this,
        $ul = $("#content ul"),
        _len = $ul.length,
        $this = $(item),
        $other = $(item).siblings('.w4'),
        _index = $this.parent().index(), // 当前行数
        momey = 0, // 平衡金额
        debitVal = 0, // 借方金额
        creditsVal = 0; // 贷方金额

      // 判断科目名称是否为空
      if ($this.siblings('.book-subList').attr("data-subtext") == undefined) {
        alert("请先选择科目");
      } else {
        //循环算出借方总数和贷方总数
        for (var i = 0; i < _len; i++) {
          if (i == _index) continue; // 计算除当前行外的借贷金额
          debitVal += parseFloat($("#content ul:eq(" + i + ") .book-left-debit").attr("data-debit")) || 0;
          creditsVal += parseFloat($("#content ul:eq(" + i + ") .book-right-credits").attr("data-credits")) || 0;
        }
        momey = Math.abs(debitVal - creditsVal); // 借贷差额
        momey = parseFloat(momey.toFixed(2));
        if ($this.hasClass('book-left-debit')) { // 借方
          if (debitVal > creditsVal) { // 借 > 贷
            momey = momey * -1;
          }
          $this.attr("data-debit", momey);
          $other.attr('data-credits', '0.00');
        } else if ($this.hasClass('book-right-credits')) { // 贷方
          if (debitVal < creditsVal) { // 借 < 贷
            momey = momey * -1;
          }
          $this.attr("data-credits", momey);
          $other.attr('data-debit', '0.00');
        }
        $this.find('input').val(momey);
        $other.find('input').val('');
        $other.children('p').children().text('');
        that._total();
      }
    },

    // 判断单价 和 金额输入，数量 ：输入的是否是数字
    _hide: function (el, value, type, price, fn) {
      var val = value.replace(/\s+/g, ""); // 去掉空格
      if (val && isNaN(parseFloat(val))) {
        layer.msg('请输入数字');
        el.parent('div').hide().next().show();
        return false;
      }

      // 先判断是不是 单价 和 金额输入， 数量
      if (type) {

        var money = this._priceType(el, val);
        if (!money) {
          return false;
        }

        if (price) { //金额输入
          var moneyNum = money.replace(/\./g, "");
          el.parent('div').hide().next().show().children('span').text(moneyNum);
        } else { // 单价
          el.parent('div').hide().next().show().text(money);
        }

      } else { //数量
        /*  if (val.length > 10) {
           layer.msg('数量输入长度仅限为4位，请从新输入');
           el.parent('div').hide().next().show();
           return false;
         } */
        // 单价
        el.parent('div').hide().next().show().text(val);
      }

      fn && fn();
    },

    // 输入的金额判断
    _priceType: function (el, val) {
      var val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 小数点后面只要2位
      var reg = /\-/g;
      var regNum = /\./g;

      // 判断是不是有整数
      if (reg.test(val)) {
        val = val.replace(/\-/g, "");
        el.parent('div').hide().next().show().children('span').css('color', 'red');
      } else {
        el.parent('div').hide().next().show().children('span').css('color', '#333');
      }

      // 判断小数点前面的的位数，如果超过9位，就提示用户重新输入
      if (!regNum.test(val) && val.length > 9) {
        layer.msg('输入金额长度不能超过9位数(不含小数点后面)，请从新输入');
        el.parent('div').hide().next().show();
        return false;
      }

      val = !val ? '' : (parseFloat(val)).toFixed(2); // 判断数字长度是否超过11位
      return val;
    },

    // 获取合计总数
    _total: function () {
      var debitNum = 0,
        creditsNum = 0,
        MONEY = 999999999.99,
        debit = document.querySelectorAll('.book-left-debit'),
        credits = document.querySelectorAll('.book-right-credits'),
        $debit = $('#debit'),
        $credits = $('#credits'),
        that = this;

      // 借方金额 / 贷方金额
      for (var i = 0, len = debit.length; i < len; i++) {
        debitNum += (+debit[i].getAttribute('data-debit')) || 0;
        creditsNum += (+credits[i].getAttribute('data-credits')) || 0;
      }

      // 借方
      var debitVals = Math.round(debitNum * 100) / 100;
      InitCertificate.debitVal = debitVals.toFixed(2);

      // 贷方
      var creditsVals = Math.round(creditsNum * 100) / 100;
      InitCertificate.creditsVal = creditsVals.toFixed(2);

      if (Math.abs(debitNum) > MONEY || Math.abs(creditsNum) > MONEY) {
        layer.msg('总金额不能大于或者小于10亿，请将金额分批录入下一张凭证');
        return false;
      }

      judgment(function () {
        var borrower = InitCertificate.debitVal.replace(/\-/g, "").replace(/\./g, ""); // 借方
        var lender = InitCertificate.creditsVal.replace(/\-/g, "").replace(/\./g, ""); // 贷方
        $debit.text(borrower);
        $credits.text(lender);
      });

      // 判断是否是负数
      function judgment(fn) {
        InitCertificate.debitVal >= 0 ? $debit.css('color', '#333') : $debit.css('color', 'red');
        InitCertificate.creditsVal >= 0 ? $credits.css('color', '#333') : $credits.css('color', 'red');
        var momey = that._formatAmount(InitCertificate.debitVal);
        $('#formatAmount').text(momey);
        fn && fn();
      }
    },

    // 科目数据
    _subJectList: function (val) {
      var that = this,
        list = top.subListData || [],
        leng = list.length,
        data,
        val = val || '',
        str = "",
        code,
        title;

      if (leng) {
        // 未输入值显示全部
        if (!val.length) {
          str = "";
          //循环遍历存储匹配成功的数据
          for (var i = 0; i < leng; i++) {
            data = list[i];
            code = data.subCode + '/' + data.fullName + '/' + data.dir + '/' + data.endingBalanceDebit + '/' + data.endingBalanceCredit;
            title = data.subCode + '-' + data.fullName;
            str += '<li class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-subIndex="' + i + '" data-code="' + code + '" title="' + title + '">' + title + '</li>';
          }
          $("#subLists").html(str);
          return;
        }
        // 模糊查询
        var num1 = parseInt(val);
        var bool1 = false;
        if (!isNaN(num1)) {
          bool1 = true;
        }
        var myReg = /^[0-9a-zA-Z_\u4e00-\u9fa5]+$/; // /^[\u4e00-\u9fa5]+$/;
        var bool2 = myReg.test(val);
        var maths;
        //输入数字进行从头开始匹配
        if (bool1) {
          maths = "^" + val;
        }
        //输入中文进行模糊匹配
        else if (bool2) {
          maths = val;
        }
        if (bool1 == false && bool2 == false) {
          //没有匹配到的数据
          $("#subLists").html('<li style="text-align:center">暂无数据</li>');
          return false;
        }
        maths = maths.replace(/\?/g, ''); // match()不能识别 ??+
        var isFirst = 0;
        str = '';
        //循环遍历存储匹配成功的数据
        for (var i = 0; i < leng; i++) {
          data = list[i];
          title = data.subCode + '-' + data.fullName;
          var flag = title.match(maths);
          if (flag) {
            isFirst++;
            code = data.subCode + '/' + data.fullName + '/' + data.dir + '/' + data.endingBalanceDebit + '/' + data.endingBalanceCredit;
            str += '<li class="sub-title ellipsis' + (isFirst === 1 ? ' on' : '') + '" data-subIndex="' + i + '" data-code="' + code + '" title="' + title + '">' + title + '</li>';
          }
        }
        //将匹配到的数据添加到HTML中
        if (str == "") {
          str = '<li class="sub-title ellipsis">暂无数据</li>';
        }
        $("#subLists").html(str);
      } else {
        $("#subLists").html('<li class="sub-title ellipsis">暂无数据</li>');
      }
    },

    // 只获取一次科目列表
    _getSubListData: function () {
      // $("#subLists").html('<li class="sub-title ellipsis">正在加载数据~</li>');
      postRequest(window.baseURL + '/vat/queryAllSubToPage', { 'keyWord': '' }, function (res) {
        var list = res.data || [],
          len = list.length,
          data,
          str = '',
          code,
          title;
        top.subListData = list;
        top.subListRefresh = false;
        if (res.code === "0") {
          if (len > 0) {
            for (var i = 0; i < len; i++) {
              data = list[i];
              if (data.subCode) {
                code = data.subCode + '/' + data.fullName + '/' + data.dir + '/' + data.endingBalanceDebit + '/' + data.endingBalanceCredit;
                title = data.subCode + '-' + data.fullName;
                str += '<li class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-subIndex="' + i + '" data-code="' + code + '" title="' + title + '">' + title + '</li>';
              }
            }
          } else {
            str = '<li class="sub-title ellipsis">暂无数据</li>';
          }
          $('#subLists').html(str);
        }
      });
    },

    // 计算科目列表的位置
    _subListPosition: function (sub) {
      var selectHeight = $('.book-select').outerHeight(),
        titleHeight = $('.book-title').outerHeight(),
        index = InitCertificate.index + 1,
        offsetHieght = $('.book-subList').eq(0).outerHeight() * index + index,
        top = selectHeight + titleHeight + offsetHieght + 'px',
        left = $('.book-subList').eq(0).prev().outerWidth() + 1 + 'px';
      sub.css({
        'top': top,
        'left': left
      });
      sub.stop().slideDown(0); //显示科目列表
    },

    // 阿拉伯金额转换成中文金额 formatAmount
    _formatAmount: function (n) {
      var fraction = ['角', '分', '厘'];
      var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
      var unit = [
        ['元', '万', '亿'],
        ['', '拾', '佰', '仟']
      ];
      var head = n < 0 ? '负' : '';
      n = Math.abs(n);
      var s = '';
      for (var i = 0; i < fraction.length; i++) {
        s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
      }
      s = s || '整';
      n = Math.floor(n);
      for (var i = 0; i < unit[0].length && n > 0; i++) {
        var p = '';
        for (var j = 0; j < unit[1].length && n > 0; j++) {
          p = digit[n % 10] + unit[1][j] + p;
          n = Math.floor(n / 10);
        }
        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
      }
      return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
    },

    // 复制粘贴
    /*_copy: function () {
     var that = this;
     // that.vouchID = 'test';
     // console.log(that.vouchID);
     // return
     if (this._saveFlag()) {
       return false;
     }
     var params = {
       vouchID: that.vouchID
     };
     postRequest(window.baseURL + '/voucher/copyVoucher', params, function (res) {
       saveFlag = 0;
       if (res.code === '0') {
         top._subListRefresh(res.data); // 修改科目列表期末余额
         layer.msg('复制粘贴凭证成功!', {
           icon: 1,
           shade: 0.1,
           time: 1000
         }, function () {
           saveFlag = 0;
         });
         // 显示复制粘贴的凭证，修改sessionStorage.editVouch,vouchID,currVoucherNo、
         var vouchID = res.voucher.voucherHead.vouchID;
         that.vouchID = vouchID;
         window.sessionStorage.editVouch = window.sessionStorage.editVouch.replace(that.vouchID, vouchID); // 修改sessionStorage.editVouch
         $('#currVoucherNo').val(res.voucher.voucherHead.voucherNO);
       } else {
         layer.msg(res.msg || '复制粘贴凭证异常!', {
           icon: 2,
           shade: 0.1,
           time: 2000
         }, function () {
           saveFlag = 0;
         });
       }
     });
    },*/

    // 只保存一次
    _saveFlag: function () {
      if (saveFlag) {
        // saveFlag > 1 && layer.msg('正在保存中，请稍候~');
        return true;
      }
      saveFlag++;
      return false;
    },

    // 保存凭证后修改科目列表期末余额
    /*_editSubListData: function(arr) {
      var newVc, _obj, _index, direction, debitAmount, creditAmount, _vc, debit, credit, money;
      for (var i = 0; i < arr.length; i++) {
        var newVc = arr[i];
        // 根据科目编码查找对应对象数据
        var _obj = top.subListData.find(function(x) {
          return x.subCode == newVc.subjectID;
        });
        // 获取该对象数据对应的数组下标
        var _index = top.subListData.indexOf(_obj);
        if (_index < 0) {
          console.log('_editSubListData未查询到此科目数据:' + newVc.subjectID);
          continue;
        }
        // 取值，判断借贷方向，计算
        var direction = newVc.direction, // 分录借贷方向
          debitAmount = Number(newVc.debitAmount) || 0, // 借方金额
          creditAmount = Number(newVc.creditAmoun) || 0, // 贷方金额
          _vc = top.subListData[_index],
          debit = _vc.endingBalanceDebit === null ? 0 : _vc.endingBalanceDebit, // 原借方金额
          credit = _vc.endingBalanceCredit === null ? 0 : _vc.endingBalanceCredit, // 原贷方金额
          money; // = direction === '1' ? (debit - credit) : (credit - debit); // 借贷金额

        if (_vc.debitCreditDirection === '1') { // 科目方向:借
          if (direction === '1') { // 分录借贷方向:借
            money = debit + debitAmount;
          } else {// 分录借贷方向:贷
            money = debit - creditAmount;
          }
          _vc.endingBalanceDebit = Number(money.toFixed(2)) || 0;
        } else {// 科目方向:贷
          if (direction === '1') { // 分录借贷方向:借
            money = credit - debitAmount;
          } else {// 分录借贷方向:贷
            money = credit + creditAmount;
          }
          _vc.endingBalanceCredit = Number(money.toFixed(2)) || 0;
        }
        // if (direction === '1') { // 分录方向:借
        //   if (debitAmount > 0) { // 借方有金额
        //     money = debit + debitAmount;
        //   } else { // 贷方有金额
        //     money = debit - creditAmount;
        //   }
        //   _vc.endingBalanceDebit = Number(money.toFixed(2)) || 0;
        //   // isNaN(_vc.endingBalanceDebit) && _vc.endingBalanceDebit = '0.00';
        //   // _vc.endingBalanceCredit = credit;
        // } else { // 分录方向:贷
        //   if (debitAmount > 0) { // 借方有金额
        //     money = credit - debitAmount;
        //   } else { // 贷方有金额
        //     money = credit + creditAmount;
        //   }
        //   // _vc.endingBalanceDebit = debit;
        //   _vc.endingBalanceCredit = Number(money.toFixed(2)) || 0;
        //   // isNaN(_vc.endingBalanceCredit) && _vc.endingBalanceCredit = '0.00';
        // }
      }
      // this._getSubListData();//重新获取一次科目列表
    },*/

    // 保存并新增
    _submint: function () {
      if (this._saveFlag()) {
        return false;
      }
      var that = this,
        voucher = this._setVoucher();
      if (!voucher) {
        // 无凭证
        saveFlag = 0;
        return false;
      }
      // 后台已经判断凭证重复问题，忘了要处理什么场景的保存凭证异常要在页面上先判断一次
      /*postRequest(window.baseURL + '/voucher/queryAllVouchno', {}, function (res) {
        if (res.success == "true") {
          var voucherNO = $("#currVoucherNo").val();
          for (var i = 0; i < res.list.length; i++) {
            if (res.list[i].voucherNO == voucherNO) {
              layer.msg('凭证字号记-' + voucherNO + '已存在！', {
                icon: 2,
                shade: 0.1,
                shadeClose: true,
                time: 2000000
              }, function () {
                saveFlag = 0;
              });
              return false;
            }
          }*/
      postRequest(window.baseURL + '/voucher/saveVoucher', voucher, function (res) {
        saveFlag = 0;
        if (res.code === '0') {
          top._subListRefresh(res.data); // 修改科目列表期末余额
          layer.msg('保存凭证成功!', {
            icon: 1,
            shade: 0.1,
            time: 1500
          }, function () {
            saveFlag = 0;
            $('#currVoucherNo').val(res.nextMaxVoucherNo);
            that.initiDom();
            $('#debit').text('');
            $('#credits').text('');
            $('#formatAmount').text('');
            $('#content li:first').click(); // 保存并新增凭证，光标回到第一行摘要
          });
          // that._getSubListData(); //重新获取一次科目列表
          window._topTabsRefresh(['生成凭证']);
        } else if ('9' === res.code) {
          //凭证检查未通过
          layer.msg('保存凭证异常!<br/>', {
            icon: 2,
            shade: 0.1,
            time: 2000
          }, function () {
            saveFlag = 0;
            $('#currVoucherNo').val(res.nextMaxVoucherNo);
            that.initiDom();
            $('#debit').text('');
            $('#credits').text('');
            $('#formatAmount').text('');
            $('#content li:first').click(); // 保存并新增凭证，光标回到第一行摘要
            // 跳转生成凭证页面并刷新（查询修正凭证）
            top.$('#navBar > ul > li:eq(2) li.layui-nav-item:eq(0) a').click();
            top.$('#top_tabs_box .refresh').click();
          });
        } else {
          layer.msg(res.msg || '保存凭证异常!<br/>请重新保存', function () {
            saveFlag = 0;
          });
        }
      });
      /*  }
      });*/
    },

    // 保存编辑
    _edit: function () {
      var that = this;
      if (this._saveFlag()) {
        return false;
      }
      var voucher = this._setVoucher();
      if (!voucher) {
        // 无凭证
        saveFlag = 0;
        return false;
      }
      postRequest(window.baseURL + '/voucher/updVoucher', voucher, function (res) {
        saveFlag = 0;
        if (res.code === '0') {
          top._subListRefresh(res.data); // 修改科目列表期末余额
          layer.msg('保存凭证成功!', {
            icon: 1,
            shade: 0.1,
            time: 1500
          }, function () {
            $('#content .book-msg').prev().css('color', '#333').end().remove();
            $('#book-remark').removeClass();
          });
          // that._getSubListData(); //重新获取一次科目列表
          window._topTabsRefresh(['生成凭证', '明细账']);
        } else if ('9' === res.code) {
          //凭证检查未通过
          layer.msg('保存凭证异常!<br/>', {
            icon: 2,
            shade: 0.1,
            time: 2000
          }, function () {
            window.location.reload();
            // that._getCredentials();
            // 跳转生成凭证页面并刷新（查询修正凭证）
            top.$('#navBar > ul > li:eq(2) li.layui-nav-item:eq(0) a').click();
            top.$('#top_tabs_box .refresh').click();
          });
        } else {
          layer.msg(res.msg || '保存凭证异常!<br/>请重新保存');
        }
      });
    },

    // 取消修改
    _cancel: function () {
      // 取消修改
      $('#cancel').off('click').on('click', function () {
        // 返回凭证列表
        var layId = window._queryStringHash('layId');
        // window._jumpTab(layId);
        // 通过layId切换至指定的选项卡，如果该页签手动关闭或开启Tab缓存，则无法找到选项卡
        top.$('#top_tabs').children('li[lay-id="' + layId + '"]').click();
        // sessionStorage.removeItem('editVouch');
      });
    },

    // 资金、存货、固定资产科目余额(数量、金额)为负数
    _confirm: function () {},

    // 数据封装
    _setVoucher: function () {
      var arr = this._isCorrect();

      if (!arr || arr.length <= 0) {
        return false;
      }

      var voucherHead = {
        'auditStatus': 0, // 审核状态(0:未审核1:审核) // 审核已作废
        'des': document.getElementById('book-remark').value.trim(), // 备注
        'vcDate': $('#period').val(), // 日期
        'vouchFlag': 0, // 0:非模凭证1:模板凭证
        'voucherNO': document.getElementById('currVoucherNo').value, // 凭证头
        'source': 5, // 凭证类型 5手动录入凭证
        'totalCredit': parseFloat(InitCertificate.debitVal), // 凭证贷方金额合计
        'totalDbit': parseFloat(InitCertificate.creditsVal) // 凭证借方金额合计
      };

      // 保存需要凭证ID
      if (this.Type.isHasData) {
        voucherHead.vouchID = this.vouchID;
        voucherHead.source = InitCertificate.source;
        voucherHead.isproblem = InitCertificate.isProblem;
      }
      var voucher = {
        'voucherHead': JSON.stringify(voucherHead), //凭证头
        'voucherBodyList': JSON.stringify(arr), //凭证数据
      };

      return voucher;
    },

    // 数据的验证
    _isCorrect: function () {
      var item,
        MONEY = 999999999.99,
        arr = [],
        index,
        $list = $('#content').children('ul'),
        codeName,
        code,
        type,
        shuliang,
        danjia,
        abstractCount = 0, // 摘要数量
        flag = false;

      // 获取数据
      for (var i = 0, len = $list.length; i < len; i++) {
        index = i + 1;
        codeName = $list.eq(i).children('li').eq(1).attr('data-subtext') || '';
        codeName = codeName.replace(/\s/g, '');
        code = codeName ? codeName.split('/') : '';
        if ($list.eq(i).children('li').eq(2).attr('data-num') == undefined || $list.eq(i).children('li').eq(3).attr('data-amount') == undefined) {
          item = {
            'rowIndex': arr.length, //分录号
            'vcabstact': $list.eq(i).children('li').eq(0).attr('data-abstract') || '', // 摘要
            'subjectID': code[0], // 科目编码
            'vcsubject': code[1], // 科目名字
            'number': $list.eq(i).children('li').eq(2).attr('data-num') || '', // 数量
            'price': $list.eq(i).children('li').eq(3).attr('data-amount') || '', // 单价,
            'debitAmount': $list.eq(i).children('li').eq(4).attr('data-debit'), // 借方金额
            'creditAmount': $list.eq(i).children('li').eq(5).attr('data-credits') // 贷方金额
          };
        } else {
          shuliang = $list.eq(i).children('li').eq(2).attr('data-num').split(',');
          danjia = $list.eq(i).children('li').eq(3).attr('data-amount').split(',');
          item = {
            'rowIndex': arr.length, //分录号
            'vcabstact': $list.eq(i).children('li').eq(0).attr('data-abstract') || '', // 摘要
            'subjectID': code[0], // 科目编码
            'vcsubject': code[1], // 科目名字
            'number': parseFloat(shuliang.join("")) || '', // 数量
            'price': parseFloat(danjia.join("")) || '', // 单价,
            'debitAmount': $list.eq(i).children('li').eq(4).attr('data-debit'), // 借方金额
            'creditAmount': $list.eq(i).children('li').eq(5).attr('data-credits') // 贷方金额
          };
        }
        item.direction = item.creditAmount != 0 ? '2' : '1'; // 方向(1:借2:贷)
        item.amount = item.price * item.number || 0; //'金额=数量*单价'
        type = this._isType(item, index);
        if (type) {
          flag = true;
          layer.msg(type);
          break;
        }

        if (!item.subjectID) {
          continue;
        }

        if (item.vcabstact.length) {
          abstractCount++;
        }

        arr.push(item);
      }

      if (flag) { // 防止重复提交
        return false;
      }

      if (arr.length < 2) {
        layer.msg('请录入至少两行数据');
        return false;
      }

      if (abstractCount === 0) {
        layer.msg('请输入至少一条摘要');
        return false;
      }

      if (Math.abs(InitCertificate.debitVal) > MONEY || Math.abs(InitCertificate.creditsVal) > MONEY) {
        layer.msg('总金额不能大于10亿或者小于负10亿，请将金额分批录入下一张凭证');
        return false;
      }

      // if (InitCertificate.debitVal === '0.00' && InitCertificate.creditsVal === '0.00') {
      if (!InitCertificate.debitVal && !InitCertificate.creditsVal) {
        layer.msg('借方总金额或贷方总金额不能为零');
        return false;
      }

      if (InitCertificate.debitVal - InitCertificate.creditsVal != 0) {
        var money = InitCertificate.debitVal - InitCertificate.creditsVal;
        money = parseFloat(money.toFixed(2));
        layer.msg('借贷不平衡，借贷相差：' + money);
        return false;
      }
      return arr;
    },

    // 判断条件
    _isType: function (item, index) {
      var isTrue = null;
      if (document.getElementById('currVoucherNo').value < 1) {
        isTrue = '请输入有效的凭证号';
        return isTrue;
      }
      if (item.subjectID || item.debitAmount || item.creditAmount) {

        /*if (!isTrue && !item.vcabstact) {
          isTrue = '第' + index + '行摘要不能为空';
        }*/

        if (!isTrue && !item.subjectID && (item.debitAmount || item.creditAmount)) {
          isTrue = '第' + index + '行会计科目不能为空';
        }
        if (item.subjectID === 'XXX__') {
          isTrue = '第' + index + '找不到该科目';
        }
        // 1403 和 1405 需要填写单价 和 数量
        var subCode = item.subjectID ? item.subjectID.substr(0, 4) : '';
        if (subCode === '1403' || subCode === '1405' || subCode === '1408') {

          if (!isTrue && !item.number) {
            isTrue = '第' + index + '行请输入数量单位';
          }
          if (!isTrue && item.number === '0') {
            isTrue = '第' + index + '行数量单位不能为零';
          }

          if (!isTrue && !item.price) {
            isTrue = '第' + index + '行请输入单价';
          }
          if (!isTrue && !item.price) {
            isTrue = '第' + index + '行单价不能为零';
          }
        }

        if (!isTrue && (!item.debitAmount || !item.creditAmount)) {
          isTrue = '第' + index + '行请输入金额';
        }

        if (!isTrue && (item.debitAmount === '0.00' && item.creditAmount === '0.00')) {
          isTrue = '第' + index + '行借方金额或贷方金额不能为零';
        }
        return isTrue;
      }
    },

    //金额 / 数量 = 单价
    _toTheUnitPrice: function (index) {
      var list = this.content.children(),
        MONEY = 999999999.99,
        item = {},
        // index = InitCertificate.clickIndex,
        parents = list.eq(index).children();
      item.num = list[index].children[2].getAttribute('data-num'); //数量
      item.debit = list[index].children[4].getAttribute('data-debit'); //借方金额
      item.amount = list[index].children[3].getAttribute('data-amount'); //单价
      item.credits = list[index].children[5].getAttribute('data-credits'); //贷方金额
      if (valText == 1403 || valText == 1405 || valText == 1408) {
        //没有填写单价，只填写数量和金额
        if (item.debit > 0 || item.credits > 0 && item.num > 0) {
          if (item.num == 0 || item.num == null && item.amount != null) {
            layer.msg('数量必填');
            return false;
          }
          // 如果有数量， 则计算单价
          if (item.debit != null || item.credits != null) {
            if (item.debit > 0) {
              if (item.num == null) {
                return false;
              }
              item.itemPrice = (item.debit / item.num).toFixed(2);
            }
            if (item.credits > 0) {
              item.itemPrice = (item.credits / item.num).toFixed(2);
            }
            parents.eq(3).attr('data-amount', item.itemPrice);
            parents.eq(3).children().eq(1).text(item.itemPrice);
            parents.eq(3).children().eq(0).children().val(item.itemPrice); //单价
          }
        }
      }
    },

    // 数量 * 单价 = 金额
    _toCalculate: function () {
      var list = this.content.children(),
        MONEY = 999999999.99,
        item = {},
        index = InitCertificate.clickIndex,
        parents = list.eq(index).children();
      item.num = list[index].children[2].getAttribute('data-num');
      item.amount = list[index].children[3].getAttribute('data-amount');
      // 数量是必须有的，否则不执行
      if ((!item.num || item.num === 'null') && !item.amount) {
        return false;
      }
      // 如果有单价， 则计算金额
      if (item.num && item.amount && !isNaN(parseFloat(item.amount))) {
        item.itemAmount = (item.num * item.amount).toFixed(2);

        if (Math.abs(item.itemAmount) > MONEY) {
          layer.msg('总金额不能大于或者小于10亿，请将金额分批录入下一张凭证');
          return false;
        }
        parents.eq(4).children().eq(0).children().val(item.itemAmount); // 借方金额
        parents.eq(5).children().eq(0).children().val(item.itemAmount); // 贷方金额
      }
    },

    // 处理编辑凭证的数据
    _hasData: function (list) {
      // 处理附征税的凭证4位小数转2位小数显示造成的借贷方金额合计可能不平衡的问题（影响：编辑凭证后会改变借方或贷方最后一条凭证分录金额）
      var list = list,
        len = list.length,
        data;
      for (var i = 0; i < len; i++) {
        data = list[i];
        data.vcabstact = !data.vcabstact ? '' : data.vcabstact;
        data.subjectID = !data.subjectID ? '' : data.subjectID;
        data.vcsubject = !data.vcsubject ? '' : data.vcsubject;
        data.number = !data.number ? '' : data.number;
        data.price = !data.price ? '' : data.price.toFixed(2);
        data.debitAmount = !data.debitAmount ? '' : data.debitAmount.toFixed(2);
        data.creditAmount = !data.creditAmount ? '' : data.creditAmount.toFixed(2);

        //查看凭证时不显示余额2018/08/27
        // 借贷方向金额
        if (data.direction === '1') {
          //        data.directionMoney = data.debitAmount + ' ' + '(借方)';
          data.directionMoney = '';
        } else {
          //        data.directionMoney = data.creditAmount + ' ' + '(贷方)';
          data.directionMoney = '';
        }
      }
      return list;
    },

    // 字符串
    _string: function () {
      var str = '';
      str += '<ul class="book-content">' +
        '<li class="w1">' +
        '<div class="book-text w1 hide">' +
        '<textarea class="book-val"></textarea>' +
        '</div>' +
        '<p class="w1 book-focus"></p>' +
        '</li>' +
        '<li class="w2 book-subList">' +
        '<div class="book-text w2 hide">' +
        '<textarea class="book-subText"></textarea>' +
        '</div>' +
        '<div class="w2 book-subList-text book-focus">' +
        '<div class="book-subName ellipsis"></div>' +
        '<div class="book-subMoney ellipsis"></div>' +
        '</div>' +
        '</li>' +
        '<li class="w3  book-self-num">' +
        '<div class="book-text w3 hide">' +
        '<input class="book-num ime-disabled" />' +
        '</div>' +
        '<p class="w3 tr book-focus"></p>' +
        '</li>' +
        '<li class="w3  book-self-amount">' +
        '<div class="book-text w3 hide">' +
        '<input class="book-amount ime-disabled" />' +
        '</div>' +
        '<p class="w3 tr book-focus"></p>' +
        '</li>' +
        '<li class="w4 book-left-debit">' +
        '<div class="w4 hide">' +
        '<input type="text" class="book-debit w4 ime-disabled" maxlength="12">' +
        '</div>' +
        '<p class="w4 book-img book-focus">' +
        '<span></span>' +
        '</p>' +
        '</li>' +
        '<li class="w4 book-right-credits">' +
        '<div class="w4 hide">' +
        '<input type="text" class="book-credits w4 ime-disabled" maxlength="12">' +
        '</div>' +
        '<p class="w4 book-img book-focus">' +
        '<span></span>' +
        '</p>' +
        '</li>' +
        '<li class="w5">' +
        '<span class="book-add layui-icon" title="向下增行">&#xe654;</span>' +
        '<span class="book-remove layui-icon" title="删除本行">&#x1006;</span>' +
        '</li>' +
        '</ul>';
      return str;
    },

    // 函数节流
    _debounce: function (del, fun) {
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
    },
  }
  window.InitCertificate = InitCertificate;

  //上传凭证
  $('.shangchuan').on('click', function () {
    var that = this,
      url = window.baseURL + '/voucher/uploadVoucher';
    uploadInit(that, url, uploadVoucher);
  })

  function uploadVoucher(result) {
    if (result.success == 'true') {
      $("#nav_certificate a:eq(0)").click();
      layer.msg('上传成功', {
        icon: 1,
        shade: 0.1,
        time: 1000
      });
    } else {
      layer.msg(result.message, {
        icon: 2,
        shade: 0.1,
        shadeClose: true,
        time: 2000000
      });
    }
  }

  $("#file1").change(function () { // 当 id 为 file 的对象发生变化时
    var fileSize = this.files[0].size;
    var size = fileSize / 1024 / 1024;
    if (size > 50) {
      alert("附件不能大于5M,请将文件压缩后重新上传！");
      this.value = "";
      return false;
    } else {
      $("#uploading .span").text($("#file1").val()); //将 #file 的值赋给 #file_name
    }
  })

  //点击借贷平衡
  $("#i-Speed").off('click').on('click', function () {
    PHmoney();
  })

  // 全局计算借贷平衡，只有一行未输入时计算正确
  // 处理浮点数问题
  function PHmoney() {
    //凭证ul行数、平衡差、借方金额和、贷方金额和
    var $ul = $("#content ul"),
      ulLength = $ul.length,
      PHmoney = 0,
      JFmoney = 0,
      DFmoney = 0;
    //循环算出借方总数和贷方总数
    // for (var i = 0; i < ulLength; i++) {
    //   JFmoney += parseFloat($("#content ul:eq(" + i + ") .book-left-debit").attr("data-debit")) || 0;
    //   DFmoney += parseFloat($("#content ul:eq(" + i + ") .book-right-credits").attr("data-credits")) || 0;
    // }
    // 转换为数字类型比较，字符串类型比较会出错
    JFmoney = parseFloat(InitCertificate.debitVal);
    DFmoney = parseFloat(InitCertificate.creditsVal);
    // 判断借贷是否平衡，允许结果小于等于零，
    // 在页面填充金额，只允许有一个空值
    // 修改已有金额的，需要$('#content').find('li.w4 input') 获取焦点
    //循环凭证ul
    for (var i = 0; i < ulLength; i++) {
      //如果科目名称不为空则进条件
      if ($ul.eq(i).find('.book-subList').attr("data-subtext") != undefined) {
        //科目不为空，判断是否已有借方或贷方金额$ul.eq(i).find("
        var JF = $ul.eq(i).find(".book-left-debit").attr("data-debit");
        var DF = $ul.eq(i).find(".book-right-credits").attr("data-credits");
        //if($("#content ul:eq("+i+") .book-left-debit").attr("data-debit") == undefined && $("#content ul:eq("+i+") .book-right-credits").attr("data-credits") == undefined){
        if (JF == undefined || JF == '0.00' || JF == "") {
          if (DF == undefined || DF == '0.00' || DF == "") {
            //如果借方大于贷方book-focus
            if (JFmoney > DFmoney) {
              PHmoney = JFmoney - DFmoney;
              PHmoney = parseFloat(PHmoney.toFixed(2));
              $ul.eq(i).find('.book-right-credits input').focus().val(PHmoney).blur();
              JFmoney = DFmoney;
              /*PHmoneyC = PHmoney.toFixed(2);
              $("#content ul:eq(" + i + ") .book-left-debit").attr("data-debit", "0.00");
              $("#content ul:eq(" + i + ") .book-right-credits").attr("data-credits", PHmoneyC);
              $("#content ul:eq(" + i + ") .book-right-credits .book-credits").val(PHmoney);
              $("#content ul:eq(" + i + ") .book-right-credits .book-focus span").text(PHmoneyC.replace(/\-/g, "").replace(/\./g, ""));
              改变贷方合计值
              var heji = parseFloat($("#credits").text()) + parseFloat(PHmoneyC.replace(/\-/g, "").replace(/\./g, ""));
              $("#credits").text(heji);
              var momey = _formatA(JFmoney);
              $('#formatAmount').text(momey);
              _totalS();*/
              return false;
              //如果贷方大于借方
            } else if (DFmoney > JFmoney) {
              PHmoney = DFmoney - JFmoney;
              PHmoney = parseFloat(PHmoney.toFixed(2));
              $ul.eq(i).find('.book-left-debit input').focus().val(PHmoney).blur();
              JFmoney = DFmoney;
              /*PHmoneyC = PHmoney.toFixed(2);
              $("#content ul:eq(" + i + ") .book-right-credits").attr("data-credits", "0.00");
              $("#content ul:eq(" + i + ") .book-left-debit").attr("data-debit", PHmoneyC);
              $("#content ul:eq(" + i + ") .book-left-debit .book-debit").val(PHmoney);
              $("#content ul:eq(" + i + ") .book-left-debit .book-focus span").text(PHmoneyC.replace(/\-/g, "").replace(/\./g, ""));
              改变借方合计值
              var heji = parseFloat($("#debit").text()) + parseFloat(PHmoneyC.replace(/\-/g, "").replace(/\./g, ""));
              $("#debit").text(heji);
              var momey = _formatA(DFmoney.toFixed(2));
              $('#formatAmount').text(momey);
              _totalS();*/
              return false;
              //贷方和借方相等
            } else {
              alert("贷方和借方已相等");
              return false;
            }
          }
        }
        //如果科目名称为空则提示
      } else if ($ul.eq(i).find('.book-subList').attr("data-subtext") == undefined && JFmoney != DFmoney) {
        alert("请先选择科目");
        return false;
      } else if (JFmoney == DFmoney) {
        alert("贷方和借方已相等");
        return false;
      }
    }
  }

  //成本结转(出库)获取数据
  function warehouse() {
    var str = '';
    postRequest(window.baseURL + '/vat/queryCommodityToVoucher', '', function (res) {
      var list = res.msg;
      if (res.code == "0") {
        for (var i = 0; i < list.length; i++) {
          var comName = list[i].sub_comName.split("_");
          str += '<tr id="' + list[i].comID + '">' +
            '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>' +
            '<td>' + list[i].sub_code + '</td>' +
            '<td>' + comName[comName.length - 1] + '</td>' +
            '<td>' + list[i].qm_balanceNum + '</td>' +
            '<td>' + list[i].qm_balanceAmount + '</td>' +
            '</tr>'
        }
      } else {
        str = '<tr><td colspan="5">暂无数据</td></tr>'
      }
      $("#warehouseBody").html(str);
      layui.form.render();
    })
  }

  //全选
  $("table").on('click', '.layui-form-checkbox', function () {
    checkBoxState($(this));
  })

  //成本结转(出库)点击生成凭证
  $(".SCimport").on('click', function () {
    var idS = '',
      strIndex = 0,
      ullength = $("#content ul").length;
    divStr = $("#warehouseBody tr");
    for (var i = 0; i < divStr.length; i++) {
      if ($("#warehouseBody tr:eq('" + i + "') input[type='checkbox']").is(':checked')) {
        idS += $("#warehouseBody tr:eq('" + i + "')").attr('id') + ",";
        strIndex += 1;
      }
      if (strIndex > ullength - 1) {
        $(".book-content:eq('" + (ullength - 1) + "') .w5 .book-add").click();
        ullength += 1;
      }
    }
    $("#content > ul:gt(" + strIndex + ")").remove();
    var params = {
      comids: idS.substring(0, idS.lastIndexOf(','))
    };
    postRequest(window.baseURL + '/vat/commodityGenerateVoucher', params, function (res) {
      if (res.code == "1") {
        var $list = res.msg.voucherBodyList,
          TotalJ = 0,
          TotalD = 0;
        for (var i = 0; i < $list.length; i++) {
          if ($list[i].direction == "1") {
            TotalD += $list[i].debitAmount;
            var debitAmount = $list[i].debitAmount.toFixed(2) || '';
            var debitAmountS = debitAmount.replace(/\./g, "");
            //借方金额
            $("#content ul:eq('" + i + "') .w2 .book-subMoney").text('期末余额：' + debitAmount + '(借)');
            $("#content ul:eq('" + i + "') .w2").attr('data-subtext', $list[i].subjectID + '/' + $list[i].vcsubject + '/' + $list[i].direction + '/' + danjia + '/0');
            $("#content ul:eq('" + i + "') .w4.book-left-debit").attr('data-debit', debitAmount);
            $("#content ul:eq('" + i + "') .w4.book-right-credits").attr('data-credits', '0.00');
            $("#content ul:eq('" + i + "') .w4.book-right-credits input").val('0.00');
            $("#content ul:eq('" + i + "') .w4.book-left-debit input").val(debitAmount);
            $("#content ul:eq('" + i + "') .w4.book-left-debit p").html('<span style="color: rgb(51, 51, 51);">' + parseFloat(debitAmountS) + '</span>');
          } else {
            TotalJ += $list[i].creditAmount;
            var danjia = $list[i].price.toFixed(2) || '';
            var creditAmount = $list[i].creditAmount.toFixed(2) || '';
            var creditAmountS = creditAmount.replace(/\./g, "");
            //贷方金额
            $("#content ul:eq('" + i + "') .w4.book-left-debit").attr('data-debit', '0.00');
            $("#content ul:eq('" + i + "') .w4.book-right-credits").attr('data-credits', creditAmount);
            $("#content ul:eq('" + i + "') .w4.book-right-credits input").val(creditAmount);
            $("#content ul:eq('" + i + "') .w4.book-left-debit input").val('0.00');
            $("#content ul:eq('" + i + "') .w4.book-right-credits p").html('<span style="color: rgb(51, 51, 51);">' + parseFloat(creditAmountS) + '</span>');
            $("#content ul:eq('" + i + "') .w2 .book-subMoney").text('期末余额：' + danjia + '(借)期末数量：' + $list[i].number);
            $("#content ul:eq('" + i + "') .w2").attr('data-subtext', $list[i].subjectID + '/' + $list[i].vcsubject + '/' + $list[i].direction + '/0/' + danjia);
          }
          if (i == 0) {
            //摘要
            $("#content >ul:eq('" + i + "')>li:eq(0)").attr('data-abstract', '结转销售成本');
            $("#content >ul:eq('" + i + "')>li:eq(0) textarea").val('结转销售成本');
            $("#content >ul:eq('" + i + "')>li:eq(0) p").text('结转销售成本');
            // $("#content ul:eq('"+i+"') .w1 p").text(comName[comName.length - 1]);
          }
          //科目
          $("#content ul:eq('" + i + "') .w2 .book-subName").attr('title', $list[i].subjectID + '-' + $list[i].vcsubject);
          $("#content ul:eq('" + i + "') .w2 .book-subName").text($list[i].subjectID + '-' + $list[i].vcsubject);
          $("#content ul:eq('" + i + "') .w2 textarea").val($list[i].subjectID + '-' + $list[i].vcsubject);
          if ($list[i].subjectID.substring(0, 4) == '1403' || $list[i].subjectID.substring(0, 4) == '1405') {
            //数量
            $("#content ul:eq('" + i + "') .w3.book-self-num").attr('data-num', $list[i].number);
            $("#content ul:eq('" + i + "') .w3.book-self-num p").text($list[i].number);
            //单价
            $("#content ul:eq('" + i + "') .w3.book-self-amount").attr('data-amount', danjia);
            $("#content ul:eq('" + i + "') .w3.book-self-amount p").text(danjia);
          }
        }
        var TotalJS = TotalJ.toFixed(2).replace(/\./g, ""),
          TotalDS = TotalD.toFixed(2).replace(/\./g, "");
        $('.book-floor .book-content #debit').text(TotalJS);
        $('.book-floor .book-content #credits').text(TotalDS);
        InitCertificate.debitVal = TotalJ.toFixed(2); //借方总金额
        InitCertificate.creditsVal = TotalD.toFixed(2); //贷方总金额
        var momey = InitCertificate.prototype._formatAmount(TotalD);
        $('#formatAmount').text(momey);
        $("#warehouse").hide();
      } else {
        layer.msg(res.msg || '生成凭证异常');
      }
    })
  })

  //成本结转(出库)点击效果
  $("#i-warehouse").on('click', function (event) {
    event.stopPropagation();
    $("#warehouse").show();
  });
  $("#warehouse").on('click', function (event) {
    event.stopPropagation();
  })
  //点击其他地方隐藏
  $(document).click(function () {
    $("#warehouse, .more ul, .templateDiv,#templateBody .modifyIuput, #templateBody .modifyBut").hide();
    $("#templateBody .modifySpan").show();
    $(".newTemplate ul li .templateR:eq(0)").css({ "border": "1px solid #ccc" });
  });
  //刷新出库列表
  $("#refresh-warehouse").on('click', function () {
    warehouse();
  });

  //点击计算器按钮
  $("#i-calculator").on('click', function () {
    calculator.clearAll(); // 清空计算器
    $("#calculator_main").show();
  });

  // 关闭计算器
  $("#calculator_close").on('click', function () {
    $("#calculator_main").hide();
  });

  // 键盘快捷键提示
  $('#i-hints,#shortcuts-hints').on('mouseenter', function () {
    $('#shortcuts-hints').stop().slideDown(0);
  }).on('mouseleave', function () {
    $('#shortcuts-hints').stop().slideUp(0);
  });
}(window));
