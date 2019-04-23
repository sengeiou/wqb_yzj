package com.wqb.security.app.authentication;

import com.wqb.security.core.support.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author Shoven
 * @date 2018/11/2 10:48
 */
@Component
public class AppOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    private static final String UNAUTHORIZED = "Full authentication is required to access this resource";

    @Override
    protected ResponseEntity<Response> enhanceResponse(ResponseEntity<?> response, Exception exception) {
        String errorMessage;
        if (exception != null) {
            Throwable cause = exception.getCause();
            if (cause instanceof InvalidTokenException) {
                errorMessage = cause.getMessage();
            } else {
                errorMessage = exception.getMessage();
            }

            if (UNAUTHORIZED.equals(errorMessage)) {
                errorMessage = "需要通过身份认证才能访问该资源";
            }
        } else {
            errorMessage = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }

        Response newResponse = new Response()
                .setSuccess(false)
                .setMessage(errorMessage)
                .setStatus(response.getStatusCodeValue());
        return ResponseEntity
                .status(response.getStatusCodeValue())
                .headers(response.getHeaders())
                .body(newResponse);
    }

}
