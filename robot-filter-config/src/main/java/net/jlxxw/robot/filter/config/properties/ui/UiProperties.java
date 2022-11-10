package net.jlxxw.robot.filter.config.properties.ui;

import java.util.Collections;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-05 10:57 PM
 */
@Configuration
@ConfigurationProperties("robot.filter.ui")
public class UiProperties {

    private String loginName = "admin";

    private String password = "robot.filter.ui";

    /**
     * ip or ip range
     */
    private Set<String> allowIpList = Collections.singleton("127.0.0.1");

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAllowIpList() {
        return allowIpList;
    }

    public void setAllowIpList(Set<String> allowIpList) {
        this.allowIpList = allowIpList;
    }
}
