package com.wqb.security.app.social;

import com.wqb.security.core.properties.SecurityConstants;
import com.wqb.security.core.social.support.SocialConfigurer;
import com.wqb.security.core.social.support.SocialConfigurerProcessor;
import org.springframework.stereotype.Component;

/**
 * APP的社交配置器的处理器
 *
 * @author Shoven
 * @since 2019-04-20 15:37
 */
@Component
public class AppSocialConfigurerProcessor implements SocialConfigurerProcessor {

    /**
     *  假如没有配置无感知处理程序connectionSignUp，需要引导用户进行注册或绑定
     *  在浏览器环境下时第一次社交登录时会跳转配置的注册页面
     *  app环境下跳到该接口存储用户信息备用，让app去跳转到注册页再来拿这个信息
     *
     * @param configurer
     */
    @Override
    public void process(SocialConfigurer configurer) {
        configurer.signupUrl(SecurityConstants.DEFAULT_CURRENT_SOCIAL_USER_INFO_URL);
    }
}
