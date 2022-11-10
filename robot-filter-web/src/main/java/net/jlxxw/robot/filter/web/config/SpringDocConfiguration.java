package net.jlxxw.robot.filter.web.config;

import java.util.Collections;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunyang.leng
 * @date 2022-11-10 2:37 PM
 */
@Configuration
public class SpringDocConfiguration {

    @ConditionalOnMissingBean(SpringDocConfigProperties.class)
    @Bean
    public SpringDocConfigProperties springDocConfigProperties(){
        SpringDocConfigProperties properties = new SpringDocConfigProperties();
        properties.setPackagesToScan(Collections.singletonList("net.jlxxw.robot.filter.web.controller"));
        return properties;
    }
}
