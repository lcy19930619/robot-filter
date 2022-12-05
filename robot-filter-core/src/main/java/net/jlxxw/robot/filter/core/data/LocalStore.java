package net.jlxxw.robot.filter.core.data;

import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;

/**
 * todo
 * @author chunyang.leng
 * @date 2022-12-05 1:19 PM
 */
public class LocalStore implements DataCore {
    /**
     * init data store
     */
    @Override
    public void init() {

    }

    /**
     * init rule
     *
     * @param filterName filter name
     * @param properties rule
     */
    @Override
    public void init(String filterName, RuleProperties properties) {

    }

    /**
     * ip pass inc
     *
     * @param filterName filter name
     * @param ruleName   name of the rule
     * @param ip         client ip
     */
    @Override
    public void incIp(String filterName, String ruleName, String ip) {

    }

    /**
     * client id pass inc
     *
     * @param filterName filter name
     * @param ruleName   name of the rule
     * @param clientId   client id
     */
    @Override public void incClientId(String filterName, String ruleName, String clientId) {

    }

    /**
     * allow pass ?
     *
     * @param filterName filter name
     * @param ruleName   rule name
     * @param ip         client ip
     * @return true is pass
     */
    @Override public boolean allowIp(String filterName, String ruleName, String ip) {
        return false;
    }

    /**
     * allow pass ?
     *
     * @param filterName filter name
     * @param ruleName   rule name
     * @param clientId   client id
     * @return true is pass
     */
    @Override public boolean allowClientId(String filterName, String ruleName, String clientId) {
        return false;
    }

    /**
     * add to temp list
     *
     * @param ip
     * @param ruleProperties
     */
    @Override public void addIpToTempBlackList(String ip, RuleProperties ruleProperties) {

    }

    /**
     * add to temp list
     *
     * @param ip
     * @param ruleProperties
     */
    @Override public void addClientIdToTempBlackList(String ip, RuleProperties ruleProperties) {

    }

    /**
     * check ip
     *
     * @param ip
     * @return
     */
    @Override public boolean checkIpInTempBlackList(String ip) {
        return false;
    }

    /**
     * check client id
     *
     * @param clientId
     * @return
     */
    @Override public boolean checkClientIdInTempBlackList(String clientId) {
        return false;
    }
}
