package net.jlxxw.robot.filter.data.share.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-07-02 12:58 下午
 */
@Component
public class RemoteShareNettyServer {

    private static final Logger logger = LoggerFactory.getLogger(RemoteShareNettyServer.class);

    @Autowired
    private RemoteShareChannelHandler javaRedisChannelHandler;

    private SslContext sslCtx = null;
    /**
     * 启动netty 监听端口
     */
    @PostConstruct
    private void startNettyServer() {
        Thread thread = new Thread(()->{
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        //最大客户端连接数默认值为1024
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                if (sslCtx != null ){
                                    // 添加SSL安装验证
                                    sc.pipeline().addFirst(sslCtx.newHandler(sc.alloc()));
                                }
                                sc.pipeline().addLast(new ServerDecoder());
                                sc.pipeline().addLast(new ServerEncoder());
                                sc.pipeline().addLast(javaRedisChannelHandler);
                            }
                        });

                ChannelFuture f = b.bind(9999).sync();
                if (f.isSuccess()) {
                    logger.info("开启 netty 监听器");
                }
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });
        thread.setDaemon(false);
        thread.setName("java-redis-server");
        thread.start();
    }
}
