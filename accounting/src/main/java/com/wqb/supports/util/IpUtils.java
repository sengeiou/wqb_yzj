package com.wqb.supports.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ip工具
 *
 * @author Shoven
 * @date 2016年8月12日 下午2:43:34
 */
@Component
public class IpUtils {

    @Autowired
    private HttpServletRequest request;

    public static String getAddress(String ip) {
        if ("127.0.0.1".equals(ip)) {
            return "内网IP-内网IP-内网IP";
        }

        String responseStr;
        Map responseMap;
        Map addressMap;
        try {
            responseStr = httpRequest("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            if(responseStr == null){
                return null;
            }
            responseMap = new ObjectMapper().readValue(responseStr, Map.class);
            if (0 != Integer.parseInt(responseMap.get("code").toString())) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        addressMap = (Map) responseMap.get("data");
        LinkedHashMap<String, Object> tempMap = new LinkedHashMap<>();

        // 国家
        tempMap.put("country", addressMap.get("country"));
        // 地区
        tempMap.put("area", addressMap.get("area"));
        // 省份
        tempMap.put("region", addressMap.get("region"));
        // 市区
        tempMap.put("city", addressMap.get("city"));
        // 地区
        tempMap.put("county", addressMap.get("county"));
        // ISP公司
        tempMap.put("isp", addressMap.get("isp"));

        StringBuilder address = new StringBuilder();
        for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
            if (entry.getValue() != null && !"".equals(entry.getValue().toString())
                    &&!"XX".equals(entry.getValue().toString().toUpperCase())) {
                address.append(entry.getValue()).append("-");
            }
        }
        String s = address.toString();

        if (s.length() == 0) {
            return null;
        }
        return s.substring(0,  s.length() - 1);
    }


    /**
     * @param urlStr   请求的地址
     * @return
     */
    private static String httpRequest(String urlStr) {
        try {
            return HttpClientUtils.get(urlStr, HttpClientUtils.commonHeaders(), Collections.emptyMap());
        } catch (IOException e) {
            return null;
        }
    }


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

            if ("127.0.0.1".equals(ip)) {
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    return null;
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

}
