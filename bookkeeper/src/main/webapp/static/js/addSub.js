(function (w) {
  /**
   * 荣： 2018/1/18
   * 新增科目组件
   * @param {*} options  对象
   */
  function Subject(options) {
    this.elem = options.elem; // 唤醒弹窗的id
    this.addUnit = options.addUnit; //唤醒弹窗id
    this.url = options.url || window.baseURL + '/vat/addSubQueryAllSubToPage'; // 获取科目列表接口(新接口2008/12/24)
    //this.url = options.url || window.baseURL + '/subject/querySubByIDAndName'; // 获取科目列表接口(报废)
    this.urlCode = options.urlCode || window.baseURL + '/subject/querySubMessageMaxSubCodeStr'; // 获取科目编码接口
    this.urlSurplus = options.urlSurplus || window.baseURL + '/measure/queryMeasureBySymbolOrName?symbolOrName='; // 计量单位表接口
    //this.addUlr = options.addUlr || window.baseURL + '/subject/addSubMessage'; // 新增科目接口
    this.addUlr = options.addUlr || window.baseURL + '/vat/addSubMessage'; // 新增科目接口(新接口2008/12/24)
    //this.urlUnit = options.urlUnit || window.baseURL + '/measure/addMeasure'; // 新增科目接口（报废）
    this.SubElem = options.SubElem; // 科目列表id
    this.codeElem = options.codeElem; // 科目编码id
    this.SurpluElem = options.SurpluElem; // 数量核算id
    this.SubValElem = options.SubValElem; // 科目名称
    this.UnitSymbol = options.UnitSymbol; // 计量单位符号
    this.UnitName = options.UnitName; // 计量单位名称
    this.UnitRemarks = options.UnitRemarks; // 备注
    this.setDirection = options.setDirection; // 默认设置
    this.indexLength = options.indexLength; // 科目输入框下标
    this.refresh = options.refresh || '' // 新增科目后回调函数
    Subject.subCode = ''; // 获取的科目编码
    Subject.surplus = ''; // 计量单位
    Subject.accounId = false; // 数量核算
    Subject.foreignId = false; // 外币核算
    this.init(); // 初始化
  }

  Subject.prototype = {
    init: function () {
      this.clearPopup(); // 清空弹窗
      this.setSubList(); // 科目列表
      this.getSurplus(); // 外币核算数据
      this.openAlert(); // 唤醒弹窗
      // $(".layui-layer.layui-layer-page.demo-class").css({"z-index":"9999999999"});
      $(".layui-layer-shade").filter(":lt(0)").show().end().filter(":gt(2)").hide();
      $(".layui-layer-shade:eq(2)").hide();
    },
    // 清空弹窗
    clearPopup: function () {
      this.SurpluElem.parent().parent().css('display', 'none');
      this.elem.find('input[type="text"]').prop('value', '');
      this.elem.find('input[type="radio"]').removeAttr('checked');
      this.elem.find('input[type="radio"]').removeAttr('disabled');
      this.elem.find('input[type="checkbox"]').prop('checked', false);
      this.addUnit.find('input[type="text"]').prop('value', '');
      layui.form.render();
    },
    // 科目弹窗
    openAlert: function () {
      var that = this;
      Subject.index = layer.open({
        'type': 1,
        'skin': 'demo-class',
        'title': ['新增科目', 'font-size:18px;'],
        'area': ['520px', '500px'],
        'shade': 0.3,
        'anim': 5,
        'content': that.elem,
        'btn': ['新增', '关闭'],
        yes: function () {
          that.getSubmint();
        },
        btn2: function () {
          $(".layui-layer-shade").remove();
          that.elem.hide();
        },
        cancel: function () {
          $(".layui-layer-shade").remove();
          that.elem.hide();
        }
      });
      // 科目列表选项
      layui.form.on('select(subList)', function (data) {
        var val = data.value;
        that.getVal(val);
      });

      // 数量核算
      layui.form.on('switch(accounTing)', function (data) {
        Subject.accounId = data.elem.checked ? '1' : '0';
        if (Subject.accounId === '1') { // 显示
          that.SurpluElem.parent().parent().fadeIn();
          layui.form.render('select');
        } else {
          that.SurpluElem.parent().parent().fadeOut();
        }
      });

      // 外币核算
      layui.form.on('switch(foreignCurrency)', function (data) {
        Subject.foreignId = data.elem.checked ? '1' : '0';
      });

      // 余额方向
      /* layui.form.on('radio(overMoney)', function (data) {
           Subject.direction = data.value;
      }); */

      // 数量单位
      layui.form.on('select(SurpluList)', function (data) {
        Subject.surplus = data.value;
      });
    },
    // 科目列表
    setSubList: function () {
      var that = this;
      if (window.sessionStorage.codeList == "") {
        this.getAjax(this.url, {}, function (res) {
          var list,
            leng,
            str = '<option value="">请选择上级科目</option>',
            i = 0;
          //借贷方向（1.debit借方， 2.credit贷方）
          if (res.code === '0') {
            window.sessionStorage.setItem("codeList", JSON.stringify(res.data));
            list = res.data;
            leng = list.length;
            for (i; i < leng; i++) {
              if (list[i].subCode) {
                list[i].codenName = list[i].subCode + '-' + list[i].fullName;
                list[i].val = list[i].subCode + '/' + list[i].codeLevel + '/' + list[i].dir + '/' + list[i].fullName;
                str += '<option value="' + list[i].val + '" >' + list[i].codenName + '</option>';
              }
            }
            that.SubElem.html(str);
            renderSelect();
          }
        })
      } else {
        var list,
          leng,
          str = '<option value="">请选择上级科目</option>',
          i = 0;
        list = eval('(' + window.sessionStorage.codeList + ')');
        leng = list.length;
        for (i; i < leng; i++) {
          if (list[i].subCode) {
            list[i].codenName = list[i].subCode + '-' + list[i].fullName;
            list[i].val = list[i].subCode + '/' + list[i].codeLevel + '/' + list[i].dir + '/' + list[i].fullName;
            str += '<option value="' + list[i].val + '" >' + list[i].codenName + '</option>';
          }
        }
        that.SubElem.html(str);
        renderSelect();
      }
      // 清空科目列表
      that.SubElem.parent().off('click').on('click', function () {
        that.SubElem.next().children().children('input').val('');
      });
    },
    // 科目编码
    getCode: function (code) {
      var that = this;
      this.getAjax(this.urlCode, { 'subCode': code }, function (res) {
        Subject.subCode = res.subMessageCode || '';
        if (Subject.subCode) {
          that.codeElem.val(Subject.subCode).prop('disabled', true);
        } else {
          layer.msg('暂无编码，请手动输入');
          that.codeElem.prop('disabled', false);
        }
      })
    },
    // 数量核算数据
    getSurplus: function () {
      var that = this;
      this.getAjax(this.urlSurplus, {}, function (res) {
        var str = '<option value="">请选择计量单位</option>',
          leng,
          list = res.queryExchangeList,
          i = 0,
          leng = list.length;
        if (leng > 0) {
          for (i; i < leng; i++) {
            str += '<option value="' + list[i].pkMeasureId + '" >' + list[i].measUnitSymbol + ' / ' + list[i].measUnitName + '</option>';
          }
          that.SurpluElem.html(str);
          renderSelect();
        }
      })
    },
    // 数据判断
    getDetection: function () {
      var isTrue,
        regNum = /^[0-9]*$/;
      Subject.subName = this.SubValElem.val();
      // 科目判断
      if (!isTrue && !Subject.code) {
        isTrue = '请选择上级科目';
      }
      if (!isTrue && !Subject.subCode) {
        isTrue = '科目编码不能空';
      }
      if (!isTrue && !regNum.test(Subject.subCode)) {
        isTrue = '科目编码只能为数字';
      }
      if (!isTrue && !Subject.subName) {
        isTrue = '科目名称不能空';
      }
      if (!isTrue && !Subject.direction) {
        isTrue = '请选择余额方向';
      }
      if (Subject.accounId === '1') {
        if (!isTrue && !Subject.surplus) {
          isTrue = '请选择计量单位';
        }
      }
      return isTrue;
    },
    // 处理数据
    getVal: function (val) {
      var data = val.split('/');
      Subject.code = data[0]; //上级编码
      Subject.codeLevel = ++data[1]; //编码级别
      Subject.direction = data[2] ? data[2] : '1'; //余额方向 1为借，2为贷
      Subject.fullName = data[3]; //科目名称+
      //Subject.initCreditBalance = data[4]; //期初余额(借方)
      //Subject.initDebitBalance = data[5]; //期初余额(贷方)

      // 默认选项
      if (Subject.direction === '1') {
        this.setDirection.children().eq(0).prop('checked', true).removeAttr('disabled');
        this.setDirection.children().eq(2).prop('disabled', true).removeAttr('checked');
      } else if (Subject.direction === '2') {
        this.setDirection.children().eq(2).prop('checked', true).removeAttr('disabled');
        this.setDirection.children().eq(0).prop('disabled', true).removeAttr('checked');
      }
      layui.form.render('radio');
      this.getCode(Subject.code);
    },
    // 提交数据
    getSubmint: function () {
      var loadIndex = layer.load();
      // 判断条件
      var that = this;
      var isTrue = this.getDetection();
      if (isTrue) {
        layer.msg(isTrue);
        layer.close(loadIndex);
        return false;
      }
      if (Subject.subCode.substr(Subject.subCode.length - 3) == "001") {
        layer.confirm("如果您在" + Subject.code + "科目下设置明细科目，它将升级为非明细科目，这样" + Subject.code + "科目本期内发生的所有业务都将转移到新增加的明细科目" + Subject.subCode + "中，是否确认这种引起科目级别发生变化的修改？", {
          btn: ['确定', '取消'] //按钮
        }, function () {
          addSub();
        }, function () {
          layer.close(loadIndex);
        });
      } else {
        addSub();
      }

      function addSub() {
        var params = {
          //'superiorCoding': Subject.code,
          'subCode': Subject.subCode,
          'fullName': Subject.fullName + '_' + Subject.subName,
          'subName': Subject.subName,
          //'measureState': Subject.accounId || 0,
          //'exchangeRateState': Subject.foreignId || 0,
          //'codeLevel': Subject.codeLevel,
          //'debitCreditDirection': Subject.direction,  //借贷方向
          //'pkMeasureId': Subject.surplus || '',
          //'indexLength': this.indexLength,
        };
        // 添加科目借方为1 贷方为2
        /*if (Subject.direction === '1') {
          // var initCreditBalance = Subject.initCreditBalance === 'null' ? 0 : Subject.initCreditBalance;
          var initCreditBalance = 0;
          $.extend(params, { 'initCreditBalance': initCreditBalance });
        } else { // 贷方
          // var initDebitBalance = Subject.initDebitBalance === 'null' ? 0 : Subject.initDebitBalance;
          var initDebitBalance = 0;
          $.extend(params, { 'initDebitBalance': initDebitBalance });
        }*/
        that.getAjax(that.addUlr, params, function (res) {
          if (res.code === "0") {
            that.elem.hide();
            layer.msg(res.msg || '科目新增成功');
            //将新增成功数据添加到sessionStorage
            if (window.location.pathname == window.baseURL + "/subinit/subMappingView") {
              $("#addSub").show();
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
                'indexLength': index // 科目输入框下标
                //'refresh': refresh // 新增科目回调函数
              });
              $('#mapping-body').find("textarea.book-subText").eq(index - 1).val(params.subCode + '-' + params.fullName); // 回写
              subListSelect.prototype._getSubListData(res.data);
            } else {
              layer.close(Subject.index);
            }
            var ss = {
              pkSubId: null,
              subCode: Subject.subCode,
              subName: Subject.subName,
              fullName: Subject.fullName + '_' + Subject.subName,
              endingBalanceDebit: null,
              endingBalanceCredit: null,
              dir: Subject.direction
            };
            //将sessionStorage取出并将josn格式转换成数组格式 JSON.parse
            var list = eval('(' + window.sessionStorage.codeList + ')');
            //截取出当前科目的父级科目编码
            var str = Subject.subCode.substring(0, Subject.subCode.length - 3);
            //判断科目编码位数是否001
            if (Subject.subCode.substr(Subject.subCode.length - 3) == "001") {
              //循环科目列表与当前科目父级匹配
              for (var i = 0; i < list.length; i++) {
                if (list[i].subCode == str) {
                  //将新增科目添加到科目列表
                  list.splice(i + 1, 0, ss);
                  // 新增科目后回调函数
                  if (that.refresh) {
                    that.refresh(params);
                  }
                  //存到sessionStorage，并将数组格式转换成josn格式
                  window.sessionStorage.setItem("codeList", JSON.stringify(list));
                  return;
                }
              }
            } else {
              //循环科目列表与当前科目父级匹配
              for (var i = 0; i < list.length; i++) {
                //获取新增科目长度
                var subCodeL = list[i].subCode.length;
                //判断当前科目是否有子级，此条件相等则有子级
                if (list[i].subCode.substring(0, subCodeL) == parseFloat(Subject.subCode) - 1) {
                  //循环当前科目之后的科目
                  for (var j = i; j < list.length; j++) {
                    //截取子级科目的父级科目编码，和新增科目编码作比较，如果不相等说明已经不是子级科目
                    if (list[j].subCode.substring(0, subCodeL) != parseFloat(Subject.subCode) - 1) {
                      //将新增科目添加到科目列表
                      list.splice(j, 0, ss);
                      //新增科目后回调函数
                      if (that.refresh) {
                        that.refresh(params);
                      }
                      //存到sessionStorage，并将数组格式转换成josn格式
                      window.sessionStorage.setItem("codeList", JSON.stringify(list));
                      //console.log(list);
                      return;
                    }
                  }
                } else {
                  //判断当前科目编码和新增科目编码是否小1
                  if (parseFloat(list[i].subCode) == parseFloat(Subject.subCode) - 1) {
                    //将新增科目添加到科目列表
                    list.splice(i + 1, 0, ss);
                    // 新增科目后回调函数
                    if (that.refresh) {
                      that.refresh(params);
                    }
                    //存到sessionStorage，并将数组格式转换成josn格式
                    window.sessionStorage.setItem("codeList", JSON.stringify(list));
                    //console.log(list);
                    return;
                  }
                }
              }
            }
          } else {
            res.msg && layer.msg(res.msg);
          }
        })
      }
    },
    // 数据请求
    getAjax: function (url, params, cb) {
      layer.load();
      $.ajax({
        'type': 'POST',
        'url': url,
        'data': params,
        'dataType': 'json',
        success: function (res) {
          //567 是ajax请求服务器，检查到身份过期返回给前端特别的号码
          if (res.code == '567') {
            layer.alert(res.msg);
            var param = encodeURI(res.msg);
            var url = decodeURI(window.baseURL + "/system/ajaxAuthentication?msg=" + res.msg);
            top.location.href = url;
          } else {
            layer.closeAll('loading');
            cb && cb(res);
          }
        },
        error: function (res) {
          layer.closeAll('loading');
          if (res.responseText) {
            if (res.responseText.search('重新登录') != -1) {
              layer.open({
                type: 1,
                title: '异地登录',
                area: ['100%', '100%'],
                content: res.responseText,
                cancel: function () {
                  layer.closeAll('tips');
                }
              });
            } else {
              layer.alert(res.responseText);
            }
          } else {
            layer.msg(res);
          }
        }
      })
    }
  };
  window.Subject = Subject;
}(window));
