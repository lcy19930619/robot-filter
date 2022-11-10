package net.jlxxw.robot.filter.config.properties;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * robot filter core properties
 *
 * @author lcy
 */
@RefreshScope
@Configuration
@ConfigurationProperties("robot.filter")
public class RobotFilterProperties {

    /**
     * in white list,no check
     */
    private Set<String> globalIpWhitelist = new HashSet<>();

    /**
     * in black list, cannot access system
     */
    private Set<String> globalIpBlacklist = new HashSet<>();
    /**
     * referer list,not in list, cannot access system
     */
    private Set<String> refererWhitelist = new HashSet<>();
    /**
     * origin list,not in list, cannot access system
     */
    private Set<String> originWhitelist = new HashSet<>();
    /**
     * in white list, no check
     *
     * get Authorization form http header "Authorization"
     */
    private Set<String> globalAuthorizationWhitelist = new LinkedHashSet<>();


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

    public Set<String> getRefererWhitelist() {
        return refererWhitelist;
    }

    public void setRefererWhitelist(Set<String> refererWhitelist) {
        this.refererWhitelist = refererWhitelist;
    }

    public Set<String> getOriginWhitelist() {
        return originWhitelist;
    }

    public void setOriginWhitelist(Set<String> originWhitelist) {
        this.originWhitelist = originWhitelist;
    }

}
