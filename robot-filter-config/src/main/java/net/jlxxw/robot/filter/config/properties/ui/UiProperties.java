package net.jlxxw.robot.filter.config.properties.ui;

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

}
