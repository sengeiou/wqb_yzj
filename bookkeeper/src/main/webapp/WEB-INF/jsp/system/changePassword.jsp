<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
	  <meta charset="UTF-8">
	  <meta name="renderer" content="webkit">
	  <meta name="viewport" content="width=device-width, initial-scale=1.0">
	  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	  <meta http-equiv="pragma" content="no-cache">
	  <meta http-equiv="cache-control" content="no-cache">
	  <meta http-equiv="expires" content="0">
	  <meta http-equiv="keywords" content="微企宝">
	  <meta http-equiv="description" content="This is my page">
	  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath }/img/favicon.ico" media="screen">
	  <link rel="stylesheet" href="${pageContext.request.contextPath }/plugins/layui/css/layui.css">
	  <link rel="stylesheet" href="${pageContext.request.contextPath }/css/common.css">
	  <link rel="stylesheet" href="${pageContext.request.contextPath }/css/login.css">
	  <title>修改密码</title>
	  <style>
	    .changePassword{
			border: 1px solid #dadada;
		    width: 580px;
		    margin: 5% auto;
		    padding: 68px 0 68px 34px;
		    border-radius: 5px;
		}
		.changePassword span{
			font-size: 12px;
	    	color: #666;
		}
		.changePassword label{
			width: 80px;
			display: block;
			float: left;
			height: 30px;
			line-height: 30px;
		}
		.changePassword input{
			width: 272px;
			height: 30px;
			padding-left: 5px;
		}
		.changePassword div{
			margin-bottom: 20px;
		}
		.changePassword .right_detail_button{
		    display: block;
		    width: 120px;
		    height: 30px;
		    background: #4381e6;
		    border: 1px solid #f8e6e6;
		    color: #fff;
		    cursor: pointer;
		    font-size: 16px;
		    border-radius: 3px;
		    margin: 30px 0 0 22%;
   			margin-right: 50%;
		}
		.changePassword h1{
			color: #333;
			text-align: center;
		    line-height: 32px;
		    padding-top: 0px;
		    padding-bottom: 30px;
		    margin-top: -20px;
		}
		.changePassword .pwdIntension{
		    border: 1px solid #fff;
		    width: 274px;
		    margin-left: 80px;
		    margin-top: -10px;
		    display: none;
		}
		.changePassword .pwdIntension label{
			height: 20px;
			line-height: 24px;
		}
		.changePassword .pwdIntension div{
			border: 1px solid #c3c3c3;
		    width: 120px;
		    margin-top: 8px;
		    margin-left: 80px;
		    height: 10px;
		    margin-bottom: 0px;
		    background: rgb(238, 238, 238);
		}
		.changePassword .pwdIntension div span{
			width: 39px;
		    height: 10px;
		    display: block;
		    float: left;
		    border-right: 1px solid rgb(238, 238, 238);
	    }
	    .changePassword .pwdIntension div span:nth-last-child(1){
	    	width: 40px;
	    	border-right:none
	    }
	    .changePassword .pwdIntension .intensionName{
	    	float: right;
		    position: relative;
		    right: 50px;
		    margin-top: -16px;
		    font-size: 14px;
	    }
	    .changePassword .newPwdCode{
	    	font-size: 12px;
		    display: inline-block;
		    height: 10px;
		    width: auto;
		    line-height: 15px;
		    margin-left: 80px;
		    color: red;
	    }
	    .changePassword .oldPwdCode,
	    .changePassword .confPwdCode,
	    .changePassword .submitCode{
    	    font-size: 12px;
		    display: block;
		    height: 18px;
		    line-height: 15px;
		    margin-left: 80px;
		    color: red;
		    width: 274px;
	    }
	    .changePassword .submitCode{
	        margin-top: -8px;
    	}
    	.newPwdCode .layui-icon,
    	.submitCode .layui-icon,
    	.confPwdCode .layui-icon,
    	.oldPwdCode .layui-icon{
	    	width: 15px;
		    font-size: 14px !important;
		    line-height: 15px;
		    color: red;
	    }
	  </style>
	</head>
    <body>
	    <form action="/managerlogin/modifyPwd.do" method="post" onsubmit="return isSubmit();" autocomplete="off">
	    	<div class="changePassword">
	    		<h1>修改密码</h1>
		        <input type="hidden" name="userId" value="${userId }"/>
		        <input type="hidden" name="level" value="${level }"/>
		        <div class="oldPwd">
		        	<label>旧密码：</label>
		        	<input type="password" maxlength="16" name="oldPwd" id="oldPwd" placeholder="请输入原始密码"/>
		        	<label class="oldPwdCode"></label>
		        </div>

		     	<div class="nPwd">
		        	<label>新密码：</label>
		        	<input type="password" maxlength="16" name="newPwd" id="nPwd" placeholder="请输入新密码" onKeyUp=pwStrength(this.value) onBlur=pwStrength(this.value)/>
		        	<span>6-16位,可由数字、大小写字母组成</span>
		        	<label class="newPwdCode"></label>
			    </div>

		        <div class="pwdIntension">
		        	<label>密码强度：</label>
		        	<div>
		        		<span></span>
		        		<span></span>
		        		<span></span>
		        	</div>
		        	<span class="intensionName"></span>
		        </div>

		        <div class="confPwd">
		        	<label>确认密码：</label>
		        	<input type="password" maxlength="16" name="confPwd" id="confPwd" placeholder="请再次确认密码"/>
		        	<label class="confPwdCode"></label>
		        </div>
		        <label class="submitCode"></label>
		        <input style="" type="submit" value="确定" class="right_detail_button"/>
		    </div>
		</form>
    	<script src="${pageContext.request.contextPath }/plugins/jquery-1.12.4.min.js"></script>
		<script src="${pageContext.request.contextPath }/plugins/layui/layui.all.js"></script>
		<script src="${pageContext.request.contextPath }/js/common.js?v=${timeStamp }"></script>
 	</body>
</html>
<script type="text/javascript">
    var info ="<label class='layui-icon'>&#xe702;</label>您设定的密码超长度应该在6-16个字符之间!";
    var info2 = "<label class='layui-icon'>&#xe702;</label>两次密码不一致，请重新输入!";
    var info3 = "<label class='layui-icon'>&#xe702;</label>请输入密码!";
    var info4 = "<label class='layui-icon'>&#xe702;</label>请输入新密码!";
    var info5 = "<label class='layui-icon'>&#xe702;</label>请再次输入密码!";
    var info6 = "<label class='layui-icon'>&#xe702;</label>您输入的密码格式不正确!";
    function validate_Password(e,v){//修改密码jQuery验证
        var i = e;
        if(e==null) i=v.index("input");
        //如果$('input:password').val()==''
        if(''==v.val()){
            //判断是第几个input：password的val值
            //第一个（请输入密码）
            if(i==0){
                $(".oldPwdCode").html(info3);
            }
            //第二个（请输入新密码）
            if(i==1){
                $(".newPwdCode").html(info4);
            }
            //第三个（请再次输入密码）
            if(i==2){
                $(".confPwdCode").html(info5);
            }
            return false;
        }else{
            /* alert('您设定的密码超长度应该在5-16个字符之间!'); */
            if(v.val().length<5 || v.val().length>16){
                if(i==0){
                    $(".submitCode").html(info6);
                }
                if(i==1 || i==2){
                    $(".submitCode").html(info);
                }
                return false;
            }
            //判断密码是否一致
            if(i==2){
                var p1 = $('#nPwd').val();                      //第一个密码输入
                var p2 = v.val();                               //第二个密码输入
                if(p1 != p2){
                    $(".submitCode").html(info2);
                    return false;
                }
            }
            return true;
        }
    };
    function isSubmit(){
        var flag = false;
        $("input:password").each(function(e, obj){
            flag = validate_Password(e, $(this));
            if(flag == false){
                return false;
            }
        });
        event.preventDefault();//阻止form表单跳转
        if(flag == true){
	        var params = {
	            oldPassword: $("#oldPwd").val(),
	            newPassword: $("#nPwd").val()
	        };
        	postRequest('${pageContext.request.contextPath }/system/savePassword', params, function(res) {
        		if(res.code == "0"){
        			layer.msg("保存成功！");
        			window.parent.document.getElementById("signOut").click();
        		}else{
        			$(".submitCode").html(res.msg);
        		}
        	})
        }
        return flag;
    };

    //判断输入密码的类型
	function CharMode(iN){
		if (iN>=48 && iN <=57) //数字
			return 1;
		if (iN>=65 && iN <=90) //大写
			return 2;
		if (iN>=97 && iN <=122) //小写
			return 4;
		else
			return 8;
	}

    //计算密码模式
	function bitTotal(num){
		modes=0;
		for (i=0;i<4;i++){
			if (num & 1) modes++;
			num>>>=1;
		}
		return modes;
	}

    function checkStrong(sPW){
    	if (sPW.length<6)
			return 0; //密码太短，不检测级别
		Modes=0;
		for (i=0;i<sPW.length;i++){
			//密码模式
			Modes|=CharMode(sPW.charCodeAt(i));
		}
		return bitTotal(Modes);
    }

    function pwStrength(pwd){
    	if(pwd.length < 6){
    		$(".changePassword .pwdIntension").hide();
    	}else{
    		$(".changePassword .pwdIntension").show();
    	}
    	Dfault_color="#eeeeee";	//默认颜色
		L_color="#FF0000";		//低强度的颜色，且只显示在最左边的单元格中
		M_color="#FF9900";		//中等强度的颜色，且只显示在左边两个单元格中
		H_color="#33CC00";		//高强度的颜色，三个单元格都显示
		if (pwd==null||pwd==''){
			Lcolor=Mcolor=Hcolor=Dfault_color;
		}else{
			S_level=checkStrong(pwd);
			switch(S_level) {
				case 0:
					Lcolor=Mcolor=Hcolor=Dfault_color;
					break;
				case 1:
					Lcolor=L_color;
					Mcolor=Hcolor=Dfault_color;
					break;
				case 2:
					Lcolor=Mcolor=M_color;
					Hcolor=Dfault_color;
					break;
				default:
					Lcolor=Mcolor=Hcolor=H_color;
			}
		}
		$(".pwdIntension div span:eq(0)").css({"background":Lcolor});
		$(".pwdIntension div span:eq(1)").css({"background":Mcolor});
		$(".pwdIntension div span:eq(2)").css({"background":Hcolor});
		if($(".pwdIntension div span:eq(0)").css("background-color") == 'rgb(255, 0, 0)'){
			$(".intensionName").text("低").css({"color":"rgb(255, 0, 0)"});
		}
		if($(".pwdIntension div span:eq(1)").css("background-color") == 'rgb(255, 153, 0)'){
			$(".intensionName").text("中").css({"color":"rgb(255, 153, 0)"});
		}
		if($(".pwdIntension div span:eq(2)").css("background-color") == 'rgb(51, 204, 0)'){
			$(".intensionName").text("高").css({"color":"rgb(51, 204, 0)"});
		}
		return;
    }
    //原始密码
    $("#oldPwd").focus(function(){
    	$(".oldPwdCode").html("");
    });
    $("#oldPwd").blur(function(){
    	if($(this).val().length == ""){
    		$(".oldPwdCode").html("<label class='layui-icon'>&#xe702;</label>请输入原始密码!");
    	}else if($(this).val().length < 6){
    		$(".oldPwdCode").html(info6);
    	}
    });
    //新密码
    $("#nPwd").focus(function(){
    	$(".newPwdCode").html("");
    });
    $("#nPwd").blur(function(){
    	var oldPwd = $("#oldPwd").val();
    	var confPwd = $("#confPwd").val();
    	var regu = "^[0-9a-zA-Z]{6,16}$";
		var re = new RegExp(regu);
    	if(oldPwd == ""){
    		$(".oldPwdCode").html("<label class='layui-icon'>&#xe702;</label>请输入原始密码!");
    		return false;
    	}
    	if(oldPwd == $(this).val()){
			$(".submitCode").html("<label class='layui-icon'>&#xe702;</label>新密码与旧密码相同!");
			return false;
		}
    	if($(this).val().length == ""){
    		$(".newPwdCode").html("<label class='layui-icon'>&#xe702;</label>请输入新密码!");
    		return false;
    	}else if($(this).val().length < 6){
    		$(".newPwdCode").html(info6);
    		return false;
    	}
    	if(confPwd != ""){
    		if(confPwd != $(this).val()){
    			$(".submitCode").html(info2);
    			return false;
    		}else{
    			$(".submitCode").html("");
    		}
    	}
    	if (re.test($(this).val())) {
			return true;
		}else{
			$(".submitCode").html("<label class='layui-icon'>&#xe702;</label>密码格式不正确，密码由数字、大小写字母组成");
			return false;
		}
    });
    //确认密码
    $("#confPwd").focus(function(){
    	$(".confPwdCode").html("");
    });
    $("#confPwd").blur(function(){
    	var nPwd = $("#nPwd").val();
    	var oldPwd = $("#oldPwd").val();
    	if(oldPwd == ""){
    		$(".oldPwdCode").html("<label class='layui-icon'>&#xe702;</label>请输入原始密码!");
    		return false;
    	}else if(nPwd == ""){
    		$(".newPwdCode").html("<label class='layui-icon'>&#xe702;</label>请输入新密码!");
    		return false;
    	}else if($(this).val() == ""){
    		$(".confPwdCode").html("<label class='layui-icon'>&#xe702;</label>请再次输入密码!");
    		return false;
    	}else if($(this).val().length < 6){
    		$(".confPwdCode").html(info6);
    		return false;
    	}
    	if($(this).val() != nPwd){
    		$(".submitCode").html(info2);
    	}else{
    		$(".submitCode").html("");
    	}
    });
</script>
