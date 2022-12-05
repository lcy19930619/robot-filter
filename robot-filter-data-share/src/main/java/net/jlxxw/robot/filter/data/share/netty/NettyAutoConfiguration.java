package net.jlxxw.robot.filter.data.share.netty;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.netty.channel.Channel;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import net.jlxxw.robot.filter.common.event.SystemEndEvent;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.config.properties.data.DataShareProperties;
import net.jlxxw.robot.filter.data.share.RobotFilterDatShareAutoConfiguration;
import net.jlxxw.robot.filter.data.share.netty.adapter.DefaultDiscoveryClientSupport;
import net.jlxxw.robot.filter.data.share.netty.adapter.DiscoveryClientAdapter;
import net.jlxxw.robot.filter.data.share.netty.client.NettyClient;
import net.jlxxw.robot.filter.data.share.netty.listener.ApplicationEventNettyListener;
import net.jlxxw.robot.filter.data.share.netty.server.RemoteShareNettyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author chunyang.leng
 * @date 2022-11-11 11:24 AM
 */
@Configuration
@ConditionalOnProperty(name = "robot.filter.share.model",havingValue = "netty")
public class NettyAutoConfiguration implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(RobotFilterDatShareAutoConfiguration.class);
  //  private static final Cache<String, Set<String>> cache = CacheBuilder.newBuilder().refreshAfterWrite(30, TimeUnit.SECONDS).build();

    private LoadingCache<String, Set<String>> CACHE = Caffeine
        .newBuilder()
        .maximumSize(100)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Set<String>>() {
            @Override
            public @Nullable Set<String> load(@NonNull String key) throws Exception {
                return getNode();
            }
        });

    private final Set<String> localIpSet = new HashSet<String>();
    @Autowired
    private DiscoveryClientAdapter discoveryClientAdapter;
    @Autowired
    private DataShareProperties dataShareProperties;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private ThreadPoolTaskExecutor robotFilterThreadpool;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${server.port:8080}")
    private int serverPort;

    private final Map<String, NettyClient> clientMap = new ConcurrentHashMap<>();

    @Bean
    public RemoteShareNettyServer remoteShareNettyServer(){
        return new RemoteShareNettyServer();
    }

    @Bean
    public ApplicationEventNettyListener applicationEventNettyListener(){
        return new ApplicationEventNettyListener();
    }

    @Bean
    @ConditionalOnMissingBean(DiscoveryClient.class)
    public DiscoveryClientAdapter defaultDiscoverySupport(){
        return new DefaultDiscoveryClientSupport();
    }

    @PostConstruct
    private void init() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface anInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = anInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address;
                        localIpSet.add(inet4Address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        logUtils.info(logger,"start data share model is netty ");
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        robotFilterThreadpool.execute(()->{
            while (true) {
                try {
                    checkAndCreateClusterNettyClient();
                }catch (Exception e) {
                    logUtils.error(logger,"robot filter data share start failed!!! start retry",e);
                }finally {
                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            for (String ip : localIpSet) {
                if ("127.0.0.1".equals(ip)){
                    continue;
                }
                applicationContext.publishEvent(new SystemEndEvent(ip,serverPort));
            }
        }));
    }

    private void checkAndCreateClusterNettyClient() throws IOException {
        Set<String> node = getAllNode();
        int port = dataShareProperties.getNetty().getServer().getPort();
        for (String serverIp : node) {
            String key = serverIp + ":" + port;
            String nettyClientBeanName = "nettyClient."+key;
            NettyClient nettyClient = clientMap.get(key);
            if (Objects.isNull(nettyClient)) {
                if (beanFactory instanceof DefaultListableBeanFactory) {
                    DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(NettyClient.class);
                    beanDefinition.setAutowireCandidate(true);
                    beanDefinition.setScope( ConfigurableBeanFactory.SCOPE_PROTOTYPE);
                    ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                    constructorArgumentValues.addIndexedArgumentValue(0,serverIp);
                    constructorArgumentValues.addIndexedArgumentValue(1,port);
                    beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
                    defaultListableBeanFactory.registerBeanDefinition(nettyClientBeanName,beanDefinition);
                    NettyClient bean = (NettyClient)defaultListableBeanFactory.getBean(nettyClientBeanName);
                    clientMap.put(key,bean);
                }
            } else {
                Channel channel = nettyClient.getChannel();
                if (!channel.isOpen()) {
                    NettyClient remove = clientMap.remove(key);
                    remove.close();
                    if (beanFactory instanceof DefaultListableBeanFactory) {
                        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
                        defaultListableBeanFactory.removeBeanDefinition(nettyClientBeanName);
                    }
                }
            }

        }
    }

    private Set<String> getAllNode() {
        return CACHE.get("key");
    }


    private Set<String> getNode(){
        Set<String> list = discoveryClientAdapter.getClusterClientIpList();
        list.removeAll(localIpSet);
        return list;
    }
}
