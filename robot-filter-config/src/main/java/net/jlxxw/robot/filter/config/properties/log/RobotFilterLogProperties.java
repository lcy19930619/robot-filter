package net.jlxxw.robot.filter.config.properties.log;

import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-03 10:27 AM
 */
@Configuration
@ConfigurationProperties("robot.filter.log")
public class RobotFilterLogProperties {

    /**
     * enable filter log
     */
    private boolean enabled = true;

    /**
     * logger level
     */
    private Level level = Level.INFO;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
