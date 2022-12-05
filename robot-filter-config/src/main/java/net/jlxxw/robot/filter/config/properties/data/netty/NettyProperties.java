package net.jlxxw.robot.filter.config.properties.data.netty;

import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:43 AM
 */
@Configuration
@ConfigurationProperties("robot.filter.share.netty")
public class NettyProperties {

    /**
     * service ip list
     * only ip
     */
    private Set<String> serviceList = new HashSet<String>();
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

    public Set<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(Set<String> serviceList) {
        this.serviceList = serviceList;
    }
}
