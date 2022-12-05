package net.jlxxw.robot.filter.data.share.redis;

import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import net.jlxxw.robot.filter.common.log.LogUtils;
import net.jlxxw.robot.filter.data.share.redis.client.RobotFilterRedisClient;
import net.jlxxw.robot.filter.data.share.redis.listener.RobotFilterRedisListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author chunyang.leng
 * @date 2022-12-05 12:19 PM
 */
@Configuration
@ConditionalOnProperty(name = "robot.filter.share.model",havingValue = "redis")
public class RedisAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisAutoConfiguration.class);
    @Autowired LogUtils logUtils;
    @PostConstruct
    private void initialize(){
        logUtils.info(logger,"start data share model is redis ");
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory)
        throws UnknownHostException {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
        throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RobotFilterRedisClient robotFilterRedisClient(){
        return new RobotFilterRedisClient();
    }

    @Bean
    public RobotFilterRedisListener robotFilterRedisListener(){
        return new RobotFilterRedisListener();
    }

}
