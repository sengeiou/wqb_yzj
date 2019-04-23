package com.wqb.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 路径工具类
 *
 * @author
 */
public class PathUtil {

    /**
     * 图片访问路径
     *
     * @param pathType     图片类型 visit-访问；save-保存
     * @param pathCategory 图片类别，如：话题图片-topic、话题回复图片-reply、商家图片
     * @return
     */
    public static String getPicturePath(String pathType, String pathCategory) {
        String strResult = "";
        StringBuffer strBuf = new StringBuffer();
        if ("visit".equals(pathType)) {
        } else if ("save".equals(pathType)) {
            String projectPath = getPorjectPath().replaceAll("\\\\", "/");
            projectPath = splitString(projectPath, "bin/");

            strBuf.append(projectPath);
            strBuf.append("webapps/ROOT/");
        }

        strResult = strBuf.toString();

        return strResult;
    }

    private static String splitString(String str, String param) {
        String result = str;

        if (str.contains(param)) {
            int start = str.indexOf(param);
            result = str.substring(0, start);
        }

        return result;
    }

    /*
     * 获取classpath1
     */
    public static String getClasspath() {
        String path = (String.valueOf(Thread.currentThread()
                .getContextClassLoader().getResource("")) + "../../")
                .replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    /*
     * 获取classpath2
     */
    public static String getClassResources() {
        String path = (String.valueOf(Thread.currentThread()
                .getContextClassLoader().getResource("")))
                .replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    public static String getRootPath() {
        String path = (String.valueOf(Thread.currentThread()
                .getContextClassLoader().getResource("")))
                .replaceAll("file:/", "").replaceAll("%20", " ").trim();
        int separatorIndex = path.lastIndexOf("WEB-INF");// WEB-INF
        path = path.substring(0, separatorIndex - 1);// 截取路径到项目名了eg:
        // C:/../../Proj
        int separatorIndex1 = path.lastIndexOf("/");
        path = path.substring(0, separatorIndex1 + 1);// 截取掉项目名 eg: C:/../..
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    public static String getWebRootPath() {
        String path = (String.valueOf(Thread.currentThread()
                .getContextClassLoader().getResource("")))
                .replaceAll("file:/", "").replaceAll("%20", " ").trim();
        int separatorIndex = path.lastIndexOf("WEB-INF");// WEB-INF
        path = path.substring(0, separatorIndex - 1);// 截取路径到项目名了eg:
        // C:/../../Proj

        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    public static String PathAddress() {
        String strResult = "";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        StringBuffer strBuf = new StringBuffer();

        strBuf.append(request.getScheme() + "://");
        strBuf.append(request.getServerName() + ":");
        strBuf.append(request.getServerPort() + "");

        strBuf.append(request.getContextPath() + "/");

        strResult = strBuf.toString();// +"ss/";//加入项目的名称

        return strResult;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getIp() {
        String ip = "";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
            // System.out.println("本机的ip=" + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }

    public static String getPorjectPath() {
        String nowpath = "";
        nowpath = System.getProperty("user.dir") + "/";

        return nowpath;
    }
}
