package net.jlxxw.robot.filter.config.pool;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author chunyang.leng
 * @date 2022-11-03 1:08 PM
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean("robotFilterThreadpool")
    public ThreadPoolTaskExecutor robotFilterThreadpool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu内核
        int cpu = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(cpu);
        //最大线程数
        executor.setMaxPoolSize(cpu * 2);
        //队列长度
        executor.setQueueCapacity(10000);
        //线程空闲时间
        executor.setKeepAliveSeconds(300);
        //线程前缀名称
        executor.setThreadNamePrefix("robot-filter-thread-pool-");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

}
