
package com.wqb.supports.security;

import com.wqb.security.core.validate.code.sms.SmsCodeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 蓝创短信发送器
 * @author WQB
 */
@Component
public class LCSmsCodeSender implements SmsCodeSender {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
     * (non-Javadoc)
	 * @see SmsCodeSender#send(java.lang.String, java.lang.String)
	 */
	@Override
	public void send(String mobile, String code) {
		logger.info("向手机 "+mobile+" 发送短信验证码"+code);
	}

}
