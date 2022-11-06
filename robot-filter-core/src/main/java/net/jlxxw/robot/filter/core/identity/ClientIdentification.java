package net.jlxxw.robot.filter.core.identity;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import net.jlxxw.robot.filter.common.encrypt.DesEncryption;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.core.limit.SimpleCountUtils;
import net.jlxxw.robot.filter.core.lru.TimeOutLru;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Identification
 *
 * @author chunyang.leng
 * @date 2022-11-03 3:18 PM
 */
@Component
public class ClientIdentification {
    @Autowired
    private RobotFilterProperties robotFilterProperties;
    /**
     * key: ip
     * value: total client id set
     */
    private static  Map<String, SimpleCountUtils> IP_COUNTERS = null;

    /**
     * key:  client id
     * value: total client ip
      */
    private static  Map<String,SimpleCountUtils> CLIENT_ID_COUNTERS = null;

    @Autowired
    private DesEncryption desEncryption;

    @PostConstruct
    private void init(){
        int age = robotFilterProperties.getTrace().getMaxAge();
        IP_COUNTERS = new TimeOutLru<>(2048,age * 1000,2048);
        CLIENT_ID_COUNTERS = new TimeOutLru<>(2048,age * 1000,2048);
    }

    /**
     * create a new client id
     *
     * @return client id ,unique identifier
     */
    public String createClientId(String ip) throws Exception {
        String id = UUID.randomUUID().toString();
        SimpleCountUtils simpleCountUtils = IP_COUNTERS.get(ip);
        if (Objects.isNull(simpleCountUtils)) {
            simpleCountUtils = new SimpleCountUtils(robotFilterProperties.getTrace().getMaxAge() * 1000L);
            IP_COUNTERS.put(ip, simpleCountUtils);
        }
        simpleCountUtils.incrementAndGet(id);
        return desEncryption.encrypt(id);
    }

    /**
     * verify data
     * @param clientId client id
     * @param ip client ip
     * @throws Exception verify failed
     */
    public String verifyClientId(String clientId,String ip) throws Exception {
        String decrypt = desEncryption.decrypt(clientId);
        SimpleCountUtils simpleCountUtils = CLIENT_ID_COUNTERS.get(clientId);
        if (Objects.isNull(simpleCountUtils)) {
            simpleCountUtils = new SimpleCountUtils(robotFilterProperties.getTrace().getMaxAge() * 1000L);
            IP_COUNTERS.put(ip, simpleCountUtils);
        }
        simpleCountUtils.incrementAndGet(ip);
        return decrypt;
    }



    /**
     *
     * @param ip
     * @return
     */
    public int countByIp(String ip){
        SimpleCountUtils simpleCountUtils = IP_COUNTERS.get(ip);
        if (Objects.isNull(simpleCountUtils)) {
            return 0;
        }
        return simpleCountUtils.countInfoSize();
    }


    /**
     *
     * @param clientId
     * @return
     */
    public int countByClientId(String clientId){
        SimpleCountUtils simpleCountUtils = CLIENT_ID_COUNTERS.get(clientId);
        if (Objects.isNull(simpleCountUtils)) {
            return 0;
        }
        return simpleCountUtils.countInfoSize();
    }

}
