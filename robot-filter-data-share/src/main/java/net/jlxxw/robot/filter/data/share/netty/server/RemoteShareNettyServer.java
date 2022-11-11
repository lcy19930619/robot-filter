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
import io.netty.handler.ssl.SslContextBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.PostConstruct;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.data.DataShareProperties;
import net.jlxxw.robot.filter.config.properties.data.netty.NettyServerSSLProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

/**
 * @author chunyang.leng
 * @date 2021-07-02 12:58 下午
 */
public class RemoteShareNettyServer {

    private static final Logger logger = LoggerFactory.getLogger(RemoteShareNettyServer.class);
    private SslContext sslContext = null;

    @Autowired
    private RemoteShareChannelHandler remoteShareChannelHandler;
    @Autowired
    private DataShareProperties dataShareProperties;
    @Autowired
    private LogUtils logUtils;
    /**
     * 启动netty 监听端口
     */
    @PostConstruct
    private void startNettyServer() {
        Thread thread = new Thread(()->{

            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            NettyServerSSLProperties ssl = dataShareProperties.getNetty().getServer().getSsl();
            if (ssl.isEnabled()){
                try (InputStream certChainFile = new ClassPathResource(ssl.getServerCert()).getInputStream();
                     InputStream keyFile = new ClassPathResource(ssl.getServerKey()).getInputStream();
                     InputStream rootFile = new ClassPathResource(ssl.getCaCert()).getInputStream();
                ){
                    sslContext = SslContextBuilder.forServer(certChainFile, keyFile).trustManager(rootFile).build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        //最大客户端连接数默认值为1024
                        .option(ChannelOption.SO_BACKLOG, dataShareProperties.getNetty().getServer().getServerMaxConnections())
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                if (sslContext != null ){
                                    // 添加SSL安装验证
                                    sc.pipeline().addFirst(sslContext.newHandler(sc.alloc()));
                                }
                                sc.pipeline().addLast(new ServerDecoder());
                                sc.pipeline().addLast(new ServerEncoder());
                                sc.pipeline().addLast(remoteShareChannelHandler);
                            }
                        });

                Map<String, Object> option = dataShareProperties.getNetty().getServer().getNettyOption();
                if (!CollectionUtils.isEmpty(option)){
                    option.forEach((k,v)->{
                        ChannelOption<Object> key = ChannelOption.valueOf(k);
                        b.option(key,v);
                    });
                }
                ChannelFuture f = b.bind(dataShareProperties.getNetty().getServer().getPort()).sync();
                if (f.isSuccess()) {
                    logUtils.info(logger,"start data share, model:netty,  server listener {} ...",dataShareProperties.getNetty().getServer().getPort());
                }
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                logUtils.error(logger," data share netty start failed!!!",e);
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });
        thread.setDaemon(false);
        thread.setName("robot-data-share-server");
        thread.start();
    }
}
