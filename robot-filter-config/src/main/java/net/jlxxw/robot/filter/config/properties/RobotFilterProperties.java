package net.jlxxw.robot.filter.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * robot filter core properties
 * @author lcy
 */
@RefreshScope
@Configuration
@ConfigurationProperties("robot")
public class RobotFilterProperties {
    /**
     * filter chain
     */
    private List<FilterProperties> filters = new LinkedList<>();

    /**
     * log properties
     */
    private FilterLogProperties log;

    /**
     * response
     */
    private ResponseProperties response;

    public List<FilterProperties> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterProperties> filters) {
        this.filters = filters;
    }

    public FilterLogProperties getLog() {
        return log;
    }

    public void setLog(FilterLogProperties log) {
        this.log = log;
    }

    public ResponseProperties getResponse() {
        return response;
    }

    public void setResponse(ResponseProperties response) {
        this.response = response;
    }
}
