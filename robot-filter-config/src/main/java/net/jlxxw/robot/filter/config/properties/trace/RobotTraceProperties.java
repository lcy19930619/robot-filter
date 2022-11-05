package net.jlxxw.robot.filter.config.properties.trace;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;

/**
 * @author chunyang.leng
 * @date 2022-11-04 10:33 AM
 */
public class RobotTraceProperties {

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
}
