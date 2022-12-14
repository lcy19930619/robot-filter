package net.jlxxw.robot.filter.servlet.context;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author chunyang.leng
 * @date 2022-11-03 2:39 PM
 */
public class RobotServletFilterWebContext {

    private static final ThreadLocal<Boolean> IN_WHITE_LIST = new ThreadLocal<Boolean>();

    private static final ThreadLocal<String> IP = new ThreadLocal<String>();

    private static final ThreadLocal<String> CLIENT_ID = new ThreadLocal<String>();

    private static final ThreadLocal<String> HOST = new ThreadLocal<String>();

    private static final ThreadLocal<String> REFERER = new ThreadLocal<String>();

    private static final ThreadLocal<String> AUTHORIZATION = new ThreadLocal<>();
    private static final ThreadLocal<String> ORIGIN = new ThreadLocal<>();

    public static void setOrigin(String origin) {
        ORIGIN.set(origin);
    }

    public static String getOrigin() {
        return ORIGIN.get();
    }

    public static void setReferer(String referer) {
        REFERER.set(referer);
    }
    public static String getReferer() {
        return REFERER.get();
    }


    public static void setAuthorization(String authorization) {
        AUTHORIZATION.set(authorization);
    }
    public static String getAuthorization() {
        return AUTHORIZATION.get();
    }


    public static boolean inWhiteList() {
        Boolean in = IN_WHITE_LIST.get();
        return ObjectUtils.getIfNull(in, () -> false);
    }

    public static void setInWhiteList(boolean in) {
        IN_WHITE_LIST.set(in);
    }



    public static void setIp(String ip) {
        IP.set(ip);
    }
    public static String getIp() {
        return IP.get();
    }


    public static void setClientId(String clientId) {
        CLIENT_ID.set(clientId);
    }
    public static String getClientId() {
        return CLIENT_ID.get();
    }


    public static void setHost(String host) {
        HOST.set(host);
    }
    public static String getHost() {
        return HOST.get();
    }



    public static void remove() {
        IN_WHITE_LIST.remove();
        IP.remove();
        CLIENT_ID.remove();
        HOST.remove();
        REFERER.remove();
        AUTHORIZATION.remove();
        ORIGIN.remove();
    }
}
