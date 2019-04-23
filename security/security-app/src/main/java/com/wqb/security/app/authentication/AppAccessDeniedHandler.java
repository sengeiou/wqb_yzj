package com.wqb.security.app.authentication;

import com.wqb.security.core.support.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 非匿名用户且没有记住我，验证是失败时会走这里
 *
 * @author Shoven
 * @date 2018/11/1 18:15
 * 请求拒绝，没有权限
 */
@Component
public class AppAccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Override
    protected ResponseEntity<Response> enhanceResponse(ResponseEntity<?> result, Exception authException) {
        OAuth2Exception auth2Exception = (OAuth2Exception) result.getBody();

        String message = auth2Exception != null && auth2Exception.getMessage() != null
                ? auth2Exception.getMessage()
                : authException.getMessage();

        Response response = new Response()
                .setSuccess(false)
                .setStatus(result.getStatusCodeValue())
                .setMessage(message);
        return ResponseEntity
                .status(result.getStatusCodeValue())
                .headers(result.getHeaders())
                .body(response);
    }

}
