package net.jlxxw.robot.filter.core.data;

import net.jlxxw.robot.filter.core.vo.RobotClientIdVO;
import net.jlxxw.robot.filter.core.vo.RobotIpVO;

/**
 * data core interface
 *
 * @author chunyang.leng
 * @date 2022-11-05 10:28 PM
 */
public interface DataCore {

    /**
     * current filter increase the client id counter once
     *
     * @param clientId client id
     * @param ruleName name of the rule
     */
    void incClientId(String clientId, String ruleName);

    /**
     * current filter increase the ip counter once
     *
     * @param ip       client ip
     * @param ruleName rule name
     */
    void incIp(String ip, String ruleName);

    /**
     * count current filter all client id
     *
     * @return key: client id,value: qps
     */
    RobotClientIdVO countClientId();

    /**
     * count current filter all ip
     *
     * @return key: ip,value: qps
     */
    RobotIpVO countIp();

    /**
     * count the current IP qps
     *
     * @param ip       client ip
     * @param ruleName name of the rule
     * @return qps
     */
    int countCurrentPassByIp(String ip, String ruleName);

    /**
     * count the current client id qps
     *
     * @param clientId client id
     * @param ruleName name of the rule
     * @return qps
     */
    int countCurrentPassByClientId(String clientId, String ruleName);
}
