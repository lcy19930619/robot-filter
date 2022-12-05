package net.jlxxw.robot.filter.core.data;

import net.jlxxw.robot.filter.data.share.component.DataCore;

/**
 *
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
     * ip pass inc
     *
     * @param filterName filter name
     * @param ruleName   name of the rule
     * @param ip         client ip
     */
    @Override public void incIp(String filterName, String ruleName, String ip) {

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
}
