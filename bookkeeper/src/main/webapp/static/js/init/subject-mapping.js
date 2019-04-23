/*
 * 科目映射
 * author: weiqi
 * 2018-11-1 16:46:11
 */
;
(function() {
  var subMapData = [], // 存储科目映射数据
    $tbody = $('#mapping-body'),
    $tableBody = $tbody.parents('.layui-table-body'),
    $tableHeader = $tableBody.prev('.layui-table-header'),
    saveFlag = 0; // 保存flag

  $(function() {
    var $subList = $('#subList');
    init();

    // table自适应
    $(window).on('resize', function() {
      _checkSide();
    });

    $('#save-btn').on('click', function() {
      $subList.hide();
      _submit();
    });

    $('#again-btn').on('click', function() {
      $subList.hide();
      _mapping();
    });
  });

  function init() {
    if (parent.location.pathname === window.baseURL + '/subinit/initView') { // 账套初始化页面弹窗
      $('body').removeClass('must-import');
    } else {
      $('body').addClass('must-import');
    }
    new subListSelect({
      'content': $('#mapping-body'),
      'subList': $('#subList')
    });
    _mappingList();
  }

  // 查询科目映射列表
  function _mappingList() {
    getRequest(window.baseURL + '/subinit/querySubMappingList', function(res) {
      parent.layer.closeAll('dialog');
      subMapData = res.list || [];
      /*if (res.code === 1) {} else {
        res.msg && layer.msg(res.msg, {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000000
        });
      }*/
      if (res.code === -1) {
        $tbody.html('<tr class="tc"><td colspan="4" style="height: 80px;">获取科目映射列表异常</td></tr>');
      } else {
        _listHTML(subMapData);
      }
    });

    function _listHTML(list) {
      var str = '',
        str2,
        data1,
        data2,
        len = list.length;

      if (len) {
        $.each(list, function(idx, el) {
          data1 = el[0];
          data2 = el[1];
          str2 = data2.subCode === null ? '' : data2.subCode + '-' + data2.fullName;
          str += '<tr id="serialno' + idx + '">' +
            '<td>' + data1.subMappingCode + '</td>' +
            '<td>' + data1.subMappingName + '</td>' +
            '<td>' + data1.similarName + '</td>' +
            // '<td><input type="text" value="' + str2 + '"></td>' +
            '<td class="book-subList"><textarea class="book-subText">' + str2 + '</textarea></td>' +
            '</tr>';
        });
      } else {
        str = '<tr class="tc"><td colspan="4" style="height: 80px;">暂无凭证数据</td></tr>';
      }
      $tbody.html(str);
      _checkSide();
    }
  }

  // 判断科目映射列表是否有滚动条
  function _checkSide() {
    if ($tbody.height() > $tbody.parents('.layui-table-body').height()) {
      $tableHeader.addClass('side');
    } else {
      $tableHeader.removeClass('side');
    }
  }

  // 只提交一次
  function _submitFlag() {
    if (saveFlag) {
      // saveFlag > 1 && layer.msg('正在保存中，请稍候~');
      return true;
    }
    saveFlag++;
    return false;
  }

  // 保存映射科目
  function _submit() {
    if (_submitFlag()) {
      return false;
    }
    var params = _setParams();
    if (!params) {
      // 映射科目有空的
      saveFlag = 0;
      return false;
    }
    // console.log(params); // subMessageAndSubMapping
    postRequest(window.baseURL + '/subinit/addSubMapping', params, function(res) {
      saveFlag = 0;
      if (res.code === 1) {
        layer.msg(res.msg || '保存映射科目成功!', {
          icon: 1,
          shade: 0.1,
          time: 2000
        }, function() {
          if (parent.location.pathname === window.baseURL + '/subinit/initView') { // 账套初始化页面弹窗
            parent.layer.closeAll('iframe'); //关闭所有的iframe层
            // 解绑点击事件
            top != window && top.$('html').off('click');
            top != parent && parent.$('html').off('click');
          } else { // 单独的页面
            // top.$("#choose-account div").attr('data-mapped', 1); // 手动改变状态
            // var curmenu = window.sessionStorage.curmenu;
            // if (curmenu !== 'undefined') {
            //   if (JSON.parse(curmenu).title === '标准科目映射') {
            //     // return;
            //   } else {
            //     top.$('#top_tabs_box .closePageAll').click(); // 关闭全部
            //   }
            // }
            // 如果是老账套补上科目映射，刷新页面
            if (_queryStringHash('reloadPage') === 'true') {
              top.location.reload();
              top.$('#top_tabs_box .closePageAll').click(); // 关闭全部
            }
          }
        });
      } else {
        layer.msg(res.msg || '保存映射科目异常!<br/>请重新保存');
      }
    });
  }

  // 保存映射科目数据封装
  function _setParams() {
    var arr = _isCorrect();

    if (!arr || arr.length <= 0) {
      return false;
    }
    var params = {
      'subMessageAndSubMapping': JSON.stringify(arr)
    };

    return params;
  }

  // 数据的验证 非空，去重
  function _isCorrect() {
    var arr = subMapData.concat(), // 复制科目映射数据
      $inputs = $tbody.find('textarea'), // 映射科目对象
      _subCode, // 映射科目
      tempArr = [], // 要去重的数组
      _obj,
      _index,
      i = 0,
      len = $inputs.length; //映射科目数量

    if (len === 0) {
      layer.msg('科目列表为空', {
        icon: 0,
        time: 2000
      });
      return false;
    }
    for (i; i < len; i++) {
      _subCode = $inputs.eq(i).val().trim();
      if (_subCode.length === 0) {
        layer.msg('第' + (i + 1) + '行映射科目不能为空');
        $inputs.eq(i).focus();
        return false;
      }
      _subCode = _subCode.split('-')[0];
      tempArr.push(_subCode);
      // 根据科目编码查找对应对象数据

   /*   var  flg = false;
      for(var sub in subListData){
    	  if(sub.subCode==_subCode){
    		  flg = true;
    		  break;
    	  }
      }
      if(flg==false){
    	  layer.msg($inputs.eq(i).val()+'映射科目不存在于科目列表中');
          return false;
      }*/


      _obj = subListData.find(function(x) {
        return x.subCode == _subCode;
      });
      // 获取该对象数据对应的数组下标
      _index = subListData.indexOf(_obj);
      if (_index < 0) {
       layer.msg($inputs.eq(i).val() + '映射科目不存在于科目列表中');
        return false;
      }
      arr[i][1] = subListData[_index];
    }
    // 去重
    if (tempArr.length !== len) {
      layer.msg('标准科目和映射科目数量不等');
      return false;
    }
    var uniqueItem, //创建一个需去重数组的指针
      uniqueArr = [], //创建一个新的数组存放结果
      uniqueObj = {}; //创建一个空对象
    for (var j = 0; j < len; j++) {
      uniqueItem = tempArr[j];
      if (!uniqueObj[uniqueItem]) {
        uniqueArr.push(uniqueItem);
        uniqueObj[uniqueItem] = 1;
      } else {
        $inputs.eq(j).focus();
        layer.msg('第' + (j + 1) + '行存在重复的科目映射:' + $inputs.eq(j).val().trim(), {
          icon: 2,
          time: 2000
        });
        return false;
      }
    }
    return arr;
  }

  // 点击 自动映射按钮  映射系统科目与客户科目
  function _mapping() {
    if (_submitFlag()) {
      return false;
    }
    var params = _setMapping();
    if (!params) {
      // 全部科目已经完成映射
      saveFlag = 0;
      return false;
    }
    // console.log(params); // subMappings
    postRequest(window.baseURL + '/subinit/againAddSubMapping', params, function(res) {
      saveFlag = 0;
      if (res.code === 1) {
        // 小规模会创建科目来完成自动隐射，需要更新末级科目列表
        if ($('#ssType').val() == '1') {
          subListSelect.prototype._getSubListData(success);
          /*postRequest(window.baseURL + '/vat/queryAllSubToPage', { 'keyWord': '' }, function (res) {
            top.subListData = res.data || []; // 末级科目列表
            top.subListRefresh = false; // 末级科目列表是否需要刷新
            success();
          });*/
        } else {
          success();
        }
      } else {
        layer.msg(res.msg || '自动映射科目异常!');
      }

      function success() {
        layer.msg(res.msg || '自动映射科目成功!', {
          icon: 1,
          time: 2000
        }, function() {
          _mappingList(); // 更新列表
          /*if (parent.location.pathname === window.baseURL + '/subinit/initView') { // 账套初始化页面弹窗
            parent.layer.closeAll('iframe'); //关闭所有的iframe层
            // 解绑点击事件
            top != window && top.$('html').off('click');
            top != parent && parent.$('html').off('click');
          } else {
            var curmenu = window.sessionStorage.curmenu;
            if (curmenu !== 'undefined') {
              if (JSON.parse(curmenu).title === '标准科目映射') {
                _mappingList(); // 更新列表
                return;
              }
            }
          }*/
        });
      }
    });
  }

  // 自动映射科目数据封装
  function _setMapping() {
    var arr = [], // 复制科目映射数据
      i = 0,
      len = $tbody.children('tr').length;

    for (i; i < len; i++) {
      // if (subMapData[i][1].subCode === null) {
      arr.push(subMapData[i][0]);
      // }
    }
    // if (arr.length <= 0) {
    //   layer.msg('已经完成自动映射');
    //   return false;
    // }

    var params = {
      'subMappings': JSON.stringify(arr)
    };

    return params;
  }

  window._submit = _submit; // 保存映射科目
  window._mapping = _mapping; // 自动映射科目
}());

/*
 * 科目下拉列表选择
 */
(function(w) {
  var subListData = []; // 本地存储会计科目列表
  function subListSelect(opt) {
    this.content = opt.content; // 渲染元素的ID
    this.subList = opt.subList; // 科目列表
    this.$input = null; // 当前输入框
    this.init();
  }
  subListSelect.prototype = {
    init: function() {
      var that = this;
      this._getSubListData(); // 请求科目列表
      this.switching();
      this.isIframe();
    },

    //DOM 操作方法
    switching: function() {
      var that = this,
        $this;

      $(window).on('resize', function() {
        that._subListPosition();
      });

      $('.layui-table-body').off('scroll').on('scroll', function() {
        that._subListPosition();
      });

      // 列表
      this.content
        .on('click', '.book-subList', function(event) {
          var e = window.event || event;
          // 阻止冒泡
          e.cancelBubble = true || e.stopPropagation();

          subListSelect.index = $(this).parents('tr').index(); // 获取当前索引

          // 点击获取光标
          $(this).children('.book-subText').focus();
        })

        // 获取焦点，选中内容
        .off('focus').on('focus', 'input, textarea', function() {
          $(this).select();
        })

        // 获取科目的指向this
        .on('focus', '.book-subText', function() {
          that.$input = $(this);
          $this = $(this);
          // 搜索当前选择科目
          var val = $this.val().trim();
          if ($this.val === '-') {
            val = '';
          }
          $this.val(val);
          that._subJectList(val);
          // 显示科目列表
          that._subListPosition();
        })

        // 科目列表
        .on('blur', '.book-subText', function() {
          $(this).parent('div').hide().next().show();
        })

        // 键盘事件
        .off('keydown').on('keydown', 'td.book-subList', function(event) {
          var e = window.event || event,
            keyCode = e.keyCode ? e.keyCode : e.which,
            $this = $(this),
            $parent = $this.parent();

          // 监听 enter键 点击切换
          if (keyCode === 13) {
            // if ($this.hasClass('book-subList')) { // 会计科目
            var $on = that.subList.find('.on');
            if ($on.length) {
              // 默认选择第一条
              $on.click();
            } else {
              layer.msg('请输入科目', {
                time: 1000
              });
              return false;
            }
            // }
            // 点击下一个td
            if (!$parent.next().length) {
              $this.blur();
              that.subList.hide();
            } else {
              $parent.next().find('.book-subList').off('click').click();
            }
            return false;
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

        // 模糊搜索
        .on('input', '.book-subText', that._debounce(0, function() {
          var val = $this.val().trim();
          $this.val(val);
          that._subJectList(val);
        }));

      // 科目选择条目
      this.subList.off('click').on('click', '.sub-title', function() {
        // 无数据
        if (subListData.length === 0) {
          return;
        }
        var parent = that.content.children('tr').eq(subListSelect.index).children().eq(3); // td.book-subList
        // 回写科目
        parent.attr('data-index', $(this).index());
        parent.find('textarea.book-subText').val($(this).text());
      });

      // 点击新增科目
      $('#sub-addSub').off('click').on('click', function(event) {
        var e = window.event || event,
          index = subListSelect.index;
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
          'indexLength': index, // 科目输入框下标
          'refresh': refresh // 新增科目回调函数
        });

        function refresh(params) {
          $('#mapping-body').find("textarea.book-subText").eq(index).val(params.subCode + '-' + params.fullName); // 回写
          that._getSubListData(); // 请求科目列表
        }
      });

      // 点击隐藏科目列表，未输入内容时默认第一个选中
      $('html').off('click').on('click', function(event) {
        // 如果是键盘事件就不执行隐藏
        var e = window.event || event,
          keyCode = e.keyCode ? e.keyCode : e.which;
        // Tab Enter Spacebar
        if (keyCode === 9 || keyCode === 13 || keyCode === 32) {
          return false;
        }
        // 解决火狐会隐藏科目列表
        if (!that.content.find('textarea.book-subText').eq(subListSelect.index).is(":focus")) {
          that.$input = null;
          $('#subLists').scrollTop(0);
          that.subList.stop().slideUp(0);
          that._subJectList();
        }
        // .find('li').eq(0).addClass('on')
        //   .siblings().removeClass('on');
      });
    },

    // 如果当前页面是iframe
    isIframe: function() {
      var that = this;
      if (window != top) {
        top.$('html').off('click').on('click', function() {
          $('#subLists').scrollTop(0);
          that.subList.stop().slideUp(0);
          that._subJectList();
        });
        top != parent && parent.$('html').off('click').on('click', function() {
          $('#subLists').scrollTop(0);
          that.subList.stop().slideUp(0);
          that._subJectList();
        });
      }
    },

    // 科目数据
    _subJectList: function(val) {
      var that = this,
        list = subListData,
        leng = list.length,
        val = val || '';

      if (leng) {
        // 未输入值显示全部
        if (!val.length) {
          var str = "";
          //循环遍历存储匹配成功的数据
          for (var i = 0; i < leng; i++) {
            var code = list[i].subCode + '/' + list[i].fullName + '/' + list[i].dir + '/' + list[i].endingBalanceDebit + '/' + list[i].endingBalanceCredit;
            str += '<li class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + '-' + list[i].fullName + '</li>';
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
        var myReg = /^[0-9a-zA-Z_\u4e00-\u9fa5]+$/;
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
        var str = "",
          isFirst = 0;
        //循环遍历存储匹配成功的数据
        for (var i = 0; i < leng; i++) {
          var flag = (list[i].subCode + '-' + list[i].fullName).match(maths);
          if (flag) {
            isFirst++;
            var code = list[i].subCode + '/' + list[i].fullName + '/' + list[i].dir + '/' + list[i].endingBalanceDebit + '/' + list[i].endingBalanceCredit;
            str += '<li class="sub-title ellipsis' + (isFirst === 1 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + '-' + list[i].fullName + '</li>';
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
    _getSubListData: function(callback) {
      var that = this;
      // $("#subLists").html('<li class="sub-title ellipsis">正在加载数据~</li>');
      postRequest(window.baseURL + '/vat/queryAllSubToPage', { 'keyWord': '' }, function(res) {
        var list = res.data || [],
          len = list.length,
          str = '';
        subListData = list;
        window.subListData = list;
        top.subListData = list;
        window.sessionStorage.subListLength = len;
        if (res.code === "0") {
          if (len > 0) {
            for (var i = 0; i < len; i++) {
              if (list[i].subCode) {
                var code = list[i].subCode + '/' + list[i].fullName + '/' + list[i].dir + '/' + list[i].endingBalanceDebit + '/' + list[i].endingBalanceCredit;
                str += '<li class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + '-' + list[i].fullName + '</li>';
              }
            }
          } else {
            str = '<li class="sub-title ellipsis">暂无数据</li>';
          }
        } else {
          str = '<li class="sub-title ellipsis">获取科目列表数据异常</li>';
        }
        $('#subLists').html(str);
        if (callback) {
          callback();
        }
      });
    },

    // 计算科目列表的位置
    _subListPosition: function() {
      if (!this.$input) {
        return false;
      }
      var _winH = $(window).height(),
        _left = this.$input.offset().left,
        _top = this.$input.offset().top + this.$input.outerHeight() + 1,
        _width = this.$input.outerWidth();
      index = subListSelect.index + 1,
        top = $('#mapping-table').offset().top + $('.book-subList').eq(0).outerHeight() * index + index + 'px',
        left = $('.book-subList').eq(0).offset().left + 1 + 'px',
        width = $('.book-subList').eq(0).width() + 1 + 'px';
      if (_top > _winH || _top + 5 < this.$input.parents('div.layui-table-body').offset().top) { // 超过显示界面则隐藏
        this.subList.hide();
        return;
      } else if (_top + 180 > _winH) { // 在上方显示
        _top = this.$input.offset().top + 1 - 180;
        this.subList.addClass('above');
      } else { // 在下方显示
        this.subList.removeClass('above');
      }
      this.subList.css({
        'top': _top,
        'left': _left,
        'width': _width
      });
      this.subList.stop().slideDown(0); //显示科目列表
    },

    // post ajax
    postRequest: function(url, params, cb, cb2, asyncType) {
      layer.load();
      $.ajax({
        async: asyncType || true,
        type: 'POST',
        url: url,
        data: params,
        dataType: 'json',
        // timeout: 30000,
        success: function(res) {
          layer.closeAll('loading');
          cb && cb(res);
        },
        error: function(res) {
          layer.closeAll('loading');
          if (cb2) {
            cb2(res);
          } else {
            if (res.responseText) {
              layer.alert(res.responseText);
            } else {
              layer.msg('请求错误');
            }
          }
        }
      });
    },

    // 函数节流
    _debounce: function(del, fun) {
      var timer;
      _this = this;
      return function() {
        if (timer) {
          clearTimeout(timer);
        }
        timer = setTimeout(function() {
          fun.apply(_this, arguments);
          timer = null;
        }, del);
      }
    },
  };
  window.subListSelect = subListSelect;
}(window));
