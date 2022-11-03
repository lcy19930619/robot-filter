package net.jlxxw.robot.filter.config.properties;

/**
 * <b>no support refresh<b/>
 * @author chunyang.leng
 * @date 2022-11-03 5:58 PM
 */
public class DataShareProperties {

    /**
     * server listener port,default is 31697
     */
    private int port = 31697;

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
     * server max connect
     */
    private int serverMaxConnections = 1024;

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
