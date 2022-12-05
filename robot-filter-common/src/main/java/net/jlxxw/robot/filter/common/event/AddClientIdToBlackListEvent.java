package net.jlxxw.robot.filter.common.event;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import org.springframework.context.ApplicationEvent;

/**
 * add client id to black list
 * @author chunyang.leng
 * @date 2022-11-09 4:23 PM
 */
public class AddClientIdToBlackListEvent extends ApplicationEvent {

    private final String clientId;
    private final String filterName;
    private final RuleProperties ruleProperties;

    public AddClientIdToBlackListEvent(String clientId,String filterName, RuleProperties ruleProperties){
        super(clientId);
        this.clientId = clientId;
        this.filterName = filterName;
        this.ruleProperties = ruleProperties;
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

}
