package com.wqb.controllers;


import com.wqb.commons.vo.Response;
import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.security.app.social.AppSingUpUtils;
import com.wqb.security.core.properties.SecurityProperties;
import com.wqb.services.UserService;
import com.wqb.supports.util.ResponseUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

/**
 * <p>
 * 用户登录信息表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User> {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

	@Autowired
	private AppSingUpUtils appSingUpUtils;

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(User user, Account account) {
        User registerUser = userService.registerUser(user, account);
        return ResponseUtils.ok(registerUser);
    }

    @PostMapping("/register/connect")
    public ResponseEntity register(HttpServletRequest request, User user, Account account) {
        User registerUser = userService.registerUserWithConnection(request, user, account);
        return ResponseUtils.ok(registerUser);
    }

    @GetMapping("/me")
    public Object getCurrentUser()  {
        return ResponseUtils.ok("获取用户信息成功", userService.getCurrentUser());
    }

    /**
     * 获取jwt附加信息（添加了的情况下）
     * @see com.wqb.security.server.TokenJwtEnhancer
     * @param request
     * @return
     */
    private Object getJwtAdditionInfo(HttpServletRequest request) {
        String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
        byte[] bytes = securityProperties.getOauth2().getJwtSigningKey().getBytes(StandardCharsets.UTF_8);
        Claims body = Jwts.parser().setSigningKey(bytes).parseClaimsJws(token).getBody();
        return ResponseUtils.ok(body);
    }
}
