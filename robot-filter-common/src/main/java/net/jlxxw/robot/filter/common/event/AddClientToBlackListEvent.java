package net.jlxxw.robot.filter.common.event;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import org.springframework.context.ApplicationEvent;

/**
 * add client id to black list
 * @author chunyang.leng
 * @date 2022-11-09 4:23 PM
 */
public class AddClientToBlackListEvent extends ApplicationEvent {

    private String ip;
    private String clientId;
    private String filterName;
    private RuleProperties ruleProperties;

    public AddClientToBlackListEvent(String ip,String clientId,String filterName, RuleProperties ruleProperties){
        super(clientId);
    }

    public String getClientId() {
        return clientId;
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
