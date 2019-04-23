package com.wqb.controller;

import com.wqb.common.Constrants;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class BaseController {

    protected String message = "";
    protected boolean result = false;
    protected boolean errorFlags = false;

    @Autowired
    private UserService userService;


    /**
     * @Function: backView
     * @Description: 返回结果类型
     * @param:参数描述
     * @return：返回结果描述
     * @throws：异常描述
     */
    public ModelAndView backView(String view, Map<String, Object> map) {
        ModelAndView v = new ModelAndView(view);
        v.addAllObjects(map);
        return v;
    }

    protected ModelAndView reView(String string, ModelAndView list) {
        list.addObject(Constrants.MESSAGE_TIP_FLAGS, true);
        list.addObject(Constrants.MESSAGE_TIP, message);
        return list;
    }

    public ModelAndView messageViewNew(String message) {
        return new ModelAndView("error/message").addObject(Constrants.MESSAGE_TIP, message);
    }

    /**
     * @Function: messageView
     * @Description: 信息提示视图
     * @param:参数描述
     * @return：返回结果描述
     * @throws：异常描述 Modification History: Date Author Version Description
     * ---------------------------------------------------------*
     */
    public String messageView(String message) {
        getRequest().setAttribute(Constrants.MESSAGE_TIP, message);
        return "error/message";
    }

    /**
     * @Function: successView
     * @Description: 返回信息提示视图
     * @param:参数描述
     * @return：返回结果描述
     * @throws：异常描述 Modification History: Date Author Version Description
     * ---------------------------------------------------------*
     */
    public ModelAndView returnView(String message, String view) {
        return new ModelAndView(view).addObject(Constrants.MESSAGE_TIP_FLAGS, true).addObject(Constrants.MESSAGE_TIP,
                message);
    }

    /**
     * 得到request对象
     */
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    //获取用户信息
    @SuppressWarnings("unchecked")
    public User getUser() {
        return userService.getCurrentUser();
    }

    //获取账套信息
    @SuppressWarnings("unchecked")
    public Account getAccount() {
        return userService.getCurrentAccount(getUser());
    }

    //获取会计期间
    @SuppressWarnings("unchecked")
    public String getUserDate() {
        return getAccount().getUseLastPeriod();
    }
}
