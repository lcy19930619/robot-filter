package net.jlxxw.robot.filter.core.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.jlxxw.robot.filter.config.properties.RobotFilterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:48 PM
 */
@Component
public class CacheService {
    private static final Cache CACHE = CacheBuilder.newBuilder().refreshAfterWrite(10, TimeUnit.SECONDS).build();

    @Autowired
    private RobotFilterProperties robotFilterProperties;

    public Set<String> getGlobalIpBlacklist() {
        try {
            return  (Set<String>) CACHE.get("globalBlackList", () -> robotFilterProperties.getGlobalIpBlacklist());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public Set<String> getGlobalIpWhiteList() {
        try {
            return  (Set<String>) CACHE.get("globalWhiteList", () -> robotFilterProperties.getGlobalIpWhitelist());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
