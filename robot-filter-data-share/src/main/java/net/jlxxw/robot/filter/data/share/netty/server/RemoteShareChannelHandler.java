package net.jlxxw.robot.filter.data.share.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.RequestProtocol;
import org.springframework.stereotype.Component;

/**
 *
 * @author chunyang.leng
 * @date 2021-07-02 1:14 下午
 */
@Component
@ChannelHandler.Sharable
public class RemoteShareChannelHandler extends SimpleChannelInboundHandler<RequestProtocol> {


    @Override
    protected void channelRead0(ChannelHandlerContext context, RequestProtocol requestProtocol) throws Exception {
        // data handler todo
    }
}
