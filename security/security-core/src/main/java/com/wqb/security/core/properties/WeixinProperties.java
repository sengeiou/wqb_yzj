
package com.wqb.security.core.properties;

import com.wqb.security.core.social.support.ConnectProperties;

/**
 * 微信登录配置项
 */
public class WeixinProperties extends ConnectProperties {

	/**
	 * 第三方id，用来决定发起第三方登录的url，默认是 weixin。
	 */
	private String providerId = "weixin";
}
