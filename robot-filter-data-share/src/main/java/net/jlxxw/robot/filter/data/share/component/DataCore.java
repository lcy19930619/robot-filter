package net.jlxxw.robot.filter.data.share.component;

import javax.annotation.PostConstruct;

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
}
