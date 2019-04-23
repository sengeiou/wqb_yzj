
package com.wqb.security.core.validate.code.sms;

import com.wqb.security.core.properties.SecurityConstants;
import com.wqb.security.core.validate.code.ValidateCode;
import com.wqb.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 短信验证码处理器
 */
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

	/**
	 * 短信验证码发送器
	 */
	@Autowired
	private SmsCodeSender smsCodeSender;

	@Override
	protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
		String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
		String mobile = request.getRequest().getParameter(paramName);
		smsCodeSender.send(mobile, validateCode.getCode());
        long second = validateCode.getExpireTime().toEpochSecond(ZoneOffset.of("+8")) -
                LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

        responseMessage(request, second + " 后秒过期", HttpStatus.OK.value());
	}

}
