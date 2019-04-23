/**
 * 凭证打印
 */
var _printContent = document.getElementById("print-content"),
  LODOP;

layui.use('form', function () {
  // var form = layui.form;

  //各种基于事件的操作，下面会有进一步介绍
});

// 输出凭证
function _innerHtml(voucherList, compName) {
  var printList = voucherList, // 凭证列表
    LIMIT = 5, // 凭证显示分录数量
    printLen = printList.length, // 凭证数量
    i = 0,
    vHead, // 每张凭证头
    vBody, // 每张凭证体
    dataLen, // 分录条数
    lv1SubCode, // 一级科目编码
    longFlag = false, // 不显示数量金额
    pageData,
    arr = [],
    page,
    pages,
    date,
    day,
    totalAmount,
    str = '';

  if (printLen === 0) {
    alert('当前没有凭证可打印');
    return;
  } else {
    // 循环每张凭证
    for (i; i < printLen; i++) {
      vHead = printList[i].voucherHead;
      vBody = printList[i].voucherBodyList;
      dataLen = vBody.length;
      if (dataLen === 0) {
        continue;
      }
      // 凭证分页
      pageData = [];
      arr = [];
      longFlag = true;
      for (var k = 0; k < dataLen; k++) {
        // 判断是否不显示数量金额
        lv1SubCode = vBody[k].subjectID.substring(0, 4);
        if (lv1SubCode == '1403' || lv1SubCode == '1405' || lv1SubCode == '1408') {
          longFlag = false;
        }
        // 循环需要处理的数组
        arr.push(vBody[k]);
        if ((k != 0 && (k + 1) % LIMIT == 0) || k == dataLen - 1) {
          pageData.push(arr);
          arr = [];
        }
      }
      pages = pageData.length;
      // 凭证分页显示
      for (var j = 0; j < pages; j++) {
        page = j + 1;
        // 显示日期
        date = vHead.period.split('-');
        day = new Date(date[0], date[1], 0).getDate();
        // 显示合计金额
        if (page === pages) {
          totalAmount = '<td>' + _formatNum(vHead.totalDbit || '') + '</td>' +
            '<td>' + _formatNum(vHead.totalCredit || '') + '</td>';
        } else {
          totalAmount = '<td></td><td></td>';
        }
        str += '<div class="print-content">' +
          '<div class="head-title">' +
          '<p>' + compName + '</p>' +
          '<h2>' +
          '<span class="title">记 账 凭 证<span></span></span>' +
          '</h2>' +
          '<p>' + vHead.period + '-' + day + '</p>' +
          '<p class="tr vchr-no">记字第' + vHead.voucherNO + '号（' + page + '/' + pages + '）</p>' +
          '</div>';
        if (longFlag) {
          str += '<table class="long">' +
            '<colgroup>' +
            '<col width="150">' +
            '<col width="355">';
        } else {
          str += '<table>' +
            '<colgroup>' +
            '<col width="150">' +
            '<col width="235">' +
            '<col width="60">' +
            '<col width="60">';
        }
        str += '<col width="97">' +
          '<col width="97">' +
          '</colgroup>' +
          '<thead>' +
          '<tr>' +
          '<th class="w1">摘要</th>' +
          '<th class="w2">会计科目</th>' +
          '<th class="w3">数量</th>' +
          '<th class="w3">单价</th>' +
          '<th class="w4">借方金额</th>' +
          '<th class="w4">贷方金额</th>' +
          '</tr>' + '</thead>' +
          '<tbody>' +
          _vBody(pageData[j]) +
          '</tbody>' +
          '<tfoot>' +
          '<tr>' +
          '<td colspan="' + (longFlag ? '2' : '4') + '">' +
          '<span class="left">附单据 0 张</span>' +
          '<span>合计</span>' +
          '</td>' +
          totalAmount +
          '</tr>' +
          '</tfoot>' +
          '</table>' +
          '<div class="vchr-input">' +
          '<span>会计主管：</span>' +
          '<span class="value">&nbsp;</span>' +
          '<span>复核：</span>' +
          '<span class="value">&nbsp;</span>' +
          '<span>记账：</span>' +
          '<span class="value">Manager&nbsp;</span>' +
          //        '<span class="value">' + vHead.createpsn + '</span>' +
          '<span>出纳：</span>' +
          '<span class="value">&nbsp;</span>' +
          '<span>经办：</span>' +
          '<span class="value">&nbsp;</span>' +
          '<span>制单：</span>' +
          //        '<span class="value">' + vHead.createpsn + '</span>' +
          '<span class="value">Manager&nbsp;</span>' +
          '</div>' +
          '</div>';
      }
    }
    _printContent.innerHTML = str;
  }

  function _vBody(data) {
    var str1 = '',
      len = data.length;

    for (var i = 0; i < len; i++) {
      str1 += '<tr>' +
        '<td class="w1">' +
        '<p class="w1">' + (data[i].vcabstact || '') + '</p>' +
        '</td>' +
        '<td class="w2">' +
        '<p class="w2">' + data[i].subjectID + ' ' + data[i].vcsubject + '</p>' +
        '</td>' +
        '<td class="tr w3">' + (data[i].number || '') + '</td>' +
        '<td class="tr w3">' + (data[i].price ? data[i].price.toFixed(2) : '') + '</td>' +
        '<td class="tr w4">' + _formatNum(data[i].debitAmount || '') + '</td>' +
        '<td class="tr w4">' + _formatNum(data[i].creditAmount || '') + '</td>' +
        '</tr>';
    }
    if (len < LIMIT) {
      for (var j = len; j < LIMIT; j++) {
        str1 += '<tr>' +
          '<td class="w1">' +
          '<p class="w1">&nbsp;</p>' +
          '</td>' +
          '<td class="w2">' +
          '<p class="w2"></p>' +
          '</td>' +
          '<td class="tr w3"></td>' +
          '<td class="tr w3"></td>' +
          '<td class="tr w4"></td>' +
          '<td class="tr w4"></td>' +
          '</tr>';
      }
    }
    return str1;
  }
}

/**
 * 输出凭证汇总表
 * sumList, compName, range, operator
 */
function _sumHTML(params) {
  var LIMIT = 7, // 凭证显示分录数量
    range,
    list = params.sumList,
    zong = params.sumListS,
    // dfAmount = params.dfAmount,
    // jfAmount = params.jfAmount,
    total = list.length,
    pageData = [],
    arr = [],
    page,
    pages,
    i = 0,
    j = 0,
    str = '',
    totalStr;

  totalStr = '<div class="total-amount">' +
    '<label>贷方总金额:' + params.dfAmount + '</label>' +
    '<label>借方总金额:' + params.jfAmount + '</label>' +
    '</div>';
  // 凭证分页
  //循环需要处理的数组
  for (i; i < total; i++) {
    arr.push(list[i]);
    if ((i != 0 && (i + 1) % LIMIT == 0) || i == total - 1) {
      pageData.push(arr);
      arr = [];
    }
  }
  pages = pageData.length;
  // 凭证分页显示
  for (j; j < pages; j++) {
    page = j + 1;
    str += '<div class="print-content sum">' +
      '<div class="head">' +
      '<h2>凭证汇总表</h2>' +
      '<p class="tr">第<span>' + page + '</span>页</p>' +
      '<p>单位名称：' + params.compName + '</p>' +
      '<p>凭证范围：' + params.range + '<span></span><span>记：' + zong + '张（1-' + zong + '）</span><span>共' + zong +
      '张</span><span>附件共0张</span></p>' +
      '</div>' +
      '<table border="1">' +
      '<colgroup>' +
      '<col width="79">' +
      '<col width="157">' +
      '<col width="157">' +
      '<col width="157">' +
      '</colgroup>' +
      '<thead>' +
      '<tr>' +
      '<th>科目代码</th>' +
      '<th>科目名称</th>' +
      '<th>借方金额</th>' +
      '<th>贷方金额</th>' +
      '</tr>' +
      '</thead>' +
      '<tbody>' +
      _vBody(pageData[j]) +
      '</tbody>' +
      '</table >' +
      '<div class="foot">' +
      (page == pages ? totalStr : '') +
      // '<p class="tr">操作员：' + params.operator + '</p>' +
      '<p class="tr">操作员： Manager</p>' +
      '<p class="tr">打印时间：' + _formatDate(new Date, 'yyyy/MM/dd') + '</p>' +
      '</div>' +
      '</div>';
  }
  // str += '<div class="labelS">' +
  //   '<label>贷方总金额:' + dfAmount + '</label>' +
  //   '<label>借方总金额:' + jfAmount + '</label>' +
  //   '</div>';
  _printContent.innerHTML = str;

  function _vBody(data) {
    var str1 = '',
      len = data.length;

    for (var i = 0; i < len; i++) {
      str1 += '<tr>' +
        '<td class="tl">' + data[i].subCode + '</td>' +
        '<td class="tl">' + data[i].subName + '</td>' +
        '<td class="tr">' + _formatNum(data[i].currentAmountDebit) + '</td>' +
        '<td class="tr">' + _formatNum(data[i].currentAmountCredit) + '</td>' +
        '</tr>';
    }
    return str1;
  }
}

// 选择凭证汇总表打印时间

// 打印预览
function _printPreview(type) {
  var $tips = document.getElementsByTagName('body')[0].getElementsByTagName('font');
  // var $tips = document.querySelectorAll('font');
  if ($tips.length) {
    $tips[0].style.paddingLeft = '20px';
    document.documentElement.scrollTop = 0;
    document.body.scrollTop = 0;
    return;
  }
  LODOP = getLodop();
  if ($tips.length) {
    $tips[0].style.paddingLeft = '20px';
    document.documentElement.scrollTop = 0;
    document.body.scrollTop = 0;
    return;
  }
  var styleCSS = '<link href="' + window.baseURL + '/css/print-voucher.css" type="text/css" rel="stylesheet">';
  var printHtml = styleCSS + '<body>' + _printContent.innerHTML + '</body>';

  // 选择纸张
  // PRINT_INITA 打印初始化 Lodop会根据该名记忆相关的打印设置、打印维护信息。若strTaskName空，控件则不保存本地化信息，打印全部由页面程序控制。
  // ADD_PRINT_HTM(Top,Left,Width,Height,strHtmlContent)
  var checkedValue = document.querySelector('[name=type]:checked').value;
  switch (checkedValue) {
    case '发票版针式':
      if (type === 'sum') {
        _print_init(550, '凭证打印_发票版_针式');
        LODOP.ADD_PRINT_HTM(36, '1.1in', 550, 450, printHtml);
      } else {
        _print_init(680, '凭证打印_发票版_针式');
        LODOP.ADD_PRINT_HTM(36, '0.5in', 680, 450, printHtml);
      }
      // LODOP.SET_PRINT_PAGESIZE(printHtml, '9.5in', '5.5in');
      LODOP.SET_PRINT_PAGESIZE(0, '9.5in', '5.5in', '');
      break;
    case '发票版纵向':
      if (type === 'sum') {
        _print_init(550, '凭证打印_发票版_纵向');
        LODOP.ADD_PRINT_HTM(36, '1.6in', 550, 450, printHtml);
      } else {
        _print_init(680, '凭证打印_发票版_纵向');
        LODOP.ADD_PRINT_HTM(36, '1in', 680, 450, printHtml);
      }
      // LODOP.SET_PRINT_PAGESIZE(printHtml, '5.5in', '9.5in');
      LODOP.SET_PRINT_PAGESIZE(2, 0, 0, '发票纸'); // 需要对打印机自定义纸张：发票纸 9.5in*5.5in
      break;
      // case 'A4':
      //   if (type === 'sum') {
      //     LODOP.ADD_PRINT_HTM(36, '0.5in', 550, 450, printHtml);
      //   } else {
      //     LODOP.ADD_PRINT_HTM(36, '0.5in', 680, 450, printHtml);
      //   }
      //   // printHtml = printHtml.replace(/class="print-content/g, 'class="print-content a4');
      //   // 0：定义纸张宽度，为0表示无效设置，A4：设置纸张为A4
      //   LODOP.SET_PRINT_PAGESIZE(0, 0, 0, 'A4');
      //   break;
      // case 'A5':
      //   LODOP.SET_PRINT_PAGESIZE(0, '148mm', '210mm');
      //   break;
      // case 'B5':
      //   // LODOP.SET_PRINT_PAGESIZE(0, '182mm', '257mm');
      //   LODOP.SET_PRINT_PAGESIZE(0, '176mm', '250mm');
      //   break;
    case '自定义':
      if (type === 'sum') {
        _print_init(550, '凭证打印_自定义');
        LODOP.ADD_PRINT_HTM(36, '5%', 550, 450, printHtml);
      } else {
        _print_init(680, '凭证打印_自定义');
        LODOP.ADD_PRINT_HTM(36, '5%', '90%', 450, printHtml);
      }
      // 如果打印纸张不固定，希望由操作者自主选择纸张时，则不要调用 SET_PRINT_PAGESIZE。
      break;
  }
  LODOP.PREVIEW();

  // 打印初始化
  function _print_init(width, strTaskName) {
    LODOP.PRINT_INITA(0, 0, width, 450, strTaskName);
  }
}

// 重写toFixed兼容方法
Number.prototype.toFixed = function (b) {
  if (20 < b || 0 > b) throw new RangeError("toFixed() digits argument must be between 0 and 20");
  if (isNaN(this) || this >= Math.pow(10, 21)) return this.toString();
  if ("undefined" == typeof b || 0 == b) return Math.round(this).toString();
  var a = this.toString(),
    d = a.split(".");
  if (2 > d.length) {
    for (var a = a + ".", c = 0; c < b; c += 1) a += "0";
    return a
  }
  c = d[0];
  d = d[1];
  if (d.length == b) return a;
  if (d.length < b) {
    for (c = 0; c < b - d.length; c += 1) a += "0";
    return a
  }
  a = c + "." + d.substr(0, b);
  c = d.substr(b, 1);
  5 <= parseInt(c, 10) && (c = Math.pow(10, b), a = (Math.round(parseFloat(a) * c) + 1) / c, a = a.toFixed(b));
  return a
};

// 时间戳转换日期格式
function _formatDate(b, d) {
  if (!b) return "";
  var a = b ? new Date(b) : new Date;
  b = a.getFullYear();
  var c = 10 > a.getMonth() + 1 ? "0" + (a.getMonth() + 1) : a.getMonth() + 1,
    e = 10 > a.getDate() ? "0" + a.getDate() : a.getDate(),
    f = 10 > a.getHours() ? "0" + a.getHours() : a.getHours(),
    g = 10 > a.getMinutes() ? "0" + a.getMinutes() : a.getMinutes(),
    a = 10 > a.getSeconds() ? "0" + a.getSeconds() : a.getSeconds();
  if (d === 'yyyy/MM/dd') {
    return b + '/' + c + '/' + e + " " + f +
      ":" + g + ":" + a;
  }
  return b + '年' + c + '月' + e + '日';
};

// 格式化金额
function _formatNum(str) {
  if (!str) {
    return '';
  }
  if (typeof str === 'string') {
    str = parseFloat(str);
  }
  str = str.toFixed(2);
  return str.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
