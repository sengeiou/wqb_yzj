package com.wqb.common;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class MySessionContext {
    private static HashMap<String, HttpSession> mymap = new HashMap<String, HttpSession>();

    public static synchronized void AddSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    public static synchronized void DelSession(HttpSession session) {
        if (session != null) {
            HttpSession session2 = mymap.remove(session.getId());//移出session
            if (session2 != null) {
                session2.invalidate();//将从sessionContext中移出的Session失效 --相当于清除当前Session对应的登录用户
            }
        }
    }

    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null)
            return null;
        return (HttpSession) mymap.get(session_id);
    }

    public static HashMap<String, HttpSession> getMymap() {
        return mymap;
    }


}
