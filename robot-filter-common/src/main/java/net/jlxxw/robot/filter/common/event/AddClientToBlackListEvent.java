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
    private RuleProperties ruleProperties;

    public AddClientToBlackListEvent(String ip,String clientId, RuleProperties ruleProperties){
        super(clientId);
        this.clientId = clientId;
        this.ip = ip;
        this.ruleProperties = ruleProperties;
    }

    public String getClientId() {
        return clientId;
    }


    public RuleProperties getRuleProperties() {
        return ruleProperties;
    }

    public String getIp() {
        return ip;
    }
}
