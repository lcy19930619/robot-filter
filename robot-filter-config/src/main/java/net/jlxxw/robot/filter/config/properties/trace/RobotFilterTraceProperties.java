package net.jlxxw.robot.filter.config.properties.trace;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-04 10:33 AM
 */
@Configuration
@ConfigurationProperties("robot.filter.trace")
public class RobotFilterTraceProperties {

    /**
     * cookie name,default is "x-trace-client-id";
     */
    private String name = "x-trace-client-id";

    /**
     * cookie age,default is 300 second
     * unit is seconds
     * <br/>
     * warn:
     * <br/>
     * <b>
     *     If it is less than the rule time, it may lead to inaccurate judgment
     *
     *     <br/>
     *     servlet model:
     *     <br/>
     *     use memory = max age * current has cookie user total * cookie size * rule size * RobotDecisionFilter size
     *     <br/>
     * </b>
     * @see RuleProperties
     */
    private int maxAge = 300 ;

    /**
     * enable trace limit
     */
    private boolean enableTraceLimit = true;

    /**
     * The number of different IP addresses allowed to pass with the same ID in ${maxAge} seconds
     * @see RobotFilterTraceProperties#maxAge
     * for example
     * <br/>
     * ip proxy address
     */
    private int ipPass = 10 ;

    /**
     * The number of different ID addresses allowed to pass with the same ip in ${maxAge} seconds
     * @see RobotFilterTraceProperties#maxAge
     * for example
     * <br/>
     *  A large number of users use the same public network outlet IP
     */
    private int idPass = 300;


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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getIpPass() {
        return ipPass;
    }

    public void setIpPass(int ipPass) {
        this.ipPass = ipPass;
    }

    public int getIdPass() {
        return idPass;
    }

    public void setIdPass(int idPass) {
        this.idPass = idPass;
    }

    public boolean isEnableTraceLimit() {
        return enableTraceLimit;
    }

    public void setEnableTraceLimit(boolean enableTraceLimit) {
        this.enableTraceLimit = enableTraceLimit;
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
}
