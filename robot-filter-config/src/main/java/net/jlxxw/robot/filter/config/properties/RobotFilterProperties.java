package net.jlxxw.robot.filter.config.properties;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * robot filter core properties
 * @author lcy
 */
@RefreshScope
@Configuration
@ConfigurationProperties("robot")
public class RobotFilterProperties {
    /**
     * enable inline filters
     */
    private boolean enabledInlineFilters = true;
    /**
     * filter chain properties
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

    /**
     * in white list,no check
     * refresh 10s
     */
    private Set<String> globalIpWhitelist = new HashSet<>();

    /**
     * in black list, cannot access system
     * refresh 10s
     */
    private Set<String> globalIpBlacklist = new HashSet<>();

    /**
     * in white list, no check
     * refresh 10s
     * get Authorization form http header "Authorization"
     */
    private Set<String> globalAuthorizationWhitelist = new LinkedHashSet<>();

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

    public Set<String> getGlobalIpWhitelist() {
        return globalIpWhitelist;
    }

    public void setGlobalIpWhitelist(Set<String> globalIpWhitelist) {
        this.globalIpWhitelist = globalIpWhitelist;
    }

    public Set<String> getGlobalIpBlacklist() {
        return globalIpBlacklist;
    }

    public void setGlobalIpBlacklist(Set<String> globalIpBlacklist) {
        this.globalIpBlacklist = globalIpBlacklist;
    }

    public Set<String> getGlobalAuthorizationWhitelist() {
        return globalAuthorizationWhitelist;
    }

    public void setGlobalAuthorizationWhitelist(Set<String> globalAuthorizationWhitelist) {
        this.globalAuthorizationWhitelist = globalAuthorizationWhitelist;
    }

    public boolean isEnabledInlineFilters() {
        return enabledInlineFilters;
    }

    public void setEnabledInlineFilters(boolean enabledInlineFilters) {
        this.enabledInlineFilters = enabledInlineFilters;
    }
}
