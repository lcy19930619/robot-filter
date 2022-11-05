package net.jlxxw.robot.filter.config.properties.trace;

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
     * cookie age,default is 7 day
     */
    private int maxAge = 7 * 24 * 60 * 60 ;

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
