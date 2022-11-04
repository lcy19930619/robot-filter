package net.jlxxw.robot.filter.config.properties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 1:07 PM
 */
public class NettyClientSSLProperties {

    /**
     * enable ssl
     */
    boolean enabled = false;

    /**
     * ca-cert.pem
     */
    private String caCert;

    /**
     * client-key.pem
     */
    private String clientKey;
    /**
     * client-cert.pem
     */
    private String clientCert;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCaCert() {
        return caCert;
    }

    public void setCaCert(String caCert) {
        this.caCert = caCert;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientCert() {
        return clientCert;
    }

    public void setClientCert(String clientCert) {
        this.clientCert = clientCert;
    }
}
