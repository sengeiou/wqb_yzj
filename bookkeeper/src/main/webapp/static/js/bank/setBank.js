/**
 ** 银行设置
 **/
$(function() {
  queryBank2Subject();
  //获取银行资料列表
  function queryBank2Subject() {
    postRequest(window.baseURL + '/bank/queryBank2Subject', {}, function(res) {
      if (res.success === 'true') {
        var i = 0,
          len = res.list.length,
          data,
          str = '';
        if (len > 0) {
          for (i; i < len; i++) {
            data = res.list[i];
            str += '<tr id=' + data.id + '>' +
              '<td class="bankNameTD">' + data.bankName + '</td>' +
              '<td class="bankTypeTD">' + data.bankType + '</td>' +
              '<td class="currencyTD">' + data.currency + '</td>' +
              '<td class="bankAccountTD">' + data.bankAccount + '</td>' +
              '<td class="subCodeTD">' + data.subCode + '-' + data.subFullName + '</td>' +
              '<td><spam class="bankData_updata" style="cursor:pointer;color:#539dfc;">修改</span></td>' +
              '</tr>';
          }
        } else {
          str = '<tr><td colspan="6">暂无数据</td></tr>'
        }
        $("#bankData").html(str);
      } else {
        res.msg && layer.msg(res.msg);
      }
    });
  }
  //获取账套科目
  postRequest(window.baseURL + '/bank/queryMjBankSubject', {}, function(res) {
    if (res.success === 'true') {
      var i = 0,
          len = res.list.length,
          data,
          str = '';
      if (len > 0) {
        for (i; i < len; i++) {
        	data = res.list[i];
          str += '<li name="' + data.subName + '" id="' + data.subCode + '" data-name="' + data.fullName + '" data-Id="' + data.pkSubId + '">' + data.subCode + '-' + data.fullName + '</li>';
        }
      } else {
        str = '<li>暂无数据</li>'
      }
      $("#select-bankKM").html(str);
    } else {
      res.msg && layer.msg(res.msg);
    }
  });

  $(".search-itemS").delegate("li", "click", function() {
    $(".right .rightKm").val($(this).text());
    $(".right .rightKm").attr("name", $(this).attr("name"));
    $(".right .rightKm").attr("id", $(this).attr("id"));
    $(".right .rightKm").attr("data-name", $(this).attr("data-name"));
    $(".right .rightKm").attr("data-Id", $(this).attr("data-Id"));
    $(".search-itemS").hide();
  });
  $(".right .rightKm").click(function() {
    $(".search-itemS").show();
  });

  //修改银行资料
  $("#bankData").delegate(".bankData_updata", "click", function() {
    var $addAndEditor = $("#addAndEditor"),
      text = "修改银行资料";
    var accountID = $(this).parent('td').parent('tr').attr("id"); //bank/queryBank2SubjectByID   id
    var params = {
      id: accountID
    }
    postRequest(window.baseURL + '/bank/queryBank2SubjectByID', params, function(res) {
      if (res.success) {
        $(".bankName").val(res.bank2Subject.bankName);
        $(".right .layui-select-title .layui-input").val(res.bank2Subject.bankType);
        $(".currency").val(res.bank2Subject.currency);
        $(".bankAccount").val(res.bank2Subject.bankAccount);
        $(".rightKm").val(res.bank2Subject.subCode + '-' + res.bank2Subject.subFullName);
        $(".rightKm").attr("id", res.bank2Subject.subCode);
        $(".rightKm").attr("name", res.bank2Subject.bankName);
        $(".rightKm").attr("data-Id", res.bank2Subject.subID);
        $(".rightKm").attr("data-name", res.bank2Subject.subFullName);
      }
    })
    $(".bankAccount").attr("readonly", "readonly");
    layer.open({
      type: 1,
      title: text,
      area: ['680px', '500px'],
      btn: ['保存'],
      content: $addAndEditor,
      yes: function(index) {
        $addAndEditor.hide();
        layer.close(index);
        baocun(accountID);
      },
      cancel: function() {
        $addAndEditor.hide();
      }
    });
  });
  //新增银行资料
  $(".AddbankData").click(function() {
    var $addAndEditor = $("#addAndEditor"),
      text = "新增银行资料";
    $("#addAndEditor input").val('');
    $(".currency").val("CNY");
    $('.bankAccount').attr("readonly", false);
    layer.open({
      type: 1,
      title: text,
      area: ['680px', '500px'],
      btn: ['保存'],
      content: $addAndEditor,
      yes: function(index) {
        if($('.bankAccount').val() == ""){
	      	layer.msg("请填写账号！");
	      	return false;
	    }
	    if($('.rightKm').val() == ""){
	      	layer.msg("请选择账套科目！");
	      	return false;
	    }
        baocun();
        $addAndEditor.hide();
        layer.close(index);
      },
      cancel: function() {
        $addAndEditor.hide();
      }
    });
  });

  //保存
  function baocun(accountID) {
    if (accountID == undefined) {
      var params = {
        bankName: $(".bankName").val(), //银行名称
        bankType: $(".right .layui-this").text(), //银行类型
        currency: $(".currency").val(), //币种
        bankAccount: $(".bankAccount").val(), //账号
        subCode: $(".rightKm").attr("id"), //科目编号
        subName: $(".rightKm").attr("name"), //科目名称
        subID: $(".rightKm").attr("data-Id"), //科目主键
        subFullName: $(".rightKm").attr("data-name") //科目全称
      };
      postRequest(window.baseURL + '/bank/addBank2Subject', params, function(res) {
        if (res.success === 'true') {
          layer.msg("保存成功");
          queryBank2Subject();
        } else {
        	layer.msg(res.msg || '保存失败');
        }
      })
    } else {
      var params = {
        id: accountID,
        bankName: $(".bankName").val(), //银行名称
        bankType: $(".right .layui-this").text(), //银行类型
        currency: $(".currency").val(), //币种
        subCode: $(".rightKm").attr("id"), //科目编号
        subName: $(".rightKm").attr("name"), //科目名称
        subID: $(".rightKm").attr("data-Id"),
        subFullName: $(".rightKm").attr("data-name") //科目全称
      };
      postRequest(window.baseURL + '/bank/updBank2Subject', params, function(res) {
        if (res.success === 'true') {
          layer.msg("保存成功");
          queryBank2Subject();
        }
      })
    }
  }
})
