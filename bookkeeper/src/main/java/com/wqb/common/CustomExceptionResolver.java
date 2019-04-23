package com.wqb.common;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object handler, Exception ex) {
        ex.printStackTrace();

        BusinessException customException = null;

        //如果抛出的是系统自定义异常则直接转换
        if (ex instanceof BusinessException) {
            customException = (BusinessException) ex;
        } else {
            //如果抛出的不是系统自定义异常则重新构造一个系统错误异常。
            customException = new BusinessException("系统错误，请与系统管理 员联系！");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", customException.getMessage());
        modelAndView.setViewName("common/fail");

        return modelAndView;
    }

}
