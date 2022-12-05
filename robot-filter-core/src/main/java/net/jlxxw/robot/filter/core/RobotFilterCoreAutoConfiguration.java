package net.jlxxw.robot.filter.core;

import net.jlxxw.robot.filter.core.data.LocalStore;
import net.jlxxw.robot.filter.core.data.DataCore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("net.jlxxw.robot.filter.core")
public class RobotFilterCoreAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(DataCore.class)
    public DataCore localStore(){
        return new LocalStore();
    }
}
