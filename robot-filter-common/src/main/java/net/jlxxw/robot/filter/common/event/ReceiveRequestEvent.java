package net.jlxxw.robot.filter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * receive request event
 * @author chunyang.leng
 * @date 2022-11-03 3:10 PM
 */
public class ReceiveRequestEvent extends ApplicationEvent {
    private final String ip;

    private final String host;

    private final String clientId;

    /**
     * rule name
     */
    private final String ruleName;


    /**
     * @param ip       client ip
     * @param host     request host
     * @param clientId request client
     * @param ruleName
     */
    public ReceiveRequestEvent(String ip, String host, String clientId,String ruleName) {
        super(ip);
        this.ip = ip;
        this.host = host;
        this.clientId = clientId;
        this.ruleName = ruleName;
    }

    public String getIp() {
        return ip;
    }

    public String getHost() {
        return host;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRuleName() {
        return ruleName;
    }
}
