
package com.wqb.security.server;

import com.wqb.security.app.authentication.AppAccessDeniedHandler;
import com.wqb.security.app.authentication.AppOAuth2AuthExceptionEntryPoint;
import com.wqb.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import com.wqb.security.core.authentication.configurer.AuthorizationConfigurerManager;
import com.wqb.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.wqb.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器配置
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer socialSecurityConfig;

    @Autowired
    private AuthorizationConfigurerManager authorizationConfigurerManager;

    @Autowired
    private AppAccessDeniedHandler appAccessDeniedHandler;

    @Autowired
    private AppOAuth2AuthExceptionEntryPoint appOAuth2AuthExceptionEntryPoint;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .authenticationEntryPoint(appOAuth2AuthExceptionEntryPoint)
                .accessDeniedHandler(appAccessDeniedHandler);
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(appOAuth2AuthExceptionEntryPoint)
                .accessDeniedHandler(appAccessDeniedHandler)
                .and()
                .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(socialSecurityConfig)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .csrf().disable();

        authorizationConfigurerManager.config(http.authorizeRequests());
    }
}
