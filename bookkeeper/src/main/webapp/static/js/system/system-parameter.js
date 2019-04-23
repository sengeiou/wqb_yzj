/**
 * 系统参数
 * weiqi 2019-3-8 10:29:06
 */
;
(function () {

  $(function () {
    var
      $companyName = $('#companyName'), // 公司名称
      $codeLen = $('#code-length'), // 编码长度
      $saveBtn = $('#save-btn'); // 保存;

    $companyName.on('blur', function () {
      removeAllSpace($(this));
    });

    // 科目级次
    layui.form.on('select(subject-level)', function (data) {
      var level = Number(data.value), // 得到被选中的值
        _currentLen = $codeLen.children('span').length, // 当前编码长度
        diff = level - _currentLen, // 科目级次 - 当前科目长度
        str = '';
      if (diff < 0) {
        $('#code-length > span:gt(' + (level - 1) + ')').remove();
      } else if (diff > 0) {
        for (var i = 0; i < diff; i++) {
          str += '<span>&nbsp;-&nbsp;<input type="number" class="layui-input sub-text" value="2" autocomplete="off" min="2" max="5"></span>';
        }
        $codeLen.append(str);
      }

    });

    // 编码长度
    $codeLen
      .off('blur').on('blur', 'input', function () {
        var $this = $(this),
          v = $this.val() || 2;
        $this.val(v);
      })
      .off('input').on('input', 'input', function () {
        var $this = $(this),
          v = $this.val().replace(/[^2-5]/g, '').substring(0, 1);
        $this.val(v);
      })
      .off('focus').on('focus', 'input', function () {
        $(this).select();
      })

    // 保存
    $saveBtn.on('click', function () {
      if (!check()) return;
      submit();

      function check() {
        var _companyName = $companyName.val();
        if (_companyName.length < 2) {
          layer.tips('请输入至少两位字符', '#companyName', {
            tips: [2, '#1eadf3']
          });
          $companyName.focus();
          return false;
        }
        return true;
      }

      function setParam() {
        var arr = [],
          $codeInput = $codeLen.find('input'),
          len = $codeInput.length;
        for (var i = 0; i < len; i++) {
          arr.push($codeInput.eq(i).val());
        }
        return {
          companyName: $companyName.val(),
          period: $('#period').val(),
          level: $('#subject-level').val(),
          rule: arr.toString().replace(/,/g, '-')
        };
      }

      function submit() {
        var params = setParam();
        layer.alert(JSON.stringify(params));
      }
    })
  });
}());
