
package com.wqb.supports.security;

import com.wqb.domains.User;
import com.wqb.security.core.validate.code.MobileUserDetailsService;
import com.wqb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * security oauth2和社交登陆用户服务
 *
 * @author Shoven
 * @since 2019-04-22 17:35
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService, MobileUserDetailsService {

	@Autowired
    private UserService userService;


    /**
     * 用户名
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User condition = new User();
        condition.setLoginUser(username);
        return buildUserDetails(selectUser(condition));
	}

    /**
     * 用户id
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User condition = new User();
        condition.setUserID(userId);
		return buildUserDetails(selectUser(condition));
	}

    /**
     * 手机号
     *
     * @param mobile
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByMobileNumber(String mobile) throws UsernameNotFoundException {
        User condition = new User();
        condition.setLoginUser(mobile);
        User user = userService.getOne(condition);
        if (user == null) {
            throw new UsernameNotFoundException("无效手机号码");
        }
        return buildUserDetails(user);
    }

    private User selectUser(User condition) {
        User user = userService.getOne(condition);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        return user;
    }

	private SocialUserDetails buildUserDetails(User user) {
        boolean enable = Objects.equals(user.getState(), 1);
        return new SocialUser(user.getLoginUser(), user.getPasword(),
                enable, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));

	}
}
