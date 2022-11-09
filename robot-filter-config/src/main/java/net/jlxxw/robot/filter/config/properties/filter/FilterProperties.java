package net.jlxxw.robot.filter.config.properties.filter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * simple filter config
 * @author lcy
 */
public class FilterProperties {
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
     * robot filter ruleProperties
     *
     */
    private List<RuleProperties> rules = Collections.singletonList(new RuleProperties());

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
}
