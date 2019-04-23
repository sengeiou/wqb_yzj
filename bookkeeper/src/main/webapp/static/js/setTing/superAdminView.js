$(function () {
	//做账统计
	var yiwancheng,
    jinxingzhong,
    weiwancheng;

    //各代理公司负责企业数量比例
    agencyCompany = [];
	agencyName = [];
	effectiveTime = [];		//平台有效企业数量趋势图时间
	effectiveData = [];		//平台有效企业数量趋势图数据
	growthLineTime = [];	//代理和自主增长率趋势图时间
	growthLineDZData = [];	//代理和自主增长率趋势图代账数据
	growthLineJZData = [];	//代理和自主增长率趋势记账图数据

    subList();		 //做账统计
    dzqyAccCount();  //各代理公司负责企业数量比例
    effectiveLine(); //平台有效企业数量趋势图
    growthLineData();//代理和自主增长率趋势图数据获取
    doZhangList();
    zonglan();

    $("#tableBody").off('click').on('click', '.getHref', function() {
      //获取账套，请求接口
      var code = $(this).attr('data-subid');
      _accounting(code);
    });
})

var val = null,
	cusType = 0;
//做账统计比例图
function subList() {
	postRequest(window.baseURL + '/superAdminController/superAdmin', {}, function (res) {
		if(res.success == "true"){
			var zongshu = res.countData1+res.countData2+res.countData3;
			var baifenbi = (res.countData1/zongshu)*100;
			$(".countDataBFB").text('占比'+baifenbi.toFixed(2)+'%')
			$(".main-superAdminView1").text(res.countData1);
			$(".countData").text(res.countData1);
	    	$(".main-superAdminView2").text(res.countData2);
	    	$(".main-superAdminView3").text(res.countData3);
			yiwancheng = res.countData1;								//已完成
	    	jinxingzhong =  res.countData2;								//进行中
	   		weiwancheng =  res.countData3;								//未完成
	   		//自适应图标
	   		_chart(yiwancheng, jinxingzhong, weiwancheng);       		// 报表数据
		}else{
			layer.msg(res.message || '暂无数据');
		}
	})
}

//各代理公司负责企业数量比例图
function dzqyAccCount() {
	postRequest(window.baseURL + '/superAdminController/getDzqyAccCount', {}, function (res) {
		var str = "";
		if(res.success == "true"){
			var list = res.list,
			rest = 0,
			len = list.length;
			if(len > 0){
				if(len < 4){
					for (let i = 0; i < len; i++) {
						agencyCompany.push(list[i].sl);
						agencyName.push(list[i].loginUser);
					}
				}else{
					for (let i = 0; i < 4; i++) {
						agencyCompany.push(list[i].sl);
						agencyName.push(list[i].loginUser);
					}
					for (var j = 4; j < len; j++) {
						rest += list[j].sl;
					}
					agencyName[4] = '其他';
					agencyCompany[4] = rest;
				}

				for (var q = 0 ; q < agencyName.length ; q++){
					str += '<tr>'
					+'<td><div class="main-color1 color'+q+'"></div></td>'
					+'<td style="line-height: 22px;">'+agencyName[q]+'<label class="mainDzqyAccCount">'+agencyCompany[q]+'</label></td>'
					+'</tr>';
				}
				 	$('#business').html(str);
			}
	   		//自适应图标
	   		_chart1(agencyCompany, agencyName, rest);
		}else{
			var str = "";
			str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
			$('#businessPie').html(str);
		}
	})
}

//平台有效企业数量趋势图
function effectiveLine(){
	var date=new Date;
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	month =(month<10 ? "0"+month:month);
	var mydate = (year.toString()+'-'+month.toString());
	postRequest(window.baseURL + '/superAdminController/getPtActiveCount',{time:mydate}, function (res) {
		if(res.success == "true"){
			var list = res.list,
			rest = 0,
			len = list.length;
			if(len > 0){
				for (let i = 0; i < len; i++) {
					effectiveTime.push(list[i].Month);
					effectiveData.push(list[i].sl);
				}
			}
	   		//自适应图标
			effectiveChart(effectiveTime,effectiveData);
		}else{
			var str = "";
			str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
			$('#effectiveLineCJ').html(str);
		}
	})
}

//代理和自主增长率趋势图数据获取
function growthLineData(){
	layer.msg('数据统计中，请稍后',{
		icon:16,
		shade:0.3,
		time:-1
	});
	postRequest(window.baseURL + '/superAdminController/getPtAccZz', {} ,function (res) {
		if(res.success == "true"){
			var list = res.jzList,
			dzList = res.dzList,
			rest = 0,
			len = list.length;
			if(len > 0){
				for (let i = 0; i < len; i++) {
					growthLineTime.push(list[i].Month);
					growthLineJZData.push(list[i].sl);
					growthLineDZData.push(dzList[i].sl);
				}
			}
	   		//自适应图标
			growthLineChart(growthLineTime,growthLineDZData,growthLineJZData);
		}else{
			var str = "";
			str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
			$('#growthLine').html(str);
		}
		layer.msg('数据统计中，请稍后',{
			icon:16,
			shade:0.3,
			time:1
		});
	})
}
//代理和自主增长率趋势图数据获取
function growthLineChart(growthLineTime,growthLineDZData,growthLineJZData){
	var obj = {
	  	//日期
	    date: growthLineTime,
	    //代账数据
	    dzdata: growthLineDZData,
	    //记账数据
	    jzdata: growthLineJZData
    }
	growthLine(obj);
}
function growthLine(obj){
	var chart = document.getElementById('growthLine');            // 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(chart);                              // 初始化
	myChart.showLoading();                                          // 动画开始
	myChart.resize();
	// 指定图表的配置项和数据
	var option = {
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['代理企业数','自主企业数']
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },

	    'xAxis': [
	    {
	      'type': 'category',
	      'boundaryGap': true,
	      'data': obj.date
	    }
	  ],
	  'yAxis': [
	    {
	      'type': 'value'
	    }
	  ],
	    series: [
	        {
	            name:'代理企业数',
	            type:'line',
	            stack: '总量',
	            'lineStyle': {
		            'normal': {
		              'color': "#ef7671"                                      //连线颜色
		            }
		          },
		          'itemStyle': {
		            'normal': {
		              'color': '#ef7671',                                     // 原点的颜色
		            }
		          },
		          'label': {                                                       // 在折线图上显示数字
		            'normal': {
		              'show': true,
		              'position': 'top'
		            }
		          },
	            data: obj.dzdata
	        },
	        {
	            name:'自主企业数',
	            type:'line',
	            stack: '总量',
	            'lineStyle': {
		            'normal': {
		              'color': "#81ca4b"                                      //连线颜色
		            }
		          },
		          'itemStyle': {
		            'normal': {
		              'color': '#81ca4b',                                     // 原点的颜色
		            }
		          },
		          'label': {                                                       // 在折线图上显示数字
		            'normal': {
		              'show': true,
		              'position': 'top'
		            }
		          },
	            data: obj.jzdata
	        }
	    ]
	};
	myChart.setOption(option);     // 使用刚指定的配置项和数据显示图表。
	myChart.hideLoading();         // 动画结束
}


//平台有效企业数量趋势数据获取
function effectiveChart(effectiveTime,effectiveData) {
    var obj = {
	  	//日期
	    date: effectiveTime,
	    //数据源
	    income: effectiveData
    }
	effectiveLineCJ(obj);
}
function effectiveLineCJ(obj){
	var chart = document.getElementById('effectiveLineCJ');            // 基于准备好的dom，初始化echarts实例
	//chart.style.width = $('.home-left').width() + 'px';           // 获取宽度
	var myChart = echarts.init(chart);                              // 初始化
	myChart.showLoading();                                          // 动画开始
	myChart.resize();
	// 指定图表的配置项和数据
	var option = {
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['企业数量']
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },

	    'xAxis': [
	    {
	      'type': 'category',
	      'boundaryGap': true,
	      'data': obj.date
	    }
	  ],
	  'yAxis': [
	    {
	      'type': 'value'
	    }
	  ],
	    series: [
	        {
	            name:'企业数量',
	            type:'line',
	            stack: '总量',
	            'lineStyle': {
		            'normal': {
		              'color': "#66aeff"                                      //连线颜色
		            }
		          },
		          'itemStyle': {
		            'normal': {
		              'color': '#66aeff',                                     // 原点的颜色
		            }
		          },
		          'label': {                                                       // 在折线图上显示数字
		            'normal': {
		              'show': true,
		              'position': 'top'
		            }
		          },
	            data: obj.income
	        }
	    ]
	};
	myChart.setOption(option);     // 使用刚指定的配置项和数据显示图表。
	myChart.hideLoading();         // 动画结束
}
//各代理公司负责企业数量比例图
 function _chart1(agencyCompany,agencyName) {
    // 做账统计计算百分比
    var chart2 = document.getElementById('businessPie');				//各财务人员负责企业数量比例图
    var chartTotal2 = _total1(agencyCompany,agencyName);
    mainchart({
      'DOM': chart2,
      'name': chartTotal2.name,
      'data': chartTotal2.data,
    });
 }
function _total1(agencyCompany,agencyName) {
  var name = agencyName;
  var data = agencyCompany;
  return {
    name: name,
    data: data,
  }
}

// 设置圆形图数据
 function _chart(yiwancheng, jinxingzhong, weiwancheng) {
    // 做账统计计算百分比
    var chart3 = document.getElementById('main-superAdminView');		//做账统计
    var chartTotal3 = _total(yiwancheng, jinxingzhong, weiwancheng);
    mainchart1({
      'DOM': chart3,
      'yiwancheng': chartTotal3.yiwancheng,
      'jinxingzhong': chartTotal3.jinxingzhong,
      'weiwancheng': chartTotal3.weiwancheng,
    })
  }
 //数量百分比
function _total(yiwancheng, jinxingzhong, weiwancheng) {
  var yiwancheng = yiwancheng;
  var jinxingzhong = jinxingzhong;
  var weiwancheng = weiwancheng;
  return {
    yiwancheng: yiwancheng,
    jinxingzhong: jinxingzhong,
    weiwancheng: weiwancheng
  }
}

 // 点击记账
// 当浏览器检测到非用户操作产生的新弹出窗口，则会对其进行阻止。因为浏览器认为这可能是一个广告，不是一个用户希望看到的页面
function _accounting(id) {
    var url,
    tempwindow = navigator.userAgent.match(/Chrome/i) == 'Chrome' ? window.open('chrome://newtab', '_blank') : window.open('about:blank', '_blank'); // 先打开页面
    postRequest(window.baseURL + '/account/chgAccount', { accountID: id }, function(res) {
      // 0没有初始化，1已经初始化
      if (res.message === 'success') {
        if (res.statu == 0) {
          url = window.baseURL + '/subinit/initView?init=0';
        } else {
          url = window.baseURL + '/invoice/importView#1';
        }
        tempwindow.location = url; // 后更改页面地址
      }
    })
}

 /**
   * 圆形图展示
   * @DOM 传入的ID
   * @complete 已完成的值
   * @percentage 已完成百分比
   * @unfinished 未完成的值
   */
//各财务人员负责企业数量比例图
function mainchart(obj) {
    var myChart = echarts.init(obj.DOM);
    myChart.showLoading();                                           // 动画开始
    myChart.resize();
    var serlist=[];
    for(var i = 0; i<obj.data.length; i++){
        serlist.push({
            name: obj.name[i],
            value: obj.data[i],
            itemStyle: labelTop
        });
    }
    // 外环有数据样式
    var labelTop = {
      'normal': {
        'label': {
          'show': true,
          'position': 'center',
          'textStyle': {
            'color': '#73a6ff',
            'fontSize': 20,
          }
        },
        'labelLine': {
          'show': true
        }
      }
    };
    // 外环没有数据的样式
    var labelBottom = {
      'normal': {
        'color': '#e4e4e4',
        'label': {
          'show': true,
        }
      }
    };

    var option = {
    	'tooltip': {
        'trigger': 'item',
        'formatter': "{c} ({d}%)"
      },
    	'title' : {
        left: 'center',
        top: 60,
        textStyle:{
          color: '#81ca4b',
          fontSize: 22,
        }
	    },
      'series': [
        {
          'type': 'pie',
          'center': ['45%', '50%'],
          'radius': ['0%','70%'],
          'avoidLabelOverlap': false,
          'hoverAnimation': true,      // 关闭 hover 在扇区上的放大动画效果。
          'cursor': 'default',          // 鼠标悬浮时在图形元素上时鼠标的样式是什么。同 CSS 的 cursor。
          'silent': false,               // 图形是否不响应和触发鼠标事件，默认为 false，即响应和触发鼠标事件。
          'itemStyle': labelTop,        // 隐藏线条
          labelLine:{
                normal:{
                    length:15
                }
            },
        'data': serlist,
		label: {
            normal: {
            	formatter: '{c}({d}%)',
	            show: true,
	            position : 'outside',
	            textStyle : {
								fontWeight : 'normal',
								fontSize : 14,
							}
            }
	       },
        }
      ],
      color:['#81ca4b','#ffb432','#f7645e','#ef3333','#b24545','#4f4747','#17683d','#04ab18','#353a10','#4416be']
    };
    myChart.setOption(option);  // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();      // 动画结束
}

function mainchart1(obj) {
    var myChart = echarts.init(obj.DOM);
    myChart.showLoading();                                           // 动画开始
    myChart.resize();
    // 外环有数据样式
    var labelTop = {
      'normal': {
        'label': {
          'show': true,
          'position': 'center',
          'textStyle': {
            'color': '#73a6ff',
            'fontSize': 20,
          }
        },
        'labelLine': {
          'show': true
        }
      }
    };
    // 外环没有数据的样式
    var labelBottom = {
      'normal': {
        'color': '#e4e4e4',
        'label': {
          'show': true,
        }
      }
    };

    var option = {
    	'tooltip': {
        'trigger': 'item',
        'formatter': "{b}: {c} ({d}%)"
      	},
    	'title' : {
	        left: 'center',
	        top: 60,
	        textStyle:{
	          color: '#81ca4b',
	          fontSize: 22,
	        }
	    },
      	'series': [{
	          'type': 'pie',
	          'center': ['45%', '50%'],
	          'radius': ['0%','70%'],
	          selectedMode: 'single', // 点击脱离圆饼
	          'avoidLabelOverlap': false,
	          'hoverAnimation': true,      // 关闭 hover 在扇区上的放大动画效果。
	          'cursor': 'default',          // 鼠标悬浮时在图形元素上时鼠标的样式是什么。同 CSS 的 cursor。
	          'silent': false,               // 图形是否不响应和触发鼠标事件，默认为 false，即响应和触发鼠标事件。
	          'itemStyle': labelTop,        // 隐藏线条
	        'data': [
	          { 'name': '已完成', 'value': obj.yiwancheng, 'itemStyle': labelTop },                // 展示的已完成值和百分比
	          { 'name': '进行中', 'value': obj.jinxingzhong, 'itemStyle': labelTop } ,            // 写入未完成的值
	          { 'name': '未开始', 'value': obj.weiwancheng, 'itemStyle': labelTop }
	        ],
			label: {
	            normal: {
	            	formatter: '{b}:{c}({d}%)',
		            show: true,
		            position : 'outside',
		            textStyle : {
									fontWeight : 'normal',
									fontSize : 14,
								}
	            }
		    },
        }],
      	color:['#81ca4b','#ffb432','#f7645e']
    };
    myChart.setOption(option);  // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();      // 动画结束
    myChart.on("click", function(param){
    	cusType = param.dataIndex+1	;//执行点击效果
    	$('#main-query').val('');
    	doZhangList();
    	return cusType;
    });	//给饼图添加点击事件
}

//总览数
function zonglan(){
	var time = session.busDate;
	postRequest(window.baseURL + '/superAdminController/getZlInfo', {time:time} , function(res){
		if(res.success == 'true'){
			var bypttyqyzb = res.bypttyqyzb || '';
			var bypttyqyzbJQ = bypttyqyzb.substring(0,bypttyqyzb.length-1);

			var byptxzqyzz = res.byptxzqyzz || '';
			var byptxzqyzzJQ = byptxzqyzz.substring(0,byptxzqyzz.length-1);

			var zzl = res.zzl || '';
			var zzlJQ = zzl.substring(0,zzl.length-1);

			$('.byxz').text(res.byptxzqy+'家');				//本月新负责企业
			$('.byTy').text(res.bypttyqy+'家');				//本月停用
			$('.zzl').text('占比'+Math.round(bypttyqyzbJQ * 100) / 100 +'%');			//本月停用比列
			$('.dlCom').text('(累计'+res.dlCom+'家)');		//累计
			$('.ptTyQy').text(res.ptljtyqy+'家');			//平台累计停用企业
			$('.byXzDlCom').text(res.byXzDlCom+'家');		//本月新增代理记账公司
			$('.ptZqy').text('累计'+res.ptzqys+'家');			//平台企业总数
			$('.byzzl').text('增长'+Math.round(zzlJQ * 100) / 100 +'%');
			$('.byptxzqyzz').text('增长'+Math.round(byptxzqyzzJQ * 100) / 100 +'%');		//本月新负责企业增长率
		}
	})
}
// 点击查询
$('#main-searchS').off('click').on('click', function() {
  val = $('#main-query').val();
  var type = $('#tableBody').attr('data-type'); // 获取标识符
  subList();
  cusType = 0;
  if (!val) {
    layer.msg('请输入关键字');
    if (type) { // 标识如果没有输入关键字，有标识就跳回第一页
      $('#tableBody').removeAttr('data-type'); //如果没有标识，便不在请求
      subList({ 'keyWord': '' }); // 从新数据请求
    }
    return false;
  }

  if (val === type) { // 如果搜索的关键字没有改变，那就不在请求数据
    return false;
  }

  // 添加标识
  $('#tableBody').attr('data-type', val);
  doZhangList({ 'keyWord': val }); // 关键字搜索
});
removeInputVal($('#main-query')); // 获取焦点清空内容

function doZhangList(obj){
	var data = {
      'curPage': 1,       // 当前页
      'maxSize': 10,      // 每页几条
      'cusType': cusType  //状态
    }
	$.extend(data, obj);
	postRequest(window.baseURL + '/superAdminController/statusCustomer', data, function(res) {
		var list = res.list,
        len = list.length,
        str,
        index,
        percent,
        filterName,
        percent1;
	    if (len < 1) {
	        $('.main-list-item li a').removeAttr("href");
	    }
	    if (res.success === 'fail') {
	        layer.msg(res.message || '暂无账套，请新建一个');
	        return;
	    }
	    if (res.success === 'true') {
	    	if (len > 0) {
	    		for (var i = 0; i < len; i++) {
	    			percent = list[i].jz === 1 ? '100%' : list[i].cs === 1 ? '75%' : list[i].cv === 1 ? '50%' : '0%';
	    			index = 1 + i;
	    			str += '<tr data-id="' + list[i].accountID + '">' +
		              '<td  class="dataName" style="text-align: center;">' + index + '</td>' +
		              '<td class="dataName">' + list[i].companyName + '</td>' +
		              '<td class="dataName">' + list[i].phoneNumber + '</td>' +
		              '<td class="dataName" style="text-align: center;">' + (list[i].statuperiod || '') + '</td>'+
		              '<td class="dataName" style="text-align: center;">' + (list[i].agencyCompany || '') + '</td>'+
		              '<td class="dataName" style="text-align: center;">' + (list[i].agent || '') + '</td>'
		            if (list[i].statutype == '正常') {
		              str += '<td class="dataName" style="text-align: center;">'+list[i].statutype+'</td>'
		            } else if (list[i].statutype == '已禁用') {
		              str += '<td class="dataName" style="color:red;text-align: center;">已禁用</td>'
		            } else {
		              str += '<td class="dataName" style="color:red;text-align: center;">未开启</td>'
		            }
		            str += '' +
		              '<td class="dataName" style="text-align: center;">' + list[i].schedule + '<td class="main-button">'
		            if (list[i].statutype == '正常') {
		              str += '<div class="getHref main-button-css" data-subId="' + list[i].accountID + '">记账</div>'
		            } else {
		              str += '<button disabled style="background-color: #ccc;" class=" main-button-css">记账</button>'
		            }
		            +
		            '</td>' +
		            '</tr>';
	    		}
	    	}else{
	    		str = ' <tr class="tc"><td colspan="15" style="height: 80px;text-align: center;font-size:18px">暂无数据</td></tr>';
	    	}
	    	// 数据渲染
        	$('#tableBody').html(str);
        	// 分页
	        flushPage($('#layui-table-page2'), data.curPage, res.listsize, data.maxSize, function(i) {
	          doZhangList({ 'keyWord': val, 'curPage': i }); // 关键字
	        });
	    }
	})
}

//回车键查询
function keySelect() {
  if (event.keyCode == 13) {
    event.returnValue = false;
    event.cancel = true;
    $('#main-search').click();
  }
}
