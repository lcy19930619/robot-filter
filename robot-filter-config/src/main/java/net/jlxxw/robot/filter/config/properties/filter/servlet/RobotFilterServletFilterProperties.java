package net.jlxxw.robot.filter.config.properties.filter.servlet;

import java.util.LinkedList;
import java.util.List;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-07 1:01 PM
 */
@Configuration
@ConfigurationProperties("robot.filter.servlet")
public class RobotFilterServletFilterProperties {
    /**
     * filter chain properties
     */
    private List<FilterProperties> filters = new LinkedList<>();

    public List<FilterProperties> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterProperties> filters) {
        this.filters = filters;
    }
}
