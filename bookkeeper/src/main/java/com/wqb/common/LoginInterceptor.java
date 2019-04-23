package com.wqb.common;

import com.google.gson.Gson;
import com.wqb.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserDao userDao;

    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //强制清空缓存和强制过期
        //因为缓存的存在会造成重复提交数据的问题，验证码图片不能正确显示的问题，等等。这个时候我们就要禁用页面缓存的功能
        response.setHeader("Cache-Control", "no-store");  //no-store Firefox
        response.setHeader("Pragma", "no-cache");  //no-cache 针对IE
        response.setDateHeader("Expires", 0);
        // ---------------end-----------------
        int serverPort = request.getServerPort();  //8090
        String serverName = request.getServerName();//localhost
        //首先从当前的url中获取访问的路径，如果/system/toLogin 和/system/Login （不需要拦截）
        String pathrequest = request.getRequestURL().toString();
        //http://localhost:8080/wqb/WEB-INF/jsp/system/login.jsp
		/*if(pathrequest.contains("system"))
			return true;
		*/
        String contextPath = request.getContextPath();///wqb
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("userDate");
        if (obj != null) {// 登陆成功---放行
            String loginUser = (String) session.getAttribute("login_phone");
            String my_ssionid = session.getId();
            HashMap<String, HttpSession> mymap = MySessionContext.getMymap();
            int com = 0;
            String other_ssionid = null;
            for (Entry<String, HttpSession> entry : mymap.entrySet()) {
                HttpSession value = entry.getValue();
                String other_phone = (String) value.getAttribute("login_phone");
                String other_id = value.getId();
                if (other_phone != null && other_phone.equals(loginUser)) {
                    if (other_id != null && !other_id.equals(my_ssionid)) {
                        ++com;
                        other_ssionid = other_id;
                        break;
                    }
                }
            }
            if (com > 0 && other_ssionid != null) {
                HttpSession other_session = MySessionContext.getSession(other_ssionid);
                long other_date = (long) other_session.getAttribute("credate");

                long my_date = (long) session.getAttribute("credate");
                ////<0  说明另外一个人最近登陆过  我在哪个人的的前面，我就要被踢下去
                if ((my_date - other_date) < 0) {
                    String other_ip = (String) other_session.getAttribute("loing_ip");
                    MySessionContext.DelSession(session);//踢人--找到并销毁Session

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                    String format = dateFormatter.format(new Date(other_date));
                    String msg = "您的账号于  " + format + " 在另一台电脑登录【" + other_ip + "】,您被踢下线了!";
                    if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        Map<String, String> map = new HashMap<>();
                        map.put("code", "567");
                        map.put("msg", msg);
                        Gson gson = new Gson();
                        //out.append(responseJSONObject.toString());
                        //{"code":"567","msg":"您的身份已过期"}
                        response.getWriter().write(gson.toJson(map));

                    } else {
                        request.setAttribute("errors", msg);
                        //内部资源可以访问/WEB-INF/
                        request.getRequestDispatcher("/WEB-INF/jsp/common/register.jsp").forward(request, response);
                    }

                    //response.sendRedirect(request.getContextPath()+ "/register2.jsp?errors="+URLEncoder.encode(DateUtil.getTime(new Date(other_date)), "utf-8"));

                    return false;
                }

            }
            return true;
        }


        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
            response.setHeader("sessionstatus", "nologin");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("code", "567");
            map.put("msg", "您的身份已过期");
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(map));

        } else {

			/* request.setAttribute("msg", "注册开放时间：9：00-12：00");
             request.getRequestDispatcher("/msg.jsp").forward(request, response);*/

            //  请求转发    跳转到login页面！    内部转发给servlet2(defaultSelvlet)
            //   /system/sessionOver 在 /WEB-INF/jsp/common/register.jsp"

            if ("localhost".equals(serverName) || serverName.contains("192.168.")) {
                //如果是本地 跳转到后台开发人员入口
                response.sendRedirect(request.getContextPath() + "/system/sessionOver");
                //request.setAttribute("errors", "您的身份已过期");
                //request.getRequestDispatcher("/WEB-INF/jsp/common/register2.jsp").forward(request, response);
            } else {
                //session 超时调用后台模板页面  正式入口访问后台
                response.sendRedirect(request.getContextPath() + "/system/sessionOver");
                //request.setAttribute("errors", "您的身份已过期");
                //request.getRequestDispatcher("/WEB-INF/jsp/common/register2.jsp").forward(request, response);
            }
        }

        return false;
    }
}
