package net.jlxxw.robot.filter.data.share.netty.adapter;

import java.util.Set;

/**
 *
 * @author chunyang.leng
 * @date 2022-11-03 5:09 PM
 */
public interface DiscoveryClientAdapter {

    /**
     * find cluster ,get all node ip and port
     * @return node ip list
     * format ip:port
     */
    Set<String> getClusterClientIpList();
}
