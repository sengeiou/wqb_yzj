$(function () {
    //各代理公司负责企业数量比例
    agencyCompany1 = [];
	agencyName1 = [];
	effectiveTime = [];		//平台有效企业数量趋势图时间
	effectiveData = [];		//平台有效企业数量趋势图数据

    dzqyAccCount();  		//各代理公司负责企业数量比例
    effectiveChartLein(); 	//平台有效企业数量趋势图
    zonglan();				//总览
})

//获取平台有效企业数据
function effectiveChartLein() {
	postRequest(window.baseURL + '/adminController/effectiveCorpTrend',{}, function (res) {
		if(res.code === 1){
			var list = res.arrayList,
			rest = 0,
			len = list.length;
			if(len > 0){
				for (let i = 0; i < len; i++) {
					effectiveTime.push(list[i].period);
					effectiveData.push(list[i].collect);
				}
				//自适应图标
				effectiveChart(effectiveTime,effectiveData);
			}else{
				var str = "";
				str += '<div class="TemporarilyNoData">'
						+'<span>暂无数据</span>'
						+'</div>';
				$('#effectiveLine').html(str);
			}
		}else{
			var str = "";
			str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
			$('#effectiveLine').html(str);
		}
	})
}
function effectiveChart(effectiveTime,effectiveData) {
    var obj = {
	  	//日期
	    date: effectiveTime,
	    //数据源
	    income: effectiveData
    }
	effectiveLineCJ(obj);
}
//平台有效企业数量趋势图
function effectiveLineCJ(obj){
	var chart = document.getElementById('effectiveLine');           // 基于准备好的dom，初始化echarts实例
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
		          'label': {                                                  // 在折线图上显示数字
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

//总览
function zonglan(){
	layer.msg('数据统计中，请稍后',{
		icon:16,
		shade:0.3,
		time:-1
	});
	postRequest(window.baseURL + '/adminController/corpOverview', {} , function(res){
		if(res.code === 1){
			$('.byxzz').text(res.countAdded+'家');				//本月新负责企业
			$('.byTy').text(res.countStatuPeriod2+'家');			//本月停用
			$('.zzl').text('占比'+res.countStatuPeriod2Rate+'%');	//本月停用比列
			//$('.dlCom').text(res.queryJz+'家');					//本月记账完成的企业
			$('.ptTyQy').text(res.countStatu2+'家');				//平台累计停用企业
			//$('.byXzDlCom').text('占比'+res.queryJzRate+'%');		//本月记账完成的企业--占比 :
			$('.ptZqy').text('累计'+res.allAcc+'家');				//平台企业总数
			if(res.countAddedRate == undefined){
				$('.byptxzqyzz').hide();
			}else{
				$('.byptxzqyzz').text('增长'+res.countAddedRate+'%');		//本月新负责企业增长率
			}
		}else{
			$('.byxzz').text('0家');				//本月新负责企业
			$('.byTy').text('0家');			//本月停用
			$('.zzl').text('占比0%');		//本月停用比列
			//$('.dlCom').text('0家');					//本月记账完成的企业
			$('.ptTyQy').text('0家');				//平台累计停用企业
			//$('.byXzDlCom').text('占比0%');			//本月记账完成的企业--占比 :
			$('.ptZqy').text('累计0家');				//平台企业总数
			$('.byptxzqyzz').hide();
		}
		layer.msg('数据统计中，请稍后',{
			icon:16,
			shade:0.3,
			time:1
		});
	})
}


//各代理公司负责企业数量比例图
function dzqyAccCount() {
	postRequest(window.baseURL + '/adminController/staffCorpScale', {}, function (res) {
		var str = "";
		if(res.code === 1){
			var list = res.staffCorpList,
			rest = 0,
			len = list.length;
			if(len > 0){
				if(len < 9){
					for (let i = 0; i < len; i++) {
						agencyCompany1.push(list[i].rate);
						agencyName1.push(list[i].user);
					}
				}else{
					for (let i = 0; i < 9; i++) {
						agencyCompany1.push(list[i].rate);
						agencyName1.push(list[i].user);
					}
					for (var j = 9; j < len; j++) {
						rest += list[j].rate;
					}
					agencyName1[9] = '其他';
					agencyCompany1[9] = rest;
				}
				for (var q = 0 ; q < agencyName1.length ; q++){
					str += '<tr>'
					+'<td><div class="main-color1 color'+q+'"></div></td>'
					+'<td style="line-height: 22px;">'+agencyName1[q]+'<label class="mainDzqyAccCount">'+agencyCompany1[q]+'</label></td>'
					+'</tr>';
				}
				$('#business').html(str);
				//自适应图标
	   		_chart2(agencyCompany1, agencyName1);
			}else{
				str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
				$('#businessPie2').html(str);
			}

		}else{
			str += '<div class="TemporarilyNoData">'
					+'<span>暂无数据</span>'
					+'</div>';
			$('#businessPie2').html(str);
		}
	})
}

//各代理公司负责企业数量比例图
 function _chart2(agencyCompany1,agencyName1) {
    //各财务人员负责企业数量比例图
    var chart2 = document.getElementById('businessPie2');
    var chartTotal2 = _total2(agencyCompany1,agencyName1);
    mainchart2({
      'DOM': chart2,
      'name1': chartTotal2.name,
      'data1': chartTotal2.data,
    });
 }
 function _total2(agencyCompany1,agencyName1) {
  var name1 = agencyName1;
  var data1 = agencyCompany1;
  return {
    name: name1,
    data: data1,
  }
}
 //各财务人员负责企业数量比例图
function mainchart2(obj) {
    var myChart = echarts.init(obj.DOM);
    myChart.showLoading();                                         // 动画开始
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
    var listmian = [];
    for(var i = 0;i<obj.data1.length;i++){
        listmian.push({
            name: obj.name1[i],
            value: obj.data1[i],
            itemStyle: labelTop
        });
    }
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
      'series': [
        {
          'type': 'pie',
          'center': ['45%', '50%'],
          'radius': ['0%','70%'],
          'avoidLabelOverlap': false,
          'hoverAnimation': true,      // 关闭 hover 在扇区上的放大动画效果。
          'cursor': 'default',         // 鼠标悬浮时在图形元素上时鼠标的样式是什么。同 CSS 的 cursor。
          'silent': false,             // 图形是否不响应和触发鼠标事件，默认为 false，即响应和触发鼠标事件。
          'itemStyle': labelTop,       // 隐藏线条
        'data': listmian,
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
        }
      ],
      color:['#81ca4b','#ffb432','#f7645e','#ef3333','#b24545','#4f4747','#17683d','#04ab18','#353a10','#4416be']
    };
    myChart.setOption(option);  // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();      // 动画结束
}
