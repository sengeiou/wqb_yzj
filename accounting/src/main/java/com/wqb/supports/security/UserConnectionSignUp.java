
package com.wqb.supports.security;

import com.wqb.domains.User;
import com.wqb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 社交登陆默认注册
 *
 * @author Shoven
 * @since 2019-04-22 17:34
 */
public class UserConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private UserService userService;

	/**
     *  (non-Javadoc)
	 * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
	 */
	@Override
	public String execute(Connection<?> connection) {

//        User user = new User();
//        user.set
        return "223d3c4b909c4df2aa45c8a6af397563";
	}

}
