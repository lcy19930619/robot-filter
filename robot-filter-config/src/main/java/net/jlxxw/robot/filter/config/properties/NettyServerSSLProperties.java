package net.jlxxw.robot.filter.config.properties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 1:07 PM
 */
public class NettyServerSSLProperties {

    /**
     * enable ssl
     */
    boolean enabled = false;

    /**
     * ca-cert.pem
     */
    private String caCert;

    /**
     * server-key.pem
     */
    private String serverKey;
    /**
     * server-cert.pem
     */
    private String serverCert;

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


    public void setServerCert(String serverCert) {
        this.serverCert = serverCert;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getServerCert() {
        return serverCert;
    }
}
