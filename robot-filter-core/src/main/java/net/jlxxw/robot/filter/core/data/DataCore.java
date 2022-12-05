package net.jlxxw.robot.filter.core.data;

import javax.annotation.PostConstruct;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;

/**
 * @author chunyang.leng
 * @date 2022-12-05 12:34 PM
 */
public interface DataCore {

    /**
     * init data store
     */
    @PostConstruct
    void init();

    /**
     * ip pass inc
     * @param filterName filter name
     * @param ruleName name of the rule
     * @param ip       client ip
     */
    void incIp(String filterName,String ruleName,String ip);

    /**
     * client id pass inc
     * @param filterName filter name
     * @param ruleName name of the rule
     * @param clientId client id
     */
    void incClientId(String filterName,String ruleName,String clientId);

    /**
     * allow pass ?
     * @param filterName filter name
     * @param ruleName rule name
     * @param ip client ip
     * @return true is pass
     */
    boolean allowIp(String filterName,String ruleName,String ip);

    /**
     * allow pass ?
     * @param filterName filter name
     * @param ruleName rule name
     * @param clientId client id
     * @return true is pass
     */
    boolean allowClientId(String filterName,String ruleName,String clientId);

    /**
     * add to temp list
     * @param ip
     */
    void addIpToTempBlackList(String ip, RuleProperties ruleProperties);


    /**
     * add to temp list
     * @param ip
     */
    void addClientIdToTempBlackList(String ip, RuleProperties ruleProperties);

    /**
     * check ip
     * @param ip
     * @return
     */
    boolean checkIpInTempBlackList(String ip);

    /**
     * check client id
     * @param clientId
     * @return
     */
    boolean checkClientIdInTempBlackList(String clientId);
}
