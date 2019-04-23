package com.wqb.supports.exceptions;

import com.wqb.commons.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shoven
 * @since 2019-04-22 20:03
 */
@Controller
public class WqbErrorAttributes extends BasicErrorController {


    public WqbErrorAttributes(ErrorAttributes errorAttributes,
                              ServerProperties serverProperties,
                              List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    /**
     * 错误处理方法
     *
     * @param request
     * @return
     */
    @Override
    @RequestMapping
    public ResponseEntity error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);

        Response response = new Response()
                .setSuccess(false)
                .setStatus(status.value())
                .setMessage(String.valueOf(body.get("message")));

        return new ResponseEntity<>(response, status);
    }
}
