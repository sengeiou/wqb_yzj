/**
** 进销项发票导入
**/
$(window).resize(function() {
  var Iheight = $("#content").height();
  var Iwidth = $("#content").width();
  $("#outputList").css({"max-height":Iheight-132});
  if($("#outputList").height() < $("#outputList table").height()){
  	if(window.screen.width < 1680){
			$("#head2").width(Iwidth-22);
		}else{
			$("#head2").width(Iwidth-17);
		}
  }else{
  	$("#head2").width(Iwidth);
  }
});
var	$index = 0,				//科目下标
	$indexS = 0,				//科目下标
	voucherType = 0,
	codeType = 0,
	identification = 0,//导入为0，修改为1，解决导入一条数据导致的问题
	treeText = [];
;(function () {
  // 显示权限按钮
  var show = getPurviewData(100),
    isCarryState = _queryStatus()[1];
  if (show === 'block') {
    $('.search').css('display', show);
    $('span.remove').show();
    $('#content').css('padding-top', '44px');
  }else{
    $('span.remove').hide();
  }
  var Iwidth = $("#content").width();
  var Iheight = $("#content").height();
  $("#outputList").css({"max-height":Iheight-132});
  if($("#outputList").height() < $("#outputList table").height()){
  	if(window.screen.width < 1680){
			$("#head2").width(Iwidth-22);
		}else{
			$("#head2").width(Iwidth-17);
		}
  }else{
  	$("#head2").width(Iwidth);
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
	if(voucherType == 1){
  	$('#popup-btn, #batch-del, .opt .remove, .opt .modifyTheBut').hide();
  	layer.alert('您已经生成凭证，不能修改数据。', {
      icon: 0
    });
  }
  $(function () {
    var layer = layui.layer,
      state = {},
      $keyWord = $('#keyWord'),
      $beginTime = $('#beginTime'),
      $endTime = $('#endTime'),
      $uploadPopup = $('#upload-popup'),
      $uploadInput = $('#upload-input'),
      $uploadBtn = $('#upload-btn'),
      $inList = $('#income-list'),
      $outList = $('#output-list'),
      listData = {},
      tabCurr = 1,
      comCurr = 1,
      outCurr = 1,
      $searchBtn = $('#search-btn'),
      $popupBtn = $('#popup-btn'),
      $batchBtn = $('#batch-del'),
      $table = $('table');
	  var url = location.hash;
	  _getSubListData();//科目
	  mapType();//映射状态
	if(url.substr(url.length-1,1) == 1){// 销项
		tabCurr = 2;
		$("#navDOM1 li:eq(1)").click();
		multiSearch(2);
		$("#beginTime").attr("placeholder","开票日期开始日期");
		$("#endTime").attr("placeholder","开票日期开始日期");
	}else if(url.substr(url.length-1,1) == 2){// 进项
		tabCurr = 1;
		$("#navDOM1 li:eq(0)").click();
		multiSearch(1);
	}
    pageInit();

    // 监听当前tab
    layui.element.on('tab(tab)', function (obj) {
      var text;
      tabCurr = obj.index + 1;
      switch (tabCurr) {
        case 1:
          text = '认证日期';
          $beginTime.attr('placeholder', text + '开始时间');
          $endTime.attr('placeholder', text + '结束时间');
          if (state.income >= 0) {
            fixedHead($('#head1'), $('#income-table'), $('#navDOM1'));
            return false;
          }
          break;
        case 2:
          text = '开票日期';
          $beginTime.attr('placeholder', text + '开始时间');
          $endTime.attr('placeholder', text + '结束时间');
          if (state.output >= 0) {
            fixedHead($('#head2'), $('#output-table'), $('#navDOM1'));
            return false;
          }
          break;
        default:
          break;
      }
      multiSearch(tabCurr);
    });

    // laydate选择日期
    lay('.wDate').each(function () {
      layui.laydate.render({
        elem: this,
        max: 0,
        theme: '#1E9FFF'
      });
    });

    // 点击搜索
    $searchBtn.on('click', function () {
      // 条件判断
      var word = $keyWord.val(),
        startTime = $beginTime.val(),
        endTime = $endTime.val();

      if (startTime || endTime) {
        if (!startTime) {
          layer.msg('请输入开始时间和结束时间', {
            time: 1000
          });
        }
        if (startTime > endTime) {
          layer.msg('开始时间不能大于结束时间', {
            time: 1000
          });
          return false;
        }
      }
      if (!word && !startTime && !endTime) {
        layer.msg('请输入任意查询条件', {
          time: 1000
        });
        if (tabCurr == 1 && $inList.attr('has-search')) {
          multiSearch(tabCurr);
        }
        else if (tabCurr == 2 && $outList.attr('has-search')) {
          multiSearch(tabCurr);
        }
        return false;
      }

      // var page = (tabCurr == 1) ? comCurr : outCurr;
      multiSearch(tabCurr);
    });

    // 打开导入Excel弹窗
    $popupBtn.on('click', showUploadPopup);

    // 改变上传Excel文件
    $uploadInput.on('change', function () {
      var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
      $(this).prev().text(v);
      $uploadBtn.removeClass('loading loaded');
    });

    // 上传Excel  发票
    $uploadBtn.on('click', function () {
      var that = this;
      uploadInit(that, window.baseURL + '/invoice/uploadFile', function(res) {
        if (res.code == '0') {
          $uploadBtn.addClass('loaded');
          layer.msg('导入成功啦', {
            icon: 1,
            shade: 0.1,
            time: 1000
          }, function () {
          	layer.closeAll();
            invoiceMapping();
            addSubject(res.invoice_crop,res.invoice_type,res.msg);
            $uploadPopup.hide();
            delete state.income;
            delete state.output;
            pageInit();
            if(url.substr(url.length-1,1) == 2){
							//invoiceMapping();
            }else if(url.substr(url.length-1,1) == 1){
							//invoiceMapping();
            }
          });
        }else if (res.code == '4' || res.code == '5') {
            layer.closeAll();
            layer.confirm('已经导入过发票了,再次导入将会覆盖之前的发票数据,确认需要覆盖吗?', {
              icon: 0,
              title: '提示',
              shade: 0.1
            }, function (index) {
                layer.closeAll();
                $uploadPopup.hide();
                delete state.income;
                delete state.output;
                // pageInit();
            	postRequest(window.baseURL + '/invoice/delFaPiaoAll', {}, function(res) {
            			if (res.code === '0') {
            				layer.msg('已清除所有进项销项发票数据,请重新导入。', {
  	        	   			  icon: 1,
  	        	   			  time: 2000 //2秒关闭（如果不配置，默认是3秒）
  	        	   			},function(){
  	        	   			  layer.closeAll();
	  	        	   		  $uploadPopup.hide();
	                      delete state.income;
	                      delete state.output;
  	        	   			  pageInit();
  	        	   			  showUploadPopup();
  	        	   			});
	        	        } else {
	        	        	$uploadPopup.hide();
	  	                layer.closeAll();
	        	        	layer.msg(res.msg || '导入失败', {
		        	   			  icon: 2,
		        	   			  shade: [0.3, '#393D49'], //遮罩  默认是0.3透明度的黑色背景（'#000'）
		        	   			  shadeClose:true,
		        	   			  time: 20000
		        	   			});
	        	        }
            	  }
            	)
            });
         } else {
           $uploadPopup.hide();
           layer.closeAll();
           layer.msg(res.msg || '导入失败', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 20000
          });
        }
      });
    });

    // 点击复选框
    $table.on('click', '.layui-form-checkbox', function () {
      checkBoxState($(this));
    })
      // 单条数据删除
      .on('click', 'span.remove', function () {
        var id = $(this).parents('tr').attr('id');
//      var Bid = $(this).parents('tr').children('.invoiceBID').children('p').length;
				var index = $(this).parent('p').index();
        var arr = [];
        for(var i=0; i<1; i++){
        	if (!id) {
					  layer.msg('本条数据ID不存在');
					  return false;
					}
					var codeId = $(this).parents('.opt').prev('.invoiceBID').children('p').get(index).innerText;
					arr.push (codeId+'-'+id);
        }
        _deleteData(arr.toString());
      });

    // 批量数据删除
    $batchBtn.on('click', function () {
      var item = (tabCurr == 1) ? $inList : $outList,
          id = batchSelect(item);
      if (!id) {
        layer.msg('请选择要删除的发票');
        return false;
      }
      _deleteData(id);
    });

    // 获取焦点， 输入框内容为空
    removeInputVal($('#endTime'));
    removeInputVal($('#beginTime'));
    removeInputVal($('#keyWord'));

		//编辑科目
		$("#subjectsBody").delegate('.subCodeDiv', 'click', function (event){
			event.stopPropagation();
			$index = $(this).parent('td').parent('tr').index();
			$indexS = $(this).parents('table').parents('tr').index();
			var heightI = $(this).offset().top,
			thisH = $(this).siblings('.subCodeInput').height(),
			thisW = $(this).siblings('.subCodeInput').width(),
			leftI = $(this).offset().left,
			scrollH = $("#content").scrollTop();
			$('.subCodeInput').hide();								//隐藏其他input框
			$('.subList').show();											//显示科目列表
			$(this).siblings('.subCodeInput').show();				//显示inpnt框
			$(this).siblings('.subCodeInput').focus();				//获取焦点
			$('.subList').css({"top":heightI+thisH+1+scrollH,"left":leftI-19,"width":thisW+2});
		})
		//阻止冒泡
		$("#subjectsBody").delegate('.subCodeInput', 'click', function (event){
			event.stopPropagation();
		})

		//悬浮TR
		$(document).on('mouseover','.codeTr',function(){
			var codeId = $(this).children('td').children('.subCodeDiv').attr('data-codeid').substring(4,0);
			$(this).children('td').children('.compile').show();
			if(codeId == "1403" || codeId == "1405"){
				$(this).children('td').children('.codeNnumber').siblings('.compile').show();
			}else{
				$(this).children('td').children('.codeNnumber').siblings('.compile').hide();
			}
		});
		$(document).on('mouseout','.codeTr',function(){
			$(this).children('td').children('.compile').hide();
		});
		//悬浮商品名称
		$(document).on('mouseover','.codeComName',function(){
			var tbodyListH = $("#subjectsBody .tr");
    	if(identification == "0"){
    		$(this).css({"border":"1px dashed red","height":"37px"});
    	}
		});
		$(document).on('mouseout','.codeComName',function(){
			$(this).css({"border":"none","height":"38px"});
		});
		//悬浮规格
		$(document).on('mouseover','.codeSpec',function(){
			var tbodyListH = $("#subjectsBody .tr");
    	if(identification == "0"){
    		$(this).css({"border":"1px dashed red","height":"37px"});
    	}
		});
		$(document).on('mouseout','.codeSpec',function(){
			$(this).css({"border":"none","height":"38px"});
		});
		//悬浮数量
		$(document).on('mouseover','.codeNnumber',function(){
			var tbodyListH = $("#subjectsBody .tr");
    	if(identification == "0"){
    		var codeId = $(this).parent('td').siblings('td').children('.subCodeDiv').attr('data-codeid').substring(4,0);
				if(codeId == "1403" || codeId == "1405"){
					$(this).css({"border":"1px dashed red","height":"37px"});
				}
    	}
		});
		$(document).on('mouseout','.codeNnumber',function(){
			$(this).css({"border":"none","height":"38px"});
		});
		//悬浮科目
		$(document).on('mouseover','.subCodeDiv',function(){
			$(this).css({"border":"1px dashed red","height":"37px"});
		});
		$(document).on('mouseout','.subCodeDiv',function(){
			if($(this).text() != "-"){
				$(this).css({"border":"none","height":"38px"});
			}
		});

		//点击选中科目
		$("#subLists").delegate('.ellipsis', 'click', function (event){
			event.stopPropagation();
			$(".subList").hide();
			$('.subCodeInput').hide();
      var $div = $("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .subCodeDiv"),
      $codeComName = $("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeComName").text(),
      $codeSpec = $("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeSpec").text();
			$div.css({"border":"none"}).text($(this).text()).attr({
        'id': $(this).attr("id"),//科目ID
        'data-codeid': $(this).attr("data-codeid"),//科目编码
        'data-name': $(this).attr("data-name"),//末级科目名称
        'data-names': $(this).attr("data-names")//科目全称
      }).next('input').val($(this).text());
      //循环遍历自动填充
      $('.subCodeDiv').css({"background":"none"});
      var $subjectsBody =$("#subjectsBody .tr"),
      $indexL = 0;
      if($(this).text().substring(0,4) != "1403" && $(this).text().substring(0,4) != "1405"){
      	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeNnumber").text('');
      	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codePrice").text('');
      	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .keycodeNnumber").val('');
      }
      for(var i=0; i<$subjectsBody.length; i++){
      	var $codeTr = $subjectsBody.find('tbody').children('.codeTr');
      	for(var j=0; j<$codeTr.length; j++){
      		if($codeComName != ""){
      			if ($($codeTr[j]).children('td').children('.subCodeDiv').text() == "-") {
	      			if($codeComName == $($codeTr[j]).children('td').children('.codeComName').text() && $codeSpec == $($codeTr[j]).children('td').children('.codeSpec').text()){
		      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-codeid',$(this).attr("data-codeid"));
		      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-name',$(this).attr("data-name"));
		      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-names',$(this).attr("data-names"));
		      			$($codeTr[j]).children('td').children('.subCodeDiv').text($(this).text());
		      			$($codeTr[j]).children('td').children('.subCodeDiv').css({"background":"#c3ddff"});
		      			$indexL = $indexL+1;
		      		}
      			}
      		}
      	}
      }
      if($indexL > 0){
      	layer.msg('已自动帮您匹配    <span style="color:red;">'+$indexL+'</span>  项');
      }
		})

		//点击空白隐藏科目列表
		$(document).click(function(){
			$(".subList").hide();
			$('.subCodeInput').hide();
		});

		// 只获取一次科目列表
    function _getSubListData() {
    	postRequest(window.baseURL + '/vat/queryAllSubToPage', { 'keyWord': '' }, function(res) {
        var list = res.data || [];
        window.subListData = list;
        if (res.code === "0") {
          if (list.length > 0) {
            for (var i = 0, len = list.length; i < len; i++) {
              if (list[i].subCode) {
                var code = list[i].subCode + '/' + list[i].fullName + '/' + list[i].dir + '/' + list[i].endingBalanceDebit + '/' + list[i].endingBalanceCredit;
              	treeText.push('<li id="'+list[i].pkSubId+'" data-codeId="'+list[i].subCode+'" data-name="'+list[i].subName+'" data-nameS="'+list[i].fullName+'" class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + ' - ' + list[i].fullName + '</li>');
              }
            }
          } else {
            str = '<li class="sub-title ellipsis">暂无数据</li>';
          }
          $('#subLists').html(treeText);
        }
      });
    }

		//点击新增科目
	  $('#sub-addSub').on('click', function(event) {
	    var e = window.event || event,
	    index = $index;
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
          var $div = $("#subjectsBody .tr:eq(" + $indexS + ") tr:eq(" + $index + ") .subCodeDiv"),
            str = params.subCode + '-' + params.fullName;
          $div.css({"border":"none"})
            .text(str)
            .attr({
              'id': '',//新增科目pkSubId未获得
              'data-codeid': params.subCode,//科目编码
              'data-name': params.subName,//末级科目名称
              'data-names': params.fullName//科目全称
            })
            .next('input').val(str);
          _getSubListData(); // 请求科目列表
        }
    });

		//页面加载获取映射状态
	  function mapType(){
	  	postRequest(window.baseURL + '/invoice/queryInvoiceMapping', {}, function(res) {
	  		if(res.code == "0"){
	  			invoiceMapping();
	  			codeType = res.invoice_type;
	  			if(codeType == '1'){
	  				$('.layui-layer .layui-layer-title').text('进项发票科目映射');
	  			}else{
	  				$('.layui-layer .layui-layer-title').text('销项发票科目映射');
	  			}
	  			addSubject(res.invoice_crop,res.invoice_type,res.msg);
	  		}
	  	})
	  }

		//获取导入成功数据
		function addSubject(crop,type,msg){
			$(".companyName").text(crop+"公司名称");
			var str = "",
			listH = msg.length;
			for(var i=0; i<listH; i++){
				var strBody = "",
				listB = msg[i].date;
				for(var j=0; j<listB.length; j++){
					var spec = listB[j].spec || '',
					subjectCode = '',
					buyCorp = listB[j].buyCorp || '',
					comName = listB[j].comName || '',
					mappingSubCode = listB[j].mappingSubCode || '',
					mappingSubName = listB[j].mappingSubName || '',
					nprice = listB[j].nprice || '',
					nnumber = listB[j].nnumber || '',
					namount = listB[j].namount || '',
					measure = listB[j].measure || '',
					taxRate = listB[j].taxRate || '',
					taxAmount = listB[j].taxAmount || '';
					if(type == "1"){
						buyCorp = listB[j].saleCorp || '';
					}else{
						buyCorp = listB[j].buyCorp || '';
					}
					//科目
			  	if(mappingSubCode == '' && mappingSubName == ''){
			  		subjectCode = '<div class="subCodeDiv chgSub" data-codeid="'+listB[j].mappingSubCode+'" data-names="'+listB[j].mappingSubName+'" style="border: 1px red dashed;">'+mappingSubCode+"-"+mappingSubName+'</div>'
			  	+'<input type="text" id="keyword"  onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/><span class="compile">编辑</span></td>';
			  	}else{
			  		subjectCode = '<div class="subCodeDiv chgSub" data-codeid="'+listB[j].mappingSubCode+'" data-names="'+listB[j].mappingSubName+'" >'+mappingSubCode+"-"+mappingSubName+'</div>'
			  	+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/><span class="compile">编辑</span></td>';
			  	}
			  	strBody += '<tr class="codeTr" style="height: 40px;" id="'+listB[j].invoiceBID+'">'
					+'<td>'+buyCorp+'</td>'
					+'<td><div class="codeComName">'+comName+'</div>'
					+'<input type="text" id="keycodeComName" class="keycodeComName"/><span class="compile">编辑</span></td>'
					+'<td><div class="codeSpec">'+spec+'</div>'
					+'<input type="text" id="keycodeSpec" class="keycodeSpec"/><span class="compile">编辑</span></td>'
					+'<td>'+subjectCode+'</td>'
					+'<td class="codePrice">'+nprice+'</td>'
					+'<td><div class="codeNnumber">'+nnumber+'</div>'
					+'<input onkeyup="codeNnumber(this)" type="text" id="keycodeNnumber" class="keycodeNnumber"/><span class="compile">编辑</span></td>'
					+'<td>'+namount+'</td>'
					+'<td>'+measure+'</td>'
					+'<td>'+taxRate+'</td>'
					+'<td>'+taxAmount+'</td>'
					+'<td class="delDate">×</td>'
					+'</tr>'
				}
				str += '<tr class="tr">'
				+'<td colspan="11" style="border-bottom:none; border-top:none">'
				+'<div style="border-bottom: 1px solid #e4e4e4; width: 100%; background: #fff; height: 8px;padding-left: 2px; margin-left: -1px;"></div>'
				+'<div class="fapiao">发票编码&nbsp;'+msg[i].invoiceCode+'&nbsp;&nbsp;&nbsp;&nbsp;发票号码&nbsp;'+msg[i].invoiceNumber+'</div>'
				+'<table style="width: 100%;" class="layui-table"><colgroup>'
				+'<col width="15%"><col width="9%"><col width="10%"><col width="22%"><col width="7%"><col width="7%">'
				+'<col width="7%"><col width="4%"><col width="6%"><col width="7%"><col width="5%"></colgroup>'
				+'<tbody id="'+msg[i].invoiceHID+'">'+strBody+'</tbody></table>'
				+'</td>'
				+'</tr>'
			}
			$("#subjectsBody").html(str);
			$(".subCodeDiv").height($(".subCodeDiv").parent("td").height());
			if($("#subjectsBody").height() > $("#subjectsJX #outputList").height()){
				if(window.screen.width < 1680){
	    		$(".outputListH").css({"width":"calc(100% - 22px)"});
	    	}else{
	    		$(".outputListH").css({"width":"calc(100% - 17px)"});
	    	}
			}
		}

		//修改导入成功单条数据
		function alterSubject(crop,type,msg,head){
			$(".companyName").text(crop+"公司名称");
			var str = "",
			listH =  1;
			for(var i=0; i<listH; i++){
				var strBody = "";
				for(var j=0; j<1; j++){
					var spec = msg.spec || '',
					subjectCode = '',
					buyCorp = head.buyCorp || '',
					comName = msg.comName || '',
					mappingSubCode = msg.sub_code || '',
					mappingSubName = msg.sub_full_name || '',
					nprice = msg.nprice || '',
					nnumber = msg.nnumber || '',
					namount = msg.namount || '',
					measure = msg.measure || '',
					taxRate = msg.taxRate || '',
					taxAmount = msg.taxAmount || '';
					if(spec == "null"){
						spec = "";
					}
					if(type == "1"){
						buyCorp = head.saleCorp || '';
					}else{
						buyCorp = head.buyCorp || '';
					}
					//科目
			  	if(mappingSubCode == '' && mappingSubName == ''){
			  		subjectCode = '<div class="subCodeDiv chgSub" data-codeid="'+mappingSubCode+'" data-names="'+mappingSubName+'" style="border: 1px red dashed;">'+mappingSubCode+"-"+mappingSubName+'</div>'
			  	+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/><span class="compile">编辑</span></td>';
			  	}else{
			  		subjectCode = '<div class="subCodeDiv chgSub" data-codeid="'+mappingSubCode+'" data-names="'+mappingSubName+'" >'+mappingSubCode+"-"+mappingSubName+'</div>'
			  	+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/><span class="compile">编辑</span></td>';
			  	}
			  	strBody += '<tr class="codeTr" style="height: 40px;" id="'+msg.invoiceBID+'">'
					+'<td>'+buyCorp+'</td>'
					+'<td><div class="codeComName">'+comName+'</div>'
					+'<input type="text" id="keycodeComName" class="keycodeComName"/><span class="compile"></span></td>'
					+'<td><div class="codeSpec">'+spec+'</div>'
					+'<input type="text" id="keycodeSpec" class="keycodeSpec"/><span class="compile"></span></td>'
					+'<td>'+subjectCode+'</td>'
					+'<td class="codePrice">'+nprice+'</td>'
					+'<td><div class="codeNnumber">'+nnumber+'</div>'
					+'<input onkeyup="codeNnumber(this)" type="text" id="keycodeNnumber" class="keycodeNnumber"/><span class="compile"></span></td>'
					+'<td>'+namount+'</td>'
					+'<td>'+measure+'</td>'
					+'<td>'+taxRate+'</td>'
					+'<td>'+taxAmount+'</td>'
					+'<td class="delDate">×</td>'
					+'</tr>'
				}
				str += '<tr class="tr">'
				+'<td colspan="11" style="border-bottom:none; border-top:none">'
				+'<div style="border-bottom: 1px solid #e4e4e4; width: 100%; background: #fff; height: 8px;padding-left: 2px; margin-left: -1px;"></div>'
				+'<div class="fapiao">发票编码&nbsp;'+head.invoiceCode+'&nbsp;&nbsp;&nbsp;&nbsp;发票号码&nbsp;'+head.invoiceNumber+'</div>'
				+'<table style="width: 100%;" class="layui-table"><colgroup>'
				+'<col width="15%"><col width="9%"><col width="10%"><col width="22%"><col width="7%"><col width="7%">'
				+'<col width="7%"><col width="4%"><col width="6%"><col width="7%"><col width="5%"></colgroup>'
				+'<tbody id="'+msg.invoiceHID+'">'+strBody+'</tbody></table>'
				+'</td>'
				+'</tr>'
			}
			$("#subjectsBody").html(str);
			$(".subCodeDiv").height($(".subCodeDiv").parent("td").height());
			$(".outputListH").css({"width":"calc(100%)"});
		}

		//商品名称编辑
		$("#subjectsBody").delegate('.codeComName', 'click', function(){
			var tbodyListH = $("#subjectsBody .tr");
    	if(identification == "0"){
				$(this).siblings('.keycodeComName').val($(this).text());
				$(this).siblings('.keycodeComName').show();
				$(this).siblings('.keycodeComName').focus();
			}
		})
		//商品规格编辑
		$("#subjectsBody").delegate('.codeSpec','click', function(){
			var tbodyListH = $("#subjectsBody .tr");
    	if(identification == "0"){
				$(this).siblings('.keycodeSpec').val($(this).text());
				$(this).siblings('.keycodeSpec').show();
				$(this).siblings('.keycodeSpec').focus();
			}
		})
		//科目编辑
		$("#subjectsBody").delegate('.compile', 'click', function (event){
			event.stopPropagation();
			$(this).siblings('.subCodeDiv').click();
		})

		//数量编辑
		$("#subjectsBody").delegate('.codeNnumber','click', function(){
			var tbodyListH = $("#subjectsBody .tr .codeTr");
    	if(identification == "0"){
				var codeId = $(this).parent('td').siblings('td').children('.subCodeDiv').attr('data-codeid').substring(4,0);
				if(codeId == "1403" || codeId == "1405"){
					$(this).siblings('.keycodeNnumber').val($(this).text());
					$(this).siblings('.keycodeNnumber').show();
					$(this).siblings('.keycodeNnumber').focus();
				}
			}
		})

		//商品名称编辑失去焦点
		$(document).on('blur','#keycodeComName',function(){
			$(this).hide();
			$(this).siblings(".codeComName").text($(this).val());
		});
		//商品规格编辑失去焦点
		$(document).on('blur','#keycodeSpec',function(){
			$(this).hide();
			$(this).siblings(".codeSpec").text($(this).val());
		});
		//数量编辑失去焦点
		$(document).on('blur','#keycodeNnumber',function(){
			var jinE = $(this).parent('td').next('td').text();	//获取金额
			if($(this).val() == ''){
				layer.msg("请输入数量");
				$(this).hide();
			}else if($(this).val() == "0"){
				layer.msg("数量不能为0");
				$(this).hide();
			}else{
				$(this).siblings(".codeNnumber").css({"border":"none"});
				var jisuan = parseFloat(jinE) / parseFloat($(this).val());
				$(this).parent('td').prev('td').text(jisuan);
				$(this).hide();
				$(this).siblings(".codeNnumber").text($(this).val());
			}
		});

		//弹出框数据单个删除
		$("#subjectsBody").delegate('.delDate', 'click', function (){
			var invoicebId = $(this).parent('tr').attr('id'),
			invoicehId = $(this).parent('tr').parent('tbody').attr('id'),
			type = tabCurr;
			var params = {
        invoiceBIDs: invoicebId +'-'+ invoicehId,
        invoiceType: type
      };
			postRequest(window.baseURL + '/invoice/deleteInvoice', params, function(res) {
				if(res.code == "0"){
					layer.msg('删除成功');
					mapType();
				}else{
					layer.msg(res.msg || "删除失败");
				}
			})
		});

		//单个修改
		$("#content").delegate('.modifyTheBut', 'click', function (){
			identification = 1;		//改变表示
			var index = $(this).parent('p').index();
			var invoiceBID = $(this).parents('.opt').prev('.invoiceBID').children('p').get(index).innerText;
			params = {
				invoiceBID: invoiceBID
			};
			postRequest(window.baseURL + '/invoice/invoiceEditOne', params, function(res){
				if(res.code == "0"){
	  			invoiceMapping();
	  			if(tabCurr == "2"){
	  				var invoice_crop = "购方";
	  			}else{
	  				var invoice_crop = "销方";
	  			}
	  			alterSubject(invoice_crop,res.msg.head.invoiceType,res.msg.body,res.msg.head);
	  		}
			})
		})
		//发票映射弹出框
    function invoiceMapping(){
    	postRequest(window.baseURL + '/invoice/queryInvoiceMapping', {}, function(res) {
	  		if(res.code == "0"){
	  			codeType = res.invoice_type;
	  		}
	  	})
    	layer.open({
        type: 1
        , title: (tabCurr == "1" ? "进":"销")+'项发票科目映射'
        , area: ['90%', '640px']
        , anim: 2
        ,offset:'10px'
        , content: $("#subjectsJX")
        , resize: false
        , move: false
        , btn: ['确定', '取消']
        , btnAlign: 'c'
        , yes: function (index) {
        	var tbodyListH = $("#subjectsBody .tr");
        	var incomeList = $("#income-list tr");
        	if(identification != "0"){
        		var invoiceBID = $("#subjectsBody .tr tbody tr").attr('id'),
			    	subCode = $("#subjectsBody .tr tbody .subCodeDiv").attr('data-codeid'),
			    	subName = $("#subjectsBody .tr tbody .subCodeDiv").attr('data-names'),
			    	params = {
							invoiceBID: invoiceBID,
							subCode: subCode,
							subName: subName
						};
						postRequest(window.baseURL + '/invoice/invoiceEditTwo', params, function(res){
							if(res.code == "0"){
								layer.msg("保存成功！");
								multiSearch(tabCurr);
							}else{
								layer.msg(res.msg || "保存失败！");
							}
						})
        	}else{
        		var str = "",
							tbodyListH = $("#subjectsBody .tr");
							for(var i=0; i<tbodyListH.length; i++){
								var tbodyListB = $(tbodyListH[i]).find('.codeTr');
								for(var j=0; j<tbodyListB.length; j++){
									var id = $(tbodyListB[j]).attr('id'),			//发票体ID
									codeComName = $(tbodyListB[j]).children('td').children('.codeComName').text() || "null",	//商品名称
									codeSpec = $(tbodyListB[j]).children('td').children('.codeSpec').text() || "null",		//规格
									dataCodeid = $(tbodyListB[j]).children('td').children('.subCodeDiv').attr('data-codeid'),	//科目编码
									dataNames = $(tbodyListB[j]).children('td').children('.subCodeDiv').attr('data-names'),		//科目全称
									codePrice = $(tbodyListB[j]).children('.codePrice').text() || "null",		//价格
									codeNnumber = $(tbodyListB[j]).children('td').children('.codeNnumber').text() || "null"; //数量
									str += id +'&&'+ codeComName +'&&'+ codeSpec +'&&'+ dataCodeid +'&&'+ dataNames +'&&'+ codeNnumber +'&&'+ codePrice +'###';
									//验证科目选择是否完善
									if(dataCodeid == "listB[j].mappingSubCode" || dataNames == "undefined"){
										layer.msg('请完善科目选择！');
										return false;
									}
									//验证科目编码为1403或1405开头的是否填写数量
									if(dataCodeid.substring(4,0) == "1403" || dataCodeid.substring(4,0) == "1405"){
										if(codeNnumber == "" || codeNnumber == "0" || codeNnumber == "null"){
											$("#subjectsBody .tr:eq('"+i+"') .codeTr:eq('"+j+"') .codeNnumber").css({"border":"1px dashed red"});
											var i = i+1,
											j = j+1;
											layer.msg('第'+i+'条'+'第'+j+'行'+'数量不能为空或等于0');
											return false;
										}
									}
								}
							}
							var params = {
								invoiceType: codeType,
								contentDate: str
							};
							postRequest(window.baseURL + '/invoice/saveInvoiceMapping', params, function(res){
								if(res.code == "0"){
									layer.msg("保存成功！");
									multiSearch(tabCurr);
								}else{
									layer.msg(res.msg || "保存失败！");
								}
							})
        	}
        	for(var i=0; i<tbodyListH.length; i++){
						var tbodyListB = $(tbodyListH[i]).find('.codeTr');
						for(var j=0; j<tbodyListB.length; j++){
							var dataCodeid = $(tbodyListB[j]).children('td').children('.subCodeDiv').attr('data-codeid'),	//科目编码
							dataNames = $(tbodyListB[j]).children('td').children('.subCodeDiv').attr('data-names');		//科目全称
							if(dataCodeid == "listB[j].mappingSubCode" || dataNames == "undefined"){
								return false;
							}
						}
					}
	  	    $("#content").css({"overflow-y":"auto"});
	  	    $("#subjectsJX, .zhezhao").hide();
	  	    layer.close(index);
        }
        , btn2: function (idx) {
	        $("#subjectsJX, .zhezhao").hide();
	  	    $("#content").css({"overflow-y":"auto"});
        }
        , cancel: function (idx) {
	        $("#subjectsJX, .zhezhao").hide();
	  	    $("#content").css({"overflow-y":"auto"});
        }
      });
      $(".layui-layer-shade").hide();
      $(".zhezhao").show();
    }

    // 页面初始化
    function pageInit() {
      // 加载进项发票首页
      $keyWord.val('');
      $beginTime.val('');
      $endTime.val('');
      multiSearch(tabCurr);
    }

    /**
     * 查询发票
     */
    // 不定项多条件查询发票(分页查询)
    function multiSearch(type, page) {
      var params = {
        invoiceType: type || 1,
        curPage: page || 1,
        keyWords: $keyWord.val().trim(),
        beginTime: $beginTime.val().trim(),
        endTime: $endTime.val().trim()
      };

      if (params.invoiceType == 1) {
        comCurr = params.curPage;
        (params.keyWords || params.beginTime) ? $inList.attr('has-search', 1) : $inList.attr('has-search', 0);
      } else {
        outCurr = params.curPage;
        (params.keyWords || params.beginTime) ? $outList.attr('has-search', 1) : $outList.attr('has-search', 0);
      }
      postRequest(window.baseURL + '/invoice/queryInvoice', params, function(res) {
        var str;
        if (res.message === 'success') {
          var list = res.list || [],
            total = res.totalCount,
            limit = res.maxPage;

          if (params.invoiceType == 1) {
            _listHTML(list, 'income-list');
            flushPage2(res, 'layui-table-page1', multiSearch);
            if (show === 'block') {
					    if(voucherType != 1){
						  	 $('span.remove').show();
						  }
					  }else{
					    $('span.remove').hide();
					  }
          } else {
            _listHTML2(list, 'output-list');
            flushPage2(res, 'layui-table-page2', multiSearch);
            if (show === 'block') {
            	if(voucherType != 1){
						  	 $('span.remove').show();
						  }
					  }else{
					    $('span.remove').hide();
					  }
          }
        } else {
          layer.msg('查询发票信息异常');
        }
      });

      // 进项
      function _listHTML(list, ele) {
        var str = '',
          len = list.length,
          invoiceHead = '',
          bodyList = '',
          arr,
          oSum,
          $listTable = $('#' + ele);

        state.income = len;
        if (len) {
        	//取消单个删除操作 把类remove去掉
        	var permission = isCarryState === 1 ? '' : '<span class="layui-icon cu red remove">&#x1006;</span><span class="layui-icon cu red modifyTheBut">&#xe642;</span>';
          $.each(list, function (idx, data) {
            invoiceHead = data.invoiceHead;
            bodyList = data.bodyList;
            oSum = {
              0: bodyList[0].namount,
              1: bodyList[0].taxAmount
            };
            if(bodyList[0].sub_code == null && bodyList[0].sub_full_name == null){
            	var code = ""
            }else{
            	var code = bodyList[0].sub_code +'-'+ bodyList[0].sub_full_name;
            }
            if(invoiceHead.saleTaxno == null){
            	invoiceHead.saleTaxno = "";
            }
            arr = initNumber(oSum);
            str += '<tr id="' + invoiceHead.invoiceHID + '">'
              + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
              + '<td>' + invoiceHead.invoiceCode + '</td>'
              + '<td>' + invoiceHead.invoiceNumber + '</td>'
              + '<td>' + formatDate(invoiceHead.invoiceDate, 'yyyy-MM-dd') + '</td>'
              + '<td>' + code + '</td>'
              + '<td>' + invoiceHead.saleTaxno + '</td>'
              + '<td>' + invoiceHead.saleCorp + '</td>'
              + '<td>' + arr[0] + '</td>'
              + '<td>' + arr[1] + '</td>'
              + '<td>' + (invoiceHead.invoice_confirmdate ? formatDate(invoiceHead.invoice_confirmdate, 'yyyy-MM-dd') : '') + '</td>'
              + '<td>' + invoiceHead.invoiceName + '</td>'
              + '<td class="invoiceBID" style="display:none;"><p>' + bodyList[0].invoiceBID + '</p></td>'
              + '<td class="opt">' + permission + '</td>'
              + '</tr>';
          });
        } else {
          str = '<tr class="tc"><td colspan="12" style="height: 80px;">暂无数据</td></tr>'
        }
        $listTable.html(str);
        if(voucherType == 1){
			  	$('#popup-btn, #batch-del, .opt .remove, .opt .modifyTheBut').hide();
			  }
        refreshCheckbox();
      }

      // 销项
      function _listHTML2(list, ele) {
        var str = '',
        	strCode = '',
          rows = 1,
          len = list.length,
          invoiceHead = '',
          bodyList = '',
          $listTable = $('#' + ele);

        state.output = len;
        if (len) {
          $.each(list, function (idx, data) {
          	var bList = '';
          	var permission ="";
            invoiceHead = data.invoiceHead;
            bodyList = data.bodyList;
            rows = bodyList.length;
            for(var i=0; i<rows; i++){
            	permission += isCarryState === 1 ? '' : '<p><span class="layui-icon cu red remove">&#x1006;</span><span class="layui-icon cu red modifyTheBut">&#xe642;</span></p>';
            	if(bodyList[i].sub_code == null){
            		bList += '<p></p>';
            	}else{
            		bList += '<p>'+bodyList[i].sub_code+'-'+bodyList[i].sub_full_name+'</p>';
            	}
            }
            str += '<tr id="'+invoiceHead.invoiceHID+'">'
              + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
              + '<td>' + invoiceHead.invoiceCode + '</td>'
              + '<td>' + invoiceHead.invoiceNumber + '</td>'
              + '<td>' + invoiceHead.buyCorp + '</td>'
              + '<td>' + bList + '</td>'
              + '<td>' + pList('comName') + '</td>'
              + '<td>' + pList('spec') + '</td>'
              + '<td>' + pList('measure') + '</td>'
              + '<td>' + pList('nnumber') + '</td>'
              + '<td>' + pList('nprice', true) + '</td>'
              + '<td>' + pList('namount', true) + '</td>'
              + '<td>' + pList('taxRate') + '</td>'
              + '<td>' + pList('taxAmount', true) + '</td>'
              + '<td class="invoiceBID" style="display:none;">' + pList('invoiceBID') + '</td>'
              + '<td class="opt">' + permission + '</td>'
              + '</tr>';
          });
        } else {
          str = '<tr class="tc"><td colspan="15" style="height: 80px;">暂无数据</td></tr>'
        }
        $listTable.html(str);
        if (tabCurr == 1) {
          fixedHead($('#head1'), $('#income-table'), $('.fixed-search'), $('#navDOM1'));
        }
        if(voucherType == 1){
			  	$('#popup-btn, #batch-del, .opt .remove, .opt .modifyTheBut').hide();
			  }
        refreshCheckbox();

        function pList(value, format) {
          var str = '',
            str2 = '';
          for (var i = 0; i < rows; i++) {
          	str2 = bodyList[i][value];
          	if (str2 == '' || str2 == null) {
              str2 = '';
            } else if (format) {
              str2 = str2.toFixed(2);
              str2 = formatNum(str2);
            }
            str += '<p>' + str2 + '</p>';
          }
          return str;
        }
      }

      // 刷新分页
      function flushPage2(data, ele, fn) {
        var currPage = (tabCurr == 1) ? comCurr : outCurr;
        layui.laypage.render({
          elem: ele
          , count: data.totalCount //数据总数，从服务端得到
          , limit: data.maxPage
          , curr: currPage
          , layout: ['prev', 'page', 'next', 'skip', 'count']
          , jump: function (obj, first) {
            //首次不执行
            if (!first) {
              fn(tabCurr, obj.curr);
            } else if (currPage > obj.curr && obj.curr > 1) {
              fn(obj.curr);
            }
          }
        });
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
     * 导入发票
     */
    // 打开导入发票弹窗
    function showUploadPopup() {
      $uploadInput.val('').prev().text('请选择97格式.xls...');
      layer.open({
        type: 1
        , title: '导入'+(tabCurr == "1" ? "进":"销")+'项发票97格式Excel'
        , area: ['450px', '255px']
        , content: $uploadPopup
        , cancel: function () {
          $uploadPopup.hide();
          layer.closeAll('tips');
        }
      });
      // layer.tips('请下载统一的模版，认证月份须和会计期间一致。', '#upload-popup a', { time: 3000, tips: [3, '#1eadf3'] });
    }

    /**
     * 删除发票
     */
    function _deleteData(id) {
      var params = {
          invoiceBIDs: id || '',
          invoiceType: tabCurr
        },
        arr = params.invoiceBIDs.split(',');
      postRequest(window.baseURL + '/invoice/deleteInvoice', params, function(res) {
        if (res.code == '0') {
          layer.msg('删除发票成功', {
            icon: 1,
            shade: 0.1,
            time: 600
          }, function (idx) {
            var currTable = (tabCurr == 1) ? $inList : $outList,
              $tableBox = currTable.parents('.table-box');
            $.each(arr, function (idx, val) {
              $('#' + val).remove();
            });
            $tableBox.find('input[name="allChoose"]').prop('checked', false);
            refreshCheckbox();
            if (!currTable.find('tr').length && $tableBox.find('.layui-laypage-next.layui-disabled').length) {
              var $skipInput = $tableBox.find('.layui-laypage-skip input'),
                page = $skipInput.val() - 1;
              page && $skipInput.val(page);
            }
            $tableBox.find('button.layui-laypage-btn').click();
          });
        } else {
          layer.msg('删除发票信息异常');
        }
      });
    }

    // 批量选择发票
    function batchSelect(obj) {
      var arr = [],
        checkbox = $(obj).find(':checked');
      $.each(checkbox, function () {
        var id = $(this).parents('tr').attr('id');
        var dd = $(this).parents('tr').children('.invoiceBID').children('p').length;
        for(var i=0; i<dd; i++){
        	if (id) {
					  arr.push($(this).parents('tr').children('.invoiceBID').children('p')[i].innerText+'-'+id);
					}
	      }
      });
      return arr.toString();
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
          voucherType = data.isCreateVoucher;
          arr = [data.isCreateVoucher, data.isCarryState, data.isCheck, data.isJz];
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
    return arr;
  }
}());


$(document).ready(function() {
	$(document).keydown(function(e) {
		e = e || window.event;
		var keycode = e.which ? e.which : e.keyCode;
		if (keycode == 38) {
			if (jQuery.trim($("#subLists").html()) == "") {
				return;
			}
			movePrev();
		} else if (keycode == 40) {
			if (jQuery.trim($("#subLists").html()) == "") {
				return;
			}
			$("#keyword").blur();
			if ($(".ellipsis").hasClass("on")) {
				moveNext();
			} else {
				$(".ellipsis").removeClass('on').eq(0).addClass('on');
			}
		} else if (keycode == 13) {
			dojob();
		}
	});
	var movePrev = function() {
		$("#keyword").blur();
		var index = $(".on").prevAll().length;
		if (index == 0) {
			$(".ellipsis").removeClass('on').eq($(".ellipsis").length - 1).addClass('on');
		} else {
			$(".ellipsis").removeClass('on').eq(index - 1).addClass('on');
		}
	}
	var moveNext = function() {
		var index = $(".on").prevAll().length;
		if (index == $(".ellipsis").length - 1) {
			$(".ellipsis").removeClass('on').eq(0).addClass('on');
		} else {
			$(".ellipsis").removeClass('on').eq(index + 1).addClass('on');
		}
	}
	var dojob = function() {
		$("#keyword").blur();
		var value = $(".on").text(),
		codeId = $(".on").attr('data-codeid'),
		name =  $(".on").attr('data-name'),
		codeName = $(".on").attr('data-names');
    var $div=$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .subCodeDiv");
    $div.text(value)
        .attr({
          'data-codeid': codeId,//科目编码
          'data-names': codeName//科目全称
        })
        .next('input').val(value);
		$(".subList").hide();
		$('.subCodeInput').hide();
		//循环遍历自动填充
		$('.subCodeDiv').css({"background":"none"});
	  var $subjectsBody =$("#subjectsBody .tr"),
	  $codeSpec = $("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeSpec").text(),
	  $codeComName = $("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeComName").text(),
	  $indexL = 0;
	  if(value.substring(0,4) != "1403" && value.substring(0,4) != "1405"){
	  	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codeNnumber").text('');
	  	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .codePrice").text('');
	  	$("#subjectsBody .tr:eq("+$indexS+") tr:eq("+$index+") .keycodeNnumber").val('');
	  } 	
	  for(var i=0; i<$subjectsBody.length; i++){
	  	var $codeTr = $subjectsBody.find('tbody').children('.codeTr');
	  	for(var j=0; j<$codeTr.length; j++){
	  		if($codeComName != ""){
	  			if ($($codeTr[j]).children('td').children('.subCodeDiv').text() == "-") {
      			if($codeComName == $($codeTr[j]).children('td').children('.codeComName').text() && $codeSpec == $($codeTr[j]).children('td').children('.codeSpec').text()){
	      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-codeid',codeId);
	      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-name',name);
	      			$($codeTr[j]).children('td').children('.subCodeDiv').attr('data-names',codeName);
	      			$($codeTr[j]).children('td').children('.subCodeDiv').text(value);
	      			$($codeTr[j]).children('td').children('.subCodeDiv').css({"background":"#c3ddff"});
	      			$indexL = $indexL+1;
	      		}
	  			}
	  		}
	  	}
	  }
	  if($indexL > 0){
	  	layer.msg('已自动帮您匹配    <span style="color:red;">'+$indexL+'</span>  项');
	  }
	}
});
//模糊匹配科目列表
function getContent(obj) {
	var that = this,
        list = window.subListData,
        leng = list.length,
        val = jQuery.trim($(obj).val()) || '';

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
            str += '<li  id="'+list[i].pkSubId+'" data-codeId="'+list[i].subCode+'" data-name="'+list[i].subName+'" data-nameS="'+list[i].fullName+'"  class="sub-title ellipsis' + (isFirst === 1 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + '-' + list[i].fullName + '</li>';
          }
        }
        //将匹配到的数据添加到HTML中
        if (str == "") {
          str = '<li class="sub-title ellipsis" style="text-align:center">暂无数据</li>';
        }
        $("#subLists").html(str);
      } else {
        $("#subLists").html('<li class="sub-title ellipsis" style="text-align:center">暂无数据</li>');
      }
}

function codeNnumber(obj){
	obj.value = obj.value.replace(/[^\d.-]/g,""); //先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/^\./g,""); 	 //必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/\.{2,}/g,"."); //保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\-{2,}/g,"-"); //保证只有出现一个负数，而没有多个.
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); //保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-"); //保证负数只出现一次，而不能出现两次以上
}
