package net.jlxxw.robot.filter.config.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:42 AM
 */
public class NettyServerProperties {
    /**
     * server listener port,default is 31697
     */
    private int port = 31697;

    /**
     * server max connect
     */
    private int serverMaxConnections = 1024;

    /**
     * server option properties
     */
    private Map<String,Object> nettyOption = new HashMap<String,Object>();

    public int getServerMaxConnections() {
        return serverMaxConnections;
    }

    public void setServerMaxConnections(int serverMaxConnections) {
        this.serverMaxConnections = serverMaxConnections;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, Object> getNettyOption() {
        return nettyOption;
    }

    public void setNettyOption(Map<String, Object> nettyOption) {
        this.nettyOption = nettyOption;
    }
}
