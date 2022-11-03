package net.jlxxw.robot.filter.config.properties;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:31 PM
 */
public class EncryptProperties {

    /**
     * enable encryption
     */
    private boolean enabled = true;

    /**
     * password encryption
     */
    private String password = "2037160912343096965639708424590011416553193428956897480135656374257278867347454410695608942307532325452053895953189550149791562271893617579237042822813647557620";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
