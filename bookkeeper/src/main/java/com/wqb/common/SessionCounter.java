package com.wqb.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

//session 监听器
public class SessionCounter implements HttpSessionListener {

    private static int activeSessions = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        MySessionContext.AddSession(se.getSession());
        activeSessions++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (activeSessions > 0) {
            activeSessions--;
        }
        MySessionContext.DelSession(se.getSession());
    }

    public static int getActiveSessions() {
        return activeSessions;
    }

}
