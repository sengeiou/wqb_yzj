package com.wqb.common;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 暂时没用到
 *
 * @author zhushuyuan
 */
@SuppressWarnings("serial")
public class MyDispatcherServlet extends DispatcherServlet {

    @Override
    protected ModelAndView processHandlerException(HttpServletRequest request,
                                                   HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return new ModelAndView("/errors/405.jsp");
        } else {
            return super
                    .processHandlerException(request, response, handler, ex);
        }
    }
}
