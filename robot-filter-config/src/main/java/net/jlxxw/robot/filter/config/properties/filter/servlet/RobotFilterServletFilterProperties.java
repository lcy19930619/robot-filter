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
     * filter  properties
     */
    private FilterProperties filter = new FilterProperties();

    public FilterProperties getFilter() {
        return filter;
    }

    public void setFilter(FilterProperties filter) {
        this.filter = filter;
    }
}
