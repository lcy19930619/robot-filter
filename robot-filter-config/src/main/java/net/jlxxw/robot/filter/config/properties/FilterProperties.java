package net.jlxxw.robot.filter.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * simple filter config
 */
public class FilterProperties {
    /**
     * filter name,globally unique
     */
    private String name;
    /**
     * enable filter
     */
    private boolean enable = true;

    /**
     * url pattern
     */
    private Set<String> urlPattern;

    /**
     * filter class name
     */
    private String className;

    /**
     * sort by order，small priority
     */
    private int order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Set<String> getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Set<String> urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
