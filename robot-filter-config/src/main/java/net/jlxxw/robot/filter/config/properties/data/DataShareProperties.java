package net.jlxxw.robot.filter.config.properties.data;

import net.jlxxw.robot.filter.config.properties.data.netty.NettyProperties;
import net.jlxxw.robot.filter.config.properties.data.redis.RobotFilterRedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <b>no support refresh<b/>
 * @author chunyang.leng
 * @date 2022-11-03 5:58 PM
 */
@Configuration
@ConfigurationProperties("robot.filter.share")
public class DataShareProperties {

    /**
     * data share model
     * redis or netty sync
     * default is netty
     */
    private Model model = Model.NETTY;

    /**
     * netty properties
     */
    private NettyProperties netty = new NettyProperties();

    /**
     * redis properties
     */
    private RobotFilterRedisProperties redis = new RobotFilterRedisProperties();

    public NettyProperties getNetty() {
        return netty;
    }

    public void setNetty(NettyProperties netty) {
        this.netty = netty;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public RobotFilterRedisProperties getRedis() {
        return redis;
    }

    public void setRedis(RobotFilterRedisProperties redis) {
        this.redis = redis;
    }

    enum Model{
        /**
         * default,
         */
        NETTY,
        /**
         * redis
         */
        REDIS,

    }
}
