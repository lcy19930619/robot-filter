package net.jlxxw.robot.filter.config.properties;

import java.util.HashSet;
import java.util.Set;

/**
 * every one robot filter rule
 *
 * @author chunyang.leng
 * @date 2022-11-03 10:01 AM
 */
public class RuleProperties {

    /**
     * robot allow pass max qps
     */
    private int maxQps = 50;

    /**
     * allow to add blacklisted
     */
    private boolean allowAddBlacklisted = true;

    /**
     * time out remove blacklisted
     * unit:second
     */
    private long blacklistedTime = 60;

    /**
     * allow remove
     */
    private boolean allowRemoveBlacklisted = true;

    /**
     * white list
     * refresh 10s
     */
    private Set<String> whitelist = new HashSet<String>();

    /**
     * blacklisted
     * refresh 10s
     */
    private Set<String> blacklisted = new HashSet<String>();

    /**
     * reject response http code,default is 403
     */
    private int httpResponseCode = 403;

    /**
     * reject response content type,default is application/json
     */
    private String contentType = "application/json";

    public int getMaxQps() {
        return maxQps;
    }

    public void setMaxQps(int maxQps) {
        this.maxQps = maxQps;
    }

    public boolean isAllowAddBlacklisted() {
        return allowAddBlacklisted;
    }

    public void setAllowAddBlacklisted(boolean allowAddBlacklisted) {
        this.allowAddBlacklisted = allowAddBlacklisted;
    }

    public long getBlacklistedTime() {
        return blacklistedTime;
    }

    public void setBlacklistedTime(long blacklistedTime) {
        this.blacklistedTime = blacklistedTime;
    }

    public boolean isAllowRemoveBlacklisted() {
        return allowRemoveBlacklisted;
    }

    public void setAllowRemoveBlacklisted(boolean allowRemoveBlacklisted) {
        this.allowRemoveBlacklisted = allowRemoveBlacklisted;
    }

    public Set<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<String> whitelist) {
        this.whitelist = whitelist;
    }

    public Set<String> getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Set<String> blacklisted) {
        this.blacklisted = blacklisted;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
