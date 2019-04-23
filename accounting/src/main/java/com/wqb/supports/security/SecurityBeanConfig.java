
package com.wqb.supports.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * 认证相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 */
@Configuration
public class SecurityBeanConfig {

	/**
	 * 默认密码处理器
	 * @return
	 */
	@Bean(name = "daoPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new Md5PasswordEncoder();
	}
}
