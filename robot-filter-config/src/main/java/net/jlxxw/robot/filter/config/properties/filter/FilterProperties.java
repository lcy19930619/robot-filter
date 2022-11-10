package net.jlxxw.robot.filter.config.properties.filter;

import java.util.List;
import java.util.Set;

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
     * lru limit
     */
    private int lru = 200;
    /**
     * enable filter
     */
    private boolean enabled = true;

    /**
     * url pattern,un support refresh
     */
    private Set<String> urlPattern;

    /**
     * sort by order，small priority
     */
    private int order;

    /**
     * custom filter class, default is net.jlxxw.robot.filter.servlet.filter.decision.RobotDecisionFilter
     *
     * <b>the class must is RobotDecisionFilter subclass</b>
     */
    private String className = "net.jlxxw.robot.filter.servlet.filter.decision.RobotDecisionFilter";

    /**
     * robot filter ruleProperties
     *
     */
    private List<RuleProperties> rules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Set<String> urlPattern) {
        this.urlPattern = urlPattern;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<RuleProperties> getRules() {
        return rules;
    }

    public void setRules(List<RuleProperties> rules) {
        this.rules = rules;
    }

    public int getLru() {
        return lru;
    }

    public void setLru(int lru) {
        this.lru = lru;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
