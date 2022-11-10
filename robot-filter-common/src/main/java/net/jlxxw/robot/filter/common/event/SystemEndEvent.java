package net.jlxxw.robot.filter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author chunyang.leng
 * @date 2022-11-10 1:19 PM
 */
public class SystemEndEvent extends ApplicationEvent {


    private final String ip;

    private final int port;

    public SystemEndEvent(String ip, int port) {
        super(ip);
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
