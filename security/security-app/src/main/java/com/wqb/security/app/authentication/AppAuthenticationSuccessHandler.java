
package com.wqb.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqb.security.core.support.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * APP环境下认证成功处理器
 */
@Component
public class AppAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired

    private PasswordEncoder passwordEncoder;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
        String[] clientInfo = new String[2];

        try {
            clientInfo = ClientUtils.getClientInfo(request);
            if (clientInfo == null) {
                ClientUtils.outputAbsentClient(response);
                return;
            }
        } catch (RuntimeException e) {
            ClientUtils.outputErrorInfo(response, HttpStatus.UNAUTHORIZED, e.getMessage());
            return;
        }

		String clientId = clientInfo[0];
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        try {
            ClientUtils.authenticateClient(passwordEncoder, clientDetails, clientInfo);
        } catch (Exception e) {
            ClientUtils.outputBadCredentials(response, e);
        }

		TokenRequest tokenRequest = new TokenRequest(Collections.emptyMap(),
                clientId, clientDetails.getScope(), "custom");

		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        Response tokenResult;

        try {
            OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

            String  message = "refresh_token".equals(request.getParameter("grant_type"))
                    ? "刷新token成功"
                    : "获取token成功";

            tokenResult = new Response<>()
                    .setMessage(message)
                    .setData(token);

        } catch (AuthenticationException e) {
            tokenResult = new Response()
                        .setSuccess(false)
                        .setStatus(HttpStatus.UNAUTHORIZED.value())
                        .setMessage(e.getMessage());

        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResult));
	}



}
