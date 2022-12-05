package net.jlxxw.robot.filter.data.share.netty.listener;

import net.jlxxw.robot.filter.common.event.AddClientIdToBlackListEvent;
import net.jlxxw.robot.filter.common.event.AddIpToBlackListEvent;
import net.jlxxw.robot.filter.common.event.NettySendDataEvent;
import net.jlxxw.robot.filter.common.event.ReceiveRequestEvent;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.RequestProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * @author chunyang.leng
 * @date 2022-11-03 4:23 PM
 */
public class ApplicationEventNettyListener {

    @Autowired
    private ApplicationContext applicationContext;

    @EventListener(ReceiveRequestEvent.class)
    public void receiveRequestEventListener(ReceiveRequestEvent event){
        RequestProtocol convert = convert(event);
        applicationContext.publishEvent(new NettySendDataEvent(convert));
    }

    @EventListener(AddClientIdToBlackListEvent.class)
    public void addTempBlackList(AddClientIdToBlackListEvent event){
        RequestProtocol convert = convert(event);
        applicationContext.publishEvent(new NettySendDataEvent(convert));
    }

    @EventListener(AddIpToBlackListEvent.class)
    public void addTempBlackList(AddIpToBlackListEvent event){
        RequestProtocol convert = convert(event);
        applicationContext.publishEvent(new NettySendDataEvent(convert));
    }



    public RequestProtocol convert(ReceiveRequestEvent event){
        // todo convert
        return  RequestProtocol.newBuilder().build();
    }

    public RequestProtocol convert(AddClientIdToBlackListEvent event){
        // todo convert
        return  RequestProtocol.newBuilder().build();
    }

    public RequestProtocol convert(AddIpToBlackListEvent event){
        // todo convert
        return  RequestProtocol.newBuilder().build();
    }
}
