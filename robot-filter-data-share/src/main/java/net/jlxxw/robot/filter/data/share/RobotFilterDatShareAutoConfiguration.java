package net.jlxxw.robot.filter.data.share;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:04 PM
 */
@ConditionalOnBean(DiscoveryClient.class)
@ComponentScan("net.jlxxw.robot.filter.data.share")
public class RobotFilterDatShareAutoConfiguration {
}
