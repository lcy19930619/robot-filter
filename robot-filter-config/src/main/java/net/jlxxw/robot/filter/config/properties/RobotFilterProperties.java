package net.jlxxw.robot.filter.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * robot filter core properties
 */
@Configuration
@ConfigurationProperties("robot")
public class RobotFilterProperties {
    /**
     * filter chain
     */
    private List<FilterProperties> filters = new LinkedList<>();

    public List<FilterProperties> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterProperties> filters) {
        this.filters = filters;
    }
}
