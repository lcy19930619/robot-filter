package net.jlxxw.robot.filter.data.share.listener;

import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 4:23 PM
 */
@Component
public class ApplicationEventListener {

    @Async
    @EventListener(ReceiveRequestEvent.class)
    public void receiveRequestEventListener(ReceiveRequestEvent event){

    }

}
