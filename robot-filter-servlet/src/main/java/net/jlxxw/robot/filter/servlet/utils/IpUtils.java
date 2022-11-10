package net.jlxxw.robot.filter.servlet.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 11:49 AM
 */
@Component
public class IpUtils {
    private static final String[] HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
        "X-Real-IP"
    };

    /**
     * 判断ip是否为空，空返回true
     *
     * @param ip
     * @return
     */
    private boolean isEmptyIp(final String ip) {
        return (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip));
    }

    /**
     * 判断ip是否不为空，不为空返回true
     *
     * @param ip
     * @return
     */
    private boolean isNotEmptyIp(final String ip) {
        return !isEmptyIp(ip);
    }

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request HttpServletRequest
     * @return
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = "";
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if (isNotEmptyIp(ip)) {
                break;
            }
        }
        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
        }
        if (isNotEmptyIp(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
    public String getIpAddress(ServletRequest request){
        return getIpAddress((HttpServletRequest) request);
    }

}
