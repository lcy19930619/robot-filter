package net.jlxxw.robot.filter.data.share.redis.listener;

import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.data.share.redis.client.RobotFilterRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

/**
 * @author chunyang.leng
 * @date 2022-12-05 1:13 PM
 */
public class RobotFilterRedisListener {

    @Autowired
    private RobotFilterRedisClient robotFilterRedisClient;


    @EventListener(ReceiveRequestEvent.class)
    public void receiveRequestEvent(ReceiveRequestEvent event){
        String filterName = event.getFilterName();
        String ruleName = event.getRuleName();
        String ip = event.getIp();
        String clientId = event.getClientId();

        robotFilterRedisClient.incClientId(filterName,ruleName,clientId);
        robotFilterRedisClient.incIp(filterName,ruleName,ip);
    }
}
