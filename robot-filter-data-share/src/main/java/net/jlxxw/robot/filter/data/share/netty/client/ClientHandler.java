package net.jlxxw.robot.filter.data.share.netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.ResponseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author chunyang.leng
 * @date 2021-07-07 1:33 下午
 */
@Component
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ResponseProtocol> {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);


    /**
     *     处理服务端返回的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseProtocol response) throws Exception {
        try {

           // logger.debug("接受到server响应数据:{} ", JSON.toJSONString(response));
        }catch (Exception e){
            logger.error("客户端未知的异常",e);
        }
    }

}

