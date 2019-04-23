/**
** 银行对账单导入
**/
var	treeText = [],
voucherType = 0,
	str1 = '<tr>'
	  	+'<td class="subjectTable">'
	  	+'<div class="subCodeDiv"></div>'
	  	+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/></td>'
	  	+'<td class="moneyTable"><input type="text" class="subCodeInput" /></td>'
	  	+'<td class="caozuo"><span class="addSpan">+</span><span class="delSpan">×</span></td>'
	  	+'</tr>',
	accountBalancing = 0,	//科目余额
	$index = 0,				//科目下标
	$indexS = 0;			//多选科目下标
;(function () {
  // 显示权限按钮
  var show = getPurviewData(200),
    isCarryState = _queryStatus()[1];
  if (show === 'block') {
    $('.search').css('display', show);
    $('#content').css('padding-top', '40px');
  }
  var IdHeight = $("#content").height();
  $("#dataList").css({"max-height":IdHeight-105});
//var Iwidth = $("#content").width();
//$("#Dywe").width(Iwidth);
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
  	$('#save-btn,.subjects,#popup-btn,#batch-del,.remove').hide();
  	layer.alert('您已经生成凭证，不能修改数据。', {
      icon: 0
    });
  }

  $(function () {
    var layer = layui.layer,
      isNull = false,
      currPage = 1,
      $selectBank = $('#select-bank'),
      $keyWord = $('#keyWord'),
      $beginTime = $('#beginTime'),
      $endTime = $('#endTime'),
      $uploadPopup = $('#upload-popup'),
      $uploadInput = $('#upload-input'),
      $uploadBtn = $('#upload-btn'),
      $downPopup = $('#down-popup'),
      $downBtn = $('#down-btn'),
      $table = $('table'),
      $tableList = $('#data-list'),
      $searchBtn = $('#search-btn'),
      $popupBtn = $('#popup-btn'),
      $batchBtn = $('#batch-del'),
      $saveBut = $('#save-btn'),
      $toDown = $('#to-down');

    // getBankList();
    pageInit();

	//加载科目列表
	_getSubListData();

    // laydate选择日期
    lay('.wDate').each(function () {
      layui.laydate.render({
        elem: this
        , max: 0
        , theme: '#1E9FFF'
      });
    });

    // 点击搜索
    $searchBtn.on('click', function () {
      // 条件判断
      var bank = $selectBank.val(),
        word = $keyWord.val(),
        startTime = $beginTime.val(),
        endTime = $endTime.val();
      if (!bank && !word && !startTime && !endTime) {
        layer.msg('请输入任意查询条件');
        return false;
      }
      if (startTime || endTime) {
        if (startTime > endTime) {
          layer.msg('开始时间不能大于结束时间');
          return false;
        }
      }
      multiSearch(1);
    });

	//保存
	//$saveBut.on('click', showUploadsave);

    // 打开导入Excel弹窗
    $popupBtn.on('click', showUploadPopup);

    $uploadInput.on('change', function () {
      var v = this.files[0] ? this.files[0].name : '请选择97格式.xls...';
      $(this).prev().text(v);
      $uploadBtn.removeClass('loading loaded');
    });

    // 上传Excel
    $uploadBtn.on('click', function () {
      var that = this;
      uploadInit(that, window.baseURL + '/bank/uploadFile', function(res) {
        if (res.success == 'true') {
          $uploadBtn.addClass('loaded');
          layer.msg('导入成功', {
            icon: 1,
            shade: 0.1,
            time: 1000
          }, function () {
            layer.closeAll('page');
            $uploadPopup.hide();
            multiSearch();
            //pageInit();	//初始化
//          layer.msg('银行导入成功，跳入工资导入', {
//	   			  icon: 1,
//	   			  time: 1000 //1秒关闭（如果不配置，默认是3秒）
//	   		});
//	   		window.parent.document.getElementById("YJlipiao3").click();
          });
        } else {
          layer.msg(res.message || '导入失败', {
            icon: 2,
            shade: 0.1,
            shadeClose: true,
            time: 2000000
          });
        }
      });
    });

    // 打开下载弹窗
    $toDown.on('click', openDown);

    $downPopup.on('click', '.layui-form-checkbox', function () {
      var attrName = $(this).prev().attr('name'),
        $parent = $downPopup,
        $selectAll = $parent.find('input[name="allChoose"]');
      if (attrName == 'ckbox') { // 单选
        if ($parent.find('input[name="ckbox"]:checked').length == $parent.find('input[name="ckbox"]').length) {
          $selectAll.prop('checked', true);
        } else {
          $selectAll.prop('checked', false);
        }
      } else if (attrName == 'allChoose') { // 全选
        if ($(this).hasClass('layui-form-checked')) {
          $parent.find('input[name="ckbox"]').prop('checked', true);
        } else {
          $parent.find('input[name="ckbox"]').prop('checked', false);
        }
      }
      refreshCheckbox();
    })

    // 点击复选框
    $table.on('click', '.layui-form-checkbox', function () {
      checkBoxState($(this));
    })
      // 单条数据删除
      .on('click', 'span.remove', function () {
        var id = $(this).parents('tr').attr('id'),
          params = {
            billIDs: id
          };

        if (!id) {
          layer.msg('本条数据ID不存在');
          return false;
        }
        _deleteData(params);
      });

    // 批量数据删除
    $batchBtn.on('click', function () {
      var id = batchSelect($table),
        params = {
          billIDs: id
        };

      if (!id) {
        layer.msg('请选择要删除的银行对账单');
        return false;
      }
      _deleteData(params);
    });

    // 获取焦点， 输入框内容为空
    removeInputVal($('#endTime'));
    removeInputVal($('#beginTime'));
    removeInputVal($('#keyWord'));

	//悬浮科目
//	$(document).on('mouseover','.subCodeDiv',function(){
//		var heightR = $(this).parent('td').height();
//		$(this).css({"border":"1px dashed red","height":heightR-1,"width":"299px"});
//	});
//	$(document).on('mouseout','.subCodeDiv',function(){
//		var heightR = $(this).parent('td').height();
//		if($(this).text() != ""){
//			$(this).css({"border":"none","height":heightR+1,"width":"300px"});
//		}
//	});

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

    // 页面初始化
    function pageInit() {
      // 加载进项发票首页
      $selectBank.val(0);
      $keyWord.val('');
      $beginTime.val('');
      $endTime.val('');
      layui.form.render('select');
      multiSearch();	
    }

    // 获取银行清单列表
    function getBankList() {
      var url = window.baseURL + '/bank/queryBankList';
      $.ajax({
        url: url,
        type: 'GET',
        success: function (res) {
          if (res.success == 'true') {
            getBankListCB(res.list);
          } else {
            layer.msg(res.message);
          }
        },
        error: function () {
          console.log(res);
          layer.msg('查询银行下拉列表异常!');
        }
      });

      // 获取银行清单列表回调
      function getBankListCB(list) {
        var str = '',
          str2 = '';

        $.each(list, function (idx, el) {
          str += '<option value="' + idx + '">' + el + '</option>';
          if (idx) {
            str2 += '<li data-id="' + idx + '">'
              + '<input type="radio" name="bank-select" title="' + el + '" value="' + idx + '">'
              + '</li>';
          }
        });
        $selectBank.html(str);
        $downPopup.find('ul').html(str2);
        layui.form.render();
      }
    }
	
//	导入银行文件
	function bankPopUpBox(){
		layer.open({
		    type: 1
		    , title: '银行导入'
		    , area: ['1580px', '610px']
		    , anim: 2
		    ,offset: '25px'
		    , content: $("#bankPopUpBox")
		    , btn: ['保存','取消']
		    , yes: function (idx) {
		      //保存
		      showUploadsave(idx);
		    }
		    , btn2: function (idx) {
		      $(".zhezhao").hide();
		      $("#bankPopUpBox").hide();
		    }
		    , cancel: function (idx) {
		      $(".zhezhao").hide();
		      $("#bankPopUpBox").hide();
		    }
		});
		$(".layui-layer-shade").hide();
        $(".zhezhao").show();
	}
    /**
     * 查询列表
     */
    // 开始时间和结束时间必须同时输入，否则不查询时间
    function multiSearch(page) {
      var params = {
        curPage: page || 1,
        bankType: $selectBank.val() || 0,
        keyWords: $keyWord.val().trim(),
        beginTime: $beginTime.val().trim(),
        endTime: $endTime.val().trim()
      };

      currPage = params.curPage;
      if (!checkSearch(params)) return;
      postRequest(window.baseURL + '/bank/queryBankBill', params, function(res) {
        var data = res;
        if (res.success == 'true') {
          var ele = 'layui-table-page1',
            list = data.list || [],
            total = data.totalCount,
            limit = data.maxPage;
          //循环判断科目是否有为NULL项，如果有就显示弹出框
          for(var i=0; i<list.length; i++){
          	if(list[i].subCode == null || list[i].subCode == ""){
          		isNull = true;
          	}
          }
          tableListHTML(list, 'data-list');
          flushPage(ele, currPage, total, limit, multiSearch);
          if (show === 'block') {
          	if(voucherType != 1){
				 $('span.remove').show();
			}
		  }else{
		    $('span.remove').hide();
		  }
        } else {
          layer.msg('银行对账单查询异常');
        }
      });

      // 检测搜索条件
      function checkSearch(params) {
        if ((params.beginTime && params.endTime == '') || (params.beginTime == '' && params.endTime)) {
          layer.msg('开始时间和结束时间只输入一个不查询交易日期，请选择时间段或者默认为空');
          return false;
        }
        return true;
      }

      // 列表innerHTML
      function tableListHTML(list, ele) {
      	//科目有为空并且未生成凭证方可执行
      	if(isNull == true && voucherType == '0'){
      		bankPopUpBox();
      	}
        var str = '',
          strS = '',
          arr,
          oSum,
          len = list.length,
          $listTable = $('#' + ele);
        if (len) {
          var permission = isCarryState === 1 ? '' : '<span class="layui-icon cu red remove">&#x1006;</span>';
          $.each(list, function (i, el) {
            oSum = {
              0: el.debitAmount,
              1: el.creditAmount,
              2: el.accountBalance
            };
            arr = initNumber(oSum);
            var subCodeifNumm = '',
            	isGjjorSB = false;
            //var isGjjorSB = '<div class="subCodeDiv isGjjorSB" data-sb="1" style="border: 1px red dashed;" id="" data-codeid="" data-name="" data-names=""></div>'
			//		+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>';
            if(el.subCode == null){
            	//公积金或社保显示多科目选择
            	if(el.isGjj == true || el.isSb == true){
            		isGjjorSB = true;
            		//如果有科目全名就显示科目全名
	            	if(el.subID != null && el.subFullName != null && el.subCode != "" && el.subFullName != ""){
						subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subFullName + '</div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+isGjjorSB
	            	//否则就显示科目名称
		            }else if(el.subID != null && el.subName != null && el.subCode != "" && el.subName != ""){
						subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subName + '</div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+isGjjorSB
		            //都没有则不显示
		            }else{
		            	subCodeifNumm = '<div class="subCodeDiv" style="border: 1px red dashed;" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'"></div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+isGjjorSB
		            }
            	}else{
            		//如果有科目全名就显示科目全名
	            	if(el.subID != null && el.subFullName != null && el.subCode != "" && el.subFullName != ""){
						subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subFullName + '</div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
	            	//否则就显示科目名称
		            }else if(el.subID != null && el.subName != null && el.subCode != "" && el.subName != ""){
						subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subName + '</div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
		            //都没有则不显示
		            }else{
		            	subCodeifNumm = '<div class="subCodeDiv" style="border: 1px red dashed;" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'"></div>'
						+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
		            }
            	}
            }else{
            	if(el.subCode.indexOf("{[|]}") != -1){
	            	//多分支
	            	arrID = el.subID.split("|");
	            	arrSubID = el.subCode.split("{[|]}");
	            	arrName = el.subName.split("|");
	            	arrQname = el.subFullName.split("|");
	            	for(var i=0; i<arrID.length; i++){
	            		var codeJq = arrSubID[i].match(/(\S*)\|/)[1];
	            		//如果有科目全名就显示科目全名
		            	if(arrID[i] != null && arrSubID[i]!= null && arrQname[i] != ""){
							subCodeifNumm += '<div class="subCodeDiv" id="'+arrID[i]+'" data-codeid="'+arrSubID[i]+'" data-name="'+arrName[i]+'" data-names="'+arrQname[i]+'">' + codeJq +'-'+ arrQname[i] + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
		            	//否则就显示科目名称
			            }else if(arrID[i] != null && arrSubID[i] != null && arrName[i] != ""){
							subCodeifNumm += '<div class="subCodeDiv" id="'+arrID[i]+'" data-codeid="'+arrSubID[i]+'" data-name="'+arrName[i]+'" data-names="'+arrName[i]+'">' + codeJq +'-'+ arrName[i] + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
			            //都没有则不显示
			            }else{
			            	subCodeifNumm += '<div class="subCodeDiv" id="'+arrID[i]+'" data-codeid="'+arrSubID[i]+'" data-name="'+arrName[i]+'" data-names="'+arrQname[i]+'"></div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
			            }
	            	}
	            }else{
	            	//一个分支
	            	if(el.isGjj == true || el.isSb == true){
	            		isGjjorSB = true;
	            		//如果有科目全名就显示科目全名
		            	if(el.subID != null && el.subFullName != null && el.subCode != "" && el.subFullName != ""){
							subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subFullName + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+isGjjorSB
		            	//否则就显示科目名称
			            }else if(el.subID != null && el.subName != null && el.subCode != "" && el.subName != ""){
							subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subName + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+//isGjjorSB
			            //都没有则不显示
			            }else{
			            	subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'"></div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'//+//isGjjorSB
			            }
	            	}else{
		            	//如果有科目全名就显示科目全名
		            	if(el.subID != null && el.subFullName != null && el.subCode != "" && el.subFullName != ""){
							subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subFullName + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
		            	//否则就显示科目名称
			            }else if(el.subID != null && el.subName != null && el.subCode != "" && el.subName != ""){
							subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'">' + el.subCode +'-'+ el.subName + '</div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
			            //都没有则不显示
			            }else{
			            	subCodeifNumm = '<div class="subCodeDiv" id="'+el.subID+'" data-codeid="'+el.subCode+'" data-name="'+el.subName+'" data-names="'+el.subFullName+'"></div>'
							+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/>'
			            }
		            }
	            }
            }
            if(isGjjorSB == true){
            	str += '<tr id="' + el.id + '" style="background: #ffd8d8;">'
              + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
              + '<td data-type="'+el.intBankType+'">' + el.bankType + '</td>'
              + '<td>' + formatDate(el.transactionDate, 'yyyy-MM-dd') + '</td>'
              + '<td>' + el.des + '</td>'
              + '<td style="padding:0;" class="subject_TD"><div class="subjects" title="添加多个科目"></div>'
              + subCodeifNumm
              +'</td>'
              + '<td class="incomeMoney">' + arr[0] + '</td>'
              + '<td class="expendMoney">' + arr[1] + '</td>'
              + '<td>' + arr[2] + '</td>'
              + '<td>' + el.dfAccountName + '</td>'
              + '<td>' + (el.bz || '') + '</td>'
              + '<td class="opt">' + permission + '</td>'
              + '</tr>';
              layer.msg('红色背景提示：账单存在公积金或者社保,请注意完善！');
            }else{
            	str += '<tr id="' + el.id + '">'
              + '<td><input type="checkbox" name="ckbox" lay-skin="primary"></td>'
              + '<td data-type="'+el.intBankType+'">' + el.bankType + '</td>'
              + '<td>' + formatDate(el.transactionDate, 'yyyy-MM-dd') + '</td>'
              + '<td>' + el.des + '</td>'
              + '<td style="padding:0;" class="subject_TD"><div class="subjects" title="添加多个科目"></div>'
              + subCodeifNumm
              +'</td>'
              + '<td class="incomeMoney">' + arr[0] + '</td>'
              + '<td class="expendMoney">' + arr[1] + '</td>'
              + '<td>' + arr[2] + '</td>'
              + '<td>' + el.dfAccountName + '</td>'
              + '<td>' + (el.bz || '') + '</td>'
              + '<td class="opt">' + permission + '</td>'
              + '</tr>';
            }
            if(isGjjorSB == true){
            	strS += '<tr id="' + el.id + '" style="background: #ffd8d8;">'
              + '<td data-type="'+el.intBankType+'">' + el.bankType + '</td>'
              + '<td>' + formatDate(el.transactionDate, 'yyyy-MM-dd') + '</td>'
              + '<td>' + el.des + '</td>'
              + '<td style="padding:0;" class="subject_TD"><div class="subjects" title="添加多个科目"></div>'
              + subCodeifNumm
              +'</td>'
              + '<td class="incomeMoney">' + arr[0] + '</td>'
              + '<td class="expendMoney">' + arr[1] + '</td>'
              + '<td>' + arr[2] + '</td>'
              + '<td>' + el.dfAccountName + '</td>'
              + '<td>' + (el.bz || '') + '</td>'
              + '</tr>';
              layer.msg('红色背景提示：账单存在公积金或者社保,请注意完善！');
            }else{
            	strS += '<tr id="' + el.id + '">'
              + '<td data-type="'+el.intBankType+'">' + el.bankType + '</td>'
              + '<td>' + formatDate(el.transactionDate, 'yyyy-MM-dd') + '</td>'
              + '<td>' + el.des + '</td>'
              + '<td style="padding:0;" class="subject_TD"><div class="subjects" title="添加多个科目"></div>'
              + subCodeifNumm
              +'</td>'
              + '<td class="incomeMoney">' + arr[0] + '</td>'
              + '<td class="expendMoney">' + arr[1] + '</td>'
              + '<td>' + arr[2] + '</td>'
              + '<td>' + el.dfAccountName + '</td>'
              + '<td>' + (el.bz || '') + '</td>'
              + '</tr>';
            }
          });
        } else {
          str = '<tr class="tc"><td colspan="11" style="height: 80px;">暂无数据</td></tr>'
          strS = '<tr class="tc"><td colspan="9" style="height: 80px;">暂无数据</td></tr>'
        }
        $listTable.html(str);
        $("#bankDataList").html(strS);
        refreshCheckbox();
        if($("#data-list").height() > $("#dataList").height()){
        	if(window.screen.width < 1680){
        		$(".table-box .fixed-head").css({"margin-right":"22px"});
        	}else{
        		$(".table-box .fixed-head").css({"margin-right":"17px"});
        	}
        }
        if(voucherType == 1){
			$('#save-btn,.subjects,#popup-btn,#batch-del,.opt .remove').hide();
		}
      }
    }
	
	//点击单条保存
	$("#data-list").delegate('.subCodeDiv', 'click', function (event){
		//未生成凭证方可执行
		if(voucherType == '0'){
			$(this).siblings('.subjects').click();
		}
	})
	//点击科目显示科目列表
	$("#bankPopUpBox").delegate('.subCodeDiv', 'click', function (event){
		var thisDiv = $(this).parent('.subject_TD').children('.subCodeDiv').length;
		if(thisDiv > 1){
			return false;
		}
		if(voucherType != 1){		//没结转才可进行
        	event.stopPropagation();
			$index = $(this).parent('td').parent('tr').index();
			var heightI = $(this).offset().top,
			thisH = $(this).siblings('.subCodeInput').height(),
			thisW = $(this).siblings('.subCodeInput').width(),
			leftI = $(this).offset().left,
			scrollH = $("#content").scrollTop();
			$('.subCodeInput').hide();								//隐藏其他input框
			$('.subList').show();									//显示科目列表
			$(this).siblings('.subCodeInput').show();				//显示inpnt框
			$(this).siblings('.subCodeInput').focus();				//获取焦点
			if(thisH > 40 && thisH < 70){
				$('.subList').css({"top":heightI+thisH-7+scrollH,"left":leftI-20,"width":thisW+2});
			}else if(thisH > 70){
				$('.subList').css({"top":heightI+thisH-12+scrollH,"left":leftI-20,"width":thisW+2});
			}else{
				$('.subList').css({"top":heightI+thisH+2+scrollH,"left":leftI-20,"width":thisW+2});
			}
       }
	});
	$("#data-list").delegate('.subCodeInput', 'click', function (event){
		event.stopPropagation();//阻止冒泡
	});
//	$("#data-list").delegate('.subCodeInput', 'blur', function (event){
//		alert(111);
//		//$("input").css("background-color","#D6D6FF");
//	});
//	$(".subCodeInput").blur(function(){
//	  alert(111);
//	});
//	$(".subCodeInput").on('blur', function(){
//		alert(111);
//	})

	//点击选中科目
	$("#subLists").delegate('.ellipsis', 'click', function (event){
		event.stopPropagation();
		$(".subList").hide();
		$('.subCodeInput').hide();
    if($("#subjectsF").css('display') == "none"){
      if($(".bankPopUpBox").css('display') === 'none'){
      	var $div = $("#data-list tr:eq("+$index+") .subCodeDiv");
      }else{
      	var $div = $("#bankDataList tr:eq("+$index+") .subCodeDiv");
      }
      $div.css({"border":"none"})
        .text($(this).text())
        .attr({
          'id': $(this).attr("id"),//科目ID
          'data-codeid': $(this).attr("data-codeid"),//科目编码
          'data-name': $(this).attr("data-name"),//末级科目名称
          'data-names': $(this).attr("data-names")//科目全称
        })
        .next('input').val($(this).text());
		}else{
        var $div = $("#subjectsBody tr:eq("+$indexS+") .subCodeDiv");
        $div.text($(this).text()).attr({
          'id': $(this).attr("id"),//科目ID
          'data-codeid': $(this).attr("data-codeid"),//科目编码
          'data-name': $(this).attr("data-name"),//末级科目名称
          'data-names': $(this).attr("data-names")//科目全称
        }).next('input').val($(this).text());
		var zong = $("#subjectsBody tr").length,	//先获取总行数
		jin_E = 0;			//定义金额变量
		//循环获取金额
		for(var i=0; i<zong; i++){
			//判断如果当前选中的等于正在执行的则不执行
			if($indexS != i){
				//判断是否填写金额，如果填写了则保存
				if($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val() != ""){
					jin_E += parseFloat($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val());
				}
			}
		}
		//如果金额等于0，则不进方法
		if(jin_E != 0){
			//去掉千位符
			var qian = accountBalancing.split(',').join("");
			//金额转换int类型并计算
			var sheng = parseFloat(qian) - jin_E;
			//计算结果赋值
			$("#subjectsBody tr:eq("+$indexS+") .moneyTable .subCodeInput").val(sheng);
			if(parseFloat(qian) == jin_E){
				$("#subjectsF .reminder").hide();
			}
		}
		}
	})

	//点击空白隐藏科目列表
	$(document).click(function(){
		$(".subList").hide();
		$('.subCodeInput').hide();
	});

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
          if ($("#subjectsF").is(':hidden')) {
            // 银行列表添加
            if($(".bankPopUpBox").css('display') === 'none'){
		      	var $div = $("#data-list tr:eq("+$index+") .subCodeDiv");
		    }else{
		      	var $div = $("#bankDataList tr:eq("+$index+") .subCodeDiv");
		    }
            $div.css({"border":"none"});
          } else {
            // 多科目添加
            var $div = $("#subjectsBody tr:eq("+$indexS+") .subCodeDiv");
          }
          var str = params.subCode + '-' + params.fullName;
          $div.text(str)
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

	//添加多个科目分路弹出框
	$("#data-list,#bankDataList").delegate('.subjects', 'click', function (){
	  $index = $(this).parent('td').parent('tr').index();
	  $("#subjectsF .reminder").hide();
	  if(parseFloat($(this).parent('td').siblings('.incomeMoney').text()) > 0 || parseFloat($(this).parent('td').siblings('.incomeMoney').text()) < 0 && $(this).parent('td').siblings('.incomeMoney').text() != "null" && $(this).parent('td').siblings('.incomeMoney').text() != ""){
	  	accountBalancing = $(this).parent('td').siblings('.incomeMoney').text();
	  }else{
	  	accountBalancing = $(this).parent('td').siblings('.expendMoney').text();
	  }
	  var trIndex = $("#subjectsBody tr").length,
	  		//dataSb = $(this).siblings('.isGjjorSB').attr('data-sb') || '';	//获取是否是公积金或者社保
			jin_E = 0,
			qian = accountBalancing.split(',').join("");
	  var str = "",
	  //获取分支个数
    $subCodeDiv = $(this).parent('.subject_TD').children('.subCodeDiv'),
    fenZhi = $subCodeDiv.length;
	  for(var i=0; i<fenZhi; i++){
	  	var CodeDiv_text = $subCodeDiv.eq(i).text(),
	  	CodeDiv_id = $subCodeDiv.eq(i).attr('id'),
	  	CodeDiv_codeid = $subCodeDiv.eq(i).attr('data-codeid'),
	  	CodeDiv_name = $subCodeDiv.eq(i).attr('data-name'),
	  	CodeDiv_names = $subCodeDiv.eq(i).attr('data-names'),
	  	CodeDiv_money = "";
  		if(CodeDiv_codeid.match(/\|(\S*)/) == null){
	  		CodeDiv_money = accountBalancing.split(',').join("");
	  	}else{
	  		CodeDiv_money = CodeDiv_codeid.match(/\|(\S*)/)[1];
	  	}
	  	str += '<tr>'
	  	+'<td class="subjectTable">'
	  	+'<div class="subCodeDiv" id="'+CodeDiv_id+'" data-codeid="'+CodeDiv_codeid+'" data-name="'+CodeDiv_name+'" data-names="'+CodeDiv_names+'">'+CodeDiv_text+'</div>'
	  	+'<input type="text" id="keyword" onKeyup="getContent(this);" onkeydown="if(event.keyCode==13) return false;" class="subCodeInput"/></td>'
	  	+'<td class="moneyTable"><input type="text" class="subCodeInput" value="'+CodeDiv_money+'"/></td>'
	  	+'<td class="caozuo"><span class="addSpan">+</span><span class="delSpan">×</span></td>'
	  	+'</tr>'
	  }
	  $("#subjectsBody").html(str);
	  layer.open({
        type: 1
        , title: '多科目添加'
        , area: ['660px', '420px']
        , anim: 2
        , content: $("#subjectsF")
        , move: false
        , btn: ['确定', '取消']
        , btnAlign: 'c'
        , yes: function (index) {
            var trIndex = $("#subjectsBody tr").length,
			jin_E = 0,
			qian = accountBalancing.split(',').join("");
			for(var i=0; i<trIndex; i++){
				if($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val().split(',').join("") == '0' || $("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val().split(',').join("") == '0.00'){
					$("#subjectsF .reminder").text('金额不能为0').show();
					return false;
				}
				jin_E += parseFloat($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val().split(',').join(""));
			}
			if(parseFloat(qian) != jin_E){
				$("#subjectsF .reminder").show();
				return false;
			}else{
				btnYes(trIndex);
			}
            $("#subjectsF, .zhezhao").hide();
      	    $("#content").css({"overflow-y":"auto"});
      	    layer.close(index);
        }
        , btn2: function (idx) {
            $("#subjectsF, .zhezhao").hide();
      	    $("#content").css({"overflow-y":"auto"});
        }
        , cancel: function (idx) {
            $("#subjectsF, .zhezhao").hide();
      	    $("#content").css({"overflow-y":"auto"});
        }
      });
      $(".layui-layer-shade").hide();
      $(".zhezhao").show();
      $("#content").css({"overflow-y":"hidden"});
	})

	//点击多科目确定按钮传值
	function btnYes(val){
		var byValue = '<div class="subjects" title="添加多个科目"></div>';
		for(var i=0; i<val; i++){
			//获取金额
			var sum = $("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val();
			//获取科目编码
			var codeIdS = $("#subjectsBody tr:eq('"+i+"') .subjectTable .subCodeDiv").attr("data-codeid"),
			codeId = "";
			if(codeIdS.match(/(\S*)\|/) == null && val == 1){
				//拼接
				$("#subjectsBody tr:eq('"+i+"') .subjectTable .subCodeDiv").attr("data-codeid",codeIdS);
			}else{
				if(codeIdS.match(/(\S*)\|/) == null){
					$("#subjectsBody tr:eq('"+i+"') .subjectTable .subCodeDiv").attr("data-codeid",codeIdS+'|'+sum);
				}else{
					codeId = codeIdS.match(/(\S*)\|/)[1];
					//拼接
					$("#subjectsBody tr:eq('"+i+"') .subjectTable .subCodeDiv").attr("data-codeid",codeId+'|'+sum);
				}
			}
			byValue += $("#subjectsBody tr:eq('"+i+"') .subjectTable").html();
		}
		if($(".bankPopUpBox").css('display') === 'none'){
			$("#data-list tr:eq('"+$index+"') .subject_TD").html(byValue);
			var str= "",
			codeStr="",
			codeXX = $("#data-list tr:eq('"+$index+"') td .subCodeDiv").length,
			id = $("#data-list tr:eq('"+$index+"')").attr("id"),		//主键ID
			bankType =  $("#data-list tr:eq('"+$index+"') td:eq(1)").attr("data-type");		//银行类型
			if(codeXX > 1){
				var codeStr2 = "";
				for(var j=0; j<codeXX; j++){
    				var Kid = $("#data-list tr:eq('"+$index+"') td .subCodeDiv:eq('"+j+"')").attr("id"),	//科目ID
		    		KdataCodeid = $("#data-list tr:eq('"+$index+"') td .subCodeDiv:eq('"+j+"')").attr("data-codeid"),//科目编码
		    		KdataName = $("#data-list tr:eq('"+$index+"') td .subCodeDiv:eq('"+j+"')").attr("data-name"),	//科目名称
		    		KdataNames = $("#data-list tr:eq('"+$index+"') td .subCodeDiv:eq('"+j+"')").attr("data-names");	//科目全称
		    		if(j == codeXX-1){
		    			var codeStr1 = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames;
		    		}else{
		    			var codeStr1 = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames +"(|)";
		    		}
		    		codeStr2 += codeStr1;
		    		var aData = id +"#@!"+ codeStr2 +"#@!"+ bankType +"|[]|";
    			}
			}else{
				Kid = $("#data-list tr:eq('"+$index+"') td .subCodeDiv").attr("id"),	//科目ID
				KdataCodeid = $("#data-list tr:eq('"+$index+"') td .subCodeDiv").attr("data-codeid"),//科目编码
			    KdataName = $("#data-list tr:eq('"+$index+"') td .subCodeDiv").attr("data-name"),	//科目名称
			    KdataNames = $("#data-list tr:eq('"+$index+"') td .subCodeDiv").attr("data-names");	//科目全称
				codeStr = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames;
		    	var aData = id +"#@!"+ codeStr +"#@!"+ bankType +"|[]|";
			}
			singleSave(aData);
		}else{
			$("#bankDataList tr:eq('"+$index+"') .subject_TD").html(byValue);
		}
	}
	
	//单条保存
	function singleSave(data){
		var params = {
    		infos: data
    	}
		postRequest(window.baseURL + '/bank/updBank', params, function(res) {
    		if(res.success == "true"){
    			layer.alert('保存成功', {
			      	icon: 0
			    });
			    multiSearch(1);
    		}else{
    			layer.alert('保存异常', {
			      	icon: 2
			    });
    		}
    	})
	}
	
	//多分路编辑科目列表
	$("#subjectsBody").delegate('.subCodeDiv', 'click', function (event){
		event.stopPropagation();
		$indexS = $(this).parent('td').parent('tr').index();
		var heightI = $(this).offset().top,
		thisH = $(this).siblings('.subCodeInput').height(),
		thisW = $(this).siblings('.subCodeInput').width(),
		leftI = $(this).offset().left,
		scrollH = $("#content").scrollTop();
		$('.subCodeInput').hide();								//隐藏其他input框
		$('.subList').show();									//显示科目列表
		$(this).siblings('.subCodeInput').show();				//显示inpnt框
		$(this).siblings('.subCodeInput').focus();				//获取焦点
		$('.subList').css({"top":heightI+thisH+2+scrollH,"left":leftI-20,"width":thisW+2});
	})

	//点击编辑多个分路金额进行计算
	$("#subjectsBody").delegate('.subCodeInput', 'click', function (){
		var zong = $("#subjectsBody tr").length,	//先获取总行数
		$indexS = $(this).parent('td').parent('tr').index(),
		jin_E = 0;			//定义金额变量
		//循环获取金额
		for(var i=0; i<zong; i++){
			//判断如果当前选中的等于正在执行的则不执行
			if($indexS != i){
				//判断是否填写金额，如果填写了则保存
				if($("#subjectsBody tr:eq('"+i+"') .subjectTable .subCodeDiv").text() != ""){
					jin_E += parseFloat($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val().split(',').join(""));
				}
			}
		}
		//如果金额等于0，则不进方法
		if(jin_E != 0){
			if(isNaN(jin_E)){
				return false;
			}else{
				//去掉千位符
				var qian = accountBalancing.split(',').join("");
				//金额转换int类型并计算
				var sheng = parseFloat(qian).toFixed(2) - jin_E.toFixed(2);
				//计算结果赋值
				$("#subjectsBody tr:eq("+$indexS+") .moneyTable .subCodeInput").val(sheng.toFixed(2));
				if(parseFloat(qian) == jin_E){
					$("#subjectsF .reminder").hide();
				}
			}
		}
	})

	//添加行
	$("#subjectsBody").delegate('.addSpan', 'click', function() {
		 $(this).parents('tr').after(str1);
    })
	//删除行
	$("#subjectsBody").delegate('.delSpan', 'click', function() {
	    var $bookContent = $('#subjectsBody tr').length;
	    // 页面至少保留两行
	    if ($bookContent <= 1) {
	        layer.msg('最少剩下一行');
	        return false;
	    }
	    $(this).parents('tr').remove();
	    var zong = $("#subjectsBody tr").length,	//先获取总行数
	    jin_E = 0;			//定义金额变量
	    //计算金额
	    //计算金额
	    if(zong == 1){
	    	$("#subjectsBody tr:eq(0) .moneyTable .subCodeInput").val(accountBalancing.split(',').join(""));
			$("#subjectsF .reminder").hide();
	    }else{
	    	for(var i=0; i<zong; i++){
				//判断是否填写金额，如果填写了则保存
				if($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val() != ""){
					jin_E += parseFloat($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val());
				}
			}
			//去掉千位符
			var qian = accountBalancing.split(',').join("");
			if(parseFloat(qian) != jin_E){
				$("#subjectsF .reminder").show();
			}
	    }
    })
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
        , title: '导入银行对账单97格式Excel'
        // ,area: [width, '80%']
        , content: $uploadPopup
        , cancel: function () {
          $uploadPopup.hide();
        }
      });
    }

    //保存
    function showUploadsave(idx){
    	var listLength = $("#bankDataList tr").length,
    	codeStr = "",
    	aData = "",
    	str = "";
    	for(var i=0; i<listLength; i++){
    		var id = $("#bankDataList tr:eq('"+i+"')").attr("id"),		//主键ID
				bankType =  $("#bankDataList tr:eq('"+i+"') td:eq(0)").attr("data-type"),		//银行类型
    			codeXX = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv").length;
    		if(codeXX > 1){
    			var codeStr2 = "";
    			for(var j=0; j<codeXX; j++){
    				var Kid = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv:eq('"+j+"')").attr("id"),	//科目ID
		    		KdataCodeid = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv:eq('"+j+"')").attr("data-codeid"),//科目编码
		    		KdataName = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv:eq('"+j+"')").attr("data-name"),	//科目名称
		    		KdataNames = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv:eq('"+j+"')").attr("data-names");	//科目全称
		    		if(j == codeXX-1){
		    			var codeStr1 = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames;
		    		}else{
		    			var codeStr1 = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames +"(|)";
		    		}
		    		codeStr2 += codeStr1;
		    		var aData = id +"#@!"+ codeStr2 +"#@!"+ bankType +"|[]|";
    			}
    		}else{
      			var Kid = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv").attr("id"),	//科目ID
	    		KdataCodeid = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv").attr("data-codeid"),//科目编码
	    		KdataName = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv").attr("data-name"),	//科目名称
	    		KdataNames = $("#bankDataList tr:eq('"+i+"') td .subCodeDiv").attr("data-names");	//科目全称
	    		codeStr = Kid +"|{}|"+  KdataCodeid +"|{}|"+ KdataName +"|{}|"+ KdataNames;
	    		var aData = id +"#@!"+ codeStr +"#@!"+ bankType +"|[]|";
    		}

    		if(Kid == undefined && KdataCodeid == undefined || Kid == "null" && KdataCodeid == "null" || Kid == "" && KdataCodeid == "" ){
    			layer.alert('请填写全部科目', {
			      	icon: 0
			    });
    			return false;
    		}
    		str += aData;
    	}
    	//关闭银行导入弹出框
    	layer.close(idx);
        $(".zhezhao").hide();
        $("#bankPopUpBox").hide();
    	var params = {
    		infos: str
    	}
    	postRequest(window.baseURL + '/bank/updBank', params, function(res) {
    		if(res.success == "true"){
    			layer.alert('保存成功', {
			      	icon: 0
			    });
			    isNull = false;
			    multiSearch(1);
    		}else{
    			layer.alert('保存异常', {
			      	icon: 2
			    });
    		}
    	})
    }

    /**
     * 选择下载银行模板
     */
    function openDown() {
      // 清空银行模板弹窗数据
      $downPopup.find('input').eq(0).prop('checked', true);
      layui.form.render('radio');
      layer.open({
        type: 1
        , title: '选择下载银行模板'
        , area: ['360px', 'auto']
        , anim: 2
        , content: $downPopup
        , btn: ['下载选中', '取消']
        , btnAlign: 'c'
        , yes: function () {
          var url = window.baseURL + '/bank/downBankExcel?bankType=',
          bankType = $downPopup.find(':checked').val();
          window.open(url + bankType);
        }
        , btn2: function (idx) {
          $downPopup.hide();
        }
        , cancel: function (idx) {
          $downPopup.hide();
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

    // 数据删除
    function _deleteData(params) {
      var arr = params.billIDs.split(',');
      postRequest(window.baseURL + '/bank/deleteBankBill', params, function(res) {
        var message = res.message;
        if (res.success === 'true') {
          layer.msg(message, {
            icon: 1,
            shade: 0.1,
            time: 600
          }, function (idx) {
            var $tableBox = $tableList.parents('.table-box');
            $.each(arr, function (idx, val) {
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
          layer.msg(message);
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
          voucherType = data.isCreateVoucher
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
		var value = $(".on").text();
		$("#data-list tr:eq("+$index+") .subCodeDiv").text(value);
		$(".subList").hide();
		$('.subCodeInput').hide();
		if($("#subjectsF").css('display') == "none"){
			var $div = $("#data-list tr:eq("+$index+") .subCodeDiv"),
			id = $(".on").attr('id'),
			dataCodeid = $(".on").attr('data-codeid'),
			dataName = $(".on").attr('data-name'),
			dataNames = $(".on").attr('data-names');
			$div.text(value)
			.attr({
			    'id': id,					//科目ID
			    'data-codeid': dataCodeid,	//科目编码
			    'data-name': dataName,		//末级科目名称
			    'data-names': dataNames		//科目全称
			}).next('input').val(value);
			$div.css({"border":"none"});
		}else{
			var $div = $("#subjectsBody tr:eq("+$indexS+") .subCodeDiv"),
			id = $(".on").attr('id'),
			dataCodeid = $(".on").attr('data-codeid'),
			dataName = $(".on").attr('data-name'),
			dataNames = $(".on").attr('data-names');
			$div.text(value)
			.attr({
			    'id': id,					//科目ID
			    'data-codeid': dataCodeid,	//科目编码
			    'data-name': dataName,		//末级科目名称
			    'data-names': dataNames		//科目全称
			}).next('input').val(value);
			$div.css({"border":"none"});
			var zong = $("#subjectsBody tr").length,	//先获取总行数
			jin_E = 0;			//定义金额变量
			//循环获取金额
			for(var i=0; i<zong; i++){
				//判断如果当前选中的等于正在执行的则不执行
				if($indexS != i){
					//判断是否填写金额，如果填写了则保存
					if($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val() != ""){
						jin_E += parseFloat($("#subjectsBody tr:eq('"+i+"') .moneyTable .subCodeInput").val());
					}
				}
			}
			//如果金额等于0，则不进方法
			if(jin_E != 0){
				//去掉千位符
				var qian = accountBalancing.split(',').join("");
				//金额转换int类型并计算
				var sheng = parseFloat(qian) - jin_E;
				//计算结果赋值
				$("#subjectsBody tr:eq("+$indexS+") .moneyTable .subCodeInput").val(sheng);
				if(parseFloat(qian) == jin_E){
					$("#subjectsF .reminder").hide();
				}
			}
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
            str += '<li id="'+list[i].pkSubId+'" data-codeId="'+list[i].subCode+'" data-name="'+list[i].subName+'" data-nameS="'+list[i].fullName+'" class="sub-title ellipsis' + (i === 0 ? ' on' : '') + '" data-code="' + code + '" title="' + list[i].subCode + '-' + list[i].fullName + '">' + list[i].subCode + '-' + list[i].fullName + '</li>';
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
