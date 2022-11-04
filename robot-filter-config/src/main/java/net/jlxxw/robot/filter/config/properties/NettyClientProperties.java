package net.jlxxw.robot.filter.config.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chunyang.leng
 * @date 2022-11-04 11:43 AM
 */
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
}
