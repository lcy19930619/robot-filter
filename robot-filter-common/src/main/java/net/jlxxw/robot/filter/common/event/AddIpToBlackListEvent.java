package net.jlxxw.robot.filter.common.event;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import org.springframework.context.ApplicationEvent;

/**
 * add client id to black list
 * @author chunyang.leng
 * @date 2022-11-09 4:23 PM
 */
public class AddIpToBlackListEvent extends ApplicationEvent {

    private final String ip;
    private final String filterName;
    private final RuleProperties ruleProperties;

    public AddIpToBlackListEvent(String ip,String filterName, RuleProperties ruleProperties){
        super(filterName);
        this.filterName = filterName;
        this.ruleProperties = ruleProperties;
        this.ip = ip;
    }


    public String getFilterName() {
        return filterName;
    }

    public RuleProperties getRuleProperties() {
        return ruleProperties;
    }

    public String getIp() {
        return ip;
    }
}
