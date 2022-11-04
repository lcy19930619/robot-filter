package net.jlxxw.robot.filter.data.share.listener;

import net.jlxxw.robot.filter.common.event.NettySendDataEvent;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.RequestProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 4:23 PM
 */
@Component
public class ApplicationEventListener {

    @Autowired
    private ApplicationContext applicationContext;

    @EventListener(ReceiveRequestEvent.class)
    public void receiveRequestEventListener(ReceiveRequestEvent event){
        RequestProtocol convert = convert(event);
        applicationContext.publishEvent(new NettySendDataEvent(convert));
    }


    public RequestProtocol convert(ReceiveRequestEvent event){
        // todo convert
        return  RequestProtocol.newBuilder().build();
    }
}
