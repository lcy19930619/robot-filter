package net.jlxxw.robot.filter.config.properties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:43 AM
 */
public class NettyProperties {
    private NettyClientProperties client = new NettyClientProperties();
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
