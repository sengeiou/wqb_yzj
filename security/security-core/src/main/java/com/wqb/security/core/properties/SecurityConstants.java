
package com.wqb.security.core.properties;


public interface SecurityConstants {
	/**
	 * 当请求需要身份认证时，默认跳转的url
	 */
	String DEFAULT_LOGIN_URL = "/login";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/login/form";
	/**
	 * 默认的手机验证码登录请求处理url
	 */
	String DEFAULT_TOKEN_PROCESSING_URL_MOBILE = "/oauth/mobile/token";
	/**
	 * 默认的OPENID登录请求处理url
	 */
	String DEFAULT_TOKEN_PROCESSING_URL_OPENID = "/oauth/connect/token";
    /**
     * 社交登陆需要注册时获取用户信息的处理url
     */
    String DEFAULT_CURRENT_SOCIAL_USER_INFO_URL = "/social/user";
    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
	/**
	 * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
	/**
	 * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
	/**
	 * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
	/**
	 * openid参数名
	 */
	String DEFAULT_PARAMETER_NAME_OPENID = "openId";
	/**
	 * providerId参数名
	 */
	String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";
    /**
     * 默认登录页面
     */
    String DEFAULT_SIGN_IN_PAGE_URL = "/signIn.html";
    /**
     * 默认注册页面
     */
    String DEFAULT_SIGN_UP_PAGE_URL = "/signUp.html";
	/**
	 * session失效默认的跳转地址
	 */
	String DEFAULT_SESSION_INVALID_URL = "/session-invalid.html";
}
