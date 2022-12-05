package net.jlxxw.robot.filter.data.share.netty.adapter;

import java.util.Set;
import net.jlxxw.robot.filter.config.properties.data.netty.NettyProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-12-05 6:27 PM
 */
public class DefaultDiscoveryClientSupport implements DiscoveryClientAdapter{

    @Autowired
    private NettyProperties nettyProperties;
    /**
     * find cluster ,get all node ip and port
     *
     * @return node ip list
     * only ip
     */
    @Override
    public Set<String> getClusterClientIpList() {
        return nettyProperties.getServiceList();
    }
}
