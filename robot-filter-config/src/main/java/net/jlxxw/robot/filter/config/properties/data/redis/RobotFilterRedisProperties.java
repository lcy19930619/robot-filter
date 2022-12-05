package net.jlxxw.robot.filter.config.properties.data.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-12-05 12:15 PM
 */
@Configuration
@ConfigurationProperties("robot.filter.share.redis")
public class RobotFilterRedisProperties {

    /**
     * redis key prefix
     */
    private String prefix = "robot:filter:";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
