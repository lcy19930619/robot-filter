package net.jlxxw.robot.filter.data.share.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.jlxxw.robot.filter.common.event.NettySendDataEvent;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.RequestProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

/**
 * netty client
 * @author chunyang.leng
 * @date 2021-08-06 4:45 下午
 */
public class NettyClient implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    /**
     * remote host
     */
    private String host;

    /**
     * remote port
     */
    private int port;

    private Channel channel;

    @Autowired
    private ClientHandler clientHandler;
    @Autowired
    private RobotFilterProperties robotFilterProperties;
    @Autowired
    private LogUtils logUtils;

    private EventLoopGroup group;

    private int retryCount;

    private int retryMaxCount;

    private int retryDelay;

    @PostConstruct
    public void start() {
        retryMaxCount = robotFilterProperties.getDataShareProperties().getNetty().getClient().getRetryMaxCount();
        retryDelay = robotFilterProperties.getDataShareProperties().getNetty().getClient().getRetryDelay();
        group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            // 使用NioSocketChannel来作为连接用的channel类
            .channel(NioSocketChannel.class)
            // 绑定连接初始化器
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //编码request
                    pipeline.addLast(new ClientEncoder());
                    //解码response
                    pipeline.addLast(new ClientDecoder());
                    //客户端处理类
                    pipeline.addLast(clientHandler);
                }
            });
        Map<String, Object> option = robotFilterProperties.getDataShareProperties().getNetty().getClient().getNettyOption();
        if (!CollectionUtils.isEmpty(option)){
            option.forEach((k,v)->{
                ChannelOption<Object> key = ChannelOption.valueOf(k);
                bootstrap.option(key,v);
            });
        }
        connect(bootstrap);
    }

    public Channel getChannel() {
        return channel;
    }


    private void connect(Bootstrap bootstrap) {
        try {
            //发起异步连接请求，绑定连接端口和host信息
            final ChannelFuture future = bootstrap.connect(host, port).sync();

            future.addListener((ChannelFutureListener) arg0 -> {
                if (future.isSuccess()) {
                    logUtils.info(logger,"robot filter data share Connected to {} success",(host+":"+port) );
                    this.channel = future.channel();
                    // reset
                    retryCount = 0;
                } else {
                    future.channel().eventLoop().schedule(() -> {
                        if (retryCount > retryMaxCount){
                           throw new RuntimeException( "robot filter data share Connected to  " + (host+":"+port) + "failed !!!! retry count exceeded ");
                        }
                        logUtils.warn(logger,"robot filter data share Connected to {} failed ... retry max:{},current:{}",(host+":"+port),retryMaxCount,retryCount );
                        try {
                           connect(bootstrap);
                           retryCount ++;
                        } catch (Exception e) {
                           logUtils.error(logger,"robot filter data share Connected to  " + (host+":"+port) + "failed exception: ",e);
                        }
                    }, retryDelay, TimeUnit.MILLISECONDS);
                }
            });
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("robotFilterThreadpool")
    @EventListener(NettySendDataEvent.class)
    public void sendMessage(NettySendDataEvent event){
        RequestProtocol requestProtocol = (RequestProtocol)event.getData();
        channel.writeAndFlush(requestProtocol);
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        channel.closeFuture();
        channel.deregister();
        channel.close();
        group.shutdownGracefully();
    }
}
