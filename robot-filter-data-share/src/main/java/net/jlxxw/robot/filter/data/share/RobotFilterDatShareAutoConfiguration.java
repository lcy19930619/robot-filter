package net.jlxxw.robot.filter.data.share;

import net.jlxxw.robot.filter.data.share.component.DiscoveryClientAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:04 PM
 */
@ConditionalOnBean(DiscoveryClientAdapter.class)
@EnableAsync
@ComponentScan("net.jlxxw.robot.filter.data.share")
public class RobotFilterDatShareAutoConfiguration  {

}
