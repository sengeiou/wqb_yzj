<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/calculate.css">
<!--计算器-->
<div id="calculator_main">
	<div class="calculator_main" id="mov" onmousedown="mouseDown(this,event)" onmousemove="mouseMove(event)" onmouseup="mouseUp(event)">
	  <div id="calculator_close" class="layui-icon">&#x1007;</div>
		<div id="calculator_base" style="display: block">
				<div id="calculator_base_cup">
					<div id="calculator_base_container">
						<table id="base_table_top" class="calculator_table" cellpadding="0" cellspacing="0" border="0">
							<tbody>
								<tr>
									<td colspan="3" style="height:17px;"></td>
								</tr>
								<tr>
									<td colspan="3" style="height:60px;">
										<input type="text" id="resultIpt" readonly="" class="displayCss" width="390" value="0" size="17" maxlength="17" style="height:53px;*height:56px;">
									</td>
								</tr>
								<tr class="topRowCss" style="display:none;">
									<td width="117" valign="middle"></td>
									<td id="baseEprsPanel" valign="middle" width="290" style="text-align:right"></td>
									<td width="30"></td>
								</tr>
								<tr>
									<td colspan="3" style="height:40px;">&nbsp;</td>
								</tr>
							</tbody>
						</table>
						<table id="base_table_main" class="calculator_table" align="center" cellpadding="0" cellspacing="0" border="0" style="width:96%;">
							<tbody>
								<tr>
									<td><input type="button" value="存储" onclick="calculator.memory(this,0);" class="baseBtnCommonCss"></td>
									<td><input type="button" value="取存" onclick="calculator.memory(this,1);" class="baseBtnCommonCss"></td>
									<td><input type="button" id="simpleDel" value="退格" onclick="calculator.remove();" class="baseBtnCommonCss baseBtnCss1"></td>
									<td><input type="button" id="simpleClearAllBtn" value="清屏" onclick="calculator.clearAll();" class="baseBtnCommonCss baseBtnCss1" style="color: rgb(255, 255, 255); background-position: 0px -44px; "></td>
								</tr>
								<tr>
									<td><input type="button" value="累存" onclick="calculator.memory(this,-1);" class="baseBtnCommonCss"></td>
									<td><input type="button" value="积存" onclick="calculator.memory(this,-2);" class="baseBtnCommonCss"></td>
									<td><input type="button" value="清存" onclick="calculator.memory(this,2);" class="baseBtnCommonCss"></td>
									<td><input type="button" id="simpleDivi" value="÷" onclick="calculator.input(this,3);" class="baseBtnCommonCss baseBtnCss2"></td>
								</tr>
								<tr>
									<td><input type="button" id="simple7" value="7" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simple8" value="8" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simple9" value="9" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3" style="color: rgb(255, 255, 255); background-position: 0px -44px; "></td>
									<td><input type="button" id="simpleMulti" value="×" onclick="calculator.input(this,2);" class="baseBtnCommonCss baseBtnCss2"></td>
								</tr>
								<tr>
									<td><input type="button" id="simple4" value="4" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simple5" value="5" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simple6" value="6" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simpleSubtr" value="-" onclick="calculator.input(this,1);" class="baseBtnCommonCss baseBtnCss2"></td>
								</tr>
								<tr>
									<td><input type="button" id="simple1" value="1" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simple2" value="2" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3" style="background-position: 0px -44px; color: rgb(255, 255, 255);"></td>
									<td><input type="button" id="simple3" value="3" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simpleAdd" value="+" onclick="calculator.input(this,0);" class="baseBtnCommonCss baseBtnCss2"></td>
								</tr>
								<tr>
									<td><input type="button" id="simple0" value="0" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simpleDot" value="." onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" value="+/-" onclick="calculator.input(this,-3);" class="baseBtnCommonCss baseBtnCss3"></td>
									<td><input type="button" id="simpleEqual" value="=" onclick="calculator.input(this,-2);" class="baseBtnCommonCss baseBtnCss4"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="calculator_tab" id="calculator_tabs">
						<ul>
							<li>基础</li>
							<li class="selTabBottom" onclick="showCalculator(1);">高级</li>
						</ul>
					</div>
				</div>
			</div>
		<div id="calculator_complete" style="display: none; position: relative; ">
				<div id="calculator_complete_cup">
					<div id="calculator_complete_container" style="height: 578px; ">
						<form id="completeFrm" name="calc" style="margin:0px;padding:0px;">
							<div id="complete_button_panel">
								<table id="complete_table_top" class="calculator_table" cellpadding="0" cellspacing="0" border="0" style="table-layout:fixed;">
									<tbody>
										<tr>
											<td colspan="3" style="height:17px;"></td>
										</tr>
										<tr>
											<td colspan="3" style="height:60px;">
												<input type="text" name="display" readonly="" value="0" class="displayCss" size="17" maxlength="17">
											</td>
										</tr>
										<tr class="topRowCss" style="display:none;">
											<td width="117" valign="middle">&nbsp;</td>
											<td id="completeEprsPanel" valign="middle" width="290" style="text-align:right"></td>
											<td width="30"></td>
										</tr>
										<tr>
											<td style="height:40px;*height:47px;" colspan="3">&nbsp;</td>
										</tr>
									</tbody>
								</table>
								<table id="complete_table_rdo" align="center" class="calculator_table" cellpadding="0" cellspacing="0" border="0" style="width:96%;height:30px;">
									<tbody>
										<tr>
											<td><input type="radio" name="carry" id="carry16" onclick="inputChangCarry(16);"><label for="carry16">十六进制</label></td>
											<td><input type="radio" name="carry" id="carry10" onclick="inputChangCarry(10);" checked="checked"><label for="carry10">十进制</label></td>
											<td><input type="radio" name="carry" id="carry8" onclick="inputChangCarry(8);"><label for="carry8">八进制</label></td>
											<td><input type="radio" name="carry" id="carry2" onclick="inputChangCarry(2);"><label for="carry2">二进制</label></td>
											<td width="10%"></td>
											<td><input type="radio" name="angle" id="angleid" checked="checked" value="d" onclick="inputChangAngle(&#39;d&#39;)"><label for="angleid">角度制</label></td>
											<td><input type="radio" name="angle" id="angleid2" value="r" onclick="inputChangAngle(&#39;r&#39;);"><label for="angleid2">弧度制</label></td>
										</tr>
									</tbody>
								</table>
								<table id="complete_table_chk" align="center" class="calculator_table" cellpadding="0" cellspacing="0" border="0" style="width:96%;height:30px;">
									<tbody>
										<tr>
											<td><input type="checkbox" name="shiftf" id="shiftid" onclick="inputshift();"><label for="shiftid" style="color:#FFF;">上档功能</label></td>
											<td><input type="checkbox" name="hypf" id="hypfid" onclick="inputshift();"><label for="hypfid" style="color:#FFF;">双曲函数</label></td>
											<td>
												<div style="float:left;"><input type="text" name="bracket" readonly="" size="3" class="helperBox" value=""></div>
												<div style="float:left;"><input type="text" name="memory" readonly="" size="3" class="helperBox"></div>
												<div style="float:left;"><input type="text" name="operator" readonly="" size="3" class="helperBox" style="width:45px;" id="operatorIpt"></div>
											</td>
											<td><input type="button" id="completeDel" value="退格" onclick="backspace();" class="completeBtnCommonCss"></td>
											<td><input type="button" value="清屏" onclick="document.calc.display.value=0;document.getElementById(&#39;completeEprsPanel&#39;).innerHTML = &#39;&#39;;this.blur();" class="completeBtnCommonCss"></td>
										</tr>
									</tbody>
								</table>
								<table id="complete_table_main" class="calculator_table" align="center" cellpadding="0" cellspacing="0" border="0" style="width:97%;*margin-top:2px;">
									<tbody>
										<tr>
											<td><input type="button" value="存储" onclick="putmemory();" class="completeBtnCommonCss completeBtnCss1"></td>
											<td><input type="button" value="取存" onclick="getmemory();" class="completeBtnCommonCss completeBtnCss1"></td>
											<td><input type="button" value="累存" onclick="addmemory();" class="completeBtnCommonCss completeBtnCss1"></td>
											<td><input type="button" value="积存" onclick="multimemory();" class="completeBtnCommonCss completeBtnCss1"></td>
											<td><input type="button" value="清存" onclick="clearmemory();" class="completeBtnCommonCss completeBtnCss1"></td>
											<td><input type="button" value="全清" onclick="clearall();" class="completeBtnCommonCss"></td>
										</tr>
										<tr>
											<td><input type="button" id="complete7" name="k7" value="7" onclick="inputkey(&#39;7&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="complete8" name="k8" value="8" onclick="inputkey(&#39;8&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="complete9" name="k9" value="9" onclick="inputkey(&#39;9&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="completeDivi" value="÷" onclick="operation(&#39;/&#39;,6);" class="completeBtnCommonCss completeBtnCss3"></td>
											<td><input type="button" value="取余" onclick="operation(&#39;%&#39;,6);" class="completeBtnCommonCss"></td>
											<td><input type="button" value="与" onclick="operation(&#39;&amp;&#39;,3);" class="completeBtnCommonCss"></td>
										</tr>
										<tr>
											<td><input type="button" id="complete4" name="k4" value="4" onclick="inputkey(&#39;4&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="complete5" name="k5" value="5" onclick="inputkey(&#39;5&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="complete6" name="k6" value="6" onclick="inputkey(&#39;6&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="completeMulti" value="×" onclick="operation(&#39;*&#39;,6);" class="completeBtnCommonCss completeBtnCss3"></td>
											<td><input type="button" value="取整" name="floor" onclick="inputfunction(&#39;floor&#39;,&#39;deci&#39;);" class="completeBtnCommonCss"></td>
											<td><input type="button" value="或" onclick="operation(&#39;|&#39;,1);" class="completeBtnCommonCss"></td>
										</tr>
										<tr>
											<td><input type="button" id="complete1" name="k1" value="1" onclick="inputkey(&#39;1&#39;);" class="completeBtnCommonCss completeBtnCss2"></td>
											<td><input type="button" id="complete2" name="k2" value="2" onclick="inputkey(&#39;2&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="complete3" name="k3" value="3" onclick="inputkey(&#39;3&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="completeSubtr" value="-" onclick="operation(&#39;-&#39;,5);" class="completeBtnCommonCss completeBtnCss3"></td>
											<td><input type="button" value="左移" onclick="operation(&#39;&lt;&#39;,4);" class="completeBtnCommonCss"></td>
											<td><input type="button" value="非" onclick="inputfunction(&#39;~&#39;,&#39;~&#39;);" class="completeBtnCommonCss"></td>
										</tr>
										<tr>
											<td><input type="button" id="complete0" name="k0" value="0" onclick="inputkey(&#39;0&#39;);" class="completeBtnCommonCss completeBtnCss2"></td>
											<td><input type="button" value="+/-" onclick="changeSign();" class="completeBtnCommonCss completeBtnCss2"></td>
											<td><input type="button" id="completeDot" name="kp" value="." onclick="inputkey(&#39;.&#39;);" class="completeBtnCommonCss completeBtnCss2" style="color: rgb(255, 255, 255); background: url(&quot;images/e3btn.gif&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
											<td><input type="button" id="completeAdd" value="+" onclick="operation(&#39;+&#39;,5);" class="completeBtnCommonCss completeBtnCss3"></td>
											<td><input type="button" id="completeEqual" value="=" onclick="result();" class="completeBtnCommonCss completeBtnCss4"></td>
											<td><input type="button" value="异或" onclick="operation(&#39;x&#39;,2);" class="completeBtnCommonCss"></td>
										</tr>
										<tr>
											<td><input type="button" name="ka" value="A" onclick="inputkey(&#39;a&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
											<td><input type="button" name="kb" value="B" onclick="inputkey(&#39;b&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
											<td><input type="button" name="kc" value="C" onclick="inputkey(&#39;c&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
											<td><input type="button" name="kd" value="D" onclick="inputkey(&#39;d&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
											<td><input type="button" name="ke" value="E" onclick="inputkey(&#39;e&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
											<td><input type="button" name="kf" value="F" onclick="inputkey(&#39;f&#39;);" class="completeBtnCommonCss" disabled="" style="color: rgb(118, 115, 105); background: url(&quot;images/e3btn-dis.gif&quot;) 0px 0px no-repeat; cursor: default;"></td>
										</tr>
										<tr>
											<td style="height:11px;" colspan="6"></td>
										</tr>
									</tbody>
								</table>
							</div>
							<table id="complete_table_more" class="calculator_table" align="center" cellpadding="0" cellspacing="0" border="0" style="width:90%;*margin-top:2px;">
								<tbody id="moreFn">
									<tr>
										<td><input type="button" name="pi" value="PI" onclick="inputfunction(&#39;pi&#39;,&#39;pi&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
										<td><input type="button" name="e" value="E" onclick="inputfunction(&#39;e&#39;,&#39;e&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
										<td><input type="button" name="bt" value="d.ms" onclick="inputfunction(&#39;dms&#39;,&#39;deg&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
										<td><input type="button" value="(" onclick="addbracket(this);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" value=")" onclick="disbracket(this);" class="completeBtnCommonCss completeBtnCss5"></td>
									</tr>
									<tr>
										<td><input type="button" name="ln" value="ln" onclick="inputfunction(&#39;ln&#39;,&#39;exp&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" name="log" value="log" onclick="inputfunction(&#39;log&#39;,&#39;expdec&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" name="sin" value="sin" onclick="inputtrig(&#39;sin&#39;,&#39;arcsin&#39;,&#39;hypsin&#39;,&#39;ahypsin&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
										<td><input type="button" name="cos" value="cos" onclick="inputtrig(&#39;cos&#39;,&#39;arccos&#39;,&#39;hypcos&#39;,&#39;ahypcos&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
										<td><input type="button" name="tan" value="tan" onclick="inputtrig(&#39;tan&#39;,&#39;arctan&#39;,&#39;hyptan&#39;,&#39;ahyptan&#39;);" class="completeBtnCommonCss completeBtnCss5" style="color: rgb(255, 255, 255); background: url(&quot;images/e5btn.jpg&quot;) 0px -34px no-repeat; cursor: pointer;"></td>
									</tr>
									<tr>
										<td><input type="button" value="n!" onclick="inputfunction(&#39;!&#39;,&#39;!&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" value="1/x" onclick="inputfunction(&#39;recip&#39;,&#39;recip&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" name="sqr" value="x^2" onclick="inputfunction(&#39;sqr&#39;,&#39;sqrt&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" name="cube" value="x^3" onclick="inputfunction(&#39;cube&#39;,&#39;cubt&#39;);" class="completeBtnCommonCss completeBtnCss5"></td>
										<td><input type="button" value="x^y" onclick="operation(&#39;^&#39;,7);" class="completeBtnCommonCss completeBtnCss5"></td>
									</tr>
								</tbody>
							</table>
						</form>
					</div>
					<div class="calculator_tab" id="calculator_tabs2">
						<ul>
							<li class="selTabBottom" onclick="showCalculator(0);">基础</li>
							<li>高级</li>
						</ul>
					</div>
				</div>
			</div>
	</div>
</div>


<script src="${pageContext.request.contextPath }/js/calculator/bdjs_client-1.0.js"></script>
<script src="${pageContext.request.contextPath }/js/calculator/calc.js"></script>
<script>
	var mouseX, mouseY;
        var objX, objY;
        var isDowm = false;
        function mouseDown(obj, e) {
        	var div = document.getElementById("mov");
            obj.style.cursor = "move";
			objX = div.offsetLeft;
			objY = div.offsetTop;
            mouseX = e.clientX;
            mouseY = e.clientY;
            isDowm = true;
        }
        function mouseMove(e) {
            var div = document.getElementById("mov");
            var x = e.clientX;
            var y = e.clientY;
            if (isDowm) {
                div.style.left = parseInt(objX) + parseInt(x) - parseInt(mouseX) + "px";
                div.style.top = parseInt(objY) + parseInt(y) - parseInt(mouseY) + "px";
            }
        }
        function mouseUp(e) {
            if (isDowm) {
                var x = e.clientX;
                var y = e.clientY;
                var div = document.getElementById("mov");
                div.style.left = parseInt(objX) + parseInt(x) - parseInt(mouseX) + "px";
                div.style.top = parseInt(objY) + parseInt(y) - parseInt(mouseY) + "px";
                div.style.cursor = "default";
                isDowm = false;
            }
        }
</script>
