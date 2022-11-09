package net.jlxxw.robot.filter.config.properties.data.netty;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:43 AM
 */
@ConfigurationProperties("robot.filter.share.netty.client")
public class NettyClientProperties {

    /**
     * retry count ,default is 10
     */
    private int retryMaxCount = 10;

    /**
     * retry delay,default is 3000
     * unit is milliseconds
     */
    private int retryDelay = 3000;


    /**
     * server option properties
     */
    private Map<String,Object> nettyOption = new HashMap<String,Object>();

    /**
     * client ssl
     */
    private NettyClientSSLProperties ssl = new NettyClientSSLProperties();

    public Map<String, Object> getNettyOption() {
        return nettyOption;
    }

    public void setNettyOption(Map<String, Object> nettyOption) {
        this.nettyOption = nettyOption;
    }

    public int getRetryMaxCount() {
        return retryMaxCount;
    }

    public void setRetryMaxCount(int retryMaxCount) {
        this.retryMaxCount = retryMaxCount;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public NettyClientSSLProperties getSsl() {
        return ssl;
    }

    public void setSsl(NettyClientSSLProperties ssl) {
        this.ssl = ssl;
    }
}
