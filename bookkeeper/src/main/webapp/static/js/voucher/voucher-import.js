/*
 * @Author: weiqi
 * @Date: 2018-12-26 10:00:00
 * @Last Modified by: weiqi
 * @Last Modified time: 2018-12-27 12:00:00
 */
;
(function () {
  var show = getPurviewData(400); // 判断权限
  var vouchIdQJ; 
  $(function () {
    var _pathname = window.location.pathname;
    if (_pathname.indexOf('/voucher/importView') >= 0) { // 新增凭证页面
      _add();
    } else if (_pathname.indexOf('/voucher/editView') >= 0) { // 编辑凭证页面
      _edit();
    }
  });
  
  lay('#copyDate').each(function() {
    layui.laydate.render({
      elem: '#copyDate',
      done: function(val) {
      	//获取凭证字号
      	if(val != ""){
      		changeDate(val);
      	}else{
      		layer.msg("请选择日期");
      	}
      }
    });
  });
  
  function changeDate(val){
  	var params = {
      changeDate: val
    };
  	postRequest(window.baseURL + '/voucher/copyVoucherChangeDate', params, function (res) {
  		if(res.code == "0"){
  			$(".nextVoucherNo").val(res.nextVoucherNo);
  		}else{
  			layer.msg(res.msg || "获取不到凭证字号！");
  		}
  	})
  }
  
  // 复制粘贴
  $('#copy').on('click', function () {
    _copy();
  });
  // 新增凭证页面
  function _add() {
    // 显示日期
    if (session.busDate) {
      var date = session.busDate.split('-'),
        day = new Date(date[0], date[1], 0).getDate();
      $('#period').val(session.busDate + '-' + day);
    }
    // 判断权限
    if ('block' === show) {
      getRequest(window.baseURL + '/vat/goinAddVoucherPage', function (res) {
        if ('0' === res.code) { // 未结转,新增凭证
          // 判断上传凭证
          if ('true' === res.importVoucher) {
            $('#uploading').show();
          } else {
            $('#uploading').hide();
          }
          $('#addSave').show();
          $('#currVoucherNo, #book-remark').prop('readonly', false);
          $('#currVoucherNo').val(res.maxVoucherNo);
          $('#book-remark').removeClass('red');
          initData(0, false, ''); // 新增凭证初始化
        } else {
          if ('2' === res.code) {
            $('#book-state').show(); // 显示结转图片
          }
          $('#currVoucherNo, #book-remark').prop('readonly', true);
          $('#i-Speed, #uploading, #addSave').hide();
          layer.alert(res.msg || '新增凭证初始化异常', {
            icon: 2
          });
          // initData(true, false, ''); // 新增凭证初始化
        }
      });
    } else {
      layer.alert('您没有权限录入修改凭证!', {
        icon: 0,
        cancel: function () {
          top.$('#top_tabs > li.layui-this > i.layui-tab-close').click();
        }
      }, function () {
        top.$('#top_tabs > li.layui-this > i.layui-tab-close').click();
      });
      $('#currVoucherNo, #book-remark').prop('readonly', true);
      $('#i-Speed, #uploading, #addSave').hide();
      initData(true, false, ''); // 新增凭证初始化
    }
  }

  // 编辑凭证页面
  function _edit() {
    // 查询凭证信息 editVouch
    if (window.sessionStorage.editVouch) {
      // 判断权限
      if ('block' === show) {
        var editVouch = JSON.parse(window.sessionStorage.editVouch);
        initData(0, editVouch.isHasData, editVouch.id);
      } else {
        layer.alert('您没有权限录入修改凭证!', {
          icon: 0,
        });
        initData(true, editVouch.isHasData, editVouch.id);
      }
      /*// 上一张凭证查看
      $('#prevVc').off('click').on('click', function () {
        _getSibling('prev');
      });

      // 下一张凭证查看
      $('#nextVc').off('click').on('click', function () {
        _getSibling('next');
      });*/
    } else {
      console.error('编辑凭证页面未找到 editVouch 凭证ID');
      layer.alert('未知异常,请重新打开页面', {
        icon: 0,
        cancel: function () {
          top.$('#top_tabs > li.layui-this > i.layui-tab-close').click();
        }
      }, function () {
        top.$('#top_tabs > li.layui-this > i.layui-tab-close').click();
      });
    }
  }

  // 初始化数据
  function initData(isCheck, isHasData, id) {
    new InitCertificate({
      'isCheck': isCheck,
      'content': $('#content'),
      'subList': $('#subList'),
      'Type': {
        'isHasData': isHasData
      },
      'vouchID': id || ''
    });
    return vouchIdQJ = id;
  }

  // 获取编辑凭证页面是从生成凭证页面哪个列表打开的
  function _getVouchTab() {
    var type = 1;
    // 生成凭证页面tab
    if (window.sessionStorage.editVouchTab === '1') {
      type = 2;
    }
    return type;
  }

  // 获取上下凭证
  function _getSibling(place) {
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
      type: _getVouchTab(), // 1查询全部凭证 2查询修正凭证
      vid: editVouch.id, // 凭证主键
      voucherNo: editVouch.voucherNo || $('#currVoucherNo').val() // 凭证号
    };
    postRequest(url, params, function (res) {
      if (res.code === '0') {
        layer.msg(res.msg || '获取凭证成功', {
          icon: 1,
          time: 1000
        });
        var vouchID = res.data.voucherHead.vouchID;
        window.sessionStorage.editVouch = window.sessionStorage.editVouch.replace(that.vouchID, vouchID); // 修改sessionStorage.editVouch
        initData(0, true, vouchID);
      } else {
        layer.msg(res.msg || '获取凭证上下页信息异常', {
          icon: 2,
          shade: 0.1,
          shadeClose: true,
          time: 2000
        });
      }
    });
  }
  
  function _copy(){
  	//$("#copyDate").val($("#period").val());
  	changeDate($("#period").val());
  	var params = {
      vouchID: vouchIdQJ
    };
    //复制
  	postRequest(window.baseURL + '/voucher/getCopyOldVoucher', params, function (res) {
  		if(res.code == "0"){
  			$("#copyDate").val(res.qj+'-01');
  			changeDate($("#copyDate").val());
//			layer.msg(res.msg || "复制成功");qj
  		}else{
  			layer.msg(res.msg || "复制异常");
  		}
  	})
  	layer.open({
	  type: 1
	  , title: '复制凭证'
	  , area: ['360px', '280px']
	  , anim: 2
	  , btn: ['粘贴', '关闭']
	  , content: $("#copyPaste")
	  , yes: function (idx) {
	    //粘贴
	    copyPaste();
	    layer.close(idx);
	    $("#copyPaste").hide();
	  }
	  , btn2: function (idx) {
	  	layer.close(idx);
	    $("#copyPaste").hide();
	  }
	  , cancel: function (idx) {
	    $("#copyPaste").hide();
	  }
	});
  }
  
  function copyPaste(){
//	var content = [],
//	contentUl = $("#content ul");
//	var ctW2 = $("#content .book-content .w2 .book-subName"),
//	ctWLength = contentUl.length;
//	for(var j=0; j<contentUl.length; j++){
//		if(ctW2[j].innerText == ""){
//			ctWLength = j;
//			contentUl.length = j;
//		}
//	}
//	for(var i=0; i<ctWLength; i++){
//		var subName = $("#content ul:eq('"+i+"') .w2 .book-subName").text().split("-");
//		if($("#content ul:eq('"+i+"') .w4").attr('data-debit') != '0.00'){	//借方方向
//			var direction = "1",//借
//			amount = $("#content ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit');
//		}else{
//			var direction = "2",//贷
//			amount = $("#content ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits');
//		}
//		var stringS = $("#content ul:eq('"+i+"') .w2 .book-subName").text(),
//		CodeS = stringS.indexOf('-'),
//		subCodeS = stringS.substring(0,CodeS),
//		subNameS = stringS.substring(CodeS+1, stringS.length);
//		var vc = $("#content ul:eq('"+i+"') .w1 p").text()==""?null:$("#content ul:eq('"+i+"') .w1 p").text();
//		number = $("#content ul:eq('"+i+"') .w3").attr('data-num') ==""?null:$("#content ul:eq('"+i+"') .w3").attr('data-num');
//		price = $("#content ul:eq('"+i+"') .w3.book-self-amount").attr('data-amount') ==""?null:$("#content ul:eq('"+i+"') .w3.book-self-amount").attr('data-amount');
//		
//		//凭证分录返回给服务器的数据
//		ss = {
//			vcabstact: vc,//摘要
//			subjectID: subCodeS,//科目编码
//			vcsubject: subNameS,//科目名称
//			number: number,//数量
//			price: price,//价格
//			direction: direction//方向
//		}
//		if(direction=="1"){
//			ss.debitAmount = amount;
//			ss.creditAmount = null;
//		}else{
//			ss.debitAmount = null;
//			ss.creditAmount = amount;
//		}
//		content.push(ss);
//	}
  	var params = {
      //vbContent: JSON.stringify(content),
      vouchID: vouchIdQJ,
      voucherNo: $(".nextVoucherNo").val(),
      changeDate :$("#copyDate").val()
    };
    postRequest(window.baseURL + '/voucher/saveCopyVoucher', params, function (res) {
    	if(res.code == "0"){
    		top._subListRefresh(res.data); // 修改科目列表期末余额
    		if($("#copyDate").val().substring(0,7) == $("#period").val().substring(0,7)){
    			layer.confirm('复制成功,是否查看凭证?', {
			        icon: 3,
			        btn: ['关闭','查看'],
			        title: '复制成功'
			    },function(index){
			    	layer.close(index);
			    },
			    function (index) {
			      	window.sessionStorage.editVouch = window.sessionStorage.editVouch.replace(vouchIdQJ, res.vouchID); 
			      	//修改sessionStorage.editVouch
			      	$('#currVoucherNo').val(params.voucherNo);
			      	_edit();
			      	layer.close(index);
			    })
    		}else{//cmccCopy
    			layer.confirm('复制成功,是否查看凭证?', {
			        icon: 3,
			        btn: ['关闭','查看'],
			        title: '复制成功'
			    }, function (index) {
			    	layer.close(index);
			    },
			    function (index) {
			    	layer.close(index);
			    	$("#cmccCopy #period").val($("#copyDate").val());//日期
			    	$("#cmccCopy #currVoucherNo").val($(".nextVoucherNo").val());//凭证号
			      	layer.open({
					    type: 1
					    , title: '凭证查看'
					    , area: ['1400px', '680px']
					    , anim: 2
					    , content: $("#cmccCopy")
					    , btn: ['关闭']
					    , yes: function (idx) {
					      layer.close(idx);
					      $("#cmccCopy").hide();
					    }
					    , cancel: function (idx) {
					      $("#cmccCopy").hide();
					    }
					});
					$("#cmccCopy .copyHint").text("检查到复制的凭证期间不属于当前期间,如要查看复制后的凭证请切换会计期间到"+$("#copyDate").val().substring(0,4)+"年第"+$("#copyDate").val().substring(6,7)+"期！")
					clickVoucherCopy(res.vouchID,1);
			    })
//  			layer.msg("保存成功,请在"+$("#copyDate").val().substring(0,4)+"年第"+$("#copyDate").val().substring(6,7)+"期查看！");
    		}
    	}else{
    		layer.msg(res.msg || "保存异常");
    	}
    })
  }
  function clickVoucherCopy(val,e){
  	//初始化表格
	new InitCertificate({
      'isCheck': 0,
      'content': $('#contentCopy'),
      'subList': $('#subList'),
      'Type': {
        'isHasData': false
      }
    });
    var params = {
      vouchID: val
    };
    postRequest(window.baseURL + '/voucher/toEditVoucher', params, function (res) {
    	if(res.code == "0"){
    		var $list = res.voucher.voucherBodyList,
	  		ullength = $("#contentCopy ul").length,
	  		TotalJ = 0,
	  		TotalD = 0;
		  	for(var i=0; i<$list.length; i++){
		  		if($list.length > ullength){
			  		$("#contentCopy .book-content:eq('"+(ullength-1)+"') .w5 .book-add").click();
			  		ullength += 1;
			  	}
		  		if(e != 1){
		  			var danjia = '',
		  			debitAmount = '',
		  			debS = '',
		  			prfS = '';
		  		}else{
		  			if($list[i].debitAmount == "" || $list[i].debitAmount == undefined || $list[i].debitAmount == null){
		  				
			  			var danjia = '',
			  			price = '',
			  			debS = '',
			  			debitAmount = '',
			  			prfS = '';
		  			}else{
			  				
			  			var danjia = $list[i].price || '',
			  			debitAmount = $list[i].debitAmount || '';
		  			}
		  		}
		  		for(var j=0; j<top.subListData.length; j++){
					if($list[i].subjectID == top.subListData[j].subCode){
						if(top.subListData[j].direction == "1"){
							var endingBalance = top.subListData[j].endingBalanceDebit - top.subListData[j].endingBalanceCredit;
						}else{
							var endingBalance = top.subListData[j].endingBalanceCredit - top.subListData[j].endingBalanceDebit;	
						}
					}else{
						var endingBalance = '';
					}
				}
		  		if($list[i].direction == "1"){
					//借方金额
					if($list[i].debitAmount != "" ){
						TotalJ += parseFloat($list[i].debitAmount);
					}
					if($list[i].subjectID.substring(0,4) == "1405" || $list[i].subjectID.substring(0,4) == "1403"){
						$("#contentCopy ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(借)期末数量：'+$list[i].number);
					}else{
						$("#contentCopy ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(借)');
					}
					$("#contentCopy ul:eq('"+i+"') .w2").attr('data-subtext',$list[i].subjectID+'/'+$list[i].vcsubject+'/'+$list[i].direction+'/'+danjia+'/0');
					$("#contentCopy ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit',debitAmount);
					$("#contentCopy ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits','0.00');
					$("#contentCopy ul:eq('"+i+"') .w4.book-right-credits input").val('0.00');
					$("#contentCopy ul:eq('"+i+"') .w4.book-left-debit input").val(debitAmount);
					$("#contentCopy ul:eq('"+i+"') .w4.book-left-debit p").html('<span style="color: rgb(51, 51, 51);">'+ parseFloat($list[i].debitAmount.toFixed(2).replace(/\./g, "")) +'</span>');
		  		}else{
					//贷方金额
					if($list[i].creditAmount != "" ){
						TotalD += parseFloat($list[i].creditAmount);
					}
					if($list[i].subjectID.substring(0,4) == "1405" || $list[i].subjectID.substring(0,4) == "1403"){
						$("#contentCopy ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(贷)期末数量：'+$list[i].number);
					}else{
						$("#contentCopy ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(贷)');
					}
					$("#contentCopy ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit','0.00');
					$("#contentCopy ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits',$list[i].creditAmount);
					$("#contentCopy ul:eq('"+i+"') .w4.book-right-credits input").val($list[i].creditAmount);
					$("#contentCopy ul:eq('"+i+"') .w4.book-left-debit input").val('0.00');
					$("#contentCopy ul:eq('"+i+"') .w4.book-right-credits p").html('<span style="color: rgb(51, 51, 51);">'+ parseFloat($list[i].creditAmount.toFixed(2).replace(/\./g, "")) +'</span>');
					$("#contentCopy ul:eq('"+i+"') .w2").attr('data-subtext',$list[i].subjectID+'/'+$list[i].vcsubject+'/'+$list[i].direction+'/0/'+danjia);
		  		}
		  		if(i == 0){
					//摘要
					$("#contentCopy >ul:eq('"+i+"')>li:eq(0)").attr('data-abstract',$list[i].vcabstact);
					$("#contentCopy >ul:eq('"+i+"')>li:eq(0) textarea").val($list[i].vcabstact);
					$("#contentCopy >ul:eq('"+i+"') > li:eq(0) p").text($list[i].vcabstact);
				}
		  		//科目
				$("#contentCopy ul:eq('"+i+"') .w2 .book-subName").attr('title',$list[i].subjectID+'-'+$list[i].vcsubject);
				$("#contentCopy ul:eq('"+i+"') .w2 .book-subName").text($list[i].subjectID+'-'+$list[i].vcsubject);
				$("#contentCopy ul:eq('"+i+"') .w2 textarea").val($list[i].subjectID+'-'+$list[i].vcsubject);
				if($list[i].subjectID.substring(0,4)=='1403' || $list[i].subjectID.substring(0,4)=='1405'){
					//数量
					$("#contentCopy ul:eq('"+i+"') .w3.book-self-num").attr('data-num',$list[i].number);
					$("#contentCopy ul:eq('"+i+"') .w3.book-self-num p").text($list[i].number);
					//单价
					$("#contentCopy ul:eq('"+i+"') .w3.book-self-amount").attr('data-amount',danjia);
					$("#contentCopy ul:eq('"+i+"') .w3.book-self-amount p").text(danjia);
				}
		  	}
		  	if(e == 1 && TotalJ != "" && TotalJ != "NaN"){
		  		var TotalJS = TotalJ.toFixed(2).replace(/\./g, ""),
				TotalDS = TotalD.toFixed(2).replace(/\./g, "");
				$('.book-floor .book-content #debit').text(TotalJS);
				$('.book-floor .book-content #credits').text(TotalDS);
				InitCertificate.debitVal = TotalJ.toFixed(2);	//借方总金额
				InitCertificate.creditsVal = TotalD.toFixed(2); //贷方总金额
				var momey = formatAmount(TotalD);
				$('#cmccCopy #formatAmount').text(momey);
				$("#warehouse").hide();
		  	}
		  	
    	}else{
    		layer.msg(res.msg || "查看异常");
    	}
    })
  	
  }
  
  
  
  
  // 模板更多提示
  $('.more').on('mouseenter', function () {
  	$('.more span').css({"height":"29px"});
  	$('.more ul').css({"border":"1px solid #1E9FFF"});
  	$('.more i').html('&#xe619;');
    $('.more ul').show();
  }).on('mouseleave', function () {
  	$('.more span').css({"height":"28px"});
  	$('.more i').html('&#xe61a;');
    $('.more ul').hide();
  });
  //点击模板类别
  $(".templateR:eq(0)").on('click', function(event){
  	event.stopPropagation();
  	$(this).css({"border":"1px solid #4381e6"});
  	$(".templateDiv").show();
  })
  $(".templateDiv").on('click', function (event){
  	event.stopPropagation();
  	$(".newTemplate ul li .templateR:eq(0)").css({"border":"1px solid #ccc"});
  	$(this).hide();
  })
  
  //选中模板类别OR选择模板
  $("#templateBody,#choiceTempBody").delegate('tr', 'click', function (){
  	$(this).css({"background":"#f9ffb1"});
  	$(this).addClass("intro");
  	$(this).siblings('tr').removeClass("intro");
  	$(this).siblings('tr').css({"background":"#fff"});
  });
  //选择模板类别
  $("#templateList").delegate('li', 'click', function (){
  	$(".templateR>span").attr('id',$(this).attr('id'));
  	$(".templateR>span").text($(this).text());
  })
  //点击保存为凭证模板
  $(".preservation").on('click', function () {
  	var ctW1 = $("#content .book-content .w1 p"),
  	ctW2 = $("#content .book-content .w2 .ellipsis"),
  	ctW3 = $("#content .book-content .book-left-debit span"),
  	ctW4 = $("#content .book-content .book-right-credits span");
  	if(ctW2[0].innerText == ''){
  		layer.msg("请录入至少一条有效分录！");
  	}else{
  		TemplateListA();
	  	$(".templateR>span").text('请选择...');
	  	$(".templateR input[type='text']").val('');
	  	layer.open({
		    type: 1
		    , title: '新增模板'
		    , area: ['360px', '314px']
		    , anim: 2
		    , content: $("#newTemplate")
		    , btn: ['保存', '取消']
		    , yes: function (idx) {
		      preservationTemplate();
		      layer.close(idx);
		      $("#newTemplate").hide();
		    }
		    , btn2: function (idx) {
		      $("#newTemplate").hide();
		    }
		    , cancel: function (idx) {
		      $("#newTemplate").hide();
		    }
		});
  	}
  })
  //新增模板类别
  $("#addType").on('click', function () {
  	TemplateListA();
  	layer.open({
	  type: 1
	  , title: '模板类别'
	  , area: ['560px', '514px']
	  , anim: 2
	  , content: $("#templateType")
	  , cancel: function (idx) {
	    $("#templateType").hide();
	  }
	});
  })
  //从模板生成凭证
  $(".generate").on('click', function () {
  	TemplateLisB();
  	layer.open({
	  type: 1
	  , title: '选择模板'
	  , area: ['580px', '550px']
	  , anim: 2
	  , content: $("#choiceTemplate")
	  , btn: ['确定', '关闭']
	  , yes: function (idx) {
	      var bodyTr = $("#choiceTempBody .intro .data_json").text(),
	      save = $("#choiceTempBody .intro .data_json").attr('data-save');
	      if(bodyTr != ""){
	      	clickVoucher(bodyTr,save);
	      }else{
	      	layer.msg('请先选择一个模板！');
	      }
	      layer.close(idx);
	  }
	  , btn2: function (idx) {
	    $("#choiceTemplate").hide();
	  }
	  , cancel: function (idx) {
	    $("#choiceTemplate").hide();
	  }
	});
  })
  
  //查询模板列表
  function TemplateLisB(){
  	postRequest(window.baseURL + '/tempType/queryTempB','', function (res) {
  		if(res.code == "0"){
  			var list = res.msg,
  			str = "";
  			if(list != null || list == 0){
  				for(var i=0; i<list.length; i++){
  					str += '<tr id="'+list[i].tempID+'">'+
					'<td><i class="layui-icon delType" title="删除">&#xe640;</i></td>'+
					'<td>'+list[i].tempName+'</td>'+
					'<td>'+list[i].assistName+'</td>'+
					'<td class="data_json" style="display:none" data-save="'+list[i].saveAmount+'">'+list[i].vbContent+'</td>'+
					'</tr>'
  				}
  			}else{
  				str = '<tr><td colspan="3">暂无数据</td></tr>';
  			}
  			$("#choiceTempBody").html(str);
  		}else{
  			layer.msg(res.msg || '获取模板列表异常');
  		}
    })
  }
  //新增类别数据获取
  function TemplateListA(){
  	postRequest(window.baseURL + '/tempType/queryTempA', '', function (res) {
  		if(res.code == 0){
  			var list = res.msg,
  			str = "",
  			str1 = "";
  			if(list != null || list == 0){
  				for(var i=0; i<list.length; i++){
					str += '<li id="'+list[i].tempID+'">'+list[i].tempName+'</li>';
					str1 += '<tr id="'+list[i].tempID+'">'+
					'<td><i class="layui-icon modifyType" title="修改">&#xe642;</i>'+
					'<i class="layui-icon delType" title="删除">&#xe640;</i></td>'+
					'<td><span class="modifySpan">'+list[i].tempName+'</span>'+
					'<input type="text" class="modifyIuput"><span class="modifyBut">确定</span></td>'
					'</tr>'
  				}
  			}else{
  				str = '<li>暂无数据</li>';
  				str1 = '<tr><td colspan="2">暂无数据</td></tr>';
  			}
  			$("#templateList").html(str);
  			$("#templateBody").html(str1);
  		}else{
  			layer.msg(res.msg || '获取模板列表异常');
  		}
  	})
  }
  $(".cancel").on('click', function () {
  	$("#templateVal").val('').focus();
  })
  //增加模板类别
  $(".preservaType").on('click', function () {
  	var inputVal = $("#templateVal").val(),
  	templateBody = $("#templateBody tr");
  	params = {
      tempName: inputVal
    };
    if(inputVal == ""){
  		layer.msg("请输入类别名称！");
  		return;
  	}
    for(var i=0; i<templateBody.length; i++){
    	if($("#templateBody tr:eq('"+i+"') .modifySpan").text() == inputVal){
    		layer.msg("保存失败,名称重复，请重新输入名称");
  			return;
    	}
    }
  	postRequest(window.baseURL + '/tempType/insertTempType', params, function (res) {
  		if(res.code == 0){
  			layer.msg("保存成功");
  			$("#templateVal").val("");
  			TemplateListA();
  		}else{
  			layer.msg(res.msg || "保存异常");
  		}
  	})
  })
  //删除模板类别delType
  $("#templateBody,#choiceTempBody").delegate('.delType','click', function (){
  	var id = $(this).parent('td').parent('tr').attr('id'),
  	params = {
      tempID: id
    };
    layer.confirm('删除的类别将不能恢复，请确认是否删除？', {
        icon: 3,
        title: '提示'
    }, function () {
        postRequest(window.baseURL + '/tempType/delTem', params, function (res) {
	  		if(res.code == 0){
	  			layer.msg("删除成功");
	  			TemplateListA();
	  			TemplateLisB();
	  		}else{
	  			layer.msg(res.msg || "删除异常");
	  		}
	  	})
    });
  })
  //点击修改模板按钮
  $("#templateBody").delegate('.modifyType','click', function (event){
  	event.stopPropagation();
  	var tempName = $(this).parent('td').next('td').children('.modifySpan').text();
  	$(this).parent('td').siblings('td').children('.modifySpan').hide();
  	$(this).parent('td').siblings('td').children('.modifyIuput').show().val(tempName);
  	$(this).parent('td').siblings('td').children('.modifyBut').show();
  })
  //编辑时阻止冒泡
  $("#templateBody").delegate('.modifyIuput','click', function (event){
  	event.stopPropagation();
  })
  //确定修改
  $("#templateBody").delegate('.modifyBut', 'click', function (){ 
  	var id = $(this).parent('td').parent('tr').attr('id'),
  	name = $(this).siblings('.modifyIuput').val();
  	if(name == ""){
  		layer.msg("修改失败,类别名称不能为空！");
  		return;
  	}
  	for(var i=0; i<$("#templateBody tr").length; i++){
  		if($("#templateBody tr:eq('"+i+"')").attr('id') != id){
  			if($("#templateBody tr:eq('"+i+"') .modifySpan").text() == name){
	    		layer.msg("保存失败,名称重复，请重新输入名称");
	  			return;
	    	}
  		}
   }
  	params = {
      tempID: id,
      tempName: name
    };
  	postRequest(window.baseURL + '/tempType/uPTem', params, function (res) {
  		if(res.code == 0){
  			layer.msg("修改成功");
  			TemplateListA();
  		}else{
  			layer.msg(res.msg || "修改异常");
  		} 	
  	})
  })
  //保存模板
  function preservationTemplate(){
  	var id = $(".templateR>span").attr('id'),	//父级ID
  	tempName =  $(".templateR>span").text(),	//大类模板名称
  	assistName = $(".templateR .assistName").val(),//小类模板名称
  	saveAmount = 1,//是否保存金额
  	content = [],
  	contentUl = $("#content ul");
  	if($(".templateR input[type='checkbox']").is(':checked')){
  		saveAmount = 1;
  	}else{
  		saveAmount = 0;
  	}
  	if(tempName == "请选择..."){
  		layer.msg("请选择模板类别");
  		return;
  	}else if(assistName == ""){
  		layer.msg("请填写模板名称");
  		return;
  	}
  	var ctW2 = $("#content .book-content .w2 .book-subName"),
  	ctWLength = contentUl.length;
  	for(var j=0; j<contentUl.length; j++){
  		if(ctW2[j].innerText == ""){
  			ctWLength = j;
  			contentUl.length = j;
  		}
  	}
  	
  	for(var i=0; i<ctWLength; i++){
		var subName = $("#content ul:eq('"+i+"') .w2 .book-subName").text().split("-");
		if($("#content ul:eq('"+i+"') .w4").attr('data-debit') != '0.00'){	//借方方向
			var direction = "1",//借
			amount = $("#content ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit');
		}else{
			var direction = "2",//贷
			amount = $("#content ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits');
		}
		if(saveAmount != 1){	//是否保存金额
			amount = ""	
		}
		var stringS = $("#content ul:eq('"+i+"') .w2 .book-subName").text(),
		CodeS = stringS.indexOf('-'),
		subCodeS = stringS.substring(0,CodeS),
		subNameS = stringS.substring(CodeS+1, stringS.length);
  		ss = {
  			zy: $("#content ul:eq('"+i+"') .w1 p").text(),//摘要
  			subCode: subCodeS,//科目编码
  			subName: subNameS,//科目名称
  			number: $("#content ul:eq('"+i+"') .w3").attr('data-num') || '',//数量
  			price: $("#content ul:eq('"+i+"') .w3.book-self-amount").attr('data-amount') || '',//价格
  			dir: direction,//方向
  			amount: amount//金额
  		}
  		content.push(ss);
  	}
  	params = {
      pid: id,
      tempName: tempName,
      assistName: assistName,
      saveAmount: saveAmount,
      content: JSON.stringify(content)
    };
  	postRequest(window.baseURL + '/tempType/saveTempVoucher', params, function (res) { 
  		if(res.code == '0'){
  			//layer.msg(res.msg || "保存成功");
  		}else{
  			layer.msg(res.msg || "保存异常");
  		}
  	})
  }
  //选中类别双击事件
  $("#templateBody").delegate('tr','dblclick', function (){
  	$(".templateR>span").text($(this).children('td').children('.modifySpan').text());
  	$(".templateR>span").attr('id',$(this).attr('id'));
  	layer.close(layer.index)
  })
  //选中模板双击事件
  $("#choiceTempBody").delegate('tr','dblclick', function (){
	var bodyTr = $("#choiceTempBody .intro .data_json").text(),
	save = $("#choiceTempBody .intro .data_json").attr('data-save');
	clickVoucher(bodyTr,save);
	layer.closeAll();
  })
  //点击模板生成凭证
  function clickVoucher(val,e){
  	//初始化表格
	new InitCertificate({
      'isCheck': 0,
      'content': $('#content'),
      'subList': $('#subList'),
      'Type': {
        'isHasData': false
      }
    });
	$('.book-floor .book-content #debit').text('');
	$('.book-floor .book-content #credits').text('');
	$('#formatAmount').text('');
  	var $list = JSON.parse(val),
  		ullength = $("#content ul").length,
  		TotalJ = 0,
  		TotalD = 0;
  	for(var i=0; i<$list.length; i++){
  		if($list.length > ullength){
	  		$(".book-content:eq('"+(ullength-1)+"') .w5 .book-add").click();
	  		ullength += 1;
	  	}
  		if(e != 1){
  			TotalD = '';
  			TotalJ = '';
  			var danjia = '',
  			amount = '',
  			debS = '',
  			prfS = '';
  		}else{
  			if($list[i].amount == "" || $list[i].amount == undefined){
  				TotalD = '';
	  			TotalJ = '';
	  			var danjia = '',
	  			price = '',
	  			debS = '',
	  			amount = '',
	  			prfS = '';
  			}else{
	  			var danjia = $list[i].price || '',
	  			amount = $list[i].amount || '',
	  			debitAmountS = amount.replace(/\./g, ""),
	  			creditAmountS = amount.replace(/\./g, ""), 
	  			debS = parseFloat(debitAmountS),
	  			prfS = parseFloat(creditAmountS);
  			}
  		}
  		for(var j=0; j<top.subListData.length; j++){
			if($list[i].subCode == top.subListData[j].subCode){
				if(top.subListData[j].dir == "1"){
					var endingBalance = top.subListData[j].endingBalanceDebit - top.subListData[j].endingBalanceCredit;
				}else{
					var endingBalance = top.subListData[j].endingBalanceCredit - top.subListData[j].endingBalanceDebit;	
				}
			}else{
				var endingBalance = '';
			}
		}
  		if($list[i].dir == "1"){
			//借方金额
			if($list[i].amount != "" ){
				TotalJ += parseFloat($list[i].amount);
			}
			if($list[i].subCode.substring(0,4) == "1405" || $list[i].subCode.substring(0,4) == "1403"){
				$("#content ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(借)期末数量：'+$list[i].number);
			}else{
				$("#content ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(借)');
			}
			$("#content ul:eq('"+i+"') .w2").attr('data-subtext',$list[i].subCode+'/'+$list[i].subName+'/'+$list[i].dir+'/'+danjia+'/0');
			$("#content ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit',amount);
			$("#content ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits','0.00');
			$("#content ul:eq('"+i+"') .w4.book-right-credits input").val('0.00');
			$("#content ul:eq('"+i+"') .w4.book-left-debit input").val(amount);
			$("#content ul:eq('"+i+"') .w4.book-left-debit p").html('<span style="color: rgb(51, 51, 51);">'+ debS +'</span>');
  		}else{
			//贷方金额
			if($list[i].amount != "" ){
				TotalD += parseFloat($list[i].amount);
			}
			if($list[i].subCode.substring(0,4) == "1405" || $list[i].subCode.substring(0,4) == "1403"){
				$("#content ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(贷)期末数量：'+$list[i].number);
			}else{
				$("#content ul:eq('"+i+"') .w2 .book-subMoney").text('期末余额：'+endingBalance+'(贷)');
			}
			$("#content ul:eq('"+i+"') .w4.book-left-debit").attr('data-debit','0.00');
			$("#content ul:eq('"+i+"') .w4.book-right-credits").attr('data-credits',amount);
			$("#content ul:eq('"+i+"') .w4.book-right-credits input").val(amount);
			$("#content ul:eq('"+i+"') .w4.book-left-debit input").val('0.00');
			$("#content ul:eq('"+i+"') .w4.book-right-credits p").html('<span style="color: rgb(51, 51, 51);">'+ prfS +'</span>');
			$("#content ul:eq('"+i+"') .w2").attr('data-subtext',$list[i].subCode+'/'+$list[i].subName+'/'+$list[i].dir+'/0/'+danjia);
  		}
  		if(i == 0){
			//摘要
			$("#content >ul:eq('"+i+"')>li:eq(0)").attr('data-abstract',$list[i].zy);
			$("#content >ul:eq('"+i+"')>li:eq(0) textarea").val($list[i].zy);
			$("#content >ul:eq('"+i+"') > li:eq(0) p").text($list[i].zy);
		}
  		//科目
		$("#content ul:eq('"+i+"') .w2 .book-subName").attr('title',$list[i].subCode+'-'+$list[i].subName);
		$("#content ul:eq('"+i+"') .w2 .book-subName").text($list[i].subCode+'-'+$list[i].subName);
		$("#content ul:eq('"+i+"') .w2 textarea").val($list[i].subCode+'-'+$list[i].subName);
		if($list[i].subCode.substring(0,4)=='1403' || $list[i].subCode.substring(0,4)=='1405'){
			//数量
			$("#content ul:eq('"+i+"') .w3.book-self-num").attr('data-num',$list[i].number);
			$("#content ul:eq('"+i+"') .w3.book-self-num p").text($list[i].number);
			//单价
			$("#content ul:eq('"+i+"') .w3.book-self-amount").attr('data-amount',danjia);
			$("#content ul:eq('"+i+"') .w3.book-self-amount p").text(danjia);
		}
  	}
  	if(e == 1 && TotalJ != "" && TotalJ != "NaN"){
  		var TotalJS = TotalJ.toFixed(2).replace(/\./g, ""),
		TotalDS = TotalD.toFixed(2).replace(/\./g, "");
		$('.book-floor .book-content #debit').text(TotalJS);
		$('.book-floor .book-content #credits').text(TotalDS);
		InitCertificate.debitVal = TotalJ.toFixed(2);	//借方总金额
		InitCertificate.creditsVal = TotalD.toFixed(2); //贷方总金额
		var momey = formatAmount(TotalD);
		$('#formatAmount').text(momey);
		$("#warehouse").hide();
  	}
  }
  function formatAmount(n) {
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
    }
}());
