package net.jlxxw.robot.filter.config.properties.data.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:43 AM
 */
@ConfigurationProperties("robot.filter.share.netty")
public class NettyProperties {
    /**
     * netty client
     */
    private NettyClientProperties client = new NettyClientProperties();
    /**
     * netty server
     */
    private NettyServerProperties server = new NettyServerProperties();

    public NettyClientProperties getClient() {
        return client;
    }

    public void setClient(NettyClientProperties client) {
        this.client = client;
    }

    public NettyServerProperties getServer() {
        return server;
    }

    public void setServer(NettyServerProperties server) {
        this.server = server;
    }
}
