package net.jlxxw.robot.filter.config.properties;

import java.util.Set;
import net.jlxxw.robot.filter.config.rule.Rule;

/**
 * simple filter config
 * @author lcy
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
     * url pattern,un support refresh
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

    /**
     * robot filter rule
     */
    private Rule rule;

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

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
