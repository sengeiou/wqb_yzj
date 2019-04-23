
package com.wqb.security.core.authentication.configurer;

import com.wqb.security.core.properties.SecurityConstants;
import com.wqb.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 核心模块的授权配置提供器，安全模块涉及的url的授权配置在这里。
 */
@Component
@Order(Integer.MIN_VALUE)
public class BaseAuthorizationConfigProvider implements AuthorizationConfigProvider{

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.antMatchers(
                "/error",
		        SecurityConstants.DEFAULT_LOGIN_URL,
				SecurityConstants.DEFAULT_TOKEN_PROCESSING_URL_MOBILE,
				SecurityConstants.DEFAULT_TOKEN_PROCESSING_URL_OPENID,
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
				SecurityConstants.DEFAULT_CURRENT_SOCIAL_USER_INFO_URL,
				securityProperties.getBrowser().getSignInPage(),
				securityProperties.getBrowser().getSignUpUrl(),
				securityProperties.getBrowser().getSession().getSessionInvalidUrl()).permitAll();

        String signOutUrl = securityProperties.getBrowser().getSignOutUrl();
        if (StringUtils.isNotBlank(signOutUrl)) {
			config.antMatchers(signOutUrl).permitAll();
		}
		return false;
	}

}
