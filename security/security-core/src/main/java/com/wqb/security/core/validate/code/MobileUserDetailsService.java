package com.wqb.security.core.validate.code;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Shoven
 * @since 2019-04-23 15:51
 */
public interface MobileUserDetailsService extends UserDetailsService {


    /**
     * 根据手机号登陆
     *
     * @param mobile
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByMobileNumber(String mobile) throws UsernameNotFoundException;
}
