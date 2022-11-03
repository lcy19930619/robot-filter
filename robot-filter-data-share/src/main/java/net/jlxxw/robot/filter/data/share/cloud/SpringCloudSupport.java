package net.jlxxw.robot.filter.data.share.cloud;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.jlxxw.robot.filter.data.share.component.DiscoveryClientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 5:11 PM
 */
@Primary
@Component
public class SpringCloudSupport implements DiscoveryClientAdapter {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String applicationName;


    /**
     * find cluster ,get every on node ip
     *
     * @return node ip list
     */
    @Override
    public Set<String> getClusterClientIpList() {
        List<ServiceInstance> instances = discoveryClient.getInstances(applicationName);

        return instances.stream().map(ServiceInstance::getHost).collect(Collectors.toSet());
    }
}
