/*
* 打印报表
*/
var _printContent = document.getElementById("print-content");

// 输出DOM
function _reportHTML(opt) {
  var params = opt.params,
    html,
    tableHTML;
  if (typeof opt !== 'object') {
    return console.log('请传入对象');
  }
  // 报表预览
  else if (opt.type === 'report') {
    if (opt.title === '资产负债表') {
      tableHTML = _balanceHTML(params);
    } else if (opt.title === '利润表') {
      tableHTML = _profitHTML(params);
    } else if (opt.title === '现金流量表') {
      tableHTML = '现金流量表';
    }
    html = '<div class="print-content' + (opt.title === '资产负债表' ? ' big' : '') + '">' +
      '<div class="head-title">' +
      '<h2 class="tc title">' + opt.title + ' </h2>' +
      '<p class="tr">第1页</p>' +
      '<div>' +
      '<p class="left">单位名称：' + opt.compName + '</p>' +
      '<p class="right">单位： 元</p>' +
      '<p class="tc">' + opt.date + '</p>' +
      '</div>' +
      '</div>' +
      tableHTML +
      '</div>';
    _printContent.innerHTML = html;
  }

  // 资产负债表预览
  function _balanceHTML(params) {
    var assList = params.assetsList,
        liaList = params.liabiliList,
        leng = assList.length,
        str = '<table class="assets">' +
          '<colgroup>' +
          '<col width="24%">' +
          '<col width="13%">' +
          '<col width="13%">' +
          '<col width="24%">' +
          '<col width="13%">' +
          '<col width="13%">' +
          '</colgroup>' +
          '<thead>' +
          '<tr>' +
          '<th>资产</th>' +
          '<th>期末余额</th>' +
          '<th>年初余额</th>' +
          '<th>负债和所有者权益(或股东权益)</th>' +
          '<th>期末余额</th>' +
          '<th>年初余额</th>' +
          '</tr>' +
          '</thead>' +
          '<tbody>';

      for (var i = 0; i < leng; i++) {
        var flag = (liaList[i].key === '' || liaList[i].key === 49 || liaList[i].key === 55);

        str += '<tr>'
          + '<td class="tl' + (assList[i].key === '' ? '' : ' ti') + '">'
          + '<span>' + assList[i].name + '</span>'
          + '</td>'
          + '<td>' + assList[i].end + '</td>'
          + '<td>' + assList[i].init + '</td>'
          + '<td class="tl' + (flag ? '' : ' ti') + '">'
          + '<span>' + liaList[i].name + '</span>'
          + '</td>'
          + '<td>' + liaList[i].end + '</td>'
          + '<td>' + liaList[i].init + '</td>'
          + '</tr>';
      }
    str += '</tbody></table>';
    return str;
  }

  // 利润表预览
  function _profitHTML(params) {
    var list = params.profitList,
      leng = list.length,
      level,
      str = '<table class="profit">' +
      '<colgroup>' +
      '<col width="56%">' +
      '<col width="24%">' +
      // '<col width="17%">' +
      '<col width="24%">' +
      '</colgroup>' +
      '<thead>' +
      '<tr>' +
      '<th>项目</th>' +
      '<th>本年金额</th>' +
      // '<th>本季金额</th>' +
      '<th>本期金额</th>' +
      '</tr>' +
      '</thead>' +
      '<tbody>';

    for (var i = 0; i < leng; i++) {
      // var flag = (list[i].key === 1 || list[i].key === 11 || list[i].key === 15 || list[i].key === 17 || list[i].key === 18);
      if (list[i].level === 2) {
        level = ' ti1';
      } else if (list[i].level === 3) {
        level = ' ti4';
      } else if (list[i].level === 4) {
        level = ' ti8';
      } else {
        level = '';
      }
      str += '<tr>' +
        '<td class="tl' + level + '">' + list[i].name + '</td>' +
        '<td>' + list[i].year + '</td>' +
        // '<td>' + list[i].quarter + '</td>' +
        '<td>' + list[i].period + '</td>' +
        '</tr>';
    }
    str += '</tbody></table>';
    return str;
  }
}


