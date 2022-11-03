package net.jlxxw.robot.filter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:10 PM
 */
public class RejectEvent extends ApplicationEvent {

    private final String ip;
    /**
     *
     * @param ip reject ip
     */
    public RejectEvent(String ip) {
        super(ip);
        this.ip = ip;
    }
    public String getIp() {
        return ip;
    }
}
