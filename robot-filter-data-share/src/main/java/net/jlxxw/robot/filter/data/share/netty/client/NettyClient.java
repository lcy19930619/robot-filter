package net.jlxxw.robot.filter.data.share.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chunyang.leng
 * @date 2021-08-06 4:45 下午
 */
public class NettyClient implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private String host;
    private int port;

    private Channel channel;

    private ClientHandler clientHandler;

    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
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
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener((ChannelFutureListener) arg0 -> {
            if (future.isSuccess()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败", future.cause());
                group.shutdownGracefully(); //关闭线程组
                channel.close();
            }
        });
        this.channel = future.channel();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void close() throws IOException {
        channel.closeFuture();
        channel.deregister();
        channel.close();
    }
}
